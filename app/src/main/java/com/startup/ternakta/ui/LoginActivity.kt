package com.startup.ternakta.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.customer.MainCustomerActivity
import com.startup.ternakta.ui.customer.Registration2CustomerActivity
import com.startup.ternakta.ui.customer.RegistrationCustomerActivity
import com.startup.ternakta.ui.seller.LoginSellerActivity
import com.startup.ternakta.utils.Constant.setShowProgress
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG = "Login"
    private val userType = "customer"
    private lateinit var sharedPref: PreferencesHelper

    private val btnLogin : MaterialButton by lazy { findViewById(R.id.btnLogin) }
    private val tvLoginSeller : TextView by lazy { findViewById(R.id.tvLoginSeller) }
    private val tvRegistration : TextView by lazy { findViewById(R.id.tvRegistration) }
    private val inputPhone: TextInputEditText by lazy { findViewById(R.id.inputPhone) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = PreferencesHelper(applicationContext)

        onClick()
    }

    private fun onClick(){

        btnLogin.setOnClickListener {
            val phone = inputPhone.text.toString()
            val password = inputPassword.text.toString()

            if (phone.isNotEmpty() && password.isNotEmpty()) {
                login(phone, password, userType)
            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }
        tvLoginSeller.setOnClickListener {
            startActivity(Intent(applicationContext, LoginSellerActivity::class.java))
        }
        tvRegistration.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationCustomerActivity::class.java))
        }
    }

    private fun login(phone: String, password: String, type: String) {
        btnLogin.setShowProgress(true)

        ApiClient.instances.loginUser(type, phone, password, "")
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val user = responseBody?.user

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")
                        saveSession(user)

                    } else {
                        Log.e(TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                    }
                    btnLogin.setShowProgress(false)

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                    Toast.makeText(applicationContext,  t.message.toString(), Toast.LENGTH_SHORT).show()
                    btnLogin.setShowProgress(false)
                }

            })
    }

    private fun saveSession(user: Model.UserModel?) {

        sharedPref.logout()
        sharedPref.put(PreferencesHelper.PREF_USER_ID, user!!.id)
        sharedPref.put(PreferencesHelper.PREF_USER_TYPE, userType)
        sharedPref.put(PreferencesHelper.PREF_USER_PHONE, user.phone)
        sharedPref.put(PreferencesHelper.PREF_USER_PASSWORD, user.password)
        sharedPref.put(PreferencesHelper.PREF_IS_LOGIN, true)

        Log.e(TAG, "saveSession: "+sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString(), )
        Log.e(TAG, "saveSession: "+sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString(), )
        Log.e(TAG, "saveSession: "+sharedPref.getString(PreferencesHelper.PREF_USER_PHONE).toString(), )
        Log.e(TAG, "saveSession: "+sharedPref.getString(PreferencesHelper.PREF_USER_PASSWORD).toString(), )
        Log.e(TAG, "saveSession: "+sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN).toString(), )

        startActivity(Intent(applicationContext, MainCustomerActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()

        if (sharedPref.getBoolean(PreferencesHelper.PREF_IS_LOGIN)) {
            if (sharedPref.getString(PreferencesHelper.PREF_USER_TYPE) == "customer") {
                startActivity(Intent(applicationContext, MainCustomerActivity::class.java))
                finish()

            } else {
                //
            }

        } else {
            //
        }
    }
}