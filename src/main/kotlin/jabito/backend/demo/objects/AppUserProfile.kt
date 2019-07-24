package jabito.backend.demo.objects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

data class AppUserProfileDAO(val id: Int,
                             val firstName: String,
                             val lastName: String,
                             val contactNo: String?,
                             val address: String?,
                             val gender: Char?,
                             val createdOn: DateTime?,
                             val createdBy: String,
                             val updatedOn: DateTime?,
                             val updatedBy: String?) {
    fun getFullName(): String = "${this.firstName} ${this.lastName}"
}

internal object AppUserProfiles: IntIdTable() {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val contactNo = varchar("contact_no", 20).nullable()
    val address = varchar("address", 200).nullable()
    val gender = char("gender").default('O')
    val createdOn = datetime("created_on").nullable()
    val createdBy = varchar("created_by",50).default("System")
    val updatedOn = datetime("updated_on").nullable()
    val updatedBy = varchar("updated_by", 50).nullable()
}


internal class AppUserProfile(id : EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AppUserProfile>(AppUserProfiles)

    var firstName by AppUserProfiles.firstName
    var lastName by AppUserProfiles.lastName
    var contactNo by AppUserProfiles.contactNo
    var address by AppUserProfiles.address
    var gender by AppUserProfiles.gender
    var createdOn by AppUserProfiles.createdOn
    var createdBy by AppUserProfiles.createdBy
    var updatedOn by AppUserProfiles.updatedOn
    var updatedBy by AppUserProfiles.updatedBy

    fun toModel(): AppUserProfileDAO {
        return AppUserProfileDAO(id.value,
                firstName,
                lastName,
                contactNo,
                address,
                gender,
                createdOn,
                createdBy,
                updatedOn,
                updatedBy)
    }
}

