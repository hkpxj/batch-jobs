package com.airwallex.batchjobs.task.job.enums

/**
 *
 * @author kun.hu
 * @createDate 2019-01-09
 */
enum class JobIsLoadingEnum {
    PAUSED, ERROR, LOADING, UN_LOADING, RUNNING;

    companion object {

        fun explain(isLoading: String): JobIsLoadingEnum? {

            return JobIsLoadingEnum.values().find { it.name == isLoading }
        }
    }
}