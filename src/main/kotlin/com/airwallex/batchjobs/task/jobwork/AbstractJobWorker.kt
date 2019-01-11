package com.airwallex.batchjobs.task.jobwork

import com.airwallex.batchjobs.manager.JobManager
import com.airwallex.batchjobs.manager.enums.FlagEnum
import com.airwallex.batchjobs.manager.enums.JobConfigStatusEnum
import com.airwallex.batchjobs.manager.enums.JobLogStatusEnum
import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.repository.model.request.JobLogRequestDO
import com.airwallex.batchjobs.repository.model.request.toJobLogRequestDO
import com.airwallex.batchjobs.task.job.constant.JobConstant
import com.airwallex.batchjobs.task.job.enums.JobIsLoadingEnum
import org.apache.logging.log4j.Logger
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.MDC
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
interface AbstractJobWorker: Job {

    val log: Logger

    val jobManager: JobManager

    override fun execute(context: JobExecutionContext) {

        MDC.put("CORRELATION_ID", UUID.randomUUID().toString())
        val jobConfigBO = this.getJobDetailBO(context)

        log.warn("job is running: {}", jobConfigBO)

        if(FlagEnum.TRUE.name == jobConfigBO.isHandExec){

            jobConfigBO.isHandExec = FlagEnum.FALSE.name
            setJobDetailBO(context, jobConfigBO)
        } else {

            jobConfigBO.execDate = Date()
        }

        try {
            if (jobManager.lockJobAddLog(jobConfigBO)) {
                log.warn("lock job error:{} ", jobConfigBO)
                return
            }
        } catch (e: Exception) {
            log.error("lock JobConfig Error, job name: ${jobConfigBO.jobName}")
            throw e
        }

        if (isMaxRetry(jobConfigBO)) {
            jobConfigBO.jobLog?.errorMsg = "batch job out of retry times!"
            success(context, jobConfigBO)
            return
        }

        try {

            try {
                beforeWorker(jobConfigBO)
            } catch (e: Exception) {
                log.error("run job beforeWorker error, RESULT: {} ", e)
                jobConfigBO.jobLog?.errorMsg = e.toString()
                fail(context, jobConfigBO)
                return
            }

            doWorker(jobConfigBO)
            afterWorker(jobConfigBO)

            jobConfigBO.jobLog?.errorMsg = "SUCCESS!"
            success(context, jobConfigBO)

            log.warn("run job success: ${jobConfigBO.jobName}")
        } catch (e: Exception) {
            log.error("run job error: ${jobConfigBO.jobName}  {}", e)
            jobConfigBO.jobLog?.errorMsg = e.toString()
            fail(context, jobConfigBO)
        } finally {
            MDC.clear()
        }
    }

    @Throws(Exception::class)
    fun beforeWorker(jobConfigBO: JobConfigBO)

    @Throws(Exception::class)
    fun doWorker(jobConfigBO: JobConfigBO)

    @Throws(Exception::class)
    fun afterWorker(jobConfigBO: JobConfigBO)

    private fun isMaxRetry(jobConfigBO: JobConfigBO): Boolean {

        log.debug("call isRunning, PARAMETER:{}", jobConfigBO)
        val jobLog = jobConfigBO.jobLog

        val jobLogRequestDO = jobLog?.toJobLogRequestDO() ?: JobLogRequestDO()

        jobLogRequestDO.status = null
        val execTimes = jobManager.queryCount(jobLogRequestDO)

        val maxRetryTimes = jobConfigBO.retryTimes ?: return false

        if (maxRetryTimes in 1..(execTimes - 1)) {
            log.error("batch job out of retry times:{}", jobConfigBO)
            return true
        }
        return false
    }

    private fun success(context: JobExecutionContext, jobConfigBO: JobConfigBO) {

        jobConfigBO.apply {
            status = JobConfigStatusEnum.NORMAL.name
            isLoading = JobIsLoadingEnum.LOADING.name
            nextExecTime = context.nextFireTime
            lastExecTime = context.previousFireTime

            jobLog?.status = JobLogStatusEnum.FINISHED.name
            jobLog?.execDate = jobConfigBO.execDate
        }

        jobManager.modifyJobConfig(jobConfigBO)
    }

    private fun fail(context: JobExecutionContext, jobConfigBO: JobConfigBO) {


        jobConfigBO.apply {
            isLoading = JobIsLoadingEnum.RUNNING.name
            nextExecTime = context.nextFireTime
            lastExecTime = context.previousFireTime

            jobLog?.status = JobLogStatusEnum.EXCEPTION.name
            jobLog?.execDate = jobConfigBO.execDate
        }

        jobManager.modifyJobConfig(jobConfigBO)

    }

    private fun getJobDetailBO(context: JobExecutionContext): JobConfigBO {

        log.debug("JobExecutionContext: {}", context)
        val jobDataMap = context.mergedJobDataMap
        val jobConfigBO = jobDataMap[JobConstant.JOB_DATA_MAP] as JobConfigBO
        log.debug("JobConfigBO：{}", jobConfigBO)
        return jobConfigBO
    }

    private fun setJobDetailBO(context: JobExecutionContext, jobConfigBO: JobConfigBO) {

        val jobDataMap = context.jobDetail.jobDataMap
        jobDataMap[JobConstant.JOB_DATA_MAP] = jobConfigBO
    }
}