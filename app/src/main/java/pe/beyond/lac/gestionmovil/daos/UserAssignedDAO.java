package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.UserAssigned;
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

public class UserAssignedDAO extends BaseDAO {
	public static final String DAO_NAME = "UserAssignedDAO";
	public static final String TB_NAME = "GLS_GR_REL_ASIG_USUARIO";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_USUARIO = "IDE_USUARIO_USU";
	public static final String COL_COD_BASE = "COD_BASE_BAS";
	public static final String COL_COD_MODULO = "COD_MODULO_MOD";
	public static final String COL_COD_CODIGO = "COD_CODIGO_AUS";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_FLG_MODULODEFAULT = "FLG_MODULODEFAULT_AUS";

	// Campos adicionales no contemplados en el diseño de BD
	public static final String COL_DES_BASE = "DES_BASE_BAS";
	public static final String COL_DES_EMPRESA = "DES_EMPRESA_EMP";
	public static final String COL_FEC_LAST_SYNC = "FEC_LAST_SYNC_AUS";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_USUARIO, Constants.INTEGER },
			{ COL_COD_BASE, Constants.INTEGER },
			{ COL_COD_MODULO, Constants.LONG },
			{ COL_COD_CODIGO, Constants.LONG },
			{ COL_COD_ESTADO, Constants.LONG },
			{ COL_FLG_MODULODEFAULT, Constants.LONG },
			{ COL_DES_BASE, Constants.TEXT }, 
			{ COL_DES_EMPRESA, Constants.TEXT }, 
			{ COL_FEC_LAST_SYNC, Constants.LONG } };

	public static final int DAO_URI_CODE = 24;

	public static final int INSERT_URI_CODE = 2499;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2401;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);
	
	public static final int QUERY_ALL_URI_CODE = 2402;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_BY_USER_MODULE_URI_CODE = 2403;
	public static final String QUERY_BY_USER_MODULE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryByUserModule";
	public static final Uri QUERY_BY_USER_MODULE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_USER_MODULE_PATH);
	
	public static final int QUERY_USER_ASIGNED_MODULE_BYUSER_URI_CODE = 2404;
	public static final String QUERY_USER_ASIGNED_MODULE_BYUSER_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryUserAsignedModule";
	public static final Uri QUERY_USER_ASIGNED_MODULE_BYUSER_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_USER_ASIGNED_MODULE_BYUSER_PATH);
	
	public static final int DELETE_ALL_URI_CODE = 2451;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);
	
	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_IDE_USUARIO));
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
				QUERY_BY_USER_MODULE_PATH, QUERY_BY_USER_MODULE_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_USER_ASIGNED_MODULE_BYUSER_PATH, QUERY_USER_ASIGNED_MODULE_BYUSER_URI_CODE);
		
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
		
		AppPreferences appPreferences = AppPreferences.getInstance(context);
		
		switch (intUriCode) {
		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO
					+ " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO
					+ " ASC ";
			break;
		case QUERY_BY_USER_MODULE_URI_CODE:
			// usado para buscar los perfiles a los que puede acceder el usuario
			queryBuilder.setTables(" " + UserAssignedDAO.TB_NAME + " LEFT JOIN " + ModuleDAO.TB_NAME + " ON ( " + TB_NAME
					+ Constants.LBL_DOT + COL_COD_MODULO + " = "
					+ ModuleDAO.TB_NAME + Constants.LBL_DOT
					+ ModuleDAO.COL_COD_MODULO + ") ");

			selection = " " + ModuleDAO.TB_NAME + Constants.LBL_DOT
					+ ModuleDAO.COL_COD_ESTADO + " = 1 AND " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO
					+ " = ? ";

			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO
					+ " ASC ";
			break;
		
		// Query usado para listar los módulos asignados al usuario actual
		case QUERY_USER_ASIGNED_MODULE_BYUSER_URI_CODE:
			queryBuilder.setTables(" " + UserAssignedDAO.TB_NAME + Constants.LBL_COMMA + ModuleDAO.TB_NAME + " ");
			
			projection = new String[] 
			{
				" DISTINCT " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " AS " + Constants.COLUMN_NAME_MODULE_COD + " ",
				" " + ModuleDAO.TB_NAME + Constants.LBL_DOT + ModuleDAO.COL_DES_MODULO + " AS " + Constants.COLUMN_NAME_MODULE_DESC + " "
			};
			
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " = " + ModuleDAO.TB_NAME + Constants.LBL_DOT + ModuleDAO.COL_COD_MODULO + 
					" AND " + TB_NAME + Constants.LBL_DOT + COL_IDE_USUARIO + " = " + appPreferences._loadUserId();
			
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_MODULO + " ASC ";
			
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
		UserAssigned sonEntity = (UserAssigned) entity;
		ContentValues values = new ContentValues();
		values.put(COL_IDE_USUARIO, sonEntity.getIntUserId());
		values.put(COL_COD_BASE, sonEntity.getIntBaseId());
		values.put(COL_COD_MODULO, sonEntity.getIntModuleId());
		values.put(COL_COD_CODIGO, sonEntity.getStrUserCode());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_FLG_MODULODEFAULT, sonEntity.isBooFlgDefaultModule());
		values.put(COL_DES_BASE, sonEntity.getStrBase());
		values.put(COL_DES_EMPRESA, sonEntity.getStrCompany());
		values.put(COL_FEC_LAST_SYNC, sonEntity.getLngLastTimeSynced());

		return values;
	}

	public static UserAssigned createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intUserId = cursor.getInt(cursor.getColumnIndex(COL_IDE_USUARIO));
		int intBaseId = cursor.getInt(cursor.getColumnIndex(COL_COD_BASE));
		int intModuleId = cursor.getInt(cursor.getColumnIndex(COL_COD_MODULO));
		String strUserCode = cursor.getString(cursor.getColumnIndex(COL_COD_CODIGO));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		boolean booFlgDefaultModule = cursor.getLong(cursor
				.getColumnIndex(COL_FLG_MODULODEFAULT)) == 1;
		String strBase = cursor.getString(cursor.getColumnIndex(COL_DES_BASE));
		String strCompany = cursor.getString(cursor.getColumnIndex(COL_DES_EMPRESA));
		long lngLastTimeSynced = cursor.getLong(cursor.getColumnIndex(COL_FEC_LAST_SYNC));

		UserAssigned objUserAssigned = new UserAssigned(intUserId, intBaseId,
				intModuleId, strUserCode, intState, booFlgDefaultModule,
				strBase, strCompany, lngLastTimeSynced);
		return objUserAssigned;
	}

	public static List<UserAssigned> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<UserAssigned> lst = new ArrayList<UserAssigned>();
		while (!cursor.isAfterLast()) {
			UserAssigned obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
