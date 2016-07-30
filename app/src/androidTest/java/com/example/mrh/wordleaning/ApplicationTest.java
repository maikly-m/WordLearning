package com.example.mrh.wordleaning;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.mrh.wordleaning.bean.Word;
import com.example.mrh.wordleaning.bean.WordList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest () {
        super(Application.class);
    }

    //统计数据总数
    public void testRead(){
        String DB_NAME = getContext().getFilesDir().getAbsolutePath() + "/database/word.db";
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                DB_NAME, null);
        String sql= "ALTER TABLE PLAN ADD COUNT text ";

        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();

        ArrayList<WordList> wordLists = QueryList(getContext(), null, null);


        for (int i = 0; i < wordLists.size(); i++){
            String list = wordLists.get(i).getList();
            String bookID = wordLists.get(i).getBookID();

            HashMap<String, Object> query = Query(getContext(), bookID,
                    null, "LIST =" + " '" + list +"'", null, null, null, null);
            Cursor cursor = (Cursor) query.get("cursor");
            SQLiteDatabase dba = (SQLiteDatabase) query.get("db");
            ArrayList<Word> lista = new ArrayList<>();
            if (cursor.moveToFirst()){
                do{
                    Word word=new Word();
                    word.setID(cursor.getString(0));
                    word.setSpelling(cursor.getString(1));
                    word.setMeanning(cursor.getString(2));
                    word.setPhonetic_alphabet(cursor.getString(3));
                    word.setList(cursor.getString(4));
                    lista.add(word);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
            dba.close();

            int size = lista.size();
            Log.d("TAG", "testRead: "+size);
            ContentValues cv = new ContentValues();
            cv.put("COUNT", size);
            String where = "LIST ='"+list+"' AND BOOKID = '"+bookID+"'";
            Update(getContext(), "PLAN", cv, where, null);
        }
    }

    public ArrayList<WordList> QueryList(Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = Query(context, "PLAN", null,
                selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        ArrayList<WordList> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                WordList wl=new WordList();
                wl.setBookID(cursor.getString(0));
                wl.setList(cursor.getString(1));
                wl.setLearned(cursor.getString(2));
                wl.setLearnedTime(cursor.getString(3));
                wl.setReview_times(cursor.getString(4));
                wl.setReviewTime(cursor.getString(5));
                wl.setBestScore(cursor.getString(6));
                wl.setShouldReview(cursor.getString(7));
                wl.setLearnedCount(cursor.getString(8));
                wl.setCount(cursor.getString(9));
                list.add(wl);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public HashMap<String, Object> Query(Context context,String table, String[] columns, String selection,
                                         String[] selectionArgs, String groupBy, String having, String orderBy){
        String DB_NAME = getContext().getFilesDir().getAbsolutePath() + "/database/word.db";
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                DB_NAME, null);
        Cursor cursor = null ;
        try{
            cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cursor", cursor);
        hashMap.put("db", db);
        return hashMap;

    }
    public void Update(Context context,String table, ContentValues values, String
            whereClause, String[] whereArgs){
        String DB_NAME = getContext().getFilesDir().getAbsolutePath() + "/database/word.db";
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                DB_NAME, null);
        try{
            db.update(table, values, whereClause, whereArgs);
//            String count = (String) values.get("COUNT");
//            String sql = "UPDATA" +table+ "SET COUNT = '"+count+ "'";
//            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }
}