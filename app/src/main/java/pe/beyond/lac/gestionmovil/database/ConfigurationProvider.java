package pe.beyond.lac.gestionmovil.database;

import java.lang.reflect.Method;

import pe.beyond.lac.gestionmovil.utils.Constants;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.SparseArray;


public class ConfigurationProvider extends ContentProvider {
    private UriMatcher uriMatcher;
    private ConfigurationDBHelper dbHelper;
    private SQLiteDatabase db; 
    private static SparseArray<String> hmpDaoUris;
    
    public void loadDaoTables(){
    	Class<?> params[] = {SQLiteDatabase.class};
	    Object paramsObj[] = {db};	     
    	for (String strDaoName : Constants.DB_ALL_DAO_NAMES)  {
    		invokeDaoMethod(strDaoName, "onCreate", params, paramsObj);
    	}
    }
    
    public void loadDaoUris(){
    	hmpDaoUris = new SparseArray<String>(Constants.DB_ALL_DAO_NAMES.length);  
    	Class<?> params[] = {UriMatcher.class};
	    Object paramsObj[] = {uriMatcher};	     
    	for (String strDaoName : Constants.DB_ALL_DAO_NAMES)  {
    		int intDaoHeader = (Integer) invokeDaoMethod(strDaoName, "loadDaoUris", params, paramsObj);
    		hmpDaoUris.put(intDaoHeader, strDaoName);
    	}
    }
    
    private Object invokeDaoMethod(String strAction, Class<?>[] params, Object[] paramsObj){
    	int intUriCode = uriMatcher.match((Uri) paramsObj[0]);
		int intDaoKey = intUriCode/(int) Math.pow(10, Constants.URI_HEADER_DIGITS);
		String strDaoName = hmpDaoUris.get(intDaoKey);
		paramsObj[3] = intUriCode;
		
		return invokeDaoMethod(strDaoName, strAction, params, paramsObj);
    }
    private Object invokeDaoMethod(String strDaoName, String strAction, Class<?>[] params, Object[] paramsObj){
		Object result = null;
    	try {	    
		    Class<?> daoClass = Class.forName(strDaoName);
			Object daoObject = daoClass.newInstance();
			Method method = daoClass.getDeclaredMethod(strAction, params);
			result = method.invoke(daoObject, paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    @Override
	public boolean onCreate() {
    	dbHelper = new ConfigurationDBHelper(getContext());
    	db = dbHelper.getWritableDatabase();
    	
    	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	loadDaoUris();
    	loadDaoTables();
        return true;
	}
    
    
    @Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Class<?> params[] = {Uri.class,Context.class,SQLiteDatabase.class,int.class,String[].class,String.class,String[].class,String.class};
	    Object paramsObj[] = {uri,getContext(),db,Constants.URI_NO_URI,projection,selection,selectionArgs,sortOrder};	    
	    return (Cursor) invokeDaoMethod("query",params,paramsObj);
	}
    
    @Override
	public Uri insert(Uri uri, ContentValues values) {
		Class<?> params[] = {Uri.class,Context.class,SQLiteDatabase.class,int.class,ContentValues.class};
	    Object paramsObj[] = {uri,getContext(),db,Constants.URI_NO_URI,values};	    
	    return (Uri) invokeDaoMethod("insert",params,paramsObj);
	}

    @Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Class<?> params[] = {Uri.class,Context.class,SQLiteDatabase.class,int.class,String.class,String[].class};
	    Object paramsObj[] = {uri,getContext(),db,Constants.URI_NO_URI,selection,selectionArgs};	    
	    Object objResult = invokeDaoMethod("delete",params,paramsObj);
	    int intResult = ((Integer)objResult).intValue();
	    return intResult;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Class<?> params[] = {Uri.class,Context.class,SQLiteDatabase.class,int.class,ContentValues.class,String.class,String[].class};
	    Object paramsObj[] = {uri,getContext(),db,Constants.URI_NO_URI,values,selection,selectionArgs};	    
	    
	    Object objResult = invokeDaoMethod("update",params,paramsObj);
	    int intResult = ((Integer)objResult).intValue();
	    return intResult;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
    protected void finalize() throws Throwable {
	    db.close();
	    dbHelper.close();
	    super.finalize();
    }
}
