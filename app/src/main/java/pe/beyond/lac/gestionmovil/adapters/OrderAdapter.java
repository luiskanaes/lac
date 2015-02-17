package pe.beyond.lac.gestionmovil.adapters;

import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.lac.gestionmovil.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<Form>{
	public Context context;
	public List<Form> lstForm;
	public OrderAdapter(Context context, int textViewResourceId,
			List<Form> lstForm) {
		super(context, textViewResourceId, lstForm);
		this.context = context;
		this.lstForm = lstForm;
	}

	
	@Override
	public int getCount() {
		return lstForm.size();
	}


	@Override
	public Form getItem(int position) {
		return lstForm.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_spinner_item, null);
		}
		String strText = lstForm.get(position).getStrName();
		
		((TextView)convertView).setText(strText);
		return convertView;
	}


	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView==null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_spinner_item, null);
		}
		((TextView)convertView).setText(lstForm.get(position).getStrName());
        return convertView;
	}
	
	
	
}
