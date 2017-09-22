package sk.vander.electride.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author marian on 21.9.2017.
 */
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val completed: Boolean
) {
   companion object {
    fun new(): Route = Route(0, false)
   }
}