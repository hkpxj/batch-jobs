package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.configure.DispatcherProperties
import com.airwallex.batchjobs.manager.CmdManager
import com.airwallex.batchjobs.manager.util.RedisLockUtil
import org.apache.logging.log4j.Logger
import org.redisson.api.RLock
import org.slf4j.MDC
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
interface Dispatcher : Runnable {

    val log: Logger

    var properties: DispatcherProperties

    var pool: ThreadPoolExecutor

    var queue: BlockingQueue<Runnable>

    var cmdManager: CmdManager

    var cmdHandler: CmdHandler

    var redisLockUtil: RedisLockUtil

    /**
     * 线程任务执行方法
     */
    override fun run() {

        MDC.put("CORRELATION_ID", UUID.randomUUID().toString())
        log.info("dispatcher[${properties.name}] start...")

        log.debug(properties.name + "-" + properties.coreSize + "-" + properties.maxSize + "-" + properties.queueSize
                + "-" + properties.hungrySize + "-" + properties.keepAliveTime + "-" + properties.noTaskSleepSeconds)

        while (true) {

            var lock: RLock? = null
            try {

                lock = redisLockUtil.getLock(properties.name)

                lock.lock(properties.lockTime, TimeUnit.SECONDS)

                if (queue.size >= properties.hungrySize) {
                    continue
                }


                val commands = cmdManager.lockAndListCommands(properties.name, properties.queueSize - queue.size)
                // 队列没有任务则进行休眠
                if (commands == null || commands.isEmpty()) {
                    try {
                        TimeUnit.SECONDS.sleep(properties.noTaskSleepSeconds)
                    } catch (e: InterruptedException) {
                        log.warn("dispatcher[${properties.name}] is interrupted!", e)
                    }

                    continue
                }

                for (command in commands) {
                    try {
                        pool.execute(HandlerWrapper(command, cmdHandler))
                    } catch (e: Exception) {
                        log.error("add pool error:{}", e)
                        cmdHandler.handlerException(command, e.toString())
                    }

                }
            } catch (e: Exception) {
                log.error("dispatcher[${properties.name}] error", e)
            } finally {
                lock?.unlock()
            }
        }
    }

    @PreDestroy
    fun destroy() {
        log.warn("dispatcher[${properties.name}] is stop!")
        pool.shutdown()
        Thread.interrupted()
    }

}