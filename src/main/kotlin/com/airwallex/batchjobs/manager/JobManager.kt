package com.airwallex.batchjobs.manager

import com.airwallex.batchjobs.manager.enums.JobLogStatusEnum
import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.manager.model.toBO
import com.airwallex.batchjobs.manager.model.toDO
import com.airwallex.batchjobs.manager.util.IPUtil
import com.airwallex.batchjobs.repository.mapper.JobConfigMapper
import com.airwallex.batchjobs.repository.mapper.JobLogMapper
import com.airwallex.batchjobs.repository.model.JobConfigDO
import com.airwallex.batchjobs.repository.model.JobLogDO
import com.airwallex.batchjobs.repository.model.request.JobConfigRequestDO
import com.airwallex.batchjobs.repository.model.request.JobLogRequestDO
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Component
class JobManager {

    @Autowired
    lateinit var jobConfigMapper: JobConfigMapper

    @Autowired
    lateinit var jobLogMapper: JobLogMapper

    private val log = LogManager.getLogger()

    @Transactional
    fun lockJobAddLog(jobConfigBO: JobConfigBO): Boolean {

        val jobConfigDO = jobConfigMapper.selectForUpdateById(jobConfigBO.id.toString())
        jobConfigDO ?: return true

        val jobLogDO = getJobLog(jobConfigBO)
        val execTimes = jobLogMapper.selectCount(getJobLogRequest(jobLogDO))
        if (execTimes > 0) {
            log.error("job is running: {}", jobConfigBO)
            return true
        }
        jobLogMapper.insert(jobLogDO)

        jobConfigBO.jobLog = jobLogDO
        jobConfigBO.retryTimes = jobConfigDO.retryTimes

        return false
    }

    fun queryJobConfig(jobConfigBO: JobConfigBO): List<JobConfigBO> {

        val jobConfigRequestDO = getJobConfigRequestDO(jobConfigBO)
        val accJobConfigDOs = jobConfigMapper.selectByList(jobConfigRequestDO)
        return accJobConfigDOs.map {
            it.toBO()
        }

    }

    @Transactional
    fun modifyJobConfig(jobConfigBO: JobConfigBO) {

        val jobConfigDO = JobConfigDO().apply {
            id = jobConfigBO.id.toString()
            status = jobConfigBO.status ?: ""
            isLoading = jobConfigBO.isLoading ?: ""
            lastExecTime = jobConfigBO.lastExecTime
            nextExecTime = jobConfigBO.nextExecTime
        }
        val result = jobConfigMapper.updateById(jobConfigDO)
        if (result < 1) {
            throw Exception("update job config error : ${jobConfigDO.id}")
        }

        val jobLog = jobConfigBO.jobLog
        if (null != jobLog) {
            jobLogMapper.update(jobLog)
        }
    }

    fun addJobConfig(jobConfigBO: JobConfigBO) {

        jobConfigMapper.insert(jobConfigBO.toDO())

    }

    fun queryByJobId(id: String): JobConfigBO? {

        return jobConfigMapper.selectById(id)?.toBO()
    }

    fun queryCount(jobLogRequestDO: JobLogRequestDO): Int {

        return jobLogMapper.selectCount(jobLogRequestDO)
    }


    private fun getJobLog(jobConfigBO: JobConfigBO): JobLogDO {

        return JobLogDO().apply {

            jobName = jobConfigBO.jobName!!
            jobGroup = jobConfigBO.jobGroup!!
            envTag = jobConfigBO.envTag!!
            jobId = jobConfigBO.id.toString()
            execDate = jobConfigBO.execDate
            execTimes = 1
            status = JobLogStatusEnum.RUNNING.name
            machineIp = IPUtil.getLocalIP()

        }
    }

    private fun getJobLogRequest(jobLogDO: JobLogDO): JobLogRequestDO {

        return JobLogRequestDO().apply {
            jobId = jobLogDO.jobId
            jobName = jobLogDO.jobName
            jobGroup = jobLogDO.jobGroup
            envTag = jobLogDO.envTag
            execDate = jobLogDO.execDate
            status = jobLogDO.status
            errorMsg = jobLogDO.errorMsg
            startTime = jobLogDO.startTime
            execTimes = jobLogDO.execTimes
            machineIp = jobLogDO.machineIp
        }
    }

    private fun getJobConfigRequestDO(jobConfigBO: JobConfigBO): JobConfigRequestDO {

        return JobConfigRequestDO().apply {

            id = jobConfigBO.id?.toString()
            jobName = jobConfigBO.jobName
            jobGroup = jobConfigBO.jobGroup
            jobClass = jobConfigBO.jobClass
            envTag = jobConfigBO.envTag
            status = jobConfigBO.status
            isLoading = jobConfigBO.isLoading
            jobCronExpress = jobConfigBO.jobCronExpress
            jobTimeZone = jobConfigBO.jobTimeZone
            retryTimes = jobConfigBO.retryTimes
            lastExecTime = jobConfigBO.lastExecTime
            nextExecTime = jobConfigBO.nextExecTime
        }
    }
}