package jabito.backend.demo.objects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime


data class ItemCategoryDAO(val id: Int,
                           val title: String,
                           val description: String,
                           val createdOn: DateTime?,
                           val createdBy: String,
                           val updatedOn: DateTime?,
                           val updatedBy: String?)

internal object ItemCategories : IntIdTable() {
    val title = varchar("title", 50)
    val description = varchar("description", 150)
    val createdOn = datetime("created_on").nullable()
    val createdBy = varchar("created_by",50).default("System")
    val updatedOn = datetime("updated_on").nullable()
    val updatedBy = varchar("updated_by", 50).nullable()
}

internal class ItemCategory(id: EntityID<Int>): Entity<Int>(id){
    companion object: EntityClass<Int, ItemCategory>(ItemCategories)

    var title by ItemCategories.title
    var description by ItemCategories.description
    var createdOn by ItemCategories.createdOn
    var createdBy by ItemCategories.createdBy
    var updatedOn by ItemCategories.updatedOn
    var updatedBy by ItemCategories.updatedBy

    fun toModel(): ItemCategoryDAO {
        return ItemCategoryDAO(id.value,
                title,
                description,
                createdOn,
                createdBy,
                updatedOn,
                updatedBy)
    }
}
