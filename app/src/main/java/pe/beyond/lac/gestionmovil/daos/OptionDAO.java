package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Option;
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

public class OptionDAO extends BaseDAO {
	public static final String DAO_NAME = "OptionDAO";
	public static final String TB_NAME = "GLS_GR_MAE_OPCION";
	public static final String COL_ID = "_id";
	public static final String COL_COD_OPCION = "COD_OPCION_OPC";
	public static final String COL_COD_PREGUNTA = "COD_PREGUNTA_PRG";
	public static final String COL_DES_OPCION_ABREV = "DES_OPCION_ABREV_OPC";
	public static final String COL_DES_OPCION_REAL = "DES_OPCION_REAL_OPC";
	public static final String COL_DES_DESCRIPCION = "DES_DESCRIPCION_OPC";
	public static final String COL_NUM_KEY = "NUM_KEY_OPC";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_FLG_IMPOSIBILIDAD = "FLG_IMPOSIBILIDAD_OPC";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_OPCION, Constants.INTEGER },
			{ COL_COD_PREGUNTA, Constants.INTEGER },
			{ COL_DES_OPCION_ABREV, Constants.TEXT },
			{ COL_DES_OPCION_REAL, Constants.TEXT },
			{ COL_DES_DESCRIPCION, Constants.TEXT },
			{ COL_NUM_KEY, Constants.INTEGER },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_FLG_IMPOSIBILIDAD, Constants.BOOLEAN } };

	public static final int DAO_URI_CODE = 10;

	public static final int INSERT_URI_CODE = 1099;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1001;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1002;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_BY_QUESTIONID_CODE = 1003;
	public static final String QUERY_BY_QUESTIONID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByQuestionId";
	public static final Uri QUERY_BY_QUESTIONID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_QUESTIONID_PATH);

	public static final int QUERY_NON_AVAILABLE_OPTIONS_URI_CODE = 1004;
	public static final String QUERY_NON_AVAILABLE_OPTIONS_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryNonAvailableOptions";
	public static final Uri QUERY_NON_AVAILABLE_OPTIONS_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_NON_AVAILABLE_OPTIONS_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 1051;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	public static final int UPDATE_STATE_BY_OPTIONID_URI_CODE = 1052;
	public static final String UPDATE_STATE_BY_OPTIONID_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateStateByOptionId";
	public static final Uri UPDATE_STATE_BY_OPTIONID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_STATE_BY_OPTIONID_PATH);
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_OPCION));
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
				QUERY_BY_QUESTIONID_PATH, QUERY_BY_QUESTIONID_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_NON_AVAILABLE_OPTIONS_PATH, QUERY_NON_AVAILABLE_OPTIONS_URI_CODE);
		
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_STATE_BY_OPTIONID_PATH, UPDATE_STATE_BY_OPTIONID_URI_CODE);
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
					+ COL_COD_OPCION + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_OPCION + " ASC ";
		case QUERY_BY_QUESTIONID_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_PREGUNTA
					+ " = ? " + " AND " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_ESTADO + " = 1 ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_OPCION
					+ " ASC ";
			break;
		case QUERY_NON_AVAILABLE_OPTIONS_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_ESTADO + " != 1 ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_OPCION + " ASC ";
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
			int intUriCode, ContentValues values, String selection, String[] selectionArgs) {
		int result = -1;
		String tableName = TB_NAME;
		Uri notificationUri = QUERY_ALL_URI;
		switch (intUriCode) {
		case UPDATE_STATE_BY_OPTIONID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_OPCION + " = ? ";
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "UPDATE");
		}
		result = db.update(tableName, values, selection, selectionArgs);
		if (result > 0) {
			context.getContentResolver().notifyChange(notificationUri, null);
		}
		return result;
	}

	public static ContentValues createContentValues(final Generic entity) {
		Option sonEntity = (Option) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_OPCION, sonEntity.getIntOptionId());
		values.put(COL_COD_PREGUNTA, sonEntity.getIntQuestionId());
		values.put(COL_DES_OPCION_ABREV, sonEntity.getStrOptionAbreviation());
		values.put(COL_DES_OPCION_REAL, sonEntity.getStrOptionReal());
		values.put(COL_DES_DESCRIPCION, sonEntity.getStrDescription());
		values.put(COL_NUM_KEY, sonEntity.getIntKey());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_FLG_IMPOSIBILIDAD, sonEntity.isBooFlgImpossibility());

		return values;
	}

	public static Option createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intOptionId = cursor.getInt(cursor.getColumnIndex(COL_COD_OPCION));
		int intQuestionId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PREGUNTA));
		String strOptionAbreviation = cursor.getString(cursor
				.getColumnIndex(COL_DES_OPCION_ABREV));
		String strOptionReal = cursor.getString(cursor
				.getColumnIndex(COL_DES_OPCION_REAL));
		String strDescription = cursor.getString(cursor
				.getColumnIndex(COL_DES_DESCRIPCION));
		int intKey = cursor.getInt(cursor.getColumnIndex(COL_NUM_KEY));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		boolean booFlgImpossibility = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_IMPOSIBILIDAD)) == 1;

		Option objRegister = new Option(intOptionId, intQuestionId,
				strOptionAbreviation, strOptionReal, strDescription, intKey,
				intState, booFlgImpossibility);

		return objRegister;
	}

	public static ArrayList<Option> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		ArrayList<Option> lst = new ArrayList<Option>();
		while (!cursor.isAfterLast()) {
			Option objForm = createObject(cursor);
			lst.add(objForm);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
