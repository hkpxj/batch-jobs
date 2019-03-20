package com.airwallex.batchjobs.task.jobwork

import com.airwallex.batchjobs.manager.CmdManager
import com.airwallex.batchjobs.manager.JobManager
import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.manager.util.RedisLockUtil
import com.airwallex.batchjobs.repository.model.BizCmdDO
import com.airwallex.batchjobs.task.dispatcher.enums.CmdStatusEnum
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class TestJobWorker(override val jobManager: JobManager,
                    override val redisLockUtil: RedisLockUtil,
                    private var cmdManager: CmdManager) : AbstractJobWorker {

    override val log: Logger = LogManager.getLogger()

    override fun beforeWorker(jobConfigBO: JobConfigBO) {

        log.info("before worker do")
    }

    override fun doWorker(jobConfigBO: JobConfigBO) {

        log.info("worker do")

        for (i in 1..100) {
            BizCmdDO().apply {
                bizId = UUID.randomUUID().toString()
                bizType = "TEST_ONE"
                isDoing = "n"
                status = CmdStatusEnum.I.name
            }.let {
                cmdManager.insert(it)
            }
        }

        for (i in 1..10) {
            BizCmdDO().apply {
                bizId = UUID.randomUUID().toString()
                bizType = "TEST_TWO"
                isDoing = "n"
                status = CmdStatusEnum.I.name
            }.let {
                cmdManager.insert(it)
            }
        }

    }

    override fun afterWorker(jobConfigBO: JobConfigBO) {

        log.info("after worker do")

    }
}