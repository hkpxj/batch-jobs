package com.airwallex.batchjobs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchJobsApplication

fun main(args: Array<String>) {
	runApplication<BatchJobsApplication>(*args)
}

