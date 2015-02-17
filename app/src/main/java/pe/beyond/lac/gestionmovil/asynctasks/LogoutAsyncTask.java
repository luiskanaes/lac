package pe.beyond.lac.gestionmovil.asynctasks;

import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.utils.ParsedLogout;
import android.os.AsyncTask;

public class LogoutAsyncTask extends AsyncTask<Boolean, Boolean, ParsedLogout>{
	private BaseActivity activity;
	
	public LogoutAsyncTask(final BaseActivity activity) {
		super();
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected ParsedLogout doInBackground(Boolean... params) {
		ParsedLogout objLogoutResult = null;
		
//		String strToken = AppPreferences.getInstance(activity)._loadToken();
//		try {
//			Connector objConnector = Connector.getInstance(activity);
//			String strResponse = objConnector._requestUserLogout(strToken);
//			
//			if (strResponse != null){
//				final Serializer serializer = new Persister();
//				objLogoutResult = serializer.read(ParsedLogout.class,strResponse);	
//			}
//		} catch(Exception e){
//			e.printStackTrace();
//		} 
		return objLogoutResult;
	}

	@Override
	protected void onPostExecute(ParsedLogout objLogoutResult) {
		super.onPostExecute(objLogoutResult);
	}
}
