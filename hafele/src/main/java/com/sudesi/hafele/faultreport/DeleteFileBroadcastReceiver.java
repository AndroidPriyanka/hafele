package com.sudesi.hafele.faultreport;


import java.io.File;

import com.netcompss.ffmpeg4android_client.BaseWizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DeleteFileBroadcastReceiver extends BroadcastReceiver {
	
	BaseWizard base;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		File license = new File("/sdcard/Android/data/qwerty/sd23/asdf/ffmpeglicense.lic"); 
		
		if(license.exists())
		{
			license.delete();
			
//			Toast.makeText(context, license.toString() + " :  Deleted", Toast.LENGTH_LONG).show();
			Log.e("Deleted", "YES");
			
		}
		else
		{
			Log.e("Deleted", "NO");
//			Toast.makeText(context, license.toString() + " : Not Deleted", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DeleteFileBroadcastReceiver.class);
      
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        //1000 * 60 * 60 * 24 * 15   //15 days
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),1000 * 60 * 24 * 15, pi); 
    }

}
