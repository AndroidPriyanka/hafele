package com.sudesi.hafele.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Complaint implements Parcelable{
	
	public String date;
	public String created_month;
	public String created_by;
	//public String service_number;
	public String complaint_number;
	public String user_type;
	public String business_category;
	public String call_category;
	public String channel_partner_name;
	public String channel_partner_address;
	public String channel_partner_region;
	public String channel_partner_city;
	public String channel_partner_mobile;
	public String end_user_name;
	public String end_user_address;
	public String end_user_region;
	public String end_user_city;
	public String end_user_details;
	public String end_user_type;
	public String end_user_mobile;
	public String product_group;
	public String product_category;
	public String product_sub_category;
	public String service_details;
	public String reason_name;
	public String sales_executive;
	public String sales_region;
	public String sales_sub_region;
	public String technician;
	public String hafele_service_team;
	public String regional_admin;
	public String designer;
	public String design_center;
	public String service_franchise;
	public String executive_feedback;
	public String status;
	public int Age;
	public String last_actioned_since;
	public String resolve_date;
	public String date_time;
	public String sync_status;
	public String is_accepted;
	public String article_no;
	public String pin_code;
	public int visit_count;
	public String case_attended;
	public String color_code;
	public String Closure_Status;
	public int delayedValue;
	
	
	 @Override
	    public void writeToParcel(Parcel dest, int flags) {
	      
	    	dest.writeString(complaint_number);
	    	dest.writeString(user_type);
	    	dest.writeString(business_category);
	    	dest.writeString(call_category);
	    	dest.writeString(channel_partner_name);
	    	dest.writeString(channel_partner_address);
	    	dest.writeString(channel_partner_region);
	    	dest.writeString(channel_partner_city);
	    	dest.writeString(channel_partner_mobile);
	    	dest.writeString(end_user_name);
	    	dest.writeString(end_user_address);
	    	dest.writeString(end_user_region);
	    	dest.writeString(end_user_city);
	    	dest.writeString(end_user_details);
	    	dest.writeString(end_user_type);
	    	dest.writeString(end_user_mobile);
	    	dest.writeString(product_group);
	    	dest.writeString(product_category);
	    	dest.writeString(product_sub_category);
	    	dest.writeString(service_details);
	    	dest.writeString(reason_name);
	    	dest.writeString(sales_executive);
	    	dest.writeString(sales_region);
	    	dest.writeString(sales_sub_region);
	    	dest.writeString(technician);
	    	dest.writeString(hafele_service_team);
	    	dest.writeString(regional_admin);
	    	dest.writeString(designer);
	    	dest.writeString(design_center);
	    	dest.writeString(service_franchise);
	    	dest.writeString(executive_feedback);
	    	dest.writeString(status);
	    	dest.writeInt(Age);
	    	dest.writeString(last_actioned_since);
	    	dest.writeString(resolve_date);
	    	dest.writeString(date_time);
	    	dest.writeString(sync_status);
	    	dest.writeString(is_accepted);
	    	dest.writeString(article_no);
	    	dest.writeString(pin_code);
	    	dest.writeInt(visit_count);
	    	dest.writeString(case_attended);
	    	dest.writeString(color_code);
	    	dest.writeString(Closure_Status);
		 	dest.writeInt(delayedValue);
	       
	    }
	 
	 private Complaint(Parcel in){
		 	this.date = in.readString();
		 	this.created_month = in.readString();
			this.created_by = in.readString();
			//this.service_number;
			this.complaint_number = in.readString();
			this.user_type = in.readString();
			this.business_category= in.readString();
			this.call_category= in.readString();
			this.channel_partner_name= in.readString();
			this.channel_partner_address= in.readString();
			this.channel_partner_region= in.readString();
			this.channel_partner_city= in.readString();
			this.channel_partner_mobile = in.readString();
			this.end_user_name= in.readString();
			this.end_user_address= in.readString();
			this.end_user_region= in.readString();
			this.end_user_city= in.readString();
			this.end_user_details= in.readString();
			this.end_user_type= in.readString();
			this.end_user_mobile= in.readString();
			this.product_group= in.readString();
			this.product_category= in.readString();
			this.product_sub_category= in.readString();
			this.service_details = in.readString();
			this.reason_name = in.readString();
			this.sales_executive= in.readString();
			this.sales_region= in.readString();
			this.sales_sub_region= in.readString();
			this.technician= in.readString();
			this.hafele_service_team= in.readString();
			this.regional_admin= in.readString();
			this.designer= in.readString();
			this.design_center= in.readString();
			this.service_franchise= in.readString();
			this.executive_feedback= in.readString();
			this.status= in.readString();
			this.Age= in.readInt();
			this.last_actioned_since= in.readString();
			this.resolve_date= in.readString();
			this.date_time= in.readString();
			this.sync_status= in.readString();
			this.is_accepted= in.readString();
			this.article_no= in.readString();
			this.pin_code= in.readString();
			this.visit_count= in.readInt();
			this.case_attended= in.readString();
			this.color_code= in.readString();
			this.Closure_Status = in.readString();
		 this.delayedValue = in.readInt();
			       
	    }
	 
	 public Complaint() {
		
	}

	public static final Parcelable.Creator<Complaint> CREATOR = new Parcelable.Creator<Complaint>() {
		 
	        @Override
	        public Complaint createFromParcel(Parcel source) {
	            return new Complaint(source);
	        }
	 
	        @Override
	        public Complaint[] newArray(int size) {
	            return new Complaint[size];
	        }
	    };


	    @Override
	    public int describeContents() {
	        // TODO Auto-generated method stub
	        return 0;
	    }
	
}
