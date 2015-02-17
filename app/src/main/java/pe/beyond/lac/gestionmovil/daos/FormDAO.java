package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Listing;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class FormDAO extends BaseDAO {
	public static final String DAO_NAME = "FormDAO";
	public static final String TB_NAME = "GLS_GR_REL_FORMULARIO";
	public static final String COL_ID = "_id";
	public static final String COL_COD_MODULO = "COD_MODULO_MOD";
	public static final String COL_NUM_ORDEN = "NUM_ORDEN_REF";
	public static final String COL_FLG_VISIBLE = "FLG_VISIBLE_REF";
	public static final String COL_DES_NOMBRE = "DES_NOMBRE_REF";
	public static final String COL_NUM_LENGTH_INICIO = "NUM_LENGTH_INICIO_REF";
	public static final String COL_NUM_LENGTH_FIN = "NUM_LENGTH_FIN_REF";
	public static final String COL_FLG_BUSQUEDA = "FLG_BUSQUEDA_REF";
	public static final String COL_FLG_ORIENTACION = "FLG_ORIENTACION_REF";
	public static final String COL_FLG_ORDEN = "FLG_ORDEN_REF";
	public static final String COL_NUM_CAMPO_ORDEN = "NUM_CAMPO_ORDEN_REF";
	public static final String COL_NUM_POSIC_BUSQUEDA = "NUM_POSIC_BUSQUEDA_REF";
	public static final String COL_DES_TIPODATO_REF = "DES_TIPODATO_REF";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_MODULO, Constants.INTEGER },
			{ COL_NUM_ORDEN, Constants.INTEGER },
			{ COL_FLG_VISIBLE, Constants.INTEGER },
			{ COL_DES_NOMBRE, Constants.TEXT },
			{ COL_NUM_LENGTH_INICIO, Constants.INTEGER },
			{ COL_NUM_LENGTH_FIN, Constants.INTEGER },
			{ COL_FLG_BUSQUEDA, Constants.BOOLEAN },
			{ COL_FLG_ORIENTACION, Constants.INTEGER },
			{ COL_FLG_ORDEN, Constants.INTEGER },
			{ COL_NUM_CAMPO_ORDEN, Constants.INTEGER },
			{ COL_NUM_POSIC_BUSQUEDA, Constants.INTEGER },
			{ COL_DES_TIPODATO_REF, Constants.TEXT }};

	public static final int DAO_URI_CODE = 20;

	public static final int INSERT_URI_CODE = 2099;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2001;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2002;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_SEARCH_SELECTED_BY_MODULEID_URI_CODE = 2003;
	public static final String QUERY_SELECTED_SEARCH_BY_MODULEID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QuerySelectedSearchByModuleID";
	public static final Uri QUERY_SELECTED_SEARCH_BY_MODULEID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_SELECTED_SEARCH_BY_MODULEID_PATH);
	
	public static final int QUERY_FOR_SEARCH_OPTIONS_URI_CODE = 2004;
	public static final String QUERY_FOR_SEARCH_OPTIONS_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QuerySearchOption";
	public static final Uri QUERY_FOR_SEARCH_OPTIONS_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_FOR_SEARCH_OPTIONS_PATH);
	
	public static final int QUERY_FOR_ORDER_OPTIONS_URI_CODE = 2005;
	public static final String QUERY_FOR_ORDER_OPTIONS_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryOrderOption";
	public static final Uri QUERY_FOR_ORDER_OPTIONS_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_FOR_ORDER_OPTIONS_PATH);
		
	public static final int QUERY_BY_FLUXID_URI_CODE = 2006;
	public static final String QUERY_BY_FLUXID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByFluxID";
	public static final Uri QUERY_BY_FLUXID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_FLUXID_PATH);
	
	public static final int QUERY_FOR_LISTING_ORDER_URI_CODE = 2007;
	public static final String QUERY_FOR_LISTING_ORDER_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryForListingOrder";
	public static final Uri QUERY_FOR_LISTING_ORDER_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_FOR_LISTING_ORDER_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 2051;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);
	
	public static final int UPDATE_OLD_SEARCH_FORM_URI_CODE = 2052;
	public static final String UPDATE_OLD_SEARCH_FORM_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateOldSearchForm";
	public static final Uri UPDATE_OLD_SEARCH_FORM_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_OLD_SEARCH_FORM_PATH);
	
	public static final int UPDATE_NEW_SEARCH_FORM_URI_CODE = 2053;
	public static final String UPDATE_NEW_SEARCH_FORM_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateNewSearchForm";
	public static final Uri UPDATE_NEW_SEARCH_FORM_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_NEW_SEARCH_FORM_PATH);
	
	public static final int UPDATE_ORDER_ENTRY_DEFAULT_URI_CODE = 2054;
	public static final String UPDATE_ORDER_ENTRY_DEFAULT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateOrderEntryDefault";
	public static final Uri UPDATE_ORDER_ENTRY_DEFAULT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_ORDER_ENTRY_DEFAULT_PATH);
	
	public static final int UPDATE_ORDER_ENTRY_VALUES_URI_CODE = 2055;
	public static final String UPDATE_ORDER_ENTRY_VALUES_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateOrderEntryValues";
	public static final Uri UPDATE_ORDER_ENTRY_VALUES_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_ORDER_ENTRY_VALUES_PATH);

	
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_MODULO));
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
				QUERY_BY_FLUXID_PATH, QUERY_BY_FLUXID_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_SELECTED_SEARCH_BY_MODULEID_PATH, QUERY_SEARCH_SELECTED_BY_MODULEID_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_FOR_SEARCH_OPTIONS_PATH, QUERY_FOR_SEARCH_OPTIONS_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_FOR_ORDER_OPTIONS_PATH, QUERY_FOR_ORDER_OPTIONS_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_FOR_LISTING_ORDER_PATH, QUERY_FOR_LISTING_ORDER_URI_CODE);
		
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_OLD_SEARCH_FORM_PATH, UPDATE_OLD_SEARCH_FORM_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_NEW_SEARCH_FORM_PATH, UPDATE_NEW_SEARCH_FORM_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_ORDER_ENTRY_DEFAULT_PATH, UPDATE_ORDER_ENTRY_DEFAULT_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_ORDER_ENTRY_VALUES_PATH, UPDATE_ORDER_ENTRY_VALUES_URI_CODE);
		return DAO_URI_CODE;
	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);
		String groupBy = null;
		String having = null;

		switch (intUriCode) {
		case QUERY_SEARCH_SELECTED_BY_MODULEID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO
					+ " = ? AND " + TB_NAME + Constants.LBL_DOT + COL_FLG_BUSQUEDA +" = 2";
			break;
		case QUERY_FOR_SEARCH_OPTIONS_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND " + TB_NAME + Constants.LBL_DOT + COL_FLG_BUSQUEDA +" >= " + Constants.FLG_ENABLED + " ";
			sortOrder = " " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_ORDEN + " ASC ";
			break;
		case QUERY_FOR_ORDER_OPTIONS_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND " + TB_NAME + Constants.LBL_DOT + COL_FLG_ORDEN +" >= " + Constants.FLG_ENABLED + " ";
			sortOrder = " " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_CAMPO_ORDEN + " ASC " + 
					" , " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_ORDEN + " ASC ";
			break;	
		case QUERY_FOR_LISTING_ORDER_URI_CODE:
			selection = " " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_COD_MODULO + " = ? AND " 
				+ FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_FLG_ORDEN +" >= " + Constants.FLG_ENABLED + " AND "
				+ FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_CAMPO_ORDEN +" > 0 ";
			sortOrder = " " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_CAMPO_ORDEN + " ASC " + 
					" , " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_ORDEN + " ASC ";
			break;
		case QUERY_BY_FLUXID_URI_CODE:
			queryBuilder.setTables(" " + FormDAO.TB_NAME
					+ " INNER JOIN " + FluxDAO.TB_NAME + " ON ( " + FormDAO.TB_NAME
					+ Constants.LBL_DOT + FormDAO.COL_COD_MODULO + " = "
					+ FluxDAO.TB_NAME + Constants.LBL_DOT + FluxDAO.COL_COD_MODULO + " ) ");
			
			selection = " " + FluxDAO.TB_NAME + Constants.LBL_DOT + FluxDAO.COL_COD_FLUJO
					+ " = ? ";
			sortOrder = " " + FormDAO.TB_NAME + Constants.LBL_DOT + FormDAO.COL_NUM_ORDEN
					+ " ASC ";
			break;
		
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "QUERY");
		}
		result = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, sortOrder);
		result.moveToFirst();
		return result;
	}

	@Override
	public Uri insert(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values) {
		Uri uriInserted = null;

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
		switch (intUriCode) {
		case DELETE_ALL_URI_CODE:
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "DELETE");
		}
		result = db.delete(tableName, selection, selectionArgs);
		return result;
	}

	@Override
	public int update(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values, String selection,
			String[] selectionArgs) {
        switch (intUriCode) {
        case UPDATE_OLD_SEARCH_FORM_URI_CODE:
        	selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND "
        			+ TB_NAME + Constants.LBL_DOT + COL_FLG_BUSQUEDA + " = 2 ";
            break;
        case UPDATE_NEW_SEARCH_FORM_URI_CODE:
        	selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND "
        			+ TB_NAME + Constants.LBL_DOT + COL_NUM_ORDEN + " = ? ";
            break;
        case UPDATE_ORDER_ENTRY_DEFAULT_URI_CODE:
        	selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND "
        			+ TB_NAME + Constants.LBL_DOT + COL_FLG_ORDEN + " = 1 AND "
        			+ TB_NAME + Constants.LBL_DOT + COL_NUM_CAMPO_ORDEN + " != 0 ";
            break;
        case UPDATE_ORDER_ENTRY_VALUES_URI_CODE:
        	selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = ? AND "
        			+ TB_NAME + Constants.LBL_DOT + COL_NUM_ORDEN + " = ? ";
            break;
        default:
        	throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "UPDATE");
        }
        
        final int rowsAffected = db.update(TB_NAME, values, selection, selectionArgs);
        return rowsAffected;
	}
	
	public static ContentValues createContentValues(final Generic entity) {
		Form sonEntity = (Form) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_MODULO, sonEntity.getIntModuleId());
		values.put(COL_NUM_ORDEN, sonEntity.getIntOrder());
		values.put(COL_DES_NOMBRE, sonEntity.getStrName());
		values.put(COL_NUM_LENGTH_INICIO, sonEntity.getIntStartLength());
		values.put(COL_NUM_LENGTH_FIN, sonEntity.getIntEndLength());
		values.put(COL_FLG_BUSQUEDA, sonEntity.getIntFlgSearch());
		values.put(COL_FLG_ORIENTACION, sonEntity.isBooFlgOrientation());
		values.put(COL_FLG_ORDEN, sonEntity.isBooFlgOrdering());
		values.put(COL_NUM_CAMPO_ORDEN, sonEntity.getIntOrderEntry());
		values.put(COL_NUM_POSIC_BUSQUEDA,sonEntity.getIntSearchLocation());
		values.put(COL_DES_TIPODATO_REF,sonEntity.getStrDataType());
		return values;
	}

	public static Form createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intModuleId = cursor.getInt(cursor.getColumnIndex(COL_COD_MODULO));
		int intOrder = cursor.getInt(cursor.getColumnIndex(COL_NUM_ORDEN));
		boolean booFlgVisible = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_VISIBLE)) == 1;
		String strName = cursor
				.getString(cursor.getColumnIndex(COL_DES_NOMBRE));
		int intStartLength = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_LENGTH_INICIO));
		int intEndLength = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_LENGTH_FIN));
		int intFlgSearch = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_BUSQUEDA));
		boolean booFlgOrientation = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_ORIENTACION)) == 1;
		boolean booFlgOrdering = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_ORDEN)) == 1;
		int intOrderEntry = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_CAMPO_ORDEN));
		int intSearchLocation = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_POSIC_BUSQUEDA));
		String strDataType = cursor
				.getString(cursor.getColumnIndex(COL_DES_TIPODATO_REF));
		
		Form objRegister = new Form(intModuleId, intOrder, booFlgVisible, strName, intStartLength, intEndLength, intFlgSearch, booFlgOrientation, booFlgOrdering, intOrderEntry, intSearchLocation, strDataType);
		return objRegister;
	}

	public static List<Form> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Form> lst = new ArrayList<Form>();
		while (!cursor.isAfterLast()) {
			Form objForm = createObject(cursor);
			lst.add(objForm);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
