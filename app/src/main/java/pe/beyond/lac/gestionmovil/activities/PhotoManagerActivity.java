package pe.beyond.lac.gestionmovil.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.beyond.gls.model.Photo;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.adapters.PhotoAdapter;
import pe.beyond.lac.gestionmovil.controllers.PhotoManagerController;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.CustomDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhotoManagerActivity extends BaseActivity {
	private PhotoManagerController controller;
	private ArrayList<Photo> lstPhoto;
	private PhotoAdapter photoAdapter;
	private ListView listViewPhotoGallery = null;
	private ImageView imgFullImage;
	private Photo objPhoto;
	private Bundle bundle;
	private boolean booMinOneImage;
	private boolean booFirstTime;
	private String strFileTemp;

	private String strPhotoPrefix;
	private int intModule;

	// Photo
	public String strTemporalImageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		controller = new PhotoManagerController(this);

		bundle = this.getIntent().getExtras();
		objPhoto = new Photo();
		_createPhotoPrefix();

		lstPhoto = bundle.getParcelableArrayList(Constants.LIST_PHOTO);
		booMinOneImage = bundle.getBoolean(Constants.BL_MIN_ONE_IMAGE);

		if (lstPhoto == null || lstPhoto.size() < 1) {
			booFirstTime = true;
			_startPhotoActivity();
		} else {
			createGUI();
		}
	}

	private void _createPhotoPrefix() {
		AppPreferences appPreferences = AppPreferences.getInstance(activity);
		String strAcronym = controller._obtainModuleByPK(
				appPreferences._loadCurrentModuleId()).getStrAcronym();
		strPhotoPrefix = appPreferences._loadCurrentBaseId() + "_" + strAcronym
				+ "_" + appPreferences._loadLogin() + "_";
		intModule = appPreferences._loadCurrentModuleId() ;
	}

	private void createGUI() {
		setContentView(R.layout.photomanager_screen);
		listViewPhotoGallery = (ListView) this
				.findViewById(R.id.listViewPhotoGallery);
		TextView textView = (TextView) this.findViewById(R.id.txtDefault);
		imgFullImage = (ImageView) this.findViewById(R.id.imgFullImage);

		photoAdapter = new PhotoAdapter(this);
		listViewPhotoGallery.setAdapter(photoAdapter);
		listViewPhotoGallery.setEmptyView(textView);

		listViewPhotoGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long id) {
				objPhoto = (Photo) adapter.getItemAtPosition(position);

				File imgFile = new File(getExternalFilesDir(
						Environment.DIRECTORY_PICTURES).getPath()
						+ "/" + objPhoto.getStrAbrevName());
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				imgFullImage.setImageBitmap(myBitmap);
			}

		});
		listViewPhotoGallery.setSelected(true);
		new PhotoTask().execute();
	}

	private class PhotoTask extends AsyncTask<Integer, Void, List<Photo>> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PhotoManagerActivity.this, null,
					"Cargando...");
			super.onPreExecute();
		}

		@Override
		protected List<Photo> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				if (bundle.getParcelableArrayList(Constants.LIST_PHOTO) != null
						&& bundle.getParcelableArrayList(Constants.LIST_PHOTO)
								.size() > 0) {
					lstPhoto = bundle
							.getParcelableArrayList(Constants.LIST_PHOTO);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return lstPhoto;
		}

		@Override
		protected void onPostExecute(List<Photo> result) {
			dialog.dismiss();

			photoAdapter.setPhotos(result);
			photoAdapter.notifyDataSetChanged();
			listViewPhotoGallery.requestFocusFromTouch();
			listViewPhotoGallery.setSelection(0);
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		activity.getMenuInflater().inflate(R.menu.photo_manager_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.photo_manager_screen_menu_exit) {
			if (lstPhoto.size() < 1 && booMinOneImage) {
				showUndeleteDialog(Constants.MSG_NO_NEW_IMAGE);
			} else {
				_return();
			}

			return true;
		} else if (item.getItemId() == R.id.photo_manager_screen_menu_delete) {
			deleteImage();

			return true;
		} else if (item.getItemId() == R.id.photo_manager_screen_menu_add) {
			_startPhotoActivity();
		}
		return false;
	}

	private void deleteImage() {
		try {
			if (lstPhoto.size() < 2 && booMinOneImage) {
				showUndeleteDialog(Constants.MSG_NO_NEW_IMAGE);
			} else if (objPhoto.isBooIsSent()) {
				showDialog(Constants.DIA_MSG_ERR_PHOTO_SENT, null);
			} else {
				File file = new File(getExternalFilesDir(
						Environment.DIRECTORY_PICTURES).getPath()
						+ "/" + objPhoto.getStrAbrevName());

				if (file.exists()) {
					Photo photo = new Photo();

					for (int i = 0; i < lstPhoto.size(); i++) {
						photo = (Photo) lstPhoto.get(i);

						if (photo
								.getStrAbrevName()
								.toUpperCase()
								.equals(objPhoto.getStrAbrevName()
										.toUpperCase())) {
							lstPhoto.remove(i);
							file.delete();
							break;
						}
					}

					imgFullImage.setImageResource(R.drawable.splash_image);

					photoAdapter.setPhotos(lstPhoto);
					photoAdapter.notifyDataSetChanged();
				} else {
					
					/* MOVER A DIALOG */
					android.widget.Toast
							.makeText(
									activity,
									"DEBE SELECCIONAR Y VERIFICAR LA IMAGEN ANTES DE ELIMINARLA",
									android.widget.Toast.LENGTH_SHORT).show();
				}
			}

		} catch (Exception e) {
			showUndeleteDialog(Constants.MSG_ERROR_TRYING_DELETE_IMAGE);
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog onCreateDialog(int dialog_id, Bundle bundle) {
		CustomDialog dialog = null;
		if (dialog_id == Constants.DIA_MSG_ERR_PHOTO_SENT) {
			dialog = new CustomDialog(activity, dialog_id, bundle);
		} else {
			dialog = (CustomDialog) super.onCreateDialog(dialog_id, bundle);
		}
		return dialog;
	}

	private void showUndeleteDialog(final String strMessage) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.CUSTOM_ERROR_MESSAGE, strMessage);
		showDialog(Constants.DIA_MSG_ERR_PHOTO_MANAGER_MESSAGE, bundle);
	}

	/**
	 * Crear la uri asociada a la ubicación de la foto temporal en la SDCard
	 * 
	 * @return Uri que identifica la ubicación de la foto temporal
	 */
	public Uri createUriLocation() {
		Uri result = null;

		createImageName();

		File file = new File(
				activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				strTemporalImageName);
		
		//ANDROID 2.2!!!! <<<<
		file.setWritable(true, true);
		
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				strFileTemp = file.getAbsolutePath();
				System.out.println(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result = Uri.fromFile(file);
		return result;
	}

	/**
	 * Crear el nombre temporal de la imágen que se va guardar y en futuro
	 * buscar en el directorio en el SDCard
	 * 
	 * @return String con el nombre temporal de la imágen
	 */
	public void createImageName() {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());

		strTemporalImageName = strPhotoPrefix + timeStamp + ".JPG";
	}

	public void _startPhotoActivity() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uriLocation = createUriLocation();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriLocation);
		startActivityForResult(intent,
				Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_MANAGER);
	}

	private Photo _createNewPhoto() {
		Photo objPhoto = new Photo();
		AppPreferences appPreferences = AppPreferences.getInstance(activity);
		objPhoto.setStrAbrevName(strTemporalImageName);

		Calendar calendar = Calendar.getInstance();
		String strFullName = calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "/"
				+ strTemporalImageName;
		objPhoto.setStrFullName(strFullName);
		objPhoto.setBooIsSent(false);
		objPhoto.setintModule( appPreferences._loadCurrentModuleId());
		return objPhoto;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_MANAGER) {
			if (resultCode == RESULT_OK) {
				try {
					File file = new File(strFileTemp);

					if (file.exists()) {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				lstPhoto.add(_createNewPhoto());
				if (booFirstTime) {
					_return();
				} else {
					if (listViewPhotoGallery == null) {
						createGUI();
					} else {
						photoAdapter.setPhotos(lstPhoto);
						photoAdapter.notifyDataSetChanged();

						listViewPhotoGallery.setSelection(0);
					}
				}
			} else if (resultCode == RESULT_CANCELED) {
				try {
					File file = new File(strFileTemp);

					if (file.exists()) {
						file.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				this.onBackPressed();
			}
		}
	}

	private void _return() {
		Bundle bundle = getIntent().getExtras();
		bundle.putParcelableArrayList(Constants.LIST_PHOTO, lstPhoto);
		getIntent().putExtras(bundle);
		this.setResult(RESULT_OK, getIntent());
		finish();
	}

	@Override
	public void onBackPressed() {
		if (!booMinOneImage || (booMinOneImage && lstPhoto.size() > 0)) {
			_return();
		} else {
			showUndeleteDialog(Constants.MSG_NO_NEW_IMAGE);
		}
	}

	public ArrayList<Photo> getLstPhoto() {
		return lstPhoto;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_A) {
			_startPhotoActivity();
		}
		if (keyCode == KeyEvent.KEYCODE_E) {

			deleteImage();
		}
		if (keyCode == KeyEvent.KEYCODE_S) {
			if (lstPhoto.size() < 1 && booMinOneImage) {
				showUndeleteDialog(Constants.MSG_NO_NEW_IMAGE);
			} else {
				_return();
			}

		}
		return super.onKeyUp(keyCode, event);
	}

}
