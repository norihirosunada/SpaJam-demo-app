package com.norihiro.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "foods")
data class Foods(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "color")
    var color: String,
    @ColumnInfo(name = "ingredient")
    var name: String,
    @ColumnInfo(name = "date")
    var date: String,
)
