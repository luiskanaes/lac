package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Photo;
import pe.beyond.gls.model.PhotoConfiguration;
import pe.beyond.gls.model.ValidationResult;
import pe.beyond.gls.model.ValidationType;
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
import android.util.Log;

public class PhotoDAO extends BaseDAO {
	public static final String DAO_NAME = "PhotoDAO";
	public static final String TB_NAME = "GLS_GR_MAE_PHOTO";
	public static final String COL_ID = "_id";
	public static final String COL_DES_ABREV_NOMBRE = "DES_ABREV_NOMBRE_PHO";
	public static final String COL_DES_FULL_NOMBRE = "DES_FULL_NOMBRE_PHO";
	public static final String COL_FLG_ENVIADO = "FLG_ENVIADO_PHO";
	public static final String COL_COD_MODULO = "COD_MODULO_MOD";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_DES_ABREV_NOMBRE, Constants.TEXT },
			{ COL_DES_FULL_NOMBRE, Constants.TEXT },
			{ COL_FLG_ENVIADO, Constants.BOOLEAN },
			{ COL_COD_MODULO, Constants.INTEGER }};

	public static final int DAO_URI_CODE = 33;
	public static final int INSERT_URI_CODE = 3399;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 3301;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 3302;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);
	
	public static final int QUERY_ALL_UNSENT_URI_CODE = 3303;
	public static final String QUERY_ALL_UNSENT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAllUnsent";
	public static final Uri QUERY_ALL_UNSENT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_UNSENT_PATH);
	
	public static final int QUERY_ONE_BY_ABREV_NAME_URI_CODE = 3304;
	public static final String QUERY_ONE_BY_ABREV_NAME_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOneByAbrevName";
	public static final Uri QUERY_ONE_BY_ABREV_NAME_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_BY_ABREV_NAME_PATH);

	public static final int DELETE_ALL_URI_CODE = 3351;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	public static final int UPDATE_PHOTO_STATE_URI_CODE = 3352;
	public static final String UPDATE_PHOTO_STATE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "UpdatePhotoState";
	public static final Uri UPDATE_PHOTO_STATE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_PHOTO_STATE_PATH);
	
	
	
	public static final int QUERY_ALL_MODULE_CHART_URI_CODE = 3353;
	public static final String QUERY_ALL_MODULE_CHART_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_MODULE_CHART_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_DES_ABREV_NOMBRE));
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
				QUERY_ALL_UNSENT_PATH, QUERY_ALL_UNSENT_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ONE_BY_ABREV_NAME_PATH, QUERY_ONE_BY_ABREV_NAME_URI_CODE);
		
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_PHOTO_STATE_PATH, UPDATE_PHOTO_STATE_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_ALL_PATH, QUERY_ALL_MODULE_CHART_URI_CODE);
		return DAO_URI_CODE;
	}

//////////////////////	
	
	/* OBTIENE NOMBRE DE COLUMNA */
	private String getAllColumnNames(String[] columnNames) {
		String s = "Column Names:\n";
		for (String t : columnNames) {
			s += t + ":\t";
		}
		return s + "\n";
	}

	/* PINTA TODA LA DATA DE UN CURSOR - TEST */
	private String printAllData(Cursor c)

	{

		if (c == null)
			return null;

		String s = "";

		int record_cnt = c.getColumnCount();

		Log.d("printAllData", "Total # of records: " + record_cnt);

		if (c.moveToFirst())

		{

			String[] columnNames = c.getColumnNames();

			Log.d("printAllData", getAllColumnNames(columnNames));

			s += getAllColumnNames(columnNames);

			do {

				String row = "";

				for (String columnIndex : columnNames)

				{

					int i = c.getColumnIndex(columnIndex);

					row += c.getString(i) + ":\t";

				}

				row += "\n";

				Log.d("printAllData", row);

				s += row;

			} while (c.moveToNext());

			Log.d("printAllData", "End Of Records");

		}

		return s;

	}
	
	
	
	
	
//////////////////////	
	
	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		AppPreferences appPreferences = AppPreferences.getInstance(context);
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);
		String groupBy = null;
		String having = null;
		switch (intUriCode) {
		case QUERY_ALL_MODULE_CHART_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_MODULO + " = " + appPreferences._loadCurrentModuleId();
			break;
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_DES_ABREV_NOMBRE + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_DES_ABREV_NOMBRE + " ASC ";
			break;
			
		case QUERY_ALL_UNSENT_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
			+ COL_FLG_ENVIADO + " = 0 ";
			
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_ID + " ASC ";
			break;
		case QUERY_ONE_BY_ABREV_NAME_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT
			+ COL_DES_ABREV_NOMBRE + " = ? ";
			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "QUERY");
		}
		result = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, sortOrder);
		result.moveToFirst();
		
//		 Cursor cur = db.rawQuery("SELECT  * FROM "+ "TB_NAME",null);
//		this.printAllData(cur);
	
		
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
		case UPDATE_PHOTO_STATE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_ID + " = ? ";
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
		Photo sonEntity = (Photo) entity;
		ContentValues values = new ContentValues();
		values.put(COL_DES_ABREV_NOMBRE,sonEntity.getStrAbrevName());
		values.put(COL_DES_FULL_NOMBRE, sonEntity.getStrFullName());
		values.put(COL_FLG_ENVIADO, sonEntity.isBooIsSent());
		values.put(COL_COD_MODULO, sonEntity.getintModule());
		return values;
	}

	public static Photo createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intId = cursor.getInt(cursor.getColumnIndex(COL_ID));
		String strAbrevName = cursor.getString(cursor
				.getColumnIndex(COL_DES_ABREV_NOMBRE));
		String strFullName = cursor.getString(cursor
				.getColumnIndex(COL_DES_FULL_NOMBRE));
		boolean booIsSent = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_ENVIADO))==1;
		int intModule = cursor.getInt(cursor.
				getColumnIndex(COL_COD_MODULO));
		Photo objPhoto = new Photo(intId, strAbrevName, strFullName, booIsSent, intModule);
		return objPhoto;
	}

	public static List<Photo> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Photo> lst = new ArrayList<Photo>();
		while (!cursor.isAfterLast()) {
			Photo obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
