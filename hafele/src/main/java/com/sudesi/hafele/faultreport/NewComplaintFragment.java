package com.sudesi.hafele.faultreport;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sudesi.hafele.adapter.ComplaintListAdapter;
import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.utils.UtilityClass;

	public class NewComplaintFragment extends Fragment {
		
	Context context;
	List<Complaint> dummyData;
	HafeleFaultReportDBAdapter dbAdapter;
	ComplaintListAdapter listAdapter;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		
		dbAdapter = new HafeleFaultReportDBAdapter(context);
		dbAdapter.createDatabase();
		dbAdapter.open();
				
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.complains_list, null);
		ListView list = (ListView)view.findViewById(R.id.list);
		//dummyData = dbAdapter.getComplainOrServiceRequests(null);	
	//	listAdapter = new ComplaintListAdapter(context,dummyData);
		list.setAdapter(listAdapter);						
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {				
				showOption(dummyData.get(position));
			}
		});
		return view;
	}
	
	private void showOption(final Complaint complain){
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.option_view, null);
				
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);		
		dialog.setView(view);
	//	TextView text = (TextView)view.findViewById(R.id.info_text);
	//	text.setText("Complaint Number : "+complain.complaint_number+"\nCustomer name : "+complain.end_user_name+"\nCustomer Mobile Number : "+complain.end_user_mobile);
		dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ContentValues cv = new ContentValues();
				cv.put("is_accepted", "y");	
				complain.is_accepted = "y";
				// move complaint/service request to accepted // update database
				int response = dbAdapter.update("complaint_service_details",cv,"complaint_number = '"+ complain.complaint_number+"'",null);
				if(response != 0 || response != -1)	{
					UtilityClass.showToast(context, "Update successful");
					dummyData.remove(complain);
					listAdapter.notifyDataSetChanged();
					
				}else{
					UtilityClass.showToast(context, "Error in update");		
				}		
			}
		});
		
		dialog.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				// move complaint/service request to not accepted 
				ContentValues cv = new ContentValues();
				cv.put("is_accepted", "n");	
				complain.is_accepted = "n";			
				int response = dbAdapter.update("complaint_service_details",cv,"complaint_number = '"+ complain.complaint_number+"'",null);
				if(response != 0 || response != -1)	{
					UtilityClass.showToast(context, "Update successful");	
					dummyData.remove(complain);
					listAdapter.notifyDataSetChanged();
				}else{
					UtilityClass.showToast(context, "Error in update");		
				}		
			}
		});
		
		dialog.show();	
	}
}
