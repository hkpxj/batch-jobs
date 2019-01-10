package com.airwallex.batchjobs.manager.model

import com.airwallex.batchjobs.repository.model.JobConfigDO
import com.airwallex.batchjobs.repository.model.JobLogDO
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
data class JobConfigBO(
        var id: UUID? = null,
        var createTime: Date? = Date(),
        var lastUpdate: Date? = Date(),
        var version: Long? = 0,
        var jobName: String? = null,
        var jobGroup: String? = null,
        var jobClass: String? = null,
        var envTag: String? = null,
        var execDate: Date = Date(),
        var status: String? = null,
        var isLoading: String? = null,
        var jobCronExpress: String? = null,
        var jobTimeZone: String? = null,
        var retryTimes: Int? = 0,
        var lastExecTime: Date? = Date(),
        var nextExecTime: Date? = Date(),
        var jobLog: JobLogDO? = null,
        var isHandExec: String? = null
)

fun JobConfigDO.toBO(): JobConfigBO {

    return this.let {
        JobConfigBO().apply {
            id = UUID.fromString(it.id)
            createTime = it.createTime
            lastUpdate = it.lastUpdate
            version = it.version
            jobName = it.jobName
            jobGroup = it.jobGroup
            jobClass = it.jobClass
            envTag = it.envTag
            execDate = it.execDate
            status = it.status
            isLoading = it.isLoading
            jobCronExpress = it.jobCronExpress
            jobTimeZone = it.jobTimeZone
            retryTimes = it.retryTimes
            lastExecTime = it.lastExecTime
            nextExecTime = it.nextExecTime
        }
    }
}

fun JobConfigBO.toDO(): JobConfigDO {

    return this.let {
        JobConfigDO().apply {
            id = it.id.toString()
            createTime = it.createTime
            lastUpdate = it.lastUpdate
            version = it.version
            jobName = it.jobName ?: ""
            jobGroup = it.jobGroup ?: ""
            jobClass = it.jobClass ?: ""
            envTag = it.envTag ?: ""
            execDate = it.execDate
            status = it.status ?: ""
            isLoading = it.isLoading ?: ""
            jobCronExpress = it.jobCronExpress ?: ""
            jobTimeZone = it.jobTimeZone ?: ""
            retryTimes = it.retryTimes ?: 0
            lastExecTime = it.lastExecTime
            nextExecTime = it.nextExecTime
        }
    }
}