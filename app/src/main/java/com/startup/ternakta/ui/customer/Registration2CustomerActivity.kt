package com.startup.ternakta.ui.customer

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Registration2CustomerActivity : AppCompatActivity() {
    private val TAG = "RegistrationCustomer"
    private val userType = "customer"

    private val btnAdd: MaterialButton by lazy { findViewById(R.id.btnAdd) }
    private val inputName: TextInputEditText by lazy { findViewById(R.id.inputName) }
    private val inputPhone: TextInputEditText by lazy { findViewById(R.id.inputPhone) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    private val imgProfile: CircleImageView by lazy { findViewById(R.id.imgProfile) }

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    private var intentProvince = ""
    private var intentCity = ""
    private var intentDistricts = ""
    private var intentAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2_customer)

        intentProvince = intent.getStringExtra("province").toString()
        intentCity = intent.getStringExtra("city").toString()
        intentDistricts = intent.getStringExtra("districts").toString()
        intentAddress = intent.getStringExtra("address").toString()

        onClick()
    }

    private fun onClick() {
        imgProfile.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(512)//Final image size will be less than 512 KB(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        btnAdd.setOnClickListener {
            val name = inputName.text.toString()
            val phone = inputPhone.text.toString()
            val password = inputPassword.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() &&
                intentProvince != "" && intentCity != "" && intentDistricts != "" && intentAddress != "") {

                if (phone.length < 9) {
                    Toast.makeText(
                        applicationContext,
                        "Nomor telepon tidak valid, terlalu pendek",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (password.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Kata sandi harus 6 karakter atau lebih",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    if (partImage != null) {

                        registerUser(intentProvince, intentCity, intentDistricts, intentAddress, name, phone, password,
                            partImage!!
                        )
                    } else {
                        Toast.makeText(applicationContext, "Lengkapi data foto", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(
        province: String,
        city: String,
        districts: String,
        address: String,
        name: String,
        phone: String,
        password: String,
        image: MultipartBody.Part
    ) {

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
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

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