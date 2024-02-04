package com.nb.findfalcone.repository

import com.nb.findfalcone.api.ApiService
import com.nb.findfalcone.model.FalconeRequest
import com.nb.findfalcone.model.FalconeResponse
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getToken() = apiService.getToken()

    suspend fun getPlanets() = apiService.getPlanets()

    suspend fun getVehicles() = apiService.getVehicles()

    suspend fun findFalcone(falconeRequest: FalconeRequest): Response<FalconeResponse>{
        return apiService.findFalcone(falconeRequest)
    }

}