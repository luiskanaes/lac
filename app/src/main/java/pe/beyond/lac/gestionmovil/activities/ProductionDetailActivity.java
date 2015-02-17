package pe.beyond.lac.gestionmovil.activities;

import java.util.Vector;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import pe.beyond.gls.model.Progress;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ProductionDetailActivity extends Activity
{
	private LinearLayout lytProductionDetail;
	
	private Progress progress;
	private Bundle bundle;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_production_detail_layout);
		
		lytProductionDetail = (LinearLayout) this.findViewById(R.id.lytProductionDetail);
		
		bundle = this.getIntent().getExtras();
		progress = (Progress) bundle.getSerializable(Constants.PARAM_PROGRESS);
		
		int intOne = Color.parseColor("#C0504D");  //COLOR: ROJO  PEN
		int intTwo = Color.parseColor("#419C53");  //COLOR: VERDE FIN  
		
		//
		Vector<Integer> vectorMedidas = new Vector<Integer>();
		Vector<Integer> vectorColores = new Vector<Integer>();
		Vector<String> vectorDescripciones = new Vector<String>();
 		
		if (progress.getIntNotSended() > 0)
		{
			vectorMedidas.add(progress.getIntNotSended());
			vectorColores.add(intOne);
			vectorDescripciones.add("Pendientes: ");
		}
		
		if (progress.getIntSended() > 0)
		{
			vectorMedidas.add(progress.getIntSended());
			vectorColores.add(intTwo);
			vectorDescripciones.add("Enviados: ");
		}
		
		int[] intMedidas = new int[vectorMedidas.size()];
		int[] intColores = new int[vectorColores.size()];
		String[] strDescripciones = new String[vectorDescripciones.size()];
		
		for (int i = 0; i < vectorMedidas.size(); i ++)
		{
			intMedidas[i] = vectorMedidas.elementAt(i);
			intColores[i] = vectorColores.elementAt(i);
			strDescripciones[i] = vectorDescripciones.elementAt(i) + vectorMedidas.elementAt(i);
		}
		
		//
		
		lytProductionDetail.addView(openChart("DETALLE DE REGISTROS \n" +progress.getStrModuleDesc(), strDescripciones, intMedidas, intColores));
	}
	
	private View openChart(String strTitle, String[] strDescriptions, int[] distribution, int[] colors)
	{
		 
        // Pie Chart Section Names
        
 
        // Pie Chart Section Value
        //double[] distribution = {100, 25, 25, 50} ;
 
        // Color of each Pie Chart Sections
        
		// Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(strTitle);
        
        for(int i=0 ;i < distribution.length;i++)
        {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(strDescriptions[i], distribution[i]);
        }
 
        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }
 
        defaultRenderer.setChartTitle(strTitle);
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setZoomButtonsVisible(false);
        //defaultRenderer.setDisplayValues(true);
 
        // Creating an intent to plot bar chart using dataset and multipleRenderer
        //Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");
        
        View viewChart = ChartFactory.getPieChartView(this, distributionSeries, defaultRenderer);
        return viewChart;
    }
}