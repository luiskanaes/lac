package pe.beyond.lac.gestionmovil.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.beyond.gls.model.DataType;
import pe.beyond.gls.model.Flux;
import pe.beyond.gls.model.Listing;
import pe.beyond.gls.model.Option;
import pe.beyond.gls.model.Question;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.activities.BaseActivity;
import pe.beyond.lac.gestionmovil.activities.DetailActivity;
import pe.beyond.lac.gestionmovil.database.ConfigurationDBHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Util {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String _createSTRDangerousZoneFormatTime(
			final long lngCurrentTime) {
		String result = null;

		SimpleDateFormat objDateFormatter = new SimpleDateFormat(
				Constants.DATE_PATTERN_DANGEROUS_ZONE);

		Date datCurrentDate = Calendar.getInstance().getTime();
		datCurrentDate.setTime(lngCurrentTime);

		result = objDateFormatter.format(datCurrentDate);

		return result;
	}

	public static String _createSTRFormatTime(final long lngCurrentTime) {
		String result = null;

		SimpleDateFormat objDateFormatter = new SimpleDateFormat(
				Constants.DATE_PATTERN_VALIDATION);

		Date datCurrentDate = Calendar.getInstance().getTime();
		datCurrentDate.setTime(lngCurrentTime);

		result = objDateFormatter.format(datCurrentDate);

		return result;
	}

	public static long _createLNGCurrentDayStart() {
		long result = -1;

		Date datCurrentDate = Calendar.getInstance().getTime();
		datCurrentDate.setHours(0);
		datCurrentDate.setMinutes(0);
		datCurrentDate.setSeconds(0);
		datCurrentDate.setTime(0);

		result = datCurrentDate.getTime();
		return result;
	}

	public static String _obtainDateToDisplay(final long lngRegisterTime,
			final long lngRealTime) {
		String strTime = Constants.LBL_EMPTY;
		if (lngRegisterTime != -1) {
			strTime = _createSTRDangerousZoneFormatTime(lngRegisterTime);
		} else if (lngRealTime != -1) {
			strTime = _createSTRDangerousZoneFormatTime(lngRealTime);
		} else {
			strTime = _createSTRDangerousZoneFormatTime(System
					.currentTimeMillis());
		}
		return strTime;
	}

	public static String _obtainDateToDisplay(final long lngRealTime) {
		String strTime = Constants.LBL_EMPTY;
		if (lngRealTime != -1) {
			strTime = Util._createSTRDangerousZoneFormatTime(lngRealTime);
		}
		return strTime;
	}

	public static long _obtainDateFromString(Date datTime, final String strTime) {
		long lngTime = -1;

		int intHours = Integer.parseInt(strTime.substring(0, 2));
		int intMinutes = Integer.parseInt(strTime.substring(3, 5));
		int intSeconds = Integer.parseInt(strTime.substring(6, 8));

		datTime.setHours(intHours);
		datTime.setMinutes(intMinutes);
		datTime.setSeconds(intSeconds);

		lngTime = datTime.getTime();

		return lngTime;
	}

	public static int _obtainCurrentFilter(int intCurrentFilter) {
		int intFilterId = -1;
		switch (intCurrentFilter) {
		case Constants.LIST_PENDIENTE:
			intFilterId = R.string.label_simple_filter_pending;
			break;
		case Constants.LIST_ENPROCESO:
			intFilterId = R.string.label_simple_filter_in_progress;
			break;
		case Constants.LIST_EJECUTADO:
			intFilterId = R.string.label_simple_filter_executed;
			break;
		case Constants.LIST_FINALIZADO:
			intFilterId = R.string.label_simple_filter_finalized;
			break;
		case Constants.LIST_TODOS:
			intFilterId = R.string.label_simple_filter_all;
			break;
		}
		return intFilterId;
	}

	public static void _resetTables(BaseActivity activity,
			String[] strTableNames) {
		ConfigurationDBHelper objHelper = new ConfigurationDBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = objHelper.getWritableDatabase();
		} catch (Exception e) {
			activity._reportError(e);
		}
		if (db != null) {
			Class<?> params[] = { SQLiteDatabase.class };
			Object paramsObj[] = { db };

			for (String strDaoName : strTableNames) {
				try {
					Class<?> clsDao = Class.forName(strDaoName);
					Object objDao = clsDao.newInstance();
					Method mtdToDo = clsDao.getDeclaredMethod("onUpgrade",
							params);
					mtdToDo.invoke(objDao, paramsObj);
				} catch (Exception e) {
					activity._reportError(e);
				}
			}
		}
	}

	/**
	 * Crear un componente para mostrar la informacion del registro
	 * 
	 * @param strFormName
	 *            - etiqueta de la informacion a mostrar
	 * @param strListingInfo
	 *            - contenido de la informacion a mostrar
	 * @return SpannableStringBuilder con el texto contruido y enriquecido.
	 */
	public static SpannableStringBuilder _createTextInfo(
			final String strFormName, String strListingInfo, int intColorFirst,
			int intColorSecond, float fltSizeFirst, float fltSizeSecond) {
		if (strListingInfo == Constants.NULL
				|| strListingInfo.equals(Constants.LBL_EMPTY)) {
			strListingInfo = Constants.LBL_TEXTO_SIN_CONTENIDO;
		}

		SpannableStringBuilder result = new SpannableStringBuilder();
		result.append(strFormName.toUpperCase()).append(strListingInfo);

		result.setSpan(new RelativeSizeSpan(fltSizeFirst), 0,
				strFormName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		result.setSpan(new ForegroundColorSpan(intColorFirst), 0,
				strFormName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		result.setSpan(new RelativeSizeSpan(fltSizeSecond),
				strFormName.length(),
				strFormName.length() + strListingInfo.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		result.setSpan(new ForegroundColorSpan(intColorSecond),
				strFormName.length(),
				strFormName.length() + strListingInfo.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return result;
	}

	public static LinearLayout _createInfo(final BaseActivity activity,
			final Question objQuestion, final int intEditTextId) {
		// se crea el layout que contiene toda lo asociado a la pregunta (para
		// manejar la visibilidad UI según la pregunta)
		LinearLayout linRoot = null;
		LinearLayout.LayoutParams paramsResult = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		linRoot = new LinearLayout(activity);
		linRoot.setLayoutParams(paramsResult);
		linRoot.setOrientation(LinearLayout.HORIZONTAL);

		int intLinearHash = "Linear".hashCode()
				+ objQuestion.getIntQuestionId();
		linRoot.setTag(intLinearHash);

		if (objQuestion.getIntState() != Constants.PREG_INVALIDA) {
			linRoot.setVisibility(View.VISIBLE);
		} else {
			linRoot.setVisibility(View.GONE);
		}

		// se crea la etiqueta de la pregunta
		int intLabelWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, Constants.WIDTH_LABEL, activity
						.getResources().getDisplayMetrics());
		LinearLayout.LayoutParams paramsLabel = new LinearLayout.LayoutParams(
				intLabelWidth, LayoutParams.WRAP_CONTENT);
		paramsLabel.setMargins(5, 5, 5, 5);
		paramsLabel.gravity = Gravity.CENTER_VERTICAL;

		TextView txtLabel = new TextView(activity);
		txtLabel.setLayoutParams(paramsLabel);
		txtLabel.setTextColor(Color.WHITE);
		txtLabel.setTextSize(20);
		txtLabel.setText(objQuestion.getStrQuestionDescription());
		txtLabel.setVisibility(View.VISIBLE);
		linRoot.addView(txtLabel);

		// se crea el view en el que se ingresa contenido
		int intEditWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, Constants.WIDTH_INPUT_VIEW,
				activity.getResources().getDisplayMetrics());

		LinearLayout.LayoutParams paramsEdit = new LinearLayout.LayoutParams(
				intEditWidth, LayoutParams.WRAP_CONTENT);
		paramsEdit.setMargins(2, 2, 2, 2);
		paramsEdit.gravity = Gravity.CENTER_VERTICAL;

		EditText ediValue = new EditText(activity);
		ediValue.setLayoutParams(paramsEdit);
		ediValue.setVisibility(View.VISIBLE);
		ediValue.setTextSize(20);
		ediValue.setMaxWidth(intEditWidth);
		ediValue.setId(intEditTextId);

		DataType objDataType = ((DetailActivity) activity).getController()
				._obtainDataTypeByDataTypeId(objQuestion.getIntDataTypeId());
		if (objDataType.getStrDescription().equals("Int")) {
			ediValue.setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (objDataType.getStrDescription().equals("String")
				|| objDataType.getStrData().equals("NO PRECISA")) {
			ediValue.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		}

		int intMaxChars = objDataType.getIntLongitudeMax();
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(intMaxChars);
		ediValue.setFilters(FilterArray);
		ediValue.setOnKeyListener((DetailActivity) activity);

		linRoot.addView(ediValue);

		return linRoot;
	}

	public static int _correctTimeFormat(final BaseActivity activity,
			final String strStartTime, final String strEndTime) {
		int intStartHour = Integer.parseInt(strStartTime.substring(0, 2));
		int intStartMinute = Integer.parseInt(strStartTime.substring(3, 5));

		int intEndHour = Integer.parseInt(strEndTime.substring(0, 2));
		int intEndMinute = Integer.parseInt(strEndTime.substring(3, 5));

		if (!((intStartHour >= 0 && intStartHour < 24)
				&& (intEndHour >= 0 && intEndHour < 24)
				&& (intStartMinute >= 0 && intStartMinute < 60) && (intEndMinute >= 0 && intEndMinute < 60))) {
			return Constants.DIA_MSG_ERR_TIME_FORMAT;
		}
		Date datTime = Calendar.getInstance().getTime();

		long lngStartTime = Util._obtainDateFromString(datTime, strStartTime);
		long lngEndTime = Util._obtainDateFromString(datTime, strEndTime);

		int intDifference = (int) (lngEndTime - lngStartTime);
		if (intDifference < 0) {
			return Constants.DIA_MSG_ERR_TIME_INCONGRUENCE;
		}

		Listing objListing = ((DetailActivity) activity).getObjListing();
		int intFluxId = objListing.getIntFluxId();
		Flux objFlux = ((DetailActivity) activity).getController()
				._obtainFluxByPK(intFluxId);

		long lngStartTimeFlux = objFlux.getLngStartHour();
		long lngEndTimeFlux = objFlux.getLngEndHour();

		Date datStartTimeFlux = Calendar.getInstance().getTime();
		datStartTimeFlux.setTime(lngStartTimeFlux);
		int intStartHourFlux = datStartTimeFlux.getHours();
		int intStartMinuteFlux = datStartTimeFlux.getMinutes();

		Date datEndTimeFlux = Calendar.getInstance().getTime();
		datEndTimeFlux.setTime(lngEndTimeFlux);
		int intEndHourFlux = datEndTimeFlux.getHours();
		int intEndMinuteFlux = datEndTimeFlux.getMinutes();

		if (intStartHour < intStartHourFlux || intStartHour > intEndHourFlux
				|| intStartHour == intStartHourFlux
				&& intStartMinute < intStartMinuteFlux
				|| intStartHour == intEndHourFlux
				&& intStartMinute > intEndMinuteFlux) {
			return Constants.DIA_MSG_ERR_TIME_OUT_FLUX;
		}

		int intUserId = objListing.getIntUserId();
		boolean booIsInConflict = ((DetailActivity) activity).getController()
				._isInConflictsWithOtherRegisters(intUserId, lngStartTime,
						lngEndTime);
		if (booIsInConflict) {
			return Constants.DIA_MSG_ERR_TIME_CONFLICTS;
		}
		return -1;
	}

	public static LinearLayout _createHelpLayout(final Dialog dialog,
			final Activity activity, final ArrayList<Option> lstOption) {
		int intHelpHashCode = "HelpOptions".hashCode();

		LinearLayout linRoot = (LinearLayout) dialog
				.findViewById(intHelpHashCode);

		linRoot.removeAllViews();

		if (lstOption != null && lstOption.size() > 0) {
			for (int intIte = 0; intIte < lstOption.size(); intIte++) {
				Option objOption = lstOption.get(intIte);

				String strAbreviation = Constants.LBL_EMPTY;
				if (objOption.getStrOptionAbreviation() != null) {
					strAbreviation = " [ "
							+ objOption.getStrOptionAbreviation() + " ] : ";
				}
				String strReal = objOption.getStrDescription();

				TextView txtInfo = new TextView(activity);
				txtInfo.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				txtInfo.setVisibility(View.VISIBLE);

				int intColorLabel = Color.rgb(228, 108, 10);
				int intColorInfo = Color.WHITE;

				float fltSizeLabel = 1.5f;
				float fltSizeInfo = 1.5f;

				txtInfo.setText(Util._createTextInfo(strAbreviation, strReal,
						intColorLabel, intColorInfo, fltSizeLabel, fltSizeInfo));

				linRoot.addView(txtInfo);
			}
		}
		return linRoot;
	}

	public static String _getKeyString(int intKey) {
		String strKey = Constants.LBL_EMPTY;
		if (intKey >= KeyEvent.KEYCODE_A && intKey <= KeyEvent.KEYCODE_Z) {
			char chrKey = (char) (36 + intKey);
			strKey = String.valueOf(chrKey);

		} else {
			switch (intKey) {
			case KeyEvent.KEYCODE_ENTER:
				strKey = "ENTER";
				break;
			case KeyEvent.KEYCODE_ALT_RIGHT: // <<<<------ ADD DE ALT IZQ Y DER
			case KeyEvent.KEYCODE_ALT_LEFT:
				strKey = "ALT";
				break;
			case KeyEvent.KEYCODE_SPACE:
				strKey = "ESPACIO";
			}
		}
		return strKey;
	}

	public static String _getStateName(final Context context, final int intState) {
		String strStateName = Constants.LBL_EMPTY;
		switch (intState) {
		case Constants.LIST_TODOS:
			strStateName = context.getString(R.string.label_filter_all);
			break;
		case Constants.LIST_PENDIENTE:
			strStateName = context.getString(R.string.label_filter_pending);
			break;
		case Constants.LIST_ENPROCESO:
			strStateName = context.getString(R.string.label_filter_in_progress);
			break;
		case Constants.LIST_EJECUTADO:
			strStateName = context.getString(R.string.label_filter_executed);
			break;
		case Constants.LIST_FINALIZADO:
			strStateName = context.getString(R.string.label_filter_finalized);
			break;
		}
		return strStateName;
	}

	// Validar la existencia de archivos

	public static boolean filesExists(String path) {
		List<String> strPaths = new ArrayList<String>();
		File directory = new File(path);
		File[] files = directory.listFiles();

		if (files.length > 0) {
			return true;
		}

		return false;
	}

	public static ArrayList<String> getPhotoList(String strPath) {
		ArrayList<String> pathsList = new ArrayList<String>();
		File directory = new File(strPath);
		File[] files = directory.listFiles();

		for (int i = 0; i < files.length; ++i) {
			pathsList.add(files[i].getName());

		}

		return pathsList;
	}

	public static Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width * 3,
				v.getLayoutParams().height * 3, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getLayoutParams().width * 3,
				v.getLayoutParams().height * 3);
		v.draw(c);
		// Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter)
		return b;
	}

	public static Bitmap shrinkBitmap(String filename, int width, int height) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filename, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filename, bmpFactoryOptions);
		return bitmap;
	}

	public static String _obtainVersionName(final Context ctxContext) {
		String versionName = null;
		StringBuilder sbrVersion = new StringBuilder();
		try {

			sbrVersion = new StringBuilder();
			sbrVersion.append("ver");
			sbrVersion.append(Constants.LBL_DOT);
			sbrVersion.append(Constants.LBL_SPACE);
			versionName = ctxContext.getPackageManager().getPackageInfo(
					ctxContext.getPackageName(), 0).versionName;
			sbrVersion.append(versionName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return sbrVersion.toString();
	}
}
