package com.sudesi.hafele.faultreport;

import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;

public class AcceptedComplaintFragment extends Fragment {
	Context context;
	String message;
	HafeleFaultReportDBAdapter dbAdapter;
	List<Complaint> data;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		message = getArguments().getString("message");
		dbAdapter = new HafeleFaultReportDBAdapter(context);
		dbAdapter.close();
		dbAdapter.open();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.complains_list, null);
		ListView list = (ListView)view.findViewById(R.id.list);
		data = new LinkedList<Complaint>();
		
		if(message == "accepted"){
			//data = dbAdapter.getComplainOrServiceRequests("y"); 
			//list.setAdapter(new ComplaintListAdapter(context,data));
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position , long id) {
					Complaint complaint = data.get(position);
					//String prodSubCat = complaint.product_sub_category;
					Intent intent = new Intent(context,FaultReportForm.class);
					intent.putExtra("sub_category", complaint.product_sub_category);
					intent.putExtra("complaint_number", complaint.complaint_number);
					intent.putExtra("customer_name", complaint.end_user_name);
					intent.putExtra("purchase_from", complaint.service_franchise);
					startActivity(intent);					
				}
			});
		}else if(message == "not_accepted"){			
			//data = dbAdapter.getComplainOrServiceRequests("n");
			//list.setAdapter(new ComplaintListAdapter(context,data));
		}					
		return view;
	}
}
