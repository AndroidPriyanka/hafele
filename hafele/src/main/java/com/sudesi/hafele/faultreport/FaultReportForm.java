package com.sudesi.hafele.faultreport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.netcompss.ffmpeg4android_client.BaseWizard;
import com.sudesi.hafele.adapter.ImageGridAdapter;
import com.sudesi.hafele.classes.FaultReport;
import com.sudesi.hafele.classes.ImageData;
import com.sudesi.hafele.classes.SignatureView;
import com.sudesi.hafele.classes.VideoData;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.faultreport.SiteVisitForm.SendSMSAsync;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.utils.UtilityClass;
import com.sudesi.hafele.utils.Validation;
import com.sudesi.hafele.webservice.HafeleWebservice;

public class FaultReportForm extends BaseWizard implements OnClickListener,
        OnCheckedChangeListener, OnFocusChangeListener, OnItemSelectedListener {
    FrameLayout frame;
    Context context;
    String product_sub_category;
    String complaint_number, complaint_date;
    String c_name;
    String purchase_from;
    String product_group;
    String product_category;
    LinearLayout form;
    EditText door_width;
    EditText door_height;
    EditText door_thickness;
    EditText handle_weight;
    static Uri mCapturedImageURI;
    // ArrayList<String> stringList = new ArrayList<String>();
    ArrayList<Bitmap> bmpArray;
    ArrayList<Bitmap> capturedBM;
    ArrayList<File> fileNameArray;
    ArrayList<File> orgFileArray;
    ArrayList<String> imgOriginalSize;
    ArrayList<String> imgCompressedSize;
    ImageGridAdapter imgAdapter;
    // VideoGridAdapter vidAdapter;
    Bitmap compressedBitmap;
    // String base64;
    String c_address;
    ImageGridAdapter feedbackImgGridAdapter;
    HafeleFaultReportDBAdapter dbAdapter;
    CheckBox vc_chk_1, vc_chk_2;
    String signatureFile, signaturePath;
    HafelePreference pref;
    EditText other_reason;
    // FaultReport faultReport;
    ContentValues contentValues = new ContentValues();
    EditText power_factor;
    String dateTime;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_VIDEO_FROM_GALLARY_ACTIVITY_REQUEST_CODE = 300;
    String outvidpath;
    String inputpath;
    String OriginalSize, CompressedSize, original_str_resolution;
    private Uri fileUri;
    int secs;
    boolean show_feedback = false;
    ImageView video_grid;
    private static String Video_Status = "";

    GridView img_grid;
    boolean is_video_attached = false;
    File videoFile;
    // UploadVideo upload;
    int stars = 0;
    HafeleWebservice ws;
    Spinner unresolve_reason, status, status1, unresolve_reason1;
    String CCExecutive;
    String end_user_mobile, service_franchise, article, service_details;
    TextView door_weight, comments1;
    TextView hafele_rate;
    int is_visited;
    ArrayList<String> reasonList, reasonList1, resultList, actionList, wrongProdList;
    boolean is_checked = false;
    CheckBox floor_chk_1, floor_chk_2, floor_chk_3, floor_chk_4, floor_chk_5,
            floor_chk_6, floor_chk_7, floor_chk_8, val_one, val_two, val_three,
            wrong_product;
    CheckBox radio_1, radio_2, radio_3, radio_4, radio_5, radio_6, radio_7,
            radio_8, radio_9, radio_10;
    TextView comp_number, customer_name, date, mobile_no, dealer, invoice_no,
            prod_cat, prod_sub_cat, complaint_deatils, comments, address,
            vol_one, vol_two, vol_three, vol_four;
    EditText article_no, v1_w1, v1_t1, v1_h1, v2_w2, v2_t2, v2_h2, v3_w3,
            v3_t3, v3_h3, v4_w4, v4_t4, v4_h4, drawer_wt, drawer_wt_tot,
            door_handle_thickness;

    CheckBox dc_chk_1, dc_chk_2, dc_chk_3, dc_chk_4, dc_chk_5, dc_chk_6,
            dc_chk_7, dc_chk_8, dc_chk_9, dc_chk_10, dc_chk_11, dc_chk_12,
            dc_chk_13, dc_chk_14, dc_chk_15, dc_chk_16;
    CheckBox ls_chk_1, ls_chk_2, ls_chk_3, ls_chk_4, ls_chk_5, ls_chk_6,
            ls_chk_7, ls_chk_8, ls_chk_9, ls_chk_10;
    CheckBox sf_chk_1, sf_chk_2, sf_chk_3, sf_chk_4, sf_chk_5, sf_chk_6,
            sf_chk_7, sf_chk_8, sf_chk_9, sf_chk_10, sf_chk_11, sf_chk_12, sf_chk_13, sf_chk_14,
            ml_chk_1, ml_chk_2, ml_chk_3, ml_chk_4, ml_chk_5, ml_chk_6;
    CheckBox rc_chk_1, rc_chk_2, rc_chk_3, rc_chk_4, rc_chk_5, rc_chk_6,
            rc_chk_7, rc_chk_8, rc_chk_9, rc_chk_10, rc_chk_11, rc_chk_12;
    CheckBox dh_chk_1, dh_chk_2, dh_chk_3, dh_chk_4, dh_chk_5, dh_chk_6,
            dh_chk_7, dh_chk_8, dh_chk_9, dh_chk_10, dh_chk_11, dh_chk_12,
            dh_chk_13, dh_chk_14, dh_chk_15, ph_chk_1, ph_chk_2, ph_chk_3,
            ph_chk_4;
    CheckBox hn_chk_1, hn_chk_2, hn_chk_3, hn_chk_4, hn_chk_5, hn_chk_6, hn_chk_7, hn_chk_8;


    //*********Komal

    CheckBox chk_wrongInstallation, chk_product_defect, chk_site_Issue;
    Spinner spin_siteIssueReason_reason, spin_action;
    EditText edt_spare_defect_articleNo, edt_complete_set_articleNo;
    LinearLayout ll_site_issue, ll_product_defect, ll_action;
    EditText cabinet_height, cabinet_width, cabinet_depth;


    Spinner spin_siteIssueReason_reason1, spin_action1;
    EditText edt_spare_defect_articleNo1, edt_complete_set_articleNo1;
    LinearLayout ll_site_issue1, ll_product_defect1, ll_action1;

    CheckBox blcr_chk_1, blcr_chk_2, blcr_chk_3, blcr_chk_4, blcr_chk_5, blcr_chk_6, blcr_chk_7, blcr_chk_8,
            blcr_chk_9, blcr_chk_10, blcr_chk_11, blcr_chk_12, blcr_chk_13, blcr_chk_14, blcr_chk_15, blcr_chk_16,
            blcr_chk_17, blcr_chk_18, blcr_chk_19, blcr_chk_20, blcr_chk_21, blcr_chk_22, vc_chk_11, vc_chk_22;
    Spinner spin_wrong_product, spin_wrong_product1;
    CheckBox cu_chk_1, cu_chk_2, cu_chk_3, cu_chk_4, cu_chk_5, cu_chk_6, cu_chk_7, cu_chk_8, cu_chk_9, cu_chk_10, cu_chk_11, cu_chk_12, cu_chk_13, cu_chk_14;
    EditText pratik_height, pratik_length_50mm, pratik_width_30mm, pratik_sfd_dimen, pratik_sfd_dimen1;
    CheckBox tv_chk_1, tv_chk_2, tv_chk_3, tv_chk_4, tv_chk_5, tv_chk_6, tv_chk_7, tv_chk_8;
    EditText tv_width, tv_depth;
    CheckBox tall_chk_1, tall_chk_2, tall_chk_3, tall_chk_4, tall_chk_5, tall_chk_6, tall_chk_7, tall_chk_8, tall_chk_9, tall_chk_10, tall_chk_11, tall_chk_12;
    CheckBox lc_chk_1, lc_chk_2, lc_chk_3, lc_chk_4, lc_chk_5, lc_chk_6, lc_chk_7, lc_chk_8;
    CheckBox ib_chk_1, ib_chk_2, ib_chk_3, ib_chk_4, ib_chk_5, ib_chk_6, ib_chk_7, ib_chk_8, ib_chk_9, ib_chk_10, ib_chk_11;
    CheckBox pr_chk_1, pr_chk_2, pr_chk_3, pr_chk_4, pr_chk_5, pr_chk_6, pr_chk_7, pr_chk_8, pr_chk_9, pr_chk_10, pr_chk_11, pr_chk_12, pr_chk_13, pr_chk_14, pr_chk_15, pr_chk_16, pr_chk_17, pr_chk_18, pr_chk_19, pr_chk_20, pr_chk_21, pr_chk_22;
    TextView stp_two;

    LinearLayout ll_pratik, ll_tavoletto, bottom_linearll, ll_other;
    RadioGroup radio_group;
    RadioButton radio_tavoletto, radio_pratik, radio_other;
    String prod_type;

    private DeleteFileBroadcastReceiver deleteFilesAlarm;

    Button btn_deleteVideo;

    String message;

    HafeleWebservice webservice;

    SendSMSAsync sendSMSAsync;

    boolean is_validation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        context = this;
        pref = new HafelePreference(context);
        dbAdapter = new HafeleFaultReportDBAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();

        webservice = new HafeleWebservice(FaultReportForm.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set status list
        String[] array = getResources().getStringArray(R.array.reason_array);
        reasonList = new ArrayList<String>();
        for (int i = 0; i < array.length; i++) {
            reasonList.add(array[i]);
        }


        String[] array1 = getResources().getStringArray(R.array.site_issue);
        reasonList1 = new ArrayList<String>();
        for (int i = 0; i < array1.length; i++) {
            reasonList1.add(array1[i]);
        }

        String[] array2 = getResources().getStringArray(R.array.result_array);
        resultList = new ArrayList<String>();
        for (int i = 0; i < array2.length; i++) {
            resultList.add(array2[i]);
        }

        String[] array3 = getResources().getStringArray(R.array.action_array);
        actionList = new ArrayList<String>();
        for (int i = 0; i < array3.length; i++) {
            actionList.add(array3[i]);
        }

        String[] array4 = getResources().getStringArray(R.array.spinner_wrong_product);
        wrongProdList = new ArrayList<String>();
        for (int i = 0; i < array4.length; i++) {
            wrongProdList.add(array4[i]);
        }

        // DecimalFormat formatter = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        dateTime = month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + +calendar.get(Calendar.YEAR) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);

        // ----- for video capture---

        deleteFilesAlarm = new DeleteFileBroadcastReceiver();

        deleteFilesAlarm.SetAlarm(FaultReportForm.this);

        setWorkingFolder("/sdcard/Android/data/qwerty/sd23/asdf/");

        copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();

        complaint_number = intent.getStringExtra("complaint_number");
        complaint_date = intent.getStringExtra("complaint_date");
        c_name = intent.getStringExtra("end_user_name");
        end_user_mobile = intent.getStringExtra("end_user_mobile");
        service_franchise = intent.getStringExtra("user_type");
        article = intent.getStringExtra("article_no");
        c_address = intent.getStringExtra("end_user_address");
        String end_user_region = intent.getStringExtra("end_user_region");
        // String sales_sub_region = intent.getStringExtra("sales_sub_region");
        product_group = intent.getStringExtra("product_group");
        product_category = intent.getStringExtra("product_category");

        message = "Dear+Customer+,+Your+case+" + complaint_number + "+is+closed.+If+the+problem+still+persist,+do+call+us+on+toll+free+no.+18002666667+within+2+working+days.+-+Customer+Care";

        //		message = "DearCustomer,Yourcase"+complaint_number+"isclosed.Iftheproblemstillpersist,docallusontollfreeno.18002666667within2workingdays-CustomerCare";


        if (product_category != null) {
            if (product_category.trim() != null) {
                product_category = product_category.trim();
            }
        }
        // product_category = "Aventos";
        product_sub_category = intent.getStringExtra("product_sub_category");
        if (product_sub_category != null) {
            if (product_sub_category.trim() != null) {
                product_sub_category = product_sub_category.trim();
            }
        }

        if (product_group != null) {
            if (product_group.trim() != null) {
                product_group = product_group.trim();
            }
        }
        // product_sub_category = "Aventos";
        service_details = intent.getStringExtra("service_details");
        is_visited = intent.getIntExtra("is_visited", -1);
        CCExecutive = intent.getStringExtra("Created_By");

        bmpArray = new ArrayList<Bitmap>();
        capturedBM = new ArrayList<Bitmap>();
        fileNameArray = new ArrayList<File>();
        orgFileArray = new ArrayList<File>();
        imgOriginalSize = new ArrayList<String>();
        imgCompressedSize = new ArrayList<String>();

        setContentView(R.layout.fault_report_form);
        FrameLayout baseFrame = (FrameLayout) findViewById(R.id.frame_layout);
        LayoutInflater inflater = LayoutInflater.from(context);

        ImageView home_button = (ImageView) findViewById(R.id.home);
        ImageView logout_button = (ImageView) findViewById(R.id.logout);

        home_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

        View view = null;
        Log.e("product_sub_category", product_sub_category);

        if (product_sub_category.contains("Floor Springs")) {
            //Floor Springs
            view = inflater.inflate(R.layout.door_control_devices, null);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            Log.e("is_visited", String.valueOf(is_visited));
            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);

            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            door_width = (EditText) view.findViewById(R.id.door_width);
            door_height = (EditText) view.findViewById(R.id.door_height);
            door_thickness = (EditText) view.findViewById(R.id.door_thickness);
            val_one = (CheckBox) view.findViewById(R.id.val_one);
            val_two = (CheckBox) view.findViewById(R.id.val_two);
            val_three = (CheckBox) view.findViewById(R.id.val_three);
            wrong_product = (CheckBox) view.findViewById(R.id.wrong_product);
            form = (LinearLayout) view.findViewById(R.id.form);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            btn_deleteVideo.setOnClickListener(this);

            comments = (TextView) view.findViewById(R.id.comments);
            address = (TextView) view.findViewById(R.id.address);
            TextView customer_info = (TextView) view.findViewById(R.id.customer_info);
            RadioButton glass = (RadioButton) view.findViewById(R.id.glass);
            RadioButton wood = (RadioButton) view.findViewById(R.id.wood);
            door_weight = (TextView) view.findViewById(R.id.door_weight);
            // LinearLayout closure_form = (LinearLayout)
            // view.findViewById(R.id.closure_form);
            // closure_form.setVisibility(View.GONE);
           /* final LinearLayout unresolved = (LinearLayout) view
                    .findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);

            final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

            status = (Spinner) view.findViewById(R.id.status);
            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);
         /*   unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);

                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());


                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
*/
            glass.setOnClickListener(this);
            wood.setOnClickListener(this);

            floor_chk_1 = (CheckBox) view.findViewById(R.id.floor_chk_1);
            floor_chk_2 = (CheckBox) view.findViewById(R.id.floor_chk_2);
            floor_chk_3 = (CheckBox) view.findViewById(R.id.floor_chk_3);
            floor_chk_4 = (CheckBox) view.findViewById(R.id.floor_chk_4);
            floor_chk_5 = (CheckBox) view.findViewById(R.id.floor_chk_5);
            floor_chk_6 = (CheckBox) view.findViewById(R.id.floor_chk_6);
            floor_chk_7 = (CheckBox) view.findViewById(R.id.floor_chk_7);
            floor_chk_8 = (CheckBox) view.findViewById(R.id.floor_chk_8);

            Button submit = (Button) view.findViewById(R.id.submit_button);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // boolean has_comment = Validation.hasText(comments);

                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //boolean otherReason = false;
                  /*  if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/
                    boolean door_wt = Validation.hasText(door_weight);
                    boolean has_width = Validation.hasText(door_width);
                    boolean has_height = Validation.hasText(door_height);
                    boolean has_thickness = Validation.hasText(door_thickness);

                 /*   boolean door_noise = false;
                    if (contentValues.get("Door_Noise") != null) {
                        if (contentValues.get("Door_Noise").equals("null")) {
                            door_noise = false;
                        } else {
                            door_noise = true;
                        }

                        if (contentValues.get("Door_Noise").equals("Y"))
                            door_noise = true;
                        if (contentValues.get("Door_Noise").equals("N"))
                            door_noise = true;
                    }

                    if (!floor_chk_5.isEnabled()) {
                        door_noise = true;
                    }

                    if (!floor_chk_6.isEnabled()) {
                        door_noise = true;
                    }

                    boolean closing_speed = false;
                    if (contentValues.get("Closing_Speed") != null) {
                        if (contentValues.get("Closing_Speed").equals("null")) {
                            closing_speed = false;
                        } else {
                            closing_speed = true;
                        }

                        if (contentValues.get("Closing_Speed").equals("Y"))
                            closing_speed = true;
                        if (contentValues.get("Closing_Speed").equals("N"))
                            closing_speed = true;
                    }
                    if (!floor_chk_7.isEnabled()) {
                        closing_speed = true;
                    }

                    if (!floor_chk_8.isEnabled()) {
                        closing_speed = true;
                    }
*/
                    if (is_article_no && has_width && has_height && has_thickness && door_wt /*&& door_noise && closing_speed*/) {
                        if (checkValidation(product_sub_category)) {
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }
                        }
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context, "Enter article number");
                        if (!has_width)
                            UtilityClass.showToast(context, "Enter Door Width");
                        if (!has_height)
                            UtilityClass.showToast(context, "Enter Door Height");
                        if (!has_thickness)
                            UtilityClass.showToast(context, "Enter Door Thickess");
                        if (!door_wt)
                            UtilityClass.showToast(context, "Please choose door material");
                      /*  if (!door_noise)
                            UtilityClass.showToast(context, "Check if door is making noise");
                        if (!closing_speed)
                            UtilityClass.showToast(context, "Check if Closing speed of door is correct");*/
                    }
                    //  }
                    /*else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {

                            if (is_article_no && has_width && has_height
                                    && has_thickness && door_wt && door_noise
                                    && closing_speed
                                    && checkValidation(product_sub_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                if (!has_width)
                                    UtilityClass.showToast(context,
                                            "Enter Door Width");
                                if (!has_height)
                                    UtilityClass.showToast(context,
                                            "Enter Door Height");
                                if (!has_thickness)
                                    UtilityClass.showToast(context,
                                            "Enter Door Thickess");
                                if (!door_wt)
                                    UtilityClass.showToast(context,
                                            "Please choose door material");
                                if (!door_noise)
                                    UtilityClass.showToast(context,
                                            "Check if door is making noise");
                                if (!closing_speed)
                                    UtilityClass
                                            .showToast(context,
                                                    "Check if Closing speed of door is correct");

                            }
                        }
                    }*/
                }
            });

            // btn_deleteVideo.setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View arg0) {
            // // TODO Auto-generated method stub
            //
            // }
            // });
            //
            final LinearLayout layout = (LinearLayout) view.findViewById(R.id.customer_form);
            customer_info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.isShown()) {
                        layout.setVisibility(View.GONE);
                    } else {
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            });
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            wrong_product.setOnCheckedChangeListener(this);
            val_one.setOnCheckedChangeListener(this);
            val_two.setOnCheckedChangeListener(this);
            val_three.setOnCheckedChangeListener(this);
            floor_chk_1.setOnCheckedChangeListener(this);
            floor_chk_2.setOnCheckedChangeListener(this);
            floor_chk_3.setOnCheckedChangeListener(this);
            floor_chk_4.setOnCheckedChangeListener(this);
            floor_chk_5.setOnCheckedChangeListener(this);
            floor_chk_6.setOnCheckedChangeListener(this);
            floor_chk_7.setOnCheckedChangeListener(this);
            floor_chk_8.setOnCheckedChangeListener(this);

            door_width.setOnFocusChangeListener(this);
            door_height.setOnFocusChangeListener(this);
            door_thickness.setOnFocusChangeListener(this);

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {

                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);
                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;

                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            img_grid.setAdapter(imgAdapter);

            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            /*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside // ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of // child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */
            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM);
            // }
            // });
            // status.setAdapter(new ArrayAdapter<String>(context,
            // android.R.layout.simple_list_item_1, stringList));
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            // set values
            // TODO
            if (report != null) {

                door_height.setText(report.Height);
                door_width.setText(report.Width);
                door_thickness.setText(report.Thickness);
                article_no.setText(report.article_no);
                if (report.G_Door_Wt != null) { // TODO
                    if (report.G_Door_Wt.equals("null")) {
                        if (report.W_Door_Wt != null) {
                            if (report.W_Door_Wt.equals("null")) {

                            } else {
                                wood.setChecked(true);
                                glass.setChecked(false);
                                door_weight.setText(report.W_Door_Wt);
                            }
                        }
                    } else {
                        glass.setChecked(true);
                        wood.setChecked(false);
                        door_weight.setText(report.G_Door_Wt);
                    }
                }

                if (report.correct_pro_one != null) {
                    if (report.correct_pro_one.equals("EN2")) {
                        val_one.setChecked(true);
                        val_two.setChecked(false);
                        val_three.setChecked(false);
                        wrong_product.setChecked(false);
                    } else {
                        val_one.setChecked(false);
                    }
                }

                if (report.correct_pro_two != null) {
                    if (report.correct_pro_two.equals("EN3")) {
                        val_one.setChecked(false);
                        val_two.setChecked(true);
                        val_three.setChecked(false);
                        wrong_product.setChecked(false);
                    } else {
                        val_two.setChecked(false);
                    }
                }

                if (report.correct_pro_three != null) {
                    if (report.correct_pro_three.equals("EN4")) {
                        val_one.setChecked(false);
                        val_two.setChecked(false);
                        val_three.setChecked(true);
                        wrong_product.setChecked(false);
                    } else {
                        val_three.setChecked(false);
                    }
                }

                if (report.wrong_product != null) {
                    if (report.wrong_product.equals("Y")) {
                        wrong_product.setChecked(true);
                    } else {
                        wrong_product.setChecked(false);
                    }
                }

                if (report.Correct_Installation != null) {
                    if (report.Correct_Installation.equals("Y")) {
                        floor_chk_1.setChecked(true);
                        floor_chk_2.setChecked(false);
                    } else if (report.Correct_Installation.equals("N")) {
                        floor_chk_1.setChecked(false);
                        floor_chk_2.setChecked(true);
                    } else {
                        floor_chk_1.setChecked(false);
                        floor_chk_2.setChecked(false);
                    }
                }

                if (report.Pivot_Alignment != null) {
                    if (report.Pivot_Alignment.equals("Y")) {
                        floor_chk_3.setChecked(true);
                        floor_chk_4.setChecked(false);
                    } else if (report.Pivot_Alignment.equals("N")) {
                        floor_chk_3.setChecked(false);
                        floor_chk_4.setChecked(true);
                    } else {
                        floor_chk_3.setChecked(false);
                        floor_chk_4.setChecked(false);
                    }
                }

                if (report.Door_Noise != null) {
                    if (report.Door_Noise.equals("Y")) {
                        floor_chk_5.setChecked(true);
                        floor_chk_6.setChecked(false);
                    } else if (report.Door_Noise.equals("N")) {
                        floor_chk_5.setChecked(false);
                        floor_chk_6.setChecked(true);
                    } else {
                        floor_chk_5.setChecked(false);
                        floor_chk_6.setChecked(false);
                    }
                }

                if (report.Closing_Speed != null) {
                    if (report.Closing_Speed.equals("Y")) {
                        floor_chk_7.setChecked(true);
                        floor_chk_8.setChecked(false);
                    } else if (report.Closing_Speed.equals("N")) {
                        floor_chk_7.setChecked(false);
                        floor_chk_8.setChecked(true);
                    } else {
                        floor_chk_7.setChecked(false);
                        floor_chk_8.setChecked(false);
                    }
                }

                /*if (report.Closure_Status != null) {
                    Log.e("report.Closure_Status", report.Closure_Status);
                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);

                        System.out.println("report.Reason_For_Unresolved--" + report.Reason_For_Unresolved);

                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);
                        // other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/
                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }

                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }


                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

        } else if (product_sub_category.contains("Door Closer")) {
            //Door Closer
            view = inflater.inflate(R.layout.door_closure, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;
            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);

         /*   final LinearLayout unresolved = (LinearLayout) view
                    .findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);
*/
            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            address = (TextView) view.findViewById(R.id.address);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);


            btn_deleteVideo.setOnClickListener(this);
            // form = (LinearLayout) view.findViewById(R.id.form);
            // form.setVisibility(View.GONE);
            // LinearLayout closure_form = (LinearLayout) view
            // .findViewById(R.id.closure_form);
            // closure_form.setVisibility(View.VISIBLE);

            door_width = (EditText) view.findViewById(R.id.door_width);
            door_height = (EditText) view.findViewById(R.id.door_height);
            door_thickness = (EditText) view.findViewById(R.id.door_thickness);
            val_one = (CheckBox) view.findViewById(R.id.val_one);
            val_two = (CheckBox) view.findViewById(R.id.val_two);
            val_three = (CheckBox) view.findViewById(R.id.val_three);
            wrong_product = (CheckBox) view.findViewById(R.id.wrong_product);
            RadioButton glass = (RadioButton) view.findViewById(R.id.glass);
            RadioButton wood = (RadioButton) view.findViewById(R.id.wood);
            door_weight = (TextView) view.findViewById(R.id.door_weight);

            Button submit = (Button) view.findViewById(R.id.submit);

            glass.setOnClickListener(this);
            wood.setOnClickListener(this);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            dc_chk_1 = (CheckBox) view.findViewById(R.id.dc_chk_1);
            dc_chk_2 = (CheckBox) view.findViewById(R.id.dc_chk_2);
            dc_chk_3 = (CheckBox) view.findViewById(R.id.dc_chk_3);
            dc_chk_4 = (CheckBox) view.findViewById(R.id.dc_chk_4);
            dc_chk_5 = (CheckBox) view.findViewById(R.id.dc_chk_5);
            dc_chk_6 = (CheckBox) view.findViewById(R.id.dc_chk_6);
            dc_chk_7 = (CheckBox) view.findViewById(R.id.dc_chk_7);
            dc_chk_8 = (CheckBox) view.findViewById(R.id.dc_chk_8);
            dc_chk_9 = (CheckBox) view.findViewById(R.id.dc_chk_9);
            dc_chk_10 = (CheckBox) view.findViewById(R.id.dc_chk_10);
            dc_chk_11 = (CheckBox) view.findViewById(R.id.dc_chk_11);
            dc_chk_12 = (CheckBox) view.findViewById(R.id.dc_chk_12);
            dc_chk_13 = (CheckBox) view.findViewById(R.id.dc_chk_13);
            dc_chk_14 = (CheckBox) view.findViewById(R.id.dc_chk_14);
            dc_chk_15 = (CheckBox) view.findViewById(R.id.dc_chk_15);
            dc_chk_16 = (CheckBox) view.findViewById(R.id.dc_chk_16);

            val_one.setOnCheckedChangeListener(this);
            val_two.setOnCheckedChangeListener(this);
            val_three.setOnCheckedChangeListener(this);
            dc_chk_1.setOnCheckedChangeListener(this);
            dc_chk_2.setOnCheckedChangeListener(this);
            dc_chk_3.setOnCheckedChangeListener(this);
            dc_chk_4.setOnCheckedChangeListener(this);
            dc_chk_5.setOnCheckedChangeListener(this);
            dc_chk_6.setOnCheckedChangeListener(this);
            dc_chk_7.setOnCheckedChangeListener(this);
            dc_chk_8.setOnCheckedChangeListener(this);
            dc_chk_9.setOnCheckedChangeListener(this);
            dc_chk_10.setOnCheckedChangeListener(this);
            dc_chk_11.setOnCheckedChangeListener(this);
            dc_chk_12.setOnCheckedChangeListener(this);
            dc_chk_13.setOnCheckedChangeListener(this);
            dc_chk_14.setOnCheckedChangeListener(this);
            dc_chk_15.setOnCheckedChangeListener(this);
            dc_chk_16.setOnCheckedChangeListener(this);
            wrong_product.setOnCheckedChangeListener(this);
            door_width.setOnFocusChangeListener(this);
            door_height.setOnFocusChangeListener(this);
            door_thickness.setOnFocusChangeListener(this);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // boolean has_comment = Validation.hasText(comments);
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //  boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                  /*  if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/
                    boolean door_wt = Validation.hasText(door_weight);
                    ;
                    boolean has_width = Validation.hasText(door_width);
                    boolean has_height = Validation.hasText(door_height);
                    boolean has_thickness = Validation.hasText(door_thickness);
                    boolean door_noise = false;
                    if (contentValues.get("Door_Noise") != null) {
                        if (contentValues.get("Door_Noise").equals("null")) {
                            door_noise = false;
                        } else {
                            door_noise = true;
                        }

                        if (contentValues.get("Door_Noise").equals("Y"))
                            door_noise = true;
                        if (contentValues.get("Door_Noise").equals("N"))
                            door_noise = true;

                    }

                    if (!dc_chk_1.isEnabled()) {
                        door_noise = true;
                    }
                    if (!dc_chk_2.isChecked()) {
                        door_noise = true;
                    }

                /*    boolean closing_speed = false;
                    if (contentValues.get("Closing_Speed") != null) {
                        if (contentValues.get("Closing_Speed").equals("null")) {
                            closing_speed = false;
                        } else {
                            closing_speed = true;
                        }
                        if (contentValues.get("Closing_Speed").equals("Y"))
                            closing_speed = true;
                        if (contentValues.get("Closing_Speed").equals("N"))
                            closing_speed = true;

                    }
                    if (!dc_chk_15.isEnabled()) {
                        closing_speed = true;
                    }
                    if (!dc_chk_16.isEnabled()) {
                        closing_speed = true;
                    }*/

                    //   if (unresolve_reason.getSelectedItem().toString()
                    //   .equals("Others")) {
                    if (is_article_no && has_width && has_height && has_thickness && door_wt && door_noise /*&& closing_speed*/) {
                        if (checkValidation(product_sub_category)) {
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }
                        }

                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                        if (!has_width)
                            UtilityClass.showToast(context,
                                    "Enter Door Width");
                        if (!has_height)
                            UtilityClass.showToast(context,
                                    "Enter Door Height");
                        if (!has_thickness)
                            UtilityClass.showToast(context,
                                    "Enter Door Thickess");
                        if (!door_wt)
                            UtilityClass.showToast(context,
                                    "Please choose door material");
                        if (!door_noise)
                            UtilityClass.showToast(context,
                                    "Check if door is making noise");
                       /* if (!closing_speed)
                            UtilityClass
                                    .showToast(context,
                                            "Check if Closing speed of door is correct");*/
                    }
                 /*   } else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {
                            if (is_article_no && has_width && has_height
                                    && has_thickness && door_wt && door_noise
                                    && closing_speed
                                    && checkValidation(product_sub_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                if (!has_width)
                                    UtilityClass.showToast(context,
                                            "Enter Door Width");
                                if (!has_height)
                                    UtilityClass.showToast(context,
                                            "Enter Door Height");
                                if (!has_thickness)
                                    UtilityClass.showToast(context,
                                            "Enter Door Thickess");
                                if (!door_wt)
                                    UtilityClass.showToast(context,
                                            "Please choose door material");
                                if (!door_noise)
                                    UtilityClass.showToast(context,
                                            "Check if door is making noise");
                                if (!closing_speed)
                                    UtilityClass
                                            .showToast(context,
                                                    "Check if Closing speed of door is correct");
                                // if(!otherReason)UtilityClass.showToast(context,
                                // "Other reason cannot be empty");

                            }

                        }
                    }*/
                }
            });

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;

                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

			/*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside // ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of // child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */

            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

            // status.setAdapter(new ArrayAdapter<String>(context,
            // android.R.layout.simple_list_item_1, stringList));

          /*  final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);
           /* unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        // unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {

                door_height.setText(report.Height);
                door_width.setText(report.Width);
                door_thickness.setText(report.Thickness);
                article_no.setText(report.article_no);
                if (report.G_Door_Wt != null) { // TODO
                    if (report.G_Door_Wt.equals("null")) {
                        if (report.W_Door_Wt != null) {
                            if (report.W_Door_Wt.equals("null")) {

                            } else {
                                wood.setChecked(true);
                                glass.setChecked(false);
                                door_weight.setText(report.W_Door_Wt);
                            }
                        }
                    } else {
                        glass.setChecked(true);
                        wood.setChecked(false);
                        door_weight.setText(report.G_Door_Wt);
                    }
                }

                if (report.correct_pro_one != null) {
                    if (report.correct_pro_one.equals("EN2")) {
                        val_one.setChecked(true);
                        val_two.setChecked(false);
                        val_three.setChecked(false);
                        wrong_product.setChecked(false);
                    } else {
                        val_one.setChecked(false);
                    }
                }

                if (report.correct_pro_two != null) {
                    if (report.correct_pro_two.equals("EN3")) {
                        val_one.setChecked(false);
                        val_two.setChecked(true);
                        val_three.setChecked(false);
                        wrong_product.setChecked(false);
                    } else {
                        val_two.setChecked(false);
                    }
                }

                if (report.correct_pro_three != null) {
                    if (report.correct_pro_three.equals("EN4")) {
                        val_one.setChecked(false);
                        val_two.setChecked(false);
                        val_three.setChecked(true);
                        wrong_product.setChecked(false);
                    } else {
                        val_three.setChecked(false);
                    }
                }

                if (report.wrong_product != null) {
                    if (report.wrong_product.equals("Y")) {
                        wrong_product.setChecked(true);
                    } else {
                        wrong_product.setChecked(false);
                    }
                }

                if (report.Door_Noise != null) {
                    if (report.Door_Noise.equals("Y")) {
                        dc_chk_1.setChecked(true);
                        dc_chk_2.setChecked(false);
                    } else if (report.Door_Noise.equals("N")) {
                        dc_chk_1.setChecked(false);
                        dc_chk_2.setChecked(true);
                    } else {
                        dc_chk_1.setChecked(false);
                        dc_chk_2.setChecked(false);
                    }
                }

                if (report.Screw_Fix != null) {
                    if (report.Screw_Fix.equals("Y")) {
                        dc_chk_3.setChecked(true);
                        dc_chk_4.setChecked(false);
                    } else if (report.Screw_Fix.equals("N")) {
                        dc_chk_3.setChecked(false);
                        dc_chk_4.setChecked(true);
                    } else {
                        dc_chk_3.setChecked(false);
                        dc_chk_4.setChecked(false);
                    }
                }

                if (report.Edge_Distance != null) {
                    if (report.Edge_Distance.equals("Y")) {
                        dc_chk_5.setChecked(true);
                        dc_chk_6.setChecked(false);
                    } else if (report.Edge_Distance.equals("N")) {
                        dc_chk_5.setChecked(false);
                        dc_chk_6.setChecked(true);
                    } else {
                        dc_chk_5.setChecked(false);
                        dc_chk_6.setChecked(false);
                    }
                }

                if (report.Door_Depth != null) {
                    if (report.Door_Depth.equals("Y")) {
                        dc_chk_7.setChecked(true);
                        dc_chk_8.setChecked(false);
                    } else if (report.Door_Depth.equals("N")) {
                        dc_chk_7.setChecked(false);
                        dc_chk_8.setChecked(true);
                    } else {
                        dc_chk_7.setChecked(false);
                        dc_chk_8.setChecked(false);
                    }
                }

                if (report.Frame_Depth != null) {
                    if (report.Frame_Depth.equals("Y")) {
                        dc_chk_9.setChecked(true);
                        dc_chk_10.setChecked(false);
                    } else if (report.Frame_Depth.equals("N")) {
                        dc_chk_9.setChecked(false);
                        dc_chk_10.setChecked(true);
                    } else {
                        dc_chk_9.setChecked(false);
                        dc_chk_10.setChecked(false);
                    }
                }

                if (report.Centre_Cut_Out != null) {
                    if (report.Centre_Cut_Out.equals("Y")) {
                        dc_chk_11.setChecked(true);
                        dc_chk_12.setChecked(false);
                    } else if (report.Centre_Cut_Out.equals("N")) {
                        dc_chk_11.setChecked(false);
                        dc_chk_12.setChecked(true);
                    } else {
                        dc_chk_11.setChecked(false);
                        dc_chk_12.setChecked(false);
                    }
                }

                if (report.Latching_Speed != null) {
                    if (report.Latching_Speed.equals("Y")) {
                        dc_chk_13.setChecked(true);
                        dc_chk_14.setChecked(false);
                    } else if (report.Latching_Speed.equals("N")) {
                        dc_chk_13.setChecked(false);
                        dc_chk_14.setChecked(true);
                    } else {
                        dc_chk_13.setChecked(false);
                        dc_chk_14.setChecked(false);
                    }
                }

                if (report.Closing_Speed != null) {
                    if (report.Closing_Speed.equals("Y")) {
                        dc_chk_15.setChecked(true);
                        dc_chk_16.setChecked(false);
                    } else if (report.Closing_Speed.equals("N")) {
                        dc_chk_15.setChecked(false);
                        dc_chk_16.setChecked(true);
                    } else {
                        dc_chk_15.setChecked(false);
                        dc_chk_16.setChecked(false);
                    }
                }

                /*if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/

                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);

                    }

                    comments.setText(report.Comment);

                }

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

        } else if (product_sub_category.contains("Aventos")) {

            //Aventos
            view = inflater.inflate(R.layout.lift_up_system, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;
            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view
                    .findViewById(R.id.complaint_deatils);
            door_width = (EditText) view.findViewById(R.id.door_width);
            door_height = (EditText) view.findViewById(R.id.door_height);
            door_thickness = (EditText) view.findViewById(R.id.door_thickness);
            handle_weight = (EditText) view.findViewById(R.id.handle_weight);
            RadioButton glass = (RadioButton) view.findViewById(R.id.glass);
            RadioButton wood = (RadioButton) view.findViewById(R.id.wood);
            door_weight = (TextView) view.findViewById(R.id.door_weight);
            glass.setOnClickListener(this);
            wood.setOnClickListener(this);
            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            address = (TextView) view.findViewById(R.id.address);
            power_factor = (EditText) view.findViewById(R.id.power_factor);
            /*final LinearLayout unresolved = (LinearLayout) view
                    .findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);*/
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);


            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            btn_deleteVideo.setOnClickListener(this);
            ls_chk_1 = (CheckBox) view.findViewById(R.id.ls_chk_1);
            ls_chk_2 = (CheckBox) view.findViewById(R.id.ls_chk_2);
            ls_chk_3 = (CheckBox) view.findViewById(R.id.ls_chk_3);
            ls_chk_4 = (CheckBox) view.findViewById(R.id.ls_chk_4);
            ls_chk_5 = (CheckBox) view.findViewById(R.id.ls_chk_5);
            ls_chk_6 = (CheckBox) view.findViewById(R.id.ls_chk_6);
            ls_chk_7 = (CheckBox) view.findViewById(R.id.ls_chk_7);
            ls_chk_8 = (CheckBox) view.findViewById(R.id.ls_chk_8);
            ls_chk_9 = (CheckBox) view.findViewById(R.id.ls_chk_9);
            ls_chk_10 = (CheckBox) view.findViewById(R.id.ls_chk_10);
            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            door_width.setOnFocusChangeListener(this);
            door_height.setOnFocusChangeListener(this);
            door_thickness.setOnFocusChangeListener(this);

            handle_weight.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    power_factor.setText("");
                    try {
                        if (Validation.hasText(door_weight)
                                && Validation.hasText(handle_weight)
                                && Validation.hasText(door_height)) {
                            double power_fac = Integer.parseInt(door_height
                                    .getText().toString())
                                    * (Double.parseDouble(door_weight.getText()
                                    .toString()) + Double
                                    .parseDouble(handle_weight
                                            .getText().toString()));
                            power_factor.setText(String.valueOf(power_fac));

                        } else {
                            UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

			/*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside // ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of // child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */
            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

          /*  final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

      /*      unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        // unresolved.setVisibility(View.VISIBLE);
                    } else {
                        // unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            if (product_sub_category.contains("Aventos HS") || product_sub_category.contains("Aventos HL")) {
                handle_weight.setVisibility(View.GONE);
                TextView pf_text = (TextView) view.findViewById(R.id.pf_text);
                TextView handle_text = (TextView) view
                        .findViewById(R.id.handle_text);
                power_factor.setVisibility(View.GONE);
                handle_text.setVisibility(View.GONE);
                pf_text.setVisibility(View.GONE);
                TextView text = (TextView) view.findViewById(R.id.text_18);
                text.setText("Check if the Front Weight of product is in available range for the Cabinet Height");
            }

            ls_chk_1.setOnCheckedChangeListener(this);
            ls_chk_2.setOnCheckedChangeListener(this);
            ls_chk_3.setOnCheckedChangeListener(this);
            ls_chk_4.setOnCheckedChangeListener(this);
            ls_chk_5.setOnCheckedChangeListener(this);
            ls_chk_6.setOnCheckedChangeListener(this);
            ls_chk_7.setOnCheckedChangeListener(this);
            ls_chk_8.setOnCheckedChangeListener(this);
            ls_chk_9.setOnCheckedChangeListener(this);
            ls_chk_10.setOnCheckedChangeListener(this);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            Button submit = (Button) view.findViewById(R.id.submit);
            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }
            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // boolean has_comment = Validation.hasText(comments);
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                 /*     boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                    if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/

                    boolean power_fac = Validation.hasText(power_factor);
                    boolean has_width = Validation.hasText(door_width);
                    boolean has_height = Validation.hasText(door_height);
                    boolean has_thickness = Validation.hasText(door_thickness);
                    boolean door_wt = Validation.hasText(door_weight);
                    boolean prod_power_fac = false;

                    if (product_sub_category.contains("Aventos HS")
                            || product_sub_category.contains("Aventos HL")) {
                        power_fac = true;
                    }

                    if (contentValues.get("Product_Power_Factor") != null) {
                        prod_power_fac = true;
                    }
                    boolean lift_mechanism = false;
                    if (contentValues.get("Lift_Mechanism_Installed") != null) {
                        lift_mechanism = true;
                    } else if (!ls_chk_3.isEnabled()) {
                        lift_mechanism = true;
                    }
                    boolean arms_installed = false;
                    if (contentValues.get("Arms_Installed") != null) {
                        arms_installed = true;
                    } else if (!ls_chk_5.isEnabled()) {
                        arms_installed = true;
                    }
                    boolean correct_arm_size = false;
                    if (contentValues.get("Arms_Size") != null) {
                        correct_arm_size = true;
                    } else if (!ls_chk_7.isEnabled()) {
                        correct_arm_size = true;
                    }
                    boolean power_adjustment = false;
                    if (contentValues.get("Power_Adjustment") != null) {
                        power_adjustment = true;
                    } else if (!ls_chk_9.isEnabled()) {
                        power_adjustment = true;
                    }

                /*  if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {*/
                    if (is_article_no && lift_mechanism && arms_installed
                            && correct_arm_size && prod_power_fac
                            && power_adjustment && power_fac && door_wt
                            && has_width && has_height && has_thickness) {

                        contentValues.put("handle_weight", handle_weight
                                .getText().toString());

                        if (checkValidation(product_sub_category)) {
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }
                        }
                        //submitData(product_sub_category);
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                        if (!has_width)
                            UtilityClass.showToast(context,
                                    "Enter Door Width");
                        if (!has_height)
                            UtilityClass.showToast(context,
                                    "Enter Door Height");
                        if (!has_thickness)
                            UtilityClass.showToast(context,
                                    "Enter Door Thickess");
                        if (!door_wt)
                            UtilityClass.showToast(context,
                                    "Please choose door material");
                        if (!power_fac)
                            UtilityClass.showToast(context,
                                    "Power factor is null");
                        if (!prod_power_fac)
                            UtilityClass
                                    .showToast(context,
                                            "Check if power factor product is in range");
                        if (!lift_mechanism)
                            UtilityClass.showToast(context,
                                    "Check lift mechanism");
                        if (!arms_installed)
                            UtilityClass.showToast(context,
                                    "Check if arms are installed");
                        if (!correct_arm_size)
                            UtilityClass.showToast(context,
                                    "Check installed arms size");
                        if (!power_adjustment)
                            UtilityClass.showToast(context,
                                    "Check if power adjustment is needed");
                          /*  if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");*/
                    }
                  /*  } else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {

                            if (is_article_no && lift_mechanism
                                    && arms_installed && correct_arm_size
                                    && prod_power_fac && power_adjustment
                                    && power_fac && door_wt && has_width
                                    && has_height && has_thickness
                                    && checkValidation(product_sub_category)) {

                                contentValues.put("handle_weight",
                                        handle_weight.getText().toString());

                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                if (!has_width)
                                    UtilityClass.showToast(context,
                                            "Enter Door Width");
                                if (!has_height)
                                    UtilityClass.showToast(context,
                                            "Enter Door Height");
                                if (!has_thickness)
                                    UtilityClass.showToast(context,
                                            "Enter Door Thickess");
                                if (!door_wt)
                                    UtilityClass.showToast(context,
                                            "Please choose door material");
                                if (!power_fac)
                                    UtilityClass.showToast(context,
                                            "Power factor is null");
                                if (!prod_power_fac)
                                    UtilityClass
                                            .showToast(context,
                                                    "Check if power factor product is in range");
                                if (!lift_mechanism)
                                    UtilityClass.showToast(context,
                                            "Check lift mechanism");
                                if (!arms_installed)
                                    UtilityClass.showToast(context,
                                            "Check if arms are installed");
                                if (!correct_arm_size)
                                    UtilityClass.showToast(context,
                                            "Check installed arms size");
                                if (!power_adjustment)
                                    UtilityClass
                                            .showToast(context,
                                                    "Check if power adjustment is needed");
                                // if(!otherReason)UtilityClass.showToast(context,
                                // "Other reason cannot be empty");
                            }
                        }*/
                    //   }

                }
            });

            // set values
            // TODO
            if (report != null) {
                door_height.setText(report.Height);
                door_width.setText(report.Width);
                door_thickness.setText(report.Thickness);
                article_no.setText(report.article_no);

                if (report.G_Door_Wt != null) { // TODO
                    if (report.G_Door_Wt.equals("null")) {
                        if (report.W_Door_Wt != null) {
                            if (report.W_Door_Wt.equals("null")) {

                            } else {
                                wood.setChecked(true);
                                glass.setChecked(false);
                                door_weight.setText(report.W_Door_Wt);
                            }
                        }
                    } else {
                        glass.setChecked(true);
                        wood.setChecked(false);
                        door_weight.setText(report.G_Door_Wt);
                    }
                }

                if (report.Handle_Weight != null) {
                    handle_weight.setText(report.Handle_Weight);
                }

                if (report.power_factor != null) {
                    power_factor.setText(report.power_factor);
                }

                if (report.Product_Power_Factor != null) {
                    if (report.Product_Power_Factor.equals("Y")) {
                        ls_chk_1.setChecked(true);
                        ls_chk_2.setChecked(false);
                    } else if (report.Product_Power_Factor.equals("N")) {
                        ls_chk_1.setChecked(false);
                        ls_chk_2.setChecked(true);
                    } else {
                        ls_chk_1.setChecked(false);
                        ls_chk_2.setChecked(false);
                    }
                }

                if (report.Lift_Mechanism_Installed != null) {
                    if (report.Lift_Mechanism_Installed.equals("Y")) {
                        ls_chk_3.setChecked(true);
                        ls_chk_4.setChecked(false);
                    } else if (report.Lift_Mechanism_Installed.equals("N")) {
                        ls_chk_3.setChecked(false);
                        ls_chk_4.setChecked(true);
                    } else {
                        ls_chk_3.setChecked(false);
                        ls_chk_4.setChecked(false);
                    }
                }

                if (report.Arms_Installed != null) {
                    if (report.Arms_Installed.equals("Y")) {
                        ls_chk_5.setChecked(true);
                        ls_chk_6.setChecked(false);
                    } else if (report.Arms_Installed.equals("N")) {
                        ls_chk_5.setChecked(false);
                        ls_chk_6.setChecked(true);
                    } else {
                        ls_chk_5.setChecked(false);
                        ls_chk_6.setChecked(false);
                    }
                }

                if (report.Arms_Size != null) {
                    if (report.Arms_Size.equals("Y")) {
                        ls_chk_7.setChecked(true);
                        ls_chk_8.setChecked(false);
                    } else if (report.Arms_Size.equals("N")) {
                        ls_chk_7.setChecked(false);
                        ls_chk_8.setChecked(true);
                    } else {
                        ls_chk_7.setChecked(false);
                        ls_chk_8.setChecked(false);
                    }
                }

                if (report.Power_Adjustment != null) {
                    if (report.Power_Adjustment.equals("Y")) {
                        ls_chk_9.setChecked(true);
                        ls_chk_10.setChecked(false);
                    } else if (report.Power_Adjustment.equals("N")) {
                        ls_chk_9.setChecked(false);
                        ls_chk_10.setChecked(true);
                    } else {
                        ls_chk_9.setChecked(false);
                        ls_chk_10.setChecked(false);
                    }
                }


                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);

                    }


                }

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }


        } else if (product_category.contains("Furniture Fitting-Sliding Fittings") || product_category.contains("Architechtural Sliding Fittings")) {

         /*   (product_sub_category.contains("Sliding Wardrobe Straight Fitting")
                    || product_sub_category.contains("Sliding and Folding Door")) {*/
            view = inflater.inflate(R.layout.sliding_fitting, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view
                    .findViewById(R.id.complaint_deatils);
            door_width = (EditText) view.findViewById(R.id.door_width);
            door_height = (EditText) view.findViewById(R.id.door_height);
            door_thickness = (EditText) view.findViewById(R.id.door_thickness);
            RadioButton glass = (RadioButton) view.findViewById(R.id.glass);
            RadioButton wood = (RadioButton) view.findViewById(R.id.wood);
            door_weight = (TextView) view.findViewById(R.id.door_weight);
            //final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            // unresolved.setVisibility(View.GONE);
            glass.setOnClickListener(this);
            wood.setOnClickListener(this);
            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            status = (Spinner) view.findViewById(R.id.status);
            btn_deleteVideo.setOnClickListener(this);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);


            TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);
            address = (TextView) view.findViewById(R.id.address);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);
            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            sf_chk_1 = (CheckBox) view.findViewById(R.id.sf_chk_1);
            sf_chk_2 = (CheckBox) view.findViewById(R.id.sf_chk_2);
            sf_chk_3 = (CheckBox) view.findViewById(R.id.sf_chk_3);
            sf_chk_4 = (CheckBox) view.findViewById(R.id.sf_chk_4);
            sf_chk_5 = (CheckBox) view.findViewById(R.id.sf_chk_5);
            sf_chk_6 = (CheckBox) view.findViewById(R.id.sf_chk_6);
            sf_chk_7 = (CheckBox) view.findViewById(R.id.sf_chk_7);
            sf_chk_8 = (CheckBox) view.findViewById(R.id.sf_chk_8);
            sf_chk_9 = (CheckBox) view.findViewById(R.id.sf_chk_9);
            sf_chk_10 = (CheckBox) view.findViewById(R.id.sf_chk_10);
            sf_chk_11 = (CheckBox) view.findViewById(R.id.sf_chk_11);
            sf_chk_12 = (CheckBox) view.findViewById(R.id.sf_chk_12);
            sf_chk_13 = (CheckBox) view.findViewById(R.id.sf_chk_13);
            sf_chk_14 = (CheckBox) view.findViewById(R.id.sf_chk_14);

            if (product_category.contains("Architechtural Sliding Fittings")) {
                text.setVisibility(View.GONE);
                sf_chk_3.setVisibility(View.GONE);
                sf_chk_4.setVisibility(View.GONE);

                sf_chk_13.setVisibility(View.GONE);
                sf_chk_14.setVisibility(View.GONE);
                //  t_xt.setText("Planofit is used?");
                t_xt.setVisibility(View.GONE);
                // glass.setVisibility(View.GONE);
                //   wood.setVisibility(View.GONE);
            }

            sf_chk_1.setOnCheckedChangeListener(this);
            sf_chk_2.setOnCheckedChangeListener(this);
            sf_chk_3.setOnCheckedChangeListener(this);
            sf_chk_4.setOnCheckedChangeListener(this);
            sf_chk_5.setOnCheckedChangeListener(this);
            sf_chk_6.setOnCheckedChangeListener(this);
            sf_chk_7.setOnCheckedChangeListener(this);
            sf_chk_8.setOnCheckedChangeListener(this);
            sf_chk_9.setOnCheckedChangeListener(this);
            sf_chk_10.setOnCheckedChangeListener(this);
            sf_chk_11.setOnCheckedChangeListener(this);
            sf_chk_12.setOnCheckedChangeListener(this);
            sf_chk_13.setOnCheckedChangeListener(this);
            sf_chk_14.setOnCheckedChangeListener(this);

            door_width.setOnFocusChangeListener(this);
            door_height.setOnFocusChangeListener(this);
            door_thickness.setOnFocusChangeListener(this);
            Button submit = (Button) view.findViewById(R.id.submit);
            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }
            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // boolean has_comment = Validation.hasText(comments);
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //       boolean otherReason = false;
                    /*if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                    boolean door_wt = Validation.hasText(door_weight);
                    boolean has_width = Validation.hasText(door_width);
                    boolean has_height = Validation.hasText(door_height);
                    boolean has_thickness = Validation.hasText(door_thickness);

                    //  if (unresolve_reason.getSelectedItem().toString()
                    //         .equals("Others")) {
                    if (is_article_no && has_width && has_height
                            && has_thickness && door_wt) {
                        if (checkValidation(product_category)) {
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }

                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");

                        if (!has_width)
                            UtilityClass.showToast(context,
                                    "Enter Door Width");
                        if (!has_height)
                            UtilityClass.showToast(context,
                                    "Enter Door Height");
                        if (!has_thickness)
                            UtilityClass.showToast(context,
                                    "Enter Door Thickess");
                        if (!door_wt)
                            UtilityClass.showToast(context,
                                    "Please choose door material");
                    }
                  /*  } else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {

                            if (is_article_no && has_width && has_height
                                    && has_thickness && door_wt
                                    && checkValidation(product_sub_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");

                                if (!has_width)
                                    UtilityClass.showToast(context,
                                            "Enter Door Width");
                                if (!has_height)
                                    UtilityClass.showToast(context,
                                            "Enter Door Height");
                                if (!has_thickness)
                                    UtilityClass.showToast(context,
                                            "Enter Door Thickess");
                                if (!door_wt)
                                    UtilityClass.showToast(context,
                                            "Please choose door material");
                                // if(!otherReason)UtilityClass.showToast(context,
                                // "Other reason cannot be empty");
                            }

                        }
                    }*/
                }
            });

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            // status.setAdapter(new ArrayAdapter<String>(context,
            // android.R.layout.simple_list_item_1, stringList));
            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);
          /*  final LinearLayout other = (LinearLayout) viewfindViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

        /*    unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {
                door_height.setText(report.Height);
                door_width.setText(report.Width);
                door_thickness.setText(report.Thickness);
                article_no.setText(report.article_no);

                if (report.G_Door_Wt != null) { // TODO
                    if (report.G_Door_Wt.equals("null")) {
                        if (report.W_Door_Wt != null) {
                            if (report.W_Door_Wt.equals("null")) {

                            } else {
                                wood.setChecked(true);
                                glass.setChecked(false);
                                door_weight.setText(report.W_Door_Wt);
                            }
                        }
                    } else {
                        glass.setChecked(true);
                        wood.setChecked(false);
                        door_weight.setText(report.G_Door_Wt);
                    }
                }

                if (report.Door_Weight_Within_Range != null) {
                    if (report.Door_Weight_Within_Range.equals("Y")) {
                        sf_chk_1.setChecked(true);
                        sf_chk_2.setChecked(false);
                    } else if (report.Door_Weight_Within_Range.equals("N")) {
                        sf_chk_1.setChecked(false);
                        sf_chk_2.setChecked(true);
                    } else {
                        sf_chk_1.setChecked(false);
                        sf_chk_2.setChecked(false);
                    }
                }

				/*
                 * if (report.Sliding_Sys_Door_Wt != null) { if
				 * (report.Sliding_Sys_Door_Wt.equals("Y")) {
				 * sf_chk_1.setChecked(true); sf_chk_2.setChecked(false); } else
				 * if (report.Sliding_Sys_Door_Wt.equals("N")) {
				 * sf_chk_1.setChecked(false); sf_chk_2.setChecked(true); } else
				 * { sf_chk_1.setChecked(false); sf_chk_2.setChecked(false); } }
				 */

                if (report.Carcase_Requirement != null) {
                    if (report.Carcase_Requirement.equals("Y")) {
                        sf_chk_3.setChecked(true);
                        sf_chk_4.setChecked(false);
                    } else if (report.Carcase_Requirement.equals("N")) {
                        sf_chk_3.setChecked(false);
                        sf_chk_4.setChecked(true);
                    } else {
                        sf_chk_3.setChecked(false);
                        sf_chk_4.setChecked(false);
                    }
                }

                if (report.Installation != null) {
                    if (report.Installation.equals("Y")) {
                        sf_chk_5.setChecked(true);
                        sf_chk_6.setChecked(false);
                    } else if (report.Installation.equals("N")) {
                        sf_chk_5.setChecked(false);
                        sf_chk_6.setChecked(true);
                    } else {
                        sf_chk_5.setChecked(false);
                        sf_chk_6.setChecked(false);
                    }
                }

                if (report.Track_Roller_Clean != null) {
                    if (report.Track_Roller_Clean.equals("Y")) {
                        sf_chk_7.setChecked(true);
                        sf_chk_8.setChecked(false);
                    } else if (report.Track_Roller_Clean.equals("N")) {
                        sf_chk_7.setChecked(false);
                        sf_chk_8.setChecked(true);
                    } else {
                        sf_chk_7.setChecked(false);
                        sf_chk_8.setChecked(false);
                    }
                }

                if (report.Door_Wrapped != null) {
                    if (report.Door_Wrapped.equals("Y")) {
                        sf_chk_9.setChecked(true);
                        sf_chk_10.setChecked(false);
                    } else if (report.Door_Wrapped.equals("N")) {
                        sf_chk_9.setChecked(false);
                        sf_chk_10.setChecked(true);
                    } else {
                        sf_chk_9.setChecked(false);
                        sf_chk_10.setChecked(false);
                    }
                }

                if (report.Softclose_Broken != null) {
                    if (report.Softclose_Broken.equals("Y")) {
                        sf_chk_13.setChecked(true);
                        sf_chk_14.setChecked(false);
                    } else if (report.Softclose_Broken.equals("N")) {
                        sf_chk_13.setChecked(false);
                        sf_chk_14.setChecked(true);
                    } else {
                        sf_chk_13.setChecked(false);
                        sf_chk_14.setChecked(false);
                    }
                }

                if (report.Plan_Used != null) {
                    if (report.Plan_Used.equals("Y")) {
                        sf_chk_11.setChecked(true);
                        sf_chk_12.setChecked(false);
                    } else if (report.Plan_Used.equals("N")) {
                        sf_chk_11.setChecked(false);
                        sf_chk_12.setChecked(true);
                    } else {
                        sf_chk_11.setChecked(false);
                        sf_chk_12.setChecked(false);
                    }
                }

                if (report.wrong_product_reason != null) {
                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                }

               /* if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/
                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                    }

                }

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

        } else if (product_category.contains("Drawers")) {
            // Drawers
            //else if (product_category.contains("Drawers"))


            view = inflater.inflate(R.layout.runners, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;


            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(), complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view
                    .findViewById(R.id.complaint_deatils);
            drawer_wt = (EditText) view.findViewById(R.id.drawer_wt);
            drawer_wt_tot = (EditText) view.findViewById(R.id.drawer_wt_tot);
            drawer_wt_tot.setEnabled(false);
            drawer_wt.setOnFocusChangeListener(this);
            address = (TextView) view.findViewById(R.id.address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

          /*  TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);
            final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);*/

            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            btn_deleteVideo.setOnClickListener(this);
            v1_w1 = (EditText) view.findViewById(R.id.v1_w1);
            v1_h1 = (EditText) view.findViewById(R.id.v1_h1);
            v1_t1 = (EditText) view.findViewById(R.id.v1_t1);
            v2_w2 = (EditText) view.findViewById(R.id.v2_w2);
            v2_h2 = (EditText) view.findViewById(R.id.v2_h2);
            v2_t2 = (EditText) view.findViewById(R.id.v2_t2);
            v3_w3 = (EditText) view.findViewById(R.id.v3_w3);
            v3_h3 = (EditText) view.findViewById(R.id.v3_h3);
            v3_t3 = (EditText) view.findViewById(R.id.v3_t3);
            v4_w4 = (EditText) view.findViewById(R.id.v4_w4);
            v4_h4 = (EditText) view.findViewById(R.id.v4_h4);
            v4_t4 = (EditText) view.findViewById(R.id.v4_t4);
            vol_one = (TextView) view.findViewById(R.id.volume_one);
            vol_two = (TextView) view.findViewById(R.id.volume_two);
            vol_three = (TextView) view.findViewById(R.id.volume_three);
            vol_four = (TextView) view.findViewById(R.id.volume_four);

            v1_w1.setOnFocusChangeListener(this);
            v1_h1.setOnFocusChangeListener(this);
            v1_t1.setOnFocusChangeListener(this);
            v2_w2.setOnFocusChangeListener(this);
            v2_h2.setOnFocusChangeListener(this);
            v2_t2.setOnFocusChangeListener(this);
            v3_w3.setOnFocusChangeListener(this);
            v3_h3.setOnFocusChangeListener(this);
            v3_t3.setOnFocusChangeListener(this);
            v4_w4.setOnFocusChangeListener(this);
            v4_h4.setOnFocusChangeListener(this);
            v4_t4.setOnFocusChangeListener(this);

            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            rc_chk_1 = (CheckBox) view.findViewById(R.id.rc_chk_1);
            rc_chk_2 = (CheckBox) view.findViewById(R.id.rc_chk_2);
            rc_chk_3 = (CheckBox) view.findViewById(R.id.rc_chk_3);
            rc_chk_4 = (CheckBox) view.findViewById(R.id.rc_chk_4);
            rc_chk_5 = (CheckBox) view.findViewById(R.id.rc_chk_5);
            rc_chk_6 = (CheckBox) view.findViewById(R.id.rc_chk_6);
            rc_chk_7 = (CheckBox) view.findViewById(R.id.rc_chk_7);
            rc_chk_8 = (CheckBox) view.findViewById(R.id.rc_chk_8);
            rc_chk_9 = (CheckBox) view.findViewById(R.id.rc_chk_9);
            rc_chk_10 = (CheckBox) view.findViewById(R.id.rc_chk_10);
            rc_chk_11 = (CheckBox) view.findViewById(R.id.rc_chk_11);
            rc_chk_12 = (CheckBox) view.findViewById(R.id.rc_chk_12);

            vc_chk_1 = (CheckBox) view.findViewById(R.id.vc_chk_1);
            vc_chk_2 = (CheckBox) view.findViewById(R.id.vc_chk_2);

            rc_chk_1.setOnCheckedChangeListener(this);
            rc_chk_2.setOnCheckedChangeListener(this);
            rc_chk_3.setOnCheckedChangeListener(this);
            rc_chk_4.setOnCheckedChangeListener(this);
            rc_chk_5.setOnCheckedChangeListener(this);
            rc_chk_6.setOnCheckedChangeListener(this);
            rc_chk_7.setOnCheckedChangeListener(this);
            rc_chk_8.setOnCheckedChangeListener(this);
            rc_chk_9.setOnCheckedChangeListener(this);
            rc_chk_10.setOnCheckedChangeListener(this);
            rc_chk_11.setOnCheckedChangeListener(this);
            rc_chk_12.setOnCheckedChangeListener(this);
            vc_chk_1.setOnCheckedChangeListener(this);
            vc_chk_2.setOnCheckedChangeListener(this);

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            // status.setAdapter(new ArrayAdapter<String>(context,
            // android.R.layout.simple_list_item_1, stringList));


            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);
            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

			/*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */

            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

            Button submit = (Button) view.findViewById(R.id.submit);
            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;

                    if (is_article_no) {
                        if (checkValidation(product_category)) {
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                    }
                }
            });


          /*  submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                    if (unresolve_reason.getSelectedItem().toString().equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString().equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }
                    if (unresolve_reason.getSelectedItem().toString().equals("Others")) {
                        if (is_article_no && otherReason && checkValidation(product_category)) {
                            submitData(product_category);
                        } else {
                            if (!is_article_no)
                                UtilityClass.showToast(context,
                                        "Enter article number");
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Other reason cannot be empty");
                        }
                    } else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {

                            if (is_article_no
                                    && checkValidation(product_category)) {
                                submitData(product_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                            }

                        }
                    }
                }
            });*/

       /*     final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
*/
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {
                article_no.setText(report.article_no);
                v1_w1.setText(report.v1_w1);
                v2_w2.setText(report.v2_w2);
                v3_w3.setText(report.v3_w3);
                v4_w4.setText(report.v4_w4);

                v1_h1.setText(report.v1_h1);
                v2_h2.setText(report.v2_h2);
                v3_h3.setText(report.v3_h3);
                v4_h4.setText(report.v4_h4);

                v1_t1.setText(report.v1_t1);
                v2_t2.setText(report.v2_t2);
                v3_t3.setText(report.v3_t3);
                v4_t4.setText(report.v4_t4);

                vol_one.setText(report.side_volume);
                vol_two.setText(report.back_volume);
                vol_three.setText(report.base_volume);
                vol_four.setText(report.facia_volume);

                if (report.Drawer_Wt != null) {
                    if (report.Drawer_Wt.equals("null")) {

                    } else {
                        try {
                            float wt = Float.parseFloat(report.Drawer_Wt);
                            drawer_wt.setText(String.valueOf(wt / 700));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // float wt =
                // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                // drawer_wt_tot.setText(String.valueOf(wt));

                // drawer_wt_tot.setText(report.Drawer_Wt);
                if (report.Weight_Length_Correct != null) {
                    if (report.Weight_Length_Correct.equals("Y")) {
                        vc_chk_1.setChecked(true);
                        vc_chk_2.setChecked(false);
                    } else if (report.Weight_Length_Correct.equals("N")) {
                        vc_chk_1.setChecked(false);
                        vc_chk_2.setChecked(true);
                    } else {
                        vc_chk_1.setChecked(false);
                        vc_chk_2.setChecked(false);
                    }
                }

                if (report.Content_weight_Within_Range != null) {
                    if (report.Content_weight_Within_Range.equals("Y")) {
                        rc_chk_1.setChecked(true);
                        rc_chk_2.setChecked(false);
                    } else if (report.Content_weight_Within_Range.equals("N")) {
                        rc_chk_1.setChecked(false);
                        rc_chk_2.setChecked(true);
                    } else {
                        rc_chk_1.setChecked(false);
                        rc_chk_2.setChecked(false);
                    }
                }

                if (report.Drawer_Construction_Alignment != null) {
                    if (report.Drawer_Construction_Alignment.equals("Y")) {
                        rc_chk_3.setChecked(true);
                        rc_chk_4.setChecked(false);
                    } else if (report.Drawer_Construction_Alignment.equals("N")) {
                        rc_chk_3.setChecked(false);
                        rc_chk_4.setChecked(true);
                    } else {
                        rc_chk_3.setChecked(false);
                        rc_chk_4.setChecked(false);
                    }
                }

                if (report.Product_System_Installation_32 != null) {
                    if (report.Product_System_Installation_32.equals("Y")) {
                        rc_chk_5.setChecked(true);
                        rc_chk_6.setChecked(false);
                    } else if (report.Product_System_Installation_32
                            .equals("N")) {
                        rc_chk_5.setChecked(false);
                        rc_chk_6.setChecked(true);
                    } else {
                        rc_chk_5.setChecked(false);
                        rc_chk_6.setChecked(false);
                    }
                }

                if (report.Drawer_Noise_Check != null) {
                    if (report.Drawer_Noise_Check.equals("Y")) {
                        rc_chk_7.setChecked(true);
                        rc_chk_8.setChecked(false);
                    } else if (report.Drawer_Noise_Check.equals("N")) {
                        rc_chk_7.setChecked(false);
                        rc_chk_8.setChecked(true);
                    } else {
                        rc_chk_7.setChecked(false);
                        rc_chk_8.setChecked(false);
                    }
                }

                if (report.Drawer_Runner_Clean != null) {
                    if (report.Drawer_Runner_Clean.equals("Y")) {
                        rc_chk_9.setChecked(true);
                        rc_chk_10.setChecked(false);
                    } else if (report.Drawer_Runner_Clean.equals("N")) {
                        rc_chk_9.setChecked(false);
                        rc_chk_10.setChecked(true);
                    } else {
                        rc_chk_9.setChecked(false);
                        rc_chk_10.setChecked(false);
                    }
                }

                if (report.Blum_Product_Servodrive != null) {
                    if (report.Blum_Product_Servodrive.equals("Y")) {
                        rc_chk_11.setChecked(true);
                        rc_chk_12.setChecked(false);
                    } else if (report.Blum_Product_Servodrive.equals("N")) {
                        rc_chk_11.setChecked(false);
                        rc_chk_12.setChecked(true);
                    } else {
                        rc_chk_11.setChecked(false);
                        rc_chk_12.setChecked(false);
                    }
                }



               /* if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }

                       *//* int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }*//*
                    }
                }*/

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);

                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }

                if (report.Result != null) {
                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }

                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.wrong_product_reason != null) {
                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                }


            }

        } else if (product_category.contains("Lever Handles") || product_category.equalsIgnoreCase("Lever Handles")) {
            //Lever Handles
            view = inflater.inflate(R.layout.door_handle, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view
                    .findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            btn_deleteVideo.setOnClickListener(this);
            TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            address = (TextView) view.findViewById(R.id.address);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

          /*  final LinearLayout unresolved = (LinearLayout) view
                    .findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);
*/
            dh_chk_1 = (CheckBox) view.findViewById(R.id.dh_chk_1);
            dh_chk_2 = (CheckBox) view.findViewById(R.id.dh_chk_2);
            dh_chk_3 = (CheckBox) view.findViewById(R.id.dh_chk_3);
            dh_chk_4 = (CheckBox) view.findViewById(R.id.dh_chk_4);
            dh_chk_5 = (CheckBox) view.findViewById(R.id.dh_chk_5);
            dh_chk_6 = (CheckBox) view.findViewById(R.id.dh_chk_6);
            dh_chk_7 = (CheckBox) view.findViewById(R.id.dh_chk_7);
            dh_chk_8 = (CheckBox) view.findViewById(R.id.dh_chk_8);
            dh_chk_9 = (CheckBox) view.findViewById(R.id.dh_chk_9);
            dh_chk_10 = (CheckBox) view.findViewById(R.id.dh_chk_10);
            dh_chk_11 = (CheckBox) view.findViewById(R.id.dh_chk_11);
            dh_chk_12 = (CheckBox) view.findViewById(R.id.dh_chk_12);
            dh_chk_13 = (CheckBox) view.findViewById(R.id.dh_chk_13);
            dh_chk_14 = (CheckBox) view.findViewById(R.id.dh_chk_14);
            dh_chk_15 = (CheckBox) view.findViewById(R.id.dh_chk_15);

            dh_chk_1.setOnCheckedChangeListener(this);
            dh_chk_2.setOnCheckedChangeListener(this);
            dh_chk_3.setOnCheckedChangeListener(this);
            dh_chk_4.setOnCheckedChangeListener(this);
            dh_chk_5.setOnCheckedChangeListener(this);
            dh_chk_6.setOnCheckedChangeListener(this);
            dh_chk_7.setOnCheckedChangeListener(this);
            dh_chk_8.setOnCheckedChangeListener(this);
            dh_chk_9.setOnCheckedChangeListener(this);
            dh_chk_10.setOnCheckedChangeListener(this);
            dh_chk_11.setOnCheckedChangeListener(this);
            dh_chk_12.setOnCheckedChangeListener(this);
            dh_chk_13.setOnCheckedChangeListener(this);
            dh_chk_14.setOnCheckedChangeListener(this);
            dh_chk_15.setOnCheckedChangeListener(this);

          /*  final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

            unresolve_reason = (Spinner) view
                    .findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);
        /*    unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);

            Button submit = (Button) view.findViewById(R.id.submit);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //      boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                  /*  if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/
                    //   if (unresolve_reason.getSelectedItem().toString()
                    //         .equals("Others")) {
                    if (is_article_no) {
                        if (checkValidation(product_category)) {
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }
                        //submitData(product_sub_category);
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                         /*   if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");*/
                    }
                   /* } else {
                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            *//*if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");*//*
                        } else {

                            if (is_article_no
                                    && checkValidation(product_sub_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                // if(!otherReason)UtilityClass.showToast(context,
                                // "Other reason cannot be empty");
                            }

                        }
                    }*/
                }
            });

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

			/*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside // ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of // child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */

            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {
                article_no.setText(report.article_no);
                if (report.Mortise_Lock_Template != null) {
                    if (report.Mortise_Lock_Template.equals("Y")) {
                        dh_chk_1.setChecked(true);
                        dh_chk_2.setChecked(false);

                    } else if (report.Mortise_Lock_Template.equals("N")) {
                        dh_chk_1.setChecked(false);
                        dh_chk_2.setChecked(true);
                    } else {
                        dh_chk_1.setChecked(false);
                        dh_chk_2.setChecked(false);
                    }
                }

                if (report.Lock_Protruding != null) {
                    if (report.Lock_Protruding.equals("Y")) {
                        dh_chk_3.setChecked(true);

                    } else {
                        dh_chk_3.setChecked(false);
                    }
                }

                if (report.Plastic_Fillers != null) {
                    if (report.Plastic_Fillers.equals("Y")) {
                        dh_chk_4.setChecked(true);
                    } else {
                        dh_chk_4.setChecked(false);
                    }
                }

                if (report.No_Mortise_Lock != null) {
                    if (report.No_Mortise_Lock.equals("Y")) {
                        dh_chk_5.setChecked(true);
                    } else {
                        dh_chk_5.setChecked(false);
                    }
                }

                if (report.Door_Range_More != null) {
                    if (report.Door_Range_More.equals("Y")) {
                        dh_chk_6.setChecked(true);
                    } else {
                        dh_chk_6.setChecked(false);
                    }
                }

                if (report.Door_Range_Less != null) {
                    if (report.Door_Range_Less.equals("Y")) {
                        dh_chk_7.setChecked(true);
                    } else {
                        dh_chk_7.setChecked(false);
                    }
                }

                if (report.Rose_Touching_Door != null) {
                    if (report.Rose_Touching_Door.equals("Y")) {
                        dh_chk_8.setChecked(true);
                    } else {
                        dh_chk_8.setChecked(false);
                    }
                }

                if (report.Spindle_Size_Correct != null) {
                    if (report.Spindle_Size_Correct.equals("Y")) {
                        dh_chk_9.setChecked(true);
                    } else {
                        dh_chk_9.setChecked(false);
                    }
                }

                if (report.Spoiled_Spindle != null) {
                    if (report.Spoiled_Spindle.equals("Y")) {
                        dh_chk_10.setChecked(true);
                        dh_chk_11.setChecked(false);
                    } else if (report.Spoiled_Spindle.equals("N")) {
                        dh_chk_10.setChecked(false);
                        dh_chk_11.setChecked(true);
                    } else {
                        dh_chk_10.setChecked(false);
                        dh_chk_11.setChecked(false);
                    }
                }

                if (report.Proper_CutOut != null) {
                    if (report.Proper_CutOut.equals("Y")) {
                        dh_chk_12.setChecked(true);
                        dh_chk_13.setChecked(false);
                    } else if (report.Proper_CutOut.equals("N")) {
                        dh_chk_12.setChecked(false);
                        dh_chk_13.setChecked(true);
                    } else {
                        dh_chk_12.setChecked(false);
                        dh_chk_13.setChecked(false);
                    }
                }
                if (report.Strike_Plate_CutOut != null) {
                    if (report.Strike_Plate_CutOut.equals("Y")) {
                        dh_chk_14.setChecked(true);
                        dh_chk_15.setChecked(false);
                    } else if (report.Strike_Plate_CutOut.equals("N")) {
                        dh_chk_14.setChecked(false);
                        dh_chk_15.setChecked(true);
                    } else {
                        dh_chk_14.setChecked(false);
                        dh_chk_15.setChecked(false);
                    }
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);

                    }

                }


                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }


              /*  if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        String r[] = getResources().getStringArray(
                                R.array.reason_array);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

        } else if (product_category.contains("Pull Handles")) {

            //Pull Handles
            view = inflater.inflate(R.layout.pull_handle, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            door_handle_thickness = (EditText) view.findViewById(R.id.thickness);
          /*  TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);*/
            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            address = (TextView) view.findViewById(R.id.address);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            btn_deleteVideo.setOnClickListener(this);
       /*     final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);*/
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            ph_chk_1 = (CheckBox) view.findViewById(R.id.ph_chk_1);
            ph_chk_2 = (CheckBox) view.findViewById(R.id.ph_chk_2);
            ph_chk_3 = (CheckBox) view.findViewById(R.id.ph_chk_3);
            ph_chk_4 = (CheckBox) view.findViewById(R.id.ph_chk_4);

            ph_chk_1.setOnCheckedChangeListener(this);
            ph_chk_2.setOnCheckedChangeListener(this);
            ph_chk_3.setOnCheckedChangeListener(this);
            ph_chk_4.setOnCheckedChangeListener(this);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

            Button submit = (Button) view.findViewById(R.id.submit);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //   boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                 /*   if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/


                    if (is_article_no) {
                        if (checkValidation(product_category)) {
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context, "Enter article number");

                    }


                 /*   boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                 //   boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                    if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }
                    if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        if (is_article_no && otherReason
                                && checkValidation(product_sub_category)) {
                            submitData(product_sub_category);
                        } else {
                            if (!is_article_no)
                                UtilityClass.showToast(context,
                                        "Enter article number");
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        }
                    } else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                            if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        } else {

                            if (is_article_no
                                    && checkValidation(product_sub_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                // if(!otherReason)UtilityClass.showToast(context,
                                // "Other reason cannot be empty");
                            }

                        }
                    }*/
                }
            });

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);
            if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                    }
                }
            }

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

			/*
             * video_grid.setOnTouchListener(new OnTouchListener() { // Setting
			 * on Touch Listener for handling the touch inside // ScrollView
			 *
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * Disallow the touch request for parent scroll on touch of // child
			 * view v.getParent().requestDisallowInterceptTouchEvent(true);
			 * return false; } });
			 */
            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

    /*        final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);
*/
            unresolve_reason = (Spinner) view
                    .findViewById(R.id.unresolve_reason);
       /*     unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
*/
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //     unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {
                article_no.setText(report.article_no);
                if (report.Screw_Built_Check != null) {
                    if (report.Screw_Built_Check.equals("Y")) {
                        ph_chk_1.setChecked(true);
                        ph_chk_2.setChecked(false);
                    } else if (report.Screw_Built_Check.equals("N")) {
                        ph_chk_1.setChecked(false);
                        ph_chk_2.setChecked(true);
                    } else {
                        ph_chk_1.setChecked(false);
                        ph_chk_2.setChecked(false);
                    }
                }

                if (report.Thikness_Range != null) {
                    if (report.Thikness_Range.equals("Y")) {
                        ph_chk_3.setChecked(true);
                        ph_chk_4.setChecked(false);
                    } else if (report.Thikness_Range.equals("N")) {
                        ph_chk_3.setChecked(false);
                        ph_chk_4.setChecked(true);
                    } else {
                        ph_chk_3.setChecked(false);
                        ph_chk_4.setChecked(false);
                    }
                }

                if (report.Thickness != null) {
                    door_handle_thickness.setText(report.Thickness);
                }


                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        // unresolved.setVisibility(View.VISIBLE);

                       /*int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            //     other.setVisibility(View.VISIBLE);
                            unresolve_reason.setSelection(i);
                         //   other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }
*/
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }


                }

                /*if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        String r[] = getResources().getStringArray(
                                R.array.reason_array);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/
                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

        } else if (product_category.contains("Hinges")) {

            //Hinges
            view = inflater.inflate(R.layout.hinges, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }

            door_width = (EditText) view.findViewById(R.id.door_width);
            door_height = (EditText) view.findViewById(R.id.door_height);
            door_thickness = (EditText) view.findViewById(R.id.door_thickness);
            door_weight = (EditText) view.findViewById(R.id.door_weight);
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            btn_deleteVideo.setOnClickListener(this);
           /* final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);

            TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);*/

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);


            address = (TextView) view.findViewById(R.id.address);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            hn_chk_1 = (CheckBox) view.findViewById(R.id.hn_chk_1);
            hn_chk_2 = (CheckBox) view.findViewById(R.id.hn_chk_2);
            hn_chk_3 = (CheckBox) view.findViewById(R.id.hn_chk_3);
            hn_chk_4 = (CheckBox) view.findViewById(R.id.hn_chk_4);
            hn_chk_5 = (CheckBox) view.findViewById(R.id.hn_chk_5);
            hn_chk_6 = (CheckBox) view.findViewById(R.id.hn_chk_6);
            hn_chk_7 = (CheckBox) view.findViewById(R.id.hn_chk_7);
            hn_chk_8 = (CheckBox) view.findViewById(R.id.hn_chk_8);

            hn_chk_1.setOnCheckedChangeListener(this);
            hn_chk_2.setOnCheckedChangeListener(this);
            hn_chk_3.setOnCheckedChangeListener(this);
            hn_chk_4.setOnCheckedChangeListener(this);
            hn_chk_5.setOnCheckedChangeListener(this);
            hn_chk_6.setOnCheckedChangeListener(this);
            hn_chk_7.setOnCheckedChangeListener(this);
            hn_chk_8.setOnCheckedChangeListener(this);

            door_width.setOnFocusChangeListener(this);
            door_height.setOnFocusChangeListener(this);
            door_thickness.setOnFocusChangeListener(this);
            door_weight.setOnFocusChangeListener(this);


         /*   final LinearLayout other = (LinearLayout) view.findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

      /*      unresolve_reason.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int arg2, long arg3) {
                    if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        other.setVisibility(View.VISIBLE);
                        contentValues.put("Reason_For_Unresolved",
                                other_reason.getText().toString());
                    } else {
                        other.setVisibility(View.GONE);
                        other_reason.setText("null");
                        contentValues.put("Reason_For_Unresolved",
                                unresolve_reason.getSelectedItem()
                                        .toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });*/


            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            Button submit = (Button) view.findViewById(R.id.submit);

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);


            if (report != null) {
                article_no.setText(report.article_no);
                door_width.setText(report.Width);
                door_height.setText(report.Height);
                door_thickness.setText(report.Thickness);

                if (report.W_Door_Wt != null) {
                    door_weight.setText(report.W_Door_Wt);
                }

                if (report.Right_Product_Door != null) {
                    if (report.Right_Product_Door.equals("Y")) {
                        hn_chk_1.setChecked(true);
                        hn_chk_2.setChecked(false);
                    } else if (report.Right_Product_Door.equals("N")) {
                        hn_chk_1.setChecked(false);
                        hn_chk_2.setChecked(true);
                    } else {
                        hn_chk_1.setChecked(false);
                        hn_chk_2.setChecked(false);
                    }
                }

                if (report.Hinges_Number_Sufficeint != null) {
                    if (report.Hinges_Number_Sufficeint.equals("Y")) {
                        hn_chk_3.setChecked(true);
                        hn_chk_4.setChecked(false);
                    } else if (report.Hinges_Number_Sufficeint.equals("N")) {
                        hn_chk_3.setChecked(false);
                        hn_chk_4.setChecked(true);
                    } else {
                        hn_chk_3.setChecked(false);
                        hn_chk_4.setChecked(false);
                    }
                }

                if (report.CutOut_Details != null) {
                    if (report.CutOut_Details.equals("Y")) {
                        hn_chk_5.setChecked(true);
                        hn_chk_6.setChecked(false);
                    } else if (report.CutOut_Details.equals("N")) {
                        hn_chk_5.setChecked(false);
                        hn_chk_6.setChecked(true);
                    } else {
                        hn_chk_5.setChecked(false);
                        hn_chk_6.setChecked(false);
                    }
                }

                if (report.Depth_Thickness_Equal != null) {
                    if (report.Depth_Thickness_Equal.equals("Y")) {
                        hn_chk_7.setChecked(true);
                        hn_chk_8.setChecked(false);
                    } else if (report.Depth_Thickness_Equal.equals("N")) {
                        hn_chk_7.setChecked(false);
                        hn_chk_8.setChecked(true);
                    } else {
                        hn_chk_7.setChecked(false);
                        hn_chk_8.setChecked(false);
                    }
                }


                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }
                }

                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }


                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);

                    }
                }

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }

            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;
                    //   boolean otherReason = false;
                    // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                    // otherReason = Validation.hasText(other_reason);
                 /*   if (unresolve_reason.getSelectedItem().toString()
                            .equals("Others")) {
                        otherReason = Validation.hasText(other_reason);
                        if (other_reason.getText().toString().trim().equals(""))
                            otherReason = false;
                    } else if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equalsIgnoreCase("--Select--")) {
                        otherReason = false;
                    }*/
                    boolean door_wt = Validation.hasText(door_weight);
                    boolean has_width = Validation.hasText(door_width);
                    boolean has_height = Validation.hasText(door_height);
                    boolean has_thickness = Validation.hasText(door_thickness);

                    if (is_article_no && has_width && has_height
                            && has_thickness && door_wt) {
                        if (checkValidation(product_category)) {
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                        if (!has_width)
                            UtilityClass.showToast(context,
                                    "Enter door width");
                        if (!has_height)
                            UtilityClass.showToast(context,
                                    "Enter door height");
                        if (!has_thickness)
                            UtilityClass.showToast(context,
                                    "Enter door thickness");
                        if (!door_wt)
                            UtilityClass.showToast(context,
                                    "Please select door material");


                    }  /*else {

                        if (status.getSelectedItem().toString()
                                .equalsIgnoreCase("Unresolved")
                                && unresolve_reason.getSelectedItem()
                                .toString().equals("--Select--")) {
                           /* if (!otherReason)
                                UtilityClass.showToast(context,
                                        "Reason cannot be empty");
                        }else {

                            if (is_article_no && has_width && has_height
                                    && has_thickness && door_wt
                                    && checkValidation(product_category)) {
                                submitData(product_sub_category);
                            } else {
                                if (!is_article_no)
                                    UtilityClass.showToast(context,
                                            "Enter article number");
                                if (!has_width)
                                    UtilityClass.showToast(context,
                                            "Enter door width");
                                if (!has_height)
                                    UtilityClass.showToast(context,
                                            "Enter door height");
                                if (!has_thickness)
                                    UtilityClass.showToast(context,
                                            "Enter door thickness");
                                if (!door_wt)
                                    UtilityClass.showToast(context,
                                            "Please select door material");
                            }

                        }
                    }*/
                }
            });



         /*   if (report != null) {
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }
                }
            }*/

            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    video_grid.setImageBitmap(bmThumbnail);
                    inputpath = vidData.get(0).FilePath;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            // video_grid.setOnTouchListener(new OnTouchListener() {
            // // Setting on Touch Listener for handling the touch inside
            // // ScrollView
            // @Override
            // public boolean onTouch(View v, MotionEvent event) {
            // // Disallow the touch request for parent scroll on touch of
            // // child view
            // v.getParent().requestDisallowInterceptTouchEvent(true);
            // return false;
            // }
            // });

            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //unresolved.setVisibility(View.VISIBLE);
                    } else {
                        // unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO




              /*  if (report.Closure_Status != null) {

                    if (report.Closure_Status.equals("Resolved")) {
                        // TODO
                        status.setSelection(0);
                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            unresolve_reason.setSelection(i);
                            other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }else
                        // {
                        // unresolve_reason.setSelection(9);other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // }
                    }
                }*/


        } else if (product_sub_category.contains("Mortise Lock")) {
            view = inflater.inflate(R.layout.locking_system, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }
            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);

            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);

            TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);

            btn_deleteVideo.setOnClickListener(this);
            address = (TextView) view.findViewById(R.id.address);

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            ml_chk_1 = (CheckBox) view.findViewById(R.id.ml_chk_1);
            ml_chk_2 = (CheckBox) view.findViewById(R.id.ml_chk_2);
            ml_chk_3 = (CheckBox) view.findViewById(R.id.ml_chk_3);
            ml_chk_4 = (CheckBox) view.findViewById(R.id.ml_chk_4);
            ml_chk_5 = (CheckBox) view.findViewById(R.id.ml_chk_5);
            ml_chk_6 = (CheckBox) view.findViewById(R.id.ml_chk_6);

            ml_chk_1.setOnCheckedChangeListener(this);
            ml_chk_2.setOnCheckedChangeListener(this);
            ml_chk_3.setOnCheckedChangeListener(this);
            ml_chk_4.setOnCheckedChangeListener(this);
            ml_chk_5.setOnCheckedChangeListener(this);
            ml_chk_6.setOnCheckedChangeListener(this);

            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);
            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            //*****************

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);


            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

            Button submit = (Button) view.findViewById(R.id.submit);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;

                    if (is_article_no) {
                        if (checkValidation(product_sub_category)) {
                          /*  if (spin_wrong_product.isEnabled()) {
                                if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                                    UtilityClass.showToast(context,
                                            "Select Wrong Product Reason");
                            }*/
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }
                        }
                    } else

                    {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                    }
                }
            });


            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);
            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);


            // if image data is not null attach images to gridview
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            // video_grid.setOnTouchListener(new OnTouchListener() {
            // // Setting on Touch Listener for handling the touch inside
            // // ScrollView
            // @Override
            // public boolean onTouch(View v, MotionEvent event) {
            // // Disallow the touch request for parent scroll on touch of
            // // child view
            // v.getParent().requestDisallowInterceptTouchEvent(true);
            // return false;
            // }
            // });

            video_grid.setOnClickListener(this);
            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });

            // video_grid.setOnItemClickListener(new OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,int
            // position, long id) {
            // Bitmap sBM = bmpArray.get(position);
            // Bitmap cBm = capturedBM.get(position);
            // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
            // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
            // showImage(scaledBM, cBm, sBM,position);
            // }
            // });

            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());

                    if (position == 1) {
                        // unresolved.setVisibility(View.VISIBLE);
                    } else {
                        // unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            // TODO
            if (report != null) {
                article_no.setText(report.article_no);
                if (report.Lock_Working != null) {
                    if (report.Lock_Working.equals("Y")) {
                        ml_chk_1.setChecked(true);
                        ml_chk_2.setChecked(false);
                    } else if (report.Lock_Working.equals("N")) {
                        ml_chk_1.setChecked(false);
                        ml_chk_2.setChecked(true);
                    } else {
                        ml_chk_1.setChecked(false);
                        ml_chk_2.setChecked(false);
                    }
                }

                if (report.wrong_product != null) {
                    if (report.wrong_product.equals("Y")) {
                        ml_chk_5.setChecked(true);
                        ml_chk_6.setChecked(false);
                    } else if (report.wrong_product.equals("N")) {
                        ml_chk_5.setChecked(false);
                        ml_chk_6.setChecked(true);
                    } else {
                        ml_chk_5.setChecked(false);
                        ml_chk_6.setChecked(false);
                    }
                }


                if (report.CutOut_Dimension != null) {
                    if (report.CutOut_Dimension.equals("Y")) {
                        ml_chk_3.setChecked(true);
                        ml_chk_4.setChecked(false);
                    } else if (report.CutOut_Dimension.equals("N")) {
                        ml_chk_3.setChecked(false);
                        ml_chk_4.setChecked(true);
                    } else {
                        ml_chk_3.setChecked(false);
                        ml_chk_4.setChecked(false);
                    }
                }


                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                    }
                }


                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }


                if (report.wrong_product_reason != null) {

                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                    if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                        ml_chk_1.setEnabled(false);
                        ml_chk_2.setEnabled(false);
                        ml_chk_3.setEnabled(false);
                        ml_chk_4.setEnabled(false);

                        if (ml_chk_5.isChecked()) {
                            spin_wrong_product.setEnabled(false);
                        } else {
                            spin_wrong_product.setEnabled(true);
                        }
                    } else {

                    }
                }


                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }
            }
        }


        //****************Komal


        else if (product_sub_category.contains("Tandem Box plus spacecorner boms")) {
            //Blum Corner Unit

            view = inflater.inflate(R.layout.new_blum_corner_unit, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }

            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            btn_deleteVideo.setOnClickListener(this);
            address = (TextView) view.findViewById(R.id.address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
            cabinet_width = (EditText) view.findViewById(R.id.cabinet_width);
            cabinet_depth = (EditText) view.findViewById(R.id.cabinet_depth);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);

            drawer_wt = (EditText) view.findViewById(R.id.drawer_wt);
            drawer_wt_tot = (EditText) view.findViewById(R.id.drawer_wt_tot);
            drawer_wt_tot.setEnabled(false);
            drawer_wt.setOnFocusChangeListener(this);
            drawer_wt_tot.setOnFocusChangeListener(this);
            stp_two = (TextView) view.findViewById(R.id.stp_two);

            stp_two.setVisibility(View.GONE);

            v1_w1 = (EditText) view.findViewById(R.id.v1_w1);
            v1_h1 = (EditText) view.findViewById(R.id.v1_h1);
            v1_t1 = (EditText) view.findViewById(R.id.v1_t1);
            v2_w2 = (EditText) view.findViewById(R.id.v2_w2);
            v2_h2 = (EditText) view.findViewById(R.id.v2_h2);
            v2_t2 = (EditText) view.findViewById(R.id.v2_t2);
            v3_w3 = (EditText) view.findViewById(R.id.v3_w3);
            v3_h3 = (EditText) view.findViewById(R.id.v3_h3);
            v3_t3 = (EditText) view.findViewById(R.id.v3_t3);
            v4_w4 = (EditText) view.findViewById(R.id.v4_w4);
            v4_h4 = (EditText) view.findViewById(R.id.v4_h4);
            v4_t4 = (EditText) view.findViewById(R.id.v4_t4);
            vol_one = (TextView) view.findViewById(R.id.volume_one);
            vol_two = (TextView) view.findViewById(R.id.volume_two);
            vol_three = (TextView) view.findViewById(R.id.volume_three);
            vol_four = (TextView) view.findViewById(R.id.volume_four);

            v1_w1.setOnFocusChangeListener(this);
            v1_h1.setOnFocusChangeListener(this);
            v1_t1.setOnFocusChangeListener(this);
            v2_w2.setOnFocusChangeListener(this);
            v2_h2.setOnFocusChangeListener(this);
            v2_t2.setOnFocusChangeListener(this);
            v3_w3.setOnFocusChangeListener(this);
            v3_h3.setOnFocusChangeListener(this);
            v3_t3.setOnFocusChangeListener(this);
            v4_w4.setOnFocusChangeListener(this);
            v4_h4.setOnFocusChangeListener(this);
            v4_t4.setOnFocusChangeListener(this);

            vc_chk_11 = (CheckBox) view.findViewById(R.id.vc_chk_11);
            vc_chk_22 = (CheckBox) view.findViewById(R.id.vc_chk_22);
            blcr_chk_1 = (CheckBox) view.findViewById(R.id.blcr_chk_1);
            blcr_chk_2 = (CheckBox) view.findViewById(R.id.blcr_chk_2);
            blcr_chk_3 = (CheckBox) view.findViewById(R.id.blcr_chk_3);
            blcr_chk_4 = (CheckBox) view.findViewById(R.id.blcr_chk_4);
            blcr_chk_5 = (CheckBox) view.findViewById(R.id.blcr_chk_5);
            blcr_chk_6 = (CheckBox) view.findViewById(R.id.blcr_chk_6);
            blcr_chk_7 = (CheckBox) view.findViewById(R.id.blcr_chk_7);
            blcr_chk_8 = (CheckBox) view.findViewById(R.id.blcr_chk_8);
            blcr_chk_9 = (CheckBox) view.findViewById(R.id.blcr_chk_9);
            blcr_chk_10 = (CheckBox) view.findViewById(R.id.blcr_chk_10);
            blcr_chk_11 = (CheckBox) view.findViewById(R.id.blcr_chk_11);
            blcr_chk_12 = (CheckBox) view.findViewById(R.id.blcr_chk_12);
            blcr_chk_13 = (CheckBox) view.findViewById(R.id.blcr_chk_13);
            blcr_chk_14 = (CheckBox) view.findViewById(R.id.blcr_chk_14);
            blcr_chk_15 = (CheckBox) view.findViewById(R.id.blcr_chk_15);
            blcr_chk_16 = (CheckBox) view.findViewById(R.id.blcr_chk_16);
            blcr_chk_17 = (CheckBox) view.findViewById(R.id.blcr_chk_17);
            blcr_chk_18 = (CheckBox) view.findViewById(R.id.blcr_chk_18);
            blcr_chk_19 = (CheckBox) view.findViewById(R.id.blcr_chk_19);
            blcr_chk_20 = (CheckBox) view.findViewById(R.id.blcr_chk_20);
            blcr_chk_21 = (CheckBox) view.findViewById(R.id.blcr_chk_21);
            blcr_chk_22 = (CheckBox) view.findViewById(R.id.blcr_chk_22);

            vc_chk_11.setOnCheckedChangeListener(this);
            vc_chk_22.setOnCheckedChangeListener(this);
            blcr_chk_1.setOnCheckedChangeListener(this);
            blcr_chk_2.setOnCheckedChangeListener(this);
            blcr_chk_3.setOnCheckedChangeListener(this);
            blcr_chk_4.setOnCheckedChangeListener(this);
            blcr_chk_5.setOnCheckedChangeListener(this);
            blcr_chk_6.setOnCheckedChangeListener(this);
            blcr_chk_7.setOnCheckedChangeListener(this);
            blcr_chk_8.setOnCheckedChangeListener(this);
            blcr_chk_10.setOnCheckedChangeListener(this);
            blcr_chk_11.setOnCheckedChangeListener(this);
            blcr_chk_12.setOnCheckedChangeListener(this);
            blcr_chk_13.setOnCheckedChangeListener(this);
            blcr_chk_14.setOnCheckedChangeListener(this);
            blcr_chk_15.setOnCheckedChangeListener(this);
            blcr_chk_16.setOnCheckedChangeListener(this);
            blcr_chk_17.setOnCheckedChangeListener(this);
            blcr_chk_18.setOnCheckedChangeListener(this);
            blcr_chk_19.setOnCheckedChangeListener(this);
            blcr_chk_20.setOnCheckedChangeListener(this);
            blcr_chk_21.setOnCheckedChangeListener(this);
            blcr_chk_22.setOnCheckedChangeListener(this);

            /*final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);
            final LinearLayout other = (LinearLayout) view.findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);
            /*
            unresolve_reason.setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/
            // if image data is not null attach images to gridview
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);

            Button submit = (Button) view.findViewById(R.id.submit);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;


                    if (is_article_no) {
                        if (checkValidation(product_sub_category)) {
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }

                            /*if (unresolve_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                                UtilityClass.showToast(context,
                                        "Select Result");
                            if (spin_action.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                                UtilityClass.showToast(context,
                                        "Select Action");
                            else
                                submitData(product_sub_category);*/
                        }
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");

                    }
                }
            });

            //submit.setOnClickListener(this);

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {

                article_no.setText(report.article_no);
                cabinet_width.setText(report.Width);
                cabinet_height.setText(report.Height);
                cabinet_depth.setText(report.Thickness);


                v1_w1.setText(report.v1_w1);
                v2_w2.setText(report.v2_w2);
                v3_w3.setText(report.v3_w3);
                v4_w4.setText(report.v4_w4);

                v1_h1.setText(report.v1_h1);
                v2_h2.setText(report.v2_h2);
                v3_h3.setText(report.v3_h3);
                v4_h4.setText(report.v4_h4);

                v1_t1.setText(report.v1_t1);
                v2_t2.setText(report.v2_t2);
                v3_t3.setText(report.v3_t3);
                v4_t4.setText(report.v4_t4);

                vol_one.setText(report.side_volume);
                vol_two.setText(report.back_volume);
                vol_three.setText(report.base_volume);
                vol_four.setText(report.facia_volume);

               /* if (report.Drawer_Wt != null) {
                    if (report.Drawer_Wt.equals("null")) {

                    } else {
                        try {
                            float wt = Float.parseFloat(report.Drawer_Wt);
                            drawer_wt.setText(String.valueOf(wt / 700));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
*/
                // float wt =
                // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                // drawer_wt_tot.setText(String.valueOf(wt));

                // drawer_wt_tot.setText(report.Drawer_Wt);


                if (report.Drawer_Wt != null) {
                    if (report.Drawer_Wt.equals("null")) {

                    } else {
                        try {
                            //       float wt = Float.parseFloat(report.Drawer_Wt);
                            //     drawer_wt_tot.setText(String.valueOf(wt / 700));
                            drawer_wt_tot.setText(report.Drawer_Wt);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (report.size_of_the_cabinet_is_more != null) {
                    if (report.size_of_the_cabinet_is_more.equals("Y")) {
                        blcr_chk_1.setChecked(true);
                        blcr_chk_2.setChecked(false);
                    } else if (report.size_of_the_cabinet_is_more.equals("N")) {
                        blcr_chk_1.setChecked(false);
                        blcr_chk_2.setChecked(true);
                    } else {
                        blcr_chk_1.setChecked(false);
                        blcr_chk_2.setChecked(false);
                    }
                }
                if (report.fitting_templete != null) {
                    if (report.fitting_templete.equals("Y")) {
                        blcr_chk_3.setChecked(true);
                        blcr_chk_4.setChecked(false);
                    } else if (report.fitting_templete.equals("N")) {
                        blcr_chk_3.setChecked(false);
                        blcr_chk_4.setChecked(true);
                    } else {
                        blcr_chk_3.setChecked(false);
                        blcr_chk_4.setChecked(false);
                    }
                }
                if (report.front_adjustments != null) {
                    if (report.front_adjustments.equals("Y")) {
                        blcr_chk_5.setChecked(true);
                        blcr_chk_6.setChecked(false);
                    } else if (report.front_adjustments.equals("N")) {
                        blcr_chk_5.setChecked(false);
                        blcr_chk_6.setChecked(true);
                    } else {
                        blcr_chk_5.setChecked(false);
                        blcr_chk_6.setChecked(false);
                    }
                }
                if (report.correct_alignment != null) {
                    if (report.correct_alignment.equals("Y")) {
                        blcr_chk_7.setChecked(true);
                        blcr_chk_8.setChecked(false);
                    } else if (report.correct_alignment.equals("N")) {
                        blcr_chk_7.setChecked(false);
                        blcr_chk_8.setChecked(true);
                    } else {
                        blcr_chk_7.setChecked(false);
                        blcr_chk_8.setChecked(false);
                    }
                }
                if (report.correct_no_of_screws != null) {
                    if (report.correct_no_of_screws.equals("Y")) {
                        blcr_chk_9.setChecked(true);
                        blcr_chk_10.setChecked(false);
                    } else if (report.correct_no_of_screws.equals("N")) {
                        blcr_chk_9.setChecked(false);
                        blcr_chk_10.setChecked(true);
                    } else {
                        blcr_chk_9.setChecked(false);
                        blcr_chk_10.setChecked(false);
                    }
                }
                if (report.correct_spacing != null) {
                    if (report.correct_spacing.equals("Y")) {
                        blcr_chk_11.setChecked(true);
                        blcr_chk_12.setChecked(false);
                    } else if (report.correct_spacing.equals("N")) {
                        blcr_chk_11.setChecked(false);
                        blcr_chk_12.setChecked(true);
                    } else {
                        blcr_chk_11.setChecked(false);
                        blcr_chk_12.setChecked(false);
                    }
                }
                if (report.Empty_the_drawer != null) {
                    if (report.Empty_the_drawer.equals("Y")) {
                        blcr_chk_13.setChecked(true);
                        blcr_chk_14.setChecked(false);
                    } else if (report.Empty_the_drawer.equals("N")) {
                        blcr_chk_13.setChecked(false);
                        blcr_chk_14.setChecked(true);
                    } else {
                        blcr_chk_13.setChecked(false);
                        blcr_chk_14.setChecked(false);
                    }
                }
                if (report.dust_gathers != null) {
                    if (report.dust_gathers.equals("Y")) {
                        blcr_chk_15.setChecked(true);
                        blcr_chk_16.setChecked(false);
                    } else if (report.dust_gathers.equals("N")) {
                        blcr_chk_15.setChecked(false);
                        blcr_chk_16.setChecked(true);
                    } else {
                        blcr_chk_15.setChecked(false);
                        blcr_chk_16.setChecked(false);
                    }
                }
                if (report.servodrive_sufficient_to_handle_weight != null) {
                    if (report.servodrive_sufficient_to_handle_weight.equals("Y")) {
                        blcr_chk_17.setChecked(true);
                        blcr_chk_18.setChecked(false);
                    } else if (report.servodrive_sufficient_to_handle_weight.equals("N")) {
                        blcr_chk_17.setChecked(false);
                        blcr_chk_18.setChecked(true);
                    } else {
                        blcr_chk_17.setChecked(false);
                        blcr_chk_18.setChecked(false);
                    }
                }
                if (report.third_hinge_is_used != null) {
                    if (report.third_hinge_is_used.equals("Y")) {
                        blcr_chk_19.setChecked(true);
                        blcr_chk_20.setChecked(false);
                    } else if (report.third_hinge_is_used.equals("N")) {
                        blcr_chk_19.setChecked(false);
                        blcr_chk_20.setChecked(true);
                    } else {
                        blcr_chk_19.setChecked(false);
                        blcr_chk_20.setChecked(false);
                    }
                }
                if (report.synchro_motion_working_properly != null) {
                    if (report.synchro_motion_working_properly.equals("Y")) {
                        blcr_chk_21.setChecked(true);
                        blcr_chk_22.setChecked(false);
                    } else if (report.synchro_motion_working_properly.equals("N")) {
                        blcr_chk_21.setChecked(false);
                        blcr_chk_22.setChecked(true);
                    } else {
                        blcr_chk_21.setChecked(false);
                        blcr_chk_22.setChecked(false);
                    }
                }

                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }


                if (report.wrong_product_reason != null) {

                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                    if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                        blcr_chk_5.setEnabled(false);
                        blcr_chk_6.setEnabled(false);
                        blcr_chk_7.setEnabled(false);
                        blcr_chk_8.setEnabled(false);
                        blcr_chk_9.setEnabled(false);
                        blcr_chk_10.setEnabled(false);
                        blcr_chk_11.setEnabled(false);
                        blcr_chk_12.setEnabled(false);
                        blcr_chk_13.setEnabled(false);
                        blcr_chk_14.setEnabled(false);
                        blcr_chk_15.setEnabled(false);
                        blcr_chk_16.setEnabled(false);
                        blcr_chk_17.setEnabled(false);
                        blcr_chk_18.setEnabled(false);
                        blcr_chk_19.setEnabled(false);
                        blcr_chk_20.setEnabled(false);
                        blcr_chk_21.setEnabled(false);
                        blcr_chk_22.setEnabled(false);
                        vc_chk_11.setEnabled(false);
                        vc_chk_22.setEnabled(false);
                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                        if (blcr_chk_3.isChecked()) {
                            spin_wrong_product.setEnabled(false);
                        } else {
                            spin_wrong_product.setEnabled(true);
                        }
                    } else {

                    }
                }

                if (report.Weight_Length_Correct != null) {
                    if (report.Weight_Length_Correct.equals("Y")) {
                        vc_chk_11.setChecked(true);
                        vc_chk_22.setChecked(false);
                    } else if (report.Weight_Length_Correct.equals("N")) {
                        vc_chk_11.setChecked(false);
                        vc_chk_22.setChecked(true);

                        blcr_chk_17.setChecked(false);
                        blcr_chk_18.setChecked(false);
                        blcr_chk_19.setChecked(false);
                        blcr_chk_20.setChecked(false);
                        blcr_chk_21.setChecked(false);
                        blcr_chk_22.setChecked(false);

                        blcr_chk_17.setEnabled(false);
                        blcr_chk_18.setEnabled(false);
                        blcr_chk_19.setEnabled(false);
                        blcr_chk_20.setEnabled(false);
                        blcr_chk_21.setEnabled(false);
                        blcr_chk_22.setEnabled(false);
                    } else {
                        vc_chk_11.setChecked(false);
                        vc_chk_22.setChecked(false);
                    }
                }
                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        // unresolved.setVisibility(View.VISIBLE);

                      /*  int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            //     other.setVisibility(View.VISIBLE);
                            unresolve_reason.setSelection(i);
                         //   other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }*/
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }

                    comments.setText(report.Comment);

                }
            }


            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            video_grid.setOnClickListener(this);

            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //unresolved.setVisibility(View.VISIBLE);
                    } else {
                        // unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


        } else if (product_category.contains("Corner Solution")) {
            //Corner Unit

            view = inflater.inflate(R.layout.new_corner_unit, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }

            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            address = (TextView) view.findViewById(R.id.address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            btn_deleteVideo.setOnClickListener(this);

            cu_chk_1 = (CheckBox) view.findViewById(R.id.cu_chk_1);
            cu_chk_2 = (CheckBox) view.findViewById(R.id.cu_chk_2);
            cu_chk_3 = (CheckBox) view.findViewById(R.id.cu_chk_3);
            cu_chk_4 = (CheckBox) view.findViewById(R.id.cu_chk_4);
            cu_chk_5 = (CheckBox) view.findViewById(R.id.cu_chk_5);
            cu_chk_6 = (CheckBox) view.findViewById(R.id.cu_chk_6);
            cu_chk_7 = (CheckBox) view.findViewById(R.id.cu_chk_7);
            cu_chk_8 = (CheckBox) view.findViewById(R.id.cu_chk_8);
            cu_chk_9 = (CheckBox) view.findViewById(R.id.cu_chk_9);
            cu_chk_10 = (CheckBox) view.findViewById(R.id.cu_chk_10);
            cu_chk_11 = (CheckBox) view.findViewById(R.id.cu_chk_11);
            cu_chk_12 = (CheckBox) view.findViewById(R.id.cu_chk_12);
            cu_chk_13 = (CheckBox) view.findViewById(R.id.cu_chk_13);
            cu_chk_14 = (CheckBox) view.findViewById(R.id.cu_chk_14);

            cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
            cabinet_width = (EditText) view.findViewById(R.id.cabinet_width);
            cabinet_depth = (EditText) view.findViewById(R.id.cabinet_depth);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);


            cu_chk_1.setOnCheckedChangeListener(this);
            cu_chk_2.setOnCheckedChangeListener(this);
            cu_chk_3.setOnCheckedChangeListener(this);
            cu_chk_4.setOnCheckedChangeListener(this);
            cu_chk_5.setOnCheckedChangeListener(this);
            cu_chk_6.setOnCheckedChangeListener(this);
            cu_chk_7.setOnCheckedChangeListener(this);
            cu_chk_8.setOnCheckedChangeListener(this);
            cu_chk_9.setOnCheckedChangeListener(this);
            cu_chk_10.setOnCheckedChangeListener(this);
            cu_chk_11.setOnCheckedChangeListener(this);
            cu_chk_12.setOnCheckedChangeListener(this);
            cu_chk_13.setOnCheckedChangeListener(this);
            cu_chk_14.setOnCheckedChangeListener(this);


            // final LinearLayout unresolved = (LinearLayout) view.findViewById(R.id.unresolved);
            //  unresolved.setVisibility(View.GONE);
            //  final LinearLayout other = (LinearLayout) view.findViewById(R.id.others);
            //  other.setVisibility(View.GONE);
            //  other_reason = (EditText) view.findViewById(R.id.other_reason);

          /*  spin_action.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });*/


            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);


            /*unresolve_reason.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int arg2, long arg3) {
                    if (unresolve_reason.getSelectedItem().toString()
                            .equals("Product Defect")) {
                        ll_product_defect.setVisibility(View.VISIBLE);
                        ll_site_issue.setVisibility(View.GONE);

                    } else if (unresolve_reason.getSelectedItem().toString()
                            .equals("Site Issues")) {
                        ll_product_defect.setVisibility(View.GONE);
                        ll_site_issue.setVisibility(View.VISIBLE);
                        //    other_reason.setText("null");
                               *//* contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());*//*
                    } else {
                        ll_product_defect.setVisibility(View.GONE);
                        ll_site_issue.setVisibility(View.GONE);
                              *//*  contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());*//*
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });*/
            // if image data is not null attach images to gridview
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);

            Button submit = (Button) view.findViewById(R.id.submit);


            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;

                    if (is_article_no) {
                        if (checkValidation(product_category)) {
                          /*  if (spin_wrong_product.isEnabled()) {
                                if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                                    UtilityClass.showToast(context, "Select Wrong Product Reason");
                            }*/
                            if (validation() == true) {
                                submitData(product_category);
                            }
                        }
                    } else

                    {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");

                    }
                }
            });

            //  submit.setOnClickListener(this);

            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {

                article_no.setText(report.article_no);
                cabinet_width.setText(report.Width);
                cabinet_height.setText(report.Height);
                cabinet_depth.setText(report.Thickness);


                if (report.part_list_is_complete != null) {
                    if (report.part_list_is_complete.equals("Y")) {
                        cu_chk_1.setChecked(true);
                        cu_chk_2.setChecked(false);
                        spin_wrong_product.setEnabled(false);
                    } else if (report.part_list_is_complete.equals("N")) {
                        //  cu_chk_1.setChecked(false);
                        cu_chk_2.setChecked(true);

                    } else {
                        cu_chk_1.setChecked(false);
                        cu_chk_2.setChecked(false);

                    }
                }
                if (report.correct_fitting_order != null) {
                    if (report.correct_fitting_order.equals("Y")) {
                        cu_chk_3.setChecked(true);
                        cu_chk_4.setChecked(false);
                    } else if (report.correct_fitting_order.equals("N")) {
                        cu_chk_3.setChecked(false);
                        cu_chk_4.setChecked(true);
                    } else {
                        cu_chk_3.setChecked(false);
                        cu_chk_4.setChecked(false);
                    }
                }
                if (report.size_of_the_cabinet_is_more != null) {
                    if (report.size_of_the_cabinet_is_more.equals("Y")) {
                        cu_chk_5.setChecked(true);
                        cu_chk_6.setChecked(false);
                    } else if (report.size_of_the_cabinet_is_more.equals("N")) {
                        cu_chk_5.setChecked(false);
                        cu_chk_6.setChecked(true);
                    } else {
                        cu_chk_5.setChecked(false);
                        cu_chk_6.setChecked(false);
                    }
                }
                if (report.door_dimensions != null) {
                    if (report.door_dimensions.equals("Y")) {
                        cu_chk_7.setChecked(true);
                        cu_chk_8.setChecked(false);
                    } else if (report.door_dimensions.equals("N")) {
                        cu_chk_7.setChecked(false);
                        cu_chk_8.setChecked(true);
                    } else {
                        cu_chk_7.setChecked(false);
                        cu_chk_8.setChecked(false);
                    }
                }
                if (report.installation_template != null) {
                    if (report.installation_template.equals("Y")) {
                        cu_chk_9.setChecked(true);
                        cu_chk_10.setChecked(false);
                    } else if (report.installation_template.equals("N")) {
                        cu_chk_9.setChecked(false);
                        cu_chk_10.setChecked(true);
                    } else {
                        cu_chk_9.setChecked(false);
                        cu_chk_10.setChecked(false);
                    }
                }
                if (report.plinth_legs != null) {
                    if (report.plinth_legs.equals("Y")) {
                        cu_chk_11.setChecked(true);
                        cu_chk_12.setChecked(false);
                    } else if (report.plinth_legs.equals("N")) {
                        cu_chk_11.setChecked(false);
                        cu_chk_12.setChecked(true);
                    } else {
                        cu_chk_11.setChecked(false);
                        cu_chk_12.setChecked(false);
                    }
                }
                if (report.check_hinges != null) {
                    if (report.check_hinges.equals("Y")) {
                        cu_chk_13.setChecked(true);
                        cu_chk_14.setChecked(false);
                    } else if (report.check_hinges.equals("N")) {
                        cu_chk_13.setChecked(false);
                        cu_chk_14.setChecked(true);
                    } else {
                        cu_chk_13.setChecked(false);
                        cu_chk_14.setChecked(false);
                    }
                }

                if (report.Result != null) {

                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);

                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }
                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }
                if (report.wrong_product_reason != null) {

                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                    if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                        cu_chk_3.setEnabled(false);
                        cu_chk_4.setEnabled(false);
                        cu_chk_5.setEnabled(false);
                        cu_chk_6.setEnabled(false);
                        cu_chk_7.setEnabled(false);
                        cu_chk_8.setEnabled(false);
                        cu_chk_9.setEnabled(false);
                        cu_chk_10.setEnabled(false);
                        cu_chk_11.setEnabled(false);
                        cu_chk_12.setEnabled(false);
                        cu_chk_13.setEnabled(false);
                        cu_chk_14.setEnabled(false);

                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                        if (cu_chk_1.isChecked()) {
                            spin_wrong_product.setEnabled(false);
                        } else {
                            spin_wrong_product.setEnabled(true);
                        }
                    } else {

                    }
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        // unresolved.setVisibility(View.VISIBLE);

                      /*  int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            //     other.setVisibility(View.VISIBLE);
                            unresolve_reason.setSelection(i);
                         //   other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }
*/
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }

                    comments.setText(report.Comment);

                }
            }

            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            video_grid.setOnClickListener(this);

            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


        } else if (product_sub_category.contains("Ironing Board")) {

            //  Ironing Board
            view = inflater.inflate(R.layout.new_ironing_board, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }

            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            btn_deleteVideo.setOnClickListener(this);
            address = (TextView) view.findViewById(R.id.address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

            cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
            cabinet_width = (EditText) view.findViewById(R.id.cabinet_height);
            cabinet_depth = (EditText) view.findViewById(R.id.cabinet_depth);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);

            ib_chk_1 = (CheckBox) view.findViewById(R.id.ib_chk_1);
            ib_chk_2 = (CheckBox) view.findViewById(R.id.ib_chk_2);
            ib_chk_3 = (CheckBox) view.findViewById(R.id.ib_chk_3);
            ib_chk_4 = (CheckBox) view.findViewById(R.id.ib_chk_4);
            ib_chk_5 = (CheckBox) view.findViewById(R.id.ib_chk_5);
            ib_chk_6 = (CheckBox) view.findViewById(R.id.ib_chk_6);
            ib_chk_7 = (CheckBox) view.findViewById(R.id.ib_chk_7);
            ib_chk_8 = (CheckBox) view.findViewById(R.id.ib_chk_8);
            ib_chk_9 = (CheckBox) view.findViewById(R.id.ib_chk_9);
            ib_chk_10 = (CheckBox) view.findViewById(R.id.ib_chk_10);
            ib_chk_11 = (CheckBox) view.findViewById(R.id.ib_chk_11);

            cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
            cabinet_width = (EditText) view.findViewById(R.id.cabinet_width);
            cabinet_depth = (EditText) view.findViewById(R.id.cabinet_depth);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);

            ib_chk_1.setOnCheckedChangeListener(this);
            ib_chk_2.setOnCheckedChangeListener(this);
            ib_chk_3.setOnCheckedChangeListener(this);
            ib_chk_4.setOnCheckedChangeListener(this);
            ib_chk_5.setOnCheckedChangeListener(this);
            ib_chk_6.setOnCheckedChangeListener(this);
            ib_chk_7.setOnCheckedChangeListener(this);
            ib_chk_8.setOnCheckedChangeListener(this);
            ib_chk_9.setOnCheckedChangeListener(this);
            ib_chk_10.setOnCheckedChangeListener(this);
            ib_chk_11.setOnCheckedChangeListener(this);

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

            // if image data is not null attach images to gridview
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);

            Button submit = (Button) view.findViewById(R.id.submit);

            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;


                    if (is_article_no) {

                        if (checkValidation(product_sub_category)) {
                            if (validation() == true) {
                                submitData(product_sub_category);
                            }
                        }
                    } else

                    {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");

                    }
                }


            });


            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {

                article_no.setText(report.article_no);
                cabinet_width.setText(report.Width);
                cabinet_height.setText(report.Height);
                cabinet_depth.setText(report.Thickness);

                if (report.part_list_is_complete != null) {
                    if (report.part_list_is_complete.equals("Y")) {
                        ib_chk_1.setChecked(true);
                        ib_chk_2.setChecked(false);
                    } else if (report.part_list_is_complete.equals("N")) {
                        ib_chk_1.setChecked(false);
                        ib_chk_2.setChecked(true);
                    } else {
                        ib_chk_1.setChecked(false);
                        ib_chk_2.setChecked(false);
                    }
                }

                if (report.correct_product_order != null) {
                    if (report.correct_product_order.equals("Y")) {
                        ib_chk_3.setChecked(true);
                        ib_chk_4.setChecked(false);
                    } else if (report.correct_product_order.equals("N")) {
                        ib_chk_3.setChecked(false);
                        ib_chk_4.setChecked(true);
                    } else {
                        ib_chk_3.setChecked(false);
                        ib_chk_4.setChecked(false);
                    }
                }

                if (report.installation_template != null) {
                    if (report.installation_template.equals("Y")) {
                        ib_chk_5.setChecked(true);
                        ib_chk_6.setChecked(false);
                    } else if (report.installation_template.equals("N")) {
                        ib_chk_5.setChecked(false);
                        ib_chk_6.setChecked(true);
                    } else {
                        ib_chk_5.setChecked(false);
                        ib_chk_6.setChecked(false);
                    }
                }

                if (report.type_of_runner != null) {
                    if (report.type_of_runner.equals("Full extension")) {
                        ib_chk_7.setChecked(true);
                        ib_chk_8.setChecked(false);
                        ib_chk_9.setChecked(false);
                    } else if (report.type_of_runner.equals("Single extension")) {
                        ib_chk_8.setChecked(true);
                        ib_chk_7.setChecked(false);
                        ib_chk_9.setChecked(false);
                    } else if (report.type_of_runner.equals("No runners used")) {
                        ib_chk_7.setChecked(false);
                        ib_chk_8.setChecked(false);
                        ib_chk_9.setChecked(true);
                    }
                }

                if (report.product_abuse != null) {
                    if (report.product_abuse.equals("Y")) {
                        ib_chk_10.setChecked(true);
                        ib_chk_11.setChecked(false);
                    } else if (report.product_abuse.equals("N")) {
                        ib_chk_10.setChecked(false);
                        ib_chk_11.setChecked(true);
                    } else {
                        ib_chk_10.setChecked(false);
                        ib_chk_11.setChecked(false);
                    }
                }

                if (report.Result != null) {
                    int index = resultList.indexOf(report.Result);
                    if (index > 0) {

                        if (index == 2) {
                            unresolve_reason.setSelection(2);
                            ll_product_defect.setVisibility(View.VISIBLE);
                            ll_site_issue.setVisibility(View.GONE);
                            edt_spare_defect_articleNo.setText(report.sparce_defect);
                            edt_complete_set_articleNo.setText(report.complete_set);
                        } else if (index == 3) {
                            unresolve_reason.setSelection(3);
                            ll_product_defect.setVisibility(View.GONE);
                            ll_site_issue.setVisibility(View.VISIBLE);

                            if (report.site_Issue_Reason != null) {
                                int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                if (index1 > 0) {
                                    spin_siteIssueReason_reason.setSelection(index1);
                                }
                            }
                        } else if (index == 1) {
                            unresolve_reason.setSelection(1);
                        } else if (index == 4) {
                            unresolve_reason.setSelection(4);
                        }
                    } else {
                        unresolve_reason.setSelection(0);
                    }

                }
                if (report.Action != null) {
                    int index = actionList.indexOf(report.Action);
                    spin_action.setSelection(index);
                }
                if (report.wrong_product_reason != null) {

                    int index = wrongProdList.indexOf(report.wrong_product_reason);
                    spin_wrong_product.setSelection(index);

                    if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {

                        ib_chk_3.setEnabled(false);
                        ib_chk_4.setEnabled(false);
                        ib_chk_5.setEnabled(false);
                        ib_chk_6.setEnabled(false);
                        ib_chk_7.setEnabled(false);
                        ib_chk_8.setEnabled(false);
                        ib_chk_9.setEnabled(false);
                        ib_chk_10.setEnabled(false);
                        ib_chk_11.setEnabled(false);
                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                        spin_wrong_product.setEnabled(true);
                    } else {

                    }
                }

                if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else if (report.Closure_Status.equals("Unresolved")) {
                        status.setSelection(1);
                        // unresolved.setVisibility(View.VISIBLE);

                      /*  int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            //     other.setVisibility(View.VISIBLE);
                            unresolve_reason.setSelection(i);
                         //   other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }*/
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }

                    comments.setText(report.Comment);

                }
            }
            if (imgData != null) {
                for (int i = 0; i < imgData.size(); i++) {
                    File file = new File(imgData.get(i).image_path);
                    orgFileArray.add(file);
                    fileNameArray.add(file);

                    imgOriginalSize.add(imgData.get(i).original_size);
                    imgCompressedSize.add(imgData.get(i).compressed_size);

                    compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                    capturedBM.add(compressedBitmap);
                    int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                            .getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(
                            compressedBitmap, 100, nh, true);
                    bmpArray.add(scaledBM);
                }
            }

            if (vidData != null) {
                try {
                    // inputpath = vidData.FilePath;
                    Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                            vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                    inputpath = vidData.get(0).FilePath;
                    video_grid.setImageBitmap(bmThumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
            img_grid.setAdapter(imgAdapter);
            img_grid.setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside
                // ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of
                    // child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            video_grid.setOnClickListener(this);

            img_grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Bitmap sBM = bmpArray.get(position);
                    Bitmap cBm = capturedBM.get(position);
                    int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                    Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                            true);
                    showImage(scaledBM, cBm, sBM, position);
                }
            });
            status.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    contentValues.put("Closure_Status", status
                            .getSelectedItem().toString());
                    if (position == 1) {
                        //  unresolved.setVisibility(View.VISIBLE);
                    } else {
                        //  unresolved.setVisibility(View.GONE);
                        contentValues.put("Reason_For_Unresolved", "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        } else if (product_sub_category.contains("Bedroom Fittings") || product_sub_category.equalsIgnoreCase("Bedroom Fittings")) {
            //Pratik
            view = inflater.inflate(R.layout.new_pratik_tavoletto, null);
            baseFrame.removeAllViews();
            baseFrame.addView(view);
            prod_type = "null";

            FaultReport report = null;
            List<ImageData> imgData = null;
            List<VideoData> vidData = null;

            if (is_visited > 0) {
                report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                        complaint_number);
                imgData = dbAdapter.getImageData(complaint_number, "ALL");
                vidData = dbAdapter.getVideoData(complaint_number);
            }

            ll_pratik = (LinearLayout) view.findViewById(R.id.ll_pratik);
            ll_tavoletto = (LinearLayout) view.findViewById(R.id.ll_tavoletto);
            bottom_linearll = (LinearLayout) view.findViewById(R.id.bottom_linearll);
            radio_group = (RadioGroup) view.findViewById(R.id.radio_group1);
            radio_tavoletto = (RadioButton) view.findViewById(R.id.radio_tavoletto);
            radio_pratik = (RadioButton) view.findViewById(R.id.radio_pratik);
            radio_other = (RadioButton) view.findViewById(R.id.radio_other);

            if (report != null) {
                if (report.prod_type != null) {
                    if (report.prod_type.equals("Pratik")) {
                        radio_pratik.setChecked(true);
                        radio_tavoletto.setEnabled(false);
                        ll_pratik.setVisibility(View.VISIBLE);
                        radio_other.setChecked(false);
                        radio_other.setEnabled(false);
                    } else if (report.prod_type.equals("Tavoletto")) {
                        radio_tavoletto.setChecked(true);
                        ll_tavoletto.setVisibility(View.VISIBLE);
                        radio_pratik.setEnabled(false);
                        radio_other.setChecked(false);
                        radio_other.setEnabled(false);
                    } else if (report.prod_type.equals("Other")) {
                        radio_tavoletto.setChecked(false);
                        radio_tavoletto.setEnabled(false);
                        radio_pratik.setEnabled(false);
                        radio_pratik.setChecked(false);
                        radio_other.setChecked(true);
                    }
                }
            }


            comp_number = (TextView) view.findViewById(R.id.complaint_number);
            customer_name = (TextView) view.findViewById(R.id.customer_name);
            date = (TextView) view.findViewById(R.id.date);
            mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            dealer = (TextView) view.findViewById(R.id.dealer);
            invoice_no = (TextView) view.findViewById(R.id.invoice_no);
            article_no = (EditText) view.findViewById(R.id.article_no);
            prod_cat = (TextView) view.findViewById(R.id.prod_cat);
            prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
            complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
            btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
            btn_deleteVideo.setOnClickListener(this);
            address = (TextView) view.findViewById(R.id.address);

            spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
            spin_action = (Spinner) view.findViewById(R.id.spin_action);
            edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
            edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
            ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
            ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
            ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);


            pratik_height = (EditText) view.findViewById(R.id.pratik_height);
            pratik_length_50mm = (EditText) view.findViewById(R.id.pratik_length_50mm);
            pratik_width_30mm = (EditText) view.findViewById(R.id.pratik_width_30mm);
            pratik_sfd_dimen = (EditText) view.findViewById(R.id.pratik_sfd_dimen);
            pratik_sfd_dimen1 = (EditText) view.findViewById(R.id.pratik_sfd_dimen1);
            spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);

            pr_chk_1 = (CheckBox) view.findViewById(R.id.pr_chk_1);
            pr_chk_2 = (CheckBox) view.findViewById(R.id.pr_chk_2);
            pr_chk_3 = (CheckBox) view.findViewById(R.id.pr_chk_3);
            pr_chk_4 = (CheckBox) view.findViewById(R.id.pr_chk_4);
            pr_chk_5 = (CheckBox) view.findViewById(R.id.pr_chk_5);
            pr_chk_6 = (CheckBox) view.findViewById(R.id.pr_chk_6);
            pr_chk_7 = (CheckBox) view.findViewById(R.id.pr_chk_7);
            pr_chk_8 = (CheckBox) view.findViewById(R.id.pr_chk_8);
            pr_chk_9 = (CheckBox) view.findViewById(R.id.pr_chk_9);
            pr_chk_10 = (CheckBox) view.findViewById(R.id.pr_chk_10);
            pr_chk_11 = (CheckBox) view.findViewById(R.id.pr_chk_11);
            pr_chk_12 = (CheckBox) view.findViewById(R.id.pr_chk_12);
            pr_chk_13 = (CheckBox) view.findViewById(R.id.pr_chk_13);
            pr_chk_14 = (CheckBox) view.findViewById(R.id.pr_chk_14);
            pr_chk_15 = (CheckBox) view.findViewById(R.id.pr_chk_15);

            pr_chk_1.setOnCheckedChangeListener(this);
            pr_chk_2.setOnCheckedChangeListener(this);
            pr_chk_3.setOnCheckedChangeListener(this);
            pr_chk_4.setOnCheckedChangeListener(this);
            pr_chk_5.setOnCheckedChangeListener(this);
            pr_chk_6.setOnCheckedChangeListener(this);
            pr_chk_7.setOnCheckedChangeListener(this);
            pr_chk_8.setOnCheckedChangeListener(this);
            pr_chk_9.setOnCheckedChangeListener(this);
            pr_chk_10.setOnCheckedChangeListener(this);
            pr_chk_11.setOnCheckedChangeListener(this);
            pr_chk_12.setOnCheckedChangeListener(this);
            pr_chk_13.setOnCheckedChangeListener(this);
            pr_chk_14.setOnCheckedChangeListener(this);
            pr_chk_15.setOnCheckedChangeListener(this);


            cabinet_width = (EditText) view.findViewById(R.id.cabinet_width);
            cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
            spin_wrong_product1 = (Spinner) view.findViewById(R.id.spin_wrong_product1);

            tv_chk_1 = (CheckBox) view.findViewById(R.id.tv_chk_1);
            tv_chk_2 = (CheckBox) view.findViewById(R.id.tv_chk_2);
            tv_chk_3 = (CheckBox) view.findViewById(R.id.tv_chk_3);
            tv_chk_4 = (CheckBox) view.findViewById(R.id.tv_chk_4);
            tv_chk_5 = (CheckBox) view.findViewById(R.id.tv_chk_5);
            tv_chk_6 = (CheckBox) view.findViewById(R.id.tv_chk_6);
            tv_chk_7 = (CheckBox) view.findViewById(R.id.tv_chk_7);
            tv_chk_8 = (CheckBox) view.findViewById(R.id.tv_chk_8);

            tv_chk_1.setOnCheckedChangeListener(this);
            tv_chk_2.setOnCheckedChangeListener(this);
            tv_chk_3.setOnCheckedChangeListener(this);
            tv_chk_4.setOnCheckedChangeListener(this);
            tv_chk_5.setOnCheckedChangeListener(this);
            tv_chk_6.setOnCheckedChangeListener(this);
            tv_chk_7.setOnCheckedChangeListener(this);
            tv_chk_8.setOnCheckedChangeListener(this);

            unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
            unresolve_reason.setOnItemSelectedListener(this);

            imgAdapter = new ImageGridAdapter(context, fileNameArray);
            comp_number.setText(complaint_number);
            customer_name.setText(c_name);
            date.setText(complaint_date);
            mobile_no.setText(end_user_mobile);
            mobile_no.setOnClickListener(this);
            dealer.setText(service_franchise);
            article_no.setText(article);
            complaint_deatils.setText(service_details);
            prod_cat.setText(product_category);
            prod_sub_cat.setText(product_sub_category);
            address.setText(c_address);

            status = (Spinner) view.findViewById(R.id.status);
            comments = (TextView) view.findViewById(R.id.comments);
            Button submit = (Button) view.findViewById(R.id.submit);


            submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean is_article_no = Validation.hasText(article_no);
                    if (article_no.getText().toString().trim().equals(""))
                        is_article_no = false;


                    if (is_article_no) {

                        if (radio_pratik.isChecked()) {
                            if (checkValidation("Pratik")) {
                                if (validation() == true) {
                                    submitData("Pratik");
                                }
                            }
                        } else if (radio_tavoletto.isChecked()) {
                            if (checkValidation("Tavoletto")) {
                                if (validation() == true) {
                                    submitData("Tavoletto");
                                }
                            }
                        } else if (radio_other.isChecked()) {

                            if (validation() == true) {
                                submitData("general_form");
                            }

                        }

                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context,
                                    "Enter article number");
                    }
                }
            });


            Button attach_img = (Button) view.findViewById(R.id.attach_img);
            Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
            attach_img.setOnClickListener(this);
            attach_vid.setOnClickListener(this);

            GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
            video_grid = (ImageView) view.findViewById(R.id.video_grid);

            if (report != null) {

                if (report.prod_type.equals("Pratik")) {
                    article_no.setText(report.article_no);
                /*cabinet_width.setText(report.Width);
                cabinet_height.setText(report.Height);
                cabinet_depth.setText(report.Thickness);*/

                    if (report.correct_product_order != null) {
                        if (report.correct_product_order.equals("Y")) {
                            pr_chk_1.setChecked(true);
                            pr_chk_2.setChecked(false);
                        } else if (report.correct_product_order.equals("N")) {
                            pr_chk_1.setChecked(false);
                            pr_chk_2.setChecked(true);
                        } else {
                            pr_chk_1.setChecked(false);
                            pr_chk_2.setChecked(false);
                        }
                    }
                    if (report.height_more_than_260mm != null) {
                        if (report.height_more_than_260mm.equals("Y")) {
                            pr_chk_3.setChecked(true);
                        } else {
                            pratik_height.setText(report.height_more_than_260mm);
                        }
                    }
                    if (report.length_slatted_or_wooden_50mm != null) {
                        if (report.length_slatted_or_wooden_50mm.equals("Y")) {
                            pr_chk_4.setChecked(true);
                        } else {
                            pratik_length_50mm.setText(report.length_slatted_or_wooden_50mm);
                        }
                    }
                    if (report.widht_slatted_or_wooden_30mm != null) {
                        if (report.widht_slatted_or_wooden_30mm.equals("Y")) {
                            pr_chk_5.setChecked(true);
                        } else {
                            pratik_width_30mm.setText(report.widht_slatted_or_wooden_30mm);
                        }
                    }
                    if (report.slatted_dimen_1400mm_2000mm != null) {
                        if (report.slatted_dimen_1400mm_2000mm.equals("Y")) {
                            pr_chk_6.setChecked(true);
                        } else {
                            pratik_sfd_dimen.setText(report.slatted_dimen_1400mm_2000mm);
                        }
                    }
                    if (report.slatted_dimen_1800mm_2000mm != null) {
                        if (report.slatted_dimen_1800mm_2000mm.equals("Y")) {
                            pr_chk_7.setChecked(true);
                        } else {
                            pratik_sfd_dimen1.setText(report.slatted_dimen_1800mm_2000mm);
                        }
                    }
                    if (report.bedding_box_fixing != null) {
                        if (report.bedding_box_fixing.equals("Y")) {
                            pr_chk_8.setChecked(true);
                            pr_chk_9.setChecked(false);
                        } else if (report.bedding_box_fixing.equals("N")) {
                            pr_chk_8.setChecked(false);
                            pr_chk_9.setChecked(true);
                        } else {
                            pr_chk_8.setChecked(false);
                            pr_chk_9.setChecked(false);
                        }
                    }
                    if (report.weight_between_23_25kgs != null) {
                        if (report.weight_between_23_25kgs.equals("Y")) {
                            pr_chk_10.setChecked(true);
                            pr_chk_11.setChecked(false);
                        } else if (report.weight_between_23_25kgs.equals("N")) {
                            pr_chk_10.setChecked(false);
                            pr_chk_11.setChecked(true);
                        } else {
                            pr_chk_10.setChecked(false);
                            pr_chk_11.setChecked(false);
                        }
                    }
                    if (report.weight_between_50_60kgs != null) {
                        if (report.weight_between_50_60kgs.equals("Y")) {
                            pr_chk_12.setChecked(true);
                            pr_chk_13.setChecked(false);
                        } else if (report.weight_between_50_60kgs.equals("N")) {
                            pr_chk_12.setChecked(false);
                            pr_chk_13.setChecked(true);
                        } else {
                            pr_chk_12.setChecked(false);
                            pr_chk_13.setChecked(false);
                        }
                    }
                    if (report.check_Stabilizing_rod != null) {
                        if (report.check_Stabilizing_rod.equals("Y")) {
                            pr_chk_14.setChecked(true);
                            pr_chk_15.setChecked(false);
                        } else if (report.check_Stabilizing_rod.equals("N")) {
                            pr_chk_14.setChecked(false);
                            pr_chk_15.setChecked(true);
                        } else {
                            pr_chk_14.setChecked(false);
                            pr_chk_15.setChecked(false);
                        }
                    }

                    if (report.Result != null) {

                        int index = resultList.indexOf(report.Result);
                        if (index > 0) {

                            if (index == 2) {
                                unresolve_reason.setSelection(2);
                                ll_product_defect.setVisibility(View.VISIBLE);
                                ll_site_issue.setVisibility(View.GONE);
                                edt_spare_defect_articleNo.setText(report.sparce_defect);
                                edt_complete_set_articleNo.setText(report.complete_set);
                            } else if (index == 3) {
                                unresolve_reason.setSelection(3);
                                ll_product_defect.setVisibility(View.GONE);
                                ll_site_issue.setVisibility(View.VISIBLE);

                                if (report.site_Issue_Reason != null) {
                                    int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                    if (index1 > 0) {
                                        spin_siteIssueReason_reason.setSelection(index1);
                                    }
                                }
                            } else if (index == 1) {
                                unresolve_reason.setSelection(1);
                            } else if (index == 4) {
                                unresolve_reason.setSelection(4);
                            }
                        } else {
                            unresolve_reason.setSelection(0);
                        }
                    }
                    if (report.Action != null) {
                        int index = actionList.indexOf(report.Action);
                        spin_action.setSelection(index);
                    }
                    if (report.wrong_product_reason != null) {
                        int index = wrongProdList.indexOf(report.wrong_product_reason);
                        spin_wrong_product.setSelection(index);
                        if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                            pr_chk_5.setEnabled(false);
                            pr_chk_6.setEnabled(false);
                            pr_chk_7.setEnabled(false);
                            pr_chk_8.setEnabled(false);
                            pr_chk_9.setEnabled(false);
                            pr_chk_10.setEnabled(false);
                            pr_chk_11.setEnabled(false);
                            pr_chk_12.setEnabled(false);
                            pr_chk_13.setEnabled(false);
                            pr_chk_14.setEnabled(false);
                            pr_chk_15.setEnabled(false);

                        /*    pr_chk_16.setEnabled(false);
                            pr_chk_17.setEnabled(false);
                            pr_chk_18.setEnabled(false);
                            pr_chk_19.setEnabled(false);
                            pr_chk_20.setEnabled(false);
                            pr_chk_21.setEnabled(false);
                            pr_chk_22.setEnabled(false);*/

                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                            spin_wrong_product.setEnabled(true);
                            if (pr_chk_1.isChecked()) {
                                spin_wrong_product.setEnabled(false);
                            } else {
                                spin_wrong_product.setEnabled(true);
                            }
                        } else {

                        }
                    }
                    if (report.Closure_Status != null) {
                        if (report.Closure_Status.equals("Resolved")) {
                            submit.setVisibility(View.GONE);
                            attach_img.setVisibility(View.GONE);
                            // img_grid.setVisibility(View.GONE);
                            // video_grid.setVisibility(View.GONE);
                            status.setEnabled(false);
                            attach_vid.setVisibility(View.GONE);
                            btn_deleteVideo.setVisibility(View.GONE);
                            bottom_linearll.setVisibility(View.VISIBLE);

                        } else if (report.Closure_Status.equals("Unresolved")) {
                            status.setSelection(1);

                            submit.setVisibility(View.VISIBLE);
                            attach_img.setVisibility(View.VISIBLE);
                            attach_vid.setVisibility(View.VISIBLE);
                            status.setEnabled(true);
                            bottom_linearll.setVisibility(View.VISIBLE);
                            // img_grid.setVisibility(View.VISIBLE);
                            // video_grid.setVisibility(View.VISIBLE);
                        }

                        comments.setText(report.Comment);

                    }

                } else if (report.prod_type.equals("Tavoletto")) {

                    if (report != null) {

                        article_no.setText(report.article_no);
                        cabinet_width.setText(report.Width);
                        cabinet_height.setText(report.Height);
                        // cabinet_depth.setText(report.Thickness);


                        if (report.correct_pro_one != null) {
                            if (report.correct_pro_one.equals("900x2000mm")) {
                                tv_chk_1.setChecked(true);
                                tv_chk_2.setChecked(false);
                                tv_chk_3.setChecked(false);
                                tv_chk_4.setChecked(false);

                                tv_chk_5.setEnabled(true);
                                tv_chk_6.setEnabled(true);
                                tv_chk_7.setEnabled(true);
                                tv_chk_8.setEnabled(true);
                            }
                        }
                        if (report.correct_pro_two != null) {
                            if (report.correct_pro_two.equals("1400x2000mm")) {
                                tv_chk_1.setChecked(false);
                                tv_chk_2.setChecked(true);
                                tv_chk_3.setChecked(false);
                                tv_chk_4.setChecked(false);

                                tv_chk_5.setEnabled(true);
                                tv_chk_6.setEnabled(true);
                                tv_chk_7.setEnabled(true);
                                tv_chk_8.setEnabled(true);
                            }
                        }
                        if (report.correct_pro_three != null) {
                            if (report.correct_pro_three.equals("1400x2000mm")) {
                                tv_chk_1.setChecked(false);
                                tv_chk_2.setChecked(false);
                                tv_chk_3.setChecked(true);
                                tv_chk_4.setChecked(false);

                                tv_chk_5.setEnabled(true);
                                tv_chk_6.setEnabled(true);
                                tv_chk_7.setEnabled(true);
                                tv_chk_8.setEnabled(true);

                            }
                        }

                        if (report.part_list_is_complete != null) {
                            if (report.part_list_is_complete.equals("Y")) {
                                tv_chk_5.setChecked(true);
                                tv_chk_6.setChecked(false);
                            } else if (report.part_list_is_complete.equals("N")) {
                                tv_chk_5.setChecked(false);
                                tv_chk_6.setChecked(true);
                            } else {
                                tv_chk_5.setChecked(false);
                                tv_chk_6.setChecked(false);
                            }
                        }

                        if (report.Wooden_Panel_dimensions != null) {
                            if (report.Wooden_Panel_dimensions.equals("Y")) {
                                tv_chk_7.setChecked(true);
                                tv_chk_8.setChecked(false);
                            } else if (report.Wooden_Panel_dimensions.equals("N")) {
                                tv_chk_7.setChecked(false);
                                tv_chk_8.setChecked(true);
                            } else {
                                tv_chk_7.setChecked(false);
                                tv_chk_8.setChecked(false);
                            }
                        }

                        if (report.wrong_product != null) {
                            if (report.wrong_product.equals("Y")) {
                                tv_chk_4.setChecked(true);
                                tv_chk_1.setChecked(false);
                                tv_chk_2.setChecked(false);
                                tv_chk_3.setChecked(false);

                                tv_chk_5.setEnabled(false);
                                tv_chk_6.setEnabled(false);
                                tv_chk_7.setEnabled(false);
                                tv_chk_8.setEnabled(false);


                            }
                        }

                        if (report.Result != null) {

                            int index = resultList.indexOf(report.Result);
                            if (index > 0) {

                                if (index == 2) {
                                    unresolve_reason.setSelection(2);
                                    ll_product_defect.setVisibility(View.VISIBLE);
                                    ll_site_issue.setVisibility(View.GONE);
                                    edt_spare_defect_articleNo.setText(report.sparce_defect);
                                    edt_complete_set_articleNo.setText(report.complete_set);
                                } else if (index == 3) {
                                    unresolve_reason.setSelection(3);
                                    ll_product_defect.setVisibility(View.GONE);
                                    ll_site_issue.setVisibility(View.VISIBLE);

                                    if (report.site_Issue_Reason != null) {
                                        int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                        if (index1 > 0) {
                                            spin_siteIssueReason_reason.setSelection(index1);
                                        }
                                    }
                                } else if (index == 1) {
                                    unresolve_reason.setSelection(1);
                                } else if (index == 4) {
                                    unresolve_reason.setSelection(4);
                                }
                            } else {
                                unresolve_reason.setSelection(0);
                            }

                        }
                        if (report.Action != null) {
                            int index = actionList.indexOf(report.Action);
                            spin_action.setSelection(index);
                        }
                        if (report.wrong_product_reason != null) {
                            int index = wrongProdList.indexOf(report.wrong_product_reason);
                            spin_wrong_product1.setSelection(index);

                            if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                               /* tv_chk_3.setEnabled(false);
                                tv_chk_4.setEnabled(false);
                                tv_chk_5.setEnabled(false);
                                tv_chk_6.setEnabled(false);
                                tv_chk_7.setEnabled(false);
                                tv_chk_8.setEnabled(false);*/
                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                                spin_wrong_product1.setEnabled(true);
                                if (tv_chk_1.isChecked()) {
                                    spin_wrong_product1.setEnabled(false);
                                } else {
                                    spin_wrong_product1.setEnabled(true);
                                }
                            } else {

                            }
                        }

                        if (report.Closure_Status != null) {
                            if (report.Closure_Status.equals("Resolved")) {
                                submit.setVisibility(View.GONE);
                                attach_img.setVisibility(View.GONE);
                                // img_grid.setVisibility(View.GONE);
                                // video_grid.setVisibility(View.GONE);
                                status.setEnabled(false);
                                attach_vid.setVisibility(View.GONE);
                                btn_deleteVideo.setVisibility(View.GONE);
                                bottom_linearll.setVisibility(View.VISIBLE);


                            } else if (report.Closure_Status.equals("Unresolved")) {
                                status.setSelection(1);
                                submit.setVisibility(View.VISIBLE);
                                attach_img.setVisibility(View.VISIBLE);
                                attach_vid.setVisibility(View.VISIBLE);
                                status.setEnabled(true);
                                bottom_linearll.setVisibility(View.VISIBLE);

                            }

                            comments.setText(report.Comment);

                        }
                    }
                }

                else if (report.prod_type.equals("Other"))
                {
                    if (report != null)
                    {

                        article_no.setText(report.article_no);

                        if (report.Result != null) {

                            int index = resultList.indexOf(report.Result);
                            if (index > 0) {

                                if (index == 2) {
                                    unresolve_reason.setSelection(2);
                                    ll_product_defect.setVisibility(View.VISIBLE);
                                    ll_site_issue.setVisibility(View.GONE);
                                    edt_spare_defect_articleNo.setText(report.sparce_defect);
                                    edt_complete_set_articleNo.setText(report.complete_set);
                                } else if (index == 3) {
                                    unresolve_reason.setSelection(3);
                                    ll_product_defect.setVisibility(View.GONE);
                                    ll_site_issue.setVisibility(View.VISIBLE);

                                    if (report.site_Issue_Reason != null) {
                                        int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                                        if (index1 > 0) {
                                            spin_siteIssueReason_reason.setSelection(index1);
                                        }
                                    }
                                } else if (index == 1) {
                                    unresolve_reason.setSelection(1);
                                } else if (index == 4) {
                                    unresolve_reason.setSelection(4);
                                }
                            } else {
                                unresolve_reason.setSelection(0);
                            }

                        }
                        if (report.Action != null) {
                            int index = actionList.indexOf(report.Action);
                            spin_action.setSelection(index);
                        }
                        if (report.wrong_product_reason != null) {
                            int index = wrongProdList.indexOf(report.wrong_product_reason);
                            spin_wrong_product1.setSelection(index);

                            if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                               /* tv_chk_3.setEnabled(false);
                                tv_chk_4.setEnabled(false);
                                tv_chk_5.setEnabled(false);
                                tv_chk_6.setEnabled(false);
                                tv_chk_7.setEnabled(false);
                                tv_chk_8.setEnabled(false);*/
                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/
                                spin_wrong_product1.setEnabled(true);
                                if (tv_chk_1.isChecked()) {
                                    spin_wrong_product1.setEnabled(false);
                                } else {
                                    spin_wrong_product1.setEnabled(true);
                                }
                            } else {

                            }
                        }

                        if (report.Closure_Status != null) {
                            if (report.Closure_Status.equals("Resolved")) {
                                submit.setVisibility(View.GONE);
                                attach_img.setVisibility(View.GONE);
                                // img_grid.setVisibility(View.GONE);
                                // video_grid.setVisibility(View.GONE);
                                status.setEnabled(false);
                                attach_vid.setVisibility(View.GONE);
                                btn_deleteVideo.setVisibility(View.GONE);
                                bottom_linearll.setVisibility(View.VISIBLE);


                            } else if (report.Closure_Status.equals("Unresolved")) {
                                status.setSelection(1);
                                submit.setVisibility(View.VISIBLE);
                                attach_img.setVisibility(View.VISIBLE);
                                attach_vid.setVisibility(View.VISIBLE);
                                status.setEnabled(true);
                                bottom_linearll.setVisibility(View.VISIBLE);

                            }

                            comments.setText(report.Comment);

                        }
                    }

                }

        }


        if (imgData != null) {
            for (int i = 0; i < imgData.size(); i++) {
                File file = new File(imgData.get(i).image_path);
                orgFileArray.add(file);
                fileNameArray.add(file);

                imgOriginalSize.add(imgData.get(i).original_size);
                imgCompressedSize.add(imgData.get(i).compressed_size);

                compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                capturedBM.add(compressedBitmap);
                int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                        .getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(
                        compressedBitmap, 100, nh, true);
                bmpArray.add(scaledBM);
            }
        }

        if (vidData != null) {
            try {
                // inputpath = vidData.FilePath;
                Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                inputpath = vidData.get(0).FilePath;
                video_grid.setImageBitmap(bmThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
        img_grid.setAdapter(imgAdapter);
        img_grid.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside
            // ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        video_grid.setOnClickListener(this);

        img_grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bitmap sBM = bmpArray.get(position);
                Bitmap cBm = capturedBM.get(position);
                int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                        true);
                showImage(scaledBM, cBm, sBM, position);
            }
        });
        status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contentValues.put("Closure_Status", status
                        .getSelectedItem().toString());
                if (position == 1) {
                    //  unresolved.setVisibility(View.VISIBLE);
                } else {
                    //  unresolved.setVisibility(View.GONE);
                    contentValues.put("Reason_For_Unresolved", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_pratik:
                        ll_pratik.setVisibility(View.VISIBLE);
                        ll_tavoletto.setVisibility(View.GONE);
                        bottom_linearll.setVisibility(View.VISIBLE);

                        contentValues.put("prod_type", "Pratik");
                        tv_chk_1.setChecked(false);
                        tv_chk_2.setChecked(false);
                        tv_chk_3.setChecked(false);
                        tv_chk_4.setChecked(false);
                        tv_chk_5.setChecked(false);
                        tv_chk_6.setChecked(false);
                        tv_chk_7.setChecked(false);
                        tv_chk_8.setChecked(false);

                        tv_chk_1.setEnabled(true);
                        tv_chk_2.setEnabled(true);
                        tv_chk_3.setEnabled(true);
                        tv_chk_4.setEnabled(true);
                        tv_chk_5.setEnabled(true);
                        tv_chk_6.setEnabled(true);
                        tv_chk_7.setEnabled(true);
                        tv_chk_8.setEnabled(true);

                        cabinet_width.setText("");
                        cabinet_height.setText("");
                        spin_wrong_product1.setSelection(0);

                        spin_siteIssueReason_reason.setSelection(0);
                        spin_action.setSelection(0);
                        edt_spare_defect_articleNo.setText("");
                        edt_complete_set_articleNo.setText("");
                        status.setSelection(0);
                        comments.setText("");


                        break;
                    case R.id.radio_tavoletto:
                        ll_tavoletto.setVisibility(View.VISIBLE);
                        ll_pratik.setVisibility(View.GONE);
                        prod_type = "Tavoletto";
                        contentValues.put("prod_type", "Tavoletto");
                        bottom_linearll.setVisibility(View.VISIBLE);

                        pratik_height.setText("");
                        pratik_length_50mm.setText("");
                        pratik_width_30mm.setText("");
                        pratik_sfd_dimen.setText("");
                        pratik_sfd_dimen1.setText("");
                        spin_wrong_product.setSelection(0);

                        pr_chk_1.setChecked(false);
                        pr_chk_2.setChecked(false);
                        pr_chk_3.setChecked(false);
                        pr_chk_4.setChecked(false);
                        pr_chk_5.setChecked(false);
                        pr_chk_6.setChecked(false);
                        pr_chk_7.setChecked(false);
                        pr_chk_8.setChecked(false);
                        pr_chk_9.setChecked(false);
                        pr_chk_10.setChecked(false);
                        pr_chk_11.setChecked(false);
                        pr_chk_12.setChecked(false);
                        pr_chk_13.setChecked(false);
                        pr_chk_14.setChecked(false);
                        pr_chk_15.setChecked(false);


                        pr_chk_1.setEnabled(true);
                        pr_chk_2.setEnabled(true);
                        pr_chk_3.setEnabled(true);
                        pr_chk_4.setEnabled(true);
                        pr_chk_5.setEnabled(true);
                        pr_chk_6.setEnabled(true);
                        pr_chk_7.setEnabled(true);
                        pr_chk_8.setEnabled(true);
                        pr_chk_9.setEnabled(true);
                        pr_chk_10.setEnabled(true);
                        pr_chk_11.setEnabled(true);
                        pr_chk_12.setEnabled(true);
                        pr_chk_13.setEnabled(true);
                        pr_chk_14.setEnabled(true);
                        pr_chk_15.setEnabled(true);

                        spin_siteIssueReason_reason.setSelection(0);
                        spin_action.setSelection(0);
                        edt_spare_defect_articleNo.setText("");
                        edt_complete_set_articleNo.setText("");
                        status.setSelection(0);
                        comments.setText("");
                        break;

                    case R.id.radio_other:
                        //    ll_other.setVisibility(View.VISIBLE);
                        ll_tavoletto.setVisibility(View.GONE);
                        ll_pratik.setVisibility(View.GONE);
                        prod_type = "Other";
                        contentValues.put("prod_type", "Other");
                        bottom_linearll.setVisibility(View.VISIBLE);

                        spin_siteIssueReason_reason.setSelection(0);
                        spin_action.setSelection(0);
                        edt_spare_defect_articleNo.setText("");
                        edt_complete_set_articleNo.setText("");
                        status.setSelection(0);
                        comments.setText("");
                        break;
                }
            }
        });

    } else if(product_category.contains("Tall Unit"))

    {
        //Tall Unit
        view = inflater.inflate(R.layout.new_tall_unit, null);
        baseFrame.removeAllViews();
        baseFrame.addView(view);

        FaultReport report = null;
        List<ImageData> imgData = null;
        List<VideoData> vidData = null;

        if (is_visited > 0) {
            report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                    complaint_number);
            imgData = dbAdapter.getImageData(complaint_number, "ALL");
            vidData = dbAdapter.getVideoData(complaint_number);
        }

        comp_number = (TextView) view.findViewById(R.id.complaint_number);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        date = (TextView) view.findViewById(R.id.date);
        mobile_no = (TextView) view.findViewById(R.id.mobile_no);
        dealer = (TextView) view.findViewById(R.id.dealer);
        invoice_no = (TextView) view.findViewById(R.id.invoice_no);
        article_no = (EditText) view.findViewById(R.id.article_no);
        prod_cat = (TextView) view.findViewById(R.id.prod_cat);
        prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
        complaint_deatils = (TextView) view
                .findViewById(R.id.complaint_deatils);
        btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
        btn_deleteVideo.setOnClickListener(this);
        address = (TextView) view.findViewById(R.id.address);

        spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
        spin_action = (Spinner) view.findViewById(R.id.spin_action);
        edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
        edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
        ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
        ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
        ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

        cabinet_height = (EditText) view.findViewById(R.id.cabinet_height);
        cabinet_width = (EditText) view.findViewById(R.id.cabinet_width);
        cabinet_depth = (EditText) view.findViewById(R.id.cabinet_depth);
        spin_wrong_product = (Spinner) view.findViewById(R.id.spin_wrong_product);

        tall_chk_1 = (CheckBox) view.findViewById(R.id.tall_chk_1);
        tall_chk_2 = (CheckBox) view.findViewById(R.id.tall_chk_2);
        tall_chk_3 = (CheckBox) view.findViewById(R.id.tall_chk_3);
        tall_chk_4 = (CheckBox) view.findViewById(R.id.tall_chk_4);
        tall_chk_5 = (CheckBox) view.findViewById(R.id.tall_chk_5);
        tall_chk_6 = (CheckBox) view.findViewById(R.id.tall_chk_6);
        tall_chk_7 = (CheckBox) view.findViewById(R.id.tall_chk_7);
        tall_chk_8 = (CheckBox) view.findViewById(R.id.tall_chk_8);
        tall_chk_9 = (CheckBox) view.findViewById(R.id.tall_chk_9);
        tall_chk_10 = (CheckBox) view.findViewById(R.id.tall_chk_10);
        tall_chk_11 = (CheckBox) view.findViewById(R.id.tall_chk_11);
        tall_chk_12 = (CheckBox) view.findViewById(R.id.tall_chk_12);

        tall_chk_1.setOnCheckedChangeListener(this);
        tall_chk_2.setOnCheckedChangeListener(this);
        tall_chk_3.setOnCheckedChangeListener(this);
        tall_chk_4.setOnCheckedChangeListener(this);
        tall_chk_5.setOnCheckedChangeListener(this);
        tall_chk_6.setOnCheckedChangeListener(this);
        tall_chk_7.setOnCheckedChangeListener(this);
        tall_chk_8.setOnCheckedChangeListener(this);
        tall_chk_9.setOnCheckedChangeListener(this);
        tall_chk_10.setOnCheckedChangeListener(this);
        tall_chk_11.setOnCheckedChangeListener(this);
        tall_chk_12.setOnCheckedChangeListener(this);

        unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
        unresolve_reason.setOnItemSelectedListener(this);

        // if image data is not null attach images to gridview
        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        comp_number.setText(complaint_number);
        customer_name.setText(c_name);
        date.setText(complaint_date);
        mobile_no.setText(end_user_mobile);
        mobile_no.setOnClickListener(this);
        dealer.setText(service_franchise);
        article_no.setText(article);
        complaint_deatils.setText(service_details);
        prod_cat.setText(product_category);
        prod_sub_cat.setText(product_sub_category);
        address.setText(c_address);

        status = (Spinner) view.findViewById(R.id.status);
        comments = (TextView) view.findViewById(R.id.comments);

        Button submit = (Button) view.findViewById(R.id.submit);

        //   submit.setOnClickListener(this);


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean is_article_no = Validation.hasText(article_no);
                if (article_no.getText().toString().trim().equals(""))
                    is_article_no = false;

                if (is_article_no) {
                    if (checkValidation(product_category)) {
                        if (validation() == true) {
                            submitData(product_category);
                        }
                            /*if (spin_wrong_product.isEnabled()) {
                                if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                                    UtilityClass.showToast(context,
                                            "Select Wrong Product Reason");
                            }*/

                    }
                } else {
                    if (!is_article_no)
                        UtilityClass.showToast(context, "Enter article number");
                }
            }
        });


        Button attach_img = (Button) view.findViewById(R.id.attach_img);
        Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
        attach_img.setOnClickListener(this);
        attach_vid.setOnClickListener(this);

        GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
        video_grid = (ImageView) view.findViewById(R.id.video_grid);

        if (report != null) {

            article_no.setText(report.article_no);
            cabinet_width.setText(report.Width);
            cabinet_height.setText(report.Height);
            cabinet_depth.setText(report.Thickness);

            if (report.drawer_within_range != null) {
                if (report.drawer_within_range.equals("Y")) {
                    tall_chk_1.setChecked(true);
                    tall_chk_2.setChecked(false);
                    spin_wrong_product.setEnabled(false);
                } else if (report.drawer_within_range.equals("N")) {
                    tall_chk_1.setChecked(false);
                    tall_chk_2.setChecked(true);

                } else {
                    tall_chk_1.setChecked(false);
                    tall_chk_2.setChecked(false);
                }
            }

            if (report.installation_template != null) {
                if (report.installation_template.equals("Y")) {
                    tall_chk_3.setChecked(true);
                    tall_chk_4.setChecked(false);
                } else if (report.installation_template.equals("N")) {
                    tall_chk_3.setChecked(false);
                    tall_chk_4.setChecked(true);


                } else {
                    tall_chk_3.setChecked(false);
                    tall_chk_4.setChecked(false);
                }
            }

            if (report.unit_if_fixed_to_wall != null) {
                if (report.unit_if_fixed_to_wall.equals("Y")) {
                    tall_chk_5.setChecked(true);
                    tall_chk_6.setChecked(false);
                } else if (report.unit_if_fixed_to_wall.equals("N")) {
                    tall_chk_5.setChecked(false);
                    tall_chk_6.setChecked(true);

                } else {
                    tall_chk_5.setChecked(false);
                    tall_chk_6.setChecked(false);
                }
            }

            if (report.enough_legs_for_support != null) {
                if (report.enough_legs_for_support.equals("Y")) {
                    tall_chk_7.setChecked(true);
                    tall_chk_8.setChecked(false);
                } else if (report.enough_legs_for_support.equals("N")) {
                    tall_chk_7.setChecked(false);
                    tall_chk_8.setChecked(true);
                } else {
                    tall_chk_7.setChecked(false);
                    tall_chk_8.setChecked(false);
                }
            }

            if (report.correct_alignment != null) {
                if (report.correct_alignment.equals("Y")) {
                    tall_chk_9.setChecked(true);
                    tall_chk_10.setChecked(false);
                } else if (report.correct_alignment.equals("N")) {
                    tall_chk_9.setChecked(false);
                    tall_chk_10.setChecked(true);
                } else {
                    tall_chk_9.setChecked(false);
                    tall_chk_10.setChecked(false);
                }
            }

            if (report.servodrive_sufficient_to_handle_weight != null) {
                if (report.servodrive_sufficient_to_handle_weight.equals("Y")) {
                    tall_chk_11.setChecked(true);
                    tall_chk_12.setChecked(false);
                } else if (report.servodrive_sufficient_to_handle_weight.equals("N")) {
                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(true);
                } else {
                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(false);
                }
            }

            if (report.Result != null) {

                int index = resultList.indexOf(report.Result);
                if (index > 0) {

                    if (index == 2) {
                        unresolve_reason.setSelection(2);
                        ll_product_defect.setVisibility(View.VISIBLE);
                        ll_site_issue.setVisibility(View.GONE);
                        edt_spare_defect_articleNo.setText(report.sparce_defect);
                        edt_complete_set_articleNo.setText(report.complete_set);
                    } else if (index == 3) {
                        unresolve_reason.setSelection(3);
                        ll_product_defect.setVisibility(View.GONE);
                        ll_site_issue.setVisibility(View.VISIBLE);

                        if (report.site_Issue_Reason != null) {
                            int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                            if (index1 > 0) {
                                spin_siteIssueReason_reason.setSelection(index1);
                            }
                        }
                    } else if (index == 1) {
                        unresolve_reason.setSelection(1);
                    } else if (index == 4) {
                        unresolve_reason.setSelection(4);
                    }
                } else {
                    unresolve_reason.setSelection(0);
                }

            }
            if (report.Action != null) {
                int index = actionList.indexOf(report.Action);
                spin_action.setSelection(index);
            }
            if (report.wrong_product_reason != null) {
                int index = wrongProdList.indexOf(report.wrong_product_reason);
                spin_wrong_product.setSelection(index);

                if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                    tall_chk_3.setEnabled(false);
                    tall_chk_4.setEnabled(false);
                    tall_chk_5.setEnabled(false);
                    tall_chk_6.setEnabled(false);
                    tall_chk_7.setEnabled(false);
                    tall_chk_8.setEnabled(false);
                    tall_chk_9.setEnabled(false);
                    tall_chk_10.setEnabled(false);
                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);
                   /* cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);*/

                    if (tall_chk_1.isChecked()) {
                        spin_wrong_product.setEnabled(false);
                    } else {
                        spin_wrong_product.setEnabled(true);
                    }
                } else {

                }

            }

            if (report.Closure_Status != null) {
                if (report.Closure_Status.equals("Resolved")) {
                    submit.setVisibility(View.GONE);
                    attach_img.setVisibility(View.GONE);
                    // img_grid.setVisibility(View.GONE);
                    // video_grid.setVisibility(View.GONE);
                    status.setEnabled(false);
                    attach_vid.setVisibility(View.GONE);
                    btn_deleteVideo.setVisibility(View.GONE);

                } else if (report.Closure_Status.equals("Unresolved")) {
                    status.setSelection(1);
                    // unresolved.setVisibility(View.VISIBLE);

                      /*  int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                            //     other.setVisibility(View.VISIBLE);
                            unresolve_reason.setSelection(i);
                         //   other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }*/
                    submit.setVisibility(View.VISIBLE);
                    attach_img.setVisibility(View.VISIBLE);
                    attach_vid.setVisibility(View.VISIBLE);
                    status.setEnabled(true);
                    // img_grid.setVisibility(View.VISIBLE);
                    // video_grid.setVisibility(View.VISIBLE);
                }

                comments.setText(report.Comment);

            }
        }
        if (imgData != null) {
            for (int i = 0; i < imgData.size(); i++) {
                File file = new File(imgData.get(i).image_path);
                orgFileArray.add(file);
                fileNameArray.add(file);

                imgOriginalSize.add(imgData.get(i).original_size);
                imgCompressedSize.add(imgData.get(i).compressed_size);

                compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                capturedBM.add(compressedBitmap);
                int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                        .getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(
                        compressedBitmap, 100, nh, true);
                bmpArray.add(scaledBM);
            }
        }

        if (vidData != null) {
            try {
                // inputpath = vidData.FilePath;
                Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                inputpath = vidData.get(0).FilePath;
                video_grid.setImageBitmap(bmThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
        img_grid.setAdapter(imgAdapter);
        img_grid.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside
            // ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        video_grid.setOnClickListener(this);

        img_grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bitmap sBM = bmpArray.get(position);
                Bitmap cBm = capturedBM.get(position);
                int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                        true);
                showImage(scaledBM, cBm, sBM, position);
            }
        });
        status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contentValues.put("Closure_Status", status
                        .getSelectedItem().toString());
                if (position == 1) {
                    //unresolved.setVisibility(View.VISIBLE);
                } else {
                    //   unresolved.setVisibility(View.GONE);
                    contentValues.put("Reason_For_Unresolved", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    } else if(product_group.contains("Lighting Technology"))

    {
        //Lighting
        view = inflater.inflate(R.layout.new_lighting_complaint, null);
        baseFrame.removeAllViews();
        baseFrame.addView(view);


        FaultReport report = null;
        List<ImageData> imgData = null;
        List<VideoData> vidData = null;

        if (is_visited > 0) {
            report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                    complaint_number);
            imgData = dbAdapter.getImageData(complaint_number, "ALL");
            vidData = dbAdapter.getVideoData(complaint_number);
        }

        comp_number = (TextView) view.findViewById(R.id.complaint_number);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        date = (TextView) view.findViewById(R.id.date);
        mobile_no = (TextView) view.findViewById(R.id.mobile_no);
        dealer = (TextView) view.findViewById(R.id.dealer);
        invoice_no = (TextView) view.findViewById(R.id.invoice_no);
        article_no = (EditText) view.findViewById(R.id.article_no);
        prod_cat = (TextView) view.findViewById(R.id.prod_cat);
        prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
        complaint_deatils = (TextView) view.findViewById(R.id.complaint_deatils);
        btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
        btn_deleteVideo.setOnClickListener(this);
        address = (TextView) view.findViewById(R.id.address);

        spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
        spin_action = (Spinner) view.findViewById(R.id.spin_action);
        edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
        edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
        ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
        ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
        ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);

        lc_chk_1 = (CheckBox) view.findViewById(R.id.lc_chk_1);
        lc_chk_2 = (CheckBox) view.findViewById(R.id.lc_chk_2);
        lc_chk_3 = (CheckBox) view.findViewById(R.id.lc_chk_3);
        lc_chk_4 = (CheckBox) view.findViewById(R.id.lc_chk_4);
        lc_chk_5 = (CheckBox) view.findViewById(R.id.lc_chk_5);
        lc_chk_6 = (CheckBox) view.findViewById(R.id.lc_chk_6);
        lc_chk_7 = (CheckBox) view.findViewById(R.id.lc_chk_7);
        lc_chk_8 = (CheckBox) view.findViewById(R.id.lc_chk_8);

        lc_chk_1.setOnCheckedChangeListener(this);
        lc_chk_2.setOnCheckedChangeListener(this);
        lc_chk_3.setOnCheckedChangeListener(this);
        lc_chk_4.setOnCheckedChangeListener(this);
        lc_chk_5.setOnCheckedChangeListener(this);
        lc_chk_6.setOnCheckedChangeListener(this);
        lc_chk_7.setOnCheckedChangeListener(this);
        lc_chk_8.setOnCheckedChangeListener(this);

        unresolve_reason = (Spinner) view.findViewById(R.id.unresolve_reason);
        unresolve_reason.setOnItemSelectedListener(this);

        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        comp_number.setText(complaint_number);
        customer_name.setText(c_name);
        date.setText(complaint_date);
        mobile_no.setText(end_user_mobile);
        mobile_no.setOnClickListener(this);
        dealer.setText(service_franchise);
        article_no.setText(article);
        complaint_deatils.setText(service_details);
        prod_cat.setText(product_category);
        prod_sub_cat.setText(product_sub_category);
        address.setText(c_address);

        status = (Spinner) view.findViewById(R.id.status);
        comments = (TextView) view.findViewById(R.id.comments);

        Button submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean is_article_no = Validation.hasText(article_no);
                if (article_no.getText().toString().trim().equals(""))
                    is_article_no = false;

                if (is_article_no) {
                    if (checkValidation(product_group)) {

                        if (validation() == true) {
                            submitData(product_group);
                        }
                    }
                } else {
                    if (!is_article_no)
                        UtilityClass.showToast(context,
                                "Enter article number");

                }
            }
        });

        //submit.setOnClickListener(this);

        Button attach_img = (Button) view.findViewById(R.id.attach_img);
        Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
        attach_img.setOnClickListener(this);
        attach_vid.setOnClickListener(this);

        GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
        video_grid = (ImageView) view.findViewById(R.id.video_grid);

        if (report != null) {
            article_no.setText(report.article_no);

            if (report.wrong_product != null) {
                if (report.wrong_product.equals("Y")) {
                    lc_chk_1.setChecked(true);
                    lc_chk_2.setChecked(false);
                } else if (report.wrong_product.equals("N")) {
                    lc_chk_1.setChecked(false);
                    lc_chk_2.setChecked(true);
                } else {
                    lc_chk_1.setChecked(false);
                    lc_chk_2.setChecked(false);
                }
            }

            if (report.power_supply_proper != null) {
                if (report.power_supply_proper.equals("Y")) {
                    lc_chk_3.setChecked(true);
                    lc_chk_4.setChecked(false);
                } else if (report.power_supply_proper.equals("N")) {
                    lc_chk_3.setChecked(false);
                    lc_chk_4.setChecked(true);
                } else {
                    lc_chk_3.setChecked(false);
                    lc_chk_4.setChecked(false);
                }
            }

            if (report.driver_working != null) {
                if (report.driver_working.equals("Y")) {
                    lc_chk_5.setChecked(true);
                    lc_chk_6.setChecked(false);
                } else if (report.driver_working.equals("N")) {
                    lc_chk_5.setChecked(false);
                    lc_chk_6.setChecked(true);
                } else {
                    lc_chk_5.setChecked(false);
                    lc_chk_6.setChecked(false);
                }
            }

            if (report.led_is_working != null) {
                if (report.led_is_working.equals("Y")) {
                    lc_chk_7.setChecked(true);
                    lc_chk_8.setChecked(false);
                } else if (report.led_is_working.equals("N")) {
                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(true);
                } else {
                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(false);
                }
            }

            if (report.Result != null) {

                int index = resultList.indexOf(report.Result);
                if (index > 0) {

                    if (index == 2) {
                        unresolve_reason.setSelection(2);
                        ll_product_defect.setVisibility(View.VISIBLE);
                        ll_site_issue.setVisibility(View.GONE);
                        edt_spare_defect_articleNo.setText(report.sparce_defect);
                        edt_complete_set_articleNo.setText(report.complete_set);
                    } else if (index == 3) {
                        unresolve_reason.setSelection(3);
                        ll_product_defect.setVisibility(View.GONE);
                        ll_site_issue.setVisibility(View.VISIBLE);

                        if (report.site_Issue_Reason != null) {
                            int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                            if (index1 > 0) {
                                spin_siteIssueReason_reason.setSelection(index1);
                            }
                        }
                    } else if (index == 1) {
                        unresolve_reason.setSelection(1);
                    } else if (index == 4) {
                        unresolve_reason.setSelection(4);
                    }
                } else {
                    unresolve_reason.setSelection(0);
                }

            }
            if (report.Action != null) {
                int index = actionList.indexOf(report.Action);
                spin_action.setSelection(index);
            }
            if (report.wrong_product_reason != null) {
                int index = wrongProdList.indexOf(report.wrong_product_reason);
                spin_wrong_product.setSelection(index);

                if (report.Reason_For_Unresolved.equalsIgnoreCase("Resolved")) {
                    lc_chk_3.setEnabled(false);
                    lc_chk_4.setEnabled(false);
                    lc_chk_5.setEnabled(false);
                    lc_chk_6.setEnabled(false);
                    lc_chk_7.setEnabled(false);
                    lc_chk_8.setEnabled(false);

                    spin_wrong_product.setEnabled(true);
                    if (lc_chk_1.isChecked()) {
                        spin_wrong_product.setEnabled(false);
                    } else {
                        spin_wrong_product.setEnabled(true);
                    }
                } else {

                }
            }

            if (report.Closure_Status != null) {
                if (report.Closure_Status.equals("Resolved")) {
                    submit.setVisibility(View.GONE);
                    attach_img.setVisibility(View.GONE);
                    // img_grid.setVisibility(View.GONE);
                    // video_grid.setVisibility(View.GONE);
                    status.setEnabled(false);
                    attach_vid.setVisibility(View.GONE);
                    btn_deleteVideo.setVisibility(View.GONE);

                } else if (report.Closure_Status.equals("Unresolved")) {
                    status.setSelection(1);

                    submit.setVisibility(View.VISIBLE);
                    attach_img.setVisibility(View.VISIBLE);
                    attach_vid.setVisibility(View.VISIBLE);
                    status.setEnabled(true);

                }

                comments.setText(report.Comment);

            }
        }

        if (imgData != null) {
            for (int i = 0; i < imgData.size(); i++) {
                File file = new File(imgData.get(i).image_path);
                orgFileArray.add(file);
                fileNameArray.add(file);

                imgOriginalSize.add(imgData.get(i).original_size);
                imgCompressedSize.add(imgData.get(i).compressed_size);

                compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                capturedBM.add(compressedBitmap);
                int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                        .getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(
                        compressedBitmap, 100, nh, true);
                bmpArray.add(scaledBM);
            }
        }

        if (vidData != null) {
            try {
                // inputpath = vidData.FilePath;
                Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                inputpath = vidData.get(0).FilePath;
                video_grid.setImageBitmap(bmThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
        img_grid.setAdapter(imgAdapter);
        img_grid.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside
            // ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        video_grid.setOnClickListener(this);

        img_grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bitmap sBM = bmpArray.get(position);
                Bitmap cBm = capturedBM.get(position);
                int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                        true);
                showImage(scaledBM, cBm, sBM, position);
            }
        });
        status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contentValues.put("Closure_Status", status
                        .getSelectedItem().toString());
                if (position == 1) {
                    //  unresolved.setVisibility(View.VISIBLE);
                } else {
                    //  unresolved.setVisibility(View.GONE);
                    contentValues.put("Reason_For_Unresolved", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    //***********


        else

    {
        view = inflater.inflate(R.layout.general_form, null);
        baseFrame.removeAllViews();
        baseFrame.addView(view);
        FaultReport report = null;
        List<ImageData> imgData = null;
        List<VideoData> vidData = null;

        if (is_visited > 0) {
            report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                    complaint_number);

           // Log.e("FaultReport", report.Closure_Status);

            imgData = dbAdapter.getImageData(complaint_number, "ALL");
            vidData = dbAdapter.getVideoData(complaint_number);

            Log.e("complaint_number", complaint_number);

        }
        comp_number = (TextView) view.findViewById(R.id.complaint_number);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        date = (TextView) view.findViewById(R.id.date);
        mobile_no = (TextView) view.findViewById(R.id.mobile_no);
        dealer = (TextView) view.findViewById(R.id.dealer);
        invoice_no = (TextView) view.findViewById(R.id.invoice_no);
        article_no = (EditText) view.findViewById(R.id.article_no);
        prod_cat = (TextView) view.findViewById(R.id.prod_cat);
        prod_sub_cat = (TextView) view.findViewById(R.id.prod_sub_cat);
        complaint_deatils = (TextView) view
                .findViewById(R.id.complaint_deatils);
        btn_deleteVideo = (Button) view.findViewById(R.id.btn_deleteVideo);
        address = (TextView) view.findViewById(R.id.address);

        btn_deleteVideo.setOnClickListener(this);
            /*  TextView text = (TextView) view.findViewById(R.id.text);
            TextView t_xt = (TextView) view.findViewById(R.id.t_xt);


          final LinearLayout unresolved = (LinearLayout) view
                    .findViewById(R.id.unresolved);
            unresolved.setVisibility(View.GONE);
            final LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.others);
            other.setVisibility(View.GONE);
            other_reason = (EditText) view.findViewById(R.id.other_reason);*/

        unresolve_reason = (Spinner) view
                .findViewById(R.id.unresolve_reason);
        unresolve_reason.setOnItemSelectedListener(this);


       /*     unresolve_reason
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            if (unresolve_reason.getSelectedItem().toString()
                                    .equals("Others")) {
                                other.setVisibility(View.VISIBLE);
                                contentValues.put("Reason_For_Unresolved",
                                        other_reason.getText().toString());
                            } else {
                                other.setVisibility(View.GONE);
                                other_reason.setText("null");
                                contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });*/
        // if image data is not null attach images to gridview

        spin_siteIssueReason_reason = (Spinner) view.findViewById(R.id.spin_siteIssueReason_reason);
        spin_action = (Spinner) view.findViewById(R.id.spin_action);
        edt_spare_defect_articleNo = (EditText) view.findViewById(R.id.edt_spare_defect_articleNo);
        edt_complete_set_articleNo = (EditText) view.findViewById(R.id.edt_complete_set_articleNo);
        ll_product_defect = (LinearLayout) view.findViewById(R.id.ll_product_defect);
        ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
        ll_site_issue = (LinearLayout) view.findViewById(R.id.ll_site_issue);


        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        comp_number.setText(complaint_number);
        customer_name.setText(c_name);
        date.setText(complaint_date);
        mobile_no.setText(end_user_mobile);
        mobile_no.setOnClickListener(this);
        dealer.setText(service_franchise);
        article_no.setText(article);
        complaint_deatils.setText(service_details);
        prod_cat.setText(product_category);
        prod_sub_cat.setText(product_sub_category);
        address.setText(c_address);

        status = (Spinner) view.findViewById(R.id.status);
        comments = (TextView) view.findViewById(R.id.comments);

        Button submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(this);

        Button attach_img = (Button) view.findViewById(R.id.attach_img);
        Button attach_vid = (Button) view.findViewById(R.id.attach_vid);
        attach_img.setOnClickListener(this);
        attach_vid.setOnClickListener(this);

        GridView img_grid = (GridView) view.findViewById(R.id.img_grid);
        video_grid = (ImageView) view.findViewById(R.id.video_grid);

        if (report != null) {

            article_no.setText(report.article_no);

            if (report.Result != null) {
                int index = resultList.indexOf(report.Result);
                if (index > 0) {
                    if (index == 2) {
                        unresolve_reason.setSelection(2);
                        ll_product_defect.setVisibility(View.VISIBLE);
                        ll_site_issue.setVisibility(View.GONE);
                        edt_spare_defect_articleNo.setText(report.sparce_defect);
                        edt_complete_set_articleNo.setText(report.complete_set);
                    } else if (index == 3) {
                        unresolve_reason.setSelection(3);
                        ll_product_defect.setVisibility(View.GONE);
                        ll_site_issue.setVisibility(View.VISIBLE);
                        if (report.site_Issue_Reason != null) {
                            int index1 = reasonList1.indexOf(report.site_Issue_Reason);
                            if (index1 > 0) {
                                spin_siteIssueReason_reason.setSelection(index1);
                            }
                        }
                    } else if (index == 1) {
                        unresolve_reason.setSelection(1);
                    } else if (index == 4) {
                        unresolve_reason.setSelection(4);
                    }
                } else {
                    unresolve_reason.setSelection(0);
                }
            }
            if (report.Action != null) {
                int index = actionList.indexOf(report.Action);
                spin_action.setSelection(index);
            }


            if (report.Closure_Status != null) {
                if (report.Closure_Status.equals("Resolved")) {
                    submit.setVisibility(View.GONE);
                    attach_img.setVisibility(View.GONE);
                    // img_grid.setVisibility(View.GONE);
                    // video_grid.setVisibility(View.GONE);
                    status.setEnabled(false);
                    attach_vid.setVisibility(View.GONE);
                    btn_deleteVideo.setVisibility(View.GONE);

                } else if (report.Closure_Status.equals("Unresolved")) {
                    status.setSelection(1);

                    submit.setVisibility(View.VISIBLE);
                    attach_img.setVisibility(View.VISIBLE);
                    attach_vid.setVisibility(View.VISIBLE);
                    status.setEnabled(true);
                    // img_grid.setVisibility(View.VISIBLE);
                    // video_grid.setVisibility(View.VISIBLE);
                }

                if (report.Comment != null) {
                    comments.setText(report.Comment);
                }

            }


                /*if (report.Closure_Status != null) {
                    if (report.Closure_Status.equals("Resolved")) {
                        submit.setVisibility(View.GONE);
                        attach_img.setVisibility(View.GONE);
                        // img_grid.setVisibility(View.GONE);
                        // video_grid.setVisibility(View.GONE);
                        status.setEnabled(false);
                        attach_vid.setVisibility(View.GONE);
                        btn_deleteVideo.setVisibility(View.GONE);

                    } else {
                        status.setSelection(1);
                       // unresolved.setVisibility(View.VISIBLE);

                        int index = reasonList
                                .indexOf(report.Reason_For_Unresolved);
                        if (index < 0) {
                            int i = reasonList.indexOf("Others");
                        //    other.setVisibility(View.VISIBLE);
                          //  unresolve_reason.setSelection(i);
                        //    other_reason.setText(report.Reason_For_Unresolved);
                        } else {
                            unresolve_reason.setSelection(index);
                        }

                        // if(report.Reason_For_Unresolved.equalsIgnoreCase("Others"))
                        // {
                        // other.setVisibility(View.VISIBLE);
                        // other_reason.setText(report.Reason_For_Unresolved);
                        // }
                        // else
                        // {
                        // other.setVisibility(View.GONE);
                        // String r[] =
                        // getResources().getStringArray(R.array.reason_array);
                        //
                        // for(int i=0;i < r.length ; i++)
                        // {
                        // if(report.Reason_For_Unresolved.equalsIgnoreCase(r[i]))
                        // {
                        // unresolve_reason.setSelection(i);
                        // break;
                        // }
                        // else
                        // {
                        // unresolve_reason.setSelection(9);
                        // }
                        // }
                        // }
                        submit.setVisibility(View.VISIBLE);
                        attach_img.setVisibility(View.VISIBLE);
                        attach_vid.setVisibility(View.VISIBLE);
                        status.setEnabled(true);
                        // img_grid.setVisibility(View.VISIBLE);
                        // video_grid.setVisibility(View.VISIBLE);
                    }

                    comments.setText(report.Comment);

                }*/

        }
        if (imgData != null) {
            for (int i = 0; i < imgData.size(); i++) {
                File file = new File(imgData.get(i).image_path);
                orgFileArray.add(file);
                fileNameArray.add(file);

                imgOriginalSize.add(imgData.get(i).original_size);
                imgCompressedSize.add(imgData.get(i).compressed_size);

                compressedBitmap = BitmapFactory.decodeFile(file.getPath());
                capturedBM.add(compressedBitmap);
                int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                        .getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(
                        compressedBitmap, 100, nh, true);
                bmpArray.add(scaledBM);
            }
        }

        if (vidData != null) {
            try {
                // inputpath = vidData.FilePath;
                Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                        vidData.get(0).FilePath, Thumbnails.MICRO_KIND);
                inputpath = vidData.get(0).FilePath;
                video_grid.setImageBitmap(bmThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imgAdapter = new ImageGridAdapter(context, fileNameArray);
        // //vidAdapter = new VideoGridAdapter(context,fileNameArray);
        img_grid.setAdapter(imgAdapter);
        img_grid.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside
            // ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // video_grid.setOnTouchListener(new OnTouchListener() {
        // // Setting on Touch Listener for handling the touch inside
        // // ScrollView
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // // Disallow the touch request for parent scroll on touch of
        // // child view
        // v.getParent().requestDisallowInterceptTouchEvent(true);
        // return false;
        // }
        // });

        video_grid.setOnClickListener(this);

        img_grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bitmap sBM = bmpArray.get(position);
                Bitmap cBm = capturedBM.get(position);
                int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,
                        true);
                showImage(scaledBM, cBm, sBM, position);
            }
        });

        // video_grid.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,int
        // position, long id) {
        // Bitmap sBM = bmpArray.get(position);
        // Bitmap cBm = capturedBM.get(position);
        // int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
        // Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh,true);
        // showImage(scaledBM, cBm, sBM,position);
        // }
        // });

        status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contentValues.put("Closure_Status", status
                        .getSelectedItem().toString());
                if (position == 1) {
                    //unresolved.setVisibility(View.VISIBLE);
                } else {
                    //unresolved.setVisibility(View.GONE);
                    contentValues.put("Reason_For_Unresolved", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}

    private boolean validation() {
        Boolean res = true;

        if (unresolve_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
            UtilityClass.showToast(context,
                    "Select Result");
            res = false;
        } else if (spin_action.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
            UtilityClass.showToast(context,
                    "Select Action");
            res = false;
        } else if (unresolve_reason.getSelectedItem().toString().equalsIgnoreCase("Site Issues")) {
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                UtilityClass.showToast(context,
                        "Select Site Issue Reason");
                res = false;
            }
        }
        return res;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.logout:
                Intent intent = new Intent(FaultReportForm.this,
                        MainLauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // finish();
                break;

            case R.id.home:
                Intent intent1 = new Intent(FaultReportForm.this,
                        DashBoardActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                // finish();
                break;

            case R.id.attach_img:

                final CharSequence[] options = {"Take Photo", "Choose Image from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Attach Image");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {

                            if (fileNameArray != null) {
                                if (fileNameArray.size() < 10) {
                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + "-" + complaint_number + ".jpeg");
                                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                                    startActivityForResult(intentPicture, 101);
                                } else {
                                    UtilityClass.showToast(FaultReportForm.this, "Only 10 images are allowed");
                                }
                            }

                        } else if (options[item].equals("Choose Image from Gallery")) {

                            if (fileNameArray != null) {
                                if (fileNameArray.size() < 10) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select File"), 103);
                                } else {
                                    UtilityClass.showToast(FaultReportForm.this, "Only 10 images are allowed");
                                }
                            }

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


			/*if (fileNameArray != null) {
                if (fileNameArray.size() < 10) {
					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.TITLE,System.currentTimeMillis() + "-" + complaint_number+ ".jpeg");
					mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
					Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intentPicture.putExtra(MediaStore.EXTRA_OUTPUT,mCapturedImageURI);
					startActivityForResult(intentPicture, 101);
				} else {
					UtilityClass.showToast(FaultReportForm.this,"Only 10 images are allowed");
				}
			}*/

                break;

            case R.id.attach_vid:

                // Intent vidintent = new
                // Intent("android.media.action.VIDEO_CAMERA");


                final CharSequence[] options1 = {"Take Video", "Choose Video from Gallery", "Cancel"};
                AlertDialog.Builder builder_video = new AlertDialog.Builder(context);
                builder_video.setTitle("Attach Video");
                builder_video.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options1[item].equals("Take Video")) {
                            if (inputpath != null) {

                                UtilityClass.showToast(FaultReportForm.this,
                                        "Kindly Delete the previous Video to record a new One");

                            } else {
                                Intent vidintent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                // vidintent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
                                // Constants.InputFileSizeParams.FILE_DURATION_SEC);
                                // create a file to save the video
                                try {
                                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // set the image file name
                                vidintent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                // set the video image quality to high
                                vidintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                // set duartion
                                vidintent.putExtra("android.intent.extra.durationLimit", 60);
                                // start the Video Capture Intent
                                startActivityForResult(vidintent,
                                        CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                            }

                        } else if (options1[item].equals("Choose Video from Gallery")) {


                            if (inputpath != null) {
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Kindly Delete the previous Video to record a new One");
                            } else {
                                Intent vidintent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                vidintent.setType("video/*");
                                startActivityForResult(vidintent, CAPTURE_VIDEO_FROM_GALLARY_ACTIVITY_REQUEST_CODE);
                            }

                        } else if (options1[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder_video.show();






			/*
            if (inputpath != null) {

				UtilityClass.showToast(FaultReportForm.this,
						"Kindly Delete the previous Video to record a new One");

			} else {
				Intent vidintent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				// vidintent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
				// Constants.InputFileSizeParams.FILE_DURATION_SEC);
				// create a file to save the video
				try {
					fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// set the image file name
				vidintent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				// set the video image quality to high
				vidintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				// set duartion
				vidintent.putExtra("android.intent.extra.durationLimit", 60);
				// start the Video Capture Intent
				startActivityForResult(vidintent,
						CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

			}*/


                break;

		/*case R.id.pin_image:
            ContentValues val = new ContentValues();
			val.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis()
					+ complaint_number + ".jpeg");
			mCapturedImageURI = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, val);
			Intent iPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			iPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
			startActivityForResult(iPicture, 101);
			break;

		case R.id.pin_video:
			// Intent vidintent = new
			// Intent("android.media.action.VIDEO_CAMERA");
			Intent vintent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			// vidintent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
			// Constants.InputFileSizeParams.FILE_DURATION_SEC);
			// create a file to save the video
			try {
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// set the image file name
			vintent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			// set the video image quality to high
			vintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			// set duartion
			vintent.putExtra("android.intent.extra.durationLimit", 60);
			// start the Video Capture Intent
			startActivityForResult(vintent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
			break;
*/
            case R.id.glass:
                contentValues.put("W_Door_Wt", "null");
                try {
                    if (Validation.hasText(door_width)
                            && Validation.hasText(door_height)
                            && Validation.hasText(door_thickness)) {
                        door_weight.setText(String.valueOf(calculateWeight(Float
                                        .parseFloat(door_width.getText().toString()), Float
                                        .parseFloat(door_height.getText().toString()),
                                Float.parseFloat(door_thickness.getText()
                                        .toString()), "g")));
                        contentValues.put("G_Door_Wt", door_weight.getText()
                                .toString());
                    } else {
                        contentValues.put("G_Door_Wt", "null");
                        com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                "Please enter required information");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.wood:
                contentValues.put("G_Door_Wt", "null");
                try {
                    if (Validation.hasText(door_width)
                            && Validation.hasText(door_height)
                            && Validation.hasText(door_thickness)) {
                        door_weight.setText(String.valueOf(calculateWeight(Integer
                                        .parseInt(door_width.getText().toString()), Integer
                                        .parseInt(door_height.getText().toString()),
                                Integer.parseInt(door_thickness.getText()
                                        .toString()), "w")));
                        contentValues.put("W_Door_Wt", door_weight.getText()
                                .toString());
                        door_weight.setError(null);
                    } else {
                        contentValues.put("W_Door_Wt", "null");
                        com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                "Please enter required information");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.mobile_no:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + end_user_mobile + ""));
                startActivity(callIntent);
                break;

            case R.id.submit:

                boolean is_article_no = Validation.hasText(article_no);
                if (article_no.getText().toString().trim().equals(""))
                    is_article_no = false;

                if (is_article_no) {
                    if (validation() == true) {
                        submitData("general_form");
                    }
                } else {
                    if (!is_article_no)
                        UtilityClass.showToast(context, "Enter article number");
                }



               /* boolean is_article_no = Validation.hasText(article_no);
                if (article_no.getText().toString().trim().equals(""))
                    is_article_no = false;
                boolean otherReason = false;
                // if(unresolve_reason.getSelectedItem().toString().equals("Others"))
                // otherReason = Validation.hasText(other_reason);
                if (unresolve_reason.getSelectedItem().toString().equals("Others")) {
                    otherReason = Validation.hasText(other_reason);
                    if (other_reason.getText().toString().trim().equals(""))
                        otherReason = false;
                } else if (status.getSelectedItem().toString()
                        .equalsIgnoreCase("Unresolved")
                        && unresolve_reason.getSelectedItem().toString()
                        .equalsIgnoreCase("--Select--")) {
                    otherReason = false;
                }
                if (unresolve_reason.getSelectedItem().toString().equals("Others")) {
                    if (is_article_no && otherReason) {
                        submitData("general_form");
                    } else {
                        if (!is_article_no)
                            UtilityClass.showToast(context, "Enter article number");
                        if (!otherReason)
                            UtilityClass.showToast(context,
                                    "Reason cannot be empty");
                    }
                } else {

                    if (status.getSelectedItem().toString()
                            .equalsIgnoreCase("Unresolved")
                            && unresolve_reason.getSelectedItem().toString()
                            .equals("--Select--")) {
                        if (!otherReason)
                            UtilityClass.showToast(context,
                                    "Reason cannot be empty");
                    } else {

                        if (is_article_no) {
                            submitData("general_form");
                        } else {
                            if (!is_article_no)
                                UtilityClass.showToast(context,
                                        "Enter article number");
                        }

                    }
                }*/
                break;

            case R.id.video_grid:
            /*
             * if (!outvidpath.contains("")) { Intent vid_intent = new Intent();
			 * vid_intent.setAction(android.content.Intent.ACTION_VIEW); File
			 * file = new File(outvidpath);
			 * vid_intent.setDataAndType(Uri.fromFile(file), "video/*");
			 * //VideocameraActivity.this.setRequestedOrientation(ActivityInfo.
			 * SCREEN_ORIENTATION_LANDSCAPE); startActivity(vid_intent); } else
			 * { AlertDialog.Builder builder1 = new
			 * AlertDialog.Builder(FaultReportForm.this);
			 * builder1.setTitle("Empty video."); builder1.setMessage(
			 * "Compressed video not found, Please compress video first.");
			 * builder1.setCancelable(true);
			 * builder1.setNeutralButton(android.R.string.ok, new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int id) { dialog.cancel(); } });
			 * AlertDialog alert11 = builder1.create(); alert11.show(); }
			 */
                break;

            case R.id.btn_deleteVideo:

                System.out.println("inputpath==" + inputpath);

                if (inputpath != null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            FaultReportForm.this);
                    builder1.setTitle("Delete Video");
                    builder1.setMessage("Are You Sure You want to delete the Video ? ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    // TODO Auto-generated method stub

                                    dialog.cancel();
                                    video_grid.setImageBitmap(null);
                                    try {
                                        File f = new File(inputpath);
                                        if (f.exists()) {

                                            if (Video_Status.equalsIgnoreCase("video_camera")) {
                                                f.delete();

                                            } else {

                                            }

                                            if (dbAdapter
                                                    .checkID(complaint_number,
                                                            "video_details",
                                                            "complaint_number")) {
                                                dbAdapter.deleteString("video_details",
                                                        "FilePath", inputpath);
                                                inputpath = null;
                                                outvidpath = null;
                                            }

                                            inputpath = null;
                                            outvidpath = null;
                                        }

							/*if (dbAdapter
                                    .checkID(complaint_number,
											"video_details",
											"complaint_number")) {
								dbAdapter.deleteString("video_details",
										"FilePath", inputpath);
								inputpath = null;
								outvidpath = null;
							}*/

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

                break;

        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        // File mediaStorageDir = new
        // File(Environment.getExternalStoragePublicDirectory(
        // Environment.DIRECTORY_PICTURES), "MyCameraVideo");

        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory()
                        + "/AdStringOVideos/Input/");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                // output.setText("Failed to create directory AdStringOVideos/Input.");

                Toast.makeText(context,
                        "Failed to create directory AdStringOVideos/Input.",
                        Toast.LENGTH_LONG).show();

                Log.d("SudesiVideoClips",
                        "Failed to create directory AdStringOVideos/Input.");
                return null;
            }
        }

        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date
                .getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file
            // name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    private float calculateWeight(float width, float height, float thickness,
                                  String material) {
        float weight = 0;
        if (material.contains("g")) {
            try {
                weight = ((float) (width / 1000) * (float) (height / 1000) * (float) (thickness / 1000)) * 2500;
            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            try {
                weight = ((float) (width / 1000) * (float) (height / 1000) * (float) (thickness / 1000)) * 700;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (weight > 0) {
            return weight;
        } else {
            return 0;
        }
    }

    private void showFeedbackForm(final String complaint_number) {
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.feedback_form, null);
        final EditText carpenter = (EditText) view.findViewById(R.id.carpenter);

        final EditText date_of_feedback = (EditText) view.findViewById(R.id.date_of_feedback);
        date_of_feedback.setEnabled(false);
        final Spinner site_v_rating = (Spinner) view.findViewById(R.id.site_v_rating);
        final Spinner Exrating = (Spinner) view.findViewById(R.id.rating);
        final TextView cc_executive = (TextView) view.findViewById(R.id.cc_executive);
        final SignatureView sign = (SignatureView) view.findViewById(R.id.signatureview);
        Button reset = (Button) view.findViewById(R.id.reset);
        hafele_rate = (TextView) view.findViewById(R.id.hafele_rate);
        // RadioGroup rg = (RadioGroup)view.findViewById(R.id.rg);
        radio_1 = (CheckBox) view.findViewById(R.id.radio_1);
        radio_2 = (CheckBox) view.findViewById(R.id.radio_2);
        radio_3 = (CheckBox) view.findViewById(R.id.radio_3);
        radio_4 = (CheckBox) view.findViewById(R.id.radio_4);
        radio_5 = (CheckBox) view.findViewById(R.id.radio_5);
        radio_6 = (CheckBox) view.findViewById(R.id.radio_6);
        radio_7 = (CheckBox) view.findViewById(R.id.radio_7);
        radio_8 = (CheckBox) view.findViewById(R.id.radio_8);
        radio_9 = (CheckBox) view.findViewById(R.id.radio_9);
        radio_10 = (CheckBox) view.findViewById(R.id.radio_10);
        // rg.setOnCheckedChangeListener(this);
        radio_1.setOnCheckedChangeListener(this);
        radio_2.setOnCheckedChangeListener(this);
        radio_3.setOnCheckedChangeListener(this);
        radio_4.setOnCheckedChangeListener(this);
        radio_5.setOnCheckedChangeListener(this);
        radio_6.setOnCheckedChangeListener(this);
        radio_7.setOnCheckedChangeListener(this);
        radio_8.setOnCheckedChangeListener(this);
        radio_9.setOnCheckedChangeListener(this);
        radio_10.setOnCheckedChangeListener(this);

        final ContentValues cv = new ContentValues();
        reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sign.clearSignature();
            }
        });

        site_v_rating.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String selection1 = site_v_rating.getSelectedItem().toString();
                if (selection1.contains("--- select from list ---")) {
                    cv.put("Technician_Rate", "");
                } else {
                    cv.put("Technician_Rate", selection1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Exrating.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String selection1 = Exrating.getSelectedItem().toString();
                if (selection1.contains("--- select from list ---")) {
                    cv.put("Executive_Rate", "");
                } else {
                    cv.put("Executive_Rate", selection1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        date.setText(complaint_date);
        carpenter.setText(pref.getUserName());
        carpenter.setEnabled(false);
        cc_executive.setText(CCExecutive);
        cc_executive.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        DecimalFormat formatter = new DecimalFormat("00");
        date_of_feedback
                .setText(formatter.format(calendar.get(Calendar.MONTH) + 1)
                        + "/"
                        + formatter.format(calendar.get(Calendar.DAY_OF_MONTH))
                        + "/" + calendar.get(Calendar.YEAR) + " "
                        + formatter.format(calendar.get(Calendar.HOUR_OF_DAY))
                        + ":" + formatter.format(calendar.get(Calendar.MINUTE))
                        + ":" + formatter.format(calendar.get(Calendar.SECOND)));
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);

        // alert.setView(view);

        alert.setTitle("Feedback Form");
        alert.setPositiveButton("Send Feedback",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // if(Validation.hasText(cc_executive) && stars != 0){
                        Bitmap bmp = ((SignatureView) view
                                .findViewById(R.id.signatureview)).getImage();
                        saveBitmap(bmp);

                        cv.put("Complaint_No", complaint_number);
                        cv.put("technician_id", pref.getUserId());
                        cv.put("sync_status", "NU");
                        cv.put("Technician_Name", carpenter.getText()
                                .toString());
                        cv.put("Executive_Name", cc_executive.getText()
                                .toString());
                        cv.put("Rating", String.valueOf(stars));
                        cv.put("SignatureFile", signatureFile);
                        cv.put("Insert_Date", date_of_feedback.getText()
                                .toString());
                        cv.put("ImagePath", signaturePath);

                        long response = dbAdapter.insert("feedback_form", cv);
                        if (response > 0) {
                            UtilityClass.showToast(context,
                                    "Thank you for Your feedback.");

                            if (outvidpath != null) {
                                File file = new File(outvidpath);

                                File in = new File(inputpath);

                                if (dbAdapter.checkID(complaint_number,
                                        "video_details", "complaint_number"))
                                {

                                    ContentValues cv1 = new ContentValues();
                                    cv1.clear();
                                    cv1.put("VideoFileName", file.getName());
                                    cv1.put("FilePath", file.getAbsolutePath());
                                    cv1.put("complaint_number", complaint_number);
                                    cv1.put("OriginalSize", String.valueOf(in.length()));
                                    cv1.put("CompressedSize",
                                            String.valueOf(file.length()));
                                    cv1.put("upload_status", "NU");
                                    cv1.put("video_count", 1);
                                    cv1.put("Device_id", pref.getDeviceId());
                                    cv1.put("username", pref.getUserName());

                                    Log.e("contentvalues", cv1.toString());

                                    dbAdapter.update("video_details", cv1,
                                            "complaint_number = '"
                                                    + complaint_number + "'", null);
                                    Log.e("ContentValues--FaultUpdate", cv1.toString());

                                } else {
                                    dbAdapter.saveVideoDetails(file.getName(),
                                            file.getAbsolutePath(), String.valueOf(in.length()),
                                            String.valueOf(file.length()),
                                            complaint_number, "NU",
                                            pref.getUserName(), 1,
                                            pref.getDeviceId());
                                }

                            }

                            System.gc();
                            Intent homeIntent = new Intent(
                                    FaultReportForm.this,
                                    DashBoardActivity.class);
                            startActivity(homeIntent);
                            // finish();
                        } else {
                            UtilityClass.showToast(context, "Error");
                        }
                        // }else{
                        // UtilityClass.showToast(context,
                        // "Your feedback is valuable");
                        // }

                        sendSMSAsync = new SendSMSAsync();
                        sendSMSAsync.execute();


                    }
                });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (outvidpath != null) {
                            File file = new File(outvidpath);

                            File in = new File(inputpath);

                            if (dbAdapter.checkID(complaint_number,
                                    "video_details", "complaint_number")) {

                                ContentValues cv1 = new ContentValues();
                                cv1.clear();
                                cv1.put("VideoFileName", file.getName());
                                cv1.put("FilePath", file.getAbsolutePath());
                                cv1.put("complaint_number", complaint_number);
                                cv1.put("OriginalSize", String.valueOf(in.length()));
                                cv1.put("CompressedSize",
                                        String.valueOf(file.length()));
                                cv1.put("upload_status", "NU");
                                cv1.put("video_count", 1);
                                cv1.put("Device_id", pref.getDeviceId());
                                cv1.put("username", pref.getUserName());

                                Log.e("contentvalues", cv1.toString());

                                dbAdapter.update("video_details", cv1,
                                        "complaint_number = '"
                                                + complaint_number + "'", null);
                                Log.e("ContentValues--FaultUpdate",
                                        cv1.toString());

                            } else {
                                dbAdapter.saveVideoDetails(file.getName(),
                                        file.getAbsolutePath(), OriginalSize,
                                        String.valueOf(file.length()),
                                        complaint_number, "NU",
                                        pref.getUserName(), 1,
                                        pref.getDeviceId());
                            }

                        }

                        sendSMSAsync = new SendSMSAsync();
                        sendSMSAsync.execute();
                        dialog.cancel();
                        System.gc();

                        Intent homeIntent = new Intent(FaultReportForm.this,
                                DashBoardActivity.class);
                        startActivity(homeIntent);
                        // finish();

                    }
                });
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void saveBitmap(Bitmap bmp) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Signatures");
        myDir.mkdirs();
        File file = new File(myDir, "signature"
                + Calendar.getInstance().getTimeInMillis() + ".png");
        signatureFile = file.getName();
        signaturePath = file.getPath();
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

            // if (resultCode == RESULT_OK) {

            String imagePath = getRealPathFromURI(mCapturedImageURI);
            if (imagePath != null) {
                if (resultCode == RESULT_OK) {
                    File F1 = new File(imagePath);
                    String fname = F1.getName();
                    byte[] arr = null;
                    try {
                        FileInputStream fileStream = new FileInputStream(F1);
                        // Instantiate array
                        arr = new byte[(int) F1.length()];
                        // / read All bytes of File stream

                        fileStream.read(arr);
                        fileStream.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    File renamedFile = null;
                    try {
                        if (F1.exists()) {
                            F1.delete();

                            //down vote accepted You can force the MediaScanner to add a specific file (or let it know it was deleted or modified)
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(F1)));

                            // F1.createNewFile();

                            renamedFile = new File(imagePath.replace(fname,
                                    System.currentTimeMillis() + "-"
                                            + complaint_number + "("
                                            + orgFileArray.size() + ").jpg"));
                            FileOutputStream fos1 = new FileOutputStream(
                                    renamedFile);
                            fos1.write(arr);
                            fos1.flush();
                            fos1.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    orgFileArray.add(renamedFile);
                    String originalSize = String.valueOf(renamedFile.length());
                    imgOriginalSize.add(String.valueOf(renamedFile.length()));
                    Log.e("originalSize", String.valueOf(renamedFile.length()));
                    if (renamedFile != null) {
                        try {

                            int imgWidth = 1366;
                            int imgHeight = 786;
                            int compressQuality = 20;
                            byte[] bmpPicByteArray;
                            ByteArrayOutputStream bao;
                            compressedBitmap = BitmapFactory
                                    .decodeFile(renamedFile.getPath());
                            int orgWidth = compressedBitmap.getWidth();
                            int orgHeight = compressedBitmap.getHeight();
                            float ratioX = (float) imgWidth / (float) orgWidth;
                            float ratioY = (float) imgHeight
                                    / (float) orgHeight;
                            float ratio = Math.min(ratioX, ratioY);

                            // New width and height based on aspect ratio
                            int newWidth = (int) (orgWidth * ratio);
                            int newHeight = (int) (orgHeight * ratio);
                            Bitmap bitmap2 = UtilityClass.scaleBitmap(
                                    compressedBitmap, newWidth, newHeight,
                                    imagePath);

                            if (bitmap2 != null) {
                                bao = new ByteArrayOutputStream();
                                bitmap2.compress(Bitmap.CompressFormat.JPEG,
                                        compressQuality, bao);
                                bmpPicByteArray = bao.toByteArray();

                            }

                            saveBitmapToFolder(bitmap2, renamedFile.getName());
                            capturedBM.add(bitmap2);
                            int nh = (int) (bitmap2.getHeight() * (100.0 / bitmap2
                                    .getWidth()));
                            Bitmap scaledBM = Bitmap.createScaledBitmap(
                                    bitmap2, 100, nh, true);
                            bmpArray.add(scaledBM);

							/*imgAdapter.notifyDataSetChanged();
                            fileNameArray.add(renamedFile);*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Image capture cancelled",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Image capture failed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 102) {

            String imagePath = getRealPathFromURI(mCapturedImageURI);
            if (imagePath != null) {

                if (resultCode == RESULT_OK) {
                    File F1 = new File(imagePath);
                    String fname = F1.getName();
                    byte[] arr = null;
                    try {
                        FileInputStream fileStream = new FileInputStream(F1);
                        // Instantiate array
                        arr = new byte[(int) F1.length()];
                        // / read All bytes of File stream

                        fileStream.read(arr);
                        fileStream.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    File renamedFile = null;
                    try {
                        if (F1.exists()) {
                            F1.delete();
                            // F1.createNewFile();
                            renamedFile = new File(imagePath.replace(fname,
                                    System.currentTimeMillis() + "-"
                                            + complaint_number + "("
                                            + orgFileArray.size() + ").jpg"));
                            FileOutputStream fos1 = new FileOutputStream(
                                    renamedFile);
                            fos1.write(arr);
                            fos1.flush();
                            fos1.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    orgFileArray.add(renamedFile);
                    String originalSize = String.valueOf(renamedFile.length());
                    Log.e("originalSize", String.valueOf(renamedFile.length()));
                    if (renamedFile != null) {
                        try {
                            // CompressionTechnique comp = new
                            // CompressionTechnique(context);
                            // File proofFile =
                            // com.sudesi.adstringocompression.CompressionTechnique.getCompressImage(file);
                            // File proofFile = comp.gC(renamedFile);
                            compressedBitmap = BitmapFactory
                                    .decodeFile(renamedFile.getPath());
                            saveBitmapToFolder(compressedBitmap,
                                    renamedFile.getName());
                            capturedBM.add(compressedBitmap);

                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            compressedBitmap.compress(
                                    Bitmap.CompressFormat.JPEG, 100, bao);
                            // byte[] bmpPicByteArray = bao.toByteArray();
                            // base64 = Base64.encodeToString(bmpPicByteArray,
                            // Base64.DEFAULT);

                            int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                                    .getWidth()));
                            Bitmap scaledBM = Bitmap.createScaledBitmap(
                                    compressedBitmap, 100, nh, true);
                            bmpArray.add(scaledBM);

                            fileNameArray.add(renamedFile);

                            imgAdapter = null;

                            imgAdapter = new ImageGridAdapter(context,
                                    fileNameArray);
                            // vidAdapter = new
                            // VideoGridAdapter(context,fileNameArray);
                            img_grid.setAdapter(imgAdapter);
                            // imgAdapter.notifyDataSetChanged();

                            // captured_img.setImageBitmap(scaledBM);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Image capture cancelled",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Image capture failed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            Video_Status = "video_camera";

            if (resultCode == RESULT_OK) {
                is_video_attached = true;
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(date.getTime());
                // MediaMetadataRetriever metadata = null ;
                // try{
                // metadata = new MediaMetadataRetriever();
                // }catch(Exception e){
                // e.printStackTrace();
                // }
                //
                // Bitmap bmpMeta = null;
                try {
                    inputpath = fileUri.getPath();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // if(metadata != null)metadata.setDataSource(inputpath);
                //
                // if(metadata != null)bmpMeta = metadata.getFrameAtTime();
                //
                // int w = bmpMeta.getWidth();
                // int h = bmpMeta.getHeight();
                //
                // String videoResolution = w + "x" + h;
                // original_str_resolution = videoResolution;
                // videoFile = new File(inputpath);
                //
                // long fileSiz = videoFile.length();
                // double fileSizeKB = fileSiz / 1024;
                // double fileSizeMB = fileSizeKB / 1024;
                // double roundOff = Math.round(fileSizeMB * 100.0) / 100.0;
                //
                // Log.e("fileSiz", String.valueOf(roundOff));
                // System.out.println("fameWidth-----" + w);
                // System.out.println("fameHeight----" + h);
                //
                // int msec = MediaPlayer.create(FaultReportForm.this,
                // Uri.fromFile(new File(inputpath))).getDuration();
                //
                // secs = (int) (msec / 1000.0);
                //
                // Log.e("Duration", String.valueOf(secs));
                //
                // File mediaStorageDir = new
                // File(Environment.getExternalStorageDirectory()
                // + "/AdStringOVideos/Output/");
                //
                // if (!mediaStorageDir.exists()) {
                // mediaStorageDir.mkdirs();
                // }
                //
                // outvidpath = Environment.getExternalStorageDirectory()
                // + "/AdStringOVideos/Output/" + timeStamp+ ".mp4";

                // OriginalSize = String.valueOf(fileSiz);
                Bitmap bmThumbnail;
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(inputpath,
                        Thumbnails.MICRO_KIND);
                video_grid.setImageBitmap(bmThumbnail);

                // Video captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Video saved to:\n" + data.getData(),
                // Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video capture cancelled",
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Video capture failed.", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (requestCode == 103 && data != null) {

            //To referesh galllery
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });


            Uri selectedImage = data.getData();

            //String imagePath=getRealPathFromURI(selectedImage);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver()
                    .query(selectedImage, filePathColumn, null, null,
                            null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            //***************************

            //	imagePath = getRealPathFromURI(mCapturedImageURI);
            if (imagePath != null) {
                if (resultCode == RESULT_OK) {

                    //File F1 = new File(imagePath);
                    File renamedFile = new File(imagePath);
                    String fname = renamedFile.getName();
                    byte[] arr = null;


					/*	try {

						FileInputStream fileStream = new FileInputStream(F1);
						// Instantiate array
						arr = new byte[(int) F1.length()];
						// / read All bytes of File stream

						fileStream.read(arr);
						fileStream.close();


					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


					File renamedFile = null;

					try {
						if (F1.exists()) {

							F1.delete();
							//down vote accepted You can force the MediaScanner to add a specific file (or let it know it was deleted or modified)
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(F1)));

							// F1.createNewFile();

							renamedFile = new File(imagePath);

							renamedFile = new File(imagePath.replace(fname,
									System.currentTimeMillis() + "-"
											+ complaint_number + "("
											+ orgFileArray.size() + ").jpg"));

							FileOutputStream fos1 = new FileOutputStream(
									renamedFile);
							fos1.write(arr);
							fos1.flush();
							fos1.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/


                    String bimapGallaryFilename = System.currentTimeMillis() + "-" + complaint_number + "(" + orgFileArray.size() + ").jpg";

                    if (!orgFileArray.contains(renamedFile)) {

                        orgFileArray.add(renamedFile);
                        String originalSize = String.valueOf(renamedFile.length());
                        Log.e("originalSize", String.valueOf(renamedFile.length()));
                        imgOriginalSize.add(String.valueOf(renamedFile.length()));
                        if (renamedFile != null) {
                            try {
                                // CompressionTechnique comp = new
                                // CompressionTechnique(context);
                                // File proofFile =
                                // com.sudesi.adstringocompression.CompressionTechnique.getCompressImage(file);
                                // File proofFile = comp.gC(renamedFile);
                                int imgWidth = 1366;
                                int imgHeight = 786;
                                int compressQuality = 30;
                                byte[] bmpPicByteArray;
                                ByteArrayOutputStream bao;
                                compressedBitmap = BitmapFactory
                                        .decodeFile(renamedFile.getPath());
                                int orgWidth = compressedBitmap.getWidth();
                                int orgHeight = compressedBitmap.getHeight();
                                float ratioX = (float) imgWidth / (float) orgWidth;
                                float ratioY = (float) imgHeight
                                        / (float) orgHeight;
                                float ratio = Math.min(ratioX, ratioY);

                                // New width and height based on aspect ratio
                                int newWidth = (int) (orgWidth * ratio);
                                int newHeight = (int) (orgHeight * ratio);
                                Bitmap bitmap2 = UtilityClass.scaleBitmap(
                                        compressedBitmap, newWidth, newHeight,
                                        imagePath);

                                // compressedBitmap.recycle();

                                if (bitmap2 != null) {
                                    bao = new ByteArrayOutputStream();
                                    bitmap2.compress(Bitmap.CompressFormat.JPEG,
                                            compressQuality, bao);
                                    bmpPicByteArray = bao.toByteArray();

                                }

                                // bitmap2.recycle();
                                // bitmap2 = null;
                                // FileOutputStream fos = new FileOutputStream();
                                // fos.write(bmpPicByteArray);
                                // compressedBitmap =
                                // BitmapFactory.decodeFile(renamedFile.getPath());


                                saveBitmapToFolder(bitmap2, bimapGallaryFilename);
                                //saveBitmapToFolder(bitmap2, renamedFile.getName());
                                capturedBM.add(bitmap2);

                                // compressedBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                                // bao);
                                // byte[] bmpPicByteArray = bao.toByteArray();
                                // base64 = Base64.encodeToString(bmpPicByteArray,
                                // Base64.DEFAULT);

                                int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap.getWidth()));
                                Bitmap scaledBM = Bitmap.createScaledBitmap(compressedBitmap, 100, nh, true);
                                bmpArray.add(scaledBM);




							  /*  imgAdapter.notifyDataSetChanged();
                                fileNameArray.add(renamedFile);*/

                                //	fileNameArray.add(renamedFile);
                                // captured_img.setImageBitmap(scaledBM);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Toast.makeText(this, "File Already Selected. Choose Another File", Toast.LENGTH_LONG).show();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Image capture cancelled",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Image capture failed.",
                            Toast.LENGTH_LONG).show();
                }
            }

        } else if (requestCode == CAPTURE_VIDEO_FROM_GALLARY_ACTIVITY_REQUEST_CODE) {

            Video_Status = "video_gallery";

            if (resultCode == RESULT_OK) {
                is_video_attached = true;
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(date.getTime());

                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();


                try {
                    inputpath = getPathgallary(selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    if (inputpath != null) {

                        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(inputpath));
                        int duration = mp.getDuration();
                        mp.release();

                        if ((duration / 1000) > 60) {
                            // Show Your Messages
                            Toast.makeText(getApplicationContext(), "Video Size is greater than 60 Seconds", Toast.LENGTH_SHORT).show();
                            inputpath = null;
                        } else {
                            Log.e("inputpath", inputpath);

                            Bitmap bmThumbnail;
                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(inputpath,
                                    Thumbnails.MICRO_KIND);
                            video_grid.setImageBitmap(bmThumbnail);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video capture cancelled",
                        Toast.LENGTH_LONG).show();

            } else {
                //***************
                Toast.makeText(this, "Video capture failed.", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    public String getPathgallary(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            // return contentUri.getPath();
            return null;
        }
    }

    private void saveBitmapToFolder(Bitmap cBitmap, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString()
                + "/Compressed_Images/";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = fileName;
        File file = new File(myDir, fname);

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            cBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            String compressedSize = String.valueOf(file.length());
            Log.e("compressedSize", String.valueOf(file.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgAdapter.notifyDataSetChanged();
        fileNameArray.add(file);
        imgCompressedSize.add(String.valueOf(file.length()));
    }

    private void showImage(final Bitmap scaledCapturedBm, final Bitmap cBM,
                           final Bitmap sBM, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_detail, null);
        ImageView img = (ImageView) view.findViewById(R.id.img);
        if (scaledCapturedBm != null)
            img.setImageBitmap(scaledCapturedBm);
        dialog.setView(view);

        String CS = dbAdapter.getClosureStatus(complaint_number);

        if (!CS.equalsIgnoreCase("")) {
            if (CS.equalsIgnoreCase("Resolved")) {
                dialog.setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });

            } else {
                dialog.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                if (dbAdapter.checkID(complaint_number,
                                        "image_details", "complaint_number")) {
                                    dbAdapter.deleteString("image_details",
                                            "image_path",
                                            fileNameArray.get(position)
                                                    .getPath());

                                }
                                if (fileNameArray.get(position).exists()) {
                                    fileNameArray.get(position).delete();
                                }

                                fileNameArray.remove(position);
                                imgOriginalSize.remove(position);
                                imgCompressedSize.remove(position);
                                bmpArray.remove(sBM);
                                capturedBM.remove(cBM);
                                imgAdapter.notifyDataSetChanged();

                            }
                        });

                dialog.setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });
            }
        } else {
            dialog.setNegativeButton("Delete",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (dbAdapter.checkID(complaint_number,
                                    "image_details", "complaint_number")) {
                                dbAdapter.deleteString("image_details",
                                        "image_path",
                                        fileNameArray.get(position).getPath());

                            }
                            if (fileNameArray.get(position).exists()) {
                                fileNameArray.get(position).delete();
                            }

                            fileNameArray.remove(position);
                            bmpArray.remove(sBM);
                            capturedBM.remove(cBM);
                            imgAdapter.notifyDataSetChanged();

                        }
                    });

            dialog.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }

        dialog.show();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.val_one:
                if (isChecked) {
                    if (val_two.isChecked())
                        val_two.setChecked(false);
                    if (val_three.isChecked())
                        val_three.setChecked(false);
                    if (wrong_product.isChecked())
                        wrong_product.setChecked(false);
                    contentValues.put("correct_pro_one", "EN2");
                } else {
                    contentValues.put("correct_pro_one", "null");
                }
                break;
            case R.id.val_two:
                if (isChecked) {
                    if (val_one.isChecked())
                        val_one.setChecked(false);
                    if (val_three.isChecked())
                        val_three.setChecked(false);
                    if (wrong_product.isChecked())
                        wrong_product.setChecked(false);
                    contentValues.put("correct_pro_two", "EN3");
                } else {
                    contentValues.put("correct_pro_two", "null");
                }
                break;
            case R.id.val_three:
                if (isChecked) {
                    if (val_two.isChecked())
                        val_two.setChecked(false);
                    if (val_one.isChecked())
                        val_one.setChecked(false);
                    if (wrong_product.isChecked())
                        wrong_product.setChecked(false);
                    contentValues.put("correct_pro_three", "EN4");
                } else {
                    contentValues.put("correct_pro_three", "null");
                }
                break;

            case R.id.wrong_product:
                if (isChecked) {
                    if (product_sub_category.contains("Floor Springs")) {

                        //Floor Springs

                        form.setEnabled(false);
                        // w_door_weight.setEnabled(false);
                        // g_door_weight.setEnabled(false);
                        if (val_one.isChecked())
                            val_one.setChecked(false);
                        if (val_two.isChecked())
                            val_two.setChecked(false);
                        if (val_three.isChecked())
                            val_three.setChecked(false);
                        if (floor_chk_1.isChecked())
                            floor_chk_1.setChecked(false);
                        if (floor_chk_2.isChecked())
                            floor_chk_2.setChecked(false);
                        if (floor_chk_3.isChecked())
                            floor_chk_3.setChecked(false);
                        if (floor_chk_4.isChecked())
                            floor_chk_4.setChecked(false);
                        if (floor_chk_5.isChecked())
                            floor_chk_5.setChecked(false);
                        if (floor_chk_6.isChecked())
                            floor_chk_6.setChecked(false);
                        if (floor_chk_7.isChecked())
                            floor_chk_7.setChecked(false);
                        if (floor_chk_8.isChecked())
                            floor_chk_8.setChecked(false);

                        floor_chk_1.setEnabled(false);
                        floor_chk_2.setEnabled(false);
                        floor_chk_3.setEnabled(false);
                        floor_chk_4.setEnabled(false);
                        floor_chk_5.setEnabled(false);
                        floor_chk_6.setEnabled(false);
                        floor_chk_7.setEnabled(false);
                        floor_chk_8.setEnabled(false);

                        contentValues.put("wrong_product", "Y");
                    } else {
                        if (val_one.isChecked())
                            val_one.setChecked(false);
                        if (val_two.isChecked())
                            val_two.setChecked(false);
                        if (val_three.isChecked())
                            val_three.setChecked(false);
                        if (dc_chk_1.isChecked())
                            dc_chk_1.setChecked(false);
                        if (dc_chk_2.isChecked())
                            dc_chk_2.setChecked(false);
                        if (dc_chk_3.isChecked())
                            dc_chk_3.setChecked(false);
                        if (dc_chk_4.isChecked())
                            dc_chk_4.setChecked(false);
                        if (dc_chk_5.isChecked())
                            dc_chk_5.setChecked(false);
                        if (dc_chk_6.isChecked())
                            dc_chk_6.setChecked(false);
                        if (dc_chk_7.isChecked())
                            dc_chk_7.setChecked(false);
                        if (dc_chk_8.isChecked())
                            dc_chk_8.setChecked(false);
                        if (dc_chk_9.isChecked())
                            dc_chk_9.setChecked(false);
                        if (dc_chk_10.isChecked())
                            dc_chk_10.setChecked(false);
                        if (dc_chk_11.isChecked())
                            dc_chk_11.setChecked(false);
                        if (dc_chk_12.isChecked())
                            dc_chk_12.setChecked(false);
                        if (dc_chk_13.isChecked())
                            dc_chk_13.setChecked(false);
                        if (dc_chk_14.isChecked())
                            dc_chk_14.setChecked(false);
                        if (dc_chk_15.isChecked())
                            dc_chk_15.setChecked(false);
                        if (dc_chk_16.isChecked())
                            dc_chk_16.setChecked(false);
                        dc_chk_1.setEnabled(false);
                        dc_chk_2.setEnabled(false);
                        dc_chk_3.setEnabled(false);
                        dc_chk_4.setEnabled(false);
                        dc_chk_5.setEnabled(false);
                        dc_chk_6.setEnabled(false);
                        dc_chk_7.setEnabled(false);
                        dc_chk_8.setEnabled(false);
                        dc_chk_9.setEnabled(false);
                        dc_chk_10.setEnabled(false);
                        dc_chk_11.setEnabled(false);
                        dc_chk_12.setEnabled(false);
                        dc_chk_13.setEnabled(false);
                        dc_chk_14.setEnabled(false);
                        dc_chk_15.setEnabled(false);
                        dc_chk_16.setEnabled(false);
                        contentValues.put("wrong_product", "Y");
                    }

                } else {
                    if (product_sub_category.contains("Floor Springs")) {
                        // w_door_weight.setEnabled(true);
                        // g_door_weight.setEnabled(true);
                        floor_chk_1.setEnabled(true);
                        floor_chk_2.setEnabled(true);
                        floor_chk_3.setEnabled(true);
                        floor_chk_4.setEnabled(true);
                        floor_chk_5.setEnabled(true);
                        if (floor_chk_5.isChecked()) {

                        } else {
                            floor_chk_6.setEnabled(true);
                            floor_chk_7.setEnabled(true);
                            floor_chk_8.setEnabled(true);
                        }

                        contentValues.put("wrong_product", "null");
                    } else {
                        dc_chk_1.setEnabled(true);
                        dc_chk_2.setEnabled(true);
                        dc_chk_3.setEnabled(true);
                        dc_chk_4.setEnabled(true);
                        dc_chk_5.setEnabled(true);
                        dc_chk_6.setEnabled(true);
                        dc_chk_7.setEnabled(true);
                        dc_chk_8.setEnabled(true);
                        dc_chk_9.setEnabled(true);
                        dc_chk_10.setEnabled(true);
                        dc_chk_11.setEnabled(true);
                        dc_chk_12.setEnabled(true);
                        dc_chk_13.setEnabled(true);
                        dc_chk_14.setEnabled(true);
                        dc_chk_15.setEnabled(true);
                        dc_chk_16.setEnabled(true);
                        contentValues.put("wrong_product", "null");
                    }
                }
                break;

            case R.id.floor_chk_1:
                if (isChecked) {
                    if (floor_chk_2.isChecked())
                        floor_chk_2.setChecked(false);
                    contentValues.put("Correct_Installation", "Y");
                } else {
                    contentValues.put("Correct_Installation", "null");
                }
                break;
            case R.id.floor_chk_2:
                if (isChecked) {
                    if (floor_chk_1.isChecked())
                        floor_chk_1.setChecked(false);
                    contentValues.put("Correct_Installation", "N");

                    if (floor_chk_3.isChecked())
                        floor_chk_3.setChecked(false);
                    if (floor_chk_4.isChecked())
                        floor_chk_4.setChecked(false);
                    if (floor_chk_5.isChecked())
                        floor_chk_5.setChecked(false);
                    if (floor_chk_6.isChecked())
                        floor_chk_6.setChecked(false);
                    if (floor_chk_7.isChecked())
                        floor_chk_7.setChecked(false);
                    if (floor_chk_8.isChecked())
                        floor_chk_8.setChecked(false);

                    floor_chk_3.setEnabled(false);
                    floor_chk_4.setEnabled(false);
                    floor_chk_5.setEnabled(false);
                    floor_chk_6.setEnabled(false);
                    floor_chk_7.setEnabled(false);
                    floor_chk_8.setEnabled(false);
                } else {
                    contentValues.put("Correct_Installation", "null");
                    floor_chk_3.setEnabled(true);
                    floor_chk_4.setEnabled(true);
                    floor_chk_5.setEnabled(true);
                    if (floor_chk_5.isChecked()) {

                    } else {
                        floor_chk_6.setEnabled(true);
                        floor_chk_7.setEnabled(true);
                        floor_chk_8.setEnabled(true);
                    }
                }
                break;
            case R.id.floor_chk_3:
                if (isChecked) {
                    // if(contentValues.get("Correct_Installation")!= null) {
                    floor_chk_3.setChecked(true);
                    if (floor_chk_4.isChecked())
                        floor_chk_4.setChecked(false);
                    contentValues.put("Pivot_Alignment", "Y");
                    // }else{
                    // UtilityClass.showToast(context,
                    // "Please check if installtion is correct");
                    // floor_chk_3.setChecked(false);
                    // if (floor_chk_4.isChecked())floor_chk_4.setChecked(false);
                    // contentValues.put("Pivot_Alignment", "null");
                    // }

                } else {
                    contentValues.put("Pivot_Alignment", "null");
                }
                break;
            case R.id.floor_chk_4:
                if (isChecked) {

                    // if(contentValues.get("Correct_Installation")!= null) {
                    floor_chk_4.setChecked(true);
                    if (floor_chk_3.isChecked())
                        floor_chk_3.setChecked(false);
                    contentValues.put("Pivot_Alignment", "N");
                    if (floor_chk_5.isChecked())
                        floor_chk_5.setChecked(false);
                    if (floor_chk_6.isChecked())
                        floor_chk_6.setChecked(false);
                    if (floor_chk_7.isChecked())
                        floor_chk_7.setChecked(false);
                    if (floor_chk_8.isChecked())
                        floor_chk_8.setChecked(false);

                    floor_chk_5.setEnabled(false);
                    floor_chk_6.setEnabled(false);
                    floor_chk_7.setEnabled(false);
                    floor_chk_8.setEnabled(false);
                    // }else{
                    // UtilityClass.showToast(context,
                    // "Please check if installtion is correct");
                    // floor_chk_3.setChecked(false);
                    // if (floor_chk_3.isChecked())floor_chk_3.setChecked(false);
                    // contentValues.put("Pivot_Alignment", "null");
                    // }

                } else {
                    contentValues.put("Pivot_Alignment", "null");
                    floor_chk_5.setEnabled(true);
                    if (floor_chk_5.isChecked()) {

                    } else {
                        floor_chk_6.setEnabled(true);
                        floor_chk_7.setEnabled(true);
                        floor_chk_8.setEnabled(true);
                    }
                }
                break;
            case R.id.floor_chk_5:
                if (isChecked) {

                    // if(contentValues.get("Correct_Installation")!= null) {
                    floor_chk_5.setChecked(true);
                    if (floor_chk_6.isChecked())
                        floor_chk_6.setChecked(false);
                    contentValues.put("Door_Noise", "Y");
                    // }else{
                    // UtilityClass.showToast(context,
                    // "Please check if installtion is correct");
                    // floor_chk_3.setChecked(false);
                    // if (floor_chk_4.isChecked())floor_chk_4.setChecked(false);
                    // contentValues.put("Door_Noise", "null");
                    // }

                    contentValues.put("Door_Noise", "Y");
                    floor_chk_7.setEnabled(false);
                    floor_chk_8.setEnabled(false);

                    if (floor_chk_7.isChecked())
                        floor_chk_7.setChecked(false);
                    if (floor_chk_8.isChecked())
                        floor_chk_8.setChecked(false);
                    // faultReport.CutOut_Dimension = "9mm";
                } else {
                    contentValues.put("Door_Noise", "null");

                    floor_chk_7.setEnabled(true);
                    floor_chk_8.setEnabled(true);
                }
                break;
            case R.id.floor_chk_6:
                if (isChecked) {
                    if (floor_chk_5.isChecked())
                        floor_chk_5.setChecked(false);
                    contentValues.put("Door_Noise", "N");
                    // faultReport.CutOut_Dimension = "110mm";
                } else {
                    contentValues.put("Door_Noise", "null");
                }
                break;
            case R.id.floor_chk_7:
                if (isChecked) {
                    if (floor_chk_8.isChecked())
                        floor_chk_8.setChecked(false);
                    contentValues.put("Closing_Speed", "Y");
                } else {
                    contentValues.put("Closing_Speed", "null");
                }
                // if(isChecked)faultReport.CutOut_Dimension = "90mm";
                break;
            case R.id.floor_chk_8:
                if (isChecked) {
                    if (floor_chk_7.isChecked())
                        floor_chk_7.setChecked(false);
                    contentValues.put("Closing_Speed", "N");
                } else {
                    contentValues.put("Closing_Speed", "null");
                }
                break;

            case R.id.dc_chk_1:
                if (isChecked) {
                    if (dc_chk_2.isChecked())
                        dc_chk_2.setChecked(false);
                    contentValues.put("Door_Noise", "Y");
                    if (dc_chk_3.isChecked())
                        dc_chk_3.setChecked(false);
                    if (dc_chk_4.isChecked())
                        dc_chk_4.setChecked(false);
                    if (dc_chk_5.isChecked())
                        dc_chk_5.setChecked(false);
                    if (dc_chk_6.isChecked())
                        dc_chk_6.setChecked(false);
                    if (dc_chk_7.isChecked())
                        dc_chk_7.setChecked(false);
                    if (dc_chk_8.isChecked())
                        dc_chk_8.setChecked(false);
                    if (dc_chk_9.isChecked())
                        dc_chk_9.setChecked(false);
                    if (dc_chk_10.isChecked())
                        dc_chk_10.setChecked(false);
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_3.setEnabled(false);
                    dc_chk_4.setEnabled(false);
                    dc_chk_5.setEnabled(false);
                    dc_chk_6.setEnabled(false);
                    dc_chk_7.setEnabled(false);
                    dc_chk_8.setEnabled(false);
                    dc_chk_9.setEnabled(false);
                    dc_chk_10.setEnabled(false);
                    dc_chk_11.setEnabled(false);
                    dc_chk_12.setEnabled(false);
                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Door_Noise", "null");

                    dc_chk_3.setEnabled(true);
                    dc_chk_4.setEnabled(true);
                    dc_chk_5.setEnabled(true);
                    dc_chk_6.setEnabled(true);
                    dc_chk_7.setEnabled(true);
                    dc_chk_8.setEnabled(true);
                    dc_chk_9.setEnabled(true);
                    dc_chk_10.setEnabled(true);
                    dc_chk_11.setEnabled(true);
                    dc_chk_12.setEnabled(true);
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_2:
                if (isChecked) {
                    if (dc_chk_1.isChecked())
                        dc_chk_1.setChecked(false);
                    contentValues.put("Door_Noise", "N");
                } else {
                    contentValues.put("Door_Noise", "null");
                }
                break;

            case R.id.dc_chk_3:
                if (isChecked) {
                    if (dc_chk_4.isChecked())
                        dc_chk_4.setChecked(false);
                    contentValues.put("Screw_Fix", "Y");
                } else {
                    contentValues.put("Screw_Fix", "null");
                }
                break;
            case R.id.dc_chk_4:
                if (isChecked) {
                    if (dc_chk_3.isChecked())
                        dc_chk_3.setChecked(false);
                    contentValues.put("Screw_Fix", "N");
                    if (dc_chk_5.isChecked())
                        dc_chk_5.setChecked(false);
                    if (dc_chk_6.isChecked())
                        dc_chk_6.setChecked(false);
                    if (dc_chk_7.isChecked())
                        dc_chk_7.setChecked(false);
                    if (dc_chk_8.isChecked())
                        dc_chk_8.setChecked(false);
                    if (dc_chk_9.isChecked())
                        dc_chk_9.setChecked(false);
                    if (dc_chk_10.isChecked())
                        dc_chk_10.setChecked(false);
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_5.setEnabled(false);
                    dc_chk_6.setEnabled(false);
                    dc_chk_7.setEnabled(false);
                    dc_chk_8.setEnabled(false);
                    dc_chk_9.setEnabled(false);
                    dc_chk_10.setEnabled(false);
                    dc_chk_11.setEnabled(false);
                    dc_chk_12.setEnabled(false);
                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Screw_Fix", "null");
                    dc_chk_5.setEnabled(true);
                    dc_chk_6.setEnabled(true);
                    dc_chk_7.setEnabled(true);
                    dc_chk_8.setEnabled(true);
                    dc_chk_9.setEnabled(true);
                    dc_chk_10.setEnabled(true);
                    dc_chk_11.setEnabled(true);
                    dc_chk_12.setEnabled(true);
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_5:
                if (isChecked) {
                    if (dc_chk_6.isChecked())
                        dc_chk_6.setChecked(false);
                    contentValues.put("Edge_Distance", "Y");
                } else {
                    contentValues.put("Edge_Distance", "null");
                }
                break;
            case R.id.dc_chk_6:
                if (isChecked) {
                    if (dc_chk_5.isChecked())
                        dc_chk_5.setChecked(false);
                    contentValues.put("Edge_Distance", "N");

                    if (dc_chk_7.isChecked())
                        dc_chk_7.setChecked(false);
                    if (dc_chk_8.isChecked())
                        dc_chk_8.setChecked(false);
                    if (dc_chk_9.isChecked())
                        dc_chk_9.setChecked(false);
                    if (dc_chk_10.isChecked())
                        dc_chk_10.setChecked(false);
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_7.setEnabled(false);
                    dc_chk_8.setEnabled(false);
                    dc_chk_9.setEnabled(false);
                    dc_chk_10.setEnabled(false);
                    dc_chk_11.setEnabled(false);
                    dc_chk_12.setEnabled(false);
                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Edge_Distance", "null");
                    dc_chk_7.setEnabled(true);
                    dc_chk_8.setEnabled(true);
                    dc_chk_9.setEnabled(true);
                    dc_chk_10.setEnabled(true);
                    dc_chk_11.setEnabled(true);
                    dc_chk_12.setEnabled(true);
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_7:
                if (isChecked) {
                    if (dc_chk_8.isChecked())
                        dc_chk_8.setChecked(false);
                    contentValues.put("Door_Depth", "Y");
                } else {
                    contentValues.put("Door_Depth", "null");
                }
                break;
            case R.id.dc_chk_8:
                if (isChecked) {
                    if (dc_chk_7.isChecked())
                        dc_chk_7.setChecked(false);
                    contentValues.put("Door_Depth", "N");
                    if (dc_chk_9.isChecked())
                        dc_chk_9.setChecked(false);
                    if (dc_chk_10.isChecked())
                        dc_chk_10.setChecked(false);
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_9.setEnabled(false);
                    dc_chk_10.setEnabled(false);
                    dc_chk_11.setEnabled(false);
                    dc_chk_12.setEnabled(false);
                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Door_Depth", "null");
                    dc_chk_9.setEnabled(true);
                    dc_chk_10.setEnabled(true);
                    dc_chk_11.setEnabled(true);
                    dc_chk_12.setEnabled(true);
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_9:
                if (isChecked) {
                    if (dc_chk_10.isChecked())
                        dc_chk_10.setChecked(false);
                    contentValues.put("Frame_Depth", "Y");
                } else {
                    contentValues.put("Frame_Depth", "null");
                }
                break;
            case R.id.dc_chk_10:
                if (isChecked) {
                    if (dc_chk_9.isChecked())
                        dc_chk_9.setChecked(false);
                    contentValues.put("Frame_Depth", "N");
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_11.setEnabled(false);
                    dc_chk_12.setEnabled(false);
                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Frame_Depth", "null");
                    dc_chk_11.setEnabled(true);
                    dc_chk_12.setEnabled(true);
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_11:
                if (isChecked) {
                    if (dc_chk_12.isChecked())
                        dc_chk_12.setChecked(false);
                    contentValues.put("Centre_Cut_Out", "Y");
                } else {
                    contentValues.put("Centre_Cut_Out", "null");
                }
                break;
            case R.id.dc_chk_12:
                if (isChecked) {
                    if (dc_chk_11.isChecked())
                        dc_chk_11.setChecked(false);
                    if (isChecked)
                        contentValues.put("Centre_Cut_Out", "N");

                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_13.setEnabled(false);
                    dc_chk_14.setEnabled(false);
                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Centre_Cut_Out", "null");
                    dc_chk_13.setEnabled(true);
                    dc_chk_14.setEnabled(true);
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_13:
                if (isChecked) {
                    if (dc_chk_14.isChecked())
                        dc_chk_14.setChecked(false);
                    contentValues.put("Latching_Speed", "Y");
                } else {
                    contentValues.put("Latching_Speed", "null");
                }
                break;
            case R.id.dc_chk_14:
                if (isChecked) {
                    if (dc_chk_13.isChecked())
                        dc_chk_13.setChecked(false);
                    contentValues.put("Latching_Speed", "N");

                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);

                    dc_chk_15.setEnabled(false);
                    dc_chk_16.setEnabled(false);
                } else {
                    contentValues.put("Latching_Speed", "null");
                    dc_chk_15.setEnabled(true);
                    dc_chk_16.setEnabled(true);
                }
                break;
            case R.id.dc_chk_15:
                if (isChecked) {
                    if (dc_chk_16.isChecked())
                        dc_chk_16.setChecked(false);
                    contentValues.put("Closing_Speed", "Y");
                } else {
                    contentValues.put("Closing_Speed", "null");
                }
                break;
            case R.id.dc_chk_16:
                if (isChecked) {
                    if (dc_chk_15.isChecked())
                        dc_chk_15.setChecked(false);
                    contentValues.put("Closing_Speed", "N");
                } else {
                    contentValues.put("Closing_Speed", "null");
                }
                break;

            case R.id.ls_chk_1:
                if (isChecked) {
                    if (ls_chk_2.isChecked())
                        ls_chk_2.setChecked(false);
                    contentValues.put("Product_Power_Factor", "Y");
                } else {
                    contentValues.put("Product_Power_Factor", "null");
                }
                break;
            case R.id.ls_chk_2:
                if (isChecked) {
                    if (ls_chk_1.isChecked())
                        ls_chk_1.setChecked(false);
                    contentValues.put("Product_Power_Factor", "N");
                    if (ls_chk_3.isChecked())
                        ls_chk_3.setChecked(false);
                    if (ls_chk_4.isChecked())
                        ls_chk_4.setChecked(false);
                    if (ls_chk_5.isChecked())
                        ls_chk_5.setChecked(false);
                    if (ls_chk_6.isChecked())
                        ls_chk_6.setChecked(false);
                    if (ls_chk_7.isChecked())
                        ls_chk_7.setChecked(false);
                    if (ls_chk_8.isChecked())
                        ls_chk_8.setChecked(false);
                    if (ls_chk_9.isChecked())
                        ls_chk_9.setChecked(false);
                    if (ls_chk_10.isChecked())
                        ls_chk_10.setChecked(false);

                    ls_chk_3.setEnabled(false);
                    ls_chk_4.setEnabled(false);
                    ls_chk_5.setEnabled(false);
                    ls_chk_6.setEnabled(false);
                    ls_chk_7.setEnabled(false);
                    ls_chk_8.setEnabled(false);
                    ls_chk_9.setEnabled(false);
                    ls_chk_10.setEnabled(false);
                } else {
                    contentValues.put("Product_Power_Factor", "null");
                    ls_chk_3.setEnabled(true);
                    ls_chk_4.setEnabled(true);
                    ls_chk_5.setEnabled(true);
                    ls_chk_6.setEnabled(true);
                    ls_chk_7.setEnabled(true);
                    ls_chk_8.setEnabled(true);
                    ls_chk_9.setEnabled(true);
                    ls_chk_10.setEnabled(true);
                }
                break;
            case R.id.ls_chk_3:
                if (isChecked) {
                    if (ls_chk_4.isChecked())
                        ls_chk_4.setChecked(false);
                    contentValues.put("Lift_Mechanism_Installed", "Y");
                } else {
                    contentValues.put("Lift_Mechanism_Installed", "null");
                }
                break;
            case R.id.ls_chk_4:
                if (isChecked) {
                    if (ls_chk_3.isChecked())
                        ls_chk_3.setChecked(false);
                    contentValues.put("Lift_Mechanism_Installed", "N");
                    if (ls_chk_5.isChecked())
                        ls_chk_5.setChecked(false);
                    if (ls_chk_6.isChecked())
                        ls_chk_6.setChecked(false);
                    if (ls_chk_7.isChecked())
                        ls_chk_7.setChecked(false);
                    if (ls_chk_8.isChecked())
                        ls_chk_8.setChecked(false);
                    if (ls_chk_9.isChecked())
                        ls_chk_9.setChecked(false);
                    if (ls_chk_10.isChecked())
                        ls_chk_10.setChecked(false);

                    ls_chk_5.setEnabled(false);
                    ls_chk_6.setEnabled(false);
                    ls_chk_7.setEnabled(false);
                    ls_chk_8.setEnabled(false);
                    ls_chk_9.setEnabled(false);
                    ls_chk_10.setEnabled(false);
                } else {
                    contentValues.put("Lift_Mechanism_Installed", "null");
                    ls_chk_5.setEnabled(true);
                    ls_chk_6.setEnabled(true);
                    ls_chk_7.setEnabled(true);
                    ls_chk_8.setEnabled(true);
                    ls_chk_9.setEnabled(true);
                    ls_chk_10.setEnabled(true);
                }
                break;
            case R.id.ls_chk_5:
                if (isChecked) {
                    if (ls_chk_6.isChecked())
                        ls_chk_6.setChecked(false);
                    contentValues.put("Arms_Installed", "Y");
                } else {
                    contentValues.put("Arms_Installed", "null");
                }
                break;
            case R.id.ls_chk_6:
                if (isChecked) {
                    if (ls_chk_5.isChecked())
                        ls_chk_5.setChecked(false);
                    contentValues.put("Arms_Installed", "N");

                    if (ls_chk_7.isChecked())
                        ls_chk_7.setChecked(false);
                    if (ls_chk_8.isChecked())
                        ls_chk_8.setChecked(false);
                    if (ls_chk_9.isChecked())
                        ls_chk_9.setChecked(false);
                    if (ls_chk_10.isChecked())
                        ls_chk_10.setChecked(false);

                    ls_chk_7.setEnabled(false);
                    ls_chk_8.setEnabled(false);
                    ls_chk_9.setEnabled(false);
                    ls_chk_10.setEnabled(false);
                } else {
                    contentValues.put("Arms_Installed", "null");
                    ls_chk_7.setEnabled(true);
                    ls_chk_8.setEnabled(true);
                    ls_chk_9.setEnabled(true);
                    ls_chk_10.setEnabled(true);
                }
                break;
            case R.id.ls_chk_7:
                if (isChecked) {
                    if (ls_chk_8.isChecked())
                        ls_chk_8.setChecked(false);
                    contentValues.put("Arms_Size", "Y");
                } else {
                    contentValues.put("Arms_Size", "null");
                }
                break;
            case R.id.ls_chk_8:
                if (isChecked) {
                    if (ls_chk_7.isChecked())
                        ls_chk_7.setChecked(false);
                    contentValues.put("Arms_Size", "N");
                    if (ls_chk_9.isChecked())
                        ls_chk_9.setChecked(false);
                    if (ls_chk_10.isChecked())
                        ls_chk_10.setChecked(false);
                    ls_chk_9.setEnabled(false);
                    ls_chk_10.setEnabled(false);
                } else {
                    contentValues.put("Arms_Size", "null");
                    ls_chk_9.setEnabled(true);
                    ls_chk_10.setEnabled(true);
                }
                break;
            case R.id.ls_chk_9:
                if (isChecked) {
                    if (ls_chk_10.isChecked())
                        ls_chk_10.setChecked(false);
                    contentValues.put("Power_Adjustment", "Y");
                } else {
                    contentValues.put("Power_Adjustment", "null");
                }
                break;
            case R.id.ls_chk_10:
                if (isChecked) {
                    if (ls_chk_9.isChecked())
                        ls_chk_9.setChecked(false);
                    contentValues.put("Power_Adjustment", "N");
                } else {
                    contentValues.put("Power_Adjustment", "null");
                }
                break;

            case R.id.sf_chk_1:
                if (isChecked) {
                    if (sf_chk_2.isChecked())
                        sf_chk_2.setChecked(false);
                    contentValues.put("Door_Weight_Within_Range", "Y");
                    spin_wrong_product.setSelection(0);
                    spin_wrong_product.setEnabled(false);
                } else {
                    contentValues.put("Door_Weight_Within_Range", "null");
                }
                break;
            case R.id.sf_chk_2:
                if (isChecked) {
                    if (sf_chk_1.isChecked())
                        sf_chk_1.setChecked(false);
                    contentValues.put("Door_Weight_Within_Range", "N");
                    if (sf_chk_3.isChecked())
                        sf_chk_3.setChecked(false);
                    if (sf_chk_4.isChecked())
                        sf_chk_4.setChecked(false);
                    if (sf_chk_5.isChecked())
                        sf_chk_5.setChecked(false);
                    if (sf_chk_6.isChecked())
                        sf_chk_6.setChecked(false);
                    if (sf_chk_7.isChecked())
                        sf_chk_7.setChecked(false);
                    if (sf_chk_8.isChecked())
                        sf_chk_8.setChecked(false);
                    if (sf_chk_9.isChecked())
                        sf_chk_9.setChecked(false);
                    if (sf_chk_10.isChecked())
                        sf_chk_10.setChecked(false);
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);

                    sf_chk_3.setEnabled(false);
                    sf_chk_4.setEnabled(false);
                    sf_chk_5.setEnabled(false);
                    sf_chk_6.setEnabled(false);
                    sf_chk_7.setEnabled(false);
                    sf_chk_8.setEnabled(false);
                    sf_chk_9.setEnabled(false);
                    sf_chk_10.setEnabled(false);
                    sf_chk_11.setEnabled(false);
                    sf_chk_12.setEnabled(false);
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);

                    spin_wrong_product.setSelection(0);
                    spin_wrong_product.setEnabled(true);
/*
                    if (product_sub_category.contains("Sliding Wardrobe Straight Fitting"))
                    {
                            if (sf_chk_13.isChecked())
                                sf_chk_13.setChecked(false);

                            if (sf_chk_14.isChecked())
                                sf_chk_14.setChecked(false);
                        sf_chk_13.setEnabled(false);
                        sf_chk_14.setEnabled(false);
                    }*/


                } else {
                    contentValues.put("Door_Weight_Within_Range", "null");
                    sf_chk_3.setEnabled(true);
                    sf_chk_4.setEnabled(true);
                    sf_chk_5.setEnabled(true);
                    sf_chk_6.setEnabled(true);
                    sf_chk_7.setEnabled(true);
                    sf_chk_8.setEnabled(true);
                    sf_chk_9.setEnabled(true);
                    sf_chk_10.setEnabled(true);
                    sf_chk_11.setEnabled(true);
                    sf_chk_12.setEnabled(true);
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);
                    spin_wrong_product.setSelection(0);
                    spin_wrong_product.setEnabled(true);
                }
                break;
            case R.id.sf_chk_3:
                if (isChecked) {
                    if (sf_chk_4.isChecked())
                        sf_chk_4.setChecked(false);
                    contentValues.put("Carcase_Requirement", "Y");
                } else {
                    contentValues.put("Carcase_Requirement", "null");
                }
                break;
            case R.id.sf_chk_4:
                if (isChecked) {
                    if (sf_chk_3.isChecked())
                        sf_chk_3.setChecked(false);
                    contentValues.put("Carcase_Requirement", "N");
                    if (sf_chk_5.isChecked())
                        sf_chk_5.setChecked(false);
                    if (sf_chk_6.isChecked())
                        sf_chk_6.setChecked(false);
                    if (sf_chk_7.isChecked())
                        sf_chk_7.setChecked(false);
                    if (sf_chk_8.isChecked())
                        sf_chk_8.setChecked(false);
                    if (sf_chk_9.isChecked())
                        sf_chk_9.setChecked(false);
                    if (sf_chk_10.isChecked())
                        sf_chk_10.setChecked(false);
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);
                    sf_chk_5.setEnabled(false);
                    sf_chk_6.setEnabled(false);
                    sf_chk_7.setEnabled(false);
                    sf_chk_8.setEnabled(false);
                    sf_chk_9.setEnabled(false);
                    sf_chk_10.setEnabled(false);
                    sf_chk_11.setEnabled(false);
                    sf_chk_12.setEnabled(false);
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);
                } else {
                    contentValues.put("Carcase_Requirement", "null");
                    sf_chk_5.setEnabled(true);
                    sf_chk_6.setEnabled(true);
                    sf_chk_7.setEnabled(true);
                    sf_chk_8.setEnabled(true);
                    sf_chk_9.setEnabled(true);
                    sf_chk_10.setEnabled(true);
                    sf_chk_11.setEnabled(true);
                    sf_chk_12.setEnabled(true);
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);
                }
                break;
            case R.id.sf_chk_5:
                if (isChecked) {
                    if (sf_chk_6.isChecked())
                        sf_chk_6.setChecked(false);
                    contentValues.put("Installation", "Y");
                } else {
                    contentValues.put("Installation", "null");
                }
                break;
            case R.id.sf_chk_6:
                if (isChecked) {
                    if (sf_chk_5.isChecked())
                        sf_chk_5.setChecked(false);
                    contentValues.put("Installation", "N");

                    if (sf_chk_7.isChecked())
                        sf_chk_7.setChecked(false);
                    if (sf_chk_8.isChecked())
                        sf_chk_8.setChecked(false);
                    if (sf_chk_9.isChecked())
                        sf_chk_9.setChecked(false);
                    if (sf_chk_10.isChecked())
                        sf_chk_10.setChecked(false);
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);
                    sf_chk_7.setEnabled(false);
                    sf_chk_8.setEnabled(false);
                    sf_chk_9.setEnabled(false);
                    sf_chk_10.setEnabled(false);
                    sf_chk_11.setEnabled(false);
                    sf_chk_12.setEnabled(false);
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);


                } else {
                    contentValues.put("Installation", "null");
                    sf_chk_7.setEnabled(true);
                    sf_chk_8.setEnabled(true);
                    sf_chk_9.setEnabled(true);
                    sf_chk_10.setEnabled(true);
                    sf_chk_11.setEnabled(true);
                    sf_chk_12.setEnabled(true);
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);

                }
                break;
            case R.id.sf_chk_7:
                if (isChecked) {
                    if (sf_chk_8.isChecked())
                        sf_chk_8.setChecked(false);
                    contentValues.put("Track_Roller_Clean", "Y");
                } else {
                    contentValues.put("Track_Roller_Clean", "null");
                }
                break;
            case R.id.sf_chk_8:
                if (isChecked) {
                    if (sf_chk_7.isChecked())
                        sf_chk_7.setChecked(false);
                    contentValues.put("Track_Roller_Clean", "N");
                    if (sf_chk_9.isChecked())
                        sf_chk_9.setChecked(false);
                    if (sf_chk_10.isChecked())
                        sf_chk_10.setChecked(false);
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);

                    sf_chk_9.setEnabled(false);
                    sf_chk_10.setEnabled(false);
                    sf_chk_11.setEnabled(false);
                    sf_chk_12.setEnabled(false);
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);
                } else {
                    contentValues.put("Track_Roller_Clean", "null");
                    sf_chk_9.setEnabled(true);
                    sf_chk_10.setEnabled(true);
                    sf_chk_11.setEnabled(true);
                    sf_chk_12.setEnabled(true);
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);
                }
                break;
            case R.id.sf_chk_9:
                if (isChecked) {
                    if (sf_chk_10.isChecked())
                        sf_chk_10.setChecked(false);
                    contentValues.put("Door_Wrapped", "Y");
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);
                    sf_chk_11.setEnabled(false);
                    sf_chk_12.setEnabled(false);
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);


                } else {
                    contentValues.put("Door_Wrapped", "null");
                    sf_chk_11.setEnabled(true);
                    sf_chk_12.setEnabled(true);
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);
                }
                break;
            case R.id.sf_chk_10:
                if (isChecked) {
                    if (sf_chk_9.isChecked())
                        sf_chk_9.setChecked(false);
                    contentValues.put("Door_Wrapped", "N");

                } else {
                    contentValues.put("Door_Wrapped", "null");
                }
                break;
            case R.id.sf_chk_11:

                if (isChecked) {
                    if (sf_chk_12.isChecked())
                        sf_chk_12.setChecked(false);
                    contentValues.put("Plan_Used", "Y");
                    sf_chk_13.setEnabled(true);
                    sf_chk_14.setEnabled(true);
                } else {
                    contentValues.put("Plan_Used", "null");
                    sf_chk_13.setEnabled(false);
                    sf_chk_14.setEnabled(false);

                }

                break;
            case R.id.sf_chk_12:

                if (isChecked) {
                    if (sf_chk_11.isChecked())
                        sf_chk_11.setChecked(false);
                    if (sf_chk_13.isChecked())
                        sf_chk_13.setChecked(false);
                    if (sf_chk_14.isChecked())
                        sf_chk_14.setChecked(false);
                    contentValues.put("Plan_Used", "N");
                } else {
                    contentValues.put("Plan_Used", "null");
                }

                break;

            case R.id.sf_chk_13:
                if (product_category.contains("Furniture Fitting-Sliding Fittings")) {
                    if (isChecked) {
                        if (sf_chk_14.isChecked())
                            sf_chk_14.setChecked(false);
                        contentValues.put("Softclose_Broken", "Y");
                    } else {
                        contentValues.put("Softclose_Broken", "null");
                    }
                }
                break;
            case R.id.sf_chk_14:
                if (product_category.contains("Furniture Fitting-Sliding Fittings")) {
                    if (isChecked) {
                        if (sf_chk_13.isChecked())
                            sf_chk_13.setChecked(false);
                        contentValues.put("Softclose_Broken", "N");
                    } else {
                        contentValues.put("Softclose_Broken", "null");
                    }
                }

                break;


            case R.id.vc_chk_1:
                if (isChecked) {
                    if (vc_chk_2.isChecked())
                        vc_chk_2.setChecked(false);
                    contentValues.put("Weight_Length_Correct", "Y");
                    try {
                        double wt = Double.parseDouble(vol_one.getText().toString()
                                .replace(" = ", "").replace(" (Sides) ", ""))
                                + Double.parseDouble(vol_two.getText().toString()
                                .replace(" = ", "").replace(" (Back) ", ""))
                                + Double.parseDouble(vol_three.getText().toString()
                                .replace(" = ", "").replace(" (Base) ", ""))
                                + Double.parseDouble(vol_four.getText().toString()
                                .replace(" = ", "")
                                .replace(" (Facia) ", ""));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));
                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Double.parseDouble(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Double.parseDouble(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentValues.put("Weight_Length_Correct", "null");
                }
                break;
            case R.id.vc_chk_2:
                if (isChecked) {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (vc_chk_1.isChecked())
                        vc_chk_1.setChecked(false);
                    contentValues.put("Weight_Length_Correct", "N");
                    if (rc_chk_1.isChecked())
                        rc_chk_1.setChecked(false);
                    if (rc_chk_2.isChecked())
                        rc_chk_2.setChecked(false);
                    if (rc_chk_3.isChecked())
                        rc_chk_3.setChecked(false);
                    if (rc_chk_4.isChecked())
                        rc_chk_4.setChecked(false);
                    if (rc_chk_5.isChecked())
                        rc_chk_5.setChecked(false);
                    if (rc_chk_6.isChecked())
                        rc_chk_6.setChecked(false);
                    if (rc_chk_7.isChecked())
                        rc_chk_7.setChecked(false);
                    if (rc_chk_8.isChecked())
                        rc_chk_8.setChecked(false);
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);

                    rc_chk_1.setEnabled(false);
                    rc_chk_2.setEnabled(false);
                    rc_chk_3.setEnabled(false);
                    rc_chk_4.setEnabled(false);
                    rc_chk_5.setEnabled(false);
                    rc_chk_6.setEnabled(false);
                    rc_chk_7.setEnabled(false);
                    rc_chk_8.setEnabled(false);
                    rc_chk_9.setEnabled(false);
                    rc_chk_10.setEnabled(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);

                } else {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    contentValues.put("Weight_Length_Correct", "null");
                    rc_chk_1.setEnabled(true);
                    rc_chk_2.setEnabled(true);
                    rc_chk_3.setEnabled(true);
                    rc_chk_4.setEnabled(true);
                    rc_chk_5.setEnabled(true);
                    rc_chk_6.setEnabled(true);
                    rc_chk_7.setEnabled(true);
                    rc_chk_8.setEnabled(true);
                    rc_chk_9.setEnabled(true);
                    rc_chk_10.setEnabled(true);
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;

            case R.id.rc_chk_1:
                if (isChecked) {
                    if (drawer_wt.getText().toString() != null) {
                        // try{
                        // drawer_wt_tot.setText(String.valueOf(calculateWeight(Integer.parseInt(drawer_wt.getText().toString()),
                        // 1, 1, "w")));
                        // contentValues.put("Drawer_Wt",
                        // drawer_wt_tot.getText().toString());
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    }

                    if (rc_chk_2.isChecked())
                        rc_chk_2.setChecked(false);
                    contentValues.put("Content_weight_Within_Range", "Y");
                } else {
                    contentValues.put("Content_weight_Within_Range", "null");
                }
                break;
            case R.id.rc_chk_2:
                if (isChecked) {
                    if (drawer_wt.getText().toString() != null) {
                        // try{
                        // drawer_wt_tot.setText(String.valueOf(calculateWeight(Integer.parseInt(drawer_wt.getText().toString()),
                        // 1, 1, "w")));
                        // contentValues.put("Drawer_Wt",
                        // drawer_wt_tot.getText().toString());
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    }

                    if (rc_chk_1.isChecked())
                        rc_chk_1.setChecked(false);

                    contentValues.put("Content_weight_Within_Range", "N");
                    if (rc_chk_3.isChecked())
                        rc_chk_3.setChecked(false);
                    if (rc_chk_4.isChecked())
                        rc_chk_4.setChecked(false);
                    if (rc_chk_5.isChecked())
                        rc_chk_5.setChecked(false);
                    if (rc_chk_6.isChecked())
                        rc_chk_6.setChecked(false);
                    if (rc_chk_7.isChecked())
                        rc_chk_7.setChecked(false);
                    if (rc_chk_8.isChecked())
                        rc_chk_8.setChecked(false);
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);

                    rc_chk_3.setEnabled(false);
                    rc_chk_4.setEnabled(false);
                    rc_chk_5.setEnabled(false);
                    rc_chk_6.setEnabled(false);
                    rc_chk_7.setEnabled(false);
                    rc_chk_8.setEnabled(false);
                    rc_chk_9.setEnabled(false);
                    rc_chk_10.setEnabled(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);
                } else {
                    contentValues.put("Content_weight_Within_Range", "null");
                    rc_chk_3.setEnabled(true);
                    rc_chk_4.setEnabled(true);
                    rc_chk_5.setEnabled(true);
                    rc_chk_6.setEnabled(true);
                    rc_chk_7.setEnabled(true);
                    rc_chk_8.setEnabled(true);
                    rc_chk_9.setEnabled(true);
                    rc_chk_10.setEnabled(true);
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;
            case R.id.rc_chk_3:
                if (isChecked) {
                    if (rc_chk_4.isChecked())
                        rc_chk_4.setChecked(false);
                    contentValues.put("Drawer_Construction_Alignment", "Y");
                } else {
                    contentValues.put("Drawer_Construction_Alignment", "null");
                }
                break;
            case R.id.rc_chk_4:
                if (isChecked) {
                    if (rc_chk_3.isChecked())
                        rc_chk_3.setChecked(false);
                    contentValues.put("Drawer_Construction_Alignment", "N");
                    if (rc_chk_5.isChecked())
                        rc_chk_5.setChecked(false);
                    if (rc_chk_6.isChecked())
                        rc_chk_6.setChecked(false);
                    if (rc_chk_7.isChecked())
                        rc_chk_7.setChecked(false);
                    if (rc_chk_8.isChecked())
                        rc_chk_8.setChecked(false);
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);

                    rc_chk_5.setEnabled(false);
                    rc_chk_6.setEnabled(false);
                    rc_chk_7.setEnabled(false);
                    rc_chk_8.setEnabled(false);
                    rc_chk_9.setEnabled(false);
                    rc_chk_10.setEnabled(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);

                } else {
                    contentValues.put("Drawer_Construction_Alignment", "null");
                    rc_chk_5.setEnabled(true);
                    rc_chk_6.setEnabled(true);
                    rc_chk_7.setEnabled(true);
                    rc_chk_8.setEnabled(true);
                    rc_chk_9.setEnabled(true);
                    rc_chk_10.setEnabled(true);
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;
            case R.id.rc_chk_5:
                if (isChecked) {
                    if (rc_chk_6.isChecked())
                        rc_chk_6.setChecked(false);
                    contentValues.put("Product_System_Installation_32", "Y");
                } else {
                    contentValues.put("Product_System_Installation_32", "null");
                }
                break;
            case R.id.rc_chk_6:
                if (isChecked) {
                    if (rc_chk_5.isChecked())
                        rc_chk_5.setChecked(false);
                    contentValues.put("Product_System_Installation_32", "N");

                    if (rc_chk_7.isChecked())
                        rc_chk_7.setChecked(false);
                    if (rc_chk_8.isChecked())
                        rc_chk_8.setChecked(false);
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);

                    rc_chk_7.setEnabled(false);
                    rc_chk_8.setEnabled(false);
                    rc_chk_9.setEnabled(false);
                    rc_chk_10.setEnabled(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);
                } else {
                    contentValues.put("Product_System_Installation_32", "null");
                    rc_chk_7.setEnabled(true);
                    rc_chk_8.setEnabled(true);
                    rc_chk_9.setEnabled(true);
                    rc_chk_10.setEnabled(true);
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;
            case R.id.rc_chk_7:
                if (isChecked) {
                    if (rc_chk_8.isChecked())
                        rc_chk_8.setChecked(false);
                    contentValues.put("Drawer_Noise_Check", "Y");
                } else {
                    contentValues.put("Drawer_Noise_Check", "null");
                }
                break;
            case R.id.rc_chk_8:
                if (isChecked) {
                    if (rc_chk_7.isChecked())
                        rc_chk_7.setChecked(false);
                    contentValues.put("Drawer_Noise_Check", "N");

                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);

                    rc_chk_9.setEnabled(false);
                    rc_chk_10.setEnabled(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);
                } else {
                    contentValues.put("Drawer_Noise_Check", "null");
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);
                    rc_chk_9.setEnabled(true);
                    rc_chk_10.setEnabled(true);
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;
            case R.id.rc_chk_9:
                if (isChecked) {
                    if (rc_chk_10.isChecked())
                        rc_chk_10.setChecked(false);
                    contentValues.put("Drawer_Runner_Clean", "Y");
                } else {
                    contentValues.put("Drawer_Runner_Clean", "null");
                }
                break;
            case R.id.rc_chk_10:
                if (isChecked) {
                    if (rc_chk_9.isChecked())
                        rc_chk_9.setChecked(false);
                    contentValues.put("Drawer_Runner_Clean", "N");
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);
                    rc_chk_11.setEnabled(false);
                    rc_chk_12.setEnabled(false);
                } else {
                    contentValues.put("Drawer_Runner_Clean", "null");
                    rc_chk_11.setEnabled(true);
                    rc_chk_12.setEnabled(true);
                }
                break;
            case R.id.rc_chk_11:
                if (isChecked) {
                    if (rc_chk_12.isChecked())
                        rc_chk_12.setChecked(false);
                    contentValues.put("Blum_Product_Servodrive", "Y");
                } else {
                    contentValues.put("Blum_Product_Servodrive", "null");
                }
                break;
            case R.id.rc_chk_12:
                if (isChecked) {
                    if (rc_chk_11.isChecked())
                        rc_chk_11.setChecked(false);
                    contentValues.put("Blum_Product_Servodrive", "N");
                } else {
                    contentValues.put("Blum_Product_Servodrive", "null");
                }
                break;

            case R.id.dh_chk_1:
                if (isChecked) {
                    if (dh_chk_2.isChecked())
                        dh_chk_2.setChecked(false);
                    contentValues.put("Mortise_Lock_Template", "Y");
                } else {
                    contentValues.put("Mortise_Lock_Template", "null");
                }
                break;
            case R.id.dh_chk_2:
                if (isChecked) {
                    if (dh_chk_1.isChecked())
                        dh_chk_1.setChecked(false);
                    contentValues.put("Mortise_Lock_Template", "N");
                    if (dh_chk_3.isChecked())
                        dh_chk_3.setChecked(false);
                    if (dh_chk_4.isChecked())
                        dh_chk_4.setChecked(false);
                    if (dh_chk_5.isChecked())
                        dh_chk_5.setChecked(false);
                    if (dh_chk_6.isChecked())
                        dh_chk_6.setChecked(false);
                    if (dh_chk_7.isChecked())
                        dh_chk_7.setChecked(false);
                    if (dh_chk_8.isChecked())
                        dh_chk_8.setChecked(false);
                    if (dh_chk_9.isChecked())
                        dh_chk_9.setChecked(false);
                    if (dh_chk_10.isChecked())
                        dh_chk_10.setChecked(false);
                    if (dh_chk_11.isChecked())
                        dh_chk_11.setChecked(false);
                    if (dh_chk_12.isChecked())
                        dh_chk_12.setChecked(false);
                    if (dh_chk_13.isChecked())
                        dh_chk_13.setChecked(false);
                    if (dh_chk_14.isChecked())
                        dh_chk_14.setChecked(false);
                    if (dh_chk_15.isChecked())
                        dh_chk_15.setChecked(false);

                    dh_chk_3.setEnabled(false);
                    dh_chk_4.setEnabled(false);
                    dh_chk_5.setEnabled(false);
                    dh_chk_6.setEnabled(false);
                    dh_chk_7.setEnabled(false);
                    dh_chk_8.setEnabled(false);
                    dh_chk_9.setEnabled(false);
                    dh_chk_10.setEnabled(false);
                    dh_chk_11.setEnabled(false);
                    dh_chk_12.setEnabled(false);
                    dh_chk_13.setEnabled(false);
                    dh_chk_14.setEnabled(false);
                    dh_chk_15.setEnabled(false);
                } else {
                    contentValues.put("Mortise_Lock_Template", "null");
                    if (dh_chk_10.isChecked()) {
                        dh_chk_3.setEnabled(true);
                        dh_chk_4.setEnabled(true);
                        dh_chk_5.setEnabled(true);
                        dh_chk_6.setEnabled(true);
                        dh_chk_7.setEnabled(true);
                        dh_chk_8.setEnabled(true);
                        dh_chk_9.setEnabled(true);
                        dh_chk_10.setEnabled(true);
                        dh_chk_11.setEnabled(true);
                        dh_chk_12.setEnabled(false);
                        dh_chk_13.setEnabled(false);
                        dh_chk_14.setEnabled(false);
                        dh_chk_15.setEnabled(false);
                    } else {
                        dh_chk_3.setEnabled(true);
                        dh_chk_4.setEnabled(true);
                        dh_chk_5.setEnabled(true);
                        dh_chk_6.setEnabled(true);
                        dh_chk_7.setEnabled(true);
                        dh_chk_8.setEnabled(true);
                        dh_chk_9.setEnabled(true);
                        dh_chk_10.setEnabled(true);
                        dh_chk_11.setEnabled(true);
                        dh_chk_12.setEnabled(true);
                        dh_chk_13.setEnabled(true);
                        dh_chk_14.setEnabled(true);
                        dh_chk_15.setEnabled(true);
                    }
                }
                break;
            case R.id.dh_chk_3:
                if (isChecked) {
                    contentValues.put("Lock_Protruding", "Y");
                    dh_chk_4.setChecked(false);
                    dh_chk_5.setChecked(false);

                } else {
                    contentValues.put("Lock_Protruding", "null");
                }
                break;
            case R.id.dh_chk_4:
                if (isChecked) {
                    dh_chk_3.setChecked(false);
                    dh_chk_5.setChecked(false);
                    contentValues.put("Plastic_Fillers", "Y");
                } else {
                    contentValues.put("Plastic_Fillers", "null");
                }
                break;
            case R.id.dh_chk_5:
                if (isChecked) {
                    dh_chk_4.setChecked(false);
                    dh_chk_3.setChecked(false);
                    contentValues.put("No_Mortise_Lock", "Y");
                    // dh_chk_6.setEnabled(false);
                    // dh_chk_7.setEnabled(false);
                    // dh_chk_8.setEnabled(false);
                    // dh_chk_9.setEnabled(false);
                    // dh_chk_10.setEnabled(false);
                    // dh_chk_11.setEnabled(false);
                    // dh_chk_12.setEnabled(false);
                    // dh_chk_13.setEnabled(false);
                    // dh_chk_14.setEnabled(false);
                    // dh_chk_15.setEnabled(false);
                } else {
                    contentValues.put("No_Mortise_Lock", "null");
                    // dh_chk_6.setEnabled(true);
                    // dh_chk_7.setEnabled(true);
                    // dh_chk_8.setEnabled(true);
                    // dh_chk_9.setEnabled(true);
                    // dh_chk_10.setEnabled(true);
                    // dh_chk_11.setEnabled(true);
                    // dh_chk_12.setEnabled(true);
                    // dh_chk_13.setEnabled(true);
                    // dh_chk_14.setEnabled(true);
                    // dh_chk_15.setEnabled(true);
                }
                break;
            case R.id.dh_chk_6:
                if (isChecked) {
                    contentValues.put("Door_Range_More", "Y");
                    // dh_chk_6.setEnabled(false);
                    dh_chk_7.setChecked(false);
                    dh_chk_8.setChecked(false);
                    dh_chk_9.setChecked(false);
                } else {
                    contentValues.put("Door_Range_More", "null");
                }
                break;
            case R.id.dh_chk_7:
                if (isChecked) {
                    contentValues.put("Door_Range_Less", "Y");
                    dh_chk_6.setChecked(false);
                    // dh_chk_7.setEnabled(false);
                    dh_chk_8.setChecked(false);
                    dh_chk_9.setChecked(false);
                } else {
                    contentValues.put("Door_Range_Less", "null");
                }
                break;
            case R.id.dh_chk_8:
                if (isChecked) {
                    contentValues.put("Rose_Touching_Door", "Y");
                    dh_chk_6.setChecked(false);
                    dh_chk_7.setChecked(false);
                    // dh_chk_8.setEnabled(false);
                    dh_chk_9.setChecked(false);
                } else {
                    contentValues.put("Rose_Touching_Door", "null");
                }
                break;
            case R.id.dh_chk_9:
                if (isChecked) {
                    contentValues.put("Spindle_Size_Correct", "Y");
                    dh_chk_6.setChecked(false);
                    dh_chk_7.setChecked(false);
                    dh_chk_8.setChecked(false);
                    // dh_chk_9.setEnabled(false);
                } else {
                    contentValues.put("Spindle_Size_Correct", "null");
                }
                break;
            case R.id.dh_chk_10:
                if (isChecked) {
                    if (dh_chk_11.isChecked())
                        dh_chk_11.setChecked(false);
                    contentValues.put("Spoiled_Spindle", "Y");
                    if (dh_chk_11.isChecked())
                        dh_chk_11.setChecked(false);
                    if (dh_chk_12.isChecked())
                        dh_chk_12.setChecked(false);
                    if (dh_chk_13.isChecked())
                        dh_chk_13.setChecked(false);
                    if (dh_chk_14.isChecked())
                        dh_chk_14.setChecked(false);
                    if (dh_chk_15.isChecked())
                        dh_chk_15.setChecked(false);

                    dh_chk_12.setEnabled(false);
                    dh_chk_13.setEnabled(false);
                    dh_chk_14.setEnabled(false);
                    dh_chk_15.setEnabled(false);
                } else {
                    contentValues.put("Spoiled_Spindle", "null");

                    dh_chk_12.setEnabled(true);
                    dh_chk_13.setEnabled(true);
                    dh_chk_14.setEnabled(true);
                    dh_chk_15.setEnabled(true);
                }
                break;
            case R.id.dh_chk_11:
                if (isChecked) {
                    if (dh_chk_10.isChecked())
                        dh_chk_10.setChecked(false);
                    contentValues.put("Spoiled_Spindle", "N");
                } else {
                    contentValues.put("Spoiled_Spindle", "null");
                }
                break;
            case R.id.dh_chk_12:
                if (isChecked) {
                    if (dh_chk_13.isChecked())
                        dh_chk_13.setChecked(false);
                    contentValues.put("Proper_CutOut", "Y");
                } else {
                    contentValues.put("Proper_CutOut", "null");
                }
                break;
            case R.id.dh_chk_13:
                if (isChecked) {
                    if (dh_chk_12.isChecked())
                        dh_chk_12.setChecked(false);
                    contentValues.put("Proper_CutOut", "N");
                    if (dh_chk_14.isChecked())
                        dh_chk_14.setChecked(false);
                    if (dh_chk_15.isChecked())
                        dh_chk_15.setChecked(false);

                    dh_chk_14.setEnabled(false);
                    dh_chk_15.setEnabled(false);
                } else {
                    contentValues.put("Proper_CutOut", "null");
                    if (dh_chk_10.isChecked()) {

                    } else {
                        dh_chk_14.setEnabled(true);
                        dh_chk_15.setEnabled(true);
                    }
                }
                break;
            case R.id.dh_chk_14:
                if (isChecked) {
                    if (dh_chk_15.isChecked())
                        dh_chk_15.setChecked(false);
                    contentValues.put("Strike_Plate_CutOut", "Y");
                } else {
                    contentValues.put("Strike_Plate_CutOut", "null");
                }
                break;
            case R.id.dh_chk_15:
                if (isChecked) {
                    if (dh_chk_14.isChecked())
                        dh_chk_14.setChecked(false);
                    contentValues.put("Strike_Plate_CutOut", "N");
                } else {
                    contentValues.put("Strike_Plate_CutOut", "null");
                }
                break;

            case R.id.ph_chk_1:
                if (isChecked) {
                    if (ph_chk_2.isChecked())
                        ph_chk_2.setChecked(false);
                    contentValues.put("Screw_Built_Check", "Y");
                } else {
                    contentValues.put("Screw_Built_Check", "null");
                }
                break;
            case R.id.ph_chk_2:
                if (isChecked) {
                    if (ph_chk_1.isChecked())
                        ph_chk_1.setChecked(false);
                    contentValues.put("Screw_Built_Check", "N");
                    if (ph_chk_3.isChecked())
                        ph_chk_3.setChecked(false);
                    if (ph_chk_4.isChecked())
                        ph_chk_4.setChecked(false);
                    ph_chk_3.setEnabled(false);
                    ph_chk_4.setEnabled(false);
                    door_handle_thickness.setText("");
                    door_handle_thickness.setEnabled(false);

                } else {
                    contentValues.put("Screw_Built_Check", "null");
                    ph_chk_3.setEnabled(true);
                    ph_chk_4.setEnabled(true);
                    door_handle_thickness.setEnabled(true);
                }
                break;
            case R.id.ph_chk_3:
                if (isChecked) {
                    if (ph_chk_4.isChecked())
                        ph_chk_4.setChecked(false);
                    contentValues.put("thickness_range", "Y");
                } else {
                    contentValues.put("thickness_range", "null");
                }
                break;
            case R.id.ph_chk_4:
                if (isChecked) {
                    if (ph_chk_3.isChecked())
                        ph_chk_3.setChecked(false);
                    contentValues.put("thickness_range", "N");
                } else {
                    contentValues.put("thickness_range", "null");
                }
                break;

            case R.id.hn_chk_1:
                if (isChecked) {
                    if (hn_chk_2.isChecked())
                        hn_chk_2.setChecked(false);
                    contentValues.put("Right_Product_Door", "Y");
                } else {
                    contentValues.put("Right_Product_Door", "null");
                }
                break;
            case R.id.hn_chk_2:
                if (isChecked) {
                    if (hn_chk_1.isChecked())
                        hn_chk_1.setChecked(false);
                    contentValues.put("Right_Product_Door", "N");
                    if (hn_chk_3.isChecked())
                        hn_chk_3.setChecked(false);
                    if (hn_chk_4.isChecked())
                        hn_chk_4.setChecked(false);
                    if (hn_chk_5.isChecked())
                        hn_chk_5.setChecked(false);
                    if (hn_chk_6.isChecked())
                        hn_chk_6.setChecked(false);
                    if (hn_chk_7.isChecked())
                        hn_chk_7.setChecked(false);
                    if (hn_chk_8.isChecked())
                        hn_chk_8.setChecked(false);

                    hn_chk_3.setEnabled(false);
                    hn_chk_4.setEnabled(false);
                    hn_chk_5.setEnabled(false);
                    hn_chk_6.setEnabled(false);
                    hn_chk_7.setEnabled(false);
                    hn_chk_8.setEnabled(false);

                } else {
                    contentValues.put("Right_Product_Door", "null");
                    hn_chk_3.setEnabled(true);
                    hn_chk_4.setEnabled(true);
                    hn_chk_5.setEnabled(true);
                    hn_chk_6.setEnabled(true);
                    hn_chk_7.setEnabled(true);
                    hn_chk_8.setEnabled(true);
                }
                break;
            case R.id.hn_chk_3:
                if (isChecked) {
                    if (hn_chk_4.isChecked())
                        hn_chk_4.setChecked(false);
                    contentValues.put("Hinges_Number_Sufficeint", "Y");
                } else {
                    contentValues.put("Hinges_Number_Sufficeint", "null");
                }
                break;
            case R.id.hn_chk_4:
                if (isChecked) {
                    if (hn_chk_3.isChecked())
                        hn_chk_3.setChecked(false);
                    contentValues.put("Hinges_Number_Sufficeint", "N");
                    if (hn_chk_5.isChecked())
                        hn_chk_5.setChecked(false);
                    if (hn_chk_6.isChecked())
                        hn_chk_6.setChecked(false);
                    if (hn_chk_7.isChecked())
                        hn_chk_7.setChecked(false);
                    if (hn_chk_8.isChecked())
                        hn_chk_8.setChecked(false);

                    hn_chk_5.setEnabled(false);
                    hn_chk_6.setEnabled(false);
                    hn_chk_7.setEnabled(false);
                    hn_chk_8.setEnabled(false);

                } else {
                    contentValues.put("Hinges_Number_Sufficeint", "null");
                    hn_chk_5.setEnabled(true);
                    hn_chk_6.setEnabled(true);
                    hn_chk_7.setEnabled(true);
                    hn_chk_8.setEnabled(true);
                }
                break;
            case R.id.hn_chk_5:
                if (isChecked) {
                    if (hn_chk_6.isChecked())
                        hn_chk_6.setChecked(false);
                    contentValues.put("CutOut_Details", "Y");
                } else {
                    contentValues.put("CutOut_Details", "null");
                }
                break;
            case R.id.hn_chk_6:
                if (isChecked) {
                    if (hn_chk_5.isChecked())
                        hn_chk_5.setChecked(false);
                    contentValues.put("CutOut_Details", "N");
                    if (hn_chk_7.isChecked())
                        hn_chk_7.setChecked(false);
                    if (hn_chk_8.isChecked())
                        hn_chk_8.setChecked(false);

                    hn_chk_7.setEnabled(false);
                    hn_chk_8.setEnabled(false);
                } else {
                    contentValues.put("CutOut_Details", "null");
                    hn_chk_7.setEnabled(true);
                    hn_chk_8.setEnabled(true);
                }
                break;
            case R.id.hn_chk_7:
                if (isChecked) {
                    if (hn_chk_8.isChecked())
                        hn_chk_8.setChecked(false);
                    contentValues.put("Depth_Thickness_Equal", "Y");
                } else {
                    contentValues.put("Depth_Thickness_Equal", "null");
                }
                break;
            case R.id.hn_chk_8:
                if (isChecked) {
                    if (hn_chk_7.isChecked())
                        hn_chk_7.setChecked(false);
                    contentValues.put("Depth_Thickness_Equal", "N");
                } else {
                    contentValues.put("Depth_Thickness_Equal", "null");
                }
                break;
            case R.id.ml_chk_1:
                if (isChecked) {
                    if (ml_chk_2.isChecked())
                        ml_chk_2.setChecked(false);
                    ml_chk_3.setEnabled(true);
                    ml_chk_4.setEnabled(true);
                    contentValues.put("Lock_Working", "Y");

                } else {
                    contentValues.put("Lock_Working", "null");
                }
                break;

            case R.id.ml_chk_2:
                if (isChecked) {
                    if (ml_chk_1.isChecked())
                        ml_chk_1.setChecked(false);
                    contentValues.put("Lock_Working", "N");
                    ml_chk_3.setEnabled(false);
                    ml_chk_4.setEnabled(false);
                    if (ml_chk_3.isChecked())
                        ml_chk_3.setChecked(false);
                    if (ml_chk_4.isChecked())
                        ml_chk_4.setChecked(false);

                } else {
                    contentValues.put("Lock_Working", "null");
                    ml_chk_3.setEnabled(true);
                    ml_chk_4.setEnabled(true);
                }
                break;
            case R.id.ml_chk_3:
                if (isChecked) {
                    if (ml_chk_4.isChecked())
                        ml_chk_4.setChecked(false);
                    contentValues.put("CutOut_Dimension", "Y");
                } else {
                    contentValues.put("CutOut_Dimension", "null");
                }
                break;
            case R.id.ml_chk_4:
                if (isChecked) {
                    if (ml_chk_3.isChecked())
                        ml_chk_3.setChecked(false);
                    contentValues.put("CutOut_Dimension", "N");
                } else {
                    contentValues.put("CutOut_Dimension", "null");
                }
                break;

            case R.id.ml_chk_5:
                if (isChecked) {
                    if (ml_chk_6.isChecked())
                        ml_chk_6.setChecked(false);

                    ml_chk_1.setEnabled(true);
                    ml_chk_2.setEnabled(true);
                    ml_chk_3.setEnabled(true);
                    ml_chk_4.setEnabled(true);

                    contentValues.put("wrong_product", "Y");
                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);
                } else {
                    contentValues.put("wrong_product", "null");
                }
                break;

            case R.id.ml_chk_6:
                if (isChecked) {
                    if (ml_chk_5.isChecked())
                        ml_chk_5.setChecked(false);
                    spin_wrong_product.setEnabled(true);

                    if (ml_chk_1.isChecked())
                        ml_chk_1.setChecked(false);
                    if (ml_chk_2.isChecked())
                        ml_chk_2.setChecked(false);
                    if (ml_chk_3.isChecked())
                        ml_chk_3.setChecked(false);
                    if (ml_chk_4.isChecked())
                        ml_chk_4.setChecked(false);
                    ml_chk_1.setEnabled(false);
                    ml_chk_2.setEnabled(false);
                    ml_chk_3.setEnabled(false);
                    ml_chk_4.setEnabled(false);
                    contentValues.put("wrong_product", "N");

                } else {
                    contentValues.put("wrong_product", "null");
                }
                break;

            case R.id.radio_1:
                if (isChecked) {

                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("1");
                    stars = 1;
                    radio_1.setChecked(true);
                } else {
                    hafele_rate.setText("0");
                    stars = 0;
                    // uncheck next radio buttons
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                break;
            case R.id.radio_2:
                if (isChecked) {
                    // radio_1.setChecked(true);

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("2");
                    stars = 2;
                    radio_2.setChecked(true);
                } else {
                    // if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                break;
            case R.id.radio_3:
                if (isChecked) {
                    // radio_1.setChecked(true);
                    // radio_2.setChecked(true);

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("3");
                    stars = 3;
                    radio_3.setChecked(true);
                } else {
                    // if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
                    // if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                break;
            case R.id.radio_4:
                if (isChecked) {
                    // radio_1.setChecked(true);
                    // radio_2.setChecked(true);
                    // radio_3.setChecked(true);

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("4");
                    stars = 4;
                    radio_4.setChecked(true);
                } else {
                    // if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
                    // if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
                    // if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                // if(isChecked) hafele_rate.setText("4"); stars = 4;
                break;
            case R.id.radio_5:
                if (isChecked) {
                    // radio_1.setChecked(true);
                    // radio_2.setChecked(true);
                    // radio_3.setChecked(true);
                    // radio_4.setChecked(true);

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("5");
                    stars = 5;
                    radio_5.setChecked(true);
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 */
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);

                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);

                }
                // if(isChecked) hafele_rate.setText("5"); stars = 5;
                break;
            case R.id.radio_6:
                if (isChecked) {
                    // radio_1.setChecked(true);
                    // radio_2.setChecked(true);
                    // radio_3.setChecked(true);
                    // radio_4.setChecked(true);
                    // radio_5.setChecked(true);
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("6");
                    stars = 6;
                    radio_6.setChecked(true);
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 * if(radio_5.isChecked())hafele_rate.setText("5"); stars = 5;
				 */
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);

                }
                // if(isChecked) hafele_rate.setText("6"); stars = 6;
                break;
            case R.id.radio_7:
                if (isChecked) {
                    // radio_1.setChecked(true);
                    // radio_2.setChecked(true);
                    // radio_3.setChecked(true);
                    // radio_4.setChecked(true);
                    // radio_5.setChecked(true);
                    // radio_6.setChecked(true);

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);

                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("7");
                    stars = 7;
                    radio_7.setChecked(true);
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 * if(radio_5.isChecked())hafele_rate.setText("5"); stars = 5;
				 * if(radio_6.isChecked())hafele_rate.setText("6"); stars = 6;
				 */
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);

                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);

                }
                // if(isChecked) hafele_rate.setText("7"); stars = 7;
                break;
            case R.id.radio_8:
                if (isChecked) {
                /*
                 * radio_1.setChecked(true); radio_2.setChecked(true);
				 * radio_3.setChecked(true); radio_4.setChecked(true);
				 * radio_5.setChecked(true); radio_6.setChecked(true);
				 * radio_7.setChecked(true);
				 */

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    hafele_rate.setText("8");
                    stars = 8;
                    radio_8.setChecked(true);
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 * if(radio_5.isChecked())hafele_rate.setText("5"); stars = 5;
				 * if(radio_6.isChecked())hafele_rate.setText("6"); stars = 6;
				 * if(radio_7.isChecked())hafele_rate.setText("7"); stars = 7;
				 */
                    // uncheck next radio buttons
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                // if(isChecked) hafele_rate.setText("8"); stars = 8;
                break;
            case R.id.radio_9:
                if (isChecked) {
                /*
                 * radio_1.setChecked(true); radio_2.setChecked(true);
				 * radio_3.setChecked(true); radio_4.setChecked(true);
				 * radio_5.setChecked(true); radio_6.setChecked(true);
				 * radio_7.setChecked(true); radio_8.setChecked(true);
				 */

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                    radio_9.setChecked(true);
                    hafele_rate.setText("9");
                    stars = 9;
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 * if(radio_5.isChecked())hafele_rate.setText("5"); stars = 5;
				 * if(radio_6.isChecked())hafele_rate.setText("6"); stars = 6;
				 * if(radio_7.isChecked())hafele_rate.setText("7"); stars = 7;
				 * if(radio_8.isChecked())hafele_rate.setText("8"); stars = 8;
				 */
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_10.isChecked())
                        radio_10.setChecked(false);
                }
                // if(isChecked) hafele_rate.setText("9"); stars = 9;
                break;
            case R.id.radio_10:
                if (isChecked) {
                /*
                 * radio_1.setChecked(true); radio_2.setChecked(true);
				 * radio_3.setChecked(true); radio_4.setChecked(true);
				 * radio_5.setChecked(true); radio_6.setChecked(true);
				 * radio_7.setChecked(true); radio_8.setChecked(true);
				 * radio_9.setChecked(true);
				 */

                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                    hafele_rate.setText("10");
                    stars = 10;
                    radio_10.setChecked(true);
                } else {
                /*
                 * if(radio_1.isChecked())hafele_rate.setText("1"); stars = 1;
				 * if(radio_2.isChecked())hafele_rate.setText("2"); stars = 2;
				 * if(radio_3.isChecked())hafele_rate.setText("3"); stars = 3;
				 * if(radio_4.isChecked())hafele_rate.setText("4"); stars = 4;
				 * if(radio_5.isChecked())hafele_rate.setText("5"); stars = 5;
				 * if(radio_6.isChecked())hafele_rate.setText("6"); stars = 6;
				 * if(radio_7.isChecked())hafele_rate.setText("7"); stars = 7;
				 * if(radio_8.isChecked())hafele_rate.setText("8"); stars = 8;
				 * if(radio_9.isChecked())hafele_rate.setText("9"); stars = 9;
				 */
                    if (radio_1.isChecked())
                        radio_1.setChecked(false);
                    if (radio_2.isChecked())
                        radio_2.setChecked(false);
                    if (radio_3.isChecked())
                        radio_3.setChecked(false);
                    if (radio_4.isChecked())
                        radio_4.setChecked(false);
                    if (radio_5.isChecked())
                        radio_5.setChecked(false);
                    if (radio_6.isChecked())
                        radio_6.setChecked(false);
                    if (radio_7.isChecked())
                        radio_7.setChecked(false);
                    if (radio_8.isChecked())
                        radio_8.setChecked(false);
                    if (radio_9.isChecked())
                        radio_9.setChecked(false);
                }
                // if(isChecked) hafele_rate.setText("10"); stars = 10;
                break;
            //******Komal*****

         /*   case R.id.chk_wrongInstallation:
                if (isChecked) {
                    ll_product_defect.setVisibility(View.GONE);
                    ll_site_issue.setVisibility(View.GONE);
                    chk_product_defect.setChecked(false);
                    chk_site_Issue.setChecked(false);

                }
                break;

            case R.id.chk_product_defect:
                if (isChecked) {
                    ll_product_defect.setVisibility(View.VISIBLE);
                    ll_site_issue.setVisibility(View.GONE);
                    chk_wrongInstallation.setChecked(false);
                    chk_site_Issue.setChecked(false);
                } else {
                    ll_product_defect.setVisibility(View.GONE);
                }
                break;

            case R.id.chk_site_Issue:
                if (isChecked) {
                    chk_wrongInstallation.setChecked(false);
                    chk_product_defect.setChecked(false);
                    ll_site_issue.setVisibility(View.VISIBLE);
                    ll_product_defect.setVisibility(View.GONE);
                } else {
                    ll_site_issue.setVisibility(View.GONE);
                }
                break;*/

            case R.id.blcr_chk_1:
                if (isChecked) {
                    blcr_chk_2.setChecked(false);
                    contentValues.put("size_of_the_cabinet_is_more", "Y");
                    blcr_chk_3.setEnabled(true);
                    blcr_chk_4.setEnabled(true);
                    blcr_chk_5.setEnabled(true);
                    blcr_chk_6.setEnabled(true);
                    blcr_chk_7.setEnabled(true);
                    blcr_chk_8.setEnabled(true);
                    blcr_chk_9.setEnabled(true);
                    blcr_chk_10.setEnabled(true);
                    blcr_chk_11.setEnabled(true);
                    blcr_chk_12.setEnabled(true);
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("size_of_the_cabinet_is_more", "null");
                }
                break;
            case R.id.blcr_chk_2:
                if (isChecked) {
                    blcr_chk_1.setChecked(false);
                    blcr_chk_3.setChecked(false);
                    blcr_chk_4.setChecked(false);
                    blcr_chk_5.setChecked(false);
                    blcr_chk_6.setChecked(false);
                    blcr_chk_7.setChecked(false);
                    blcr_chk_8.setChecked(false);
                    blcr_chk_9.setChecked(false);
                    blcr_chk_10.setChecked(false);
                    blcr_chk_11.setChecked(false);
                    blcr_chk_12.setChecked(false);
                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("size_of_the_cabinet_is_more", "N");
                    blcr_chk_3.setEnabled(false);
                    blcr_chk_4.setEnabled(false);
                    blcr_chk_5.setEnabled(false);
                    blcr_chk_6.setEnabled(false);
                    blcr_chk_7.setEnabled(false);
                    blcr_chk_8.setEnabled(false);
                    blcr_chk_9.setEnabled(false);
                    blcr_chk_10.setEnabled(false);
                    blcr_chk_11.setEnabled(false);
                    blcr_chk_12.setEnabled(false);
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");


                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);

                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);
                } else {
                    contentValues.put("size_of_the_cabinet_is_more", "null");
                }
                break;
            case R.id.blcr_chk_3:
                if (isChecked) {
                    blcr_chk_4.setChecked(false);
                    contentValues.put("fitting_templete", "Y");
                    blcr_chk_5.setEnabled(true);
                    blcr_chk_6.setEnabled(true);
                    blcr_chk_7.setEnabled(true);
                    blcr_chk_8.setEnabled(true);
                    blcr_chk_9.setEnabled(true);
                    blcr_chk_10.setEnabled(true);
                    blcr_chk_11.setEnabled(true);
                    blcr_chk_12.setEnabled(true);
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);
                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("fitting_templete", "null");
                }
                break;
            case R.id.blcr_chk_4:
                if (isChecked) {
                    blcr_chk_3.setChecked(false);

                    blcr_chk_5.setChecked(false);
                    blcr_chk_6.setChecked(false);
                    blcr_chk_7.setChecked(false);
                    blcr_chk_8.setChecked(false);
                    blcr_chk_9.setChecked(false);
                    blcr_chk_10.setChecked(false);
                    blcr_chk_11.setChecked(false);
                    blcr_chk_12.setChecked(false);
                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("fitting_templete", "N");
                    blcr_chk_5.setEnabled(false);
                    blcr_chk_6.setEnabled(false);
                    blcr_chk_7.setEnabled(false);
                    blcr_chk_8.setEnabled(false);
                    blcr_chk_9.setEnabled(false);
                    blcr_chk_10.setEnabled(false);
                    blcr_chk_11.setEnabled(false);
                    blcr_chk_12.setEnabled(false);
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);
                    spin_wrong_product.setEnabled(true);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);
                } else {
                    contentValues.put("fitting_templete", "null");
                }
                break;
            case R.id.blcr_chk_5:
                if (isChecked) {
                    blcr_chk_6.setChecked(false);
                    contentValues.put("front_adjustments", "Y");
                    blcr_chk_7.setEnabled(true);
                    blcr_chk_8.setEnabled(true);
                    blcr_chk_9.setEnabled(true);
                    blcr_chk_10.setEnabled(true);
                    blcr_chk_11.setEnabled(true);
                    blcr_chk_12.setEnabled(true);
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("front_adjustments", "null");
                }
                break;
            case R.id.blcr_chk_6:
                if (isChecked) {
                    blcr_chk_5.setChecked(false);

                    blcr_chk_7.setChecked(false);
                    blcr_chk_8.setChecked(false);
                    blcr_chk_9.setChecked(false);
                    blcr_chk_10.setChecked(false);
                    blcr_chk_11.setChecked(false);
                    blcr_chk_12.setChecked(false);
                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("front_adjustments", "N");
                    blcr_chk_7.setEnabled(false);
                    blcr_chk_8.setEnabled(false);
                    blcr_chk_9.setEnabled(false);
                    blcr_chk_10.setEnabled(false);
                    blcr_chk_11.setEnabled(false);
                    blcr_chk_12.setEnabled(false);
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);

                } else {
                    contentValues.put("front_adjustments", "null");
                }
                break;
            case R.id.blcr_chk_7:
                if (isChecked) {
                    blcr_chk_8.setChecked(false);
                    contentValues.put("correct_alignment", "Y");
                    blcr_chk_9.setEnabled(true);
                    blcr_chk_10.setEnabled(true);
                    blcr_chk_11.setEnabled(true);
                    blcr_chk_12.setEnabled(true);
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);
                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("correct_alignment", "null");
                }
                break;
            case R.id.blcr_chk_8:
                if (isChecked) {
                    blcr_chk_7.setChecked(false);
                    blcr_chk_9.setChecked(false);
                    blcr_chk_10.setChecked(false);
                    blcr_chk_11.setChecked(false);
                    blcr_chk_12.setChecked(false);
                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("correct_alignment", "N");
                    blcr_chk_9.setEnabled(false);
                    blcr_chk_10.setEnabled(false);
                    blcr_chk_11.setEnabled(false);
                    blcr_chk_12.setEnabled(false);
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);
                } else {
                    contentValues.put("correct_alignment", "null");
                }
                break;
            case R.id.blcr_chk_9:
                if (isChecked) {
                    blcr_chk_10.setChecked(false);
                    contentValues.put("correct_no_of_screws", "Y");
                    blcr_chk_11.setEnabled(true);
                    blcr_chk_12.setEnabled(true);
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("correct_no_of_screws", "null");
                }
                break;
            case R.id.blcr_chk_10:
                if (isChecked) {
                    blcr_chk_9.setChecked(false);

                    blcr_chk_11.setChecked(false);
                    blcr_chk_12.setChecked(false);
                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("correct_no_of_screws", "N");
                    blcr_chk_11.setEnabled(false);
                    blcr_chk_12.setEnabled(false);
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);
                } else {
                    contentValues.put("correct_no_of_screws", "null");
                }
                break;
            case R.id.blcr_chk_11:
                if (isChecked) {
                    blcr_chk_12.setChecked(false);
                    contentValues.put("correct_spacing", "Y");
                    blcr_chk_13.setEnabled(true);
                    blcr_chk_14.setEnabled(true);
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("correct_spacing", "null");
                }
                break;
            case R.id.blcr_chk_12:
                if (isChecked) {
                    blcr_chk_11.setChecked(false);

                    blcr_chk_13.setChecked(false);
                    blcr_chk_14.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    contentValues.put("correct_spacing", "N");
                    blcr_chk_13.setEnabled(false);
                    blcr_chk_14.setEnabled(false);
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);
                } else {
                    contentValues.put("correct_spacing", "null");
                }
                break;
            case R.id.blcr_chk_13:
                if (isChecked) {
                    blcr_chk_14.setChecked(false);
                    contentValues.put("Empty_the_drawer", "Y");
                    blcr_chk_15.setEnabled(false);
                    blcr_chk_16.setEnabled(false);
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);
                    blcr_chk_15.setChecked(false);
                    blcr_chk_16.setChecked(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);

                    v1_w1.setText("");
                    v1_h1.setText("");
                    v1_t1.setText("");
                    v2_w2.setText("");
                    v2_h2.setText("");
                    v2_t2.setText("");
                    v3_w3.setText("");
                    v3_h3.setText("");
                    v3_t3.setText("");
                    v4_w4.setText("");
                    v4_h4.setText("");
                    v4_t4.setText("");
                    vol_one.setText("");
                    vol_two.setText("");
                    vol_three.setText("");
                    vol_four.setText("");
                    drawer_wt_tot.setText("");

                    v1_w1.setEnabled(false);
                    v1_h1.setEnabled(false);
                    v1_t1.setEnabled(false);
                    v2_w2.setEnabled(false);
                    v2_h2.setEnabled(false);
                    v2_t2.setEnabled(false);
                    v3_w3.setEnabled(false);
                    v3_h3.setEnabled(false);
                    v3_t3.setEnabled(false);
                    v4_w4.setEnabled(false);
                    v4_h4.setEnabled(false);
                    v4_t4.setEnabled(false);
                    vol_one.setEnabled(false);
                    vol_two.setEnabled(false);
                    vol_three.setEnabled(false);
                    vol_four.setEnabled(false);
                    drawer_wt_tot.setEnabled(false);
                } else {
                    contentValues.put("Empty_the_drawer", "null");
                }
                break;
            case R.id.blcr_chk_14:
                if (isChecked) {
                    blcr_chk_13.setChecked(false);

                    contentValues.put("Empty_the_drawer", "N");
                    blcr_chk_15.setEnabled(true);
                    blcr_chk_16.setEnabled(true);
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);

                    v1_w1.setEnabled(true);
                    v1_h1.setEnabled(true);
                    v1_t1.setEnabled(true);
                    v2_w2.setEnabled(true);
                    v2_h2.setEnabled(true);
                    v2_t2.setEnabled(true);
                    v3_w3.setEnabled(true);
                    v3_h3.setEnabled(true);
                    v3_t3.setEnabled(true);
                    v4_w4.setEnabled(true);
                    v4_h4.setEnabled(true);
                    v4_t4.setEnabled(true);
                    vol_one.setEnabled(true);
                    vol_two.setEnabled(true);
                    vol_three.setEnabled(true);
                    vol_four.setEnabled(true);
                    drawer_wt_tot.setEnabled(true);
                } else {
                    contentValues.put("Empty_the_drawer", "null");
                }
                break;
            case R.id.blcr_chk_15:
                if (isChecked) {
                    blcr_chk_16.setChecked(false);
                    contentValues.put("dust_gathers", "Y");
                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                    vc_chk_11.setEnabled(false);
                    vc_chk_22.setEnabled(false);
                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    vc_chk_11.setChecked(false);
                    vc_chk_22.setChecked(false);

                } else {
                    contentValues.put("dust_gathers", "null");
                }
                break;
            case R.id.blcr_chk_16:
                if (isChecked) {
                    blcr_chk_15.setChecked(false);
                    contentValues.put("dust_gathers", "N");
                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);

                    vc_chk_11.setEnabled(true);
                    vc_chk_22.setEnabled(true);
                } else {
                    contentValues.put("dust_gathers", "null");
                }
                break;


            case R.id.vc_chk_11:
                if (isChecked) {
                    if (vc_chk_22.isChecked())
                        vc_chk_22.setChecked(false);
                    contentValues.put("Weight_Length_Correct", "Y");
                    try {
                        double wt = Double.parseDouble(vol_one.getText().toString()
                                .replace(" = ", "").replace(" (Sides) ", ""))
                                + Double.parseDouble(vol_two.getText().toString()
                                .replace(" = ", "").replace(" (Back) ", ""))
                                + Double.parseDouble(vol_three.getText().toString()
                                .replace(" = ", "").replace(" (Base) ", ""))
                                + Double.parseDouble(vol_four.getText().toString()
                                .replace(" = ", "")
                                .replace(" (Facia) ", ""));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));
                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Double.parseDouble(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Double.parseDouble(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentValues.put("Weight_Length_Correct", "null");
                }
                break;
            case R.id.vc_chk_22:
                if (isChecked) {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (vc_chk_11.isChecked())
                        vc_chk_11.setChecked(false);
                    contentValues.put("Weight_Length_Correct", "N");


                    blcr_chk_17.setChecked(false);
                    blcr_chk_18.setChecked(false);
                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);

                    blcr_chk_17.setEnabled(false);
                    blcr_chk_18.setEnabled(false);
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);

                } else {
                    try {
                        double wt = Double
                                .parseDouble(vol_one.getText().toString())
                                + Double.parseDouble(vol_two.getText().toString())
                                + Double.parseDouble(vol_three.getText().toString())
                                + Double.parseDouble(vol_four.getText().toString());
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // float wt =
                        // (float)((Float.parseFloat(drawer_wt.getText().toString())/1000)*700);
                        // drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(Float.parseFloat(drawer_wt.getText().toString())*700));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    contentValues.put("Weight_Length_Correct", "null");

                    blcr_chk_17.setEnabled(true);
                    blcr_chk_18.setEnabled(true);
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                }
                break;


            case R.id.blcr_chk_17:
                if (isChecked) {
                    blcr_chk_18.setChecked(false);
                    contentValues.put("servodrive_sufficient_to_handle_weight", "Y");
                    blcr_chk_19.setEnabled(true);
                    blcr_chk_20.setEnabled(true);
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                } else {
                    contentValues.put("servodrive_sufficient_to_handle_weight", "null");
                }
                break;
            case R.id.blcr_chk_18:
                if (isChecked) {
                    blcr_chk_17.setChecked(false);

                    blcr_chk_19.setChecked(false);
                    blcr_chk_20.setChecked(false);
                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    contentValues.put("servodrive_sufficient_to_handle_weight", "N");
                    blcr_chk_19.setEnabled(false);
                    blcr_chk_20.setEnabled(false);
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                } else {
                    contentValues.put("servodrive_sufficient_to_handle_weight", "null");
                }
                break;
            case R.id.blcr_chk_19:
                if (isChecked) {
                    blcr_chk_20.setChecked(false);
                    contentValues.put("third_hinge_is_used", "Y");
                    blcr_chk_21.setEnabled(true);
                    blcr_chk_22.setEnabled(true);
                } else {
                    contentValues.put("third_hinge_is_used", "null");
                }
                break;
            case R.id.blcr_chk_20:
                if (isChecked) {
                    blcr_chk_19.setChecked(false);

                    blcr_chk_21.setChecked(false);
                    blcr_chk_22.setChecked(false);
                    contentValues.put("third_hinge_is_used", "N");
                    blcr_chk_21.setEnabled(false);
                    blcr_chk_22.setEnabled(false);
                } else {
                    contentValues.put("third_hinge_is_used", "null");
                }
                break;
            case R.id.blcr_chk_21:
                if (isChecked) {
                    blcr_chk_22.setChecked(false);
                    contentValues.put("synchro_motion_working_properly", "Y");
                } else {
                    contentValues.put("synchro_motion_working_properly", "null");
                }
                break;
            case R.id.blcr_chk_22:
                if (isChecked) {
                    blcr_chk_21.setChecked(false);
                    contentValues.put("synchro_motion_working_properly", "N");
                } else {
                    contentValues.put("synchro_motion_working_properly", "null");
                }
                break;

            case R.id.cu_chk_1:
                if (isChecked) {
                    cu_chk_2.setChecked(false);
                    contentValues.put("part_list_is_complete", "Y");
                    cu_chk_3.setEnabled(true);
                    cu_chk_4.setEnabled(true);
                    cu_chk_5.setEnabled(true);
                    cu_chk_6.setEnabled(true);
                    cu_chk_7.setEnabled(true);
                    cu_chk_8.setEnabled(true);
                    cu_chk_9.setEnabled(true);
                    cu_chk_10.setEnabled(true);
                    cu_chk_11.setEnabled(true);
                    cu_chk_12.setEnabled(true);
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);

                    cabinet_height.setEnabled(true);
                    cabinet_width.setEnabled(true);
                    cabinet_depth.setEnabled(true);
                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);
                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.cu_chk_2:
                if (isChecked) {
                    cu_chk_1.setChecked(false);
                    contentValues.put("part_list_is_complete", "N");
                    cu_chk_3.setEnabled(false);
                    cu_chk_4.setEnabled(false);
                    cu_chk_5.setEnabled(false);
                    cu_chk_6.setEnabled(false);
                    cu_chk_7.setEnabled(false);
                    cu_chk_8.setEnabled(false);
                    cu_chk_9.setEnabled(false);
                    cu_chk_10.setEnabled(false);
                    cu_chk_11.setEnabled(false);
                    cu_chk_12.setEnabled(false);
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);

                    cu_chk_3.setChecked(false);
                    cu_chk_4.setChecked(false);
                    cu_chk_5.setChecked(false);
                    cu_chk_6.setChecked(false);
                    cu_chk_7.setChecked(false);
                    cu_chk_8.setChecked(false);
                    cu_chk_9.setChecked(false);
                    cu_chk_10.setChecked(false);
                    cu_chk_11.setChecked(false);
                    cu_chk_12.setChecked(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);

                    cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);

                    cabinet_height.setText("");
                    cabinet_width.setText("");
                    cabinet_depth.setText("");

                    spin_wrong_product.setEnabled(true);


                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.cu_chk_3:
                if (isChecked) {
                    cu_chk_4.setChecked(false);
                    contentValues.put("correct_fitting_order", "Y");

                    cu_chk_5.setEnabled(true);
                    cu_chk_6.setEnabled(true);
                    cu_chk_7.setEnabled(true);
                    cu_chk_8.setEnabled(true);
                    cu_chk_9.setEnabled(true);
                    cu_chk_10.setEnabled(true);
                    cu_chk_11.setEnabled(true);
                    cu_chk_12.setEnabled(true);
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);

                    cabinet_height.setEnabled(true);
                    cabinet_width.setEnabled(true);
                    cabinet_depth.setEnabled(true);

                } else {
                    contentValues.put("correct_fitting_order", "null");
                }
                break;
            case R.id.cu_chk_4:
                if (isChecked) {
                    cu_chk_3.setChecked(false);
                    contentValues.put("correct_fitting_order", "N");

                    cu_chk_5.setEnabled(false);
                    cu_chk_6.setEnabled(false);
                    cu_chk_7.setEnabled(false);
                    cu_chk_8.setEnabled(false);
                    cu_chk_9.setEnabled(false);
                    cu_chk_10.setEnabled(false);
                    cu_chk_11.setEnabled(false);
                    cu_chk_12.setEnabled(false);
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);

                    cu_chk_5.setChecked(false);
                    cu_chk_6.setChecked(false);
                    cu_chk_7.setChecked(false);
                    cu_chk_8.setChecked(false);
                    cu_chk_9.setChecked(false);
                    cu_chk_10.setChecked(false);
                    cu_chk_11.setChecked(false);
                    cu_chk_12.setChecked(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);

                    cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);

                    cabinet_height.setText("");
                    cabinet_width.setText("");
                    cabinet_depth.setText("");


                } else {
                    contentValues.put("correct_fitting_order", "null");
                }
                break;
            case R.id.cu_chk_5:
                if (isChecked) {
                    cu_chk_6.setChecked(false);
                    contentValues.put("size_of_the_cabinet_is_more", "Y");
                    cu_chk_7.setEnabled(true);
                    cu_chk_8.setEnabled(true);
                    cu_chk_9.setEnabled(true);
                    cu_chk_10.setEnabled(true);
                    cu_chk_11.setEnabled(true);
                    cu_chk_12.setEnabled(true);
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);
                } else {
                    contentValues.put("size_of_the_cabinet_is_more", "null");
                }
                break;
            case R.id.cu_chk_6:
                if (isChecked) {
                    cu_chk_5.setChecked(false);
                    contentValues.put("size_of_the_cabinet_is_more", "N");

                    cu_chk_7.setEnabled(false);
                    cu_chk_8.setEnabled(false);
                    cu_chk_9.setEnabled(false);
                    cu_chk_10.setEnabled(false);
                    cu_chk_11.setEnabled(false);
                    cu_chk_12.setEnabled(false);
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);

                    cu_chk_7.setChecked(false);
                    cu_chk_8.setChecked(false);
                    cu_chk_9.setChecked(false);
                    cu_chk_10.setChecked(false);
                    cu_chk_11.setChecked(false);
                    cu_chk_12.setChecked(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);
                } else {
                    contentValues.put("size_of_the_cabinet_is_more", "null");
                }
                break;
            case R.id.cu_chk_7:
                if (isChecked) {
                    cu_chk_8.setChecked(false);
                    contentValues.put("door_dimensions", "Y");
                    cu_chk_9.setEnabled(true);
                    cu_chk_10.setEnabled(true);
                    cu_chk_11.setEnabled(true);
                    cu_chk_12.setEnabled(true);
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);
                } else {
                    contentValues.put("door_dimensions", "null");
                }
                break;
            case R.id.cu_chk_8:
                if (isChecked) {
                    cu_chk_7.setChecked(false);
                    contentValues.put("door_dimensions", "Y");

                    cu_chk_9.setEnabled(false);
                    cu_chk_10.setEnabled(false);
                    cu_chk_11.setEnabled(false);
                    cu_chk_12.setEnabled(false);
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);

                    cu_chk_9.setChecked(false);
                    cu_chk_10.setChecked(false);
                    cu_chk_11.setChecked(false);
                    cu_chk_12.setChecked(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);
                } else {
                    contentValues.put("door_dimensions", "null");
                }
                break;
            case R.id.cu_chk_9:
                if (isChecked) {
                    cu_chk_10.setChecked(false);
                    contentValues.put("installation_template", "Y");
                    cu_chk_11.setEnabled(true);
                    cu_chk_12.setEnabled(true);
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.cu_chk_10:
                if (isChecked) {
                    cu_chk_9.setChecked(false);
                    contentValues.put("installation_template", "N");

                    cu_chk_11.setEnabled(false);
                    cu_chk_12.setEnabled(false);
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);

                    cu_chk_11.setChecked(false);
                    cu_chk_12.setChecked(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.cu_chk_11:
                if (isChecked) {
                    cu_chk_12.setChecked(false);
                    contentValues.put("plinth_legs", "Y");
                    cu_chk_13.setEnabled(true);
                    cu_chk_14.setEnabled(true);
                } else {

                    contentValues.put("plinth_legs", "null");
                }
                break;
            case R.id.cu_chk_12:
                if (isChecked) {
                    cu_chk_11.setChecked(false);
                    contentValues.put("plinth_legs", "N");
                    cu_chk_13.setEnabled(false);
                    cu_chk_14.setEnabled(false);
                    cu_chk_13.setChecked(false);
                    cu_chk_14.setChecked(false);
                } else {
                    contentValues.put("plinth_legs", "null");
                }
                break;
            case R.id.cu_chk_13:
                if (isChecked) {
                    cu_chk_14.setChecked(false);
                    contentValues.put("check_hinges", "Y");
                } else {
                    contentValues.put("check_hinges", "null");
                }
                break;
            case R.id.cu_chk_14:
                if (isChecked) {
                    cu_chk_13.setChecked(false);
                    contentValues.put("check_hinges", "N");
                } else {
                    contentValues.put("check_hinges", "null");
                }
                break;

            case R.id.tall_chk_1:
                if (isChecked) {
                    tall_chk_2.setChecked(false);
                    contentValues.put("drawer_within_range", "Y");

                    tall_chk_3.setEnabled(true);
                    tall_chk_4.setEnabled(true);
                    tall_chk_5.setEnabled(true);
                    tall_chk_6.setEnabled(true);
                    tall_chk_7.setEnabled(true);
                    tall_chk_8.setEnabled(true);
                    tall_chk_9.setEnabled(true);
                    tall_chk_10.setEnabled(true);
                    tall_chk_11.setEnabled(true);
                    tall_chk_12.setEnabled(true);

                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);

                } else {
                    contentValues.put("drawer_within_range", "null");
                }
                break;
            case R.id.tall_chk_2:
                if (isChecked) {
                    tall_chk_1.setChecked(false);
                    contentValues.put("drawer_within_range", "N");

                    if (tall_chk_3.isChecked())
                        tall_chk_3.setChecked(false);
                    if (tall_chk_4.isChecked())
                        tall_chk_4.setChecked(false);
                    if (tall_chk_5.isChecked())
                        tall_chk_5.setChecked(false);
                    if (tall_chk_6.isChecked())
                        tall_chk_6.setChecked(false);
                    if (tall_chk_7.isChecked())
                        tall_chk_7.setChecked(false);
                    if (tall_chk_8.isChecked())
                        tall_chk_8.setChecked(false);
                    if (tall_chk_9.isChecked())
                        tall_chk_9.setChecked(false);
                    if (tall_chk_10.isChecked())
                        tall_chk_10.setChecked(false);
                    if (tall_chk_11.isChecked())
                        tall_chk_11.setChecked(false);
                    if (tall_chk_12.isChecked())
                        tall_chk_12.setChecked(false);

                    tall_chk_3.setEnabled(false);
                    tall_chk_4.setEnabled(false);
                    tall_chk_5.setEnabled(false);
                    tall_chk_6.setEnabled(false);
                    tall_chk_7.setEnabled(false);
                    tall_chk_8.setEnabled(false);
                    tall_chk_9.setEnabled(false);
                    tall_chk_10.setEnabled(false);
                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);

                    spin_wrong_product.setEnabled(true);

                } else {
                    contentValues.put("drawer_within_range", "null");
                }
                break;
            case R.id.tall_chk_3:
                if (isChecked) {
                    tall_chk_4.setChecked(false);
                    contentValues.put("installation_template", "Y");

                    tall_chk_5.setEnabled(true);
                    tall_chk_6.setEnabled(true);
                    tall_chk_7.setEnabled(true);
                    tall_chk_8.setEnabled(true);
                    tall_chk_9.setEnabled(true);
                    tall_chk_10.setEnabled(true);
                    tall_chk_11.setEnabled(true);
                    tall_chk_12.setEnabled(true);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.tall_chk_4:
                if (isChecked) {
                    tall_chk_3.setChecked(false);
                    contentValues.put("installation_template", "N");

                    tall_chk_5.setChecked(false);
                    tall_chk_6.setChecked(false);
                    tall_chk_7.setChecked(false);
                    tall_chk_8.setChecked(false);
                    tall_chk_9.setChecked(false);
                    tall_chk_10.setChecked(false);
                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(false);

                    tall_chk_5.setEnabled(false);
                    tall_chk_6.setEnabled(false);
                    tall_chk_7.setEnabled(false);
                    tall_chk_8.setEnabled(false);
                    tall_chk_9.setEnabled(false);
                    tall_chk_10.setEnabled(false);
                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.tall_chk_5:
                if (isChecked) {
                    tall_chk_6.setChecked(false);
                    contentValues.put("unit_if_fixed_to_wall", "Y");
                    tall_chk_7.setEnabled(true);
                    tall_chk_8.setEnabled(true);
                    tall_chk_9.setEnabled(true);
                    tall_chk_10.setEnabled(true);
                    tall_chk_11.setEnabled(true);
                    tall_chk_12.setEnabled(true);
                } else {
                    contentValues.put("unit_if_fixed_to_wall", "null");
                }
                break;
            case R.id.tall_chk_6:
                if (isChecked) {
                    tall_chk_5.setChecked(false);
                    contentValues.put("unit_if_fixed_to_wall", "N");

                    tall_chk_7.setChecked(false);
                    tall_chk_8.setChecked(false);
                    tall_chk_9.setChecked(false);
                    tall_chk_10.setChecked(false);
                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(false);

                    tall_chk_7.setEnabled(false);
                    tall_chk_8.setEnabled(false);
                    tall_chk_9.setEnabled(false);
                    tall_chk_10.setEnabled(false);
                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);
                } else {
                    contentValues.put("unit_if_fixed_to_wall", "null");
                }
                break;
            case R.id.tall_chk_7:
                if (isChecked) {
                    tall_chk_8.setChecked(false);
                    contentValues.put("enough_legs_for_support", "Y");

                    tall_chk_9.setEnabled(true);
                    tall_chk_10.setEnabled(true);
                    tall_chk_11.setEnabled(true);
                    tall_chk_12.setEnabled(true);
                } else {
                    contentValues.put("enough_legs_for_support", "null");
                }
                break;
            case R.id.tall_chk_8:
                if (isChecked) {
                    tall_chk_7.setChecked(false);
                    contentValues.put("enough_legs_for_support", "N");

                    tall_chk_9.setChecked(false);
                    tall_chk_10.setChecked(false);
                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(false);

                    tall_chk_9.setEnabled(false);
                    tall_chk_10.setEnabled(false);
                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);
                } else {
                    contentValues.put("enough_legs_for_support", "null");
                }
                break;
            case R.id.tall_chk_9:
                if (isChecked) {
                    tall_chk_10.setChecked(false);
                    contentValues.put("correct_alignment", "Y");
                    tall_chk_11.setEnabled(true);
                    tall_chk_12.setEnabled(true);
                } else {
                    contentValues.put("correct_alignment", "null");
                }
                break;
            case R.id.tall_chk_10:
                if (isChecked) {
                    tall_chk_9.setChecked(false);
                    contentValues.put("correct_alignment", "N");

                    tall_chk_11.setChecked(false);
                    tall_chk_12.setChecked(false);

                    tall_chk_11.setEnabled(false);
                    tall_chk_12.setEnabled(false);
                } else {
                    contentValues.put("correct_alignment", "null");
                }
                break;
            case R.id.tall_chk_11:
                if (isChecked) {
                    tall_chk_12.setChecked(false);
                    contentValues.put("servodrive_sufficient_to_handle_weight", "Y");
                } else {
                    contentValues.put("servodrive_sufficient_to_handle_weight", "null");
                }
                break;
            case R.id.tall_chk_12:
                if (isChecked) {
                    tall_chk_11.setChecked(false);
                    contentValues.put("servodrive_sufficient_to_handle_weight", "N");
                } else {
                    contentValues.put("servodrive_sufficient_to_handle_weight", "null");
                }
                break;

            case R.id.ib_chk_1:
                if (isChecked) {
                    ib_chk_2.setChecked(false);
                    contentValues.put("part_list_is_complete", "Y");

                    ib_chk_3.setEnabled(true);
                    ib_chk_4.setEnabled(true);
                    ib_chk_5.setEnabled(true);
                    ib_chk_6.setEnabled(true);
                    ib_chk_7.setEnabled(true);
                    ib_chk_8.setEnabled(true);
                    ib_chk_9.setEnabled(true);
                    ib_chk_10.setEnabled(true);
                    ib_chk_11.setEnabled(true);

                    cabinet_height.setEnabled(true);
                    cabinet_width.setEnabled(true);
                    cabinet_depth.setEnabled(true);


                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.ib_chk_2:
                if (isChecked) {
                    ib_chk_1.setChecked(false);
                    contentValues.put("part_list_is_complete", "N");

                    ib_chk_3.setChecked(false);
                    ib_chk_4.setChecked(false);
                    ib_chk_5.setChecked(false);
                    ib_chk_6.setChecked(false);
                    ib_chk_7.setChecked(false);
                    ib_chk_8.setChecked(false);
                    ib_chk_9.setChecked(false);
                    ib_chk_10.setChecked(false);
                    ib_chk_11.setChecked(false);

                    ib_chk_3.setEnabled(false);
                    ib_chk_4.setEnabled(false);
                    ib_chk_5.setEnabled(false);
                    ib_chk_6.setEnabled(false);
                    ib_chk_7.setEnabled(false);
                    ib_chk_8.setEnabled(false);
                    ib_chk_9.setEnabled(false);
                    ib_chk_10.setEnabled(false);
                    ib_chk_11.setEnabled(false);

                    cabinet_height.setEnabled(false);
                    cabinet_width.setEnabled(false);
                    cabinet_depth.setEnabled(false);

                    cabinet_height.setError(null);
                    cabinet_width.setError(null);
                    cabinet_depth.setError(null);

                    cabinet_height.setText("");
                    cabinet_width.setText("");
                    cabinet_depth.setText("");

                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);
                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.ib_chk_3:
                if (isChecked) {
                    ib_chk_4.setChecked(false);
                    contentValues.put("correct_product_order", "Y");

                    ib_chk_5.setEnabled(true);
                    ib_chk_6.setEnabled(true);
                    ib_chk_7.setEnabled(true);
                    ib_chk_8.setEnabled(true);
                    ib_chk_9.setEnabled(true);
                    ib_chk_10.setEnabled(true);
                    ib_chk_11.setEnabled(true);

                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);

                } else {
                    contentValues.put("correct_product_order", "null");
                }
                break;
            case R.id.ib_chk_4:
                if (isChecked) {
                    ib_chk_3.setChecked(false);
                    contentValues.put("correct_product_order", "N");

                    ib_chk_5.setChecked(false);
                    ib_chk_6.setChecked(false);
                    ib_chk_7.setChecked(false);
                    ib_chk_8.setChecked(false);
                    ib_chk_9.setChecked(false);
                    ib_chk_10.setChecked(false);
                    ib_chk_11.setChecked(false);

                    ib_chk_5.setEnabled(false);
                    ib_chk_6.setEnabled(false);
                    ib_chk_7.setEnabled(false);
                    ib_chk_8.setEnabled(false);
                    ib_chk_9.setEnabled(false);
                    ib_chk_10.setEnabled(false);
                    ib_chk_11.setEnabled(false);

                    spin_wrong_product.setEnabled(true);

                } else {
                    contentValues.put("correct_product_order", "null");
                }
                break;
            case R.id.ib_chk_5:
                if (isChecked) {
                    ib_chk_6.setChecked(false);
                    contentValues.put("installation_template", "Y");
                    ib_chk_7.setEnabled(true);
                    ib_chk_8.setEnabled(true);
                    ib_chk_9.setEnabled(true);
                    ib_chk_10.setEnabled(true);
                    ib_chk_11.setEnabled(true);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.ib_chk_6:
                if (isChecked) {
                    ib_chk_5.setChecked(false);
                    contentValues.put("installation_template", "N");

                    ib_chk_7.setChecked(false);
                    ib_chk_8.setChecked(false);
                    ib_chk_9.setChecked(false);
                    ib_chk_10.setChecked(false);
                    ib_chk_11.setChecked(false);

                    ib_chk_7.setEnabled(false);
                    ib_chk_8.setEnabled(false);
                    ib_chk_9.setEnabled(false);
                    ib_chk_10.setEnabled(false);
                    ib_chk_11.setEnabled(false);
                } else {
                    contentValues.put("installation_template", "null");
                }
                break;
            case R.id.ib_chk_7:
                if (isChecked) {
                    ib_chk_8.setChecked(false);
                    ib_chk_9.setChecked(false);
                    contentValues.put("type_of_runner", "Full extension");
                } else {
                    contentValues.put("type_of_runner", "null");
                }
                break;
            case R.id.ib_chk_8:
                if (isChecked) {
                    ib_chk_7.setChecked(false);
                    ib_chk_9.setChecked(false);
                    contentValues.put("type_of_runner", "Single extension");
                } else {
                    contentValues.put("type_of_runner", "null");
                }
                break;
            case R.id.ib_chk_9:
                if (isChecked) {
                    ib_chk_7.setChecked(false);
                    ib_chk_8.setChecked(false);
                    contentValues.put("type_of_runner", "No runners used");
                } else {
                    contentValues.put("type_of_runner", "null");
                }
                break;
            case R.id.ib_chk_10:
                if (isChecked) {
                    ib_chk_11.setChecked(false);
                    contentValues.put("product_abuse", "Y");
                } else {
                    contentValues.put("product_abuse", "null");
                }
                break;
            case R.id.ib_chk_11:
                if (isChecked) {
                    ib_chk_10.setChecked(false);
                    contentValues.put("product_abuse", "N");
                } else {
                    contentValues.put("product_abuse", "null");
                }
                break;

            case R.id.pr_chk_1:
                if (isChecked) {
                    pr_chk_2.setChecked(false);
                    contentValues.put("correct_product_order", "Y");
                    pr_chk_3.setEnabled(true);
                    pr_chk_4.setEnabled(true);
                    pr_chk_5.setEnabled(true);
                    pr_chk_6.setEnabled(true);
                    pr_chk_7.setEnabled(true);
                    pr_chk_8.setEnabled(true);
                    pr_chk_9.setEnabled(true);
                    pr_chk_10.setEnabled(true);
                    pr_chk_11.setEnabled(true);
                    pr_chk_12.setEnabled(true);
                    pr_chk_13.setEnabled(true);
                    pr_chk_14.setEnabled(true);
                    pr_chk_15.setEnabled(true);

                    pratik_height.setEnabled(true);
                    pratik_length_50mm.setEnabled(true);
                    pratik_width_30mm.setEnabled(true);
                    pratik_sfd_dimen.setEnabled(true);
                    pratik_sfd_dimen1.setEnabled(true);
                    spin_wrong_product.setEnabled(true);

                    spin_wrong_product.setEnabled(false);
                    spin_wrong_product.setSelection(0);

                } else {
                    contentValues.put("correct_product_order", "null");
                }
                break;
            case R.id.pr_chk_2:
                if (isChecked) {
                    pr_chk_1.setChecked(false);

                    pr_chk_3.setChecked(false);
                    pr_chk_4.setChecked(false);
                    pr_chk_5.setChecked(false);
                    pr_chk_6.setChecked(false);
                    pr_chk_7.setChecked(false);
                    pr_chk_8.setChecked(false);
                    pr_chk_9.setChecked(false);
                    pr_chk_10.setChecked(false);
                    pr_chk_11.setChecked(false);
                    pr_chk_12.setChecked(false);
                    pr_chk_13.setChecked(false);
                    pr_chk_14.setChecked(false);
                    pr_chk_15.setChecked(false);
                    contentValues.put("correct_product_order", "N");
                    pr_chk_3.setEnabled(false);
                    pr_chk_4.setEnabled(false);
                    pr_chk_5.setEnabled(false);
                    pr_chk_6.setEnabled(false);
                    pr_chk_7.setEnabled(false);
                    pr_chk_8.setEnabled(false);
                    pr_chk_9.setEnabled(false);
                    pr_chk_10.setEnabled(false);
                    pr_chk_11.setEnabled(false);
                    pr_chk_12.setEnabled(false);
                    pr_chk_13.setEnabled(false);
                    pr_chk_14.setEnabled(false);
                    pr_chk_15.setEnabled(false);

                    pratik_height.setEnabled(false);
                    pratik_length_50mm.setEnabled(false);
                    pratik_width_30mm.setEnabled(false);
                    pratik_sfd_dimen.setEnabled(false);
                    pratik_sfd_dimen1.setEnabled(false);

                    pratik_height.setText("");
                    pratik_length_50mm.setText("");
                    pratik_width_30mm.setText("");
                    pratik_sfd_dimen.setText("");
                    pratik_sfd_dimen1.setText("");

                    spin_wrong_product.setEnabled(true);
                } else {
                    contentValues.put("correct_product_order", "null");
                    spin_wrong_product.setSelection(0);
                }
                break;
            case R.id.pr_chk_3:
                if (isChecked) {
                    contentValues.put("height_more_than_260mm", "Y");
                    pratik_height.setEnabled(false);
                    pratik_height.setText("");
                } else {
                    contentValues.put("height_more_than_260mm", pratik_height.getText().toString());
                    pratik_height.setEnabled(true);
                }
                break;
            case R.id.pr_chk_4:
                if (isChecked) {
                    contentValues.put("length_slatted_or_wooden_50mm", "Y");
                    pratik_length_50mm.setEnabled(false);
                    pratik_length_50mm.setText("");
                } else {
                    contentValues.put("length_slatted_or_wooden_50mm", pratik_length_50mm.getText().toString());
                    pratik_length_50mm.setEnabled(true);
                }
                break;
            case R.id.pr_chk_5:
                if (isChecked) {
                    contentValues.put("widht_slatted_or_wooden_30mm", "Y");
                    pratik_width_30mm.setEnabled(false);
                    pratik_width_30mm.setText("");
                } else {
                    contentValues.put("widht_slatted_or_wooden_30mm", pratik_width_30mm.getText().toString());
                    pratik_width_30mm.setEnabled(true);
                }
                break;
            case R.id.pr_chk_6:
                if (isChecked) {
                    contentValues.put("slatted_dimen_1400mm_2000mm", "Y");
                    pratik_sfd_dimen.setEnabled(false);
                    pratik_sfd_dimen.setText("");
                } else {
                    contentValues.put("slatted_dimen_1400mm_2000mm", pratik_sfd_dimen.getText().toString());
                    pratik_sfd_dimen.setEnabled(true);
                }
                break;
            case R.id.pr_chk_7:
                if (isChecked) {
                    contentValues.put("slatted_dimen_1800mm_2000mm", "Y");
                    pratik_sfd_dimen1.setEnabled(false);
                    pratik_sfd_dimen1.setText("");
                } else {
                    contentValues.put("slatted_dimen_1800mm_2000mm", pratik_sfd_dimen1.getText().toString());
                    pratik_sfd_dimen1.setEnabled(true);
                }
                break;
            case R.id.pr_chk_8:
                if (isChecked) {
                    pr_chk_9.setChecked(false);
                    contentValues.put("bedding_box_fixing", "Y");
                    pr_chk_10.setEnabled(true);
                    pr_chk_11.setEnabled(true);
                    pr_chk_12.setEnabled(true);
                    pr_chk_13.setEnabled(true);
                    pr_chk_14.setEnabled(true);
                    pr_chk_15.setEnabled(true);
                } else {
                    contentValues.put("bedding_box_fixing", "null");
                }
                break;
            case R.id.pr_chk_9:
                if (isChecked) {
                    pr_chk_8.setChecked(false);

                    pr_chk_10.setChecked(false);
                    pr_chk_11.setChecked(false);
                    pr_chk_12.setChecked(false);
                    pr_chk_13.setChecked(false);
                    pr_chk_14.setChecked(false);
                    pr_chk_15.setChecked(false);

                    pr_chk_10.setEnabled(false);
                    pr_chk_11.setEnabled(false);
                    pr_chk_12.setEnabled(false);
                    pr_chk_13.setEnabled(false);
                    pr_chk_14.setEnabled(false);
                    pr_chk_15.setEnabled(false);
                    contentValues.put("bedding_box_fixing", "N");
                } else {
                    contentValues.put("bedding_box_fixing", "null");
                }
                break;
            case R.id.pr_chk_10:
                if (isChecked) {
                    pr_chk_11.setChecked(false);
                    contentValues.put("weight_between_23_25kgs", "Y");

                    pr_chk_12.setEnabled(true);
                    pr_chk_13.setEnabled(true);
                    pr_chk_14.setEnabled(true);
                    pr_chk_15.setEnabled(true);
                } else {
                    contentValues.put("weight_between_23_25kgs", "null");
                }
                break;
            case R.id.pr_chk_11:
                if (isChecked) {
                    pr_chk_10.setChecked(false);

                    pr_chk_12.setChecked(false);
                    pr_chk_13.setChecked(false);
                    pr_chk_14.setChecked(false);
                    pr_chk_15.setChecked(false);
                    contentValues.put("weight_between_23_25kgs", "N");
                    pr_chk_12.setEnabled(false);
                    pr_chk_13.setEnabled(false);
                    pr_chk_14.setEnabled(false);
                    pr_chk_15.setEnabled(false);
                } else {
                    contentValues.put("weight_between_23_25kgs", "null");
                }
                break;
            case R.id.pr_chk_12:
                if (isChecked) {
                    pr_chk_13.setChecked(false);
                    contentValues.put("weight_between_50_60kgs", "Y");

                    pr_chk_14.setEnabled(true);
                    pr_chk_15.setEnabled(true);
                } else {
                    contentValues.put("weight_between_50_60kgs", "null");
                }
                break;
            case R.id.pr_chk_13:
                if (isChecked) {
                    pr_chk_12.setChecked(false);

                    pr_chk_14.setChecked(false);
                    pr_chk_15.setChecked(false);
                    contentValues.put("weight_between_50_60kgs", "N");

                    pr_chk_14.setEnabled(false);
                    pr_chk_15.setEnabled(false);
                } else {
                    contentValues.put("weight_between_50_60kgs", "null");
                }
                break;
            case R.id.pr_chk_14:
                if (isChecked) {
                    pr_chk_15.setChecked(false);
                    contentValues.put("check_Stabilizing_rod", "Y");

                } else {
                    contentValues.put("check_Stabilizing_rod", "null");
                }
                break;
            case R.id.pr_chk_15:
                if (isChecked) {
                    pr_chk_14.setChecked(false);
                    contentValues.put("check_Stabilizing_rod", "N");

                } else {
                    contentValues.put("check_Stabilizing_rod", "null");
                }
                break;

            case R.id.tv_chk_1:
                if (isChecked) {
                    tv_chk_2.setChecked(false);
                    tv_chk_3.setChecked(false);
                    tv_chk_4.setChecked(false);

                    tv_chk_5.setEnabled(true);
                    tv_chk_6.setEnabled(true);
                    tv_chk_7.setEnabled(true);
                    tv_chk_8.setEnabled(true);


                    spin_wrong_product1.setSelection(0);
                    spin_wrong_product1.setEnabled(false);

                    contentValues.put("correct_pro_one", "900x2000mm");
                } else {
                    contentValues.put("correct_pro_one", "null");
                }
                break;
            case R.id.tv_chk_2:
                if (isChecked) {
                    tv_chk_1.setChecked(false);
                    tv_chk_3.setChecked(false);
                    tv_chk_4.setChecked(false);
                    tv_chk_5.setEnabled(true);
                    tv_chk_6.setEnabled(true);
                    tv_chk_7.setEnabled(true);
                    tv_chk_8.setEnabled(true);
                    spin_wrong_product1.setSelection(0);
                    spin_wrong_product1.setEnabled(false);

                    contentValues.put("correct_pro_two", "1400x2000mm");
                } else {
                    contentValues.put("correct_pro_two", "null");
                }
                break;
            case R.id.tv_chk_3:
                if (isChecked) {
                    tv_chk_2.setChecked(false);
                    tv_chk_1.setChecked(false);
                    tv_chk_4.setChecked(false);
                    tv_chk_5.setEnabled(true);
                    tv_chk_6.setEnabled(true);
                    tv_chk_7.setEnabled(true);
                    tv_chk_8.setEnabled(true);
                    spin_wrong_product1.setSelection(0);
                    spin_wrong_product1.setEnabled(false);

                    contentValues.put("correct_pro_three", "1400x2000mm");
                } else {
                    contentValues.put("correct_pro_three", "null");
                }
                break;
            case R.id.tv_chk_4:
                if (isChecked) {

                    spin_wrong_product1.setEnabled(true);
                    tv_chk_1.setChecked(false);
                    tv_chk_2.setChecked(false);
                    tv_chk_3.setChecked(false);
                    contentValues.put("wrong_product", "Y");
                    tv_chk_5.setEnabled(false);
                    tv_chk_6.setEnabled(false);
                    tv_chk_7.setEnabled(false);
                    tv_chk_8.setEnabled(false);
                    tv_chk_5.setChecked(false);
                    tv_chk_6.setChecked(false);
                    tv_chk_7.setChecked(false);
                    tv_chk_8.setChecked(false);
                } else {
                    spin_wrong_product1.setSelection(0);
                    contentValues.put("wrong_product", "null");
                }
                break;
            case R.id.tv_chk_5:
                if (isChecked) {
                    tv_chk_6.setChecked(false);
                    contentValues.put("part_list_is_complete", "Y");
                    tv_chk_7.setEnabled(true);
                    tv_chk_8.setEnabled(true);

                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.tv_chk_6:
                if (isChecked) {
                    tv_chk_5.setChecked(false);
                    contentValues.put("part_list_is_complete", "N");
                    tv_chk_7.setEnabled(false);
                    tv_chk_8.setEnabled(false);
                    tv_chk_7.setChecked(false);
                    tv_chk_8.setChecked(false);

                } else {
                    contentValues.put("part_list_is_complete", "null");
                }
                break;
            case R.id.tv_chk_7:
                if (isChecked) {
                    tv_chk_8.setChecked(false);
                    contentValues.put("Wooden_Panel_dimensions", "Y");
                } else {
                    contentValues.put("Wooden_Panel_dimensions", "null");
                }
                break;
            case R.id.tv_chk_8:
                if (isChecked) {
                    tv_chk_7.setChecked(false);
                    contentValues.put("Wooden_Panel_dimensions", "N");
                } else {
                    contentValues.put("Wooden_Panel_dimensions", "null");
                }
                break;

            case R.id.lc_chk_1:
                if (isChecked) {
                    lc_chk_2.setChecked(false);
                    contentValues.put("wrong_product", "Y");
                    lc_chk_3.setEnabled(false);
                    lc_chk_4.setEnabled(false);
                    lc_chk_5.setEnabled(false);
                    lc_chk_6.setEnabled(false);
                    lc_chk_7.setEnabled(false);
                    lc_chk_8.setEnabled(false);

                    lc_chk_3.setChecked(false);
                    lc_chk_4.setChecked(false);
                    lc_chk_5.setChecked(false);
                    lc_chk_6.setChecked(false);
                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(false);

                } else {
                    contentValues.put("wrong_product", "null");
                }
                break;
            case R.id.lc_chk_2:
                if (isChecked) {
                    lc_chk_1.setChecked(false);
                    contentValues.put("wrong_product", "N");
                    lc_chk_3.setEnabled(true);
                    lc_chk_4.setEnabled(true);
                    lc_chk_5.setEnabled(true);
                    lc_chk_6.setEnabled(true);
                    lc_chk_7.setEnabled(true);
                    lc_chk_8.setEnabled(true);
                } else {
                    contentValues.put("wrong_product", "null");
                }
                break;
            case R.id.lc_chk_3:
                if (isChecked) {
                    lc_chk_4.setChecked(false);
                    contentValues.put("power_supply_proper", "Y");
                    lc_chk_5.setEnabled(true);
                    lc_chk_6.setEnabled(true);
                    lc_chk_7.setEnabled(true);
                    lc_chk_8.setEnabled(true);

                    lc_chk_5.setChecked(false);
                    lc_chk_6.setChecked(false);
                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(false);
                } else {
                    contentValues.put("power_supply_proper", "null");
                }
                break;
            case R.id.lc_chk_4:
                if (isChecked) {
                    lc_chk_3.setChecked(false);
                    contentValues.put("power_supply_proper", "N");
                    lc_chk_5.setChecked(false);
                    lc_chk_6.setChecked(false);
                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(false);

                    lc_chk_5.setEnabled(false);
                    lc_chk_6.setEnabled(false);
                    lc_chk_7.setEnabled(false);
                    lc_chk_8.setEnabled(false);

                } else {
                    contentValues.put("power_supply_proper", "null");
                }
                break;
            case R.id.lc_chk_5:
                if (isChecked) {
                    lc_chk_6.setChecked(false);
                    contentValues.put("driver_working", "Y");
                    lc_chk_7.setEnabled(true);
                    lc_chk_8.setEnabled(true);
                } else {
                    contentValues.put("driver_working", "null");
                }
                break;
            case R.id.lc_chk_6:
                if (isChecked) {
                    lc_chk_5.setChecked(false);

                    contentValues.put("driver_working", "N");

                    lc_chk_7.setChecked(false);
                    lc_chk_8.setChecked(false);
                    lc_chk_7.setEnabled(false);
                    lc_chk_8.setEnabled(false);
                } else {
                    contentValues.put("driver_working", "null");
                }
                break;
            case R.id.lc_chk_7:
                if (isChecked) {
                    lc_chk_8.setChecked(false);
                    contentValues.put("led_is_working", "Y");
                } else {
                    contentValues.put("led_is_working", "null");
                }
                break;
            case R.id.lc_chk_8:
                if (isChecked) {
                    lc_chk_7.setChecked(false);
                    contentValues.put("led_is_working", "N");
                } else {
                    contentValues.put("led_is_working", "null");
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.v1_w1:
                if (!hasFocus) {
                    try {
                        vol_one.setText(" = "
                                + calculateVolume(Integer.parseInt(v1_w1.getText()
                                .toString()), Integer.parseInt(v1_t1
                                .getText().toString()), Integer
                                .parseInt(v1_h1.getText().toString()))
                                + " (Sides) ");
                        contentValues.put("side_volume",
                                vol_one.getText().toString().replace(" = ", "")
                                        .replace(" (Sides) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v1_t1:
                if (!hasFocus) {
                    try {
                        vol_one.setText(" = "
                                + calculateVolume(Integer.parseInt(v1_w1.getText()
                                .toString()), Integer.parseInt(v1_t1
                                .getText().toString()), Integer
                                .parseInt(v1_h1.getText().toString()))
                                + " (Sides) ");
                        contentValues.put("side_volume",
                                vol_one.getText().toString().replace(" = ", "")
                                        .replace(" (Sides) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v1_h1:
                if (!hasFocus) {
                    try {
                        vol_one.setText(" = "
                                + calculateVolume(Integer.parseInt(v1_w1.getText()
                                .toString()), Integer.parseInt(v1_t1
                                .getText().toString()), Integer
                                .parseInt(v1_h1.getText().toString()))
                                + " (Sides) ");
                        contentValues.put("side_volume",
                                vol_one.getText().toString().replace(" = ", "")
                                        .replace(" (Sides) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.v2_w2:
                if (!hasFocus) {
                    try {
                        vol_two.setText(" = "
                                + calculateVolume(Integer.parseInt(v2_w2.getText()
                                .toString()), Integer.parseInt(v2_t2
                                .getText().toString()), Integer
                                .parseInt(v2_h2.getText().toString()))
                                + " (Back) ");
                        contentValues.put("back_volume",
                                vol_two.getText().toString().replace(" = ", "")
                                        .replace(" (Back) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v2_t2:
                if (!hasFocus) {
                    try {
                        vol_two.setText(" = "
                                + calculateVolume(Integer.parseInt(v2_w2.getText()
                                .toString()), Integer.parseInt(v2_t2
                                .getText().toString()), Integer
                                .parseInt(v2_h2.getText().toString()))
                                + " (Back) ");
                        contentValues.put("back_volume",
                                vol_two.getText().toString().replace(" = ", "")
                                        .replace(" (Back) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v2_h2:
                if (!hasFocus) {
                    try {
                        vol_two.setText(" = "
                                + calculateVolume(Integer.parseInt(v2_w2.getText()
                                .toString()), Integer.parseInt(v2_t2
                                .getText().toString()), Integer
                                .parseInt(v2_h2.getText().toString()))
                                + " (Back) ");
                        contentValues.put("back_volume",
                                vol_two.getText().toString().replace(" = ", "")
                                        .replace(" (Back) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v3_w3:
                if (!hasFocus) {
                    try {
                        vol_three.setText(" = "
                                + calculateVolume(Integer.parseInt(v3_w3.getText().toString()),
                                Integer.parseInt(v3_t3.getText().toString()),
                                Integer.parseInt(v3_h3.getText().toString()))
                                + " (Base) ");
                        contentValues.put("base_volume",
                                vol_three.getText().toString().replace(" = ", "")
                                        .replace(" (Base) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v3_t3:
                if (!hasFocus) {
                    try {
                        vol_three.setText(" = "
                                + calculateVolume(Integer.parseInt(v3_w3.getText()
                                .toString()), Integer.parseInt(v3_t3
                                .getText().toString()), Integer
                                .parseInt(v3_h3.getText().toString()))
                                + " (Base) ");
                        contentValues.put("base_volume", vol_three.getText().toString().replace(" = ", "")
                                .replace(" (Base) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v3_h3:
                if (!hasFocus) {
                    try {
                        vol_three.setText(" = "
                                + calculateVolume(Integer.parseInt(v3_w3.getText()
                                .toString()), Integer.parseInt(v3_t3
                                .getText().toString()), Integer
                                .parseInt(v3_h3.getText().toString()))
                                + " (Base) ");
                        contentValues.put("base_volume",
                                vol_three.getText().toString().replace(" = ", "")
                                        .replace(" (Base) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case R.id.v4_w4:
                if (!hasFocus) {
                    try {
                        vol_four.setText(" = "
                                + calculateVolume(Integer.parseInt(v4_w4.getText()
                                .toString()), Integer.parseInt(v4_t4
                                .getText().toString()), Integer
                                .parseInt(v4_h4.getText().toString()))
                                + " (Facia) ");
                        contentValues.put("facia_volume",
                                vol_four.getText().toString().replace(" = ", "")
                                        .replace(" (Facia) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v4_t4:
                if (!hasFocus) {
                    try {
                        vol_four.setText(" = "
                                + calculateVolume(Integer.parseInt(v4_w4.getText()
                                .toString()), Integer.parseInt(v4_t4
                                .getText().toString()), Integer
                                .parseInt(v4_h4.getText().toString()))
                                + " (Facia) ");
                        contentValues.put("facia_volume",
                                vol_four.getText().toString().replace(" = ", "")
                                        .replace(" (Facia) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.v4_h4:
                if (!hasFocus) {
                    try {
                        vol_four.setText(" = "
                                + calculateVolume(Integer.parseInt(v4_w4.getText()
                                .toString()), Integer.parseInt(v4_t4
                                .getText().toString()), Integer
                                .parseInt(v4_h4.getText().toString()))
                                + " (Facia) ");
                        contentValues.put("facia_volume",
                                vol_four.getText().toString().replace(" = ", "")
                                        .replace(" (Facia) ", ""));
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.drawer_wt:
                if (!hasFocus) {
                    try {
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(calculateWeight(Integer.parseInt(drawer_wt.getText().toString()),
                        // 1, 1, "w")));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.drawer_wt_tot:
                if (!hasFocus) {
                    try {
                        // float wt =
                        // Float.parseFloat(vol_one.getText().toString())+Float.parseFloat(vol_two.getText().toString())+Float.parseFloat(vol_three.getText().toString())+Float.parseFloat(vol_four.getText().toString());
                        float wt = Float.parseFloat(getVolume(" = ", " (Sides) ",
                                vol_one))
                                + Float.parseFloat(getVolume(" = ", " (Back) ",
                                vol_two))
                                + Float.parseFloat(getVolume(" = ", " (Base) ",
                                vol_three))
                                + Float.parseFloat(getVolume(" = ", " (Facia) ",
                                vol_four));
                        wt = wt * 700;
                        drawer_wt_tot.setText(String.valueOf(wt));

                        // drawer_wt_tot.setText(String.valueOf(calculateWeight(Integer.parseInt(drawer_wt.getText().toString()),
                        // 1, 1, "w")));
                        contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                                .toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.door_height:
                if (!hasFocus) {
                    try {
                        if (Validation.hasText(door_width)
                                && Validation.hasText(door_height)
                                && Validation.hasText(door_thickness)) {
                            door_weight.setText(String.valueOf(calculateWeight(
                                    Integer.parseInt(door_width.getText()
                                            .toString()), Integer
                                            .parseInt(door_height.getText()
                                                    .toString()), Integer
                                            .parseInt(door_thickness.getText()
                                                    .toString()), "g")));
                            contentValues.put("W_Door_Wt", door_weight.getText()
                                    .toString());
                            door_width.setError(null);
                        } else {
                            contentValues.put("W_Door_Wt", "");
                            com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.door_width:
                if (!hasFocus) {

                    try {
                        if (Validation.hasText(door_width)
                                && Validation.hasText(door_height)
                                && Validation.hasText(door_thickness)) {
                            door_weight.setText(String.valueOf(calculateWeight(Integer.parseInt(door_width.getText().toString()), Integer.parseInt(door_height.getText().toString()), Integer.parseInt(door_thickness.getText().toString()), "g")));
                            contentValues.put("W_Door_Wt", door_weight.getText()
                                    .toString());
                            door_width.setError(null);
                        } else {
                            contentValues.put("W_Door_Wt", "");
                            com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.door_thickness:
                if (!hasFocus) {
                    try {
                        if (Validation.hasText(door_width)
                                && Validation.hasText(door_height)
                                && Validation.hasText(door_thickness)) {
                            door_weight.setText(String.valueOf(calculateWeight(
                                    Integer.parseInt(door_width.getText()
                                            .toString()), Integer
                                            .parseInt(door_height.getText()
                                                    .toString()), Integer
                                            .parseInt(door_thickness.getText()
                                                    .toString()), "g")));
                            contentValues.put("W_Door_Wt", door_weight.getText()
                                    .toString());
                            door_width.setError(null);
                        } else {
                            contentValues.put("W_Door_Wt", "");
                            com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.door_weight:
                if (hasFocus) {
                    try {
                        if (Validation.hasText(door_width)
                                && Validation.hasText(door_height)
                                && Validation.hasText(door_thickness)) {
                            door_weight.setText(String.valueOf(calculateWeight(Integer.parseInt(door_width.getText().toString()), Integer.parseInt(door_height.getText().toString()), Integer.parseInt(door_thickness.getText().toString()), "w")));
                            contentValues.put("W_Door_Wt", door_weight.getText()
                                    .toString());
                            door_width.setError(null);
                        } else {
                            contentValues.put("W_Door_Wt", "");
                            com.sudesi.hafele.utils.UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.handle_weight:
                if (!hasFocus) {
                    try {
                        if (Validation.hasText(door_weight)
                                && Validation.hasText(handle_weight)
                                && Validation.hasText(door_height)) {
                            double power_fac = Integer.parseInt(door_height
                                    .getText().toString())
                                    * (Double.parseDouble(door_weight.getText()
                                    .toString()) + Integer
                                    .parseInt(handle_weight.getText()
                                            .toString()));
                            power_factor.setText(String.valueOf(power_fac));

                        } else {
                            UtilityClass.showToast(context,
                                    "Please enter required information");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

        }
    }

    private String calculateVolume(int weight, int thickess, int height) {
        float volume = 0;
        double wt = (double) weight / 1000;
        Log.d("weight", String.valueOf(wt));
        double hgt = (double) height / 1000;
        Log.d("height", String.valueOf(hgt));
        double thik = (double) thickess / 1000;
        Log.d("thickness", String.valueOf(thik));
        volume = (float) ((float) wt * hgt * thik) * 2;
        Log.d("weight", String.valueOf(wt));
        Log.d("volume", String.valueOf(volume));
        return String.valueOf(volume);
    }

    private void submitData(String product_sub_category) {
        // upload image if any
        long uploadReponse = 0;
        if (fileNameArray.size() > 0) {
            for (int i = 0; i < fileNameArray.size(); i++) {
                if (dbAdapter.checkID(fileNameArray.get(i).getName(),
                        "image_details", "image_name")) {
                    // ContentValues cv = new ContentValues();
                    // cv.put("image_name", fileNameArray.get(i).getName());
                    // cv.put("image_path",
                    // fileNameArray.get(i).getAbsolutePath());
                    // cv.put("complaint_number", complaint_number);
                    // cv.put("original_size",
                    // String.valueOf(orgFileArray.get(i).length()));
                    //
                    // Log.e("original_size",
                    // String.valueOf(orgFileArray.get(i).length()));
                    // cv.put("compressed_size",
                    // String.valueOf(fileNameArray.get(i).length()));
                    //
                    // Log.e("compressed_size",
                    // String.valueOf(fileNameArray.get(i).length()));
                    // cv.put("image_count", fileNameArray.size());
                    // cv.put("Device_id", pref.getDeviceId());
                    // cv.put("upload_status", "NU");
                    // uploadReponse = dbAdapter.update("image_details", cv,
                    // "image_name = '" + fileNameArray.get(i).getName()
                    // + "'", null);
                } else {
                    //					uploadReponse = dbAdapter.saveImageDetails(fileNameArray
                    //							.get(i).getName(), fileNameArray.get(i)
                    //							.getAbsolutePath(), String.valueOf(orgFileArray
                    //							.get(i).length()), String.valueOf(fileNameArray
                    //							.get(i).length()), complaint_number, "NU",
                    //							fileNameArray.size(), pref.getDeviceId());

                    uploadReponse = dbAdapter.saveImageDetails(fileNameArray
                                    .get(i).getName(), fileNameArray.get(i).getAbsolutePath(), imgOriginalSize.get(i), imgCompressedSize.get(i), complaint_number, "NU",
                            fileNameArray.size(), pref.getDeviceId());


                }
            }

            Log.e("imgOriginalSize", imgOriginalSize.toString());
            Log.e("imgCompressedSize", imgCompressedSize.toString());


        }

        // compress and upload video

        if (inputpath != null) {
            if (!inputpath.equalsIgnoreCase("") && is_video_attached == true) {
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(date.getTime());
                File root = new File(Environment.getExternalStorageDirectory()
                        + "/AdStringOVideos/Output/");

                root.mkdirs();

                outvidpath = Environment.getExternalStorageDirectory()
                        + "/AdStringOVideos/Output/" + "OUT_VID" + timeStamp
                        + ".mp4";
                File file = new File(outvidpath);
                // save video details to local db
                // if (dbAdapter.checkID(complaint_number, "video_details",
                // "complaint_number")) {
                // ContentValues cv = new ContentValues();
                // cv.put("VideoFileName", file.getName());
                // cv.put("FilePath", file.getAbsolutePath());
                // cv.put("complaint_number", complaint_number);
                // cv.put("OriginalSize", OriginalSize);
                // cv.put("CompressedSize", String.valueOf(file.length()));
                // cv.put("upload_status", "NU");
                // cv.put("video_count", 1);
                // cv.put("Device_id", pref.getDeviceId());
                // cv.put("username", pref.getUserName());
                // uploadReponse = dbAdapter.update("video_details", cv,
                // "complaint_number = '" + complaint_number + "'",
                // null);
                // } else {

                Log.e("Page", "FaultReport");
                AssetManager assetManager = getAssets();
                InputStream in = null;
                OutputStream out = null;
                File f = new File(getFilesDir(), "logo.png");
                try {
                    in = assetManager.open("logo.png");
                    out = openFileOutput(f.getName(),
                            Context.MODE_WORLD_READABLE);

                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());

                }

                Uri u = Uri.parse("file://" + getFilesDir() + "/logo.png");

                String logo = u.getPath();

                // String[] complexCommand = { "ffmpeg", "-y", "-i",
                // inputpath, "-i", logo, "-strict", "experimental",
                // "-s", "320x240", "-ac", "2", "-ar", "44100", "-b",
                // "256k", "-filter_complex", "overlay", "-vb", "20M",
                // outvidpath };

                String[] complexCommand = {"ffmpeg", "-y", "-i", inputpath,
                        "-i", logo, "-strict", "experimental", "-s", "320x240",/*
                         * "-ac"
						 * ,
						 * "2"
						 * ,
						 * "-ar"
						 * ,
						 * "44100"
						 * ,
						 */
                        "-b", "128k", "-filter_complex", "overlay",
                        /* "-vb", "20M", */
                        outvidpath};
                setCommandComplex(complexCommand);
                setOutputFilePath(outvidpath);
                setProgressDialogTitle("AdStringO Compression");
                setProgressDialogMessage("Depending on your video size, it can take a few minutes..Please Wait..");

                setNotificationIcon(R.drawable.icon);
                setNotificationMessage("Process is running...");
                setNotificationTitle("AdStringO Compression");
                setNotificationfinishedMessageTitle("Process Completed");
                setNotificationfinishedMessageDesc("Click to play demo");
                setNotificationStoppedMessage("Process Failed");
                runTranscoing();

                // 13th October 2015
                // inIt();
                //
                // copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();
                //
                // setInputFilePath(inputpath);
                //
                // setOutputFilePath(outvidpath);
                //
                // setCommandComplex();
                //
                // runTranscoing();

                // uploadReponse = dbAdapter.saveVideoDetails(file.getName(),
                // file.getAbsolutePath(), OriginalSize,
                // String.valueOf(file.length()), complaint_number,
                // "NU", pref.getUserName(), 1, pref.getDeviceId());

				/*
                 * inIt(); copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();
				 * setInputFilePath(inputpath); setOutputFilePath(outvidpath);
				 * setCommandComplex(); runTranscoing();
				 *
				 * copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();
				 *
				 * SaveVideoTask videoTask = new SaveVideoTask();
				 * videoTask.execute(); uploadReponse =
				 * dbAdapter.saveVideoDetails
				 * (file.getName(),file.getAbsolutePath(), OriginalSize,
				 * String.valueOf(file.length()), complaint_number,
				 * "NU",pref.getUserName(),1,pref.getDeviceId());
				 */
                // }

            } else {
                if (is_video_attached == true) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            FaultReportForm.this);
                    builder1.setTitle("Empty file.");
                    builder1.setMessage("Please record video before applying to compression technique.");
                    builder1.setCancelable(true);
                    builder1.setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

        }

        Long response = (long) 0;
        if (product_sub_category.contains("Door Closer")) {
            //Door Closer
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("article_no", article_no.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());

            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());


           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/
            // contentValues.put("door_weight",
            // door_weight.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_sub_category.contains("Floor Springs")) {
            //Floor Springs
            // save data to db
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            // contentValues.put("Door_Wt", door_weight.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sync_status", "NU");

            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());
           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }

        } else if (product_sub_category.contains("Aventos")) {
            //Aventos
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            // contentValues.put("door_weight",
            // door_weight.getText().toString());
            contentValues.put("power_factor", power_factor.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //  contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
//            contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }
*/
            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Furniture Fitting-Sliding Fittings")) {

            Log.e("product_sub_category++++++", product_sub_category);
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            Log.e("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            Log.e("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            Log.e("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());

            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            // contentValues.put("door_weight",
            // door_weight.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());
           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }
*/
            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_sub_category.contains("Architechtural Sliding Fittings")) {
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            // contentValues.put("door_weight",
            // door_weight.getText().toString());
            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());
           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Drawers")) {

            //Drawers
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("v1_w1", v1_w1.getText().toString());
            contentValues.put("v1_h1", v1_h1.getText().toString());
            contentValues.put("v1_t1", v1_t1.getText().toString());
            contentValues.put("v2_w2", v2_w2.getText().toString());
            contentValues.put("v2_h2", v2_h2.getText().toString());
            contentValues.put("v2_t2", v2_t2.getText().toString());
            contentValues.put("v3_w3", v3_w3.getText().toString());
            contentValues.put("v3_h3", v3_h3.getText().toString());
            contentValues.put("v3_t3", v3_t3.getText().toString());
            contentValues.put("v4_w4", v4_w4.getText().toString());
            contentValues.put("v4_h4", v4_h4.getText().toString());
            contentValues.put("v4_t4", v4_t4.getText().toString());

            try {
                drawer_wt_tot.setText(String.valueOf(calculateWeight(
                        Integer.parseInt(drawer_wt.getText().toString()), 1, 1,
                        "w")));
                contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                        .toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //  contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
//            contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Lever Handles")) {
            //Lever Handles
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());
           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());


            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }
            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Pull Handles")) {
            //Pull Handles
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("Thickness", door_handle_thickness.getText()
                    .toString());
            contentValues.put("article_no", article_no.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());


         /*   if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }

        } else if (product_category.contains("Hinges")) {

            //Hinges
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Width", door_width.getText().toString());
            contentValues.put("Height", door_height.getText().toString());
            contentValues.put("Thickness", door_thickness.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("W_Door_Wt", door_weight.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

           /* if (other_reason.getText().toString() != null) {
                if (other_reason.getText().toString().equals("null")) {

                } else {
                    contentValues.put("Reason_For_Unresolved", other_reason
                            .getText().toString());
                }
            }*/
            try {
                if (Validation.hasText(door_width)
                        && Validation.hasText(door_height)
                        && Validation.hasText(door_thickness)) {
                    door_weight.setText(String.valueOf(calculateWeight(Integer
                                    .parseInt(door_width.getText().toString()), Integer
                                    .parseInt(door_height.getText().toString()),
                            Integer.parseInt(door_thickness.getText()
                                    .toString()), "w")));
                    contentValues.put("W_Door_Wt", door_weight.getText()
                            .toString());
                } else {
                    contentValues.put("W_Door_Wt", "");
                    com.sudesi.hafele.utils.UtilityClass.showToast(context,
                            "Please enter required information");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }

        } else if (product_sub_category.contains("Tandem Box plus spacecorner boms")) {

            // Blum Corner Unit

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Width", cabinet_width.getText().toString());
            contentValues.put("Height", cabinet_height.getText().toString());
            contentValues.put("Thickness", cabinet_depth.getText().toString());

            contentValues.put("v1_w1", v1_w1.getText().toString());
            contentValues.put("v1_h1", v1_h1.getText().toString());
            contentValues.put("v1_t1", v1_t1.getText().toString());
            contentValues.put("v2_w2", v2_w2.getText().toString());
            contentValues.put("v2_h2", v2_h2.getText().toString());
            contentValues.put("v2_t2", v2_t2.getText().toString());
            contentValues.put("v3_w3", v3_w3.getText().toString());
            contentValues.put("v3_h3", v3_h3.getText().toString());
            contentValues.put("v3_t3", v3_t3.getText().toString());
            contentValues.put("v4_w4", v4_w4.getText().toString());
            contentValues.put("v4_h4", v4_h4.getText().toString());
            contentValues.put("v4_t4", v4_t4.getText().toString());

            contentValues.put("side_volume", vol_one.getText().toString());
            contentValues.put("back_volume", vol_two.getText().toString());
            contentValues.put("base_volume", vol_three.getText().toString());
            contentValues.put("facia_volume", vol_four.getText().toString());
            // contentValues.put("Drawer_Wt", drawer_wt_tot.getText().toString());

            try {
                drawer_wt_tot.setText(String.valueOf(calculateWeight(
                        Integer.parseInt(drawer_wt_tot.getText().toString()), 1, 1,
                        "w")));
                contentValues.put("Drawer_Wt", drawer_wt_tot.getText()
                        .toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //   contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Corner Solution")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Width", cabinet_width.getText().toString());
            contentValues.put("Height", cabinet_height.getText().toString());
            contentValues.put("Thickness", cabinet_depth.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //  contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_category.contains("Tall Unit")) {

            //Tall Unit
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Width", cabinet_width.getText().toString());
            contentValues.put("Height", cabinet_height.getText().toString());
            contentValues.put("Thickness", cabinet_depth.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //  contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_sub_category.contains("Ironing Board")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Width", cabinet_width.getText().toString());
            contentValues.put("Height", cabinet_height.getText().toString());
            contentValues.put("Thickness", cabinet_depth.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_sub_category.contains("Tavoletto")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("Width", cabinet_width.getText().toString());
            contentValues.put("Height", cabinet_height.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product1.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product1.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_group.contains("Lighting Technology")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //  contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else if (product_sub_category.contains("Pratik")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);


            if (!pr_chk_3.isChecked()) {
                contentValues.put("height_more_than_260mm", pratik_height.getText().toString());
            }
            if (!pr_chk_4.isChecked()) {
                contentValues.put("length_slatted_or_wooden_50mm", pratik_length_50mm.getText().toString());
            }
            if (!pr_chk_5.isChecked()) {
                contentValues.put("widht_slatted_or_wooden_30mm", pratik_width_30mm.getText().toString());
            }
            if (!pr_chk_6.isChecked()) {
                contentValues.put("slatted_dimen_1400mm_2000mm", pratik_sfd_dimen.getText().toString());
            }
            if (!pr_chk_7.isChecked()) {
                contentValues.put("slatted_dimen_1800mm_2000mm", pratik_sfd_dimen1.getText().toString());
            }

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            // contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }

        } else if (product_sub_category.contains("Mortise Lock")) {

            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("article_no", article_no.getText().toString());
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //   contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("wrong_product_reason", "");
            } else {
                contentValues.put("wrong_product_reason", spin_wrong_product.getSelectedItem().toString());
            }

            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
        } else {

            // if (!outvidpath.contains("")) {
            // upload = new UploadVideo();
            // upload.execute();
            // }
            // Long response = (long) 0;
            // if(uploadReponse > 0){
            contentValues.put("Complant_No", complaint_number);
            contentValues.put("Product_Category", product_category);
            contentValues.put("product_Sub_Category", product_sub_category);
            contentValues.put("Comment", comments.getText().toString());
            contentValues.put("sync_status", "NU");
            contentValues.put("Insert_Date", dateTime);
            contentValues.put("article_no", article_no.getText().toString());

            contentValues.put("Result", unresolve_reason.getSelectedItem().toString());
            contentValues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
            contentValues.put("complete_set", edt_complete_set_articleNo.getText().toString());
            //contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());

            if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                contentValues.put("site_Issue_Reason", "");
            } else {
                contentValues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
            }
            contentValues.put("Action", spin_action.getSelectedItem().toString());
            contentValues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());

            Calendar calendar = Calendar.getInstance();
            Date updatedDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_updatedDate = sdf.format(updatedDate);

            Calendar calendar1 = Calendar.getInstance();
            Date closedDate = calendar1.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_closedDate = sdf1.format(closedDate);

            if (contentValues.get("Closure_Status").equals("Resolved")) {
                contentValues.put("Updated_Date", str_updatedDate);
                contentValues.put("Closed_Date", str_closedDate);
            } else {
                contentValues.put("Updated_Date", str_updatedDate);
            }

            if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                    "Complant_No")) {
                response = (long) dbAdapter.update("Fault_Finding_Details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response = dbAdapter.submitQuery(contentValues);
            }
            // }
        }

        if (response > 0) {
            int visitCount = dbAdapter.getVisitCount(complaint_number);
            ContentValues cv = new ContentValues();
            cv.put("is_visited", visitCount + 1);
            cv.put("case_attended", "Y");
            cv.put("status", status.getSelectedItem().toString());
            int updateReponse = dbAdapter.update("complaint_service_details",
                    cv, "complaint_number = '" + complaint_number + "'", null);
            if (updateReponse > 0) {
                if (contentValues.get("Closure_Status") != null) {
                    if (contentValues.get("Closure_Status").equals("Resolved")) {

                        showFeedbackForm(complaint_number);
                        contentValues.clear();
                    } else {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                FaultReportForm.this);
                        builder1.setTitle("Status");
                        builder1.setMessage("Data Saved");
                        builder1.setCancelable(false);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        //										File out = new File(outvidpath);
                                        if (outvidpath != null) {
                                            File file = new File(outvidpath);
                                            File in = new File(inputpath);
                                            if (dbAdapter.checkID(
                                                    complaint_number,
                                                    "video_details",
                                                    "complaint_number")) {

                                                ContentValues cv = new ContentValues();
                                                cv.put("VideoFileName",
                                                        file.getName());
                                                cv.put("FilePath",
                                                        file.getAbsolutePath());
                                                cv.put("complaint_number",
                                                        complaint_number);
                                                cv.put("OriginalSize",
                                                        String.valueOf(in.length()));
                                                cv.put("CompressedSize", String
                                                        .valueOf(file.length()));
                                                cv.put("upload_status", "NU");
                                                cv.put("video_count", 1);
                                                cv.put("Device_id",
                                                        pref.getDeviceId());
                                                cv.put("username",
                                                        pref.getUserName());
                                                dbAdapter
                                                        .update("video_details",
                                                                cv,
                                                                "complaint_number = '"
                                                                        + complaint_number
                                                                        + "'",
                                                                null);
                                                Log.e("ContentValues--FaultUpdate",
                                                        cv.toString());

                                                Log.e("contentvalues", cv.toString());

                                            } else {
                                                dbAdapter.saveVideoDetails(file
                                                                .getName(), file
                                                                .getAbsolutePath(),
                                                        String.valueOf(in.length()),
                                                        String.valueOf(file
                                                                .length()),
                                                        complaint_number, "NU",
                                                        pref.getUserName(), 1,
                                                        pref.getDeviceId());

                                            }


                                        }

                                        dialog.cancel();

                                        System.gc();

                                        Intent homeIntent = new Intent(
                                                FaultReportForm.this,
                                                DashBoardActivity.class);
                                        startActivity(homeIntent);
                                        //

                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        // Intent homeIntent = new
                        // Intent(FaultReportForm.this,DashBoardActivity.class);
                        // startActivity(homeIntent);
                        // finish();
                    }
                } else {


                    System.gc();

                    Intent homeIntent = new Intent(FaultReportForm.this,
                            DashBoardActivity.class);
                    startActivity(homeIntent);
                    // finish();
                }
            }
        } else {
            UtilityClass.showToast(context, "Error");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String getVolume(String replace1, String replace2, TextView vol) {
        String result = "";
        try {
            result = vol.getText().toString().replace(replace1, "")
                    .replace(replace2, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkValidation(String category)

    {
        Boolean result = true;
        try {
            if (category.contains("Floor Springs") || category.equalsIgnoreCase("Floor Springs")) {
                Log.e("Category", "Correct");
                if (!val_one.isChecked() && !val_two.isChecked()
                        && !val_three.isChecked() && !wrong_product.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1 ");
                    result = false;
                } else if (!wrong_product.isChecked()) {
                    Log.e("Wrong", "not checkd");
                    if (!floor_chk_1.isChecked() && !floor_chk_2.isChecked()) {
                        Log.e("Check", "2");
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please Complete Step 2 ");
                        result = false;
                    }

                    if (!floor_chk_2.isChecked()) {
                        if (!floor_chk_3.isChecked()
                                && !floor_chk_4.isChecked()) {
                            Log.e("Check", "3");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 3 ");
                            result = false;
                        }

                        if (!floor_chk_4.isChecked()) {
                            if (!floor_chk_5.isChecked()
                                    && !floor_chk_6.isChecked()) {
                                Log.e("Check", "4");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 4 ");
                                result = false;
                            }

                            if (!floor_chk_5.isChecked()) {
                                if (!floor_chk_7.isChecked()
                                        && !floor_chk_8.isChecked()) {
                                    Log.e("Check", "4");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Complete Step 5 ");
                                    result = false;
                                }
                            }

                        }

                    }

                } else if (floor_chk_1.isChecked() || floor_chk_3.isChecked() || floor_chk_6.isChecked() || floor_chk_7.isChecked()) {
                    result = true;
                }
            }

            if (category.contains("Door Closer") || category.equalsIgnoreCase("Door Closer")) {

                //Door Closer
                if (!val_one.isChecked() && !val_two.isChecked()
                        && !val_three.isChecked() && !wrong_product.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1 ");
                    result = false;
                } else if (!wrong_product.isChecked()) {
                    if (!dc_chk_1.isChecked() && !dc_chk_2.isChecked()) {
                        Log.e("Check", "2");
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please Complete Step 2 ");
                        result = false;
                    }

                    if (!dc_chk_1.isChecked()) {
                        if (!dc_chk_3.isChecked() && !dc_chk_4.isChecked()) {
                            Log.e("Check", "2");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 3 ");
                            result = false;
                        }

                        if (!dc_chk_4.isChecked()) {
                            if (!dc_chk_5.isChecked() && !dc_chk_6.isChecked()) {
                                Log.e("Check", "2");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Check Distance From edge");
                                result = false;
                            }

                            if (!dc_chk_6.isChecked()) {
                                if (!dc_chk_7.isChecked()
                                        && !dc_chk_8.isChecked()) {
                                    Log.e("Check", "2");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Check Depth of door");
                                    result = false;
                                }

                                if (!dc_chk_8.isChecked()) {
                                    if (!dc_chk_9.isChecked()
                                            && !dc_chk_10.isChecked()) {
                                        Log.e("Check", "2");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Check Depth of frame");
                                        result = false;
                                    }

                                    if (!dc_chk_10.isChecked()) {
                                        if (!dc_chk_11.isChecked()
                                                && !dc_chk_12.isChecked()) {
                                            Log.e("Check", "2");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Is Cut out is at center of door");
                                            result = false;
                                        }

                                        if (!dc_chk_12.isChecked()) {
                                            if (!dc_chk_13.isChecked()
                                                    && !dc_chk_14.isChecked()) {
                                                Log.e("Check", "2");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Is Latching speed of door correct");
                                                result = false;
                                            }

                                            if (!dc_chk_14.isChecked()) {
                                                if (!dc_chk_15.isChecked()
                                                        && !dc_chk_16
                                                        .isChecked()) {
                                                    Log.e("Check", "2");
                                                    UtilityClass.showToast(FaultReportForm.this,
                                                            "Is Closing speed of door correct");
                                                    result = false;
                                                }
                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                } else if (dc_chk_2.isChecked() || dc_chk_4.isChecked() || dc_chk_6.isChecked() || dc_chk_8.isChecked() || dc_chk_10.isChecked() || dc_chk_12.isChecked() || dc_chk_14.isChecked()) {
                    result = true;
                }
            }

            if (category.contains("Lever Handles") || category.equalsIgnoreCase("Lever Handles")) {
                if (!dh_chk_1.isChecked() && !dh_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 2");
                }

                if (!dh_chk_2.isChecked()) {
                    if (!dh_chk_3.isChecked() && !dh_chk_4.isChecked()
                            && !dh_chk_5.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 3");
                    }

                    if (!dh_chk_6.isChecked() && !dh_chk_7.isChecked()
                            && !dh_chk_8.isChecked() && !dh_chk_9.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 4");
                    }

                    if (!dh_chk_10.isChecked() && !dh_chk_11.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 5");
                    }

                    if (!dh_chk_10.isChecked()) {
                        if (!dh_chk_12.isChecked() && !dh_chk_13.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 6");
                        }

                        if (!dh_chk_13.isChecked()) {
                            if (!dh_chk_14.isChecked()
                                    && !dh_chk_15.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 7");
                            }
                        }

                    }

                }

            }

            if (category.contains("Pull Handles") || category.equalsIgnoreCase("Pull Handles")) {
                //Pull Handles
                if (!ph_chk_1.isChecked() && !ph_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }
                if (!ph_chk_2.isChecked() && ph_chk_1.isChecked()) {

                    boolean has_thickness = Validation.hasText(door_handle_thickness);
                    if (!has_thickness)
                        UtilityClass.showToast(context, "Enter door thickness");

                   /* if (door_handle_thickness.getText().toString().equals("")) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }*/

                    if (!ph_chk_3.isChecked() && !ph_chk_4.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                }

            }

            if (category.contains("Aventos") || category.equalsIgnoreCase("Aventos")) {
                //Aventos
                if (!ls_chk_1.isChecked() && !ls_chk_2.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1 ");
                    result = false;
                } else {
                    if (!ls_chk_2.isChecked()) {
                        if (!ls_chk_3.isChecked() && !ls_chk_4.isChecked()) {
                            Log.e("Check", "2");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 2 ");
                            result = false;
                        }

                        if (!ls_chk_4.isChecked()) {
                            if (!ls_chk_5.isChecked() && !ls_chk_6.isChecked()) {
                                Log.e("Check", "3");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 2 ");
                                result = false;
                            }

                            if (!ls_chk_6.isChecked()) {
                                if (!ls_chk_7.isChecked()
                                        && !ls_chk_8.isChecked()) {
                                    Log.e("Check", "4");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Complete Step 2 ");
                                    result = false;
                                }

                                if (!ls_chk_8.isChecked()) {
                                    if (!ls_chk_9.isChecked()
                                            && !ls_chk_10.isChecked()) {
                                        Log.e("Check", "5");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Complete Step 3 ");
                                        result = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (category.contains("Furniture Fitting-Sliding Fittings") || category.contains("Architechtural Sliding Fittings")) {
                if (!sf_chk_1.isChecked() && !sf_chk_2.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1");
                    result = false;
                }

                if (!sf_chk_2.isChecked()) {
                    if (category.contains("Architechtural Sliding Fittings")) {

                        if (sf_chk_1.isChecked()) {
                            if (!sf_chk_5.isChecked() && !sf_chk_6.isChecked()) {
                                Log.e("Check", "3");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 2");
                                result = false;
                            }

                            if (sf_chk_5.isChecked()) {
                                if (!sf_chk_7.isChecked() && !sf_chk_8.isChecked()) {
                                    Log.e("Check", "4");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Complete Step 2");
                                    result = false;
                                }

                                if (sf_chk_7.isChecked()) {
                                    if (!sf_chk_9.isChecked() && !sf_chk_10.isChecked()) {
                                        Log.e("Check", "4");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Complete Step 3");
                                        result = false;
                                    }
                                }

                                if (sf_chk_10.isChecked()) {
                                    if (!sf_chk_11.isChecked() && !sf_chk_12.isChecked()) {
                                        Log.e("Check", "5");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Complete Step 3");
                                        result = false;
                                    }
                                }
                            }
                        }
                    } else {
                        if (sf_chk_1.isChecked()) {

                            if (!sf_chk_3.isChecked() && !sf_chk_4.isChecked()) {
                                Log.e("Check", "2");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 1");
                                result = false;
                            }

                            if (sf_chk_3.isChecked()) {
                                if (!sf_chk_5.isChecked() && !sf_chk_6.isChecked()) {
                                    Log.e("Check", "3");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Complete Step 2");
                                    result = false;
                                }

                                if (sf_chk_5.isChecked()) {
                                    if (!sf_chk_7.isChecked() && !sf_chk_8.isChecked()) {
                                        Log.e("Check", "4");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Complete Step 2");
                                        result = false;
                                    }

                                    if (!sf_chk_8.isChecked()) {
                                        if (!sf_chk_9.isChecked()
                                                && !sf_chk_10.isChecked()) {
                                            Log.e("Check", "4");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please Complete Step 3");
                                            result = false;
                                        }
                                    }

                                    if (sf_chk_10.isChecked()) {
                                        if (!sf_chk_11.isChecked()
                                                && !sf_chk_12.isChecked()) {
                                            Log.e("Check", "5");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please Complete Step 3");
                                            result = false;
                                        }

                                        if (sf_chk_11.isChecked()) {
                                            if (!sf_chk_13.isChecked()
                                                    && !sf_chk_14.isChecked()) {
                                                Log.e("Check", "5");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please Complete Step 3");
                                                result = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                } else if (sf_chk_2.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                        result = false;
                    UtilityClass.showToast(context, "Select Wrong Product Reason");

                }
            }

            if (category.contains("Drawers") || category.equalsIgnoreCase("Drawers")) { // TODO
                //Drawers
                if (!Validation.hasText(vol_one)) {
                    Log.e("vol_one", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please enter volume one");
                    result = false;
                }

                if (!Validation.hasText(vol_two)) {
                    Log.e("vol_one", "2");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please enter volume two");
                    result = false;
                }

                if (!Validation.hasText(vol_three)) {
                    Log.e("vol_three", "3");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please enter volume three");
                    result = false;
                }

                if (!Validation.hasText(vol_four)) {
                    Log.e("vol_four", "4");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please enter volume four");
                    result = false;
                }

                if (!Validation.hasText(drawer_wt_tot)) {
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please weight of drawer");
                    result = false;
                }

                if (!vc_chk_1.isChecked() && !vc_chk_2.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1");
                    result = false;
                }


                if (!vc_chk_2.isChecked()) {
                    if (!rc_chk_1.isChecked() && !rc_chk_2.isChecked()) {
                        Log.e("Check", "2");
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please Complete Step 2");
                        result = false;
                    }

                    if (!rc_chk_2.isChecked()) {
                        if (!rc_chk_3.isChecked() && !rc_chk_4.isChecked()) {
                            Log.e("Check", "3");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 3");
                            result = false;
                        }

                        if (!rc_chk_4.isChecked()) {
                            if (!rc_chk_5.isChecked() && !rc_chk_6.isChecked()) {
                                Log.e("Check", "4");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 4");
                                result = false;
                            }

                            if (!rc_chk_6.isChecked()) {
                                if (!rc_chk_7.isChecked()
                                        && !rc_chk_8.isChecked()) {
                                    Log.e("Check", "5");
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please Complete Step 5");
                                    result = false;
                                }

                                if (!rc_chk_8.isChecked()) {
                                    if (!rc_chk_9.isChecked()
                                            && !rc_chk_10.isChecked()) {
                                        Log.e("Check", "6");
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please Complete Step 6");
                                        result = false;
                                    }

                                    if (!rc_chk_10.isChecked()) {
                                        if (!rc_chk_11.isChecked()
                                                && !rc_chk_12.isChecked()) {
                                            Log.e("Check", "7");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please Complete Step 7");
                                            result = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // TODO
            }
            if (category.contains("Mortise Lock") || category.equalsIgnoreCase("Mortise Lock")) {

                if (!ml_chk_5.isChecked() && !ml_chk_6.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1");
                    result = false;
                }


                if (ml_chk_5.isChecked()) {

                    if (!ml_chk_1.isChecked() && !ml_chk_2.isChecked()) {
                        Log.e("Check", "1");
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please Complete Step 2");
                        result = false;
                    }


                    if (ml_chk_1.isChecked()) {
                        if (!ml_chk_3.isChecked() && !ml_chk_4.isChecked()) {
                            Log.e("Check", "2");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 3");
                            result = false;
                        }
                    }
                } else if (ml_chk_6.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                        result = false;
                    UtilityClass.showToast(context, "Select Wrong Product Reason");
                }

            }
            if (category.contains("Hinges") || category.equalsIgnoreCase("Hinges")) {
                // Hinges
                // TODO
                if (!hn_chk_1.isChecked() && !hn_chk_2.isChecked()) {
                    Log.e("Check", "1");
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please Complete Step 1");
                    result = false;
                }

                if (!hn_chk_2.isChecked()) {
                    if (!hn_chk_3.isChecked() && !hn_chk_4.isChecked()) {
                        Log.e("Check", "2");
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please Complete Step 2");
                        result = false;
                    }

                    if (!hn_chk_4.isChecked()) {
                        if (!hn_chk_5.isChecked() && !hn_chk_6.isChecked()) {
                            Log.e("Check", "3");
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please Complete Step 2");
                            result = false;
                        }

                        if (!hn_chk_6.isChecked()) {
                            if (!hn_chk_7.isChecked() && !hn_chk_8.isChecked()) {
                                Log.e("Check", "4");
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please Complete Step 2");
                                result = false;
                            }
                        }

                    }
                }
            }


            if (category.contains("Corner Solution") && category.equalsIgnoreCase("Corner Solution")) {
                if (!cu_chk_1.isChecked() && !cu_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }

                if (cu_chk_1.isChecked()) {

                    if (!cu_chk_3.isChecked() && !cu_chk_4.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (cu_chk_3.isChecked()) {
                        if ((cabinet_width.getText().toString().length() == 0) && (cabinet_height.getText().toString().length() == 0) && (cabinet_depth.getText().toString().length() == 0)) {

                            boolean has_width = Validation.hasText(cabinet_width);
                            boolean has_height = Validation.hasText(cabinet_height);
                            boolean has_thickness = Validation.hasText(cabinet_depth);

                            if (!has_width)
                                UtilityClass.showToast(context,
                                        "Enter Cabinet width");
                            if (!has_height)
                                UtilityClass.showToast(context,
                                        "Enter Cabinet height");
                            if (!has_thickness)
                                UtilityClass.showToast(context,
                                        "Enter Cabinet Depth");

                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }

                        if (!cu_chk_5.isChecked() && !cu_chk_6.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }

                        if (cu_chk_5.isChecked()) {
                            if (!cu_chk_7.isChecked() && !cu_chk_8.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 4");
                            }
                            if (cu_chk_7.isChecked()) {

                                if (!cu_chk_9.isChecked() && !cu_chk_10.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please complete Step 5");
                                }
                                if (cu_chk_9.isChecked()) {

                                    if (!cu_chk_11.isChecked()
                                            && !cu_chk_12.isChecked()) {
                                        result = false;
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please complete Step 6");
                                    }
                                    if (cu_chk_11.isChecked()) {

                                        if (!cu_chk_13.isChecked()
                                                && !cu_chk_14.isChecked()) {
                                            result = false;
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please complete Step 7");
                                        }
                                    }
                                }
                            }

                        }
                    }
                } else if (cu_chk_2.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                        result = false;
                    UtilityClass.showToast(context, "Select Wrong Product Reason");

                } else if (cu_chk_2.isChecked() || cu_chk_4.isChecked() || cu_chk_6.isChecked() || cu_chk_8.isChecked() || cu_chk_10.isChecked() || cu_chk_12.isChecked() || cu_chk_14.isChecked()) {
                    result = true;
                }

            }

            if (category.contains("Tall Unit") || category.equalsIgnoreCase("Tall Unit")) {
                if (!tall_chk_1.isChecked() && !tall_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");

                }
                if (tall_chk_1.isChecked()) {
                    if ((cabinet_width.getText().toString().length() == 0) && (cabinet_height.getText().toString().length() == 0) && (cabinet_depth.getText().toString().length() == 0)) {

                        boolean has_width = Validation.hasText(cabinet_width);
                        boolean has_height = Validation.hasText(cabinet_height);
                        boolean has_thickness = Validation.hasText(cabinet_depth);

                        if (!has_width)
                            UtilityClass.showToast(context,
                                    "Enter Cabinet width");
                        if (!has_height)
                            UtilityClass.showToast(context,
                                    "Enter Cabinet height");
                        if (!has_thickness)
                            UtilityClass.showToast(context,
                                    "Enter Cabinet Depth");

                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 1");
                    }
                    if (tall_chk_1.isChecked()) {
                        if (!tall_chk_3.isChecked() && !tall_chk_4.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 2");
                        }

                        if (tall_chk_3.isChecked()) {
                            if (!tall_chk_5.isChecked() && !tall_chk_6.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 3");
                            }
                            if (tall_chk_5.isChecked()) {
                                if (!tall_chk_7.isChecked() && !tall_chk_8.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please complete Step 4");
                                }
                                if (tall_chk_7.isChecked()) {
                                    if (!tall_chk_9.isChecked() && !tall_chk_10.isChecked()) {
                                        result = false;
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please complete Step 5");

                                    }
                                    if (tall_chk_9.isChecked()) {
                                        {
                                            if (!tall_chk_11.isChecked()
                                                    && !tall_chk_12.isChecked()) {
                                                result = false;
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please complete Step 6");
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                } else if (tall_chk_2.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                        result = false;
                    UtilityClass.showToast(context, "Select Wrong Product Reason");

                } else if (tall_chk_2.isChecked() || tall_chk_4.isChecked() || tall_chk_6.isChecked() || tall_chk_8.isChecked() || tall_chk_10.isChecked() || tall_chk_12.isChecked()) {
                    result = true;
                }
            }

            if (category.contains("Ironing Board") || category.equalsIgnoreCase("Ironing Board")) {
                if (!ib_chk_1.isChecked() && !ib_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }

                if (ib_chk_1.isChecked()) {
                    if (!ib_chk_3.isChecked() && !ib_chk_4.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (ib_chk_4.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                            result = false;
                        UtilityClass.showToast(context, "Select Wrong Product Reason");

                    }
                    if ((cabinet_width.getText().toString().length() == 0) && (cabinet_height.getText().toString().length() == 0) && (cabinet_depth.getText().toString().length() == 0)) {

                        boolean has_width = Validation.hasText(cabinet_width);
                        boolean has_height = Validation.hasText(cabinet_height);
                        boolean has_thickness = Validation.hasText(cabinet_depth);

                        if (!has_width)
                            UtilityClass.showToast(context, "Enter Cabinet width");
                        if (!has_height)
                            UtilityClass.showToast(context, "Enter Cabinet height");
                        if (!has_thickness)
                            UtilityClass.showToast(context, "Enter Cabinet Depth");

                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }

                    if (ib_chk_3.isChecked()) {
                        if (!ib_chk_5.isChecked() && !ib_chk_6.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }
                        if (ib_chk_5.isChecked()) {
                            if (!ib_chk_7.isChecked() && !ib_chk_8.isChecked() && !ib_chk_9.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 4");
                            }
                            if (ib_chk_7.isChecked() || ib_chk_8.isChecked() || ib_chk_9.isChecked()) {

                                if (!ib_chk_10.isChecked() && !ib_chk_11.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please complete Step 5");
                                }
                            }
                        }
                    }

                } else if (ib_chk_2.isChecked() || ib_chk_4.isChecked() || ib_chk_6.isChecked() || ib_chk_11.isChecked()) {
                    result = true;
                }

            }

            if (category.contains("Tavoletto") || category.equalsIgnoreCase("Tavoletto")) {
                if ((cabinet_width.getText().toString().length() == 0) && (cabinet_height.getText().toString().length() == 0)) {

                    boolean has_width = Validation.hasText(cabinet_width);
                    boolean has_height = Validation.hasText(cabinet_height);

                    if (!has_width)
                        UtilityClass.showToast(context,
                                "Enter Cabinet width");
                    if (!has_height)
                        UtilityClass.showToast(context,
                                "Enter Cabinet height");
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }

                if (!tv_chk_1.isChecked() && !tv_chk_2.isChecked() && !tv_chk_3.isChecked() && !tv_chk_4.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");

                }
                if (tv_chk_1.isChecked() || tv_chk_2.isChecked() || tv_chk_3.isChecked()) {
                    if (!tv_chk_5.isChecked() && !tv_chk_6.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (tv_chk_5.isChecked()) {
                        if (!tv_chk_7.isChecked() && !tv_chk_8.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }
                    }
                }
                if (tv_chk_4.isChecked()) {
                    if (tv_chk_4.isChecked() && spin_wrong_product1.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        if (spin_wrong_product1.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                            result = false;
                        UtilityClass.showToast(context, "Select Wrong Product Reason");
                    }
                }
            }
            if (category.contains("Lighting Technology") || category.equalsIgnoreCase("Lighting Technology")) {
                if (!lc_chk_1.isChecked() && !lc_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");

                }
                if (lc_chk_2.isChecked()) {
                    if (!lc_chk_3.isChecked() && !lc_chk_4.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (lc_chk_3.isChecked()) {
                        if (!lc_chk_5.isChecked() && !lc_chk_6.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }
                        if (lc_chk_5.isChecked()) {
                            if (!lc_chk_7.isChecked() && !lc_chk_8.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 4");
                            }
                        }
                    }

                } else if (lc_chk_1.isChecked() || lc_chk_4.isChecked() || lc_chk_6.isChecked() || lc_chk_8.isChecked()) {
                    result = true;
                }
            }
            if (category.contains("Pratik") || category.equalsIgnoreCase("Pratik")) {
                if (!pr_chk_1.isChecked() && !pr_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }
                if (pr_chk_1.isChecked()) {
                    if (!pr_chk_3.isChecked() && pratik_height.getText().length() == 0) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Enter Height");
                    }
                    if (!pr_chk_4.isChecked() && pratik_length_50mm.getText().length() == 0) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Enter Length");
                    }
                    if (!pr_chk_5.isChecked() && pratik_width_30mm.getText().length() == 0) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Enter Width");
                    }
                    if (!pr_chk_6.isChecked() && pratik_sfd_dimen.getText().length() == 0) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Enter Dimension");
                    }
                    if (!pr_chk_7.isChecked() && pratik_sfd_dimen1.getText().length() == 0) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Enter Dimension");
                    }
                    if (!pr_chk_8.isChecked() && !pr_chk_9.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (pr_chk_8.isChecked()) {
                        if (!pr_chk_10.isChecked() && !pr_chk_11.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }
                        if (pr_chk_10.isChecked()) {
                            if (!pr_chk_12.isChecked() && !pr_chk_13.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 3");
                            }

                            if (pr_chk_12.isChecked()) {
                                if (!pr_chk_14.isChecked() && !pr_chk_15.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please complete Step 4");
                                }

                            }
                        }
                    }
                } else if (pr_chk_2.isChecked()) {
                    if (pr_chk_2.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                            result = false;
                            UtilityClass.showToast(context, "Select Wrong Product Reason");
                        } else {
                            result = true;
                        }
                    }
                } else if (pr_chk_9.isChecked() || pr_chk_11.isChecked() || pr_chk_13.isChecked() || pr_chk_15.isChecked()) {
                    result = true;
                }
            }

            if (category.contains("Tandem Box plus spacecorner boms") || category.equalsIgnoreCase("Tandem Box plus spacecorner boms")) {

                if ((cabinet_width.getText().toString().length() == 0) && (cabinet_height.getText().toString().length() == 0) && (cabinet_depth.getText().toString().length() == 0)) {

                    boolean has_width = Validation.hasText(cabinet_width);
                    boolean has_height = Validation.hasText(cabinet_height);
                    boolean has_thickness = Validation.hasText(cabinet_depth);

                    if (!has_width)
                        UtilityClass.showToast(context,
                                "Enter Cabinet width");
                    if (!has_height)
                        UtilityClass.showToast(context,
                                "Enter Cabinet height");
                    if (!has_thickness)
                        UtilityClass.showToast(context,
                                "Enter Cabinet Depth");

                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }
                if (!blcr_chk_1.isChecked() && !blcr_chk_2.isChecked()) {
                    result = false;
                    UtilityClass.showToast(FaultReportForm.this,
                            "Please complete Step 1");
                }
                if (blcr_chk_1.isChecked()) {

                    if (!blcr_chk_3.isChecked() && !blcr_chk_4.isChecked()) {
                        result = false;
                        UtilityClass.showToast(FaultReportForm.this,
                                "Please complete Step 2");
                    }
                    if (blcr_chk_4.isChecked() && spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        if (spin_wrong_product.getSelectedItem().toString().equalsIgnoreCase("--Select--"))
                            result = false;
                        UtilityClass.showToast(context, "Select Wrong Product Reason");

                    }
                    if (blcr_chk_3.isChecked()) {

                        if (!blcr_chk_5.isChecked() && !blcr_chk_6.isChecked()) {
                            result = false;
                            UtilityClass.showToast(FaultReportForm.this,
                                    "Please complete Step 3");
                        }
                        if (blcr_chk_5.isChecked()) {
                            if (!blcr_chk_7.isChecked() && !blcr_chk_8.isChecked() && !blcr_chk_9.isChecked() && !blcr_chk_10.isChecked() && !blcr_chk_11.isChecked() && !blcr_chk_12.isChecked()) {
                                result = false;
                                UtilityClass.showToast(FaultReportForm.this,
                                        "Please complete Step 4");
                            }
                            if (blcr_chk_5.isChecked()) {
                                if (!blcr_chk_7.isChecked() && !blcr_chk_8.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Is Alignment Correct");
                                }
                                if (blcr_chk_7.isChecked()) {
                                    if (!blcr_chk_9.isChecked() && !blcr_chk_10.isChecked()) {
                                        result = false;
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Correct No of Screws is used");
                                    }
                                }
                                if (blcr_chk_9.isChecked()) {
                                    if (!blcr_chk_11.isChecked() && !blcr_chk_12.isChecked()) {
                                        result = false;
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Check space between screws is correct");
                                    }
                                }
                            }
                            if (blcr_chk_11.isChecked()) {
                                if (!blcr_chk_13.isChecked() && !blcr_chk_14.isChecked()) {
                                    result = false;
                                    UtilityClass.showToast(FaultReportForm.this,
                                            "Please complete Step 5");
                                }
                                if (blcr_chk_14.isChecked()) {

                                    if (!blcr_chk_15.isChecked() && !blcr_chk_16.isChecked()) {
                                        result = false;
                                        UtilityClass.showToast(FaultReportForm.this,
                                                "Please complete Step 6");
                                    }
                                    if (blcr_chk_16.isChecked()) {

                                        if (!Validation.hasText(vol_one)) {
                                            Log.e("vol_one", "1");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please enter volume one");
                                            result = false;
                                        }
                                        if (!Validation.hasText(vol_two)) {
                                            Log.e("vol_one", "2");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please enter volume two");
                                            result = false;
                                        }
                                        if (!Validation.hasText(vol_three)) {
                                            Log.e("vol_three", "3");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please enter volume three");
                                            result = false;
                                        }
                                        if (!Validation.hasText(vol_four)) {
                                            Log.e("vol_four", "4");
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please enter volume four");
                                            result = false;
                                        }
                                        if (!Validation.hasText(drawer_wt_tot)) {
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Please weight of drawer");
                                            result = false;
                                        }
                                        if (!vc_chk_11.isChecked() && !vc_chk_22.isChecked()) {
                                            result = false;
                                            UtilityClass.showToast(FaultReportForm.this,
                                                    "Weight and Length correct");
                                        }
                                        if (blcr_chk_17.isChecked()) {
                                            if (!blcr_chk_19.isChecked() && !blcr_chk_20.isChecked()) {
                                                result = false;
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please complete Step 8");
                                            }
                                            if (blcr_chk_19.isChecked()) {

                                                if (!blcr_chk_21.isChecked() && !blcr_chk_22.isChecked()) {
                                                    result = false;
                                                    UtilityClass.showToast(FaultReportForm.this,
                                                            "Please complete Step 9");
                                                }
                                            }
                                        }
                                        if (vc_chk_11.isChecked()) {

                                            if (!blcr_chk_17.isChecked() && !blcr_chk_18.isChecked()) {
                                                result = false;
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please complete Step 7");

                                            }
                                            if (!Validation.hasText(vol_one)) {
                                                Log.e("vol_one", "1");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please enter volume one");
                                                result = false;
                                            }
                                            if (!Validation.hasText(vol_two)) {
                                                Log.e("vol_one", "2");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please enter volume two");
                                                result = false;
                                            }
                                            if (!Validation.hasText(vol_three)) {
                                                Log.e("vol_three", "3");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please enter volume three");
                                                result = false;
                                            }
                                            if (!vc_chk_1.isChecked() && !vc_chk_2.isChecked()) {
                                                Log.e("Check", "1");
                                                UtilityClass.showToast(FaultReportForm.this,
                                                        "Please Complete Step 6");
                                                result = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (blcr_chk_2.isChecked() || blcr_chk_4.isChecked() || blcr_chk_6.isChecked() || blcr_chk_8.isChecked() || blcr_chk_10.isChecked() || blcr_chk_12.isChecked() || blcr_chk_14.isChecked() || blcr_chk_16.isChecked() || blcr_chk_18.isChecked() || blcr_chk_20.isChecked() || blcr_chk_22.isChecked()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        //***************
        if (spinner.getId() == R.id.unresolve_reason) {
            if (unresolve_reason.getSelectedItem().toString().equals("Product Defect")) {
                ll_product_defect.setVisibility(View.VISIBLE);
                ll_site_issue.setVisibility(View.GONE);
                spin_siteIssueReason_reason.setSelection(0);

            } else if (unresolve_reason.getSelectedItem().toString().equals("Site Issues")) {
                ll_product_defect.setVisibility(View.GONE);
                ll_site_issue.setVisibility(View.VISIBLE);
                edt_spare_defect_articleNo.setText("");
                edt_complete_set_articleNo.setText("");

                //    other_reason.setText("null");
                               /* contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                                .toString());*/
            } else {
                ll_product_defect.setVisibility(View.GONE);
                ll_site_issue.setVisibility(View.GONE);
                spin_siteIssueReason_reason.setSelection(0);
                edt_spare_defect_articleNo.setText("");
                edt_complete_set_articleNo.setText("");
                              /*  contentValues.put("Reason_For_Unresolved",
                                        unresolve_reason.getSelectedItem()
                                               .toString());*/
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

public class SendSMSAsync extends AsyncTask<Void, Void, Boolean> {

    String result;

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO Auto-generated method stub
        boolean value = webservice.sendSMS(mobile_no.getText().toString().trim(), message);
        return value;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result != null) {
            if (result == true) {
                Toast.makeText(getApplicationContext(), "SMS sent successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SMS was not sent", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SMS was not sent", Toast.LENGTH_LONG).show();
        }
    }
}
}
