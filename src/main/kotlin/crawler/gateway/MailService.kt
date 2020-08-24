package crawler.gateway

import com.github.houbb.email.bs.EmailBs

object MailService {
    fun send(content: String, to: String) {
        EmailBs.auth("13093687239@163.com", "SJYQIGOBOCNLGRWG")
            .content("飞机票抢票提醒", content)
            .sendTo(to)
    }
}