package com.mason.kakao.masonsgallary.view.adapter.holder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.base.BaseVH
import com.mason.kakao.masonsgallary.model.data.ImageListData
import com.mason.kakao.masonsgallary.model.data.Tag
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kakao on 2017. 11. 2..
 */

class ImageVH(
        itemView: View
) : BaseVH<ImageListData>(itemView) {
    private val context = itemView.context
    private val imageTag = itemView.image_tag
    private val imageView = itemView.image_view
    private val textName = itemView.text_name
    private val textPath = itemView.text_path
    private val textDate = itemView.text_date
    private val viewDim = itemView.view_dim

    override fun bindView(data: ImageListData) {
        this.data = data
        val resourceId = when(data.imageData.tag) {
            Tag.Ryan -> R.drawable.tag_ryan
            Tag.Muzi -> R.drawable.tag_muzi
            Tag.Apeach -> R.drawable.tag_apeach
            Tag.Frodo -> R.drawable.tag_frodo
            Tag.Neo -> R.drawable.tag_neo
            Tag.Tube -> R.drawable.tag_tube
            Tag.Jay_G -> R.drawable.tag_jay_g
            Tag.Con -> R.drawable.tag_con
            else -> {
                0
            }
        }
        if(resourceId == 0) {
            imageTag.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            Picasso.with(context)
                    .load(resourceId)
                    .fit()
                    .into(imageTag)
        }
        Picasso.with(context)
                .load(File(data.imageData.path))
                .fit()
                .centerCrop()
                .into(imageView)
        textName.text = data.imageData.name
        textPath.text = data.imageData.path
        val date = Date(data.imageData.date.toLong() * 1000)
        textDate.text = SimpleDateFormat.getInstance().format(date)
        viewDim.visibility =
                if(data.isChecked) View.VISIBLE
                else View.GONE
    }
}