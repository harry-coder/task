package com.example.noteskeeper.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")

@Parcelize
data class Task(
    val name: String="",
    val description: String="",
    val important: Int = 3,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
   // val created: Long =0L,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateInstance().format(created)
}