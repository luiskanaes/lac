package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Log;
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

public class LogDAO extends BaseDAO{
	public static final String DAO_NAME = "LogDAO";
	public static final String TB_NAME = "GLS_GR_LOG_CATCH";
	public static final String COL_ID = "_id";
	public static final String COL_NUM_LOG_ID = "NUM_LOG_ID_CTC";
	public static final String COL_DES_USUARIO = "DES_USUARIO_ERL";
	public static final String COL_DES_IMEI = "DES_IMEI_ERL";
	public static final String COL_DES_ERROR_MENSAJE = "DES_ERROR_MENSAJE_ERL";
	public static final String COL_NUM_ERROR_LINEA = "NUM_ERROR_LINEA_ERL";
	public static final String COL_DES_ERROR_PROCEDIMIENTO = "DES_ERROR_PROCEDIMIENTO_ERL";
	public static final String COL_FEC_REGISTRO_MOVIL = "FEC_REGISTRO_MOVIL_ERL";
	
    private final String[][] TB_CREATE_INFO= new String[][]{
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_NUM_LOG_ID, Constants.INTEGER },
			{ COL_DES_USUARIO, Constants.TEXT },
			{ COL_DES_IMEI, Constants.TEXT },
			{ COL_DES_ERROR_MENSAJE, Constants.TEXT },
			{ COL_NUM_ERROR_LINEA, Constants.INTEGER },
			{ COL_DES_ERROR_PROCEDIMIENTO, Constants.TEXT },
			{ COL_FEC_REGISTRO_MOVIL, Constants.LONG } };
	
	public static final int DAO_URI_CODE = 35;
	
	public static final int INSERT_URI_CODE = 3599;
	public static final String INSERT_PATH = TB_NAME+ Constants.LBL_SLASH + "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH +INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 3501;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 3502;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int DELETE_ALL_URI_CODE = 3551;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);
	
	public static final int DELETE_BY_ID_URI_CODE = 3552;
	public static final String DELETE_BY_ID_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteByID";
	public static final Uri DELETE_BY_ID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_BY_ID_PATH);
	
	
	
	@Override
	public boolean onCreate(final SQLiteDatabase db){
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_DES_USUARIO));
		return true;
	}
	
	@Override
	public boolean onUpgrade(final SQLiteDatabase db){
		db.execSQL(dropTable(TB_NAME));
		onCreate(db);
		return true;
	}
	
	@Override
	public int loadDaoUris(final UriMatcher uriMatcher){
        uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY, INSERT_PATH, INSERT_URI_CODE);
        uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ONE_PATH, QUERY_ONE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ALL_PATH, QUERY_ALL_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_BY_ID_PATH, DELETE_BY_ID_URI_CODE);
		return DAO_URI_CODE;
	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db, int intUriCode,
			String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Cursor result = null;
		
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);
		String groupBy = null;
		String having = null;
		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_DES_USUARIO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_NUM_LOG_ID + " ASC ";
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
	public Uri insert(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			ContentValues values) {
		Uri uriInserted = QUERY_ALL_URI;

		switch (intUriCode){
		case INSERT_URI_CODE:
			break; 
		default:
            throw new SQLException("INVALID URI :" + uri + " --- DAO: " + TB_NAME + " --- ACTION: " + "INSERT");
        }
		
		long lngResultId = db.insert(TB_NAME, null, values);
		
		if (lngResultId > -1) {        	
        	uriInserted = ContentUris.withAppendedId(uri, lngResultId);
        } else{
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
		case DELETE_BY_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_ID + " = ? ";
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "DELETE");
		}
		result = db.delete(tableName, selection, selectionArgs);
		return result;
	}

	@Override
	public int update(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			ContentValues values, String strWhereClause, String[] strWhereArgs) {
		int result = -1;
        return result;
	}
	
	public static ContentValues createContentValues(final Generic entity){
		Log sonEntity = (Log) entity;
		ContentValues values = new ContentValues();
		values.put(COL_NUM_LOG_ID, String.valueOf(sonEntity.getIntLogId()));
		values.put(COL_DES_USUARIO, String.valueOf(sonEntity.getStrUserId()));
		values.put(COL_DES_IMEI, String.valueOf(sonEntity.getStrCellId()));
		values.put(COL_DES_ERROR_MENSAJE, sonEntity.getStrErrorMessage());
		values.put(COL_NUM_ERROR_LINEA, sonEntity.getIntErrorLine());
		values.put(COL_DES_ERROR_PROCEDIMIENTO, sonEntity.getStrErrorProcedure());
		values.put(COL_FEC_REGISTRO_MOVIL, sonEntity.getLngDate());
		return values;
	}
	
	public static Log createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intLogId = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_LOG_ID));
		String strUserId = String.valueOf(cursor.getInt(cursor
				.getColumnIndex(COL_DES_USUARIO)));
		String strCellId = String.valueOf(cursor.getInt(cursor
				.getColumnIndex(COL_DES_IMEI)));
		String strErrorMessage = cursor.getString(cursor
				.getColumnIndex(COL_DES_ERROR_MENSAJE));
		int intErrorLine = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_ERROR_LINEA));
		String strErrorProcedure = cursor.getString(cursor
				.getColumnIndex(COL_DES_ERROR_PROCEDIMIENTO));
		long lngDate = cursor.getLong(cursor.getColumnIndex(COL_FEC_REGISTRO_MOVIL));
		Log objLog = new Log(intLogId, strUserId, strCellId, strErrorMessage, intErrorLine, strErrorProcedure, lngDate);
		
		return objLog;
	}
	
	public static List<Log> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();	
		List<Log> lst = new ArrayList<Log>();	
		while(!cursor.isAfterLast()){
			Log obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
