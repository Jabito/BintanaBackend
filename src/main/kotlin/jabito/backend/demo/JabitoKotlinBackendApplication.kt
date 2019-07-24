package jabito.backend.demo

import jabito.backend.demo.data.*
import jabito.backend.demo.objects.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JabitoKotlinBackendApplication

fun main(args: Array<String>) {
    val app = runApplication<JabitoKotlinBackendApplication>(*args)
    val env = app.environment
    Database.connect(env.getProperty("spring.datasource.url")!!,
            driver = env.getProperty("spring.datasource.driver-class-name")!!,
            user = env.getProperty("spring.datasource.username")!!,
            password = env.getProperty("spring.datasource.password")!!)

    transaction {
        addLogger(StdOutSqlLogger)
    //		SchemaUtils.drop(
    //                AppUserProfiles,
    //                AppUserRoles,
    //                ItemCategories,
    //                Items, AppUsersLogin)

        SchemaUtils.create(AppUserRoles,
                AppUserProfiles,
                AppUsersLogin,
                ItemCategories,
                Items)

        AppUserRoles.insertIgnore {
            it[title] = "ADMIN"
            it[description] = "System Administrator"
            it[createdOn] = Global.CURRENT_TIMESTAMP
        }
        AppUserProfiles.insertIgnore {
            it[firstName] = "System"
            it[lastName] = "Admin"
            it[contactNo] = "09055155224"
            it[createdOn] = Global.CURRENT_TIMESTAMP
        }
    }
}
