package com.sudesi.hafele.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.faultreport.R;

public class DelayedListAdapter extends BaseAdapter{
	Context context;
	LayoutInflater inflater;
	List<Complaint> complaints_list;
	ViewHolder holder;
	HafeleFaultReportDBAdapter dbAdapter;
	
	public DelayedListAdapter(Context context, List<Complaint> result5) {
		this.context = context;
		this.complaints_list = result5;
		inflater = LayoutInflater.from(context);
		dbAdapter = new HafeleFaultReportDBAdapter(context);
		dbAdapter.open();
	}

	@Override
	public int getCount() {		
		return complaints_list.size();
	}

	@Override
	public Complaint getItem(int position) {		
		return complaints_list.get(position);
	}

	@Override
	public long getItemId(int position) {	
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.complaint_item_view, null);
			holder.item_one = (TextView)convertView.findViewById(R.id.item_one);
			convertView.setTag(holder);
			
			
			convertView.setBackgroundColor(Color.parseColor("#FE6F6F"));
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Complaint complain = getItem(position);

		try{
			if(complain.visit_count != 0){
				holder.item_one.setText(complain.complaint_number+"("+complain.visit_count+")");
			}else{
				holder.item_one.setText(complain.complaint_number);	
			}			
		
		
		if (dbAdapter.getUnresolvedReason(complain.complaint_number)
				.equalsIgnoreCase("Material not Received")
				|| dbAdapter.getUnresolvedReason(complain.complaint_number)
						.equalsIgnoreCase("MTR Required")
				|| dbAdapter.getUnresolvedReason(complain.complaint_number)
						.equalsIgnoreCase("Incomplete Material")
				|| dbAdapter.getUnresolvedReason(complain.complaint_number)
						.equalsIgnoreCase("Site not ready")
				|| dbAdapter.getUnresolvedReason(complain.complaint_number)
						.equalsIgnoreCase("Wrong Material")
				|| dbAdapter.getUnresolvedReason(complain.complaint_number)
						.equalsIgnoreCase("It is a complaint")) 
		 
		{
			convertView.setBackgroundColor(Color.parseColor("#00ccff"));
		}
		
		else
		{
			convertView.setBackgroundColor(Color.parseColor("#FE6F6F"));
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	
	private class ViewHolder{
		TextView item_one;
		
	}
}
