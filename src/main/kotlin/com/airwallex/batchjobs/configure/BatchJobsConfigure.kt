package com.airwallex.batchjobs.configure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Configuration
@EnableConfigurationProperties
class BatchJobsConfigure {

    @Bean
    @ConfigurationProperties(prefix = "application.batch")
    fun batchJobsProperties(): BatchJobsProperties = BatchJobsProperties()

    @Bean
    @ConfigurationProperties(prefix = "dispatcher.starter")
    fun dispatcherStartProperties(): DispatcherStartProperties = DispatcherStartProperties()

    @Bean(name = ["dispatcherOneProperties"])
    @ConfigurationProperties(prefix = "dispatcher.dispatcher-one")
    fun myDispatcherOne(): DispatcherProperties = DispatcherProperties()

    @Bean(name = ["dispatcherTwoProperties"])
    @ConfigurationProperties(prefix = "dispatcher.dispatcher-two")
    fun myDispatcherTwo(): DispatcherProperties = DispatcherProperties()
}