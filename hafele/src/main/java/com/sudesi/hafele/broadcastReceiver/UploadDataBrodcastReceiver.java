package com.sudesi.hafele.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.Toast;

public class UploadDataBrodcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		
	//	Toast.makeText(context, "Don't panik but your time is up!!!!.",
              // Toast.LENGTH_LONG).show();
		
		Intent background = new Intent(context, BackgroundServiceUploadData.class);
		context.startService(background);
	}

}
