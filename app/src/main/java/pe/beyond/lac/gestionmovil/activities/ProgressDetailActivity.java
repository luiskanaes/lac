package pe.beyond.lac.gestionmovil.activities;

import java.util.ArrayList;
import java.util.Vector;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import pe.beyond.gls.model.Progress;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.asynctasks.PayloadAsyncTask;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class ProgressDetailActivity extends Activity {
	private LinearLayout lytPieChart;
	private Progress progress;
	private Bundle bundle;

	private final int ID_MENU_PRODUCTION = 1;
	private final int ID_MENU_PRODUCTION_PHOTOS = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_detail_layout);

		bundle = this.getIntent().getExtras();
		progress = (Progress) bundle.getSerializable(Constants.PARAM_PROGRESS);

		lytPieChart = (LinearLayout) this.findViewById(R.id.lytPieChart);
		
		//Colores de la 1era pantalla de avance 		
				int intOne = Color.parseColor("#C0504D");  //COLOR: ROJO  PEN
				int intTwo = Color.parseColor("#E1D719");  //COLOR: AMARI PRO 
				int intTree = Color.parseColor("#4ABA60"); //COLOR: VERDE EJE
				int intFour = Color.parseColor("#419C53"); //COLOR: VERDE FIN
		
		Vector<Integer> vectorMedidas = new Vector<Integer>();
		Vector<Integer> vectorColores = new Vector<Integer>();
		Vector<String> vectorDescripciones = new Vector<String>();
 		
		if (progress.getIntPending() > 0)
		{
			vectorMedidas.add(progress.getIntPending());
			vectorColores.add(intOne);
			vectorDescripciones.add("Pendientes: ");
		}
		
		if (progress.getIntInProgress() > 0)
		{
			vectorMedidas.add(progress.getIntInProgress());
			vectorColores.add(intTwo);
			vectorDescripciones.add("En Proceso: ");
		}
		
		if (progress.getIntExecuted() > 0)
		{
			vectorMedidas.add(progress.getIntExecuted());
			vectorColores.add(intTree);
			vectorDescripciones.add("Ejecutados: ");
		}
		
		if (progress.getIntFinished() > 0)
		{
			vectorMedidas.add(progress.getIntFinished());
			vectorColores.add(intFour);
			vectorDescripciones.add("Finalizados: ");
		}
		
		/*for (int i = 0; i < vectorMedidas.size(); i++)
		{
			
		}*/
		
		int[] intMedidas = new int[vectorMedidas.size()];
		int[] intColores = new int[vectorColores.size()];
		String[] strDescripciones = new String[vectorDescripciones.size()];
		
		for (int i = 0; i < vectorMedidas.size(); i ++)
		{
			intMedidas[i] = vectorMedidas.elementAt(i);
			intColores[i] = vectorColores.elementAt(i);
			strDescripciones[i] = vectorDescripciones.elementAt(i) + vectorMedidas.elementAt(i);
		}
		
		lytPieChart.addView(openChart("AVANCE DE CARGA \n"+progress.getStrModuleDesc(), strDescripciones, intMedidas, intColores));
	}

	private View openChart(String strTitle, String[] strDescriptions, int[] distribution, int[] colors) {

		// Pie Chart Section Names
		

		// Pie Chart Section Value
		// double[] distribution = {100, 25, 25, 50} ;
		

		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries(strTitle);

		for (int i = 0; i < distribution.length; i++) {
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(strDescriptions[i], distribution[i]);
		}

		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		for (int i = 0; i < distribution.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(false);

			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}

		defaultRenderer.setChartTitle(strTitle);
		defaultRenderer.setChartTitleTextSize(20);
		defaultRenderer.setLabelsColor(Color.BLACK);
		defaultRenderer.setZoomButtonsVisible(false);
		defaultRenderer.setZoomEnabled(true);
		defaultRenderer.setFitLegend(true);
		// defaultRenderer.setDisplayValues(true);

		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		// Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
		// distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");

		View viewChart = ChartFactory.getPieChartView(this, distributionSeries,
				defaultRenderer);
		return viewChart;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem itemProduction = menu.add(Menu.NONE, ID_MENU_PRODUCTION,
				Menu.NONE, "DETALLE DE REGISTROS");
		MenuItem itemProductionPhotos = menu.add(Menu.NONE, ID_MENU_PRODUCTION_PHOTOS,
				Menu.NONE, "DETALLE DE FOTOS");
		
		itemProduction.setShortcut('5', 'p');
		itemProductionPhotos.setShortcut('6', 'f');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ID_MENU_PRODUCTION) {
			launchPieChartProductionDetail();
			return true;
		}
		
		if (item.getItemId() == ID_MENU_PRODUCTION_PHOTOS) {
			launchPieChartProductionDetailPhotos();
			return true;
		} 
		
		return false;
	}

	private void launchPieChartProductionDetail() {
		Bundle arguments = new Bundle();
		arguments.putSerializable(Constants.PARAM_PROGRESS, progress);

		Intent intent = new Intent(this, ProductionDetailActivity.class);
		intent.putExtras(arguments);

		startActivity(intent);
	}
	
	private void launchPieChartProductionDetailPhotos() {
		Bundle arguments = new Bundle();
		arguments.putSerializable(Constants.PARAM_PROGRESS, progress);

		Intent intent = new Intent(this, ProductionDetailActivityPhotos.class);
		intent.putExtras(arguments);

		startActivity(intent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_P) {
			launchPieChartProductionDetail();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}