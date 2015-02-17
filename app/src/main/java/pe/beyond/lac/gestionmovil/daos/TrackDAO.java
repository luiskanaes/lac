package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Track;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
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

public class TrackDAO extends BaseDAO {
	public static final String DAO_NAME = "TrackDAO";
	public static final String TB_NAME = "GLS_GR_MAE_TRACK";
	public static final String COL_ID = "_id";
	public static final String COL_COD_PROVIDER = "COD_PROVIDER_TRK";
	public static final String COL_CAN_LATITUD = "CAN_LATITUD_TRK";
	public static final String COL_CAN_LONGITUD = "CAN_LONGITUD_TRK";
	public static final String COL_POR_PRECISION = "POR_PRECISION_TRK";
	public static final String COL_FEC_HORA = "FEC_HORA_TRK";
	
	//COL_FLG_ENVIADO TRACK
	public static final String COL_FLG_ENVIADO = "FLG_ENVIADO_TRK";
	
	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_PROVIDER, Constants.INTEGER },
			{ COL_CAN_LATITUD, Constants.INTEGER },
			{ COL_CAN_LONGITUD, Constants.INTEGER },
			{ COL_POR_PRECISION, Constants.TEXT },
			{ COL_FEC_HORA, Constants.INTEGER },
			{ COL_FLG_ENVIADO, Constants.INTEGER }};

	public static final int DAO_URI_CODE = 34;

	public static final int INSERT_URI_CODE = 3499;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 3401;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 3402;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH+ QUERY_ALL_PATH);

	public static final int QUERY_MOST_RECENT_URI_CODE = 3403;
	public static final String QUERY_MOST_RECENT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryMostRecent";
	public static final Uri QUERY_MOST_RECENT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_MOST_RECENT_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 3451;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH + "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH+ DELETE_ALL_PATH);
	
	/* TRACKING URIs JV*/
	public static final int QUERY_TRACK_URI_CODE = 3405;
	public static final String QUERY_TRACK_PATH = TB_NAME + Constants.LBL_SLASH + "QueryTrack";
	public static final Uri QUERY_TRACK_URI = Uri.parse("content://"+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH+ QUERY_TRACK_PATH);
	
	public static final int UPDATE_TRACK_URI_CODE = 3406;
	public static final String UPDATE_TRACK_PATH = TB_NAME+ Constants.LBL_SLASH + "UpdateTrack";
	public static final Uri UPDATE_TRACK_URI = Uri.parse("content://"+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH+ UPDATE_TRACK_PATH);
	
	
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_PROVIDER));
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
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,QUERY_ONE_PATH, QUERY_ONE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,QUERY_ALL_PATH, QUERY_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,QUERY_MOST_RECENT_PATH, QUERY_MOST_RECENT_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,QUERY_TRACK_PATH, QUERY_TRACK_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,UPDATE_TRACK_PATH, UPDATE_TRACK_URI_CODE);
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
		String limit = null;
		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT+ COL_FEC_HORA + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT+ COL_FEC_HORA + " ASC ";
			break;
		case QUERY_MOST_RECENT_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT+ COL_FEC_HORA + " DESC ";
			limit = " 1 ";
			break;
			//
		case QUERY_TRACK_URI_CODE:
			/* QUERY TRACK URI CODE */
			
			db.execSQL("UPDATE GLS_GR_MAE_TRACK SET FLG_ENVIADO_TRK = 99 WHERE _id%5<>0 AND _id<>1"); //<=== /!\ OPTIMIZAR ESTO /!\
			
			selection = " " + TB_NAME + Constants.LBL_DOT+ COL_FLG_ENVIADO + " = ? ";
			limit = " 15 ";
			
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "	+ TB_NAME + " --- ACTION: " + "QUERY");
		}
		
		result = queryBuilder.query(db, projection, selection, selectionArgs,groupBy, having, sortOrder,limit);
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
		
		
        final int rowsAffected = db.update(TB_NAME, values, selection, selectionArgs);
//        if (rowsAffected > 0) {
//        	context.getContentResolver().notifyChange(notificationUri, null);
//        }
        return rowsAffected;
	}
	
	public static ContentValues createContentValues(final Generic entity) {
		Track sonEntity = (Track) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_PROVIDER, sonEntity.getIntProviderId());
		values.put(COL_CAN_LATITUD, sonEntity.getFltLatitude());
		values.put(COL_CAN_LONGITUD, sonEntity.getFltLongitude());
		values.put(COL_POR_PRECISION, sonEntity.getFltAccuracy());
		values.put(COL_FEC_HORA, sonEntity.getLngTime());
		values.put(COL_FLG_ENVIADO, sonEntity.getIntEnviado());
		return values;
	}

	public static Track createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intProviderId = cursor.getInt(cursor.getColumnIndex(COL_COD_PROVIDER));
		float fltLatitude = cursor.getFloat(cursor.getColumnIndex(COL_CAN_LATITUD));
		float fltLongitude = cursor.getFloat(cursor.getColumnIndex(COL_CAN_LONGITUD));
		float fltAccuracy = cursor.getFloat(cursor.getColumnIndex(COL_POR_PRECISION));
		long lngTime = cursor.getLong(cursor.getColumnIndex(COL_FEC_HORA));
		
		int intEnviado = cursor.getInt(cursor.getColumnIndex(COL_FLG_ENVIADO));
		int intId = cursor.getInt(cursor.getColumnIndex(COL_ID));
		
		Track objTrack = new Track(intProviderId, fltLatitude, fltLongitude, fltAccuracy, lngTime,intEnviado,intId );
		
		return objTrack;
	}

	public static List<Track> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Track> lst = new ArrayList<Track>();
		while (!cursor.isAfterLast()) {
			Track objTrack = createObject(cursor);
			lst.add(objTrack);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
