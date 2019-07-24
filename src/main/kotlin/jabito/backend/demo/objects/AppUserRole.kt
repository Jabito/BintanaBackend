package jabito.backend.demo.objects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

data class AppUserRoleDAO(val id: Int,
                          val title: String,
                          val description: String,
                          val createdOn: DateTime?,
                          val createdBy: String,
                          val updatedOn: DateTime?,
                          val updatedBy: String?)

internal object AppUserRoles : IntIdTable(){
    val title = varchar("title", 50).uniqueIndex()
    val description = varchar("description", 150)
    val createdOn = datetime("created_on").nullable()
    val createdBy = varchar("created_by",50).default("System")
    val updatedOn = datetime("updated_on").nullable()
    val updatedBy = varchar("updated_by", 50).nullable()
}

internal class AppUserRole(id : EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, AppUserRole>(AppUserRoles)

    var title by AppUserRoles.title
    var description by AppUserRoles.description
    var createdOn by AppUserRoles.createdOn
    var createdBy by AppUserRoles.createdBy
    var updatedOn by AppUserRoles.updatedOn
    var updatedBy by AppUserRoles.updatedBy

    fun toModel(): AppUserRoleDAO {
        return AppUserRoleDAO(id.value,
                title,
                description,
                createdOn,
                createdBy,
                updatedOn,
                updatedBy)
    }
}
