package com.startup.ternakta.ui.seller

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private val cardArticle: CardView by lazy { findViewById(R.id.cardArticle) }
    private val tvProfile: TextView by lazy { findViewById(R.id.tvProfile) }
    private val tvAddress: TextView by lazy { findViewById(R.id.tvAddress) }
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

        getUserInfo("")
    }

    private fun onClick() {
        tvOtherLogout.setOnClickListener {
          deleteAlert()
        }
        tvCallCenter.setOnClickListener {
            sendMessage("Halo... ")
        }
        tvAddress.setOnClickListener {
            getUserInfo("address")
        }
        tvProfile.setOnClickListener {
            getUserInfo("profile")
        }


    }

    private fun getUserInfo(action: String) {
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
                        imgProfile.load(Constant.IMAGE_URL_STORE + user?.image)

                        if (user?.status == "1") {
                            tvStatusSeller.text = "Aktif"
                            tvStatusSeller.setTextColor(Color.WHITE)

                            cardProduct.setOnClickListener {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        ProductListActivity::class.java
                                    )
                                )
                            }
                            cardOrder.setOnClickListener {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        OrderHistoryActivity::class.java
                                    )
                                )
                            }
                            cardArticle.setOnClickListener {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        ArticleListActivity::class.java
                                    )
                                )
                            }

                        } else {
                            tvStatusSeller.text = "Non aktif"
                            tvStatusSeller.setTextColor(Color.RED)
                            cardProduct.setOnClickListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Anda belum bisa menambahkan produk, akun anda belum diverifikasi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            cardOrder.setOnClickListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Anda belum bisa menambahkan produk, akun anda belum diverifikasi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            cardArticle.setOnClickListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Anda belum bisa menambahkan produk, akun anda belum diverifikasi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                        if (action != ""){
                            getUser(action,user)
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

    private fun getUser(action: String, user: Model.UserModel?) {
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        when(action) {
            "profile" -> {
                startActivity(
                    Intent(
                        applicationContext,
                        Registration3SellerActivity::class.java
                    )
                        .putExtra("action", "edit")
                        .putExtra("type", "store")
                        .putExtra("id", userId)
                        .putExtra("name", user?.name)
                        .putExtra("image", user?.image)
                        .putExtra("phone", user?.phone)
                        .putExtra("password", user?.password)
                        .putExtra("province", user?.province)
                        .putExtra("city", user?.city)
                        .putExtra("districts", user?.districts)
                        .putExtra("address", user?.address)
                )
            }
            "address" -> {
                startActivity(
                    Intent(
                        applicationContext,
                        Registration2SellerActivity::class.java
                    )
                        .putExtra("action", "edit")
                        .putExtra("type", "store")
                        .putExtra("id", userId)
                        .putExtra("name", user?.name)
                        .putExtra("image", user?.image)
                        .putExtra("phone", user?.phone)
                        .putExtra("password", user?.password)
                        .putExtra("province", user?.province)
                        .putExtra("city", user?.city)
                        .putExtra("districts", user?.districts)
                        .putExtra("address", user?.address)
                )
            }
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

    private fun sendMessage(message:String){

            // Creating intent with action send
            val intent = Intent(Intent.ACTION_SEND)

            // Setting Intent type
            intent.type = "text/plain"

            // Setting whatsapp package name
            intent.setPackage("com.whatsapp")

            val phoneNumberWithCountryCode = Constant.PHONE_ADMIN

            // Starting Whatsapp
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        String.format(
                            "https://api.whatsapp.com/send?phone=%s&text=%s",
                            phoneNumberWithCountryCode,
                            message
                        )
                    )
                )
            )
    }
}