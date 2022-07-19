package com.startup.ternakta.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.adapter.SliderBannerAdapter
import com.startup.ternakta.model.ProductModel
import com.startup.ternakta.model.SliderItem

class ProductListActivity : AppCompatActivity() {

    var productAll = ArrayList<ProductModel>()
    val rvProduct: RecyclerView by lazy { findViewById(R.id.rvProduct) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val image1 = "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
        val image2 = "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
        val image3 = "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"

        productAll.add(ProductModel("product 01","20000",image1))
        productAll.add(ProductModel("product 02","25000",image2))
        productAll.add(ProductModel("product 03","70000",image3))
        productAll.add(ProductModel("product 04","20000",image1))
        productAll.add(ProductModel("product 05","25000",image2))
        productAll.add(ProductModel("product 06","70000",image3))
        productAll.add(ProductModel("product 07","20000",image1))
        productAll.add(ProductModel("product 08","25000",image2))
        productAll.add(ProductModel("product 09","70000",image3))

    }

    override fun onResume() {
        super.onResume()

        val adapterProduct = ProductAdapter("", productAll)
        rvProduct.layoutManager = GridLayoutManager(applicationContext, 2);
        rvProduct.adapter = adapterProduct

    }
}