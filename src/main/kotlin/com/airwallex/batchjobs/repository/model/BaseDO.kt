package com.airwallex.batchjobs.repository.model

import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2018/10/17
 * @projectName ledger
 */
interface BaseDO {
    var id: String
    var createTime: Date?
    var lastUpdate: Date?
    var version: Long?
}