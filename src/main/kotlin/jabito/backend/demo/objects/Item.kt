package jabito.backend.demo.objects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

data class ItemDAO(val itemCategory: ItemCategoryDAO,
                   val id: Int,
                   val title: String,
                   val description: String?,
                   val cost: Double,
                   val isActive: Boolean,
                   val createdOn: DateTime?,
                   val createdBy: String,
                   val updatedOn: DateTime?,
                   val updatedBy: String?)

internal object Items : IntIdTable() {
    val itemCategoryId = reference("item_category_id", ItemCategories)
    val title = varchar("title", 50)
    val description = varchar("description", 150).nullable()
    val cost = double("cost").default(0.0)
    val isActive = bool("is_active").default(true)
    val createdOn = datetime("created_on").nullable()
    val createdBy = varchar("created_by", 50).default("System")
    val updatedOn = datetime("updated_on").nullable()
    val updatedBy = varchar("updated_by", 50).nullable()
}

internal class Item(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Item>(Items)

    var itemCategory by ItemCategory referencedOn Items.itemCategoryId
    var title by Items.title
    var description by Items.description
    var cost by Items.cost
    var isActive by Items.isActive
    var createdOn by Items.createdOn
    var createdBy by Items.createdBy
    var updatedOn by Items.updatedOn
    var updatedBy by Items.updatedBy

    fun toModel(): ItemDAO{
        return ItemDAO(itemCategory.toModel(),
                id.value,
                title,
                description,
                cost,
                isActive,
                createdOn,
                createdBy,
                updatedOn,
                updatedBy)
    }
}
