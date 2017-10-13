package com.sudesi.hafele.faultreport;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.sudesi.hafele.adapter.AttendedListAdapter;
import com.sudesi.hafele.adapter.ComplaintListAdapter;
import com.sudesi.hafele.adapter.DelayedListAdapter;
import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.utils.UtilityClass;
import com.sudesi.hafele.utils.Validation;
import com.sudesi.hafele.webservice.HafeleWebservice;

public class DashBoardActivity extends Activity implements OnClickListener,
        OnDateChangedListener, OnTimeChangedListener {
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat);
    Context context;
    Complaint complaint;
    HafeleFaultReportDBAdapter dbAdapter;
    ComplaintListAdapter listAdapter1;
    ComplaintListAdapter listAdapter2;
    AttendedListAdapter listAdapter3;
    ComplaintListAdapter listAdapter4;
    DelayedListAdapter listAdapter5;
    ListView new_complaints_list;
    ListView accepted_complaints_list;
    ListView attended_complaints_list;
    // ListView on_hold_complaints_list ;
    ListView delayed_complaints_list;
    List<Complaint> result1;
    List<Complaint> result2;
    List<Complaint> result3;
    List<Complaint> result4;
    List<Complaint> result5;
    List<String> newDealylist;
    HafeleWebservice ws;
    HafelePreference pref;
    AlertDialog builder;
    // boolean showSubmit = false;
    TextView count_1, count_2, count_3, count_4;
    TextView appointment_dt;
    Calendar mCalendar;
    DecimalFormat format = new DecimalFormat("00");
    TimePicker app_time;
    CalendarView app_date;
    String resheduleDateTime;
    ProgressDialog progress;
    LinearLayout layout;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");// yyyy-MM-dd
    String date;
    Spinner reason;
    TextView dt_time;
    View inflated = null;
    CalendarView dt_picker;
    TimePicker tm_picker;
    Boolean delay_flag = false;


    public static final String inputFormat = "HH:mm";

    private Date dateeee;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dashboard);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        progress = new ProgressDialog(context);
        dbAdapter = new HafeleFaultReportDBAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        ws = new HafeleWebservice(context);
        pref = new HafelePreference(context);
        GetDataTask getData = new GetDataTask();
        getData.execute();

        Cursor c = dbAdapter.name();
        Log.e("Fault", DatabaseUtils.dumpCursorToString(c));

        ImageView home_button = (ImageView) findViewById(R.id.home);
        ImageView logout_button = (ImageView) findViewById(R.id.logout);
        new_complaints_list = (ListView) findViewById(R.id.new_complaints_list);
        accepted_complaints_list = (ListView) findViewById(R.id.accepted_complaints_list);
        attended_complaints_list = (ListView) findViewById(R.id.attended_complaints_list);
        // on_hold_complaints_list =
        // (ListView)findViewById(R.id.on_hold_complaints_list);
        delayed_complaints_list = (ListView) findViewById(R.id.delayed_complaints_list);

        count_1 = (TextView) findViewById(R.id.count_1);
        count_2 = (TextView) findViewById(R.id.count_2);
        count_3 = (TextView) findViewById(R.id.count_3);
        count_4 = (TextView) findViewById(R.id.count_4);

        // home_button.setVisibility(View.GONE);
        home_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

        new_complaints_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                showOption(result5.get(position), "");
            }
        });

        accepted_complaints_list
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {

                        showCallInitiateOption(result1.get(position), position);

                    }
                });

        attended_complaints_list
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        Complaint complaint = result2.get(position);
                        if (complaint.complaint_number.startsWith("S")) {
                            Intent s_visit = new Intent(context,
                                    SiteVisitForm.class);
                            s_visit.putExtra("complaint_number", complaint.complaint_number);
                            s_visit.putExtra("complaint_date", complaint.date);
                            s_visit.putExtra("user_type", complaint.user_type);
                            s_visit.putExtra("end_user_name", complaint.end_user_name);
                            s_visit.putExtra("end_user_mobile", complaint.end_user_mobile);
                            s_visit.putExtra("service_franchise", complaint.service_franchise);
                            s_visit.putExtra("article_no", complaint.article_no);
                            s_visit.putExtra("end_user_address", complaint.end_user_address);
                            s_visit.putExtra("end_user_region", complaint.end_user_region);
                            s_visit.putExtra("sales_sub_region", complaint.sales_sub_region);
                            s_visit.putExtra("product_group", complaint.product_group);
                            s_visit.putExtra("product_category", complaint.product_category);
                            s_visit.putExtra("product_sub_category", complaint.product_sub_category);
                            s_visit.putExtra("service_details", complaint.service_details);
                            s_visit.putExtra("is_visited", complaint.visit_count);
                            s_visit.putExtra("Created_By", complaint.created_by);
                            startActivity(s_visit);

                        } else {
                            Intent f_report = new Intent(context,
                                    FaultReportForm.class);
                            // try{
                            f_report.putExtra("complaint_number", complaint.complaint_number);
                            if (complaint.date != null)
                                f_report.putExtra("complaint_date", complaint.date);
                            if (complaint.end_user_name != null)
                                f_report.putExtra("end_user_name",
                                        complaint.end_user_name);
                            if (complaint.end_user_mobile != null)
                                f_report.putExtra("end_user_mobile",
                                        complaint.end_user_mobile);
                            if (complaint.user_type != null)
                                f_report.putExtra("user_type",
                                        complaint.user_type);
                            if (complaint.service_franchise != null)
                                f_report.putExtra("service_franchise",
                                        complaint.service_franchise);
                            if (complaint.article_no != null)
                                f_report.putExtra("article_no", complaint.article_no);
                            if (complaint.end_user_address != null)
                                f_report.putExtra("end_user_address", complaint.end_user_address);
                            if (complaint.end_user_region != null)
                                f_report.putExtra("end_user_region", complaint.end_user_region);
                            if (complaint.sales_sub_region != null)
                                f_report.putExtra("sales_sub_region", complaint.sales_sub_region);
                            if (complaint.product_group != null)
                                f_report.putExtra("product_group", complaint.product_group);
                            if (complaint.product_category != null)
                                f_report.putExtra("product_category", complaint.product_category);
                            if (complaint.product_sub_category != null)
                                f_report.putExtra("product_sub_category", complaint.product_sub_category);
                            if (complaint.service_details != null)
                                f_report.putExtra("service_details", complaint.service_details);
                            if (complaint.visit_count != 0)
                                f_report.putExtra("is_visited", complaint.visit_count);
                            if (complaint.created_by != null)
                                f_report.putExtra("Created_By", complaint.created_by);
                            startActivity(f_report);
                            // }catch(Exception e){
                            // e.printStackTrace();
                            // }
                        }
                    }
                });

        delayed_complaints_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


                Complaint complain = (Complaint) delayed_complaints_list.getAdapter().getItem(position);

                String complnt_no = complain.complaint_number;

                //Toast.makeText(getApplicationContext(), String.valueOf(comp_no),Toast.LENGTH_SHORT).show();

                if (dbAdapter.getAcceptedStatusbyCompaintNumber(complnt_no) == false) {


                    showOption1(result4.get(position), "");
                    Log.e("Cmpletent accepted sucessful", String.valueOf(result4.toString()));

                } else {

                    if (dbAdapter.getCalleddate(complnt_no) == true) {
                        showCallInitiateOption(result4.get(position), position);
                    } else {
                        Complaint complaint = result4.get(position);

                        if (complaint.complaint_number.startsWith("S")) {
                            Intent s_visit = new Intent(context,
                                    SiteVisitForm.class);
                            s_visit.putExtra("complaint_number",
                                    complaint.complaint_number);
                            s_visit.putExtra("complaint_date", complaint.date);
                            s_visit.putExtra("user_type", complaint.user_type);
                            s_visit.putExtra("end_user_name",
                                    complaint.end_user_name);
                            s_visit.putExtra("end_user_mobile",
                                    complaint.end_user_mobile);
                            s_visit.putExtra("service_franchise",
                                    complaint.service_franchise);
                            s_visit.putExtra("article_no", complaint.article_no);
                            s_visit.putExtra("end_user_address",
                                    complaint.end_user_address);
                            s_visit.putExtra("end_user_region",
                                    complaint.end_user_region);
                            s_visit.putExtra("sales_sub_region",
                                    complaint.sales_sub_region);
                            s_visit.putExtra("product_group",
                                    complaint.product_group);
                            s_visit.putExtra("product_category",
                                    complaint.product_category);
                            s_visit.putExtra("product_sub_category",
                                    complaint.product_sub_category);
                            s_visit.putExtra("service_details",
                                    complaint.service_details);
                            s_visit.putExtra("is_visited",
                                    complaint.visit_count);
                            s_visit.putExtra("Created_By", complaint.created_by);
                            startActivity(s_visit);

                        } else {
                            Intent f_report = new Intent(context,
                                    FaultReportForm.class);
                            // try{
                            f_report.putExtra("complaint_number",
                                    complaint.complaint_number);
                            if (complaint.date != null)
                                f_report.putExtra("complaint_date",
                                        complaint.date);
                            if (complaint.end_user_name != null)
                                f_report.putExtra("end_user_name",
                                        complaint.end_user_name);
                            if (complaint.end_user_mobile != null)
                                f_report.putExtra("end_user_mobile",
                                        complaint.end_user_mobile);
                            if (complaint.user_type != null)
                                f_report.putExtra("user_type",
                                        complaint.user_type);
                            if (complaint.service_franchise != null)
                                f_report.putExtra("service_franchise",
                                        complaint.service_franchise);
                            if (complaint.article_no != null)
                                f_report.putExtra("article_no",
                                        complaint.article_no);
                            if (complaint.end_user_address != null)
                                f_report.putExtra("end_user_address",
                                        complaint.end_user_address);
                            if (complaint.end_user_region != null)
                                f_report.putExtra("end_user_region",
                                        complaint.end_user_region);
                            if (complaint.sales_sub_region != null)
                                f_report.putExtra("sales_sub_region",
                                        complaint.sales_sub_region);
                            if (complaint.product_group != null)
                                f_report.putExtra("product_group",
                                        complaint.product_group);
                            if (complaint.product_category != null)
                                f_report.putExtra("product_category",
                                        complaint.product_category);
                            if (complaint.product_sub_category != null)
                                f_report.putExtra("product_sub_category",
                                        complaint.product_sub_category);
                            if (complaint.service_details != null)
                                f_report.putExtra("service_details",
                                        complaint.service_details);
                            if (complaint.visit_count != 0)
                                f_report.putExtra("is_visited",
                                        complaint.visit_count);
                            if (complaint.created_by != null)
                                f_report.putExtra("Created_By",
                                        complaint.created_by);
                            startActivity(f_report);

                        }


                    }
                }

				/*				Complaint complaint = result4.get(position);
                        if (complaint.complaint_number.startsWith("S")) {
							Intent s_visit = new Intent(context,
									SiteVisitForm.class);
							s_visit.putExtra("complaint_number",
									complaint.complaint_number);
							s_visit.putExtra("complaint_date", complaint.date);
							s_visit.putExtra("end_user_name",
									complaint.end_user_name);
							s_visit.putExtra("end_user_mobile",
									complaint.end_user_mobile);
							s_visit.putExtra("service_franchise",
									complaint.service_franchise);
							s_visit.putExtra("article_no", complaint.article_no);
							s_visit.putExtra("end_user_address",
									complaint.end_user_address);
							s_visit.putExtra("end_user_region",
									complaint.end_user_region);
							s_visit.putExtra("sales_sub_region",
									complaint.sales_sub_region);
							s_visit.putExtra("product_group",
									complaint.product_group);
							s_visit.putExtra("product_category",
									complaint.product_category);
							s_visit.putExtra("product_sub_category",
									complaint.product_sub_category);
							s_visit.putExtra("service_details",
									complaint.service_details);
							s_visit.putExtra("is_visited",
									complaint.visit_count);
							s_visit.putExtra("Created_By", complaint.created_by);
							startActivity(s_visit);

						} else {
							Intent f_report = new Intent(context,
									FaultReportForm.class);
							// try{
							f_report.putExtra("complaint_number",
									complaint.complaint_number);
							if (complaint.date != null)
								f_report.putExtra("complaint_date",
										complaint.date);
							if (complaint.end_user_name != null)
								f_report.putExtra("end_user_name",
										complaint.end_user_name);
							if (complaint.end_user_mobile != null)
								f_report.putExtra("end_user_mobile",
										complaint.end_user_mobile);
							if (complaint.service_franchise != null)
								f_report.putExtra("service_franchise",
										complaint.service_franchise);
							if (complaint.article_no != null)
								f_report.putExtra("article_no",
										complaint.article_no);
							if (complaint.end_user_address != null)
								f_report.putExtra("end_user_address",
										complaint.end_user_address);
							if (complaint.end_user_region != null)
								f_report.putExtra("end_user_region",
										complaint.end_user_region);
							if (complaint.sales_sub_region != null)
								f_report.putExtra("sales_sub_region",
										complaint.sales_sub_region);
							if (complaint.product_group != null)
								f_report.putExtra("product_group",
										complaint.product_group);
							if (complaint.product_category != null)
								f_report.putExtra("product_category",
										complaint.product_category);
							if (complaint.product_sub_category != null)
								f_report.putExtra("product_sub_category",
										complaint.product_sub_category);
							if (complaint.service_details != null)
								f_report.putExtra("service_details",
										complaint.service_details);
							if (complaint.visit_count != 0)
								f_report.putExtra("is_visited",
										complaint.visit_count);
							if (complaint.created_by != null)
								f_report.putExtra("Created_By",
										complaint.created_by);
							startActivity(f_report);
							// }catch(Exception e){
							// e.printStackTrace();
							// }
						//}
						}
				}*/
            }

        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home:
                Intent intent1 = new Intent(DashBoardActivity.this,
                        HomeScreenActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                break;

            case R.id.logout:
                Intent intent = new Intent(DashBoardActivity.this,
                        MainLauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.accept:
                builder.dismiss();
                ContentValues cv = new ContentValues();
                // cv.put("case_attended","Y");
                cv.put("color_code", "");

                Long resp;
                if (dbAdapter.checkID(complaint.complaint_number,
                        "complaint_service_details", "complaint_number")) {
                    resp = (long) dbAdapter.update("complaint_service_details", cv,
                            "complaint_number = '" + complaint.complaint_number
                                    + "'", null);
                } else {
                    resp = dbAdapter.submitQuery(cv);
                }
                Calendar calendar = Calendar.getInstance();
                Date calledDate = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String str_calledDate = sdf.format(calledDate);

                ContentValues conValues = new ContentValues();

                conValues.put("Called_Date", str_calledDate);
                dbAdapter.update("Fault_Finding_Details", conValues, "Complant_No=?",
                        new String[]{complaint.complaint_number});

                if (complaint.complaint_number.startsWith("S")) {
                    Intent s_visit = new Intent(context, SiteVisitForm.class);
                    s_visit.putExtra("complaint_number", complaint.complaint_number);
                    s_visit.putExtra("complaint_date", complaint.date);
                    s_visit.putExtra("user_type", complaint.user_type);
                    s_visit.putExtra("end_user_name", complaint.end_user_name);
                    s_visit.putExtra("end_user_mobile", complaint.end_user_mobile);
                    s_visit.putExtra("service_franchise", complaint.service_franchise);
                    s_visit.putExtra("article_no", complaint.article_no);
                    s_visit.putExtra("end_user_address", complaint.end_user_address);
                    s_visit.putExtra("end_user_region", complaint.end_user_region);
                    s_visit.putExtra("sales_sub_region", complaint.sales_sub_region);
                    s_visit.putExtra("product_group", complaint.product_group);
                    s_visit.putExtra("product_category", complaint.product_category);
                    s_visit.putExtra("product_sub_category", complaint.product_sub_category);
                    s_visit.putExtra("service_details", complaint.service_details);
                    s_visit.putExtra("is_visited", complaint.visit_count);

                    s_visit.putExtra("Created_By", complaint.created_by);
                    startActivity(s_visit);

                } else {
                    Intent f_report = new Intent(context, FaultReportForm.class);
                    // try{
                    f_report.putExtra("complaint_number",
                            complaint.complaint_number);
                    if (complaint.date != null)
                        f_report.putExtra("complaint_date", complaint.date);
                    if (complaint.end_user_name != null)
                        f_report.putExtra("end_user_name", complaint.end_user_name);
                    if (complaint.end_user_mobile != null)
                        f_report.putExtra("end_user_mobile",
                                complaint.end_user_mobile);
                    if (complaint.user_type != null)
                        f_report.putExtra("user_type", complaint.user_type);
                    if (complaint.service_franchise != null)
                        f_report.putExtra("service_franchise",
                                complaint.service_franchise);
                    if (complaint.article_no != null)
                        f_report.putExtra("article_no", complaint.article_no);
                    if (complaint.end_user_address != null)
                        f_report.putExtra("end_user_address",
                                complaint.end_user_address);
                    if (complaint.end_user_region != null)
                        f_report.putExtra("end_user_region",
                                complaint.end_user_region);
                    if (complaint.sales_sub_region != null)
                        f_report.putExtra("sales_sub_region",
                                complaint.sales_sub_region);
                    if (complaint.product_group != null)
                        f_report.putExtra("product_group", complaint.product_group);
                    if (complaint.product_category != null)
                        f_report.putExtra("product_category",
                                complaint.product_category);
                    if (complaint.product_sub_category != null)
                        f_report.putExtra("product_sub_category",
                                complaint.product_sub_category);
                    if (complaint.service_details != null)
                        f_report.putExtra("service_details",
                                complaint.service_details);
                    if (complaint.visit_count != 0)
                        f_report.putExtra("is_visited", complaint.visit_count);
                    if (complaint.created_by != null)
                        f_report.putExtra("Created_By", complaint.created_by);
                    startActivity(f_report);
                    // }catch(Exception e){
                    // e.printStackTrace();
                    // }
                }
                break;
        }
    }

    private void showCallInitiateOption(final Complaint complain,
                                        final int position) {
        complaint = complain;

        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.call_initiate_view, null);
        RadioButton accept = (RadioButton) view.findViewById(R.id.accept);
        accept.setOnClickListener(this);
        RadioButton reject = (RadioButton) view.findViewById(R.id.reject);
        // final ViewStub stub = (ViewStub)view.findViewById(R.id.stub);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout.setVisibility(View.GONE);

        reject.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // inflated = stub.inflate();
                // inflated.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
            }
        });

        reason = (Spinner) view.findViewById(R.id.reason);
        dt_time = (TextView) view.findViewById(R.id.dt_time);
        dt_picker = (CalendarView) view.findViewById(R.id.dt_picker);
        tm_picker = (TimePicker) view.findViewById(R.id.tm_picker);
        mCalendar = Calendar.getInstance();

        dt_picker.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // int hour = tm_picker.getCurrentHour();
                // String formattedDt = dateFormat.format(dt_picker.getDate());
                // //
                // appointment_dt.setBackgroundColor(Color.parseColor("#FE6F6F"));
                // int date = Integer.parseInt(formattedDt.substring(8, 10));
                // int _month = Integer.parseInt(formattedDt.substring(5, 7));

                // if (_month < mCalendar.get(Calendar.MONTH) + 1) {
                // UtilityClass.showToast(context, "Past month not allowed");
                // } else {
                // if (date < mCalendar.get(Calendar.DATE)) {
                // UtilityClass
                // .showToast(context, "Past date not allowed");
                // } else {
                // if (hour < mCalendar.get(Calendar.HOUR_OF_DAY)) {
                // UtilityClass.showToast(context,
                // "Past time not allowed");
                // } else {
                //
                // if (hour > 12) {
                // hour = hour - 12;
                // dt_time.setText(dateFormat.format(dt_picker
                // .getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(tm_picker
                // .getCurrentMinute()) + " PM");
                // } else if (hour == 12) {
                // dt_time.setText(dateFormat.format(dt_picker
                // .getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(tm_picker
                // .getCurrentMinute()) + " PM");
                // } else if (hour == 0) {
                // dt_time.setText(dateFormat.format(dt_picker
                // .getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(tm_picker
                // .getCurrentMinute()) + " AM");
                // } else {
                // dt_time.setText(dateFormat.format(dt_picker
                // .getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(tm_picker
                // .getCurrentMinute()) + " AM");
                // }
                //
                // resheduleDateTime = dt_time.getText().toString();
                // }
                //
                // }
                // }
            }
        });
        tm_picker.setOnTimeChangedListener(new OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                // int hour = tm_picker.getCurrentHour();
                // //
                // appointment_dt.setBackgroundColor(Color.parseColor("#FE6F6F"));
                // int date = Integer.parseInt(dateFormat.format(
                // dt_picker.getDate()).substring(8, 10));
                // // int _year =
                // //
                // Integer.parseInt(dateFormat.format(app_date.getDate()).substring(0,
                // // 4));
                // int _month = Integer.parseInt(dateFormat.format(
                // dt_picker.getDate()).substring(5, 7));

                // if (_month < mCalendar.get(Calendar.MONTH) + 1) {
                // UtilityClass.showToast(context, "Past month not allowed");
                // } else {
                // if (date < mCalendar.get(Calendar.DATE)) {
                // UtilityClass
                // .showToast(context, "Past date not allowed");
                // } else {
                // if (hour < mCalendar.get(Calendar.HOUR_OF_DAY)) {
                // UtilityClass.showToast(context,
                // "Past time not allowed");
                // } else {
                // if (resheduleDateTime != null)
                // resheduleDateTime = resheduleDateTime
                // .substring(0, 10);
                // if (hour > 12) {
                // hour = hour - 12;
                // dt_time.setText("");
                //
                // dt_time.setText(resheduleDateTime + " "
                // + format.format(hour) + ":"
                // + format.format(minute) + " PM");
                // } else if (hour == 12) {
                // dt_time.setText("");
                // dt_time.setText(resheduleDateTime + " "
                // + format.format(hour) + ":"
                // + format.format(minute) + " PM");
                // } else if (hour == 0) {
                // dt_time.setText("");
                // dt_time.setText(resheduleDateTime + " "
                // + format.format(hour) + ":"
                // + format.format(minute) + " AM");
                // } else {
                // dt_time.setText("");
                // dt_time.setText(resheduleDateTime + " "
                // + format.format(hour) + ":"
                // + format.format(minute) + " AM");
                // }
                // }
                // }
                // }
            }
        });

        Button submit_reason = (Button) view.findViewById(R.id.submit_reason);
        submit_reason.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = tm_picker.getCurrentHour();
                Calendar c = Calendar.getInstance();
                Date today = c.getTime();

                Date date1 = new Date(dt_picker.getDate());
                if (dateFormat.format(today).equals(dateFormat.format(date1))) {
                    System.out.println("Date1 is equal Date2");

                    if (hour > 12) {
                        hour = hour - 12;
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " PM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " PM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " AM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " AM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    }

                    // Calendar now = Calendar.getInstance();
                    //
                    // String compareStringOne = String.valueOf(dt_picker
                    // .getCurrentHour()) + ":"
                    // + String.valueOf(dt_picker.getCurrentMinute());
                    //
                    //
                    // int h = now.get(Calendar.HOUR);
                    // int m = now.get(Calendar.MINUTE);
                    //
                    // dateeee = parseDate(h + ":" + m);
                    // dateCompareOne = parseDate(compareStringOne);
                    //
                    // Log.e("dateeee", String.valueOf(parseDate(h + ":" + m)));
                    // Log.e("dateCompareOne",
                    // String.valueOf(parseDate(compareStringOne)));
                    //
                    // if (dateCompareOne.before(dateeee)) {
                    // // yada yada
                    //
                    // System.out.println("dateCompareOne is equal current time");
                    // }

                    ResheduleMeetingTask reschedule = new ResheduleMeetingTask(
                            complain, position);
                    reschedule.execute();
                    // listAdapter2.setbackground(complain, position);
                    builder.dismiss();

                } else if (date1.after(today)) {
                    System.out.println("Date1 is after Date2");
                    if (hour > 12) {
                        hour = hour - 12;
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " PM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " PM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " AM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    } else {
                        dt_time.setText(dateFormat.format(dt_picker.getDate())
                                + " " + format.format(hour) + ":"
                                + format.format(tm_picker.getCurrentMinute())
                                + " AM");
                        dt_time.setBackgroundColor(Color.parseColor("#FE6F6F"));
                    }

                    ResheduleMeetingTask reschedule = new ResheduleMeetingTask(
                            complain, position);
                    reschedule.execute();
                    // listAdapter2.setbackground(complain, position);
                    builder.dismiss();

                } else {
                    UtilityClass.showToast(context, "Past Date is Not Allowed");
                    dt_time.setText("");
                }

            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Call Initiating");
        builder = dialog.create();
        builder.setView(view);
        builder.show();
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<Complaint>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // progress.setMessage("Loading ...");
            // progress.show();
        }

        @Override
        protected List<Complaint> doInBackground(Void... params) {

            List<Complaint> complaint_list = dbAdapter.getComplainOrServiceRequests(pref.getUserName(), null);
            dbAdapter.close();
            return complaint_list;
        }

        @Override
        protected void onPostExecute(List<Complaint> result) {
            // if (progress.isShowing())
            // progress.dismiss();
            result1 = new LinkedList<Complaint>();
            result1 = dbAdapter.getComplainOrServiceRequests(pref.getUserName(), "y");
            result2 = new LinkedList<Complaint>();
            // result3 = new LinkedList<Complaint>();
            result4 = new LinkedList<Complaint>();
            result5 = new LinkedList<Complaint>();
            newDealylist = new LinkedList<String>();


            for (int i = 0; i < result.size(); i++) {
                Complaint complaint = (Complaint) result.get(i);

                // add delayed value
           /*    int delayedCompliant = (int) (long)(UtilityClass.calculateDateDifference(context, complaint.date));
                complaint.delayedValue = delayedCompliant;
*/

                if (complaint.is_accepted != null)
                {
//                  if (complaint.is_accepted.equalsIgnoreCase("y"))
                    //                      result1.add(complaint);
                    // if(complain.is_accepted.equalsIgnoreCase("n"))result2.add(complain);
                    int count = 0;
                    try {
                        count = complaint.visit_count;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (count != 0) {
                        for (int j = 0; j < result1.size(); j++) {
                            if (result1.get(j).complaint_number.equalsIgnoreCase(complaint.complaint_number))
                            {
                                result1.remove(j);
                                result2.add(complaint);
                                break;
                            }
                        }
//                        result1.remove(result1.indexOf(complaint.complaint_number));
//                        result2.add(complaint);
                    }
//                    else if(count==0)
//                    {
//                        if (result1.contains(complaint))
//                        result1.add(complaint);
//                    }

                    if (UtilityClass.calculateDateDifference(context, complaint.date)
                            > 72)
                    {
                        if (result5.contains(complaint))
                            result5.remove(complaint);

                        for (int j = 0; j < result1.size(); j++) {
                            if (result1.get(j).complaint_number.equalsIgnoreCase(complaint.complaint_number)) {
                                result1.remove(j);
                                break;
                            }
                        }


                        if (result2.contains(complaint))
                            result2.remove(complaint);

                        result4.add(complaint);
                    }

                } else {
                    result5.add(complaint);
                    if (UtilityClass.calculateDateDifference(context, complaint.date)
                            > 72) {
                        if (result5.contains(complaint))
                            result5.remove(complaint);
                        result4.add(complaint);
                    }
                }
            }

       /*    for(int k =0 ;k < result4.size();k++)
            {
                Map<String, List<String>> hm = new HashMap<String, List<String>>();
                ArrayList<String,String> values = new ArrayList<String, String>();
                values.add("Value 1");
                hm.put("Key1", values);

                    Long delay=UtilityClass.calculateDateDifference(context, result4.get(k).complaint_number);
                    newDealylist.add(result4.get(k).complaint_number);
                    break;
            }*/

            count_1.setText(String.valueOf(result5.size()));
            count_2.setText(String.valueOf(result1.size()));
            count_3.setText(String.valueOf(result2.size()));
            count_4.setText(String.valueOf(result4.size()));

            listAdapter1 = new ComplaintListAdapter(context, result5);
            new_complaints_list.setAdapter(listAdapter1);


            listAdapter2 = new ComplaintListAdapter(context, result1);
            accepted_complaints_list.setAdapter(listAdapter2);

            listAdapter3 = new AttendedListAdapter(context, result2);
            attended_complaints_list.setAdapter(listAdapter3);

            // listAdapter4 = new ComplaintListAdapter(context , result3);
            // on_hold_complaints_list.setAdapter(listAdapter4);


           /* Collections.sort(result4, new Comparator<Complaint>() {
                @Override
                public int compare(Complaint a1, Complaint a2) {
                    return a1.delayedValue - a2.delayedValue;
                }
            });
*/
            listAdapter5 = new DelayedListAdapter(context, result4);
            delayed_complaints_list.setAdapter(listAdapter5);

            listAdapter1.notifyDataSetChanged();
            listAdapter2.notifyDataSetChanged();
            listAdapter3.notifyDataSetChanged();
            listAdapter5.notifyDataSetChanged();

          /*  finish();
            startActivity(getIntent());*/
        }
    }

    private void showOption(final Complaint complain, String param) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.option_view, null);

        Pattern zipPattern = Pattern.compile("(\\d{6})");
        Matcher zipMatcher = zipPattern.matcher(complain.end_user_address);

        TextView txt_1 = (TextView) view.findViewById(R.id.txt_1);
        TextView txt_2 = (TextView) view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView) view.findViewById(R.id.txt_3);
        TextView txt_4 = (TextView) view.findViewById(R.id.txt_4);
        TextView txt_5 = (TextView) view.findViewById(R.id.txt_5);
        TextView txt_6 = (TextView) view.findViewById(R.id.txt_6);
        TextView txt_7 = (TextView) view.findViewById(R.id.txt_7);
        TextView txt_8 = (TextView) view.findViewById(R.id.txt_8);
        TextView txt_9 = (TextView) view.findViewById(R.id.txt_9);
        // TextView txt_10 = (TextView) view.findViewById(R.id.txt_10);
        TextView txt_11 = (TextView) view.findViewById(R.id.txt_11);
        TextView txt_12 = (TextView) view.findViewById(R.id.txt_12);
        TextView txt_13 = (TextView) view.findViewById(R.id.txt_13);
        TextView txt_14 = (TextView) view.findViewById(R.id.txt_14);
        app_date = (CalendarView) view.findViewById(R.id.app_date);
        appointment_dt = (TextView) view.findViewById(R.id.appointment_dt);
        // set validation on date
        mCalendar = Calendar.getInstance();
        app_date.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // date =
                // year+"-"+format.format(month+1)+"-"+format.format(dayOfMonth);
                // appointment_dt.setText(date);

                // int hour = app_time.getCurrentHour();
                //
                // int date = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(8, 10));
                // int _year = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(0, 4));
                // int _month = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(5, 7));

                // if (_year < mCalendar.get(Calendar.YEAR)) {
                // UtilityClass.showToast(context,
                // "Past month not allowed");
                // } else {
                // if (_month < mCalendar.get(Calendar.MONTH) + 1 && _year <
                // mCalendar.get(Calendar.YEAR)) {
                // UtilityClass.showToast(context,
                // "Past month not allowed");
                // } else {
                // if (date < mCalendar.get(Calendar.DATE) && _month <
                // mCalendar.get(Calendar.MONTH) + 1) {
                // UtilityClass.showToast(context,
                // "Past date not allowed");
                // } else {
                // if (hour < mCalendar.get(Calendar.HOUR_OF_DAY) && date <
                // mCalendar.get(Calendar.DATE)) {
                // UtilityClass.showToast(context,
                // "Time is already passed");
                // } else {
                // if (hour > 12) {
                // hour = hour - 12;
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute())
                // + " PM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else if (hour == 12) {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute())
                // + " PM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else if (hour == 0) {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute())
                // + " AM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute())
                // + " AM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // }
                // }
                //
                // }
                // }

                // }
            }
        });
        app_time = (TimePicker) view.findViewById(R.id.app_time);
        app_time.setOnTimeChangedListener(new OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // int hour = hourOfDay;
                // int date = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(8, 10));
                // int _year = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(0, 4));
                // int _month = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(5, 7));

                // if (_month < mCalendar.get(Calendar.MONTH) + 1) {
                // UtilityClass.showToast(context, "Past month not allowed");
                // } else {
                // if (date < mCalendar.get(Calendar.DATE)) {
                // UtilityClass
                // .showToast(context, "Past date not allowed");
                // } else {
                // if (hour < mCalendar.get(Calendar.HOUR_OF_DAY)) {
                // UtilityClass.showToast(context,
                // "Time is already passed");
                // } else {
                // if (hour > 12) {
                // hour = hour - 12;
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute()) + " PM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else if (hour == 12) {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute()) + " PM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else if (hour == 0) {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute()) + " AM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // } else {
                // appointment_dt.setText(dateFormat
                // .format(app_date.getDate())
                // + " "
                // + format.format(hour)
                // + ":"
                // + format.format(app_time
                // .getCurrentMinute()) + " AM");
                // appointment_dt.setBackgroundColor(Color
                // .parseColor("#FE6F6F"));
                // }
                // }
                // }
                // }

            }
        });
        TextView t_xt_2 = (TextView) view.findViewById(R.id.t_xt_2);
        TextView t_xt_1 = (TextView) view.findViewById(R.id.t_xt_1);

        // appointment_dt.setText(dateFormat.format(app_date.getDate()));
        Button set_dt = (Button) view.findViewById(R.id.set_dt);
        set_dt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = app_time.getCurrentHour();

                // int minutes = app_time.getCurrentMinute();
                //
                // int date = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(8, 10));
                // int year = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(0, 4));
                // int month = Integer.parseInt(dateFormat.format(
                // app_date.getDate()).substring(5, 7));

                Calendar c = Calendar.getInstance();
                Date today = c.getTime();

                Date date1 = new Date(app_date.getDate());

                Log.e("today", dateFormat.format(today));
                Log.e("date1", dateFormat.format(date1));
                // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // Date date1 = sdf.parse("2009-12-31");
                // Date date2 = sdf.parse("2010-01-31");
                //
                // System.out.println(sdf.format(date1));
                // System.out.println(sdf.format(date2));
                //

                if (dateFormat.format(today).equals(dateFormat.format(date1))) {
                    System.out.println("Date1 is equal Date2");

                    if (hour > 12) {
                        hour = hour - 12;
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    }

                    // Calendar now = Calendar.getInstance();
                    //
                    // String compareStringOne = String.valueOf(app_time
                    // .getCurrentHour()) + ":"
                    // + String.valueOf(app_time.getCurrentMinute());
                    //
                    //
                    // int h = now.get(Calendar.HOUR);
                    // int m = now.get(Calendar.MINUTE);
                    //
                    // dateeee = parseDate(h + ":" + m);
                    // dateCompareOne = parseDate(compareStringOne);
                    //
                    // Log.e("dateeee", String.valueOf(parseDate(h + ":" + m)));
                    // Log.e("dateCompareOne",
                    // String.valueOf(parseDate(compareStringOne)));
                    //
                    // if (dateCompareOne.before(dateeee)) {
                    // // yada yada
                    //
                    // System.out.println("dateCompareOne is equal current time");
                    // }

                } else if (date1.after(today)) {
                    System.out.println("Date1 is after Date2");
                    if (hour > 12) {
                        hour = hour - 12;
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    }

                } else {
                    UtilityClass.showToast(context, "Past Date is Not Allowed");
                    appointment_dt.setText("");
                }

            }
        });

        if (complain.complaint_number.startsWith("C")) {
            t_xt_1.setText("Complaint Number");
            t_xt_2.setText("Complaint Details");
        }
        txt_1.setText(complain.complaint_number);
        txt_2.setText(complain.date);
        txt_3.setText(complain.end_user_name);
        txt_4.setText(complain.end_user_mobile);

        txt_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + complain.end_user_mobile
                        + ""));
                startActivity(callIntent);
            }
        });

        txt_5.setText(complain.user_type);
        txt_6.setText(complain.article_no); // article number
        txt_7.setText(complain.end_user_address);
        txt_8.setText(complain.pin_code);
        txt_9.setText(complain.end_user_region);
        // txt_10.setText(complain.sales_sub_region);
        txt_11.setText(complain.product_group);
        txt_12.setText(complain.product_category);
        txt_13.setText(complain.product_sub_category);
        txt_14.setText(complain.service_details);

        if (zipMatcher.find()) {
            String zip = zipMatcher.group(1);
            complain.pin_code = zip;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        if (param == "na") {
            builder.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else {
            builder.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        }
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;
                        if (Validation.hasText(appointment_dt)) {
                            wantToCloseDialog = true;

                            ContentValues cv = new ContentValues();
                            cv.put("is_accepted", "y");

                            // move complaint/service request to accepted //
                            // update database
                            int response = dbAdapter.update(
                                    "complaint_service_details", cv,
                                    "complaint_number = '"
                                            + complain.complaint_number + "'",
                                    null);

                            Calendar calendar = Calendar.getInstance();
                            Date acceptDate = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss");
                            String str_acceptDate = sdf.format(acceptDate);
                            /*Date date_acceptdate ;
							  SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						        try {
						        	date_acceptdate = dt.parse(str_acceptDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 */

                            cv.clear();
                            cv.put("Appointment_Date", appointment_dt.getText()
                                    .toString());
                            cv.put("sync_status", "NU");
                            cv.put("Complant_No", complain.complaint_number);
                            cv.put("Accepted_Date", str_acceptDate);
                            Log.v("", "cv---------------------" + cv.toString());
                            long resp = dbAdapter.insert(
                                    "Fault_Finding_Details", cv);

                            if (resp > 0) {
                                // UtilityClass.showToast(context,"Update successful");
                                result5.remove(complain);
                                result1.add(complain);

                                listAdapter1.notifyDataSetChanged();
                                count_1.setText(String.valueOf(result5.size()));
                                count_2.setText(String.valueOf(result1.size()));
                                listAdapter2.notifyDataSetChanged();

                            } else {
                                UtilityClass.showToast(context,
                                        "Error in update");
                            }
                        } else {
                            wantToCloseDialog = false;
                            UtilityClass.showToast(context,
                                    "Please enter Date and time");
                        }
                        if (wantToCloseDialog)
                            dialog.dismiss();

                        finish();
                        startActivity(getIntent());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        dbAdapter.close();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        int minHour = -1;
        int minMinute = -1;
        int maxHour = hourOfDay;
        int maxMinute = minute;
        int currentHour = hourOfDay;
        int currentMinute = minute;
        // Update the internal Calendar instance

        boolean validTime = true;
        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
            validTime = false;
        }

        if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
            validTime = false;
        }

        if (validTime) {
            currentHour = maxHour;
            currentMinute = maxMinute;
        }

        updateTime(currentHour, currentMinute);
        mCalendar.set(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH), currentHour,
                currentMinute);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE));
    }

    public void updateTime(int currentHour, int currentMinute) {
        app_time.setCurrentHour(currentHour);
        app_time.setCurrentMinute(currentMinute);
    }

    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    private class ResheduleMeetingTask extends AsyncTask<Void, Long, Long> {
        Long response = (long) 0;
        Complaint complain;
        int position;

        public ResheduleMeetingTask(Complaint complaint, int pos) {
            this.complain = complaint;
            this.position = pos;
        }

        @Override
        protected Long doInBackground(Void... params) {
            String dateTime = dt_time.getText().toString().replace(" PM", "")
                    .replace(" AM", "");

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);


            ContentValues cv = new ContentValues();
            cv.put("Complant_No", complain.complaint_number);
            cv.put("Comment", reason.getSelectedItem().toString());
            cv.put("Closure_Status", "on_hold");
            cv.put("status", "on_hold");
            cv.put("Insert_Date", str_updatedDate);
            cv.put("Appointment_Date", dateTime);
            cv.put("sync_status", "NU");
            if (dbAdapter.checkID(complain.complaint_number,
                    "Fault_Finding_Details", "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details", cv,
                        "Complant_No = '" + complain.complaint_number + "'",
                        null);
            } else {
                response = dbAdapter.submitQuery(cv);
            }

            complain.color_code = "#FE6F6F";

            // listAdapter2.notifyDataSetChanged();
            cv.clear();
            cv.put("status", "on_hold");
            cv.put("case_attended", "N");
            cv.put("color_code", "#FE6F6F");
            Long resp;
            if (dbAdapter.checkID(complain.complaint_number,
                    "complaint_service_details", "complaint_number")) {
                resp = (long) dbAdapter.update("complaint_service_details", cv,
                        "complaint_number = '" + complain.complaint_number
                                + "'", null);
            } else {
                resp = dbAdapter.submitQuery(cv);
            }
            return resp;
        }

        @Override
        public void onPostExecute(Long result) {
            if (result > 0) {
                UtilityClass.showToast(context, "Recorded");
            } else {
                UtilityClass.showToast(context, "Failed ");
            }
        }
    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }


    private void showOption1(final Complaint complain, String param) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.option_view, null);


        Pattern zipPattern = Pattern.compile("(\\d{6})");
        Matcher zipMatcher = zipPattern.matcher(complain.end_user_address);

        TextView txt_1 = (TextView) view.findViewById(R.id.txt_1);
        TextView txt_2 = (TextView) view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView) view.findViewById(R.id.txt_3);
        TextView txt_4 = (TextView) view.findViewById(R.id.txt_4);
        TextView txt_5 = (TextView) view.findViewById(R.id.txt_5);
        TextView txt_6 = (TextView) view.findViewById(R.id.txt_6);
        TextView txt_7 = (TextView) view.findViewById(R.id.txt_7);
        TextView txt_8 = (TextView) view.findViewById(R.id.txt_8);
        TextView txt_9 = (TextView) view.findViewById(R.id.txt_9);
        // TextView txt_10 = (TextView) view.findViewById(R.id.txt_10);
        TextView txt_11 = (TextView) view.findViewById(R.id.txt_11);
        TextView txt_12 = (TextView) view.findViewById(R.id.txt_12);
        TextView txt_13 = (TextView) view.findViewById(R.id.txt_13);
        TextView txt_14 = (TextView) view.findViewById(R.id.txt_14);
        app_date = (CalendarView) view.findViewById(R.id.app_date);
        appointment_dt = (TextView) view.findViewById(R.id.appointment_dt);
        // set validation on date
        mCalendar = Calendar.getInstance();
        app_date.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {

            }
        });
        app_time = (TimePicker) view.findViewById(R.id.app_time);
        app_time.setOnTimeChangedListener(new OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });
        TextView t_xt_2 = (TextView) view.findViewById(R.id.t_xt_2);
        TextView t_xt_1 = (TextView) view.findViewById(R.id.t_xt_1);

        // appointment_dt.setText(dateFormat.format(app_date.getDate()));
        Button set_dt = (Button) view.findViewById(R.id.set_dt);
        set_dt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = app_time.getCurrentHour();
                Calendar c = Calendar.getInstance();
                Date today = c.getTime();

                Date date1 = new Date(app_date.getDate());

                Log.e("today", dateFormat.format(today));
                Log.e("date1", dateFormat.format(date1));

                if (dateFormat.format(today).equals(dateFormat.format(date1))) {
                    System.out.println("Date1 is equal Date2");

                    if (hour > 12) {
                        hour = hour - 12;
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    }


                } else if (date1.after(today)) {
                    System.out.println("Date1 is after Date2");
                    if (hour > 12) {
                        hour = hour - 12;
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 12) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " PM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else if (hour == 0) {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    } else {
                        appointment_dt.setText(dateFormat.format(app_date
                                .getDate())
                                + " "
                                + format.format(hour)
                                + ":"
                                + format.format(app_time.getCurrentMinute())
                                + " AM");
                        appointment_dt.setBackgroundColor(Color
                                .parseColor("#FE6F6F"));
                    }

                } else {
                    UtilityClass.showToast(context, "Past Date is Not Allowed");
                    appointment_dt.setText("");
                }

            }
        });

        if (complain.complaint_number.startsWith("C")) {
            t_xt_1.setText("Complaint Number");
            t_xt_2.setText("Complaint Details");
        }
        txt_1.setText(complain.complaint_number);
        txt_2.setText(complain.date);
        txt_3.setText(complain.end_user_name);
        txt_4.setText(complain.end_user_mobile);

        txt_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + complain.end_user_mobile
                        + ""));
                startActivity(callIntent);
            }
        });

        txt_5.setText(complain.user_type);
        txt_6.setText(complain.article_no); // article number
        txt_7.setText(complain.end_user_address);
        txt_8.setText(complain.pin_code);
        txt_9.setText(complain.end_user_region);
        // txt_10.setText(complain.sales_sub_region);
        txt_11.setText(complain.product_group);
        txt_12.setText(complain.product_category);
        txt_13.setText(complain.product_sub_category);
        txt_14.setText(complain.service_details);

        if (zipMatcher.find()) {
            String zip = zipMatcher.group(1);
            complain.pin_code = zip;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        if (param == "na") {
            builder.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else {
            builder.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        }
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;
                        if (Validation.hasText(appointment_dt)) {
                            wantToCloseDialog = true;

                            ContentValues cv = new ContentValues();
                            cv.put("is_accepted", "y");


                            // move complaint/service request to accepted //
                            // update database
                            int response = dbAdapter.update(
                                    "complaint_service_details", cv,
                                    "complaint_number = '"
                                            + complain.complaint_number + "'",
                                    null);

                            Calendar calendar = Calendar.getInstance();
                            Date acceptDate = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss");
                            String str_acceptDate = sdf.format(acceptDate);
							/*Date date_acceptdate ;
							  SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						        try {
						        	date_acceptdate = dt.parse(str_acceptDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 */

                            cv.clear();
                            cv.put("Appointment_Date", appointment_dt.getText()
                                    .toString());
                            cv.put("sync_status", "NU");
                            cv.put("Complant_No", complain.complaint_number);
                            cv.put("Accepted_Date", str_acceptDate);
                            Log.v("", "cv---------------------" + cv.toString());
                            long resp = dbAdapter.insert(
                                    "Fault_Finding_Details", cv);

                            if (resp > 0) {


                                // UtilityClass.showToast(context,"Update successful");

                                //**********************

								/*	result5.remove(complain);
								result1.add(complain);

								listAdapter1.notifyDataSetChanged();
								count_1.setText(String.valueOf(result5.size()));
								count_2.setText(String.valueOf(result1.size()));
								listAdapter2.notifyDataSetChanged();
								 */
                            } else {
                                UtilityClass.showToast(context,
                                        "Error in update");
                            }
                        } else {
                            wantToCloseDialog = false;
                            UtilityClass.showToast(context,
                                    "Please enter Date and time");
                        }
                        if (wantToCloseDialog)
                            dialog.dismiss();
                    }
                });
    }


}
