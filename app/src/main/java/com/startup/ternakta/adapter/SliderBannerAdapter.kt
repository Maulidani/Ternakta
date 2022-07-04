package com.startup.ternakta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import coil.load
import com.smarteist.autoimageslider.SliderViewAdapter
import com.startup.ternakta.R
import com.startup.ternakta.model.SliderItem


class SliderBannerAdapter(list: ArrayList<SliderItem>) :
    SliderViewAdapter<SliderBannerAdapter.ListViewHolder>() {

    val itemList = list

    inner class ListViewHolder(itemView: View) : ViewHolder(itemView) {

        private val imgAccount: ImageView by lazy { itemView.findViewById(R.id.imgBanner) }

        fun bindData(item: SliderItem) {

            imgAccount.load(item.image)

//            Toast.makeText(itemView.context, item.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.item_slider_banner, parent, false)
        )    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getCount(): Int = itemList.size
}