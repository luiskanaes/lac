package pe.beyond.lac.gestionmovil.tracking;

import java.util.Calendar;
import java.util.List;

import pe.beyond.gls.model.Track;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationListenerService extends Service implements
		LocationListener {

	public static final int UPDATE_MESSAGE = 1;
	public static final String ACTION_PROXIMTY_ALERT_INTENT = "pe.gmd.lac.gestionmovil.cast.PROXIMTY_ALERT";
	public static final String ACTION_PREFERNCES_CHANGED_INTENT = "pe.gmd.lac.gestionmovil.cast.PREFERENCES_CHANGED";
	public static final String ACTION_SET_START_OF_ACTIVITY = "pe.gmd.lac.gestionmovil.cast.SET_START_OF_ACTIVITY";
	public static final String ACTION_SET_END_OF_ACTIVITY = "pe.gmd.lac.gestionmovil.cast.SET_END_OF_ACTIVITY";

	private static final String TAG = LocationListenerService.class.toString();

	public static int INT_SAMPLE_DISTANCE = 5; // 10 MTS
	public static int INT_SAMPLE_TIME_INTERVAL = 15 * 1000; // 300
																// SECONDS
	private LocationManager locationManager;

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.i(TAG, "Received intent " + intent.toString());
			if (ACTION_PREFERNCES_CHANGED_INTENT.equals(intent.getAction())) {
				requestLocationUpdates();
				addProximityAlert();
			} else if (ACTION_PROXIMTY_ALERT_INTENT.equals(intent.getAction())) {
				handleProximityAlert(intent);
			}
		}

	};

	private void handleProximityAlert(Intent intent) {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		
		if (location == null) {
			// We'll just write an empty location
			location = new Location("prx");
		}
		
		
//		if (location == null) {
//			location = locationManager
//					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			if (location == null) {
//				// We'll just write an empty location
//				location = new Location("prx");
//			}
//		}

		if (intent.hasExtra(LocationManager.KEY_PROXIMITY_ENTERING)) {
			if (intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING,
					true) == true) {
				location.setProvider("prx_enter");
			} else {
				location.setProvider("prx_exit");
			}
		}
		addLocationToDB(location);
	}

	//
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
	 // Log.i(TAG, "onStartCommand()");
	 super.onStartCommand(intent, flags, startId);
	 // We want this service to continue running until it is explicitly
	 // stopped, so return sticky.
	
	 Log.d("onStartLocalizadorService", "onStartCommand");
	 return START_STICKY;
	 }

	@Override
	public void onCreate() {

		Log.i(TAG, "onCreate!");
		requestLocationUpdates();
		addProximityAlert();
		
		// TODO Normally this is declared in the Manifest
		IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction("com.shinetech.android.PREFERENCES_CHANGED");

		intentFilter.addAction(ACTION_PREFERNCES_CHANGED_INTENT);


		this.registerReceiver(this.broadcastReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		// Log.i(TAG, "onDestroy()");
		super.onDestroy();
		locationManager.removeUpdates(this);
		// unregisterReceiver(broadcastReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onLocationChanged(Location location) {
		addLocationToDB(location);
	}

	public int intCounter = 0;

	private void addLocationToDB(Location location) {
		// Log.i(TAG, "Location changed: " + location.toString());
		
		final int intFlgNuevo = 0;
		final int intUnrealId = -1;
		
		if (location.getAccuracy() > 250) {
			// Avoid registering points over 250 m. in accuracy.
			return;
		}
		if (intCounter == 0) {
			intCounter++;
		} else {
			intCounter = 0;
			int intProviderId = location.getProvider().equals(
					LocationManager.GPS_PROVIDER) ? 0 : 1;
			long lngTime = Calendar.getInstance().getTimeInMillis();
			Track objTrack = new Track(
										intProviderId,
										(float) location.getLatitude(),					
										(float) location.getLongitude(), 
										location.getAccuracy(),
										lngTime,
										intFlgNuevo,
										intUnrealId
									);
			ContentValues values = TrackDAO.createContentValues(objTrack);
			this.getApplication().getBaseContext().getContentResolver().insert(TrackDAO.INSERT_URI, values);

			Intent intent = new Intent("com.shinetech.android.UPDATE_UI");
			sendBroadcast(intent);
		}
		// Log.i(TAG, "UPDATE_UI intent broadcasted");
	}

	public void onProviderEnabled(String provider) {
		// Log.i(TAG, "Provider " + provider + " is now enabled.");
	}

	public void onProviderDisabled(String provider) {
		// Log.i(TAG, "Provider " + provider + " is now disabled.");
	}

	public void onStatusChanged(String provider, int i, Bundle b) {
		// Log.i(TAG, "Provider " + provider + " has changed status to " + i);
	}

	private void requestLocationUpdates() {
		// int sampleDistance = 5;
		// int sampleInterval = 15 * 1000;
		// Log.i(TAG, "Setting up location updates with sample distance "
		// + sampleDistance + " m and sample interval " + sampleInterval
		// + " ms.");

		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
		List<String> enabledProviders = this.locationManager.getProviders(true);

		for (String provider : enabledProviders) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				this.locationManager.requestLocationUpdates(provider, INT_SAMPLE_TIME_INTERVAL, INT_SAMPLE_DISTANCE, this);
				Log.i(TAG, provider);
				Log.i(TAG, "INTERVALOS A USAR EN SERVICIO: "+ INT_SAMPLE_TIME_INTERVAL + " y => "+ INT_SAMPLE_DISTANCE);
			}

		}

	}

	private void addProximityAlert() {
		// Log.i(TAG, "addProximityAlert()");
		int vicinityRadius = 0;
		double latitude = 0f;
		double longitude = 0f;

		long expiration = -1;

		Intent intent = new Intent(ACTION_PROXIMTY_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);

//		locationManager.addProximityAlert(latitude, longitude, vicinityRadius,
//				expiration, proximityIntent);

		IntentFilter filter = new IntentFilter(ACTION_PROXIMTY_ALERT_INTENT);
		registerReceiver(this.broadcastReceiver, filter);
	}

}
