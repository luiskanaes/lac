package pe.beyond.lac.gestionmovil.tracking;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class ShowStoredLocationActivity extends ListActivity {
	private static final String TAG = "LocationTrackerActivity";

	private SimpleCursorAdapter cursorAdapter;

	public SimpleCursorAdapter getCursorAdapter() {
		return cursorAdapter;
	}

	public void setCursorAdapter(SimpleCursorAdapter cursorAdapter) {
		this.cursorAdapter = cursorAdapter;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "Received UPDATE_UI message");
			getCursorAdapter().getCursor().requery();
			getCursorAdapter().notifyDataSetChanged();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracking_stored_locations);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ComponentName locationListenerServiceName = new ComponentName(getPackageName(),
				LocationListenerService.class.getName());
		startService(new Intent().setComponent(locationListenerServiceName));
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();
		String[] from = { TrackDAO.COL_COD_PROVIDER, TrackDAO.COL_CAN_LATITUD, TrackDAO.COL_CAN_LONGITUD,
				TrackDAO.COL_POR_PRECISION, TrackDAO.COL_FEC_HORA};
		int[] to = { R.id.locationname, R.id.locationlatitude, R.id.locationlongitude, R.id.locationaccuracy,
				R.id.locationtime };
		
		Cursor cursor = getContentResolver().query(TrackDAO.QUERY_ALL_URI, null, null, null, null);
		cursorAdapter = new LocationCursorAdapter(this, R.layout.tracking_stored_locations_row_layout,
				cursor, from, to);
		this.setListAdapter(cursorAdapter);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.shinetech.android.UPDATE_UI");
		this.registerReceiver(this.broadcastReceiver, intentFilter);
		getCursorAdapter().getCursor().requery();
		getCursorAdapter().notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();
		this.unregisterReceiver(this.broadcastReceiver);
	}
}