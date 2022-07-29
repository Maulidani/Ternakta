package com.startup.ternakta.ui.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.startup.ternakta.R
import com.startup.ternakta.ui.customer.Registration2CustomerActivity

class RegistrationSellerActivity : AppCompatActivity() {
    private val TAG = "RegisSeller"
    private val userType = "store"
    private val btnNext : MaterialButton by lazy { findViewById(R.id.btnAdd) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_seller)

        onClick()
    }

    private fun onClick(){

        btnNext.setOnClickListener {
            startActivity(Intent(applicationContext, Registration2SellerActivity::class.java))
        }
    }
}