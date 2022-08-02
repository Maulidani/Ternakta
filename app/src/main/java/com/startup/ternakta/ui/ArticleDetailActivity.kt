package com.startup.ternakta.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper

class ArticleDetailActivity : AppCompatActivity() {
    private val TAG = "articleDetail"
    private var userType = ""
    private lateinit var sharedPref: PreferencesHelper

    private val tvTitle : TextView by lazy { findViewById(R.id.tvTitleArticle) }
    private val imgArticle : ImageView by lazy { findViewById(R.id.imgArticle) }
    private val tvDesc : TextView by lazy { findViewById(R.id.tvDescription) }
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }

    private var intentImage = ""
    private var intentTitle = ""
    private var intentDesc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        onClick()

        intentImage = intent.getStringExtra("image").toString()
        intentTitle = intent.getStringExtra("title").toString()
        intentDesc = intent.getStringExtra("description").toString()

        imgArticle.load(Constant.IMAGE_URL_ARTICLE+intentImage)
        tvTitle.text = intentTitle
        tvDesc.text = intentDesc
    }

    private fun onClick(){
        imgBack.setOnClickListener { finish() }
    }
}