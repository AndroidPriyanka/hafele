package com.sudesi.hafele.faultreport;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sudesi.hafele.webservice.HafeleWebservice;

public class ComplaintsList extends Activity{
	
	ProgressDialog progress;
	Context context;
	HafeleWebservice ws;
	//AlertDialog alert_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.complains_list);
		context = this;
		progress = new ProgressDialog(context);
		progress.setMessage("Loading data ...");
		
		ListView list = (ListView)findViewById(R.id.list);
		// get data for complaint raised by user
		//if(UtilityClass.isConnectingToInternet(context)){
			//GetDataTask getData = new GetDataTask();
			//getData.execute();
		//}else{
			//UtilityClass.showToast(context, "No network");
		//}
		
		final List<String> dummyData = new LinkedList<String>();
		dummyData.add("SFD00001");
		dummyData.add("LSF00001");		
		dummyData.add("FSF00001");						
		dummyData.add("CDR00001");
		dummyData.add("DRF00001");	
		dummyData.add("SWF00001");
		dummyData.add("DLF00001");
//		dummyData.add("IBF00001");
//		dummyData.add("PFR00001");
//		dummyData.add("TFR00001");
//		dummyData.add("TLF00001");
//		dummyData.add("LHR00001");
//		dummyData.add("PHR00001");
//		dummyData.add("BCU00001");
//		dummyData.add("CUF00001");
//		dummyData.add("DHF00001");
//		dummyData.add("TUF00001");
//		dummyData.add("FBH00001");
//		dummyData.add("PPF00001");
//		dummyData.add("CTF00001");
//		dummyData.add("DRF00001");
//		dummyData.add("BPF00001");
//		dummyData.add("DFR00001");
//	
		//list.setAdapter(new ComplaintListAdapter(context,dummyData));
				
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {				
				showOption(dummyData.get(position));
			}
		});
	}
	
	private void showOption(final String complainId){
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.option_view, null);
	
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setView(view);
		dialog.show();	
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute(){
			progress.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
						
		}
	}
}
