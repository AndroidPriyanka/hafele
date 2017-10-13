package com.sudesi.hafele.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class UtilityClass {
	
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
	
	public static void showToast(Context context, String message){		
		Toast toast = Toast.makeText(context, "message", Toast.LENGTH_LONG);
		toast.setText(message);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	  }
	
	public static long calculateDateDifference(Context context, String dateTime1){
		long diffHours = 0;		
		String assignedDate = dateTime1+" 00:00:00";
		Log.d("assignedDate", dateTime1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// String ttt = sdff.format(new Date()).toString();
		Date date = null;
		String time1 = "";
		try {

			DateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			if (assignedDate == null) {

				String ttt = sdf1.format(new Date()).toString();
				date = inFormat.parse(ttt);
			} else {
				date = inFormat.parse(assignedDate);
			}

			time1 = sdf1.format(date);

		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// String time1 = date.toString();
		// String time1 = ttt+" 23:30:00";

		Date date2 = null;
		Date date1 = null;

		// SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyy hh:mm:ss aa");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String currentDateandTime = sdf.format(new Date()).toString();
		Log.v("", "currentDateandTime==" + currentDateandTime);

		try {
			try {
				if (time1.equalsIgnoreCase("")) {

					date2 = sdf.parse(currentDateandTime);
					// date1 = sdf.parse(currentDateandTime);
				} else {

					date2 = sdf.parse(currentDateandTime);
					try {
						date1 = sdf.parse(time1);
					} catch (java.text.ParseException e)
					{
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						e.printStackTrace();
						// we.writeToSD(errors.toString());
					}
				}
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block

				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));

				e.printStackTrace();
				// we.writeToSD(errors.toString());
			}

			// Log.v("","date2.getTime()=="+date2.getTime());
			// Log.v("","date1.getTime()=="+date1.getTime());
			
			try {
				long diff = date2.getTime() - date1.getTime();
				// long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				diffHours = diff / (60 * 60 * 1000);
				Log.v("", "i============" + diffMinutes);
				// if (date1.compareTo(date2)==0)
				if (diffHours >= 72) {
					//showToast(context, "Delayed");						
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));

			e.printStackTrace();
			// we.writeToSD(errors.toString());
		}

		return diffHours;
	}
	
	public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight,String URL) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
       
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
//        m.setRotate(getImageOrientation(URL), px, py)
//        m.setRotate(getImageOrientation(URL));
//        m.postRotate(getImageOrientation(URL));
        Log.e("URL", URL);
        canvas.drawBitmap(bitmap, m, new Paint());

       // Bitmap result=waterMark(output, 90, 30, true);
        return output;
    }
}
