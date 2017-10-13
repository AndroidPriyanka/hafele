package com.sudesi.hafele.fileupload;

import java.io.File;
import java.io.IOException;
public class FtpUpload {
	
	static SimpleFTP ftp;
	
	 public static String getVideoPath(File imgFile) {
		// File ProofFile = com.sudesi.adstringocompression.CompressionTechnique.getCompressImage(imgFile);
		 ftp = new SimpleFTP();
		 try {
			ftp.connect("hinccms.in", 21,"tabsudesi","Sudesi_123");		
			// Set binary mode.
	         ftp.bin();
	         ftp.stor(imgFile);
	         // Quit from the FTP server.
	         ftp.disconnect();
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
		// Bitmap bitmap = BitmapFactory.decodeFile(ProofFile.getPath());
		 //return orgImagePath;		 
		return imgFile.getName();
	 }
}
