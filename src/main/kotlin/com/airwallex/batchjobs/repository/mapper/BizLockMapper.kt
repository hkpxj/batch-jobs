package com.airwallex.batchjobs.repository.mapper

import com.airwallex.batchjobs.repository.model.BizLockDO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Mapper
@Repository
interface BizLockMapper {

    fun lock(lockName: String, envTag: String, status: String): BizLockDO?
}