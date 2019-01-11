package com.airwallex.batchjobs.repository.mapper

import com.airwallex.batchjobs.repository.model.BizCmdDO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository
import java.util.*

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
@Mapper
@Repository
interface BizCmdMapper {

    fun insert(bizCmdDO: BizCmdDO): Int

    fun update(bizCmdDO: BizCmdDO): Int

    fun selectToDoCmdList(bizType: String, cmdNum: Int, envTag: String): List<BizCmdDO>

    fun selectByBizIdAndType(bizId: Long, bizType: String): List<BizCmdDO>

    fun selectByBizId(bizId: String): BizCmdDO

    fun reactiveCommandsServerIP(serverIP: String): Int

    fun delayEndDate(): Int

    fun reactiveCommand(bizId: String, accDate: Date): Int
}