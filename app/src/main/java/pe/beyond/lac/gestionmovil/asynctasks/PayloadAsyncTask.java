package pe.beyond.lac.gestionmovil.asynctasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.beyond.gls.model.Photo;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.MainActivity;
import pe.beyond.lac.gestionmovil.daos.PhotoDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterAnswerDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Connector;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.ParsedConfiguration;
import pe.beyond.lac.gestionmovil.utils.ParsedInformation;
import pe.beyond.lac.gestionmovil.utils.ParsedLogin;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class PayloadAsyncTask extends AsyncTask<Object, Integer, Integer> {
	public BaseActivity activity;
	public ProgressDialog dialog;

	public PayloadAsyncTask(final BaseActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (activity!=null){
			try{
			boolean booAllOk = activity._isNetworkAvailable();
			if (activity==null){
				Log.e("GMD", "ACTIVITY NULL AND CRASHED");
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				this.cancel(true);
			}
			
			if (booAllOk) {
				dialog = activity._createProgressDialog(R.string.lbl_loading_prepare);
				dialog.show();
			} else {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				activity.showDialog(Constants.DIA_MSG_ERR_NETWORK);
				
				this.cancel(true);
			}
			} catch(Exception e){
				Log.e("GMD", "ACTIVITY NULL AND CRASHED");
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				this.cancel(true);
			}
		} else{
			Log.e("GMD", "ACTIVITY IS NULL IN PAYLOAD ACTIVITY");
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			this.cancel(true);
		}
		
	}
	
	public int _loadConfiguration(){
		publishProgress(R.string.lbl_loading_config_dwn);
		
		Connector objConnector = Connector.getInstance(activity);
		String strConfigurationXML = objConnector._requestConfiguration();
		ParsedConfiguration objParsedConfiguration = null;
		if (strConfigurationXML!=null && !strConfigurationXML.equals(Constants.LBL_EMPTY)){
			publishProgress(R.string.lbl_loading_config_par);	
			Serializer serializer = new Persister();
			try {
				objParsedConfiguration = serializer.read(ParsedConfiguration.class,strConfigurationXML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return Constants.DIA_MSG_ERR_CONFIG_DOWNLOAD;	 
		}	
		if (objParsedConfiguration != null) {
			publishProgress(R.string.lbl_loading_config_sav);
			Util._resetTables(activity,Constants.DB_CONFIGURATION_DAO_NAMES);
			((MainActivity)activity).getController().saveData(false, objParsedConfiguration);
		} else {
			return Constants.DIA_MSG_ERR_CONFIG_PARSE;
		}
		return -1;
	}
	
	public int _loadInformation(){
		publishProgress(R.string.lbl_loading_payload_dwn);
		
		Connector objConnector = Connector.getInstance(activity);
		String strInformationXML = objConnector._requestInformation();
		ParsedInformation objParsedInformation = null;
		if (strInformationXML!=null && !strInformationXML.equals(Constants.LBL_EMPTY)){
			publishProgress(R.string.lbl_loading_payload_par);
			
			Serializer serializer = new Persister();
			try {
				objParsedInformation = serializer.read(ParsedInformation.class,strInformationXML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return Constants.DIA_MSG_ERR_INFO_DOWNLOAD;	 
		}
		
		if (objParsedInformation != null) {
			publishProgress(R.string.lbl_loading_payload_sav);
			((MainActivity)activity).getController()._registerData(objParsedInformation);
		} else {
			return Constants.DIA_MSG_ERR_INFO_PARSE;
		}
		return -1;
	}
	
	public boolean isSameDay(Date dteLastLoggedDay, Date dteCurrentDay){
		return dteLastLoggedDay.getYear()==dteCurrentDay.getYear() && dteLastLoggedDay.getMonth()==dteCurrentDay.getMonth() && dteLastLoggedDay.getDay() == dteCurrentDay.getDay();
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		long lngLastLoggedDay = AppPreferences.getInstance(activity)._loadLastLoggedDay();
		if (lngLastLoggedDay==-1){
			AppPreferences.getInstance(activity).savePreference(AppPreferences.PREF_LAST_LOGGED_DAY, System.currentTimeMillis());
		} else{
			Date dteLastLoggedDay = new Date(lngLastLoggedDay);
			Date dteCurrentDay = new Date(System.currentTimeMillis());
			
			if (!isSameDay(dteLastLoggedDay,dteCurrentDay)){
				AppPreferences.getInstance(activity).savePreference(AppPreferences.PREF_LAST_LOGGED_DAY, dteCurrentDay.getTime());
				_deleteAllCache();
			}
		}
		
		_loadUserInformation();
		int intDownloadStatus = _loadConfiguration();
		if (intDownloadStatus==-1){
			intDownloadStatus = _loadInformation();
		}
		return intDownloadStatus;
	}

	private List<Photo> _obtainLstPhotosToDelete() {
		Cursor cursorPhotos = this.activity.getContentResolver().query(PhotoDAO.QUERY_ALL_URI, null, null, null, null);
		List<Photo> lstPhotos = PhotoDAO.createObjects(cursorPhotos);
		return lstPhotos;
	}
	
	private void _deleteAllCache() {
		publishProgress(R.string.lbl_deleting_all_info);
		
		File str2 = this.activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		deleteDirectoryFiles(str2);
		File str3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "DCIM/");
		deleteDirectoryFiles(str3);
		this.activity.getContentResolver().delete(PhotoDAO.DELETE_ALL_URI, null, null);
		this.activity.getContentResolver().delete(RegisterAnswerDAO.DELETE_ALL_URI, null, null);
		this.activity.getContentResolver().delete(RegisterDAO.DELETE_ALL_URI, null, null);
		
		String[] arrTableNames = new String[]{PhotoDAO.TB_NAME,RegisterAnswerDAO.TB_NAME,RegisterDAO.TB_NAME,TrackDAO.TB_NAME};
		Util._resetTables(activity, arrTableNames);
	}
	
	public boolean deleteDirectoryFiles(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return false;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	        	 deleteDirectoryFiles(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return true;
	  }
	
	private void _loadUserInformation() {
		publishProgress(R.string.lbl_loading_userinfo_dwn);
		String strLogin = AppPreferences.getInstance(activity)._loadLogin();
		String strPassword = AppPreferences.getInstance(activity)._loadPassword();
		
		ParsedLogin objLoginResult = null;
		try {
			Connector objConnector = Connector.getInstance(activity);
			String strResponse = objConnector._requestUserLogin(strLogin, strPassword);
			
			if (strResponse != null){
				final Serializer serializer = new Persister();
				objLoginResult = serializer.read(ParsedLogin.class,strResponse);	
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
		
		if (objLoginResult!=null){
			Util._resetTables(activity, Constants.DB_LOGIN_DAO_NAMES);
			((MainActivity)activity).getController()._saveLoginUserInformation(objLoginResult);
		} 
	}

	@Override
	protected void onPostExecute(Integer intResult) {
		super.onPostExecute(intResult);
		
		if (dialog != null || dialog.isShowing()) {
			dialog.dismiss();
		}
		
		if (intResult >= 0) {
			activity.showDialog(intResult);
		} else{
			AppPreferences.getInstance(activity).savePreference(AppPreferences.PREF_DAILY_SYNC, true);
			
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int intMessageId = values[0];
		if (dialog != null) {
			String strMessage = activity.getString(intMessageId);
			dialog.setMessage(strMessage);
		} else{
			dialog = activity._createProgressDialog(intMessageId);
			dialog.show();
		}
	}
}
