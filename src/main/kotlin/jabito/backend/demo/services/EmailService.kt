package jabito.backend.demo.services

import jabito.backend.demo.data.MailProperties
import org.springframework.stereotype.Service
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import org.springframework.beans.factory.annotation.Autowired

@Service
class EmailService {

    @Autowired
    lateinit var mailProperties: MailProperties

    fun sendForgotPasswordEmail(){

    }

    fun emailTemporaryPassword(email: String, newPassword: String) {

        sendEmail(email, "Your new Password", "Your new Password is $newPassword.")
    }

    fun verifyAccountByEmail(email: String, newPassword: String){
        sendEmail(email,"REGISTRATION SUCCESSFUL",
                "You have successfully registered your account. Please login using this temporary password : ${newPassword}")
    }

    fun sendEmail(targetEmail: String, subject: String, message: String){

        val email = HtmlEmail()
        email.hostName = mailProperties.host
        email.setSmtpPort(mailProperties.port.toInt())
        email.setAuthenticator(DefaultAuthenticator(mailProperties.email, mailProperties.password))
        email.isSSLOnConnect = true
        email.setFrom(mailProperties.email)
        email.addTo(targetEmail)
        email.subject = subject
//        val kotlinLogoURL = URL("https://kotlinlang.org/assets/images/twitter-card/kotlin_800x320.png")
//        val cid = email.embed(kotlinLogoURL, "Kotlin logo")
        email.setHtmlMsg(message)
        email.send()
    }
}
