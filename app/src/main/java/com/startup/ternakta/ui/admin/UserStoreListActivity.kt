package com.startup.ternakta.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.ternakta.R
import com.startup.ternakta.adapter.UserAdapter
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserStoreListActivity : AppCompatActivity(), UserAdapter.IUserRecycler {
    private val TAG = "UserStoreList"
    val rvUserStore: RecyclerView by lazy { findViewById(R.id.rvUserStore) }
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_store_list)

        onClick()
    }

    private fun onClick(){
        imgBack.setOnClickListener { finish() }
    }

    private fun getUserStore(){

        ApiClient.instances.showUser("store", "")
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

                        val adapterUser = data?.let { UserAdapter( "store",it,this@UserStoreListActivity) }
                        rvUserStore.layoutManager = LinearLayoutManager(applicationContext);
                        rvUserStore.adapter = adapterUser

                    } else {
                        Log.e(TAG, "onResponse: $response")
                    }
                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
            })
    }

    override fun onResume() {
        super.onResume()

        getUserStore()
    }

    override fun refreshView(onUpdate: Boolean) {
        getUserStore()
    }
}