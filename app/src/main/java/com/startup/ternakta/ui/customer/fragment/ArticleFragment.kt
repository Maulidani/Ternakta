package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ArticleAdapter
import com.startup.ternakta.model.ArticleModel

class ArticleFragment : Fragment() {

    var articleItem = ArrayList<ArticleModel>()
    val rvArticle: RecyclerView by lazy { requireActivity().findViewById(R.id.rvArticle) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image1 =
            "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
        val image2 =
            "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
        val image3 =
            "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"

        articleItem.add(ArticleModel("ayam bahagia", image1, image1, image1))
        articleItem.add(ArticleModel("bibit ayam unggul", "tidak adaji", image2, image2))
        articleItem.add(ArticleModel("penjual ayam", image3, image3, image3))

    }

    override fun onResume() {
        super.onResume()

        val adapter = ArticleAdapter(articleItem)
        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        rvArticle.adapter = adapter
    }
}