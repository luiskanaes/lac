package pe.beyond.lac.gestionmovil.database;

import java.lang.reflect.Method;

import pe.beyond.lac.gestionmovil.utils.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConfigurationDBHelper extends SQLiteOpenHelper {
	public SQLiteDatabase db;	
	
	public ConfigurationDBHelper(Context context) {
		super(context, Constants.DB_NAME_CONFIGURATION, null, Constants.DB_VERSION_CONFIGURATION);
	}

	private Object invokeDaoMethod(String strDaoName,String strAction, Class<?>[] clsParams, Object[] objParams){
    	Object objResult = null;
    	try {	    
		    Class<?> clsDao = Class.forName(strDaoName);
			Object objDao = clsDao.newInstance();
			Method mtdToDo = clsDao.getDeclaredMethod(strAction, clsParams);
			objResult = mtdToDo.invoke(objDao, objParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return objResult;
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Class<?> params[] = {SQLiteDatabase.class};
	    Object paramsObj[] = {db};
	    for (String strDaoName : Constants.DB_ALL_DAO_NAMES)  {
			invokeDaoMethod(strDaoName,"onCreate",params,paramsObj);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Class<?> params[] = {SQLiteDatabase.class,Integer.class,Integer.class};
	    Object paramsObj[] = {db,oldVersion,newVersion};
	    for (String strDaoName : Constants.DB_ALL_DAO_NAMES)  {
			invokeDaoMethod(strDaoName,"onUpgrade",params,paramsObj);
		}
	}
}

