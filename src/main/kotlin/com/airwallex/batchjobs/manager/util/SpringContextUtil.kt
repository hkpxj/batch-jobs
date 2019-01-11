package com.airwallex.batchjobs.manager.util

import org.springframework.context.ApplicationContext
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContextAware

/**
 *
 * @author kun.hu
 * @createDate 2019-01-11
 */
object SpringContextUtil: ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringContextUtil.applicationContext = applicationContext
    }

    @Throws(BeansException::class)
    fun getBean(name: String): Any {
        return applicationContext.getBean(name)
    }
}