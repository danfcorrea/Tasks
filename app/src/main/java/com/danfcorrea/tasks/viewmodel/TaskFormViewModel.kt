package com.danfcorrea.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danfcorrea.tasks.service.listener.APIListener
import com.danfcorrea.tasks.service.model.PriorityModel
import com.danfcorrea.tasks.service.model.TaskModel
import com.danfcorrea.tasks.service.model.ValidationModel
import com.danfcorrea.tasks.service.repository.PriorityRepository
import com.danfcorrea.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave
    fun loadPriorities(){
        _priorityList.value = priorityRepository.list()
    }

    fun save(task: TaskModel){
        taskRepository.create(task, object :APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message)
            }
        })
    }

}