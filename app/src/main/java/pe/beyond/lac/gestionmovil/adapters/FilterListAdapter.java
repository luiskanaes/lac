package pe.beyond.lac.gestionmovil.adapters;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FilterListAdapter extends ArrayAdapter<String>{
	public BaseActivity activity;
	
	public FilterListAdapter(BaseActivity activity, int resource,int textViewResourceId, String[] objects) {
		super(activity, resource, textViewResourceId, objects);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
	        convertView = activity.getLayoutInflater().inflate(R.layout.search_list_item, null);

	        holder = new ViewHolder();
	        holder.text = (TextView) convertView.findViewById(R.id.search_text_form_name);
	        convertView.setTag(holder);
	    } else{
			holder = (ViewHolder) convertView.getTag();
	    }

		int intState = Integer.valueOf(this.getItem(position));
		String strText = Util._getStateName(activity,intState);
		holder.text.setText(strText);
		return convertView;
	}
	private class ViewHolder {
		TextView text;
	}
}

