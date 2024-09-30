package com.danfcorrea.tasks.service.repository

import android.content.Context
import com.danfcorrea.tasks.R
import com.danfcorrea.tasks.service.listener.APIListener
import com.danfcorrea.tasks.service.model.PriorityModel
import com.danfcorrea.tasks.service.repository.local.TaskDatabase
import com.danfcorrea.tasks.service.repository.remote.PriorityService
import com.danfcorrea.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository() {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int):String{
            return cache[id]?: ""
        }
        fun setDescription(id:Int, string: String){
            cache[id] = string
        }
    }

    fun list(listener: APIListener<List<PriorityModel>>) {
        val call = remote.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })

    }

    fun list(): List<PriorityModel> {
        return database.list()
    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }

    fun getPriorityDescription(id: Int): String{
        val cached = getDescription(id)
        return if(cached == ""){
            val description = database.getDescription(id)
            setDescription(id, description)
            description
        }else{
            cached
        }
    }
}
