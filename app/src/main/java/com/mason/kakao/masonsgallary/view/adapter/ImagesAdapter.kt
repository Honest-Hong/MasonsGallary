package com.mason.kakao.masonsgallary.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.model.data.ImageListData
import com.mason.kakao.masonsgallary.view.adapter.holder.ImageVH
import java.util.*

/**
 * Created by kakao on 2017. 11. 1..
 */

class ImagesAdapter(
        private val context : Context,
        private val itemEventListener: ItemEventListener,
        var list : MutableList<ImageListData> = Collections.emptyList()
) : RecyclerView.Adapter<ImageVH>() {

    interface ItemEventListener {
        fun onClick(data : ImageListData)
        fun onLongClick(data : ImageListData)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageVH {
        val holder = ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false))
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            itemEventListener.onClick(list[position])
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            itemEventListener.onLongClick(list[position])
            false
        }
        return holder
    }

    override fun onBindViewHolder(holder: ImageVH?, position: Int) {
        holder?.bindView(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun changeImageData(data : ImageListData) {
        notifyItemChanged(list.indexOf(data))
    }

    fun removeImageData(data : ImageListData) {
        val index = list.indexOf(data)
        list.removeAt(index)
        notifyItemRemoved(index)
    }
}