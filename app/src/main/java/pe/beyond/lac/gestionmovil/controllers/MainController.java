package pe.beyond.lac.gestionmovil.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.beyond.gls.model.ConfigurationEntity;
import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.OptionResult;
import pe.beyond.gls.model.User;
import pe.beyond.gls.model.UserAssigned;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.MainActivity;
import pe.beyond.lac.gestionmovil.adapters.ProfileAdapter;
import pe.beyond.lac.gestionmovil.asynctasks.LoginAsyncTask;
import pe.beyond.lac.gestionmovil.asynctasks.LogoutAsyncTask;
import pe.beyond.lac.gestionmovil.asynctasks.PayloadAsyncTask;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.ModuleDAO;
import pe.beyond.lac.gestionmovil.daos.OptionResultDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.ParsedConfiguration;
import pe.beyond.lac.gestionmovil.utils.ParsedInformation;
import pe.beyond.lac.gestionmovil.utils.ParsedLogin;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MainController extends BaseController{
	public MainController(MainActivity activity) {
		super(activity);
	}	
	
	public void saveData(final boolean isInformation,
			final Object obj) {
		if (isInformation) {
			String strDaoName = Constants.DAO_PACKAGE + ListingDAO.DAO_NAME;
			List<Listing> lstInformation = ((ParsedInformation)obj).getListElement();
			insertEntities(strDaoName, lstInformation);
		} else {
			
			for (ConfigurationEntity auxEntity : ((ParsedConfiguration)obj).getTables()) {
				String strAuxName = auxEntity.name;
				List<Generic> entities = auxEntity.getEntities();
				String strDaoName = seachDaoName(strAuxName);

				insertEntities(strDaoName, entities);
			}
		}
	}

	public void insertEntities(final String strDaoName,
			final Object auxEntities) {

		@SuppressWarnings("unchecked")
		List<Generic> entities = (List<Generic>) auxEntities;
		
		if (strDaoName != Constants.LBL_EMPTY) {
			try {
				Class<?> daoClass = Class.forName(strDaoName);

				Field field = daoClass.getField("INSERT_URI");
				Uri uriInsert = (Uri) field.get(Uri.class);

				Class<?> params[] = { Generic.class };
				for (Generic entity : entities) {
					Object paramsObj[] = { entity };
					Object daoObject = daoClass.newInstance();
					Method method = daoClass.getDeclaredMethod("createContentValues", params);
					ContentValues values = (ContentValues) method.invoke(daoObject, paramsObj);
					activity.getContentResolver().insert(uriInsert, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String seachDaoName(final String strAuxName) {
		String result = Constants.LBL_EMPTY;
		for (String auxDao : Constants.DB_ALL_DAO_NAMES) {
			try {
				Class<?> daoClass = Class.forName(auxDao);
				Field field = daoClass.getField("TB_NAME");
				String strTableName = (String) field.get(null);
				if (strAuxName.equals(strTableName)) {
					result = auxDao;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public void _saveLoginUserInformation(final ParsedLogin objParsedLogin) {
		int intModuleId = -1;
		int intBaseId = -1;
		String strUserCodeId = Constants.LBL_EMPTY;
		for (UserAssigned objUserAssigned : objParsedLogin.getLstAssignedUser()) {
			ContentValues objUserAssignedValues = UserAssignedDAO.createContentValues(objUserAssigned);
			activity.getContentResolver().insert(UserAssignedDAO.INSERT_URI,objUserAssignedValues);

			String strModuleId = String.valueOf(objUserAssigned.getModule().getIntModuleId());
			Cursor cursor = activity.getContentResolver().query(ModuleDAO.QUERY_ONE_URI, null, null, new String[]{strModuleId}, null);
			if (!(cursor!=null && cursor.getCount()>0)){
				ContentValues objModuleValues = ModuleDAO.createContentValues(objUserAssigned.getModule());
				activity.getContentResolver().insert(ModuleDAO.INSERT_URI, objModuleValues);
			}
			
			if (objUserAssigned.isBooFlgDefaultModule()) {
				intModuleId = objUserAssigned.getIntModuleId();
				intBaseId = objUserAssigned.getIntBaseId();
				strUserCodeId = objUserAssigned.getStrUserCode();
				
			}
		}

		User objUser = objParsedLogin.getObjUser();

		// TODO OPTIMIZACION: todo ingrese en un solo Editor.commit
		AppPreferences preferences = AppPreferences.getInstance(activity);		
		preferences.savePreference(AppPreferences.PREF_ISUSERLOGGED, true);
		preferences.savePreference(AppPreferences.PREF_USERID,
				objUser.getIntUserId());
		preferences.savePreference(AppPreferences.PREF_USERNAME,
				objUser.getStrName());
		preferences.savePreference(AppPreferences.PREF_FLASTNAME,
				objUser.getStrFLastName());
		preferences.savePreference(AppPreferences.PREF_MLASTNAME,
				objUser.getStrMLastName());
		preferences.savePreference(AppPreferences.PREF_PHONENUMBER,
				objUser.getStrPhoneNumber());
		preferences.savePreference(AppPreferences.PREF_STATE,
				objUser.getIntState());
		preferences.savePreference(AppPreferences.PREF_LOGIN,
				objUser.getStrLogin());
		preferences.savePreference(AppPreferences.PREF_PASSWORD,
				objUser.getStrPassword());
		
		preferences.savePreference(AppPreferences.PREF_TOKEN,
				objUser.getStrToken());
		preferences.savePreference(AppPreferences.PREF_LISTINGCURRENTFILTER, 
				Constants.LIST_PENDIENTE);
		preferences.savePreference(AppPreferences.PREF_CURRENTUSERCODE,
				strUserCodeId);
		preferences.savePreference(AppPreferences.PREF_CURRENTMODULEID,
				intModuleId);
		preferences.savePreference(AppPreferences.PREF_CURRENTBASEID,
				intBaseId);
		
		
//		preferences.savePreference(AppPreferences.PREF_SERVICE_URL,
//				Constants.URL_DETAULT_BASE);
//		preferences.savePreference(AppPreferences.PREF_FTP_URL,
//				Constants.FTP_DEFAULT_URL);
//		preferences.savePreference(AppPreferences.PREF_FTP_PORT,
//				Constants.FTP_DEFAULT_PORT);
//		preferences.savePreference(AppPreferences.PREF_FTP_USERNAME,
//				Constants.FTP_DEFAULT_USERNAME);
//		preferences.savePreference(AppPreferences.PREF_FTP_PASSWORD,
//				Constants.FTP_DEFAULT_PASSWORD);
	}
	
	/**
	 * Se customiza el adapter de la lista que muestran las opciones de perfiles
	 * permitidas para el usuario
	 * 
	 * @param objCursorUserAssigned 
	 * 
	 * @return adapter
	 */
	public ProfileAdapter _fillProfileList(Cursor objCursorUserAssigned) {
		ProfileAdapter adapter = null;
		int layout = R.layout.search_list_item;

		String[] from = new String[]{UserAssignedDAO.COL_DES_BASE};
		int[] to = new int[]{R.id.list_item_view};

		adapter = new ProfileAdapter(activity, layout, objCursorUserAssigned, from,to);
		return adapter;
	}

	/**
	 * @param lstElement - Elementos obtenidos del servicio
	 * @return
	 */
	public List<Listing> _obtainListingToInsert(List<Listing> lstElement) {
		ArrayList<Listing> lstListingToInsert = new ArrayList<Listing>();
		
		int intUserId = AppPreferences.getInstance(activity)._loadUserId();
		String[] proyection = new String[]{RegisterDAO.COL_IDE_LISTADO};
		String[] selectionArgs = new String[]{String.valueOf(intUserId)};
		
		//cursor contiene la lista de registros trabajados por el usuario
		Cursor objCursor = activity.getContentResolver().query(RegisterDAO.QUERY_ALL_WORKED_BY_USER_URI, proyection, null, selectionArgs, null);
		
		//si el elemento de la lista se encuentra dentro de los resultados del cursor, quiere decir que
		//el elemento fue trabajado, por lo tanto no debe eliminarse.
		
		if (objCursor.getCount()>0){
			boolean booListingFound;
			for (Listing objListing : lstElement){
				booListingFound = false;
				objCursor.moveToFirst();
				while(!objCursor.isAfterLast()){
					int intId = objCursor.getInt(objCursor.getColumnIndex(RegisterDAO.COL_IDE_LISTADO));
					if (objListing.getIntListId()== intId){
						booListingFound = true;
						break;
					}
					objCursor.moveToNext();
				}
				
				if (!booListingFound){
					lstListingToInsert.add(objListing);
				}
			}
		} else{
			lstListingToInsert = (ArrayList<Listing>) lstElement;
		}
		objCursor.close();
		
		return lstListingToInsert;
	}

	public void _registerData(ParsedInformation objParsedInformation) {
		_deleteListingPendingByUser();

		List<Listing> lstRetainedListing = _obtainRetainedListing();
		
//		if (lstRetainedListing!=null &&lstRetainedListing.size()>0){
//			Iterator it = objParsedInformation.getListElement().iterator();
//
//			while (it.hasNext()) {
//				Listing objListing = (Listing)it.next();
//				if (objListing.getIntListId()== objRetainedListing.getIntListId()){
//					objParsedInformation.getListElement().remove(objListing);
//					break;
//				}
//			}
//			
//			for(Listing objListing:objParsedInformation.getListElement()){
//				for (Listing objRetainedListing : lstRetainedListing){
//					
//				}
//			}
//		}
		
		
		if (lstRetainedListing!=null && lstRetainedListing.size()>0){
			//for(Listing objListing:objParsedInformation.getListElement()){
			Iterator it = objParsedInformation.getListElement().iterator();

			while (it.hasNext()) {	
				Listing objListing = (Listing)it.next();
				for (Listing objRetainedListing : lstRetainedListing){
					if (objListing.getIntListId()== objRetainedListing.getIntListId()){
						objParsedInformation.getListElement().remove(objListing);
						break;
					}
				}
			}
		}
		//seleccionar el contenido que quedó restante luego de la eliminacion
		//borrar de la lista descargada, los articulos que han podido quedar pendientes
//		List<Listing> lstListing = ((MainActivity)activity).getController()._obtainListingToInsert(objParsedInformation.getListElement());
//		
//		String[] selectionArgs = null;
//		for(Listing objListing: lstListing){
//			selectionArgs = new String[]{String.valueOf(objListing.getIntListId())};
//			activity.getContentResolver().delete(ListingDAO.DELETE_ONE_URI, null, selectionArgs);
//		}
//		
//		objParsedInformation.setListElement(lstListing);
		saveData(true, objParsedInformation);
	}

	private void _deleteListingPendingByUser() {
		String strUserId = String.valueOf(AppPreferences.getInstance(activity)._loadUserId());
		String strState = String.valueOf(Constants.LIST_PENDIENTE);
		String[]selectionArgs = new String[]{strUserId,strState};
		activity.getContentResolver().delete(ListingDAO.DELETE_PENDING_URI, null, selectionArgs);
	}

}
