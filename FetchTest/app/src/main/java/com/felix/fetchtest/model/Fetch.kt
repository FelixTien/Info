package com.felix.fetchtest.model

import retrofit2.Call
import retrofit2.http.GET

data class Fetch(
    var id: Int,
    var listId: Int,
    var name: String?
)

interface FetchApi {
    @GET("hiring.json")
    fun getFetch(): Call<List<Fetch>>
}