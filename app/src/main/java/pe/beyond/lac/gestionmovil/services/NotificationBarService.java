package pe.beyond.lac.gestionmovil.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NotificationBarService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//Log.v("NotificationBarService-onCreate()", "inicio del servicio");
		handler.postDelayed(runnable, 250);
		super.onCreate();

	}

	private Handler handler = new Handler();

	private Runnable runnable = new Runnable() {

		public void run() {

			//Log.v("NotificationBarService", "NOTIFICACION");
			
			Object sbservice = getSystemService("statusbar");
			Class<?> statusbarManager;
			Method showsb;
			try {
				statusbarManager = Class
						.forName("android.app.StatusBarManager");
				showsb = statusbarManager.getMethod("collapse");
				showsb.invoke(sbservice);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			handler.postDelayed(this, 250); /* and there goes the "trick" */
		}
	};

}
