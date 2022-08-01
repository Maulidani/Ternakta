package com.startup.ternakta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant
import de.hdodenhof.circleimageview.CircleImageView


class ArticleAdapter(
    private val list: ArrayList<Model.DataModel>) :
    RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgArticle: ImageView by lazy { itemView.findViewById(R.id.imgArticle) }
        private val nameArticle: TextView by lazy { itemView.findViewById(R.id.tvName) }
        private val descArticle: TextView by lazy { itemView.findViewById(R.id.tvDescription) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardArticle) }

        fun bindData(list: Model.DataModel) {

//            nameProfileArticle.text = list.title

            val urlArticle = "${Constant.IMAGE_URL_ARTICLE}${list.image}"
            imgArticle.load(urlArticle)

            nameArticle.text = list.title
            descArticle.text = list.description

            item.setOnClickListener {
                Toast.makeText(itemView.context, list.title, Toast.LENGTH_SHORT).show()
            }
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

}
