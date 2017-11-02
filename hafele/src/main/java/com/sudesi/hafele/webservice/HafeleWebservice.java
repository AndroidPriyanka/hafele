package com.sudesi.hafele.webservice;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.sudesi.hafele.classes.FaultReport;
import com.sudesi.hafele.classes.Feedback;
import com.sudesi.hafele.classes.Sanitary_Details;

public class HafeleWebservice {
    Context con;

   //  String url = "http://tab.hinccms.in/Service1.svc"; // Final Production Link India
  //  String url = "http://tabsrilanka.hinccms.in/Service1.svc"; //Shrilanka production apk link

  String url ="http://192.168.0.130/ccms/Service1.svc";  // ...._local uat
    //151
    //121


    // String url = "http://hafelereportws.smartforcecrm.com/Service1.svc";
    // String url = "http://192.168.0.129/hafele_ccms/service1.svc";
    // String url="http://tabuat.hinccms.in/Service1.svc"; //UAT india testing apk link

    String compressionUrl = "http://licads.in/activation/service.svc"; // new PRo
    //String compressionUrl = "http://licads.in/adsws/Service1.svc"; // new UAT


    // String compressionUrl = "http://23.229.229.20/adsws/service1.svc";//UAT

//	String compressionUrl = "http://23.229.229.20/activation/service.svc";//old Pro

    String smsUrl = "http://180.179.146.71/api/v3/sendsms/plain?user=Hafele1&password=HaFeLe12&sender=Hafele";

  // "http://203.153.222.25:5000/sms/send_sms.php?username=hafele&password=Test123&src=Hafele&dst=" + mobile + "&msg=" + message + "&dr=1";

    //	string url = "http://180.179.146.71/api/v3/sendsms/plain?user=Hafele1&password=HaFeLe12&sender=Hafele&SMSText=" + message + "&GSM=" + mobile;


    static final String KEY_RESULT = "result"; // parent node
    static final String KEY_STATUS = "status";
    static final String KEY_MESSAGEID = "messageid";
    static final String KEY_DESTINATION = "destination";

    public HafeleWebservice(Context context) {
        this.con = context;
    }

    public SoapObject Get_login_service(String Username, String Password,
                                        String deviceid) {
        SoapObject result = null;

        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "Get_login");
            request.addProperty("Username", Username);
            request.addProperty("Password", Password);
            request.addProperty("deviceid", deviceid);

            Log.e("request", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call("http://tempuri.org/IService1/Get_login",
                    envelope);
            // response soap object
            result = (SoapObject) envelope.getResponse();
            Log.e("Get_login", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public SoapPrimitive RegisterJarUser(String Username, String IMEINo) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "RegisterUser");
            request.addProperty("Username", Username);
            request.addProperty("UDID", IMEINo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    compressionUrl);// http transport call
            androidHttpTransport.call(
                    "http://tempuri.org/IService/RegisterUser", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("RegisterUser", result.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive SaveImageCount(String Username, String IMEINo,
                                        int Count, String type) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "UpdateCount");
            request.addProperty("Username", Username);
            request.addProperty("UDID", IMEINo);
            request.addProperty("Count", Count);
            request.addProperty("Type", type);
            Log.e("Request", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    compressionUrl);// http transport call
            androidHttpTransport.call(
                    "http://tempuri.org/IService/UpdateCount", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("UpdateCount", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapObject getComplainOrServiceRequests(String status, int user_id) {
        SoapObject result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "getComplainOrServiceRequests");
            request.addProperty("Status", status);
            request.addProperty("FkTechId", user_id);

            Log.e("REQUEST", request.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport
                    .call("http://tempuri.org/IService1/getComplainOrServiceRequests",
                            envelope);
            // response soap object
            result = (SoapObject) envelope.getResponse();
            Log.e("getComplainOrServiceRequests", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive saveFaultReport(FaultReport faultReport) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "Insert_Complaint_Service_Details");
            // request.addProperty("Status", status);
            request.addProperty("Complaint_No", faultReport.Complant_No);
            request.addProperty("Product_Category",
                    faultReport.Product_Category);
            request.addProperty("Product_Sub_Category",
                    faultReport.product_Sub_Category);
            request.addProperty("Width", faultReport.Width);
            request.addProperty("Height", faultReport.Height);
            request.addProperty("Thickness", faultReport.Thickness);
            request.addProperty("Correct_Installation",
                    faultReport.Correct_Installation);
            request.addProperty("Pivot_Alignment", faultReport.Pivot_Alignment);
            request.addProperty("Door_Noise", faultReport.Door_Noise);
            request.addProperty("Closing_Speed", faultReport.Closing_Speed);
            request.addProperty("Screw_Fix", faultReport.Screw_Fix);
            request.addProperty("Edge_Distance", faultReport.Edge_Distance);
            request.addProperty("Door_Depth", faultReport.Door_Depth);
            request.addProperty("Frame_Depth", faultReport.Frame_Depth);
            request.addProperty("Centre_Cut_Out", faultReport.Centre_Cut_Out);
            request.addProperty("Latchin_Speed", faultReport.Latching_Speed);
            request.addProperty("Lift_Mechanism_Installed",
                    faultReport.Lift_Mechanism_Installed);
            request.addProperty("Arms_Installed", faultReport.Arms_Installed);
            request.addProperty("Arms_Size", faultReport.Arms_Size);
            request.addProperty("Power_Adjustment",
                    faultReport.Power_Adjustment);
            request.addProperty("Product_Power_Factor",
                    faultReport.Product_Power_Factor);
            request.addProperty("Door_Weight_Within_Range",
                    faultReport.Door_Weight_Within_Range);
            request.addProperty("Installation", faultReport.Installation);
            request.addProperty("Track_Roller_Clean",
                    faultReport.Track_Roller_Clean);
            request.addProperty("Door_Wrapped", faultReport.Door_Wrapped);
            request.addProperty("Plan_Used", faultReport.Plan_Used);
            request.addProperty("Carcase_Requirement",
                    faultReport.Carcase_Requirement);
            request.addProperty("Softclose_Broken",
                    faultReport.Softclose_Broken);
            request.addProperty("Product_Front_Wt_Available_Cabinet_Ht",
                    faultReport.Product_Front_Wt_Available_Cabinet_Ht);
            request.addProperty("Weight_Length_Correct",
                    faultReport.Weight_Length_Correct);
            request.addProperty("Content_weight_Within_Range",
                    faultReport.Content_weight_Within_Range);
            request.addProperty("Drawer_Construction_Alignment",
                    faultReport.Drawer_Construction_Alignment);
            request.addProperty("Product_System_Installation_32",
                    faultReport.Product_System_Installation_32);
            request.addProperty("Drawer_Noise_Check",
                    faultReport.Drawer_Noise_Check);
            request.addProperty("Drawer_Runner_Clean",
                    faultReport.Drawer_Runner_Clean);
            request.addProperty("Blum_Product_Servodrive",
                    faultReport.Blum_Product_Servodrive);
            request.addProperty("Lock_Working", faultReport.Lock_Working);
            request.addProperty("CutOut_Dimension",
                    faultReport.CutOut_Dimension);
            request.addProperty("Mortise_Lock_Template",
                    faultReport.Mortise_Lock_Template);
            request.addProperty("Lock_Protruding", faultReport.Lock_Protruding);
            request.addProperty("Plastic_Fillers", faultReport.Plastic_Fillers);
            request.addProperty("No_Mortise_Lock", faultReport.No_Mortise_Lock);
            request.addProperty("Door_Range_More", faultReport.Door_Range_More);
            request.addProperty("Door_Range_Less", faultReport.Door_Range_Less);
            request.addProperty("Rose_Touching_Door",
                    faultReport.Rose_Touching_Door);
            request.addProperty("Spindle_Size_Correct",
                    faultReport.Spindle_Size_Correct);
            request.addProperty("Spoiled_Spindle", faultReport.Spoiled_Spindle);
            request.addProperty("Proper_CutOut", faultReport.Proper_CutOut);
            request.addProperty("Strike_Plate_CutOut",
                    faultReport.Strike_Plate_CutOut);
            request.addProperty("Screw_Built_Check",
                    faultReport.Screw_Built_Check);
            request.addProperty("Thikness_Range", faultReport.Thikness_Range);
            request.addProperty("Right_Product_Door",
                    faultReport.Right_Product_Door);
            request.addProperty("Hinges_Number_Sufficeint",
                    faultReport.Hinges_Number_Sufficeint);
            request.addProperty("CutOut_Details", faultReport.CutOut_Details);
            request.addProperty("Depth_Thickness_Equal",
                    faultReport.Depth_Thickness_Equal);
            request.addProperty("Cabinet_Hgt_Door_Wt",
                    faultReport.Cabinet_Hgt_Door_Wt);
            request.addProperty("Sliding_Sys_Door_Wt",
                    faultReport.Sliding_Sys_Door_Wt);
            request.addProperty("G_Door_Wt", faultReport.G_Door_Wt);
            request.addProperty("W_Door_Wt", faultReport.W_Door_Wt);
            request.addProperty("Insert_Date", faultReport.Insert_Date);
            request.addProperty("Sides_Volume", faultReport.side_volume);
            request.addProperty("Back_Volume", faultReport.back_volume);
            request.addProperty("Base_Volume", faultReport.base_volume);
            request.addProperty("Facial_Volume", faultReport.facia_volume);
            request.addProperty("Drawer_Wt", faultReport.Drawer_Wt);
            request.addProperty("Closure_Status", faultReport.Closure_Status);
            request.addProperty("Reason_For_Unresolved",
                    faultReport.Reason_For_Unresolved);
            request.addProperty("Wrong_Product", faultReport.wrong_product);
            request.addProperty("Corrcet_Pro_One", faultReport.correct_pro_one);
            request.addProperty("Correct_Pro_Two", faultReport.correct_pro_two);
            request.addProperty("Correct_Pro_Three",
                    faultReport.correct_pro_three);
            request.addProperty("Comment", faultReport.Comment);
            request.addProperty("Appointment_Date",
                    faultReport.Appointment_Date);
            request.addProperty("Handle_Wt", faultReport.Handle_Weight);
            request.addProperty("Article_No", faultReport.article_no);
            request.addProperty("Power_Factor", faultReport.power_factor);

            Log.e("REQUEST", request.toString());
            // ArrayList<HeaderProperty> headerPropertyArrayList = new
            // ArrayList<HeaderProperty>();
            // headerPropertyArrayList.add(new HeaderProperty("Connection",
            // "close"));
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport
                    .call("http://tempuri.org/IService1/Insert_Complaint_Service_Details",
                            envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Insert_Complaint_Service_Details", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive Insert_Complaint_Service_Details1(FaultReport faultReport, String responceId) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "Insert_Complaint_Service_Details1");
            // request.addProperty("Status", status);

            request.addProperty("fault_ref_id", responceId);
            request.addProperty("wrong_product_reason", faultReport.wrong_product_reason);
            request.addProperty("Result", faultReport.Result);
            request.addProperty("Action", faultReport.Action);
            request.addProperty("sparce_defect", faultReport.sparce_defect);
            request.addProperty("complete_set", faultReport.complete_set);
            request.addProperty("site_Issue_Reason", faultReport.site_Issue_Reason);
            request.addProperty("part_list_is_complete", faultReport.part_list_is_complete);
            request.addProperty("correct_fitting_order", faultReport.correct_fitting_order);
            request.addProperty("size_of_the_cabinet_is_more", faultReport.size_of_the_cabinet_is_more);
            request.addProperty("door_dimensions", faultReport.door_dimensions);
            request.addProperty("installation_template", faultReport.installation_template);
            request.addProperty("plinth_legs", faultReport.plinth_legs);
            request.addProperty("check_hinges", faultReport.check_hinges);
            request.addProperty("drawer_within_range", faultReport.drawer_within_range);
            request.addProperty("unit_if_fixed_to_wall", faultReport.unit_if_fixed_to_wall);
            request.addProperty("enough_legs_for_support", faultReport.enough_legs_for_support);
            request.addProperty("correct_alignment", faultReport.correct_alignment);
            request.addProperty("servodrive_sufficient_to_handle_weight", faultReport.servodrive_sufficient_to_handle_weight);
            request.addProperty("Wooden_Panel_dimensions", faultReport.Wooden_Panel_dimensions);
            request.addProperty("power_supply_proper", faultReport.power_supply_proper);
            request.addProperty("driver_working", faultReport.driver_working);
            request.addProperty("led_is_working", faultReport.led_is_working);
            request.addProperty("fitting_templete", faultReport.fitting_templete);
            request.addProperty("front_adjustments", faultReport.front_adjustments);
            request.addProperty("correct_no_of_screws", faultReport.correct_no_of_screws);
            request.addProperty("correct_spacing", faultReport.correct_spacing);
            request.addProperty("Empty_the_drawer", faultReport.Empty_the_drawer);
            request.addProperty("dust_gathers", faultReport.dust_gathers);
            request.addProperty("third_hinge_is_used", faultReport.third_hinge_is_used);
            request.addProperty("synchro_motion_working_properly", faultReport.synchro_motion_working_properly);
            request.addProperty("length_slatted_or_wooden_50mm", faultReport.length_slatted_or_wooden_50mm);
            request.addProperty("widht_slatted_or_wooden_30mm", faultReport.widht_slatted_or_wooden_30mm);
            request.addProperty("slatted_dimen_1400mm_2000mm", faultReport.slatted_dimen_1400mm_2000mm);
            request.addProperty("slatted_dimen_1800mm_2000mm", faultReport.slatted_dimen_1800mm_2000mm);
            request.addProperty("bedding_box_fixing", faultReport.bedding_box_fixing);
            request.addProperty("weight_between_23_25kgs", faultReport.weight_between_23_25kgs);
            request.addProperty("weight_between_50_60kgs", faultReport.weight_between_50_60kgs);
            request.addProperty("check_Stabilizing_rod", faultReport.check_Stabilizing_rod);
            request.addProperty("height_more_than_260mm", faultReport.height_more_than_260mm);
            request.addProperty("correct_product_order", faultReport.correct_product_order);
            request.addProperty("type_of_runner", faultReport.type_of_runner);
            request.addProperty("product_abuse", faultReport.product_abuse);
            request.addProperty("complaintno", faultReport.Complant_No);

            Log.e("REQUEST", request.toString());
            // ArrayList<HeaderProperty> headerPropertyArrayList = new
            // ArrayList<HeaderProperty>();
            // headerPropertyArrayList.add(new HeaderProperty("Connection",
            // "close"));
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call("http://tempuri.org/IService1/Insert_Complaint_Service_Details1", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Insert_Complaint_Service_Details1", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //.........................Sanitary Details page .........................

    public SoapPrimitive insert_Sanitary_Details(Sanitary_Details sanitary_details) {
        SoapPrimitive result = null;
        try {
            int delayed_days = 0;


            SoapObject request = new SoapObject("http://tempuri.org/", "Insert_Sanitary_Details");
            // request.addProperty("Status", status);
          //  request.addProperty("fault_ref_id", responceId);
            request.addProperty("Complaint_No", sanitary_details.Complant_No);
            request.addProperty("radio_sanitary", sanitary_details.radio_sanitary);
            request.addProperty("type_of_sanitary", sanitary_details.type_of_sanitary);
            request.addProperty("sanitary_product", sanitary_details.sanitary_product);
            request.addProperty("sanitary_leakage", sanitary_details.sanitary_leakage);
            request.addProperty("sanitary_type_of_leakage", sanitary_details.sanitary_type_of_leakage);
            request.addProperty("does_not_operate", sanitary_details.does_not_operate);
            request.addProperty("type_does_not_operate", sanitary_details.type_does_not_operate);
            request.addProperty("weak_flow", sanitary_details.weak_flow);
            request.addProperty("type_of_weak_flow", sanitary_details.type_of_weak_flow);
            request.addProperty("asthetics", sanitary_details.asthetics);
            request.addProperty("type_of_asthetics", sanitary_details.type_of_asthetics);
            request.addProperty("warranty", sanitary_details.warranty);
            request.addProperty("noise", sanitary_details.noise);
            request.addProperty("flush_not_working", sanitary_details.flush_not_working);
            request.addProperty("type_of_flush_not_working", sanitary_details.type_of_flush_not_working);
            request.addProperty("drainage", sanitary_details.drainage);
            request.addProperty("type_of_drainage", sanitary_details.type_of_drainage);
            request.addProperty("Closure_Status", sanitary_details.Closure_Status);
            request.addProperty("LMD", sanitary_details.LMD);
            request.addProperty("Accepted_Date",sanitary_details.date);
            request.addProperty("Called_Date",sanitary_details.date);
            request.addProperty("Updated_Date",sanitary_details.Updated_Date);
            request.addProperty("Closed_Date",sanitary_details.Closed_Date);
            request.addProperty("delayed_days",delayed_days);


            Log.e("REQUEST", request.toString());
            // ArrayList<HeaderProperty> headerPropertyArrayList = new
            // ArrayList<HeaderProperty>();
            // headerPropertyArrayList.add(new HeaderProperty("Connection",
            // "close"));
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call("http://tempuri.org/IService1/Insert_Sanitary_Details", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Insert_Sanitary_Details", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }













    public SoapPrimitive SaveFeedback(Feedback feedback) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "Insert_Feedback");
            request.addProperty("Technician_Name", feedback.Technician_Name);
            request.addProperty("Technician_Rate", feedback.Technician_Rate);
            request.addProperty("Executive_Name", feedback.Executive_Name);
            request.addProperty("Executive_Rate", feedback.Executive_Rate);
            request.addProperty("Rating", feedback.Rating);
            request.addProperty("Signature", feedback.Signature);
            request.addProperty("Complaint_No", feedback.Complaint_No);
            request.addProperty("Insert_Date", feedback.Insert_Date);
            request.addProperty("FkTechId", feedback.User_Ref_Id);
            Log.e("Request", request.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/Insert_Feedback", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Insert_Feedback", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive uploadFile(String base64, String fileName) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "Base64ToImage");
            request.addProperty("base64String", base64);
            request.addProperty("Image_Name", fileName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/Base64ToImage", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Base64ToImage", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // string Complaint_No, string Image_Name, string Original_Size, string
    // Compress_Size
    public SoapPrimitive uploadImageData(String ComplaintID, String imageName,
                                         String OrgSize, String CompSize) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "UploadImageData");
            request.addProperty("Complaint_No", ComplaintID);
            request.addProperty("Image_Name", imageName);
            request.addProperty("Original_Size", OrgSize);
            request.addProperty("Compress_Size", CompSize);
            Log.e("Request", request.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/UploadImageData", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("UploadImageData", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapObject getLoginData() {
        SoapObject result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "getComplainOrServiceRequests");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport
                    .call("http://tempuri.org/IService1/getComplainOrServiceRequests",
                            envelope);
            // response soap object
            result = (SoapObject) envelope.getResponse();
            Log.e("getComplainOrServiceRequests", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive UploadVideoData(String complant_No, String username,
                                         String videoName, String filePath, String originalSize,
                                         String compressedSize, String storageSaved) {
        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "UploadVideoData");
            request.addProperty("Complaint_No", complant_No);
            request.addProperty("User_Name", username);
            request.addProperty("File_Name", videoName);
            request.addProperty("File_Path", "");
            request.addProperty("Original_Size", originalSize);
            request.addProperty("Compress_Size", compressedSize);
            request.addProperty("Spaced_Saved", storageSaved);

            Log.e("Request", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/UploadVideoData", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("UploadVideoData", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive VideoUpload(String FileName, String base64, int Offset) {

        SoapPrimitive result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "VideoUpload");
            request.addProperty("FileName", FileName);
            request.addProperty("base64", base64);
            request.addProperty("Offset", Offset);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/VideoUpload", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("VideoUpload", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public SoapObject get_oldtech(String tkid) {
        SoapObject result = null;
        try {
            SoapObject request = new SoapObject("http://tempuri.org/",
                    "get_oldtech");
            request.addProperty("tkid", tkid);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/get_oldtech", envelope);
            // response soap object
            result = (SoapObject) envelope.getResponse();
            Log.e("get_oldtech", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public SoapPrimitive Update_FaulFinding(String id, String complaintno,
                                            String Accepted_Date, String Called_Date, String Updated_Date,
                                            String Closed_Date, int delayed_days) {
        SoapPrimitive result = null;
        try {

            SoapObject request = new SoapObject("http://tempuri.org/",
                    "Update_FaulFinding");
            request.addProperty("id", id);
            request.addProperty("complaintno", complaintno);
            request.addProperty("Accepted_Date", Accepted_Date);
            request.addProperty("Called_Date", Called_Date);
            request.addProperty("Updated_Date", Updated_Date);
            request.addProperty("Closed_Date", Closed_Date);
            request.addProperty("delayed_days", delayed_days);

            Log.e("Request", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call(
                    "http://tempuri.org/IService1/Update_FaulFinding", envelope);
            // response soap object
            result = (SoapPrimitive) envelope.getResponse();
            Log.e("Update_FaulFinding", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean sendSMS(String number, String msg) {

        boolean res = false;

        try {
//			 String responseString = null;
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 25000);

            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpClient sslhttpclient = ExSSLSocketFactory.getHttpsClient(httpclient);
            HttpResponse response;

            StringBuilder messageRequestStringBuffer = new StringBuilder();

            messageRequestStringBuffer.append(smsUrl);
            messageRequestStringBuffer.append("&SMSText=" + msg);
            messageRequestStringBuffer.append("&GSM=91" + number);

            Log.e("messageRequest", messageRequestStringBuffer.toString());

            HttpGet httpGet = new HttpGet(messageRequestStringBuffer.toString());
            response = sslhttpclient.execute(httpGet);

            HttpEntity hhEntity = response.getEntity();
            String xml = EntityUtils.toString(hhEntity);

            if (parseXML(xml)) {
                res = true;
            } else {
                res = false;
            }




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;


    }

    public boolean parseXML(String xml) {


        boolean result = false;
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

            NodeList nl = doc.getElementsByTagName("result");

//                for (int i = 0; i < nl.getLength(); i++) {

//                	HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(0);

            String status = getValue(e, "status"); // name child value
            String messageid = getValue(e, "messageid"); // cost child value
            String destination = getValue(e, "destination"); // description child value

            Log.e("status", status);
            Log.e("messageid", messageid);
            Log.e("destination", destination);


            if (status.equals("0")) {
                result = true;
            } else {
                result = false;
            }

//                }

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());

        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());

        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());

        }
        return result;


    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public SoapObject get_stgComplainData(String complaint_no) {
        SoapObject result = null;

        try {

            SoapObject request = new SoapObject("http://tempuri.org/",
                    "get_stgComplainData");

            request.addProperty("tkid", complaint_no);
            Log.e("request", request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);// soap envelop with version
            envelope.setOutputSoapObject(request); // set request object
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
            // transport
            // call
            androidHttpTransport.call("http://tempuri.org/IService1/get_stgComplainData",
                    envelope);
            // response soap object
            result = (SoapObject) envelope.getResponse();
            Log.e("Get_status", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

	



}
