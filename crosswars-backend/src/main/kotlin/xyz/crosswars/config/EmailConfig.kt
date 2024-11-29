package xyz.crosswars.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
@ConfigurationProperties(prefix = "email")
class EmailConfig(
    var enabled: Boolean = false,
    var username: String = "",
    var password: String = "",
    var host: String = "smtp.gmail.com",
    var port: Int = 587,
    var protocol: String = "smtp",
    var smtpAuthEnabled: Boolean = true,
    var smtpStarttlesEnabled: Boolean = true,
    var debugEnabled: Boolean = false
) {

    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = this.host
        mailSender.port = this.port
        mailSender.username = this.username
        mailSender.password = this.password

        val properties = mailSender.javaMailProperties
        properties.put("mail.transport.protocol", this.protocol)
        properties.put("mail.smtp.auth", this.smtpAuthEnabled.toString())
        properties.put("mail.smtp.starttls.enable", this.smtpStarttlesEnabled.toString())
        properties.put("mail.debug", this.debugEnabled.toString())

        return mailSender
    }
}