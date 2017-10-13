package com.sudesi.hafele.adapter;

import java.io.File;
import java.util.ArrayList;
import com.sudesi.hafele.faultreport.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class VideoGridAdapter extends BaseAdapter{
	Context context;
	ViewHolder holder;
	LayoutInflater inflater;
	ArrayList<File> bmp_array ;
	
	public VideoGridAdapter(Context context, ArrayList<File> fileNameArray) {
		this.context = context;
		this.bmp_array = fileNameArray;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return bmp_array.size();
	}

	@Override
	public File getItem(int position) {
		
		return bmp_array.get(position);
	}
	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();			
			convertView = inflater.inflate(R.layout.image_grid_item, null);
			holder.captured_img = (ImageView)convertView.findViewById(R.id.captured_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
	
		File file = getItem(position);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		Bitmap bm = BitmapFactory.decodeFile(file.getPath(),options);
		holder.captured_img.setImageBitmap(bm);
		return convertView;
	}
	
	private class ViewHolder{
		ImageView  captured_img ; 
		//ImageView cancel_img;
	}
}
