package com.startup.ternakta.ui.customer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant.setShowProgress
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationCustomerActivity : AppCompatActivity() {
    private val TAG = "RegisCustomer"
    var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }

    private val btnNext: MaterialButton by lazy { findViewById(R.id.btnAdd) }
    private val inputProvince: TextInputEditText by lazy { findViewById(R.id.inputProvince) }
    private val inputCity: TextInputEditText by lazy { findViewById(R.id.inputCity) }
    private val inputDistricts: TextInputEditText by lazy { findViewById(R.id.inputDistricts) }
    private val inputAddress: TextInputEditText by lazy { findViewById(R.id.inputAddress) }

    private var intentProvince = ""
    private var intentCity = ""
    private var intentDistricts = ""
    private var intentAddress = ""
    private var intentName = ""
    private var intentPhone = ""
    private var intentPassword = ""
    private var intentId = ""
    private var intentAction = ""
    private var intentType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_customer)

        sharedPref = PreferencesHelper(applicationContext)
        userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()

        intentAction = intent.getStringExtra("action").toString()
        intentType = intent.getStringExtra("type").toString()
        intentId = intent.getStringExtra("id").toString()
        intentName = intent.getStringExtra("name").toString()
        intentPhone = intent.getStringExtra("phone").toString()
        intentPassword = intent.getStringExtra("password").toString()

        intentProvince = intent.getStringExtra("province").toString()
        intentCity = intent.getStringExtra("city").toString()
        intentDistricts = intent.getStringExtra("districts").toString()
        intentAddress = intent.getStringExtra("address").toString()

        if (intentAction == "edit") {
            btnNext.text = "Edit alamat"
            inputProvince.setText(intentProvince)
            inputCity.setText(intentCity)
            inputDistricts.setText(intentDistricts)
            inputAddress.setText(intentAddress)
        }
        onClick()
    }

    private fun onClick() {
        imgBack.setOnClickListener { finish() }

        btnNext.setOnClickListener {
            val province = inputProvince.text.toString()
            val city = inputCity.text.toString()
            val districts = inputDistricts.text.toString()
            val address = inputAddress.text.toString()

            if (province.isNotEmpty() && city.isNotEmpty() && districts.isNotEmpty() && address.isNotEmpty()) {

                if (intentAction == "edit") {
                    Log.e(TAG, "Next: $province, $city, $districts, $address")
                    if (intentProvince == inputProvince.text.toString() &&
                        intentCity == inputCity.text.toString() &&
                        intentDistricts == inputDistricts.text.toString() &&
                        intentAddress == inputAddress.text.toString()
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "Tidak ada data yang berubah",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        editUser(
                            intentId,
                            inputProvince.text.toString(),
                            inputCity.text.toString(),
                            inputDistricts.text.toString(),
                            inputAddress.text.toString(),
                            intentName,
                            intentPhone,
                            intentPassword
                        )

                    }
                } else {
                    startActivity(
                        Intent(applicationContext, Registration2CustomerActivity::class.java)
                            .putExtra("province", province)
                            .putExtra("city", city)
                            .putExtra("districts", districts)
                            .putExtra("address", address)
                    )
                    finish()
                }

            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editUser(
        userId: String,
        province: String,
        city: String,
        districts: String,
        address: String,
        name: String,
        phone: String,
        password: String,
    ) {
        btnNext.setShowProgress(true)

        ApiClient.instances.editWithoutImgUser(
            userId,
            userType,
            name,
            phone,
            password,
            province,
            city,
            districts,
            address,
        ).enqueue(object : Callback<Model.ResponseModel> {
            override fun onResponse(
                call: Call<Model.ResponseModel>,
                response: Response<Model.ResponseModel>
            ) {
                val responseBody = response.body()
                val message = responseBody?.message
                val user = responseBody?.user

                if (response.isSuccessful && message == "Success") {
                    Log.e(TAG, "onResponse: $responseBody")
                    Toast.makeText(
                        applicationContext,
                        "Berhasil edit alamat",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (user != null) {
                        sharedPref.put(PreferencesHelper.PREF_USER_PHONE, user.phone)
                        sharedPref.put(PreferencesHelper.PREF_USER_PASSWORD, user.password)

                        finish()
                    }

                } else {
                    Log.e(TAG, "onResponse: $response")
                    Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                }
                btnNext.setShowProgress(false)

            }

            override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                btnNext.setShowProgress(false)
            }

        })

    }
}