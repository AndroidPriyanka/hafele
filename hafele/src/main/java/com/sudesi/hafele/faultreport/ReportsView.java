package com.sudesi.hafele.faultreport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.sudesi.hafele.database.HafeleFaultReportDBAdapter;

public class ReportsView extends Activity{
	
	Context context;
	HafeleFaultReportDBAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		context = this;		
		dbAdapter = new HafeleFaultReportDBAdapter(context);
		dbAdapter.createDatabase();
		dbAdapter.open();
		
		setContentView(R.layout.reports_view);
		FrameLayout chart_area = (FrameLayout) findViewById(R.id.chart_area);
	}
}
