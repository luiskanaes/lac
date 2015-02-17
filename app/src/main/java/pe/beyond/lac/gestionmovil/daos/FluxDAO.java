package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Flux;
import pe.beyond.gls.model.Generic;
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

public class FluxDAO extends BaseDAO {
	public static final String DAO_NAME = "FluxDAO";
	public static final String TB_NAME = "GLS_GR_REL_FLUJO";
	public static final String COL_ID = "_id";
	public static final String COL_COD_FLUJO = "COD_FLUJO_RFL";
	public static final String COL_DES_FLUJO = "DES_FLUJO_RFL";
	public static final String COL_COD_MODULO = "COD_MODULO_MOD";
	public static final String COL_COD_CONFIGURACION = "COD_CONFIGURACION_CFO";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_FEC_HORA_INI = "FEC_HORA_INI_RFL";
	public static final String COL_FEC_HORA_FIN = "FEC_HORA_FIN_RFL";
	public static final String COL_FLG_PROCESO = "FLG_PROCESO_RFL";
	
	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_FLUJO, Constants.INTEGER },
			{ COL_DES_FLUJO, Constants.TEXT },
			{ COL_COD_MODULO, Constants.INTEGER },
			{ COL_COD_CONFIGURACION, Constants.INTEGER },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_FEC_HORA_INI, Constants.LONG },
			{ COL_FEC_HORA_FIN, Constants.LONG },
			{ COL_FLG_PROCESO, Constants.INTEGER }};

	public static final int DAO_URI_CODE = 18;

	public static final int INSERT_URI_CODE = 1899;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1801;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1802;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int DELETE_ALL_URI_CODE = 1851;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_FLUJO));
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
					+ COL_COD_FLUJO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_FLUJO + " ASC ";
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
		Flux sonEntity = (Flux) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_FLUJO, sonEntity.getIntFluxId());
		values.put(COL_DES_FLUJO, sonEntity.getStrDescription());
		values.put(COL_COD_MODULO, sonEntity.getIntModuleId());
		values.put(COL_COD_CONFIGURACION, sonEntity.getIntConfigurationId());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_FEC_HORA_INI, sonEntity.getLngStartHour());
		values.put(COL_FEC_HORA_FIN, sonEntity.getLngEndHour());
		values.put(COL_FLG_PROCESO, sonEntity.getIntProcess());
		return values;
	}

	public static Flux createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intFluxId = cursor.getInt(cursor.getColumnIndex(COL_COD_FLUJO));
		String strDescription = cursor.getString(cursor
				.getColumnIndex(COL_DES_FLUJO));
		int intModuleId = cursor.getInt(cursor.getColumnIndex(COL_COD_MODULO));
		int intConfigurationId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_CONFIGURACION));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		long lngStartHour = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_INI));
		long lngEndHour = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_FIN));
		int intProcess = cursor.getInt(cursor.getColumnIndex(COL_FLG_PROCESO));
		
		Flux objFlux = new Flux(intFluxId, strDescription, intModuleId,
				intConfigurationId, intState, lngStartHour, lngEndHour,intProcess);
		return objFlux;
	}

	public static List<Flux> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Flux> lst = new ArrayList<Flux>();
		while (!cursor.isAfterLast()) {
			Flux obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
