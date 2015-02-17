package pe.beyond.lac.gestionmovil.asynctasks;

import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import android.os.AsyncTask;

public class InitialAsyncTask extends AsyncTask<Boolean, Boolean, Boolean>{
	private BaseActivity activity;
	public InitialAsyncTask(final BaseActivity activity){
		this.activity = activity;
	}
	
	
	@Override
	protected Boolean doInBackground(Boolean... params) {
		return null;
	}

}
