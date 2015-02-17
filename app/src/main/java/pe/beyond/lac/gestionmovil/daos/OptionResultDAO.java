package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.OptionResult;
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

public class OptionResultDAO extends BaseDAO {
	public static final String DAO_NAME = "OptionResultDAO";
	public static final String TB_NAME = "GLS_GR_REL_RESULT_OPCION";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_RESULTADO = "IDE_RESULTADO_OPC";
	public static final String COL_COD_PREGUNTA = "COD_PREGUNTA_PRG";
	public static final String COL_COD_OPCION = "COD_OPCION_OPC";
	public static final String COL_FLG_EDICION = "FLG_EDICION_ROP";
	public static final String COL_DES_VALOR = "DES_VALOR_ROP";
	public static final String COL_DES_VALOR_REAL = "DES_VALOR_REAL_ROP";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_RESULTADO, Constants.INTEGER },
			{ COL_COD_PREGUNTA, Constants.INTEGER },
			{ COL_COD_OPCION, Constants.INTEGER },
			{ COL_FLG_EDICION, Constants.BOOLEAN },
			{ COL_DES_VALOR, Constants.TEXT },
			{ COL_DES_VALOR_REAL, Constants.TEXT },
			{ COL_COD_ESTADO, Constants.INTEGER } };

	public static final int DAO_URI_CODE = 11;

	public static final int INSERT_URI_CODE = 1199;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1101;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1102;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_BY_OPTIONID_CODE = 1103;
	public static final String QUERY_BY_OPTIONID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByOptionId";
	public static final Uri QUERY_BY_OPTIONID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + "/"
			+ QUERY_BY_OPTIONID_PATH);

	public static final int DELETE_ALL_URI_CODE = 1151;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

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
				QUERY_BY_OPTIONID_PATH, QUERY_BY_OPTIONID_CODE);

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
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_RESULTADO
					+ " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_RESULTADO
					+ " ASC ";
		case QUERY_BY_OPTIONID_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_OPCION
					+ " = ? ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_OPCION
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
		OptionResult sonEntity = (OptionResult) entity;
		ContentValues values = new ContentValues();
		values.put(COL_IDE_RESULTADO, sonEntity.getIntOptionResultId());
		values.put(COL_COD_PREGUNTA, sonEntity.getIntQuestionId());
		values.put(COL_COD_OPCION, sonEntity.getIntOptionId());
		values.put(COL_FLG_EDICION, sonEntity.isBooFlgEdition());
		values.put(COL_DES_VALOR, sonEntity.getStrValue());
		values.put(COL_DES_VALOR_REAL, sonEntity.getStrRealValue());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		return values;
	}

	public static OptionResult createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intOptionResultId = cursor.getInt(cursor
				.getColumnIndex(COL_IDE_RESULTADO));
		int intQuestionId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PREGUNTA));
		int intOptionId = cursor.getInt(cursor.getColumnIndex(COL_COD_OPCION));
		boolean booFlgEdition = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_EDICION)) == 1;
		String strValue = cursor
				.getString(cursor.getColumnIndex(COL_DES_VALOR));
		String strRealValue = cursor
				.getString(cursor.getColumnIndex(COL_DES_VALOR_REAL));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		OptionResult objRegister = new OptionResult(intOptionResultId, intQuestionId, intOptionId, booFlgEdition, strValue, strRealValue, intState);

		return objRegister;
	}

	public static List<OptionResult> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<OptionResult> lst = new ArrayList<OptionResult>();
		while (!cursor.isAfterLast()) {
			OptionResult objForm = createObject(cursor);
			lst.add(objForm);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
