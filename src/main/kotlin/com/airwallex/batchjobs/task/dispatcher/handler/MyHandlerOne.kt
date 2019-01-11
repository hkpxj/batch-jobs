package com.airwallex.batchjobs.task.dispatcher.handler

import com.airwallex.batchjobs.repository.model.BizCmdDO
import com.airwallex.batchjobs.task.dispatcher.CmdHandler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Component("myHandlerOne")
class MyHandlerOne: BaseCmdHandler() {

    override fun getHandlerName(): String {

        return "myHandlerOne"
    }

    override fun doCmd(command: BizCmdDO) {

        log.info("doCmd: {}", command)
    }

    override fun failedFinally(command: BizCmdDO) {

        log.info("failedFinally: {}", command)

    }

}