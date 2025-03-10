package com.example.dogfoodapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dogfoodapp.EducationalContent;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 11;
    private static final String TABLE_USERS = "users";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PAYMENT_TYPE = "payment_type";

    private static final String TABLE_CATEGORIES = "categories";
    public static final String KEY_CAT_ID = "cat_id";
    public static final String KEY_CAT_NAME = "cat_name";

    public static final String TABLE_PRODUCTS = "products";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";

    private static final String TABLE_EDUCATION = "education";
    private static final String KEY_EDU_ID = "edu_id";
    private static final String KEY_EDU_TYPE = "type";
    private static final String KEY_EDU_TITLE = "title";
    private static final String KEY_EDU_CONTENT = "content";
    private static final String KEY_EDU_BREED = "breed";
    private static final String KEY_EDU_LIFE_STAGE = "life_stage";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_USER_TYPE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_PAYMENT_TYPE + " TEXT" + ")";


        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Add this line
                + KEY_CAT_ID + " TEXT,"
                + KEY_CAT_NAME + " TEXT" + ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_PRODUCT_ID + " TEXT PRIMARY KEY,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_CATEGORY_ID + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_QUANTITY + " INTEGER" + ")";

        String CREATE_EDUCATION_TABLE = "CREATE TABLE " + TABLE_EDUCATION + "("
                + KEY_EDU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EDU_TYPE + " TEXT,"
                + KEY_EDU_TITLE + " TEXT,"
                + KEY_EDU_CONTENT + " TEXT,"
                + KEY_EDU_BREED + " TEXT,"
                + KEY_EDU_LIFE_STAGE + " TEXT)";
        db.execSQL(CREATE_EDUCATION_TABLE);


        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

    }

    public long addEducationalContent(String type, String title, String content,
                                      String breed, String lifeStage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EDU_TYPE, type);
        values.put(KEY_EDU_TITLE, title);
        values.put(KEY_EDU_CONTENT, content);
        values.put(KEY_EDU_BREED, breed);
        values.put(KEY_EDU_LIFE_STAGE, lifeStage);
        return db.insert(TABLE_EDUCATION, null, values);
    }

    public List<EducationalContent> getAllEducationalContent() {
        List<EducationalContent> contentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDUCATION,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                EducationalContent content = new EducationalContent(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EDU_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EDU_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EDU_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EDU_BREED)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_EDU_LIFE_STAGE))
                );
                contentList.add(content);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contentList;
    }

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"  // Add this line
            + KEY_CAT_ID + " TEXT,"
            + KEY_CAT_NAME + " TEXT" + ")";

// working code march 5    //
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
//        onCreate(db);
//    }
//

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 11) { // Update version check
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION);
            onCreate(db);
        }
    }


    public boolean addUser(String email, String password, String userType, String location, String paymentType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_USER_TYPE, userType);
        values.put(KEY_LOCATION, location);
        values.put(KEY_PAYMENT_TYPE, paymentType);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_EMAIL};
        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public String getUserType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_TYPE},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String userType = cursor.getString(0);
            cursor.close();
            return userType;
        }
        return "";
    }

//    public boolean addCategory(String catId, String catName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_CAT_ID, catId);
//        values.put(KEY_CAT_NAME, catName);
//
//        long result = db.insert(TABLE_CATEGORIES, null, values);
//        return result != -1;
//    }

    public boolean addCategory(String catId, String catName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, catId);
        values.put(KEY_CAT_NAME, catName);

        try {
            long result = db.insertOrThrow(TABLE_CATEGORIES, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting category: " + e.getMessage());
            return false;
        }
    }

//    public Cursor getAllCategories() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT _id, cat_id, cat_name FROM " + TABLE_CATEGORIES, null);
//    }


    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES,
                new String[]{KEY_CAT_ID, KEY_CAT_NAME},
                null, null, null, null, null);
    }

    public boolean updateCategory(String oldCatId, String newCatId, String newCatName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, newCatId);
        values.put(KEY_CAT_NAME, newCatName);

        int result = db.update(TABLE_CATEGORIES, values, KEY_CAT_ID + " = ?", new String[]{oldCatId});
        return result > 0;
    }

    public boolean deleteCategory(String catId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CATEGORIES, KEY_CAT_ID + " = ?", new String[]{catId});
        return result > 0;
    }

    public boolean addProduct(String productId, String productName, String categoryId, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productId);
        values.put(KEY_PRODUCT_NAME, productName);
        values.put(KEY_CATEGORY_ID, categoryId);
        values.put(KEY_PRICE, price);
        values.put(KEY_QUANTITY, quantity);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS,
                new String[]{KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRICE, KEY_QUANTITY},
                null, null, null, null, null);
    }

    public boolean updateProduct(String productId, String newName, double newPrice, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, newName);
        values.put(KEY_PRICE, newPrice);
        values.put(KEY_QUANTITY, newQuantity);

        int result = db.update(TABLE_PRODUCTS, values,
                KEY_PRODUCT_ID + " = ?", new String[]{productId});
        return result > 0;
    }

    public boolean deleteProduct(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS,
                KEY_PRODUCT_ID + " = ?", new String[]{productId});
        return result > 0;
    }

    public String[] getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{KEY_EMAIL, KEY_PASSWORD, KEY_LOCATION, KEY_PAYMENT_TYPE},
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String[] details = {
                    cursor.getString(0), // Email
                    cursor.getString(1), // Password
                    cursor.getString(2), // Location
                    cursor.getString(3)  // Payment Type
            };
            cursor.close();
            return details;
        }
        return null;
    }


    public boolean updateUser(String email, String name, String password, String location, String paymentType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, name);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_LOCATION, location);
        values.put(KEY_PAYMENT_TYPE, paymentType);

        int result = db.update(TABLE_USERS, values, KEY_EMAIL + "=?", new String[]{email});
        return result > 0;
    }

    public boolean deleteEducationalContent(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EDUCATION, KEY_EDU_TITLE + "=?", new String[]{title}) > 0;
    }

    public boolean updateEducationalContent(String oldTitle, EducationalContent newContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EDU_TYPE, newContent.getType());
        values.put(KEY_EDU_TITLE, newContent.getTitle());
        values.put(KEY_EDU_CONTENT, newContent.getContent());
        values.put(KEY_EDU_BREED, newContent.getBreed());
        values.put(KEY_EDU_LIFE_STAGE, newContent.getLifeStage());

        return db.update(TABLE_EDUCATION, values, KEY_EDU_TITLE + "=?",
                new String[]{oldTitle}) > 0;
    }

    public boolean updateUserProfile(String email, String location, String paymentType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION, location);
        values.put(KEY_PAYMENT_TYPE, paymentType);

        return db.update(TABLE_USERS, values, KEY_EMAIL + "=?",
                new String[]{email}) > 0;
    }

}