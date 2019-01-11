package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.repository.model.BizCmdDO

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
interface CmdHandler {

    /**
     * 处理具体命令
     *
     * @param command
     * @throws Exception
     */
    @Throws(Exception::class)
    fun execute(command: BizCmdDO)

    /**
     * 任务执行失败的异常处理
     *
     * @param command
     * @param failReason
     */
    fun handlerException(command: BizCmdDO, failReason: String)
}