package com.startup.ternakta.ui.customer.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ArticleAdapter
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleFragment : Fragment(), ArticleAdapter.IUserRecycler {
    private val TAG = "Article"
    private val userType = "customer"

    var articleItem = ArrayList<Model.ArticleModel>()
    lateinit var rvArticle: RecyclerView
    lateinit var search: EditText
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        dataStatic()

        onClick()
        getArticle("")
    }


    private fun onClick(){
        if (isAdded){
            search = requireView().findViewById(R.id.searchArticle)
            swipeRefresh = requireView().findViewById(R.id.swipeRefreshArticle)

            swipeRefresh.isRefreshing = true

            search.addTextChangedListener {
                getArticle(it.toString())
            }

            swipeRefresh.setOnRefreshListener {
                getArticle(search.text.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        onClick()
        getArticle("")
    }

    private fun getArticle(search:String){
        if (isAdded) {
            swipeRefresh.isRefreshing = true

            rvArticle = requireView().findViewById(R.id.rvArticle)

            ApiClient.instances.showArticle("", search)
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

                            val adapter = data?.let { ArticleAdapter( it,this@ArticleFragment) }
                            rvArticle.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
                            rvArticle.adapter = adapter

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

    override fun refreshView(onUpdate: Boolean) {
        //
    }

//    private fun dataStatic(){
//        val image1 =
//            "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"
//        val image2 =
//            "https://img.freepik.com/free-vector/gradient-10-10-background_157027-577.jpg?w=740&t=st=1656926532~exp=1656927132~hmac=01b73444a25dfd30f9ddc8ba70392a6324622580f820e83d9bb8a2dbd4c6af4d"
//        val image3 =
//            "https://img.freepik.com/free-vector/sale-banner-template-background_157027-660.jpg?t=st=1656894181~exp=1656894781~hmac=d6bf1147b5cc78eeab6f4c9733e4a6591d759086618bf862d8171f980310662d&w=996"
//
//        articleItem.add(ArticleModel("ayam bahagia", image1, image1, image1))
//        articleItem.add(ArticleModel("bibit ayam unggul", "tidak adaji", image2, image2))
//        articleItem.add(ArticleModel("penjual ayam", image3, image3, image3))
//    }

}