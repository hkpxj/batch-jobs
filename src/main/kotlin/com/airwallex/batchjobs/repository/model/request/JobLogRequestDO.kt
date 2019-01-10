package com.airwallex.batchjobs.repository.model.request

import com.airwallex.batchjobs.repository.model.JobLogDO
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
data class JobLogRequestDO(
        var id: String? = null,
        var jobId: String? = null,
        var jobName: String? = null,
        var jobGroup: String? = null,
        var envTag: String? = null,
        var execDate: Date? = Date(),
        var status: String? = null,
        var errorMsg: String? = null,
        var startTime: Date? = Date(),
        var execTimes: Int? = 0,
        var machineIp: String? = null
)

fun JobLogDO.toJobLogRequestDO(): JobLogRequestDO{

    return this.let {
        JobLogRequestDO().apply {
            id = it.id
            jobId = it.jobId
            jobName = it.jobName
            jobGroup = it.jobGroup
            envTag = it.envTag
            execDate = it.execDate
            status = it.status
            errorMsg = it.errorMsg
            startTime = it.startTime
            execTimes = it.execTimes
            machineIp = it.machineIp
        }
    }
}