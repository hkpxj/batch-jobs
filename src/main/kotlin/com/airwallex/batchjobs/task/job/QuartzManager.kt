package com.airwallex.batchjobs.task.job

import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.task.job.constant.JobConstant
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component
import java.text.ParseException
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
@Component
class QuartzManager {

    private val log: Logger = LogManager.getLogger()

    /** Spring Scheduler  */
    @Autowired
    lateinit var schedulerFactoryBean: SchedulerFactoryBean

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun runJob(scheduleJob: JobConfigBO) {

        log.debug("call runJob ${scheduleJob.jobName}")
        val triggerKey = TriggerKey.triggerKey(scheduleJob.jobName, scheduleJob.jobGroup)
        var trigger = schedulerFactoryBean.scheduler.getTrigger(triggerKey)
        if (null == trigger) {
            addJob(scheduleJob)
        } else {
            trigger as CronTrigger
            if (trigger.cronExpression != scheduleJob.jobCronExpress || trigger.timeZone != TimeZone.getTimeZone(scheduleJob.jobTimeZone)) {
                log.debug("call rescheduleJob!")
                val scheduleBuilder = createCronScheduleBuilder(scheduleJob)

                trigger = trigger.triggerBuilder.withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build()

                val jobDetail = schedulerFactoryBean.scheduler.getJobDetail(trigger.jobKey)
                jobDetail.jobDataMap[JobConstant.JOB_DATA_MAP] = scheduleJob
                log.info("first fire time for ${scheduleJob.jobName}: {}", schedulerFactoryBean.scheduler.rescheduleJob(triggerKey, trigger))
            }
        }
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun addJob(scheduleJob: JobConfigBO) {

        log.debug("call addJob ${scheduleJob.jobName}!")
        val beanClass = Class.forName(scheduleJob.jobClass) as Class<Job>
        val jobDetail = JobBuilder.newJob(beanClass).withIdentity(scheduleJob.jobName, scheduleJob.jobGroup).build()
        jobDetail.jobDataMap[JobConstant.JOB_DATA_MAP] = scheduleJob
        val scheduleBuilder = createCronScheduleBuilder(scheduleJob)

        val trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.jobName, scheduleJob.jobGroup)
                .withSchedule(scheduleBuilder).build()
        log.info("first fire time for ${scheduleJob.jobName}: {}", schedulerFactoryBean.scheduler.scheduleJob(jobDetail, trigger))
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun pauseJob(scheduleJob: JobConfigBO) {

        log.debug("call pauseJob ${scheduleJob.jobName}!")
        if (!isExistsJob(scheduleJob)) {
            addJob(scheduleJob)
        }
        if (scheduleJob(scheduleJob) == scheduleJob.isLoading) {
            return
        }
        val jobKey = JobKey.jobKey(scheduleJob.jobName, scheduleJob.jobGroup)
        schedulerFactoryBean.scheduler.pauseJob(jobKey)
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun resumeJob(scheduleJob: JobConfigBO) {

        log.debug("call resumeJob ${scheduleJob.jobName}!")
        if (!isExistsJob(scheduleJob)) {
            addJob(scheduleJob)
        }
        val jobKey = JobKey.jobKey(scheduleJob.jobName, scheduleJob.jobGroup)
        schedulerFactoryBean.scheduler.resumeJob(jobKey)
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun removeJob(scheduleJob: JobConfigBO): Boolean {

        log.debug("call removeJob ${scheduleJob.jobName}!")
        if (!isExistsJob(scheduleJob)) {
            return true
        }
        val jobKey = JobKey.jobKey(scheduleJob.jobName, scheduleJob.jobGroup)
        return schedulerFactoryBean.scheduler.deleteJob(jobKey)
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun triggerJob(scheduleJob: JobConfigBO) {

        log.debug("call triggerJob ${scheduleJob.jobName}!")
        if (!isExistsJob(scheduleJob)) {
            addJob(scheduleJob)
        }

        val jobKey = JobKey(scheduleJob.jobName, scheduleJob.jobGroup)
        val jobDetail = schedulerFactoryBean.scheduler.getJobDetail(jobKey)
        val jobDataMap = jobDetail.jobDataMap
        jobDataMap[JobConstant.JOB_DATA_MAP] = scheduleJob

        schedulerFactoryBean.scheduler.triggerJob(jobKey, jobDataMap)

    }

    @Throws(SchedulerException::class)
    fun scheduleJob(scheduleJob: JobConfigBO): String? {

        log.debug("call scheduleJob ${scheduleJob.jobName}!")
        var status: String? = null
        val triggerKey = TriggerKey.triggerKey(scheduleJob.jobName, scheduleJob.jobGroup)
        val trigger = schedulerFactoryBean.scheduler.getTrigger(triggerKey)
        if (null != trigger) {
            val triggerState = schedulerFactoryBean.scheduler.getTriggerState(trigger.key)
            status = triggerState.name
        }
        return status
    }

    @Throws(SchedulerException::class)
    private fun isExistsJob(scheduleJob: JobConfigBO): Boolean {

        val triggerKey = TriggerKey.triggerKey(scheduleJob.jobName, scheduleJob.jobGroup)
        schedulerFactoryBean.scheduler.getTrigger(triggerKey)
                ?: return false
        return true
    }

    @Throws(SchedulerException::class, ParseException::class, ClassNotFoundException::class)
    fun reloadJob(scheduleJob: JobConfigBO) {

        log.debug("call reloadJob ${scheduleJob.jobName}!")
        if (this.removeJob(scheduleJob)) {
            addJob(scheduleJob)
        }
    }

    private fun createCronScheduleBuilder(scheduleJob: JobConfigBO): CronScheduleBuilder {

        return CronScheduleBuilder
                .cronSchedule(scheduleJob.jobCronExpress)
                .inTimeZone(TimeZone.getTimeZone(scheduleJob.jobTimeZone))
    }
}