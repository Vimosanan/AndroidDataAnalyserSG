package com.vimosanan.dataanalysersg.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.vimosanan.dataanalysersg.repository.models.InternalRecordData

@Dao
interface InternalRecordDao {

    @Insert(onConflict = IGNORE)
    fun insertDataToLocalDatabase(list: List<InternalRecordData>)

    @Query("SELECT * FROM data_usage ORDER BY year ASC")
    fun getAllData():List<InternalRecordData>
}