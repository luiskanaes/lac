package pe.beyond.lac.gestionmovil.utils;

import org.apache.commons.lang.ArrayUtils;

import pe.beyond.lac.gestionmovil.core.AppGMD;
import pe.beyond.lac.gestionmovil.daos.DataTypeDAO;
import pe.beyond.lac.gestionmovil.daos.FluxDAO;
import pe.beyond.lac.gestionmovil.daos.FluxValidationDAO;
import pe.beyond.lac.gestionmovil.daos.FormDAO;
import pe.beyond.lac.gestionmovil.daos.ListingDAO;
import pe.beyond.lac.gestionmovil.daos.LogDAO;
import pe.beyond.lac.gestionmovil.daos.ModuleDAO;
import pe.beyond.lac.gestionmovil.daos.ObsCodeDAO;
import pe.beyond.lac.gestionmovil.daos.OptionDAO;
import pe.beyond.lac.gestionmovil.daos.OptionModResultDAO;
import pe.beyond.lac.gestionmovil.daos.OptionResultDAO;
import pe.beyond.lac.gestionmovil.daos.PhotoConfigurationDAO;
import pe.beyond.lac.gestionmovil.daos.PhotoDAO;
import pe.beyond.lac.gestionmovil.daos.QuestionDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterAnswerDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.StateDAO;
import pe.beyond.lac.gestionmovil.daos.TableDAO;
import pe.beyond.lac.gestionmovil.daos.TemplateDAO;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import pe.beyond.lac.gestionmovil.daos.UserAssignedDAO;
import pe.beyond.lac.gestionmovil.daos.UserValidationDAO;
import pe.beyond.lac.gestionmovil.daos.ValidationDAO;
import pe.beyond.lac.gestionmovil.daos.ValidationResultDAO;
import pe.beyond.lac.gestionmovil.daos.ValidationTypeDAO;
import android.view.KeyEvent;

public final class Constants {
	// Tiempo que demora el splash en desaparecer (en milisegundos)
	public static final int TIME_SPLASH_DISPLAY_LENGTH = 2000;

	// Tiempo l�mite para la validaci�n del acceso a la aplicacion (en
	// milisgundos)
	public static final long TIME_OUT_APP = 30000;

	public static final String ADMIN_USERNAME = "4530";
	public static final String ADMIN_PASSWORD = "4530";
	public static final String DEFAULT = " DEFAULT ";
	public static final String TEXT = " TEXT ";
	public static final String INTEGER = " INTEGER ";
	public static final String BOOLEAN = " BOOLEAN ";
	public static final String REAL = " REAL ";
	public static final String LONG = " LONG ";
	public static final String NULL = " NULL ";
	public static final String NOT_NULL = " NOT NULL ";
	public static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT ";
	public static final String LBL_DOT = ".";
	public static final String LBL_COMMA_SEPARATOR = ",";

	public static final String LBL_COMMA = " , ";
	public static final String LBL_SLASH = "/";
	public static final String LBL_SPACE = " ";
	public static final String LBL_EMPTY = "";
	public static final String LBL_SEPARATOR = " - ";
	public static final String LBL_TEXTO_SIN_CONTENIDO = " ";
	public static final String LIST_PHOTO = "list_photos";
	public static final String BL_MIN_ONE_IMAGE = "min_one_image";
	public static final String MSG_CANT_DELETE_IMAGE = "No se pueden borrar las im�genes ya guardadas.";
	public static final String MSG_NO_NEW_IMAGE = "Debe haber una imagen nueva como m�nimo.";
	public static final String MSG_ERROR_TRYING_DELETE_IMAGE = "Error al tratar de suprimir la imagen.";
	
	public static final String MSG_ERROR_MAP_NOT_FOUND = "I001 - UBICACION NO REGISTRADA";
	
	public static final String CUSTOM_ERROR_MESSAGE = "CustomErrorMessage";
	public static final String COLUMN_NAME_HOWMUCH = "TXT_CANTIDAD";
	public static final String COLUMN_NAME_STATE = "TXT_TIPO_ID";
	public static final String COLUMN_NAME_MODULE_COD = "TXT_MODULE_COD";
	public static final String COLUMN_NAME_MODULE_DESC = "TXT_MODULE_DESC";
	public static final String COLUMN_NAME_COD_LISTADO = "TXT_COD_LISTADO";

	public static final String PARAM_PROGRESS = "progress";

	public static final int PERFIL_OPERARIO = 0;
	public static final int PERFIL_SUPERVISOR = 1;
	public static final int PERFIL_ADMINISTRADOR = 2;

	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_DETAIL = 100;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_MANAGER = 101;

	public static final int INT_SAMPLE_SIZE = 64;
	public static final int INT_MIN_CHAR_LOGIN = 3;

	public static final int FLG_ENABLED = 1;
	public static final int FLG_DISABLED = 0;
	public static final int FLG_SELECTED = 2;
	public static final int FLG_DESC = 0;
	public static final int FLG_ASC = 1;

	public static final int NUM_UPLOAD_SET_LIMIT = 5;

	public static final int ASYNCTASK_LOGIN = 10;

	public static final int DIALOG_LOADING = 12;
	public static final int DIALOG_PROFILE = 13;

	public static final int DIALOG_MULTIPLE_INPUT = 50;

	public static final int DIA_STEP = 1000;

	public static final int DIA_CON = 1000;
	public static final int DIA_CON_SYNC = 1001;
	public static final int DIA_CON_BACK = 1002;
	public static final int DIA_CON_LOGOUT = 1003;
	public static final int DIA_CON_UNRESTRICTIVE = 1004;

	public static final int DIA_MSG = 2000;
	public static final int DIA_MSG_ERR_PAYLOAD_UNSYNCED = 2001;
	public static final int DIA_MSG_ERR_LOGIN = 2002;
	public static final int DIA_MSG_ERR_NETWORK = 2003;
	public static final int DIA_MSG_ERR_CONFIG_DOWNLOAD = 2004;
	public static final int DIA_MSG_ERR_CONFIG_PARSE = 2005;
	public static final int DIA_MSG_ERR_INFO_DOWNLOAD = 2006;
	public static final int DIA_MSG_ERR_INFO_PARSE = 2007;
	public static final int DIA_MSG_ERR_SEARCH_NOT_FOUND = 2008;
	public static final int DIA_MSG_ERR_OPTION_UNDEFINED = 2009;
	public static final int DIA_MSG_ERR_QR_SEARCH_NOT_FOUND = 2010;
	public static final int DIA_MSG_ERR_TIME_FORMAT = 2011;
	public static final int DIA_MSG_ERR_TIME_INCONGRUENCE = 2012;
	public static final int DIA_MSG_ERR_TIME_OUT_FLUX = 2013;
	public static final int DIA_MSG_ERR_TIME_CONFLICTS = 2014;
	public static final int DIA_MSG_ERR_READING_EMPTY = 2015;
	public static final int DIA_MSG_ERR_CUSTOM_VALUE = 2016;
	public static final int DIA_MSG_ERR_INPUT_EMPTY = 2017;
	public static final int DIA_MSG_ERR_INPUT_NOT_MIN = 2018;
	public static final int DIA_MSG_PROCESS_STARTED = 2019;
	public static final int DIA_MSG_USER_WITHOUT_MODULES = 2020;
	public static final int DIA_MSG_ERR_VALIDATION_NOT_PASSED = 2021;
	public static final int DIA_MSG_ERR_CUSTOM = 2022;
	public static final int DIA_MSG_ERR_PHOTO_MANAGER_MESSAGE = 2023;
	public static final int DIA_MSG_GPS_DISABLED = 2024;
	public static final int DIA_MSG_ERR_PHOTO_SENT = 2025;
	public static final int DIA_MSG_ERR_LOGIN_MORE_CHARS = 2026;
	//
//	public static final int DIA_MSG_INFO_MAP_UNAVAIBLE = 2027;
	

	public static final int DIA_CUS = 3000;
	public static final int DIA_CUS_SELECT_PROFILE = 3001;
	public static final int DIA_CUS_LIST_CONFIGURATION = 3002;
	public static final int DIA_CUS_CONFIGURATION_SEARCH = 3003;
	public static final int DIA_CUS_CONFIGURATION_ORDER = 3004;
	public static final int DIA_CUS_CONFIGURATION_FILTER = 3005;
	public static final int DIA_CUS_READING_INPUT = 3006;
	public static final int DIA_CUS_DETAIL_END_ANSWER = 3007;
	public static final int DIA_CUS_DANGEROUS_ZONE_TIME = 3008;
	public static final int DIA_CUS_HELP_OPTIONS = 3009;
	public static final int DIA_CUS_SERVICE_URL = 3010;

	public static final int DIA_MUL = 4000;

	public static final int NOTIFICATION_CONTENT = 0;
	public static final int NOTIFICATION_PHOTOS = 1;
	public static final int NOTIFICATION_HIDE = 0;
	public static final int NOTIFICATION_DISPLAY = 1;

	public static final String OVER_DIALOG_WITHOUT_REPLACE = "OVER_DIALOG_WITHOUT_REPLACE";
	public static final String OVER_DIALOG_ERROR_CUSTOM = "ERROR_CUSTOM";

	public static final int ACTIVITY_ACTION_NEXT = KeyEvent.KEYCODE_SPACE;
//	public static final int ACTIVITY_ACTION_PREVIOUS = KeyEvent.KEYCODE_ALT_RIGHT;
	
	private static final int _ACTIVITY_ACTION_PREVIOUS_CALC = (android.os.Build.MANUFACTURER.contains("samsung") && android.os.Build.MODEL.contains("GT-B5510L"))? KeyEvent.KEYCODE_ALT_RIGHT : KeyEvent.KEYCODE_ALT_LEFT;
	public static final int ACTIVITY_ACTION_PREVIOUS =_ACTIVITY_ACTION_PREVIOUS_CALC; 
	public static final int DIALOG_ACTION_NEXT = KeyEvent.KEYCODE_ENTER;
//	public static final int DIALOG_ACTION_PREVIOUS = KeyEvent.KEYCODE_ALT_RIGHT;
	public static final int DIALOG_ACTION_PREVIOUS = (android.os.Build.MANUFACTURER.contains("samsung") && android.os.Build.MODEL.contains("GT-B5510L"))? KeyEvent.KEYCODE_ALT_RIGHT : KeyEvent.KEYCODE_ALT_LEFT;;

	public static final int WIDTH_LABEL = 150;
	public static final int WIDTH_INPUT_VIEW = 200;

	public static final int LIST_PENDIENTE = 0;
	public static final int LIST_ENPROCESO = 1;
	public static final int LIST_EJECUTADO = 2;
	public static final int LIST_FINALIZADO = 3;
	public static final int LIST_TODOS = 4;

//	 public static final String FTP_DEFAULT_USERNAME = "LAC\\FTP_BEYOND";
//	 public static final String FTP_DEFAULT_PASSWORD = "%Movil$BYND@";
//	 public static final String FTP_DEFAULT_URL = "10.200.71.10";
	
	public static final String FTP_DEFAULT_URL = "10.200.71.10";
	public static final String FTP_DEFAULT_USERNAME = "LAC\\FTP_MOVIL";
	public static final String FTP_DEFAULT_PASSWORD = "%LAC@g3stion$";
	
//	public static final String FTP_DEFAULT_URL = "10.200.71.10";
//	public static final String FTP_DEFAULT_USERNAME = "LAC\\FTP_MOVIL";
//	public static final String FTP_DEFAULT_PASSWORD = "%LAC@g3stion$";
//	
	
	/*
	 * EDELNOR...
	 */
//	public static final String FTP_DEFAULT_URL = "172.25.15.74";
//	public static final String FTP_DEFAULT_USERNAME = "DESARROLLO\\administrator";
//	public static final String FTP_DEFAULT_PASSWORD = "des@rrollo2012";
//	public static final int FTP_DEFAULT_PORT = 4000;
	
	public static final int FTP_DEFAULT_PORT = 21;

	public static final String ACTIVITIES_PKG_NAME = "pe.beyond.lac.gestionmovil.activities.";
	public static final String SELECTED_ID = "selected_id";
	public static final String OBTAINED_BY_QR = "booObtainedByQR";
	public static final String KEY_REQUEST_FOCUS = "request_focus";

	public static final int PREG_OPCIONAL_TEMPORAL = 0;
	public static final int PREG_OBLIGATORIA = 1;
	public static final int PREG_INVALIDA = 2;
	public static final int PREG_OPCIONAL_PERMANENTE = 3;

	public static final int RESP_ACCION_IMPLICITA = 1;
	public static final int RESP_ACCION_MULTIPLE_INPUT = 2;
	public static final int RESP_ACCION_TOMA_FOTO = 3;
	public static final int RESP_ACCION_TOMA_LECTURA = 4;

	public static final int URI_NO_URI = -1;
	public static final int URI_HEADER_DIGITS = 2;

	public static final int DET_ESTADO_PREFLUJO_PENDIENTE = 0;
	public static final int DET_ESTADO_PREFLUJO_ENPROCESO = 1;
	public static final int DET_ESTADO_ENFLUJO = 2;
	public static final int DET_ESTADO_POSFLUJO = 3;
	public static final int DET_ESTADO_NO_EDITABLE = 4;

	public static final int DELIVERY_STATE_WAITING = 0;
	public static final int DELIVERY_STATE_SENT = 1;
	public static final int DELIVERY_STATE_NOT_CONSIDERED = 2;

	public static final String QR_CODE_VALUE = "QR_CODE_VALUE";
	public static final int QR_RESULT_CODE = 100;
	public static final int OPCION_SEARCH_RESULT_CODE = 101;

	public static final String DB_NAME_CONFIGURATION = "gls_configuration.db";
	public static final int DB_VERSION_CONFIGURATION = 1;
	public final static String KEY_START_TIME = "DANGEROUS_ZONE_START_TIME";
	public final static String KEY_END_TIME = "DANGEROUS_ZONE_END_TIME";
	public final static String KEY_QUESTIONS_ASKED = "QUESTIONS_ASKED";
	public final static String KEY_POSSIBLE_OPTIONS = "POSSIBLE_OPTIONS";
	public final static String KEY_READING_STEP = "READING_STEP";
	public final static String KEY_HEADER = "HEADER";

	public static final String CONFIGURATION_PROVIDER_AUTHORITY = "pe.beyond.lac.gestionmovil.providers.configurationprovider";
	public static final String PHOTO_PROVIDER_AUTHORITY = "pe.beyond.lac.gestionmovil.providers.photoprovider";

	public static final String DAO_PACKAGE = "pe.beyond.lac.gestionmovil.daos.";

	public static final String[] DB_LOGIN_DAO_NAMES = {
			DAO_PACKAGE + UserAssignedDAO.DAO_NAME,
			DAO_PACKAGE + ModuleDAO.DAO_NAME };

	public static final String[] DB_CONFIGURATION_DAO_NAMES = {
			DAO_PACKAGE + OptionDAO.DAO_NAME,
			DAO_PACKAGE + OptionResultDAO.DAO_NAME,
			DAO_PACKAGE + QuestionDAO.DAO_NAME,
			DAO_PACKAGE + TemplateDAO.DAO_NAME,
			DAO_PACKAGE + ValidationDAO.DAO_NAME,
			DAO_PACKAGE + ValidationResultDAO.DAO_NAME,
			DAO_PACKAGE + ValidationTypeDAO.DAO_NAME,
			DAO_PACKAGE + FluxDAO.DAO_NAME, DAO_PACKAGE + FormDAO.DAO_NAME,
			DAO_PACKAGE + PhotoConfigurationDAO.DAO_NAME,
			DAO_PACKAGE + DataTypeDAO.DAO_NAME,
			DAO_PACKAGE + ObsCodeDAO.DAO_NAME, DAO_PACKAGE + TableDAO.DAO_NAME,
			DAO_PACKAGE + StateDAO.DAO_NAME,
			DAO_PACKAGE + OptionModResultDAO.DAO_NAME,
			DAO_PACKAGE + FluxValidationDAO.DAO_NAME,
			DAO_PACKAGE + UserValidationDAO.DAO_NAME,
			DAO_PACKAGE + TrackDAO.DAO_NAME };

	public static final String[] DB_INFORMATION_DAO_NAMES = {
			DAO_PACKAGE + ListingDAO.DAO_NAME,
			DAO_PACKAGE + RegisterAnswerDAO.DAO_NAME,
			DAO_PACKAGE + RegisterDAO.DAO_NAME,
			DAO_PACKAGE + PhotoDAO.DAO_NAME, DAO_PACKAGE + LogDAO.DAO_NAME };

	public static final String[] DB_ALL_DAO_NAMES = (String[]) ArrayUtils
			.addAll(ArrayUtils.addAll(DB_LOGIN_DAO_NAMES,
					DB_CONFIGURATION_DAO_NAMES), DB_INFORMATION_DAO_NAMES);

	public static final String LOCATION_KEY_INDIVIDUAL = "MAP_INDIVIDUAL";
	public static final String LOCATION_KEY_LISTINGID = "MAP_LISTINGID";

	public static final int LISTING_STATE_PENDING = 0;
	public static final int LISTING_STATE_ONPROGRESS = 1;
	public static final int LISTING_STATE_EXECUTED = 2;
	public static final int LISTING_STATE_FINALIZED = 3;
	public static final int LISTING_STATE_CANCELED = 4;

	public static final int REGISTER_STATE_EXECUTED = 0;
	public static final int REGISTER_STATE_FINALIZED = 1;
	public static final int REGISTER_STATE_INVALIDATED = 2;
	public static final int REGISTER_STATE_INPROGRESS = 3;

	public static final int VALIDACION_USUARIO_MEDIDORES = 7;

	public static final int ID_DIALOG_ROOT = 1000;

	public static final int KEY_DAN_ZONE = KeyEvent.KEYCODE_Z;
	public static final int KEY_MAP = KeyEvent.KEYCODE_M;
	public static final int KEY_NEXT = KeyEvent.KEYCODE_ENTER;

	public static final int STATE_UNANSWERED = 0;
	public static final int STATE_ANSWERED = 1;
	public static final int STATE_INVALIDATED = 2;
	public static final int STATE_VALIDATED = 3;
	public static final int STATE_RESTRICTIVE = 4;
	public static final int STATE_UNRESTRICTIVE = 5;

	public static final int VIBRATE_TIME = 50;
	public static final int TIME_CONNECTION_TIMEOUT = 15000;
	public static final int DEFAULT_SOCKET_TIMEOUT = TIME_CONNECTION_TIMEOUT;
	public static final int MULT = 40;
	public static final int TIME_EXECUTE_UPLOAD = 10000;

	public static final String URL_DETAULT_BASE = "http://118.247.25.201:8080/WS_LAC_PRE/";
//	public static final String URL_DETAULT_BASE = "http://10.200.71.10:9080/WS_LAC_PRD/";	
//	public static final String URL_DETAULT_BASE = "http://10.200.71.10:9080/WS_LAC_PRE/";	
//	public static final String URL_DETAULT_BASE = "http://10.200.71.11:9080/WS_LAC_DESA/";
	//public static final String URL_DETAULT_BASE = "http://200.62.152.55:9080/WS_LAC_PRD/";

  //  public static final String URL_DETAULT_BASE = "http://200.62.152.55:9280/WS_LAC_PRE/";

	//public static final String URL_DETAULT_BASE = "http://192.168.220.161:8080/WS_LAC_PRE/";
//	public static final String URL_DETAULT_BASE = "http://172.24.1.118:9080/WS_LAC_DESA/"; 
//	public static final String URL_DETAULT_BASE = "http://172.24.1.117:9080/WS_LAC_DESA/";
//	public static final String URL_DETAULT_BASE = "http://172.10.14.9:9080/WS_LAC_PRD/";
//	public static final String URL_DETAULT_BASE = "http://172.25.15.74:9080/WS_LAC_DESA/";
	
	public static final String URL_LOGIN = "login.action";
	public static final String URL_LOGOUT = "logout.action";
	public static final String URL_CONFIGURATION = "getConfiguration.action";
	public static final String URL_INFORMATION = "getList.action?";
	public static final String URL_TRANSFER = "registerData.action";
	public static final String URL_UPDATE = "updateData.action";
	public static final String URL_PHOTO = "registerPhoto.action";
	public static final String URL_ERROR = "registerError.action";
	public static final String URL_TRACKING = "registerTracking.action";
	

	public static final String DATE_PATTERN_FULL = "dd/WW/yyyy HH:KK:ss";
	public static final String DATE_PATTERN_DANGEROUS_ZONE = "HH:mm";
	public static final String DATE_PATTERN_VALIDATION = "HH:mm:ss";

	public static final String DIALOG_ERROR_TITLE = "dialog_error_title";
	public static final String DIALOG_ERROR_MESSAGE = "dialog_error_message";

	public static final int VAL_MIXMAX = 0;
	public static final int VAL_INT_EQUAL = 1;
	public static final int VAL_STR_EQUAL = 2;

}
