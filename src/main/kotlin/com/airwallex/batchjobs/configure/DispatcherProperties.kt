package com.airwallex.batchjobs.configure

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class DispatcherProperties {

    var name: String = ""

    var coreSize: Int = 0

    var maxSize: Int = 10

    var queueSize: Int = 100

    var keepAliveTime: Long = 24 * 60 * 60

    var noTaskSleepSeconds: Long = 5

    var hungrySize: Int = (queueSize * 0.8).toInt()
}
