package pe.beyond.lac.gestionmovil.daos;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Generic;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Question;
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

public class QuestionDAO extends BaseDAO {
	public static final String DAO_NAME = "QuestionDAO";
	public static final String TB_NAME = "GLS_GR_MAE_PREGUNTA";
	public static final String COL_ID = "_id";
	public static final String COL_COD_PREGUNTA = "COD_PREGUNTA_PRG";
	public static final String COL_COD_PLANTILLA = "COD_PLANTILLA_PLT";
	public static final String COL_COD_FLUJO = "COD_FLUJO_RFL";
	public static final String COL_COD_TIPODATO = "COD_TIPODATO_TDA";
	public static final String COL_COD_VALIDACION = "COD_VALIDACION_VAL";
	public static final String COL_DES_PREGUNTA = "DES_PREGUNTA_PRG";
	public static final String COL_FLG_OPCION = "FLG_OPCION_PRG";
	public static final String COL_FLG_EDICION = "FLG_EDICION_PRG";
	public static final String COL_NUM_KEY = "NUM_KEY_PRG";
	public static final String COL_NUM_ORDEN = "NUM_ORDEN_PRG";
	public static final String COL_COD_ESTADO = "COD_ESTADO_EST";
	public static final String COL_NUM_TIPO_ENTRADA = "NUM_TIPO_ENTRADA_PRG";
	public static final String COL_NUM_GRUPO = "NUM_GRUPO_PRG";
	public static final String COL_NUM_POSICION = "NUM_POSICION_PRG";

	private final String[][] TB_CREATE_INFO = new String[][] {
			{ COL_ID, Constants.INTEGER, Constants.PRIMARY_KEY },
			{ COL_COD_PREGUNTA, Constants.INTEGER },
			{ COL_COD_PLANTILLA, Constants.INTEGER },
			{ COL_COD_FLUJO, Constants.INTEGER },
			{ COL_COD_TIPODATO, Constants.INTEGER },
			{ COL_COD_VALIDACION, Constants.INTEGER },
			{ COL_DES_PREGUNTA, Constants.TEXT },
			{ COL_FLG_OPCION, Constants.BOOLEAN },
			{ COL_FLG_EDICION, Constants.BOOLEAN },
			{ COL_NUM_KEY, Constants.INTEGER },
			{ COL_NUM_ORDEN, Constants.INTEGER },
			{ COL_COD_ESTADO, Constants.INTEGER },
			{ COL_NUM_TIPO_ENTRADA, Constants.INTEGER },
			{ COL_NUM_GRUPO, Constants.INTEGER },
			{ COL_NUM_POSICION, Constants.INTEGER } };

	public static final int DAO_URI_CODE = 12;

	public static final int INSERT_URI_CODE = 1299;
	public static final String INSERT_PATH = TB_NAME + Constants.LBL_SLASH
			+ "Insert";
	public static final Uri INSERT_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ INSERT_PATH);

	public static final int QUERY_ONE_URI_CODE = 1201;
	public static final String QUERY_ONE_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryOne";
	public static final Uri QUERY_ONE_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ONE_PATH);

	public static final int QUERY_ALL_URI_CODE = 1202;
	public static final String QUERY_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "QueryAll";
	public static final Uri QUERY_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_ALL_PATH);

	public static final int QUERY_BY_FLUXID_CODE = 1203;
	public static final String QUERY_BY_FLUXID_PATH = TB_NAME
			+ Constants.LBL_SLASH + "QueryByFluxId";
	public static final Uri QUERY_BY_FLUXID_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ QUERY_BY_FLUXID_PATH);

	public static final int DELETE_ALL_URI_CODE = 1251;
	public static final String DELETE_ALL_PATH = TB_NAME + Constants.LBL_SLASH
			+ "DeleteAll";
	public static final Uri DELETE_ALL_URI = Uri.parse("content://"
			+ Constants.CONFIGURATION_PROVIDER_AUTHORITY + Constants.LBL_SLASH
			+ DELETE_ALL_PATH);

	@Override
	public boolean onCreate(final SQLiteDatabase db) {
		db.execSQL(createTable(TB_NAME, TB_CREATE_INFO));
		db.execSQL(createIndex(TB_NAME, COL_COD_PREGUNTA));
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
				QUERY_BY_FLUXID_PATH, QUERY_BY_FLUXID_CODE);

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
			selection = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_PREGUNTA + " = ? ";
			break;
		case QUERY_ALL_URI_CODE:
			sortOrder = " " + TB_NAME + Constants.LBL_DOT
					+ COL_COD_PREGUNTA + " ASC ";
			break;
		case QUERY_BY_FLUXID_CODE:
			selection = " " + TB_NAME + Constants.LBL_DOT + COL_COD_FLUJO
					+ " = ? ";
			sortOrder = " " + TB_NAME + Constants.LBL_DOT + COL_NUM_ORDEN
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
		Question sonEntity = (Question) entity;
		ContentValues values = new ContentValues();
		values.put(COL_COD_PREGUNTA, sonEntity.getIntQuestionId());
		values.put(COL_COD_PLANTILLA, sonEntity.getIntTemplateId());
		values.put(COL_COD_FLUJO, sonEntity.getIntFluxId());
		values.put(COL_COD_TIPODATO, sonEntity.getIntDataTypeId());
		values.put(COL_COD_VALIDACION, sonEntity.getIntValidationId());
		values.put(COL_DES_PREGUNTA, sonEntity.getStrQuestionDescription());
		values.put(COL_FLG_OPCION, sonEntity.isBooFlgOption());
		values.put(COL_FLG_EDICION, sonEntity.isBooFlgEdition());
		values.put(COL_NUM_KEY, sonEntity.getIntKey());
		values.put(COL_NUM_ORDEN, sonEntity.getIntOrderNum());
		values.put(COL_COD_ESTADO, sonEntity.getIntState());
		values.put(COL_NUM_TIPO_ENTRADA, sonEntity.getIntEntryType());
		values.put(COL_NUM_GRUPO, sonEntity.getIntGroup());
		values.put(COL_NUM_POSICION, sonEntity.getIntPosition());
		return values;
	}

	public static Question createObject(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		int intQuestionId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PREGUNTA));
		int intTemplateId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_PLANTILLA));
		int intFluxId = cursor.getInt(cursor.getColumnIndex(COL_COD_FLUJO));
		int intDataTypeId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_TIPODATO));
		int intValidationId = cursor.getInt(cursor
				.getColumnIndex(COL_COD_VALIDACION));
		String strQuestionDescription = cursor.getString(cursor
				.getColumnIndex(COL_DES_PREGUNTA));
		boolean booFlgOption = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_OPCION)) == 1;
		boolean booFlgEdition = cursor.getInt(cursor
				.getColumnIndex(COL_FLG_EDICION)) == 1;
		int intKey = cursor.getInt(cursor.getColumnIndex(COL_NUM_KEY));
		int intOrderNum = cursor.getInt(cursor.getColumnIndex(COL_NUM_ORDEN));
		int intState = cursor.getInt(cursor.getColumnIndex(COL_COD_ESTADO));
		int intEntryType = cursor.getInt(cursor
				.getColumnIndex(COL_NUM_TIPO_ENTRADA));
		int intGroup = cursor.getInt(cursor.getColumnIndex(COL_NUM_GRUPO));
		int intPosition = cursor
				.getInt(cursor.getColumnIndex(COL_NUM_POSICION));
		
		Question objQuestion = new Question(intQuestionId, intTemplateId,
				intFluxId, intDataTypeId, intValidationId,
				strQuestionDescription, booFlgOption, booFlgEdition, intKey,
				intOrderNum, intState, intEntryType, intGroup, intPosition);
		return objQuestion;
	}

	public static List<Question> createObjects(Cursor cursor) {
		if (cursor ==null || cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		List<Question> lst = new ArrayList<Question>();
		while (!cursor.isAfterLast()) {
			Question obj = createObject(cursor);
			obj.setStrTemporalAbrevAnswer(null);
			obj.setStrTemporalRealAnswer(null);
			lst.add(obj);
			cursor.moveToNext();
		}
		cursor.close();
		return lst;
	}
}
