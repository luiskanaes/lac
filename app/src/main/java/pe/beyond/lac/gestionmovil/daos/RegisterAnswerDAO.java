package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.RegisterAnswer;
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

public class RegisterAnswerDAO extends BaseDAO {
	public static final String DAO_NAME = "RegisterAnswerDAO";
	public static final String TB_NAME = "GLS_GR_TRX_REGISTRO_PREG";
	public static final String COL_ID = "_id";
	public static final String COL_IDE_LISTADO = "IDE_LISTADO_LIS";
	public static final String COL_NUM_DESCARGA = "NUM_DESCARGA_LIS";
	public static final String COL_COD_PREGUNTA = "COD_PREGUNTA_PRG";
	public static final String COL_DES_VALOR = "DES_VALOR_TRP";
	public static final String COL_DES_VALOR_REAL = "DES_VALOR_REAL_TRP";
	public static final String COL_FLG_ACTUALIZADO = "FLG_ACTUALIZADO_TRP";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_IDE_LISTADO, Constants.INTEGER },
			{ COL_NUM_DESCARGA, Constants.INTEGER },
			{ COL_COD_PREGUNTA, Constants.INTEGER },
			{ COL_DES_VALOR, Constants.TEXT },
			{ COL_DES_VALOR_REAL, Constants.TEXT },
			{ COL_FLG_ACTUALIZADO, Constants.BOOLEAN } };

	public static final int DAO_URI_CODE = 22;

	public static final int INSERT_URI_CODE = 2299;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 2201;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 2202;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_ALL_BY_REGISTER_ID_URI_CODE = 2203;
	public static final String QUERY_ALL_BY_REGISTER_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryAllByRegisterId";
	public static final Uri QUERY_ALL_BY_REGISTER_ID_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_ALL_BY_REGISTER_ID_PATH);

	public static final int QUERY_UPLOAD_BY_REGISTER_ID_URI_CODE = 2204;
	public static final String QUERY_UPLOAD_BY_REGISTER_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryUploadByRegisterId";
	public static final Uri QUERY_UPLOAD_BY_REGISTER_ID_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + QUERY_UPLOAD_BY_REGISTER_ID_PATH);

	public static final int DELETE_ALL_URI_CODE = 2251;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	public static final int UPDATE_BY_PK_URI_CODE = 2252;
	public static final String UPDATE_BY_PK_PATH = TB_NAME
			+ Constants.LBL_SLASH + "UpdateByPK";
	public static final Uri UPDATE_BY_PK_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_BY_PK_PATH);

	public static final int UPDATE_BY_LIST_ID_URI_CODE = 2253;
	public static final String UPDATE_BY_LIST_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "UpdateByListId";
	public static final Uri UPDATE_BY_LIST_ID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ UPDATE_BY_LIST_ID_PATH);

	public static final int QUERY_UPLOAD_BY_REGISTER_UPDATED_URI_CODE = 2254;
	public static final String QUERY_UPLOAD_BY_REGISTER_UPDATED_PATH = TB_NAME
			+ Constants.LBL_SLASH + "UpdateByRegisterUpdated";
	public static final Uri QUERY_UPLOAD_BY_REGISTER_UPDATED_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH
					+ QUERY_UPLOAD_BY_REGISTER_UPDATED_PATH);

	public static final int UPDATE_BY_LIST_QUESTION_ID_URI_CODE = 2255;
	public static final String UPDATE_BY_LIST_QUESTION_ID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "UpdateByListQuestionId";
	public static final Uri UPDATE_BY_LIST_QUESTION_ID_URI = Uri
			.parse("content://" + Constants.CONFIGURATION_PROVIDER_AUTHORITY
					+ Constants.LBL_SLASH + UPDATE_BY_LIST_QUESTION_ID_PATH);

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
				QUERY_ALL_BY_REGISTER_ID_PATH,
				QUERY_ALL_BY_REGISTER_ID_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_UPLOAD_BY_REGISTER_ID_PATH,
				QUERY_UPLOAD_BY_REGISTER_ID_URI_CODE);

		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				DELETE_ALL_PATH, DELETE_ALL_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_BY_PK_PATH, UPDATE_BY_PK_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_BY_LIST_ID_PATH, UPDATE_BY_LIST_ID_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				QUERY_UPLOAD_BY_REGISTER_UPDATED_PATH,
				QUERY_UPLOAD_BY_REGISTER_UPDATED_URI_CODE);
		uriMatcher.addURI(Constants.CONFIGURATION_PROVIDER_AUTHORITY,
				UPDATE_BY_LIST_QUESTION_ID_PATH,
				UPDATE_BY_LIST_QUESTION_ID_URI_CODE);
		return DAO_URI_CODE;
	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;
		Uri notificationUri = QUERY_ALL_URI;

		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TB_NAME);

		String groupBy = null;
		String having = null;
		switch (intUriCode) {

		case QUERY_ONE_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? AND " + TB_NAME + Constants.LBL_DOT
					+ COL_NUM_DESCARGA + " = ? AND " + TB_NAME
					+ Constants.LBL_DOT + COL_COD_PREGUNTA + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " ASC, " + TB_NAME + Constants.LBL_DOT + COL_COD_PREGUNTA
					+ " ASC";
			// TODO UNCOMMENT THIS
			// selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_REGISTRO
			// + " = ?";
			break;
		case QUERY_ALL_BY_REGISTER_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? AND " + TB_NAME + Constants.LBL_DOT
					+ COL_NUM_DESCARGA + " = ? ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_COD_PREGUNTA
					+ " ASC ";
			break;
		case QUERY_UPLOAD_BY_REGISTER_ID_URI_CODE:

			queryBuilder.setTables(" " + RegisterAnswerDAO.TB_NAME
					+ " INNER JOIN " + QuestionDAO.TB_NAME + " ON ( " + TB_NAME
					+ Constants.LBL_DOT + RegisterAnswerDAO.COL_COD_PREGUNTA
					+ " = " + QuestionDAO.TB_NAME + Constants.LBL_DOT
					+ QuestionDAO.COL_COD_PREGUNTA + " ) ");
			selection = " " + RegisterAnswerDAO.TB_NAME + Constants.LBL_DOT
					+ RegisterAnswerDAO.COL_IDE_LISTADO + " = ? AND "
					+ RegisterAnswerDAO.TB_NAME + Constants.LBL_DOT
					+ RegisterAnswerDAO.COL_NUM_DESCARGA + " = ? ";
			sortOrder = " " + RegisterAnswerDAO.TB_NAME + Constants.LBL_DOT
					+ RegisterAnswerDAO.COL_COD_PREGUNTA + " ASC ";
			break;
		case QUERY_UPLOAD_BY_REGISTER_UPDATED_URI_CODE:

			queryBuilder.setTables(" " + RegisterAnswerDAO.TB_NAME
					+ " INNER JOIN " + QuestionDAO.TB_NAME + " ON ( " + TB_NAME
					+ Constants.LBL_DOT + RegisterAnswerDAO.COL_COD_PREGUNTA
					+ " = " + QuestionDAO.TB_NAME + Constants.LBL_DOT
					+ QuestionDAO.COL_COD_PREGUNTA + " ) ");
			selection = " " + RegisterAnswerDAO.TB_NAME + Constants.LBL_DOT
					+ RegisterAnswerDAO.COL_FLG_ACTUALIZADO + " = ? ";
			sortOrder = " " + RegisterAnswerDAO.TB_NAME + Constants.LBL_DOT
					+ RegisterAnswerDAO.COL_COD_PREGUNTA + " ASC ";
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
	public Uri insert(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values) {
		Uri uriInserted = null;

		switch (intUriCode) {
		case INSERT_URI_CODE:
			/*
			 * INSERT DE RESPUESTAS PARA EL DUPLICADO DE SUMINISTROS
			 */

			// UBICAR IDS
//			switch (AppPreferences.getInstance(context)._loadCurrentModuleId()) {
//			case 1:
//
//				break;
//			case 2:
//
//				break;
//			case 3:
//
//				break;
//			default:
//				break;
//			}
//
//			if (AppPreferences.getInstance(context)._loadCurrentModuleId() == 1) {
//
//			}
//			String strQuery = "SELECT COUNT(1) FROM GLS_GR_MAE_LISTADO WHERE ";
//			Cursor curIds = db.rawQuery("", null);
//			RegisterAnswer obj;

			break;
		default:
			throw new SQLException("INVALID URI :" + uri + " --- DAO: "
					+ TB_NAME + " --- ACTION: " + "INSERT");
		}
		long lngResultId = db.insert(TB_NAME, null, values);

		if (lngResultId > -1) {
			uriInserted = ContentUris.withAppendedId(uri, lngResultId);
			context.getContentResolver().notifyChange(uriInserted, null);
		} else {
			uriInserted = ContentUris.withAppendedId(uri, 0);
		}
		return uriInserted;
	}

	@Override
	public int delete(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String strWhereClause, String[] strWhereArgs) {
		int result = -1;
		return result;
	}

	@Override
	public int update(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values, String selection,
			String[] selectionArgs) {
		int result = -1;
		String tableName = TB_NAME;
		Uri notificationUri = QUERY_ALL_URI;
		switch (intUriCode) {
		case UPDATE_BY_PK_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? " + " AND " + TB_NAME + Constants.LBL_DOT
					+ COL_NUM_DESCARGA + " = ? " + " AND " + TB_NAME
					+ Constants.LBL_DOT + COL_COD_PREGUNTA + " = ? ";
			break;
		case UPDATE_BY_LIST_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? ";
			break;
		case UPDATE_BY_LIST_QUESTION_ID_URI_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_IDE_LISTADO
					+ " = ? " + " AND " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_PREGUNTA + " = ? ";
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
		RegisterAnswer sonEntity = (RegisterAnswer) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_PREGUNTA, sonEntity.getIntQuestionId());
		values.put(COL_IDE_LISTADO, sonEntity.getIntListId());
		values.put(COL_NUM_DESCARGA, sonEntity.getIntDownloadCounter());
		values.put(COL_DES_VALOR, sonEntity.getStrValue());
		values.put(COL_DES_VALOR_REAL, sonEntity.getStrValueReal());
		values.put(COL_FLG_ACTUALIZADO, sonEntity.isBooIsUpdated());
		return values;
	}

	public static RegisterAnswer createObject(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		int intListId = cursor.getInt(cursor.getColumnIndex(COL_IDE_LISTADO));
		int intDownloadCounter = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_DESCARGA));
		int intQuestionId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PREGUNTA));
		String strValue = cursor
				.getString(cursor.getColumnIndex(COL_DES_VALOR));
		String strValueReal = cursor.getString(cursor
				.getColumnIndex(COL_DES_VALOR_REAL));
		boolean booIsUpdated = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_ACTUALIZADO)) == 1;
		RegisterAnswer objRegisterAnswer = new RegisterAnswer(intListId,
				intDownloadCounter, intQuestionId, strValue, strValueReal,
				booIsUpdated);

		try {
			int intTemplateId = cursor.getInt(cursor
					.getColumnIndex(QuestionDAO.COL_COD_PLANTILLA));
			objRegisterAnswer.setIntTemplateId(intTemplateId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objRegisterAnswer;
	}

	public static List<RegisterAnswer> createObjects(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		List<RegisterAnswer> lst = new ArrayList<RegisterAnswer>();
		while (!cursor.isAfterLast()) {
			RegisterAnswer obj = createObject(cursor);
			lst.add(obj);
			cursor.moveToNext();
		}
		return lst;
	}
}
