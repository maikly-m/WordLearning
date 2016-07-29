package com.example.mrh.wordleaning.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class SqlHelper {
	public static String BOOKLIST_TABLE = "BOOKS";
	public static String WORDLIST_TABLE = "PLAN";
	public static String ATTENTION_TABLE = "ATTENTION";
	public static String DB_NAME;

	public void Insert(Context context,String table, ContentValues values){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.insert(table, null, values);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
		}
	
	public void CreateTable(Context context,String table){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		String sql="CREATE TABLE " + table + " ( ID text not null, SPELLING text not null , MEANNING text not null, PHONETIC_ALPHABET text, LIST text not null" + ");";
		try{
			db.execSQL(sql);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public void Update(Context context,String table, ContentValues values, String
			whereClause, String[] whereArgs){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.update(table, values, whereClause, whereArgs); 
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public HashMap<String, Object> Query(Context context,String table, String[] columns, String selection,
							   String[] selectionArgs, String groupBy, String having, String orderBy){
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
	
	public void Delete(Context context,String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		try{
			db.delete(table, whereClause, whereArgs);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public void DeleteTable(Context context,String table){

		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase( 
				DB_NAME, null); 
		String sql="drop table " + table;
		try{
		db.execSQL(sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
		db.close();
	}
}
