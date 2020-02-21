package com.vimosanan.dataanalysersg.repository.network

import com.vimosanan.dataanalysersg.repository.models.EntityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    /**
     * REST API ACCESS POINT
     */

    //<<<<<<<<<<GET API FOR ALL THE RESPONSE OF DATA QUARTERS>>>>>>>>>>>>>>\\
    /**
     * @param resourceId
     * @param size
     */
    @GET("/api/action/datastore_search")
    suspend fun  getResults(@Query("offset") offset: Int,
                   @Query("resource_id") resourceId: String,
                   @Query("limit") size: Int): Response<EntityResponse>
}