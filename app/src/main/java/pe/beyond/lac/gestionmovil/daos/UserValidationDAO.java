package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.UserValidation;
import pe.beyond.gls.model.ValidationResult;
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

public class UserValidationDAO extends BaseDAO {
	public static final String DAO_NAME = "UserValidationDAO";
	public static final String TB_NAME = "GLS_GR_REL_VALIDACION_USUARIO";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_VAL_USUARIO = "IDE_VAL_USUARIO_VUS";
	public static final String COL_IDE_USUARIO = "IDE_USUARIO_USU";
	public static final String COL_COD_VALIDACION = "COD_VALIDACION_VAL";
	public static final String COL_FEC_ASIGNACION = "FEC_ASIGNACION_VUS";
	public static final String COL_DES_OPCION = "DES_OPCION_VUS";
	public static final String COL_DES_OPCION_REAL = "DES_OPCION_REAL_VUS";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_VAL_USUARIO, Constants.INTEGER },
			{ COL_IDE_USUARIO, Constants.INTEGER },
			{ COL_COD_VALIDACION, Constants.INTEGER },
			{ COL_FEC_ASIGNACION, Constants.LONG },
			{ COL_DES_OPCION, Constants.TEXT },
			{ COL_DES_OPCION_REAL, Constants.TEXT },
			{ COL_COD_ESTADO, Constants.INTEGER } };

	public static final int DAO_URI_CODE = 32;

	public static final int INSERT_URI_CODE = 3299;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 3201;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 3202;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);
	
	public static final int QUERY_BY_DATA_URI_CODE = 3203;
	public static final String QUERY_BY_DATA_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryByData";
	public static final Uri QUERY_BY_DATA_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_DATA_PATH);
	
	public static final int QUERY_BY_IDS_URI_CODE = 3204;
	public static final String QUERY_BY_IDS_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryByIDs";
	public static final Uri QUERY_BY_IDS_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_IDS_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 3251;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	public static final int UPDATE_STATE_BY_PK_URI_CODE = 3252;
	public static final String UPDATE_STATE_BY_PK_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdateStateByPK";
	public static final Uri UPDATE_STATE_BY_PK_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_STATE_BY_PK_PATH);

	
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_IDE_VAL_USUARIO));
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
				QUERY_BY_DATA_PATH, QUERY_BY_DATA_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_BY_IDS_PATH, QUERY_BY_IDS_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_STATE_BY_PK_PATH, UPDATE_STATE_BY_PK_URI_CODE);

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
				+ COL_IDE_VAL_USUARIO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
				+ COL_IDE_VAL_USUARIO + " ASC ";
			break;
		case QUERY_BY_DATA_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO + " = ? AND " +
					TB_NAME + Constants.LBL_DOT + COL_COD_VALIDACION + " = ? AND " +
					TB_NAME + Constants.LBL_DOT + COL_DES_OPCION + " = ? AND " + 
					TB_NAME + Constants.LBL_DOT + COL_COD_ESTADO + " = ? " ;
			break;
		case QUERY_BY_IDS_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO + " = ? AND " +
					TB_NAME + Constants.LBL_DOT + COL_COD_VALIDACION + " = ? AND " +
					TB_NAME + Constants.LBL_DOT + COL_COD_ESTADO + " = " + Constants.FLG_ENABLED + " ";
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
		case UPDATE_STATE_BY_PK_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_VAL_USUARIO + " = ? ";
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
		UserValidation sonEntity = (UserValidation) entity;
		ContentValues values = new ContentValues();
		values.put(COL_IDE_VAL_USUARIO,sonEntity.getIntUserValidationId());
		values.put(COL_IDE_USUARIO, sonEntity.getIntUserId());
		values.put(COL_COD_VALIDACION, sonEntity.getIntValidationId());
		values.put(COL_FEC_ASIGNACION, sonEntity.getLngAssignationDate());
		values.put(COL_DES_OPCION, sonEntity.getStrOptionAbreviation());
		values.put(COL_DES_OPCION_REAL, sonEntity.getStrOptionReal());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());

		return values;
	}

	public static UserValidation createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intUserValidationId = cursor.getInt(cursor.getColumnIndex(COL_IDE_VAL_USUARIO));
		int intUserId = cursor.getInt(cursor.getColumnIndex(COL_IDE_USUARIO));
		int intValidationId = cursor.getInt(cursor.getColumnIndex(COL_COD_VALIDACION));
		long lngAssignationDate = cursor.getLong(cursor.getColumnIndex(COL_FEC_ASIGNACION));
		String strOptionAbreviation = cursor.getString(cursor.getColumnIndex(COL_DES_OPCION));
		String strOptionReal = cursor.getString(cursor.getColumnIndex(COL_DES_OPCION_REAL));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		UserValidation objUserValidation = new UserValidation(intUserValidationId, intUserId, intValidationId, lngAssignationDate, strOptionAbreviation, strOptionReal, intState); 
		return objUserValidation;
	}

	public static List<UserValidation> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<UserValidation> lst = new ArrayList<UserValidation>();
		while (!cursor.isAfterLast()) {
			UserValidation obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
