package edu.hui.vassistant.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DetailImage extends DetailEntity {
	String path = "";
	Context context = null;
	Drawable dw = null;

	public DetailImage(String path, int layoutID, Context context) {
		this.context = context;
		this.path = path;
		super.layoutID = layoutID;
		dw = resizeImage(280,420);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDetailName() {
		return "image";
	}

	
	public Drawable getDw() {
		return dw;
	}

	public  Drawable resizeImage( int w, int h) {  
		
        Bitmap BitmapOld   =  decodeFile(new File(path));
  
        int width = BitmapOld .getWidth();  
        int height = BitmapOld .getHeight();  
        int newWidth = w;  
        int newHeight = h;  
          
          
        float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  
  
          
        Matrix matrix = new Matrix();  
        matrix.setRotate(90);
//��ͼƬ��ת �仯  
//API ��ô д�� ��Postconcats the matrix with the specified scale��  
//�ҵ�������  ����ָ���� ��С �ڻ��ƾ���(����ͼƬ��)  
        matrix.postScale(scaleWidth, scaleHeight);  
//�ػ� ֮���ͼƬ  
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOld, 0, 0, width,height, matrix, true);  
  
        return new BitmapDrawable(resizedBitmap);  
  
    }  
	private Bitmap decodeFile(File f){ 
	    try { 
	        //Decode image size 
	        BitmapFactory.Options o = new BitmapFactory.Options(); 
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 300;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}
		return null;
	}

}
