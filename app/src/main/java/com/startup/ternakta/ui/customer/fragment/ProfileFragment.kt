package com.startup.ternakta.ui.customer.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.OrderHistoryActivity
import com.startup.ternakta.ui.customer.Registration2CustomerActivity
import com.startup.ternakta.ui.customer.RegistrationCustomerActivity
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private val TAG = "Profile"
    private val userType = "customer"
    private lateinit var sharedPref: PreferencesHelper

    private lateinit var tvNameProfile: TextView
    private lateinit var tvPhoneProfile: TextView
    private lateinit var imgProfile: CircleImageView
    private lateinit var imgMore: ImageView
    private lateinit var tvOrder: TextView
    private lateinit var tvAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            sharedPref = PreferencesHelper(requireContext())
        }

        onClick()
        getUserInfo("")
    }

    private fun onClick() {
        if (isAdded) {
            imgMore = requireView().findViewById(R.id.imgMore)
            tvOrder = requireView().findViewById(R.id.tvHistoryOrder)
            tvAddress = requireView().findViewById(R.id.tvAddress)

            imgMore.setOnClickListener {
                getUserInfo("imgMore")
            }

            tvOrder.setOnClickListener {
                getUserInfo("orderHistory")

            }
            tvAddress.setOnClickListener {

                getUserInfo("address")

            }
        }
    }

    override fun onResume() {
        super.onResume()

        getUserInfo("")
    }

    private fun getUserInfo(action: String) {
        if (isAdded) {

            tvNameProfile = requireView().findViewById(R.id.tvNameProfile)
            tvPhoneProfile = requireView().findViewById(R.id.tvPhoneProfile)
            imgProfile = requireView().findViewById(R.id.imgProfile)

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

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(TAG, "onResponse: $responseBody")

                            val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID)

                            when (action) {
                                "imgMore" -> {
                                    startActivity(Intent(requireContext(), Registration2CustomerActivity::class.java)
                                        .putExtra("action","edit")
                                        .putExtra("type","customer")
                                        .putExtra("id",userId)
                                        .putExtra("name",user?.name)
                                        .putExtra("phone",user?.phone)
                                        .putExtra("password",user?.password)
                                        .putExtra("image",user?.image)
                                    )
                                }
                                "address" -> {
                                    startActivity(Intent(requireContext(), RegistrationCustomerActivity::class.java)
                                        .putExtra("action","edit")
                                        .putExtra("type","customer")
                                        .putExtra("id",userId)
                                        .putExtra("name",user?.name)
                                        .putExtra("phone",user?.phone)
                                        .putExtra("password",user?.password)
                                        .putExtra("province",user?.province)
                                        .putExtra("city",user?.city)
                                        .putExtra("districts",user?.districts)
                                        .putExtra("address",user?.address)
                                    )
                                }
                                "orderHistory" -> {
                                    getUserInfo("orderHistory")

                                }
                                else -> {

                                    tvNameProfile.text = user?.name
                                    tvPhoneProfile.text = user?.phone
                                    imgProfile.load(Constant.IMAGE_URL_CUSTOMER + user?.image)

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
    }

}