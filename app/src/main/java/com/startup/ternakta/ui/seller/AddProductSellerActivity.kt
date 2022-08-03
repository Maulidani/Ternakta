package com.startup.ternakta.ui.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R

class AddProductSellerActivity : AppCompatActivity() {

    private val imgBack:ImageView by lazy { findViewById(R.id.imgBack) }
    private val imgProduct : ImageView by lazy { findViewById(R.id.imgProduct) }
    private val inputName : TextInputEditText by lazy { findViewById(R.id.inputName) }
    private val inputDistricts : TextInputEditText by lazy { findViewById(R.id.inputDistricts) }
    private val inputPrice : TextInputEditText by lazy { findViewById(R.id.inputPrice) }
    private val inputPricePromo : TextInputEditText by lazy { findViewById(R.id.inputPricePromo) }
    private val btnAdd : MaterialButton by lazy { findViewById(R.id.btnAddProduct) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_seller)

        onClick()
    }

    private fun onClick(){


    }
}