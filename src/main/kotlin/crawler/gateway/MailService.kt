package crawler.gateway

import com.github.houbb.email.bs.EmailBs
import crawler.domain.FlightConfig

object MailService {
    val config = FlightConfig.read()
    fun send(content: String, to: String) {
        EmailBs.auth(config.sender, config.code)
            .content("飞机票抢票提醒", content)
            .sendTo(to)
    }
}