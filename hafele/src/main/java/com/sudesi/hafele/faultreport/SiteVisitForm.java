package com.sudesi.hafele.faultreport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import android.database.DatabaseUtils;
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
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.sudesi.hafele.classes.Sanitary_Details;
import com.sudesi.hafele.classes.SignatureView;
import com.sudesi.hafele.classes.VideoData;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.utils.UtilityClass;
import com.sudesi.hafele.utils.Validation;
import com.sudesi.hafele.webservice.HafeleWebservice;

public class SiteVisitForm extends BaseWizard implements OnClickListener,
        OnCheckedChangeListener {


    TextView mobile_no;
    String complaint_number;
    String complaint_date;
    HafeleFaultReportDBAdapter dbAdapter;
    Context context;
    HafelePreference pref;
    EditText comments;
    ContentValues contentvalues;
    String signatureFile, signaturePath;
    int stars = 0;
    TextView date;
    EditText article_number;
    ArrayList<Bitmap> bmpArray;
    ArrayList<Bitmap> capturedBM;
    ArrayList<File> fileNameArray;
    ArrayList<File> orgFileArray;
    ArrayList<String> imgOriginalSize;
    ArrayList<String> imgCompressedSize;
    ImageGridAdapter imgAdapter;
    int is_visited;
    ImageView video_grid;
    GridView img_grid;
    private Uri fileUri;
    String base64;
    int secs;
    boolean is_video_attached = false;
    Bitmap compressedBitmap;
    String CCExecutive;
    static Uri mCapturedImageURI;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_VIDEO_FROM_GALLARY_ACTIVITY_REQUEST_CODE = 300;
    private static String Video_Status = "";
    String outvidpath;
    String inputpath;
    String OriginalSize, CompressedSize, original_str_resolution;
    TextView hafele_rate;
    File videoFile;
    EditText other_reason;
    String dateTime;
    ArrayList<String> reasonList, reasonList1, resultList, actionList;
    Spinner unresolve_reason;
    CheckBox radio_1, radio_2, radio_3, radio_4, radio_5, radio_6, radio_7,
            radio_8, radio_9, radio_10;
    Spinner status;
    Spinner spin_siteIssueReason_reason, spin_action;
    EditText edt_spare_defect_articleNo, edt_complete_set_articleNo;
    LinearLayout ll_site_issue, ll_product_defect, ll_action;
    CheckBox chk_1, chk_2;

    EditText cabinet_height, cabinet_width, cabinet_depth;

    String message;

    String product_category, product_sub_category;

    private DeleteFileBroadcastReceiver deleteFilesAlarm;
    String end_user_mobile;

    HafeleWebservice webservice;

    SendSMSAsync sendSMSAsync;
    String product_group;
    ContentValues contentValues = new ContentValues();
    //............sanitary service
    Sanitary_Details report1 = null;
    LinearLayout sanitaty_service;
    RadioGroup radio_group_sanitary_service;
    RadioButton radio_sanitary_service_wrong_product, radio_sanitary_service_wrong_installation, radio_sanitary_service_product_damage, radio_sanitary_service_guidance_given;
    Spinner spin_sanitary_service_warranty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.site_visit_form);

        context = this;
        pref = new HafelePreference(context);
        dbAdapter = new HafeleFaultReportDBAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        contentvalues = new ContentValues();

        deleteFilesAlarm = new DeleteFileBroadcastReceiver();

        deleteFilesAlarm.SetAlarm(SiteVisitForm.this);

        webservice = new HafeleWebservice(SiteVisitForm.this);

        setWorkingFolder("/sdcard/Android/data/qwerty/sd23/asdf/");

        copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();

        bmpArray = new ArrayList<Bitmap>();
        capturedBM = new ArrayList<Bitmap>();
        fileNameArray = new ArrayList<File>();
        orgFileArray = new ArrayList<File>();
        imgOriginalSize = new ArrayList<String>();
        imgCompressedSize = new ArrayList<String>();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set status list
        String[] array = getResources().getStringArray(R.array.reason_array_2);
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

        // DecimalFormat formatter = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        dateTime = month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + +calendar.get(Calendar.YEAR) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);

        Intent intent = getIntent();
        complaint_number = intent.getStringExtra("complaint_number");
        complaint_date = intent.getStringExtra("complaint_date");
        String c_name = intent.getStringExtra("end_user_name");
        end_user_mobile = intent.getStringExtra("end_user_mobile");
        String service_franchise = intent.getStringExtra("user_type");
        String article = intent.getStringExtra("article_no");
        String c_address = intent.getStringExtra("end_user_address");
        String sales_region = intent.getStringExtra("end_user_region");
        String sales_sub_region = intent.getStringExtra("sales_sub_region");
        product_group = intent.getStringExtra("product_group");
        product_category = intent.getStringExtra("product_category");
        product_sub_category = intent.getStringExtra("product_sub_category");
        String service_details = intent.getStringExtra("service_details");
        is_visited = intent.getIntExtra("is_visited", -1);
        CCExecutive = intent.getStringExtra("Created_By");

        message = "Dear+Customer+,+Your+case+" + complaint_number + "+is+closed.+If+the+problem+still+persist,+do+call+us+on+toll+free+no.+18002666667+within+2+working+days.+-+Customer+Care";

        //		message = "DearCustomer,Yourcase"+complaint_number+"isclosed.Iftheproblemstillpersist,docallusontollfreeno.18002666667within2workingdays-CustomerCare";
        sanitaty_service = (LinearLayout) findViewById(R.id.sanitaty_service);
        radio_group_sanitary_service = (RadioGroup) findViewById(R.id.radio_group_sanitary_service);

        radio_sanitary_service_wrong_product = (RadioButton) findViewById(R.id.radio_sanitary_service_wrong_product);
        radio_sanitary_service_wrong_installation = (RadioButton) findViewById(R.id.radio_sanitary_service_wrong_installation);
        radio_sanitary_service_product_damage = (RadioButton) findViewById(R.id.radio_sanitary_service_product_damage);
        radio_sanitary_service_guidance_given = (RadioButton) findViewById(R.id.radio_sanitary_service_guidance_given);
        spin_sanitary_service_warranty = (Spinner) findViewById(R.id.spin_sanitary_service_warranty);


        Log.v("message", message);

        if (product_category != null) {
            if (product_category.trim() != null) {
                product_category = product_category.trim();
            }
        }

        if (product_group != null) {
            if (product_group.trim() != null) {
                product_group = product_group.trim();
            }
        }

        // product_group="Lighting Technology";
      /*  if (product_group.contains("Lighting Technology")) {
            setContentView(R.layout.new_lighting_service);
            chk_1 = (CheckBox) findViewById(R.id.chk_1);
            chk_2 = (CheckBox) findViewById(R.id.chk_2);
            chk_1.setOnCheckedChangeListener(this);
            chk_2.setOnCheckedChangeListener(this);
        }
*/
        // get data from database
        FaultReport report = null;
        List<ImageData> imgData = null;
        List<VideoData> vidData = null;
        if (is_visited > 0) {
            report = dbAdapter.getFaultReportDetails(pref.getUserName(),
                    complaint_number);
            report1 = dbAdapter.getSanitaryReportDetails(pref.getUserName(),
                    complaint_number);
            imgData = dbAdapter.getImageData(complaint_number, "ALL");
            vidData = dbAdapter.getVideoData(complaint_number);

        }
        //.................................sanitary servie......................
        if (product_sub_category != null && product_sub_category.contains("Sanitary_abc")) {
            sanitaty_service.setVisibility(View.VISIBLE);

            if (report1 != null) {
                if (report1.sanitary_product != null) {
                    if (report1.sanitary_product.equals("Wrong Product")) {
                        radio_sanitary_service_wrong_product.setChecked(true);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                    if (report1.sanitary_product.equals("Wrong Installation")) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(true);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                    if (report1.sanitary_product.equals("Product damage")) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(true);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                    if (report1.sanitary_product.equals("Guidance given")) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(true);
                    }
                }
                if (report1.warranty != null) {
                    int pos = Integer.parseInt(report1.warranty);
                    spin_sanitary_service_warranty.setSelection(pos);
                }

            }


            radio_sanitary_service_wrong_product.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radio_sanitary_service_wrong_product.setChecked(true);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                }
            });
            radio_sanitary_service_wrong_installation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(true);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                }
            });
            radio_sanitary_service_product_damage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(true);
                        radio_sanitary_service_guidance_given.setChecked(false);
                    }
                }
            });

            radio_sanitary_service_guidance_given.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        radio_sanitary_service_wrong_product.setChecked(false);
                        radio_sanitary_service_wrong_installation.setChecked(false);
                        radio_sanitary_service_product_damage.setChecked(false);
                        radio_sanitary_service_guidance_given.setChecked(true);
                    }
                }
            });


        }

        //.....................................................................................


        TextView customer_info = (TextView) findViewById(R.id.customer_info);
        // customer_info.setVisibility(View.GONE);
        TextView comp_number = (TextView) findViewById(R.id.complaint_number);
        TextView head_1 = (TextView) findViewById(R.id.head_1);
        head_1.setText("Service Req Number");
        TextView complaint_deatils = (TextView) findViewById(R.id.complaint_deatils);
        TextView customer_name = (TextView) findViewById(R.id.customer_name);
        date = (TextView) findViewById(R.id.date);
        mobile_no = (TextView) findViewById(R.id.mobile_no);
        TextView dealer = (TextView) findViewById(R.id.dealer);
        TextView invoice_no = (TextView) findViewById(R.id.invoice_no);
        // TextView article_no = (TextView)findViewById(R.id.article_no);
        TextView prod_cat = (TextView) findViewById(R.id.prod_cat);
        TextView prod_sub_cat = (TextView) findViewById(R.id.prod_sub_cat);
        complaint_deatils = (TextView) findViewById(R.id.complaint_deatils);
        TextView address = (TextView) findViewById(R.id.address);
        TextView text_12 = (TextView) findViewById(R.id.text_12);
        article_number = (EditText) findViewById(R.id.article_no);
        other_reason = (EditText) findViewById(R.id.other_reason);
        text_12.setText("Service Details");
        ImageView home_button = (ImageView) findViewById(R.id.home);
        ImageView logout_button = (ImageView) findViewById(R.id.logout);

        Button submit = (Button) findViewById(R.id.submit);
        Button btn_deleteVideo = (Button) findViewById(R.id.btn_deleteVideo);

        spin_siteIssueReason_reason = (Spinner) findViewById(R.id.spin_siteIssueReason_reason);
        spin_action = (Spinner) findViewById(R.id.spin_action);
        edt_spare_defect_articleNo = (EditText) findViewById(R.id.edt_spare_defect_articleNo);
        edt_complete_set_articleNo = (EditText) findViewById(R.id.edt_complete_set_articleNo);
        ll_product_defect = (LinearLayout) findViewById(R.id.ll_product_defect);
        ll_action = (LinearLayout) findViewById(R.id.ll_action);
        ll_site_issue = (LinearLayout) findViewById(R.id.ll_site_issue);

        btn_deleteVideo.setOnClickListener(this);
        submit.setOnClickListener(this);
        status = (Spinner) findViewById(R.id.status);
        comments = (EditText) findViewById(R.id.comments);

        // final LinearLayout unresolved = (LinearLayout) findViewById(R.id.unresolved);

		/*unresolved.setVisibility(View.GONE);
        final LinearLayout other = (LinearLayout) findViewById(R.id.others);
		other.setVisibility(View.GONE);
		final EditText other_reason = (EditText) findViewById(R.id.other_reason);
*/
        Button attach_img = (Button) findViewById(R.id.attach_img);
        Button attach_vid = (Button) findViewById(R.id.attach_vid);
        attach_img.setOnClickListener(this);
        attach_vid.setOnClickListener(this);
        img_grid = (GridView) findViewById(R.id.img_grid);
        video_grid = (ImageView) findViewById(R.id.video_grid);

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
                    btn_deleteVideo.setOnClickListener(this);
                    status.setEnabled(true);
                    // .setVisibility(View.VISIBLE);
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
                Bitmap scaledBM = Bitmap.createScaledBitmap(compressedBitmap,
                        100, nh, true);
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

        video_grid.setOnClickListener(this);
        img_grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bitmap sBM = bmpArray.get(position);
                Bitmap cBm = capturedBM.get(position);
                int nh = (int) (cBm.getHeight() * (500.0 / cBm.getWidth()));
                Bitmap scaledBM = Bitmap.createScaledBitmap(cBm, 500, nh, true);
                showImage(scaledBM, cBm, sBM, position);
            }
        });

        comp_number.setText(complaint_number);
        customer_name.setText(c_name);
        date.setText(complaint_date);
        mobile_no.setText(end_user_mobile);
        mobile_no.setOnClickListener(this);
        dealer.setText(service_franchise);
        // article_no.setText(article);
        article_number.setText(article);
        complaint_deatils.setText(service_details);
        prod_cat.setText(product_category);
        prod_sub_cat.setText(product_sub_category);
        address.setText(c_address);

        home_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

        unresolve_reason = (Spinner) findViewById(R.id.unresolve_reason);
        unresolve_reason.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                Log.v("Unresolve_reason", unresolve_reason
                        .getSelectedItem().toString());

                if (unresolve_reason.getSelectedItem().toString().equals("Product Defect")) {
                    ll_product_defect.setVisibility(View.VISIBLE);
                    ll_site_issue.setVisibility(View.GONE);
                    spin_siteIssueReason_reason.setSelection(0);

                } else if (unresolve_reason.getSelectedItem().toString().equals("Site Issues")) {
                    ll_product_defect.setVisibility(View.GONE);
                    ll_site_issue.setVisibility(View.VISIBLE);
                    edt_spare_defect_articleNo.setText("");
                    edt_complete_set_articleNo.setText("");


                } else {
                    ll_product_defect.setVisibility(View.GONE);
                    ll_site_issue.setVisibility(View.GONE);
                    spin_siteIssueReason_reason.setSelection(0);
                    edt_spare_defect_articleNo.setText("");
                    edt_complete_set_articleNo.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        status.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                contentvalues.put("Closure_Status", status.getSelectedItem()
                        .toString());
                if (position == 1) {

                } else {
                    contentvalues.put("Reason_For_Unresolved", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        if (report != null) {
            article_number.setText(report.article_no);


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

                if (report.wrong_product != null) {
                    if (report.wrong_product.equals("Y")) {
                        chk_1.setChecked(true);
                        chk_2.setChecked(false);
                    } else if (report.wrong_product.equals("N")) {
                        chk_1.setChecked(false);
                        chk_2.setChecked(true);
                    } else {
                        chk_1.setChecked(false);
                        chk_2.setChecked(false);
                    }
                }
            }

			/*if (report.Closure_Status != null) {

				if (report.Closure_Status.equals("Resolved")) {
					// TODO
					status.setSelection(0);
				} else if (report.Closure_Status.equals("Unresolved")) {
					status.setSelection(1);
					System.out.println("Reason_For_Unresolved----"
							+ report.Reason_For_Unresolved);
					int index = reasonList
							.indexOf(report.Reason_For_Unresolved);
					if (index < 0) {
						int i = reasonList.indexOf("Others");
						//other.setVisibility(View.VISIBLE);
						unresolve_reason.setSelection(i);
						other_reason.setText(report.Reason_For_Unresolved);
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
					//
					// String r[] =
					// getResources().getStringArray(R.array.reason_array_2);
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
					// unresolve_reason.setSelection(10);other_reason.setText(report.Reason_For_Unresolved);
					// }
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

            if (report.Comment != null) {
                comments.setText(report.Comment);
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                Intent intent = new Intent(SiteVisitForm.this,
                        MainLauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // finish();
                break;

            case R.id.home:
                Intent intent1 = new Intent(SiteVisitForm.this,
                        DashBoardActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                // finish();
                break;

            case R.id.submit:
                boolean is_article_no = Validation.hasText(article_number);
                if (article_number.getText().toString().trim().equals(""))
                    is_article_no = false;

                if (is_article_no) {
                    // Boolean result = true;

                    if (validation() == true) {
                        submitData();
                    }
                    /*if (product_group.contains("Lighting Technology") || product_group.equalsIgnoreCase("Lighting Technology")) {
                        Log.e("Category", "Correct");
                        if (!chk_1.isChecked() && !chk_2.isChecked()) {
                            UtilityClass.showToast(SiteVisitForm.this, "Please Complete Step 1 ");
                            result = false;
                        } else {
                            result = true;
                        }

                        if (result == true) {
                            if (validation() == true) {
                                submitData();
                            }
                        }

                    } else {
                        if (validation() == true) {
                            submitData();
                        }
                    }*/
                    //submitData();
                } else {
                    if (!is_article_no)
                        UtilityClass.showToast(context, "Enter article number");
                }


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
                                    values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".jpeg");
                                    // if directory is not found on device . application fails
                                    // to save images on specified path hence crashes
                                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                                    startActivityForResult(intentPicture, 101);
                                } else {
                                    UtilityClass.showToast(SiteVisitForm.this,
                                            "Only 10 images are allowed");
                                }
                            }
                        } else if (options[item].equals("Choose Image from Gallery")) {
                        /*Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                         startActivityForResult(intent, 103);*/

                            if (fileNameArray != null) {
                                if (fileNameArray.size() < 10) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select File"), 103);
                                } else {
                                    UtilityClass.showToast(SiteVisitForm.this,
                                            "Only 10 images are allowed");
                                }
                            }

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


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
                                UtilityClass.showToast(SiteVisitForm.this,
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

                                vidintent.putExtra("android.intent.extra.durationLimit", 60);

                                vidintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                // start the Video Capture Intent
                                startActivityForResult(vidintent,
                                        CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                            }


                        } else if (options1[item].equals("Choose Video from Gallery")) {
                            if (inputpath != null) {
                                UtilityClass.showToast(SiteVisitForm.this,
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


                break;
            case R.id.video_grid:
                break;

            case R.id.btn_deleteVideo:
                if (inputpath != null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            SiteVisitForm.this);
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
                                    File f = new File(inputpath);
                                    if (f.exists()) {

                                        if (Video_Status.equalsIgnoreCase("video_camera")) {
                                            f.delete();
                                            //down vote accepted You can force the MediaScanner to add a specific file (or let it know it was deleted or modified)
                                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));

                                        } else {

                                        }


                                        if (dbAdapter.checkID(complaint_number,
                                                "video_details", "complaint_number")) {
                                            dbAdapter.deleteString("video_details",
                                                    "FilePath", inputpath);
                                            inputpath = null;
                                            outvidpath = null;
                                        }

                                        inputpath = null;
                                        outvidpath = null;


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

            case R.id.mobile_no:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + end_user_mobile + ""));
                startActivity(callIntent);
                break;
        }
    }

    private boolean validation() {

        Boolean res = true;
      /*  if (product_sub_category.contains("Sanitary_abc") || product_sub_category.equalsIgnoreCase("Sanitary_abc")) {
            if (radio_group_sanitary_service.getCheckedRadioButtonId() == -1) {
                res = false;
                UtilityClass.showToast(context,
                        "Please complete Step 1");

            }

        }*/
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

    private void submitData() {
        // TODO Auto-generated method stub
        long uploadReponse = 0;
        if (bmpArray.size() != 0) {
            for (int i = 0; i < bmpArray.size(); i++) {
                if (dbAdapter.checkID(fileNameArray.get(i).getName(),
                        "image_details", "image_name")) {

                } else {
                    Log.v("original_size",
                            String.valueOf(orgFileArray.get(i).length()));
                    Log.v("compressed_size",
                            String.valueOf(fileNameArray.get(i).length()));

                    uploadReponse = dbAdapter.saveImageDetails(fileNameArray
                                    .get(i).getName(), fileNameArray.get(i)
                                    .getAbsolutePath(), imgOriginalSize.get(i),
                            imgCompressedSize.get(i), complaint_number, "NU",
                            fileNameArray.size(), pref.getDeviceId());
                }
            }
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


                Log.v("Page", "SiteVisit");
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
                    Log.v("tag", e.getMessage());

                }

                Uri u = Uri.parse("file://" + getFilesDir() + "/logo.png");

                String logo = u.getPath();

                String[] complexCommand = {"ffmpeg", "-y", "-i", inputpath,
                        "-i", logo, "-strict", "experimental", "-s", "320x240",
                        "-b", "128k", "-filter_complex", "overlay",
                        outvidpath};

                setCommandComplex(complexCommand);
                setOutputFilePath(outvidpath);
                setProgressDialogTitle("AdStringO Compression");
                setProgressDialogMessage("Depending on your video size, it can take a few minutes..Please Wait..");

                setNotificationIcon(R.drawable.icon);
                setNotificationMessage("Process is running...");
                setNotificationTitle("AdStringO Compression");
                setNotificationfinishedMessageTitle("Process Completed");
                setNotificationfinishedMessageDesc("");
                setNotificationStoppedMessage("Process Failed");
                runTranscoing();
                OriginalSize = String.valueOf(inputpath.length());// calculated


            } else {
                if (is_video_attached == true) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            SiteVisitForm.this);
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

        Long response1 = (long) 0;
        if (complaint_number!=null){
            contentValues.put("Complant_No",complaint_number);
        }
        contentValues.put("sync_status", "NU");
        if (product_sub_category.contains("Sanitary_abc")) {
            if (radio_sanitary_service_wrong_product.isChecked()) {
                String str_sanitary_service_wrong_product = radio_sanitary_service_wrong_product.getText().toString();
                contentValues.put("sanitary_product", str_sanitary_service_wrong_product);
            } else if (radio_sanitary_service_wrong_installation.isChecked()) {
                String str_sanitary_service_wrong_installation = radio_sanitary_service_wrong_installation.getText().toString();
                contentValues.put("sanitary_product", str_sanitary_service_wrong_installation);
            } else if (radio_sanitary_service_product_damage.isChecked()) {
                String str_sanitary_service_product_damage = radio_sanitary_service_product_damage.getText().toString();
                contentValues.put("sanitary_product", str_sanitary_service_product_damage);
            } else if (radio_sanitary_service_guidance_given.isChecked()) {
                String str_sanitary_service_guidance_given = radio_sanitary_service_guidance_given.getText().toString();
                contentValues.put("sanitary_product", str_sanitary_service_guidance_given);
            }
            contentValues.put("warranty",spin_sanitary_service_warranty.getSelectedItemPosition());
            if (dbAdapter.checkID(complaint_number, "sanitary_details",
                    "Complant_No")) {
                response1 = (long) dbAdapter.update("sanitary_details",
                        contentValues, "Complant_No = '" + complaint_number
                                + "'", null);
            } else {
                response1 = dbAdapter.submitQuery1(contentValues);
            }
        }
        contentvalues.put("Complant_No", complaint_number);
        contentvalues.put("sync_status", "NU");
        contentvalues.put("Comment", comments.getText().toString());
        contentvalues.put("Insert_Date", dateTime);
        contentvalues.put("article_no", article_number.getText().toString());
        contentvalues.put("Product_Category", product_category);
        contentvalues.put("product_Sub_Category", product_sub_category);

        contentvalues.put("Result", unresolve_reason.getSelectedItem().toString());
        contentvalues.put("sparce_defect", edt_spare_defect_articleNo.getText().toString());
        contentvalues.put("complete_set", edt_complete_set_articleNo.getText().toString());
        // contentvalues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
        if (spin_siteIssueReason_reason.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
            contentvalues.put("site_Issue_Reason", "");
        } else {
            contentvalues.put("site_Issue_Reason", spin_siteIssueReason_reason.getSelectedItem().toString());
        }
        contentvalues.put("Action", spin_action.getSelectedItem().toString());
        contentvalues.put("Reason_For_Unresolved", unresolve_reason.getSelectedItem().toString());


        Calendar calendar = Calendar.getInstance();
        Date updatedDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str_updatedDate = sdf.format(updatedDate);

        Calendar calendar1 = Calendar.getInstance();
        Date closedDate = calendar1.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str_closedDate = sdf1.format(closedDate);

        if (!contentvalues.get("Closure_Status").equals("") && contentvalues.get("Closure_Status").equals("Resolved")) {
            contentvalues.put("Updated_Date", str_updatedDate);
            contentvalues.put("Closed_Date", str_closedDate);
        } else {
            contentvalues.put("Updated_Date", str_updatedDate);
        }

        // dbAdapter.insert("Fault_Finding_Details", contentValues);
        Long response;
        if (dbAdapter.checkID(complaint_number, "Fault_Finding_Details",
                "Complant_No")) {
            response = (long) dbAdapter.update("Fault_Finding_Details",
                    contentvalues, "Complant_No = '" + complaint_number + "'",
                    null);

            Log.v("Site---update", contentvalues.toString());

        } else {
            response = dbAdapter.submitQuery(contentvalues);
            Log.v("Site---insert", contentvalues.toString());
        }

        if (response > 0) {
            int visitCount = dbAdapter.getVisitCount(complaint_number);
            ContentValues cv = new ContentValues();
            cv.put("is_visited", visitCount + 1);
            cv.put("case_attended", "Y");
            int updateReponse = dbAdapter.update("complaint_service_details",
                    cv, "complaint_number = '" + complaint_number + "'", null);
            if (updateReponse > 0) {

                if (contentvalues.get("Closure_Status") != null) {
                    if (contentvalues.get("Closure_Status").equals("Resolved")) {


                        showFeedbackForm(complaint_number);
                        contentvalues.clear();
                    } else {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                SiteVisitForm.this);
                        builder1.setTitle("Status");
                        builder1.setMessage("Data Saved");
                        builder1.setCancelable(false);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
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
                                                cv.put("OriginalSize", String
                                                        .valueOf(in.length()));
                                                cv.put("CompressedSize", String
                                                        .valueOf(file.length()));
                                                cv.put("upload_status", "NU");
                                                cv.put("username",
                                                        pref.getUserName());
                                                dbAdapter
                                                        .update("video_details",
                                                                cv,
                                                                "complaint_number = '"
                                                                        + complaint_number
                                                                        + "'",
                                                                null);

                                                Log.v("ContentValues--SiteUpdate",
                                                        cv.toString());
                                            } else {

                                                dbAdapter.saveVideoDetails(file
                                                                .getName(), file
                                                                .getAbsolutePath(),
                                                        String.valueOf(in
                                                                .length()),
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
                                                SiteVisitForm.this,
                                                DashBoardActivity.class);
                                        startActivity(homeIntent);
                                        // finish();

                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        // Intent homeIntent = new
                        // Intent(SiteVisitForm.this,DashBoardActivity.class);
                        // startActivity(homeIntent);
                        // finish();
                    }
                } else {
                    // finish();

                    System.gc();

                    Intent homeIntent = new Intent(SiteVisitForm.this,
                            DashBoardActivity.class);
                    startActivity(homeIntent);

                }
            }
        } else {
            UtilityClass.showToast(context, "Error");
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

    private void showFeedbackForm(final String complaint_number) {

        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.feedback_form, null);
        final EditText carpenter = (EditText) view.findViewById(R.id.carpenter);
        final EditText date_of_feedback = (EditText) view
                .findViewById(R.id.date_of_feedback);
        date_of_feedback.setEnabled(false);
        final Spinner site_v_rating = (Spinner) view
                .findViewById(R.id.site_v_rating);
        final Spinner Exrating = (Spinner) view.findViewById(R.id.rating);
        final TextView cc_executive = (TextView) view
                .findViewById(R.id.cc_executive);
        final SignatureView sign = (SignatureView) view
                .findViewById(R.id.signatureview);
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
                if (selection1.equalsIgnoreCase("--- select from list ---")) {
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
                if (selection1.equalsIgnoreCase("--- select from list ---")) {
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
                        cv.put("ImagePath", signaturePath);
                        cv.put("Insert_Date", date_of_feedback.getText()
                                .toString());

                        long response = dbAdapter.insert("feedback_form", cv);
                        if (response > 0) {

                            if (outvidpath != null) {
                                File file = new File(outvidpath);

                                File in = new File(inputpath);

                                if (dbAdapter.checkID(complaint_number,
                                        "video_details", "complaint_number")) {

                                    ContentValues cv1 = new ContentValues();
                                    cv1.clear();
                                    cv1.put("VideoFileName", file.getName());
                                    cv1.put("FilePath", file.getAbsolutePath());
                                    cv1.put("complaint_number",
                                            complaint_number);
                                    cv1.put("OriginalSize",
                                            String.valueOf(in.length()));
                                    cv1.put("CompressedSize",
                                            String.valueOf(file.length()));
                                    cv1.put("upload_status", "NU");
                                    cv1.put("video_count", 1);
                                    cv1.put("Device_id", pref.getDeviceId());
                                    cv1.put("username", pref.getUserName());

                                    Log.v("contentvalues", cv1.toString());

                                    dbAdapter.update("video_details", cv1,
                                            "complaint_number = '"
                                                    + complaint_number + "'",
                                            null);
                                    Log.v("ContentValues--FaultUpdate",
                                            cv1.toString());

                                } else {
                                    dbAdapter.saveVideoDetails(file.getName(),
                                            file.getAbsolutePath(),
                                            String.valueOf(in.length()),
                                            String.valueOf(file.length()),
                                            complaint_number, "NU",
                                            pref.getUserName(), 1,
                                            pref.getDeviceId());
                                }

                            }

                            System.gc();

                            UtilityClass.showToast(context,
                                    "Thank you for Your feedback.");
                            Intent homeIntent = new Intent(SiteVisitForm.this,
                                    DashBoardActivity.class);
                            startActivity(homeIntent);
                            // finish();
                        } else {
                            UtilityClass.showToast(context, "Error");
                        }

                        sendSMSAsync = new SendSMSAsync();
                        //   sendSMSAsync.execute();

                        // }else{
                        // UtilityClass.showToast(context,
                        // "Your feedback is valuable");
                        // }
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
                                cv1.put("OriginalSize",
                                        String.valueOf(in.length()));
                                cv1.put("CompressedSize",
                                        String.valueOf(file.length()));
                                cv1.put("upload_status", "NU");
                                cv1.put("video_count", 1);
                                cv1.put("Device_id", pref.getDeviceId());
                                cv1.put("username", pref.getUserName());

                                Log.v("contentvalues", cv1.toString());

                                dbAdapter.update("video_details", cv1,
                                        "complaint_number = '"
                                                + complaint_number + "'", null);
                                Log.v("ContentValues--FaultUpdate",
                                        cv1.toString());

                            } else {
                                dbAdapter.saveVideoDetails(file.getName(),
                                        file.getAbsolutePath(),
                                        String.valueOf(in.length()),
                                        String.valueOf(file.length()),
                                        complaint_number, "NU",
                                        pref.getUserName(), 1,
                                        pref.getDeviceId());
                            }

                        }

                        webservice.sendSMS(mobile_no.getText().toString().trim(), message);


                        System.gc();

                        Intent homeIntent = new Intent(SiteVisitForm.this,
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
                        /*if (fileNameArray.get(position).exists()) {
                            fileNameArray.get(position).delete();
						}*/


                                fileNameArray.remove(position);

                                //&&&&&&&&&&&&&&&&
                                orgFileArray.remove(position);
                                int size = imgOriginalSize.size();


                                //   Log.e("originalSize", String.valueOf(imgOriginalSize));

                                bmpArray.remove(sBM);
                                capturedBM.remove(cBM);
                                imgOriginalSize.remove(position);
                                imgCompressedSize.remove(position);

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
                            //					if (fileNameArray.get(position).exists()) {
                            //						fileNameArray.get(position).delete();
                            //
                            //						//&&&&&&&&&&&&&&&&
                            //						orgFileArray.get(position).delete();
                            //
                            //					}

                            //	Toast.makeText(getApplicationContext(),fileNameArray.get(position).getPath().toString(),Toast.LENGTH_SHORT).show();

                            //&&&&&&&&&&&&&&&&
                            orgFileArray.remove(position);


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

                break;

            case R.id.radio_8:
                if (isChecked) {

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

                break;

            case R.id.radio_9:
                if (isChecked) {

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

                break;

            case R.id.radio_10:
                if (isChecked) {

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

            case R.id.chk_1:
                if (isChecked) {
                    chk_2.setChecked(false);
                    contentvalues.put("wrong_product", "Y");
                } else {
                    contentvalues.put("wrong_product", "null");
                }
                break;
            case R.id.chk_2:
                if (isChecked) {
                    chk_1.setChecked(false);
                    contentvalues.put("wrong_product", "N");
                } else {
                    contentvalues.put("wrong_product", "null");
                }
                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

			/*// To refresh gallery
			MediaScannerConnection.scanFile(this, new String[] { Environment.getExternalStorageDirectory().toString() }, null, new MediaScannerConnection.OnScanCompletedListener() {

				// *   (non-Javadoc)
				// * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)

				public void onScanCompleted(String path, Uri uri)
				{

					Log.i("ExternalStorage", "Scanned " + path + ":");
					Log.i("ExternalStorage", "-> uri=" + uri);
				}
			});*/


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
                    Log.v("originalSize", String.valueOf(renamedFile.length()));
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
                            saveBitmapToFolder(bitmap2, renamedFile.getName());
                            capturedBM.add(bitmap2);

                            // compressedBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                            // bao);
                            // byte[] bmpPicByteArray = bao.toByteArray();
                            // base64 = Base64.encodeToString(bmpPicByteArray,
                            // Base64.DEFAULT);

                            int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap.getWidth()));
                            Bitmap scaledBM = Bitmap.createScaledBitmap(compressedBitmap, 100, nh, true);
                            bmpArray.add(scaledBM);
							/*imgAdapter.notifyDataSetChanged();
							fileNameArray.add(renamedFile);*/

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
                    Log.v("originalSize", String.valueOf(renamedFile.length()));
                    if (renamedFile != null) {
                        try {
                            // CompressionTechnique comp = new
                            // CompressionTechnique(context);
                            // File proofFile =
                            // com.sudesi.adstringocompression.CompressionTechnique.getCompressImage(file);
                            // File proofFile = comp.gC(renamedFile);
                            int imgWidth = 1366;
                            int imgHeight = 786;
                            int compressQuality = 80;
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

                            compressedBitmap.recycle();

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
                            saveBitmapToFolder(bitmap2, renamedFile.getName());
                            capturedBM.add(bitmap2);

                            // compressedBitmap.compress(Bitmap.CompressFormat.JPEG,100,
                            // bao);
                            // byte[] bmpPicByteArray = bao.toByteArray();
                            // base64 = Base64.encodeToString(bmpPicByteArray,
                            // Base64.DEFAULT);

                            int nh = (int) (compressedBitmap.getHeight() * (100.0 / compressedBitmap
                                    .getWidth()));
                            Bitmap scaledBM = Bitmap.createScaledBitmap(
                                    compressedBitmap, 100, nh, true);
                            bmpArray.add(scaledBM);

                            // imgAdapter.notifyDataSetChanged();
                            fileNameArray.add(renamedFile);

                            imgAdapter = null;

                            imgAdapter = new ImageGridAdapter(context,
                                    fileNameArray);
                            // vidAdapter = new
                            // VideoGridAdapter(context,fileNameArray);
                            img_grid.setAdapter(imgAdapter);

                            // captured_img.setImageBitmap(scaledBM);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Image capture cancelled",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Image capture failed.", Toast.LENGTH_LONG).show();
                }
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

                Log.v("inputpath", inputpath);

                Bitmap bmThumbnail;
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(inputpath,
                        Thumbnails.MICRO_KIND);
                video_grid.setImageBitmap(bmThumbnail);

				/*
				 * if (inputpath != null) { if (!inputpath.equalsIgnoreCase("")
				 * && is_video_attached == true) {
				 *
				 * //java.util.Date date = new java.util.Date(); //String
				 * timeStamp = new
				 * SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
				 *
				 * File root = new
				 * File(Environment.getExternalStorageDirectory() +
				 * "/AdStringOVideos/Output/");
				 *
				 * root.mkdirs();
				 *
				 * outvidpath = Environment.getExternalStorageDirectory() +
				 * "/AdStringOVideos/Output/" + "OUT_VID" + timeStamp + ".mp4";
				 * File file = new File(outvidpath); Log.e("Page", "SiteVisit");
				 * AssetManager assetManager = getAssets(); InputStream in =
				 * null; OutputStream out = null; File f = new
				 * File(getFilesDir(), "logo.png"); try { in =
				 * assetManager.open("logo.png"); out =
				 * openFileOutput(f.getName(), Context.MODE_WORLD_READABLE);
				 *
				 * copyFile(in, out); in.close(); in = null; out.flush();
				 * out.close(); out = null; } catch (Exception e) { Log.e("tag",
				 * e.getMessage());
				 *
				 * }
				 *
				 * Uri u = Uri.parse("file://" + getFilesDir() + "/logo.png");
				 *
				 * String logo = u.getPath();
				 *
				 * String[] complexCommand = { "ffmpeg", "-y", "-i", inputpath,
				 * "-i", logo, "-strict", "experimental", "-s", "320x240", "-b",
				 * "128k", "-filter_complex", "overlay",
				 *
				 * outvidpath };
				 *
				 * setCommandComplex(complexCommand);
				 * setOutputFilePath(outvidpath);
				 * setProgressDialogTitle("AdStringO Compression");
				 * setProgressDialogMessage(
				 * "Depending on your video size, it can take a few minutes..Please Wait.."
				 * );
				 *
				 * setNotificationIcon(R.drawable.icon);
				 * setNotificationMessage("Process is running...");
				 * setNotificationTitle("AdStringO Compression");
				 * setNotificationfinishedMessageTitle("Process Completed");
				 * setNotificationfinishedMessageDesc("");
				 * setNotificationStoppedMessage("Process Failed");
				 * runTranscoing(); OriginalSize =
				 * String.valueOf(inputpath.length());//calculated OG video size
				 *
				 * } else { AlertDialog.Builder builder1 = new
				 * AlertDialog.Builder( SiteVisitForm.this);
				 * builder1.setTitle("Empty file."); builder1.setMessage(
				 * "Please record video before applying to compression technique."
				 * ); builder1.setCancelable(true);
				 * builder1.setNeutralButton(android.R.string.ok, new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { dialog.cancel(); }
				 * }); AlertDialog alert11 = builder1.create(); alert11.show();
				 * }
				 *
				 * }
				 */

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video capture cancelled",
                        Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Video capture failed.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAPTURE_VIDEO_FROM_GALLARY_ACTIVITY_REQUEST_CODE) {

            Video_Status = "video_gallery";

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
                            Log.v("inputpath", inputpath);

                            Bitmap bmThumbnail;
                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(inputpath,
                                    Thumbnails.MICRO_KIND);
                            video_grid.setImageBitmap(bmThumbnail);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
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
            Log.v("compressedSize", String.valueOf(file.length()));

            imgCompressedSize.add(String.valueOf(file.length()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        imgAdapter.notifyDataSetChanged();
        fileNameArray.add(file);
        imgCompressedSize.add(String.valueOf(file.length()));

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
