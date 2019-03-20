package com.airwallex.batchjobs.configure

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Configuration
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

    @Bean
    @ConfigurationProperties(prefix = "redisson")
    fun redissonProperties(): RedissonProperties = RedissonProperties()

    @Bean
    fun redissonClient(redissonProperties: RedissonProperties): RedissonClient {

        val config = Config()
        val serverConfig = config.useSingleServer()
        serverConfig.setAddress(redissonProperties.address)
        serverConfig.database = redissonProperties.database
        redissonProperties.password?.let {
            serverConfig.setPassword(it)
        }
        return Redisson.create(config)
    }
}