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
import com.startup.ternakta.ui.ArticleDetailActivity
import com.startup.ternakta.ui.ProductDetailActivity
import com.startup.ternakta.ui.seller.AddArticleActivity
import com.startup.ternakta.ui.seller.AddProductSellerActivity
import com.startup.ternakta.utils.Constant
import com.startup.ternakta.utils.PreferencesHelper
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArticleAdapter(
    private val list: ArrayList<Model.DataModel>,
    private val mListener: IUserRecycler
) :
    RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgArticle: ImageView by lazy { itemView.findViewById(R.id.imgArticle) }
        private val nameArticle: TextView by lazy { itemView.findViewById(R.id.tvName) }
        private val descArticle: TextView by lazy { itemView.findViewById(R.id.tvDescription) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardArticle) }

        private lateinit var sharedPref: PreferencesHelper
        var userType = ""
        var userId = ""

        fun bindData(list: Model.DataModel) {
            sharedPref = PreferencesHelper(itemView.context)
            userType = sharedPref.getString(PreferencesHelper.PREF_USER_TYPE).toString()
            userId = sharedPref.getString(PreferencesHelper.PREF_USER_ID).toString()

//            nameProfileArticle.text = list.title

            val urlArticle = "${Constant.IMAGE_URL_ARTICLE}${list.image}"
            imgArticle.load(urlArticle)

            nameArticle.text = list.title
            descArticle.text = list.description

            item.setOnClickListener {
                if (userType == "store") {
                    optionAlert(list)
                } else {
                    ContextCompat.startActivity(
                        itemView.context,
                        Intent(itemView.context, ArticleDetailActivity::class.java)
                            .putExtra("title", list.title)
                            .putExtra("image", list.image)
                            .putExtra("description", list.description), null
                    )
                }
            }
        }

        private fun optionAlert(list: Model.DataModel) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)

            val options = arrayOf("Lihat detail", "Edit artikel", "Hapus artikel")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                            ContextCompat.startActivity(
                                itemView.context,
                                Intent(itemView.context, ArticleDetailActivity::class.java)
                                    .putExtra("title",list.title)
                                    .putExtra("image",list.image)
                                    .putExtra("description",list.description)
                                , null
                            )

                    }
                    1 -> {
                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, AddArticleActivity::class.java)
                                .putExtra("action", "edit")
                                .putExtra("article_id",list.id)
                                .putExtra("title", list.title)
                                .putExtra("image", list.image)
                                .putExtra("description", list.description)
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
            builder.setMessage("Hapus artikel ${result.title} ?")

            builder.setPositiveButton("Ya") { _, _ ->
                delete(result.id)
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // cancel
            }
            builder.show()
        }

        private fun delete(id: String) {

            ApiClient.instances.deleteArticle(id, userId)
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message

                        if (response.isSuccessful && message == "Success") {
                            Toast.makeText(itemView.context, "Berhasil hapus artikel", Toast.LENGTH_SHORT).show()
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
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
