package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.configure.DispatcherProperties
import com.airwallex.batchjobs.manager.CmdManager
import com.airwallex.batchjobs.manager.util.RedisLockUtil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Component
class MyDispatcherOne(@Qualifier("dispatcherOneProperties") override var properties: DispatcherProperties,
                      override var cmdManager: CmdManager,
                      override var redisLockUtil: RedisLockUtil,
                      @Qualifier("myHandlerOne") override var cmdHandler: CmdHandler
) : Dispatcher {
    override val log: Logger = LogManager.getLogger()

    override var queue: BlockingQueue<Runnable> = ArrayBlockingQueue<Runnable>(properties.queueSize)
    override var pool: ThreadPoolExecutor = ThreadPoolExecutor(properties.coreSize,
            properties.maxSize, properties.keepAliveTime, TimeUnit.SECONDS, queue)
}