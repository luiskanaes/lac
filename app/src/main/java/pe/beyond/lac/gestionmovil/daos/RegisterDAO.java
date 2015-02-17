package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Register;
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

public class RegisterDAO extends BaseDAO {
	public static final String DAO_NAME = "RegisterDAO";
	public static final String TB_NAME = "GLS_GR_TRX_REGISTRO";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_LISTADO = "IDE_LISTADO_LIS";
	public static final String COL_NUM_DESCARGA = "NUM_DESCARGA_LIS";	
	public static final String COL_FEC_HORA_INICIO_REAL = "FEC_HORA_INICIO_REAL_TRE";
	public static final String COL_FEC_HORA_FIN_REAL = "FEC_HORA_FIN_REAL_TRE";
	public static final String COL_FEC_HORA_INICIO_REG = "FEC_HORA_INICIO_REG_TRE";
	public static final String COL_FEC_HORA_FIN_REG = "FEC_HORA_FIN_REG_TRE";
	public static final String COL_CAN_GPS_LATITUD_REAL = "CAN_GPS_LATITUD_REAL_TRE";
	public static final String COL_CAN_GPS_LONGITUD_REAL = "CAN_GPS_LONGITUD_REAL_TRE";
	public static final String COL_CAN_GPS_LATITUD_REG = "CAN_GPS_LATITUD_REG_TRE";
	public static final String COL_CAN_GPS_LONGITUD_REG = "CAN_GPS_LONGITUD_REG_TRE";
	public static final String COL_POR_GPS_PRECISION = "POR_GPS_PRECISION_TRE";
	public static final String COL_FLG_QR = "FLG_QR_TRE";
	public static final String COL_FLG_ZONA_PELIGROSA = "FLG_ZONA_PELIGROSA_TRE";
	public static final String COL_IDE_USUARIO = "IDE_USUARIO_USU";
	public static final String COL_COD_CODIGO = "COD_CODIGO_AUS";
	public static final String COL_NUM_MOVIL = "NUM_MOVIL_TRE";
	public static final String COL_COD_GESTIONCOMERCIAL = "COD_GESTIONCOMERCIAL_LIS";
	public static final String COL_COD_FLUJO = "COD_FLUJO_RFL";
	public static final String COL_FLG_ENVIO_COMPLETO = "FLG_ENVIO_COMPLETO_TRE";	
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_LISTADO, Constants.INTEGER },
			{ COL_NUM_DESCARGA, Constants.INTEGER },
			{ COL_FEC_HORA_INICIO_REAL, Constants.LONG },
			{ COL_FEC_HORA_FIN_REAL, Constants.LONG },
			{ COL_FEC_HORA_INICIO_REG, Constants.LONG },
			{ COL_FEC_HORA_FIN_REG, Constants.LONG },
			{ COL_CAN_GPS_LATITUD_REAL, Constants.REAL },
			{ COL_CAN_GPS_LONGITUD_REAL, Constants.REAL },
			{ COL_CAN_GPS_LATITUD_REG, Constants.REAL },
			{ COL_CAN_GPS_LONGITUD_REG, Constants.REAL },
			{ COL_POR_GPS_PRECISION, Constants.INTEGER },
			{ COL_FLG_QR, Constants.BOOLEAN },
			{ COL_FLG_ZONA_PELIGROSA, Constants.BOOLEAN },
			{ COL_IDE_USUARIO, Constants.INTEGER },
			{ COL_COD_CODIGO, Constants.TEXT },
			{ COL_NUM_MOVIL, Constants.TEXT },
			{ COL_COD_GESTIONCOMERCIAL, Constants.TEXT },
			{ COL_COD_FLUJO, Constants.INTEGER },
			{ COL_FLG_ENVIO_COMPLETO, Constants.BOOLEAN },
			{ COL_COD_ESTADO, Constants.INTEGER }	
	};

	public static final int DAO_URI_CODE = 23;

	public static final int INSERT_URI_CODE = 2399;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2301;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2302;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_TIME_CONFLICTS_URI_CODE = 2303;
	public static final String QUERY_TIME_CONFLICTS_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryTimeConflicts";
	public static final Uri QUERY_TIME_CONFLICTS_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_TIME_CONFLICTS_PATH);

	public static final int QUERY_ONE_BY_LISTING_ID_URI_CODE = 2304;
	public static final String QUERY_ONE_BY_LISTING_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryOneByListingId";
	public static final Uri QUERY_ONE_BY_LISTING_ID_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_ONE_BY_LISTING_ID_PATH);
	
	public static final int QUERY_UPLOAD_SET_URI_CODE = 2305;
	public static final String QUERY_UPLOAD_SET_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryUploadSet";
	public static final Uri QUERY_UPLOAD_SET_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_UPLOAD_SET_PATH);
	
	public static final int QUERY_ALL_WORKED_BY_USER_URI_CODE = 2306;
	public static final String QUERY_ALL_WORKED_BY_USER_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryAllWorkedByUser";
	public static final Uri QUERY_ALL_WORKED_BY_USER_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_ALL_WORKED_BY_USER_PATH);
	
	
	// Registros de ejecutados
		public static final int QUERY_REGISTERED_BY_USER_URI_CODE = 2307;
		public static final String QUERY_REGISTERED_BY_USER_PATH = TB_NAME
				+ Constants.LBL_SLASH + "QueryRegisteredByUser";
		public static final Uri QUERY_REGISTERED_BY_USER_URI = Uri
				.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY 
						+ Constants.LBL_SLASH + QUERY_REGISTERED_BY_USER_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 2351;
	public static final String DELETE_ALL_PATH = TB_NAME
			+ Constants.LBL_SLASH + "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);
	
	public static final int UPDATE_BY_PK_URI_CODE = 2352;
	public static final String UPDATE_BY_PK_PATH = TB_NAME + Constants.LBL_SLASH + "UpdateByPK";
	public static final Uri UPDATE_BY_PK_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_BY_PK_PATH);
	
	public static final int UPDATE_BY_LIST_ID_URI_CODE = 2353;
	public static final String UPDATE_BY_LIST_ID_PATH = TB_NAME + Constants.LBL_SLASH + "UpdateByListId";
	public static final Uri UPDATE_BY_LIST_ID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_BY_LIST_ID_PATH);
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_IDE_LISTADO));
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
				QUERY_TIME_CONFLICTS_PATH, QUERY_TIME_CONFLICTS_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ONE_BY_LISTING_ID_PATH, QUERY_ONE_BY_LISTING_ID_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_UPLOAD_SET_PATH, QUERY_UPLOAD_SET_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ALL_WORKED_BY_USER_PATH, QUERY_ALL_WORKED_BY_USER_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY, 
				QUERY_REGISTERED_BY_USER_PATH, QUERY_REGISTERED_BY_USER_URI_CODE);
		
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_BY_PK_PATH, UPDATE_BY_PK_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_BY_LIST_ID_PATH, UPDATE_BY_LIST_ID_URI_CODE);
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
		
		AppPreferences appPreferences = AppPreferences.getInstance(context);
		
		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_IDE_LISTADO + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " ASC ";
			break;
		case QUERY_TIME_CONFLICTS_URI_CODE:

			selection = " " + TB_NAME + Constants.LBL_DOT + 
			
			COL_IDE_USUARIO + " = ? AND ( " +
			"? BETWEEN " + 
				TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_INICIO_REG + " AND " + 
				TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_FIN_REG + " OR " +
			"? BETWEEN " + TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_INICIO_REG + " AND " + 
				TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_FIN_REG + " OR " +
				
				TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_INICIO_REG + " BETWEEN ? AND ? OR " +  
				TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_FIN_REG + " BETWEEN ? AND ? )";
			break;
			
			
		case QUERY_ONE_BY_LISTING_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " = ? AND "
					+ TB_NAME + Constants.LBL_DOT + COL_NUM_DESCARGA + " = ? ";
			break;
		case QUERY_UPLOAD_SET_URI_CODE:
			
			queryBuilder.setTables(" " + TB_NAME + " INNER JOIN " + ListingDAO.TB_NAME + " ON ( " +  
					
			RegisterDAO.TB_NAME + Constants.LBL_DOT + RegisterDAO.COL_IDE_LISTADO + " = " +
			ListingDAO.TB_NAME + Constants.LBL_DOT  + ListingDAO.COL_IDE_LISTADO  + " AND "+
			
			RegisterDAO.TB_NAME + Constants.LBL_DOT + RegisterDAO.COL_NUM_DESCARGA + " = " +
			ListingDAO.TB_NAME + Constants.LBL_DOT  + ListingDAO.COL_NUM_DESCARGA  + " AND "+
			
			ListingDAO.TB_NAME + Constants.LBL_DOT  + ListingDAO.COL_COD_ESTADO  + " = 3 " + " )");

			selection = " " + TB_NAME + Constants.LBL_DOT + COL_FLG_ENVIO_COMPLETO + " = ? ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " ASC ";
			limit = String.valueOf(Constants.NUM_UPLOAD_SET_LIMIT);
			break;
		case QUERY_ALL_WORKED_BY_USER_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO + " = ? ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " DESC ";
			break;
			
		case QUERY_REGISTERED_BY_USER_URI_CODE:
			queryBuilder.setTables(" " + TB_NAME + Constants.LBL_COMMA + ListingDAO.TB_NAME + Constants.LBL_COMMA + FluxDAO.TB_NAME + " ");
			
			projection = new String[]
			{
				" DISTINCT " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " AS " + Constants.COLUMN_NAME_COD_LISTADO + " "
			};
			
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " = " + 
					ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_IDE_LISTADO + " AND " +
					ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_COD_FLUJO + " = " +
					FluxDAO.TB_NAME + Constants.LBL_DOT + FluxDAO.COL_COD_FLUJO + " AND " +
					ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_IDE_USUARIO + " = " + appPreferences._loadUserId() + " AND " +
					FluxDAO.TB_NAME + Constants.LBL_DOT + FluxDAO.COL_COD_MODULO + " = ?" + " AND " +
					" DATE(CURRENT_TIMESTAMP) >= DATE( " + TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_INICIO_REG + " / 1000,'unixepoch') AND " +
					" DATE(CURRENT_TIMESTAMP) <= DATE( " + TB_NAME + Constants.LBL_DOT + COL_FEC_HORA_FIN_REG + " / 1000,'unixepoch') AND " +
					TB_NAME + Constants.LBL_DOT + COL_FLG_ENVIO_COMPLETO + " = ? ";
			
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " ASC ";
			
			break;
			
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "QUERY");
		}
		result = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, sortOrder,limit);
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
		case UPDATE_BY_PK_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " = ? " + 
					" AND " + TB_NAME + Constants.LBL_DOT + COL_NUM_DESCARGA + " = ? ";
			break;
		case UPDATE_BY_LIST_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO + " = ? ";
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
		Register sonEntity = (Register) entity;
		ContentValues values = new ContentValues();
		values.put(COL_IDE_LISTADO, sonEntity.getIntListId());
		values.put(COL_NUM_DESCARGA, sonEntity.getIntDownloadCounter());
		values.put(COL_FEC_HORA_INICIO_REAL, sonEntity.getLngStartDateReal());
		values.put(COL_FEC_HORA_FIN_REAL, sonEntity.getLngEndDateReal());
		values.put(COL_FEC_HORA_INICIO_REG, sonEntity.getLngStartDateRegister());
		values.put(COL_FEC_HORA_FIN_REG, sonEntity.getLngEndDateRegister());
		values.put(COL_CAN_GPS_LATITUD_REAL, sonEntity.getFltLatitudeReal());
		values.put(COL_CAN_GPS_LONGITUD_REAL,sonEntity.getFltLongitudeReal());
		values.put(COL_CAN_GPS_LATITUD_REG, sonEntity.getFltLatitudeRegister());
		values.put(COL_CAN_GPS_LONGITUD_REG,sonEntity.getFltLongitudeRegister());
		values.put(COL_POR_GPS_PRECISION, sonEntity.getFltGPSAccuracy());
		values.put(COL_FLG_QR, sonEntity.isBooFlgQR());
		values.put(COL_FLG_ZONA_PELIGROSA, sonEntity.isBooFlgDangerousZone());
		values.put(COL_IDE_USUARIO, sonEntity.getIntUserId());
		values.put(COL_COD_CODIGO, sonEntity.getStrUserCode());
		values.put(COL_NUM_MOVIL, sonEntity.getStrMovilNumber());
		values.put(COL_COD_GESTIONCOMERCIAL,
				sonEntity.getStrCommercialManagmentCode());
		values.put(COL_COD_FLUJO, sonEntity.getIntFluxId());
		values.put(COL_FLG_ENVIO_COMPLETO, sonEntity.getIntDeliveryState());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		return values;
	}

	public static Register createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intListId = cursor.getInt(cursor.getColumnIndex(COL_IDE_LISTADO));
		int intDownloadCounter = cursor.getInt(cursor.getColumnIndex(COL_NUM_DESCARGA));
		long lngStartDateReal = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_INICIO_REAL));
		long lngEndDateReal = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_FIN_REAL));
		long lngStartDateRegister = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_INICIO_REG));
		long lngEndDateRegister = cursor.getLong(cursor
				.getColumnIndex(COL_FEC_HORA_FIN_REG));
		float fltLatitudeReal = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_LATITUD_REAL));
		float fltLongitudeReal = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_LONGITUD_REAL));
		float fltLatitudeRegister = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_LATITUD_REG));
		float fltLongitudeRegister = cursor.getFloat(cursor
				.getColumnIndex(COL_CAN_GPS_LONGITUD_REG));
		float fltGPSAccuracy = cursor.getFloat(cursor
				.getColumnIndex(COL_POR_GPS_PRECISION));
		boolean booFlgQR = cursor.getInt(cursor.getColumnIndex(COL_FLG_QR)) == 1;
		boolean booFlgDangerousZone = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_ZONA_PELIGROSA)) == 1;
		int intUserId = cursor.getInt(cursor.getColumnIndex(COL_IDE_USUARIO));
		String strUserCode = cursor.getString(cursor
				.getColumnIndex(COL_COD_CODIGO));
		String strMovilNumber = cursor.getString(cursor
				.getColumnIndex(COL_NUM_MOVIL));
		String strCommercialManagmentCode = cursor.getString(cursor
				.getColumnIndex(COL_COD_GESTIONCOMERCIAL));
		int intFluxId = cursor.getInt(cursor.getColumnIndex(COL_COD_FLUJO));
		int intDeliveryState = cursor.getInt(cursor.getColumnIndex(COL_FLG_ENVIO_COMPLETO));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));

		Register objRegister = new Register(intListId,intDownloadCounter,
				lngStartDateReal, lngEndDateReal, lngStartDateRegister,
				lngEndDateRegister, fltLatitudeReal, fltLongitudeReal,
				fltLatitudeRegister, fltLongitudeRegister, fltGPSAccuracy,
				booFlgQR, booFlgDangerousZone, intUserId, strUserCode, strMovilNumber,
				strCommercialManagmentCode, intFluxId, intDeliveryState, intState);
		return objRegister;
	}

	public static List<Register> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Register> lst = new ArrayList<Register>();
		while (!cursor.isAfterLast()) {
			Register obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
