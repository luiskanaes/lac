package pe.beyond.lac.gestionmovil.utils;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Connector {
	private final String LBL_KEY_CONTENT_TYPE = "Content-Type";
	private final String LBL_VALUE_CONTENT_TYPE = "text/xml; charset=UTF-8";
	
	private final String LBL_KEY_LOGIN = "login";
	private final String LBL_KEY_PASSWORD = "password";
	private final String LBL_KEY_TOKEN = "token";
	
	private final String LBL_KEY_IMAGE = "photoContent";
	private final String LBL_KEY_SIZE = "photoSize";
	private final String LBL_KEY_PHOTONAME = "photoName";
	
	private final String LBL_KEY_USERID = "userId";
	private final String LBL_KEY_MODULEID = "moduleId";
	private final String LBL_KEY_MODULES = "modules";
	
	private final String LBL_KEY_CONSUMED_INFO= "consumedInfo";
	
	private static Connector objConnector;
	private Context context;
	
	public static Connector getInstance(Context context){
		if (objConnector == null) {
			objConnector = new Connector();
		}
		objConnector.context = context;
		return objConnector;
	}
	
	public String _callService(String strUrl, Map mapParams,int intConnTimeout, int intSocketTimeout) {
		String strResult = null;
		try {
			HttpParams objHttpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(objHttpParams,intConnTimeout);
			HttpConnectionParams.setSoTimeout(objHttpParams, intSocketTimeout);
			HttpClient objHttpClient = new DefaultHttpClient(objHttpParams);
			
			List lstParams = new ArrayList(mapParams.keySet());
			List<NameValuePair> lstNameValuePair = new ArrayList<NameValuePair>(mapParams.size());
	        
			for (int i = 0; i < mapParams.size(); i++){
				String strKey = lstParams.get(i).toString();
				String strValue = mapParams.get(lstParams.get(i)).toString();
				BasicNameValuePair objNameValuePair = new BasicNameValuePair(strKey,strValue);
				lstNameValuePair.add(objNameValuePair);
			}
			
			HttpPost objHttpPost = new HttpPost();
			
			objHttpPost.setEntity(new UrlEncodedFormEntity(lstNameValuePair));
			objHttpPost.setURI(new URI(strUrl));
			
			try{
				HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
				HttpEntity resEntity = objHttpResponse.getEntity();
				strResult = EntityUtils.toString(resEntity);
			} catch (ConnectTimeoutException ex){
				Log.i("GMD", "CATCH EN ConnectTimeoutException");
				ex.printStackTrace();
			} catch(SocketTimeoutException ex1){
				Log.i("GMD", "CATCH EN SocketTimeoutException");
				ex1.printStackTrace();
			}
			
			
		} catch (Exception e) {
			Log.i("GMD", "CATCH EN OTHER EXCEPTION CALL SERVICE");
			e.printStackTrace();
		}
		return strResult;	
	}
	
	public String _requestUserLogin(final String strLogin, final String strPassword) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_LOGIN;
		Log.d("UPLOAD THREAT", "_requestUserLogin:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		mapParams.put(LBL_KEY_LOGIN, strLogin);
	    mapParams.put(LBL_KEY_PASSWORD, strPassword);
		
	    long lngStartMillis = System.currentTimeMillis();
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,Constants.DEFAULT_SOCKET_TIMEOUT);
		long lngEndMillis = System.currentTimeMillis();
		int intTimeMillis = (int) (lngEndMillis-lngStartMillis);
		AppPreferences.getInstance(context).savePreference(AppPreferences.PREF_SOCKET_TIMEOUT, intTimeMillis*Constants.MULT);
		return strResult;
	}

	public String _requestConfiguration() {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_CONFIGURATION;
		Log.d("UPLOAD THREAT", "_requestConfiguration:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		addModulesInfo(mapParams);
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());
		return strResult;
	}
	
	public String _requestInformation() {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_INFORMATION;
		Log.d("UPLOAD THREAT", "_requestInformation:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		
		AppPreferences appPreferences = AppPreferences.getInstance(context);
		int intUserId = appPreferences._loadUserId();
		int intModuleId = appPreferences._loadCurrentModuleId();
		mapParams.put(LBL_KEY_USERID, String.valueOf(intUserId));
		mapParams.put(LBL_KEY_MODULEID, String.valueOf(intModuleId));
		
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());
		return strResult;
	}
	
	private void addModulesInfo(Map<String, String> mapParams) {
		int intUserId = AppPreferences.getInstance(context)._loadUserId();
		String[] selectionArgs = new String[] { String.valueOf(intUserId) };

		Cursor objCursorUserAssigned = context.getContentResolver().query(
				UserAssignedDAO.QUERY_BY_USER_MODULE_URI, null, null,
				selectionArgs, null);
		
		String strModules = "";
		while(!objCursorUserAssigned.isAfterLast()){
			strModules+=objCursorUserAssigned.getString(objCursorUserAssigned.getColumnIndex(UserAssignedDAO.COL_COD_MODULO));
			if (!objCursorUserAssigned.isLast()){
				strModules+=",";
			}
			objCursorUserAssigned.moveToNext();
		}
		
		mapParams.put(LBL_KEY_MODULES, strModules);
		
	}

	public String _sendConsumedInfo(final String strTransferStream) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_TRANSFER;
		Log.d("UPLOAD THREAT", "_sendConsumedInfo:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		
		mapParams.put(LBL_KEY_CONSUMED_INFO, strTransferStream);
		//strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());
		strResult = _callService(strURL,mapParams, 15000, 30000);

		return strResult;
	}
	
	public String _sendUpdatedInfo(final String strTransferStream) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_UPDATE;
		
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		
		mapParams.put(LBL_KEY_CONSUMED_INFO, strTransferStream);
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());

		return strResult;
	}
	
	public String _sendErrorInfo(final String strTransferStream) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_ERROR;
		Log.d("UPLOAD THREAT", "_sendErrorInfo:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		
		mapParams.put(LBL_KEY_CONSUMED_INFO, strTransferStream);
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());

		return strResult;
	}
	
	
	//TR-JV
	public String _sendTrackingInfo(final String strTransferStream) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_TRACKING;
		Log.d("UPLOAD THREAT", "_sendTrackingInfo:   " + strURL);
		Map<String,String> mapParams = new HashMap<String,String>();
		addBasicInfo(mapParams);
		
		mapParams.put(LBL_KEY_CONSUMED_INFO, strTransferStream);
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());

		return strResult;
	}
	
	
	public String _requestUserLogout(final String strToken) {
		String strResult = null;
		
		String strURL = AppPreferences.getInstance(context)._loadServiceUrl() + Constants.URL_LOGOUT;
		
		Map<String,String> mapParams = new HashMap<String,String>();
		mapParams.put(LBL_KEY_TOKEN, strToken);
         
		strResult = _callService(strURL,mapParams, Constants.TIME_CONNECTION_TIMEOUT,AppPreferences.getInstance(context)._loadSocketTimeout());

		return strResult;
	}
	
	public void addBasicInfo(Map<String,String> mapParams){
		AppPreferences appPreferences = AppPreferences.getInstance(context);
		
		String strToken = appPreferences._loadToken(); 
		String strLogin = appPreferences._loadLogin();
		String strPassword = appPreferences._loadPassword();
		
		mapParams.put(LBL_KEY_TOKEN, strToken);
		mapParams.put(LBL_KEY_LOGIN, strLogin);
		mapParams.put(LBL_KEY_PASSWORD, strPassword);
	}
	
	
}
