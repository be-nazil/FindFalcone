package com.nb.findfalcone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.nb.findfalcone.api.ApiResource
import com.nb.findfalcone.model.ApiResponse
import com.nb.findfalcone.model.FalconeRequest
import com.nb.findfalcone.model.FalconeResponse
import com.nb.findfalcone.model.Planet
import com.nb.findfalcone.model.Vehicle
import com.nb.findfalcone.model.VehicleToPlanet
import com.nb.findfalcone.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class FalconeViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): ViewModel() {

    private fun <T> coroutineExceptionHandler(d: MutableLiveData<ApiResource<T>>?) : CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.d("FalconeViewModel", throwable.localizedMessage ?:"")
            d?.postValue(ApiResource.error(throwable.localizedMessage ?: "", null, 0))
        }
    }

    val vehicleToPlanetMutable  = MutableLiveData<VehicleToPlanet>()
    val vehicleToPlanet :MutableList<VehicleToPlanet> = mutableListOf()

    private val _planets = MutableLiveData<List<Planet>>()
    val planetsLiveData: LiveData<List<Planet>>
        get() = _planets


    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehiclesLiveData: LiveData<List<Vehicle>>
        get() = _vehicles

    private var tokenStr: String? = null


    private suspend fun getToken(): String? {
        var token : String? = null
        apiRepository.getToken().let {
            if (it.isSuccessful && it.body()?.token != null) {
                token = it.body()?.token
            }
        }
        return token
    }

    fun findFalcone(fR: FalconeRequest): LiveData<ApiResource<FalconeResponse>> {
        val falconeResponse = MutableLiveData<ApiResource<FalconeResponse>>()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler(falconeResponse)) {
            tokenStr?.let { token ->
                falconeResponse.postValue(ApiResource.loading(null))
                apiRepository.findFalcone(
                    fR.copy(token = token)
                ).let {
                    if (it.isSuccessful && it.body() != null) {
                        falconeResponse.postValue(
                            ApiResource.success(
                                it.body(), it.code()
                            )
                        )
                    } else {
                        falconeResponse.postValue(
                            ApiResource.failure("King! it seems there is some issue in our space vehicles")
                        )
                    }
                }
            } ?: run {
                falconeResponse.postValue(
                    ApiResource.error("King! we failed to get token", null, 1)
                )
            }
        }
        return falconeResponse
    }

    fun getPlanets(): LiveData<ApiResource<ApiResponse>> {
        val planetList = MutableLiveData<ApiResource<ApiResponse>>()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler(planetList)) {
            planetList.postValue(ApiResource.loading(null))
            apiRepository.getPlanets().let {
                if (it.isSuccessful && it.body() != null) {
                    if (it.body().isNullOrEmpty().not()) {
                        _planets.postValue(it.body())
                        planetList.postValue(
                            ApiResource.success(ApiResponse(""), it.code())
                        )
                        return@launch
                    }
                    planetList.postValue(
                        ApiResource.failure("Oops, King! we couldn't' find any planets.")
                    )
                } else {
                    planetList.postValue(
                        ApiResource.failure("Oops, King! we couldn't' find any planets.")
                    )
                }
            }
        }
        return planetList
    }

    fun getVehicles(): LiveData<ApiResource<ApiResponse>> {
        val vehicleList = MutableLiveData<ApiResource<ApiResponse>>()
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler(vehicleList)) {
            vehicleList.postValue(ApiResource.loading(null))
            apiRepository.getVehicles().let {
                if (it.isSuccessful && it.body() != null) {
                    if (it.body().isNullOrEmpty().not()) {
                        _vehicles.postValue(it.body())
                        vehicleList.postValue(
                            ApiResource.success(ApiResponse("success"), it.code())
                        )
                        return@launch
                    }
                    vehicleList.postValue(
                        ApiResource.failure("Oops, King! it seems we lost our space vehicles.")
                    )
                } else {
                    vehicleList.postValue(
                        ApiResource.failure("Oops, King! we couldn't' find any vehicles.")
                    )
                }
            }
        }
        return vehicleList
    }

    suspend fun initialSetUp() {
        viewModelScope.async {
            getPlanets()
            getVehicles()
            getToken()?.let {
                tokenStr = it
            }
        }
    }

}