package com.danfcorrea.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danfcorrea.tasks.service.constants.TaskConstants
import com.danfcorrea.tasks.service.listener.APIListener
import com.danfcorrea.tasks.service.model.PersonModel
import com.danfcorrea.tasks.service.model.PriorityModel
import com.danfcorrea.tasks.service.model.ValidationModel
import com.danfcorrea.tasks.service.repository.PersonRepository
import com.danfcorrea.tasks.service.repository.PriorityRepository
import com.danfcorrea.tasks.service.repository.SecurityPreferences
import com.danfcorrea.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser
    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _login.value = ValidationModel(message)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, personKey)

        _loggedUser.value = (token.isNotEmpty() && personKey.isNotEmpty())
    }

    fun downloadPriorities(){
        priorityRepository.list(object : APIListener<List<PriorityModel>> {
            override fun onSuccess(result: List<PriorityModel>) {
                priorityRepository.save(result)
            }

            override fun onFailure(message: String) {
            }

        })
    }

}