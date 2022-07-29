package com.startup.ternakta.ui.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.startup.ternakta.R

class Registration2SellerActivity : AppCompatActivity() {
    private val TAG = "Regis2Seller"
    private val userType = "store"
    private val btnNext : MaterialButton by lazy { findViewById(R.id.btnAdd) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2_seller)

        onClick()
    }

    private fun onClick(){
        btnNext.setOnClickListener {
            startActivity(Intent(applicationContext, Registration3SellerActivity::class.java))
        }

    }
}