package pe.beyond.lac.gestionmovil.tracking;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LocationCursorAdapter extends SimpleCursorAdapter {
	private Cursor c;
	private Context context;

	private static final String TAG = LocationCursorAdapter.class.toString();
	
	public LocationCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;
	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.tracking_stored_locations_row_layout, null);
		}
		this.c.moveToPosition(pos);
		String value = this.c.getString(this.c.getColumnIndex(TrackDAO.COL_COD_PROVIDER));
		if (Integer.valueOf(value)==0){
			value = "gps";
		} else if (Integer.valueOf(value)==1){
			value = "network";
		}
		TextView view = (TextView) v.findViewById(R.id.locationname);
		view.setText(value);
		String latitude = new DecimalFormat("###.####").format(this.c.getDouble(this.c.getColumnIndex(TrackDAO.COL_CAN_LATITUD)));
		view = (TextView) v.findViewById(R.id.locationlatitude);
		view.setText(latitude);
		String longitude = new DecimalFormat("###.####").format(this.c.getDouble(this.c.getColumnIndex(TrackDAO.COL_CAN_LONGITUD)));
		view = (TextView) v.findViewById(R.id.locationlongitude);
		view.setText(longitude);
		value = new DecimalFormat("####").format(this.c.getLong(this.c.getColumnIndex(TrackDAO.COL_POR_PRECISION)));
		view = (TextView) v.findViewById(R.id.locationaccuracy);
		view.setText(value);
		
		long time = this.c.getLong(this.c.getColumnIndex(TrackDAO.COL_FEC_HORA));
		
		value = Util._createSTRFormatTime(time);
		view = (TextView) v.findViewById(R.id.locationtime);
		view.setText(value);
		return (v);
	}

}
