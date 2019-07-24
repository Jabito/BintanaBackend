package jabito.backend.demo.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MailProperties {

    @Value("\${spring.mail.host}")
    lateinit var host: String

    @Value("\${spring.mail.port}")
    lateinit var port: String

    @Value("\${spring.mail.username}")
    lateinit var email: String

    @Value("\${spring.mail.password}")
    lateinit var password: String
}
