package pe.beyond.lac.gestionmovil.adapters;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import pe.beyond.gls.model.Progress;
import pe.beyond.lac.gestionmovil.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressAdapter extends BaseAdapter
{
	private LayoutInflater progressInflater;
	private List<Progress> progresses;
	private Context context;
	
	public ProgressAdapter(Context context)
	{
		progressInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public List<Progress> getProgresses()
	{
		return progresses;
	}
	
	public void setProgresses(List<Progress> progresses)
	{
		this.progresses = progresses;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return (progresses != null? progresses.size():0);
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return progresses.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Holder holder;
		
		if (convertView == null)
		{
			convertView = progressInflater.inflate(R.layout.progress_item, null);
			holder = new Holder();
			
			holder.txtModuloName = (TextView) convertView.findViewById(R.id.txtModuloName);
			//holder.lyPieChartGraph = (LinearLayout) convertView.findViewById(R.id.linearGraphChart);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		Progress progress = progresses.get(position);
		
		//double[] dbValues = {progress.getIntPending(), progress.getIntInProgress(), progress.getIntExecuted(), progress.getIntFinished()};
		
		holder.txtModuloName.setText(progress.getStrModuleDesc());
		
		/*if(((LinearLayout) holder.lyPieChartGraph).getChildCount() > 0)
		{
			((LinearLayout) holder.lyPieChartGraph).removeAllViews();
		}  
		holder.lyPieChartGraph.addView(openChart(dbValues));*/
		
		return convertView;
	}
	
	private static final class Holder
	{
		TextView txtModuloName;
	}
	
	private View openChart(int[] distribution)
	{
		 
        // Pie Chart Section Names
        String[] code = new String[] {"Pendientes", "En Proceso", "Ejecutados", "Finalizados"};
 
        // Pie Chart Section Value
        //double[] distribution = { 3.9, 12.9, 55.8, 1.9, 23.7, 1.8 } ;
 
        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN};
 
        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");
        
        for(int i=0 ;i < distribution.length;i++)
        {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
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
 
        defaultRenderer.setChartTitle("Android version distribution as on October 1, 2012 ");
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);
 
        // Creating an intent to plot bar chart using dataset and multipleRenderer
        //Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");
        
        View viewChart = ChartFactory.getPieChartView(context, distributionSeries, defaultRenderer);
        System.out.println("acaaaaaa: " +viewChart);
        return viewChart;
    }
}
