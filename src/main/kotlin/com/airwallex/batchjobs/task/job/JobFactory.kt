package com.airwallex.batchjobs.task.job

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.scheduling.quartz.AdaptableJobFactory

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
class JobFactory: AdaptableJobFactory() {

    /** The one of Spring BeanFactory  */
    @Autowired
    lateinit var capableBeanFactory: AutowireCapableBeanFactory

    /**
     * 1、JOB 注入
     *
     * @param bundle        TriggerFiredBundle
     * @return Object 		Autowire Bean
     * @throws Exception    异常
     */
    @Throws(Exception::class)
    override fun createJobInstance(bundle: TriggerFiredBundle): Any {
        val jobInstance = super.createJobInstance(bundle)
        capableBeanFactory.autowireBean(jobInstance)
        return jobInstance
    }
}