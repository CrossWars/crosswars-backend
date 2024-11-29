package xyz.crosswars.service

import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.crosswars.config.EmailConfig

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val emailConfig: EmailConfig,
    private val userService: UserService,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val subject = "Daily Crossword Reminder!"

    fun sendSimpleMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom(emailConfig.username)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)

        mailSender.send(message)
    }

    @Transactional(readOnly = true)
    fun sendDailyReminderEmails() {
        if (emailConfig.enabled) {
            val usersToRemind = userService.findUsersByEmailReminder(true)

            usersToRemind.forEach { user ->
                val message =
                    "Hello ${user.name},\nDon't forget to complete the daily crossword and submit your time to https://crosswars.xyz!\n\nThis account is not monitored, please do not reply to this email."
                user.email?.let { email ->
                    sendSimpleMessage(to = email, subject = subject, text = message)
                } ?: log.warn("couldn't send daily reminder to [user=${user.name},id=${user.userId}] because they don't have an email")
            }
        }
    }

    /**
     * Send weekday reminder at 4pm
     */
    @Scheduled(cron = "0 0 16 * * MON-FRI", zone = "EST")
    fun sendWeekDayReminder() {
        sendDailyReminderEmails()
    }

    /**
     * Send weekend reminder at 8pm
     */
    @Scheduled(cron = "0 0 20 * * SAT-SUN", zone = "EST")
    fun sendWeekendReminder() {
        sendDailyReminderEmails()
    }
}