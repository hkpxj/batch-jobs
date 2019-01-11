package com.airwallex.batchjobs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class BatchJobsApplication

fun main(args: Array<String>) {
	runApplication<BatchJobsApplication>(*args)
}

