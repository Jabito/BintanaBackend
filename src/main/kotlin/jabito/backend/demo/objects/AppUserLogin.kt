package jabito.backend.demo.objects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

data class AppUserLoginDAO(val appUserProfile: AppUserProfileDAO,
                           val appUserRole: AppUserRoleDAO,
                           val id: Int,
                           val username: String,
                           val password: String,
                           val email: String,
                           val loginAttempts: Int,
                           val isLocked: Boolean,
                           val isActive: Boolean,
                           val isLoggedOn: Boolean,
                           val passwordForReset: Boolean,
                           val lastLogDate: DateTime?,
                           val createdOn: DateTime?,
                           val createdBy: String,
                           val updatedOn: DateTime?,
                           val updatedBy: String?)

internal object AppUsersLogin: IntIdTable(){
    val appUserProfileId = reference("user_profile_id", AppUserProfiles)
    val appUserRoleId = reference("user_role_id", AppUserRoles)
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 80).uniqueIndex()
    val password = varchar("password", 64)
    val loginAttempts = integer("login_attempts").default(0)
    val isLocked = bool("is_locked").default(false)
    val isActive = bool("is_active").default(false)
    val isLoggedOn = bool("is_logged_on").default(false)
    val passwordForReset = bool("password_for_reset").default(false)
    val lastLogDateTime = datetime("last_log_datetime").nullable()
    val createdOn = datetime("created_on").nullable()
    val createdBy = varchar("created_by", 50).default("System")
    val updatedOn = datetime("updated_on").nullable()
    val updatedBy = varchar("updated_by", 50).nullable()
}

internal class AppUserLogin(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AppUserLogin>(AppUsersLogin)

    var appUserProfile by AppUserProfile referencedOn AppUsersLogin.appUserProfileId
    var appUserRole by AppUserRole referencedOn AppUsersLogin.appUserRoleId
    var username by AppUsersLogin.username
    var email by AppUsersLogin.email
    var password by AppUsersLogin.password
    var loginAttempts by AppUsersLogin.loginAttempts
    var isLocked by AppUsersLogin.isLocked
    var isActive by AppUsersLogin.isActive
    var isLoggedOn by AppUsersLogin.isLoggedOn
    var passwordForReset by AppUsersLogin.passwordForReset
    var lastLogDateTime by AppUsersLogin.lastLogDateTime
    var createdOn by AppUsersLogin.createdOn
    var createdBy by AppUsersLogin.createdBy
    var updatedOn by AppUsersLogin.updatedOn
    var updatedBy by AppUsersLogin.updatedBy

    fun toModel(): AppUserLoginDAO {
        return AppUserLoginDAO(appUserProfile.toModel(),
                appUserRole.toModel(),
                id.value,
                username,
                password,
                email,
                loginAttempts,
                isLocked,
                isActive,
                isLoggedOn,
                passwordForReset,
                lastLogDateTime,
                createdOn,
                createdBy,
                updatedOn,
                updatedBy)
    }
}
