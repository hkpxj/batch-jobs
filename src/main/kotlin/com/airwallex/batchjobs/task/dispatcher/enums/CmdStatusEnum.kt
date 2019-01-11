package com.airwallex.batchjobs.task.dispatcher.enums

/**
 *
 * @author kun.hu
 * @createDate 2019-01-11
 */
enum class CmdStatusEnum {
    S, F, I, W, P;

    companion object{

        fun isCmdEnd(status: String): Boolean {

            val enums = getCmdStatusEnum(status)

            return when (enums) {
                I, W -> false
                S, P, F -> true
            }
        }

        fun isCmdSuccess(status: String): Boolean {

            val enums = getCmdStatusEnum(status)
            return enums === CmdStatusEnum.S
        }

        fun isCmdFailure(status: String): Boolean {

            val enums = getCmdStatusEnum(status)
            return enums === CmdStatusEnum.F
        }

        fun getCmdStatusEnum(status: String): CmdStatusEnum {

            return CmdStatusEnum.values().find { it.name == status }
                    ?: throw Exception("value not supported: $status")
        }
    }
}