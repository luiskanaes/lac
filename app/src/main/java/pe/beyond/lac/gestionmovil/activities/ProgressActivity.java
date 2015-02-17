package pe.beyond.lac.gestionmovil.activities;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Progress;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.adapters.ProgressAdapter;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProgressActivity extends Activity {
	private ListView listView;
	private List<Progress> listProgress;
	private ProgressAdapter progressAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_layout);

		listProgress = new ArrayList<Progress>();

		progressAdapter = new ProgressAdapter(this);

		listView = (ListView) this.findViewById(R.id.lstvProgresses);
		listView.setAdapter(progressAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long id) {
				Bundle arguments = new Bundle();
				Progress progress = (Progress) progressAdapter
						.getItem(position);
				arguments.putSerializable(Constants.PARAM_PROGRESS, progress);

				lanzarActivity(arguments);
			}
		});

		new ProgressTask().execute();
	}

	private class ProgressTask extends AsyncTask<Integer, Void, List<Progress>> {
		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(ProgressActivity.this, null,
					"Cargando...");
			super.onPreExecute();
		}

		@Override
		protected List<Progress> doInBackground(Integer... params) {
			try {
				Cursor cursorModules = getContentResolver().query(
						UserAssignedDAO.QUERY_USER_ASIGNED_MODULE_BYUSER_URI,
						null, null, null, null);
				cursorModules.moveToFirst();

				for (int i = 0; i < cursorModules.getCount(); i++) {

					int intCodModule = cursorModules.getInt(cursorModules
							.getColumnIndex(Constants.COLUMN_NAME_MODULE_COD));
					String[] strArgs = { "" + intCodModule };

					Cursor cursorProgress = getContentResolver().query(
							ListingDAO.QUERY_USER_LIST_GROUPBY_STATE_URI, null,
							null, strArgs, null);
					cursorProgress.moveToFirst();
					
					//
					String[] strProductionArgsNoSended = { "" + intCodModule, "" + 0 };

					Cursor cursorProductionNoSended = getContentResolver().query(
							RegisterDAO.QUERY_REGISTERED_BY_USER_URI, null,
							null, strProductionArgsNoSended, null);
					
					String[] strProductionArgsSended = { "" + intCodModule, "" + 1 };
					
					Cursor cursorProductionSended = getContentResolver().query(
							RegisterDAO.QUERY_REGISTERED_BY_USER_URI, null,
							null, strProductionArgsSended, null);
					
					
					
					Progress progress = new Progress();
					progress.setStrModuleCod(cursorModules.getString(cursorModules
							.getColumnIndex(Constants.COLUMN_NAME_MODULE_COD)));
					progress.setStrModuleDesc(cursorModules.getString(cursorModules
							.getColumnIndex(Constants.COLUMN_NAME_MODULE_DESC)));

					for (int j = 0; j < cursorProgress.getCount(); j++) {
						int intState = cursorProgress.getInt(cursorProgress
								.getColumnIndex(Constants.COLUMN_NAME_STATE));

						switch (intState) {
						case 0:
							progress.setIntPending(cursorProgress.getInt(cursorProgress
									.getColumnIndex(Constants.COLUMN_NAME_HOWMUCH)));
							break;

						case 1:
							progress.setIntInProgress(cursorProgress.getInt(cursorProgress
									.getColumnIndex(Constants.COLUMN_NAME_HOWMUCH)));
							break;

						case 2:
							progress.setIntExecuted(cursorProgress.getInt(cursorProgress
									.getColumnIndex(Constants.COLUMN_NAME_HOWMUCH)));
							break;

						case 3:
							progress.setIntFinished(cursorProgress.getInt(cursorProgress
									.getColumnIndex(Constants.COLUMN_NAME_HOWMUCH)));
							break;
						}

						progress.setIntSended(cursorProductionSended.getCount());
						
						progress.setIntNotSended(cursorProductionNoSended.getCount());
						
						cursorProgress.moveToNext();
					}

					listProgress.add(progress);

					cursorModules.moveToNext();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return listProgress;
		}

		protected void onPostExecute(List<Progress> result) {
			dialog.dismiss();

			progressAdapter.setProgresses(result);
			progressAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}

	private void lanzarActivity(Bundle bundle) {
		Intent intent = new Intent(this, ProgressDetailActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}