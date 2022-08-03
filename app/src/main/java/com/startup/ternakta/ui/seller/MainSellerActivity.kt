package com.startup.ternakta.ui.seller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.LoginActivity
import com.startup.ternakta.ui.OrderHistoryActivity
import com.startup.ternakta.ui.ProductListActivity
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainSellerActivity : AppCompatActivity() {
    private val TAG = "MainSeller"
    private val userType = "store"
    private lateinit var sharedPref: PreferencesHelper

    private val imgProfile: ImageView by lazy { findViewById(R.id.imgProfile) }
    private val tvNameProfile: TextView by lazy { findViewById(R.id.tvNameProfile) }
    private val tvStatusSeller: TextView by lazy { findViewById(R.id.tvStatusSeller) }
    private val cardProduct: CardView by lazy { findViewById(R.id.cardProduct) }
    private val cardOrder: CardView by lazy { findViewById(R.id.cardOrder) }
    private val tvCallCenter: TextView by lazy { findViewById(R.id.tvOtherCS) }
    private val tvOtherLogout: TextView by lazy { findViewById(R.id.tvOtherLogout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_seller)

        sharedPref = PreferencesHelper(applicationContext)
        onClick()

    }

    override fun onResume() {
        super.onResume()

        getUserInfo()
    }

    private fun onClick() {
        tvOtherLogout.setOnClickListener {
            sharedPref.logout()
            finish()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        cardProduct.setOnClickListener {
            startActivity(Intent(applicationContext, ProductListActivity::class.java))
        }
        cardOrder.setOnClickListener {
            startActivity(Intent(applicationContext, OrderHistoryActivity::class.java))

        }

    }

    private fun getUserInfo() {
        val phone = sharedPref.getString(PreferencesHelper.PREF_USER_PHONE).toString()
        val password = sharedPref.getString(PreferencesHelper.PREF_USER_PASSWORD).toString()

        ApiClient.instances.loginUser(userType, phone, password, "")
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

                        tvNameProfile.text = user?.name
                        imgProfile.load(Constant.IMAGE_URL_STORE+user?.image)

                        if (user?.status == "1"){
                            tvStatusSeller.text = "Aktif"
                            tvStatusSeller.setTextColor(Color.WHITE)
                        } else {
                            tvStatusSeller.text = "Non aktif"
                            tvStatusSeller.setTextColor(Color.RED)
                        }

                    } else {
                        Log.e(TAG, "onResponse: $response")

                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Log.e(TAG, "onResponse: ${t.message}")

                }

            })
    }
}