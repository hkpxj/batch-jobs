package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.configure.DispatcherStartProperties
import com.airwallex.batchjobs.manager.CmdManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.slf4j.MDC
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Service
class DispatcherStart(var properties: DispatcherStartProperties,
                      private var dispatcherList: List<Dispatcher>,
                      private val cmdManager: CmdManager) {

    private val log: Logger = LogManager.getLogger()

    @PostConstruct
    fun init() {

        MDC.put("CORRELATION_ID", UUID.randomUUID().toString())
        log.info("------- [${properties.name}] is starting...... -------")
        if (!properties.runFlag) {
            log.info("this machine can not run Dispatcher!")
            return
        }

        reactiveCommandServerIP()
        val cmdDispatchExecutor = SimpleAsyncTaskExecutor()
        cmdDispatchExecutor.isDaemon = true
        cmdDispatchExecutor.concurrencyLimit = dispatcherList.size

        dispatcherList.forEach {
            cmdDispatchExecutor.setThreadNamePrefix(it.properties.name)
            cmdDispatchExecutor.execute(it)
        }
        log.info("------- [${properties.name}] is completed! -------")
    }

    private fun reactiveCommandServerIP() {
        try {
            val count = cmdManager.reactiveCommandServerIP()
            log.info("reactive $count cmd")
        } catch (e: Exception) {
            log.warn("reactive error！", e)
        }

    }
}