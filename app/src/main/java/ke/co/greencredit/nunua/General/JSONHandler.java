package ke.co.greencredit.nunua.General;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ke.co.greencredit.nunua.Delivery.DeliveryStoreDetails;
import ke.co.greencredit.nunua.Delivery.TypeDelivery;
import ke.co.greencredit.nunua.Delivery.item_delivery;
import ke.co.greencredit.nunua.History.TypeCartHistory;
import ke.co.greencredit.nunua.History.TypeHistory;
import ke.co.greencredit.nunua.Home.PopularItem;
import ke.co.greencredit.nunua.Loyalty.LoyaltyItems;
import ke.co.greencredit.nunua.Loyalty.SimpleProduct;
import ke.co.greencredit.nunua.Product.Product;
import ke.co.greencredit.nunua.StoreNavigation.Department;
import ke.co.greencredit.nunua.StoreNavigation.Store;

/**
 * Created by Erick Genius on 7/4/2014.
 */
public class JSONHandler {
    SharedPreferences settings;
    AppConstants mAppConstants = new AppConstants();


    //{"success":1,"message":[{"ProductID":"573e1044952db1220ba219ca"
    // ,"StoreName":"Fresh Green","StoreID":"573e0575952db1220ba219c6","Quantity":200,
    // "UnitPrice":500,"UnitMeasure":"500 grams","QuantityMeasure":"5"
    // ,"Virtual":200,"Image":"","Name":"Apple","Category":"Fruits","CategoryID":"573e0f58952db1220ba219c8"}]}

    public List<Product> handleDepartmentItems(String s) {
        List<Product> items = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getInt("success") == 0) {
                return items;
            }

            JSONArray products = jsonObject.getJSONArray("message");

            for (int x = 0; x < products.length(); x++) {//loop through products
                JSONObject singleItem = products.getJSONObject(x);
                String id = singleItem.getString("ProductID");
                String name = singleItem.getString("Name");
                String category = singleItem.getString("Category");
                String unit_price = singleItem.getString("UnitPrice");
                String unit_measure = singleItem.getString("UnitMeasure");

                String image_path = singleItem.getString("Image");
                String quantity = singleItem.getString("Quantity");
                String storeID = singleItem.getString("StoreID");

                Product mProduce = new Product(id, storeID, name, unit_price, category, quantity, unit_measure, image_path);
                items.add(mProduce);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return items;
    }

    public List<PopularItem> handlePopularItems(String s) {
        String storeName, storeId;
        List<PopularItem> items = new ArrayList<>();
        List<Product> productList;

        try {

            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getInt("success") == 0) {
                return items;
            }

            JSONArray arr = jsonObject.getJSONArray("message");


            for (int i = 0; i < arr.length(); i++) {//loop through stores
                JSONObject store = arr.getJSONObject(i);//store object
                storeName = store.getString("storeName");
                storeId = store.getString("storeId");
                boolean empty = store.getBoolean("empty");

                if (!empty) {
                    productList = new ArrayList<>();
                    JSONArray products = store.getJSONArray("products");
                    for (int x = 0; x < products.length(); x++) {//loop through products
                        JSONObject singleItem = products.getJSONObject(x);
                        String id = singleItem.getString("ProductID");
                        String name = singleItem.getString("Name");
                        String category = singleItem.getString("Category");
                        String unit_price = singleItem.getString("UnitPrice");
                        String unit_measure = singleItem.getString("UnitMeasure");

                        String image_path = singleItem.getString("Image");
                        String quantity = singleItem.getString("Quantity");
                        String storeID = singleItem.getString("StoreID");

                        Product mProduce = new Product(id, storeID, name, unit_price, category, quantity, unit_measure, image_path);
                        if (productList.size() < 6) {
                            productList.add(mProduce);
                        }
                    }

                    items.add(new PopularItem(storeId, storeName, productList));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return items;
    }

    public String HandleResp(String s) {
        try {
            Log.d("log message>>>", s);

            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                return jsonObject.getString("message");
            } else {
                return "false";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public String HandleResp4(String s) {
        try {
            Log.d("log message>>>", s);

            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                return "Success";
            } else {
                return "false";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public String HandleRespCart(String s, Context context) {
        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            Log.d("log message>>>", s);

            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                Log.d("cart id >> ", jsonObject.getString("message"));
                return jsonObject.getString("message");

            } else if (success == 2) {
                String paymentRef, cartId;
                int totalAmount;

                JSONArray cart = jsonObject.getJSONArray("message");
                jsonObject = cart.getJSONObject(0);
                totalAmount = jsonObject.getInt("total_amount");
                cartId = jsonObject.getString("cart_id");


                settings.edit().putInt("PrevCartStatus", 1).apply();
                settings.edit().putString("PaymentRef", cartId).apply();
                settings.edit().putString("cartat", "checkout").apply();
                settings.edit().putInt("cartprice", totalAmount).apply();
                settings.edit().putString("cartat", "checkout").apply();

                return "cartactive";
            } else if (success == 3) {
                return "delivery_on";
            } else {
                return "false";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String HandleResp2(String s, Context context) {
        Log.d("log message login", s);

        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                String uid, name, pic, location;
                JSONObject message = jsonObject.getJSONObject("message");
                uid = message.getString("uid");
                name = message.getString("business_name");
//                pic = message.getString("pic");
                location = message.getString("location");

                settings.edit().putString("uname", name).apply();
//                settings.edit().putString("pic",pic).apply();
                settings.edit().putString("location", location).apply();
                return uid;
            } else {
                return "false";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "false";
    }


    public ArrayList<LoyaltyItems> handleLoyaltyItems(String s) {

        ArrayList<LoyaltyItems> loyaltyItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                String date, name;
                int points, quantity;
                JSONArray message = jsonObject.getJSONArray("message");
//                JSONObject deliveryObj = jsonObject.getJSONObject("message");

                for (int i = 0; i < message.length(); i++) {
                    JSONObject singleItem = message.getJSONObject(i);
                    JSONArray products = singleItem.getJSONArray("products");
                    ArrayList<SimpleProduct> arrProducts = new ArrayList<>();
                    for (int b = 0; b < products.length(); b++) {

                        jsonObject = products.getJSONObject(b);

                        name = jsonObject.getString("productName");
                        quantity = jsonObject.getInt("quantity");
                        SimpleProduct product = new SimpleProduct(name, quantity);

                        arrProducts.add(product);
                    }


                    points = singleItem.getInt("points");
                    date = singleItem.getString("date");
                    LoyaltyItems item = new LoyaltyItems(arrProducts, date, points);

                    loyaltyItems.add(item);
                }
                return loyaltyItems;

            } else if (success == 0) {
                return null;

            } else {
                return loyaltyItems;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public TypeDelivery HandleDelivery(String s, Context context) {
        Log.d("log message>>>>", s);

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);

        TypeDelivery currDelivery = null;


        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");

            if (success == 1) {
                List<DeliveryStoreDetails> arrStoreDetails = null;

                ArrayList<item_delivery> deliveryItems = null;
                String storeId, storeName;
                String orderId = "";
                String amount = "";
                String pendingAmount = "";
                String paymentMethod = "";
                String time = "";
                String date = "";


                JSONArray message = jsonObject.getJSONArray("message");

                arrStoreDetails = new ArrayList<>();
                for (int x = 0; x < message.length(); x++) {//whole order
                    //fetch store
                    jsonObject = message.getJSONObject(x);
                    storeId = jsonObject.getString("storeId");
                    storeName = jsonObject.getString("storeName");
                    orderId = jsonObject.getString("orderId");
                    amount = jsonObject.getString("amount");
                    pendingAmount = jsonObject.getString("pendingAmount");
                    paymentMethod = jsonObject.getString("paymentMethod");
                    time = jsonObject.getString("time");
                    date = jsonObject.getString("date");
                    JSONArray arrItems = jsonObject.getJSONArray("items");
                    deliveryItems = new ArrayList<>();
                    for (int y = 0; y < arrItems.length(); y++) {//products
                        String productName, productQuantity, unitMeasure, unitPrice, unitQuantity;
                        JSONObject item = arrItems.getJSONObject(y);
                        productName = item.getString("productName");
                        productQuantity = item.getString("quantity");
                        unitMeasure = item.getString("unitMeasure");
                        unitPrice = item.getString("unitPrice");
                        unitQuantity = item.getString("unitQuantity");
                        item_delivery itemDelivery = new item_delivery(productName, productQuantity, unitMeasure, unitPrice, unitQuantity);
                        deliveryItems.add(itemDelivery);
                    }
                    DeliveryStoreDetails deliveryStoreDetails = new DeliveryStoreDetails(storeId, storeName, deliveryItems);
                    arrStoreDetails.add(deliveryStoreDetails);
                    //add store to delivery
                }

                currDelivery = new TypeDelivery(orderId, amount, pendingAmount, paymentMethod, time, date, arrStoreDetails);
                sharedPreferences.edit().putString("orderId", orderId).apply();
                Log.d("details >> ", currDelivery.toString());

            } else if (success == 0) {
                return null;

            } else {
                return currDelivery;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currDelivery;
    }

    //    {"success":1,"message":[{"amount_left":0,"cart_id":"573e388b952db12d02a5cf84"
// ,"order_id":"573e388b952db12d02a5cf85"
// ,"order_status":"COMPLETE","total_amount":"1000"}]}
    public ArrayList<TypeCartHistory> HandleOrderHistory(String s) {
        Log.d("log message>>>>", s);
        ArrayList<TypeCartHistory> arrCartHistories = new ArrayList<>();
        ArrayList<TypeHistory> typeHistories = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                String productName, productQuantity, cartId, total_amount, order_id, uid, order_status, amount_left, storeName;

                JSONArray histArr = jsonObject.getJSONArray("message");
                JSONArray productsArray;
                for (int i = 0; i < histArr.length(); i++) {
                    jsonObject = histArr.getJSONObject(i);

                    cartId = jsonObject.getString("cart_id");
                    total_amount = jsonObject.getString("total_amount");
                    order_id = jsonObject.getString("order_id");
//                    uid = jsonObject.getString("uid");
                    order_status = jsonObject.getString("order_status");
                    amount_left = jsonObject.getString("amount_left");

//                    productsArray = jsonObject.getJSONArray("products");
//                    for (int a = 0; a < productsArray.length(); a++) {
//                        jsonObject = productsArray.getJSONObject(a);
//                        productName = jsonObject.getString("productName");
//                        productQuantity = jsonObject.getString("quantity");
//                        storeName = jsonObject.getString("quantity");
//                        TypeHistory typeHistory = new TypeHistory(productName, productQuantity, storeName);
//                        typeHistories.add(typeHistory);
//                    }

                    TypeCartHistory typeCartHistory = new TypeCartHistory(cartId, total_amount, order_id, "", order_status, amount_left, typeHistories);
                    arrCartHistories.add(typeCartHistory);

                }


            } else {
                return arrCartHistories;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrCartHistories;
    }

    public ArrayList<Store> HandleStores(String s, Context context) {
//        Log.d("log message ", s);


        //{"success":1,"message":[{"categories":[{"department_id":"573a30466b68366a0bebb949",
        // "department_name":"Fruits","Image":"placeholder","Timestamp":"Mon May 16 16:40:38 2016"}],
        // "storeId":"573a301b6b68366a0bebb948","storeImage":"","storeName":"Fresh Apple"}]}
        AppConstants mAppConstants = new AppConstants();
        ArrayList<Store> storeList = new ArrayList<>();
        ArrayList<Department> departments = null;
        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                String name, id, graphic;
                JSONArray arr = jsonObject.getJSONArray("message");
                JSONArray arrDepts;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject objStore = arr.getJSONObject(i);
                    name = objStore.getString("storeName");
                    id = objStore.getString("storeId");
                    graphic = objStore.getString("storeImage");
//                    graphic = "";
                    arrDepts = objStore.getJSONArray("categories");
                    departments = new ArrayList<>();
                    for (int a = 0; a < arrDepts.length(); a++) {
                        String dptId, storeId, dptName, icon;
                        JSONObject objDpt = arrDepts.getJSONObject(a);
                        dptId = objDpt.getString("department_id");
//                        storeId = objDpt.getString("store_id");
                        dptName = objDpt.getString("department_name");
                        icon = objDpt.getString("Image");

                        Department newDpt = new Department(dptName, dptId, id, icon);
                        departments.add(newDpt);
                    }
                    Store newStore = new Store(name, id, graphic, departments);
                    storeList.add(newStore);
                }
                Log.d("log message>>>", storeList.size() + "");
                return storeList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    //  {"success":1,"message":[{"department_id":"573e0f58952db1220ba219c8"
// ,"department_name":"Fruits","Image":"placeholder","Timestamp":"Thu May 19 15:09:12 2016"}]}
    public ArrayList<Department> HandleDepartment(String s) {
        AppConstants mAppConstants = new AppConstants();

        ArrayList<Department> departments = null;
//    settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                JSONArray arr = jsonObject.getJSONArray("message");
                departments = new ArrayList<>();
                for (int a = 0; a < arr.length(); a++) {
                    String dptId, storeId, dptName, icon;
                    JSONObject objDpt = arr.getJSONObject(a);
                    dptId = objDpt.getString("department_id");
//                storeId = objDpt.getString("store_id");
                    dptName = objDpt.getString("department_name");
                    icon = objDpt.getString("Image");

                    Department newDpt = new Department(dptName, dptId, "", icon);
                    departments.add(newDpt);
                }


                return departments;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return departments;

    }

    //    {"success":1,"message":[{"categories":[{"department_id":"573e0f58952db1220ba219c8"
// ,"department_name":"Fruits","Image":"placeholder","Timestamp":"Thu May 19 15:09:12 2016"}]
// ,"storeId":"573e0575952db1220ba219c6","storeImage":"","storeName":"Fresh Green"}]}
    public ArrayList<Department> HandleDepartment2(String s) {
        AppConstants mAppConstants = new AppConstants();

        ArrayList<Department> departments = null;
//        settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if (success == 1) {
                JSONArray arr = jsonObject.getJSONArray("message");
                departments = new ArrayList<>();
                for (int a = 0; a < arr.length(); a++) {
                    String dptId, storeId, dptName, icon;
                    JSONObject objDpt = arr.getJSONObject(a);
                    dptId = objDpt.getString("department_id");
                    storeId = objDpt.getString("store_id");
                    dptName = objDpt.getString("department_name");
                    icon = objDpt.getString("department_image");

                    Department newDpt = new Department(dptName, dptId, storeId, icon);
                    departments.add(newDpt);
                }


                return departments;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return departments;

    }

}

