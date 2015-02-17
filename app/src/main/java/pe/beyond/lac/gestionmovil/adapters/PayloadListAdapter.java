package pe.beyond.lac.gestionmovil.adapters;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PayloadListAdapter extends SimpleCursorAdapter{
	public BaseActivity activity;
	private String strName;

	public static String strField;
	
	
	public PayloadListAdapter(BaseActivity activity, String strName,String strField, int layout, Cursor cursor, String[] from,
			int[] to) {
		super(activity, layout, cursor, from, to);
		this.activity = activity;
		this.strName = strName;
		this.strField = strField;
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
	        convertView = activity.getLayoutInflater().inflate(R.layout.payload_list_item, null);

	        holder = new ViewHolder();
	        holder.text = (TextView) convertView.findViewById(R.id.payload_text_register);
	        convertView.setTag(holder);
	    } else{
			holder = (ViewHolder) convertView.getTag();
	    }
		getCursor().moveToPosition(position);
		
		String strText = Constants.LBL_EMPTY;
		strText += strName + Constants.LBL_SPACE + getCursor().getString(getCursor().getColumnIndex(strField));
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

