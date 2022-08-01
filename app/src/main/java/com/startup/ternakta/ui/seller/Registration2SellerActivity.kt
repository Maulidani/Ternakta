package com.startup.ternakta.ui.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.ui.customer.Registration2CustomerActivity

class Registration2SellerActivity : AppCompatActivity() {
    private val TAG = "Regis2Seller"
    private val userType = "store"
    private val btnNext : MaterialButton by lazy { findViewById(R.id.btnAdd) }
    private val inputProvince: TextInputEditText by lazy { findViewById(R.id.inputProvince) }
    private val inputCity: TextInputEditText by lazy { findViewById(R.id.inputCity) }
    private val inputDistricts: TextInputEditText by lazy { findViewById(R.id.inputDistricts) }
    private val inputAddress: TextInputEditText by lazy { findViewById(R.id.inputAddress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2_seller)

        onClick()
    }

    private fun onClick(){

        btnNext.setOnClickListener {
            val province = inputProvince.text.toString()
            val city = inputCity.text.toString()
            val districts = inputDistricts.text.toString()
            val address = inputAddress.text.toString()

            if (province.isNotEmpty() && city.isNotEmpty() && districts.isNotEmpty() && address.isNotEmpty() ) {

                Log.e(TAG, "Next: $province, $city, $districts, $address", )
                startActivity(Intent(applicationContext, Registration3SellerActivity::class.java)
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