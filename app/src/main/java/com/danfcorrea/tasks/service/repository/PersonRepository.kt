package com.danfcorrea.tasks.service.repository

import android.content.Context
import com.danfcorrea.tasks.R
import com.danfcorrea.tasks.service.listener.APIListener
import com.danfcorrea.tasks.service.model.PersonModel
import com.danfcorrea.tasks.service.repository.remote.PersonService
import com.danfcorrea.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository() {

    private val remote = RetrofitClient.getService(PersonService::class.java)
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        val call = remote.login(email, password)
        call.enqueue(object : Callback<PersonModel> {
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}