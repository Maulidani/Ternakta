package com.startup.ternakta.ui.customer

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.Constant.setShowProgress
import com.startup.ternakta.utils.PreferencesHelper
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Registration2CustomerActivity : AppCompatActivity() {
    private val TAG = "Registration2Customer"
    var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val btnAdd: MaterialButton by lazy { findViewById(R.id.btnAdd) }
    private val inputName: TextInputEditText by lazy { findViewById(R.id.inputName) }
    private val inputPhone: TextInputEditText by lazy { findViewById(R.id.inputPhone) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    private val imgProfile: CircleImageView by lazy { findViewById(R.id.imgProfile) }

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null
    private var imgNewSource = false

    private var intentProvince = ""
    private var intentCity = ""
    private var intentDistricts = ""
    private var intentAddress = ""
    private var intentImage = ""

    private var intentAction = ""
    private var intentType = ""
    private var intentId = ""
    private var intentName = ""
    private var intentPhone = ""
    private var intentPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2_customer)

        sharedPref = PreferencesHelper(applicationContext)
        userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()

        intentAction = intent.getStringExtra("action").toString()
        intentType = intent.getStringExtra("type").toString()
        intentId = intent.getStringExtra("id").toString()
        intentName = intent.getStringExtra("name").toString()
        intentPhone = intent.getStringExtra("phone").toString()
        intentPassword = intent.getStringExtra("password").toString()

        intentProvince = intent.getStringExtra("province").toString()
        intentCity = intent.getStringExtra("city").toString()
        intentDistricts = intent.getStringExtra("districts").toString()
        intentAddress = intent.getStringExtra("address").toString()
        intentImage = intent.getStringExtra("image").toString()

        if (intentAction == "edit") {
            btnAdd.text = "Edit akun"
            inputName.setText(intentName)
            inputPhone.setText(intentPhone)
            inputPassword.setText(intentPassword)
            if (intentType == "customer") {
                imgProfile.load(Constant.IMAGE_URL_CUSTOMER + intentImage)
            } else if (intentType == "store") {
                imgProfile.load(Constant.IMAGE_URL_STORE + intentImage)
            }
        }

        onClick()
    }

    private fun onClick() {

        imgBack.setOnClickListener { finish() }

        imgProfile.setOnClickListener {
            if (userType == "admin") {
                //
            } else {
                ImagePicker.with(this)
                    .cropSquare()
                    .compress(512)//Final image size will be less than 512 KB(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
        }

        btnAdd.setOnClickListener {
            val name = inputName.text.toString()
            val phone = inputPhone.text.toString()
            val password = inputPassword.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() &&
                intentProvince != "" && intentCity != "" && intentDistricts != "" && intentAddress != ""
            ) {

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
                    if (intentAction == "edit") {

                        if (intentName == inputName.text.toString() &&
                            intentPhone == inputPhone.text.toString() &&
                            intentPassword == inputPassword.text.toString() &&
                            !imgNewSource
                        ) {
                            Toast.makeText(
                                applicationContext,
                                "Tidak ada data yang berubah",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            getUserInfo()
                        }

                    } else {

                        if (partImage != null) {
                            registerUser(
                                intentProvince,
                                intentCity,
                                intentDistricts,
                                intentAddress,
                                name,
                                phone,
                                password,
                            )

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Lengkapi data foto",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getUserInfo() {

        val phone = sharedPref.getString(PreferencesHelper.PREF_USER_PHONE).toString()
        val password = sharedPref.getString(PreferencesHelper.PREF_USER_PASSWORD).toString()

        ApiClient.instances.loginUser(intentType, phone, password, "")
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

                        if (user != null) {

                            if (imgNewSource) {
                                editUser(
                                    user.id,
                                    user.province,
                                    user.city,
                                    user.districts,
                                    user.address,
                                    inputName.text.toString(),
                                    inputPhone.text.toString(),
                                    inputPassword.text.toString(),
                                    partImage!!
                                )
                            } else {
                                editUserWithoutImage(
                                    user.id,
                                    user.province,
                                    user.city,
                                    user.districts,
                                    user.address,
                                    inputName.text.toString(),
                                    inputPhone.text.toString(),
                                    inputPassword.text.toString()
                                )
                            }
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

    private fun editUser(
        userId: String,
        province: String,
        city: String,
        districts: String,
        address: String,
        name: String,
        phone: String,
        password: String,
        partImage: MultipartBody.Part
    ) {
        val partId: RequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val partProvince: RequestBody = province.toRequestBody("text/plain".toMediaTypeOrNull())
        val partCity: RequestBody = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val partDistricts: RequestBody = districts.toRequestBody("text/plain".toMediaTypeOrNull())
        val partAddress: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val partName: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPhone: RequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPassword: RequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val partType: RequestBody = userType.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.instances.editUser(
            partId,
            partType,
            partName,
            partImage,
            partPhone,
            partPassword,
            partProvince,
            partCity,
            partDistricts,
            partAddress,
        ).enqueue(object : Callback<Model.ResponseModel> {
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
                        "Berhasil edit akun",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (user != null) {

                        sharedPref.put(PreferencesHelper.PREF_USER_PHONE, user.phone)
                        sharedPref.put(PreferencesHelper.PREF_USER_PASSWORD, user.password)

                        finish()
                    }

                } else {
                    Log.e(TAG, "onResponse: $response")
                    Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                }
                btnAdd.setShowProgress(false)

            }

            override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                btnAdd.setShowProgress(false)
            }

        })

    }

    private fun editUserWithoutImage(
        userId: String,
        province: String,
        city: String,
        districts: String,
        address: String,
        name: String,
        phone: String,
        password: String,
    ) {
        ApiClient.instances.editWithoutImgUser(
            userId,
            userType,
            name,
            phone,
            password,
            province,
            city,
            districts,
            address,
        ).enqueue(object : Callback<Model.ResponseModel> {
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
                        "Berhasil edit akun",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (user != null) {
                        sharedPref.put(PreferencesHelper.PREF_USER_PHONE, user.phone)
                        sharedPref.put(PreferencesHelper.PREF_USER_PASSWORD, user.password)

                        finish()
                    }

                } else {
                    Log.e(TAG, "onResponse: $response")
                    Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                }
                btnAdd.setShowProgress(false)

            }

            override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                btnAdd.setShowProgress(false)
            }

        })

    }

    private fun registerUser(
        province: String,
        city: String,
        districts: String,
        address: String,
        name: String,
        phone: String,
        password: String,
    ) {
        btnAdd.setShowProgress(true)

        val partProvince: RequestBody = province.toRequestBody("text/plain".toMediaTypeOrNull())
        val partCity: RequestBody = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val partDistricts: RequestBody = districts.toRequestBody("text/plain".toMediaTypeOrNull())
        val partAddress: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val partName: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPhone: RequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPassword: RequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val partType: RequestBody = "customer".toRequestBody("text/plain".toMediaTypeOrNull())
        val partNull: RequestBody = "0".toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.instances.addUser(
            partType,
            businessPermission = partNull,
            partName,
            partImage!!,
            partPhone,
            partPassword,
            partProvince,
            partCity,
            partDistricts,
            partAddress,
            status = partNull
        ).enqueue(object : Callback<Model.ResponseModel> {
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
                        "Berhasil membuat akun, silahkan masuk",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } else {
                    Log.e(TAG, "onResponse: $response")
                    Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()

                }
                btnAdd.setShowProgress(false)

            }

            override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {

                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                btnAdd.setShowProgress(false)
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
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

                Log.e(TAG, "image format: uri = $fileUri")
                Log.e(TAG, "image format: file path = $image")
                Log.e(TAG, "image format: file absolute path = ${image.absolutePath}")

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())
                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)
                imgNewSource = true

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

}