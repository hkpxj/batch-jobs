package com.airwallex.batchjobs.repository.mapper

import com.airwallex.batchjobs.repository.model.JobConfigDO
import com.airwallex.batchjobs.repository.model.request.JobConfigRequestDO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
@Mapper
@Repository
interface JobConfigMapper {

    fun insert(jobConfigDO: JobConfigDO): Int

    fun selectForUpdateById(id: String): JobConfigDO?

    fun selectById(id: String): JobConfigDO?

    fun selectByList(jobConfigRequestDO: JobConfigRequestDO): List<JobConfigDO>

    fun updateById(jobConfigDO: JobConfigDO): Int
}
