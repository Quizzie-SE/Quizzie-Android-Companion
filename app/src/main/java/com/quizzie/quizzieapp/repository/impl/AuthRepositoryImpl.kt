package com.quizzie.quizzieapp.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quizzie.quizzieapp.network.AuthService
import com.quizzie.quizzieapp.repository.AuthRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepositoryImpl : AuthRepository {

    fun adminLogin(email: String, password: String): LiveData<String> {

        val loginResponse = MutableLiveData<String>()

        AuthService().adminLogin(email, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        loginResponse.value = response.body()?.string()
                    } else {
                        loginResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    loginResponse.value = t.message
                }
            })
        return loginResponse
    }

}