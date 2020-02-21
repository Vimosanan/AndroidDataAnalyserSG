package com.vimosanan.dataanalysersg.repository.models

data class RecordViewData(
    var isRegular: Boolean = false,
    var total: Double = 0.0,
    val actualData: InternalRecordData
)