package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.repository.model.BizCmdDO

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
interface CmdHandler {

    @Throws(Exception::class)
    fun execute(command: BizCmdDO)

    fun handlerException(command: BizCmdDO, failReason: String)
}