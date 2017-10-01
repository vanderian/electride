package sk.vander.electride.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.LocalDate
import sk.vander.electride.db.entity.Waypoint
import sk.vander.electride.ui.Recurrence

/**
 * @author marian on 24.9.2017.
 */
object Converters {
  @JvmStatic @TypeConverter
  fun fromLocalDate(localDate: LocalDate): Long = localDate.toEpochDay()

  @JvmStatic @TypeConverter
  fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

  @JvmStatic @TypeConverter
  fun fromRecurrence(recurrence: Recurrence): Int = recurrence.ordinal

  @JvmStatic @TypeConverter
  fun toRecurrence(ordinal: Int): Recurrence = Recurrence.values()[ordinal]

  @JvmStatic @TypeConverter
  fun fromWaypoints(waypoints: List<Waypoint>): String = Gson().toJson(waypoints)

  @JvmStatic @TypeConverter
  fun toWaypoints(waypointsJson: String): List<Waypoint> =
      Gson().fromJson(waypointsJson, object : TypeToken<List<Waypoint>>() {}.type)
}