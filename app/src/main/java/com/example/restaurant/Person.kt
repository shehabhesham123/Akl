package com.example.restaurant

abstract class Person {
    protected var username : String? = null    // primary kay
    protected var phone:String? = null

    constructor(username: String?, phone: String?) {
        var flag : String? = null
        if(checkUsername(username)) this.username = username
        else flag = "Possible letters is (a~z), (A~Z), (0~9) or `_`"
        if(checkPhoneNumber(phone)) this.phone = phone
        else flag = "You have entered false phone number. ):"
        if(flag != null)
            throw Exception(flag)
    }

    fun username():String{
        if(this.username.isNullOrBlank() || this.username.isNullOrEmpty())
            throw Exception("It is likely that data load error has occurred ):")
        return this.username!!
    }

    fun phoneNumber():String{
        if(this.phone.isNullOrEmpty() || this.phone.isNullOrBlank())
            throw Exception("It is likely that data load error has occurred ):")
        return this.phone!!
    }

    protected fun checkUsername(username:String?):Boolean{
        if(username.isNullOrEmpty() || username.isNullOrBlank()) return false
        for(i in username){
            if(i !in 'A'..'Z' && i !in 'a'..'z' && i !in '0'..'9' && i != '_')
                return false
        }
        return true
    }

    protected fun checkPhoneNumber(phoneNumber: String?):Boolean{
        if(phoneNumber.isNullOrBlank() || phoneNumber.isNullOrEmpty()) return false
        for(i in phoneNumber){
            if(i !in '0'..'9') return false
        }
        if((phoneNumber.length == 11 && phoneNumber[0] == '0' && phoneNumber[1] == '1' && (phoneNumber[2] == '0'||phoneNumber[2] == '1'||phoneNumber[2] == '2'||phoneNumber[2] == '5') || (phoneNumber.length == 10 && phoneNumber[0] == '1' && (phoneNumber[1] == '0'||phoneNumber[1] == '1'||phoneNumber[1] == '2'||phoneNumber[1] == '5'))))
            return true
        return false
    }
}