package com.danfcorrea.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.danfcorrea.tasks.R
import com.danfcorrea.tasks.databinding.ActivityLoginBinding
import com.danfcorrea.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        viewModel.verifyLoggedUser()
        viewModel.downloadPriorities()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        if(v.id == R.id.button_login){
            handleLogin()
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if(it.status()){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loggedUser.observe(this){
            if(it){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun handleLogin(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.doLogin(email, password)
    }
}