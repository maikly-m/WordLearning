package com.example.mrh.wordleaning.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mrh.wordleaning.bean.Attention;
import com.example.mrh.wordleaning.bean.BookList;
import com.example.mrh.wordleaning.bean.Word;
import com.example.mrh.wordleaning.bean.WordList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MR.H on 2016/7/27 0027.
 */
public class DataAccess {
    public static String BOOK_ID = "";

    public ArrayList<BookList> QueryBook(Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = new SqlHelper().Query(context, SqlHelper.BOOKLIST_TABLE,
                null, selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        ArrayList<BookList> list= new ArrayList<>();;
        if (cursor.moveToFirst()){
            do{
                BookList bl=new BookList();
                bl.setID(cursor.getString(0));
                bl.setName(cursor.getString(1));
                bl.setGenerateTime(cursor.getString(2));
                bl.setNumOfList(cursor.getString(3));
                bl.setNumOfWord(cursor.getInt(4));
                list.add(bl);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<WordList> QueryList(Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = new SqlHelper().Query(context, SqlHelper.WORDLIST_TABLE, null,
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
    /**
     * 查询单词
     * @param selection
     * @param selectionArgs
     * @return
     */
    public ArrayList<Word> queryWord (Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = new SqlHelper().Query(context, DataAccess.BOOK_ID, null,
                selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        ArrayList<Word> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                Word word=new Word();
                word.setID(cursor.getString(0));
                word.setSpelling(cursor.getString(1));
                word.setMeanning(cursor.getString(2));
                word.setPhonetic_alphabet(cursor.getString(3));
                word.setList(cursor.getString(4));
                list.add(word);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询一个单词
     * @param selection
     * @param selectionArgs
     * @return
     */
    public Attention queryWordOne (Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = new SqlHelper().Query(context, SqlHelper.ATTENTION_TABLE,
                null, selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        Attention attention = new Attention();
        if (cursor.moveToFirst()){
            do{
                attention.setId(cursor.getString(0));
                attention.setSpelling(cursor.getString(1));
                attention.setMeaning(cursor.getString(2));
                attention.setPhonetic_alphabet(cursor.getString(3));
                attention.setList(cursor.getString(4));
                attention.setNewword(cursor.getString(5));
                attention.setReviewword(cursor.getString(6));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attention;
    }

    public void UpdateList(WordList list, Context context){
        ContentValues cv = new ContentValues(10);
        cv.put("BOOKID", list.getBookID());
        cv.put("LIST", list.getList());
        cv.put("LEARNED", list.getLearned());
        cv.put("LEARN_TIME", list.getLearnedTime());
        cv.put("REVIEW_TIMES", list.getReview_times());
        cv.put("REVIEWTIME", list.getReviewTime());
        cv.put("BESTSCORE", list.getBestScore());
        cv.put("SHOULDREVIEW", list.getShouldReview());
        cv.put("LEARNED_COUNT", list.getLearnedCount());
        cv.put("COUNT", list.getCount());

        new SqlHelper().Update(context, SqlHelper.WORDLIST_TABLE, cv, " BOOKID ='"+list.getBookID()+"'AND LIST ='"+list.getList()+"'", null);
    }

    /**
     * 查询生词
     * @param selection
     * @param selectionArgs
     * @return
     */
    public ArrayList<Attention> queryAttention (Context context, String selection, String[]
            selectionArgs){
        HashMap<String, Object> query = new SqlHelper().Query(context, SqlHelper.ATTENTION_TABLE, null,
                selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        ArrayList<Attention> list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                Attention attention =new Attention();
                attention.setId(cursor.getString(0));
                attention.setSpelling(cursor.getString(1));
                attention.setMeaning(cursor.getString(2));
                attention.setPhonetic_alphabet(cursor.getString(3));
                attention.setList(cursor.getString(4));
                list.add(attention);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    /*
	 * 加入复习
	 * 参数
	 * word：要添加的单词
	 */
    public void InsertIntoReview(Word word, Context context, String has){
        ContentValues cv = new ContentValues();
        cv.put("ID", String.valueOf(0));
        cv.put("SPELLING", word.getSpelling());
        cv.put("MEANNING", word.getMeanning());
        cv.put("PHONETIC_ALPHABET", word.getPhonetic_alphabet());
        cv.put("LIST", "Attention");
        cv.put("REVIEW_WORD", "1");
        cv.put("NEW_WORD", has);
        new SqlHelper().Insert(context, SqlHelper.ATTENTION_TABLE, cv);
    }
    /*
    * 加入生词本
    * 参数
    * word：要添加的单词
    */
    public void InsertIntoAttention(Word word, Context context, String has){
        ContentValues cv = new ContentValues();
        cv.put("ID", String.valueOf(0));
        cv.put("SPELLING", word.getSpelling());
        cv.put("MEANNING", word.getMeanning());
        cv.put("PHONETIC_ALPHABET", word.getPhonetic_alphabet());
        cv.put("LIST", "Attention");
        cv.put("NEW_WORD", "1");
        cv.put("REVIEW_WORD", has);
        new SqlHelper().Insert(context, SqlHelper.ATTENTION_TABLE, cv);

    }

    /**
     * 删除生词
     * @param attention
     * @param context
     */
    public void UpdateAttention(Attention attention, Context context){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                SqlHelper.DB_NAME, null);
        String sql = "UPDATE ATTENTION SET NEW_WORD = '0' WHERE SPELLING = '" +attention.getSpelling()+"'";
        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }

    /**
     * 删除复习单词
     * @param attention
     * @param context
     */
    public void UpdateReviewWord(Attention attention, Context context){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                SqlHelper.DB_NAME, null);
        String sql = "UPDATE ATTENTION SET REVIEW_WORD = '0' WHERE SPELLING = '" +attention
                .getSpelling()+"'";
        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }

    /**
     * 清空已学习的单词
     * @param context
     */
    public void deleteLearnedWord(Context context){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                SqlHelper.DB_NAME, null);
        String sql = "UPDATE PLAN SET LEARNED = '0' , LEARNED_COUNT = '0'";
        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }

    /**
     * 清空已复习的单词
     * @param context
     */
    public void deleteReviewWord(Context context){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                SqlHelper.DB_NAME, null);
        String sql = "UPDATE ATTENTION SET REVIEW_WORD = '0'";
        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }

    /**
     * 清空生词
     * @param context
     */
    public void deleteNewWord(Context context){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(
                SqlHelper.DB_NAME, null);
        String sql = "UPDATE ATTENTION SET NEW_WORD = '0'";
        try{
            db.execSQL(sql);
        }
        catch(Exception e){
            e.getStackTrace();
        }
        db.close();
    }
}
