package com.vimosanan.dataanalysersg.repository.models

import com.google.gson.annotations.SerializedName

data class Record (

    @SerializedName("volume_of_mobile_data")
    var volume: Double? = null,

    @SerializedName("quarter")
    var quarter:String? = null,

    @SerializedName("_id")
    var id: Int? = null
)