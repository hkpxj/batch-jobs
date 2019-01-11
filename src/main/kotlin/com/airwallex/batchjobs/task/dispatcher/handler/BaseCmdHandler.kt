package com.airwallex.batchjobs.task.dispatcher.handler

import com.airwallex.batchjobs.manager.CmdManager
import com.airwallex.batchjobs.repository.model.BizCmdDO
import com.airwallex.batchjobs.task.dispatcher.CmdHandler
import com.airwallex.batchjobs.task.dispatcher.enums.CmdStatusEnum
import org.apache.commons.lang3.time.DateUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-11
 */
abstract class BaseCmdHandler: CmdHandler {

    val log: Logger = LogManager.getLogger()

    @Autowired
    private lateinit var cmdManager: CmdManager

    var retryInterval: Int = 5
    
    private val cmdStart = "cmd[{}] start, param[{}]"
    
    private val cmdEnd = "cmd[{}] ends in [{}]ms, param[{}]"
    
    private val cmdError = "cmd[{}] error, exception information[{}]"


    /**
     * handler执行方法
     *
     * @param command  基础命令对象
     * @throws Exception
     */
    @Throws(Exception::class)
    override fun execute(command: BizCmdDO) {

        MDC.put("CORRELATION_ID", UUID.randomUUID().toString())
        if (CmdStatusEnum.isCmdEnd(command.status)) {
            return
        }
        try {
            log.info(cmdStart, getHandlerName(), command.toString())
            val startTime = System.currentTimeMillis()
            doCmd(command)
            success(command)
            val endTime = System.currentTimeMillis()
            log.info(cmdEnd, getHandlerName(), endTime - startTime, command.toString())
        } catch (e: Exception) {
            
            log.error(cmdError, getHandlerName(), e)
            throw e
        }

    }

    @Transactional
    override fun handlerException(command: BizCmdDO, failReason: String) {

        command.failReason = failReason

        if (needRetry(command)) {
            retry(command)
        } else {
            fail(command)
            failedFinally(command)
        }
    }

    protected abstract fun getHandlerName(): String

    @Throws(Exception::class)
    protected abstract fun doCmd(command: BizCmdDO)

    protected abstract fun failedFinally(command: BizCmdDO)

    private fun success(command: BizCmdDO) {

        command.status = CmdStatusEnum.S.name
        command.isDoing = "n"
        cmdManager.update(command)
    }

    private fun needRetry(command: BizCmdDO): Boolean {

        if (command.enableEndDate != null &&
                DateUtils.addSeconds(command.nextExeTime, retryInterval).after(command.enableEndDate)) {
            return false
        }
        return !(command.maxRetryTimes > 0 && command.retryTimes >= command.maxRetryTimes)
    }

    private fun retry(command: BizCmdDO) {

        command.status = CmdStatusEnum.W.name
        command.isDoing = "n"
        command.retryTimes = command.retryTimes
        command.nextExeTime = DateUtils.addSeconds(command.nextExeTime, retryInterval)

        cmdManager.update(command)
    }

    private fun fail(command: BizCmdDO) {

        command.status = CmdStatusEnum.F.name
        command.isDoing = "n"
        cmdManager.update(command)
    }
}