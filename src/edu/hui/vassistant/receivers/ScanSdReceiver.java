package edu.hui.vassistant.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

public class ScanSdReceiver extends BroadcastReceiver {
//
//	private AlertDialog.Builder  builder = null;
//	private AlertDialog ad = null;
	private int count1;
	private int count2;
	private int count;
	private String title= "";
	private String URI  = "";
	private Cursor c;
	private String[] path;	
	private int [] ids;
	private String[]titles;
	boolean isDone= false;
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)){
			Cursor c1 = context.getContentResolver()
			.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME },
					null, null, null);
			count1 = c1.getCount();
			System.out.println("count:"+count);
			
			title = MediaStore.Audio.Media.TITLE;
			URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString();
			
		}else if(Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)){
			Cursor c2 = context.getContentResolver()
			.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME },
					null, null, null);
			count2 = c2.getCount();
			count = count2-count1;
			
			c = context.getContentResolver()
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
			    isDone= true;
			
		}	
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	public int[] getIds() {
		return ids;
	}
	public void setIds(int[] ids) {
		this.ids = ids;
	}
	public String[] getTitles() {
		return titles;
	}
	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public int nameToId(String name){
		int result = -1;
		for(int i = 0;i<titles.length;i++){
			if(titles[i].toLowerCase().contains(name.toLowerCase())){
				result  = ids[i];
			}
		}
		return result;
	}
	
	
}
