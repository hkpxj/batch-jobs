package com.airwallex.batchjobs

import com.airwallex.batchjobs.manager.model.JobConfigBO

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class ObjectConvertTest {
}

fun main(args: Array<String>) {

    JobConfigBO::class.java.declaredFields.map { println( it.name + "= \" \"") }
}