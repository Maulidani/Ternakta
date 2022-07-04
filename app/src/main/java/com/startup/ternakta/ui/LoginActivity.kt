package com.startup.ternakta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.startup.ternakta.R
import com.startup.ternakta.ui.customer.MainCustomerActivity

class LoginActivity : AppCompatActivity() {
    val btnLogin : MaterialButton by lazy { findViewById(R.id.btnLogin) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onClick()
    }

    private fun onClick(){

        btnLogin.setOnClickListener {
            startActivity(Intent(applicationContext, MainCustomerActivity::class.java))
        }
    }
}