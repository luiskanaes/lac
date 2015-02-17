package pe.beyond.lac.gestionmovil.interfaces;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public interface DAOInterface {

	public boolean onCreate(final SQLiteDatabase db);

	public boolean onUpgrade(final SQLiteDatabase db);

	public int loadDaoUris(final UriMatcher uriMatcher);

	public Cursor query(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			String[] projection, String selection, String[] selectionArgs,
			String sortOrder);

	public Uri insert(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			ContentValues values);
	
	public int delete(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			String selection, String[] selectionArgs);

	public int update(Uri uri, Context context, SQLiteDatabase db,int intUriCode,
			ContentValues values, String selection, String[] selectionArgs);
}
