package com.startup.ternakta.ui

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class OrderDetailActivity : AppCompatActivity() {
    private val TAG = "orderDetail"
    private var userType = ""
    private lateinit var sharedPref: PreferencesHelper
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }

    private val btnVerifition: MaterialButton by lazy { findViewById(R.id.btnVerifition) }
    private val tvOrderId: TextView by lazy { findViewById(R.id.tvOrderId) }
    private val tvStatusOrder: TextView by lazy { findViewById(R.id.tvStatusOrder) }
    private val imgTrasactionProof: ImageView by lazy { findViewById(R.id.imgTrasactionProof) }
    private val tvProductName: TextView by lazy { findViewById(R.id.tvProductName) }
    private val tvPrice: TextView by lazy { findViewById(R.id.tvPrice) }
    private val tvCountProduct: TextView by lazy { findViewById(R.id.tvCountProduct) }
    private val tvTotalPrice: TextView by lazy { findViewById(R.id.tvTotalPrice) }
    private val tvPhoneStore: TextView by lazy { findViewById(R.id.tvPhoneStore) }

    private var intentOrderId = ""

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        intentOrderId = intent.getStringExtra("order_id").toString()
        sharedPref = PreferencesHelper(applicationContext)
        getOrder()

        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        if (userType == "store") {
            btnVerifition.visibility = View.VISIBLE
        } else {
            btnVerifition.visibility = View.GONE
        }

    }

    private fun init(data: ArrayList<Model.DataModel>?, product: ArrayList<Model.DataModel>?) {

        imgBack.setOnClickListener { finish() }
        imgTrasactionProof.setOnClickListener {
            if (userType == "customer") {
                ImagePicker.with(this)
                    .cropSquare()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

        }

        tvOrderId.text = intentOrderId
        if (data != null) {

            for (i in data) {

                if (intentOrderId == i.id) {
                    imgTrasactionProof.load(Constant.IMAGE_URL_TRANSACTION + i.image_transaction)

                    if (i.status == "1") {
                        tvStatusOrder.text = "Diverifikasi"
                        tvStatusOrder.setTextColor(Color.BLACK)
                    } else {
                        tvStatusOrder.text = "Belum diverifikasi"
                        tvStatusOrder.setTextColor(Color.RED)
                    }

                    tvProductName.text = i.name

                    if (i.price_promo != null) {
                        tvPrice.text = i.price_promo
                    } else {
                        tvPrice.text = i.price
                    }

                    var itemProduct = 0

                    if (product != null) {
                        for (i in product) {

                            if (intentOrderId == i.id) {
                                itemProduct += 1
                            }
                        }
                    }
                    tvCountProduct.text = itemProduct.toString()
                    tvTotalPrice.text =
                        (itemProduct * tvPrice.text.toString().toIntOrNull()!!).toString()
                }
            }

        }

        btnVerifition.setOnClickListener {
            verification()
        }

    }

    private fun verification() {

        ApiClient.instances.addStatusOrder(intentOrderId, "1")
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
                            "Berhasil verifikasi",
                            Toast.LENGTH_SHORT
                        ).show()

                        getOrder()

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

                        init(data, product)

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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
//                    imageView.setImageURI(fileUri)

                val image: File = File(fileUri.path!!)
                imgTrasactionProof.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

                Log.e(TAG, "image format: uri = $fileUri")
                Log.e(TAG, "image format: file path = $image")
                Log.e(TAG, "image format: file absolute path = ${image.absolutePath}")

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())
                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)

                Toast.makeText(applicationContext, "Mengupload bukti transaksi", Toast.LENGTH_SHORT)
                    .show()

                uploadImg()

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun uploadImg() {

        val partId: RequestBody = intentOrderId.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.instances.addTransactionProofOrder(partId, partImage!!)
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
                        Toast.makeText(
                            applicationContext,
                            "Berhasil upload bukti transaksi, tunggu toko untuk melakakukan transaksi anda",
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

}