package com.startup.ternakta.ui.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.startup.ternakta.R
import com.startup.ternakta.adapter.ArticleAdapter
import com.startup.ternakta.adapter.ProductAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleListActivity : AppCompatActivity(), ArticleAdapter.IUserRecycler {
    private val TAG = "ArticleList"
    private val userType = ""
    private lateinit var sharedPref: PreferencesHelper

    val rvArticle: RecyclerView by lazy { findViewById(R.id.rvArticle) }

    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val search: EditText by lazy { findViewById(R.id.searchArticle) }
    private val swipeRefresh: SwipeRefreshLayout by lazy { findViewById(R.id.swipeRefreshProduct) }
    private val fabAddArticle: FloatingActionButton by lazy { findViewById(R.id.fabAddArticle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        sharedPref = PreferencesHelper(applicationContext)

        swipeRefresh.isRefreshing = true

        onClick()
    }
    private fun onClick(){
        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        imgBack.setOnClickListener { finish() }
        search.addTextChangedListener {
            if (userType == "store") {
                getArticle(userId,it.toString())
            } else {
                getArticle("",it.toString())
            }
        }

        swipeRefresh.setOnRefreshListener {
            if (userType == "store") {
                getArticle(userId,search.text.toString())
            } else {
                getArticle("",search.text.toString())
            }
        }

        fabAddArticle.setOnClickListener {
            startActivity(Intent(applicationContext,AddArticleActivity::class.java))
        }
    }

    private fun getArticle(userId: String,search: String) {
        swipeRefresh.isRefreshing = true

        ApiClient.instances.showArticle(userId, search)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val data = responseBody?.data

                    if (response.isSuccessful && message == "Success") {
                        Log.e(TAG, "onResponse: $responseBody")

                        val adapterProduct = data?.let { ArticleAdapter( it, this@ArticleListActivity) }
                        rvArticle.layoutManager = LinearLayoutManager(applicationContext);
                        rvArticle.adapter = adapterProduct

                    } else {
                        Log.e(TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                    swipeRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    swipeRefresh.isRefreshing = false

                }
            })
    }

    override fun onResume() {
        super.onResume()

        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        if (userType == "store") {
            fabAddArticle.visibility = View.VISIBLE
            getArticle(userId,"")
        } else {
            fabAddArticle.visibility = View.GONE
            getArticle("","")
        }
    }

    override fun refreshView(onUpdate: Boolean) {

        val userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
        val userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

        if (userType == "store") {
            fabAddArticle.visibility = View.VISIBLE
            getArticle(userId,"")
        } else {
            fabAddArticle.visibility = View.GONE
            getArticle("","")
        }

    }
}