package com.airwallex.batchjobs.task.jobwork

import com.airwallex.batchjobs.manager.model.JobConfigBO
import org.apache.logging.log4j.LogManager

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
class TestJobWorker: AbstractJobWorker() {

    private val log = LogManager.getLogger()

    override fun beforeWorker(jobConfigBO: JobConfigBO) {

        log.info("before worker do")
    }

    override fun doWorker(jobConfigBO: JobConfigBO) {

        log.info("worker do")

    }

    override fun afterWorker(jobConfigBO: JobConfigBO) {

        log.info("after worker do")

    }
}