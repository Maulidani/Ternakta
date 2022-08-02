package com.startup.ternakta.ui

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {
    private val TAG = "ProductList"
    private val userType = "customer"

    var productAll = ArrayList<Model.DataModel>()
    val rvProduct: RecyclerView by lazy { findViewById(R.id.rvProduct) }

    private val imgBack:ImageView by lazy { findViewById(R.id.imgBack) }
    private val search:EditText by lazy { findViewById(R.id.searchProduct) }
    private val swipeRefresh:SwipeRefreshLayout by lazy { findViewById(R.id.swipeRefreshProduct) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        swipeRefresh.isRefreshing = true

        onClick()
        getProduct("")
    }

    private fun onClick(){

        imgBack.setOnClickListener { finish() }
        search.addTextChangedListener {
            getProduct(it.toString())
        }

        swipeRefresh.setOnRefreshListener {
            getProduct(search.text.toString())
        }
    }

    private fun getProduct(search: String) {
        swipeRefresh.isRefreshing = true

        ApiClient.instances.showProduct("", search)
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

                        val adapterProduct = data?.let { ProductAdapter("", it) }
                        rvProduct.layoutManager = GridLayoutManager(applicationContext, 2);
                        rvProduct.adapter = adapterProduct

                    } else {
                        Log.e(TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

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

        getProduct("")
    }
}