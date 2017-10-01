package sk.vander.electride.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDate
import sk.vander.electride.ui.Recurrence

/**
 * @author marian on 21.9.2017.
 */
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val date: LocalDate,
    val recurrence: Recurrence
) {
  companion object {
//    fun new(): Route = Route(0, )
  }
}