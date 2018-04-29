package ca.saileshbechar.imhere;

/**
 * Created by Sailesh on 4/8/2018.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import ca.saileshbechar.imhere.models.PersonObject;

public class MyDbHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "persons.db";
    private static final String TABLE_PRODUCTS = "persons";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LONG = "long";
    private static final String COLUMN_RADIUS = "radius";

    MyDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LONG + " REAL, " +
                COLUMN_RADIUS + " INTEGER " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }

    public boolean addPerson(PersonObject mPersonObject){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, mPersonObject.getName());
        values.put(COLUMN_PHONE, mPersonObject.getPhoneNumber());
        values.put(COLUMN_LAT, mPersonObject.getLat());
        values.put(COLUMN_LONG, mPersonObject.getLng());
        values.put(COLUMN_RADIUS, mPersonObject.getRadius());
        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        if (result == 1){
            return false;
        }else{
            return true;
        }
    }

    public void deletePerson(String mName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_NAME + "=\"" + mName + "\";");
    }

    public String printData(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            Log.d("DB", "In Loop");
            if (c.getString(c.getColumnIndex(COLUMN_ID)) != null){
                dbString += c.getString(c.getColumnIndex(COLUMN_NAME));
                dbString += " ";
                dbString += c.getString(c.getColumnIndex(COLUMN_PHONE));
                dbString += " ";
                dbString += c.getString(c.getColumnIndex(COLUMN_LAT));
                dbString += " ";
                dbString += c.getString(c.getColumnIndex(COLUMN_LONG));
                dbString += " ";
                dbString += c.getString(c.getColumnIndex(COLUMN_RADIUS));
                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public String[] printPerson(){
        String[] dbString = new String[4];
        int counter = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                    dbString[counter] = c.getString(c.getColumnIndex(COLUMN_NAME));
                    counter++;
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public String printNumber(String name){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
                if (c.getString(c.getColumnIndex(COLUMN_NAME)).contains(name)) {

                    dbString = c.getString(c.getColumnIndex(COLUMN_PHONE));
                }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }
    public String printLat(String name){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                if (c.getString(c.getColumnIndex(COLUMN_NAME)).contains(name))
                    dbString = c.getString(c.getColumnIndex(COLUMN_LAT));

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public String printLong(String name){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                if (c.getString(c.getColumnIndex(COLUMN_NAME)).contains(name))
                    dbString = c.getString(c.getColumnIndex(COLUMN_LONG));

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }
    public String printRadius(String name){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                if (c.getString(c.getColumnIndex(COLUMN_NAME)).contains(name))
                    dbString = c.getString(c.getColumnIndex(COLUMN_RADIUS));

            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }


}
