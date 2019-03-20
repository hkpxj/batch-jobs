package com.airwallex.batchjobs.task.job

import com.airwallex.batchjobs.manager.JobManager
import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.task.job.enums.JobIsLoadingEnum
import org.apache.logging.log4j.LogManager
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableScheduling
@ConditionalOnProperty("application.job")
class JobDispatcher {

    private val log = LogManager.getLogger()

    @Autowired
    lateinit var jobManager: JobManager

    @Autowired
    lateinit var quartzManager: QuartzManager


    @Scheduled(fixedRate = 1 * 60 * 1000)
    fun jobDispatcher() {

        MDC.put("CORRELATION_ID", UUID.randomUUID().toString())
        log.info("dispatcher job starting!")
        try {
            val jobConfigBOList = jobManager.queryJobConfig(JobConfigBO())
            jobConfigBOList.forEach {
                doDispatcher(it)
            }
        } catch (e: Exception) {
            log.error("do job dispatcher error: {}", e)
        } finally {
            MDC.clear()
        }
    }


    @Throws(Exception::class)
    private fun doDispatcher(jobConfigBO: JobConfigBO) {
        val jobIsLoadingEnum =
                JobIsLoadingEnum.explain(jobConfigBO.isLoading ?: "") ?: return

        try {
            when (jobIsLoadingEnum) {
                JobIsLoadingEnum.LOADING    // load job
                -> quartzManager.runJob(jobConfigBO)

                JobIsLoadingEnum.RUNNING    // run immediately
                -> {

                    jobConfigBO.isLoading = JobIsLoadingEnum.LOADING.name
                    jobManager.modifyJobConfig(jobConfigBO)
                    quartzManager.triggerJob(jobConfigBO)
                }

                JobIsLoadingEnum.PAUSED    // pause job
                -> quartzManager.pauseJob(jobConfigBO)

                else -> {
                }
            }
        } catch (e: Exception) {
            log.error("job information:{}，exception information：{}", jobConfigBO, e)
            throw e
        }

    }

}
