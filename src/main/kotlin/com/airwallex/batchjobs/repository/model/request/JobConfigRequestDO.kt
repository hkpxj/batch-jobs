package com.airwallex.batchjobs.repository.model.request

import com.airwallex.batchjobs.repository.model.BaseDO
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
data class JobConfigRequestDO(
        var id: String? = null,
        var jobName: String? = null,
        var jobGroup: String? = null,
        var jobClass: String? = null,
        var envTag: String? = null,
        var status: String? = null,
        var isLoading: String? = null,
        var jobCronExpress: String? = null,
        var jobTimeZone: String? = null,
        var retryTimes: Int? = 0,
        var lastExecTime: Date? = Date(),
        var nextExecTime: Date? = Date()
)