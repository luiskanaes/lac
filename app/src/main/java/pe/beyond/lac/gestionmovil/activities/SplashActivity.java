package pe.beyond.lac.gestionmovil.activities;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.asynctasks.InitialAsyncTask;
import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {

	private final String STR_APN_NAME = "name";
	private final String STR_APN_NUMERIC = "numeric";
	private final String STR_APN_MCC = "mcc";
	private final String STR_APN_MNC = "mnc";
	private final String STR_APN_APN = "apn";
	private final String STR_APN_USER = "user";
	// private final String STR_APN_SERVER = "server";
	private final String STR_APN_PASSWORD = "password";
	private final String STR_APN_AUTH_TYPE = "authtype";
	private final String STR_APN_INSERT_NAME = "limaco";

//	public static final Uri APN_TABLE_URI = Uri
//			.parse("content://telephony/carriers");
//	public static final Uri APN_PREFER_URI = Uri
//			.parse("content://telephony/carriers/preferapn");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		boolean blnApnCreation = this.insertarAPN(STR_APN_INSERT_NAME,
				"limaco.claro.pe", "claro", "claro", "");
		
		if (blnApnCreation) {
			setClaroAPN(this, STR_APN_INSERT_NAME);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		TelephonyManager objTelephonyManager = (TelephonyManager) activity
				.getSystemService(Activity.TELEPHONY_SERVICE);
		AppPreferences.getInstance(activity).savePreference(
				AppPreferences.PREF_CELL_ID, objTelephonyManager.getDeviceId());
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Task encargado de realizar la descarga de alg�n contenido al
				// ejecutar la aplicaci�n.
				InitialAsyncTask task = new InitialAsyncTask(
						SplashActivity.this);
				task.execute();
				SplashActivity.this.finish();
				callActivity(MainActivity.class.getName());
			}
		}, Constants.TIME_SPLASH_DISPLAY_LENGTH);
	}

	/* APN */

	public boolean insertarAPN(String strNombre, String strAPN,
			String strUsuario, String strPassword, String strServidor) {

		String where = "name = ?";
		String wargs[] = new String[] { strNombre };
//		Cursor cur = this.getContentResolver().query(APN_TABLE_URI, null,
//				where, wargs, null);

//		if (cur.getCount() != 0) {
//			return false;
//		}

		// this.getContentResolver().delete(APN_TABLE_URI, "name=?",new String[]
		// { strNombre });

		TelephonyManager oTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		String strMCC = oTm.getSimOperator().substring(1, 3);
//		String strMNC = oTm.getSimOperator().substring(4, 5);

		ContentValues values = new ContentValues(8);
		values.put(STR_APN_NAME, strNombre.trim());
		values.put(STR_APN_NUMERIC, String.valueOf(oTm.getSimOperator()));
		values.put(STR_APN_MCC, "716");
		values.put(STR_APN_MNC, "10");
		values.put(STR_APN_APN, strAPN.trim());
		values.put(STR_APN_USER, strUsuario.trim());
		// values.put(STR_APN_SERVER, strServidor);
		values.put(STR_APN_PASSWORD, strPassword);
		values.put(STR_APN_AUTH_TYPE, "1");

//		Uri newRow = this.getContentResolver().insert(APN_TABLE_URI, values);

		int id = -1;
//		if (newRow != null) {
//			//TODO AQUI VA MENSAJE CUANDO SE CREO EL APN, PARA ESTE CASO NO HAY QUE AVISAR NADA AL USUARIO.
			
			
			
//		} else {
//			//TODO AQUI VA MENSAJE CUANDO ___NO__ SE CREO EL APN, PARA ESTE CASO NO HAY QUE AVISAR NADA AL USUARIO.
//		}

//		Log.d("APN", oTm.getSimOperator() + "<< getSimOperator!");
//		Log.d("APN", oTm.getSimCountryIso() + "<<  getSimCountryIso!");
//		Log.d("APN", oTm.getSimOperatorName() + "<<  getSimOperatorName!");
//		Log.d("APN", oTm.getNetworkOperatorName()
//				+ "<< getNetworkOperatorName!");

		return true;
	}

	public static boolean setClaroAPN(Context context, String name) {
		boolean changed = false;
		String columns[] = new String[] { "_id", "name" };
		String where = "name = ?";
		String wargs[] = new String[] { name };
		String sortOrder = null;
//		Cursor cur = context.getContentResolver().query(APN_TABLE_URI, columns,
//				where, wargs, sortOrder);
//		if (cur != null) {
//			if (cur.moveToFirst()) {
//				ContentValues values = new ContentValues(1);
//				values.put("apn_id", cur.getLong(0));
//				if (context.getContentResolver().update(APN_PREFER_URI, values,
//						null, null) == 1)
//					changed = true;
//			}
//			cur.close();
//		}
		return changed;
	}

}
