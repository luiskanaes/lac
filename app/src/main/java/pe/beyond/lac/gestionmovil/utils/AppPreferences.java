package pe.beyond.lac.gestionmovil.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

public class AppPreferences {
	private static final String PREF_NAME = "gmd-lac";
	public static final String PREF_ISUSERLOGGED = "IsUserLogged";
	
	public static final String PREF_USERID = "UserId";
	public static final String PREF_USERNAME = "UserName";
	public static final String PREF_FLASTNAME = "FLastName";
	public static final String PREF_MLASTNAME = "MLastName";
	public static final String PREF_PHONENUMBER = "PhoneNumber";
	public static final String PREF_STATE = "State";
	public static final String PREF_LOGIN = "Login";
	public static final String PREF_PASSWORD = "Password";
	public static final String PREF_TOKEN= "Token";
	public static final String PREF_CURRENTUSERCODE = "CurrentUserCode";
	public static final String PREF_CURRENTMODULEID = "CurrentModuleId";
	public static final String PREF_CURRENTBASEID = "CurrentBaseId";
	public static final String PREF_ISSPLASHENABLED = "issplashenabled";
	public static final String PREF_QUITSTARTTIME = "lngQuitStartTime";
	public static final String PREF_LISTINGCURRENTFILTER = "listingcurrentfilter";
	public static final String BACKFROMBACKGROUND = "frombackground";
	public static final String PREF_DAILY_SYNC = "dailySync";
	public static final String PREF_SERVICE_URL = "ServiceUrl";
	public static final String PREF_CELL_ID = "CellId";
	
	public static final String PREF_SOCKET_TIMEOUT = "SocketTimeout";
	public static final String PREF_LAST_LOGGED_DAY = "LastDayLogged";
	
	public static final String PREF_FTP_USERNAME = "FtpUsername";
	public static final String PREF_FTP_PASSWORD = "FtpPassword";
	public static final String PREF_FTP_URL = "FtpUrl";
	public static final String PREF_FTP_PORT = "FtpPort";
	
	public static AppPreferences appPreferences;
	public Context context;
	public SharedPreferences sharedPreferences;
	
	public static AppPreferences getInstance(final Context context){
		if (appPreferences==null){
			appPreferences = new AppPreferences(context);
		}
		return appPreferences;
	}
	
	public AppPreferences(final Context context){
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
	}
	
	public boolean _IsLogged(){
		boolean booIsLogged = sharedPreferences.getBoolean(PREF_ISUSERLOGGED, false);
		return booIsLogged;
	}
	
	public int _loadUserId(){
		int intUserId = sharedPreferences.getInt(PREF_USERID, -1);
		return intUserId;
	}
	
	public int _loadSocketTimeout(){
		int intUserId = sharedPreferences.getInt(PREF_SOCKET_TIMEOUT, Constants.DEFAULT_SOCKET_TIMEOUT);
		return intUserId;
	}
	
	public long _loadLastLoggedDay(){
		long lngLastLoggedDay = sharedPreferences.getLong(PREF_LAST_LOGGED_DAY, -1);
		return lngLastLoggedDay;
	}
	public String _loadUserName(){
		String strUsername = sharedPreferences.getString(PREF_USERNAME, Constants.LBL_EMPTY);
		return strUsername;
	}
	
	public String _loadCellId(){
		String strUsername = sharedPreferences.getString(PREF_CELL_ID, Constants.LBL_EMPTY);
		return strUsername;
	}
	
	public String _loadFLastName(){
		String strFLastName = sharedPreferences.getString(PREF_FLASTNAME, Constants.LBL_EMPTY);
		return strFLastName;
	}
	
	public String _loadMLastName(){
		String strMLastName = sharedPreferences.getString(PREF_MLASTNAME, Constants.LBL_EMPTY);
		return strMLastName;
	}
	
	public String _loadPhoneNumber(){
		String strPhoneNumber = sharedPreferences.getString(PREF_PHONENUMBER, Constants.LBL_EMPTY);
		return strPhoneNumber;
	}
	
	public int _loadState(){
		int intState = sharedPreferences.getInt(PREF_STATE, -1);
		return intState;
	}
	
	public String _loadLogin(){
		String strLogin = sharedPreferences.getString(PREF_LOGIN, Constants.LBL_EMPTY);
		return strLogin;
	}
	
	public String _loadPassword(){
		String strPassword = sharedPreferences.getString(PREF_PASSWORD, Constants.LBL_EMPTY);
		return strPassword;
	}
	
	public String _loadToken(){
		String strToken = sharedPreferences.getString(PREF_TOKEN, Constants.LBL_EMPTY);
		return strToken;
	}
	
	public int _loadCurrentModuleId(){
		int intCurrentModuleId = sharedPreferences.getInt(PREF_CURRENTMODULEID, -1);
		return intCurrentModuleId;
	}
	
	public int _loadCurrentBaseId(){
		int intCurrentBaseId = sharedPreferences.getInt(PREF_CURRENTBASEID, -1);
		return intCurrentBaseId;
	}
	public String _loadCurrentUserCode(){
		String strCurrentModuleId = sharedPreferences.getString(PREF_CURRENTUSERCODE, Constants.LBL_EMPTY);
		return strCurrentModuleId;
	}
	
	public boolean _IsSplashEnabled() {
		boolean booIsEnabled = sharedPreferences.getBoolean(PREF_ISSPLASHENABLED, true);
		return booIsEnabled;
	}
	public long _loadQuitStartTime(){
		long lngQuitStartTime = sharedPreferences.getLong(PREF_QUITSTARTTIME, Calendar.getInstance().getTimeInMillis());
		return lngQuitStartTime;
	}
	
	public int _loadListingCurrentFilter(){
		int intListingCurrentFilter = sharedPreferences.getInt(PREF_LISTINGCURRENTFILTER, Constants.LIST_PENDIENTE);
		return intListingCurrentFilter;
	}
	
	public boolean _loadDailySync(){
		boolean booDailySync = sharedPreferences.getBoolean(PREF_DAILY_SYNC, false);
		return booDailySync;
	}
	
	public String _loadServiceUrl(){
		String strToken = sharedPreferences.getString(PREF_SERVICE_URL, Constants.URL_DETAULT_BASE);
		return strToken;
	}
	
	public String _loadFtpUsername(){
		String strUsername = sharedPreferences.getString(PREF_FTP_USERNAME, Constants.FTP_DEFAULT_USERNAME);
		return strUsername;
	}
	
	public String _loadFtpPassword(){
		String strPassword = sharedPreferences.getString(PREF_FTP_PASSWORD, Constants.FTP_DEFAULT_PASSWORD);
		return strPassword;
	}
	public String _loadFtpUrl(){
		String strToken = sharedPreferences.getString(PREF_FTP_URL, Constants.FTP_DEFAULT_URL);
		return strToken;
	}
	public int _loadFtpPort(){
		int intPort = sharedPreferences.getInt(PREF_FTP_PORT, Constants.FTP_DEFAULT_PORT);
		return intPort;
	}
	
	public Boolean _IsBackFromBackground() {
        return sharedPreferences.getBoolean(BACKFROMBACKGROUND, false);
    }
	
	public void savePreference(final String strPrefKey, final Object objPrefValue){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (objPrefValue instanceof String) {
			editor.putString(strPrefKey, (String) objPrefValue);
        } else if (objPrefValue instanceof Boolean) {
        	editor.putBoolean(strPrefKey, (Boolean) objPrefValue);
        } else if (objPrefValue instanceof Integer) {
        	editor.putInt(strPrefKey, (Integer) objPrefValue);
        } else if (objPrefValue instanceof Long) {
        	editor.putLong(strPrefKey, (Long) objPrefValue);
        } else if (objPrefValue instanceof Float) {
        	editor.putFloat(strPrefKey, (Float) objPrefValue);
        }
		
		//do something to recover the value of strPrefKey
		editor.commit();
	}

	
	private static final String TAG = AppPreferences.class.toString();

	private Location location = null;
	private int vicinityRadius;
	private int sampleInterval;
	private int sampleDistance;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getVicinityRadius() {
		return vicinityRadius;
	}

	public void setVicinityRadius(int vicinityRadius) {
		this.vicinityRadius = vicinityRadius;
	}

	public int getSampleInterval() {
		return sampleInterval;
	}

	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}

	public int getSampleDistance() {
		return sampleDistance;
	}

	public void setSampleDistance(int sampleDistance) {
		this.sampleDistance = sampleDistance;
	}

	public void load() {
		Log.i(TAG, "load()");
		// Only fetch preferences if they exist
		this.location = new Location(sharedPreferences.getString("name", ""));
		// TODO: These are actually doubles not floats
		this.location.setLatitude(sharedPreferences.getFloat("latitude", 0f));
		this.location.setLongitude(sharedPreferences.getFloat("longitude", 0f));
		this.location.setAccuracy(sharedPreferences.getFloat("accuracy", 0f));
		this.location.setTime(sharedPreferences.getLong("time", 0l));

		this.vicinityRadius = sharedPreferences.getInt("vicinityradius", 0);
		this.sampleInterval = sharedPreferences.getInt("sampleinterval", 0);
		this.sampleDistance = sharedPreferences.getInt("sampledistance", 0);
	}

	public void store() {
		Log.i(TAG, "store()");
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("name", location.getProvider());
		editor.putFloat("latitude", (float) location.getLatitude());
		editor.putFloat("longitude", (float) location.getLongitude());
		editor.putFloat("accuracy", location.getAccuracy());
		editor.putLong("time", location.getTime());
		editor.putInt("vicinityradius", vicinityRadius);
		editor.putInt("sampleinterval", sampleInterval);
		editor.putInt("sampledistance", sampleDistance);

		editor.commit();
	}
}
