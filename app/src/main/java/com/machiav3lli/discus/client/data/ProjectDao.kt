package com.machiav3lli.discus.client.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectDao {
    @Insert
    fun insert(vararg product: Project)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg obj: Project): Int

    @Query("SELECT * FROM Project WHERE _id = :id")
    fun get(id: Long): LiveData<Project?>

    @get:Query("SELECT * FROM project ORDER BY _id ASC")
    val all: LiveData<List<Project>>

    @Delete
    fun delete(obj: Project)

    @Query("DELETE FROM project WHERE _id = :id")
    fun deleteById(vararg id: Long): Int
}
