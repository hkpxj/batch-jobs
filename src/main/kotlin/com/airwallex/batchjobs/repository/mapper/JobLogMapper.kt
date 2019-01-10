package com.airwallex.batchjobs.repository.mapper

import com.airwallex.batchjobs.repository.model.JobConfigDO
import com.airwallex.batchjobs.repository.model.JobLogDO
import com.airwallex.batchjobs.repository.model.request.JobLogRequestDO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
@Mapper
@Repository
interface JobLogMapper {

    fun insert(jobLogDO: JobLogDO): Int

    fun selectByList(jobLogRequestDO: JobLogRequestDO): List<JobConfigDO>

    fun selectCount(jobLogRequestDO: JobLogRequestDO): Int

    fun update(jobLogDO: JobLogDO): Int
}
