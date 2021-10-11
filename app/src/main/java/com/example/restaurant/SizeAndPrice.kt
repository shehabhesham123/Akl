package com.example.restaurant

class SizeAndPrice {
    private var size : String
    private var price : Float

    constructor(size: String, price: Float) {
        this.size = size
        this.price = price
    }

    fun size():String{
        return this.size
    }

    fun size(size:String){
        this.size = size
    }

    fun price():Float{
        return this.price
    }

    fun price(price : Float){
        this.price = price
    }
}