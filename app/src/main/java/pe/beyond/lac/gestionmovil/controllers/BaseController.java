package pe.beyond.lac.gestionmovil.controllers;

import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Flux;
import pe.beyond.gls.model.FluxValidation;
import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Module;
import pe.beyond.gls.model.ObsCode;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.OptionModResult;
import pe.beyond.gls.model.OptionResult;
import pe.beyond.gls.model.Register;
import pe.beyond.gls.model.RegisterAnswer;
import pe.beyond.gls.model.Track;
import pe.beyond.gls.model.UserValidation;
import pe.beyond.gls.model.Validation;
import pe.beyond.gls.model.ValidationResult;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.LocationActivity;
import pe.beyond.lac.gestionmovil.daos.DataTypeDAO;
import pe.beyond.lac.gestionmovil.daos.FluxDAO;
import pe.beyond.lac.gestionmovil.daos.FluxValidationDAO;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.ModuleDAO;
import pe.beyond.lac.gestionmovil.daos.ObsCodeDAO;
import pe.beyond.lac.gestionmovil.daos.OptionDAO;
import pe.beyond.lac.gestionmovil.daos.OptionModResultDAO;
import pe.beyond.lac.gestionmovil.daos.OptionResultDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterAnswerDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import pe.beyond.lac.gestionmovil.daos.UserValidationDAO;
import pe.beyond.lac.gestionmovil.daos.ValidationDAO;
import pe.beyond.lac.gestionmovil.daos.ValidationResultDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;

public class BaseController {
	protected BaseActivity activity;

	public BaseController(BaseActivity activity) {
		super();
		this.activity = activity;
	}
	/**
	 * Obtener los resultados a partir del �ndice de una opci�n
	 *
	 * @param intOptionIndex
	 *            - �ndice de la opci�n en el listado de opciones
	 * @return listado de resultados generados por la opci�n
	 */
	public Form _obtainSearchForm(final int intModuleId) {
		String[] selectionArgs = new String[] { String.valueOf(intModuleId) };
		Cursor cursor = activity.getContentResolver().query(
				FormDAO.QUERY_SELECTED_SEARCH_BY_MODULEID_URI, null, null,
				selectionArgs, null);
		Form objForm = FormDAO.createObject(cursor);
		cursor.close();
		return objForm;
	}
	
	
	
	
	/**
	 * This method works up to get duplicate listings.
	 * @param _strDesCampo
	 * @returns collection
	 */
	public List<Listing> _obtainDuplicates(String _strDesCampo) {

		Form objForm = _obtainSearchForm(AppPreferences.getInstance(activity)
				._loadCurrentModuleId());
		String strField = "DES_CAMPO" + String.valueOf(objForm.getIntOrder())
				+ "_LIS";

		String[] projection = new String[] {
				ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_ID,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_LISTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_NUM_DESCARGA,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_FLUJO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_GESTIONCOMERCIAL,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_FEC_FECHA_INICIO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_FEC_FECHA_FIN,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_USUARIO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_CAN_GPS_CATASTRO_LATITUD,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_CAN_GPS_CATASTRO_LONGITUD,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_NUM_ORDEN_LISTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_NUM_NRO_VISITA,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_ESTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO01,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO02,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO03,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO04,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO05,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO06,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO07,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO08,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO09,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_CAMPO10,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION01,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION02,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION03,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION04,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION05,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION06,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION07,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION08,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION09,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION10,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION11,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION12,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION13,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION14,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION15,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION16,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION17,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION18,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION19,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_DES_VALIDACION20,
				ListingDAO.TB_NAME + Constants.LBL_DOT + strField };

		String strWhere = strField + " = '" + _strDesCampo +"'";
		

		/**
		 * Cursor android.content.ContentResolver.query(Uri uri, String[]
		 * projection, String selection, String[] selectionArgs, String
		 * sortOrder)
		 */

		Cursor cursorPayload = activity.getContentResolver().query(
				ListingDAO.QUERY_ALL_LIST_URI, projection, 	 strWhere ,	null, null);

		List<Listing> lstDuplicateAll = ListingDAO.createObjects(cursorPayload);

//		Cursor cursor = activity.getContentResolver().query(
//				ListingDAO.QUERY_ALL_LIST_URI, projection, 	 strWhere ,	null, null);

		return lstDuplicateAll;
	}
	
	
	
	
	
	/**
	 * Obtener la informaci�n del elemento seg�n el Listing id.
	 * 
	 * @param intListingId
	 * @return result
	 */
	public Listing _obtainListingByPK(final int intListingId) {
		String[] selectionArgs = new String[] { String.valueOf(intListingId) };
		Cursor cursor = activity.getContentResolver().query(
				ListingDAO.QUERY_ONE_URI, null, null, selectionArgs, null);
		Listing result = ListingDAO.createObject(cursor);
		cursor.close();

		return result;
	}
	
	/**
	 * Obtener la informaci�n del tipo de dato seg�n el Data Type id.
	 * 
	 * @param intDataTypeId
	 * @return result
	 */
	public DataType _obtainDataTypeByDataTypeId(final int intDataTypeId) {
		String[] selectionArgs = new String[] { String.valueOf(intDataTypeId) };
		Cursor cursor = activity.getContentResolver().query(
				DataTypeDAO.QUERY_ONE_URI, null, null, selectionArgs, null);
		DataType result = DataTypeDAO.createObject(cursor);
		cursor.close();
		return result;
	}
	
	/**
	 * Obtener la informaci�n del registro seg�n el PK
	 * 
	 * @param intListingId
	 * @return result
	 */
	public Register _obtainRegisterByPK(final int intListingId, final int intDownloadCounter) {
		String[] selectionArgs = new String[] { String.valueOf(intListingId),String.valueOf(intDownloadCounter) };
		Cursor cursor = activity.getContentResolver().query(
				RegisterDAO.QUERY_ONE_BY_LISTING_ID_URI, null, null,
				selectionArgs, null);
		Register result = RegisterDAO.createObject(cursor);
		cursor.close();

		return result;
	}
	

	/**
	 * Obtener la informaci�n del m�dulo seg�n el PK
	 * 
	 * @param intModuleId
	 * @return result
	 */
	public Module _obtainModuleByPK(final int intModuleId) {
		String[] selectionArgs = new String[] { String.valueOf(intModuleId) };
		Cursor cursor = activity.getContentResolver().query(
				ModuleDAO.QUERY_ONE_URI, null, null,
				selectionArgs, null);
		Module result = ModuleDAO.createObject(cursor);
		cursor.close();

		return result;
	}
	
	/**
	 * Obtener la informaci�n del flujo seg�n el PK.
	 * 
	 * @param intFluxId
	 * @return result
	 */
	public Flux _obtainFluxByPK(int intFluxId) {
		String[] selectionArgs = new String[] { String.valueOf(intFluxId) };
		Cursor cursor = activity.getContentResolver().query(
				FluxDAO.QUERY_ONE_URI, null, null, selectionArgs, null);
		Flux result = FluxDAO.createObject(cursor);
		cursor.close();

		return result;
	}
	
	/**
	 * Obtener la informaci�n del track m�s reciente.
	 * 
	 * @return result
	 */
	public Track _obtainTrackMostRecent() {
		Cursor cursor = activity.getContentResolver().query(
				TrackDAO.QUERY_MOST_RECENT_URI, null, null, null, null);
		Track result = TrackDAO.createObject(cursor);
		cursor.close();

		return result;
	}
	/**
	 * Obtener la informaci�n del flujo seg�n el PK.
	 * 
	 * @param intListId
	 * @param intQuestionId
	 * @return result
	 */
	protected RegisterAnswer _obtainRegisterAnswerByPK(
			final int intListId, final int intDownloadCounter, final int intQuestionId) {
		String[] selectionArgs = new String[] { String.valueOf(intListId), String.valueOf(intDownloadCounter),
				String.valueOf(intQuestionId) };
		Cursor cursor = activity.getContentResolver().query(
				RegisterAnswerDAO.QUERY_ONE_URI, null, null,
				selectionArgs, null);
		RegisterAnswer result = RegisterAnswerDAO
				.createObject(cursor);
		cursor.close();

		return result;
	}
	
	/**
	 * Obtener la informaci�n de una validaci�n seg�n el PK.
	 * 
	 * @param intValidationId
	 * @return result
	 */
	public Validation _obtainValidationByPK(final int intValidationId) {
		String[] selectionArgs = new String[] { String.valueOf(intValidationId) };
		Cursor cursor = activity.getContentResolver().query(
				ValidationDAO.QUERY_ONE_URI, null, null, selectionArgs, null);
		Validation result = ValidationDAO.createObject(cursor);
		cursor.close();
		return result;
	}
	

	/**
	 * Obtener los resultados de una opci�n a partir del PK.
	 * 
	 * @param intOptionId
	 * @return lstResult
	 */
	public List<OptionResult> _obtainOptionResultsByPK(final int intOptionId) {
		String[] selectionArgs = new String[] { String.valueOf(intOptionId) };
		Cursor cursor = activity.getContentResolver().query(
				OptionResultDAO.QUERY_BY_OPTIONID_URI, null, null,
				selectionArgs, null);
		List<OptionResult> lstResult = OptionResultDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}
	
	/**
	 * Obtener los resultados de una opci�n a partir del PK.
	 * 
	 * @param intOptionId
	 * @return lstResult
	 */
	public UserValidation _obtainUserValidationByData(final int intUserId, final int intValidationId, final String strInputValue,final int intState) {
		String[] selectionArgs = new String[] { String.valueOf(intUserId), String.valueOf(intValidationId), strInputValue, String.valueOf(intState)};
		Cursor cursor = activity.getContentResolver().query(UserValidationDAO.QUERY_BY_DATA_URI, null, null,selectionArgs, null);
		UserValidation objUserValidation = UserValidationDAO.createObject(cursor);
		cursor.close();
		return objUserValidation;
	}
	
	/**
	 * Obtener los medidores entre los que se puede elegir.
	 * 
	 * @param intUserId
	 * @param intValidationId
	 * @return
	 */
	public List<UserValidation> _obtainLstUserValidationByIds(int intUserId,
			int intValidationId) {
		String[] selectionArgs = new String[] { String.valueOf(intUserId), String.valueOf(intValidationId)};
		Cursor cursor = activity.getContentResolver().query(UserValidationDAO.QUERY_BY_IDS_URI, null, null,selectionArgs, null);
		List<UserValidation> lstResult = UserValidationDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}
	
	/**
	 * Obtener las opciones a modificad a partir del PK.
	 * 
	 * @param intOptionId
	 * @return lstResult
	 */
	public List<OptionModResult> _obtainOptionModResultsByOptionId(final int intOptionId) {
		String[] selectionArgs = new String[] { String.valueOf(intOptionId) };
		Cursor cursor = activity.getContentResolver().query(
				OptionModResultDAO.QUERY_BY_OPTIONID_URI, null, null,
				selectionArgs, null);
		List<OptionModResult> lstResult = OptionModResultDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}


	public List<Form> _obtainLstFormByOrder() {
		String[] selectionArgs = new String[]{String.valueOf(AppPreferences.getInstance(activity)._loadCurrentModuleId())};
		Cursor cursor = activity.getContentResolver().query(FormDAO.QUERY_FOR_ORDER_OPTIONS_URI, null, null, selectionArgs, null);
		List<Form> lstForm = FormDAO.createObjects(cursor);
		return lstForm;
	}
	
	public List<Form> _obtainLstFormByListingOrder() {
		String[] selectionArgs = new String[]{String.valueOf(AppPreferences.getInstance(activity)._loadCurrentModuleId())};
		Cursor cursor = activity.getContentResolver().query(FormDAO.QUERY_FOR_LISTING_ORDER_URI, null, null, selectionArgs, null);
		List<Form> lstForm = FormDAO.createObjects(cursor);
		return lstForm;
	}
	
	/**
	 * Obtener los resultados de una validaci�n a partir del PK.
	 * 
	 * @param intValidationId
	 * @return lstResult
	 */
	public List<ValidationResult> _obtainValidationResultsByPK(final int intValidationId) {
		String[] selectionArgs = new String[] { String.valueOf(intValidationId) };
		Cursor cursor = activity.getContentResolver().query(
				ValidationResultDAO.QUERY_BY_VALIDATIONID_URI, null, null,
				selectionArgs, null);
		List<ValidationResult> lstResult = ValidationResultDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}

	/**
	 * Obtener c�digo de observaci�n a partir del ObsCode Id y Flux Id.
	 * 
	 * @param intObsCodeId
	 * @param intFluxId
	 * @return lstResult
	 */
	public List<ObsCode> _obtainObsCodesByObsCodeIdAndFluxId(final int intObsCodeId,final int intFluxId) {
		
		String[] selectionArgs = new String[] { String.valueOf(intObsCodeId),
				String.valueOf(intFluxId) };
		Cursor cursor = activity.getContentResolver().query(
				ObsCodeDAO.QUERY_BY_OBSCODEID_URI, null, null, selectionArgs,
				null);
		List<ObsCode> lstResult = ObsCodeDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}

	/**
	 * Obtener las opciones seg�n el Question Id.
	 * 
	 * @param intQuestionId
	 * @return lstResult
	 */
	public ArrayList<Option> _obtainOptionsByQuestionId(
			final int intQuestionId) {
		String[] selectionArgs = new String[] { String
				.valueOf(intQuestionId) };
		Cursor cursor = activity.getContentResolver().query(
				OptionDAO.QUERY_BY_QUESTIONID_URI, null, null, selectionArgs,null);
		ArrayList<Option> lstResult = OptionDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}
	
	/**
	 * Obtener todas las opciones con estado diferente a 1.
	 * 
	 */
	public List<Option> _obtainAllNonAvailableOptions() {
		Cursor cursor = activity.getContentResolver().query(
				OptionDAO.QUERY_NON_AVAILABLE_OPTIONS_URI, null, null, null,null);
		ArrayList<Option> lstResult = OptionDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}
	
	/**
	 * Obtener las respuestas del registro seg�n el Register Id.
	 * 
	 * @param intRegisterId
	 * @return lstResult
	 */
	public List<RegisterAnswer> _obtainLstRegisterAnswer(final int intRegisterId, final int intDownloadCounter) {
		String[] selectionArgs = new String[] { String.valueOf(intRegisterId) ,String.valueOf(intDownloadCounter)};
		Cursor cursor = activity.getContentResolver().query(
				RegisterAnswerDAO.QUERY_ALL_BY_REGISTER_ID_URI, null, null, selectionArgs, null);
		List<RegisterAnswer> lstResult = RegisterAnswerDAO
				.createObjects(cursor);
		cursor.close();

		return lstResult;
	}
	
	/**
	 * Obtener el formulario seg�n el Flux Id.
	 * 
	 * @param intFluxId
	 * @return lstResult
	 */
	public List<Form> _obtainLstFormByPK(final int intFluxId) {
		String[] selectionArgs = new String[] { String.valueOf(intFluxId) };
		Cursor cursor = activity.getContentResolver().query(
				FormDAO.QUERY_BY_FLUXID_URI, null, null, selectionArgs, null);
		List<Form> lstResult = FormDAO.createObjects(cursor);
		cursor.close();

		return lstResult;
	}
	
	/**
	 * Obtener lista de validaci�n de flujos seg�n el Flux Id.
	 * 
	 * @param intFluxId
	 * @return lstResult
	 */
	public List<FluxValidation> _obtainLstFluxValidationByFluxId(final int intFluxId) {
		String[] selectionArgs = new String[] { String.valueOf(intFluxId) };
		Cursor cursor = activity.getContentResolver().query(
				FluxValidationDAO.QUERY_BY_FLUXID_URI, null, null, selectionArgs, null);
		List<FluxValidation> lstResult = FluxValidationDAO.createObjects(cursor);
		cursor.close();

		return lstResult;
	}

	/**
	 * Obtener la ubicacion mas precisa del dispositivo
	 * 
	 * @return objeto con la informacion de la ubicacion
	 */
	protected Track _obtainCurrentLocation() {
		Cursor cursor = activity.getContentResolver().query(TrackDAO.QUERY_MOST_RECENT_URI, null,null,null,null);
		Track objTrack = TrackDAO.createObject(cursor);

		return objTrack;
	}
	
	/**
	 * Llamar a la actividad encargada de mostrar la ubicaci�n del dispositivo y
	 * de la ubicaci�n del registro.
	 */
	public void _displayLocationInMap(final int intListid) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.LOCATION_KEY_INDIVIDUAL, true);
		bundle.putInt(Constants.LOCATION_KEY_LISTINGID, intListid);
	    //activity.callActivity(LocationActivity.class.getName(), bundle);
	}
	
	protected List<Listing> _obtainRetainedListing() {
		
		String[] selectionArgs = new String[] { String.valueOf(AppPreferences.getInstance(activity)._loadUserId()) };
		Cursor cursor = activity.getContentResolver().query(
				ListingDAO.QUERY_BY_USER_ID_URI, null, null,
				selectionArgs, null);
		List<Listing> lstResult = ListingDAO.createObjects(cursor);
		cursor.close();
		return lstResult;
	}
}
