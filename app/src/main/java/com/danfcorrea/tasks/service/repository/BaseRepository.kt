package com.danfcorrea.tasks.service.repository

import com.danfcorrea.tasks.service.constants.TaskConstants
import com.danfcorrea.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response

open class BaseRepository {
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) {
        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            response.body()?.let { listener.onSuccess(it) }
        } else {
            response.errorBody()
                .let { if (it != null) listener.onFailure(failResponse(it.string())) }
        }
    }
}