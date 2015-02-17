package pe.beyond.lac.gestionmovil.utils;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.Question;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.DetailActivity;
import pe.beyond.lac.gestionmovil.activities.MainActivity;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.activities.PhotoManagerActivity;
import pe.beyond.lac.gestionmovil.adapters.FilterListAdapter;
import pe.beyond.lac.gestionmovil.adapters.ProfileAdapter;
import pe.beyond.lac.gestionmovil.adapters.SearchListAdapter;
import pe.beyond.lac.gestionmovil.asynctasks.LogoutAsyncTask;
import pe.beyond.lac.gestionmovil.asynctasks.PayloadAsyncTask;
import pe.beyond.lac.gestionmovil.controllers.OrderController;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.BaseInputConnection;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	public static final int STATE_ANSWERED = 1;
	private BaseActivity activity;
	public Dialog dialog;

	private int[] arrInputIds;
	
	public CustomDialog(final BaseActivity activity, final int dialog_id,
			final Bundle bundle) {
		super(activity);
		this.activity = activity;
		this.dialog = this;
		if (dialog_id < Constants.DIA_CON + Constants.DIA_STEP) {
			_manageConfirmationDialogs(dialog_id,bundle);
		} else if (dialog_id < Constants.DIA_MSG + Constants.DIA_STEP) {
			_manageMessageDialogs(dialog_id, bundle);
		} else if (dialog_id < Constants.DIA_CUS + Constants.DIA_STEP) {
			_manageCustomizedDialogs(dialog_id, bundle);
		} else if (dialog_id > Constants.DIA_MUL) {
			_createMultipleInputDialog(bundle);
		}
	}

	private void _manageConfirmationDialogs(final int dialog_id,final Bundle bundle) {
		View.OnClickListener btnYes = null;
		View.OnClickListener btnNo = null;
		switch (dialog_id) {
		case Constants.DIA_CON_SYNC:
			btnYes = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					PayloadAsyncTask objTask = new PayloadAsyncTask(activity);
					objTask.execute();
					
				}
			};
			btnNo = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};
			this.setOnKeyListener(new OnKeyListener() {	
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						switch (keyCode){
						case Constants.ACTIVITY_ACTION_NEXT:
							PayloadAsyncTask objTask = new PayloadAsyncTask(activity);
							objTask.execute();
							dialog.dismiss();
							return true;
						case KeyEvent.KEYCODE_ALT_RIGHT:
						case KeyEvent.KEYCODE_ALT_LEFT:
//						case Constants.ACTIVITY_ACTION_PREVIOUS:  <------- ALT !!!
							dialog.dismiss();
							return true;
						}
						return false;
					}
					return false;
				}}
			);
			_createConfirmationDialog(R.string.dialog_title_confirm_sync,
					R.string.dialog_message_confirm_sync, btnYes, btnNo);
			break;
		case Constants.DIA_CON_BACK:
			btnYes = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					activity.finish();
				}
			};
			btnNo = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};
			this.setOnKeyListener(new OnKeyListener() {	
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					switch (keyCode){
					case Constants.DIALOG_ACTION_NEXT:
						dialog.dismiss();
						activity.finish();
						return true;
//					case Constants.DIALOG_ACTION_PREVIOUS: <----- ALT
					case KeyEvent.KEYCODE_ALT_RIGHT:
					case KeyEvent.KEYCODE_ALT_LEFT:
						dialog.dismiss();
						return true;
					}
					return false;
				}}
			);
			_createConfirmationDialog(R.string.dialog_title_confirm_back,
					R.string.dialog_message_confirm_back, btnYes, btnNo);
			break;
		case Constants.DIA_CON_LOGOUT:
			btnYes = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					AppPreferences.getInstance(activity).savePreference(AppPreferences.PREF_ISUSERLOGGED, false);
					((MainActivity)activity)._loadLoginScreen();
					LogoutAsyncTask objTask = new LogoutAsyncTask(activity);
					objTask.execute();
				}
			};
			btnNo = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};
			this.setOnKeyListener(new OnKeyListener() {	
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					switch (keyCode){
					case Constants.DIALOG_ACTION_NEXT:
						AppPreferences.getInstance(activity).savePreference(AppPreferences.PREF_ISUSERLOGGED, false);
						((MainActivity)activity)._loadLoginScreen();
						LogoutAsyncTask objTask = new LogoutAsyncTask(activity);
						objTask.execute();
						dialog.dismiss();
						return true;
//					case Constants.DIALOG_ACTION_PREVIOUS: //<<<<<<<<<<<<<<<<--------- ALT!!!
					case KeyEvent.KEYCODE_ALT_RIGHT:
					case KeyEvent.KEYCODE_ALT_LEFT:
						dialog.dismiss();
						return true;
					}
					return false;
				}}
			);
			_createConfirmationDialog(R.string.dialog_title_confirm_logout,
					R.string.dialog_message_confirm_logout, btnYes, btnNo);
			break;
		case Constants.DIA_CON_UNRESTRICTIVE:
			btnYes = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					int intActivityState = bundle.getInt("intActivityState");
					String strInputValue = bundle.getString("strInputValue");
					Question objQuestion = (Question) bundle.getParcelable("objQuestion");
					int intState =((DetailActivity)activity).getController()._fillInputValue(intActivityState,strInputValue,objQuestion);
					if (intState == STATE_ANSWERED){
						((DetailActivity)activity)._executeAnsweredQuestion(objQuestion);
					}
				}
			};
			btnNo = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};
			this.setOnKeyListener(new OnKeyListener() {	
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (event.getAction()==KeyEvent.ACTION_DOWN ){
						switch (keyCode){
						case Constants.DIALOG_ACTION_NEXT:
							dialog.dismiss();
							int intActivityState = bundle.getInt("intActivityState");
							String strInputValue = bundle.getString("strInputValue");
							Question objQuestion = (Question) bundle.getParcelable("objQuestion");
							int intState =((DetailActivity)activity).getController()._fillInputValue(intActivityState,strInputValue,objQuestion);
							if (intState == STATE_ANSWERED){
								((DetailActivity)activity)._executeAnsweredQuestion(objQuestion);
							}
							return true;
//						case Constants.DIALOG_ACTION_PREVIOUS: <----------- ALT
						case KeyEvent.KEYCODE_ALT_RIGHT:
						case KeyEvent.KEYCODE_ALT_LEFT:
							dialog.dismiss();
							return true;
						}
						return false;
					} else{
						return false;
					}
				}}
			);
			String strMessageId = bundle.getString(Constants.CUSTOM_ERROR_MESSAGE);
			_createConfirmationDialog(R.string.dialog_title_custom_error,
					strMessageId, btnYes, btnNo);
			break;
		}

	}

	private void _createConfirmationDialog(final int intTitle,
			final Object objMessage, final View.OnClickListener YesClickListener,
			View.OnClickListener NoClickListener) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirmation_dialog);

		TextView txtTitle = (TextView) findViewById(R.id.text_yes_no_title);
		TextView txtMessage = (TextView) findViewById(R.id.text_yes_no_message);
		txtTitle.setText(activity.getString(intTitle));
		
		if (objMessage instanceof String){
			txtMessage.setText((String)objMessage);
		} else{
			txtMessage.setText(activity.getString((Integer)objMessage));
		}

		Button btnYes = (Button) findViewById(R.id.btn_yes);
		if (YesClickListener != null) {
			btnYes.setOnClickListener(YesClickListener);
		} else {
			btnYes.setVisibility(View.GONE);
		}

		Button btnNo = (Button) findViewById(R.id.btn_no);
		if (NoClickListener != null) {
			btnNo.setOnClickListener(NoClickListener);
		} else {
			btnNo.setVisibility(View.GONE);
		}
	}

	private void _manageMessageDialogs(final int dialog_id, final Bundle bundle) {
		String strTitle = activity.getString(R.string.dialog_no_title);
		String strMessage = activity.getString(R.string.dialog_no_message);
		switch (dialog_id) {
		case Constants.DIA_MSG_ERR_PAYLOAD_UNSYNCED:
			strTitle = activity
					.getString(R.string.dialog_title_error_unsynced_payload);
			strMessage = activity
					.getString(R.string.dialog_message_error_unsynced_payload);
			break;
		case Constants.DIA_MSG_ERR_LOGIN:
			strTitle = activity.getString(R.string.dialog_title_error_login);
			strMessage = activity
					.getString(R.string.dialog_message_error_login);
			break;
		case Constants.DIA_MSG_ERR_NETWORK:
			strTitle = activity
					.getString(R.string.dialog_title_error_network);
			strMessage = activity
					.getString(R.string.dialog_message_error_network);
			break;
		case Constants.DIA_MSG_ERR_CONFIG_DOWNLOAD:
			strTitle = activity
					.getString(R.string.dialog_title_error_config_download);
			strMessage = activity
					.getString(R.string.dialog_message_error_config_download);
			break;
		case Constants.DIA_MSG_ERR_CONFIG_PARSE:
			strTitle = activity
					.getString(R.string.dialog_title_error_config_parse);
			strMessage = activity
					.getString(R.string.dialog_message_error_config_parse);
			break;
		case Constants.DIA_MSG_ERR_INFO_DOWNLOAD:
			strTitle = activity
					.getString(R.string.dialog_title_error_info_download);
			strMessage = activity
					.getString(R.string.dialog_message_error_info_download);
			break;
		case Constants.DIA_MSG_ERR_INFO_PARSE:
			strTitle = activity
					.getString(R.string.dialog_title_error_info_parse);
			strMessage = activity
					.getString(R.string.dialog_message_error_info_parse);
			break;
		case Constants.DIA_MSG_ERR_SEARCH_NOT_FOUND:
			strTitle = activity
					.getString(R.string.dialog_title_error_search_not_found);
			strMessage = activity
					.getString(R.string.dialog_message_error_search_not_found);
		case Constants.DIA_MSG_ERR_OPTION_UNDEFINED:
			strTitle = activity
					.getString(R.string.dialog_title_error_option_undefined);
			strMessage = activity
					.getString(R.string.dialog_message_error_option_undefined);
			break;
		case Constants.DIA_MSG_ERR_TIME_FORMAT:
			strTitle = activity
					.getString(R.string.dialog_title_error_time_format);
			strMessage = activity
					.getString(R.string.dialog_message_error_time_format);
			break;
		case Constants.DIA_MSG_ERR_TIME_INCONGRUENCE:
			strTitle = activity
					.getString(R.string.dialog_title_error_time_incongruence);
			strMessage = activity
					.getString(R.string.dialog_message_error_time_incongruence);
			break;
		case Constants.DIA_MSG_ERR_TIME_OUT_FLUX:
			strTitle = activity
					.getString(R.string.dialog_title_error_time_out_flux);
			strMessage = activity
					.getString(R.string.dialog_message_error_time_out_flux);
			break;
		case Constants.DIA_MSG_ERR_TIME_CONFLICTS:
			strTitle = activity
					.getString(R.string.dialog_title_error_time_conflicts);
			strMessage = activity
					.getString(R.string.dialog_message_error_time_conflicts);
			break;
		case Constants.DIA_MSG_ERR_READING_EMPTY:
			strTitle = activity
					.getString(R.string.dialog_title_error_reading_empty);
			strMessage = activity
					.getString(R.string.dialog_message_error_reading_empty);
			break;
		case Constants.DIA_MSG_ERR_CUSTOM_VALUE:
			strTitle = bundle.getString(Constants.OVER_DIALOG_ERROR_CUSTOM);
			strMessage = bundle.getString(Constants.OVER_DIALOG_ERROR_CUSTOM);
			break;
		case Constants.DIA_MSG_ERR_INPUT_EMPTY:
			strTitle = activity.getString(R.string.dialog_title_input_empty);
			strMessage = activity
					.getString(R.string.dialog_message_input_empty);
			break;
		case Constants.DIA_MSG_ERR_INPUT_NOT_MIN:
			strTitle = activity
					.getString(R.string.dialog_title_input_not_minimun);
			strMessage = activity
					.getString(R.string.dialog_message_input_not_minimun);
			break;
		case Constants.DIA_MSG_PROCESS_STARTED:
			strTitle = activity
					.getString(R.string.dialog_title_process_started);
			strMessage = activity
					.getString(R.string.dialog_message_process_started);
			break;
		case Constants.DIA_MSG_USER_WITHOUT_MODULES:
			strTitle = activity
					.getString(R.string.dialog_title_user_without_modules);
			strMessage = activity
					.getString(R.string.dialog_message_user_without_modules);
			break;
		case Constants.DIA_MSG_ERR_VALIDATION_NOT_PASSED:
			strTitle = activity
					.getString(R.string.dialog_title_validation_not_passed);
			strMessage = activity
					.getString(R.string.dialog_message_validation_not_passed);
			break;
		case Constants.DIA_MSG_ERR_CUSTOM:
			strTitle = activity.getString(R.string.dialog_title_custom_error);
			strMessage = bundle.getString(Constants.CUSTOM_ERROR_MESSAGE);
			break;
		case Constants.DIA_MSG_ERR_PHOTO_MANAGER_MESSAGE:
			strTitle = activity.getString(R.string.dialog_title_photo_manager_error);
			strMessage = bundle.getString(Constants.CUSTOM_ERROR_MESSAGE);
			break;
		case Constants.DIA_MSG_GPS_DISABLED:
			strTitle = activity.getString(R.string.dialog_title_gps_disabled);
			strMessage = activity.getString(R.string.dialog_message_gps_disabled);
			break;
		case Constants.DIA_MSG_ERR_PHOTO_SENT:
			strTitle = activity.getString(R.string.dialog_title_photo_sent_error);
			strMessage = activity.getString(R.string.dialog_message_photo_sent_error);
			break;
		case Constants.DIA_MSG_ERR_LOGIN_MORE_CHARS:
			strTitle = activity.getString(R.string.dialog_title_login_more_chars);
			strMessage = activity.getString(R.string.dialog_message_login_more_chars);
			break;
			
			/*
		case Constants.DIA_MSG_INFO_MAP_UNAVAIBLE:
			strTitle = activity.getString(R.string.dialog_title_map_unavaible);
			strMessage = activity.getString(R.string.dialog_message_login_more_chars);
			break;
			*/
			
		}
		_createMessageDialog(dialog_id, strTitle, strMessage);
	}

	private void _createMessageDialog(final int dialog_id,
			final String strTitle, final String strMessage) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_dialog);

		TextView txtTitle = (TextView) findViewById(R.id.txt_dialog_title_message);
		TextView txtMessage = (TextView) findViewById(R.id.txt_dialog_message_message);
		txtTitle.setText(strTitle);
		txtMessage.setText(strMessage);

		Button btnClose = (Button) findViewById(R.id.btn_dialog_message_close);
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (dialog_id == Constants.DIA_MSG_ERR_READING_EMPTY
						|| dialog_id == Constants.DIA_MSG_ERR_CUSTOM_VALUE) {
					if (!((DetailActivity) activity).isBooAnswerReading()) {
						((DetailActivity) activity)._answerReading();
					}
				} else if (dialog_id == Constants.DIA_MSG_PROCESS_STARTED) {
					((DetailActivity) activity)._startProcess();
				} else if (dialog_id == Constants.DIA_MSG_ERR_PHOTO_MANAGER_MESSAGE){
					
					if (strMessage.equals(Constants.MSG_NO_NEW_IMAGE)
							&& ((PhotoManagerActivity)activity).getLstPhoto().size() < 1) {
						((PhotoManagerActivity)activity)._startPhotoActivity();	
					}
				} else if (dialog_id == Constants.DIA_MSG_GPS_DISABLED){
					Intent I = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					activity.startActivity(I);
				}
			}
		});
	}

	private void _manageCustomizedDialogs(final int dialog_id,
			final Bundle bundle) {
		switch (dialog_id) {
		case Constants.DIA_CUS_SELECT_PROFILE:
			_createProfileDialog(false);
			break;
		case Constants.DIA_CUS_LIST_CONFIGURATION:
			_createConfigurationDialog();
			break;
		case Constants.DIA_CUS_CONFIGURATION_SEARCH:
			_createListSearchDialog(false);
			break;
		case Constants.DIA_CUS_CONFIGURATION_ORDER:
			_createOrderDialog(false);
			break;
		case Constants.DIA_CUS_CONFIGURATION_FILTER:
			_createListFilterDialog(false);
			break;
		case Constants.DIA_CUS_READING_INPUT:
			_createReadingDialog();
			break;
		case Constants.DIA_CUS_DETAIL_END_ANSWER:
			_createEndAnswerDialog();
			break;
		case Constants.DIA_CUS_DANGEROUS_ZONE_TIME:
			_createDangerousZoneTime(bundle);
			break;
		case Constants.DIA_CUS_HELP_OPTIONS:
			_createHelpOptions(bundle);
			break;
		case Constants.DIA_CUS_SERVICE_URL:
			_createServiceUrl();
			break;
		}
	}

	private void _createServiceUrl() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.service_url_dialog);
		View.OnClickListener ServiceClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_accept:
					EditText ediUrlInput = (EditText) findViewById(R.id.service_url_dialog_input);
					EditText ediFtpUrlInput = (EditText) findViewById(R.id.service_ftp_url_dialog_input);

					String strServiceUrl = ediUrlInput.getText().toString();
					String strFtpUrl = ediFtpUrlInput.getText().toString();
					
					AppPreferences appPreferences = AppPreferences.getInstance(activity);
					
					appPreferences.savePreference(AppPreferences.PREF_SERVICE_URL, strServiceUrl);
					appPreferences.savePreference(AppPreferences.PREF_FTP_URL, strFtpUrl);
					
					break;
				case R.id.button_cancel:
					break;
				}
				((MainActivity)activity)._cleanCredentials();
				dialog.dismiss();
				
			}
		};

		Button btnAccept = (Button) this.findViewById(R.id.button_accept);
		btnAccept.setOnClickListener(ServiceClickListener);

		Button btnCancel = (Button) this.findViewById(R.id.button_cancel);
		btnCancel.setOnClickListener(ServiceClickListener);

	}

	/**
	 * Crear el di�logo para seleccionar el perfil en el que va a trabajar el
	 * operario.
	 */
	public void _createProfileDialog(boolean booRefreshed) {

		int intUserId = AppPreferences.getInstance(activity)._loadUserId();
		String[] selectionArgs = new String[] { String.valueOf(intUserId) };

		Cursor objCursorUserAssigned = activity.getContentResolver().query(
				UserAssignedDAO.QUERY_BY_USER_MODULE_URI, null, null,
				selectionArgs, null);
		if (objCursorUserAssigned.getCount() == 0) {
			_manageMessageDialogs(Constants.DIA_MSG_USER_WITHOUT_MODULES, null);
		} else {
			if (!booRefreshed){
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				setContentView(R.layout.list_dialog);
	
				TextView txtHeader = (TextView) findViewById(R.id.list_header);
				txtHeader.setText(R.string.profile_label_header);
			}
			ProfileAdapter objProfileAdapter = ((MainActivity) activity)
					.getController()._fillProfileList(objCursorUserAssigned);

			ListView lstProfile = (ListView) findViewById(R.id.list);
			lstProfile.setAdapter(objProfileAdapter);
			lstProfile.setOnItemClickListener(ProfileItemClickListener);

			setOnKeyListener((MainActivity) activity);
		}
	}

	AdapterView.OnItemClickListener ProfileItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProfileAdapter cursorAdapter = (ProfileAdapter) parent.getAdapter();
			Cursor cursor = cursorAdapter.getCursor();
			cursor.moveToPosition(position);

			int intBaseId = cursor.getInt(cursor
					.getColumnIndex(UserAssignedDAO.COL_COD_BASE));
			int intModuleId = cursor.getInt(cursor
					.getColumnIndex(UserAssignedDAO.COL_COD_MODULO));
			String strUserCode = cursor.getString(cursor
					.getColumnIndex(UserAssignedDAO.COL_COD_CODIGO));

			AppPreferences appPreferences = AppPreferences.getInstance(activity);
			int intCurrentModuleId = appPreferences._loadCurrentModuleId();
			int intCurrentBaseId = appPreferences._loadCurrentBaseId();

			if (intCurrentModuleId == intModuleId
					&& intCurrentBaseId == intBaseId) {
				activity._showToast(R.string.toast_message_current_module);
			} else {
				appPreferences.savePreference(AppPreferences.PREF_CURRENTMODULEID, intModuleId);
				appPreferences.savePreference(AppPreferences.PREF_CURRENTBASEID, intBaseId);
				appPreferences.savePreference(AppPreferences.PREF_CURRENTUSERCODE, strUserCode);
			}
			dialog.dismiss();
		}
	};

	/**
	 * Crear el di�logo con las opciones de b�squeda, ordenamiento y filtrado
	 */
	private void _createConfigurationDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_dialog);

		TextView txtHeader = (TextView) findViewById(R.id.list_header);
		txtHeader.setText(R.string.configuration_label_header);

		String[] strConfigurationOptions = activity.getResources()
				.getStringArray(R.array.list_configuration);

		ArrayAdapter<String> objConfigurationAdapter = new ArrayAdapter<String>(
				activity, R.layout.list_item, strConfigurationOptions);

		ListView lstConfiguration = (ListView) findViewById(R.id.list);
		lstConfiguration.setAdapter(objConfigurationAdapter);
		lstConfiguration.setOnItemClickListener(ConfigurationItemClickListener);
		
		// TODO Implementar shortcuts
		setOnKeyListener((PayloadActivity) activity);
		
	}

	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		boolean result = false;
        // Detectar y manejar el uso del bot�n HOME

        
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

	AdapterView.OnItemClickListener ConfigurationItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			String strSelectedOption = ((TextView) view).getText().toString();

			if (strSelectedOption.equals(activity
					.getString(R.string.label_configuration_search))) {
				activity.showDialog(Constants.DIA_CUS_CONFIGURATION_SEARCH);

			} else if (strSelectedOption.equals(activity
					.getString(R.string.label_configuration_order))) {
				activity.showDialog(Constants.DIA_CUS_CONFIGURATION_ORDER);

			} else if (strSelectedOption.equals(activity
					.getString(R.string.label_configuration_filter))) {
				activity.showDialog(Constants.DIA_CUS_CONFIGURATION_FILTER);

			}
			dialog.dismiss();
		}
	};

	public void _createListSearchDialog(boolean booRefreshed) {
		if (!booRefreshed){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.list_dialog);

			TextView txtHeader = (TextView) findViewById(R.id.list_header);
			txtHeader.setText(R.string.search_label_header);
		}
		
		SearchListAdapter objSearchListAdapter = ((PayloadActivity) activity)
				.getController()._fillSearchList();

		ListView lstSearch = (ListView) findViewById(R.id.list);
		lstSearch.setAdapter(objSearchListAdapter);
		lstSearch.setOnItemClickListener(SearchItemClickListener);

		setOnKeyListener((PayloadActivity) activity);
	}

	AdapterView.OnItemClickListener SearchItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SearchListAdapter cursorAdapter = (SearchListAdapter) parent
					.getAdapter();
			Cursor cursor = cursorAdapter.getCursor();
			cursor.moveToPosition(position);

			// Orden del Formulario del m�dulo actual que va a volverse el campo
			// de b�squeda
			int intFormOrder = cursor.getInt(cursor
					.getColumnIndex(FormDAO.COL_NUM_ORDEN));

			((PayloadActivity) activity).getController()._replaceSearchDialog(
					intFormOrder);
			((PayloadActivity) activity)._initUI();
			dialog.dismiss();
		}
	};

	public void _createOrderDialog(boolean booRefreshed) {
		if (!booRefreshed){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.order_dialog);

			TextView txtHeader = (TextView) findViewById(R.id.list_header);
			txtHeader.setText(R.string.order_label_header);
		}
		

		List<Form> lstForm = ((PayloadActivity)activity).getController()._obtainLstFormByOrder();
		
		
		LinearLayout linOrder1 = (LinearLayout) this.findViewById(R.id.lin_order1);
		LinearLayout linOrder2 = (LinearLayout) this.findViewById(R.id.lin_order2);
		LinearLayout linOrder3 = (LinearLayout) this.findViewById(R.id.lin_order3);
		
		Spinner spnOrderType1 = (Spinner) this.findViewById(R.id.spi_order_typ_1);
		Spinner spnOrderType2 = (Spinner) this.findViewById(R.id.spi_order_typ_2);
		Spinner spnOrderType3 = (Spinner) this.findViewById(R.id.spi_order_typ_3);
		Spinner spnOrderWay1 = (Spinner) this.findViewById(R.id.spi_order_way_1);
		Spinner spnOrderWay2 = (Spinner) this.findViewById(R.id.spi_order_way_2);
		Spinner spnOrderWay3 = (Spinner) this.findViewById(R.id.spi_order_way_3);
		
		spnOrderType1.setOnKeyListener((android.view.View.OnKeyListener) activity);
		spnOrderType2.setOnKeyListener((android.view.View.OnKeyListener) activity);
		spnOrderType3.setOnKeyListener((android.view.View.OnKeyListener) activity);
		final OrderController orderController = new OrderController((PayloadActivity)activity,lstForm,linOrder1,linOrder2,linOrder3,spnOrderType1,spnOrderWay1,spnOrderType2,spnOrderWay2,spnOrderType3,spnOrderWay3);
		
		View.OnClickListener OrderClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_accept:
					((PayloadActivity)activity).getController()._saveOrder(orderController);
					((PayloadActivity)activity)._initUI();
					break;
				case R.id.button_cancel:
					break;
				}
				dialog.dismiss();
			}
		};
		
		Button btnCancel = (Button) this.findViewById(R.id.button_cancel);
		btnCancel.setOnClickListener(OrderClickListener);
		Button btnAccept = (Button) this.findViewById(R.id.button_accept);
		btnAccept.setOnClickListener(OrderClickListener);
		
		setOnKeyListener((PayloadActivity) activity);
	}

	public void _createListFilterDialog(boolean booRefreshed) {
		if (!booRefreshed){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.list_dialog);

			TextView txtHeader = (TextView) findViewById(R.id.list_header);
			txtHeader.setText(R.string.filter_label_header);

		}
		FilterListAdapter objFilterListAdapter = ((PayloadActivity) activity)
				.getController()._fillFilterList();

		AdapterView.OnItemClickListener FilterItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FilterListAdapter cursorAdapter = (FilterListAdapter) parent
						.getAdapter();
				int intState = Integer.valueOf(cursorAdapter.getItem(position));
				AppPreferences.getInstance(activity).savePreference(
						AppPreferences.PREF_LISTINGCURRENTFILTER, intState);
				dialog.dismiss();
				((PayloadActivity) activity)._initUI();
			}
		};

		ListView lstSearch = (ListView) findViewById(R.id.list);
		lstSearch.setAdapter(objFilterListAdapter);
		lstSearch.setOnItemClickListener(FilterItemClickListener);

		setOnKeyListener((PayloadActivity) activity);
	}

	/**
	 * Se customiza el di�logo para lecturas considerando el m�dulo trabajado y
	 * ubicaci�n dentro del proceso.
	 * 
	 * @return dialog - dialog customizado
	 */
	private void _createReadingDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reading_dialog);
		this.setCancelable(false);

		Button btnValidate = (Button) findViewById(R.id.button_reading_input);
		EditText ediInput = (EditText) findViewById(R.id.edit_reading_input);

		View.OnClickListener ValidationClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText ediInput = (EditText) findViewById(R.id.edit_reading_input);
				String strInput = ediInput.getText().toString();
				((DetailActivity) activity).getController()._validateReading(
						strInput);
				ediInput.requestFocus();
			}
		};
		btnValidate.setOnClickListener(ValidationClickListener);
		btnValidate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				EditText ediInput = (EditText) findViewById(R.id.edit_reading_input);
				ediInput.requestFocus();
			}
		});
		ediInput.setOnKeyListener((DetailActivity) activity);
		ediInput.setNextFocusDownId(ediInput.getId());
		ediInput.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	private void _createEndAnswerDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.endanswer_dialog);
		Dialog.OnKeyListener EndAnswerKeyListener = new Dialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == Constants.ACTIVITY_ACTION_NEXT
							|| keyCode == Constants.ACTIVITY_ACTION_PREVIOUS) {
						dialog.dismiss();
						((DetailActivity) activity)._finishObliguedQuestions(keyCode);
						return true;
					}
				}
				if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
					return true;
				}
				return false;
			}

		};

		this.setOnKeyListener(EndAnswerKeyListener);

		View.OnClickListener EndAnswerClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int keyCode = -1;
				switch (view.getId()) {
				case R.id.button_endanswer_yes:
					keyCode = KeyEvent.KEYCODE_SPACE;
					break;
				case R.id.button_endanswer_no:
//					keyCode = KeyEvent.KEYCODE_ALT_RIGHT;
					keyCode = Constants.ACTIVITY_ACTION_PREVIOUS;
					break;
				}
				dialog.dismiss();
				((DetailActivity) activity)._finishObliguedQuestions(keyCode);
			}
		};

		Button btnNegative = (Button) findViewById(R.id.button_endanswer_no);
		Button btnAffirmative = (Button) findViewById(R.id.button_endanswer_yes);

		btnNegative.setOnClickListener(EndAnswerClickListener);
		btnAffirmative.setOnClickListener(EndAnswerClickListener);

	}

	private static final int KEY_DAN_ZONE_ACCEPT = KeyEvent.KEYCODE_SPACE;
	private static final int KEY_DAN_ZONE_CANCEL = KeyEvent.KEYCODE_ALT_RIGHT;

	private void _createDangerousZoneTime(final Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);
		setContentView(R.layout.dangerous_zone_time);

		String strStartTime = bundle.getString(Constants.KEY_START_TIME);
		String strEndTime = bundle.getString(Constants.KEY_END_TIME);

		EditText ediStartTime = (EditText) findViewById(R.id.edit_starttime);
		EditText ediEndTime = (EditText) findViewById(R.id.edit_endtime);

		ediStartTime.setSelectAllOnFocus(true);
		ediEndTime.setSelectAllOnFocus(true);

		android.view.View.OnKeyListener StartTimeKeyListener = new android.view.View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				EditText ediTime = (EditText) v;

				String strTime = ediTime.getText().toString();
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					ediTime.setText("");
				} else if (strTime.length() == 2) {
					strTime += ":";
					ediTime.setText(strTime);
					ediTime.setSelection(strTime.length());
				} else if (strTime.length() == 5
						&& ediTime.getSelectionStart() == 5) {
					EditText ediEndTime = (EditText) dialog
							.findViewById(R.id.edit_endtime);
					ediEndTime.requestFocus();
					dispatchKeyEvent(event);
				}
				return false;
			}

		};

		android.view.View.OnKeyListener EndTimeKeyListener = new android.view.View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				EditText ediTime = (EditText) v;

				String strTime = ediTime.getText().toString();
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					ediTime.setText("");
				} else if (strTime.length() == 2) {
					strTime += ":";
					ediTime.setText(strTime);
					ediTime.setSelection(strTime.length());
				}
				return false;
			}

		};
		ediStartTime.setOnKeyListener(StartTimeKeyListener);
		ediEndTime.setOnKeyListener(EndTimeKeyListener);

		ediStartTime.setText(strStartTime);
		ediEndTime.setText(strEndTime);

		View.OnClickListener EndTimeClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_actionEndDangerousTime(view.getId());
			}
		};

		Button btnCancel = (Button) findViewById(R.id.button_cancel);
		Button btnAccept = (Button) findViewById(R.id.button_accept);

		btnCancel.setOnClickListener(EndTimeClickListener);
		btnAccept.setOnClickListener(EndTimeClickListener);
		this.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KEY_DAN_ZONE_CANCEL) {
					_actionEndDangerousTime(R.id.button_cancel);
				} else if (keyCode == KEY_DAN_ZONE_ACCEPT) {
					// TODO VALIDAR QUE EN AMBAS CAJAS DE TEXTO TENGAN 5
					// CARACTERES

					EditText ediStartTime = (EditText) findViewById(R.id.edit_starttime);
					EditText ediEndTime = (EditText) findViewById(R.id.edit_endtime);
					if (ediStartTime.getText().toString().length() == 5
							&& ediEndTime.getText().toString().length() == 5) {
						_actionEndDangerousTime(R.id.button_accept);
					}
				}
				
				else if (keyCode == KeyEvent.KEYCODE_BACK) {
					_actionEndDangerousTime(R.id.button_cancel);
				}
				return false;
			}
		});
	}

	public void _actionEndDangerousTime(final int intViewId) {
		switch (intViewId) {
		case R.id.button_cancel:
			((DetailActivity) activity).getController()
					._updateDangerousZoneTime(false, Constants.LBL_EMPTY,
							Constants.LBL_EMPTY);
			dialog.dismiss();
			break;
		case R.id.button_accept:
			EditText ediStartTime = (EditText) findViewById(R.id.edit_starttime);
			EditText ediEndTime = (EditText) findViewById(R.id.edit_endtime);

			String strStartTime = ediStartTime.getText().toString();
			String strEndTime = ediEndTime.getText().toString();

			int intResultId = Util._correctTimeFormat(activity, strStartTime+":00",
					strEndTime+ ":00");
			if (intResultId < 0) {
				((DetailActivity) activity).getController()
						._updateDangerousZoneTime(true, strStartTime,
								strEndTime);
				dialog.dismiss();
			} else {
				Bundle bundle = new Bundle();
				bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
				activity.showDialog(intResultId, bundle);
			}
			break;
		}
	}

	/**
	 * 
	 */
	private void _createMultipleInputDialog(final Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);

		ArrayList<Question> lstQuestion = bundle
				.getParcelableArrayList(Constants.KEY_QUESTIONS_ASKED);

		ScrollView scrollRoot = new ScrollView(activity);

		scrollRoot.setId(Constants.ID_DIALOG_ROOT);
		scrollRoot.setLayoutParams(new ScrollView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LinearLayout linRoot = new LinearLayout(activity);
		linRoot.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linRoot.setOrientation(LinearLayout.VERTICAL);
		linRoot.setVisibility(View.VISIBLE);

		int intHelpHashCode = "HelpOptions".hashCode();
		LinearLayout linHelp = new LinearLayout(activity);
		linHelp.setId(intHelpHashCode);
		linHelp.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		linHelp.setOrientation(LinearLayout.VERTICAL);
		linHelp.setVisibility(View.GONE);

		arrInputIds = new int[lstQuestion.size()];
		for (int intIte = 0; intIte < lstQuestion.size(); intIte++) {
			Question objQuestion = lstQuestion.get(intIte);
			
			int intEditTextId = "Edit".hashCode() + objQuestion.getIntQuestionId();
			arrInputIds[intIte] = intEditTextId;
			LinearLayout linInfo = Util._createInfo(activity, objQuestion,intEditTextId);
			linRoot.addView(linInfo);
		}
		linRoot.addView(linHelp);

		scrollRoot.addView(linRoot);
		setContentView(scrollRoot);
	}

	private void _createHelpOptions(Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);
		this.setContentView(R.layout.help_options_dialog);

		float fltSizeLabel = 1.5f;
		float fltSizeInfo = 1.5f;

		String strHeader = bundle.getString(Constants.KEY_HEADER);
		TextView txtView = (TextView) this
				.findViewById(R.id.first_help_options_header);
		int intColorHeaderLabel = Color.rgb(0, 176, 240);
		int intColorHeaderInfo = Color.WHITE;
		txtView.setText(Util._createTextInfo(strHeader + " : ",
				Constants.LBL_EMPTY, intColorHeaderLabel, intColorHeaderInfo,
				fltSizeLabel, fltSizeInfo));

		ArrayList<Option> lstOption = bundle
				.getParcelableArrayList(Constants.KEY_POSSIBLE_OPTIONS);
		LinearLayout linRoot = (LinearLayout) this
				.findViewById(R.id.first_help_options_items);
		int intColorItemLabel = Color.rgb(228, 108, 10);
		int intColorItemInfo = Color.WHITE;

		for (int intIte = 0; intIte < lstOption.size(); intIte++) {
			Option objOption = lstOption.get(intIte);

			String strAbreviation = " [ "
					+ Util._getKeyString(objOption.getIntKey()) + " ] ";
			String strReal = objOption.getStrDescription();

			TextView txtInfo = new TextView(activity);
			txtInfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			txtInfo.setVisibility(View.VISIBLE);
			txtInfo.setText(Util._createTextInfo(strAbreviation + " : ",
					strReal, intColorItemLabel, intColorItemInfo, fltSizeLabel,
					fltSizeInfo));
			linRoot.addView(txtInfo);
		}

		this.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SPACE) {
					dialog.dismiss();
				}
				return true;
			}
		});
	}

	@Override
	public boolean onSearchRequested() {
		return false;
	}

	public int[] getArrInputIds() {
		return arrInputIds;
	}
}
