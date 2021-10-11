package com.example.restaurant


class Order {
    private var orderId:Int = -1
    private var totalCost:Float? = null
    private var client:Client? = null
    private var date:String? = null
    private var duration:Int? = null
    private var items:ArrayList<CartItem>? = null

    constructor(orderId: Int, totalCost: Float?, client: Client?, date: String?, duration: Int?, items: ArrayList<CartItem>?) {
        var flag :String?= null
        this.orderId = orderId
        if(checkTotalCost(totalCost)) this.totalCost = totalCost
        else flag = "You have entered false total cost. ):"
        if(client != null) this.client = client
        else flag = "You have entered false customer info. ):"
        if(date != null) this.date = date
        else flag = "You have entered false date info. ):"
        if(duration != null && duration != 0) this.duration = duration
        else flag = "You have entered false duration info. ):"
        if(checkItems(items)) this.items = items
        else flag = "You have entered false items info. ):"

        if(flag != null)
            throw Exception(flag)

    }

    constructor(orderId: Int, date: String?, duration: Int?) {
        var flag :String?= null
        this.orderId = orderId
        if(date != null) this.date = date
        else flag = "You have entered false date info. ):"
        if(duration != null && duration != 0) this.duration = duration
        else flag = "You have entered false duration info. ):"

        if(flag != null)
            throw Exception(flag)

    }

    constructor(client: Client?, date: String?, duration: Int?, items: ArrayList<CartItem>?) {
        var flag :String?= null
        if(client != null) this.client = client
        else flag = "You have entered false customer info. ):"
        if(date != null) this.date = date
        else flag = "You have entered false date info. ):"
        if(duration != null && duration != 0) this.duration = duration
        else flag = "You have entered false duration info. ):"
        if(checkItems(items)) this.items = items
        else flag = "You have entered false items info. ):"

        if(flag != null)
            throw Exception(flag)
    }

    fun orderId():Int{
        if(this.orderId == -1)
            throw Exception("It is likely that data load error has occurred ):")
        return this.orderId
    }

    fun totalCost():Float{
        if(!checkTotalCost(this.totalCost))
            throw Exception("It is likely that data load error has occurred ):")
        return this.totalCost!!
    }

    fun customerInfo():Client{
        if(this.client == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.client!!
    }

    fun customerInfo(client: Client?){
        if(client == null)
            throw Exception("It is likely that data load error has occurred ):")
        this.client = client
    }

    fun date():String{
        if(date == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.date!!
    }

    fun duration():Int{
        if(this.duration == null) return 2700 // 45 minute
        return this.duration!!
    }

    fun items():ArrayList<CartItem>{
        if(!checkItems(this.items))
            throw Exception("It is likely that data load error has occurred ):")
        return this.items!!
    }

    fun totalCost(totalCost: Float?){
        if(!checkTotalCost(totalCost))
            throw Exception("You have entered false total cost. ):")
        this.totalCost = totalCost
    }

    fun duration(duration:Int?){
        if(duration == null || duration == 0)
            throw Exception("You have entered false duration. ):")
        this.duration = duration
    }

    fun date(date:String?){
        if(date == null)
            throw Exception("It is likely that data load error has occurred ):")
        this.date = date
    }

    fun items(items:ArrayList<CartItem>){
        if(!checkItems(items))
            throw Exception("You have entered false items. ):")
        this.items = items
    }

    private fun checkTotalCost(totalCost: Float?):Boolean{
        return (totalCost != null && totalCost != 0.0f )
    }

    private fun checkItems(listOfItem: ArrayList<CartItem>?):Boolean{
        return (listOfItem != null && listOfItem.size != 0)
    }
}