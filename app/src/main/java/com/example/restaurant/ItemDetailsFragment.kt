package com.example.restaurant

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator

private var SIZES_SIZE :Int = 0
private var IMAGES_SIZE :Int = 0

private const val SIZE_EXTRA = "SIZE"
private const val PRICE_EXTRA = "PRICE"
private const val NAME_EXTRA = "NAME"
private const val ID_EXTRA = "ID"
private const val INGREDIENTS_EXTRA = "INGREDIENTS"
private const val DISCOUNT_EXTRA = "DISCOUNT"
private const val RATE_EXTRA = "RATE"
private const val IMAGE_EXTRA = "IMAGE"
private const val MAX_QUANTITY_EXTRA = "MAX_QUANTITY"
private const val NUMBER_OF_REVIEWERS_EXTRA = "NUMBER_OF_REVIEWERS"
private const val ITEM_TYPE_EXTRA = "ITEM_TYPE"

class ItemDetailsFragment : Fragment() {

    private lateinit var menuItem: MenuItem
    private lateinit var item : RestaurantItem
    private var sizePosition : Int = 0
    private lateinit var listener : ItemOrderListener

    interface ItemOrderListener{
        fun addToCart(cartItem: CartItem?)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{listener = context as ItemOrderListener}
        catch (e:Exception) {throw Exception("You don't implements from ItemOrderListener")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        menuItem = getInfo(bundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = RestaurantItem.getItem(menuItem)

        val images :ViewPager = view.findViewById(R.id.ItemDetailsFragment_ViewPager_Images)
        val name : TextView = view.findViewById(R.id.ItemDetailsFragment_TextView_Name)
        val rate : RatingBar = view.findViewById(R.id.ItemDetailsFragment_RatingBar_Rate)
        val reviewerNumber : TextView = view.findViewById(R.id.ItemDetailsFragment_TextView_ReviewersNumber)
        val ingredients : TextView = view.findViewById(R.id.ItemDetailsFragment_TextView_Ingredients)
        val addToCart : Button = view.findViewById(R.id.ItemDetailsFragment_Button_AddToCart)
        val add : FloatingActionButton = view.findViewById(R.id.ItemDetailsFragment_FloatingActionButton_Add)
        val sub :FloatingActionButton = view.findViewById(R.id.ItemDetailsFragment_FloatingActionButton_Subtract)
        val quantity : TextView = view.findViewById(R.id.ItemDetailsFragment_TextView_Quantity)
        val listOfSizes :ListView = view.findViewById(R.id.ItemDetailsFragment_ListView_listOfSize)
        val price :TextView = view.findViewById(R.id.ItemDetailsFragment_TextView_Price)
        val imagesIndicator : SpringDotsIndicator = view.findViewById(R.id.ItemDetailsFragment_DotsIndicator_Images)

        val l = listOfSizes.layoutParams
        l.height = item.sizes().size * 85
        listOfSizes.layoutParams = l

        images.adapter = ItemDetailsImageAdapter(item.images())
        images.pageMargin = 25
        imagesIndicator.setViewPager(images)
        name.text = item.name()
        rate.rating = item.rate()
        reviewerNumber.text = item.numberOfReviews().toString()
        ingredients.text = item.ingredients()
        listOfSizes.adapter = ItemDetailsSizeAndPriceAdapter(item.sizes())
        price.text = item.sizes()[0].price().toString()

        addToCart.setOnClickListener {
            listener.addToCart(CartItem(menuItem,sizePosition,quantity.text.toString().toInt()))
        }

        add.setOnClickListener {
            var quan = quantity.text.toString().toInt()
            if(item.maxQuantity()!=null) {
                if (quan < item.maxQuantity()!!)
                    quan++
            }
            else
                quan++
            price.text = String.format("%.2f",(item.sizes()[sizePosition].price() * quan))
            quantity.text = quan.toString()
        }

        sub.setOnClickListener {
            var quan = quantity.text.toString().toInt()
            if (quan > 1) {
                quan--
                price.text = String.format("%.2f",(item.sizes()[sizePosition].price() * quan))
                quantity.text = quan.toString()
            }
        }
    }

    fun sizeChanged(position : Int){
        sizePosition = position
        val view = view
        val price :TextView? = view?.findViewById(R.id.ItemDetailsFragment_TextView_Price)
        val quantity : TextView? = view?.findViewById(R.id.ItemDetailsFragment_TextView_Quantity)
        price?.text = String.format("%.2f",(item.sizes()[sizePosition].price() * quantity?.text.toString().toFloat()))
    }

   companion object{

       fun getInfo(menuItem: MenuItem?):Bundle{
           if(menuItem == null)
               throw Exception("You enter null menu item in Item details")

           val bundle = Bundle()
           try {
               bundle.putString(ITEM_TYPE_EXTRA, menuItem.itemType())
               val item = RestaurantItem.getItem(menuItem)
               val sizes = item.sizes()
               val images = item.images()

               SIZES_SIZE = sizes.size
               IMAGES_SIZE = images.size
               bundle.putInt(ID_EXTRA, item.id())
               bundle.putString(NAME_EXTRA, item.name())
               bundle.putString(INGREDIENTS_EXTRA, item.ingredients())
               bundle.putFloat(DISCOUNT_EXTRA, item.discount())
               bundle.putFloat(RATE_EXTRA, item.rate())
               bundle.putInt(NUMBER_OF_REVIEWERS_EXTRA, item.numberOfReviews())
               if (item.maxQuantity() != null)
                   bundle.putInt(MAX_QUANTITY_EXTRA, item.maxQuantity()!!)

               for (i in 0 until sizes.size) {
                   bundle.putString("$i$SIZE_EXTRA", sizes[i].size())
                   bundle.putFloat("$i$PRICE_EXTRA", sizes[i].price())
               }
               for (i in 0 until images.size)
                   bundle.putString("$i$IMAGE_EXTRA", images[i])
           }
           catch (e:Exception){}
            return bundle
        }

       fun getInfo(bundle:Bundle?):MenuItem{
           if(bundle == null)
               throw Exception("You enter null bundle in Item details")

           var menuItem : MenuItem? = null
           try {
                val type = bundle.getString(ITEM_TYPE_EXTRA, null)
                val id = bundle.getInt(ID_EXTRA,-1)
                val name = bundle.getString(NAME_EXTRA, null)
                val ingredients = bundle.getString(INGREDIENTS_EXTRA, null)
                val discount = bundle.getFloat(DISCOUNT_EXTRA, 0f)
                val rate = bundle.getFloat(RATE_EXTRA, 2.5f)
                val numberOfReviewers = bundle.getInt(NUMBER_OF_REVIEWERS_EXTRA, 0)
                var maxQuantity: Int? = bundle.getInt(MAX_QUANTITY_EXTRA, 0)
                if (maxQuantity == 0) maxQuantity = null

                val sizes = ArrayList<SizeAndPrice>()
                val images = ArrayList<String>()
                for (i in 0 until SIZES_SIZE) {
                    val size = bundle.getString("$i$SIZE_EXTRA", null)
                    val price = bundle.getFloat("$i$PRICE_EXTRA", 0f)
                    sizes.add(SizeAndPrice(size, price))
                }
                for (i in 0 until IMAGES_SIZE) {
                    val image = bundle.getString("$i$IMAGE_EXTRA", "")
                    images.add(image)
                }
                menuItem = RestaurantItem.getItem(type,name,images,discount,rate,numberOfReviewers,ingredients,sizes,maxQuantity,id)
           }
           catch (e:Exception){}
           if(menuItem == null)
               throw Exception("null menu item item details activity")
           return menuItem
        }

       fun getInstance(menuItem: MenuItem):ItemDetailsFragment{
           val obj = ItemDetailsFragment()
           val bundle = getInfo(menuItem)
           obj.arguments = bundle
           return obj
       }
    }
}
