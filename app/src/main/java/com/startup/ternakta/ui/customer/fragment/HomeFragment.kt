package com.startup.ternakta.ui.customer.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.startup.ternakta.ui.ProductListActivity
import com.startup.ternakta.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), ProductAdapter.IUserRecycler {
    private val TAG = "Home"
    private val userType = "customer"

    var sliderItem = ArrayList<SliderItem>()
    lateinit var rvProductPromo: RecyclerView
    lateinit var rvProductNew: RecyclerView
    lateinit var appBar: ConstraintLayout
    lateinit var tvShowAllNewAdded: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()

        //dataStatic()
        getBanner()
        getProductPromo()
        getProduct()
    }

    override fun onResume() {
        super.onResume()

        onClick()

        //dataStatic()
        getBanner()
        getProductPromo()
        getProduct()
    }

    private fun onClick() {

        if (isAdded) {
            appBar = requireView().findViewById(R.id.appBar)
            tvShowAllNewAdded = requireView().findViewById(R.id.tvShowAllNewAdded)

            appBar.setOnClickListener {
                startActivity(Intent(requireContext(), ProductListActivity::class.java))
            }
            tvShowAllNewAdded.setOnClickListener {
                startActivity(Intent(requireContext(), ProductListActivity::class.java))
            }


        }

    }

    private fun getBanner() {
        if (isAdded) {
            val image1 = "${Constant.IMAGE_URL_ARTICLE}banner_1.png"
            val image2 = "${Constant.IMAGE_URL_ARTICLE}banner_2.png"
            val image3 = "${Constant.IMAGE_URL_ARTICLE}banner_3.png"

            sliderItem.add(SliderItem("banner 01", "description 01", image1))
            sliderItem.add(SliderItem("banner 02", "description 02", image2))
            sliderItem.add(SliderItem("banner 03", "description 03", image3))

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

                            val adapterNewProduct =
                                data?.let { ProductAdapter("home", it, this@HomeFragment) }
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
                                val adapterPromo =
                                    ProductAdapter("home_promo", dataPromo, this@HomeFragment)
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

    override fun refreshView(onUpdate: Boolean) {
        //
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