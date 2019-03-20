package com.airwallex.batchjobs.task.dispatcher

import com.airwallex.batchjobs.repository.model.BizCmdDO

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class HandlerWrapper(private var command: BizCmdDO, private var cmdHandler: CmdHandler) : Runnable {

    override fun run() {
        try {
            cmdHandler.execute(command)
        } catch (t: Exception) {
            cmdHandler.handlerException(command, t.toString())
        }
    }
}