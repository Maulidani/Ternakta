package com.startup.ternakta.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper

class ProductDetailActivity : AppCompatActivity() {
    private val TAG = "productDetail"
    private var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val tvNameProduct : TextView by lazy { findViewById(R.id.tvNameProduct) }
    private val tvPriceProduct : TextView by lazy { findViewById(R.id.tvPrice) }
    private val imgProduct : ImageView by lazy { findViewById(R.id.imgProduct) }
    private val tvDesc : TextView by lazy { findViewById(R.id.tvDescription) }
    private val imgBack:ImageView by lazy { findViewById(R.id.imgBack) }
    private val AddCart:ConstraintLayout by lazy { findViewById(R.id.AddCart) }
    private val OrderNow:ConstraintLayout by lazy { findViewById(R.id.OrderNow) }

    private var intentImage = ""
    private var intentName = ""
    private var intentPrice = ""
    private var intentPricePromo = ""
    private var intentDesc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        onClick()

        intentImage = intent.getStringExtra("image").toString()
        intentName = intent.getStringExtra("name").toString()
        intentPrice = intent.getStringExtra("price").toString()
        intentPricePromo = intent.getStringExtra("price_promo").toString()
        intentDesc = intent.getStringExtra("description").toString()

        imgProduct.load(Constant.IMAGE_URL_PRODUCT+intentImage)
        tvNameProduct.text = intentName
        if (intentPricePromo != "") {
            tvPriceProduct.text = intentPricePromo
            tvDesc.text = intentDesc
        } else {
            tvPriceProduct.text = intentPrice
            tvDesc.text = intentDesc
        }

    }

    private fun onClick(){
        imgBack.setOnClickListener { finish() }

        AddCart.setOnClickListener {
            Toast.makeText(applicationContext, "Tambah ke keranjang", Toast.LENGTH_SHORT).show()
        }
        OrderNow.setOnClickListener {
            Toast.makeText(applicationContext, "Beli sekarang", Toast.LENGTH_SHORT).show()

        }
    }
}
