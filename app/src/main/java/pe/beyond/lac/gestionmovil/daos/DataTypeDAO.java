package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.DataType;
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

public class DataTypeDAO extends BaseDAO{
	public static final String DAO_NAME = "DataTypeDAO";
	public static final String TB_NAME = "GLS_GR_MAE_TIPO_DATO";
	public static final String COL_ID = "_id";
	public static final String COL_COD_TIPODATO = "COD_TIPODATO_TDA";
	public static final String COL_DES_TIPODATO = "DES_TIPODATO_TDA";
	public static final String COL_NUM_LONGITUD_MIN = "NUM_LONGITUD_MIN_TDA";
	public static final String COL_NUM_LONGITUD_MAX = "NUM_LONGITUD_MAX_TDA";
	public static final String COL_DES_DATO = "DES_DATO_TDA";
	public static final String COL_DES_CHARS_NOVALIDOS = "DES_CARACTERES_NOVALIDOS_TDA";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_DES_SENTENCIA_FORMATO = "DES_SENTENCIA_FORMATO_TDA";
	public static final String COL_NUM_DECIMALES = "NUM_DECIMALES_TDA";
    
    private final String[][] TB_CREATE_INFO= new String[][]{
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_TIPODATO, Constants.INTEGER },
			{ COL_DES_TIPODATO, Constants.TEXT },
			{ COL_NUM_LONGITUD_MIN, Constants.INTEGER },
			{ COL_NUM_LONGITUD_MAX, Constants.INTEGER },
			{ COL_DES_DATO, Constants.TEXT },
			{ COL_DES_CHARS_NOVALIDOS, Constants.TEXT },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_DES_SENTENCIA_FORMATO, Constants.TEXT },
			{ COL_NUM_DECIMALES, Constants.INTEGER }
    };
	
	public static final int DAO_URI_CODE = 26;
	
	public static final int INSERT_URI_CODE = 2699;
	public static final String INSERT_PATH = TB_NAME+ Constants.LBL_SLASH + "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH +INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2601;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2602;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int DELETE_ALL_URI_CODE = 2651;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	
	@Override
	public boolean onCreate(final SQLiteDatabase db){
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_TIPODATO));
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
					+ COL_COD_TIPODATO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_TIPODATO + " ASC ";
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
		DataType sonEntity = (DataType) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_TIPODATO, sonEntity.getIntDataTypeId());
		values.put(COL_DES_TIPODATO, sonEntity.getStrDescription());
		values.put(COL_NUM_LONGITUD_MIN, sonEntity.getIntLongitudeMin());
		values.put(COL_NUM_LONGITUD_MAX, sonEntity.getIntLongitudeMax());
		values.put(COL_DES_DATO, sonEntity.getStrData());
		values.put(COL_DES_CHARS_NOVALIDOS, sonEntity.getStrInvalidCharacters());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_DES_SENTENCIA_FORMATO, sonEntity.getStrFormatSentence());
		values.put(COL_NUM_DECIMALES, sonEntity.getIntDecimalsQuantity());
		return values;
	}
	
	public static DataType createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intDataTypeId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_TIPODATO));
		String strDescription = cursor.getString(cursor
				.getColumnIndex(COL_DES_TIPODATO));
		int intLongitudeMin = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_LONGITUD_MIN));
		int intLongitudeMax = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_LONGITUD_MAX));
		String strData = cursor.getString(cursor
				.getColumnIndex(COL_DES_DATO));
		String strInvalidCharacters = cursor.getString(cursor
				.getColumnIndex(COL_DES_CHARS_NOVALIDOS));
		int intState = cursor.getInt(cursor
				.getColumnIndex(COL_COD_ESTADO));
		String strFormatSentence = cursor.getString(cursor
				.getColumnIndex(COL_DES_SENTENCIA_FORMATO));
		int intDecimalsQuantity = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_DECIMALES));
		DataType objDataType = new DataType(intDataTypeId, strDescription, intLongitudeMin, intLongitudeMax, strData, strInvalidCharacters, intState, strFormatSentence,intDecimalsQuantity);
		return objDataType;
	}
	
	public static List<DataType> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();	
		List<DataType> lst = new ArrayList<DataType>();	
		while(!cursor.isAfterLast()){
			DataType obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
