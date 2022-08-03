package com.startup.ternakta.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {
    private val TAG = "productDetail"
    private var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvNameProduct: TextView by lazy { findViewById(R.id.tvNameProduct) }
    private val tvPriceProduct: TextView by lazy { findViewById(R.id.tvPrice) }
    private val imgProduct: ImageView by lazy { findViewById(R.id.imgProduct) }
    private val tvDesc: TextView by lazy { findViewById(R.id.tvDescription) }
//    private val AddCart: ConstraintLayout by lazy { findViewById(R.id.AddCart) }
    private val orderNow: ConstraintLayout by lazy { findViewById(R.id.OrderNow) }
    private val tvMines: TextView by lazy { findViewById(R.id.tvMines) }
    private val tvPlus: TextView by lazy { findViewById(R.id.tvPlus) }
    private val etCount: EditText by lazy { findViewById(R.id.etCount) }

    private var intentId = ""
    private var intentStoreId = ""
    private var intentImage = ""
    private var intentName = ""
    private var intentPrice = ""
    private var intentPricePromo = ""
    private var intentDesc = ""

    private var partProduct: ArrayList<RequestBody> = arrayListOf()
    var price = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        onClick()

        sharedPref = PreferencesHelper(applicationContext)

        intentId = intent.getStringExtra("id").toString()
        intentStoreId = intent.getStringExtra("store_id").toString()
        intentImage = intent.getStringExtra("image").toString()
        intentName = intent.getStringExtra("name").toString()
        intentPrice = intent.getStringExtra("price").toString()
        intentPricePromo = intent.getStringExtra("price_promo").toString()
        intentDesc = intent.getStringExtra("description").toString()

        imgProduct.load(Constant.IMAGE_URL_PRODUCT + intentImage)
        tvNameProduct.text = intentName
        if (intentPricePromo != "") {
            tvPriceProduct.text = intentPricePromo
            price = intentPricePromo.toIntOrNull().toString()
        } else {
            tvPriceProduct.text = intentPrice
            price = intentPricePromo.toIntOrNull().toString()
        }
        tvDesc.text = intentDesc

        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        if (userType == "customer"){
            orderNow.visibility = View.VISIBLE
            tvMines.visibility = View.VISIBLE
            tvPlus.visibility = View.VISIBLE
            etCount.visibility = View.VISIBLE
        } else {
            orderNow.visibility = View.GONE
            tvMines.visibility = View.GONE
            tvPlus.visibility = View.GONE
            etCount.visibility = View.GONE
        }
    }

    private fun onClick() {
        imgBack.setOnClickListener { finish() }

        var countProduct = 0

        tvMines.setOnClickListener {
            countProduct -= 1
            etCount.setText(countProduct.toString())
        }

        tvPlus.setOnClickListener {
            countProduct += 1
            etCount.setText(countProduct.toString())
        }

//        AddCart.setOnClickListener {
//            addCart()
//        }

        orderNow.setOnClickListener {
            if (countProduct != null) {
                if (countProduct <= 0) {
                    Toast.makeText(
                        applicationContext,
                        "jumlah produk tidak valid",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val partProductId: RequestBody =
                        intentId.toRequestBody("text/plain".toMediaTypeOrNull())

                    for (i in 1..countProduct) {
                        partProduct.add(partProductId)
                    }
                    addOrder(partProduct)
                }
            }
        }

    }

    private fun addCart() {
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        ApiClient.instances.addCart(intentId, userId, intentStoreId)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")
                        Toast.makeText(
                            applicationContext,
                            "Menambahkan ke keranjang",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Log.e(TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun addOrder(partProductList: ArrayList<RequestBody>) {
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        val partUserId: RequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val partUserStoreId: RequestBody =
            intentStoreId.toRequestBody("text/plain".toMediaTypeOrNull())
        val partStatus: RequestBody = "0".toRequestBody("text/plain".toMediaTypeOrNull())

        Log.e(TAG, "addOrder: productId $partProductList")

        ApiClient.instances.addOrder(partUserId, partUserStoreId, partStatus, partProductList)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")
                        Toast.makeText(
                            applicationContext,
                            "Berhasil order, selesaikan pemnbayaran",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    } else {
                        Log.e(TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }
}
