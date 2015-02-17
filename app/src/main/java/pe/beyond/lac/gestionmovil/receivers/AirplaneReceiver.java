package pe.beyond.lac.gestionmovil.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirplaneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		// OBTIENE SI EL MODO AVION ESTA ACTIVADO
		boolean isEnabled = Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1;

		if (isEnabled == true) {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
			Intent oIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			oIntent.putExtra("state", false);
			context.sendBroadcast(oIntent);
		}
		
		
		
	}

}
