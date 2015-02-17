package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.ObsCode;
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

public class ObsCodeDAO extends BaseDAO{
	public static final String DAO_NAME = "ObsCodeDAO";
	public static final String TB_NAME = "GLS_TE_MAE_CODOBS";
	public static final String COL_ID = "_id";
	public static final String COL_COD_OBS = "COD_OBS_TMC";
	public static final String COL_COD_FLUJO = "COD_FLUJO_RFL";
	public static final String COL_COD_PREGUNTA = "COD_PREGUNTA_PRG";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
    private final String[][] TB_CREATE_INFO= new String[][]{
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_OBS, Constants.INTEGER },
			{ COL_COD_FLUJO, Constants.INTEGER },
			{ COL_COD_PREGUNTA, Constants.INTEGER },
			{ COL_COD_ESTADO, Constants.INTEGER } };
	
	public static final int DAO_URI_CODE = 27;
	
	public static final int INSERT_URI_CODE = 2799;
	public static final String INSERT_PATH = TB_NAME+ Constants.LBL_SLASH + "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH +INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2701;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2702;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);
	
	public static final int QUERY_BY_OBSCODEID_URI_CODE = 2703;
	public static final String QUERY_BY_OBSCODEID_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryByObsCodeId";
	public static final Uri QUERY_BY_OBSCODEID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_OBSCODEID_PATH);
	
	
	
	public static final int DELETE_ALL_URI_CODE = 2751;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	
	@Override
	public boolean onCreate(final SQLiteDatabase db){
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_OBS));
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
				QUERY_BY_OBSCODEID_PATH, QUERY_BY_OBSCODEID_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		return DAO_URI_CODE;
	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db, int intUriCode,
			String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Cursor result = null;
		Uri notificationUri = QUERY_ALL_URI;

		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);
		String groupBy = null;
		String having = null;
		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_OBS + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_OBS + " ASC ";
			break;
		case QUERY_BY_OBSCODEID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
			+ COL_COD_OBS + " = ? AND " + TB_NAME + Constants.LBL_DOT
			+ COL_COD_FLUJO + " = ? ";
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
        	context.getContentResolver().notifyChange(uriInserted, null); 
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
		Uri notificationUri = QUERY_ALL_URI;
		switch (intUriCode) {
		case DELETE_ALL_URI_CODE:
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
	public int update(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			ContentValues values, String strWhereClause, String[] strWhereArgs) {
		int result = -1;
        return result;
	}
	
	public static ContentValues createContentValues(final Generic entity){
		ObsCode sonEntity = (ObsCode) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_OBS, sonEntity.getIntObsId());
		values.put(COL_COD_FLUJO, sonEntity.getIntFluxId());
		values.put(COL_COD_PREGUNTA, sonEntity.getIntQuestionId());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		return values;
	}
	
	public static ObsCode createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intObsId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_OBS));
		int intFluxId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_FLUJO));
		int intQuestionId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PREGUNTA));
		int intState = cursor.getInt(cursor
				.getColumnIndex(COL_COD_ESTADO));

		ObsCode objObs = new ObsCode(intObsId, intFluxId, intQuestionId,
				intState);
		return objObs;
	}
	
	public static List<ObsCode> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();	
		List<ObsCode> lst = new ArrayList<ObsCode>();	
		while(!cursor.isAfterLast()){
			ObsCode obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
