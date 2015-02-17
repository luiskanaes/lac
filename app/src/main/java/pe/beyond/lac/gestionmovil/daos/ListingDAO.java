package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.simpleframework.xml.Order;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Module;
import pe.beyond.lac.gestionmovil.adapters.PayloadListAdapter;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Spinner;

public class ListingDAO extends BaseDAO {
	public static final String DAO_NAME = "ListingDAO";
	public static final String TB_NAME = "GLS_GR_MAE_LISTADO";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_LISTADO = "IDE_LISTADO_LIS";
	public static final String COL_NUM_DESCARGA = "NUM_DESCARGA_LIS";
	public static final String COL_COD_FLUJO = "COD_FLUJO_RFL";
	public static final String COL_COD_GESTIONCOMERCIAL = "COD_GESTIONCOMERCIAL_LIS";
	public static final String COL_FEC_FECHA_INICIO = "FEC_FECHA_INICIO_LIS";
	public static final String COL_FEC_FECHA_FIN = "FEC_FECHA_FIN_LIS";
	public static final String COL_IDE_USUARIO = "IDE_USUARIO_USU";
	public static final String COL_CAN_GPS_CATASTRO_LATITUD = "CAN_GPS_CATASTRO_LATITUD_LIS";
	public static final String COL_CAN_GPS_CATASTRO_LONGITUD = "CAN_GPS_CATASTRO_LONGITUD_LIS";
	public static final String COL_NUM_ORDEN_LISTADO = "NUM_ORDEN_LISTADO_LIS";
	public static final String COL_NUM_NRO_VISITA = "NUM_NRO_VISITA_LIS";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_DES_CAMPO01 = "DES_CAMPO1_LIS";
	public static final String COL_DES_CAMPO02 = "DES_CAMPO2_LIS";
	public static final String COL_DES_CAMPO03 = "DES_CAMPO3_LIS";
	public static final String COL_DES_CAMPO04 = "DES_CAMPO4_LIS";
	public static final String COL_DES_CAMPO05 = "DES_CAMPO5_LIS";
	public static final String COL_DES_CAMPO06 = "DES_CAMPO6_LIS";
	public static final String COL_DES_CAMPO07 = "DES_CAMPO7_LIS";
	public static final String COL_DES_CAMPO08 = "DES_CAMPO8_LIS";
	public static final String COL_DES_CAMPO09 = "DES_CAMPO9_LIS";
	public static final String COL_DES_CAMPO10 = "DES_CAMPO10_LIS";
	public static final String COL_DES_VALIDACION01 = "DES_VALIDACION01_LIS";
	public static final String COL_DES_VALIDACION02 = "DES_VALIDACION02_LIS";
	public static final String COL_DES_VALIDACION03 = "DES_VALIDACION03_LIS";
	public static final String COL_DES_VALIDACION04 = "DES_VALIDACION04_LIS";
	public static final String COL_DES_VALIDACION05 = "DES_VALIDACION05_LIS";
	public static final String COL_DES_VALIDACION06 = "DES_VALIDACION06_LIS";
	public static final String COL_DES_VALIDACION07 = "DES_VALIDACION07_LIS";
	public static final String COL_DES_VALIDACION08 = "DES_VALIDACION08_LIS";
	public static final String COL_DES_VALIDACION09 = "DES_VALIDACION09_LIS";
	public static final String COL_DES_VALIDACION10 = "DES_VALIDACION10_LIS";
	public static final String COL_DES_VALIDACION11 = "DES_VALIDACION11_LIS";
	public static final String COL_DES_VALIDACION12 = "DES_VALIDACION12_LIS";
	public static final String COL_DES_VALIDACION13 = "DES_VALIDACION13_LIS";
	public static final String COL_DES_VALIDACION14 = "DES_VALIDACION14_LIS";
	public static final String COL_DES_VALIDACION15 = "DES_VALIDACION15_LIS";
	public static final String COL_DES_VALIDACION16 = "DES_VALIDACION16_LIS";
	public static final String COL_DES_VALIDACION17 = "DES_VALIDACION17_LIS";
	public static final String COL_DES_VALIDACION18 = "DES_VALIDACION18_LIS";
	public static final String COL_DES_VALIDACION19 = "DES_VALIDACION19_LIS";
	public static final String COL_DES_VALIDACION20 = "DES_VALIDACION20_LIS";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_LISTADO, Constants.INTEGER },
			{ COL_NUM_DESCARGA, Constants.INTEGER },
			{ COL_COD_FLUJO, Constants.INTEGER },
			{ COL_COD_GESTIONCOMERCIAL, Constants.TEXT },
			{ COL_FEC_FECHA_INICIO, Constants.LONG },
			{ COL_FEC_FECHA_FIN, Constants.LONG },
			{ COL_IDE_USUARIO, Constants.INTEGER },
			{ COL_CAN_GPS_CATASTRO_LATITUD, Constants.REAL },
			{ COL_CAN_GPS_CATASTRO_LONGITUD, Constants.REAL },
			{ COL_NUM_ORDEN_LISTADO, Constants.INTEGER },
			{ COL_NUM_NRO_VISITA, Constants.INTEGER },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_DES_CAMPO01, Constants.TEXT },
			{ COL_DES_CAMPO02, Constants.TEXT },
			{ COL_DES_CAMPO03, Constants.TEXT },
			{ COL_DES_CAMPO04, Constants.TEXT },
			{ COL_DES_CAMPO05, Constants.TEXT },
			{ COL_DES_CAMPO06, Constants.TEXT },
			{ COL_DES_CAMPO07, Constants.TEXT },
			{ COL_DES_CAMPO08, Constants.TEXT },
			{ COL_DES_CAMPO09, Constants.TEXT },
			{ COL_DES_CAMPO10, Constants.TEXT },
			{ COL_DES_VALIDACION01, Constants.TEXT },
			{ COL_DES_VALIDACION02, Constants.TEXT },
			{ COL_DES_VALIDACION03, Constants.TEXT },
			{ COL_DES_VALIDACION04, Constants.TEXT },
			{ COL_DES_VALIDACION05, Constants.TEXT },
			{ COL_DES_VALIDACION06, Constants.TEXT },
			{ COL_DES_VALIDACION07, Constants.TEXT },
			{ COL_DES_VALIDACION08, Constants.TEXT },
			{ COL_DES_VALIDACION09, Constants.TEXT },
			{ COL_DES_VALIDACION10, Constants.TEXT },
			{ COL_DES_VALIDACION11, Constants.TEXT },
			{ COL_DES_VALIDACION12, Constants.TEXT },
			{ COL_DES_VALIDACION13, Constants.TEXT },
			{ COL_DES_VALIDACION14, Constants.TEXT },
			{ COL_DES_VALIDACION15, Constants.TEXT },
			{ COL_DES_VALIDACION16, Constants.TEXT },
			{ COL_DES_VALIDACION17, Constants.TEXT },
			{ COL_DES_VALIDACION18, Constants.TEXT },
			{ COL_DES_VALIDACION19, Constants.TEXT },
			{ COL_DES_VALIDACION20, Constants.TEXT } };

	public static final int DAO_URI_CODE = 19;

	public static final int INSERT_URI_CODE = 1999;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1901;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1902;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_NEAREST_URI_CODE = 1903;
	public static final String QUERY_NEAREST_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryNearest";
	public static final Uri QUERY_NEAREST_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_NEAREST_PATH);

	public static final int QUERY_QR_URI_CODE = 1904;
	public static final String QUERY_QR_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryQR";
	public static final Uri QUERY_QR_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_QR_PATH);

	public static final int QUERY_ALL_LIST_URI_CODE = 1905;
	public static final String QUERY_ALL_LIST_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryAllList";
	public static final Uri QUERY_ALL_LIST_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_LIST_PATH);

	public static final int QUERY_ID_SUGGESTED_URI_CODE = 1906;
	public static final String QUERY_ID_SUGGESTED_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QuerySuggested";
	public static final Uri QUERY_ID_SUGGESTED_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ID_SUGGESTED_PATH);

	public static final int QUERY_BY_FIELD_URI_CODE = 1907;
	public static final String QUERY_BY_FIELD_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByField";
	public static final Uri QUERY_BY_FIELD_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_FIELD_PATH);

	public static final int QUERY_FOR_FILTER_OPTIONS_URI_CODE = 1908;
	public static final String QUERY_FOR_FILTER_OPTIONS_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryForFilterOptions";
	public static final Uri QUERY_FOR_FILTER_OPTIONS_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_FOR_FILTER_OPTIONS_PATH);

	public static final int QUERY_USER_LIST_GROUPBY_STATE_URI_CODE = 1909;
	public static final String QUERY_USER_LIST_GROUPBY_STATE_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryUserListGroupByState";
	public static final Uri QUERY_USER_LIST_GROUPBY_STATE_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_USER_LIST_GROUPBY_STATE_PATH);

	public static final int QUERY_BY_USER_ID_URI_CODE = 1910;
	public static final String QUERY_BY_USER_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByUserId";
	public static final Uri QUERY_BY_USER_ID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_USER_ID_PATH);

	public static final int DELETE_ALL_URI_CODE = 1951;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	public static final int DELETE_ONE_URI_CODE = 1952;
	public static final String DELETE_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteOne";
	public static final Uri DELETE_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ONE_PATH);

	public static final int DELETE_PENDING_URI_CODE = 1953;
	public static final String DELETE_PENDING_PATH = TB_NAME
			+ Constants.LBL_SLASH + "DeletePending";
	public static final Uri DELETE_PENDING_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_PENDING_PATH);

	public static final int QUERY_SUGGEST_CODE = 1954;
	public static final String QUERY_SUGGEST_PATH = "search_suggest_query";
	public static final Uri QUERY_SUGGEST_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_SUGGEST_PATH);

	public static final int UPDATE_WORKED_URI_CODE = 1955;
	public static final String UPDATE_WORKED_PATH = TB_NAME
			+ Constants.LBL_SLASH + "UpdateWorked";
	public static final Uri UPDATE_WORKED_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_WORKED_PATH);

	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_IDE_LISTADO));
		return true;
	}

	@Override
	public boolean onUpgrade(final SQLiteDatabase db) {
		db.execSQL(dropTable(TB_NAME));
		onCreate(db);
		return true;
	}

	@Override
	public int loadDaoUris(final UriMatcher uriMatcher) {
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				INSERT_PATH, INSERT_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ONE_PATH, QUERY_ONE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ALL_PATH, QUERY_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_NEAREST_PATH, QUERY_NEAREST_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_QR_PATH, QUERY_QR_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ALL_LIST_PATH, QUERY_ALL_LIST_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ID_SUGGESTED_PATH, QUERY_ID_SUGGESTED_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_BY_FIELD_PATH, QUERY_BY_FIELD_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_FOR_FILTER_OPTIONS_PATH,
				QUERY_FOR_FILTER_OPTIONS_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_USER_LIST_GROUPBY_STATE_PATH,
				QUERY_USER_LIST_GROUPBY_STATE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_BY_USER_ID_PATH, QUERY_BY_USER_ID_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_WORKED_PATH, UPDATE_WORKED_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ONE_PATH, DELETE_ONE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_PENDING_PATH, DELETE_PENDING_URI_CODE);

		// URI que se encarga de manejar las sugerencias de la opción del search
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_SUGGEST_PATH, QUERY_SUGGEST_CODE);

		return DAO_URI_CODE;

	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);
		Uri notificationUri = QUERY_ALL_URI;

		String groupBy = null;
		String having = null;

		AppPreferences appPreferences = AppPreferences.getInstance(context);
		String strUserId = String.valueOf(appPreferences._loadUserId());
		String strModuleId = String.valueOf(appPreferences
				._loadCurrentModuleId());
		String strTimeInMillis = String.valueOf(Calendar.getInstance()
				.getTimeInMillis());
		int intFilterId = AppPreferences.getInstance(context)
				._loadListingCurrentFilter();

		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " ASC ";
			break;
		case QUERY_NEAREST_URI_CODE:
			projection = new String[] { TB_NAME + Constants.LBL_DOT + COL_ID,
					TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO, };

			selection = TB_NAME + Constants.LBL_DOT + COL_COD_FLUJO + " = 1";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " ASC ";
			break;
		case QUERY_ALL_LIST_URI_CODE:

			String tmpQuerySelect = (selection == null) ? Constants.LBL_EMPTY : " AND "+selection;
			
			queryBuilder.setTables(" " + ListingDAO.TB_NAME + " INNER JOIN "
					+ FluxDAO.TB_NAME + " ON ( " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_FLUJO + " ) ");
			selection = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_IDE_USUARIO + " = ? AND "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_MODULO + " = ? AND " + " ? BETWEEN "
					+ ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_FEC_FECHA_INICIO + " AND "
					+ ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_FEC_FECHA_FIN
					+ tmpQuerySelect;
			
			selectionArgs = new String[] { strUserId, strModuleId,
					strTimeInMillis };

			// filtro del COL_COD_ESTADO_EST
			if (intFilterId != Constants.LIST_TODOS) {
				String strFilter = String.valueOf(intFilterId);
				selectionArgs = (String[]) ArrayUtils.add(selectionArgs,
						strFilter);
				selection += " AND " + ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_ESTADO + " = ? ";

			}
			break;
		case QUERY_SUGGEST_CODE:
			String strFieldSuggestion = ListingDAO.TB_NAME + Constants.LBL_DOT
					+ PayloadListAdapter.strField;
			projection = new String[] {
					ListingDAO.TB_NAME + Constants.LBL_DOT + COL_ID,
					strFieldSuggestion + " AS "
							+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
					strFieldSuggestion + " AS "
							+ SearchManager.SUGGEST_COLUMN_TEXT_1 };
			queryBuilder.setTables(" " + ListingDAO.TB_NAME + " INNER JOIN "
					+ FluxDAO.TB_NAME + " ON ( " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_FLUJO + " ) ");

			Cursor cursorModule = context.getContentResolver().query(
					ModuleDAO.QUERY_ONE_URI, null, null,
					new String[] { strModuleId }, null);
			Module objModule = ModuleDAO.createObject(cursorModule);
			cursorModule.close();
			int intSearchRestriction = objModule.getIntSearchQuantity();

			int intNumChars = selectionArgs[0].length();
			if (intNumChars >= intSearchRestriction) {
				Cursor cursorForm = context.getContentResolver().query(
						FormDAO.QUERY_SELECTED_SEARCH_BY_MODULEID_URI, null,
						null, new String[] { strModuleId }, null);
				int intSearchLocation = cursorForm.getInt(cursorForm
						.getColumnIndex(FormDAO.COL_NUM_POSIC_BUSQUEDA));
				cursorForm.close();

				selection = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_USUARIO + " = ? AND "
						+ FluxDAO.TB_NAME + Constants.LBL_DOT
						+ FluxDAO.COL_COD_MODULO + " = ? AND "
						+ ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_FEC_FECHA_INICIO + " <= ? AND "
						+ ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_FEC_FECHA_FIN + " >= ? ";

				String strSearched = selectionArgs[0];
				selectionArgs = new String[] { strUserId, strModuleId,
						strTimeInMillis, strTimeInMillis };

				// filtro del COL_COD_ESTADO_EST
				if (intFilterId != Constants.LIST_TODOS) {
					String strFilter = String.valueOf(intFilterId);
					selectionArgs = (String[]) ArrayUtils.add(selectionArgs,
							strFilter);
					selection += " AND " + ListingDAO.TB_NAME
							+ Constants.LBL_DOT + ListingDAO.COL_COD_ESTADO
							+ " = ? ";

				}

				selection += " AND " + strFieldSuggestion + " LIKE ? ";
				String strSpaces = "";
				for (int intSpace = 0; intSpace < intSearchLocation; intSpace++) {
					strSpaces += "_";
				}
				selectionArgs = (String[]) ArrayUtils.add(selectionArgs,
						strSpaces + strSearched + "%");
			} else {
				if (selectionArgs[0] == "") {
					selection = " !='' ";
				} else {
					selection = " !='' AND FALSE ";
				}
			}
			break;
		case QUERY_ID_SUGGESTED_URI_CODE:
			queryBuilder.setTables(" " + ListingDAO.TB_NAME + " INNER JOIN "
					+ FluxDAO.TB_NAME + " ON ( " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_FLUJO + " ) ");

			projection = new String[] { TB_NAME + Constants.LBL_DOT
					+ COL_IDE_LISTADO };
			selection = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ PayloadListAdapter.strField + " = ? AND "
					+ ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_IDE_USUARIO + " = ? AND "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_MODULO + " = ? ";

			selectionArgs = (String[]) ArrayUtils.addAll(selectionArgs,
					new String[] { strUserId, strModuleId });

			// filtro del COL_COD_ESTADO_EST
			if (intFilterId != Constants.LIST_TODOS) {
				String strFilter = String.valueOf(intFilterId);
				selectionArgs = (String[]) ArrayUtils.add(selectionArgs,
						strFilter);
				selection += " AND " + ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_ESTADO + " = ? ";
			}
			//
			break;
		case QUERY_QR_URI_CODE:
			projection = new String[] { TB_NAME + Constants.LBL_DOT + COL_ID,
					TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO };

			String strFieldQR = PayloadListAdapter.strField;

			queryBuilder.setTables(" " + ListingDAO.TB_NAME + " INNER JOIN "
					+ FluxDAO.TB_NAME + " ON ( " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_FLUJO + " ) ");

			selection = " " + TB_NAME + Constants.LBL_DOT + strFieldQR
					+ " = ? AND " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_IDE_USUARIO + " = ? AND "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_MODULO + " = ? AND " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_FEC_FECHA_INICIO
					+ " <= ? AND " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_FEC_FECHA_FIN + " >= ? ";

			selectionArgs = (String[]) ArrayUtils.addAll(selectionArgs,
					new String[] { strUserId, strModuleId, strTimeInMillis,
							strTimeInMillis });

			// filtro del COL_COD_ESTADO_EST
			if (intFilterId != Constants.LIST_TODOS) {
				String strFilter = String.valueOf(intFilterId);
				selectionArgs = (String[]) ArrayUtils.add(selectionArgs,
						strFilter);
				selection += " AND " + ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_COD_ESTADO + " = ? ";
			}
			//

			break;
		case QUERY_BY_FIELD_URI_CODE:
			break;
		case QUERY_FOR_FILTER_OPTIONS_URI_CODE:
			queryBuilder.setTables(" " + ListingDAO.TB_NAME + " INNER JOIN "
					+ FluxDAO.TB_NAME + " ON ( " + ListingDAO.TB_NAME
					+ Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_FLUJO + " ) ");

			projection = new String[] {
					" DISTINCT ( " + TB_NAME + Constants.LBL_DOT
							+ COL_COD_ESTADO + " ) AS " + COL_COD_ESTADO,
					TB_NAME + Constants.LBL_DOT + COL_ID };
			selection = " " + FluxDAO.TB_NAME + Constants.LBL_DOT
					+ FluxDAO.COL_COD_MODULO + " = ? ";
			groupBy = " " + COL_COD_ESTADO + " ";
			break;

		case QUERY_USER_LIST_GROUPBY_STATE_URI_CODE:
			/*
			 * queryBuilder.setTables(ListingDAO.TB_NAME);
			 * 
			 * projection = new String[] { " COUNT(" + ListingDAO.TB_NAME +
			 * Constants.LBL_DOT + COL_COD_ESTADO + ") AS " +
			 * Constants.COLUMN_NAME_HOWMUCH + " ", " " + ListingDAO.TB_NAME +
			 * Constants.LBL_DOT + COL_COD_ESTADO + " AS " +
			 * Constants.COLUMN_NAME_STATE + " " };
			 * 
			 * selection = " " + ListingDAO.TB_NAME + Constants.LBL_DOT +
			 * COL_IDE_USUARIO + " = " + appPreferences._loadUserId() + " ";
			 * groupBy = " " + ListingDAO.TB_NAME + Constants.LBL_DOT +
			 * COL_COD_ESTADO + " "; sortOrder = " COUNT(" + ListingDAO.TB_NAME
			 * + Constants.LBL_DOT + COL_COD_ESTADO + ") ASC ";
			 */

			// =============================================================================================
			// =============================================================================================
			queryBuilder.setTables(" " + ListingDAO.TB_NAME
					+ Constants.LBL_COMMA + ModuleDAO.TB_NAME
					+ Constants.LBL_COMMA + FluxDAO.TB_NAME + " ");

			projection = new String[] {
					" COUNT(" + ListingDAO.TB_NAME + Constants.LBL_DOT
							+ COL_COD_ESTADO + ") AS "
							+ Constants.COLUMN_NAME_HOWMUCH + " ",
					" " + ListingDAO.TB_NAME + Constants.LBL_DOT
							+ COL_COD_ESTADO + " AS "
							+ Constants.COLUMN_NAME_STATE + " " };

			selection = " " + ModuleDAO.TB_NAME + Constants.LBL_DOT
					+ ModuleDAO.COL_COD_MODULO + " = " + FluxDAO.TB_NAME
					+ Constants.LBL_DOT + FluxDAO.COL_COD_MODULO + " AND " +

					FluxDAO.TB_NAME + Constants.LBL_DOT + FluxDAO.COL_COD_FLUJO
					+ " = " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ COL_COD_FLUJO + " AND " +

					ListingDAO.TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO
					+ " = " + appPreferences._loadUserId() + " AND " +

					ModuleDAO.TB_NAME + Constants.LBL_DOT
					+ ModuleDAO.COL_COD_MODULO + " = ? AND " +
					/*
					" DATE(CURRENT_TIMESTAMP) >= DATE( " + TB_NAME+ Constants.LBL_DOT + COL_FEC_FECHA_INICIO+ " / 1000,'unixepoch') AND "+
					" DATE(CURRENT_TIMESTAMP) <= DATE( " + TB_NAME+ Constants.LBL_DOT + COL_FEC_FECHA_FIN	+ " / 1000,'unixepoch') "
					*/
					
			 		" DATETIME("+strTimeInMillis+"/1000, 'unixepoch')"+ "BETWEEN " + "DATETIME(" + ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_FEC_FECHA_INICIO +"/1000, 'unixepoch')"+ " AND " + 
			 		"DATETIME(" +ListingDAO.TB_NAME + Constants.LBL_DOT+ ListingDAO.COL_FEC_FECHA_FIN+"/1000, 'unixepoch')";
					
			;

			groupBy = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ COL_COD_ESTADO + " ";

			sortOrder = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ COL_COD_ESTADO + " ASC ";

			break;
		case QUERY_BY_USER_ID_URI_CODE:
			selection = " " + ListingDAO.TB_NAME + Constants.LBL_DOT
					+ ListingDAO.COL_IDE_USUARIO + " = ? ";
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "QUERY");
		}
		result = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, sortOrder);
		result.moveToFirst();
		result.setNotificationUri(context.getContentResolver(), notificationUri);
		return result;
	}

	@Override
	public Uri insert(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values) {
		Uri uriInserted = QUERY_ALL_URI;

		switch (intUriCode) {
		case INSERT_URI_CODE:
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "INSERT");
		}
		long lngResultId = db.insert(TB_NAME, null, values);

		if (lngResultId > -1) {
			uriInserted = ContentUris.withAppendedId(uri, lngResultId);
			context.getContentResolver().notifyChange(uriInserted, null);
		} else {
			uriInserted = ContentUris.withAppendedId(uri, 0);
		}
		return uriInserted;
	}

	@Override
	public int delete(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String selection, String[] selectionArgs) {
		int result = -1;
		String tableName = TB_NAME;
		Uri notificationUri = QUERY_ALL_URI;
		switch (intUriCode) {
		case DELETE_ALL_URI_CODE:
			break;
		case DELETE_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? ";
			break;
		case DELETE_PENDING_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO
					+ " = ? AND " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_ESTADO + " = ? ";
			break;
		default:

			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "DELETE");
		}
		result = db.delete(tableName, selection, selectionArgs);
		if (result > 0) {
			context.getContentResolver().notifyChange(notificationUri, null);
		}
		return result;
	}

	@Override
	public int update(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values, String selection,
			String[] selectionArgs) {
		Uri notificationUri = null;

		switch (intUriCode) {
		case UPDATE_WORKED_URI_CODE:
			notificationUri = QUERY_ALL_URI;
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? ";
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "UPDATE");
		}

		final int rowsAffected = db.update(TB_NAME, values, selection,
				selectionArgs);
		if (rowsAffected > 0) {
			context.getContentResolver().notifyChange(notificationUri, null);
		}

		//

		AppPreferences appPreferences = AppPreferences.getInstance(context);
		String strModuleId = String.valueOf(appPreferences
				._loadCurrentModuleId());

		/*
		 * 
		 * int intTotal = 0; int intAfectar = 0;
		 * 
		 * Cursor curTotal =
		 * db.rawQuery("SELECT COUNT(1) AS TOTAL FROM GLS_GR_MAE_LISTADO AND ",
		 * null); if (curTotal.getCount() > 0) { curTotal.moveToFirst();
		 * intTotal = curTotal.getInt(0); Log.v("CURTOTAL = ", intTotal+" <"); }
		 * curTotal.close();
		 * 
		 * Cursor curVal = db.rawQuery(
		 * "SELECT COUNT(1) FROM GLS_GR_MAE_LISTADO WHERE COD_ESTADO_EST = 3",
		 * null); if (curVal.getCount() > 0) { curVal.moveToFirst(); intAfectar
		 * = curVal.getInt(0); Log.v("CURVALOR= ", intAfectar+" <"); }
		 * curVal.close();
		 * 
		 * if (intAfectar == 1) {
		 * context.getApplicationContext().sendBroadcast(new
		 * Intent("pe.gmd.lac.gestionmovil.cast.SET_START_OF_ACTIVITY"));
		 * 
		 * } else if(intAfectar == 3) {
		 * context.getApplicationContext().sendBroadcast(new
		 * Intent("pe.gmd.lac.gestionmovil.cast.SET_END_OF_ACTIVITY")); }
		 */
		return rowsAffected;
	}

	public static ContentValues createContentValues(final Generic entity) {
		Listing sonEntity = (Listing) entity;
		ContentValues values = new ContentValues();
		values.put(COL_IDE_LISTADO, sonEntity.getIntListId());
		values.put(COL_NUM_DESCARGA, sonEntity.getIntDownloadCounter());
		values.put(COL_COD_FLUJO, sonEntity.getIntFluxId());
		values.put(COL_COD_GESTIONCOMERCIAL,
				sonEntity.getStrCommercialManagmentCode());
		values.put(COL_FEC_FECHA_INICIO, sonEntity.getLngStartDate());
		values.put(COL_FEC_FECHA_FIN, sonEntity.getLngEndDate());
		values.put(COL_IDE_USUARIO, sonEntity.getIntUserId());
		values.put(COL_CAN_GPS_CATASTRO_LATITUD, sonEntity.getFltLatitude());
		values.put(COL_CAN_GPS_CATASTRO_LONGITUD, sonEntity.getFltLongitude());
		values.put(COL_NUM_ORDEN_LISTADO, sonEntity.getIntOrderNumber());
		values.put(COL_NUM_NRO_VISITA, sonEntity.getIntVisitNumber());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_DES_CAMPO01,
				(sonEntity.getStrField1() != null) ? sonEntity.getStrField1()
						: "");
		values.put(COL_DES_CAMPO02,
				(sonEntity.getStrField2() != null) ? sonEntity.getStrField2()
						: "");
		values.put(COL_DES_CAMPO03,
				(sonEntity.getStrField3() != null) ? sonEntity.getStrField3()
						: "");
		values.put(COL_DES_CAMPO04,
				(sonEntity.getStrField4() != null) ? sonEntity.getStrField4()
						: "");
		values.put(COL_DES_CAMPO05,
				(sonEntity.getStrField5() != null) ? sonEntity.getStrField5()
						: "");
		values.put(COL_DES_CAMPO06,
				(sonEntity.getStrField6() != null) ? sonEntity.getStrField6()
						: "");
		values.put(COL_DES_CAMPO07,
				(sonEntity.getStrField7() != null) ? sonEntity.getStrField7()
						: "");
		values.put(COL_DES_CAMPO08,
				(sonEntity.getStrField8() != null) ? sonEntity.getStrField8()
						: "");
		values.put(COL_DES_CAMPO09,
				(sonEntity.getStrField9() != null) ? sonEntity.getStrField9()
						: "");
		values.put(COL_DES_CAMPO10,
				(sonEntity.getStrField10() != null) ? sonEntity.getStrField10()
						: "");
		values.put(
				COL_DES_VALIDACION01,
				(sonEntity.getStrValidation1() != null) ? sonEntity
						.getStrValidation1() : "");
		values.put(
				COL_DES_VALIDACION02,
				(sonEntity.getStrValidation2() != null) ? sonEntity
						.getStrValidation2() : "");
		values.put(
				COL_DES_VALIDACION03,
				(sonEntity.getStrValidation3() != null) ? sonEntity
						.getStrValidation3() : "");
		values.put(
				COL_DES_VALIDACION04,
				(sonEntity.getStrValidation4() != null) ? sonEntity
						.getStrValidation4() : "");
		values.put(
				COL_DES_VALIDACION05,
				(sonEntity.getStrValidation5() != null) ? sonEntity
						.getStrValidation5() : "");
		values.put(
				COL_DES_VALIDACION06,
				(sonEntity.getStrValidation6() != null) ? sonEntity
						.getStrValidation6() : "");
		values.put(
				COL_DES_VALIDACION07,
				(sonEntity.getStrValidation7() != null) ? sonEntity
						.getStrValidation7() : "");
		values.put(
				COL_DES_VALIDACION08,
				(sonEntity.getStrValidation8() != null) ? sonEntity
						.getStrValidation8() : "");
		values.put(
				COL_DES_VALIDACION09,
				(sonEntity.getStrValidation9() != null) ? sonEntity
						.getStrValidation9() : "");
		values.put(
				COL_DES_VALIDACION10,
				(sonEntity.getStrValidation10() != null) ? sonEntity
						.getStrValidation10() : "");
		values.put(
				COL_DES_VALIDACION11,
				(sonEntity.getStrValidation11() != null) ? sonEntity
						.getStrValidation11() : "");
		values.put(
				COL_DES_VALIDACION12,
				(sonEntity.getStrValidation12() != null) ? sonEntity
						.getStrValidation12() : "");
		values.put(
				COL_DES_VALIDACION13,
				(sonEntity.getStrValidation13() != null) ? sonEntity
						.getStrValidation13() : "");
		values.put(
				COL_DES_VALIDACION14,
				(sonEntity.getStrValidation14() != null) ? sonEntity
						.getStrValidation14() : "");
		values.put(
				COL_DES_VALIDACION15,
				(sonEntity.getStrValidation15() != null) ? sonEntity
						.getStrValidation15() : "");
		values.put(
				COL_DES_VALIDACION16,
				(sonEntity.getStrValidation16() != null) ? sonEntity
						.getStrValidation16() : "");
		values.put(
				COL_DES_VALIDACION17,
				(sonEntity.getStrValidation17() != null) ? sonEntity
						.getStrValidation17() : "");
		values.put(
				COL_DES_VALIDACION18,
				(sonEntity.getStrValidation18() != null) ? sonEntity
						.getStrValidation18() : "");
		values.put(
				COL_DES_VALIDACION19,
				(sonEntity.getStrValidation19() != null) ? sonEntity
						.getStrValidation19() : "");
		values.put(
				COL_DES_VALIDACION20,
				(sonEntity.getStrValidation20() != null) ? sonEntity
						.getStrValidation20() : "");
		return values;
	}

	public static Listing createObject(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		int intListId = cursor.getInt(cursor.getColumnIndex(COL_IDE_LISTADO));
		int intDownloadCounter = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_DESCARGA));
		String strCommercialManagmentCode = cursor.getString(cursor
				.getColumnIndex(COL_COD_GESTIONCOMERCIAL));
		int intFluxId = cursor.getInt(cursor.getColumnIndex(COL_COD_FLUJO));
		long lngStartDate = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_FECHA_INICIO));
		long lngEndDate = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_FECHA_FIN));
		int intUserId = cursor.getInt(cursor.getColumnIndex(COL_IDE_USUARIO));
		float fltLatitude = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_CATASTRO_LATITUD));
		float fltLongitude = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_CATASTRO_LONGITUD));
		int intOrderNumber = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_ORDEN_LISTADO));
		int intVisitNumber = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_NRO_VISITA));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		String strField1 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO01));
		String strField2 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO02));
		String strField3 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO03));
		String strField4 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO04));
		String strField5 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO05));
		String strField6 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO06));
		String strField7 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO07));
		String strField8 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO08));
		String strField9 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO09));
		String strField10 = cursor.getString(cursor
				.getColumnIndex(COL_DES_CAMPO10));
		String strValidation1 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION01));
		String strValidation2 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION02));
		String strValidation3 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION03));
		String strValidation4 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION04));
		String strValidation5 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION05));
		String strValidation6 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION06));
		String strValidation7 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION07));
		String strValidation8 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION08));
		String strValidation9 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION09));
		String strValidation10 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION10));
		String strValidation11 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION11));
		String strValidation12 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION12));
		String strValidation13 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION13));
		String strValidation14 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION14));
		String strValidation15 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION15));
		String strValidation16 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION16));
		String strValidation17 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION17));
		String strValidation18 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION18));
		String strValidation19 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION19));
		String strValidation20 = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALIDACION20));

		Listing objRegister = new Listing(intListId, intDownloadCounter,
				strCommercialManagmentCode, intFluxId, lngStartDate,
				lngEndDate, intUserId, fltLatitude, fltLongitude,
				intOrderNumber, intVisitNumber, intState, strField1, strField2,
				strField3, strField4, strField5, strField6, strField7,
				strField8, strField9, strField10, strValidation1,
				strValidation2, strValidation3, strValidation4, strValidation5,
				strValidation6, strValidation7, strValidation8, strValidation9,
				strValidation10, strValidation11, strValidation12,
				strValidation13, strValidation14, strValidation15,
				strValidation16, strValidation17, strValidation18,
				strValidation19, strValidation20);
		return objRegister;
	}

	public static List<Listing> createObjects(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		List<Listing> lst = new ArrayList<Listing>();
		while (!cursor.isAfterLast()) {
			Listing objForm = createObject(cursor);
			lst.add(objForm);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
