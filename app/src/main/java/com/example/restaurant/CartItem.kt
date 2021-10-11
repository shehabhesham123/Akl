package com.example.restaurant

import android.util.Log

class CartItem{

    private var menuItem :MenuItem? = null
    private var sizePosition: Int? = null
    private var quantity: Int? = null

    constructor(menuItem: MenuItem?, sizePosition: Int?, quantity: Int?) {
        var flag :String? = null
        if(menuItem != null) this.menuItem = menuItem
        else flag = "You have entered false menu item info. ):"
        if(checkSizePosition(sizePosition,menuItem)) this.sizePosition = sizePosition
        else flag = "You have entered false position size info. ):"
        if(checkQuantity(quantity,menuItem)) this.quantity = quantity
        else flag = "You have entered false quantity info. ):"

        if(flag!= null)
            throw Exception(flag)
    }

    fun menuItem():MenuItem{
        if(this.menuItem == null)
            throw Exception("It is likely that data load error has occurred ):")
        return menuItem!!
    }

    fun sizePosition():Int{
        if(!checkSizePosition(this.sizePosition,this.menuItem))
            throw Exception("It is likely that data load error has occurred ):")
        return this.sizePosition!!
    }

    fun quantity():Int {
        if(!checkQuantity(this.quantity,this.menuItem))
            throw Exception("It is likely that data load error has occurred ):")
        return this.quantity!!
    }

    fun menuItem(menuItem: MenuItem?){
        if(menuItem == null)
            throw Exception("You have entered false menu item Info. ):")
        this.menuItem = menuItem
    }

    fun sizePosition(sizePosition: Int?){
        if(!checkSizePosition(sizePosition,this.menuItem))
            throw Exception("You have entered false position size Info. ):")
        this.sizePosition = sizePosition
    }

    fun quantity(quantity : Int?){
        if(!checkQuantity(quantity,this.menuItem))
            throw Exception("You have entered false quantity Info. ):")
        this.quantity = quantity
    }

    private fun checkSizePosition(positionSize: Int?, menuItem: MenuItem?):Boolean{
        if(positionSize == null || menuItem == null) return false
        val item = when(menuItem.itemType()){
            FOOD_CODE -> menuItem.food()
            DRINK_CODE -> menuItem.drink()
            else -> menuItem.dessert()
        }
        if(positionSize < 0 || positionSize >= item.sizes().size) return false

        return true
    }

    private fun checkQuantity(quantity: Int?, menuItem: MenuItem?):Boolean{
        if(quantity == null || menuItem == null) return false
        val item = when(menuItem.itemType()){
            FOOD_CODE -> menuItem.food()
            DRINK_CODE -> menuItem.drink()
            else -> menuItem.dessert()
        }
        if(item.maxQuantity() != null && ( quantity < 0 || quantity > item.maxQuantity()!!)) return false

        return true
    }

}