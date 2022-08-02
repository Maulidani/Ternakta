package com.startup.ternakta.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.startup.ternakta.R
import com.startup.ternakta.utils.PreferencesHelper

class OrderDetailActivity : AppCompatActivity() {
    private val TAG = "orderDetail"
    private var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        sharedPref = PreferencesHelper(applicationContext)
        onClick()
    }

    private fun onClick(){

    }

}