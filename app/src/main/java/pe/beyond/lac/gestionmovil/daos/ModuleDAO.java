package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Module;
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
import android.widget.ExpandableListAdapter;

public class ModuleDAO extends BaseDAO {
	public static final String DAO_NAME = "ModuleDAO";
	public static final String TB_NAME = "GLS_GR_MAE_MODULO";
	public static final String COL_ID = "_id";
	public static final String COL_COD_MODULO = "COD_MODULO_MOD";
	public static final String COL_DES_MODULO = "DES_MODULO_MOD";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_COD_ACRONIMO = "COD_ACRONIMO_MOD";
	public static final String COL_CAN_BUSQUEDA = "CAN_BUSQUEDA_MOD";
	public static final String COL_FLG_BUSQUEDA = "FLG_BUSQUEDA_MOD";
	public static final String COL_FLG_QR = "FLG_QR_MOD";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_MODULO, Constants.INTEGER },
			{ COL_DES_MODULO, Constants.TEXT },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_COD_ACRONIMO, Constants.TEXT },
			{ COL_CAN_BUSQUEDA, Constants.INTEGER },
			{ COL_FLG_BUSQUEDA, Constants.BOOLEAN },
			{ COL_FLG_QR, Constants.INTEGER } };

	public static final int DAO_URI_CODE = 21;
	
	public static final int INSERT_URI_CODE = 2199;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH + "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2101;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH + "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2102;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH + "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int DELETE_ALL_URI_CODE = 2151;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH + "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

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
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		
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
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_MODULO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_MODULO + " ASC ";
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
			int intUriCode, ContentValues values, String strWhereClause,
			String[] strWhereArgs) {
		int result = -1;
		return result;
	}

	public static ContentValues createContentValues(final Generic entity) {
		Module sonEntity = (Module) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_MODULO, sonEntity.getIntModuleId());
		values.put(COL_DES_MODULO, sonEntity.getStrDescription());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_COD_ACRONIMO, sonEntity.getStrAcronym());
		values.put(COL_CAN_BUSQUEDA, sonEntity.getIntSearchQuantity());
		values.put(COL_FLG_BUSQUEDA, sonEntity.isBooSearch());
		values.put(COL_FLG_QR, sonEntity.getIntQR());
		return values;
	}
	
	public static Module createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intModuleId = cursor.getInt(cursor.getColumnIndex(COL_COD_MODULO));
		String strDescription = cursor.getString(cursor
				.getColumnIndex(COL_DES_MODULO));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		String strAcronym = cursor.getString(cursor
				.getColumnIndex(COL_COD_ACRONIMO));
		int intSearchQuantity = cursor.getInt(cursor
				.getColumnIndex(COL_CAN_BUSQUEDA));
		boolean booSearch = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_BUSQUEDA)) == 1;
		int intQR = cursor.getInt(cursor.getColumnIndex(COL_FLG_QR));

		Module objModule = new Module(intModuleId, strDescription, intState,
				strAcronym, intSearchQuantity, booSearch, intQR);

		return objModule;
	}

	public static List<Module> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();	
		List<Module> lst = new ArrayList<Module>();	
		while(!cursor.isAfterLast()){
			Module obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
