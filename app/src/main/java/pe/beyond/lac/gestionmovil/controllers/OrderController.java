package pe.beyond.lac.gestionmovil.controllers;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.Form;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.adapters.OrderAdapter;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderController implements OnItemSelectedListener,OnTouchListener{
	public PayloadActivity activity;
	public List<Form> lstForm;
	public LinearLayout linOrder1, linOrder2,linOrder3;
	public Spinner spnOrderType1,spnOrderType2,spnOrderType3,spnOrderWay1,spnOrderWay2,spnOrderWay3;
	
	public String strForm1,strForm2,strForm3;
	public OrderController(PayloadActivity activity, List<Form> lstForm, 
			LinearLayout linOrder1,LinearLayout linOrder2,LinearLayout linOrder3,
			Spinner spnOrderType1, Spinner spnOrderWay1, 
			Spinner spnOrderType2, Spinner spnOrderWay2,
			Spinner spnOrderType3, Spinner spnOrderWay3) {
		this.activity = activity;
		this.lstForm = lstForm;
		
		this.linOrder1 =linOrder1;
		this.linOrder2 =linOrder2;
		this.linOrder3 =linOrder3;
		this.spnOrderType1 =spnOrderType1;
		this.spnOrderType2 =spnOrderType2;
		this.spnOrderType3 =spnOrderType3;
		this.spnOrderWay1 = spnOrderWay1;
		this.spnOrderWay2 = spnOrderWay2;
		this.spnOrderWay3 = spnOrderWay3;
		
		ArrayAdapter<String> WayAdapter = new ArrayAdapter<String>(activity, R.layout.order_spinner_item, new String[]{" DESC ", " ASC "});
		spnOrderWay1.setAdapter(WayAdapter);
		spnOrderWay2.setAdapter(WayAdapter);
		spnOrderWay3.setAdapter(WayAdapter);
		
		_createOrderAdapter(View.VISIBLE,1,linOrder1,spnOrderType1,spnOrderWay1);
		_createOrderAdapter(View.VISIBLE,2,linOrder2,spnOrderType2,spnOrderWay2);
		_createOrderAdapter(View.GONE,3,linOrder3,spnOrderType3,spnOrderWay3);
		
		spnOrderType1.setOnItemSelectedListener(this);
		spnOrderType2.setOnItemSelectedListener(this);
		spnOrderType3.setOnItemSelectedListener(this);
		spnOrderWay1.setOnItemSelectedListener(this);
		spnOrderWay2.setOnItemSelectedListener(this);
		spnOrderWay3.setOnItemSelectedListener(this);	
		
		spnOrderType1.setOnTouchListener(this);
		spnOrderType2.setOnTouchListener(this);
		spnOrderType3.setOnTouchListener(this);
		spnOrderWay1.setOnTouchListener(this);
		spnOrderWay2.setOnTouchListener(this);
		spnOrderWay3.setOnTouchListener(this);
		
	}
	
	public Form[] _obtainOrderInformation(){
		Form[] arrOrdering = new Form[3];
		Form arrOrderSpinner = _getSpinnerInfo(1,spnOrderType1,spnOrderWay1);
		arrOrdering[0] = arrOrderSpinner;
		arrOrderSpinner = _getSpinnerInfo(2,spnOrderType2,spnOrderWay2);
		arrOrdering[1] = arrOrderSpinner;
		arrOrderSpinner = _getSpinnerInfo(3,spnOrderType3,spnOrderWay3);
		arrOrdering[2] = arrOrderSpinner;
		return arrOrdering;
	}
	
	private Form _getSpinnerInfo(int i, Spinner spnOrderType, Spinner spnOrderWay) {
		Form objForm = (Form)spnOrderType.getSelectedItem();
		if (objForm.getStrName().equals(" - NO USADO - ")){
			objForm = null;
		} else {
			objForm.setIntOrderEntry(i);
			objForm.setBooFlgOrientation(spnOrderWay.getSelectedItemPosition()==Constants.FLG_ASC);			
		}
		return objForm;
	}

	private void _createOrderAdapter(int intVisibility, int i, LinearLayout linOrder, Spinner spnOrderType, Spinner spnOrderWay) {
		ArrayList<Form> lstTemporal = new ArrayList<Form>();
		if (i>1){
			Form objForm = new Form();
			objForm.setStrName(" - NO USADO - ");
			lstTemporal.add(objForm);
		}
		
		int intSelected = 0;
		int intWay = Constants.FLG_ASC;
		for (int intIte =0;intIte<lstForm.size();intIte ++){
			if (lstForm.get(intIte).getIntOrderEntry()==0 || lstForm.get(intIte).getIntOrderEntry()>=i){
				lstTemporal.add(lstForm.get(intIte));
				if (lstForm.get(intIte).getIntOrderEntry()==i){
					intSelected = lstTemporal.size()-1;
					intWay = lstForm.get(intIte).isBooFlgOrientation()?Constants.FLG_ASC:Constants.FLG_DESC;
				}
			}
		}
		
		OrderAdapter objOrderAdapter = new OrderAdapter(activity, R.layout.order_spinner_item, lstTemporal);
		spnOrderType.setAdapter(objOrderAdapter);
		spnOrderType.setSelection(intSelected);
		spnOrderWay.setSelection(intWay);
		linOrder.setVisibility(intVisibility);
		
		if (i>1 && lstTemporal.size()<2){
			linOrder.setVisibility(View.GONE);
		} else if (spnOrderType.getSelectedItemPosition()>0){
			linOrder.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		int intSelectedSpinnerId = ((PayloadActivity)activity).getIntSelectedSpinnerId();
		switch (parent.getId()){
		case R.id.spi_order_typ_1:
			if (intSelectedSpinnerId==R.id.spi_order_typ_1){
				String strText = ((TextView)view).getText().toString();
				for (int intIte =0;intIte<lstForm.size();intIte ++){
					if (view!= null){
						if (lstForm.get(intIte).getIntOrderEntry()>=1 && !(lstForm.get(intIte).getStrName().equals(strText))){
							lstForm.get(intIte).setIntOrderEntry(0);
						} else 	if (lstForm.get(intIte).getStrName().equals(strText)){
							lstForm.get(intIte).setIntOrderEntry(1);
							int intWay = lstForm.get(intIte).isBooFlgOrientation()?1:0;
							spnOrderWay1.setSelection(intWay);
						}
					}
				}
				_createOrderAdapter(View.VISIBLE,2,linOrder2,spnOrderType2,spnOrderWay2);
				_createOrderAdapter(View.GONE,3,linOrder3,spnOrderType3,spnOrderWay3);
			}
			break;
		case R.id.spi_order_typ_2:
			if (intSelectedSpinnerId==R.id.spi_order_typ_2){
				String strText = ((TextView)view).getText().toString();
				for (int intIte =0;intIte<lstForm.size();intIte ++){
					if (view!= null){
						if (lstForm.get(intIte).getIntOrderEntry()>=2 && !(lstForm.get(intIte).getStrName().equals(strText))){
							lstForm.get(intIte).setIntOrderEntry(0);
						} else if (lstForm.get(intIte).getStrName().equals(strText)){
							lstForm.get(intIte).setIntOrderEntry(2);
							int intWay = lstForm.get(intIte).isBooFlgOrientation()?1:0;
							spnOrderWay2.setSelection(intWay);
						}
					}
				}
				_createOrderAdapter(View.VISIBLE,3,linOrder3,spnOrderType3,spnOrderWay3);
			}
			break;
		case R.id.spi_order_typ_3:
			if (intSelectedSpinnerId==R.id.spi_order_typ_3){
				String strText = ((TextView)view).getText().toString();					
				for (int intIte =0;intIte<lstForm.size();intIte ++){
					if (view!= null){
						if (lstForm.get(intIte).getIntOrderEntry()>=3 && !(lstForm.get(intIte).getStrName().equals(strText))){
							lstForm.get(intIte).setIntOrderEntry(0);
						} else if (lstForm.get(intIte).getStrName().equals(strText)){
							lstForm.get(intIte).setIntOrderEntry(3);
							int intWay = lstForm.get(intIte).isBooFlgOrientation()?1:0;
							spnOrderWay3.setSelection(intWay);
						}
					}
				}
			}
			break;
		case R.id.spi_order_way_1:
			
			if (intSelectedSpinnerId==R.id.spi_order_way_1){
				Form objForm = (Form) spnOrderType1.getSelectedItem();
				boolean booWay = spnOrderWay1.getSelectedItemPosition()==1?true:false;
				objForm.setBooFlgOrientation(booWay);
			}
			break;
		case R.id.spi_order_way_2:
			if (intSelectedSpinnerId==R.id.spi_order_way_2){
				Form objForm = (Form) spnOrderType2.getSelectedItem();
				boolean booWay = spnOrderWay2.getSelectedItemPosition()==1?true:false;
				objForm.setBooFlgOrientation(booWay);
			}
			break;
		case R.id.spi_order_way_3:
			if (intSelectedSpinnerId==R.id.spi_order_way_3){
				Form objForm = (Form) spnOrderType3.getSelectedItem();
				boolean booWay = spnOrderWay3.getSelectedItemPosition()==1?true:false;
				objForm.setBooFlgOrientation(booWay);
			}
	break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction()==KeyEvent.ACTION_DOWN && 
				   (v.getId()==R.id.spi_order_typ_1 || 
					v.getId()==R.id.spi_order_typ_2 || 
					v.getId()==R.id.spi_order_typ_3 ||
					v.getId()==R.id.spi_order_way_1 || 
					v.getId()==R.id.spi_order_way_2 || 
					v.getId()==R.id.spi_order_way_3)){
				((PayloadActivity)activity).setIntSelectedSpinnerId(v.getId());
			}
		return false;
	}

}
