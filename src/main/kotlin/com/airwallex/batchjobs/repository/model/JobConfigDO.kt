package com.airwallex.batchjobs.repository.model

import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
data class JobConfigDO(
        override var id: String = UUID.randomUUID().toString(),
        override var createTime: Date? = Date(),
        override var lastUpdate: Date? = Date(),
        override var version: Long? = 0,
        var jobName: String = "",
        var jobGroup: String = "",
        var jobClass: String = "",
        var envTag: String = "",
        var execDate: Date = Date(),
        var status: String = "",
        var isLoading: String = "",
        var jobCronExpress: String = "",
        var jobTimeZone: String = "",
        var retryTimes: Int = 0,
        var lastExecTime: Date? = Date(),
        var nextExecTime: Date? = Date()
) : BaseDO