package pe.beyond.lac.gestionmovil.activities;

import java.util.List;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.asynctasks.LoginAsyncTask;
import pe.beyond.lac.gestionmovil.asynctasks.PayloadAsyncTask;
import pe.beyond.lac.gestionmovil.controllers.MainController;
import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.CustomDialog;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard.Key;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnKeyListener {
	private MainController controller;

	// private boolean booIsRestarted = false;

	/**
	 * Cargar la pantalla con el menú principal de la aplicación.
	 */
	public void _loadMainScreen() {
		setContentView(R.layout.main_screen);

		ListView lstOptions = (ListView) findViewById(R.id.main_list);
		TextView txtUserName = (TextView) findViewById(R.id.main_text_header);

		MainListAdapter objMainListAdapter = new MainListAdapter(activity);
		lstOptions.setAdapter(objMainListAdapter);
		lstOptions.setOnItemClickListener(MainListItemClickListener);

		lstOptions.requestFocus();

		AppPreferences objPreferences = AppPreferences.getInstance(activity);
		objPreferences.savePreference(AppPreferences.PREF_ISSPLASHENABLED,
				false);

		String strFullName = objPreferences._loadUserName()
				+ Constants.LBL_SPACE + objPreferences._loadFLastName();
		txtUserName.setText(strFullName);

	}

	/**
	 * Cargar la pantalla de login del usuario.
	 */
	public void _loadLoginScreen() {
		setContentView(R.layout.login_screen);

		final EditText ediLogin = (EditText) findViewById(R.id.edit_username);
		final EditText ediPassword = (EditText) findViewById(R.id.edit_password);
		ediPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

		String strLogin = AppPreferences.getInstance(activity)._loadLogin();
		ediLogin.setText(strLogin);
		ediPassword.setText(Constants.LBL_EMPTY);

		// ediLogin.setText("42266603");
		// ediPassword.setText("42266603");

		Button btnLogin = (Button) findViewById(R.id.button_login);
		TextView txtInfoVersion = (TextView) this
				.findViewById(R.id.txtAppVersion);

		String strVersionApp = Util._obtainVersionName(activity);

		if (strVersionApp != null) {
			txtInfoVersion.setText(strVersionApp);
		}else {
			txtInfoVersion.setText("x.x"); 
		}

		btnLogin.requestFocus();
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strLogin = ediLogin.getText().toString();
				String strPassword = ediPassword.getText().toString();

				if (strLogin.length() < Constants.INT_MIN_CHAR_LOGIN
						|| strPassword.length() < Constants.INT_MIN_CHAR_LOGIN) {
					showDialog(Constants.DIA_MSG_ERR_LOGIN_MORE_CHARS);
				} else {
					LoginAsyncTask objTask = new LoginAsyncTask(activity,
							strLogin, strPassword);
					objTask.execute();
				}
			}
		});
	}

	// public String _obtainVersionName() {
	// String versionName = null;
	// StringBuilder sbrVersion = new StringBuilder();
	// try {
	//
	// sbrVersion = new StringBuilder();
	// sbrVersion.append("ver");
	// sbrVersion.append(Constants.LBL_DOT);
	// sbrVersion.append(Constants.LBL_SPACE);
	// versionName = getPackageManager().getPackageInfo(getPackageName(),
	// 0).versionName;
	// sbrVersion.append(versionName);
	//
	// } catch (NameNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// }
	//
	// return sbrVersion.toString();
	// }

	public void _cleanCredentials() {
		EditText ediLogin = (EditText) findViewById(R.id.edit_username);
		EditText ediPassword = (EditText) findViewById(R.id.edit_password);
		ediLogin.setText(Constants.LBL_EMPTY);
		ediPassword.setText(Constants.LBL_EMPTY);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		controller = new MainController(this);

		LocationManager L = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (!L.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showDialog(Constants.DIA_MSG_GPS_DISABLED, null);
		}
	}

	@Override
	protected void onRestart() {
		// booIsRestarted = true;
		super.onRestart();
	}

	@Override
	public void onResume() {
		super.onResume();
		_checkUploadService();
		/*
		 * ( TODO RO032 Sessión de Usuario Esto se utiliza cuando se requiere
		 * manejar el deslogueo del usuario luego de un tiempo de inactividad.
		 * Se ha comentado ya que hay varios módulos que pueden durar tiempos
		 * prolongados y no deberia obligarlo a loguearse (y perder la
		 * información ya avanzada).
		 */
		// boolean booExceededTimeout = false;
		// if (!booIsRestarted) {
		// booExceededTimeout = exceededTimeout();
		// } else{
		// booIsRestarted = false;
		// }

		// if (!booExceededTimeout &&
		// AppPreferences.getInstance(this)._IsLogged()) {
		if (AppPreferences.getInstance(this)._IsLogged()) {
			_loadMainScreen();
		} else {
			AppPreferences.getInstance(activity).savePreference(
					AppPreferences.PREF_ISUSERLOGGED, false);
			_loadLoginScreen();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		activity.getMenuInflater().inflate(R.menu.main_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		MenuItem itemService = menu.findItem(R.id.main_screen_menu_service);
		final EditText ediLogin = (EditText) findViewById(R.id.edit_username);
		final EditText ediPassword = (EditText) findViewById(R.id.edit_password);
		String strLogin = null;
		String strPassword = null;

		if (ediLogin == null) {
			strLogin = AppPreferences.getInstance(activity)._loadLogin();
		} else {
			strLogin = ediLogin.getText().toString();

		}

		if (ediPassword == null) {
			strPassword = AppPreferences.getInstance(activity)._loadPassword();
		} else {
			strPassword = ediPassword.getText().toString();

		}

		if (strLogin.equals(Constants.ADMIN_USERNAME)
				&& strPassword.equals(Constants.ADMIN_PASSWORD)) {
			itemService.setVisible(true);
		} else {
			itemService.setVisible(false);
		}
		/*
		 * ( TODO RO032 Sessión de Usuario Esto se utiliza cuando se requiere
		 * manejar el deslogueo del usuario luego de un tiempo de inactividad.
		 * Se ha comentado ya que hay varios módulos que pueden durar tiempos
		 * prolongados y no deberia obligarlo a loguearse (y perder la
		 * información ya avanzada).
		 */
		// if (!AppPreferences.getInstance(this)._IsLogged()) {
		// // itemSendContent.setVisible(false);
		// if (AppPreferences.getInstance(this)._loadProfileId() !=
		// Constants.PERFIL_OPERARIO);
		// itemService.setVisible(true);
		//
		// } else {
		// // itemSendContent.setVisible(true);
		// itemService.setVisible(false);
		//
		// }
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_screen_menu_service:
			showDialog(Constants.DIA_CUS_SERVICE_URL, null);
			break;
		// case R.id.main_screen_menu_tracking:
		// callActivity(ShowStoredLocationActivity.class.getName(),new
		// Bundle());
		// break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int dialog_id, Bundle bundle) {
		Dialog dialog = null;
		switch (dialog_id) {
		case Constants.DIA_CUS_SERVICE_URL:
			dialog = new CustomDialog(activity, dialog_id, bundle);
			break;
		case Constants.DIA_CUS_SELECT_PROFILE:
			dialog = new CustomDialog(activity, dialog_id, bundle);
			break;
		default:
			dialog = super.onCreateDialog(dialog_id, bundle);
		}
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int dialog_id, Dialog dialog, Bundle args) {
		switch (dialog_id) {
		case Constants.DIA_CUS_SELECT_PROFILE:
			((CustomDialog) dialog)._createProfileDialog(true);
			break;
		case Constants.DIA_CUS_SERVICE_URL:
			EditText ediInput = (EditText) dialog
					.findViewById(R.id.service_url_dialog_input);
			ediInput.setText(AppPreferences.getInstance(activity)
					._loadServiceUrl());

			EditText ediFtpUrlInput = (EditText) dialog
					.findViewById(R.id.service_ftp_url_dialog_input);
			ediFtpUrlInput.setText(AppPreferences.getInstance(activity)
					._loadFtpUrl());

//			 EditText ediFtpPortInput = (EditText) dialog
//			 .findViewById(R.id.service_ftp_port_dialog_input);
//			 ediFtpPortInput.setText(String.valueOf(AppPreferences.getInstance(
//			 activity)._loadFtpPort()));
//			
//			 EditText ediFtpUsernameInput = (EditText) dialog
//			 .findViewById(R.id.service_ftp_username_dialog_input);
//			 ediFtpUsernameInput.setText(AppPreferences.getInstance(activity)
//			 ._loadFtpUsername());
//			
//			 EditText ediFtpPasswordInput = (EditText) dialog
//			 .findViewById(R.id.service_ftp_password_dialog_input);
//			 ediFtpPasswordInput.setText(AppPreferences.getInstance(activity)
//			 ._loadFtpPassword());
			break;

		default:
			super.onPrepareDialog(dialog_id, dialog, args);
		}
	}

	@Override
	public void onBackPressed() {
		setQuitStartTime();
		super.onBackPressed();
	}

	/**
	 * Observador de eventos en los items de la lista del menú principal.
	 */
	private OnItemClickListener MainListItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			TextView txtItem = (TextView) view
					.findViewById(R.id.main_list_item_text_1);
			String strItem = txtItem.getText().toString();
			if (strItem.equals(activity.getResources().getString(
					R.string.title_view_list))) {
				if (AppPreferences.getInstance(activity)._loadDailySync()) {
					callActivity(PayloadActivity.class.getName());
				} else {
					showDialog(Constants.DIA_MSG_ERR_PAYLOAD_UNSYNCED);
				}
			} else if (strItem.equals(activity.getResources().getString(
					R.string.title_synchronize_load))) {

				if (!AppPreferences.getInstance(activity)._loadDailySync()) {
					PayloadAsyncTask objTask = new PayloadAsyncTask(activity);
					objTask.execute();
				} else {
					showDialog(Constants.DIA_CON_SYNC);
				}

			} else if (strItem.equals(activity.getResources().getString(
					R.string.title_change_profile))) {
				showDialog(Constants.DIA_CUS_SELECT_PROFILE);
			} else if (strItem.equals(activity.getResources().getString(
					R.string.title_view_summary))) {
				if (AppPreferences.getInstance(activity)._loadDailySync()) {
					callActivity(ProgressActivity.class.getName());
				} else {
					showDialog(Constants.DIA_MSG_ERR_PAYLOAD_UNSYNCED);
				}
			}

			else if (strItem.equals(activity.getResources().getString(
					R.string.title_session_logout))) {
				showDialog(Constants.DIA_CON_LOGOUT);
			}
		}
	};

	/**
	 * Adaptador de la lista del menú principal.
	 */
	private class MainListAdapter extends BaseAdapter {
		private final LayoutInflater mInflater;
		private final String[] strOptions;

		public MainListAdapter(final Context mContext) {
			super();
			mInflater = LayoutInflater.from(mContext);
			final Resources resources = mContext.getResources();
			strOptions = resources.getStringArray(R.array.list_main);
		}

		public int getCount() {
			return strOptions.length;
		}

		public Object getItem(final int position) {
			return position;
		}

		public long getItemId(final int position) {
			return position;
		}

		public View getView(final int position, final View convert,
				final ViewGroup parent) {
			ViewHolder holder;
			View convertView = convert;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main_list_item, null);
				holder = new ViewHolder();
				holder.text_item = (TextView) convertView
						.findViewById(R.id.main_list_item_text_1);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text_item.setText(strOptions[position]);
			return convertView;
		}

		class ViewHolder {
			private TextView text_item;
		}
	}

	public MainController getController() {
		return controller;
	}

	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return false;
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP ) {
//				
////			super.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN,new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP));
//
//			super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_UP));
//			return true;
//			}
//		if (event.getAction() == KeyEvent.ACTION_DOWN  && event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN ) {
//			
////			super.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN,new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN));
//
//			super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_DOWN));
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}
	
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
//	        if (event.getAction() == KeyEvent.ACTION_DOWN){
//
////	         enter();
//	        	super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP));
//	        	super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_UP));
//	        	
//	        	EditText oTxt = (EditText)this.findViewById(R.id.edit_password);
//	        	oTxt.requestFocus();
//	        	
//	            return true;
//	    }}
//	    
//	    if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
//	        if (event.getAction() == KeyEvent.ACTION_DOWN){
//
////	         enter();
//	        	super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN));
//	        	super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_DOWN));
//	        	
//	            return true;
//	    }}
//	    return super.dispatchKeyEvent(event);
//	};
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) 
//	{ 
//		
//		KeyEvent oEv = null;
//	
//	   if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) { 
//			oEv = new KeyEvent(event.getDownTime(), 
//			event.getEventTime(), 
//			event.getAction(), 19, 
//			event.getRepeatCount());
//		   
//			keyCode = KeyEvent.KEYCODE_DPAD_UP;
//		   
////		   return true;
//	   } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { 
//			oEv = new KeyEvent(event.getDownTime(), 
//			event.getEventTime(), 
//			event.getAction(), 20, 
//			event.getRepeatCount());		    
//			keyCode = KeyEvent.KEYCODE_DPAD_DOWN;   
//			   
////		   return true;
//	 
//	   }
//	   
//	   oEv = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
//	   return this.dispatchKeyEvent(oEv);
////	   return true;
////	       return super.onKeyDown(keyCode, oEv); 
//	   
//	}
	
	
	
	
}
