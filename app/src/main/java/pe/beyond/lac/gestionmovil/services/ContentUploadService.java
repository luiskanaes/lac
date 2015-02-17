package pe.beyond.lac.gestionmovil.services;

import pe.beyond.lac.gestionmovil.threads.ContentUploadThread;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ContentUploadService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.w("GMD", "onStartCommand in SERVICE");
//		ContentUploadThread thrContentUpload = new ContentUploadThread(this.getBaseContext());
//		thrContentUpload.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		Log.w("GMD", "onCreate in SERVICE");
		ContentUploadThread thrContentUpload = new ContentUploadThread(this.getBaseContext());
		thrContentUpload.start();
		super.onCreate();
	}
}
