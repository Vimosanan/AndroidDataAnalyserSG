package com.vimosanan.dataanalysersg.repository.models

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("resource_id")
    var resourceId: String? = null,

    @SerializedName("records")
    var records: List<Record> = emptyList(),

    @SerializedName("_links")
    var links: Links? = null,

    @SerializedName("limit")
    var limit: Int? = null,

    @SerializedName("total")
    var total: Int? = null
)