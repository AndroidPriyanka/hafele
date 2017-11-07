package com.sudesi.hafele.faultreport;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.sudesi.hafele.classes.FaultReport;
import com.sudesi.hafele.classes.Feedback;
import com.sudesi.hafele.classes.ImageData;
import com.sudesi.hafele.classes.Sanitary_Details;
import com.sudesi.hafele.classes.VideoData;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.utils.UtilityClass;
import com.sudesi.hafele.webservice.HafeleWebservice;

public class HomeScreenActivity extends Activity implements OnClickListener {

    Context context;
    HafeleFaultReportDBAdapter dbAdapter;
    ProgressDialog progress;
    HafeleWebservice ws;
    HafelePreference pref;
  // public static int delayed_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_screen_activity);
        context = HomeScreenActivity.this;
        ws = new HafeleWebservice(context);
        progress = new ProgressDialog(HomeScreenActivity.this);
        progress.setMessage("Logging in ...");
        pref = new HafelePreference(context);
        // get database instance
        dbAdapter = new HafeleFaultReportDBAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();

        ImageView home = (ImageView) findViewById(R.id.home);
        ImageView logout = (ImageView) findViewById(R.id.logout);
        ImageView dashBoard = (ImageView) findViewById(R.id.dashboard);
        ImageView userManual = (ImageView) findViewById(R.id.user_manual);
        ImageView uploadSync = (ImageView) findViewById(R.id.upload_sync);
        ImageView downloadSync = (ImageView) findViewById(R.id.download_sync);
        ImageView masterSync = (ImageView) findViewById(R.id.master_sync);
        ImageView settings = (ImageView) findViewById(R.id.settings);
        ImageView reports = (ImageView) findViewById(R.id.reports);

        home.setVisibility(View.GONE);

        // home.setOnClickListener(this);
        logout.setOnClickListener(this);
        dashBoard.setOnClickListener(this);
        userManual.setOnClickListener(this);
        uploadSync.setOnClickListener(this);
        downloadSync.setOnClickListener(this);
        masterSync.setOnClickListener(this);
        settings.setOnClickListener(this);
        reports.setOnClickListener(this);

/*

        Cursor c = dbAdapter.fetchallOrder("image_details", null, null);
       // Log.e("ImageDetails", DatabaseUtils.dumpCursorToString(c));

        Cursor c1 = dbAdapter.fetchallOrder("video_details", null, null);
        //Log.e("ImageDetails", DatabaseUtils.dumpCursorToString(c1));
*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.home:
                break;

            case R.id.logout:
                Intent intent2 = new Intent(HomeScreenActivity.this,
                        MainLauncherActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                break;

            case R.id.dashboard:
                Intent intent3 = new Intent(HomeScreenActivity.this,
                        DashBoardActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                finish();
                break;

            case R.id.user_manual:

                break;

            case R.id.upload_sync:
                if (UtilityClass.isConnectingToInternet(context)) {
                    try {
                        UploadTask data = new UploadTask();
                        data.execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "No network connection",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.download_sync:
                if (UtilityClass.isConnectingToInternet(context)) {
                    GetDataFromServer data = new GetDataFromServer();
                    data.execute();
                } else {
                    Toast.makeText(context, "No network connection",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.master_sync:
                break;

            case R.id.settings:
                break;

            case R.id.reports:

                break;

        }
    }

    private class GetDataFromServer extends AsyncTask<Void, Long, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!progress.isShowing()) {
                progress.setMessage("Syncing ...");
                progress.show();
            }
        }

        @Override
        protected Long doInBackground(Void... params) {
            long response = getDataFromServer();

            if (response > 0) {
                // delete service
                SoapObject soap_result = ws.get_oldtech(String.valueOf(pref.getUserId()));
                if (soap_result != null) {
                    for (int i = 0; i < soap_result.getPropertyCount(); i++) {
                        SoapObject sObject = (SoapObject) soap_result
                                .getProperty(i);
                        if (sObject.getProperty("Flag") != null) {
                            if (sObject.getProperty("Flag").toString()
                                    .equalsIgnoreCase("true")) {
                                boolean boolDelete = dbAdapter.deleteString(
                                        "complaint_service_details",
                                        "complaint_number", sObject
                                                .getProperty("ComplaintNo")
                                                .toString());
                                if (boolDelete == true) {


                                }

                            }
                        }
                    }
                }


                //SoapObject soap_result1 = ws.get_hinccms_status(String.valueOf(pref
                //	.getUserId()));

            }

            String all_complaint_number = null;
            Cursor cursor_complaint_number = dbAdapter.fetchallOrder("complaint_service_details", null, null);
            //Log.e("complaint_service_details Data", DatabaseUtils.dumpCursorToString(cursor_complaint_number));
            if (cursor_complaint_number != null && cursor_complaint_number.getCount() > 0) {
                cursor_complaint_number.moveToFirst();

                do {
                    if (all_complaint_number == null) {
                        all_complaint_number = cursor_complaint_number.getString(cursor_complaint_number.getColumnIndex("complaint_number"));
                        ;
                    } else {
                        all_complaint_number = all_complaint_number.toString() + "," + cursor_complaint_number.getString(cursor_complaint_number.getColumnIndex("complaint_number"));
                    }
                }
                while (cursor_complaint_number.moveToNext());
            }

            //Log.e("All Complaint Number", all_complaint_number);


            SoapObject soap_result1 = ws.get_stgComplainData(String.valueOf(all_complaint_number));
            if (soap_result1 != null) {

                for (int i = 0; i < soap_result1.getPropertyCount(); i++) {
                    SoapObject soapObject = (SoapObject) soap_result1.getProperty(i);

                    if (soapObject.getProperty("Flag") != null) {
                        if (soapObject.getProperty("Flag").toString().equalsIgnoreCase("true")) {
                            if (soapObject.getProperty("status").toString().equalsIgnoreCase("Resolved")) {
                                boolean boolDelete = dbAdapter.deleteString("complaint_service_details", "complaint_number",
                                        soapObject.getProperty("requestno").toString());
                            }

                        }
                    }
                }
            }


            return response;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (progress.isShowing())
                progress.dismiss();
            if (result > 0) {
                UtilityClass.showToast(context, "Sync sucessful");
            } else if (result == -100) {
                UtilityClass.showToast(context, "No new data to download");
            } else {
                UtilityClass.showToast(context, "Error in Sync");
            }
        }

    }

    @SuppressWarnings("null")
    private long getDataFromServer() {
        // get data for complaints and services
        long response = 0;
        // get data for complaints and services

        SoapObject sObj = ws.getComplainOrServiceRequests("Open", pref.getUserId());

        if (sObj != null) {
            if (sObj.getPropertyCount() > 0) {
                for (int i = 0; i < sObj.getPropertyCount(); i++) {
                    SoapObject soapObj = (SoapObject) sObj.getProperty(i);
                    String date = soapObj.getProperty("RegistrationDate")
                            .toString();
                    String RequestNo = soapObj.getProperty("RequestNo")
                            .toString();
                    // String Created_Month =
                    // soapObj.getProperty("Created_Month").toString();
                    String Created_By = soapObj.getProperty("Created_By")
                            .toString();
                    String Complaint_No = soapObj.getProperty("Complaint_No")
                            .toString();
                    String User_Type = soapObj.getProperty("DealerName")
                            .toString();
                    String Business_Category = soapObj.getProperty(
                            "DealerAddress").toString();
                    String Call_Category = soapObj.getProperty("DealerRegion")
                            .toString();
                    String Channel_Partner_Name = soapObj.getProperty(
                            "DealerCity").toString();
                    String Channel_Partner_Address = soapObj.getProperty(
                            "DealerPincode").toString();
                    String Channel_Partner_Region = soapObj.getProperty(
                            "DealerMobile").toString();
                    String End_User_Name = soapObj.getProperty("End_User_Name")
                            .toString();
                    String End_User_Address = soapObj.getProperty(
                            "End_User_Address").toString();
                    String End_User_Region = soapObj.getProperty(
                            "End_User_Region").toString();
                    String End_User_City = soapObj.getProperty("End_User_City")
                            .toString();
                    String End_User_Mobile = soapObj.getProperty(
                            "End_User_Mobile").toString();
                    String End_User_Details = soapObj.getProperty(
                            "End_User_Details").toString();
                    String End_User_Type = soapObj.getProperty("End_User_Type")
                            .toString();
                    String Product_Group = soapObj.getProperty("Product_Group")
                            .toString();
                    String Product_Category = soapObj.getProperty(
                            "Product_Category").toString();
                    String Product_Sub_Category = soapObj.getProperty(
                            "Product_Sub_Category").toString();
                    // String Service_Details =
                    // soapObj.getProperty("Service_Details").toString();
                    String Complaints_Details = soapObj.getProperty(
                            "Complaints_Details").toString();
                    String Reason_Name = soapObj.getProperty("I_CreatedByID")
                            .toString();
                    String Sales_Executive = soapObj.getProperty(
                            "I_UpdatedByID").toString();
                    String Sales_Region = soapObj.getProperty("C_CreatedByID")
                            .toString();
                    String Sales_Sub_Region = soapObj.getProperty(
                            "C_UpdatedByID").toString();
                    String Technician = soapObj.getProperty("Technician_Name")
                            .toString();
                    // String Hafele_Service_Team =
                    // soapObj.getProperty("Hafele_Service_Team").toString();

                    String Status = null;
                    try {
                        if (soapObj.getProperty("Status") != null) {
                            Status = soapObj.getProperty("Status").toString();
                        } else {
                            Status = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // String Age = soapObj.getProperty("Age").toString();

                    ContentValues cv = new ContentValues();
                    if (checkBoolean(date)) {
                        date = "";
                    }
                    if (date.equalsIgnoreCase("false")) {
                        date = "";
                    }

                    cv.put("date", date);
                    //
                    if (checkBoolean(Status)) {
                        Status = "";
                    }
                    try {
                        if (Status == null || Status.equalsIgnoreCase("false")) {
                            Status = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    cv.put("status", Status);

                    if (checkBoolean(Created_By)) {
                        Created_By = "";
                    }
                    if (Created_By.equalsIgnoreCase("false")) {
                        Created_By = "";
                    }
                    cv.put("created_by", Created_By);

                    // request number is complaint number
                    if (checkBoolean(RequestNo)) {
                        RequestNo = "";
                    }
                    if (RequestNo.equalsIgnoreCase("false")) {
                        RequestNo = "";
                    }
                    cv.put("complaint_number", RequestNo);

                    if (checkBoolean(User_Type)) {
                        User_Type = "";
                    }
                    if (User_Type.equalsIgnoreCase("false")) {
                        User_Type = "";
                    }
                    cv.put("user_type", User_Type);

                    if (checkBoolean(Business_Category)) {
                        Business_Category = "";
                    }
                    if (Business_Category.equalsIgnoreCase("false")) {
                        Business_Category = "";
                    }
                    cv.put("business_category", Business_Category);

                    if (checkBoolean(Call_Category)) {
                        Call_Category = "";
                    }
                    if (Call_Category.equalsIgnoreCase("false")) {
                        Call_Category = "";
                    }
                    cv.put("call_category", Call_Category);

                    if (checkBoolean(Channel_Partner_Name)) {
                        Channel_Partner_Name = "";
                    }
                    if (Channel_Partner_Name.equalsIgnoreCase("false")) {
                        Channel_Partner_Name = "";
                    }
                    cv.put("channel_partner_name", Channel_Partner_Name);

                    if (checkBoolean(Channel_Partner_Address)) {
                        Channel_Partner_Address = "";
                    }
                    if (Channel_Partner_Address.equalsIgnoreCase("false")) {
                        Channel_Partner_Address = "";
                    }
                    cv.put("channel_partner_address", Channel_Partner_Address);

                    if (checkBoolean(Channel_Partner_Region)) {
                        Channel_Partner_Region = "";
                    }
                    if (Channel_Partner_Region.equalsIgnoreCase("false")) {
                        Channel_Partner_Region = "";
                    }
                    cv.put("channel_partner_region", Channel_Partner_Region);

                    if (checkBoolean(End_User_Name)) {
                        End_User_Name = "";
                    }
                    if (End_User_Name.equalsIgnoreCase("false")) {
                        End_User_Name = "";
                    }
                    cv.put("end_user_name", End_User_Name);

                    if (checkBoolean(End_User_Address)) {
                        End_User_Address = "";
                    }
                    if (End_User_Address.equalsIgnoreCase("false")) {
                        End_User_Address = "";
                    }
                    cv.put("end_user_address", End_User_Address);

                    if (checkBoolean(End_User_Region)) {
                        End_User_Region = "";
                    }
                    if (End_User_Region.equalsIgnoreCase("false")) {
                        End_User_Region = "";
                    }
                    cv.put("end_user_region", End_User_Region);

                    if (checkBoolean(End_User_City)) {
                        End_User_City = "";
                    }
                    if (End_User_City.equalsIgnoreCase("false")) {
                        End_User_City = "";
                    }
                    cv.put("end_user_city", End_User_City);

                    if (checkBoolean(End_User_Mobile)) {
                        End_User_Mobile = "";
                    }
                    if (End_User_Mobile.equalsIgnoreCase("false")) {
                        End_User_Mobile = "";
                    }
                    cv.put("end_user_mobile", End_User_Mobile);

                    if (checkBoolean(End_User_Details)) {
                        End_User_Details = "";
                    }
                    if (End_User_Details.equalsIgnoreCase("false")) {
                        End_User_Details = "";
                    }
                    cv.put("end_user_details", End_User_Details);

                    if (checkBoolean(End_User_Type)) {
                        End_User_Type = "";
                    }
                    if (End_User_Type.equalsIgnoreCase("false")) {
                        End_User_Type = "";
                    }
                    cv.put("end_user_type", End_User_Type);

                    if (checkBoolean(Product_Group)) {
                        Product_Group = "";
                    }
                    if (Product_Group.equalsIgnoreCase("false")) {
                        Product_Group = "";
                    }
                    cv.put("product_group", Product_Group);

                    if (checkBoolean(Product_Category)) {
                        Product_Category = "";
                    }
                    if (Product_Category.equalsIgnoreCase("false")) {
                        Product_Category = "";
                    }
                    cv.put("product_category", Product_Category);

                    if (checkBoolean(Product_Sub_Category)) {
                        Product_Sub_Category = "";
                    }
                    if (Product_Sub_Category.equalsIgnoreCase("false")) {
                        Product_Sub_Category = "";
                    }
                    cv.put("product_sub_category", Product_Sub_Category);

                    if (checkBoolean(Complaints_Details)) {
                        Complaints_Details = "";
                    }
                    if (Complaints_Details.equalsIgnoreCase("false")) {
                        Complaints_Details = "";
                    }
                    cv.put("service_details", Complaints_Details);

                    if (checkBoolean(Reason_Name)) {
                        Reason_Name = "";
                    }
                    if (Reason_Name.equalsIgnoreCase("false")) {
                        Reason_Name = "";
                    }
                    cv.put("reason_name", Reason_Name);

                    if (checkBoolean(Sales_Executive)) {
                        Sales_Executive = "";
                    }
                    if (Sales_Executive.equalsIgnoreCase("false")) {
                        Sales_Executive = "";
                    }
                    cv.put("sales_executive", Sales_Executive);

                    if (checkBoolean(Sales_Region)) {
                        Sales_Region = "";
                    }
                    if (Sales_Region.equalsIgnoreCase("false")) {
                        Sales_Region = "";
                    }
                    cv.put("sales_region", Sales_Region);

                    if (checkBoolean(Sales_Sub_Region)) {
                        Sales_Sub_Region = "";
                    }
                    if (Sales_Sub_Region.equalsIgnoreCase("false")) {
                        Sales_Sub_Region = "";
                    }
                    cv.put("sales_sub_region", Sales_Sub_Region);

                    if (checkBoolean(Technician)) {
                        Technician = "";
                    } else {
                        Technician = pref.getUserName();
                    }
                    if (Technician.equalsIgnoreCase("false")) {
                        Technician = "";
                    } else {
                        Technician = pref.getUserName();
                    }
                    cv.put("technician", Technician);

                    Calendar cal = Calendar.getInstance();
                    cv.put("date_time", cal.getTimeInMillis());

                    if (dbAdapter.checkID(RequestNo,
                            "complaint_service_details", "complaint_number")) {
                        Log.v("", "updateeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                        response = dbAdapter.update(
                                "complaint_service_details", cv,
                                "complaint_number = '" + RequestNo + "'", null);
                    } else {
                        Log.v("", "insertttttttttttttttttttttttttttttttttttttt");
                        response = dbAdapter.insert("complaint_service_details", cv);
                    }
                }
            } else {
                response = -100;
            }
        }
        return response;
    }

    public boolean checkBoolean(String str_anyType) {
        Boolean bool = false;
        if (str_anyType != null) {
            if (str_anyType.equalsIgnoreCase("anyType{}")) {
                bool = true;
            } else {
                bool = false;
            }
        } else {
            bool = false; // pravin 12.11.2015
        }
        return bool;
    }

    private class UploadTask extends AsyncTask<Void, Integer, Integer> {

        // ProgressDialog progress = new
        // ProgressDialog(HomeScreenActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progress != null) {
                try {
                    progress.setMessage("Syncing ...");
                    progress.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            boolean isSaved = false;
            int responseID = 0;
            List<FaultReport> list = dbAdapter.getFaultReports(pref.getUserName());
            ContentValues cv = new ContentValues();
            ContentValues cv2=new ContentValues();
            if (list.size() == 0) {
                responseID = 2;
            } else {

                int listCount = 0;

                for (int i = 0; i < list.size(); i++) {
                    SoapPrimitive Soapresponse = ws.saveFaultReport(list.get(i)); // response is id
                    // retvalue
                    if (Soapresponse != null) {
                        cv2.put("Fault_Finding_Id",String.valueOf(Soapresponse));
                        int response = dbAdapter.update("sanitary_details",
                                cv2, "Complant_No = '"+ list.get(i).Complant_No
                                        + "'", null);

                        if (isInteger(Soapresponse.toString())) {
                            listCount = listCount + 1;
                            cv.put("sync_status", "U");
                            int responseId = dbAdapter.update(
                                    "Fault_Finding_Details", cv,
                                    "Complant_No = '" + list.get(i).Complant_No
                                            + "'", null);

                            //...................................

                            int delayed_days = 0;
                            int diffInDays = 0;

                            String registration_date = list.get(i).Registration_Date;
                            String closed_date = list.get(i).Closed_Date;
                            String format_closed_date;
                            Date new_date_registration, new_closed_date;

                            //Closed_Date=2016/11/25 12:28:42  2016/11/25 12:28:42

                            if (closed_date != null) {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                                Date myDate = null;
                                try {
                                    myDate = dateFormat.parse(closed_date);

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    format_closed_date = timeFormat.format(myDate);

                                    new_closed_date = timeFormat.parse(format_closed_date);
                                    new_date_registration = timeFormat.parse(registration_date);


                                    diffInDays = (int) ((new_closed_date.getTime() - new_date_registration.getTime()) / (1000 * 60 * 60 * 24)) - 2;
                                    if (diffInDays < 2) {
                                        delayed_days = 0;
                                    } else {
                                        delayed_days = diffInDays;
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }


                            //    SoapPrimitive Soapresponse1 = ws.Insert_Complaint_Service_Details1(list.get(i), Soapresponse.toString());


                            SoapPrimitive soap_updateFault = ws.Update_FaulFinding(
                                    Soapresponse.toString(),
                                    list.get(i).Complant_No,
                                    list.get(i).Accepted_Date,
                                    list.get(i).Called_Date,
                                    list.get(i).Updated_Date,
                                    list.get(i).Closed_Date,
                                    delayed_days);
                            isSaved = true;


                            if (responseId > 0) {
                                if (list.get(i).Closure_Status != null) {
                                    if (list.get(i).Closure_Status
                                            .equals("Resolved"))
                                    {
                                        dbAdapter.delete(
                                                "complaint_service_details",
                                                "complaint_number",
                                                "'" + list.get(i).Complant_No
                                                        + "'");

                                        dbAdapter.delete("Fault_Finding_Details", "Complant_No", "'" + list.get(i).Complant_No + "'");

                                    } else if ((list.get(i).Closure_Status.equals("Unresolved")) && (list.get(i).Action.equalsIgnoreCase("MTR required")))
                                    {

                                        dbAdapter.delete(
                                                "complaint_service_details",
                                                "complaint_number",
                                                "'" + list.get(i).Complant_No
                                                        + "'");

                                        dbAdapter.delete("Fault_Finding_Details", "Complant_No", "'" + list.get(i).Complant_No + "'");
                                    }
                                }
                            }



                         //   SoapPrimitive Soapresponse2 = ws.insert_Sanitary_Details(list.get(i));


                        }
                    }
                }

                if (listCount == list.size()) {
                    responseID = 2;
                } else {
                    responseID = 3;
                }

            }



            //..........................sanitary detaisl
            List<Sanitary_Details> list1 = dbAdapter.getSanitaryReports(pref.getUserName());
            //  ContentValues cv2 = new ContentValues();

            if (list1.size() == 0) {
                responseID = 2;
            } else {

                int listCount = 0;

                for (int i = 0; i < list1.size(); i++) {
                    SoapPrimitive Soapresponse = ws.insert_Sanitary_Details(list1.get(i)); // response is id
                    // retvalue
                    if (Soapresponse != null) {

                        if (isInteger(Soapresponse.toString())) {
                            listCount = listCount + 1;
                            cv.put("sync_status", "NU");
                            int responseId = dbAdapter.update(
                                    "sanitary_details", cv,
                                    "Complant_No = '" + list1.get(i).Complant_No
                                            + "'", null);

                            if (responseId > 0) {
                                if (list1.get(i).Closure_Status != null) {
                                    if (list1.get(i).Closure_Status
                                            .equals("Resolved"))
                                    {
                                        dbAdapter.delete(
                                                "complaint_service_details",
                                                "complaint_number",
                                                "'" + list1.get(i).Complant_No
                                                        + "'");
                                        dbAdapter.delete("sanitary_details", "Complant_No", "'" + list1.get(i).Complant_No + "'");

                                    } /*else if ((list1.get(i).Closure_Status.equals("Unresolved")) && (list1.get(i).Action.equalsIgnoreCase("MTR required")))
                                    {
                                        dbAdapter.delete(
                                                "complaint_service_details",
                                                "complaint_number",
                                                "'" + list1.get(i).Complant_No
                                                        + "'");
                                        dbAdapter.delete("sanitary_details", "Complant_No", "'" + list1.get(i).Complant_No + "'");
                                    }*/
                                }
                            }


                            int diffInDays = 0;

                            String registration_date = list1.get(i).date;
                            String closed_date = list1.get(i).Closed_Date;
                            String format_closed_date;
                            Date new_date_registration, new_closed_date;

                            //Closed_Date=2016/11/25 12:28:42  2016/11/25 12:28:42

                /*            if (closed_date != null) {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                                Date myDate = null;
                                try {
                                    myDate = dateFormat.parse(closed_date);

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    format_closed_date = timeFormat.format(myDate);

                                    new_closed_date = timeFormat.parse(format_closed_date);
                                    new_date_registration = timeFormat.parse(registration_date);


                                    diffInDays = (int) ((new_closed_date.getTime() - new_date_registration.getTime()) / (1000 * 60 * 60 * 24)) - 2;
                                    if (diffInDays < 2) {
                                        delayed_days = 0;
                                    } else {
                                        delayed_days = diffInDays;
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/


                            //      SoapPrimitive Soapresponse1 = ws.Insert_Complaint_Service_Details1(list1.get(i), Soapresponse.toString());

                        /*    SoapPrimitive soap_updateFault = ws.Update_FaulFinding(
                                    Soapresponse.toString(),
                                    list1.get(i).Complant_No,
                                    list1.get(i).Accepted_Date,
                                    list1.get(i).Called_Date,
                                    list1.get(i).Updated_Date,
                                    list1.get(i).Closed_Date,
                                    delayed_days);*/
                            isSaved = true;

                        }
                    }
                }

                if (listCount == list1.size()) {
                    responseID = 2;
                } else {
                    responseID = 3;
                }

            }
		/*	
            Cursor c=dbAdapter.fetchallOrder("image_details", null, null);
			Log.e("ImageDetails", DatabaseUtils.dumpCursorToString(c));
			
			Cursor c1=dbAdapter.fetchallOrder("video_details", null, null);
			Log.e("VideoDetails", DatabaseUtils.dumpCursorToString(c));*/

            // get image data from device db and update it to server
            List<ImageData> imgData = dbAdapter.getImageData("", "NU");
            Log.e("Image", "OUT");
            if (imgData.size() == 0) {
                responseID = 2;
            } else {
                for (int k = 0; k < imgData.size(); k++) {
                    ImageData img = imgData.get(k);

                    Log.e("Image", "Enter");

                    // upload file to server
                    SoapPrimitive fileUploadResponse = null;
                    String root = Environment.getExternalStorageDirectory()
                            .toString() + "/Compressed_Images/";
                    File myDir = new File(root);
                    myDir.mkdirs();
                    String fname = img.image_name;
                    File file = new File(myDir, fname);
                    if (file.exists()) {

                        Log.e("Image", "File_Exist");
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                        bmp.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                        byte[] bmpPicByteArray = bao.toByteArray();
                        String base64 = Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);

                        try {
                            fileUploadResponse = ws.uploadFile(base64, file.getName());
                            SoapPrimitive response = ws.RegisterJarUser("Hafele_image", pref.getDeviceId());
                            if (response != null) {
                                if (response.toString().equalsIgnoreCase("Active")) {
                                    SoapPrimitive resp = ws.SaveImageCount(
                                            "Hafele_image", pref.getDeviceId(),
                                            Integer.parseInt(img.image_count),
                                            "Image");
                                    if (resp != null) {

                                    } else {
                                        responseID = -1;
                                    }
                                } else {
                                    responseID = -1;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileUploadResponse != null) {
                        SoapPrimitive response = ws.uploadImageData(
                                imgData.get(k).complaint_number,
                                img.image_name, img.original_size,
                                img.compressed_size);
                        if (response != null) {

                            if (response.toString().equalsIgnoreCase("true")) {
                                cv.clear();
                                cv.put("upload_status", "U");
                                cv.put("complaint_number",
                                        imgData.get(k).complaint_number);
                                responseID = dbAdapter
                                        .update("image_details",
                                                cv,
                                                "complaint_number = '"
                                                        + imgData.get(k).complaint_number
                                                        + "'", null);
                                if (responseID > 0)
                                    isSaved = true;
                            }

                        }
                    } else {
                        responseID = 1001;
                    }
                }
            }
            // TODO
            // // get video details and upload to ftp

            List<VideoData> vidDataList = dbAdapter.getVideoData(null);

//			VideoData vidData = dbAdapter.getVideoData(null);
            for (int i = 0; i < vidDataList.size(); i++) {
                VideoData vidData = vidDataList.get(i);
                File vidFilef = null;
                if (vidData.FilePath != null)
                    vidFilef = new File(vidData.FilePath);
                if (vidFilef != null) {

                    String Username = pref.getUserName();
                    String VideoName = null;
                    try {
                        VideoName = vidData.FilePath.substring(
                                vidData.FilePath.lastIndexOf("/") + 1,
                                vidData.FilePath.length());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String VideoFormat = ".mp4";
                    String originalSize = vidData.OriginalSize;
                    File file = null;
                    try {
                        file = new File(vidData.FilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // File file = new
                    // File("/storage/emulated/0/AdStringOVideos/Input/VID_20150706_134413.mp4");
                    String CompressedSize = vidData.CompressedSize;
                    int s = 0;
                    try {
                        s = Integer.parseInt(originalSize)
                                - Integer.valueOf(CompressedSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String storageSaved = String.valueOf(s);
                    // TODO

                    SoapPrimitive videoUpload = null;

                    long lenght = 0;
                    // upload video
                    if (file != null) {
                        lenght = file.length();
                    }
                    int ChunkSize = 65536;
                    byte[] Buffer = new byte[ChunkSize];
                    int Offset = 0;
                    int BytesRead = 0;

                    BufferedInputStream in = null;
                    try {
                        in = new BufferedInputStream(new FileInputStream(file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    while (Offset != lenght) {
                        try {
                            BytesRead = in.read(Buffer, 0, ChunkSize);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (BytesRead != Buffer.length) {
                            ChunkSize = BytesRead;
                            byte[] TrimmedBuffer = new byte[BytesRead];
                            TrimmedBuffer = Arrays.copyOf(Buffer, BytesRead);
                            Buffer = TrimmedBuffer;

                        }

                        String base64encode = Base64.encodeToString(Buffer,
                                Base64.DEFAULT);

                        videoUpload = ws.VideoUpload(file.getName(), base64encode,
                                Offset);
                        Offset += BytesRead;
                    }

                    if (videoUpload != null) {

                        if (videoUpload.toString().equalsIgnoreCase("true")) {
                            Log.e("video upload sucessfull",
                                    "video upload sucessfull");
                            SoapPrimitive soap_result = ws.UploadVideoData(
                                    vidData.complaint_number, Username, VideoName,
                                    vidData.FilePath, originalSize, CompressedSize,
                                    storageSaved);
                            if (soap_result != null) {
                                if (soap_result.toString().equalsIgnoreCase("true")) {

                                    try {
                                        ContentValues c_v = new ContentValues();
                                        c_v.put("upload_status", "U");
                                        responseID = dbAdapter.update(
                                                "video_details", c_v,
                                                "complaint_number = '"
                                                        + vidData.complaint_number
                                                        + "'", null);
                                        isSaved = true;

                                        SoapPrimitive resp = ws.SaveImageCount(
                                                "Hafele_image", pref.getDeviceId(),
                                                1, "Video");
                                    } catch (Exception e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                }
                            }

                        } else {
                            responseID = 1001;

                        }

                    } else {
                        responseID = 1001;

                    }

//				fgfg
                } else {
                    responseID = 2;
                }

            }

            List<Feedback> feedback = dbAdapter.getFeedback(pref.getUserId());
            if (feedback.size() == 0) {
                responseID = 2;
            } else {
                for (int i = 0; i < feedback.size(); i++) {
                    try {
                        if (!feedback.get(i).Image_Path.equalsIgnoreCase("")) {
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            Bitmap bmp = BitmapFactory.decodeFile(feedback
                                    .get(i).Image_Path);


                            //bmp.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                            bmp.compress(Bitmap.CompressFormat.PNG, 50, bao);
                            byte[] bmpPicByteArray = bao.toByteArray();
                            String base64 = Base64.encodeToString(
                                    bmpPicByteArray, Base64.DEFAULT);

                            SoapPrimitive soap_uploadSignature = ws.uploadFile(
                                    base64, feedback.get(i).Signature);
//							if (soap_uploadSignature.toString()
//									.equalsIgnoreCase("Success")) {
                            SoapPrimitive response = ws
                                    .SaveFeedback(feedback.get(i));

                            if (response != null) {
                                if (response.toString().equalsIgnoreCase(
                                        "true")) {
                                    ContentValues cv1 = new ContentValues();
                                    cv1.put("sync_status", "U");
                                    responseID = dbAdapter
                                            .update("feedback_form",
                                                    cv1,
                                                    "complaint_no = '"
                                                            + feedback
                                                            .get(i).Complaint_No
                                                            + "'", null);
                                    isSaved = true;
                                }
                            }
//							}
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return responseID;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (result > 0) {
                if (result == 1001) {
                    UtilityClass.showToast(context, "File upload failed");
                } else if (result == 3) {
                    UtilityClass.showToast(context,
                            "Sync Failed !! Please Try Again !!");
                } else {
                    UtilityClass.showToast(context, "Sync sucessful");
                    // Intent intent2 = new
                    // Intent(HomeScreenActivity.this,MainLauncherActivity.class);
                    // intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // startActivity(intent2);
                    // HomeScreenActivity.this.finish();
                }
            } else {
                UtilityClass.showToast(context, "Error in Sync");
            }
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}
