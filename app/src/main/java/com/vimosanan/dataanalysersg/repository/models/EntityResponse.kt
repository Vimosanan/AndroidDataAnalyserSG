package com.vimosanan.dataanalysersg.repository.models

import com.google.gson.annotations.SerializedName

data class EntityResponse (
    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("result")
    var result: Result? = null
)