package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.startup.ternakta.R
import com.startup.ternakta.adapter.CartShopAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {
    private val TAG = "Cart"
    private val userType = "customer"
    private lateinit var sharedPref: PreferencesHelper
    private lateinit var tvMyAddress: TextView
    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var rvCart: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            sharedPref = PreferencesHelper(requireContext())
            swipeRefresh = requireView().findViewById(R.id.swipeRefreshCart)

            swipeRefresh.isRefreshing = true

            swipeRefresh.setOnRefreshListener {
                getCart()
            }

            //dataStatic()
            getUserInfo()
            getCart()


        }
    }

    override fun onResume() {
        super.onResume()

        //dataStatic()
        getUserInfo()
        getCart()
    }


    private fun getCart() {
        if (isAdded) {
            swipeRefresh.isRefreshing = true

            rvCart = requireView().findViewById(R.id.rvCart)

            val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

            ApiClient.instances.showCart(userId, "")
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        val data = responseBody?.data
                        val dataProduct = responseBody?.product

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(TAG, "onResponse: $responseBody")

                            val adapter = dataProduct?.let {
                                data?.let { it1 ->
                                    CartShopAdapter(
                                        it,
                                        it1
                                    )
                                }
                            }
                            rvCart.layoutManager =
                                LinearLayoutManager(requireActivity().applicationContext)
                            rvCart.adapter = adapter

                        } else {
                            Log.e(TAG, "onResponse: $response")

                        }
                        swipeRefresh.isRefreshing = false

                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(TAG, "onResponse: ${t.message}")
                        swipeRefresh.isRefreshing = false

                    }

                })
        }
    }

    private fun getUserInfo() {
        if (isAdded) {
            tvMyAddress = requireView().findViewById(R.id.tvMyPlace)

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

                            tvMyAddress.text = user?.address

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

//    private fun dataStatic(){
//        val image1 =
//            "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
//        val image2 =
//            "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
//        val image3 =
//            "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"
//
//        cartShop.add(CartShopModel("toko 1",image1,"3"))
//        cartShop.add(CartShopModel("toko 2",  image2,"2"))
//        cartShop.add(CartShopModel("toko 3", image3, "1"))
//    }


}