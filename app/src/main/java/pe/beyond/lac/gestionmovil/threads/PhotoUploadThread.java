package pe.beyond.lac.gestionmovil.threads;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import pe.beyond.gls.model.Photo;
import pe.beyond.lac.gestionmovil.daos.PhotoDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class PhotoUploadThread extends Thread{
	private Context context;
	public boolean booRequiresRetry;
	
	public PhotoUploadThread(Context context) {
		super();
		this.context = context;
		booRequiresRetry = true;
	}
	
	public int bucle;
	@Override
	public void run() {
		bucle = 1;
		
		while (booRequiresRetry){
			
			booRequiresRetry = false;
			List<Photo> lstPhoto = _obtainLstPhotoUnsent();
			if (lstPhoto!=null && lstPhoto.size()>0){
				
				Date date = new Date(System.currentTimeMillis());
				Log.d("GMD",  "PHOTOS EXEC A LAS : " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()  + " --- iteracion : " + bucle);
				bucle++;
				for(Photo objPhoto:lstPhoto){
					try{
						_sendPhotoToServer(objPhoto);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void _sendPhotoToServer(Photo objPhoto) {
		File filImage = new  File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + objPhoto.getStrAbrevName());
		String pPathName = objPhoto.getStrFullName();
		String pPathFile = filImage.getAbsolutePath();
        try {
        	InputStream pInputStream  = new FileInputStream(filImage); 			
 			boolean booPhotoUploaded = uploadImageFTP(pInputStream, pPathFile,objPhoto.getStrFullName().split("/"));		
 			if (booPhotoUploaded){
 				_updatePhotoState(objPhoto.getIntId(),true);
 			}
 		} catch (FileNotFoundException e1) {
 			e1.printStackTrace();
 		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean uploadImageFTP(InputStream pInputStream, String pPathFile,String[] strings)
			throws Exception {
		boolean booPhotoUploaded = false;
		FTPClient ftpClient = new FTPClient();	 
		try {
			AppPreferences appPreferences = AppPreferences.getInstance(context);
			
		    ftpClient.connect(InetAddress.getByName(appPreferences._loadFtpUrl()), appPreferences._loadFtpPort());
		    ftpClient.login(appPreferences._loadFtpUsername(), appPreferences._loadFtpPassword());
		    
		    for (int intIte =0;intIte<strings.length;intIte++){
		    	if (intIte<strings.length-1){
		    		boolean booChildExists = ftpClient.changeWorkingDirectory(strings[intIte]);
		    		if (!booChildExists){
		    			ftpClient.makeDirectory(strings[intIte]);
		    			ftpClient.changeWorkingDirectory(strings[intIte]);
		    		}
		    	} else{

				    if (ftpClient.getReplyString().contains("250")) {
				        ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				        
				        File file = new File(pPathFile);
				        ftpClient.deleteFile(file.getName());
				        
				        booPhotoUploaded =  _compressAndSend(ftpClient, file,strings,intIte);
				        ftpClient.logout();
				        ftpClient.disconnect();
				    }
		    	}
		    }		 
		} catch (SocketException e) {
		    Log.e("GMD","SOCKET EXCEPTION " + e.getStackTrace().toString()+" - " + e.toString());
		} catch (UnknownHostException e) {
		    Log.e("GMD"," UNKNOWN HOST EXCEPTION " + e.getStackTrace().toString()+" - " + e.toString());
		} catch (IOException e) {
		    Log.e("GMD","IO EXCEPTION " + e.getStackTrace().toString() +" - " + e.toString());
		}
		return booPhotoUploaded;
	}

	private void _updatePhotoState(int intId, boolean booNewState) {
		String strId = String.valueOf(intId);

		String[] selectionArgs = new String[] { strId};
		ContentValues values = new ContentValues();
		values.put(PhotoDAO.COL_FLG_ENVIADO, booNewState);
		context.getContentResolver().update(
				PhotoDAO.UPDATE_PHOTO_STATE_URI, values,null, selectionArgs);
	}

	private List<Photo> _obtainLstPhotoUnsent() {
		Cursor cursorPhotos = context.getContentResolver().query(PhotoDAO.QUERY_ALL_UNSENT_URI, null, null, null, null);
		List<Photo> lstPhoto = PhotoDAO.createObjects(cursorPhotos);
		cursorPhotos.close();
		return lstPhoto;
	}
	

	public Bitmap shrinkBitmap(String filename, int width, int height) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filename, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filename, bmpFactoryOptions);
		return bitmap;
	}
	
	public boolean _compressAndSend(final FTPClient ftpClient, final File file,final String[] strings, final int intIte) throws IOException{
		Bitmap bm = BitmapFactory.decodeFile(file.getPath());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();     
        bm.compress(CompressFormat.JPEG, 10, bytes);
        
        File fcompresseDir = new File(file.getParent()+ "/compressed/");
        fcompresseDir.mkdirs();
        String filelocation =fcompresseDir.getPath() + "/" + file.getName();
        File f = new File(filelocation);
		FileOutputStream fo = null;
		try {
			f.createNewFile();
			fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			Log.d("TEST", "FILE CREATED::: " + filelocation);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedInputStream buffIn = null;
        long startTime = System.currentTimeMillis();
        buffIn = new BufferedInputStream(new FileInputStream(filelocation));
        ftpClient.enterLocalPassiveMode();
        //Log.d("PHOTOSENT", "FOTO A ENVIAR : " + f.getName());
        boolean booPhotoUploaded = ftpClient.storeFile(strings[strings.length-1], buffIn);
        Log.d("PHOTOSENT", (System.currentTimeMillis()-startTime)/1000 + " segundos para la imagen "  + intIte + " - FOTO ENVIADA : " + f.getName());
        buffIn.close();
        
        return booPhotoUploaded;
	}
}

