package edu.hui.vassistant.supports;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	public static final String dbName = "datememo";
	public static final int version = 1;
	public static final String tbName = "memo";
	public static final String idField = "_id";
	public static final String titleField = "title";
	public static final String contentField = "content";
	public static final String dateField = "date";

	public MySQLiteOpenHelper(Context context) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* ������ */
		String createTableSQL = "create table "+tbName+"(" +
				idField+" integer not null primary key," +
				titleField+" text not null," +
				contentField+" text not null," +
				dateField+" text not null)";
		
		db.execSQL(createTableSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	/*�������ڲ���*/
	public Cursor select(String date){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c= db.query(tbName, null, dateField +"=?", new String[]{date}, null, null, 
				dateField+" asc");
		
		return c;
		
	}
	
	/*��������*/
	public Cursor select(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(tbName, null, null, null, null, null, 
				dateField+" asc");
		
		return c;
	}
	
	/*��������*/
	public long insert(String title, String content, String dt){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(titleField, title);
		cv.put(contentField, content);
		cv.put(dateField, dt);
		long c = db.insert(tbName, null, cv);
		
		return c;
	}
	
	/*��������*/
	public long update(String _id, String title, String content, String dt){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(titleField, title);
		cv.put(contentField, content);
		cv.put(dateField, dt);
		long c = db.update(tbName, cv,
				idField+"=?", 
				new String[]{_id});
	
		return c;
	}
	
	/*����idɾ������*/
	public int delete(String _id){
		SQLiteDatabase db = getWritableDatabase();
		int c = db.delete(tbName, idField+"=?", new String[]{_id});
		
		return c;
	}
	
	/*ɾ����������*/
	public int delete(){
		SQLiteDatabase db = getWritableDatabase();
		int c =  db.delete(tbName, null, null);
	
		return c;
	}
	public void finish(){
		
	}
}
