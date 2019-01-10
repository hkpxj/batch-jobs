package com.airwallex.batchjobs

import com.airwallex.batchjobs.manager.JobManager
import com.airwallex.batchjobs.manager.enums.JobConfigStatusEnum
import com.airwallex.batchjobs.manager.model.JobConfigBO
import com.airwallex.batchjobs.task.job.enums.JobIsLoadingEnum
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class JobManagerTest {

    @Autowired
    lateinit var jobManager: JobManager

    @Test
    fun addTest(){

        val jobConfigBO = JobConfigBO().apply {

            id= UUID.randomUUID()
            jobName= "MyTestJob"
            jobGroup= "Test"
            jobClass= "com.airwallex.batchjobs.task.jobwork.TestJobWorker"
            envTag= "dev"
            status= JobConfigStatusEnum.NORMAL.name
            isLoading= JobIsLoadingEnum.LOADING.name
            jobCronExpress= "0 0/1 * * * ? *"
            jobTimeZone= "Asia/Hong_Kong"
            retryTimes= 10
        }

        jobManager.addJobConfig(jobConfigBO)
    }

    @Test
    fun modifyTest(){

        val jobConfigBO = jobManager.queryJobConfig(JobConfigBO())
                .find { it.id?.toString() == "3da9186b-a4b6-426e-8d09-26cea9a7b659" } ?: return

        jobConfigBO.apply {
            jobCronExpress = "0/20 * * * * ? *"
            retryTimes = -1
        }

        jobManager.modifyJobConfig(jobConfigBO)

    }
}