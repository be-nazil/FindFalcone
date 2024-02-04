package com.nb.findfalcone.api

import com.nb.findfalcone.model.Auth
import com.nb.findfalcone.model.FalconeRequest
import com.nb.findfalcone.model.FalconeResponse
import com.nb.findfalcone.model.Planet
import com.nb.findfalcone.model.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/token")
    suspend fun getToken(): Response<Auth>

    @GET("/vehicles")
    suspend fun getVehicles(): Response<List<Vehicle>>

    @GET("/planets")
    suspend fun getPlanets(): Response<List<Planet>>

    @POST("/find")
    suspend fun findFalcone(@Body falconeRequest: FalconeRequest): Response<FalconeResponse>

}