package com.startup.ternakta.ui.seller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.customer.Registration2CustomerActivity
import com.startup.ternakta.utils.Constant.setShowProgress
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registration2SellerActivity : AppCompatActivity() {
    private val TAG = "Regis2Seller"
    private var userType = "store"
    private lateinit var sharedPref: PreferencesHelper

    private val imgback : ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead : TextView by lazy { findViewById(R.id.tvHead) }
    private val btnNext : MaterialButton by lazy { findViewById(R.id.btnAdd) }
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
        setContentView(R.layout.activity_registration2_seller)


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
        }else if (intentAction == "view") {
            tvHead.text = "Alamat"
            btnNext.visibility = View.GONE
            inputProvince.setTextColor(Color.BLACK)
            inputCity.setTextColor(Color.BLACK)
            inputDistricts.setTextColor(Color.BLACK)
            inputAddress.setTextColor(Color.BLACK)
            inputProvince.setText(intentProvince)
            inputProvince.isEnabled = false
            inputCity.setText(intentCity)
            inputCity.isEnabled = false
            inputDistricts.setText(intentDistricts)
            inputDistricts.isEnabled = false
            inputAddress.setText(intentAddress)
            inputAddress.isEnabled = false
        }
        onClick()
    }

    private fun onClick(){

        imgback.setOnClickListener { finish() }

        btnNext.setOnClickListener {
            val province = inputProvince.text.toString()
            val city = inputCity.text.toString()
            val districts = inputDistricts.text.toString()
            val address = inputAddress.text.toString()


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

                if (province.isNotEmpty() && city.isNotEmpty() && districts.isNotEmpty() && address.isNotEmpty()) {

                    Log.e(TAG, "Next: $province, $city, $districts, $address",)
                    startActivity(
                        Intent(applicationContext, Registration3SellerActivity::class.java)
                            .putExtra("province", province)
                            .putExtra("city", city)
                            .putExtra("districts", districts)
                            .putExtra("address", address)
                    )
                    finish()

                } else {
                    Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
                }
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