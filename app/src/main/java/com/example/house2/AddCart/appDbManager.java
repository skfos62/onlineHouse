package com.example.house2.AddCart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import static com.example.house2.AddCart.addChatAdapter.date;
import static com.example.house2.mypage.mypage.staticUser;

/**
 * sqlite 클래스
 */

public class appDbManager {
    static final String DB_MOVIE = "App4.db";   //DB이름
    static final String TABLE_addCart = "addCart"; //Table 이름
    static final int DB_VERSION = 1;			//DB 버전

    Context myContext = null;

    private static appDbManager myDBManager = null;
    private SQLiteDatabase mydatabase = null;


    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static appDbManager getInstance(Context context)
    {
        if(myDBManager == null)
        {
            myDBManager = new appDbManager(context);
        }

        return myDBManager;
    }

    private appDbManager(Context context)
    {
        myContext = context;

        //DB Open
        mydatabase = context.openOrCreateDatabase(DB_MOVIE, context.MODE_PRIVATE,null);

        //Table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_addCart +
                "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "nickname TEXT," +
                "title TEXT," +
                "totalCount INTEGER," +
                "sum INTEGER," +
                "purchars TEXT," +
                "image TEXT," +
                "date DEFAULT CURRENT_TIMESTAMP);");
    }

    public long insert(ContentValues addRowValue)
    {
        return mydatabase.insert(TABLE_addCart, null, addRowValue);
    }

    public Cursor query(String[] colums,
                               String selection,
                               String[] selectionArgs,
                               String groupBy,
                               String having,
                               String orderby)
    {
        return mydatabase.query(TABLE_addCart,
                colums,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderby);
    }
    public void delete(View v) {
        mydatabase.execSQL("DELETE FROM addCart WHERE date = '" + date + "';");
    }

    public void alldelete(View v) {
        mydatabase.execSQL("DELETE FROM addCart;");
    }



    public int count(){
        int cnt = 0;
        Cursor cursor = mydatabase.rawQuery("SELECT * FROM addCart",null);
        cnt = cursor.getCount();
        return cnt;
    }


    public int sum(){
        int sum = 0;
        Cursor cursor = mydatabase.rawQuery("SELECT sum(sum) FROM addCart",null);
        if (cursor.moveToFirst())
        {
            sum = cursor.getInt(0);
        }
        while (cursor.moveToNext());

        return sum;
    }




}
