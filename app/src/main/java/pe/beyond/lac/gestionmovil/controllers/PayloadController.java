package pe.beyond.lac.gestionmovil.controllers;

import java.util.List;

import org.w3c.dom.ls.LSResourceResolver;

import pe.beyond.gls.model.Form;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.adapters.FilterListAdapter;
import pe.beyond.lac.gestionmovil.adapters.PayloadListAdapter;
import pe.beyond.lac.gestionmovil.adapters.SearchListAdapter;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.content.ContentValues;
import android.database.Cursor;

public class PayloadController extends BaseController {
	public PayloadController(PayloadActivity activity) {
		super(activity);
		this.activity = activity;
	}

	/**
	 * Se customiza el adapter de la lista que muestra la carga del operario
	 * según el módulo en el que se encuentre laborando
	 * 
	 * @return adapter - adapter customizado
	 */
	public PayloadListAdapter _fillPayloadList() {
		PayloadListAdapter adapter = null;
		int layout = R.layout.payload_list_item;

		Form objForm = _obtainSearchForm(AppPreferences.getInstance(activity)._loadCurrentModuleId());
		String strField = "DES_CAMPO" + String.valueOf(objForm.getIntOrder())+ "_LIS";

		String[] projection = new String[] {
				ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_ID,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_LISTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT + strField };

		String sortOrder = ListingDAO.COL_IDE_LISTADO;
		List<Form> lstOrderForm = _obtainLstFormByListingOrder();
		if(lstOrderForm!=null && lstOrderForm.size()>0){
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(" ");
			for (int intIte = 0; intIte <lstOrderForm.size();intIte ++){
				Form objOrderForm =lstOrderForm.get(intIte);
				String strOrder = null;
				if (objOrderForm.getStrDataType().equals("String")){
					strOrder = "DES_CAMPO" + String.valueOf(objOrderForm.getIntOrder())+ "_LIS ";
				} else if(objOrderForm.getStrDataType().equals("Int")){
					strOrder = "CAST ( DES_CAMPO" + String.valueOf(objOrderForm.getIntOrder())+ "_LIS AS INTEGER ) ";
				}
				
				String strWay = objOrderForm.isBooFlgOrientation()?" ASC " : " DESC ";
				strBuilder.append(strOrder).append(strWay);
				if (intIte <lstOrderForm.size()-1){
					strBuilder.append(" , ");
				}
			}
			sortOrder = strBuilder.toString();
		}
		Cursor cursorPayload = activity.getContentResolver().query(
				ListingDAO.QUERY_ALL_LIST_URI, projection, null, null, sortOrder);
		String[] from = new String[] { ListingDAO.COL_IDE_LISTADO };
		int[] to = new int[] { R.id.payload_text_register };

		adapter = new PayloadListAdapter(activity, objForm.getStrName(),
				strField, layout, cursorPayload, from, to);
		return adapter;
	}

	/**
	 * Se customiza el adapter de la lista que muestran las opciones de búsqueda
	 * permitidas para el módulo
	 * 
	 * @return adapter
	 */
	public SearchListAdapter _fillSearchList() {
		SearchListAdapter adapter = null;
		int layout = R.layout.search_list_item;

		String strModuleId = String.valueOf(AppPreferences
				.getInstance(activity)._loadCurrentModuleId());
		String[] selectionArgs = new String[] { strModuleId };

		Cursor cursorSearch = activity.getContentResolver().query(
				FormDAO.QUERY_FOR_SEARCH_OPTIONS_URI, null, null,
				selectionArgs, null);
		String[] from = new String[] { };
		int[] to = new int[] {};

		adapter = new SearchListAdapter(activity, layout, cursorSearch, from,
				to);
		return adapter;
	}
	
	/**
	 * Se customiza el adapter de la lista que muestran las opciones de filtrado
	 * permitidas para el módulo
	 * 
	 * @return adapter
	 */
	public FilterListAdapter _fillFilterList() {
		FilterListAdapter adapter = null;
		int layout = R.layout.search_list_item;

		String strModuleId = String.valueOf(AppPreferences
				.getInstance(activity)._loadCurrentModuleId());
		String[] selectionArgs = new String[] { strModuleId };

		Cursor cursorFilter = activity.getContentResolver().query(
				ListingDAO.QUERY_FOR_FILTER_OPTIONS_URI, null, null,
				selectionArgs, null);

		String[] items = new String[cursorFilter.getCount()+1];
		
		int intIte = 0;
		items[intIte] = "4";
		while (!cursorFilter.isAfterLast()){
			intIte++;
			items[intIte] = cursorFilter.getString(cursorFilter.getColumnIndex(ListingDAO.COL_COD_ESTADO));
			cursorFilter.moveToNext();
		}
		
		adapter = new FilterListAdapter(activity, layout, R.id.search_text_form_name, items);
		return adapter;
	}

	
	

	/**
	 * Cambiar el formulario de búsqueda en la base de datos
	 * 
	 * @param intFormNewSearch
	 */
	public void _replaceSearchDialog(int intFormNewSearch) {
		int intModuleId = AppPreferences.getInstance(activity)
				._loadCurrentModuleId();
		ContentValues objValuesOld = new ContentValues();
		objValuesOld.put(FormDAO.COL_FLG_BUSQUEDA, 1);
		String[] selectionArgsOld = new String[] { String.valueOf(intModuleId) };
		activity.getContentResolver().update(
				FormDAO.UPDATE_OLD_SEARCH_FORM_URI, objValuesOld, null,
				selectionArgsOld);

		ContentValues objValuesNew = new ContentValues();
		objValuesNew.put(FormDAO.COL_FLG_BUSQUEDA, 2);
		String[] selectionArgsNew = new String[] { String.valueOf(intModuleId),
				String.valueOf(intFormNewSearch) };
		activity.getContentResolver().update(
				FormDAO.UPDATE_NEW_SEARCH_FORM_URI, objValuesNew, null,
				selectionArgsNew);

	}

	public void _saveOrder(OrderController orderController) {
		String strModuleId = String.valueOf(AppPreferences.getInstance(activity)._loadCurrentModuleId());
		String[] strSelectionArgs = new String[]{strModuleId};
		ContentValues objContentValues = new ContentValues();
		objContentValues.put(FormDAO.COL_NUM_CAMPO_ORDEN, 0);
		objContentValues.put(FormDAO.COL_FLG_ORIENTACION, false);
		
		activity.getContentResolver().update(FormDAO.UPDATE_ORDER_ENTRY_DEFAULT_URI, objContentValues, null, strSelectionArgs);
		
		Form[] arrOrdering = orderController._obtainOrderInformation();
		for(Form objForm : arrOrdering){
			if (objForm!=null){
				objContentValues = new ContentValues();
				objContentValues.put(FormDAO.COL_NUM_CAMPO_ORDEN, objForm.getIntOrderEntry());
				objContentValues.put(FormDAO.COL_FLG_ORIENTACION, objForm.isBooFlgOrientation());
				
				String strOrder = String.valueOf(objForm.getIntOrder());
				strSelectionArgs = new String[]{strModuleId, strOrder};
				activity.getContentResolver().update(FormDAO.UPDATE_ORDER_ENTRY_VALUES_URI, objContentValues, null, strSelectionArgs);
			} else{
				break;
			}
		}
	};
}
