package com.example.spellingshody;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
public static final String DB_NAME = "Spelling.db";
public static final int DB_VERSION = 2;

public static final String WORDS_TB_NAME = "words";
public static final String WORDS_CLM_ID = "id";
public static final String WORDS_CLM_WORD = "word";
public static final String WORDS_CLM_DAY = "day";
public static final String WORDS_CLM_WEEK = "week";


    public MyDatabase (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creat New Table
        db.execSQL("CREATE TABLE "+WORDS_TB_NAME+" ("+WORDS_CLM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ""+WORDS_CLM_WORD+" TEXT NOT NULL,"+WORDS_CLM_DAY+" TEXT,"+WORDS_CLM_WEEK+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS words");
//        onCreate(db);
    }
    //Method to Insert Words
    public boolean insertWOrd(Word word){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORDS_CLM_WORD,word.getWord());
        values.put(WORDS_CLM_DAY,word.getDay());
        values.put(WORDS_CLM_WEEK,word.getWeek());

        long result=db.insert(WORDS_TB_NAME,null,values);
        return result !=-1;
    }
    //Method to get words
    public ArrayList getWords(String week, String day){
        ArrayList words = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String []where = {week,day};
//        String query="SELECT word FROM words WHERE week=? AND day=?";
        Cursor cursor=db.rawQuery("SELECT word FROM words WHERE week=? AND day=?",where);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String word = cursor.getString(cursor.getColumnIndex(WORDS_CLM_WORD));
            words.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return words;
    }
    //Method to get All Words
    public ArrayList getAllWords(String week,String day){
        ArrayList get=new ArrayList();
        SQLiteDatabase db= getReadableDatabase();
        String []where = {week,day};
        Cursor cursor=db.rawQuery("SELECT * FROM words WHERE week=? AND day=?",where);
        cursor.moveToFirst();
        for (int i = 0; i <cursor.getCount() ; i++) {
            String id =cursor.getString(cursor.getColumnIndex(WORDS_CLM_ID));
            String word = cursor.getString(cursor.getColumnIndex(WORDS_CLM_WORD));
            String w = cursor.getString(cursor.getColumnIndex(WORDS_CLM_WEEK));
            String d = cursor.getString(cursor.getColumnIndex(WORDS_CLM_DAY));
            get.add(id+"    "+word+"      "+w+"         "+d);
            cursor.moveToNext();
        }
        cursor.close();
        return get;
    }
    //Method to Delete word
    public void deleteWord (int id){
        SQLiteDatabase db= getWritableDatabase();
        String[] where={String.valueOf(id)};
        db.delete(WORDS_TB_NAME,"id=?",where);
    }
    //Method to Delete All Words
    public void deleteAllWords(String week,String day){
        SQLiteDatabase db= getWritableDatabase();
        String[] where={week,day};
        db.delete(WORDS_TB_NAME,"week=? AND day=?",where);
    }
//    public void upDateWord(int id){
//        SQLiteDatabase db = getWritableDatabase();
//        String[] where = {String.valueOf(id)};
//        db.update();
//    }
}
