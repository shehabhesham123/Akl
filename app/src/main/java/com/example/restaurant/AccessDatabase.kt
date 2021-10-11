package com.example.restaurant

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

private const val DATABASE_NAME = "com.example.restaurant.database"
private const val VERSION = 1
private const val CLIENT_TABLE_NAME = "Client"
private const val CLIENT_USERNAME_TEXT_COLUMN_NAME = "username"
private const val CLIENT_PHONE_INT_COLUMN_NAME = "phone"
private const val CLIENT_ADDRESS_TEXT_COLUMN_NAME = "address"
private const val CLIENT_VISA_INT_COLUMN_NAME = "visa"
private const val ANNOUNCEMENT_TABLE_NAME = "Announcement"
private const val ANNOUNCEMENT_ID_INT_COLUMN_NAME = "id"
private const val ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME = "announcement"
private const val ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME = "_date"
private const val ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME = "type"
private const val ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME = "image"
private const val ITEM_TABLE_NAME = "MenuItem"
private const val ITEM_ID_INT_COLUMN_NAME = "id"
private const val ITEM_NAME_TEXT_COLUMN_NAME = "name"
private const val ITEM_DISCOUNT_REAL_COLUMN_NAME = "discount"
private const val ITEM_INGREDIENTS_TEXT_COLUMN_NAME = "ingredients"
private const val ITEM_MAX_QUANTITY_INT_COLUMN_NAME = "maxQuantity"
private const val ITEM_TYPE_TEXT_COLUMN_NAME = "type"
private const val INTERACTION_TABLE_NAME = "Interaction"
private const val INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME = "username "
private const val INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME = "id"
private const val INTERACTION_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME = "FK_Interaction_Username_Client"
private const val INTERACTION_FOREIGN_KEY_ANNOUNCEMENT_ID_INT_COLUMN_NAME = "FK_Interaction_Id_Announcement"
private const val RATE_APP_TABLE_NAME = "RateApp"
private const val RATE_APP_RATE_INT_COLUMN_NAME = "rate"
private const val RATE_APP_DATE_TEXT_COLUMN_NAME = "_date"
private const val RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME = "username"
private const val RATE_APP_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME = "FK_RateApp_username_Client"
private const val FEEDBACK_TABLE_NAME = "Feedback"
private const val FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME = "isHappy"
private const val FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME = "experience"
private const val FEEDBACK_DATE_TEXT_COLUMN_NAME = "_date"
private const val FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME = "username"
private const val FEEDBACK_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME = "FK_Feedback_username_Client"
private const val RATE_TABLE_NAME = "Rate"
private const val RATE_RATE_REAL_COLUMN_NAME = "rate"
private const val RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME = "username"
private const val RATE_ITEM_ID_INT_COLUMN_NAME = "id"
private const val RATE_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME = "FK_Rate_Username_Client"
private const val RATE_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME = "FK_Rate_Id_MenuItem"
private const val SIZE_PRICE_TABLE_NAME = "SizePrice"
private const val SIZE_PRICE_SIZE_TEXT_COLUMN_NAME = "size"
private const val SIZE_PRICE_PRICE_REAL_COLUMN_NAME = "price"
private const val SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME = "id"
private const val SIZE_PRICE_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME = "FK_SizePrice_Id_MenuItem"
private const val ITEM_IMAGES_TABLE_NAME = "ItemImages"
private const val ITEM_IMAGES_IMAGE_STRING_COLUMN_NAME = "image"
private const val ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME = "id"
private const val ITEM_IMAGES_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME = "FK_ItemImages_Id_MenuItem"
private const val ORDER_TABLE_NAME = "OrderTable"
private const val ORDER_ID_INT_COLUMN_NAME = "id"
private const val ORDER_DURATION_INT_COLUMN_NAME = "duration"
private const val ORDER_DATE_TEXT_COLUMN_NAME = "_date"
private const val ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME = "username"
private const val ORDER_CLIENT_FOREIGN_KEY_USERNAME_TEXT_COLUMN_NAME = "FK_OrderTable_Username_Client"
private const val ORDER_ITEMS_TABLE_NAME = "OrderItems"
private const val ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME = "size"
private const val ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME = "quantity"
private const val ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME = "itemID"
private const val ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME = "orderID"
private const val ORDER_ITEMS_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME = "FK_OrderItems_MenuItem_id"
private const val ORDER_ITEMS_FOREIGN_KEY_ORDER_ID_INT_COLUMN_NAME = "FK_OrderItems_OrderTable_id"

class AccessDatabase private constructor(context: Context) {

    private  var db : SQLiteDatabase
    private var open : SQLiteOpenHelper? = null

    init {
        open = Database(context)
        db = (this.open as SQLiteOpenHelper).writableDatabase
    }

    companion object{
        private var instance : AccessDatabase? = null
        fun getInstance(context: Context):AccessDatabase{
            synchronized(this) {
                if (instance == null)
                    instance = AccessDatabase(context)
                return instance as AccessDatabase
            }
        }
    }

    private class Database(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

        override fun onCreate(db: SQLiteDatabase?) {
            checkUpdate(db, 0)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            checkUpdate(db, oldVersion)
        }

        private fun checkUpdate(db: SQLiteDatabase?, oldVersion: Int){
            Toast.makeText(context,"db1",Toast.LENGTH_SHORT).show()
            if(oldVersion < 1){
                version1(db)
            }
        }

        private fun version1(db: SQLiteDatabase?){
            val sqlCreateClientTable = "create table $CLIENT_TABLE_NAME (" +
                    "$CLIENT_USERNAME_TEXT_COLUMN_NAME text not null primary key, " +
                    "$CLIENT_PHONE_INT_COLUMN_NAME integer not null, " +
                    "$CLIENT_ADDRESS_TEXT_COLUMN_NAME text not null, " +
                    "$CLIENT_VISA_INT_COLUMN_NAME integer) "

            val sqlCreateAnnouncementTable = "create table $ANNOUNCEMENT_TABLE_NAME (" +
                    "$ANNOUNCEMENT_ID_INT_COLUMN_NAME integer primary key autoincrement, " +
                    "$ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME text not null, " +
                    "$ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME text not null, " +
                    "$ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME text not null, " +
                    "$ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME text not null )"

            val sqlCreateItemTable = "create table $ITEM_TABLE_NAME (" +
                    "$ITEM_ID_INT_COLUMN_NAME integer primary key autoincrement, " +
                    "$ITEM_NAME_TEXT_COLUMN_NAME text not null, " +
                    "$ITEM_INGREDIENTS_TEXT_COLUMN_NAME text not null, " +
                    "$ITEM_DISCOUNT_REAL_COLUMN_NAME real not null, " +
                    "$ITEM_MAX_QUANTITY_INT_COLUMN_NAME integer, " +
                    "$ITEM_TYPE_TEXT_COLUMN_NAME text not null )"

            val sqlCreateInteractionTable = "create table $INTERACTION_TABLE_NAME (" +
                    "$INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME text not null , " +
                    "$INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME integer not null , " +
                    "CONSTRAINT $INTERACTION_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME FOREIGN key ($INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME) references $CLIENT_TABLE_NAME ($CLIENT_USERNAME_TEXT_COLUMN_NAME), " +
                    "CONSTRAINT $INTERACTION_FOREIGN_KEY_ANNOUNCEMENT_ID_INT_COLUMN_NAME FOREIGN key ($INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME) references $ANNOUNCEMENT_TABLE_NAME ($ANNOUNCEMENT_ID_INT_COLUMN_NAME )  )"

            val sqlCreateRateAppTable = "create table $RATE_APP_TABLE_NAME (" +
                    "$RATE_APP_RATE_INT_COLUMN_NAME Int not null, " +
                    "$RATE_APP_DATE_TEXT_COLUMN_NAME text not null, " +
                    "$RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME text primary key ," +
                    "CONSTRAINT $RATE_APP_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME FOREIGN key ($RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME) references $CLIENT_TABLE_NAME ($CLIENT_USERNAME_TEXT_COLUMN_NAME ) )"

            val sqlCreateFeedbackTable = "create table $FEEDBACK_TABLE_NAME (" +
                    "$FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME numeric not null, " +
                    "$FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME text not null, " +
                    "$FEEDBACK_DATE_TEXT_COLUMN_NAME text not null, " +
                    "$FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME text not null ," +
                    "CONSTRAINT $FEEDBACK_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME FOREIGN key ($FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME) references $CLIENT_TABLE_NAME ($CLIENT_USERNAME_TEXT_COLUMN_NAME ))"

            val sqlCreateRateTable = "create table $RATE_TABLE_NAME (" +
                    "$RATE_RATE_REAL_COLUMN_NAME real not null, " +
                    "$RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME text not null  , " +
                    "$RATE_ITEM_ID_INT_COLUMN_NAME integer not null  ," +
                    "primary key ($RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME, $RATE_ITEM_ID_INT_COLUMN_NAME)," +
                    "CONSTRAINT $RATE_FOREIGN_KEY_CLIENT_USERNAME_TEXT_COLUMN_NAME FOREIGN key ($RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME) references $CLIENT_TABLE_NAME ($CLIENT_USERNAME_TEXT_COLUMN_NAME )," +
                    "CONSTRAINT $RATE_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME FOREIGN key ($RATE_ITEM_ID_INT_COLUMN_NAME) references $ITEM_TABLE_NAME ($ITEM_ID_INT_COLUMN_NAME))"

            val sqlCreateSizePriceTable = "create table $SIZE_PRICE_TABLE_NAME (" +
                    "$SIZE_PRICE_SIZE_TEXT_COLUMN_NAME text not null, " +
                    "$SIZE_PRICE_PRICE_REAL_COLUMN_NAME real not null, " +
                    "$SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME integer not null," +
                    "primary key($SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME, $SIZE_PRICE_SIZE_TEXT_COLUMN_NAME)," +
                    "CONSTRAINT $SIZE_PRICE_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME FOREIGN key ($SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME) references $ITEM_TABLE_NAME ($ITEM_ID_INT_COLUMN_NAME) )"

            val sqlCreateItemImagesTable = "create table $ITEM_IMAGES_TABLE_NAME (" +
                    "$ITEM_IMAGES_IMAGE_STRING_COLUMN_NAME text not null, " +
                    "$ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME integer not null ," +
                    "CONSTRAINT $ITEM_IMAGES_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME FOREIGN key ($ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME)references $ITEM_TABLE_NAME ($ITEM_ID_INT_COLUMN_NAME) )"

            val sqlCreateOrderTable = "create table $ORDER_TABLE_NAME (" +
                    "$ORDER_ID_INT_COLUMN_NAME integer primary key autoincrement, " +
                    "$ORDER_DATE_TEXT_COLUMN_NAME text not null, " +
                    "$ORDER_DURATION_INT_COLUMN_NAME integer not null, " +
                    "$ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME text not null ," +
                    "CONSTRAINT $ORDER_CLIENT_FOREIGN_KEY_USERNAME_TEXT_COLUMN_NAME FOREIGN key ($ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME) references $CLIENT_TABLE_NAME ($CLIENT_USERNAME_TEXT_COLUMN_NAME) )"

            val sqlCreateOrderItemsTable = "create table $ORDER_ITEMS_TABLE_NAME (" +
                    "$ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME text not null, " +
                    "$ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME integer not null, " +
                    "$ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME integer not null , " +
                    "$ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME integer not null ," +
                    "primary key($ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME, $ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME, $ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME)" +
                    "CONSTRAINT $ORDER_ITEMS_FOREIGN_KEY_ITEM_ID_INT_COLUMN_NAME FOREIGN key ($ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME) references $ITEM_TABLE_NAME ($ITEM_ID_INT_COLUMN_NAME)," +
                    "CONSTRAINT $ORDER_ITEMS_FOREIGN_KEY_ORDER_ID_INT_COLUMN_NAME FOREIGN key ($ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME) references $ORDER_TABLE_NAME ($ORDER_ID_INT_COLUMN_NAME))"

            db?.execSQL(sqlCreateClientTable)
            db?.execSQL(sqlCreateAnnouncementTable)
            db?.execSQL(sqlCreateInteractionTable)
            db?.execSQL(sqlCreateItemTable)
            db?.execSQL(sqlCreateSizePriceTable)
            db?.execSQL(sqlCreateItemImagesTable)
            db?.execSQL(sqlCreateRateTable)
            db?.execSQL(sqlCreateFeedbackTable)
            db?.execSQL(sqlCreateRateAppTable)
            db?.execSQL(sqlCreateOrderTable)
            db?.execSQL(sqlCreateOrderItemsTable)

            testInsertClient(db,"Shehab","Egypt,luxor",1063248175,962184)
        }

        private fun testInsertAnnouncement(db: SQLiteDatabase?, announcement: String, type: String, data:String, image: String){
            val contentValues = ContentValues()
            contentValues.put(ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME,announcement)
            contentValues.put(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME,type)
            contentValues.put(ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME,data)
            contentValues.put(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME,image)
            db?.insert(ANNOUNCEMENT_TABLE_NAME,null,contentValues)
        }

        private fun testInsertFeedback(db: SQLiteDatabase?, isHappy : Boolean , experience:String, date: String, username: String){
            val contentValues = ContentValues()
            contentValues.put(FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME,experience)
            contentValues.put(FEEDBACK_DATE_TEXT_COLUMN_NAME,date)
            contentValues.put(FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME,isHappy)
            contentValues.put(FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            db?.insert(FEEDBACK_TABLE_NAME,null,contentValues)
        }

        private fun testInsertClient(db: SQLiteDatabase?, username: String, address: String, phone:Int, visa:Int):Boolean{
            val contentValues = ContentValues()
            contentValues.put(CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            contentValues.put(CLIENT_ADDRESS_TEXT_COLUMN_NAME,address)
            contentValues.put(CLIENT_PHONE_INT_COLUMN_NAME,phone)
            contentValues.put(CLIENT_VISA_INT_COLUMN_NAME,visa)
            return db?.insert(CLIENT_TABLE_NAME, null, contentValues)!= -1L
        }

        private fun testInsertInteraction(db: SQLiteDatabase?, username: String,id: Int){
            val contentValues = ContentValues()
            contentValues.put(INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            contentValues.put(INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME,id)
            db?.insert(INTERACTION_TABLE_NAME,null,contentValues)
        }

        private fun testInsertItem(db: SQLiteDatabase?, name: String, discount:Float, ingredients:String, maxQuantity:Int?, type:String):Boolean{
            val contentValues = ContentValues()
            contentValues.put(ITEM_NAME_TEXT_COLUMN_NAME,name)
            contentValues.put(ITEM_INGREDIENTS_TEXT_COLUMN_NAME,ingredients)
            contentValues.put(ITEM_DISCOUNT_REAL_COLUMN_NAME,discount)
            contentValues.put(ITEM_MAX_QUANTITY_INT_COLUMN_NAME,maxQuantity)
            contentValues.put(ITEM_TYPE_TEXT_COLUMN_NAME,type)
            return db?.insert(ITEM_TABLE_NAME, null, contentValues) != -1L
        }

        private fun testInsertRate(db: SQLiteDatabase?, rate:Float, username: String, id: Int):Boolean{
            val contentValues = ContentValues()
            contentValues.put(RATE_RATE_REAL_COLUMN_NAME,rate)
            contentValues.put(RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            contentValues.put(RATE_ITEM_ID_INT_COLUMN_NAME,id)
            return db?.insert(RATE_TABLE_NAME, null, contentValues) != -1L
        }

        private fun testInsertSizes(db:SQLiteDatabase?, id:Int, size:String, price: Float):Boolean{
            val contentValues = ContentValues()
            contentValues.put(SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME, id)
            contentValues.put(SIZE_PRICE_PRICE_REAL_COLUMN_NAME, price)
            contentValues.put(SIZE_PRICE_SIZE_TEXT_COLUMN_NAME, size)
            return db?.insert(SIZE_PRICE_TABLE_NAME,null,contentValues) != -1L
        }

        private fun testInsertImages(db:SQLiteDatabase?, id: Int, image:Int):Boolean{
            val contentValues = ContentValues()
            contentValues.put(ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME, id)
            contentValues.put(ITEM_IMAGES_IMAGE_STRING_COLUMN_NAME, image)
            return db?.insert(ITEM_IMAGES_TABLE_NAME,null,contentValues) != -1L
        }

        private fun testInsertRateApp(db: SQLiteDatabase?, rate: Int, date: String, username: String):Boolean{
            val contentValues = ContentValues()
            contentValues.put(RATE_APP_RATE_INT_COLUMN_NAME,rate)
            contentValues.put(RATE_APP_DATE_TEXT_COLUMN_NAME,date)
            contentValues.put(RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            return db?.insert(RATE_APP_TABLE_NAME,null,contentValues) != -1L
        }

        private fun testInsertOrderTable(db: SQLiteDatabase?,date: String, duration: Int, username: String):Boolean{
            val contentValues = ContentValues()
            contentValues.put(ORDER_DATE_TEXT_COLUMN_NAME,date)
            contentValues.put(ORDER_DURATION_INT_COLUMN_NAME,duration)
            contentValues.put(ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME,username)
            return db?.insert(ORDER_TABLE_NAME,null,contentValues) != -1L
        }

        private fun testInsertOrderItemsTable(db: SQLiteDatabase?, size: String, quantity:Int, orderID:Int, itemID:Int):Boolean{
            val contentValues = ContentValues()
            contentValues.put(ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME,size)
            contentValues.put(ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME,quantity)
            contentValues.put(ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME,orderID)
            contentValues.put(ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME,itemID)
            return db?.insert(ORDER_ITEMS_TABLE_NAME,null,contentValues) !=-1L
        }
    }


    //////////////////////////////////////////   Customer    ///////////////////////////////////////////

    fun customer(client: Client):Boolean{
        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(CLIENT_USERNAME_TEXT_COLUMN_NAME, client.username())
            contentValues.put(CLIENT_PHONE_INT_COLUMN_NAME, client.phoneNumber())
            contentValues.put(CLIENT_VISA_INT_COLUMN_NAME, client.visaNumber())
            contentValues.put(CLIENT_ADDRESS_TEXT_COLUMN_NAME, client.address())
            val b = db.insert(CLIENT_TABLE_NAME, null, contentValues)

            return b != -1L
        }
    }

    fun interaction(username: String, announcementID: Int):Boolean {
        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME, username)
            contentValues.put(INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME, announcementID)
            return db.insert(INTERACTION_TABLE_NAME, null, contentValues) != -1L
        }
    }

    fun customer(username:String):Client{
        synchronized(this) {
            val cursor: Cursor = db.rawQuery(
                "select * from $CLIENT_TABLE_NAME where $CLIENT_USERNAME_TEXT_COLUMN_NAME = ?",
                arrayOf(username)
            )
            var client: Client? = null
            if (cursor.moveToFirst()) {
                val phoneNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_PHONE_INT_COLUMN_NAME))
                val address =
                    cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS_TEXT_COLUMN_NAME))
                val visaNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_VISA_INT_COLUMN_NAME))
                client = Client(username, address, phoneNumber.toString(), visaNumber.toString())
            }
            return client!!
        }
    }

    fun customer(phoneNumber:Int):Client{
        synchronized(this) {
            val cursor: Cursor = db.rawQuery(
                "select * from $CLIENT_TABLE_NAME where $CLIENT_PHONE_INT_COLUMN_NAME = ?",
                arrayOf("$phoneNumber")
            )
            var client: Client? = null
            if (cursor.moveToFirst()) {
                val username =
                    cursor.getString(cursor.getColumnIndex(CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val address =
                    cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS_TEXT_COLUMN_NAME))
                val visaNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_VISA_INT_COLUMN_NAME))
                client = Client(username, address, phoneNumber.toString(), visaNumber.toString())
            }
            return client!!
        }
    }

    fun customers():ArrayList<Client>{

        synchronized(this) {
            val cursor: Cursor = db.rawQuery("select * from $CLIENT_TABLE_NAME", null)
            val list: ArrayList<Client> = ArrayList()
            while (cursor.moveToNext()) {
                val username =
                    cursor.getString(cursor.getColumnIndex(CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val phoneNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_PHONE_INT_COLUMN_NAME))
                val address =
                    cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS_TEXT_COLUMN_NAME))
                val visaNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_VISA_INT_COLUMN_NAME))
                list.add(Client(username, address, phoneNumber.toString(), visaNumber.toString()))
            }
            return list
        }
    }

    fun customers(address:String):ArrayList<Client>{
        synchronized(this) {
            val cursor: Cursor = db.rawQuery(
                "select * from $CLIENT_TABLE_NAME where $CLIENT_ADDRESS_TEXT_COLUMN_NAME like ?",
                arrayOf("%$address%")
            )
            val list: ArrayList<Client> = ArrayList()
            while (cursor.moveToNext()) {
                val username =
                    cursor.getString(cursor.getColumnIndex(CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val phoneNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_PHONE_INT_COLUMN_NAME))
                val visaNumber = cursor.getInt(cursor.getColumnIndex(CLIENT_VISA_INT_COLUMN_NAME))
                list.add(Client(username, address, phoneNumber.toString(), visaNumber.toString()))
            }
            return list
        }
    }

    fun customerInteraction(username: String):ArrayList<Announcement>{
        synchronized(this) {
            val list = ArrayList<Announcement>()
            val cursor = db.rawQuery(
                "SELECT $ANNOUNCEMENT_TABLE_NAME.$ANNOUNCEMENT_ID_INT_COLUMN_NAME, $ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME, $ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME, $ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME, $ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME " +
                        "from $ANNOUNCEMENT_TABLE_NAME, $INTERACTION_TABLE_NAME " +
                        "WHERE $INTERACTION_TABLE_NAME.$INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME = $ANNOUNCEMENT_TABLE_NAME.$ANNOUNCEMENT_ID_INT_COLUMN_NAME and $INTERACTION_TABLE_NAME.$INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME = ? ",
                arrayOf(username)
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ANNOUNCEMENT_ID_INT_COLUMN_NAME))
                val announcement: String = cursor.getString(
                    cursor.getColumnIndex(ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME)
                )
                val date: String =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME))
                val type: String =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME))
                val image: String =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME))
                list.add(Announcement(id, image, date, type, announcement))
            }
            return list
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////   Announcement   ////////////////////////////////////////////

    fun announcement(announcement: Announcement):Boolean{
        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(
                ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME,
                announcement.announcement()
            )
            contentValues.put(ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME, announcement.date())
            contentValues.put(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME, announcement.type())
            contentValues.put(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME, announcement.image())
            val b = db.insert(ANNOUNCEMENT_TABLE_NAME, null, contentValues)
            return b != -1L
        }
    }

    fun announcement(id:Int):Announcement{
        synchronized(this) {
            val cursor = db.rawQuery(
                "select * from $ANNOUNCEMENT_TABLE_NAME where $ANNOUNCEMENT_ID_INT_COLUMN_NAME = ?",
                arrayOf(id.toString())
            )
            var announcementObj: Announcement? = null
            if (cursor.moveToFirst()) {
                val image = cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME))
                val date =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME))
                val announcement = cursor.getString(
                    cursor.getColumnIndex(ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME)
                )
                val type =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME))
                announcementObj = Announcement(id, image, date, type, announcement)
            }
            return announcementObj!!
        }
    }

    fun announcements():ArrayList<Announcement>{
        synchronized(this) {
            val list = ArrayList<Announcement>()
            val cursor = db.rawQuery("select * from $ANNOUNCEMENT_TABLE_NAME", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ANNOUNCEMENT_ID_INT_COLUMN_NAME))
                val image = cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME))
                val date =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME))
                val announcement = cursor.getString(
                    cursor.getColumnIndex(ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME)
                )
                val type =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME))
                list.add(Announcement(id, image, date, type, announcement))
            }
            return list
        }
    }

    fun announcements(date:String):ArrayList<Announcement>{
        synchronized(this) {
            val list = ArrayList<Announcement>()
            val cursor = db.rawQuery(
                "select * from $ANNOUNCEMENT_TABLE_NAME where $ANNOUNCEMENT_DATE_TEXT_COLUMN_NAME = ?",
                arrayOf(date)
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ANNOUNCEMENT_ID_INT_COLUMN_NAME))
                val image = cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_IMAGE_STRING_COLUMN_NAME))
                val announcement = cursor.getString(
                    cursor.getColumnIndex(ANNOUNCEMENT_ANNOUNCEMENT_TEXT_COLUMN_NAME)
                )
                val type =
                    cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_TYPE_TEXT_COLUMN_NAME))
                list.add(Announcement(id, image, date, type, announcement))
            }
            return list
        }
    }

    fun announcementInteraction(id: Int):ArrayList<Client>{
        synchronized(this) {
            val list = ArrayList<Client>()
            val cursor = db.rawQuery(
                "SELECT $CLIENT_TABLE_NAME.$CLIENT_USERNAME_TEXT_COLUMN_NAME, $CLIENT_PHONE_INT_COLUMN_NAME, $CLIENT_ADDRESS_TEXT_COLUMN_NAME, $CLIENT_VISA_INT_COLUMN_NAME " +
                        "from $INTERACTION_TABLE_NAME, $CLIENT_TABLE_NAME " +
                        "WHERE $CLIENT_TABLE_NAME.$CLIENT_USERNAME_TEXT_COLUMN_NAME = $INTERACTION_TABLE_NAME.$INTERACTION_CLIENT_USERNAME_TEXT_COLUMN_NAME " +
                        "and $INTERACTION_TABLE_NAME.$INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME = ?",
                arrayOf(id.toString())
            )
            while (cursor.moveToNext()) {
                val username =
                    cursor.getString(cursor.getColumnIndex(CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val phone = cursor.getInt(cursor.getColumnIndex(CLIENT_PHONE_INT_COLUMN_NAME))
                val address =
                    cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS_TEXT_COLUMN_NAME))
                var visa: Int? = cursor.getInt(cursor.getColumnIndex(CLIENT_VISA_INT_COLUMN_NAME))
                if (visa == 0) visa = null
                list.add(Client(username, address, phone.toString(), visa.toString()))
            }
            return list
        }
    }

    fun announcementViewers(id:Int):Int{

        synchronized(this) {
            var num = 0
            val cursor = db.rawQuery(
                "SELECT COUNT(*) from $INTERACTION_TABLE_NAME WHERE $INTERACTION_ANNOUNCEMENT_ID_INT_COLUMN_NAME = ? ",
                arrayOf(id.toString())
            )
            if (cursor.moveToFirst())
                num = cursor.getInt(cursor.getColumnIndex("COUNT(*)"))
            return num
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////    Item  //////////////////////////////////////////////////////

    private fun itemImages(images: ArrayList<String>, itemID: Int):Boolean{

        synchronized(this) {
            if (images.size == 0)
                return false
            var b = true
            for (i in images) {
                val contentValues = ContentValues()
                contentValues.put(ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME, itemID)
                contentValues.put(ITEM_IMAGES_IMAGE_STRING_COLUMN_NAME, i)
                b = b and (db.insert(ITEM_IMAGES_TABLE_NAME, null, contentValues) != -1L)
            }
            return b
        }
    }

    private fun itemSizePrice(itemID: Int,sizeAndPrice: ArrayList<SizeAndPrice>):Boolean{

        synchronized(this) {
            if (sizeAndPrice.size == 0)
                return false
            var b = true
            for (i in sizeAndPrice) {
                val contentValues = ContentValues()
                contentValues.put(SIZE_PRICE_SIZE_TEXT_COLUMN_NAME, i.size())
                contentValues.put(SIZE_PRICE_PRICE_REAL_COLUMN_NAME, i.price())
                contentValues.put(SIZE_PRICE_ITEM_ID_INT_COLUMN_NAME, itemID)
                b = b and (db.insert(SIZE_PRICE_TABLE_NAME, null, contentValues) != -1L)
            }
            return b
        }
    }

    private fun itemImages(itemID: Int):ArrayList<String>{

        synchronized(this) {
            val listOfImages = ArrayList<String>()
            val cursor = db.rawQuery("select * from $ITEM_IMAGES_TABLE_NAME where $ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME = ? ", arrayOf(itemID.toString()))
            while (cursor.moveToNext()) {
                listOfImages.add(cursor.getString(cursor.getColumnIndex(ITEM_IMAGES_IMAGE_STRING_COLUMN_NAME)))
            }
            if (listOfImages.size == 0)
                throw Exception()
            return listOfImages
        }
    }

    private fun itemSizePrice(itemID: Int):ArrayList<SizeAndPrice>{
        synchronized(this) {
            val listOfSizeAndPrice = ArrayList<SizeAndPrice>()
            val cursor = db.rawQuery(
                "select * from $SIZE_PRICE_TABLE_NAME where $ITEM_IMAGES_ITEM_ID_INT_COLUMN_NAME = ? ",
                arrayOf(itemID.toString())
            )
            while (cursor.moveToNext()) {
                val size = cursor.getString(cursor.getColumnIndex(SIZE_PRICE_SIZE_TEXT_COLUMN_NAME))
                val price =
                    cursor.getFloat(cursor.getColumnIndex(SIZE_PRICE_PRICE_REAL_COLUMN_NAME))
                listOfSizeAndPrice.add(SizeAndPrice(size, price))
            }
            if (listOfSizeAndPrice.size == 0)
                throw Exception()
            return listOfSizeAndPrice
        }
    }



    fun item(item:MenuItem):Boolean{

        synchronized(this) {
            val obj = RestaurantItem.getItem(item)
            val contentValues = ContentValues()
            contentValues.put(ITEM_TYPE_TEXT_COLUMN_NAME, item.itemType())
            contentValues.put(ITEM_NAME_TEXT_COLUMN_NAME, obj.name())
            contentValues.put(ITEM_MAX_QUANTITY_INT_COLUMN_NAME, obj.maxQuantity())
            contentValues.put(ITEM_DISCOUNT_REAL_COLUMN_NAME, obj.discount())
            contentValues.put(ITEM_INGREDIENTS_TEXT_COLUMN_NAME, obj.ingredients())
            val id = db.insert(ITEM_TABLE_NAME, null, contentValues).toInt()
            var b1 = true
            var b2 = true
            if (id != -1) {
                b1 = itemImages(obj.images(), id)
                b2 = itemSizePrice(id, obj.sizes())
            }

            return ((id != -1) and b1 and b2)
        }
    }

    fun itemRate(rate: Float, username: String, id: Int):Boolean{

        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(RATE_RATE_REAL_COLUMN_NAME, rate)
            contentValues.put(RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME, username)
            contentValues.put(RATE_ITEM_ID_INT_COLUMN_NAME, id)
            return db.insert(RATE_TABLE_NAME, null, contentValues) != 1L
        }
    }

    fun itemRate(username: String, id: Int):Pair<Boolean,Float>{


        synchronized(this) {
            val cursor = db.rawQuery(
                "select * from $RATE_TABLE_NAME where $RATE_ITEM_ID_INT_COLUMN_NAME = ? and $RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME = ? ",
                arrayOf(id.toString(), username)
            )
            var pair: Pair<Boolean, Float> = Pair(false, 0f)
            if (cursor.moveToFirst()) {
                val isRate = cursor.moveToFirst()
                val rate = cursor.getFloat(cursor.getColumnIndex(RATE_RATE_REAL_COLUMN_NAME))
                pair = Pair(isRate, rate)
            }
            return pair
        }
    }

    fun editItemRate(rate: Float, username: String, id: Int):Boolean{

        synchronized(this) {
            val b = this.itemRate(username, id)
            var flag = false
            if (b.first) {
                val contentValues = ContentValues()
                contentValues.put(RATE_RATE_REAL_COLUMN_NAME, rate)
                flag = db.update(
                    RATE_TABLE_NAME,
                    contentValues,
                    "$RATE_CLIENT_USERNAME_TEXT_COLUMN_NAME = ? and $RATE_ITEM_ID_INT_COLUMN_NAME = ? ",
                    arrayOf(username, id.toString())
                ) != 0
            }
            return flag
        }
    }

    fun item(id:Int):MenuItem{

        synchronized(this) {
            var menuItem: MenuItem? = null
            val cursor = db.rawQuery(
                "SELECT $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME, $ITEM_NAME_TEXT_COLUMN_NAME, $ITEM_INGREDIENTS_TEXT_COLUMN_NAME, $ITEM_DISCOUNT_REAL_COLUMN_NAME, $ITEM_MAX_QUANTITY_INT_COLUMN_NAME, $ITEM_TYPE_TEXT_COLUMN_NAME, avg($RATE_RATE_REAL_COLUMN_NAME) as\"Rate\", COUNT($RATE_RATE_REAL_COLUMN_NAME) as\"NumberOfReviewers\" " +
                        "FROM $ITEM_TABLE_NAME left join $RATE_TABLE_NAME " +
                        "on $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME = $RATE_TABLE_NAME.$RATE_ITEM_ID_INT_COLUMN_NAME " +
                        "GROUP by $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME " +
                        "HAVING $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME = ?",
                arrayOf(id.toString())
            )
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndex(ITEM_NAME_TEXT_COLUMN_NAME))
                val discount = cursor.getFloat((cursor.getColumnIndex(ITEM_DISCOUNT_REAL_COLUMN_NAME)))
                val ingredients = cursor.getString(cursor.getColumnIndex(ITEM_INGREDIENTS_TEXT_COLUMN_NAME))
                val type = cursor.getString(cursor.getColumnIndex(ITEM_TYPE_TEXT_COLUMN_NAME))
                val rate = String.format("%.2f", cursor.getFloat(cursor.getColumnIndex("Rate"))).toFloat()
                val numberOfReviewers = cursor.getInt(cursor.getColumnIndex("NumberOfReviewers"))
                var maxQuantity: Int? = cursor.getInt(cursor.getColumnIndex(ITEM_MAX_QUANTITY_INT_COLUMN_NAME))
                if (maxQuantity == 0) maxQuantity = null
                val listOfSizeAndPrice = itemSizePrice(id)
                val listOfImages = itemImages(id)
                menuItem = RestaurantItem.getItem(
                    type,
                    name,
                    listOfImages,
                    discount,
                    rate,
                    numberOfReviewers,
                    ingredients,
                    listOfSizeAndPrice,
                    maxQuantity,
                    id
                )
            }
            return menuItem!!
        }
    }

    fun items(type:String):ArrayList<MenuItem>{

        synchronized(this) {
            var list: ArrayList<MenuItem> = ArrayList()

            val cursor = db.rawQuery(
                "SELECT $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME, $ITEM_NAME_TEXT_COLUMN_NAME, $ITEM_INGREDIENTS_TEXT_COLUMN_NAME, $ITEM_DISCOUNT_REAL_COLUMN_NAME, $ITEM_MAX_QUANTITY_INT_COLUMN_NAME, $ITEM_TYPE_TEXT_COLUMN_NAME, avg($RATE_RATE_REAL_COLUMN_NAME) as\"Rate\", COUNT($RATE_RATE_REAL_COLUMN_NAME) as\"NumberOfReviewers\" " +
                        "FROM $ITEM_TABLE_NAME left join $RATE_TABLE_NAME " +
                        "on $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME = $RATE_TABLE_NAME.$RATE_ITEM_ID_INT_COLUMN_NAME " +
                        "GROUP by $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME " +
                        "HAVING $ITEM_TABLE_NAME.$ITEM_TYPE_TEXT_COLUMN_NAME = ?", arrayOf(type)
            )
            while (cursor.moveToNext()) {

                val id = cursor.getInt(cursor.getColumnIndex(ITEM_ID_INT_COLUMN_NAME))
                val name = cursor.getString(cursor.getColumnIndex(ITEM_NAME_TEXT_COLUMN_NAME))
                val discount =
                    cursor.getFloat((cursor.getColumnIndex(ITEM_DISCOUNT_REAL_COLUMN_NAME)))
                val ingredients =
                    cursor.getString(cursor.getColumnIndex(ITEM_INGREDIENTS_TEXT_COLUMN_NAME))
                val rate =
                    String.format("%.1f", cursor.getFloat(cursor.getColumnIndex("Rate"))).toFloat()
                val numberOfReviewers = cursor.getInt(cursor.getColumnIndex("NumberOfReviewers"))
                var maxQuantity: Int? =
                    cursor.getInt(cursor.getColumnIndex(ITEM_MAX_QUANTITY_INT_COLUMN_NAME))
                if (maxQuantity == 0) maxQuantity = null
                val listOfSizeAndPrice = itemSizePrice(id)
                val listOfImages = itemImages(id)

                val item = RestaurantItem.getItem(
                    type,
                    name,
                    listOfImages,
                    discount,
                    rate,
                    numberOfReviewers,
                    ingredients,
                    listOfSizeAndPrice,
                    maxQuantity,
                    id
                )
                list.add(item)
            }
            return list
        }
    }

    fun itemsByName(name:String, type:String):ArrayList<MenuItem>{

        synchronized(this) {
            val list: ArrayList<MenuItem> = ArrayList()

            val cursor = db.rawQuery(
                "SELECT $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME, $ITEM_NAME_TEXT_COLUMN_NAME, $ITEM_INGREDIENTS_TEXT_COLUMN_NAME, $ITEM_DISCOUNT_REAL_COLUMN_NAME, $ITEM_MAX_QUANTITY_INT_COLUMN_NAME, $ITEM_TYPE_TEXT_COLUMN_NAME, avg($RATE_RATE_REAL_COLUMN_NAME) as\"Rate\", COUNT($RATE_RATE_REAL_COLUMN_NAME) as\"NumberOfReviewers\" " +
                        "FROM $ITEM_TABLE_NAME left join $RATE_TABLE_NAME " +
                        "on $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME = $RATE_TABLE_NAME.$RATE_ITEM_ID_INT_COLUMN_NAME " +
                        "GROUP by $ITEM_TABLE_NAME.$ITEM_ID_INT_COLUMN_NAME " +
                        "HAVING $ITEM_TABLE_NAME.$ITEM_NAME_TEXT_COLUMN_NAME like ? and $ITEM_TYPE_TEXT_COLUMN_NAME = ? ",
                arrayOf("%$name%", type)
            )
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(ITEM_NAME_TEXT_COLUMN_NAME))
                val id = cursor.getInt(cursor.getColumnIndex(ITEM_ID_INT_COLUMN_NAME))
                val discount =
                    cursor.getFloat((cursor.getColumnIndex(ITEM_DISCOUNT_REAL_COLUMN_NAME)))
                val ingredients =
                    cursor.getString(cursor.getColumnIndex(ITEM_INGREDIENTS_TEXT_COLUMN_NAME))
                val rate =
                    String.format("%.1f", cursor.getFloat(cursor.getColumnIndex("Rate"))).toFloat()
                val numberOfReviewers = cursor.getInt(cursor.getColumnIndex("NumberOfReviewers"))
                var maxQuantity: Int? =
                    cursor.getInt(cursor.getColumnIndex(ITEM_MAX_QUANTITY_INT_COLUMN_NAME))
                if (maxQuantity == 0) maxQuantity = null
                val listOfSizeAndPrice = itemSizePrice(id)
                val listOfImages = itemImages(id)

                val item = RestaurantItem.getItem(
                    type,
                    name,
                    listOfImages,
                    discount,
                    rate,
                    numberOfReviewers,
                    ingredients,
                    listOfSizeAndPrice,
                    maxQuantity,
                    id
                )
                list.add(item)
            }

            return list
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////    feedback   /////////////////////////////////////////////////

    fun feedback(feedBack: FeedBack):Boolean{

        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME, feedBack.isHappy())
            contentValues.put(
                FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME,
                feedBack.customer().username()
            )
            contentValues.put(FEEDBACK_DATE_TEXT_COLUMN_NAME, feedBack.date())
            contentValues.put(FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME, feedBack.experience())
            val b = db.insert(FEEDBACK_TABLE_NAME, null, contentValues) != -1L

            return b
        }
    }

    fun feedback():ArrayList<FeedBack>{

        synchronized(this) {
            val list = ArrayList<FeedBack>()
            val cursor = db.rawQuery("SELECT * from $FEEDBACK_TABLE_NAME", null)
            while (cursor.moveToNext()) {
                val isHappy =
                    cursor.getInt(cursor.getColumnIndex(FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME)) == 1
                val experience =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndex(FEEDBACK_DATE_TEXT_COLUMN_NAME))
                val username =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME))
                list.add(FeedBack(this.customer(username), isHappy, experience, date))
            }
            return list
        }
    }

    fun feedback(isHappy:Boolean):ArrayList<FeedBack>{

        synchronized(this) {
            val b = if (isHappy) 1 else 0
            val list = ArrayList<FeedBack>()
            val cursor = db.rawQuery(
                "SELECT * from $FEEDBACK_TABLE_NAME where $FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME = ? ",
                arrayOf("$b")
            )
            while (cursor.moveToNext()) {
                val experience =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndex(FEEDBACK_DATE_TEXT_COLUMN_NAME))
                val username =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME))
                list.add(FeedBack(this.customer(username), isHappy, experience, date))
            }

            return list
        }
    }

    fun feedback(date:String):ArrayList<FeedBack>{

        synchronized(this) {
            val list = ArrayList<FeedBack>()
            val cursor = db.rawQuery(
                "SELECT * from $FEEDBACK_TABLE_NAME where $FEEDBACK_DATE_TEXT_COLUMN_NAME = ? ",
                arrayOf(date)
            )
            while (cursor.moveToNext()) {
                val isHappy =
                    cursor.getInt(cursor.getColumnIndex(FEEDBACK_IS_HAPPY_NUMERIC_COLUMN_NAME)) == 1
                val experience =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_EXPERIENCE_TEXT_COLUMN_NAME))
                val username =
                    cursor.getString(cursor.getColumnIndex(FEEDBACK_CLIENT_USERNAME_TEXT_COLUMN_NAME))
                list.add(FeedBack(this.customer(username), isHappy, experience, date))
            }

            return list
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////    rateApp  ///////////////////////////////////////////////////

    fun rate(rating: Rating):Boolean{

        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(RATE_APP_RATE_INT_COLUMN_NAME, rating.rating())
            contentValues.put(RATE_APP_DATE_TEXT_COLUMN_NAME, rating.date())
            contentValues.put(
                RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME,
                rating.customerInfo().username()
            )
            return db.insert(RATE_APP_TABLE_NAME, null, contentValues) != -1L
        }
    }

    fun rate(username: String):Pair<Boolean,Int>{

        synchronized(this) {
            val cursor = db.rawQuery(
                "SELECT * FROM $RATE_APP_TABLE_NAME where $RATE_APP_CLIENT_USERNAME_TEXT_COLUMN_NAME = ?",
                arrayOf(username)
            )
            var b  = false
            var rate = 0
            if(cursor.moveToFirst()) {
                b = true
                rate = cursor.getInt(cursor.getColumnIndex(RATE_APP_RATE_INT_COLUMN_NAME))
            }
            return Pair(b, rate)
        }
    }

    fun appRate():Float{

        synchronized(this) {
            var totalRate = 0f
            val cursor = db.rawQuery(
                "SELECT avg($RATE_APP_RATE_INT_COLUMN_NAME) as \"rate\" FROM $RATE_APP_TABLE_NAME",
                null
            )

            if (cursor.moveToFirst())
                totalRate = cursor.getFloat(cursor.getColumnIndex("rate"))
            return totalRate
        }
    }

    fun appRate(date:String):Float{

        synchronized(this) {
            var totalRate = 0f
            val cursor = db.rawQuery(
                "SELECT avg($RATE_APP_RATE_INT_COLUMN_NAME) as \"rate\" FROM $RATE_APP_TABLE_NAME where $RATE_APP_DATE_TEXT_COLUMN_NAME = ? ",
                arrayOf("$date")
            )

            if (cursor.moveToFirst())
                totalRate = cursor.getFloat(cursor.getColumnIndex("rate"))
            return totalRate
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////  orders   /////////////////////////////////////////////////

    private fun find(sizeAndPrice: ArrayList<SizeAndPrice>?, size: String):Int{
        synchronized(this) {
            if (sizeAndPrice != null) {
                for (i in 0 until sizeAndPrice.size) {
                    if (sizeAndPrice[i].size() == size)
                        return i
                }
            }
            throw Exception("the size of item is not found in its sizes")
        }
    }

    private data class ItemInfo (val itemID: Int,val size: String, val quantity: Int)

    fun cartItem(orderID: Int):Pair<ArrayList<CartItem>,Float>{
        synchronized(this) {
            var totalCost = 0f
            val listOfItems = ArrayList<CartItem>()
            val cursorItems = db.rawQuery(
                "SELECT * from $ORDER_ITEMS_TABLE_NAME where $ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME = ? ",
                arrayOf(orderID.toString())
            )

            val listOfItemInfo = ArrayList<ItemInfo>()
            while (cursorItems.moveToNext()) {
                val itemId = cursorItems.getInt(
                    cursorItems.getColumnIndex(ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME)
                )
                val size = cursorItems.getString(
                    cursorItems.getColumnIndex(ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME)
                )
                val quantity = cursorItems.getInt(
                    cursorItems.getColumnIndex(ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME)
                )
                listOfItemInfo.add(ItemInfo(itemId,size,quantity))
            }
            val list : ArrayList<MenuItem> = ArrayList()
            for(i in listOfItemInfo) {
                list.add( this.item(i.itemID))
            }

            for(i in 0 until list.size) {
                val item = RestaurantItem.getItem(list[i])
                val sizePosition = find(item.sizes(), listOfItemInfo[i].size)
                totalCost += (item.sizes()[sizePosition].price() * listOfItemInfo[i].quantity)
                listOfItems.add(CartItem(list[i], sizePosition, listOfItemInfo[i].quantity))
            }
            return Pair(listOfItems, String.format("%.2f", totalCost).toFloat())
        }
    }

    private fun cartItem(size: String, quantity:Int, orderID:Int, itemID:Int):Boolean{

        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME, size)
            contentValues.put(ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME, quantity)
            contentValues.put(ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME, orderID)
            contentValues.put(ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME, itemID)
            val b = db.insert(ORDER_ITEMS_TABLE_NAME, null, contentValues) != -1L

            return b
        }
    }

    fun order(order: Order):Pair<Boolean,Int>{

        synchronized(this) {
            val contentValues = ContentValues()
            contentValues.put(ORDER_DATE_TEXT_COLUMN_NAME, order.date())
            contentValues.put(ORDER_DURATION_INT_COLUMN_NAME, order.duration())
            contentValues.put(ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME, order.customerInfo().username())
            val id = db.insert(ORDER_TABLE_NAME, null, contentValues).toInt()
            var b = true
            if (id != -1) {
                for (i in order.items()) {
                    val item = RestaurantItem.getItem(i.menuItem())
                    b = b and this.cartItem(item.sizes()[i.sizePosition()].size(), i.quantity(), id, item.id())
                }
            }
            return Pair(((id != -1) and b), id)
        }
    }

    fun order(id: Int):Order{

        synchronized(this) {
            var order: Order? = null
            val cursor = db.rawQuery(
                "SELECT * FROM $ORDER_TABLE_NAME where $ORDER_ID_INT_COLUMN_NAME = ?  ",
                arrayOf(id.toString())
            )
            if (cursor.moveToFirst()) {
                val date = cursor.getString(cursor.getColumnIndex(ORDER_DATE_TEXT_COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndex(ORDER_DURATION_INT_COLUMN_NAME))
                val username = cursor.getString(cursor.getColumnIndex(ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val pair = this.cartItem(id)
                order = Order(id, pair.second, this.customer(username), date, duration, pair.first)
            }

            return order!!
        }
    }

    fun checkOrders(username: String):ArrayList<Order>{

        synchronized(this) {
            val list = ArrayList<Order>()
            val cursor = db.rawQuery("SELECT * FROM $ORDER_TABLE_NAME where $ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME = ?", arrayOf(username))
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ORDER_ID_INT_COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndex(ORDER_DATE_TEXT_COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndex(ORDER_DURATION_INT_COLUMN_NAME))
                list.add(Order(id, date, duration))
            }
            return list
        }
    }

    fun orders(username:String):ArrayList<Order>{

        synchronized(this) {
            val list = ArrayList<Order>()
            val cursor = db.rawQuery(
                "SELECT * FROM $ORDER_TABLE_NAME where $ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME = ? ",
                arrayOf(username)
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ORDER_ID_INT_COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndex(ORDER_DATE_TEXT_COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndex(ORDER_DURATION_INT_COLUMN_NAME))
                list.add(Order(id, date, duration))
            }
            return list
        }
    }

    fun orders():ArrayList<Order>{

        synchronized(this) {
            val list = ArrayList<Order>()
            val cursor = db.rawQuery(
                "SELECT * FROM $ORDER_TABLE_NAME",null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ORDER_ID_INT_COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndex(ORDER_DATE_TEXT_COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndex(ORDER_DURATION_INT_COLUMN_NAME))
                list.add(Order(id, date, duration))
            }

            return list
        }
    }

    fun ordersByDate(date:String):ArrayList<Order>{

        synchronized(this) {
            val list = ArrayList<Order>()
            val cursor = db.rawQuery(
                "SELECT * FROM $ORDER_TABLE_NAME where $ORDER_DATE_TEXT_COLUMN_NAME = ? ",
                arrayOf(date)
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ORDER_ID_INT_COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndex(ORDER_DURATION_INT_COLUMN_NAME))
                val username = cursor.getString(cursor.getColumnIndex(ORDER_CLIENT_USERNAME_TEXT_COLUMN_NAME))
                val pair = cartItem(id)
                list.add(Order(id, pair.second, this.customer(username), date, duration, pair.first))
            }

            return list
        }
    }

    fun edit(orderID: Int, addedItems:ArrayList<CartItem>,deletedItems:ArrayList<CartItem>):Boolean{

        synchronized(this) {
            var b = true
            for (i in deletedItems) {
                val item = RestaurantItem.getItem(i.menuItem())
                val size = item.sizes()[i.sizePosition()].size()
                b = b and (db.delete(ORDER_ITEMS_TABLE_NAME, " $ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME = ? and $ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME = ? and $ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME = ?", arrayOf(orderID.toString(), item.id().toString(), size )) != 0)
            }
            if (b) {
                for (i in addedItems) {
                    val item = RestaurantItem.getItem(i.menuItem())
                    val contentValues = ContentValues()
                    contentValues.put(ORDER_ITEMS_ORDER_ID_INT_COLUMN_NAME, orderID)
                    contentValues.put(ORDER_ITEMS_ITEM_ID_INT_COLUMN_NAME, item.id())
                    contentValues.put(ORDER_ITEMS_QUANTITY_INT_COLUMN_NAME, i.quantity())
                    contentValues.put(ORDER_ITEMS_SIZE_TEXT_COLUMN_NAME, item.sizes()[i.sizePosition()].size())
                    b = b and (db.insert(ORDER_ITEMS_TABLE_NAME, null, contentValues) != -1L)
                }
            }
            Log.i("editOrder2","$b")
            return b
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
}
