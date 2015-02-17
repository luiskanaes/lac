package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Template;
import pe.beyond.gls.model.Validation;
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

public class ValidationDAO extends BaseDAO {
	public static final String DAO_NAME = "ValidationDAO";
	public static final String TB_NAME = "GLS_GR_REL_VALIDACION";
	public static final String COL_ID = "_id";
	public static final String COL_COD_VALIDACION = "COD_VALIDACION_VAL";
	public static final String COL_DES_VALOR_MAX = "DES_VALOR_MAX_VAL";
	public static final String COL_DES_VALOR_MIN = "DES_VALOR_MIN_VAL";
	public static final String COL_DES_VALOR = "DES_VALOR_VAL";
	public static final String COL_DES_MENSAJE_ERROR = "DES_MENSAJE_ERROR_VAL";
	public static final String COL_FLG_RESTRICTIVO = "FLG_RESTRICTIVO_VAL";
	public static final String COL_COD_TIPO_VALIDACION = "COD_TIPO_VALIDACION_TVA";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_VALIDACION, Constants.INTEGER },
			{ COL_DES_VALOR_MAX, Constants.TEXT },
			{ COL_DES_VALOR_MIN, Constants.TEXT },
			{ COL_DES_VALOR, Constants.TEXT },
			{ COL_DES_MENSAJE_ERROR, Constants.TEXT },
			{ COL_FLG_RESTRICTIVO, Constants.BOOLEAN },
			{ COL_COD_TIPO_VALIDACION, Constants.INTEGER } };

	public static final int DAO_URI_CODE = 15;

	public static final int INSERT_URI_CODE = 1599;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1501;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1502;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int DELETE_ALL_URI_CODE = 1551;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_VALIDACION));
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
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_VALIDACION
					+ " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_VALIDACION
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
		Validation sonEntity = (Validation) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_VALIDACION, sonEntity.getIntValidationId());
		values.put(COL_DES_VALOR_MAX, sonEntity.getStrMaxValue());
		values.put(COL_DES_VALOR_MIN, sonEntity.getStrMinValue());
		values.put(COL_DES_VALOR, sonEntity.getStrValue());
		values.put(COL_DES_MENSAJE_ERROR, sonEntity.getStrErrorMessage());
		values.put(COL_FLG_RESTRICTIVO, sonEntity.isBooRestrictive());
		values.put(COL_COD_TIPO_VALIDACION, sonEntity.getIntValidationType());

		return values;
	}

	public static Validation createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intValidationId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_VALIDACION));
		String strMaxValue = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALOR_MAX));
		String strMinValue = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALOR_MIN));
		String strValue = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALOR));
		String strErrorMessage = cursor.getString(cursor
				.getColumnIndex(COL_DES_MENSAJE_ERROR));
		boolean booRestrictive = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_RESTRICTIVO)) == 1;
		int intValidationType = cursor.getInt(cursor
				.getColumnIndex(COL_COD_TIPO_VALIDACION));

		Validation objValidation = new Validation(intValidationId, strMaxValue,
				strMinValue, strValue, strErrorMessage,
				booRestrictive, intValidationType);
		return objValidation;
	}

	public static List<Validation> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Validation> lst = new ArrayList<Validation>();
		while (!cursor.isAfterLast()) {
			Validation obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
