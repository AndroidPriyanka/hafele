package com.sudesi.hafele.faultreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sudesi.hafele.broadcastReceiver.UploadDataBrodcastReceiver;
import com.sudesi.hafele.classes.UserClass;
import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;
import com.sudesi.hafele.database.HafeleFaultReportDBHelper;
import com.sudesi.hafele.preferences.HafelePreference;
import com.sudesi.hafele.utils.UtilityClass;
import com.sudesi.hafele.utils.Validation;
import com.sudesi.hafele.webservice.HafeleWebservice;

public class MainLauncherActivity extends Activity implements OnClickListener {

    EditText username;
    EditText password;
    Context context;
    HafeleFaultReportDBAdapter dbAdapter;
    ProgressDialog progress;
    HafeleWebservice ws;
    HafelePreference pref;
    String android_id;

    //public String downloadURL = "http://tabuat.hinccms.in/New_Realese/hafele.apk"; //uat
    //public String downloadConfigFile = "http://tabuat.hinccms.in/New_Realese/Config.txt"; //uat

    public String downloadURL = "http://tab.hinccms.in/New_Realese/hafele.apk"; //prod
    public String downloadConfigFile = "http://tab.hinccms.in/New_Realese/Config.txt"; //prod

  ///  public String downloadURL = "http://tabsrilanka.hinccms.in/New_Realese/hafele.apk"; //shrilanka prod
    //public String downloadConfigFile = "http://tabsrilanka.hinccms.in/New_Realese/Config.txt"; //srilanka prod


//tabsrilanka.hinccms.in
    //	public String downloadURL = "http://hafele.sudesi.in/apk/hafele.apk"; //UAT
    //	public String downloadConfigFile = "http://hafele.sudesi.in/apk/Config.txt"; // UAT

    SyncApkCheck syncapkcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_launcher);
        context = this;
        ws = new HafeleWebservice(context);
        progress = new ProgressDialog(context);
        progress.setMessage("Logging in ...");
        pref = new HafelePreference(context);
        // get database instance
        dbAdapter = new HafeleFaultReportDBAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();

        username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_password);
        android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);

  	exportDB();
    }

	private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		// String currentDBPath = "/data/" + "clubmahindra.com.myapplication" + "/databases/" + DbHelper.DATABASE_NAME;
		String currentDBPath = "//data//" + getPackageName() + "//databases//" + HafeleFaultReportDBHelper.DB_NAME;
		String backupDBPath = HafeleFaultReportDBHelper.DB_NAME;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
//            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    private void DataUploadAlaramReceiver() {
        // TODO Auto-generated method stub

        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, 13);
        cal1.set(Calendar.MINUTE, 00);
        cal1.set(Calendar.SECOND, 00);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 18);
        cal2.set(Calendar.MINUTE, 00);
        cal2.set(Calendar.SECOND, 00);

        // Test if the times are in the past, if they are add one day
        Calendar now = Calendar.getInstance();
        if (now.after(cal1))
            cal1.add(Calendar.HOUR_OF_DAY, 24);
        if (now.after(cal2))
            cal2.add(Calendar.HOUR_OF_DAY, 24);

        // Create two different PendingIntents, they MUST have different requestCodes
        Intent intent = new Intent(this, UploadDataBrodcastReceiver.class);
        PendingIntent morningAlarm = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        PendingIntent eveningAlarm = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);

        // Start both alarms, set to repeat once every day
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), morningAlarm);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), eveningAlarm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:


                //To Set Alram
                DataUploadAlaramReceiver();


                if (UtilityClass.isConnectingToInternet(context)) {
                    syncapkcheck = new SyncApkCheck();
                    syncapkcheck.execute();
                } else {
                    LoginUser();
                }


                //Intent intent = new Intent(MainLauncherActivity.this,DashBoardActivity.class);
                //startActivity(intent);
                break;
        }
    }

    private void LoginUser() {
        if (checkValidation()) {
            Log.e("Enter", "checkValidation");
            boolean is_network = false;
            if (UtilityClass.isConnectingToInternet(context)) {
                is_network = true;
                Log.e("Enter", "Internet--Yes");
                LoginTask asyncLogin = new LoginTask(is_network);
                asyncLogin.execute();
            } else {
                if (pref.isFirstlogin()) {
                    Log.e("Enter", "First Login");
                    UtilityClass.showToast(context, "Please connect to network");
                } else {

                    Log.e("Enter", "Internet--No");
                    is_network = false;
                    LoginTask asyncLogin = new LoginTask(is_network);
                    asyncLogin.execute();
                }
            }
        } else {
            Toast.makeText(context, "Fill required information", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidation() {
        boolean is_valid = true;

        if (!Validation.hasText(username)) {
            Toast.makeText(context, "Username cannot be null", Toast.LENGTH_SHORT).show();
            is_valid = false;
        } else {
            is_valid = true;
        }
        if (!Validation.hasText(password)) {
            Toast.makeText(context, "password cannot be null", Toast.LENGTH_SHORT).show();
            is_valid = false;
        } else {
            is_valid = true;
        }
        return is_valid;
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        boolean network_available;

        public LoginTask(boolean is_network) {
            network_available = is_network;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean is_user = false;
            if (UtilityClass.isConnectingToInternet(context)) {
                SoapObject response = ws.Get_login_service(username.getText().toString(), password.getText().toString(), android_id);
                if (response != null) {
                    if (!checkBoolean(response.toString())) {
                        try {
                            SoapObject soapObj = (SoapObject) response.getProperty(0);
                            if (soapObj.getProperty("Flag").toString().equals("FALSE")) {
                                is_user = false;
                            } else if (soapObj.getProperty("Flag").toString().equals("TRUE")) {
                                is_user = true;
                                pref.saveisFirstlogin(false);
                                pref.saveUsername(soapObj.getProperty("Name").toString());
                                pref.saveUserID(Integer.parseInt(soapObj.getProperty("Id").toString()));
                                pref.saveDeviceId(android_id);

                                Log.e("", "saveuserid===================================" + Integer.parseInt(soapObj.getProperty("Id").toString()));
                                String Name = soapObj.getProperty("Name").toString();
                                String Designation = soapObj.getProperty("Designation").toString();
                                String Reporting_To = soapObj.getProperty("Reporting_To").toString();
                                String SecondLine_Reporting = soapObj.getProperty("SecondLine_Reporting").toString();
                                String Mobile_No1 = soapObj.getProperty("Mobile_No1").toString();
                                String Mobile_No2 = soapObj.getProperty("Mobile_No2").toString();
                                String Mobile_No3 = soapObj.getProperty("Mobile_No3").toString();
                                String Android_Uid = soapObj.getProperty("Android_Uid").toString();
                                String ActivationKey = soapObj.getProperty("ActivationKey").toString();
                                String Deactivationdate = soapObj.getProperty("Deactivationdate").toString();
                                String ActivationDate = soapObj.getProperty("ActivationDate").toString();
                                String status = soapObj.getProperty("Status").toString();
                                String location = soapObj.getProperty("Location").toString();
                                String zone = soapObj.getProperty("Zone").toString();
                                String user_id = soapObj.getProperty("Id").toString();

                                ContentValues cv = new ContentValues();
                                cv.put("username", username.getText().toString());
                                cv.put("Name", Name);
                                cv.put("password", password.getText().toString());
                                cv.put("Designation", Designation);
                                cv.put("Reporting_To", Reporting_To);
                                cv.put("SecondLine_Reporting", SecondLine_Reporting);
                                cv.put("Mobile_No1", Mobile_No1);
                                cv.put("Mobile_No2", Mobile_No2);
                                cv.put("Mobile_No3", Mobile_No3);
                                cv.put("Android_Uid", Android_Uid);
                                cv.put("ActivationKey", ActivationKey);
                                cv.put("Deactivationdate", Deactivationdate);
                                cv.put("Status", status);
                                cv.put("ActivationDate", ActivationDate);
                                cv.put("Location", location);
                                cv.put("Zone", zone);
                                cv.put("user_id", user_id);
                                dbAdapter.insert("login_master", cv);
                            } else if (soapObj.getProperty("Flag").equals("REGISTERED")) {
                                is_user = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {

                Log.e("Background", "no Internet--login");
                UserClass login_response = dbAdapter.login(username.getText().toString(), password.getText().toString(), android_id);
                if (login_response.Name != null) {
                    is_user = true;
                    pref.saveUsername(login_response.Name);
                    pref.saveUserID(login_response.user_id);
                } else {

                    Log.e("Background", "no Internet--login");
                    is_user = false;
                }
            }
            return is_user;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.dismiss();

            if (result) {
                //if(result.Name != null){
                username.setText("");
                password.setText("");


                Intent intent = new Intent(MainLauncherActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                //					}else {
                //						password.setText("");
                //						Toast.makeText(context, "Login failed ", Toast.LENGTH_SHORT).show();
                //					}

            } else {
                password.setText("");
                Toast.makeText(context, "Login failed ", Toast.LENGTH_SHORT).show();
            }

            // get data for complaints and services

        }


    }

    public boolean checkBoolean(String str_anyType) {
        Boolean bool = false;
        if (str_anyType != null) {
            if (str_anyType.equalsIgnoreCase("anyType{}")) {
                bool = true;
            } else {
                bool = false;
            }
        }
        return bool;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    public void CheckServerApkVersionDownloadFile(String configFileUrl) {
        try {
            URL url = new URL(configFileUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(false);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory()
                    + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "config.txt");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//

        } catch (IOException e) {
            // Toast.makeText(LoginActivity.this,"Server APK Version not getting",
            // Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    public class SyncApkCheck extends AsyncTask<Void, Void, String> {

        String retResult;

        ProgressDialog mprogress;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mprogress = new ProgressDialog(context);
            mprogress.setTitle("Checking APK");
            mprogress.setMessage("Checking.....Please Wait....!!");
            mprogress.setCancelable(false);
            mprogress.show();


        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mprogress.dismiss();
            if (result != null) {
                if (result.equalsIgnoreCase("DOWNLD")) {
                    Toast.makeText(MainLauncherActivity.this, "New Apk Version Installed !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainLauncherActivity.this, "No New Version !", Toast.LENGTH_LONG).show();
                    LoginUser();
                }
            } else {
                Toast.makeText(MainLauncherActivity.this, "No New Version !", Toast.LENGTH_LONG).show();
                LoginUser();
            }

        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            CheckServerApkVersionDownloadFile(downloadConfigFile);
            if (!ReadVersionFromSDCardFile().equalsIgnoreCase("")) {
                if (!getVersion().trim().equalsIgnoreCase(ReadVersionFromSDCardFile().trim())) {

                    Update(downloadURL);

                    retResult = "DOWNLD";

                } else {
                    retResult = "NODOWNLD";
                }
            } else {
                retResult = "NODOWNLD";
            }


            return retResult;
        }

    }

    public String ReadVersionFromSDCardFile() {

        String serverApkVersion = "";


        try {

            String PATH = Environment.getExternalStorageDirectory()
                    + "/download/Config.txt";
            File myFile = new File(PATH);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }

            return serverApkVersion = aBuffer;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return serverApkVersion;

    }

    public String getVersion() {
        String version;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return version = null;
        }
        return version;
    }

    public void Update(String apkurl) {
        try {
            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(false);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory()
                    + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "app1.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory()
                            + "/download/"
                            + "app1.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);

            // mProgress.dismiss();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Update error!",
                    Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

}
