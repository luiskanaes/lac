package pe.beyond.lac.gestionmovil.adapters;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SearchListAdapter extends SimpleCursorAdapter{
	public BaseActivity activity;
	
	public SearchListAdapter(BaseActivity activity, int layout, Cursor cursor, String[] from,
			int[] to) {
		super(activity, layout, cursor, from, to);
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return this.getCursor().getCount();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
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
		getCursor().moveToPosition(position);
		
		String strText = getCursor().getString(getCursor().getColumnIndex(FormDAO.COL_DES_NOMBRE));
		holder.text.setText(strText);
		return convertView;
	}

	@Override
	protected void onContentChanged() {
		super.onContentChanged();
		this.notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView text;
	}
}

