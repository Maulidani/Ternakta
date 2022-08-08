package com.startup.ternakta.adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.ApiClient
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.seller.Registration2SellerActivity
import com.startup.ternakta.ui.seller.Registration3SellerActivity
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserAdapter(
    private val type: String,
    private val list: ArrayList<Model.DataModel>,
    private val mListener: IUserRecycler
) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    val _type = type

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgUser: ImageView by lazy { itemView.findViewById(R.id.imgUser) }
        private val nameUser: TextView by lazy { itemView.findViewById(R.id.tvNameUser) }
        private val tvStatusStore: TextView by lazy { itemView.findViewById(R.id.tvStatusStore) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardUser) }

        private lateinit var sharedPref: PreferencesHelper
        var userType = ""
        var userId = ""

        fun bindData(list: Model.DataModel) {
            sharedPref = PreferencesHelper(itemView.context)
            userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
            userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

            var urlImg = ""
            if (_type == "store") {
                urlImg = "${Constant.IMAGE_URL_STORE}${list.image}"

                if (list.status != "1") {
                    tvStatusStore.text = "Non aktif"
                    tvStatusStore.setTextColor(Color.RED)
                } else {
                    tvStatusStore.text = " Aktif"
                    tvStatusStore.setTextColor(Color.BLACK)
                }

            } else {
                tvStatusStore.visibility = View.GONE
                urlImg = "${Constant.IMAGE_URL_CUSTOMER}${list.image}"
            }
            imgUser.load(urlImg)

            nameUser.text = list.name

            item.setOnClickListener {
                if (_type == "store" && list.status != "1") {
                    optionAlert(list, "verification")
                } else {
                    optionAlert(list, "")

                }
            }
        }

        private fun optionAlert(list: Model.DataModel, verification: String) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)

            if (verification == "verification") {

                val options =
                    arrayOf("Lihat alamat", "Lihat profil", "Verifikasi toko ini?")
                builder.setItems(
                    options
                ) { _, which ->
                    when (which) {
                        0 -> {
                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, Registration2SellerActivity::class.java)
                                    .putExtra("action", "view")
                                    .putExtra("type", _type)
                                    .putExtra("id", userId)
                                    .putExtra("name", list.name)
                                    .putExtra("image", list.image)
                                    .putExtra("phone", list.phone)
                                    .putExtra("password", list.password)
                                    .putExtra("province", list.province)
                                    .putExtra("city", list.city)
                                    .putExtra("districts", list.districts)
                                    .putExtra("address", list.address), null
                            )

                        }
                        1 -> {
                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, Registration3SellerActivity::class.java)
                                    .putExtra("action", "view")
                                    .putExtra("type", _type)
                                    .putExtra("id", userId)
                                    .putExtra("name", list.name)
                                    .putExtra("image", list.image)
                                    .putExtra("phone", list.phone)
                                    .putExtra("password", list.password)
                                    .putExtra("province", list.province)
                                    .putExtra("city", list.city)
                                    .putExtra("districts", list.districts)
                                    .putExtra("address", list.address), null
                            )
                        }
                        2 -> verificationAlert(list)
                    }
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {

                val options = arrayOf("Lihat alamat", "Lihat profil")
                builder.setItems(
                    options
                ) { _, which ->
                    when (which) {
                        0 -> {
                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, Registration2SellerActivity::class.java)
                                    .putExtra("action", "view")
                                    .putExtra("type", _type)
                                    .putExtra("id", userId)
                                    .putExtra("name", list.name)
                                    .putExtra("image", list.image)
                                    .putExtra("phone", list.phone)
                                    .putExtra("password", list.password)
                                    .putExtra("province", list.province)
                                    .putExtra("city", list.city)
                                    .putExtra("districts", list.districts)
                                    .putExtra("address", list.address), null
                            )

                        }
                        1 -> {
                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, Registration3SellerActivity::class.java)
                                    .putExtra("action", "view")
                                    .putExtra("type", _type)
                                    .putExtra("id", userId)
                                    .putExtra("name", list.name)
                                    .putExtra("image", list.image)
                                    .putExtra("phone", list.phone)
                                    .putExtra("password", list.password)
                                    .putExtra("province", list.province)
                                    .putExtra("city", list.city)
                                    .putExtra("districts", list.districts)
                                    .putExtra("address", list.address), null
                            )
                        }
                    }
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }

        private fun verificationAlert(result: Model.DataModel) {

            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Verifikasi")
            builder.setMessage("Verifikasi toko ${result.name} ?")

            builder.setPositiveButton("Ya") { _, _ ->
                verification(result.id)
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // cancel
            }
            builder.show()
        }

        private fun verification(id: String) {

            ApiClient.instances.addStatusStoreUser(id, "1")
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message

                        if (response.isSuccessful && message == "Success") {

                            mListener.refreshView(true)
                            notifyDataSetChanged()

                        } else {
                            Toast.makeText(itemView.context, "Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Toast.makeText(
                            itemView.context,
                            t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface IUserRecycler {
        fun refreshView(onUpdate: Boolean)
    }
}
