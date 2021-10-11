package com.example.restaurant

import android.content.Context
import android.util.Log
import kotlin.math.E

class Client : Person{

    private var db : AccessDatabase? = null
    private var address : String? = null
    private var visaNumber:String? = null

    constructor( username: String?, address: String?, phoneNumber: String?, visaNumber: String?):super(username,phoneNumber) {
        var flag : String? = null
        if(!address.isNullOrBlank() && !address.isNullOrEmpty()) this.address = address
        else flag = "The address is null, You should enter your address ):"
        if(checkVisaNumber(visaNumber)) this.visaNumber = visaNumber
        else flag = "You have entered false visa number. ):"
        if(flag != null)
            throw Exception(flag)
    }

    constructor( username: String?, address: String?, phoneNumber: String?):super(username,phoneNumber)  {
        var flag : String? = null
        if(!address.isNullOrBlank() && !address.isNullOrEmpty()) this.address = address
        else flag = "The address is null, You should enter your address ):"
        if(flag != null)
            throw Exception(flag)
    }

    fun open(context: Context){
        this.db = AccessDatabase.getInstance(context)
    }

    fun visaNumber():String{
        if(!checkVisaNumber(this.visaNumber))
            throw Exception("It is likely that data load error has occurred ):")
        return this.visaNumber!!
    }

    fun address():String{
        if(this.address.isNullOrEmpty() || this.address.isNullOrBlank())
            throw Exception("It is likely that data load error has occurred ):")
        return this.address!!
    }

    fun username(username:String?){
        if(!checkUsername(username))
            throw Exception("Possible letters is (a~z), (A~Z), (0~9) or `_` ")
        this.username =username
    }

    fun phoneNumber(phoneNumber: String?){
        if(!checkPhoneNumber(phoneNumber))
            throw Exception("You have entered false phone number. ):")
        this.phone = phoneNumber
    }

    fun visaNumber(visaNumber: String?){
        if(!checkVisaNumber(visaNumber))
            throw Exception("You have entered false visa number. ):")
        this.visaNumber = visaNumber
    }

    fun address(address: String?){
        if(address.isNullOrBlank()||address.isNullOrEmpty())
            throw Exception("The address is null, You should enter your address ):")
        this.address = address
    }

    fun interact(announcementID: Int?):Boolean{
        if(announcementID == null || db == null)
            throw Exception("EXCEPTION")
        val b = db!!.interaction(this.username(),announcementID)
        return b
    }

    fun checkout(order: Order?):Pair<Boolean,Int>{
        if(order == null || db == null)
            throw Exception("You have entered false order. ):")
        val b = db!!.order(order)
        return b
    }

    fun reorder(orderID:Int?):Order{
        if(orderID == null || db == null)
            throw Exception("You have entered false orderID. ):")
        val b = db!!.order(orderID)
        return b
    }

    fun editOrder(orderID: Int?, addedItems:ArrayList<CartItem>?,deletedItems:ArrayList<CartItem>?):Boolean{
        if(orderID == null || addedItems == null || deletedItems == null || db == null)
            throw Exception()
        val b = db!!.edit(orderID,addedItems,deletedItems)
        return b
    }

    fun makeFeedback(feedBack: FeedBack?):Boolean{
        if(feedBack == null || db == null)
            throw Exception("You have entered false feedBack. ):")
        val b = db!!.feedback(feedBack)
        return b
    }

    fun rateItem(rate:Float?, itemID:Int?):Boolean{
        if(itemID == null || rate == null || db == null)
            throw Exception("You have entered false itemID or false rate. ):")
        val b = db!!.itemRate(rate,this.username(),itemID)
        return b
    }

    fun editRateItem(rate:Float?, itemID:Int?):Boolean{
        if(itemID == null || rate == null || db == null)
            throw Exception("You have entered false itemID or false rate. ):")
        val b = db!!.editItemRate(rate,this.username(),itemID)
        return b
    }

    fun rateApp(rating: Rating?):Boolean{
        if(rating == null || db == null)
            throw Exception("You have entered false rating .")
        val b = db!!.rate(rating)
        return b
    }

    fun search(name:String?,type : String?):ArrayList<MenuItem>{
        if(name == null || type == null || db == null)
            throw Exception()
        val b = db!!.itemsByName(name,type)
        return b
    }

    private fun checkVisaNumber(visaNumber:String?):Boolean{
        if(visaNumber.isNullOrEmpty() || visaNumber.isNullOrBlank() )
            return false
        for(i in visaNumber){
            if(i !in '0'..'9') return false
        }
        return true
    }

}