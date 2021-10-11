package com.example.restaurant

class Announcement {

    private var id : Int = -1
    private var image:String? = null
    private var date:String? = null
    private var type:String? = null
    private var announcement:String? = null

    constructor(id :Int, image: String?, date: String?, type: String?, announcement: String?) {
        var flag :String? = null
        this.id = id
        if(!image.isNullOrBlank() && !image.isNullOrEmpty()) this.image = image
        else flag = "You have entered false image Info."
        if(!date.isNullOrBlank() && !date.isNullOrEmpty()) this.date = date
        else flag = "You have entered false date Info."
        if(!type.isNullOrBlank() && !type.isNullOrEmpty()) this.type = doneSentence(type)
        else flag = "The type of announcement is null, You should enter its type ):"
        if(!announcement.isNullOrEmpty() && !announcement.isNullOrBlank()) this.announcement = announcement
        else flag = "The announcement is null, You should enter your announcement ):"

        if(flag != null)
            throw Exception(flag)
    }

    constructor(image: String?, date: String?, type: String?, announcement: String?) {
        var flag :String? = null
        if(!image.isNullOrBlank() && !image.isNullOrEmpty()) this.image = image
        else flag = "You have entered false image Info."
        if(!date.isNullOrBlank() && !date.isNullOrEmpty()) this.date = date
        else flag = "You have entered false date Info."
        if(!type.isNullOrBlank() && !type.isNullOrEmpty()) this.type = doneSentence(type)
        else flag = "The type of announcement is null, You should enter its type ):"
        if(!announcement.isNullOrEmpty() && !announcement.isNullOrBlank()) this.announcement = announcement
        else flag = "The announcement is null, You should enter your announcement ):"

        if(flag != null)
            throw Exception(flag)
    }

    fun id():Int{
        if(this.id == -1)
            throw Exception("It is likely that data load error has occurred ):")
        return this.id
    }

    fun image():String{
        if(this.image == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.image!!
    }

    fun date():String {
        if(this.date.isNullOrBlank() || this.date.isNullOrEmpty())
            throw Exception("It is likely that data load error has occurred ):")
        return this.date!!
    }

    fun type():String{
        if(this.type.isNullOrBlank() || this.type.isNullOrEmpty())
            throw Exception("It is likely that data load error has occurred ):")
        return this.type!!
    }

    fun announcement():String{
        if(this.announcement.isNullOrEmpty() || this.announcement.isNullOrBlank())
            throw Exception("It is likely that data load error has occurred ):")
        return this.announcement!!
    }

    fun announcement(announcement: String?){
        if(announcement.isNullOrEmpty() || announcement.isNullOrBlank())
            throw Exception("The announcement is null, You should enter your announcement ):")
        this.announcement = doneSentence(announcement)
    }

    fun image(image:String?){
        if(image.isNullOrEmpty() || image.isNullOrBlank())
            throw Exception("You have entered false image Info. ):")
        this.image=image
    }

    fun date(date:String?){
        if(date.isNullOrBlank() || date.isNullOrEmpty())
            throw Exception("You have entered false date Info. ):")
        this.date = date
    }

    fun type(type:String?){
        if(type.isNullOrBlank() || type.isNullOrEmpty())
            throw Exception("The type of announcement is null, You should enter its type ):")
        this.type = doneSentence(type)
    }

    private fun doneSentence(sentence:String):String{
        var text : String = sentence.toLowerCase()
        text = text.replaceRange(0,1, text!![0].toUpperCase().toString())
        return text
    }

    private fun checkDate(date: String){
        /*
        var year :Int = ("${date[0]}${date[1]}${date[2]}${date[3]}").toInt()
        var month :Int = ("${date[5]}${date[6]}").toInt()
        var day :Int = ("${date[8]}${date[9]}").toInt()
        var hour :Int = ("${date[11]}${date[12]}").toInt()
        var minute :Int = ("${date[14]}${date[15]}").toInt()
        var second :Int = ("${date[17]}${date[18]}").toInt()
        var amOrPm : String = ("${date[20]}${date[21]}")
        if(amOrPm == "PM") hour+=12

        val currentYear =SimpleDateFormat("yyyy").format(Date()).toInt()
        val currentMonth =SimpleDateFormat("MM").format(Date()).toInt()
        val currentDay =SimpleDateFormat("dd").format(Date()).toInt()
        var currentHour =SimpleDateFormat("hh").format(Date()).toInt()
        val currentMinute =SimpleDateFormat("mm").format(Date()).toInt()
        val currentSecond =SimpleDateFormat("ss").format(Date()).toInt()
        val currentAmOrPm = SimpleDateFormat("aa").format(Date())
        if(currentAmOrPm == "PM") currentHour+=12

        while(true){
            if(year==currentYear || month==currentMonth || day==currentDay || hour==currentHour || minute==currentMinute || second==currentSecond )
                break
            if()

         */
    }
}