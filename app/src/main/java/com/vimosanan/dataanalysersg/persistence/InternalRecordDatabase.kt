package com.vimosanan.dataanalysersg.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vimosanan.dataanalysersg.repository.models.InternalRecordData

@Database(entities = [InternalRecordData::class], version = 1, exportSchema = false)
abstract class InternalRecordDatabase: RoomDatabase() {
    abstract fun internalDataDao(): InternalRecordDao
}
