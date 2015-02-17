package pe.beyond.lac.gestionmovil.activities;

import java.util.Calendar;
import java.util.List;

import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.daos.LogDAO;
import pe.beyond.lac.gestionmovil.services.ContentUploadService;
import pe.beyond.lac.gestionmovil.tracking.LocationListenerService;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.CustomDialog;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected BaseActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		String strMarca = ((AppGMD) this.getApplication())._deviceMan;
		String strModelo = ((AppGMD) this.getApplication())._deviceName;
		
		
		if(strMarca.contains("motorola") && strModelo.contains("MB632")){
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else if(strMarca.contains("samsung") && strModelo.contains("GT-B5510L")){
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	
	}

	protected void callActivity(final String classCalled) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.SELECTED_ID, -1);
		bundle.putBoolean(Constants.OBTAINED_BY_QR, false);
		callActivity(classCalled, bundle);
	}

	public void callActivity(final String classCalled, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClassName(this, classCalled);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void callActivity(final String classCalled, Bundle bundle,
			final int requestCode) {
		Intent intent = new Intent();
		intent.setClassName(this, classCalled);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Guardar en el SharedPreferences el tiempo en el que se sale de la
	 * aplicación
	 */
	protected void setQuitStartTime() {
		Calendar calTime = Calendar.getInstance();
		long lngTime = calTime.getTimeInMillis();
		AppPreferences appPreferences = AppPreferences.getInstance(this);
		appPreferences.savePreference(AppPreferences.PREF_QUITSTARTTIME,
				lngTime);
	}

	@Override
	protected void onPause() {
		super.onPause();
		final AppPreferences appPreferences = new AppPreferences(this);
		if (isApplicationBroughtToBackground()) {
			appPreferences.savePreference(AppPreferences.BACKFROMBACKGROUND,
					true);
			setQuitStartTime();
		} else {
			appPreferences.savePreference(AppPreferences.BACKFROMBACKGROUND,
					false);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * ( TODO RO032 Sessión de Usuario Esto se utiliza cuando se requiere
		 * manejar el deslogueo del usuario luego de un tiempo de inactividad.
		 * Se ha comentado ya que hay varios módulos que pueden durar tiempos
		 * prolongados y no deberia obligarlo a loguearse (y perder la
		 * información ya avanzada).
		 */
		// AppPreferences appPreferences = new AppPreferences(this);
		// if (appPreferences._IsBackFromBackground()) {
		// if (exceededTimeout()){
		// appPreferences.savePreference(AppPreferences.PREF_ISUSERLOGGED,
		// false);
		//
		// Intent intent = new Intent();
		// intent.setClassName(this, MainActivity.class.getName());
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		// }
		// }
	}

	@Override
	protected Dialog onCreateDialog(int dialog_id, Bundle bundle) {
		CustomDialog dialog = new CustomDialog(this, dialog_id, bundle);
		return dialog;
	}

	/**
	 * Validar si el tiempo que el usuario estuvo fuera de la aplicación excedió
	 * al límite
	 * 
	 * @return Se excedió o no el límite de tiempo fuera de la aplicación
	 */
	protected boolean exceededTimeout() {
		AppPreferences appPreferences = AppPreferences.getInstance(this);
		long lngTimeGone = appPreferences._loadQuitStartTime();
		long lngTimeBack = Calendar.getInstance().getTimeInMillis();
		long lngTimeout = lngTimeBack - lngTimeGone;
		boolean result = (lngTimeout >= Constants.TIME_OUT_APP) ? true : false;
		Log.d("GMD", "TIMEOUT MAX ::: " + Constants.TIME_OUT_APP / 1000
				+ " - REAL OUT :::" + lngTimeout / 1000 + " RESULT ::: "
				+ result);

		return result;
	}

	/**
	 * Determinar si la aplicación esta siendo enviada al background
	 * 
	 * @return se envió o no la aplicación al background
	 */
	public boolean isApplicationBroughtToBackground() {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> tasks = manager.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			final ComponentName topActivity = tasks.get(0).topActivity;
			String strTopPackage = topActivity.getPackageName();
			String strAppPackage = getPackageName();
			if (!strTopPackage.equals(strAppPackage)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Mostrar Toast
	 * 
	 * @param intResId
	 *            - Id del recurso de la cadena de texto a mostrar en el Toast
	 */
	public void _showToast(int intResId) {
		Toast.makeText(activity, getString(intResId), Toast.LENGTH_SHORT)
				.show();

	}

	/**
	 * Determinar si se tiene habilitada alguna forma de conectividad (Cell Id o
	 * Wifi)
	 * 
	 * @return se encontró habilitada alguna forma de conectividad
	 */
	public boolean _isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		boolean result = false;
//		// Detectar y manejar el uso del botón HOME
//		if (keyCode == KeyEvent.KEYCODE_HOME) {
//			setQuitStartTime();
//
//			Intent startMain = new Intent(Intent.ACTION_MAIN);
//			startMain.addCategory(Intent.CATEGORY_HOME);
//			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(startMain);
//			result = true;
//		} else {
//			result = super.onKeyDown(keyCode, event);
//		}
//		return result;
//	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

		
		
		boolean result = false;
          // Detectar y manejar el uso del botón HOME
          if (keyCode == KeyEvent.KEYCODE_HOME) {
                 setQuitStartTime();

                 Intent startMain = new Intent(Intent.ACTION_MAIN);
                 startMain.addCategory(Intent.CATEGORY_HOME);
                 startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(startMain);
                 result = true;
          }
          
          BaseInputConnection bic=new BaseInputConnection(this.getWindow().getDecorView(),false);
          if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_W || keyCode == KeyEvent.KEYCODE_I){  //UPPER
                 KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY); 
                 bic.sendKeyEvent(event2);
                 result = true;
          } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_S || keyCode == KeyEvent.KEYCODE_K){ //DOWN
                 KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY); 
                 bic.sendKeyEvent(event2);
                 result = true;
          } else if(keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_J){ //LEFT 
	        	  KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY); 
	              bic.sendKeyEvent(event2);
	              result = true;
          } else if(keyCode == KeyEvent.KEYCODE_D || keyCode == KeyEvent.KEYCODE_L){ // RIGHT
	        	  KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY); 
	              bic.sendKeyEvent(event2);
	              result = true;
          }
          else {
                 result = super.onKeyDown(keyCode, event);
          }
          return result;
    }


	/**
	 * Crear diálogo dprogreso indefinido
	 * 
	 * @param intMessageId
	 *            - Id del mensaje a mostrar en el diálogo
	 * @return objeto ProgressDialog a mostrar
	 */
	public ProgressDialog _createProgressDialog(final int intMessageId) {
		ProgressDialog result = null;

		result = new ProgressDialog(activity);
		result.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		result.setMessage(activity.getResources().getString(intMessageId));
		result.setCancelable(false);
		return result;
	}

	/**
	 * Revisar el servicio de envío, para ver si es que está activo. En caso de
	 * no estarlo, se levanta el servicio.
	 */
	protected void _checkUploadService() {
		ActivityManager manager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		boolean booServiceFound = false;
		boolean booTrackingFound = false;
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("pe.beyond.lac.gestionmovil.services.ContentUploadService"
					.equals(service.service.getClassName())) {
				Log.d("GMD", "Servicio de envío ya se encuentra en ejecución");
				booServiceFound = true;
			}
			if ("pe.beyond.lac.gestionmovil.tracking.LocationListenerService"
					.equals(service.service.getClassName())) {
				Log.d("GMD", "Tracking de envío ya se encuentra en ejecución");
				booTrackingFound = true;
			}
			if (booTrackingFound && booServiceFound) {
				break;
			}
		}
		if (!booServiceFound) {
			Intent startServiceIntent = new Intent(activity,
					ContentUploadService.class);
			activity.startService(startServiceIntent);
			Log.d("GMD",
					"Servicio de envío se ha iniciado al ejecutar la aplicación");
		}
		if (!booTrackingFound) {
			Intent startTrackingIntent = new Intent(activity,
					LocationListenerService.class);
			activity.startService(startTrackingIntent);
			Log.d("GMD",
					"Tracking de envío se ha iniciado al ejecutar la aplicación");
		}
	}

	public void _reportError(Exception e) {
		StackTraceElement objTraceElement = e.getStackTrace()[0];
		System.out.println(e.toString());

		String strUserId = AppPreferences.getInstance(this)
				._loadCurrentUserCode();
		String strCellId = AppPreferences.getInstance(this)._loadCellId();
		String strErrorMessage = e.toString();
		
		int intErrorLine = objTraceElement.getLineNumber();
		String strErrorProcedure = objTraceElement.getMethodName();
		long lngDate = System.currentTimeMillis();
		int intLogId = (strUserId + strCellId + strErrorProcedure + String.valueOf(intErrorLine) + strErrorMessage).hashCode();
		
		pe.beyond.gls.model.Log objLog = new pe.beyond.gls.model.Log(intLogId,
				strUserId, strCellId, strErrorMessage, intErrorLine,
				strErrorProcedure, lngDate);
		ContentValues values = LogDAO.createContentValues(objLog);
		this.getContentResolver().insert(LogDAO.INSERT_URI, values);
	}
}
