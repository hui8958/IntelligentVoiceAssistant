package edu.hui.vassistant.activities;

import android.app.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;


public class Song extends Activity  {

//	private String[] albums;
//	private String[] artists;
	private String[] path;	
	private int [] ids;
	private String[]titles;
	private Cursor c;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this.getContentResolver()
				.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						new String[]{MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA},
						null, null, null);
		
		  if (c==null || c.getCount()==0){
		    System.out.println("No music in the library!");
		    }
		    c.moveToFirst();
		    ids = new int[c.getCount()];
		    titles = new String[c.getCount()];
		    path = new String[c.getCount()];
		    for(int i=0;i<c.getCount();i++){
		    	ids[i] = c.getInt(3);
		    	titles[i] = c.getString(0);
		    	path[i] = c.getString(5).substring(4);
		    	c.moveToNext();
		    }

	}
	
	
	public int[] getIds() {
		return ids;
	}
	public void setIds(int[] ids) {
		this.ids = ids;
	}
	
	
	
	
	
}
