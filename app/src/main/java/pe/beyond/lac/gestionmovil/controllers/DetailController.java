package pe.beyond.lac.gestionmovil.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Flux;
import pe.beyond.gls.model.FluxValidation;
import pe.beyond.gls.model.Form;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.ObsCode;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.OptionModResult;
import pe.beyond.gls.model.OptionResult;
import pe.beyond.gls.model.Photo;
import pe.beyond.gls.model.Question;
import pe.beyond.gls.model.Register;
import pe.beyond.gls.model.RegisterAnswer;
import pe.beyond.gls.model.Track;
import pe.beyond.gls.model.UserValidation;
import pe.beyond.gls.model.Validation;
import pe.beyond.gls.model.ValidationResult;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.DetailActivity;
import pe.beyond.lac.gestionmovil.activities.PayloadActivity;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.OptionDAO;
import pe.beyond.lac.gestionmovil.daos.QuestionDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterAnswerDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.UserValidationDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DetailController extends BaseController {
	public final String LOGNAME = " DetailController ";

	public static final int STATE_UNANSWERED = 0;
	public static final int STATE_ANSWERED = 1;
	public static final int STATE_INVALIDATED = 2;
	public static final int STATE_VALIDATED = 3;
	public static final int STATE_RESTRICTIVE = 4;
	public static final int STATE_UNRESTRICTIVE = 5;

	// Se usan para la validación de las lecturas
	public int intObsCode;
	public int input1;
	public int input2;
	public int input3;

	public DetailController(DetailActivity activity) {
		super(activity);
	}

	/**
	 * Obtener el id del registro abierto, ya sea si se abrío la actividad desde
	 * la lista de registros, o desde la funcionalidad de búsqueda nativa de la
	 * aplicación.
	 * 
	 * @param objIntent
	 *            - intent de la actividad.
	 * @return int con el valor del id del registro a visualizar
	 */
	public int _obtainListingIdFromIntent(Intent objIntent) {
		int result = -1;

		// se valida si la acción del intent está asociado a la búsqueda nativa.
		String strAction = objIntent.getAction();

		if (Intent.ACTION_VIEW.equals(strAction)
				|| Intent.ACTION_SEARCH.equals(strAction)) {

			String strSearched = null;
			if (Intent.ACTION_VIEW.equals(strAction)) {
				Uri uriData = objIntent.getData();
				String strData = uriData.toString();
				String[] arrData = strData.split(Constants.LBL_SLASH);
				strSearched = arrData[arrData.length - 1];
			} else if (Intent.ACTION_SEARCH.equals(strAction)) {
				// handles a search query
				strSearched = objIntent.getStringExtra(SearchManager.QUERY);
			}

			String[] selectionArgs = new String[] { strSearched };
			Cursor curSearched = activity.getContentResolver().query(
					ListingDAO.QUERY_ID_SUGGESTED_URI, null, null,
					selectionArgs, null);
			if (curSearched.getCount() > 0) {
				result = curSearched.getInt(curSearched
						.getColumnIndex(ListingDAO.COL_IDE_LISTADO));
			}
			_getPositionForListView(strSearched);
		} else {
			result = objIntent.getExtras().getInt(Constants.SELECTED_ID);
		}
		return result;
	}

	private void _getPositionForListView(String strSearched) {
		Cursor cursor = getCursorListView();
		while (!cursor.isAfterLast()) {
			String strCurrentID = cursor.getString(cursor
					.getColumnIndex(ListingDAO.COL_IDE_LISTADO));
			if (strCurrentID.equalsIgnoreCase(strSearched)) {
				if (cursor.getPosition() + 1 < cursor.getCount()) {
					PayloadActivity.intPositionListView = cursor.getPosition();
				} else {
					PayloadActivity.intPositionListView = -1;
				}
				break;
			}
			cursor.moveToNext();
		}
	}

	/**
	 * RETURNS NEXT POSITION WITH LISTINGID AS PARAMETER
	 */
	public int _getNextPosition(int intListingId) {

		Form objForm = _obtainSearchForm(AppPreferences.getInstance(activity)
				._loadCurrentModuleId());
		String strField = "DES_CAMPO" + String.valueOf(objForm.getIntOrder())
				+ "_LIS";

		String[] projection = new String[] {
				ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_ID,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_LISTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT + strField };

		String sortOrder = ListingDAO.COL_IDE_LISTADO;
		List<Form> lstOrderForm = _obtainLstFormByListingOrder();
		if (lstOrderForm != null && lstOrderForm.size() > 0) {
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(" ");
			for (int intIte = 0; intIte < lstOrderForm.size(); intIte++) {
				Form objOrderForm = lstOrderForm.get(intIte);
				String strOrder = null;
				if (objOrderForm.getStrDataType().equals("String")) {
					strOrder = "DES_CAMPO"
							+ String.valueOf(objOrderForm.getIntOrder())
							+ "_LIS ";
				} else if (objOrderForm.getStrDataType().equals("Int")) {
					strOrder = "CAST ( DES_CAMPO"
							+ String.valueOf(objOrderForm.getIntOrder())
							+ "_LIS AS INTEGER ) ";
				}

				String strWay = objOrderForm.isBooFlgOrientation() ? " ASC "
						: " DESC ";
				strBuilder.append(strOrder).append(strWay);
				if (intIte < lstOrderForm.size() - 1) {
					strBuilder.append(" , ");
				}
			}
			sortOrder = strBuilder.toString();
		}
		Cursor cursorPayload = activity.getContentResolver().query(
				ListingDAO.QUERY_ALL_LIST_URI, projection, null, null,
				sortOrder);
		int intCursorPayLoadCount = cursorPayload.getCount();
		if (intCursorPayLoadCount <= 1) {
			return -1;
		}

		int intIndexReturn = 0;
		cursorPayload.moveToFirst();
		if (cursorPayload.getInt(cursorPayload
				.getColumnIndex(ListingDAO.COL_IDE_LISTADO)) == intListingId) {
			cursorPayload.close();
			return 0;
		} else {
			while (cursorPayload.moveToNext()) {
				intIndexReturn++;
				if (cursorPayload.getInt(cursorPayload
						.getColumnIndex(ListingDAO.COL_IDE_LISTADO)) == intListingId) {
					break;
				}
			}
			;
		}

		cursorPayload.close();
		return intIndexReturn;
	}

	private Cursor getCursorListView() {
		Form objForm = _obtainSearchForm(AppPreferences.getInstance(activity)
				._loadCurrentModuleId());
		String strField = "DES_CAMPO" + String.valueOf(objForm.getIntOrder())
				+ "_LIS";

		String[] projection = new String[] {
				ListingDAO.TB_NAME + Constants.LBL_DOT + ListingDAO.COL_ID,
				ListingDAO.TB_NAME + Constants.LBL_DOT
						+ ListingDAO.COL_IDE_LISTADO,
				ListingDAO.TB_NAME + Constants.LBL_DOT + strField };

		String sortOrder = ListingDAO.COL_IDE_LISTADO;
		List<Form> lstOrderForm = _obtainLstFormByListingOrder();
		if (lstOrderForm != null && lstOrderForm.size() > 0) {
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(" ");
			for (int intIte = 0; intIte < lstOrderForm.size(); intIte++) {
				Form objOrderForm = lstOrderForm.get(intIte);
				String strOrder = null;
				if (objOrderForm.getStrDataType().equals("String")) {
					strOrder = "DES_CAMPO"
							+ String.valueOf(objOrderForm.getIntOrder())
							+ "_LIS ";
				} else if (objOrderForm.getStrDataType().equals("Int")) {
					strOrder = "CAST ( DES_CAMPO"
							+ String.valueOf(objOrderForm.getIntOrder())
							+ "_LIS AS INTEGER ) ";
				}

				String strWay = objOrderForm.isBooFlgOrientation() ? " ASC "
						: " DESC ";
				strBuilder.append(strOrder).append(strWay);
				if (intIte < lstOrderForm.size() - 1) {
					strBuilder.append(" , ");
				}
			}
			sortOrder = strBuilder.toString();
		}
		Cursor cursorPayload = activity.getContentResolver().query(
				ListingDAO.QUERY_ALL_LIST_URI, projection, null, null,
				sortOrder);
		return cursorPayload;
	}

	/**
	 * Obtener los valores con los que se valida el input de una pregunta
	 * 
	 * @param intValOpt
	 *            - opción con la que se determinan los valores a retornar
	 * @param strRestrictionValueEntry
	 *            - nombre del campo del valor a comparar
	 * @param strMinValueEntry
	 *            - nombre del campo del valor mínimo a comparar, puede ser null
	 * @param strMaxValueEntry
	 *            - nombre del campo del valor máximo a comparar, puede ser null
	 * @return objeto con los valores para realizar la validación
	 */
	private Object _obtainValidationValues(final int intValOpt,
			final String strRestrictionValueEntry, String strMinValueEntry,
			String strMaxValueEntry) {

		int intListingId = ((DetailActivity) activity).getObjListing()
				.getIntListId();
		String[] selectionArgs = new String[] { String.valueOf(intListingId) };
		Cursor cursor = activity.getContentResolver().query(
				ListingDAO.QUERY_ONE_URI, null, null, selectionArgs, null);

		Object[] arrValues = null;
		if (intValOpt == Constants.VAL_MIXMAX) {
			arrValues = new Integer[3];
			String strValue = cursor.getString(cursor
					.getColumnIndex(strRestrictionValueEntry));
			String strMinValue = cursor.getString(cursor
					.getColumnIndex(strMinValueEntry));
			String strMaxValue = cursor.getString(cursor
					.getColumnIndex(strMaxValueEntry));

			if (strValue != null && !strValue.equals(Constants.LBL_EMPTY)
					&& strMinValue != null
					&& !strMinValue.equals(Constants.LBL_EMPTY)
					&& strMaxValue != null
					&& !strMaxValue.equals(Constants.LBL_EMPTY)) {

				try {
					arrValues[0] = Integer.parseInt(strValue);
					arrValues[1] = Integer.parseInt(strMinValue);
					arrValues[2] = Integer.parseInt(strMaxValue);
				} catch (Exception e) {
					Log.d("LOG", LOGNAME + " _obtainValidationValues "
							+ " VAL_MIXMAX algún valor no numérico ");
				}
			} else {
				Log.d("LOG", LOGNAME + " _obtainValidationValues "
						+ " VAL_MIXMAX algún valor es null o vacio ");
			}
		} else if (intValOpt == Constants.VAL_INT_EQUAL) {
			arrValues = new Integer[1];
			String strValue = cursor.getString(cursor
					.getColumnIndex(strRestrictionValueEntry));

			if (strValue != null && !strValue.equals(Constants.LBL_EMPTY)) {
				try {
					arrValues[0] = Integer.parseInt(strValue);
				} catch (Exception e) {
					Log.d("LOG", LOGNAME + " _obtainValidationValues "
							+ " VAL_INT_EQUAL no numérico ");
				}
			} else {
				Log.d("LOG", LOGNAME + " _obtainValidationValues "
						+ " VAL_INT_EQUAL null o vacío ");
			}
		} else if (intValOpt == Constants.VAL_STR_EQUAL) {
			arrValues = new String[1];
			String strValue = cursor.getString(cursor
					.getColumnIndex(strRestrictionValueEntry));
			if (strValue != null) {
				arrValues[0] = strValue;
			} else {
				Log.d("LOG", LOGNAME + " _obtainValidationValues "
						+ " VAL_STR_EQUAL null ");
			}
		}

		cursor.close();
		return arrValues;
	}

	public void _updateRegister(final Register objRegister) {
		String strListId = String.valueOf(objRegister.getIntListId());
		String strDownloadCounter = String.valueOf(objRegister
				.getIntDownloadCounter());

		String[] selectionArgs = new String[] { strListId, strDownloadCounter };
		ContentValues valuesRegister = RegisterDAO
				.createContentValues(objRegister);
		activity.getContentResolver().update(RegisterDAO.UPDATE_BY_PK_URI,
				valuesRegister, null, selectionArgs);

	}

	public void _updateRegisterAnswer(final RegisterAnswer objRegisterAnswer) {
		String strListId = String.valueOf(objRegisterAnswer.getIntListId());
		String strDownloadCounter = String.valueOf(objRegisterAnswer
				.getIntDownloadCounter());
		String strQuestionId = String.valueOf(objRegisterAnswer
				.getIntQuestionId());

		String[] selectionArgs = new String[] { strListId, strDownloadCounter,
				strQuestionId };
		ContentValues valuesRegister = RegisterAnswerDAO
				.createContentValues(objRegisterAnswer);
		activity.getContentResolver().update(
				RegisterAnswerDAO.UPDATE_BY_PK_URI, valuesRegister, null,
				selectionArgs);

	}

	public void _updateUserValidationState(final int intUserValidationId,
			final int booNewState) {
		String strUserValidationId = String.valueOf(intUserValidationId);

		String[] selectionArgs = new String[] { strUserValidationId };
		ContentValues valuesUserValidation = new ContentValues();
		valuesUserValidation.put(UserValidationDAO.COL_COD_ESTADO, booNewState);

		activity.getContentResolver().update(
				UserValidationDAO.UPDATE_STATE_BY_PK_URI, valuesUserValidation,
				null, selectionArgs);

	}

	/**
	 * Obtener la información de las preguntas que se van a utilizar en el flujo
	 * del registro.
	 * 
	 * @param intFluxId
	 *            - id del flujo de respuestas a trabajar
	 * @return List<Question> con la información de todas las preguntas
	 */
	public List<Question> _obtainLstQuestionByFluxId(final int intFluxId) {
		String[] selectionArgs = new String[] { String.valueOf(intFluxId) };
		Cursor cursor = activity.getContentResolver().query(
				QuestionDAO.QUERY_BY_FLUXID_URI, null, null, selectionArgs,
				null);
		List<Question> lstQuestion = QuestionDAO.createObjects(cursor);
		cursor.close();

		for (int iQuestion = 0; iQuestion < lstQuestion.size(); iQuestion++) {
			lstQuestion.get(iQuestion).setStrTemporalAbrevAnswer(
					Constants.LBL_EMPTY);
			lstQuestion.get(iQuestion).setStrTemporalRealAnswer(
					Constants.LBL_EMPTY);
			lstQuestion.get(iQuestion).setBooIsUpdated(false);
		}

		return lstQuestion;
	}

	/**
	 * Llenar la información asociada a la fecha y manejo de zonas peligrosas.
	 */
	public void _displayTimeDangerousZone() {
		Button btnDangerousZone = ((DetailActivity) activity)
				.getBtnDangerousZone();

		btnDangerousZone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button btnDangerousZone = (Button) v;
				boolean booDangerousZoneOn = (Boolean) btnDangerousZone
						.getTag(R.id.btnDangerousZone);
				_executeDangerousZone(!booDangerousZoneOn);
			}
		});
		_updateDZButton(false);
	}

	public void _updateDZButton(final boolean booDangerousZoneOn) {
		Button btnDangerousZone = ((DetailActivity) activity)
				.getBtnDangerousZone();
		if (booDangerousZoneOn) {
			btnDangerousZone.setBackgroundColor(Color.GREEN);
			btnDangerousZone.setTextColor(Color.BLUE);
			btnDangerousZone.setText(R.string.lbl_dangerous_zone_yes);
		} else {
			btnDangerousZone.setBackgroundColor(Color.DKGRAY);
			btnDangerousZone.setTextColor(Color.RED);
			btnDangerousZone.setText(R.string.lbl_dangerous_zone_no);
		}
		btnDangerousZone.setTag(R.id.btnDangerousZone, booDangerousZoneOn);
	}

	public void _executeDangerousZone(final boolean booDangerousZone) {
		_updateDZButton(booDangerousZone);
		Register objRegister = ((DetailActivity) activity).getObjRegister();
		if (booDangerousZone) {

			Bundle bundle = new Bundle();
			long lngRegister = objRegister.getLngStartDateRegister();
			long lngReal = objRegister.getLngStartDateReal();
			String strTime = Util._obtainDateToDisplay(lngRegister, lngReal);
			bundle.putString(Constants.KEY_START_TIME, strTime);

			lngRegister = objRegister.getLngEndDateRegister();
			lngReal = objRegister.getLngEndDateReal();
			strTime = Util._obtainDateToDisplay(lngRegister, lngReal);
			bundle.putString(Constants.KEY_END_TIME, strTime);

			activity.showDialog(Constants.DIA_CUS_DANGEROUS_ZONE_TIME, bundle);
		} else {
			objRegister.setBooFlgDangerousZone(false);
			long lngStartReal = objRegister.getLngStartDateReal();
			long lngEndReal = objRegister.getLngEndDateReal();

			TextView txtStartTime = ((DetailActivity) activity)
					.getTxtStartTime();
			TextView txtEndTime = ((DetailActivity) activity).getTxtEndTime();

			String strTime = Util._obtainDateToDisplay(lngStartReal);
			txtStartTime.setText(strTime);

			strTime = Util._obtainDateToDisplay(lngEndReal);
			txtEndTime.setText(strTime);
		}
	}

	public void _updateDangerousZoneTime(final boolean booAccepted,
			final String strStartTime, final String strEndTime) {
		Register objRegister = ((DetailActivity) activity).getObjRegister();
		TextView txtStartTime = ((DetailActivity) activity).getTxtStartTime();
		TextView txtEndTime = ((DetailActivity) activity).getTxtEndTime();
		if (booAccepted) {
			objRegister.setBooFlgDangerousZone(true);

			Date datTime = Calendar.getInstance().getTime();

			long lngRegister = Util._obtainDateFromString(datTime, strStartTime
					+ ":00");
			objRegister.setLngStartDateRegister(lngRegister);

			lngRegister = Util._obtainDateFromString(datTime, strEndTime
					+ ":00");
			objRegister.setLngEndDateRegister(lngRegister);

			txtStartTime.setText(strStartTime);
			txtEndTime.setText(strEndTime);
		} else {
			objRegister.setBooFlgDangerousZone(false);

			String strRealStartTime = Constants.LBL_EMPTY;

			long lngRealStartTime = objRegister.getLngStartDateReal();
			if (lngRealStartTime != -1) {
				strRealStartTime = Util
						._createSTRDangerousZoneFormatTime(lngRealStartTime);
			}

			txtStartTime.setText(strRealStartTime);
			txtEndTime.setText(Constants.LBL_EMPTY);
		}
		_updateDZButton(booAccepted);
	}

	/**
	 * Llenar la información asociada al detalle del registro.
	 */
	public void _displayInformationScreen() {
		LinearLayout linDetailInfo = (LinearLayout) activity
				.findViewById(R.id.linDetailInfo);
		linDetailInfo.removeAllViews();

		Listing objListing = ((DetailActivity) activity).getObjListing();
		List<Form> lstForm = _obtainLstFormByPK(objListing.getIntFluxId());

		if (lstForm == null) {
			Log.d("DetailController",
					"_displayInformationScreen - _obtainLstFormByPK() retornó NULL");
			return;
		}
		for (int intIte = 0; intIte < lstForm.size(); intIte++) {
			// TODO DESCOMENTAR PARA QUE SE PUEDA USAR EL FLAG DE VISIBILIDAD
			// DEL DATO
			// if(lstForm.get(intIte).isBooFlgVisible()){
			String strLabel = lstForm.get(intIte).getStrName() + " : ";
			String strInfo = objListing.getLstField().get(intIte);

			SpannableStringBuilder strSpanBuilder = Util._createTextInfo(
					strLabel, strInfo, Color.rgb(228, 108, 10), Color.WHITE,
					1.5f, 1.5f);

			TextView txtInfo = new TextView(activity);
			txtInfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			txtInfo.setText(strSpanBuilder);
			txtInfo.setVisibility(View.VISIBLE);

			linDetailInfo.addView(txtInfo);
			// }
		}
	}

	/**
	 * Customizar el bloque de UI separado para mostrar la información ingresada
	 * por el operador. Se llena la lista de preguntas con las respuestas
	 * registradas y se llama al método que se encarga de llenar la UI.
	 */
	public void _displayAnsweredScreen() {
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();
		Register objRegister = ((DetailActivity) activity).getObjRegister();
		List<RegisterAnswer> lstRegisterAnswer = _obtainLstRegisterAnswer(
				objRegister.getIntListId(), objRegister.getIntDownloadCounter());
		if (lstRegisterAnswer == null) {
			Log.d("DetailController",
					"_displayAnsweredScreen - _obtainLstRegisterAnswer() retornó NULL");
		} else {
			for (int iRegAnswer = 0; iRegAnswer < lstRegisterAnswer.size(); iRegAnswer++) {
				RegisterAnswer objRegisterAnswer = lstRegisterAnswer
						.get(iRegAnswer);
				int intQuestionId = objRegisterAnswer.getIntQuestionId();
				Question objQuestion = lstQuestion
						.get(_obtainQuestionIndexById(intQuestionId));
				objQuestion.setStrTemporalAbrevAnswer(objRegisterAnswer
						.getStrValue());
				objQuestion.setStrTemporalRealAnswer(objRegisterAnswer
						.getStrValueReal());
				if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO) {
					_createPhotoQuestionFilled(objQuestion);
				} else {
					if (objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE) {
						objQuestion.setObjTemporalExtraInfo(objRegisterAnswer
								.getStrValueReal() + " | ");
					}
				}
			}
		}
		_displayAnswerScreen(false);
	}

	public void _displayAnswerScreen(final boolean booIsEditable) {
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();
		LinearLayout linAnswersInfo = (LinearLayout) activity
				.findViewById(R.id.linDetailAnswers);
		linAnswersInfo.removeAllViews();

		for (Question objQuestion : lstQuestion) {
			if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO
					&& objQuestion.getObjTemporalExtraInfo() == null) {
				objQuestion.setObjTemporalExtraInfo(new ArrayList<Photo>());
			}
			String strLabel = _displayQuestionLabel(objQuestion, booIsEditable);
			String strInfo = objQuestion.getStrTemporalRealAnswer();

			SpannableStringBuilder strSpanBuilder = Util._createTextInfo(
					strLabel, strInfo, Color.rgb(0, 176, 240), Color.WHITE,
					1.5f, 1.5f);

			TextView txtAnswer = new TextView(activity);
			txtAnswer.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			int intHash = "Text".hashCode() + objQuestion.getIntQuestionId();
			txtAnswer.setId(intHash);
			txtAnswer.setText(strSpanBuilder);

			int intState = objQuestion.getIntState();

			if (booIsEditable) {
				if (intState == Constants.PREG_OPCIONAL_PERMANENTE) {
					txtAnswer.setVisibility(View.VISIBLE);
				} else {
					txtAnswer.setVisibility(View.GONE);
				}
			} else {
				if ((!strInfo.equals(Constants.LBL_EMPTY) && objQuestion
						.getIntTemplateId() != Constants.RESP_ACCION_TOMA_FOTO)
						|| (!strInfo.equals(Constants.LBL_EMPTY)
								&& !strInfo.equals("0") && objQuestion
								.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO)
						|| intState == Constants.PREG_OPCIONAL_PERMANENTE) {
					txtAnswer.setVisibility(View.VISIBLE);
				} else {
					txtAnswer.setVisibility(View.GONE);
				}
			}
			linAnswersInfo.addView(txtAnswer);
		}
	}

	private void _createPhotoQuestionFilled(final Question objQuestion) {
		ArrayList<Photo> lstPhoto = new ArrayList<Photo>();
		if (!(objQuestion.getStrTemporalRealAnswer() == null || objQuestion
				.getStrTemporalRealAnswer().equals(Constants.LBL_EMPTY))) {
			String[] arrPhotoAbrevNames = objQuestion
					.getStrTemporalAbrevAnswer().split(",");
			String[] arrPhotoRealNames = objQuestion.getStrTemporalRealAnswer()
					.split(",");
			for (int itePhotoNames = 0; itePhotoNames < arrPhotoAbrevNames.length; itePhotoNames++) {
				Photo objPhoto = new Photo(-1,
						arrPhotoAbrevNames[itePhotoNames],
						arrPhotoRealNames[itePhotoNames], true);
				lstPhoto.add(objPhoto);
			}
		}

		int intPhotoCounter = lstPhoto.size();

		objQuestion.setObjTemporalExtraInfo(lstPhoto);
		objQuestion.setStrTemporalAbrevAnswer(String.valueOf(intPhotoCounter));
		objQuestion.setStrTemporalRealAnswer(String.valueOf(intPhotoCounter));
	}

	/**
	 * Se genera el label a mostrar en el detalle del elemento considerando si
	 * es que es posible editarlo y si tiene asociado un shortcut.
	 * 
	 * @param objQuestion
	 * @return strLabel
	 */
	public String _displayQuestionLabel(final Question objQuestion,
			final boolean booIsEditable) {
		if (!booIsEditable
				&& objQuestion.getIntState() != Constants.PREG_OPCIONAL_PERMANENTE) {
			objQuestion.setBooFlgEdition(false);
		}
		return _displayQuestionLabel(objQuestion);
	}

	/**
	 * Se genera el label a mostrar en el detalle del elemento considerando si
	 * es que es posible editarlo y si tiene asociado un shortcut.
	 * 
	 * @param objQuestion
	 * @return strLabel
	 */
	public String _displayQuestionLabel(final Question objQuestion) {
		StringBuilder strBuilder = new StringBuilder();
		if (!(!objQuestion.isBooFlgEdition() && !objQuestion
				.getStrTemporalRealAnswer().equals(Constants.LBL_EMPTY))) {

			int intKey = objQuestion.getIntKey();
			if (intKey != 0) {
				strBuilder.append(" [ ").append(Util._getKeyString(intKey))
						.append(" ] ");
			}
		}
		strBuilder.append(objQuestion.getStrQuestionDescription())
				.append(" : ");

		String strLabel = strBuilder.toString();
		return strLabel;
	}

	/**
	 * Se llena el contenido de la pregunta y se modifica la visibilidad según
	 * el estado
	 * 
	 * @param objQuestion
	 */
	public void _displayCurrentAnswer(final Question objQuestion) {
		LinearLayout linAnswersInfo = (LinearLayout) activity
				.findViewById(R.id.linDetailAnswers);
		int intHash = "Text".hashCode() + objQuestion.getIntQuestionId();
		TextView txtAnswer = (TextView) linAnswersInfo.findViewById(intHash);
		String strLabel = _displayQuestionLabel(objQuestion);
		String strInfo = objQuestion.getStrTemporalRealAnswer();
		txtAnswer.setText(Util._createTextInfo(strLabel, strInfo,
				Color.rgb(0, 176, 240), Color.WHITE, 1.5f, 1.5f));
		txtAnswer.setVisibility(View.VISIBLE);
	}

	/**
	 * Se determina si las horas ingresadas estan en conflicto con alguno de los
	 * registros ya terminados.
	 * 
	 * @param intUserId
	 *            - id del usuario del cual se va a obtener los registros
	 * @param lngStartTime
	 *            - tiempo de inicio que se busca agregar al registro
	 * @param lngEndTime
	 *            - tiempo de fin que se busca agregar al registro
	 * @return booIsInConflict
	 */
	public boolean _isInConflictsWithOtherRegisters(final int intUserId,
			final long lngStartTime, final long lngEndTime) {
		boolean booIsInConflict = false;

		String strStartTime = String.valueOf(lngStartTime);
		String strEndTime = String.valueOf(lngEndTime);

		String[] selectionArgs = new String[] { String.valueOf(intUserId),
				strStartTime, strEndTime, strStartTime, strEndTime,
				strStartTime, strEndTime };
		Cursor objCursor = activity.getContentResolver().query(
				RegisterDAO.QUERY_TIME_CONFLICTS_URI, null, null,
				selectionArgs, null);

		if (objCursor != null && objCursor.getCount() > 0) {
			booIsInConflict = true;
		}
		return booIsInConflict;
	}

	/**
	 * Obtener las preguntas del grupo que se va a empezar a responder.
	 * 
	 * @param intGroupId
	 *            - grupo de preguntas a responder
	 * @return lstQuestionGroup
	 */
	public ArrayList<Question> _obtainLstQuestionByGroupId(final int intGroupId) {
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();

		ArrayList<Question> lstQuestionGroup = new ArrayList<Question>();

		for (Question objQuestion : lstQuestion) {
			if (intGroupId == objQuestion.getIntGroup()) {
				lstQuestionGroup.add(objQuestion);
			}
		}
		return lstQuestionGroup;
	}

	/**
	 * Se obtiene el índice de la pregunta a partir del Question Id.
	 * 
	 * @param intQuestionId
	 *            - id de la pregunta que se desea encontrar en el listado
	 * @return intQuestionIndex - índice de la pregunta buscada en el listado
	 */
	public int _obtainQuestionIndexById(final int intQuestionId) {
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();

		int intQuestionIndex = -1;
		for (int intIte = 0; intIte < lstQuestion.size(); intIte++) {
			Question objQuestion = lstQuestion.get(intIte);
			if (intQuestionId == objQuestion.getIntQuestionId()) {
				intQuestionIndex = intIte;
				break;
			}
		}
		return intQuestionIndex;
	}

	/**
	 * Se guarda la ubicación y la fecha en el registro y de ser posible se
	 * muestra en el detalle.
	 */
	public void _loadLocAndDate() {
		Register objRegister = ((DetailActivity) activity).getObjRegister();
		TextView txtStartTime = ((DetailActivity) activity).getTxtStartTime();
		if (objRegister.getFltLatitudeReal() <= 0) {
			Track objTrack = _obtainCurrentLocation();
			if (objTrack != null) {
				objRegister.setFltLatitudeReal(objTrack.getFltLatitude());
				objRegister.setFltLatitudeRegister(objTrack.getFltLatitude());
				objRegister.setFltLongitudeReal(objTrack.getFltLongitude());
				objRegister.setFltLongitudeRegister(objTrack.getFltLongitude());
				objRegister.setFltGPSAccuracy(objTrack.getFltAccuracy());
			}
		}

		long lngCurrentTime = Calendar.getInstance().getTimeInMillis();
		objRegister.setLngStartDateReal(lngCurrentTime);

		if (!objRegister.isBooFlgDangerousZone()) {
			String strStartTime = Util
					._createSTRDangerousZoneFormatTime(lngCurrentTime);
			txtStartTime.setText(strStartTime);
		}
	}

	/**
	 * Modificar los valores de las preguntas a partir de los resultados
	 * generados al responder una pregunta
	 * 
	 * @param intOptionId
	 */
	private void _changeQuestionsStateFromOptionId(final int intOptionId) {

		List<OptionResult> lstOptionResults = _obtainOptionResultsByPK(intOptionId);
		if (lstOptionResults == null) {
			Log.d("DetailController",
					"_changeQuestionsStateFromOptionId - _obtainOptionResultsByPK() retornó NULL");
			return;
		}
		for (OptionResult objOptionResult : lstOptionResults) {

			int intQuestionIndex = _obtainQuestionIndexById(objOptionResult
					.getIntQuestionId());
			Question objQuestion = ((DetailActivity) activity).getLstQuestion()
					.get(intQuestionIndex);

			String strAbrevAnswer = (objOptionResult.getStrValue()) != null ? objOptionResult
					.getStrValue() : Constants.LBL_EMPTY;
			String strRealAnswer = (objOptionResult.getStrRealValue()) != null ? objOptionResult
					.getStrRealValue() : Constants.LBL_EMPTY;
			_changeQuestionContent(objQuestion, objOptionResult.getIntState(),
					strAbrevAnswer, strRealAnswer,
					objOptionResult.isBooFlgEdition());
		}
	}

	/**
	 * Modificar los valores de las opciones a partir de la opción seleccionada
	 * 
	 * @param intOptionId
	 */
	private void _changeOptionsStateFromOptionId(final int intOptionId) {
		List<OptionModResult> lstOptionModResults = _obtainOptionModResultsByOptionId(intOptionId);
		if (lstOptionModResults == null) {
			Log.d("DetailController",
					"_changeOptionsStateFromOptionId - _obtainOptionModResultsByOptionId() retornó NULL");
			return;
		}
		for (OptionModResult objOptionModResult : lstOptionModResults) {
			_updateOptionState(objOptionModResult.getIntOptionModId(),
					objOptionModResult.getIntState());
		}
	}

	private void _updateOptionState(final int intOptionId, final int intState) {
		String strOptionId = String.valueOf(intOptionId);
		String strState = String.valueOf(intState);

		String[] selectionArgs = new String[] { strOptionId };

		ContentValues valuesOptionModResult = new ContentValues();
		valuesOptionModResult.put(OptionDAO.COL_COD_ESTADO, strState);

		activity.getContentResolver().update(
				OptionDAO.UPDATE_STATE_BY_OPTIONID_URI, valuesOptionModResult,
				null, selectionArgs);
	}

	/**
	 * Modificar los valores de las preguntas a partir de los resultados
	 * generados por la validación de una pregunta
	 * 
	 * @param intOptionIndex
	 *            - indice de la opcion seleccionada
	 */
	public void _changeQuestionsStateFromValidationId(final int intValidationId) {
		List<ValidationResult> lstValidationResults = _obtainValidationResultsByPK(intValidationId);
		if (lstValidationResults == null) {
			Log.d("DetailController",
					"_changeQuestionsStateFromValidationId - _obtainValidationResultsByPK() retornó NULL");
		} else {
			for (ValidationResult objValidationesult : lstValidationResults) {
				int intQuestionIndex = _obtainQuestionIndexById(objValidationesult
						.getIntQuestionId());
				Question objQuestion = ((DetailActivity) activity)
						.getLstQuestion().get(intQuestionIndex);

				String strAbrevAnswer = (objValidationesult.getStrValue()) != null ? objValidationesult
						.getStrValue() : Constants.LBL_EMPTY;
				String strRealAnswer = (objValidationesult.getStrRealValue()) != null ? objValidationesult
						.getStrRealValue() : Constants.LBL_EMPTY;
				_changeQuestionContent(objQuestion,
						objValidationesult.getIntState(), strAbrevAnswer,
						strRealAnswer, objValidationesult.isBooFlgEdition());
			}
		}
	}

	public void _changeQuestionsStateFromObsCodeId(final int intObsCodeId) {
		int intFluxId = ((DetailActivity) activity).getObjListing()
				.getIntFluxId();

		List<ObsCode> lstObsCode = _obtainObsCodesByObsCodeIdAndFluxId(
				intObsCodeId, intFluxId);

		if (lstObsCode == null) {
			Log.d("DetailController",
					"_changeQuestionsStateFromObsCodeId - _obtainObsCodesByObsCodeIdAndFluxId() retornó NULL");
			return;
		}
		for (int iObsCode = 0; iObsCode < lstObsCode.size(); iObsCode++) {
			ObsCode objObsCode = lstObsCode.get(iObsCode);

			int intQuestionIndex = _obtainQuestionIndexById(objObsCode
					.getIntQuestionId());
			Question objQuestion = ((DetailActivity) activity).getLstQuestion()
					.get(intQuestionIndex);

			_changeQuestionContent(objQuestion, objObsCode.getIntState(),
					Constants.LBL_EMPTY, Constants.LBL_EMPTY,
					objQuestion.isBooFlgEdition());
		}
	}

	private void _changeQuestionContent(Question objQuestion, int intState,
			String strAbrevAnswer, String strRealAnswer, boolean booFlgEdition) {
		if (((DetailActivity) activity).getObjRegister()
				.isBooFlgDangerousZone()
				&& (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO)
				&& (intState == Constants.PREG_OBLIGATORIA)) {
			objQuestion.setIntState(Constants.PREG_OPCIONAL_TEMPORAL);
		} else {
			objQuestion.setIntState(intState);
		}

		objQuestion.setBooFlgEdition(booFlgEdition);

		if (!strAbrevAnswer.equals(Constants.LBL_EMPTY)) {
			objQuestion.setStrTemporalAbrevAnswer(strAbrevAnswer);
		}

		if (!strRealAnswer.equals(Constants.LBL_EMPTY)) {
			objQuestion.setStrTemporalRealAnswer(strRealAnswer);

			// TODO Validar si es que otras preguntas se ponen UPDATED = true al
			// momento de responder una pregunta previa.
			objQuestion.setBooIsUpdated(true);
		}
	}

	public boolean _answerFirstQuestion(final int intKeyCode) {
		Question objQuestion = ((DetailActivity) activity).getLstQuestion()
				.get(0);
		List<Option> lstOption = ((DetailActivity) activity).getLstOption();

		if (objQuestion.isBooFlgOption()) {
			for (int iOption = 0; iOption < lstOption.size(); iOption++) {
				Option objOption = lstOption.get(iOption);
				if (intKeyCode == objOption.getIntKey() || intKeyCode == objOption.getIntKey()-1) {
					_executeQuestionModifierByFlux(objQuestion.getIntFluxId());
					_fillQuestionAndOptions(iOption, objQuestion, objOption);
					return true;
				}
			}
			Log.w(LOGNAME, "no se encontró respuesta para la primera pregunta "
					+ objQuestion.getStrQuestionDescription());
		} else {
			Log.w(LOGNAME, "primera pregunta con flag opción = 0");
		}
		return false;
	}

	/**
	 * Habilitar preguntas a partir de valores relacionados con campos del
	 * registro
	 * 
	 * @param intFluxId
	 */
	private void _executeQuestionModifierByFlux(final int intFluxId) {
		Listing objListing = ((DetailActivity) activity).getObjListing();
		List<FluxValidation> lstFluxValidation = _obtainLstFluxValidationByFluxId(intFluxId);
		if (lstFluxValidation != null && lstFluxValidation.size() > 0) {
			for (FluxValidation objFluxValidation : lstFluxValidation) {
				Validation objValidation = _obtainValidationByPK(objFluxValidation
						.getIntValidationId());
				if (objValidation != null) {
					String[] selectionArgs = new String[] {
							String.valueOf(objListing.getIntListId()),
							objFluxValidation.getStrValue() };
					Cursor curSearched = activity.getContentResolver().query(
							ListingDAO.QUERY_BY_FIELD_URI,
							null,
							ListingDAO.TB_NAME + Constants.LBL_DOT
									+ ListingDAO.COL_IDE_LISTADO + " = ? AND "
									+ ListingDAO.TB_NAME + Constants.LBL_DOT
									+ objValidation.getStrValue() + " = ? ",
							selectionArgs, null);
					if (curSearched != null && curSearched.getCount() > 0) {
						_changeQuestionsStateFromValidationId(objValidation
								.getIntValidationId());
					}
				}
			}
		}
	}

	public int _answerCurrentQuestion(final String strInputValue) {
		int intQuestionIndex = ((DetailActivity) activity).getIntTempIndex();
		Question objQuestion = ((DetailActivity) activity).getLstQuestion()
				.get(intQuestionIndex);
		List<Option> lstOption = ((DetailActivity) activity).getLstOption();
		int intActivityState = ((DetailActivity) activity)
				.getIntActivityState();

		DataType objDataType = ((DetailActivity) activity).getController()
				._obtainDataTypeByDataTypeId(objQuestion.getIntDataTypeId());
		if (strInputValue.length() < objDataType.getIntLongitudeMin()) {
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
			activity.showDialog(Constants.DIA_MSG_ERR_INPUT_NOT_MIN, bundle);
			return STATE_UNANSWERED;
		}

		if (objQuestion.isBooFlgOption()) {
			String strInputValueClean = strInputValue.replaceAll("\\s",
					Constants.LBL_EMPTY);
			for (int iOption = 0; iOption < lstOption.size(); iOption++) {
				Option objOption = lstOption.get(iOption);
				if (strInputValueClean.equals(objOption
						.getStrOptionAbreviation())) {
					_fillQuestionAndOptions(iOption, objQuestion, objOption);
					return STATE_ANSWERED;
				}
			}
			Log.w(LOGNAME, "no se encontró respuesta para la pregunta "
					+ objQuestion.getStrQuestionDescription());
		} else {
			// si se tiene alguna validación a realizar
			if (objQuestion.getIntValidationId() > 0) {
				int intState = _validateQuestion(
						objQuestion.getIntValidationId(), strInputValue);
				if (intState == STATE_INVALIDATED) {
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE,
							true);
					activity.showDialog(
							Constants.DIA_MSG_ERR_VALIDATION_NOT_PASSED, bundle);
					return STATE_UNANSWERED;
				} else if (intState == STATE_RESTRICTIVE) {
					Validation objValidation = _obtainValidationByPK(objQuestion
							.getIntValidationId());
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE,
							true);
					bundle.putString(Constants.CUSTOM_ERROR_MESSAGE,
							objValidation.getStrErrorMessage());
					activity.showDialog(Constants.DIA_MSG_ERR_CUSTOM, bundle);
					return STATE_UNANSWERED;
				} else if (intState == STATE_UNRESTRICTIVE) {
					Validation objValidation = _obtainValidationByPK(objQuestion
							.getIntValidationId());
					Bundle bundle = new Bundle();
					bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE,
							true);

					bundle.putInt("intActivityState", intActivityState);
					bundle.putString("strInputValue", strInputValue);
					bundle.putParcelable("objQuestion", objQuestion);

					bundle.putString(Constants.CUSTOM_ERROR_MESSAGE,
							objValidation.getStrErrorMessage());
					activity.showDialog(Constants.DIA_CON_UNRESTRICTIVE, bundle);
					return STATE_UNRESTRICTIVE;
				}
			}

			int intStateResult = _fillInputValue(intActivityState,
					strInputValue, objQuestion);
			return intStateResult;
		}
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
		activity.showDialog(Constants.DIA_MSG_ERR_OPTION_UNDEFINED, bundle);
		return STATE_UNANSWERED;
	}

	public int _fillInputValue(int intActivityState, String strInputValue,
			Question objQuestion) {
		String strInputValueClean = strInputValue.replaceAll("\\s",
				Constants.LBL_EMPTY);
		if (objQuestion.getIntState() == Constants.PREG_OBLIGATORIA
				&& strInputValueClean.equals(Constants.LBL_EMPTY)) {
			Bundle bundle = new Bundle();
			bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
			activity.showDialog(Constants.DIA_MSG_ERR_INPUT_EMPTY, bundle);
			return STATE_UNANSWERED;
		} else {
			objQuestion.setBooIsUpdated(true);
			if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE
					&& objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE
					&& objQuestion.getIntTemplateId() != Constants.RESP_ACCION_TOMA_FOTO) {
				String strInputUpdated = (String) objQuestion
						.getStrTemporalAbrevAnswer() + " | " + strInputValue;
				// objQuestion.setStrTemporalAbrevAnswer(strInputUpdated);
				// objQuestion.setStrTemporalRealAnswer(strInputUpdated);				
				objQuestion.setStrTemporalRealAnswer(strInputUpdated);
				/**
				 * OBS REQUERIMIENTO DE OBS AGREGADO - JV
				 */
				objQuestion.setStrTemporalAbrevAnswer(strInputUpdated);

			} else {
				objQuestion.setStrTemporalAbrevAnswer(strInputValue);
				objQuestion.setStrTemporalRealAnswer(strInputValue);

			}
			_displayCurrentAnswer(objQuestion);
			return STATE_ANSWERED;
		}
	}

	/**
	 * Validar la pregunta
	 * 
	 * @param intValidationId
	 * @param strInputValue
	 */
	private int _validateQuestion(int intValidationId, String strInputValue) {
		Validation objValidation = _obtainValidationByPK(intValidationId);
		if (objValidation.getIntValidationType() == Constants.VALIDACION_USUARIO_MEDIDORES) {
			int intUserId = AppPreferences.getInstance(activity)._loadUserId();
			UserValidation objUserValidation = _obtainUserValidationByData(
					intUserId, intValidationId, strInputValue,
					Constants.FLG_ENABLED);
			if (objUserValidation != null) {
				return STATE_VALIDATED;
			} else {
				if (objValidation.isBooRestrictive()) {
					return STATE_RESTRICTIVE;
				} else {
					return STATE_UNRESTRICTIVE;
				}
			}
		} else {
			String[] arrValidationValues = (String[]) _obtainValidationValues(
					Constants.VAL_STR_EQUAL, objValidation.getStrValue(),
					objValidation.getStrMinValue(),
					objValidation.getStrMaxValue());

			if (strInputValue.compareTo(arrValidationValues[0]) == 0) {
				_changeQuestionsStateFromValidationId(intValidationId);
				return STATE_VALIDATED;
			} else {
				if (objValidation.isBooRestrictive()) {
					return STATE_RESTRICTIVE;
				} else {
					return STATE_UNRESTRICTIVE;
				}
			}
		}
	}

	private void _fillQuestionAndOptions(int iOption, Question objQuestion,
			Option objOption) {
		if (objOption.isBooFlgImpossibility()) {
			// el registro no se pudo finalizar, se indica que solo se ejecutó.
			Register objRegister = ((DetailActivity) activity).getObjRegister();
			objRegister.setIntState(Constants.REGISTER_STATE_EXECUTED);
		}
		objQuestion.setStrTemporalAbrevAnswer(objOption.getStrDescription());
		objQuestion.setStrTemporalRealAnswer(objOption.getStrOptionReal());
		objQuestion.setBooIsUpdated(true);

		_changeQuestionsStateFromOptionId(objOption.getIntOptionId());
		_changeOptionsStateFromOptionId(objOption.getIntOptionId());
		_displayCurrentAnswer(objQuestion);
	}

	public void _answerReadingQuestion(final Integer[] arrReadingResults) {
		int intQuestionIndex = ((DetailActivity) activity).getIntTempIndex();
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();
		Question objQuestion = lstQuestion.get(intQuestionIndex);

		String strLastReading = String
				.valueOf(arrReadingResults[arrReadingResults.length - 1]);
		objQuestion.setStrTemporalAbrevAnswer(strLastReading);
		objQuestion.setStrTemporalRealAnswer(strLastReading);
		objQuestion.setObjTemporalExtraInfo(arrReadingResults);
		objQuestion.setBooIsUpdated(true);
		_displayCurrentAnswer(objQuestion);
		for (int iReadingResults = 1; iReadingResults < arrReadingResults.length; iReadingResults++) {
			objQuestion = lstQuestion.get(intQuestionIndex + iReadingResults);
			String strReadingInput = String
					.valueOf(arrReadingResults[iReadingResults]);
			objQuestion.setStrTemporalAbrevAnswer(strReadingInput);
			objQuestion.setStrTemporalRealAnswer(strReadingInput);
		}

		objQuestion = lstQuestion.get(intQuestionIndex + 4);

		String strObsCode = String.valueOf(arrReadingResults[0]);
		objQuestion.setStrTemporalAbrevAnswer(strObsCode);
		objQuestion.setStrTemporalRealAnswer(strObsCode);

		int intObsCode = arrReadingResults[0];
		_changeQuestionsStateFromObsCodeId(intObsCode);

		_answerCodObsQuestion(objQuestion, lstQuestion.get(intQuestionIndex));
	}

	/**
	 * Responder la pregunta según el código de observación 1. Se obtiene la
	 * validación asociada a la pregunta del código de observación. 2. Se
	 * obtiene el valor con el cual se comprar la última lectura. 3. Se
	 * modifican las preguntas si es que el valor de la última lectura es la
	 * misma a la indicada por la validación
	 * 
	 * @param objQuestionCodObs
	 *            - objeto pregunta donde se registra el código de observación
	 * @param objQuestionFinalReading
	 *            - objeto pregunta donde se registra la última lectura
	 */
	public void _answerCodObsQuestion(final Question objQuestionCodObs,
			final Question objQuestionFinalReading) {
		if (objQuestionCodObs.getIntValidationId() == 0) {
			return;
		}
		Validation objValidation = _obtainValidationByPK(objQuestionCodObs
				.getIntValidationId());
		Integer[] arrValidationValues = (Integer[]) _obtainValidationValues(
				Constants.VAL_INT_EQUAL, objValidation.getStrValue(), null,
				null);

		int intFinalReading = Integer.parseInt(objQuestionFinalReading
				.getStrTemporalRealAnswer());

		if (intFinalReading == arrValidationValues[0]) {
			_changeQuestionsStateFromValidationId(objQuestionCodObs
					.getIntValidationId());
		}
	}

	public boolean _validateInWorkingHours(Register objRegister) {
		String strStartTime = null;
		String strEndTime = null;
		if (!((DetailActivity) activity).isBooDone()) {
			if (!objRegister.isBooFlgDangerousZone()) {
				strStartTime = (Util._createSTRFormatTime(objRegister
						.getLngStartDateReal()));
				strEndTime = (Util._createSTRFormatTime(objRegister
						.getLngEndDateReal()));

			} else {
				strStartTime = (Util._createSTRFormatTime(objRegister
						.getLngStartDateRegister()));
				strEndTime = (Util._createSTRFormatTime(objRegister
						.getLngEndDateRegister()));
			}

			int intResult = Util._correctTimeFormat(activity, strStartTime,
					strEndTime);
			if (intResult < 0) {
				return true;
			} else {
				Bundle bundle = new Bundle();
				bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
				activity.showDialog(intResult, bundle);
				return false;
			}
		} else {
			return true;
		}
	}

	private void _displayReadingDialogEmpty() {
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
		activity.showDialog(Constants.DIA_MSG_ERR_READING_EMPTY, bundle);
		((DetailActivity) activity).setBooAnswerReading(false);
	}

	private void _displayReadingDialogMinimun(final String strErrorMessage) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.OVER_DIALOG_WITHOUT_REPLACE, true);
		bundle.putString(Constants.OVER_DIALOG_ERROR_CUSTOM, strErrorMessage);
		((DetailActivity) activity).setBooAnswerReading(false);
		activity.showDialog(Constants.DIA_MSG_ERR_CUSTOM_VALUE, bundle);
	}

	public Integer[] _validateReading(String strInput) {
		((DetailActivity) activity).setBooAnswerReading(true);
		if (strInput.equals(Constants.LBL_EMPTY)) {
			_displayReadingDialogEmpty();
			return null;
		}
		int intInput = Integer.parseInt(strInput);
		int intReadingStep = ((DetailActivity) activity).getIntReadingStep();

		int intQuestionIndex = ((DetailActivity) activity).getIntTempIndex();
		int intValidationId = ((DetailActivity) activity).getLstQuestion()
				.get(intQuestionIndex).getIntValidationId();
		Validation objValidation = _obtainValidationByPK(intValidationId);

		Integer[] arrValidationValues = (Integer[]) _obtainValidationValues(
				Constants.VAL_MIXMAX, objValidation.getStrValue(),
				objValidation.getStrMinValue(), objValidation.getStrMaxValue());

		boolean booIsLessThanMinimun = intInput < arrValidationValues[0];
		boolean booCanTestValue = !objValidation.isBooRestrictive()
				|| (objValidation.isBooRestrictive() && !booIsLessThanMinimun);

		Integer[] arrResult = null;
		if (booCanTestValue) {
			arrResult = _validateReading(intReadingStep, intInput,
					arrValidationValues);
		}

		if (booIsLessThanMinimun) {
			_displayReadingDialogMinimun(objValidation.getStrErrorMessage());
		}
		return arrResult;
	}

	/**
	 * ON THIS METHOD WE GET TO HANDLE THE MIN AN MAX VALUE AND ALSO THE COD_OBS
	 * TO BE PUT
	 */
	private Integer[] _validateReading(int intReadingStep, int intInput,
			Integer[] arrValidationValues) {
		switch (intReadingStep) {
		case 1:
			intObsCode = 8;
			input1 = intInput;
			input2 = -1;
			input3 = -1;

			if (arrValidationValues[1] <= input1
					&& input1 <= arrValidationValues[2]) {
				intObsCode = 1;
				return new Integer[] { intObsCode, input1 };
			} else {
				((DetailActivity) activity).setIntReadingStep(2);
				activity.showDialog(Constants.DIA_CUS_READING_INPUT, null);
			}
			break;
		case 2:
			input2 = intInput;
			if (input2 == input1) {
				intObsCode = 2;
				return new Integer[] { intObsCode, input1, input2 };
			} else if (arrValidationValues[1] <= input2
					&& input2 <= arrValidationValues[2]) {
				intObsCode = 3;
				return new Integer[] { intObsCode, input1, input2 };
			} else {
				((DetailActivity) activity).setIntReadingStep(3);
				activity.showDialog(Constants.DIA_CUS_READING_INPUT, null);
			}
			break;
		case 3:
			input3 = intInput;
			if (input3 == input1) {
				intObsCode = 4;
			} else if (input3 == input2) {
				intObsCode = 5;
			} else if (arrValidationValues[1] <= input3
					&& input3 <= arrValidationValues[2]) {
				intObsCode = 6;
			} else {
				intObsCode = 7;
			}
			return new Integer[] { intObsCode, input1, input2, input3 };
		}
		return null;
	}

	/**
	 * Se resetean los valores de las opciones
	 */
	public void _restartOptions() {
		// TODO requiere optimizar este método para que no se realice update a
		// todos los registros de la tabla
		List<Option> lstOption = _obtainAllNonAvailableOptions();
		if (lstOption == null) {
			Log.d("DetailController",
					"_restartOptions - _obtainAllNonAvailableOptions() retornó NULL");
			return;
		}
		for (Option objOption : lstOption) {
			_updateOptionState(objOption.getIntOptionId(), 1);
		}
	}

	private List<Listing> _obtainDuplicateListing(Listing _objListingCheck) {

		List<Listing> lstListingDuplicate = super
				._obtainDuplicates(_objListingCheck.getStrField1());
		if (lstListingDuplicate.size() > 1)
			return lstListingDuplicate;

		else
			return null;
	}
	
	
	public void _saveInfo(int detEstadoNoEditable) {
		Register objRegister = ((DetailActivity) activity).getObjRegister();
		Flux objFlux = ((DetailActivity) activity).getObjFlux();
		List<Question> lstQuestion = ((DetailActivity) activity)
				.getLstQuestion();

		Listing objListingCheck = ((DetailActivity) activity).getObjListing();
		List<Listing> lstListDuplicate = this
				._obtainDuplicateListing(objListingCheck);
		
		
		if (objFlux.getIntProcess() == Constants.FLG_DISABLED) {
			if (detEstadoNoEditable != Constants.DET_ESTADO_NO_EDITABLE) {
				_fillDateLocationID(objRegister);
				_fillInfo(objRegister, lstQuestion);
				
				if (lstListDuplicate != null) {
					for (Listing listing : lstListDuplicate) {
						if (listing.getIntListId() == objRegister.getIntListId()) {
							Log.d("Register ID continue =>", String.valueOf(listing.getIntListId()) );
							continue;
							
						}
						Register objRegisterTmp = objRegister;
						objRegisterTmp.setIntListId(listing.getIntListId());
						objRegisterTmp.setIntDownloadCounter(listing.getIntDownloadCounter());
						objRegisterTmp.setStrCommercialManagmentCode(listing.getStrCommercialManagmentCode());
						Log.d("Register lstListDuplicate ID =>", String.valueOf(objRegisterTmp.getIntListId()) );
						_fillDateLocationID(objRegisterTmp);
						_fillInfo(objRegisterTmp, lstQuestion);
					}
				}
				
			} else {
				_updateOrInsertRegisterAnswers(objRegister, lstQuestion);
			}

		} else if (objFlux.getIntProcess() == Constants.FLG_ENABLED) {
			if (detEstadoNoEditable == Constants.DET_ESTADO_PREFLUJO_ENPROCESO) {
				objRegister
						.setIntDeliveryState(Constants.DELIVERY_STATE_NOT_CONSIDERED);
				_fillInfo(objRegister, lstQuestion);
			} else if (detEstadoNoEditable == Constants.DET_ESTADO_NO_EDITABLE) {
				_updateOrInsertRegisterAnswers(objRegister, lstQuestion);
			} else {
				_fillDateLocationID(objRegister);
				Register objRegisterSearched = _obtainRegisterByPK(
						objRegister.getIntListId(),
						objRegister.getIntDownloadCounter());
				if (objRegisterSearched == null) {
					_fillInfo(objRegister, lstQuestion);
				} else {
					_updateRegister(objRegister);
					_updateOrInsertRegisterAnswers(objRegister, lstQuestion);
				}

			}
		}
		Date dat1 = new Date();
		dat1.setTime(objRegister.getLngStartDateRegister());

		Date dat2 = new Date();
		dat2.setTime(objRegister.getLngEndDateRegister());

		// Toast.makeText(activity, dat1.toLocaleString()+ " - " +
		// dat2.toLocaleString(), Toast.LENGTH_LONG).show();
		Listing objListing = ((DetailActivity) activity).getObjListing();
		_updateListing(objRegister, objListing);
		
		if (lstListDuplicate != null) {
			for (Listing listing : lstListDuplicate) {
				if (listing.getIntListId() == objRegister.getIntListId()) {
					continue;
				}
				Register objRegisterTmp = objRegister;
				objRegisterTmp.setIntListId(listing.getIntListId());
				_updateListing(objRegisterTmp, listing);
				Log.v("INSERTADO DUP ID =>", "");
			}
		}
	}

	private void _fillInfo(Register objRegister, List<Question> lstQuestion) {
		Register objRegisterSearched = _obtainRegisterByPK(
				objRegister.getIntListId(), objRegister.getIntDownloadCounter());
		if (objRegisterSearched == null) {
			_insertRegister(objRegister);
			_insertRegisterAnswers(objRegister, lstQuestion);
		} else {
			Log.d("GMD", "Se intentó registrar un duplicado: listado_id = "
					+ objRegister.getIntListId() + " - contador de descarga = "
					+ objRegister.getIntDownloadCounter());
		}
	}

	public void _fillDateLocationID(Register objRegister) {
		objRegister.setIntDeliveryState(Constants.DELIVERY_STATE_WAITING);

		if (!objRegister.isBooFlgDangerousZone()) {
			objRegister.setLngStartDateRegister(objRegister
					.getLngStartDateReal());
			objRegister.setLngEndDateRegister(objRegister.getLngEndDateReal());

			objRegister
					.setFltLatitudeRegister(objRegister.getFltLatitudeReal());
			objRegister.setFltLongitudeRegister(objRegister
					.getFltLongitudeReal());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(objRegister.getLngStartDateRegister());
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			objRegister.setLngStartDateRegister(cal.getTimeInMillis());

			cal.setTimeInMillis(objRegister.getLngEndDateRegister());
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			objRegister.setLngEndDateRegister(cal.getTimeInMillis());
		}

		TelephonyManager objTelephonyManager = (TelephonyManager) activity
				.getSystemService(Activity.TELEPHONY_SERVICE);
		objRegister.setStrMovilNumber(objTelephonyManager.getDeviceId());
	}

	public void _insertRegister(final Register objRegister) {
		ContentValues objContentValues = RegisterDAO
				.createContentValues(objRegister);
		activity.getContentResolver().insert(RegisterDAO.INSERT_URI,
				objContentValues);
	}

	public void _insertRegisterAnswers(final Register objRegister,
			final List<Question> lstQuestion) {
		ContentValues objContentValues = null;
		RegisterAnswer objRegisterAnswer = null;
		for (Question objQuestion : lstQuestion) {
			_updateByValidation(objQuestion);

			if (!objQuestion.getStrTemporalRealAnswer().equals(
					Constants.LBL_EMPTY)) {
				if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO) {
					_addPhotoNamesToInsert(objQuestion);
				}

				DataType objDataType = ((DetailActivity) activity)
						.getController()._obtainDataTypeByDataTypeId(
								objQuestion.getIntDataTypeId());
				if (objDataType.getStrDescription().equals("Int")
						&& objDataType.getIntDecimalsQuantity() > 0) {
					int intAnswer = Integer.parseInt(objQuestion
							.getStrTemporalRealAnswer())
							/ (10 ^ objDataType.getIntDecimalsQuantity());
					objQuestion.setStrTemporalAbrevAnswer(String
							.valueOf(intAnswer));
					objQuestion.setStrTemporalRealAnswer(String
							.valueOf(intAnswer));
				}

				objRegisterAnswer = new RegisterAnswer(objQuestion,
						objRegister.getIntListId(),
						objRegister.getIntDownloadCounter());
				objContentValues = RegisterAnswerDAO
						.createContentValues(objRegisterAnswer);
				activity.getContentResolver().insert(
						RegisterAnswerDAO.INSERT_URI, objContentValues);
			}
		}
	}

	public void _updateListing(final Register objRegister,
			final Listing objListing) {
		int intListingFinalState = -1;
		if (objRegister.getIntState() == Constants.REGISTER_STATE_EXECUTED) {
			intListingFinalState = Constants.LIST_EJECUTADO;
		} else if (objRegister.getIntState() == Constants.REGISTER_STATE_FINALIZED) {
			intListingFinalState = Constants.LIST_FINALIZADO;
		} else if (objRegister.getIntState() == Constants.REGISTER_STATE_INPROGRESS) {
			intListingFinalState = Constants.LIST_ENPROCESO;
		}

		ContentValues valuesListing = new ContentValues();
		valuesListing.put(ListingDAO.COL_COD_ESTADO, intListingFinalState);

		String[] selectionArgs = new String[] { String.valueOf(objListing
				.getIntListId()) };
		activity.getContentResolver().update(ListingDAO.UPDATE_WORKED_URI,
				valuesListing, null, selectionArgs);
	}

	private void _updateByValidation(final Question objQuestion) {
		if (objQuestion.getIntValidationId() > 0) {
			Validation objValidation = _obtainValidationByPK(objQuestion
					.getIntValidationId());
			if (objValidation.getIntValidationType() == Constants.VALIDACION_USUARIO_MEDIDORES) {
				int intUserId = AppPreferences.getInstance(activity)
						._loadUserId();
				UserValidation objUserValidation = _obtainUserValidationByData(
						intUserId, objQuestion.getIntValidationId(),
						objQuestion.getStrTemporalAbrevAnswer(),
						Constants.FLG_ENABLED);
				if (objUserValidation != null) {
					_updateUserValidationState(
							objUserValidation.getIntUserValidationId(),
							Constants.FLG_DISABLED);
				}
			}
		}
	}

	/**
	 * Guardar la información registrada en la carga
	 */
	public void _updateOrInsertRegisterAnswers(final Register objRegister,
			final List<Question> lstQuestion) {
		for (Question objQuestion : lstQuestion) {
			DataType objDataType = ((DetailActivity) activity)
					.getController()
					._obtainDataTypeByDataTypeId(objQuestion.getIntDataTypeId());
			if (objDataType.getStrDescription().equals("Int")
					&& objDataType.getIntDecimalsQuantity() > 0) {
				int intAnswer = Integer.parseInt(objQuestion
						.getStrTemporalRealAnswer())
						/ (10 ^ objDataType.getIntDecimalsQuantity());
				objQuestion
						.setStrTemporalAbrevAnswer(String.valueOf(intAnswer));
				objQuestion.setStrTemporalRealAnswer(String.valueOf(intAnswer));
			}
			if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO) {
				_addPhotoNamesToInsert(objQuestion);
			}
			if (objQuestion.isBooIsUpdated()) {
				_updateByValidation(objQuestion);
				RegisterAnswer objRegisterAnswer = _obtainRegisterAnswerByPK(
						objRegister.getIntListId(),
						objRegister.getIntDownloadCounter(),
						objQuestion.getIntQuestionId());
				if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO) {
					_addPhotoNamesToInsert(objQuestion);
				}
				if (objRegisterAnswer != null) {
					objRegisterAnswer.setStrValue(objQuestion
							.getStrTemporalAbrevAnswer());
					objRegisterAnswer.setStrValueReal(objQuestion
							.getStrTemporalRealAnswer());
					objRegisterAnswer.setBooIsUpdated(objQuestion
							.isBooIsUpdated());
					_updateRegisterAnswer(objRegisterAnswer);
				} else {
					objRegisterAnswer = new RegisterAnswer(
							objRegister.getIntListId(),
							objRegister.getIntDownloadCounter(),
							objQuestion.getIntQuestionId(),
							objQuestion.getStrTemporalAbrevAnswer(),
							objQuestion.getStrTemporalRealAnswer(),
							objQuestion.isBooIsUpdated());

					ContentValues objContentValues = RegisterAnswerDAO
							.createContentValues(objRegisterAnswer);
					activity.getContentResolver().insert(
							RegisterAnswerDAO.INSERT_URI, objContentValues);
				}
			}
		}
	}

	private void _addPhotoNamesToInsert(Question objQuestion) {
		String strAbrevAnswer = Constants.LBL_EMPTY;
		String strRealAnswer = Constants.LBL_EMPTY;

		ArrayList<Photo> lstPhoto = (ArrayList<Photo>) objQuestion
				.getObjTemporalExtraInfo();

		for (Photo objPhoto : lstPhoto) {
			strAbrevAnswer += objPhoto.getStrAbrevName();
			strRealAnswer += objPhoto.getStrFullName();
			if (!(objPhoto.equals(lstPhoto.get(lstPhoto.size() - 1)))) {
				strRealAnswer += ",";
				strAbrevAnswer += ",";
			}
		}
		objQuestion.setStrTemporalAbrevAnswer(strAbrevAnswer);
		objQuestion.setStrTemporalRealAnswer(strRealAnswer);
	}

	public boolean _isWithOptionalAnswers(List<Question> lstQuestion) {
		for (Question objQuestion : lstQuestion) {
			if ((!objQuestion.getStrTemporalRealAnswer().equals(
					Constants.LBL_EMPTY) && objQuestion.getIntTemplateId() != Constants.RESP_ACCION_TOMA_FOTO)
					|| (!objQuestion.getStrTemporalRealAnswer().equals("0") && !objQuestion
							.getStrTemporalRealAnswer().equals(
									Constants.LBL_EMPTY))
					&& objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO) {
				return true;
			}
		}
		return false;
	}
}
