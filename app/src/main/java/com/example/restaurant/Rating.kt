package com.example.restaurant

class Rating {

    private var client:Client? = null
    private var rating : Int? = null
    private var date : String? = null

    constructor(id : Int, client: Client?, rating: Int?, date:String?) {
        var flag :String? = null
        if(date != null) this.date = date
        else flag = "You have entered false date info. ):"
        if(client != null) this.client = client
        else flag = "You have entered false customer info. ):"
        if(rating != null) this.rating = rating
        else flag = "You have entered false rating info. ):"

        if(flag != null)
            throw Exception(flag)
    }

    constructor(client: Client?, rating: Int?, date:String?) {
        var flag :String? = null
        if(date != null) this.date = date
        else flag = "You have entered false date info. ):"
        if(client != null) this.client = client
        else flag = "You have entered false customer info. ):"
        if(rating != null) this.rating = rating
        else flag = "You have entered false rating info. ):"

        if(flag!=null)
            throw Exception(flag)
    }

    fun customerInfo():Client{
        if(this.client == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.client!!
    }

    fun rating():Int{
        if(this.rating == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.rating!!
    }

    fun date():String{
        if(this.date == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.date!!
    }

}