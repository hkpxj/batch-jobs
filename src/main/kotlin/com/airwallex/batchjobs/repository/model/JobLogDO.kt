package com.airwallex.batchjobs.repository.model

import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
data class JobLogDO(
        override var id: String = UUID.randomUUID().toString(),
        override var createTime: Date? = Date(),
        override var lastUpdate: Date? = Date(),
        override var version: Long? = 0,
        var jobId: String = "",
        var jobName: String = "",
        var jobGroup: String = "",
        var envTag: String = "",
        var execDate: Date? = Date(),
        var status: String? = null,
        var errorMsg: String? = null,
        var startTime: Date? = Date(),
        var execTimes: Int? = 0,
        var machineIp: String? = null
) : BaseDO