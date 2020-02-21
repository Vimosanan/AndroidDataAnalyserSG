package com.vimosanan.dataanalysersg.repository.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_usage")
data class InternalRecordData(
    @field:PrimaryKey
    @field:ColumnInfo(name = "year")
    val year: String,

    @field:ColumnInfo(name = "first_quarter_volume")
    val firstQuarter: Double,

    @field:ColumnInfo(name = "second_quarter_volume")
    val secondQuarter: Double,

    @field:ColumnInfo(name = "third_quarter_volume")
    val thirdQuarter: Double,

    @field:ColumnInfo(name = "forth_quarter_volume")
    val forthQuarter: Double)