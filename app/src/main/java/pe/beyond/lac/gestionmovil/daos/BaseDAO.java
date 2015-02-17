package pe.beyond.lac.gestionmovil.daos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import pe.beyond.lac.gestionmovil.interfaces.DAOInterface;
import pe.beyond.lac.gestionmovil.utils.Constants;

public class BaseDAO implements DAOInterface{
	@Override
	public boolean onCreate(SQLiteDatabase db) {
		return false;
	}

	@Override
	public boolean onUpgrade(SQLiteDatabase db) {
		return false;
	}

	@Override
	public int loadDaoUris(UriMatcher uriMatcher) {
		return 0;
	}

	@Override
	public Cursor query(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, String selection, String[] selectionArgs) {
		return -1;
	}

	@Override
	public int update(Uri uri, Context context, SQLiteDatabase db,
			int intUriCode, ContentValues values, String selection,
			String[] selectionArgs) {
		return -1;
	}

	protected String createTable(final String strTableName,final String[][]strTableValues){
    	StringBuilder bldString = new StringBuilder("CREATE TABLE IF NOT EXISTS " + strTableName + "( ");
    	String strValue = null;
    	for (int ite1 = 0; ite1<strTableValues.length;ite1++){
    		for (int ite2 = 0; ite2<strTableValues[ite1].length;ite2++){
    			strValue = strTableValues[ite1][ite2];
    			bldString.append(strValue);
        		if (ite2 == (strTableValues[ite1].length-1) && ite1 < (strTableValues.length-1)){
        			bldString.append(Constants.LBL_COMMA);
        		}
        	}
    	}
    	bldString.append(" );");
    	
    	String strResult = bldString.toString();
    	return strResult;
    }
	
	protected String createIndex(final String strTableName,final String strPrimaryKey){
		StringBuilder bld = new StringBuilder("CREATE INDEX IF NOT EXISTS ")
				.append(strTableName).append("_INDEX ON ").append(strTableName)
				.append(" (").append(strPrimaryKey).append(" );");

		String strResult = bld.toString();
		return strResult;
    }
	
	protected String dropTable(final String strTableName){
		StringBuilder bld = new StringBuilder("DROP TABLE IF EXISTS ")
				.append(strTableName).append(";");
		
		String strResult = bld.toString();
		return strResult;
    }
	
	protected String dropIndex(final String strTableName){
		StringBuilder bld = new StringBuilder("DROP INDEX IF EXISTS ")
				.append(strTableName);
		
		String strResult = bld.toString();
		return strResult;
    }
	
	public static ContentValues createContentValues(final HashMap<?, ?> hmpContent) {
		hmpContent.keySet();
        final ContentValues contentValues = new ContentValues();  
        Iterator<?> iterator = hmpContent.entrySet().iterator();
        while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String,Object> pair = (Map.Entry<String, Object>) iterator.next();
            iterator.remove();
            String strKey = pair.getKey();
            Object objValue = pair.getValue();
            if (objValue instanceof String) {
            	contentValues.put(strKey, (String) objValue);
            } else if (objValue instanceof Boolean) {
            	contentValues.put(strKey, (Boolean) objValue);
            } else if (objValue instanceof Integer) {
            	contentValues.put(strKey, (Integer) objValue);
            } else if (objValue instanceof Long) {
            	contentValues.put(strKey, (Long) objValue);
            }            
        }
        return contentValues;
    }
	
	public static ContentValues createContentValues(final String strKey, final Object objValue) {
        final ContentValues contentValues = new ContentValues();
        if (objValue instanceof String) {
        	contentValues.put(strKey, (String) objValue);
        } else if (objValue instanceof Boolean) {
        	contentValues.put(strKey, (Boolean) objValue);
        } else if (objValue instanceof Integer) {
        	contentValues.put(strKey, (Integer) objValue);
        } else if (objValue instanceof Long) {
        	contentValues.put(strKey, (Long) objValue);
        } else if (objValue instanceof Float) {
        	contentValues.put(strKey, (Float) objValue);
        }
        return contentValues;
    }
}
