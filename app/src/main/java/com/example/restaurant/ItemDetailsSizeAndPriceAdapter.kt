package com.example.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ItemDetailsSizeAndPriceAdapter(private val listOfSizeAndPrice: ArrayList<SizeAndPrice>):BaseAdapter() {

    private var radioButtons:Array<RadioButton?> = Array(listOfSizeAndPrice.size){null}   // الاراى ده يحتوى على جميع ال radioButtons علشان لو داس على اى وحده يلغى الباقى
    private lateinit var listener : SizeListener

    interface SizeListener{
        fun sizeChanged(position: Int)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        try{ listener = parent?.context as SizeListener }
        catch (e:Exception){ throw Exception("You don't implements from SizeListener") }

        var view = convertView
        if(view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_details_size_price, null, false)
            val radioButton: RadioButton = view!!.findViewById(R.id.ItemDetailsSizePrice_RadioButton_Size)
            radioButtons[position] = radioButton
            if(position == 0) radioButton.isChecked = true   // Mark first choice As default
        }

        val radioButton :RadioButton=view.findViewById(R.id.ItemDetailsSizePrice_RadioButton_Size)
        val price :TextView = view.findViewById(R.id.ItemDetailsSizePrice_TextView_Price)
        radioButton.text = listOfSizeAndPrice[position].size()
        price.text = listOfSizeAndPrice[position].price().toString()

        radioButton.setOnCheckedChangeListener { _ , isChecked ->       // لو علم ع واحده يلغى الباقى
            if(isChecked) {
                listener.sizeChanged(position)
                for(i in radioButtons.indices)
                    if(i!=position)
                        radioButtons[i]?.isChecked = false
            }
        }
        return view
    }

    override fun getCount(): Int { return listOfSizeAndPrice.size }

    override fun getItem(position: Int): Any { return 0 }

    override fun getItemId(position: Int): Long { return 0 }

}