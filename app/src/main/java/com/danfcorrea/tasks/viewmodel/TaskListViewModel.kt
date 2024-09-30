package com.danfcorrea.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danfcorrea.tasks.service.listener.APIListener
import com.danfcorrea.tasks.service.model.TaskModel
import com.danfcorrea.tasks.service.repository.PriorityRepository
import com.danfcorrea.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    fun listTasks(){
        taskRepository.list(object : APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach{
                    it.priorityDescription = priorityRepository.getPriorityDescription(it.priorityId)
                }
                _tasks.value = result
            }

            override fun onFailure(message: String) {
            }
        })
    }

}