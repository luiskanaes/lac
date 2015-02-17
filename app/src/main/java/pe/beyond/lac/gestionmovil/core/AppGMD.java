package pe.beyond.lac.gestionmovil.core;

import android.app.Application;
import android.view.KeyEvent;

public class AppGMD extends Application {

	public int _stateList;
	public int _executeNextPosition;
	public int _NextListingPosition;
	public String _strIMEI;
	public String _deviceName = android.os.Build.MODEL;
	public String _deviceMan = android.os.Build.MANUFACTURER;
//	public int _DevicePrevious = (_deviceMan.contains("samsung") && _deviceName.contains("GT-B5510L"))? KeyEvent.KEYCODE_ALT_RIGHT : KeyEvent.KEYCODE_ALT_LEFT;
	
	
	
	
	

}
