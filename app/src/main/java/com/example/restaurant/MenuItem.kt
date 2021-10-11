package com.example.restaurant

import kotlin.system.measureTimeMillis

const val FOOD_CODE = "FOOD"
const val DRINK_CODE = "DRINK"
const val DESSERT_CODE = "DESSERT"

abstract class RestaurantItem{
    protected var id : Int = -1
    protected var name : String? = null
    protected var images : ArrayList<String>? = null
    protected var discount : Float? = null
    protected var rate : Float? = null
    protected var numberOfReviews :Int? = null
    protected var ingredients : String? = null
    protected var sizes : ArrayList<SizeAndPrice>? = null
    protected var maxQuantity : Int? = null

    companion object{
        fun getItem(menuItem: MenuItem?):RestaurantItem{
            if(menuItem == null)
                throw Exception()
            return when(menuItem.itemType()){
                FOOD_CODE-> menuItem.food()
                DRINK_CODE-> menuItem.drink()
                else-> menuItem.dessert()
            }
        }

        fun getItem(itemType: String?,name: String?, images: ArrayList<String>?, discount: Float?, rate: Float?, numberOfReviews: Int?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?, id: Int):MenuItem{
            if(itemType.isNullOrBlank() || itemType.isNullOrEmpty())
                throw Exception()
            return when(itemType){
                FOOD_CODE-> {
                    val food = Food(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity, id)
                    MenuItem(food)
                }
                DRINK_CODE-> {
                    val drink = Drink(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity, id)
                    MenuItem(drink)
                }
                else->{
                    val dessert = Dessert(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity, id)
                    MenuItem(dessert)
                }
            }
        }

        fun getItem(itemType: String?,name: String?, images: ArrayList<String>?, discount: Float?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?):MenuItem{
            if(itemType.isNullOrBlank() || itemType.isNullOrEmpty())
                throw Exception()
            return when(itemType){
                FOOD_CODE-> {
                    val food = Food(name, images, discount, ingredients, sizes, maxQuantity)
                    MenuItem(food)
                }
                DRINK_CODE-> {
                    val drink = Drink(name, images, discount, ingredients, sizes, maxQuantity)
                    MenuItem(drink)
                }
                else->{
                    val dessert = Dessert(name, images, discount, ingredients, sizes, maxQuantity)
                    MenuItem(dessert)
                }
            }
        }

        fun checkName(name:String?):Boolean{
            if(name.isNullOrEmpty() || name.isNullOrBlank() || name[0]==' ') return false
            for(i in 0 until name.length-1){
                if(name[i] == ' ' && name[i+1] == ' ')
                    return false
            }
            for(i in name.indices){
                if(name[i] !in 'a'..'z' && name[i] !in 'A'..'Z' && name[i] !in '0'..'9' && name[i] !in 'أ'..'ي'&& name[i] != '-' && name[i] != ' ')
                    return false
            }
            return true
        }
    }

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, rate: Float?, numberOfReviews: Int?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?) {
        var flag :String? = null
        if(!name.isNullOrEmpty() && !name.isNullOrBlank() && checkName(name)) this.name = doneSentence(name)
        else flag = "Possible letters is (a~z), (A~Z), (0~9),(أّ~ي) or `-` and one space between two words"
        if(images != null && images.size > 0) this.images = images
        else flag =  "You have entered false images info. ):"
        if(discount != null) this.discount = discount
        else flag = "You have entered false discount info. ):"
        if(rate != null && rate in 0.0..5.0) this.rate = rate
        else flag = "You have entered false rate info. ):"
        if(numberOfReviews != null) this.numberOfReviews = numberOfReviews
        else flag = "You have entered false number of reviews info. ):"
        if(!ingredients.isNullOrBlank() && !ingredients.isNullOrEmpty()) this.ingredients = doneSentence(ingredients)
        else flag = "You have entered false ingredients info. ):"
        if(sizes != null && sizes.size > 0) {
            for(i in 0 until sizes.size){
                for(j in i+1 until sizes.size){
                    if(sizes[i].price() > sizes[j].price()){
                        val sizeAndPrice = sizes[i]
                        sizes[i] = sizes[j]
                        sizes[j] = sizeAndPrice
                    }
                }
            }
            this.sizes = sizes
        }
        else flag = "You have entered false sizes info. ):"
        if(maxQuantity != null && maxQuantity > 0) this.maxQuantity = maxQuantity
        else if(maxQuantity != null) flag = "You have entered false max quantity info. $maxQuantity ):"

        if(flag!=null)
            throw Exception(flag)
    }

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?) {
        var flag :String? = null
        if(!name.isNullOrEmpty() && !name.isNullOrBlank() && checkName(name)) this.name = doneSentence(name)
        else flag = "Possible letters is (a~z), (A~Z), (0~9),(أّ~ي) or `-` and one space between two words"
        if(images != null && images.size > 0) this.images = images
        else flag =  "You have entered false images info. ):"
        if(discount != null) this.discount = discount
        else flag = "You have entered false discount info. ):"
        if(!ingredients.isNullOrBlank() && !ingredients.isNullOrEmpty()) this.ingredients = doneSentence(ingredients)
        else flag = "You have entered false ingredients info. ):"
        if(sizes != null && sizes.size > 0) {
            for(i in 0 until sizes.size){
                for(j in i+1 until sizes.size){
                    if(sizes[i].price() > sizes[j].price()){
                        val sizeAndPrice = sizes[i]
                        sizes[i] = sizes[j]
                        sizes[j] = sizeAndPrice
                    }
                }
            }
            this.sizes = sizes
        }
        else flag = "You have entered false sizes info.  ):"
        if(maxQuantity != null && maxQuantity > 0) this.maxQuantity = maxQuantity
        else if(maxQuantity != null) flag = "You have entered false max quantity info. $maxQuantity ):"

        if(flag!=null)
            throw Exception(flag)
    }

    fun name():String {
        if(!checkName(this.name))
            throw Exception("It is likely that data load error has occurred ):")
        return this.name!!
    }

    fun images():ArrayList<String> {
        if(this.images == null || this.images!!.size == 0 )
            throw Exception("It is likely that data load error has occurred ):")
        return this.images!!
    }

    fun discount():Float {
        if(this.discount == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.discount!!
    }

    fun rate():Float{
        if(this.rate == null || this.rate!! !in 0.0..5.0)
            throw Exception("It is likely that data load error has occurred ):")
        return this.rate!!
    }

    fun numberOfReviews():Int{
        if(this.numberOfReviews == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.numberOfReviews!!
    }

    fun ingredients():String{
        if(this.ingredients.isNullOrBlank() || this.ingredients.isNullOrEmpty())
            throw Exception("It is likely that data load error has occurred ):")
        return this.ingredients!!
    }

    fun sizes():ArrayList<SizeAndPrice>{
        if(this.sizes == null || this.sizes!!.size == 0)
            throw Exception("It is likely that data load error has occurred ):")
        return this.sizes!!
    }

    fun maxQuantity():Int?{
        if(this.maxQuantity != null && this.maxQuantity == 0)
            throw Exception("It is likely that data load error has occurred ):")
        return this.maxQuantity
    }

    fun id():Int{
        if(this.id == -1)
            throw Exception("It is likely that data load error has occurred ):")
        return this.id
    }

    protected fun doneSentence(sentence : String):String{
        var text = sentence.toLowerCase()
        text = text.replaceRange(0,1, text[0].toUpperCase().toString())
        for( i in 1 until text.length){
            if(text[i] in 'a'..'z' && text[i-1] ==' ')
                text = text.replaceRange(i,i+1, text[i].toUpperCase().toString())
        }
        return text
    }

    abstract fun name(name:String?)

    abstract fun images(images:ArrayList<String>?)

    abstract fun discount(discount:Float?)

    abstract fun rate(rate : Float?)

    abstract fun numberOfReviews(numberOfReviews:Int?)

    abstract fun ingredients(ingredients : String?)

    abstract fun sizes(sizes : ArrayList<SizeAndPrice>?)

    abstract fun maxQuantity(maxQuantity:Int?)

    protected fun sort(sizes: ArrayList<SizeAndPrice>):ArrayList<SizeAndPrice>{
        for(i in 0 until sizes.size){
            for(j in i+1 until sizes.size){
                if(sizes[i].price() > sizes[j].price()){
                    val sizeAndPrice = sizes[i]
                    sizes[i] = sizes[j]
                    sizes[j] = sizeAndPrice
                }
            }
        }
        return sizes
    }
}

class Food : RestaurantItem{

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, rate: Float?, numberOfReviews: Int?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?, id: Int
    ) : super(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity) {
        this.id = id
    }

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?
    ) : super(name, images, discount, ingredients, sizes, maxQuantity)

    override fun name(name:String?){
        if(!checkName(name))
            throw Exception("Possible letters is (a~z), (A~Z), (0~9) or `-` and one space between two words")
        this.name = doneSentence(name!!)
    }

    override fun images(images:ArrayList<String>?){
        if(images == null || images.size == 0)
            throw Exception("You have entered false images. ):")
        this.images = images
    }

    override fun discount(discount: Float?) {
        if(discount == null)
            throw Exception("You have entered false discount. ):")
        this.discount = discount
    }

    override fun rate(rate : Float?){
        if(rate == null || rate !in 0.0..5.0)
            throw Exception("You have entered false rate. ):")
        this.rate = rate
    }

    override fun numberOfReviews(numberOfReviews:Int?){
        if(numberOfReviews == null)
            throw Exception("You have entered false number of reviews. ):")
        this.numberOfReviews = numberOfReviews
    }

    override fun ingredients(ingredients : String?){
        if(ingredients.isNullOrBlank() || ingredients.isNullOrEmpty())
            throw Exception("You have entered false ingredients. ):")
        this.ingredients = doneSentence(ingredients)
    }

    override fun sizes(sizes : ArrayList<SizeAndPrice>?){
        if(sizes == null || sizes.size == 0)
            throw Exception("You have entered false sizes. ):")
        this.sizes = sort(sizes)
    }

    override fun maxQuantity(maxQuantity:Int?){
        if(maxQuantity != null && maxQuantity <= 0)
            throw Exception("You have entered false max quantity. ):")
        this.maxQuantity = maxQuantity
    }



}

class Drink : RestaurantItem{

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, rate: Float?, numberOfReviews: Int?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?, id: Int
    ) : super(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity) {
        this.id = id
    }

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?
    ) : super(name, images, discount, ingredients, sizes, maxQuantity)

    override fun name(name:String?){
        if(!checkName(name))
            throw Exception("Possible letters is (a~z), (A~Z), (0~9) or `-` and one space between two words")
        this.name = doneSentence(name!!)
    }

    override fun images(images:ArrayList<String>?){
        if(images == null || images.size == 0)
            throw Exception("You have entered false images. ):")
        this.images = images
    }

    override fun discount(discount: Float?) {
        if(discount == null)
            throw Exception("You have entered false discount. ):")
        this.discount = discount
    }

    override fun rate(rate : Float?){
        if(rate == null || rate !in 0.0..5.0)
            throw Exception("You have entered false rate. ):")
        this.rate = rate
    }

    override fun numberOfReviews(numberOfReviews:Int?){
        if(numberOfReviews == null)
            throw Exception("You have entered false number of reviews. ):")
        this.numberOfReviews = numberOfReviews
    }

    override fun ingredients(ingredients : String?){
        if(ingredients.isNullOrBlank() || ingredients.isNullOrEmpty())
            throw Exception("You have entered false ingredients. ):")
        this.ingredients = doneSentence(ingredients)
    }

    override fun sizes(sizes : ArrayList<SizeAndPrice>?){
        if(sizes == null || sizes.size == 0)
            throw Exception("You have entered false sizes. ):")
        this.sizes = sort(sizes)
    }

    override fun maxQuantity(maxQuantity:Int?){
        if(maxQuantity != null && maxQuantity <= 0)
            throw Exception("You have entered false max quantity. ):")
        this.maxQuantity = maxQuantity
    }

}

class Dessert : RestaurantItem{

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, rate: Float?, numberOfReviews: Int?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?, id: Int
    ) : super(name, images, discount, rate, numberOfReviews, ingredients, sizes, maxQuantity) {
        this.id = id
    }

    constructor(name: String?, images: ArrayList<String>?, discount: Float?, ingredients: String?, sizes: ArrayList<SizeAndPrice>?, maxQuantity: Int?
    ) : super(name, images, discount, ingredients, sizes, maxQuantity)

    override fun name(name:String?){
        if(!checkName(name))
            throw Exception("Possible letters is (a~z), (A~Z), (0~9) or `-` and one space between two words")
        this.name = doneSentence(name!!)
    }

    override fun images(images:ArrayList<String>?){
        if(images == null || images.size == 0)
            throw Exception("You have entered false images. ):")
        this.images = images
    }

    override fun discount(discount: Float?) {
        if(discount == null)
            throw Exception("You have entered false discount. ):")
        this.discount = discount
    }

    override fun rate(rate : Float?){
        if(rate == null || rate !in 0.0..5.0)
            throw Exception("You have entered false rate. ):")
        this.rate = rate
    }

    override fun numberOfReviews(numberOfReviews:Int?){
        if(numberOfReviews == null)
            throw Exception("You have entered false number of reviews. ):")
        this.numberOfReviews = numberOfReviews
    }

    override fun ingredients(ingredients : String?){
        if(ingredients.isNullOrBlank() || ingredients.isNullOrEmpty())
            throw Exception("You have entered false ingredients. ):")
        this.ingredients = doneSentence(ingredients)
    }

    override fun sizes(sizes : ArrayList<SizeAndPrice>?){
        if(sizes == null || sizes.size == 0)
            throw Exception("You have entered false sizes. ):")
        this.sizes = sort(sizes)
    }

    override fun maxQuantity(maxQuantity:Int?){
        if(maxQuantity != null && maxQuantity <= 0)
            throw Exception("You have entered false max quantity. ):")
        this.maxQuantity = maxQuantity
    }

}

class MenuItem {

    private val itemType : String     // food || drinks || desserts
    private var food : Food? = null
    private var drink : Drink? = null
    private var dessert : Dessert? = null

    constructor(food: Food?) {
        if(food == null)
            throw Exception("You have entered false food Info. ):")
        this.food = food
        this.itemType = FOOD_CODE
    }

    constructor(drink: Drink?) {
        if(drink == null)
            throw Exception("You have entered false drink Info. ):")
        this.drink = drink
        this.itemType = DRINK_CODE
    }

    constructor(dessert: Dessert?) {
        if(dessert == null)
            throw Exception("You have entered false dessert Info. ):")
        this.dessert = dessert
        this.itemType = DESSERT_CODE
    }

    fun itemType():String {
        if(this.itemType.isNullOrEmpty() || this.itemType.isNullOrBlank())
            throw Exception("It is likely that data load error has occurred ):")
        return this.itemType
    }

    fun food():Food{
        if(this.food == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.food!!
    }

    fun drink():Drink{
        if(this.drink == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.drink!!
    }

    fun dessert():Dessert{
        if(this.dessert == null)
            throw Exception("It is likely that data load error has occurred ):")
        return this.dessert!!
    }

}