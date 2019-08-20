package jabito.backend.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JabitoKotlinBackendApplication

fun main(args: Array<String>) {
    runApplication<JabitoKotlinBackendApplication>(*args)
}
