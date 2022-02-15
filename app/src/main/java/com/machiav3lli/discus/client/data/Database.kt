package com.machiav3lli.discus.client.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@androidx.room.Database(entities = [Project::class], version = 2)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract val projectDao: ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getInstance(context: Context): Database {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            Database::class.java,
                            "projects.db"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}

object Converters {
    @TypeConverter
    fun toList(string: String): MutableList<String> {
        return if (string == "") mutableListOf()
        else string.split(",").toMutableList()
    }

    @TypeConverter
    fun toString(list: MutableList<String?>?): String {
        return if (list?.isNotEmpty() == true) list.joinToString(",")
        else ""
    }

    @TypeConverter
    fun toCalendar(l: Long): Calendar? {
        val c = Calendar.getInstance()
        c.timeInMillis = l
        return c
    }

    @TypeConverter
    fun toLong(c: Calendar?): Long? {
        return c?.time?.time
    }
}
