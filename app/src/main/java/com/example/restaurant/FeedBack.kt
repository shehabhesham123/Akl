package com.example.restaurant

class FeedBack {
    private var client : Client? = null
    private var isHappy : Boolean = false
    private var date : String? = null
    private var experience : String? = null

    constructor(client: Client?, isHappy: Boolean?, experience: String?, date:String?) {
        var flag :String? = null
        if(date!=null) this.date = date
        else flag = "You have entered false date info. ):"
        if(client != null) this.client = client
        else flag = "You have entered false customer info. ):"
        if(isHappy != null ) this.isHappy = isHappy
        else flag = "You have entered false isHappy info. ):"
        if(!experience.isNullOrEmpty() && !experience.isNullOrBlank()) this.experience = doneSentence(experience)

        if(flag != null)
            throw Exception(flag)
    }

    fun customer():Client{
        if(this.client == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.client!!
    }

    fun isHappy():Boolean{
        return this.isHappy
    }

    fun date():String{
        if(date == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.date!!
    }

    fun experience():String{
        if(experience.isNullOrBlank() || experience.isNullOrEmpty())
            return ""
        return this.experience!!
    }

    private fun doneSentence(sentence:String):String{
        var text : String = sentence.toLowerCase()
        text = text.replaceRange(0,1, text[0].toUpperCase().toString())
        return text
    }

}