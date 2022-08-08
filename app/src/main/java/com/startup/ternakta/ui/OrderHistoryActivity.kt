package com.startup.ternakta.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.OrderAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryActivity : AppCompatActivity() {
    private val TAG = "ProductList"
    private val userType = ""
    private lateinit var sharedPref: PreferencesHelper

    val rvOrder: RecyclerView by lazy { findViewById(R.id.rvOrder) }
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        sharedPref = PreferencesHelper(applicationContext)

        imgBack.setOnClickListener { finish() }

        getOrder()
    }

    override fun onResume() {
        super.onResume()
        getOrder()
    }

    private fun getOrder() {
        var userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()
        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        var userStoreId = ""

        if (userType == "admin") {
            userId = ""
        } else if (userType == "store") {
            userStoreId = userId
            userId = ""
        }

        ApiClient.instances.showOrder(userId, userStoreId)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val data = responseBody?.data
                    val product = responseBody?.product

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")

                        val adapterOrder = data?.let { OrderAdapter(product!!, it) }
                        rvOrder.layoutManager = LinearLayoutManager(applicationContext)
                        rvOrder.adapter = adapterOrder

                    } else {
                        Log.e(TAG, "onResponse: $response")
                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }

            })
    }
}