package com.airwallex.batchjobs.manager.util

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.apache.logging.log4j.LogManager
import org.springframework.util.StringUtils
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

/**
 *
 * @author kun.hu
 * @createDate 2019-01-10
 */
object IPUtil {

    private val log = LogManager.getLogger()

    @Volatile
    private var IP_ADDRESS: String = ""

    /**
     * 获取本地IP
     *
     * @return IP地址
     */
    fun getLocalIP(): String {
        if (IP_ADDRESS != "") {
            return IP_ADDRESS
        }
        try {
            val allNetInterfaces = NetworkInterface.getNetworkInterfaces()
            var ip: InetAddress?
            while (allNetInterfaces.hasMoreElements()) {
                val netInterface = allNetInterfaces.nextElement() as NetworkInterface
                val addresses = netInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement() as InetAddress
                    if (ip != null && ip is Inet4Address) {
                        IP_ADDRESS = ip.hostAddress
                        return IP_ADDRESS
                    }
                }
            }
        } catch (e: SocketException) {
            log.error("get local IP, socket error:{}", e)
        } catch (e: Exception) {
            log.error("get local IP error:{}:{}", e)
        }

        return "127.0.0.1"
    }
}