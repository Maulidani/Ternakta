package com.startup.ternakta.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.seller.AddProductSellerActivity
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity(), ProductAdapter.IUserRecycler {
    private val TAG = "ProductList"
    private val userType = ""
    private lateinit var sharedPref: PreferencesHelper

    val rvProduct: RecyclerView by lazy { findViewById(R.id.rvProduct) }

    private val imgBack:ImageView by lazy { findViewById(R.id.imgBack) }
    private val search:EditText by lazy { findViewById(R.id.searchProduct) }
    private val swipeRefresh:SwipeRefreshLayout by lazy { findViewById(R.id.swipeRefreshProduct) }
    private val fabAddProduct:FloatingActionButton by lazy { findViewById(R.id.fabAddProduct) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        sharedPref = PreferencesHelper(applicationContext)

        swipeRefresh.isRefreshing = true

        onClick()

    }

    private fun onClick(){
        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        imgBack.setOnClickListener { finish() }
        search.addTextChangedListener {
            if (userType == "store") {
                getProduct(userId,it.toString())
            } else {
                getProduct("",it.toString())
            }
        }

        swipeRefresh.setOnRefreshListener {
            if (userType == "store") {
                getProduct(userId,search.text.toString())
            } else {
                getProduct("",search.text.toString())
            }
        }

        fabAddProduct.setOnClickListener {
            startActivity(Intent(applicationContext,AddProductSellerActivity::class.java))
        }
    }

    private fun getProduct(userId: String,search: String) {
        swipeRefresh.isRefreshing = true

        ApiClient.instances.showProduct(userId, search)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val data = responseBody?.data

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")

                        val adapterProduct = data?.let { ProductAdapter("", it, this@ProductListActivity) }
                        rvProduct.layoutManager = GridLayoutManager(applicationContext, 2);
                        rvProduct.adapter = adapterProduct

                    } else {
                        Log.e(TAG, "onResponse: $response")
                    }
                    swipeRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    swipeRefresh.isRefreshing = false

                }
            })
    }

    override fun onResume() {
        super.onResume()

        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        if (userType == "store") {
            fabAddProduct.visibility = View.VISIBLE
            getProduct(userId,"")
        } else {
            fabAddProduct.visibility = View.GONE
            getProduct("","")
        }
    }

    override fun refreshView(onUpdate: Boolean) {

        if (onUpdate) {

            val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
            val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

            if (userType == "store") {
                fabAddProduct.visibility = View.VISIBLE
                getProduct(userId, "")
                finish()
            } else {
                fabAddProduct.visibility = View.GONE
                getProduct("", "")
            }
        }
    }
}