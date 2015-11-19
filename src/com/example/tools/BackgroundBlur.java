package com.example.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class BackgroundBlur {
	
	private Context context;
	private Bitmap bitmaps;
	
	public Bitmap getBitmap() {
		return bitmaps;
	}

	public BackgroundBlur(Context context) {
		super();
		this.context = context;
	}
	
	public void blur(Bitmap bkg) {  
		  long startMs = System.currentTimeMillis();  
		  float radius = 20;  
		  
		  bkg = small(bkg); 
		  Bitmap bitmap = bkg.copy(bkg.getConfig(), true); 
		  
		  final RenderScript rs = RenderScript.create(context); 
		  final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE, 
		      Allocation.USAGE_SCRIPT); 
		  final Allocation output = Allocation.createTyped(rs, input.getType()); 
		  final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); 
		  script.setRadius(radius); 
		  script.setInput(input); 
		  script.forEach(output); 
		  output.copyTo(bitmap); 
		  
		  bitmap = big(bitmap); 
		  this.bitmaps = bitmap; 
		  //setBackground(new BitmapDrawable(context.getResources(), bitmap));  
		  rs.destroy();  
		  Log.d("zhangle","blur take away:" + (System.currentTimeMillis() - startMs )+ "ms");  
	}  
	  
	private static Bitmap big(Bitmap bitmap) { 
	   Matrix matrix = new Matrix();  
	   matrix.postScale(2f,2f); //长和宽放大缩小的比例 
	   Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true); 
	   return resizeBmp; 
	 } 
	  
	 private static Bitmap small(Bitmap bitmap) { 
	   Matrix matrix = new Matrix();  
	   matrix.postScale(0.75f,0.75f); //长和宽放大缩小的比例 
	   Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true); 
	   return resizeBmp; 
	} 

}
