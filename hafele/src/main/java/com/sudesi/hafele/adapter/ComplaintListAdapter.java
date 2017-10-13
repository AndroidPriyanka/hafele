package com.sudesi.hafele.adapter;

import java.util.List;
import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.faultreport.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComplaintListAdapter extends BaseAdapter{
	
	Context context;
	LayoutInflater inflater;
	List<Complaint> complaints_list;
	ViewHolder holder;

	
	public ComplaintListAdapter(Context context, List<Complaint> result5) {
		this.context = context;
		this.complaints_list = result5;
		inflater = LayoutInflater.from(context);
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
//		if(complain.color_code != null){
//			if(complain.color_code.equals("#FE6F6F")){
//				holder.item_one.setBackgroundColor(Color.parseColor(complain.color_code));
//			}else{
//				holder.item_one.setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
		
//		if(complain.case_attended != null){			
//			if(complain.case_attended.equals("N")){
//				holder.item_one.setBackgroundColor(Color.parseColor("#FE6F6F"));				
//			}else{
//				holder.item_one.setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
		
//		if(position % 2 == 0){
//			convertView.setBackgroundColor(Color.parseColor("#e5e5e5"));
//		}else{
//			convertView.setBackgroundColor(Color.parseColor("#e8e8e8"));
//		}
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView item_one;		
	}

//	public void setbackground(Complaint complain, int pos) {
//		if(complaints_list.contains(complain)){
//			holder.item_one.setBackgroundColor(Color.parseColor("#FE6F6F"));
//		}		
//	}
}
