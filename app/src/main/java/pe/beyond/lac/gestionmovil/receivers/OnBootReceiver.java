package pe.beyond.lac.gestionmovil.receivers;

import pe.beyond.lac.gestionmovil.services.ContentUploadService;
import pe.beyond.lac.gestionmovil.services.NotificationBarService;
import pe.beyond.lac.gestionmovil.tracking.LocationListenerService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

	    Intent startNotificationService = new Intent(context, NotificationBarService.class);
        context.startService(startNotificationService);
        
		
		Intent startServiceIntent = new Intent(context, ContentUploadService.class);
        context.startService(startServiceIntent);
        Intent startTrackingIntent = new Intent(context, LocationListenerService.class);
        context.startService(startTrackingIntent);
    
        
        
	}

}
