package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.adapter.SliderBannerAdapter
import com.startup.ternakta.model.ProductModel
import com.startup.ternakta.model.SliderItem

class HomeFragment : Fragment() {

    var sliderItem = ArrayList<SliderItem>()
    var productPromo = ArrayList<ProductModel>()
    var productNewAdded = ArrayList<ProductModel>()
    val rvProductPromo: RecyclerView by lazy { requireActivity().findViewById(R.id.rvPromo) }
    val rvProductNew: RecyclerView by lazy { requireActivity().findViewById(R.id.rvNewAdded) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image1 = "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
        val image2 = "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
        val image3 = "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"

        sliderItem.add( SliderItem("name 01","description 01",image1))
        sliderItem.add( SliderItem("name 02","description 02",image2))
        sliderItem.add( SliderItem("name 03","description 03",image3))

        productPromo.add(ProductModel("product 01","20000",image1))
        productPromo.add(ProductModel("product 02","25000",image2))
        productPromo.add(ProductModel("product 03","70000",image3))

        productNewAdded.add(ProductModel("product 01","20000",image1))
        productNewAdded.add(ProductModel("product 02","25000",image2))
        productNewAdded.add(ProductModel("product 03","70000",image3))
        productNewAdded.add(ProductModel("product 04","20000",image1))
        productNewAdded.add(ProductModel("product 05","25000",image2))
        productNewAdded.add(ProductModel("product 06","70000",image3))
        productNewAdded.add(ProductModel("product 07","20000",image1))
        productNewAdded.add(ProductModel("product 08","25000",image2))
        productNewAdded.add(ProductModel("product 09","70000",image3))

    }

    override fun onResume() {
        super.onResume()

        val sliderBanner : SliderView = requireActivity().findViewById(R.id.sliderBanner)
        sliderBanner.setSliderAdapter(SliderBannerAdapter(sliderItem))
        sliderBanner.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderBanner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderBanner.startAutoCycle();

        val adapterPromo = ProductAdapter("home", productPromo)
        rvProductPromo.layoutManager = GridLayoutManager(requireContext(), 2);
        rvProductPromo.adapter = adapterPromo

        val adapterNewProduct = ProductAdapter("home", productNewAdded)
        rvProductNew.layoutManager = GridLayoutManager(requireContext(), 2);
        rvProductNew.adapter = adapterNewProduct
    }

}