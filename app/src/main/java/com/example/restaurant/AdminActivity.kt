package com.example.restaurant

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.add_to_menu.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val MENU_ITEM_ADD_IMAGE_REQUEST_CODE = 1
private const val MENU_ITEM_EDIT_IMAGE_REQUEST_CODE = 2
private const val ANNOUNCEMENT_ADD_IMAGE_REQUEST_CODE = 3
private const val ANNOUNCEMENT_EDIT_IMAGE_REQUEST_CODE = 4
class AdminActivity : AppCompatActivity(),ImagesAdapter.ImageListener {

    private var imagePosition : Int = 0
    ///////////////////////////////////////////////////////////
    private lateinit var announcement: TextInputEditText
    private lateinit var announcementType: TextInputEditText
    private lateinit var announcementImageUri: TextView
    private lateinit var announcementImageAdd: ImageView
    private lateinit var announcementImageDelete: ImageView
    private lateinit var announcementImageEdit: ImageView
    //////////////////////////////////////////////////////////
    private var layoutId = 0        // 1 to addToMenu    2 addToAnnouncement
    private lateinit var doneToolbar : MenuItem
    private lateinit var itemType : Spinner
    private lateinit var name : TextInputEditText
    private lateinit var ingredients : TextInputEditText
    private lateinit var maxQuantity : TextInputEditText
    private lateinit var discount : TextInputEditText
    private lateinit var admin : Admin
    private lateinit var add : FloatingActionButton
    private lateinit var linearLayout: LinearLayout
    private lateinit var layoutContainerAddToMenu: ScrollView
    private lateinit var layoutContainerAddToAnnouncement: LinearLayout
    private lateinit var addToAnnouncement : TextView
    private lateinit var addToMenu : TextView
    private lateinit var listOfSizes : ListView
    private lateinit var listOfImages : ListView
    private lateinit var adapter: ImagesAdapter
    private lateinit var adapter2: SizesAdapter
    private var flag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity)

        setSupportActionBar(findViewById(R.id.AdminActivity_Toolbar))
        admin = Admin(baseContext)
        //////////////////////////////// addToMenu  ////////////////////////////////
        itemType = findViewById(R.id.AddToMenu_Spinner_ItemType)
        name = findViewById(R.id.AddToMenu_TextInputEditText_Name)
        ingredients = findViewById(R.id.AddToMenu_TextInputEditText_Ingredients)
        maxQuantity = findViewById(R.id.AddToMenu_TextInputEditText_MaxQuantity)
        discount = findViewById(R.id.AddToMenu_TextInputEditText_Discount)
        listOfSizes = findViewById(R.id.AddToMenu_ListView_Sizes)
        listOfImages = findViewById(R.id.AddToMenu_ListView_Images)
        ///////////////////////////////////////////////////////////////////////////

        //////////////////////////////// addToAnnouncement  ////////////////////////////////
        announcement = findViewById(R.id.AddToAnnouncement_TextInputEditText_Announcement)
        announcementType = findViewById(R.id.AddToAnnouncement_TextInputEditText_AnnouncementType)
        announcementImageUri = findViewById(R.id.AddToAnnouncement_TextView_Image)
        announcementImageAdd = findViewById(R.id.AddToAnnouncement_ImageView_Add)
        announcementImageDelete = findViewById(R.id.AddToAnnouncement_ImageView_Delete)
        announcementImageEdit = findViewById(R.id.AddToAnnouncement_ImageView_Edit)
        ///////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////
        add = findViewById(R.id.AdminActivity_FloatingActionButton_Add)
        linearLayout = findViewById(R.id.AdminActivity_LinearLayout)
        layoutContainerAddToMenu = findViewById(R.id.AdminActivity_Layout_Container_AddToMenu)
        layoutContainerAddToAnnouncement = findViewById(R.id.AdminActivity_Layout_Container_AddToAnnouncement)
        addToAnnouncement = findViewById(R.id.AdminActivity_TextView_AddToAnnouncement)
        addToMenu = findViewById(R.id.AdminActivity_TextView_AddToMenu)
        ////////////////////////////////////////////////////////////////////////////////

        adapter = ImagesAdapter(listOfImages)
        adapter2 = SizesAdapter(listOfSizes)
        listOfImages.adapter = adapter
        listOfSizes.adapter = adapter2

        add.setOnClickListener {
            if(flag)
                linearLayout.visibility = View.VISIBLE
            else
                linearLayout.visibility = View.GONE
            flag = !flag
        }

        addToMenu.setOnClickListener {
            flag = !flag
            layoutId = 1
            doneToolbar.isVisible = true
            clearLayout2()
            layoutContainerAddToMenu.visibility = View.VISIBLE
            layoutContainerAddToAnnouncement.visibility = View.INVISIBLE
            linearLayout.visibility = View.GONE
        }

        addToAnnouncement.setOnClickListener {
            flag = !flag
            layoutId = 2
            doneToolbar.isVisible = true
            clearLayout1()
            layoutContainerAddToMenu.visibility = View.INVISIBLE
            layoutContainerAddToAnnouncement.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
        }

        announcementImageAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, ANNOUNCEMENT_ADD_IMAGE_REQUEST_CODE)
        }

        announcementImageDelete.setOnClickListener {
            announcementImageUri.text = null
            announcementImageDelete.visibility = View.GONE
            announcementImageEdit.visibility = View.GONE
            announcementImageAdd.visibility = View.VISIBLE
        }

        announcementImageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, ANNOUNCEMENT_EDIT_IMAGE_REQUEST_CODE)
        }
    }

    override fun addImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent,MENU_ITEM_ADD_IMAGE_REQUEST_CODE)
    }

    override fun editImage(position: Int) {
        val intent = Intent()
        imagePosition = position
        intent.action = Intent.ACTION_PICK
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent,MENU_ITEM_EDIT_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MENU_ITEM_ADD_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val d = data?.data
            adapter.addImage(d.toString())
        }
        else  if(requestCode == MENU_ITEM_EDIT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val d = data?.data
            adapter.editImage(d.toString(),imagePosition)
        }
        else if (requestCode == ANNOUNCEMENT_ADD_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            announcementImageUri.text = data?.data.toString()
            announcementImageAdd.visibility = View.GONE
            announcementImageEdit.visibility = View.VISIBLE
            announcementImageDelete.visibility = View.VISIBLE
        }
        else if(requestCode == ANNOUNCEMENT_EDIT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            announcementImageUri.text = data?.data.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_activity_menu,menu)
        doneToolbar = menu!!.findItem(R.id.AdminActivityMenu_Done)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == R.id.AdminActivityMenu_Done){
            if(layoutId == 1) {
                var isDone = true

                val name = name.text.toString()
                val ingredients = ingredients.text.toString()
                val maxQuantity = maxQuantity.text.toString().toIntOrNull()
                var discount = discount.text.toString()
                val images = adapter.getImages()
                val sizes = adapter2.getSizes()

                val sizesErrorView: TextView = findViewById(R.id.AddToMenu_TextView_SizesError)
                val imagesErrorView: TextView = findViewById(R.id.AddToMenu_TextView_ImagesError)

                if (this.itemType.selectedItemPosition == 0) {
                    isDone = false
                    Snackbar.make(this.add, resources.getText(R.string.AdminItemTypeError), Snackbar.LENGTH_LONG).show()
                }

                if (name.isNullOrEmpty() || name.isNullOrBlank()) {
                    isDone = false
                    this.name.error =  resources.getText(R.string.AdminNameError)
                } else if (!RestaurantItem.checkName(name)) {
                    isDone = false
                    this.name.error = resources.getText(R.string.AdminNameError2)
                }

                if (ingredients.isNullOrBlank() || ingredients.isNullOrBlank()) {
                    isDone = false
                    this.ingredients.error =  resources.getText(R.string.AdminIngredientsError)
                }

                if (discount.isNullOrBlank() || discount.isNullOrBlank() )
                    discount="0.0"
                if(discount.toFloat() !in 0.0 .. 100.0){
                    isDone = false
                    this.discount.error = "0 ~ 100"
                }

                if (sizes.size == 0) {
                    isDone = false
                    sizesErrorView.text = resources.getText(R.string.AdminSizesError)
                }
                else
                    sizesErrorView.text = null


                if (images.size == 0) {
                    isDone = false
                    imagesErrorView.text = resources.getText(R.string.AdminImagesError)
                }
                else
                    imagesErrorView.text = null

                if (isDone) {
                    val type = when (itemType.selectedItemPosition) {
                        1 -> FOOD_CODE
                        2 -> DRINK_CODE
                        else -> DESSERT_CODE
                    }
                    try {
                        val menuItem = RestaurantItem.getItem(type, name, images, discount.toFloat(), ingredients, sizes, maxQuantity)
                        val b = admin.addMenuItem(menuItem)
                        if(b){
                            this.layoutContainerAddToMenu.visibility = View.GONE
                            Toast.makeText(baseContext, "Added Successfully", Toast.LENGTH_SHORT).show()
                            doneToolbar.isVisible = false
                            clearLayout1()
                        }
                    } catch (e: Exception) {
                        Snackbar.make(findViewById(R.id.AdminActivity_FloatingActionButton_Add), "${e.message}", Snackbar.LENGTH_LONG).show()
                    }

                }
            }

            else if(layoutId == 2){

                var isDone = true
                val announcement = this.announcement.text.toString()
                val announcementType = this.announcementType.text.toString()
                val imageUri = this.announcementImageUri.text.toString()


                if(announcement.isNullOrBlank() || announcement.isNullOrEmpty()) {
                    this.announcement.error = resources.getText(R.string.AdminAnnouncementError)
                    isDone = false
                }
                if(announcementType.isNullOrEmpty() || announcementType.isNullOrBlank()) {
                    this.announcementType.error = resources.getText(R.string.AdminAnnouncementTypeError)
                    isDone = false
                }
                if(imageUri.isNullOrEmpty() || imageUri.isNullOrBlank()) {
                    Snackbar.make(findViewById(R.id.AdminActivity_FloatingActionButton_Add),  resources.getText(R.string.AdminAnnouncementImageError),Snackbar.LENGTH_LONG).show()
                    isDone = false
                }

                if(isDone) {
                    try {
                        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm aa")
                        val b = admin.addAnnouncement(Announcement(imageUri, sdf.format(Date()), announcementType, announcement))
                        if(b){
                            Toast.makeText(baseContext, resources.getText(R.string.AdminAddedSuccessfully), Toast.LENGTH_SHORT).show()
                            doneToolbar.isVisible = false
                            layoutContainerAddToAnnouncement.visibility = View.GONE
                            clearLayout2()
                        }

                    }catch (e:Exception){Snackbar.make(findViewById(R.id.AdminActivity_FloatingActionButton_Add), "${e.message}", Snackbar.LENGTH_LONG).show()}
                }
            }
        }
        return true
    }

    private fun clearLayout1(){
        this.name.text = null
        this.ingredients.text = null
        this.maxQuantity.text = null
        this.discount.text = null
        adapter.reset()
        adapter2.reset()
    }

    private fun clearLayout2(){
        announcement.text = null
        announcementType.text = null
        announcementImageEdit.visibility = View.GONE
        announcementImageDelete.visibility = View.GONE
        announcementImageAdd.visibility = View.VISIBLE
        announcementImageUri.text = null
    }

}

class SizesAdapter(private val listView: ListView) : BaseAdapter(){
    private val listOfSizes = ArrayList<SizeAndPrice>()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        if(view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.add_to_menu_add_sizes, null, false)
        }
        val size : EditText = view!!.findViewById(R.id.AddSize_Size)
        val price : EditText = view.findViewById(R.id.AddSize_Price)
        val add : ImageView = view.findViewById(R.id.AddSize_ImageView_Add)
        val edit : ImageView = view.findViewById(R.id.AddSize_ImageView_Edit)
        val done : ImageView = view.findViewById(R.id.AddSize_ImageView_Done)
        val delete: ImageView = view.findViewById(R.id.AddSize_ImageView_Delete)

        if(position<listOfSizes.size){
            size.setText(listOfSizes[position].size())
            price.setText(listOfSizes[position].price().toString())
        }
        else{
            size.text = null
            price.text = null
            size.isEnabled = true
            price.isEnabled = true
            add.visibility = View.VISIBLE
            edit.visibility = View.GONE
            done.visibility = View.GONE
            delete.visibility = View.GONE
        }

        add.setOnClickListener {
            if (!size.text.toString().isBlank() && size.text.toString().isNotEmpty() && !price.text.toString().isBlank() && price.text.toString().isNotEmpty() && price.text.toString().toFloat() > 0.0) {
                listOfSizes.add(SizeAndPrice(size.text.toString(), price.text.toString().toFloat()))
                notifyDataSetChanged()
                val p = listView.layoutParams
                p.height += 75
                listView.layoutParams = p
                add.visibility = View.GONE
                edit.visibility = View.VISIBLE
                delete.visibility = View.VISIBLE
                size.isEnabled = false
                price.isEnabled = false
            }
        }

        edit.setOnClickListener {
            done.visibility = View.VISIBLE
            edit.visibility = View.GONE
            delete.visibility = View.GONE
            size.isEnabled = true
            price.isEnabled = true
        }

        done.setOnClickListener {
            if (!size.text.toString().isBlank() && size.text.toString().isNotEmpty() && !price.text.toString().isBlank() && price.text.toString().isNotEmpty() && price.text.toString().toFloat() > 0.0) {
                done.visibility = View.GONE
                edit.visibility = View.VISIBLE
                delete.visibility = View.VISIBLE
                size.isEnabled = false
                price.isEnabled = false
                listOfSizes[position].price(price.text.toString().toFloat())
                listOfSizes[position].size(size.text.toString())
                notifyDataSetChanged()
            }
        }

        delete.setOnClickListener {
            listOfSizes.removeAt(position)
            val p = listView.layoutParams
            p.height -= 75
            listView.layoutParams = p
            notifyDataSetChanged()
        }
        return view
    }

    override fun getCount(): Int {
        return listOfSizes.size+1
    }

    fun getSizes():ArrayList<SizeAndPrice>{
        return listOfSizes
    }

    fun reset(){
        this.listOfSizes.clear()
        val p = listView.layoutParams
        p.height = 75
        listView.layoutParams = p
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

}

class ImagesAdapter(private val listView: ListView) : BaseAdapter(){
    private val listOfImages = ArrayList<String>()
    private lateinit var listener : ImageListener
    private lateinit var add:ImageView
    private lateinit var edit:ImageView
    private lateinit var delete:ImageView
    interface ImageListener{
        fun addImage()
        fun editImage(position: Int)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        try{listener = parent?.context as ImageListener}
        catch (e:Exception){throw Exception("You don't implements from ImageListener")}

        var view = convertView
        if(view == null)
            view = LayoutInflater.from(parent.context).inflate(R.layout.add_to_menu_add_images, null, false)
        val image :TextView= view!!.findViewById(R.id.AddImage_TextView_Image)
        add = view.findViewById(R.id.AddImage_ImageView_Add)
        edit = view.findViewById(R.id.AddImage_ImageView_Edit)
        delete = view.findViewById(R.id.AddImage_ImageView_Delete)
        if(position < listOfImages.size) {
            image.text = listOfImages[position]
        }
        else{
            image.text = null
            edit.visibility = View.GONE
            delete.visibility = View.GONE
            add.visibility = View.VISIBLE
        }

        add.setOnClickListener {
            listener.addImage()
            val p = listView.layoutParams
            p.height += 75
            listView.layoutParams = p
        }

        edit.setOnClickListener {
            listener.editImage(position)
        }

        delete.setOnClickListener {
            listOfImages.removeAt(position)
            val p = listView.layoutParams
            p.height -= 75
            notifyDataSetChanged()
        }

        return view
    }

    override fun getCount(): Int {
        return listOfImages.size+1
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    fun addImage(image: String) {
       listOfImages.add(image)
        add.visibility = View.GONE
        edit.visibility = View.VISIBLE
        delete.visibility = View.VISIBLE
        notifyDataSetChanged()
    }

    fun editImage(image: String, position: Int){
        listOfImages[position] = image
        notifyDataSetChanged()
    }

    fun getImages():ArrayList<String>{
        return listOfImages
    }

    fun reset(){
        this.listOfImages.clear()
        val p = listView.layoutParams
        p.height = 75
        listView.layoutParams = p
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

}