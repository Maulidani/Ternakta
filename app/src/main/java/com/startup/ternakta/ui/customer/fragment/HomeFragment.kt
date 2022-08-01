package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.adapter.SliderBannerAdapter
import com.startup.ternakta.model.SliderItem
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private val TAG = "Home"
    private val userType = "customer"

    var sliderItem = ArrayList<SliderItem>()
    lateinit var rvProductPromo: RecyclerView
    lateinit var rvProductNew: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dataStatic()
        getBanner()
        getProductPromo()
        getProduct()
    }

    private fun getBanner() {
        if (isAdded) {
            val image1 =
                "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
            val image2 =
                "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
            val image3 =
                "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"

            sliderItem.add(SliderItem("name 01", "description 01", image1))
            sliderItem.add(SliderItem("name 02", "description 02", image2))

            val sliderBanner: SliderView = requireActivity().findViewById(R.id.sliderBanner)
            sliderBanner.setSliderAdapter(SliderBannerAdapter(sliderItem))
            sliderBanner.setIndicatorAnimation(IndicatorAnimationType.WORM)
            sliderBanner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            sliderBanner.startAutoCycle()
        }
    }

    private fun getProduct() {
        if (isAdded) {
            rvProductNew = requireView().findViewById(R.id.rvNewAdded)

            ApiClient.instances.showProduct("", "")
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        val data = responseBody?.data

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(TAG, "onResponse: $responseBody")

                            val adapterNewProduct = data?.let { ProductAdapter("home", it) }
                            rvProductNew.layoutManager = GridLayoutManager(requireActivity(), 2)
                            rvProductNew.adapter = adapterNewProduct

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

    private fun getProductPromo() {
        if (isAdded) {
            rvProductPromo = requireView().findViewById(R.id.rvPromo)

            ApiClient.instances.showProduct("", "")
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        val data = responseBody?.data

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(TAG, "onResponse: $responseBody")

                            val dataPromo = arrayListOf<Model.DataModel>()
                            if (data != null) {
                                for (i in data) {

                                    if (i.price_promo != null) {
                                        dataPromo.add(i)
                                    }
                                }
                            }

                            if (dataPromo.isNotEmpty()) {
                                val adapterPromo = ProductAdapter("home", dataPromo)
                                rvProductPromo.layoutManager =
                                    GridLayoutManager(requireActivity(), 2)
                                rvProductPromo.adapter = adapterPromo
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

//    private fun dataStatic() {
//        sliderItem.add(SliderItem("name 03", "description 03", image3))
//
//        productPromo.add(ProductModel("product 01", "20000", image1))
//        productPromo.add(ProductModel("product 02", "25000", image2))
//        productPromo.add(ProductModel("product 03", "70000", image3))
//
//        productNewAdded.add(ProductModel("product 01", "20000", image1))
//        productNewAdded.add(ProductModel("product 02", "25000", image2))
//        productNewAdded.add(ProductModel("product 03", "70000", image3))
//        productNewAdded.add(ProductModel("product 04", "20000", image1))
//        productNewAdded.add(ProductModel("product 05", "25000", image2))
//        productNewAdded.add(ProductModel("product 06", "70000", image3))
//        productNewAdded.add(ProductModel("product 07", "20000", image1))
//        productNewAdded.add(ProductModel("product 08", "25000", image2))
//        productNewAdded.add(ProductModel("product 09", "70000", image3))
//
//    }
}