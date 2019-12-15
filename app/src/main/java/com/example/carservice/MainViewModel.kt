package com.example.carservice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carservice.item.Placemark
import com.example.carservice.item.PlacemarksResponse
import com.example.carservice.service.RemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class MainViewModel : ViewModel(){

    private val remoteRepository: RemoteRepository =
        RemoteRepository()

    val carList = MutableLiveData<List<Placemark>>()
    val placemarksRequestFailure = MutableLiveData<Failure>()

    fun requestCarList(){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                handleCarListResponse(remoteRepository.getPlacemarks())
            }
        }
    }

    private fun handleCarListResponse(placemarksResponse: Response<PlacemarksResponse>) {
        try {
            if (placemarksResponse.isSuccessful) {
                placemarksResponse.body()?.let {
                    carList.value = it.placemarks
                }
            } else {
                placemarksRequestFailure.value = Failure(placemarksResponse.errorBody().toString())
            }
        } catch (e: HttpException) {
            placemarksRequestFailure.value = Failure(e.message())
        } catch (e: Throwable) {
            e.message?.let{
                placemarksRequestFailure.value = Failure(it)
            }
        }
    }

}