package jabito.backend.demo.services

import jabito.backend.demo.data.*
import jabito.backend.demo.objects.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class AppUserService(@Autowired private val passwordEncoder: PasswordEncoder,
                     @Autowired private val emailService: EmailService) {

    @Autowired
    lateinit var mailProperties: MailProperties

    fun createUserAdmin(): ResponseEntity<Any> {
        transaction {
            addLogger(StdOutSqlLogger)
            val adminRole = AppUserRole.find { AppUserRoles.title eq "ADMIN" }.first()
            val adminProfile = AppUserProfile.find {
                AppUserProfiles.firstName eq "System" and (AppUserProfiles.lastName eq "Admin")
            }.first()

            AppUsersLogin.insertIgnore {
                it[appUserProfileId] = adminProfile.id
                it[appUserRoleId] = adminRole.id
                it[username] = "admin"
                it[password] = encodeRandomPasswordAndEmail(mailProperties.email)
                it[createdOn] = Global.CURRENT_TIMESTAMP
            }
        }
        return ResponseEntity("res", HttpStatus.OK)
    }

    fun registerAppUser(params: JsonAppUserRegister): HashMap<String, Any> {
        val response: HashMap<String, Any> = hashMapOf()
        val regDate: DateTime? = Global.CURRENT_TIMESTAMP
        transaction {
            addLogger(StdOutSqlLogger)
            val user = AppUserLogin.find {
                AppUsersLogin.username eq params.username or (
                        AppUsersLogin.email eq params.email) }.firstOrNull()?.toModel()

            if (user != null) {
                val userHit: Boolean = params.username.equals(user.username)
                val emailHit: Boolean = params.email.equals(user.email)
                val match: String = if (userHit && emailHit) "Username and Email"
                else if (userHit) "Username"
                else if (emailHit) "Email" else ""

                response.put("responseCode", HttpStatus.NOT_ACCEPTABLE)
                response.put("errorDesc", "$match already exists.")
            } else {
                val appUserProfile = AppUserProfiles.insertAndGetId {
                    it[firstName] = params.firstName
                    it[lastName] = params.lastName
                    it[createdBy] = params.appUsername
                    it[createdOn] = regDate
                }

                val appUserId = AppUsersLogin.insertAndGetId {
                    it[appUserProfileId] = appUserProfile
                    it[appUserRoleId] = AppUserRole.get(params.roleId).id
                    it[username] = params.username
                    it[email] = params.email
                    it[password] = encodeRandomPasswordAndEmail(params.email)
                    it[createdBy] = params.appUsername
                    it[createdOn] = regDate
                }
                response.put("appUserProfileId", appUserProfile)
                response.put("appUserId", appUserId)
                response.put("responseCode", HttpStatus.OK)
                response.put("responseDesc", "Successfully Registered.")
            }
        }

        return response
    }

    fun encodeRandomPasswordAndEmail(email: String): String {
        val newPassword: String = generateRandomString(7)
        emailService.emailTemporaryPassword(email, newPassword)
        return passwordEncoder.encode(newPassword)
    }


    fun generateRandomString(charCount: Int): String = UUID.randomUUID().toString().take(charCount)

    fun getAppUser(appUserId: Int): HashMap<String, Any> {
        val response: HashMap<String, Any> = hashMapOf()
        val appUser = transaction {
            addLogger(StdOutSqlLogger)
            AppUserLogin.findById(appUserId)?.toModel()
        }
        if (null != appUser) {
            response.put("appUserDetails", appUser)
            response.put("responseCode", HttpStatus.OK)
            response.put("responseDesc", "AppUser Found.")
        } else {
            response.put("responseDesc", "AppUser not found.")
            response.put("responseCode", HttpStatus.NOT_FOUND)
        }

        return response
    }

    fun login(username: String, password: String): Any {
        val response: HashMap<String, Any> = hashMapOf()
        val appUser: AppUserLoginDAO? = transaction {
            addLogger(StdOutSqlLogger)
            AppUserLogin.find {
                AppUsersLogin.username eq username
            }.firstOrNull()?.toModel()
        }
        if(null == appUser){
            response.put("responseDesc", "Username not found.")
            response.put("responseCode", HttpStatus.NOT_FOUND)
        }else{
            if(passwordEncoder.matches(password, appUser.password)){
                transaction {
                    addLogger(StdOutSqlLogger)
                    response.put("userInfo", appUser)
                    response.put("responseCode", HttpStatus.OK)
                    response.put("responseDesc", "Welcome back ${appUser.appUserProfile.getFullName()}")
                }
            }else{
                response.put("responseDesc", "Password incorrect.")
                response.put("responseCode", HttpStatus.UNAUTHORIZED)
            }
        }

        return response
    }
}
