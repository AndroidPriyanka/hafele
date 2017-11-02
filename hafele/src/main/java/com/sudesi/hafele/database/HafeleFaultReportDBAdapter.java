package com.sudesi.hafele.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sudesi.hafele.classes.Complaint;
import com.sudesi.hafele.classes.FaultReport;
import com.sudesi.hafele.classes.Feedback;
import com.sudesi.hafele.classes.ImageData;
import com.sudesi.hafele.classes.ProductSubCategory;
import com.sudesi.hafele.classes.Sanitary_Details;
import com.sudesi.hafele.classes.UserClass;
import com.sudesi.hafele.classes.VideoData;
import com.sudesi.hafele.faultreport.DashBoardActivity;
import com.sudesi.hafele.faultreport.MainLauncherActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HafeleFaultReportDBAdapter {

    protected static final String TAG = "DataAdapter";
    private final Context mContext;
    private SQLiteDatabase mDb;
    private HafeleFaultReportDBHelper mDbHelper;

    public HafeleFaultReportDBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new HafeleFaultReportDBHelper(mContext);
    }

    public HafeleFaultReportDBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDatabase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public HafeleFaultReportDBAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDb = mDbHelper.getReadableDatabase();

        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Complaint> getComplainOrServiceRequests(String technician,
                                                        String param) {
        ArrayList<Complaint> complaints = new ArrayList<Complaint>();
        try {
            //String sql = "Select * from complaint_service_details where technician = '"+ technician + "'";

            String sql = "Select * from complaint_service_details where technician = '" + technician + "' order by  substr(date, 7, 4)," +
                    "         substr(date, 4, 2)," +
                    "         substr(date, 1, 2)";
            // String sql = "Select * from complaint_service_details";
            Log.d("query", sql);


            if (param != null) {
                //  sql ="SELECT * FROM Fault_Finding_Details fd, complaint_service_details cd where fd.Complant_No = cd.complaint_number and cd.is_accepted='"+param+"' order by substr(fd.Appointment_Date, 7, 4), substr(fd.Appointment_Date, 4, 2), substr(fd.Appointment_Date, 1, 2), substr(fd.Appointment_Date, 12, 2), substr(fd.Appointment_Date, 15, 2),  substr(fd.Appointment_Date, 18, 2)";
                sql = "SELECT * FROM Fault_Finding_Details fd, complaint_service_details cd where fd.Complant_No = cd.complaint_number and cd.is_accepted='" + param + "' order by fd.Appointment_Date";
                /* sql ="Select * from Fault_Finding_Details where is_accepted = '"+param+"' order by  substr(Appointment_Date, 7, 4)," +
                         "substr(Appointment_Date, 4, 2)," +
                         "substr(Appointment_Date, 1, 2), substr(Appointment_Date, 12, 2), substr(Appointment_Date, 15, 2), substr(Appointment_Date, 18, 2)";*/
            }


            // if(param != null){
            // sql =
            // "Select * from complaint_service_details where is_accepted = '"+param+"'";
            // }else{
            // sql =
            // "Select * from complaint_service_details where is_accepted is null";
            // }
            if (!mDb.isOpen()) {

                try {
                    mDbHelper.openDataBase();
                    mDb = mDbHelper.getReadableDatabase();

                } catch (SQLException mSQLException) {
                    Log.e(TAG, "open >>" + mSQLException.toString());
                    throw mSQLException;
                }
            }
            Cursor c = mDb.rawQuery(sql, null);
            if (c != null) {
                if (c.moveToFirst() && c.getCount() > 0) {
                    do {
                        Complaint complaint = new Complaint();
                        complaint.date = c.getString(c.getColumnIndex("date"));
                        complaint.created_month = c.getString(c
                                .getColumnIndex("created_month"));
                        complaint.created_by = c.getString(c
                                .getColumnIndex("created_by"));
                        // complaint.service_number =
                        // c.getString(c.getColumnIndex("service_number"));
                        complaint.complaint_number = c.getString(c
                                .getColumnIndex("complaint_number"));
                        complaint.user_type = c.getString(c
                                .getColumnIndex("user_type"));
                        complaint.business_category = c.getString(c
                                .getColumnIndex("business_category"));
                        complaint.call_category = c.getString(c
                                .getColumnIndex("call_category"));
                        complaint.channel_partner_name = c.getString(c
                                .getColumnIndex("channel_partner_name"));
                        complaint.channel_partner_address = c.getString(c
                                .getColumnIndex("channel_partner_address"));
                        complaint.channel_partner_region = c.getString(c
                                .getColumnIndex("channel_partner_region"));
                        complaint.channel_partner_city = c.getString(c
                                .getColumnIndex("channel_partner_city"));
                        complaint.channel_partner_mobile = c.getString(c
                                .getColumnIndex("channel_partner_mobile"));
                        complaint.end_user_name = c.getString(c
                                .getColumnIndex("end_user_name"));
                        complaint.end_user_address = c.getString(c
                                .getColumnIndex("end_user_address"));
                        complaint.end_user_region = c.getString(c
                                .getColumnIndex("end_user_region"));
                        complaint.end_user_city = c.getString(c
                                .getColumnIndex("end_user_city"));
                        complaint.end_user_details = c.getString(c
                                .getColumnIndex("end_user_details"));
                        complaint.end_user_type = c.getString(c
                                .getColumnIndex("end_user_type"));
                        complaint.end_user_mobile = c.getString(c
                                .getColumnIndex("end_user_mobile"));
                        complaint.product_group = c.getString(c
                                .getColumnIndex("product_group"));
                        complaint.product_category = c.getString(c
                                .getColumnIndex("product_category"));
                        complaint.product_sub_category = c.getString(c
                                .getColumnIndex("product_sub_category"));
                        complaint.service_details = c.getString(c
                                .getColumnIndex("service_details"));
                        complaint.reason_name = c.getString(c
                                .getColumnIndex("reason_name"));
                        complaint.sales_executive = c.getString(c
                                .getColumnIndex("sales_executive"));
                        complaint.sales_region = c.getString(c
                                .getColumnIndex("sales_region"));
                        complaint.sales_sub_region = c.getString(c
                                .getColumnIndex("sales_sub_region"));
                        complaint.technician = c.getString(c
                                .getColumnIndex("technician"));
                        complaint.hafele_service_team = c.getString(c
                                .getColumnIndex("hafele_service_team"));
                        complaint.regional_admin = c.getString(c
                                .getColumnIndex("regional_admin"));
                        complaint.designer = c.getString(c
                                .getColumnIndex("designer"));
                        complaint.design_center = c.getString(c
                                .getColumnIndex("design_center"));
                        complaint.service_franchise = c.getString(c
                                .getColumnIndex("service_franchise"));
                        complaint.executive_feedback = c.getString(c
                                .getColumnIndex("executive_feedback"));
                        complaint.status = c.getString(c
                                .getColumnIndex("status"));

                        complaint.Age = c.getInt(c.getColumnIndex("Age"));
                        complaint.last_actioned_since = c.getString(c
                                .getColumnIndex("last_actioned_since"));
                        complaint.resolve_date = c.getString(c
                                .getColumnIndex("resolve_date"));
                        complaint.date_time = c.getString(c
                                .getColumnIndex("date_time"));
                        complaint.sync_status = c.getString(c
                                .getColumnIndex("sync_status"));
                        complaint.is_accepted = c.getString(c
                                .getColumnIndex("is_accepted"));
                        complaint.visit_count = c.getInt(c
                                .getColumnIndex("is_visited"));
                        complaint.case_attended = c.getString(c
                                .getColumnIndex("case_attended"));
                        complaint.color_code = c.getString(c
                                .getColumnIndex("color_code"));
                        complaint.Closure_Status = getClosureStatus(complaint.complaint_number);
                        // Log.e("complaint status++", complaint.status);
                        complaints.add(complaint);

                    } while (c.moveToNext());
                    c.close();
                }
            }

        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        return complaints;
    }

    public int update(String table, ContentValues cv, String whereClause,
                      String whereArgs[]) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        int row_id = mDb.update(table, cv, whereClause, whereArgs);

        return row_id;
    }

    public long insert(String table, ContentValues cv) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        long row_id = mDb.insert(table, null, cv);

        return row_id;
    }

    public boolean checkID(String ID, String table, String colName) {
        Boolean result;

        String sql = "select * from " + table + " where " + colName + " = '"
                + ID + "'";
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public List<String> getSubCategories(int prod_id) {
        ArrayList<String> list = new ArrayList<String>();
        String sql = null;
        sql = "Select * from Mst_product_sub_category where prod_cat_id = "
                + prod_id;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    // ProductSubCategory sCat = new ProductSubCategory();
                    String sCat = c.getString(c
                            .getColumnIndex("prod_sub_category"));
                    list.add(sCat);
                } while (c.moveToNext());
                c.close();
            }
        }
        return list;
    }

    public UserClass login(String userName, String password, String android_id) {
        UserClass user = new UserClass();
        String sql = null;
        sql = "select * from login_master where username = '" + userName
                + "' and password = '" + password + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    user.user_id = c.getInt(c.getColumnIndex("user_id"));
                    user.date_time = c.getString(c.getColumnIndex("date_time"));
                    user.id = c.getInt(c.getColumnIndex("id"));
                    user.Designation = c.getString(c
                            .getColumnIndex("Designation"));
                    user.Name = c.getString(c.getColumnIndex("Name"));
                    user.Location = c.getString(c.getColumnIndex("Location"));
                    user.Zone = c.getString(c.getColumnIndex("Zone"));
                    user.Reporting_to = c.getString(c
                            .getColumnIndex("Reporting_To"));
                    user.Second_line_reporting = c.getString(c
                            .getColumnIndex("SecondLine_Reporting"));
                    user.Mobile_Number1 = c.getString(c
                            .getColumnIndex("Mobile_No1"));
                    user.Mobile_Number2 = c.getString(c
                            .getColumnIndex("Mobile_No2"));
                    user.Mobile_Number3 = c.getString(c
                            .getColumnIndex("Mobile_No3"));
                    user.Android_Uid = c.getString(c
                            .getColumnIndex("Android_Uid"));
                    user.ActivationKey = c.getString(c
                            .getColumnIndex("ActivationKey"));
                    user.Deactivationdate = c.getString(c
                            .getColumnIndex("Deactivationdate"));
                    user.ActivationDate = c.getString(c
                            .getColumnIndex("ActivationDate"));
                    user.Status = c.getString(c.getColumnIndex("Status"));

                } while (c.moveToNext());
                c.close();
            }
        }
        return user;
    }

    public Long submitQuery(ContentValues values) {
        Long response = mDb.insert("Fault_Finding_Details", null, values);
        return response;
    }

    public Long submitQuery1(ContentValues values) {
        Long response1 = mDb.insert("sanitary_details", null, values);
        return response1;
    }

    public int getVisitCount(String complaint_number) {
        int count = 0;
        String sql = "Select * from complaint_service_details where complaint_number = '"
                + complaint_number + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                count = c.getInt(c.getColumnIndex("is_visited"));
            }
        }
        return count;
    }

    public List<FaultReport> getFaultReports(String userName) {
        List<FaultReport> reports = new LinkedList<FaultReport>();
        String sql = "SELECT a.*,b.technician,b.date FROM Fault_Finding_Details a , complaint_service_details b where b.complaint_number = a.Complant_No and b.technician = '"
                + userName + "' and a.sync_status = 'NU'";
        Cursor c = mDb.rawQuery(sql, null);
        //	Log.e("getFaultReport", DatabaseUtils.dumpCursorToString(c));

        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    FaultReport report = new FaultReport();
                    report.article_no = c.getString(c
                            .getColumnIndex("article_no"));
                    report.Complant_No = c.getString(c
                            .getColumnIndex("Complant_No"));
                    report.Product_Category = c.getString(c
                            .getColumnIndex("Product_Category"));
                    report.product_Sub_Category = c.getString(c
                            .getColumnIndex("product_Sub_Category"));
                    report.Width = c.getString(c.getColumnIndex("Width"));
                    report.Height = c.getString(c.getColumnIndex("Height"));
                    report.Thickness = c.getString(c
                            .getColumnIndex("Thickness"));
                    report.Correct_Installation = c.getString(c
                            .getColumnIndex("Correct_Installation"));
                    report.Pivot_Alignment = c.getString(c
                            .getColumnIndex("Pivot_Alignment"));
                    report.Door_Noise = c.getString(c
                            .getColumnIndex("Door_Noise"));
                    report.Closing_Speed = c.getString(c
                            .getColumnIndex("Closing_Speed"));
                    report.correct_pro_one = c.getString(c
                            .getColumnIndex("correct_pro_one"));
                    report.correct_pro_two = c.getString(c
                            .getColumnIndex("correct_pro_two"));
                    report.correct_pro_three = c.getString(c
                            .getColumnIndex("correct_pro_three"));
                    report.wrong_product = c.getString(c
                            .getColumnIndex("wrong_product"));
                    report.Screw_Fix = c.getString(c
                            .getColumnIndex("Screw_Fix"));
                    report.Edge_Distance = c.getString(c
                            .getColumnIndex("Edge_Distance"));
                    report.Door_Depth = c.getString(c
                            .getColumnIndex("Door_Depth"));
                    report.Frame_Depth = c.getString(c
                            .getColumnIndex("Frame_Depth"));
                    report.Centre_Cut_Out = c.getString(c
                            .getColumnIndex("Centre_Cut_Out"));
                    report.Latching_Speed = c.getString(c
                            .getColumnIndex("Latching_Speed"));
                    report.Lift_Mechanism_Installed = c.getString(c
                            .getColumnIndex("Lift_Mechanism_Installed"));
                    report.Arms_Installed = c.getString(c
                            .getColumnIndex("Arms_Installed"));
                    report.Arms_Size = c.getString(c
                            .getColumnIndex("Arms_Size"));
                    report.Power_Adjustment = c.getString(c
                            .getColumnIndex("Power_Adjustment"));
                    report.power_factor = c.getString(c
                            .getColumnIndex("power_factor"));
                    report.Product_Power_Factor = c.getString(c
                            .getColumnIndex("Product_Power_Factor"));
                    report.Door_Weight_Within_Range = c.getString(c
                            .getColumnIndex("Door_Weight_Within_Range"));
                    report.Installation = c.getString(c
                            .getColumnIndex("Installation"));
                    report.Track_Roller_Clean = c.getString(c
                            .getColumnIndex("Track_Roller_Clean"));
                    report.Door_Wrapped = c.getString(c
                            .getColumnIndex("Door_Wrapped"));
                    report.Plan_Used = c.getString(c
                            .getColumnIndex("Plan_Used"));
                    report.Carcase_Requirement = c.getString(c
                            .getColumnIndex("Carcase_Requirement"));
                    report.Softclose_Broken = c.getString(c
                            .getColumnIndex("Softclose_Broken"));
                    report.Product_Front_Wt_Available_Cabinet_Ht = c
                            .getString(c
                                    .getColumnIndex("Product_Front_Wt_Available_Cabinet_Ht"));
                    report.Weight_Length_Correct = c.getString(c
                            .getColumnIndex("Weight_Length_Correct"));
                    report.Content_weight_Within_Range = c.getString(c
                            .getColumnIndex("Content_weight_Within_Range"));
                    report.Drawer_Construction_Alignment = c.getString(c
                            .getColumnIndex("Drawer_Construction_Alignment"));
                    report.Product_System_Installation_32 = c.getString(c
                            .getColumnIndex("Product_System_Installation_32"));
                    report.Drawer_Noise_Check = c.getString(c
                            .getColumnIndex("Drawer_Noise_Check"));
                    report.Drawer_Runner_Clean = c.getString(c
                            .getColumnIndex("Drawer_Runner_Clean"));
                    report.Blum_Product_Servodrive = c.getString(c
                            .getColumnIndex("Blum_Product_Servodrive"));
                    report.Lock_Working = c.getString(c
                            .getColumnIndex("Lock_Working"));
                    report.CutOut_Dimension = c.getString(c
                            .getColumnIndex("CutOut_Dimension"));
                    report.Mortise_Lock_Template = c.getString(c
                            .getColumnIndex("Mortise_Lock_Template"));
                    report.Lock_Protruding = c.getString(c
                            .getColumnIndex("Lock_Protruding"));
                    report.Plastic_Fillers = c.getString(c
                            .getColumnIndex("Plastic_Fillers"));
                    report.No_Mortise_Lock = c.getString(c
                            .getColumnIndex("No_Mortise_Lock"));
                    report.Door_Range_More = c.getString(c
                            .getColumnIndex("Door_Range_More"));
                    report.Door_Range_Less = c.getString(c
                            .getColumnIndex("Door_Range_Less"));
                    report.Rose_Touching_Door = c.getString(c
                            .getColumnIndex("Rose_Touching_Door"));
                    report.Spindle_Size_Correct = c.getString(c
                            .getColumnIndex("Spindle_Size_Correct"));
                    report.Spoiled_Spindle = c.getString(c
                            .getColumnIndex("Spoiled_Spindle"));
                    report.Proper_CutOut = c.getString(c
                            .getColumnIndex("Proper_CutOut"));
                    report.Strike_Plate_CutOut = c.getString(c
                            .getColumnIndex("Strike_Plate_CutOut"));
                    report.Thikness_Range = c.getString(c
                            .getColumnIndex("thickness_range"));
                    report.Screw_Built_Check = c.getString(c
                            .getColumnIndex("Screw_Built_Check"));
                    report.Right_Product_Door = c.getString(c
                            .getColumnIndex("Right_Product_Door"));
                    report.Hinges_Number_Sufficeint = c.getString(c
                            .getColumnIndex("Hinges_Number_Sufficeint"));
                    report.CutOut_Details = c.getString(c
                            .getColumnIndex("CutOut_Details"));
                    report.Depth_Thickness_Equal = c.getString(c
                            .getColumnIndex("Depth_Thickness_Equal"));
                    report.Cabinet_Hgt_Door_Wt = c.getString(c
                            .getColumnIndex("Cabinet_Hgt_Door_Wt"));
                    report.Sliding_Sys_Door_Wt = c.getString(c
                            .getColumnIndex("Sliding_Sys_Door_Wt"));
                    report.G_Door_Wt = c.getString(c
                            .getColumnIndex("G_Door_Wt"));
                    report.W_Door_Wt = c.getString(c
                            .getColumnIndex("W_Door_Wt"));
                    report.Insert_Date = c.getString(c
                            .getColumnIndex("Insert_Date"));
                    report.Drawer_Wt = c.getString(c
                            .getColumnIndex("Drawer_Wt"));
                    report.side_volume = c.getString(c
                            .getColumnIndex("side_volume"));
                    report.back_volume = c.getString(c
                            .getColumnIndex("back_volume"));
                    report.base_volume = c.getString(c
                            .getColumnIndex("base_volume"));

                    report.v1_w1 = c.getString(c.getColumnIndex("v1_w1"));
                    report.v2_w2 = c.getString(c.getColumnIndex("v2_w2"));
                    report.v3_w3 = c.getString(c.getColumnIndex("v3_w3"));
                    report.v4_w4 = c.getString(c.getColumnIndex("v4_w4"));

                    report.v1_h1 = c.getString(c.getColumnIndex("v1_h1"));
                    report.v2_h2 = c.getString(c.getColumnIndex("v2_h2"));
                    report.v3_h3 = c.getString(c.getColumnIndex("v3_h3"));
                    report.v4_h4 = c.getString(c.getColumnIndex("v4_h4"));

                    report.v1_t1 = c.getString(c.getColumnIndex("v1_t1"));
                    report.v2_t2 = c.getString(c.getColumnIndex("v2_t2"));
                    report.v3_t3 = c.getString(c.getColumnIndex("v3_t3"));
                    report.v4_t4 = c.getString(c.getColumnIndex("v4_t4"));


                    report.facia_volume = c.getString(c
                            .getColumnIndex("facia_volume"));
                    report.Closure_Status = c.getString(c
                            .getColumnIndex("Closure_Status"));
                    report.Reason_For_Unresolved = c.getString(c
                            .getColumnIndex("Reason_For_Unresolved"));
                    report.Comment = c.getString(c.getColumnIndex("Comment"));
                    report.Appointment_Date = c.getString(c
                            .getColumnIndex("Appointment_Date"));
                    report.Handle_Weight = c.getString(c
                            .getColumnIndex("handle_weight"));

                    Log.v("",
                            "Accepted_Date=="
                                    + c.getString(c
                                    .getColumnIndex("Accepted_Date")));
                    Log.v("",
                            "Called_Date=="
                                    + c.getString(c
                                    .getColumnIndex("Called_Date")));
                    Log.v("",
                            "Updated_Date=="
                                    + c.getString(c
                                    .getColumnIndex("Updated_Date")));
                    Log.v("",
                            "Closed_Date=="
                                    + c.getString(c
                                    .getColumnIndex("Closed_Date")));

                    report.Accepted_Date = c.getString(c
                            .getColumnIndex("Accepted_Date"));
                    report.Called_Date = c.getString(c
                            .getColumnIndex("Called_Date"));
                    report.Updated_Date = c.getString(c
                            .getColumnIndex("Updated_Date"));
                    report.Closed_Date = c.getString(c
                            .getColumnIndex("Closed_Date"));

                    report.Registration_Date = c.getString(c
                            .getColumnIndex("date"));


                    report.Result = c.getString(c.getColumnIndex("Result"));
                    report.Action = c.getString(c.getColumnIndex("Action"));
                    report.sparce_defect = c.getString(c.getColumnIndex("sparce_defect"));
                    report.complete_set = c.getString(c.getColumnIndex("complete_set"));
                    report.site_Issue_Reason = c.getString(c.getColumnIndex("site_Issue_Reason"));

                    report.wrong_product_reason = c.getString(c.getColumnIndex("wrong_product_reason"));
                    report.part_list_is_complete = c.getString(c.getColumnIndex("part_list_is_complete"));
                    report.correct_fitting_order = c.getString(c.getColumnIndex("correct_fitting_order"));
                    report.size_of_the_cabinet_is_more = c.getString(c.getColumnIndex("size_of_the_cabinet_is_more"));
                    report.door_dimensions = c.getString(c.getColumnIndex("door_dimensions"));
                    report.installation_template = c.getString(c.getColumnIndex("installation_template"));
                    report.plinth_legs = c.getString(c.getColumnIndex("plinth_legs"));
                    report.check_hinges = c.getString(c.getColumnIndex("check_hinges"));

                    report.drawer_within_range = c.getString(c.getColumnIndex("drawer_within_range"));
                    report.unit_if_fixed_to_wall = c.getString(c.getColumnIndex("unit_if_fixed_to_wall"));
                    report.enough_legs_for_support = c.getString(c.getColumnIndex("enough_legs_for_support"));
                    report.correct_alignment = c.getString(c.getColumnIndex("correct_alignment"));
                    report.servodrive_sufficient_to_handle_weight = c.getString(c.getColumnIndex("servodrive_sufficient_to_handle_weight"));

                    report.correct_product_order = c.getString(c.getColumnIndex("correct_product_order"));
                    report.type_of_runner = c.getString(c.getColumnIndex("type_of_runner"));
                    report.product_abuse = c.getString(c.getColumnIndex("product_abuse"));

                    report.Wooden_Panel_dimensions = c.getString(c.getColumnIndex("Wooden_Panel_dimensions"));

                    report.power_supply_proper = c.getString(c.getColumnIndex("power_supply_proper"));

                    report.driver_working = c.getString(c.getColumnIndex("driver_working"));
                    report.led_is_working = c.getString(c.getColumnIndex("led_is_working"));

                    report.fitting_templete = c.getString(c.getColumnIndex("fitting_templete"));
                    report.front_adjustments = c.getString(c.getColumnIndex("front_adjustments"));
                    report.correct_no_of_screws = c.getString(c.getColumnIndex("correct_no_of_screws"));
                    report.correct_spacing = c.getString(c.getColumnIndex("correct_spacing"));
                    report.Empty_the_drawer = c.getString(c.getColumnIndex("Empty_the_drawer"));
                    report.dust_gathers = c.getString(c.getColumnIndex("dust_gathers"));
                    report.third_hinge_is_used = c.getString(c.getColumnIndex("third_hinge_is_used"));
                    report.synchro_motion_working_properly = c.getString(c.getColumnIndex("synchro_motion_working_properly"));


                    //	report.correct_product = c.getString(c.getColumnIndex("correct_product"));
                    report.height_more_than_260mm = c.getString(c.getColumnIndex("height_more_than_260mm"));
                    report.length_slatted_or_wooden_50mm = c.getString(c.getColumnIndex("length_slatted_or_wooden_50mm"));
                    report.widht_slatted_or_wooden_30mm = c.getString(c.getColumnIndex("widht_slatted_or_wooden_30mm"));
                    report.slatted_dimen_1400mm_2000mm = c.getString(c.getColumnIndex("slatted_dimen_1400mm_2000mm"));
                    report.slatted_dimen_1800mm_2000mm = c.getString(c.getColumnIndex("slatted_dimen_1800mm_2000mm"));
                    report.bedding_box_fixing = c.getString(c.getColumnIndex("bedding_box_fixing"));
                    report.weight_between_23_25kgs = c.getString(c.getColumnIndex("weight_between_23_25kgs"));
                    report.weight_between_50_60kgs = c.getString(c.getColumnIndex("weight_between_50_60kgs"));
                    report.check_Stabilizing_rod = c.getString(c.getColumnIndex("check_Stabilizing_rod"));


                    reports.add(report);
                } while (c.moveToNext());
                c.close();
            }
        }
        return reports;
    }

    //.......................sanitary details........................
    public List<Sanitary_Details> getSanitaryReports(String userName) {
        Cursor c=null;
        List<Sanitary_Details> reports1 = new LinkedList<Sanitary_Details>();
        String sql = "SELECT a.*,b.technician,b.date FROM sanitary_details a , complaint_service_details b where b.complaint_number = a.Complant_No and b.technician = '"
                + userName + "' and a.sync_status = 'NU'";
         c = mDb.rawQuery(sql, null);
        //	Log.e("getFaultReport", DatabaseUtils.dumpCursorToString(c));

        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    Sanitary_Details report1 = new Sanitary_Details();
                    report1.radio_sanitary = c.getString(c.getColumnIndex("radio_sanitary"));
                    report1.type_of_sanitary = c.getString(c.getColumnIndex("type_of_sanitary"));
                    report1.sanitary_product = c.getString(c.getColumnIndex("sanitary_product"));
                    report1.sanitary_leakage = c.getString(c.getColumnIndex("sanitary_leakage"));
                    report1.sanitary_type_of_leakage = c.getString(c.getColumnIndex("sanitary_type_of_leakage"));
                    report1.does_not_operate = c.getString(c.getColumnIndex("does_not_operate"));
                    report1.type_does_not_operate = c.getString(c.getColumnIndex("type_does_not_operate"));
                    report1.weak_flow = c.getString(c.getColumnIndex("weak_flow"));
                    report1.type_of_weak_flow = c.getString(c.getColumnIndex("type_of_weak_flow"));
                    report1.asthetics = c.getString(c.getColumnIndex("asthetics"));
                    report1.type_of_asthetics = c.getString(c.getColumnIndex("type_of_asthetics"));
                    report1.warranty = c.getString(c.getColumnIndex("warranty"));
                    report1.noise = c.getString(c.getColumnIndex("noise"));
                    report1.flush_not_working = c.getString(c.getColumnIndex("flush_not_working"));
                    report1.type_of_flush_not_working = c.getString(c.getColumnIndex("type_of_flush_not_working"));
                    report1.drainage = c.getString(c.getColumnIndex("drainage"));
                    report1.type_of_drainage = c.getString(c.getColumnIndex("type_of_drainage"));
                    report1.LMD = c.getString(c.getColumnIndex("LMD"));
                    report1.Complant_No = c.getString(c.getColumnIndex("Complant_No"));
                    report1.Product_Category = c.getString(c.getColumnIndex("Product_Category"));
                    report1.product_Sub_Category = c.getString(c.getColumnIndex("product_Sub_Category"));
                    report1.article_no = c.getString(c.getColumnIndex("article_no"));
                    report1.Comment = c.getString(c.getColumnIndex("Comment"));
                    report1.sync_status = c.getString(c.getColumnIndex("sync_status"));
                    report1.Insert_Date = c.getString(c.getColumnIndex("Insert_Date"));
                    report1.Result = c.getString(c.getColumnIndex("Result"));
                    report1.sparce_defect = c.getString(c.getColumnIndex("sparce_defect"));
                    report1.complete_set = c.getString(c.getColumnIndex("complete_set"));
                    report1.site_Issue_Reason = c.getString(c.getColumnIndex("site_Issue_Reason"));
                    report1.Action = c.getString(c.getColumnIndex("Action"));
                    report1.wrong_product_reason = c.getString(c.getColumnIndex("wrong_product_reason"));
                    report1.Reason_For_Unresolved = c.getString(c.getColumnIndex("Reason_For_Unresolved"));
                    report1.Closure_Status = c.getString(c.getColumnIndex("Closure_Status"));
                    report1.Closed_Date = c.getString(c.getColumnIndex("Closed_Date"));
                    report1.Updated_Date = c.getString(c.getColumnIndex("Updated_Date"));
                    report1.technician = c.getString(c.getColumnIndex("technician"));
                    report1.date = c.getString(c.getColumnIndex("date"));
                  //  report1.gudance_given=c.getString(c.getColumnIndex("gudance_given"));
                    reports1.add(report1);
                } while (c.moveToNext());
                c.close();
            }
        }
        return reports1;
    }


    public List<Feedback> getFeedback(int userId) {
        List<Feedback> custFeedback = new LinkedList<Feedback>();
        String sql = null;
        sql = "select * from feedback_form where sync_status = 'NU' and technician_id = '"
                + userId + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    Feedback feedback = new Feedback();
                    feedback.Complaint_No = c.getString(c
                            .getColumnIndex("complaint_no"));
                    feedback.User_Ref_Id = c.getInt(c
                            .getColumnIndex("technician_id"));
                    feedback.Technician_Name = c.getString(c
                            .getColumnIndex("Technician_Name"));
                    feedback.Technician_Rate = c.getString(c
                            .getColumnIndex("Technician_Rate"));
                    feedback.Executive_Name = c.getString(c
                            .getColumnIndex("Executive_Name"));
                    feedback.Executive_Rate = c.getString(c
                            .getColumnIndex("Executive_Rate"));
                    feedback.Rating = c.getString(c.getColumnIndex("Rating"));
                    feedback.Signature = c.getString(c
                            .getColumnIndex("SignatureFile"));
                    feedback.Insert_Date = c.getString(c
                            .getColumnIndex("Insert_Date"));
                    feedback.sync_status = c.getString(c
                            .getColumnIndex("sync_status"));
                    feedback.Image_Path = c.getString(c
                            .getColumnIndex("ImagePath"));
                    custFeedback.add(feedback);
                } while (c.moveToNext());
                c.close();
            }
        }
        return custFeedback;
    }

    public List<ImageData> getImageData(String complainNumber, String param) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        String sql;
        /*
         * if(complainNumber == null){ sql =
		 * "select * from image_details where upload_status = 'NU' order by complaint_number"
		 * ; }else{ sql =
		 * "select * from image_details where complaint_number = '" +
		 * complainNumber+
		 * "' and upload_status = 'NU' order by complaint_number"; }
		 */

        if (param.equals("ALL")) {
            sql = "select * from image_details where complaint_number = '"
                    + complainNumber + "' order by complaint_number";
        } else {
            sql = "select * from image_details where upload_status = 'NU' order by complaint_number";
        }

        Cursor c = mDb.rawQuery(sql, null);
        List<ImageData> imgData = new LinkedList<ImageData>();
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {
                    ImageData ticket = new ImageData();

                    ticket.complaint_number = c.getString(c
                            .getColumnIndex("complaint_number"));
                    ticket.image_name = c.getString(c
                            .getColumnIndex("image_name"));
                    ticket.image_path = c.getString(c
                            .getColumnIndex("image_path"));
                    ticket.original_size = c.getString(c
                            .getColumnIndex("original_size"));
                    ticket.compressed_size = c.getString(c
                            .getColumnIndex("compressed_size"));
                    ticket.device_id = c.getString(c
                            .getColumnIndex("Device_id"));
                    ticket.image_count = c.getString(c
                            .getColumnIndex("image_count"));
                    imgData.add(ticket);

                } while (c.moveToNext());
                c.close();
            }
        }
        return imgData;
    }

    public long saveImageDetails(String name, String absolutePath,
                                 String original_size, String compressed_size,
                                 String complaint_number, String status, int count, String device_id) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put("image_name", name);
        cv.put("image_path", absolutePath);
        cv.put("complaint_number", complaint_number);
        cv.put("original_size", original_size);
        cv.put("compressed_size", compressed_size);
        cv.put("upload_status", status);
        cv.put("image_count", count);
        cv.put("Device_id", device_id);

        return insert("image_details", cv);
    }

    public long saveVideoDetails(String name, String outvidpath,
                                 String originalSize, String compressedSize,
                                 String complaint_number, String status, String userName, int count,
                                 String device_id) {
        if (!mDb.isOpen()) {
            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();
            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put("VideoFileName", name);
        cv.put("FilePath", outvidpath);
        cv.put("complaint_number", complaint_number);
        cv.put("OriginalSize", originalSize);
        cv.put("CompressedSize", compressedSize);
        cv.put("upload_status", status);
        cv.put("username", userName);
        cv.put("video_count", count);
        cv.put("Device_id", device_id);

        Log.e("ContentValues--Insert--Video", cv.toString());

        return insert("video_details", cv);
    }

    public List<VideoData> getVideoData(String complainNumber) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        String sql;
        List<VideoData> videoDataList = new LinkedList<VideoData>();
        if (complainNumber == null) {
            sql = "select * from video_details where upload_status = 'NU' order by complaint_number";
        } else {
            sql = "select * from video_details where complaint_number ='"
                    + complainNumber + "'";
        }

        Cursor c = mDb.rawQuery(sql, null);

        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {

                    VideoData vidData = new VideoData();
                    vidData.complaint_number = c.getString(c
                            .getColumnIndex("complaint_number"));
                    vidData.VideoFileName = c.getString(c
                            .getColumnIndex("VideoFileName"));
                    vidData.FilePath = c
                            .getString(c.getColumnIndex("FilePath"));
                    vidData.OriginalSize = c.getString(c
                            .getColumnIndex("OriginalSize"));
                    vidData.CompressedSize = c.getString(c
                            .getColumnIndex("CompressedSize"));
                    vidData.username = c
                            .getString(c.getColumnIndex("username"));
                    vidData.Device_id = c.getString(c
                            .getColumnIndex("Device_id"));
                    vidData.video_count = c.getString(c
                            .getColumnIndex("video_count"));

                    videoDataList.add(vidData);
                } while (c.moveToNext());
                c.close();
            }
        }
        return videoDataList;
    }

    public FaultReport getFaultReportDetails(String userName,
                                             String complaint_number) {
        FaultReport report = new FaultReport();
        String sql = "SELECT a.*,b.technician FROM Fault_Finding_Details a , complaint_service_details b where b.complaint_number = a.Complant_No and b.technician = '"
                + userName + "' and a.Complant_No = '" + complaint_number + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {

                    Log.e("Fault", DatabaseUtils.dumpCursorToString(c));
                    report.article_no = c.getString(c.getColumnIndex("article_no"));
                    report.Complant_No = c.getString(c.getColumnIndex("Complant_No"));
                    report.Product_Category = c.getString(c.getColumnIndex("Product_Category"));
                    report.product_Sub_Category = c.getString(c.getColumnIndex("product_Sub_Category"));
                    report.Width = c.getString(c.getColumnIndex("Width"));
                    report.Height = c.getString(c.getColumnIndex("Height"));
                    report.Thickness = c.getString(c.getColumnIndex("Thickness"));
                    report.Correct_Installation = c.getString(c.getColumnIndex("Correct_Installation"));
                    report.Pivot_Alignment = c.getString(c.getColumnIndex("Pivot_Alignment"));
                    report.Door_Noise = c.getString(c.getColumnIndex("Door_Noise"));
                    report.Closing_Speed = c.getString(c.getColumnIndex("Closing_Speed"));
                    report.correct_pro_one = c.getString(c
                            .getColumnIndex("correct_pro_one"));
                    report.correct_pro_two = c.getString(c
                            .getColumnIndex("correct_pro_two"));
                    report.correct_pro_three = c.getString(c
                            .getColumnIndex("correct_pro_three"));
                    report.wrong_product = c.getString(c
                            .getColumnIndex("wrong_product"));
                    report.Screw_Fix = c.getString(c
                            .getColumnIndex("Screw_Fix"));
                    report.Edge_Distance = c.getString(c
                            .getColumnIndex("Edge_Distance"));
                    report.Door_Depth = c.getString(c
                            .getColumnIndex("Door_Depth"));
                    report.Frame_Depth = c.getString(c
                            .getColumnIndex("Frame_Depth"));
                    report.Centre_Cut_Out = c.getString(c
                            .getColumnIndex("Centre_Cut_Out"));
                    report.Latching_Speed = c.getString(c
                            .getColumnIndex("Latching_Speed"));
                    report.Lift_Mechanism_Installed = c.getString(c
                            .getColumnIndex("Lift_Mechanism_Installed"));
                    report.Arms_Installed = c.getString(c
                            .getColumnIndex("Arms_Installed"));
                    report.Arms_Size = c.getString(c
                            .getColumnIndex("Arms_Size"));
                    report.Power_Adjustment = c.getString(c
                            .getColumnIndex("Power_Adjustment"));
                    report.power_factor = c.getString(c
                            .getColumnIndex("power_factor"));
                    report.Product_Power_Factor = c.getString(c
                            .getColumnIndex("Product_Power_Factor"));
                    report.Door_Weight_Within_Range = c.getString(c
                            .getColumnIndex("Door_Weight_Within_Range"));
                    report.Installation = c.getString(c
                            .getColumnIndex("Installation"));
                    report.Track_Roller_Clean = c.getString(c
                            .getColumnIndex("Track_Roller_Clean"));
                    report.Door_Wrapped = c.getString(c
                            .getColumnIndex("Door_Wrapped"));
                    report.Plan_Used = c.getString(c
                            .getColumnIndex("Plan_Used"));
                    report.Carcase_Requirement = c.getString(c
                            .getColumnIndex("Carcase_Requirement"));
                    report.Softclose_Broken = c.getString(c
                            .getColumnIndex("Softclose_Broken"));
                    report.Product_Front_Wt_Available_Cabinet_Ht = c
                            .getString(c
                                    .getColumnIndex("Product_Front_Wt_Available_Cabinet_Ht"));
                    report.Weight_Length_Correct = c.getString(c
                            .getColumnIndex("Weight_Length_Correct"));
                    report.Content_weight_Within_Range = c.getString(c
                            .getColumnIndex("Content_weight_Within_Range"));
                    report.Drawer_Construction_Alignment = c.getString(c
                            .getColumnIndex("Drawer_Construction_Alignment"));
                    report.Product_System_Installation_32 = c.getString(c
                            .getColumnIndex("Product_System_Installation_32"));
                    report.Drawer_Noise_Check = c.getString(c
                            .getColumnIndex("Drawer_Noise_Check"));
                    report.Drawer_Runner_Clean = c.getString(c
                            .getColumnIndex("Drawer_Runner_Clean"));
                    report.Blum_Product_Servodrive = c.getString(c
                            .getColumnIndex("Blum_Product_Servodrive"));
                    report.Lock_Working = c.getString(c
                            .getColumnIndex("Lock_Working"));
                    report.CutOut_Dimension = c.getString(c
                            .getColumnIndex("CutOut_Dimension"));
                    report.Mortise_Lock_Template = c.getString(c
                            .getColumnIndex("Mortise_Lock_Template"));
                    report.Lock_Protruding = c.getString(c
                            .getColumnIndex("Lock_Protruding"));
                    report.Plastic_Fillers = c.getString(c
                            .getColumnIndex("Plastic_Fillers"));
                    report.No_Mortise_Lock = c.getString(c
                            .getColumnIndex("No_Mortise_Lock"));
                    report.Door_Range_More = c.getString(c
                            .getColumnIndex("Door_Range_More"));
                    report.Door_Range_Less = c.getString(c
                            .getColumnIndex("Door_Range_Less"));
                    report.Rose_Touching_Door = c.getString(c
                            .getColumnIndex("Rose_Touching_Door"));
                    report.Spindle_Size_Correct = c.getString(c
                            .getColumnIndex("Spindle_Size_Correct"));
                    report.Spoiled_Spindle = c.getString(c
                            .getColumnIndex("Spoiled_Spindle"));
                    report.Proper_CutOut = c.getString(c
                            .getColumnIndex("Proper_CutOut"));
                    report.Strike_Plate_CutOut = c.getString(c
                            .getColumnIndex("Strike_Plate_CutOut"));
                    report.Screw_Built_Check = c.getString(c
                            .getColumnIndex("Screw_Built_Check"));
                    report.Thikness_Range = c.getString(c
                            .getColumnIndex("thickness_range"));
                    report.Right_Product_Door = c.getString(c
                            .getColumnIndex("Right_Product_Door"));
                    report.Hinges_Number_Sufficeint = c.getString(c
                            .getColumnIndex("Hinges_Number_Sufficeint"));
                    report.CutOut_Details = c.getString(c
                            .getColumnIndex("CutOut_Details"));
                    report.Depth_Thickness_Equal = c.getString(c
                            .getColumnIndex("Depth_Thickness_Equal"));
                    report.Cabinet_Hgt_Door_Wt = c.getString(c
                            .getColumnIndex("Cabinet_Hgt_Door_Wt"));
                    report.Sliding_Sys_Door_Wt = c.getString(c
                            .getColumnIndex("Sliding_Sys_Door_Wt"));
                    report.G_Door_Wt = c.getString(c
                            .getColumnIndex("G_Door_Wt"));
                    report.W_Door_Wt = c.getString(c
                            .getColumnIndex("W_Door_Wt"));
                    report.Insert_Date = c.getString(c
                            .getColumnIndex("Insert_Date"));
                    report.Drawer_Wt = c.getString(c
                            .getColumnIndex("Drawer_Wt"));
                    report.side_volume = c.getString(c
                            .getColumnIndex("side_volume"));
                    report.back_volume = c.getString(c
                            .getColumnIndex("back_volume"));
                    report.base_volume = c.getString(c
                            .getColumnIndex("base_volume"));
                    report.facia_volume = c.getString(c
                            .getColumnIndex("facia_volume"));

                    report.v1_w1 = c.getString(c.getColumnIndex("v1_w1"));
                    report.v2_w2 = c.getString(c.getColumnIndex("v2_w2"));
                    report.v3_w3 = c.getString(c.getColumnIndex("v3_w3"));
                    report.v4_w4 = c.getString(c.getColumnIndex("v4_w4"));

                    report.v1_h1 = c.getString(c.getColumnIndex("v1_h1"));
                    report.v2_h2 = c.getString(c.getColumnIndex("v2_h2"));
                    report.v3_h3 = c.getString(c.getColumnIndex("v3_h3"));
                    report.v4_h4 = c.getString(c.getColumnIndex("v4_h4"));

                    report.v1_t1 = c.getString(c.getColumnIndex("v1_t1"));
                    report.v2_t2 = c.getString(c.getColumnIndex("v2_t2"));
                    report.v3_t3 = c.getString(c.getColumnIndex("v3_t3"));
                    report.v4_t4 = c.getString(c.getColumnIndex("v4_t4"));
                    report.Closure_Status = c.getString(c.getColumnIndex("Closure_Status"));
                    report.Reason_For_Unresolved = c.getString(c.getColumnIndex("Reason_For_Unresolved"));
                    report.Comment = c.getString(c.getColumnIndex("Comment"));
                    report.Appointment_Date = c.getString(c.getColumnIndex("Appointment_Date"));
                    report.Handle_Weight = c.getString(c.getColumnIndex("handle_weight"));


                    report.Result = c.getString(c.getColumnIndex("Result"));
                    report.Action = c.getString(c.getColumnIndex("Action"));
                    report.sparce_defect = c.getString(c.getColumnIndex("sparce_defect"));
                    report.complete_set = c.getString(c.getColumnIndex("complete_set"));
                    report.site_Issue_Reason = c.getString(c.getColumnIndex("site_Issue_Reason"));

                    report.wrong_product_reason = c.getString(c.getColumnIndex("wrong_product_reason"));
                    report.part_list_is_complete = c.getString(c.getColumnIndex("part_list_is_complete"));
                    report.correct_fitting_order = c.getString(c.getColumnIndex("correct_fitting_order"));
                    report.size_of_the_cabinet_is_more = c.getString(c.getColumnIndex("size_of_the_cabinet_is_more"));
                    report.door_dimensions = c.getString(c.getColumnIndex("door_dimensions"));
                    report.installation_template = c.getString(c.getColumnIndex("installation_template"));
                    report.plinth_legs = c.getString(c.getColumnIndex("plinth_legs"));
                    report.check_hinges = c.getString(c.getColumnIndex("check_hinges"));

                    report.drawer_within_range = c.getString(c.getColumnIndex("drawer_within_range"));
                    report.unit_if_fixed_to_wall = c.getString(c.getColumnIndex("unit_if_fixed_to_wall"));
                    report.enough_legs_for_support = c.getString(c.getColumnIndex("enough_legs_for_support"));
                    report.correct_alignment = c.getString(c.getColumnIndex("correct_alignment"));
                    report.servodrive_sufficient_to_handle_weight = c.getString(c.getColumnIndex("servodrive_sufficient_to_handle_weight"));

                    report.correct_product_order = c.getString(c.getColumnIndex("correct_product_order"));
                    report.type_of_runner = c.getString(c.getColumnIndex("type_of_runner"));
                    report.product_abuse = c.getString(c.getColumnIndex("product_abuse"));

                    report.Wooden_Panel_dimensions = c.getString(c.getColumnIndex("Wooden_Panel_dimensions"));

                    report.power_supply_proper = c.getString(c.getColumnIndex("power_supply_proper"));
                    report.driver_working = c.getString(c.getColumnIndex("driver_working"));
                    report.led_is_working = c.getString(c.getColumnIndex("led_is_working"));

                    report.fitting_templete = c.getString(c.getColumnIndex("fitting_templete"));
                    report.front_adjustments = c.getString(c.getColumnIndex("front_adjustments"));
                    report.correct_no_of_screws = c.getString(c.getColumnIndex("correct_no_of_screws"));
                    report.correct_spacing = c.getString(c.getColumnIndex("correct_spacing"));
                    report.Empty_the_drawer = c.getString(c.getColumnIndex("Empty_the_drawer"));
                    report.dust_gathers = c.getString(c.getColumnIndex("dust_gathers"));
                    report.third_hinge_is_used = c.getString(c.getColumnIndex("third_hinge_is_used"));
                    report.synchro_motion_working_properly = c.getString(c.getColumnIndex("synchro_motion_working_properly"));


                    //	report.correct_product = c.getString(c.getColumnIndex("correct_product"));
                    report.height_more_than_260mm = c.getString(c.getColumnIndex("height_more_than_260mm"));
                    report.length_slatted_or_wooden_50mm = c.getString(c.getColumnIndex("length_slatted_or_wooden_50mm"));
                    report.widht_slatted_or_wooden_30mm = c.getString(c.getColumnIndex("widht_slatted_or_wooden_30mm"));
                    report.slatted_dimen_1400mm_2000mm = c.getString(c.getColumnIndex("slatted_dimen_1400mm_2000mm"));
                    report.slatted_dimen_1800mm_2000mm = c.getString(c.getColumnIndex("slatted_dimen_1800mm_2000mm"));
                    report.bedding_box_fixing = c.getString(c.getColumnIndex("bedding_box_fixing"));
                    report.weight_between_23_25kgs = c.getString(c.getColumnIndex("weight_between_23_25kgs"));
                    report.weight_between_50_60kgs = c.getString(c.getColumnIndex("weight_between_50_60kgs"));
                    report.check_Stabilizing_rod = c.getString(c.getColumnIndex("check_Stabilizing_rod"));

                    report.prod_type = c.getString(c.getColumnIndex("prod_type"));


                } while (c.moveToNext());
                c.close();
            }
        }
        return report;

    }

    //...................get all sanitary details
    public Sanitary_Details getSanitaryReportDetails(String userName,
                                             String complaint_number) {
        Sanitary_Details report1 = new Sanitary_Details();
        String sql = "SELECT a.*,b.technician,b.date FROM sanitary_details a , complaint_service_details b where b.complaint_number = a.Complant_No and b.technician = '"
                + userName + "' and a.Complant_No = '" + complaint_number + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst() && c.getCount() > 0) {
                do {

                    Log.e("Fault", DatabaseUtils.dumpCursorToString(c));
                    report1.radio_sanitary = c.getString(c.getColumnIndex("radio_sanitary"));
                    report1.type_of_sanitary = c.getString(c.getColumnIndex("type_of_sanitary"));
                    report1.sanitary_product = c.getString(c.getColumnIndex("sanitary_product"));
                    report1.sanitary_leakage = c.getString(c.getColumnIndex("sanitary_leakage"));
                    report1.sanitary_type_of_leakage = c.getString(c.getColumnIndex("sanitary_type_of_leakage"));
                    report1.does_not_operate = c.getString(c.getColumnIndex("does_not_operate"));
                    report1.type_does_not_operate = c.getString(c.getColumnIndex("type_does_not_operate"));
                    report1.weak_flow = c.getString(c.getColumnIndex("weak_flow"));
                    report1.type_of_weak_flow = c.getString(c.getColumnIndex("type_of_weak_flow"));
                    report1.asthetics = c.getString(c.getColumnIndex("asthetics"));
                    report1.type_of_asthetics = c.getString(c.getColumnIndex("type_of_asthetics"));
                    report1.warranty = c.getString(c.getColumnIndex("warranty"));
                    report1.noise = c.getString(c.getColumnIndex("noise"));
                    report1.flush_not_working = c.getString(c.getColumnIndex("flush_not_working"));
                    report1.type_of_flush_not_working = c.getString(c.getColumnIndex("type_of_flush_not_working"));
                    report1.drainage = c.getString(c.getColumnIndex("drainage"));
                    report1.type_of_drainage = c.getString(c.getColumnIndex("type_of_drainage"));
                    report1.LMD = c.getString(c.getColumnIndex("LMD"));
                    report1.Complant_No = c.getString(c.getColumnIndex("Complant_No"));
                    report1.Product_Category = c.getString(c.getColumnIndex("Product_Category"));
                    report1.product_Sub_Category = c.getString(c.getColumnIndex("product_Sub_Category"));
                    report1.article_no = c.getString(c.getColumnIndex("article_no"));
                    report1.Comment = c.getString(c.getColumnIndex("Comment"));
                    report1.sync_status = c.getString(c.getColumnIndex("sync_status"));
                    report1.Insert_Date = c.getString(c.getColumnIndex("Insert_Date"));
                    report1.Result = c.getString(c.getColumnIndex("Result"));
                    report1.sparce_defect = c.getString(c.getColumnIndex("sparce_defect"));
                    report1.complete_set = c.getString(c.getColumnIndex("complete_set"));
                    report1.site_Issue_Reason = c.getString(c.getColumnIndex("site_Issue_Reason"));
                    report1.Action = c.getString(c.getColumnIndex("Action"));
                    report1.wrong_product_reason = c.getString(c.getColumnIndex("wrong_product_reason"));
                    report1.Reason_For_Unresolved = c.getString(c.getColumnIndex("Reason_For_Unresolved"));
                    report1.Closure_Status = c.getString(c.getColumnIndex("Closure_Status"));
                    report1.Closed_Date = c.getString(c.getColumnIndex("Closed_Date"));
                    report1.Updated_Date = c.getString(c.getColumnIndex("Updated_Date"));
                    report1.technician = c.getString(c.getColumnIndex("technician"));
                    report1.date = c.getString(c.getColumnIndex("date"));
                    report1.date = c.getString(c.getColumnIndex("date"));


                } while (c.moveToNext());
                c.close();
            }
        }
        return report1;

    }
    //................................

    public boolean delete(String DATABASE_TABLE, String KEY_NAME, String value) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        return mDb.delete(DATABASE_TABLE, KEY_NAME + "=" + value, null) > 0;
    }

    public boolean deleteString(String DATABASE_TABLE, String KEY_NAME,
                                String value) {
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        return mDb.delete(DATABASE_TABLE, KEY_NAME + "='" + value + "'", null) > 0;
    }

    public String getClosureStatus(String Complaint_No) {
        String result = "";
        String sql = "select Closure_Status from Fault_Finding_Details where Complant_No = '"
                + Complaint_No + "'";

        Cursor c = mDb.rawQuery(sql, null);
		/*
		 * if (c != null && c.getCount() > 0) { c.moveToFirst(); result =
		 * c.getString(c.getColumnIndex("Closure_Status")); } else { result =
		 * ""; }
		 */

        // /////////////////////
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    result = c.getString(c.getColumnIndex("Closure_Status"));
                } while (c.moveToNext());
            }
        }
        try {
            if (result.equals(null)) {
                result = "";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        c.close();
        // /////////////////////
        return result;

    }

    public String getUnresolvedReason(String Complaint_No) {
        String result = "";

        String sql = "select Reason_For_Unresolved from Fault_Finding_Details where Complant_No = '" + Complaint_No + "'";

        try {
            Cursor c = mDb.rawQuery(sql, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                result = c.getString(c.getColumnIndex("Reason_For_Unresolved"));
            }

            c.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public boolean getAcceptedStatus(String ID, String table, String colName) {
        Boolean result;


        String sql = "select * from " + table + " where " + colName + " = '"
                + ID + "'";
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean getAcceptedStatusbyCompaintNumber(String Complaint_No) {
        Boolean result;


        String sql = "select * from complaint_service_details where is_accepted='y' and complaint_number = '" + Complaint_No + "'";
		
		
		/*String sql = "select * from " + table + " where " + colName + " = '"
				+ ID + "' and complaint_number";*/
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    public boolean getCalleddate(String Complaint_No) {
        Boolean result;


        String sql = "select * from Fault_Finding_Details where Called_Date IS NULL and Complant_No = '" + Complaint_No + "'";
		
		
		/*String sql = "select * from " + table + " where " + colName + " = '"
				+ ID + "' and complaint_number";*/
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    public Cursor fetchallOrder(String tbl, String names[], String order) {
        return mDb.query(tbl, names, null, null, null, null, order);
    }


    public Cursor name() {

        Boolean result;

//Complant_No,status,sync_status,Accepted_Date,Called_Date,Updated_Date,Closed_Date
        String sql = "select  * from Fault_Finding_Details";


		/*String sql = "select * from " + table + " where " + colName + " = '"
				+ ID + "' and complaint_number";*/
        if (!mDb.isOpen()) {

            try {
                mDbHelper.openDataBase();
                mDb = mDbHelper.getReadableDatabase();

            } catch (SQLException mSQLException) {
                Log.e(TAG, "open >>" + mSQLException.toString());
                throw mSQLException;
            }
        }
        Cursor c = mDb.rawQuery(sql, null);

        return c;
    }


}
