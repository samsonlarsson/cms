package ke.co.greencredit.nunua.General;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.Cart.TypeCart;
import ke.co.greencredit.nunua.Product.Product;

public class DataManager {
    ServiceHandler mServiceHandler;
    JSONHandler mJSONHandler;
    SharedPreferences preferences;
    AppConstants appConstants= new AppConstants();
    private Context mContext;
    private int request; //0-query,1-insert,2-delete,3-update
    private String query;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase currDb;

    public DataManager(Context mContext, int request, String query) {

        this.mContext = mContext;
        this.request = request;
        this.query = query;
        databaseHelper = new DatabaseHelper(this.mContext);
        preferences = mContext.getSharedPreferences(appConstants.PREFS_NAME, Context.MODE_PRIVATE);

        switch (request){
            case 0:

                currDb = databaseHelper.getReadableDatabase();
                break;
            case 1:

                currDb = databaseHelper.getWritableDatabase();
                break;
            case 2:

                currDb = databaseHelper.getWritableDatabase();
                break;
        }
        mServiceHandler = new ServiceHandler();
        mJSONHandler = new JSONHandler();
    }

    public boolean AddCartItem(TypeCart typeCart, int quantity,boolean overwrite){
        ContentValues cartDetails = ConverToContentValue(typeCart, quantity);

        String cartid = cartDetails.get(databaseHelper.CART_ITEM_ID).toString();

        Cursor cursor1 = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ITEM_ID + " = ? ", new String[]{cartid}
                , null, null, null);

        if(cursor1!=null && cursor1.getCount() !=0){
            cursor1.moveToFirst();
            int already = cursor1.getInt(cursor1.getColumnIndex(databaseHelper.CART_ITEM_QUANTITY));
            int availa = Integer.parseInt(typeCart.getCartItem().getQuantity());
            int curr;
            if(overwrite){
                curr = quantity;
            }else{
                curr = already + quantity;
                if(curr > availa){
                    Toast.makeText(mContext, "Only: " + availa + " items are in stock.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            Log.d("debug message >> ", "already > " + already + " available " + availa + " projected to: " + curr);



            ContentValues values = ConverToContentValue(typeCart,curr);
            long insertId = this.currDb.update(databaseHelper.TABLE_CART, values, databaseHelper.CART_ITEM_ID + " = ? ", new String[]{cartid});
            Log.d("cursor", insertId + " is the insert id");
            Cursor cursor = this.currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart,
                    databaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
                    null);
            Log.d("cursor", cursor.getCount() + "");
            if(cursor.getCount() !=0){
                Log.d("Message >> ", insertId + " updated");
                return  true;
            }
            return false;


        }else {
            return  cursor1 != null && insertItemToCart(typeCart,quantity);
        }

    }
    public boolean insertItemToCart(TypeCart cartItem,int quantity){

        ContentValues contentValues = ConverToContentValue(cartItem,quantity);

        long insertId = this.currDb.insert(databaseHelper.TABLE_CART, null, contentValues);
        Log.d("cursor", insertId + " is the insert id");
        Cursor cursor = this.currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart,
                databaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
                null);

        Log.d("cursor", cursor.getCount() + "");
        if(cursor.getCount() !=0){
            cursor.moveToFirst();
            return true;
        }
        return false;
    }
    public List<TypeCart> getAllCartItems(String cartid){
        List<TypeCart> mItem= new ArrayList<>();
        Cursor cursor = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ID + " = ? ", new String[]{cartid}
                , null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TypeCart item = cursorToTypeCart(cursor);
            mItem.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return mItem;
    }
    public int getCartBP(String cartid){
        int price = 0;
        Cursor cursor = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ID + " = ? ", new String[]{cartid}
                , null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TypeCart _Item = cursorToTypeCart(cursor);
//            Log.d("log message>>>>>",_Item.getCartItem().getUnit_price()+" q="+_Item.getCartItem().getQuantity());
            price = price + (Integer.parseInt(_Item.getCartItem().getUnit_price()) * Integer.parseInt(_Item.getCartItem().getQuantity()));

            cursor.moveToNext();
        }
        cursor.close();
        return price;
    }
    public boolean deleteItem(String inventoryid){
        return currDb.delete(databaseHelper.TABLE_CART,
                databaseHelper.CART_ITEM_ID + " = " + inventoryid, null) > 0;
    }
    public int getItemsCount(String cartid){

        Cursor cursor = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ID + " = ? ", new String[]{cartid}
                , null, null, null);
        if(cursor!=null){
            return cursor.getCount();
        }
        return 0;
    }

    public boolean deleteCart(String cartid){
        Cursor cursor = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ID + " = ? ", new String[]{cartid}
                , null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TypeCart cursorToCart = cursorToTypeCart(cursor);
            boolean delete  =  currDb.delete(databaseHelper.TABLE_CART,
                    databaseHelper.CART_ITEM_ID + "=" + cursorToCart.getId(), null) > 0;
            if(cursor.isLast()){
                return delete;
            }
            cursor.moveToNext();
        }
        return false;
    }

    public JSONArray getCartJSON(String cartid) {
        JSONObject cartObject;
        JSONArray cartArray = new JSONArray();

        Cursor cursor = currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart, databaseHelper.CART_ID + " = ? ", new String[]{cartid}
                , null, null, null);


        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            cartObject = new JSONObject();
            TypeCart _Item = cursorToTypeCart(cursor);

            try {
                cartObject.put("inventoryId",_Item.getCartItem().getId());
                cartObject.put("quantity",_Item.getCartItem().getQuantity());
                cartObject.put("storeId", _Item.getCartItem().getStore_id());
//                Log.d("log message >>> ", _Item.getCartItem().getStore_id());
                cartArray.put(cartObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }
        cursor.close();
        return cartArray;
    }
    private TypeCart cursorToTypeCart(Cursor cursor){
        Product produce = new Product(cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_STORE_ID)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_NAME)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_PRICE))
                ,cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_UOM)),
                cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ITEM_IMAGE)));
        TypeCart convertedTalk = new TypeCart(cursor.getString(cursor.getColumnIndex(databaseHelper.CART_ID)),cursor.getString(cursor.getColumnIndex(databaseHelper.CART_STATUS)),produce);
        return convertedTalk;
    }

    public boolean updateCartItem(ContentValues cartDetails){
        String itemid = cartDetails.get(databaseHelper.CART_ITEM_ID).toString();

        long insertId = this.currDb.update(databaseHelper.TABLE_CART, cartDetails, databaseHelper.COLUMN_ID + " = ? ", new String[]{itemid});
        Log.d("cursor", insertId + " is the insert id");
        Cursor cursor = this.currDb.query(databaseHelper.TABLE_CART, databaseHelper.projectionCart,
                databaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
                null);
        Log.d("cursor", cursor.getCount() + "");
        if(cursor.getCount() !=0){
            cursor.moveToFirst();
            Log.d("Message >> ", insertId + " updated");
            return  true;
        }
        return false;
    }
    public ContentValues ConverToContentValue(TypeCart cartItem, int quantity){
        Product newItem = cartItem.getCartItem();
        String cartID  = cartItem.getUUID();
        String cartStatus  = cartItem.getStatus();

        String produceId = newItem.getId();
        String storeID = newItem.getStore_id();
        String produceName = newItem.getName();
        String producePrice = newItem.getUnit_price();
        String category = newItem.getCategory();
        String unitofmeasure = newItem.getUnit_measure();
        String supplierId = "";
        String produceImage = newItem.getImage_path();

        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseHelper.CART_ID,cartID);
        contentValues.put(databaseHelper.CART_ITEM_STORE_ID, storeID);
        contentValues.put(databaseHelper.CART_STATUS,cartStatus);
        contentValues.put(databaseHelper.CART_ITEM_ID,produceId);
        contentValues.put(databaseHelper.CART_ITEM_NAME,produceName);
        contentValues.put(databaseHelper.CART_ITEM_PRICE,producePrice);
        contentValues.put(databaseHelper.CART_ITEM_CATEGORY,category);
        contentValues.put(databaseHelper.CART_ITEM_QUANTITY,quantity);
        contentValues.put(databaseHelper.CART_ITEM_UOM,unitofmeasure);
        contentValues.put(databaseHelper.CART_ITEM_SUPPLIERID,supplierId);
        contentValues.put(databaseHelper.CART_ITEM_IMAGE, produceImage);
        return contentValues;

    }



//    //talk handling functions......
//    public boolean isMessageSaved(String id){
//        Cursor cursor = this.currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages,
//                databaseHelper.MESSAGE_ID + " = ? ", new String[]{id}, null, null,
//                null);
//        if(cursor!=null && cursor.getCount() > 0){
//            return true;
//        }else{
//            return false;
//        }
//    }


//    public Message updateMessage(ContentValues messageDetails){
//        Message mNewMessage = null;
//        String messageID = messageDetails.get(databaseHelper.MESSAGE_ID).toString();
//
//        long insertId = this.currDb.update(databaseHelper.TABLE_MESSAGES, messageDetails, databaseHelper.MESSAGE_ID + " = ? ", new String[]{messageID});
//        Log.d("cursor",insertId + " is the insert id");
//        Cursor cursor = this.currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages,
//                databaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
//                null);
//        Log.d("cursor",cursor.getCount()+"");
//        if(cursor!=null && cursor.getCount() !=0){
//            cursor.moveToFirst();
//            mNewMessage = cursorToMessage(cursor);
//            Log.d("Message >> ",insertId + " successfully added.");
//            return  mNewMessage;
//        }
//        return mNewMessage;
//    }
//
//    public boolean messagesAvailable(String messageID){
//        Cursor cursor = this.currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages, databaseHelper.MESSAGE_ID + " = ? ",
//                new String[]{messageID},  null, null,
//                null);
//
//        if(cursor!=null && cursor.getCount() > 0){
//            return true;
//        }else{
//            return false;
//        }
//    }

//    public Message insertMessage(Message currMessage){
//        Message newMessage = null;
//        String messageID = currMessage.getMess_id();
//        String sender = currMessage.getFrom();
//        String content = currMessage.getContents();
//        String synced = currMessage.getSynced();
//
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(databaseHelper.MESSAGE_SENDER,sender);
//        contentValues.put(databaseHelper.MESSAGE_CONTENT,content);
//        contentValues.put(databaseHelper.MESSAGE_SYNCED,synced);
//        contentValues.put(databaseHelper.MESSAGE_ID,messageID);
//
//
//        long insertId = this.currDb.insert(databaseHelper.TABLE_MESSAGES, null, contentValues);
//        Log.d("cursor",insertId + " is the insert id");
//        Cursor cursor = this.currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages,
//                databaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
//                null);
//        Log.d("cursor",cursor.getCount()+"");
//        if(cursor!=null && cursor.getCount() !=0){
//            cursor.moveToFirst();
//            newMessage = cursorToMessage(cursor);
//            return newMessage;
//        }
//        return newMessage;
//    }r


//    public List<Message> getAllMessages(){
//        List<Message> mTalk= new ArrayList<Message>();
//        Cursor cursor = currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages, null, null
//                , null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Message _Talk = cursorToMessage(cursor);
//            mTalk.add(_Talk);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return mTalk;
//    }
//    public List<Message> getAllMessagesStatus(String synced){
//        List<Message> mTalk= new ArrayList<Message>();
//        Cursor cursor = currDb.query(databaseHelper.TABLE_MESSAGES, databaseHelper.projectionMessages, databaseHelper.MESSAGE_SYNCED + " = ? ",
//                new String[]{synced}, null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Message _Talk = cursorToMessage(cursor);
//            mTalk.add(_Talk);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return mTalk;
//    }
//
//    private Message cursorToMessage(Cursor cursor){
//        Message convertedTalk = new Message(cursor.getString(cursor.getColumnIndex(databaseHelper.MESSAGE_ID)),cursor.getString(cursor.getColumnIndex(databaseHelper.MESSAGE_CONTENT)),cursor.getString(cursor.getColumnIndex(databaseHelper.MESSAGE_SENDER)),
//                cursor.getString(cursor.getColumnIndex(databaseHelper.MESSAGE_SYNCED)));
//        return convertedTalk;
//    }




    public void closeDb(){
        if (currDb.isOpen()){
            currDb.close();
        }
    }

}
