package com.airwallex.batchjobs.manager

import com.airwallex.batchjobs.configure.BatchJobsProperties
import com.airwallex.batchjobs.manager.util.IPUtil
import com.airwallex.batchjobs.repository.mapper.BizCmdMapper
import com.airwallex.batchjobs.repository.mapper.BizLockMapper
import com.airwallex.batchjobs.repository.model.BizCmdDO
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Component
class CmdManager {

    private val log: Logger = LogManager.getLogger()

    private val status = "y"

    private val bizLockStatus = "ACTIVE"

    @Autowired
    private lateinit var bizLockMapper: BizLockMapper

    @Autowired
    private lateinit var bizCmdMapper: BizCmdMapper
    
    @Autowired
    private lateinit var batchJobsProperties: BatchJobsProperties

    @Transactional
    @Throws(Exception::class)
    fun lockAndListCommands(bizType: String, cmdNum: Int): List<BizCmdDO>? {

        val bizLock = bizLockMapper.lock(bizType, batchJobsProperties.envTag, bizLockStatus)

        return if (null == bizLock) {
            null
        } else queryToDoCmdList(bizType, cmdNum)

    }

    @Throws(Exception::class)
    fun queryToDoCmdList(bizType: String, cmdNum: Int): List<BizCmdDO>? {
        val toDoList = ArrayList<BizCmdDO>()
        try {
            //查询带有环境标签的命令

            val cmdList: List<BizCmdDO> = bizCmdMapper.selectToDoCmdList(bizType, cmdNum, batchJobsProperties.envTag)
            cmdList.forEach {

                if(status != it.isDoing){
                    it.isDoing = status
                    it.serverIP = IPUtil.getLocalIP()
                    bizCmdMapper.update(it)
                    toDoList.add(it)
                }

            }
            return toDoList
        } catch (e: Exception) {
            log.error("query cmdList error", e)
        }

        return null
    }

    @Transactional
    @Throws(Exception::class)
    fun update(cmdObject: BizCmdDO) {
        try {
            bizCmdMapper.update(cmdObject)
        } catch (e: Exception) {
            log.error("update cmd error", e)
            throw Exception("update cmd error", e)
        }

    }

    @Throws(Exception::class)
    fun insert(cmdObject: BizCmdDO) {

        log.debug("call BaseCmdManager insert, cmdObject:{}", cmdObject)
        //add envTag
        cmdObject.envTag = batchJobsProperties.envTag
        bizCmdMapper.insert(cmdObject)
    }

    fun reactiveCommandServerIP(): Int {

        return bizCmdMapper.reactiveCommandsServerIP(IPUtil.getLocalIP())
    }

    fun delayEndDate(): Int {

        return bizCmdMapper.delayEndDate()
    }
}