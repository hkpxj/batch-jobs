package com.airwallex.batchjobs.repository.model

import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
data class BizCmdDO(
        override var id: String = UUID.randomUUID().toString(),
        override var createTime: Date? = Date(),
        override var lastUpdate: Date? = Date(),
        override var version: Long? = 0,
        var bizId: String = UUID.randomUUID().toString(),
        var bizType: String = "",
        var serverIP: String? = null,
        var failReason: String? = null,
        var envTag: String = "",
        var isDoing: String = "",
        var status: String = "",
        var exec_date: Date? = Date(),
        var retryTimes: Int = 0,
        var maxRetryTimes: Int = 0,
        var nextExeTime: Date? = null,
        var enableStartDate: Date? = null,
        var enableEndDate: Date? = null
) : BaseDO