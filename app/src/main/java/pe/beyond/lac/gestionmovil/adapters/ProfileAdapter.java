package pe.beyond.lac.gestionmovil.adapters;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.ModuleDAO;
import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ProfileAdapter extends SimpleCursorAdapter{
	public BaseActivity activity;
	
	public ProfileAdapter(BaseActivity activity, int layout, Cursor cursor, String[] from,
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
	        convertView = activity.getLayoutInflater().inflate(R.layout.list_item, null);

	        holder = new ViewHolder();
	        holder.text = (TextView) convertView.findViewById(R.id.profile_item);
	        convertView.setTag(holder);
	    } else{
			holder = (ViewHolder) convertView.getTag();
	    }
		getCursor().moveToPosition(position);
		
		String strText = getCursor().getString(getCursor().getColumnIndex(UserAssignedDAO.COL_DES_BASE)) + Constants.LBL_SEPARATOR + 
				getCursor().getString(getCursor().getColumnIndex(ModuleDAO.COL_DES_MODULO));
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

