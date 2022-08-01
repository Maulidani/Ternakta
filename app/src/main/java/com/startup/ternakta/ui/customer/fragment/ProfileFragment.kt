package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) { sharedPref = PreferencesHelper(requireContext()) }

        getUserInfo()
    }

    private fun getUserInfo(){
        if (isAdded) {

            tvNameProfile = requireView().findViewById(R.id.tvNameProfile)
            tvPhoneProfile = requireView().findViewById(R.id.tvPhoneProfile)
            imgProfile = requireView().findViewById(R.id.imgProfile)

            val phone = sharedPref.getString(PreferencesHelper.PREF_USER_PHONE).toString()
            val password = sharedPref.getString(PreferencesHelper.PREF_USER_PASSWORD).toString()

            ApiClient.instances.loginUser(userType, phone,password,"")
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

                            tvNameProfile.text = user?.name
                            tvPhoneProfile.text = user?.phone
                            imgProfile.load(Constant.IMAGE_URL_CUSTOMER+user?.image)

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