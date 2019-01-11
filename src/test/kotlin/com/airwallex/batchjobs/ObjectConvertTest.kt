package com.airwallex.batchjobs

import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.task.dispatcher.Dispatcher

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class ObjectConvertTest {
}

fun main(args: Array<String>) {

    Dispatcher::class.java.declaredFields.map { println( it.name + "= dispatcherProperties." + it.name) }
}