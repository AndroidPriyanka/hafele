package com.sudesi.hafele.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HafeleFaultReportDBHelper extends SQLiteOpenHelper {

    Context _context;
    //	private static String DB_NAME = "HafeleFaultReports.sqlite";
    public static String DB_NAME = "HafeleFaultReports.sqlite";
    //	private static String DB_NAME = "HafeleFaultReports111";
    private SQLiteDatabase mDb;
    private static String DB_PATH = "";
    private static final int DB_VERSION = 3;
    private static String TAG = "DataBaseHelper";
    /*private static final String ALTER_FAULT_FINDING_FOR_DATE1="ALTER TABLE Fault_Finding_Details ADD COLUMN Accepted_Date DATETIME";
    private static final String ALTER_FAULT_FINDING_FOR_DATE2="ALTER TABLE Fault_Finding_Details ADD COLUMN Called_Date DATETIME";
    private static final String ALTER_FAULT_FINDING_FOR_DATE3="ALTER TABLE Fault_Finding_Details ADD COLUMN Updated_Date DATETIME";
    private static final String ALTER_FAULT_FINDING_FOR_DATE4="ALTER TABLE Fault_Finding_Details ADD COLUMN Closed_Date DATETIME";
    */
    public static final String sanitary_details = "create table sanitary_details(radio_sanitary TEXT,type_of_sanitary TEXT,sanitary_product TEXT,sanitary_leakage TEXT,sanitary_type_of_leakage TEXT,does_not_operate TEXT,type_does_not_operate TEXT,weak_flow TEXT,type_of_weak_flow TEXT,asthetics TEXT,type_of_asthetics TEXT,warranty TEXT,noise TEXT,flush_not_working TEXT,type_of_flush_not_working TEXT,drainage TEXT,type_of_drainage TEXT,LMD TEXT,Complant_No TEXT,sync_status TEXT,Closure_Status TEXT,Fault_Finding_Id TEXT)";

    public HafeleFaultReportDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this._context = context;
    }


    public void createDatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (dbexist) {
            this.getReadableDatabase();
            this.close();
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
                Log.e(TAG, "createDatabase database created");

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkdatabase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = _context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public boolean openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        mDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDb != null;
    }


    @Override
    public synchronized void close() {
        if (mDb != null)
            mDb.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(ALTER_FAULT_FINDING_FOR_DATE1);
        //db.execSQL(ALTER_FAULT_FINDING_FOR_DATE2);
        //db.execSQL(ALTER_FAULT_FINDING_FOR_DATE3);
        //db.execSQL(ALTER_FAULT_FINDING_FOR_DATE4);
        //db.execSQL(ALTER_FEEDBACK_ADD_IMAGEPATH);
      //  db.execSQL(sanitary_details);
        db.execSQL(sanitary_details);


    }

}
