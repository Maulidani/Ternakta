package com.startup.ternakta.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.startup.ternakta.R
import com.startup.ternakta.network.Model
import com.startup.ternakta.ui.OrderDetailActivity
import com.startup.ternakta.ui.ProductDetailActivity


class OrderAdapter(
    private val list: ArrayList<Model.DataModel>
) :
    RecyclerView.Adapter<OrderAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val id: TextView by lazy { itemView.findViewById(R.id.tvOrderId) }
        private val status: TextView by lazy { itemView.findViewById(R.id.tvStatusOrder) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardOrder) }

        fun bindData(list: Model.DataModel) {


            id.text = list.id
            if (list.status == "1") {
                status.text = "Diverifikasi"
                status.setTextColor(Color.BLACK)
            } else {
                status.text = "Belum diverifikasi"
                status.setTextColor(Color.RED)
            }

            item.setOnClickListener {
                ContextCompat.startActivity(
                    itemView.context,
                    Intent(itemView.context, OrderDetailActivity::class.java)
                        .putExtra("id",list.id)
                    , null
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}
