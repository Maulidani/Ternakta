package com.startup.ternakta.ui.seller

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
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

class AddProductSellerActivity : AppCompatActivity() {
    private val TAG = "AddProductSeller"
    var userId = ""
    var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val imgProduct: ImageView by lazy { findViewById(R.id.imgProduct) }
    private val inputName: TextInputEditText by lazy { findViewById(R.id.inputName) }
    private val inputDescription: TextInputEditText by lazy { findViewById(R.id.inputDescription) }
    private val inputPrice: TextInputEditText by lazy { findViewById(R.id.inputPrice) }
    private val inputPricePromo: TextInputEditText by lazy { findViewById(R.id.inputPricePromo) }
    private val btnAdd: MaterialButton by lazy { findViewById(R.id.btnAddProduct) }

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_seller)

        sharedPref = PreferencesHelper(applicationContext)
        userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()
        userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()

        onClick()
    }

    private fun onClick() {
        imgBack.setOnClickListener { finish() }

        imgProduct.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(512)//Final image size will be less than 512 KB(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        btnAdd.setOnClickListener {

            val name = inputName.text.toString()
            val description = inputDescription.text.toString()
            val price = inputPrice.text.toString().toIntOrNull()
            val pricePromo = inputPricePromo.text.toString().toIntOrNull()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && partImage != null ) {
                addProduct(name,description,price.toString(),pricePromo.toString(),partImage!!)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Lengkapi data",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    private fun addProduct(
        name: String,
        description: String,
        price: String,
        pricePromo: String,
        partImage: MultipartBody.Part
    ) {
        val partId: RequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val partName: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val partDesc: RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPrice: RequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPricePromo: RequestBody = pricePromo.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.instances.addProduct(partId,partName,partPrice,partPricePromo, partDesc,partImage)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")
                        Toast.makeText(applicationContext, "Berhasil menambah produk", Toast.LENGTH_SHORT).show()

                        finish()

                    } else {
                        Log.e(TAG, "onResponse: $response")

                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Log.e(TAG, "onResponse: ${t.message}")

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
                imgProduct.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

                Log.e(TAG, "image format: uri = $fileUri")
                Log.e(TAG, "image format: file path = $image")
                Log.e(TAG, "image format: file absolute path = ${image.absolutePath}")

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())
                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}