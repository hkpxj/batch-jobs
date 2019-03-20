package com.airwallex.batchjobs.task.dispatcher.handler

import com.airwallex.batchjobs.repository.model.BizCmdDO
import org.springframework.stereotype.Component

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Component("myHandlerTwo")
class MyHandlerTwo : BaseCmdHandler() {

    override fun getHandlerName(): String {

        return "myHandlerTwo"
    }

    override fun doCmd(command: BizCmdDO) {

        log.info("doCmd: {}", command)
    }

    override fun failedFinally(command: BizCmdDO) {

        log.info("failedFinally: {}", command)

    }
}