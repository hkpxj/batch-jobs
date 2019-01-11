package com.airwallex.batchjobs.manager.util

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author kun.hu
 * @createDate 2019-01-11
 */
@Service
class RedisLockUtil {

    @Autowired
    lateinit var redissonClient: RedissonClient

    fun getLock(key: String): RLock {
        return redissonClient.getLock(key)
    }
}