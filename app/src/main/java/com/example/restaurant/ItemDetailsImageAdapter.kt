package com.example.restaurant

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class ItemDetailsImageAdapter(private val images : ArrayList<String>):PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_details_image_view,null,false)
        val image :ImageView= view.findViewById(R.id.Image)
        image.setImageURI(Uri.parse(images[position]))
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}