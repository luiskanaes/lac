package pe.beyond.lac.gestionmovil.activities;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Module;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.adapters.PayloadListAdapter;
import pe.beyond.lac.gestionmovil.controllers.PayloadController;
import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.ModuleDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.CustomDialog;
import pe.beyond.lac.gestionmovil.utils.Util;
import pe.beyond.zxing.client.android.BarcodeCaptureActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.BaseInputConnection;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PayloadActivity extends BaseActivity implements OnKeyListener,
		android.view.View.OnKeyListener {
	public PayloadActivity activity;
	private PayloadController controller;
	private boolean wasQRCode = false;
	private ListView lstPayload;

	public static int intPositionListView = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payload_screen);
		activity = this;

		controller = new PayloadController(this);

		_initUI();
	}

	public void _clickFromLst(AdapterView<?> parent, int position) {
		PayloadListAdapter cursorAdapter = (PayloadListAdapter) parent
				.getAdapter();
		Cursor cursor = cursorAdapter.getCursor();
		cursor.moveToPosition(position);
		int intSelectedId = cursor.getInt(cursor
				.getColumnIndex(ListingDAO.COL_IDE_LISTADO));
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.SELECTED_ID, intSelectedId);
		bundle.putBoolean(Constants.OBTAINED_BY_QR, false);
		callActivity(DetailActivity.class.getName(), bundle);

		// GMD INDEX LOCATOR
		// ((AppGMD) this.getApplication()).intListingIndex = position;

	}

	public void _initUI() {
		PayloadListAdapter payloadAdapter = controller._fillPayloadList();

		AdapterView.OnItemClickListener PayloadItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				_clickFromLst(parent, position);
				// PayloadListAdapter cursorAdapter = (PayloadListAdapter)
				// parent.getAdapter();
				// Cursor cursor = cursorAdapter.getCursor();
				// cursor.moveToPosition(position);
				// int intSelectedId =
				// cursor.getInt(cursor.getColumnIndex(ListingDAO.COL_IDE_LISTADO));
				// Bundle bundle = new Bundle();
				// bundle.putInt(Constants.SELECTED_ID, intSelectedId);
				// bundle.putBoolean(Constants.OBTAINED_BY_QR, false);
				// callActivity(DetailActivity.class.getName(),bundle);
			}
		};

		lstPayload = (ListView) findViewById(R.id.payload_list);
		lstPayload.setAdapter(payloadAdapter);
		lstPayload.setOnItemClickListener(PayloadItemClickListener);
		lstPayload.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		_initHeader();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		boolean result = false;

		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			event.startTracking();
			wasQRCode = false;

		}

		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			showDialog(Constants.DIA_CUS_LIST_CONFIGURATION);
			result = true;
		}
    	else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;

		// return super.onKeyDown(keyCode, event);
	}

	/* JV - I */

	private void scrollToPrevious() {
		int currentPosition = lstPayload.getFirstVisiblePosition();

		if (currentPosition == 0)
			return;

		if ((currentPosition - 5) < 0) {
			lstPayload.setSelection(currentPosition - 1);
			lstPayload.clearFocus();
			return;
		}

		lstPayload.setSelection(currentPosition - 5);
		lstPayload.clearFocus();
	}

	private void scrollToNext() {
		int currentPosition = lstPayload.getFirstVisiblePosition();
		if (currentPosition == lstPayload.getCount() - 5)
			return;

		lstPayload.setSelection(currentPosition + 5);
		lstPayload.clearFocus();
	}

	/* JV - F */

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub

		/*
		 * ESTO NO SE HACE _STATELIST SE USA COMO FLAG DE ACCESO CUANDO SE ENTRA
		 * EN EL DETALLE
		 */
		if (event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {
			int intSeleccion = lstPayload.getSelectedItemPosition();
			int intOrigen = ((AppGMD) this.getApplication())._stateList;
			if (intSeleccion > -1 && intOrigen == 0) {
				lstPayload.performItemClick(lstPayload, intSeleccion, 0);
			}
			((AppGMD) this.getApplication())._stateList = 0;
			return false;

		}

		if (event.getAction() == KeyEvent.ACTION_DOWN) {


//			switch (event.getKeyCode()) {
//			case KeyEvent.KEYCODE_VOLUME_UP:
//				//scrollToPrevious();
//				 BaseInputConnection bic=new BaseInputConnection(this.getWindow().getDecorView(),false);
//	                KeyEvent event1 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
//	                bic.sendKeyEvent(event1);
//				return true;
//			case KeyEvent.KEYCODE_VOLUME_DOWN:
//				//scrollToNext();
//				 BaseInputConnection bic2=new BaseInputConnection(this.getWindow().getDecorView(),false);
//	                KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
//	                bic2.sendKeyEvent(event2);
//				return true;
//			}



			int keyCode = event.getKeyCode();
			   BaseInputConnection bic=new BaseInputConnection(this.getWindow().getDecorView(),false);
		          if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_W || keyCode == KeyEvent.KEYCODE_I){  //UPPER
		                 KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
		                 bic.sendKeyEvent(event2);

                      int intCurrentModuleId = AppPreferences.getInstance(activity)
                              ._loadCurrentModuleId();
                      Form objForm = controller._obtainSearchForm(intCurrentModuleId);
                      if (objForm.getIntFlgSearch() == Constants.FLG_SELECTED) {
                          onSearchRequested();
                      }

		                 return true;
		          } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_S || keyCode == KeyEvent.KEYCODE_K){ //DOWN
		                 KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
		                 bic.sendKeyEvent(event2);

                      int intCurrentModuleId = AppPreferences.getInstance(activity)
                              ._loadCurrentModuleId();
                      Form objForm = controller._obtainSearchForm(intCurrentModuleId);
                      if (objForm.getIntFlgSearch() == Constants.FLG_SELECTED) {
                          onSearchRequested();
                      }

                      return true;
		          } else if(keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_J){ //LEFT
			        	  KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
			              bic.sendKeyEvent(event2);
			              return true;
		          } else if(keyCode == KeyEvent.KEYCODE_D || keyCode == KeyEvent.KEYCODE_L){ // RIGHT
			        	  KeyEvent event2 = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT,0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY);
			              bic.sendKeyEvent(event2);
			              return true;
		          }



		}
		if (event.getAction() == KeyEvent.ACTION_UP
				&& (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event
						.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH ||
		/* AGREGADO DE ACUERDO AL MODELO B5510L */
		keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_E
				|| keyCode == KeyEvent.KEYCODE_R
				|| keyCode == KeyEvent.KEYCODE_T
				|| keyCode == KeyEvent.KEYCODE_D
				|| keyCode == KeyEvent.KEYCODE_F
				|| keyCode == KeyEvent.KEYCODE_G
				|| keyCode == KeyEvent.KEYCODE_X
				|| keyCode == KeyEvent.KEYCODE_C
				|| keyCode == KeyEvent.KEYCODE_V) {
			if (!wasQRCode) {

				((AppGMD) this.getApplication())._executeNextPosition = 1;

				int intCurrentModuleId = AppPreferences.getInstance(activity)
						._loadCurrentModuleId();
				Form objForm = controller._obtainSearchForm(intCurrentModuleId);
				if (objForm.getIntFlgSearch() == Constants.FLG_SELECTED) {
					onSearchRequested();
				}

			}

			return true;
		}

		// if (keyCode==KeyEvent.KEYCODE_SPACE){
		// lstPayload.performItemClick(lstPayload,
		// lstPayload.getSelectedItemPosition(), 0);
		// return false;
		// }

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {

			int intCurrentModuleId = AppPreferences.getInstance(activity)
					._loadCurrentModuleId();
			Module objModule = controller._obtainModuleByPK(intCurrentModuleId);
			if (objModule.getIntQR() == Constants.FLG_ENABLED) {
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(Constants.VIBRATE_TIME);
				wasQRCode = true;
				Intent intent = new Intent(activity,
						BarcodeCaptureActivity.class);
				intent.putExtra(
						"pe.beyond.zxing.client.android.SCAN.SCAN_MODE",
						"QR_CODE_MODE");
				startActivityForResult(intent, Constants.QR_RESULT_CODE);
			}
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int dialog_id, Bundle bundle) {
		CustomDialog dialog = new CustomDialog(activity, dialog_id, bundle);
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Constants.DIA_CUS_CONFIGURATION_FILTER) {
			((CustomDialog) dialog)._createListFilterDialog(true);
		} else if (id == Constants.DIA_CUS_CONFIGURATION_ORDER) {
			intSelectedSpinnerId = -1;
			((CustomDialog) dialog)._createOrderDialog(true);
		} else if (id == Constants.DIA_CUS_CONFIGURATION_SEARCH) {
			((CustomDialog) dialog)._createListSearchDialog(true);
		} else {
			super.onPrepareDialog(id, dialog, args);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.QR_RESULT_CODE) {
			if (resultCode == RESULT_OK) {
				int intQRCodeFound = data.getIntExtra(Constants.QR_CODE_VALUE,
						-1);
				Log.i("TEST", "QR detectado y abriendo registro : "
						+ intQRCodeFound);
				if (intQRCodeFound < 0) {
					Log.w("TEST",
							" no deberia entrar aqui nunca, onActivityResult"
									+ intQRCodeFound);
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putInt(Constants.SELECTED_ID, intQRCodeFound);
				bundle.putBoolean(Constants.OBTAINED_BY_QR, true);

				Intent intent = new Intent();
				intent.setClassName(this, DetailActivity.class.getName());
				intent.putExtras(bundle);
				startActivityForResult(intent,
						Constants.OPCION_SEARCH_RESULT_CODE);

			} else if (resultCode == RESULT_CANCELED) {
				Log.i("TEST", "Se cancelo la busqueda de QR");
			}
		} else if (requestCode == Constants.OPCION_SEARCH_RESULT_CODE) {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(Constants.VIBRATE_TIME);

			System.out.println("SEARCH LONG PRESS");
			wasQRCode = true;
			Intent intent = new Intent(activity, BarcodeCaptureActivity.class);
			intent.putExtra("pe.beyond.zxing.client.android.SCAN.SCAN_MODE",
					"QR_CODE_MODE");
			startActivityForResult(intent, Constants.QR_RESULT_CODE);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (DetailActivity.FLAG_SEARCH_NOT_FOUND) {
			showDialog(Constants.DIA_MSG_ERR_SEARCH_NOT_FOUND, null);
			DetailActivity.FLAG_SEARCH_NOT_FOUND = false;
		}
		_initHeader();
		if (intPositionListView != -1) {
			lstPayload.setSelectionFromTop(intPositionListView, 0);
		}

		/* ADDS ACTION TO SET SELECTION ON LISTVIEW */

		if (((AppGMD) this.getApplication())._executeNextPosition == 1) {
			lstPayload
					.setSelection(((AppGMD) this.getApplication())._NextListingPosition);
			((AppGMD) this.getApplication())._executeNextPosition = 0;
		}

	}

	private void _initHeader() {
		String strTotal = String.valueOf(lstPayload.getCount());
		String strModuleId = String.valueOf(AppPreferences
				.getInstance(activity)._loadCurrentModuleId());

		String[] selectionArgs = new String[] { strModuleId };
		Cursor objCursor = getContentResolver().query(ModuleDAO.QUERY_ONE_URI,
				null, null, selectionArgs, null);
		String strModuleAcronym = objCursor.getString(objCursor
				.getColumnIndex(ModuleDAO.COL_COD_ACRONIMO));
		objCursor.close();

		int intCurrentFilter = AppPreferences.getInstance(activity)
				._loadListingCurrentFilter();
		int intFilterId = Util._obtainCurrentFilter(intCurrentFilter);

		String strFilter = Constants.LBL_SEPARATOR + getString(intFilterId)
				+ Constants.LBL_SEPARATOR + strTotal;
		int intColorName = Color.WHITE;
		int intColorFilter = Color.rgb(0, 176, 240);
		float fltSizeName = 1.0f;
		float fltSizeFilter = 1.0f;
		SpannableStringBuilder strHeader = Util._createTextInfo(
				strModuleAcronym, strFilter, intColorName, intColorFilter,
				fltSizeName, fltSizeFilter);

		TextView txtHeader = (TextView) findViewById(R.id.payload_text_header);
		txtHeader.setText(strHeader);
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO MANEJAR LAS OPCIONES DE LAS PANTALLAS DE CONFIGURACION CON EL
		// TECLADO FISICO
		return false;
	}

	public PayloadController getController() {
		return controller;
	}

	private int intSelectedSpinnerId = -1;

	public int getIntSelectedSpinnerId() {
		return intSelectedSpinnerId;
	}

	public void setIntSelectedSpinnerId(int intSelectedSpinnerId) {
		this.intSelectedSpinnerId = intSelectedSpinnerId;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& (v.getId() == R.id.spi_order_typ_1
						|| v.getId() == R.id.spi_order_typ_2
						|| v.getId() == R.id.spi_order_typ_3
						|| v.getId() == R.id.spi_order_way_1
						|| v.getId() == R.id.spi_order_way_2 || v.getId() == R.id.spi_order_way_3)) {
			intSelectedSpinnerId = v.getId();
		}
		return false;
	}
}
