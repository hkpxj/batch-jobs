package com.airwallex.batchjobs.repository.model

import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
data class BizLockDO(
        override var id: String = UUID.randomUUID().toString(),
        override var createTime: Date? = Date(),
        override var lastUpdate: Date? = Date(),
        override var version: Long? = 0,
        var lockName: String = "",
        var envTag: String = "",
        var status: String = ""
) : BaseDO