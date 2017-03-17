package ke.co.greencredit.nunua.General;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erick Genius on 7/15/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database3";
    private static final int DATABASE_VERSION = 2;

    public final String TABLE_CART = "cart";
    public final String CART_ID = "cartid";
    public final String CART_STATUS = "status";

    public final String CART_ITEM_ID = "itemid";
    public final String CART_ITEM_STORE_ID = "store_id";
    public final String CART_ITEM_NAME = "itemname";
    public final String CART_ITEM_PRICE = "price";
    public final String CART_ITEM_CATEGORY = "category";
    public final String CART_ITEM_QUANTITY = "quantity";
    public final String CART_ITEM_UOM = "uom";
    public final String CART_ITEM_SUPPLIERID = "supplierid";
    public final String CART_ITEM_IMAGE = "image";


    public final String COLUMN_ID = "id";


    public final String[] projectionCart = {CART_ID, CART_ITEM_STORE_ID, CART_STATUS, CART_ITEM_ID, CART_ITEM_NAME, CART_ITEM_PRICE, CART_ITEM_CATEGORY, CART_ITEM_QUANTITY, CART_ITEM_UOM,
            CART_ITEM_SUPPLIERID, CART_ITEM_IMAGE, COLUMN_ID};

    public final String CREATE_TABLE_CART = "CREATE TABLE "
            + TABLE_CART + "(" + CART_ID + " TEXT, " + CART_ITEM_STORE_ID + " TEXT, " + CART_STATUS + " TEXT, " + CART_ITEM_ID + " TEXT, " + CART_ITEM_NAME + " TEXT, " + CART_ITEM_PRICE + " TEXT, " + CART_ITEM_CATEGORY + " TEXT, "
            + CART_ITEM_QUANTITY + " TEXT, " + CART_ITEM_UOM + " TEXT, " + CART_ITEM_SUPPLIERID + " TEXT, " + CART_ITEM_IMAGE + " TEXT, "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ");";


    Context mContext;

    public DatabaseHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CART);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL(CREATE_TABLE_CART);

    }
}
