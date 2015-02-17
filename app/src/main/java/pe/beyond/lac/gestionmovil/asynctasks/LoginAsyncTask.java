package pe.beyond.lac.gestionmovil.asynctasks;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.MainActivity;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Connector;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.ParsedLogin;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoginAsyncTask extends AsyncTask<Boolean, Boolean, ParsedLogin>{
	private BaseActivity activity;
	private ProgressDialog dialog;

	private String strLogin;
	private String strPassword;
	
	public LoginAsyncTask(final BaseActivity activity, final String strLogin, final String strPassword) {
		super();
		this.activity = activity;
		this.strLogin = strLogin;
		this.strPassword = strPassword;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();		
//		if (activity._isNetworkAvailable()) {
			dialog = activity._createProgressDialog(R.string.lbl_dialog_login_download);
			dialog.show();
//		} else {
//			activity.showDialog(Constants.DIA_MSG_ERR_NETWORK);
//			this.cancel(true);
//		}
	}
	
	
	@Override
	protected ParsedLogin doInBackground(Boolean... params) {
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
		return objLoginResult;
	}

	@Override
	protected void onPostExecute(ParsedLogin objLoginResult) {
		super.onPostExecute(objLoginResult);
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		AppPreferences appPreferences = AppPreferences
				.getInstance(activity);
		String strCurrentLogin = appPreferences._loadLogin();
		String strCurrentPassword = appPreferences._loadPassword();
		
		if (objLoginResult!=null){
			AppPreferences preferences = AppPreferences.getInstance(activity);
			preferences.savePreference(AppPreferences.PREF_DAILY_SYNC,
					false);
			if (!(strCurrentLogin.equalsIgnoreCase(objLoginResult.getObjUser().getStrLogin()) && 
					strCurrentPassword.equalsIgnoreCase(objLoginResult.getObjUser().getStrPassword()))){
				Util._resetTables(activity, Constants.DB_LOGIN_DAO_NAMES);	
				((MainActivity)activity).getController()._saveLoginUserInformation(objLoginResult);
				
			} else{	
				preferences.savePreference(AppPreferences.PREF_ISUSERLOGGED, true);
				preferences.savePreference(AppPreferences.PREF_DAILY_SYNC,
						true);
			}
			
			((MainActivity)activity)._loadMainScreen();
		} else{
			

			if (strLogin.equals(strCurrentLogin) && strPassword.equals(strCurrentPassword)) {
				appPreferences.savePreference(AppPreferences.PREF_ISUSERLOGGED, true);
				((MainActivity)activity)._loadMainScreen();
			} else {
				activity.showDialog(Constants.DIA_MSG_ERR_LOGIN);
			}
			
		}
	}
}
