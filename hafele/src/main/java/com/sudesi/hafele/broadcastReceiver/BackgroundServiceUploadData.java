package com.sudesi.hafele.broadcastReceiver;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.sudesi.hafele.classes.FaultReport;
import com.sudesi.hafele.classes.Feedback;
import com.sudesi.hafele.classes.ImageData;
import com.sudesi.hafele.classes.Sanitary_Details;
import com.sudesi.hafele.classes.VideoData;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.webservice.HafeleWebservice;

import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BackgroundServiceUploadData extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    HafeleFaultReportDBAdapter dbAdapter;
    HafelePreference pref;
    HafeleWebservice ws;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        // Toast.makeText(BackgroundServiceUploadData.this, "2 Minutes are OVER", Toast.LENGTH_LONG).show();
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            //*************To Uplaod data on Server
            //Log.e("bACKGROUND","UPLOADDATA");
            // 	Toast.makeText(BackgroundServiceUploadData.this, "2 Minutes are OVER", Toast.LENGTH_LONG).show();
            Log.v("bACKGROUND", "UPLOADDATA");
            // get database instance
            dbAdapter = new HafeleFaultReportDBAdapter(context);
            dbAdapter.createDatabase();
            dbAdapter.open();
            pref = new HafelePreference(context);
            ws = new HafeleWebservice(context);

            boolean isSaved = false;
            int responseID = 0;
            List<FaultReport> list = dbAdapter.getFaultReports(pref.getUserName());
            ContentValues cv = new ContentValues();
            if (list.size() == 0) {
                responseID = 2;
            } else {

                int listCount = 0;

                for (int i = 0; i < list.size(); i++) {
                    SoapPrimitive Soapresponse = ws.saveFaultReport(list.get(i)); // response is id
                    // retvalue
                    if (Soapresponse != null) {

                        if (isInteger(Soapresponse.toString())) {
                            listCount = listCount + 1;
                            cv.put("sync_status", "U");
                            int responseId = dbAdapter.update(
                                    "Fault_Finding_Details", cv,
                                    "Complant_No = '" + list.get(i).Complant_No
                                            + "'", null);

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


                            SoapPrimitive Soapresponse1 = ws.Insert_Complaint_Service_Details1(list.get(i), Soapresponse.toString());

                            SoapPrimitive soap_updateFault = ws.Update_FaulFinding(
                                    Soapresponse.toString(),
                                    list.get(i).Complant_No,
                                    list.get(i).Accepted_Date,
                                    list.get(i).Called_Date,
                                    list.get(i).Updated_Date,
                                    list.get(i).Closed_Date,
                                    delayed_days);
                            isSaved = true;

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
                            cv.put("sync_status", "U");
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

                                    } else if ((list1.get(i).Closure_Status.equals("Unresolved")) && (list1.get(i).Action.equalsIgnoreCase("MTR required")))
                                    {
                                        dbAdapter.delete(
                                                "complaint_service_details",
                                                "complaint_number",
                                                "'" + list1.get(i).Complant_No
                                                        + "'");
                                        dbAdapter.delete("sanitary_details", "Complant_No", "'" + list1.get(i).Complant_No + "'");
                                    }
                                }
                            }

                            int delayed_days = 0;
                            int diffInDays = 0;

                            String registration_date = list1.get(i).date;
                            String closed_date = list1.get(i).Closed_Date;
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


            //*************

            stopSelf();
        }


        public boolean isInteger(String s) {
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


    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}