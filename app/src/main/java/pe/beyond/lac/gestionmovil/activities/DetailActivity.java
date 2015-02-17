package pe.beyond.lac.gestionmovil.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Flux;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.Photo;
import pe.beyond.gls.model.Question;
import pe.beyond.gls.model.Register;
import pe.beyond.gls.model.UserValidation;
import pe.beyond.gls.model.Validation;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.controllers.DetailController;
import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.CustomDialog;
import pe.beyond.lac.gestionmovil.utils.Util;

public class DetailActivity extends BaseActivity implements OnKeyListener {
	/**
	 * Controlador de la actividad
	 */
	private DetailController controller;

	/**
	 * Informaci�n del registro visualizado (contiene la informaci�n que se
	 * recibe del servidor)
	 */
	private Listing objListing;

	/**
	 * Informaci�n del registro trabajado (contiene la informaci�n que se env�a
	 * al servidor)
	 */
	private Register objRegister;

	/**
	 * Informaci�n del flujo que sigue el registro
	 */
	private Flux objFlux;

	/**
	 * Lista que contiene las opciones de la pregunta trabajada en cierto
	 * momento
	 */
	private ArrayList<Option> lstOption;

	/**
	 * Lista que contiene las preguntas a responder, acorde al flujo asociado al
	 * registro
	 */
	private List<Question> lstQuestion;

	/**
	 * Estado en la que se encuentra la actividad: PREFLUJO_PENDIENTE = 0,
	 * PREFLUJO_ENPROCESO = 1, ENFLUJO = 2, POSFLUJO = 3, NO_EDITABLE = 4
	 */
	private int intActivityState;

	/**
	 * Indicador de la posici�n del item de lstQuestion que se est� respondiendo
	 * en cierto momento
	 */
	private int intCurrentQuestionIndex;

	/**
	 * Indicador auxiliar del intCurrentQuestionIndex
	 */
	private int intTempIndex;

	/**
	 * Indicador del GroupId anterior, para verificar si la siguiente pregunta
	 * requiere una respuesta dentro del mismo di�logo o si requiere otro
	 * di�logo
	 */
	private int intPreviousGroupId;

	/**
	 * Instancia del di�logo trabajado en cierto momento
	 */
	private Dialog dlgCurrentDialog;

	/**
	 * Botones de acceso a zonas peligrosas y mapa.
	 */
	private Button btnDangerousZone, btnLocation;

	/**
	 * Indicador de si el registro fue actualizado o no, para pedir confirmaci�n
	 * de salida para un registro ya trabajado
	 */
	private boolean booRegisterUpdated = false;

	/**
	 * Indicador de si se esta respondiendo una pregunta de lectura.
	 */
	private boolean booAnswerReading;

	/**
	 * Indicador del n�mero de lectura ingresado, utilizado para las preguntas
	 * con Constants.RESP_ACCION_TOMA_LECTURA.
	 */
	private int intReadingStep;

	/**
	 * Guarda el input de la informaci�n ingresada en la pregunta de lecturas
	 * [0] c�digo de observaci�n resultante de los inputs [1...3] lecturas
	 * ingresadas. Puede ser entre 1 y 3 seg�n las validaciones previamente
	 * realizadas
	 */
	private Integer[] arrReadingResults;

	/**
	 * Guarda la referencia a los di�logos que han sido creados, con la
	 * finalidad de poder limpiar el input en caso se aplique el reset de las
	 * preguntas.
	 */
	private ArrayList<Dialog> lstDialogs = new ArrayList<Dialog>();

	/*
	 * TextViews que muestran la hora de inicio/fin entre las que se trabaj� el
	 * registro.
	 */
	private TextView txtStartTime, txtEndTime;

	/*
	 * Variable requerida para indicar a la actividad anterior que no se
	 * encontr� la registro buscado, primordialmente para cuando se realice la
	 * b�squeda nativa. Desde el listado no debe generar este error, mediante QR
	 * el error es manejado en la actividad BarCodeCaptureActivity.
	 */
	public static boolean FLAG_SEARCH_NOT_FOUND = false;

	/********** OVERRIDED METHODS **********/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		controller = new DetailController(this);

		int intListingId = controller._obtainListingIdFromIntent(getIntent());
		if (intListingId < 0) {
			FLAG_SEARCH_NOT_FOUND = true;
			finish();
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.detail_screen);
			_restart(intListingId);
		}
	}

	@Override
	public boolean onSearchRequested() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		activity.getMenuInflater().inflate(R.menu.detail_screen, menu);
		MenuItem itemInit = menu.findItem(R.id.detail_screen_menu_init_process);
		if (!(objFlux.getIntProcess() == Constants.FLG_ENABLED && intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE)) {
			itemInit.setVisible(false);
		}

		
		MenuItem itemReset = menu.findItem(R.id.detail_screen_menu_reset);
		if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
			itemReset.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem itemHelp = menu.findItem(R.id.detail_screen_menu_help);
		if ((objFlux.getIntProcess() == Constants.FLG_ENABLED && (intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO || intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE))
				|| (objFlux.getIntProcess() == Constants.FLG_DISABLED && intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE)) {
			itemHelp.setVisible(true);
		} else {
			itemHelp.setVisible(false);
		}
		return true;
	}

	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.detail_screen_menu_reset:
			_restart(objListing.getIntListId());
			break;
		case R.id.detail_screen_menu_help:
			Bundle bundle = new Bundle();
			bundle.putString(Constants.KEY_HEADER, lstQuestion.get(0)
					.getStrQuestionDescription());
			bundle.putParcelableArrayList(Constants.KEY_POSSIBLE_OPTIONS,
					(ArrayList<? extends Parcelable>) lstOption);
			showDialog(Constants.DIA_CUS_HELP_OPTIONS, bundle);
			break;
		case R.id.detail_screen_menu_init_process:
			showDialog(Constants.DIA_MSG_PROCESS_STARTED, null);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
		Dialog objDialog = super.onCreateDialog(id, bundle);

		if (id >= Constants.DIA_MUL || id == Constants.DIA_CUS_READING_INPUT) {
			lstDialogs.add(objDialog);
		}
		return objDialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
		super.onPrepareDialog(id, dialog, bundle);
		if (bundle != null) {
			// si no se agrega el flag, entonces se reemplaza el di�logo
			boolean booWithoutReplace = bundle.getBoolean(
					Constants.OVER_DIALOG_WITHOUT_REPLACE, false);
			if (!booWithoutReplace) {
				dlgCurrentDialog = dialog;
			}
		} else {
			dlgCurrentDialog = dialog;
		}
		if (id >= Constants.DIA_MUL) {
			_prepareMultipleInputDialog(dialog, bundle);

		} else if (id == Constants.DIA_CUS_READING_INPUT) {
			_prepareReadingInputDialog(dialog);

		}
	}

	public void _prepareMultipleInputDialog(final Dialog dialog,
			final Bundle bundle) {
		Util._createHelpLayout(dialog, activity, lstOption);
		ScrollView scrRoot = (ScrollView) dialog
				.findViewById(Constants.ID_DIALOG_ROOT);

		int intLinearHash = "Linear".hashCode()
				+ bundle.getInt(Constants.KEY_REQUEST_FOCUS);
		LinearLayout linQuestion = (LinearLayout) scrRoot
				.findViewWithTag(intLinearHash);
		linQuestion.setVisibility(View.VISIBLE);

		int intEditHash = "Edit".hashCode()
				+ bundle.getInt(Constants.KEY_REQUEST_FOCUS);
		dialog.findViewById(intEditHash).requestFocus();
		int intGroupId = lstQuestion.get(intTempIndex).getIntGroup();

		if (intGroupId != 0) {
			ArrayList<Question> lstQuestionsAsked = controller
					._obtainLstQuestionByGroupId(intPreviousGroupId);
			for (int iQuestion = 0; iQuestion < lstQuestionsAsked.size(); iQuestion++) {
				Question objQuestion = lstQuestionsAsked.get(iQuestion);
				int intFocusHash = "Edit".hashCode()
						+ objQuestion.getIntQuestionId();
				if (intGroupId == objQuestion.getIntGroup()
						&& bundle.getInt(Constants.KEY_REQUEST_FOCUS) != objQuestion
								.getIntQuestionId()) {
					dialog.findViewById(intFocusHash).setFocusable(false);
				} else {
					dialog.findViewById(intFocusHash).setFocusable(true);
					dialog.findViewById(intFocusHash).requestFocus();
				}
			}
		} else {
			Question objQuestion = lstQuestion.get(intTempIndex);
			int intFocusHash = "Edit".hashCode()
					+ objQuestion.getIntQuestionId();
			if (intGroupId == objQuestion.getIntGroup()
					&& bundle.getInt(Constants.KEY_REQUEST_FOCUS) != objQuestion
							.getIntQuestionId()) {
				dialog.findViewById(intFocusHash).setFocusable(false);
			} else {
				dialog.findViewById(intFocusHash).setFocusable(true);
				dialog.findViewById(intFocusHash).requestFocus();
			}
		}
		int intFocusHash = "Edit".hashCode()
				+ bundle.getInt(Constants.KEY_REQUEST_FOCUS);
		dialog.findViewById(intFocusHash).setFocusable(true);
		dialog.findViewById(intFocusHash).requestFocus();
	}

	public void _prepareReadingInputDialog(final Dialog dialog) {
		int intTitleId = -1;
		switch (intReadingStep) {
		case 1:
			intTitleId = R.string.dialog_reading_title_first;
			break;
		case 2:
			intTitleId = R.string.dialog_reading_title_second;
			break;
		case 3:
			intTitleId = R.string.dialog_reading_title_third;
			break;
		}
		TextView txtTitle = (TextView) dialog
				.findViewById(R.id.text_reading_title);
		txtTitle.setText(intTitleId);

		EditText ediInput = (EditText) dialog
				.findViewById(R.id.edit_reading_input);

		DataType objDataType = ((DetailActivity) activity).getController()
				._obtainDataTypeByDataTypeId(
						lstQuestion.get(intTempIndex).getIntDataTypeId());
		if (objDataType.getStrDescription().equals("Int")) {
			ediInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (objDataType.getStrDescription().equals("String")
				|| objDataType.getStrData().equals("NO PRECISA")) {
			ediInput.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		}

		int intMaxChars = objDataType.getIntLongitudeMax();
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(intMaxChars);
		ediInput.setFilters(FilterArray);

		ediInput.setText(Constants.LBL_EMPTY);
		ediInput.requestFocus();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_DETAIL) {
			Question objQuestion = lstQuestion.get(intTempIndex);
			if (resultCode == RESULT_OK) {
				ArrayList<Photo> lstPhoto = null;
				Bundle bundle = data.getExtras();
				lstPhoto = bundle.getParcelableArrayList(Constants.LIST_PHOTO);
				ArrayList<Photo> lstPhotoPrevious = (ArrayList<Photo>) objQuestion
						.getObjTemporalExtraInfo();

				boolean booPhotosModified = false;
				if (lstPhoto.size() == lstPhotoPrevious.size()) {
					for (int intIte = 0; intIte < lstPhoto.size(); intIte++) {
						if (lstPhoto
								.get(intIte)
								.getStrAbrevName()
								.compareTo(
										lstPhotoPrevious.get(intIte)
												.getStrAbrevName()) != 0) {
							booPhotosModified = true;
							break;
						}
					}
				} else {
					booPhotosModified = true;
				}

				if (booPhotosModified) {
					objQuestion.setObjTemporalExtraInfo(lstPhoto);
					int intState = controller._answerCurrentQuestion(String
							.valueOf(lstPhoto.size()));
					if (intState == Constants.STATE_ANSWERED) {
						if (objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE
								&& (intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO || intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE)) {
							controller._loadLocAndDate();
						}
					}

					if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
						booRegisterUpdated = true;
					}
				}

				if (intActivityState == Constants.DET_ESTADO_POSFLUJO
						|| intActivityState == Constants.DET_ESTADO_ENFLUJO) {
					if (!(intActivityState == Constants.DET_ESTADO_POSFLUJO && (objQuestion
							.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE || objQuestion
							.getIntState() == Constants.PREG_OPCIONAL_TEMPORAL))) {
						intActivityState = Constants.DET_ESTADO_ENFLUJO;
						intCurrentQuestionIndex = intTempIndex;

						_proceedToNextQuestion();
					}
				}

			} else if (resultCode == RESULT_CANCELED) {
				if (objQuestion.getIntState() == Constants.PREG_OBLIGATORIA) {
					ArrayList<String> lstPhoto = (ArrayList<String>) objQuestion
							.getObjTemporalExtraInfo();

					Bundle bundle = new Bundle();
					bundle.putStringArrayList(Constants.LIST_PHOTO, lstPhoto);
					bundle.putBoolean(
							Constants.BL_MIN_ONE_IMAGE,
							objQuestion.getIntState() == Constants.PREG_OBLIGATORIA);

					callActivity(
							PhotoManagerActivity.class.getName(),
							bundle,
							Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_DETAIL);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		switch (intActivityState) {
		case Constants.DET_ESTADO_PREFLUJO_PENDIENTE:
			if (controller._isWithOptionalAnswers(lstQuestion)) {
				showDialog(Constants.DIA_CON_BACK, null);
			} else {
				super.onBackPressed();
			}
			break;
		case Constants.DET_ESTADO_PREFLUJO_ENPROCESO:
			super.onBackPressed();
			break;
		case Constants.DET_ESTADO_POSFLUJO:
			// se consulta si desea salir sin guardar lo trabajado
			showDialog(Constants.DIA_CON_BACK, null);
			break;
		case Constants.DET_ESTADO_NO_EDITABLE:
			// si alg�n registro fue modificado (p.e. foto u observaci�n), se
			// consulta si desea salir sin guardar los cambios
			if (booRegisterUpdated) {
				showDialog(Constants.DIA_CON_BACK, null);
			} else {
				super.onBackPressed();
			}
			break;
		}
	}

	public boolean _answerFirstQuestion(final int keyCode) {
		lstOption = controller._obtainOptionsByQuestionId(lstQuestion.get(
				intCurrentQuestionIndex).getIntQuestionId());
		boolean booAnswered = controller._answerFirstQuestion(keyCode == 55 ? 58 : keyCode);//lcanales  	boolean booAnswered = controller._answerFirstQuestion(keyCode == 55 ? 58 : keyCode);
		if (booAnswered) {
			intActivityState = Constants.DET_ESTADO_ENFLUJO;
			controller._loadLocAndDate();
			_proceedToNextQuestion();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO OPTIMIZACION: Usar un HASH de los Keys de todas las preguntas,
		// y obtener la pregunta sin tener que recorrer toda la lista de
		// preguntas.
		if (keyCode == Constants.KEY_MAP) {
			 controller._displayLocationInMap(objListing.getIntListId());

			int intValido = java.lang.Math.round(objListing.getFltLatitude());
			
			/* MAPA VALIDADO */
			if ( intValido  != 0 && 
				 intValido  != -1) {
				controller._displayLocationInMap(objListing.getIntListId());
			} else {
				android.widget.Toast.makeText(activity,
						Constants.MSG_ERROR_MAP_NOT_FOUND,
						android.widget.Toast.LENGTH_LONG).show();
			}

			return true;
		}

		if ((intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE)
				|| (intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO)) {
			if (_answerFirstQuestion(keyCode)) {
				return true;
			}
		}
		if (intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE
				|| intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO
				|| intActivityState == Constants.DET_ESTADO_POSFLUJO) {
			if (keyCode == Constants.KEY_DAN_ZONE) {
				boolean booDangerousZoneOn = !(Boolean) btnDangerousZone
						.getTag(R.id.btnDangerousZone);
				controller._executeDangerousZone(booDangerousZoneOn);
				return true;
			}
		}

		if (intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE
				|| intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO) {
			for (int iQuestion = 0; iQuestion < lstQuestion.size(); iQuestion++) {
				Question objQuestion = lstQuestion.get(iQuestion);

				if (keyCode == objQuestion.getIntKey()
						&& objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE) {
					intTempIndex = iQuestion;
					lstOption = controller
							._obtainOptionsByQuestionId(lstQuestion.get(
									intTempIndex).getIntQuestionId());
					_executeQuestion(objQuestion);
					return true;
				}
			}
		}

		switch (intActivityState) {
		case Constants.DET_ESTADO_PREFLUJO_PENDIENTE:
			break;
		case Constants.DET_ESTADO_PREFLUJO_ENPROCESO:
			break;
		case Constants.DET_ESTADO_ENFLUJO:
			// se responden mediante el uso del onKey(View view, int keyCode,
			// KeyEvent event)
			break;
		case Constants.DET_ESTADO_POSFLUJO:
			// si se usa el key para concluir con el input
			if (keyCode == Constants.ACTIVITY_ACTION_NEXT) {
				long lngCurrentTime = System.currentTimeMillis();
				objRegister.setLngEndDateReal(lngCurrentTime);
				if (!objRegister.isBooFlgDangerousZone() && !booDone) {
					objRegister.setLngEndDateRegister(lngCurrentTime);
					txtEndTime.setText(Util
							._createSTRDangerousZoneFormatTime(lngCurrentTime));
				}
				boolean booInWorkingHours = controller
						._validateInWorkingHours(objRegister);
				if (booInWorkingHours) {
					_proceedToLastQuestion();
				}
				return true;
			}

			for (int iQuestion = 0; iQuestion < lstQuestion.size(); iQuestion++) {
				Question objQuestion = lstQuestion.get(iQuestion);
				if (keyCode == objQuestion.getIntKey()
						&& (objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE || (objQuestion
								.getIntState() != Constants.PREG_INVALIDA && objQuestion
								.isBooFlgEdition()))) {
					intTempIndex = iQuestion;
					lstOption = controller
							._obtainOptionsByQuestionId(lstQuestion.get(
									intTempIndex).getIntQuestionId());
					_executeQuestion(objQuestion);
					return true;
				}
			}
			break;
		case Constants.DET_ESTADO_NO_EDITABLE:
			for (int iQuestion = 0; iQuestion < lstQuestion.size(); iQuestion++) {
				Question objQuestion = lstQuestion.get(iQuestion);

				if (keyCode == objQuestion.getIntKey()
						&& objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE) {
					intTempIndex = iQuestion;
					lstOption = controller
							._obtainOptionsByQuestionId(lstQuestion.get(
									intTempIndex).getIntQuestionId());
					_executeQuestion(objQuestion);
					return true;
				}
			}
			// si se usa el key para concluir con el input
			if (keyCode == Constants.ACTIVITY_ACTION_NEXT) {
				if (booRegisterUpdated) {
					_proceedToLastQuestion();
				} else {
					finish();
				}
				return true;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		Question objQuestion = lstQuestion.get(intTempIndex);
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
				int intHelpHashCode = "HelpOptions".hashCode();
				LinearLayout linHelp = (LinearLayout) dlgCurrentDialog
						.findViewById(intHelpHashCode);
				if (linHelp != null) {
					if (linHelp.getVisibility() == View.VISIBLE) {
						linHelp.setVisibility(View.GONE);
					} else if (linHelp.getVisibility() == View.GONE) {
						linHelp.setVisibility(View.VISIBLE);
					}
					return true;
				}
			}

			if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_MULTIPLE_INPUT) {

				if (keyCode == Constants.KEY_NEXT
						|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {

					int intHash = "Edit".hashCode()
							+ objQuestion.getIntQuestionId();
					EditText ediInput = (EditText) dlgCurrentDialog
							.findViewById(intHash);
					String strInput = ediInput.getText().toString();

					lstOption = controller
							._obtainOptionsByQuestionId(lstQuestion.get(
									intTempIndex).getIntQuestionId());
					if (lstOption == null) {
						Log.d("DetailActivity",
								"onKey - _obtainOptionsByQuestionId() retorn� NULL");
					}
					int intState = controller._answerCurrentQuestion(strInput
							.toUpperCase());
					if (intState == Constants.STATE_ANSWERED) {
						_executeAnsweredQuestion(objQuestion);
					} else if (intState == Constants.STATE_UNRESTRICTIVE) {
						// manejado dentro del
						// controller._answerCurrentQuestion()
					}
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (intActivityState != Constants.DET_ESTADO_ENFLUJO) {
						dlgCurrentDialog.dismiss();
					}
				}
			} else if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_LECTURA) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						|| keyCode == Constants.ACTIVITY_ACTION_NEXT
						|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
					EditText ediInput = (EditText) view;
					String strInput = ediInput.getText().toString();

					arrReadingResults = controller._validateReading(strInput);
					if (booAnswerReading) {
						_answerReading();
					}
				}
			}

		} else if (event.getAction() == KeyEvent.ACTION_UP) {
			if (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_LECTURA
					&& (keyCode == Constants.KEY_NEXT || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {

				return true;
			}
		}
		return false;
	}

	public void _executeAnsweredQuestion(Question objQuestion) {
		dlgCurrentDialog.dismiss();
		dlgCurrentDialog = null;
		if (objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE
				&& (intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO || intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE)) {
			controller._loadLocAndDate();
		}
		if (intActivityState == Constants.DET_ESTADO_POSFLUJO) {
			if (objQuestion.getIntState() == Constants.PREG_OBLIGATORIA) {
				intActivityState = Constants.DET_ESTADO_ENFLUJO;
				intCurrentQuestionIndex = intTempIndex;
			}

			if (objQuestion.getIntState() == Constants.PREG_OPCIONAL_PERMANENTE
					|| objQuestion.getIntState() == Constants.PREG_OPCIONAL_TEMPORAL) {
				// si la pregunta es individual
				_executeIfNotIndividual(objQuestion.getIntQuestionId());
			}
		}

		if (intActivityState == Constants.DET_ESTADO_ENFLUJO) {
			_proceedToNextQuestion();
		}

		if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
			booRegisterUpdated = true;
		}
	}

	public void _executeIfNotIndividual(final int intQuestionId) {
		if (intPreviousGroupId != 0) {
			ArrayList<Question> lstQuestionsAsked = controller
					._obtainLstQuestionByGroupId(intPreviousGroupId);
			boolean booHasNextQuestion = false;
			if (lstQuestionsAsked.size() > 1) {
				int intCurrentIndex = -1;
				for (int iQuestion = 0; iQuestion < lstQuestionsAsked.size(); iQuestion++) {
					if (intCurrentIndex == -1) {
						if (intQuestionId == lstQuestionsAsked.get(iQuestion)
								.getIntQuestionId()) {
							intCurrentIndex = iQuestion;
						}
					} else {
						if (lstQuestionsAsked.get(iQuestion).getIntState() == Constants.PREG_OBLIGATORIA) {
							intTempIndex = controller
									._obtainQuestionIndexById(lstQuestionsAsked
											.get(iQuestion).getIntQuestionId());
							intCurrentQuestionIndex = intTempIndex;
							_executeQuestion(lstQuestionsAsked.get(iQuestion));
							booHasNextQuestion = true;
							break;
						}
					}
				}
				if (!booHasNextQuestion) {
					_executeWithoutNextQuestion(lstQuestionsAsked.get(
							intCurrentIndex).getIntQuestionId());
				}
			}
		}
	}

	public void _executeWithoutNextQuestion(final int intQuestionId) {
		int intCurrentIndex = controller
				._obtainQuestionIndexById(intQuestionId);
		for (int iQuestionUpdate = intCurrentIndex; iQuestionUpdate < lstQuestion
				.size(); iQuestionUpdate++) {
			Question objQuestionUpdate = lstQuestion.get(iQuestionUpdate);
			if (objQuestionUpdate.getIntState() != Constants.PREG_INVALIDA) {
				if (objQuestionUpdate.getIntState() == Constants.PREG_OBLIGATORIA) {
					intActivityState = Constants.DET_ESTADO_ENFLUJO;
					break;
				} else {
					controller._displayCurrentAnswer(objQuestionUpdate);
				}
			}
		}
	}

	/**
	 * Cargar la data inicial y la UI seg�n el estado del registro a trabajar
	 * 
	 * @param intListingId
	 */
	public void _restart(final int intListingId) {
		objListing = controller._obtainListingByPK(intListingId);
		objFlux = controller._obtainFluxByPK(objListing.getIntFluxId());

		if (((AppGMD) this.getApplication())._executeNextPosition == 1) {
			int nextPosition = controller._getNextPosition(intListingId);
			if (nextPosition != -1) {
				((AppGMD) this.getApplication())._NextListingPosition = nextPosition;
			}
			// ((AppGMD) this.getApplication())._executeNextPosition = 0;
		}

		switch (objListing.getIntState()) {
		case Constants.LIST_PENDIENTE:
			intActivityState = Constants.DET_ESTADO_PREFLUJO_PENDIENTE;
			break;
		case Constants.LIST_ENPROCESO:
			intActivityState = Constants.DET_ESTADO_PREFLUJO_ENPROCESO;
			break;
		case Constants.LIST_EJECUTADO:
			intActivityState = Constants.DET_ESTADO_NO_EDITABLE;
			break;
		case Constants.LIST_FINALIZADO:
			intActivityState = Constants.DET_ESTADO_NO_EDITABLE;
			break;
		}
		_initInformation();
		_initUI();

		_restartMultipleInputDialogs(lstDialogs, lstQuestion);

	}

	private void _restartMultipleInputDialogs(
			ArrayList<Dialog> lstMultipleInputDialogs,
			List<Question> lstQuestion) {
		for (Dialog objDialog : lstMultipleInputDialogs) {
			if (objDialog != null && objDialog instanceof CustomDialog) {
				int[] arrEditTextId = ((CustomDialog) objDialog)
						.getArrInputIds();
				for (int intEditTextId : arrEditTextId) {
					View ediInput = objDialog.findViewById(intEditTextId);
					if (ediInput != null && ediInput instanceof EditText) {
						((EditText) ediInput).setText(Constants.LBL_EMPTY);
					}
				}
			}
		}
	}

	private boolean booDone = false;

	public boolean isBooDone() {
		return booDone;
	}

	/**
	 * Cargar la data inicial
	 */
	public void _initInformation() {
		intCurrentQuestionIndex = 0;
		intPreviousGroupId = -1;

		controller._restartOptions();
		lstQuestion = controller._obtainLstQuestionByFluxId(objListing
				.getIntFluxId());
		if (intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE) {
			boolean booFlgQR = getIntent().getExtras().getBoolean(
					Constants.OBTAINED_BY_QR);
			String strUserCode = AppPreferences.getInstance(activity)
					._loadCurrentUserCode();
			objRegister = new Register(objListing, booFlgQR, strUserCode);
		} else if (objFlux.getIntProcess() == Constants.FLG_ENABLED
				&& intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO) {
			objRegister = controller._obtainRegisterByPK(
					objListing.getIntListId(),
					objListing.getIntDownloadCounter());
			objRegister.setIntState(Constants.REGISTER_STATE_FINALIZED);
		} else if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
			booDone = true;
			objRegister = controller._obtainRegisterByPK(
					objListing.getIntListId(),
					objListing.getIntDownloadCounter());
		}
	}

	public void _initUI() {

		/*
		 * SE SETEA EL FOCUS PARA QUE AL INGRESAR AL PAYLOADACTIVITY CON DOS
		 * CLICKS EN EL TRACKPAD NO SE SELECCIONE AUTOMATICAMENTE LA ZONA
		 * PELIGROSA.
		 */
		txtStartTime = (TextView) findViewById(R.id.txtStartTime);
		txtStartTime.setFocusable(true);
		txtStartTime.requestFocus();

		/* SE CREA OnTouchListener PARA SOBREESCRIBIR LA INTERACCION TACTIL
		OnTouchListener oTbtn = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		}; */

		/*
		 * 
		 */
		txtEndTime = (TextView) findViewById(R.id.txtEndTime);
		btnDangerousZone = (Button) findViewById(R.id.btnDangerousZone);

		//btnDangerousZone.setOnTouchListener(oTbtn);

		btnLocation = (Button) findViewById(R.id.btn_display_map);
		txtStartTime.setText(Constants.LBL_EMPTY);
		txtEndTime.setText(Constants.LBL_EMPTY);
		//btnLocation.setOnTouchListener(oTbtn);

		btnLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int intListingId = objListing.getIntListId();

                //lcanales
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                //lcanales


				/*
				 * VALIDA SI EL REGISTRO TIENE PUNTO GPS DISPONIBLE. NO =>
				 * MENSAJE TOAST
				 */
				int intValido = java.lang.Math.round(objListing.getFltLatitude());
		
				if ( intValido  != 0 && 
					 intValido  != -1) {
					controller._displayLocationInMap(intListingId);
				} else {
					android.widget.Toast.makeText(activity,
							Constants.MSG_ERROR_MAP_NOT_FOUND,
							android.widget.Toast.LENGTH_LONG).show();
				}
			}
		});

		if (intActivityState == Constants.DET_ESTADO_PREFLUJO_PENDIENTE) {
			controller._displayAnswerScreen(true);
			controller._displayTimeDangerousZone();
		} else if (intActivityState == Constants.DET_ESTADO_PREFLUJO_ENPROCESO) {
			controller._displayAnsweredScreen();
			controller._displayTimeDangerousZone();

			String strStartTime = Util._obtainDateToDisplay(objRegister
					.getLngStartDateRegister());
			String strEndTime = Util._obtainDateToDisplay(objRegister
					.getLngEndDateRegister());

			txtStartTime.setText(strStartTime);
			txtEndTime.setText(strEndTime);
		} else if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
			controller._displayAnsweredScreen();

			String strStartTime = Util._obtainDateToDisplay(objRegister
					.getLngStartDateRegister());
			String strEndTime = Util._obtainDateToDisplay(objRegister
					.getLngEndDateRegister());

			txtStartTime.setText(strStartTime);
			txtEndTime.setText(strEndTime);
			btnDangerousZone.setVisibility(View.INVISIBLE);
		}
		controller._displayInformationScreen();
	}

	/**
	 * Iniciar proceso, en caso el flujo asociado al registro lo permita
	 */
	public void _startProcess() {
		objRegister.setIntState(Constants.REGISTER_STATE_INPROGRESS);
		controller._saveInfo(Constants.DET_ESTADO_PREFLUJO_ENPROCESO);
		finish();
	}

	/**
	 * 
	 */
	public void _answerReading() {
		if (arrReadingResults != null) {
			dlgCurrentDialog.dismiss();
			dlgCurrentDialog = null;
			controller._answerReadingQuestion(arrReadingResults);
			arrReadingResults = null;
			_proceedToNextQuestion();
		}

		booAnswerReading = false;
	}

	/**
	 * Obtener el �ndice de la siguiente pregunta a trabajar tomando en cuenta
	 * si es o no obligatoria y si ya se respondi� de manera impl�cita
	 * 
	 * La lista se cre� ordenada por NUM_ORDEN_PRG, por lo que el primero que
	 * cumpla la condici�n es el siguiente.
	 * 
	 * @return �ndice de la siguiente pregunta o -1 si lleg� al final
	 */
	private int _obtainNextObligedQuestionIndex() {
		for (int iQuestion = intCurrentQuestionIndex + 1; iQuestion < lstQuestion
				.size(); iQuestion++) {
			Question objQuestion = lstQuestion.get(iQuestion);

			if (objQuestion.getIntState() != Constants.PREG_INVALIDA) {
				controller._displayCurrentAnswer(objQuestion);
			}
			if (objQuestion.getIntState() == Constants.PREG_OBLIGATORIA) {
				return iQuestion;
			}
		}
		return -1;
	}

	/**
	 * Buscar la siguiente pregunta y configurar la aplicaci�n para que habilite
	 * lo necesario para trabajarla. Se usa cuando se esta siguiendo el flujo de
	 * preguntas obligatorias.
	 */
	private void _proceedToNextQuestion() {

		intCurrentQuestionIndex = _obtainNextObligedQuestionIndex();

		/*
		 * si el �ndice no es negativo, se trabaja con la pregunta en dicha
		 * posici�n, sino, quiere decir que se lleg� al final del flujo.
		 */
		if (intCurrentQuestionIndex >= 0) {
			/*
			 * si la pregunta tiene una respuesta, se busca nuevamente por la
			 * que le procede, sino se prepara la UI para responderla
			 */
			Question objQuestion = lstQuestion.get(intCurrentQuestionIndex);

			boolean booPhotoIsEmpty = (objQuestion.getIntTemplateId() == Constants.RESP_ACCION_TOMA_FOTO && objQuestion
					.getStrTemporalRealAnswer().equals("0"));

			if (objQuestion.getStrTemporalRealAnswer().equals(
					Constants.LBL_EMPTY)
					|| booPhotoIsEmpty) {
				intTempIndex = intCurrentQuestionIndex;
				_executeQuestion(objQuestion);
			} else {
				_proceedToNextQuestion();
			}
		} else {
			if (intActivityState != Constants.DET_ESTADO_POSFLUJO) {
				long lngCurrentTime = System.currentTimeMillis();
				objRegister.setLngEndDateReal(lngCurrentTime);
				if (!objRegister.isBooFlgDangerousZone() && !booDone) {
					objRegister.setLngEndDateRegister(lngCurrentTime);
					txtEndTime.setText(Util
							._createSTRDangerousZoneFormatTime(lngCurrentTime));
				}
				_proceedToLastQuestion();
			}
		}
	}

	/**
	 * Mostrar el di�logo para finalizar el ingreso de respuestas.
	 */
	private void _proceedToLastQuestion() {
		showDialog(Constants.DIA_CUS_DETAIL_END_ANSWER, null);
		((AppGMD) getApplication())._stateList = 1;
	}

	/**
	 * Preparar la actividad para responder a la pregunta que se esta enviando
	 * 
	 * @param objQuestion
	 *            - pregunta que se va a trabajar en la actividad
	 */
	private void _executeQuestion(final Question objQuestion) {
		boolean booIsDifferentGroup = objQuestion.getIntGroup() != intPreviousGroupId;
		if (booIsDifferentGroup) {
			intPreviousGroupId = objQuestion.getIntGroup();
		}

		int intQuestionId = lstQuestion.get(intTempIndex).getIntQuestionId();
		lstOption = controller._obtainOptionsByQuestionId(intQuestionId);
		switch (objQuestion.getIntTemplateId()) {
		case Constants.RESP_ACCION_IMPLICITA:
			break;
		case Constants.RESP_ACCION_MULTIPLE_INPUT:
			_executeMultipleInput(booIsDifferentGroup, objQuestion);
			break;
		case Constants.RESP_ACCION_TOMA_FOTO:
			Bundle bundle = new Bundle();
			ArrayList<Photo> lstPhoto = (ArrayList<Photo>) objQuestion
					.getObjTemporalExtraInfo();
			bundle.putParcelableArrayList(Constants.LIST_PHOTO, lstPhoto);
			bundle.putBoolean(Constants.BL_MIN_ONE_IMAGE,
					objQuestion.getIntState() == Constants.PREG_OBLIGATORIA);
			callActivity(PhotoManagerActivity.class.getName(), bundle,
					Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_DETAIL);
			break;
		case Constants.RESP_ACCION_TOMA_LECTURA:
			intReadingStep = 1;
			showDialog(Constants.DIA_CUS_READING_INPUT, null);
			break;
		}
	}

	public void _executeMultipleInput(final boolean booIsDifferentGroup,
			final Question objQuestion) {
		Bundle bundle = new Bundle();
		if (objQuestion.getIntValidationId() > 0) {
			Validation objValidation = controller
					._obtainValidationByPK(objQuestion.getIntValidationId());
			if (objValidation.getIntValidationType() == Constants.VALIDACION_USUARIO_MEDIDORES) {
				int intUserId = AppPreferences.getInstance(activity)
						._loadUserId();
				List<UserValidation> lstUserValidation = controller
						._obtainLstUserValidationByIds(intUserId,
								objValidation.getIntValidationId());
				if (lstUserValidation != null && lstUserValidation.size() > 0) {
					lstOption = new ArrayList<Option>();
					for (UserValidation objUserValidation : lstUserValidation) {
						Option objOption = new Option();
						objOption.setStrOptionAbreviation(null);
						objOption.setStrDescription(objUserValidation
								.getStrOptionReal());
						lstOption.add(objOption);
					}
				}
			}
		}
		bundle.putInt(Constants.KEY_REQUEST_FOCUS,
				objQuestion.getIntQuestionId());

		if (objQuestion.getIntGroup() != 0) {
			if (booIsDifferentGroup) {
				ArrayList<Question> lstQuestionsAsked = controller
						._obtainLstQuestionByGroupId(intPreviousGroupId);
				bundle.putParcelableArrayList(Constants.KEY_QUESTIONS_ASKED,
						(ArrayList<? extends Parcelable>) lstQuestionsAsked);
			}
			showDialog(Constants.DIA_MUL + intPreviousGroupId * 1000, bundle);
		} else {
			ArrayList<Question> lstQuestionsAsked = new ArrayList<Question>();
			lstQuestionsAsked.add(objQuestion);
			bundle.putParcelableArrayList(Constants.KEY_QUESTIONS_ASKED,
					(ArrayList<? extends Parcelable>) lstQuestionsAsked);
			showDialog(Constants.DIA_MUL + objQuestion.getIntOrderNum(), bundle);
		}
	}

	public void _finishObliguedQuestions(final int keyCode) {
		switch (keyCode) {
		case Constants.ACTIVITY_ACTION_NEXT:

			if (intActivityState == Constants.DET_ESTADO_NO_EDITABLE) {
				controller._saveInfo(Constants.DET_ESTADO_NO_EDITABLE);
				finish();

			} else {
				boolean booInWorkingHours = controller
						._validateInWorkingHours(objRegister);
				if (booInWorkingHours) {
					controller._saveInfo(Constants.DET_ESTADO_POSFLUJO);
					finish();
				} else {
					boolean booDangerousZone = objRegister
							.isBooFlgDangerousZone();
					if (!booDangerousZone && !booDone) {
						objRegister.setLngEndDateReal(-1);
						txtEndTime.setText(Constants.LBL_EMPTY);
					}
					intActivityState = Constants.DET_ESTADO_POSFLUJO;
				}
			}

			break;
//		case Constants.ACTIVITY_ACTION_PREVIOUS:
		case KeyEvent.KEYCODE_ALT_RIGHT:
		case KeyEvent.KEYCODE_ALT_LEFT:
			boolean booDangerousZone = objRegister.isBooFlgDangerousZone();
			if (!booDangerousZone && !booDone) {
				objRegister.setLngEndDateReal(-1);
				txtEndTime.setText(Constants.LBL_EMPTY);
			}
			intActivityState = Constants.DET_ESTADO_POSFLUJO;
			break;
		}

	}

	public boolean isBooAnswerReading() {
		return booAnswerReading;
	}

	public void setBooAnswerReading(boolean booAnswerReading) {
		this.booAnswerReading = booAnswerReading;
	}

	public List<Option> getLstOption() {
		return lstOption;
	}

	public Button getBtnDangerousZone() {
		return btnDangerousZone;
	}

	public Listing getObjListing() {
		return objListing;
	}

	public Register getObjRegister() {
		return objRegister;
	}

	public Flux getObjFlux() {
		return objFlux;
	}

	public DetailController getController() {
		return controller;
	}

	public TextView getTxtStartTime() {
		return txtStartTime;
	}

	public TextView getTxtEndTime() {
		return txtEndTime;
	}

	public List<Question> getLstQuestion() {
		return lstQuestion;
	}

	public int getIntCurrentQuestionIndex() {
		return intCurrentQuestionIndex;
	}

	public int getIntTempIndex() {
		return intTempIndex;
	}

	public int getIntActivityState() {
		return intActivityState;
	}

	public int getIntReadingStep() {
		return intReadingStep;
	}

	public void setIntReadingStep(int intReadingStep) {
		this.intReadingStep = intReadingStep;
	}

	public String strTemporalImageName;
	public String strPermanentImageName;
}