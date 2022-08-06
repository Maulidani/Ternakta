package com.startup.ternakta.adapter

import android.app.AlertDialog
import android.content.Intent
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
import com.startup.ternakta.ui.OrderDetailActivity
import com.startup.ternakta.ui.ProductDetailActivity
import com.startup.ternakta.ui.seller.AddProductSellerActivity
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductAdapter(
    private val type: String,
    private val list: ArrayList<Model.DataModel>,
    private val mListener: IUserRecycler

) :
    RecyclerView.Adapter<ProductAdapter.ListViewHolder>() {

    val _type = type

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProduct: ImageView by lazy { itemView.findViewById(R.id.imgProduct) }
        private val nameProduct: TextView by lazy { itemView.findViewById(R.id.tvProductName) }
        private val priceProduct: TextView by lazy { itemView.findViewById(R.id.tvProductPrice) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardProduct) }

        private lateinit var sharedPref: PreferencesHelper
        var userType = ""
        var userId = ""

        fun bindData(list: Model.DataModel) {
            sharedPref = PreferencesHelper(itemView.context)
            userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
            userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

            val urlProduct = "${Constant.IMAGE_URL_PRODUCT}${list.image}"
            imgProduct.load(urlProduct)

            nameProduct.text = list.name
            priceProduct.text = list.price
            if (list.price_promo != null) {
                priceProduct.text = list.price_promo
            } else {
                priceProduct.text = list.price
            }

            item.setOnClickListener {
                if (userType == "store") {
                    optionAlert(list)
                } else {
                    if (list.price_promo != null) {

                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, ProductDetailActivity::class.java)
                                .putExtra("id",list.id)
                                .putExtra("store_id",list.user_store_id)
                                .putExtra("name",list.name)
                                .putExtra("image",list.image)
                                .putExtra("price",list.price)
                                .putExtra("price_promo",list.price_promo)
                                .putExtra("description",list.description)
                            , null
                        )
                    } else {

                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, ProductDetailActivity::class.java)
                                .putExtra("id",list.id)
                                .putExtra("store_id",list.user_store_id)
                                .putExtra("name",list.name)
                                .putExtra("image",list.image)
                                .putExtra("price",list.price)
                                .putExtra("price_promo","")
                                .putExtra("description",list.description)
                            , null
                        )
                    }
                }

            }
        }

        private fun optionAlert(list: Model.DataModel) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)

            val options = arrayOf("Lihat detail", "Edit produk", "Hapus produk")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        if (list.price_promo != null) {

                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, ProductDetailActivity::class.java)
                                    .putExtra("id",list.id)
                                    .putExtra("store_id",list.user_store_id)
                                    .putExtra("name",list.name)
                                    .putExtra("image",list.image)
                                    .putExtra("price",list.price)
                                    .putExtra("price_promo",list.price_promo)
                                    .putExtra("description",list.description)
                                , null
                            )
                        } else {

                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, ProductDetailActivity::class.java)
                                    .putExtra("id",list.id)
                                    .putExtra("store_id",list.user_store_id)
                                    .putExtra("name",list.name)
                                    .putExtra("image",list.image)
                                    .putExtra("price",list.price)
                                    .putExtra("price_promo","")
                                    .putExtra("description",list.description)
                                , null
                            )
                        }
                    }
                    1 -> {
                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, AddProductSellerActivity::class.java)
                                .putExtra("action", "edit")
                                .putExtra("product_id",list.id)
                                .putExtra("image", list.image)
                                .putExtra("name", list.name)
                                .putExtra("description", list.description)
                                .putExtra("price", list.price)
                                .putExtra("price_promo", list.price_promo)
                            ,null,
                        )
                    }
                    2 -> deleteAlert(list)
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        private fun deleteAlert(result: Model.DataModel) {

            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Hapus")
            builder.setMessage("Hapus produk ${result.name} ?")

            builder.setPositiveButton("Ya") { _, _ ->
                delete(result.id)
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // cancel
            }
            builder.show()
        }

        private fun delete(id: String) {

            ApiClient.instances.deleteProduct(id, userId)
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return if (_type == "home") {

            if (list.size <= 6) {
                list.size
            } else {
                6
            }

        } else {
            list.size
        }
    }

    interface IUserRecycler {
        fun refreshView(onUpdate: Boolean)
    }
}
