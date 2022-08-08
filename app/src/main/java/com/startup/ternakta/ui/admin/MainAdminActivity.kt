package com.startup.ternakta.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.startup.ternakta.R
import com.startup.ternakta.ui.LoginActivity
import com.startup.ternakta.ui.OrderHistoryActivity
import com.startup.ternakta.ui.ProductListActivity
import com.startup.ternakta.utils.PreferencesHelper

class MainAdminActivity : AppCompatActivity() {
    private val TAG = "MainAdminActivity"
    var userType = "admin"
    private lateinit var sharedPref: PreferencesHelper

    private val logout: ImageView by lazy { findViewById(R.id.imgLogout) }
    private val cardSeller: CardView by lazy { findViewById(R.id.cardSeller) }
    private val cardCustomer: CardView by lazy { findViewById(R.id.cardCustomer) }
    private val cardProduct: CardView by lazy { findViewById(R.id.cardProduct) }
    private val cardOrder: CardView by lazy { findViewById(R.id.cardOrder) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        sharedPref = PreferencesHelper(applicationContext)
        onClick()

    }

    private fun onClick() {

        logout.setOnClickListener {
           deleteAlert()
        }

        cardSeller.setOnClickListener {
            startActivity(Intent(applicationContext, UserStoreListActivity::class.java))
        }
        cardCustomer.setOnClickListener {
            startActivity(Intent(applicationContext, UserCustomerListActivity::class.java))
        }
        cardProduct.setOnClickListener {
            startActivity(Intent(applicationContext, ProductListActivity::class.java))
        }
        cardOrder.setOnClickListener {
            startActivity(Intent(applicationContext, OrderHistoryActivity::class.java))
        }

    }

    private fun deleteAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Keluar")
        builder.setMessage("Yakin untuk keluar ?")

        builder.setPositiveButton("Ya") { _, _ ->
            sharedPref.logout()
            finish()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        builder.setNegativeButton("Tidak") { _, _ ->
            // cancel
        }
        builder.show()
    }
}