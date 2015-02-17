package pe.beyond.lac.gestionmovil.controllers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.beyond.gls.model.Photo;
import pe.beyond.gls.model.Register;
import pe.beyond.gls.model.RegisterAnswer;
import pe.beyond.gls.model.SavedIdEntity;
import pe.beyond.gls.model.Track;
import pe.beyond.gls.model.TransferEntity;
import pe.beyond.gls.model.TransferErrorEntity;
import pe.beyond.gls.model.TransferTrackEntity;
import pe.beyond.lac.gestionmovil.R;
import pe.beyond.lac.gestionmovil.daos.LogDAO;
import pe.beyond.lac.gestionmovil.daos.PhotoDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterAnswerDAO;
import pe.beyond.lac.gestionmovil.daos.RegisterDAO;
import pe.beyond.lac.gestionmovil.daos.TrackDAO;
import pe.beyond.lac.gestionmovil.utils.AppPreferences;
import pe.beyond.lac.gestionmovil.utils.Constants;
import pe.beyond.lac.gestionmovil.utils.Util;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ContentController{
	private Context context;
	public ContentController(Context context) {
		this.context = context;
	}
	
	
	//TR - JV
	public TransferTrackEntity _createTrackEntity() {
		TransferTrackEntity objTransferEntity = null;
		
		String [] selectionArgsRegister = new String[]{"0"};
		
		Log.v("_tracking",TrackDAO.QUERY_TRACK_URI.toString());
		Cursor curRegisters = context.getContentResolver().query(TrackDAO.QUERY_TRACK_URI, null, null, selectionArgsRegister, null);
		
		if (curRegisters!=null && curRegisters.getCount()>0){
			Log.v("curRegisters!=null", String.valueOf(curRegisters.getCount()));
			List< pe.beyond.gls.model.Track> lstTrack = TrackDAO.createObjects(curRegisters);
			curRegisters.close();
			String strUserId = String.valueOf(AppPreferences.getInstance(context)._loadUserId());
			String strIMEI = String.valueOf(AppPreferences.getInstance(context)._loadCellId());
			String strVersion = Util._obtainVersionName(context);
			if (strUserId==null) {
				return objTransferEntity;
			}
			
			if (lstTrack!=null && lstTrack.size()>0){
				objTransferEntity = new TransferTrackEntity();
				objTransferEntity.setLstTrack(lstTrack);
				objTransferEntity.setTrackEntityUser(Integer.parseInt(strUserId));
				objTransferEntity.setTrackEntityIMEI(strIMEI);
				objTransferEntity.setTrackEntityVersion(strVersion);

			}
		}		
		return objTransferEntity;
	}
	

	public TransferErrorEntity _createErrorEntity() {
		TransferErrorEntity objTransferErrorEntity = null;
		Cursor cur = context.getContentResolver().query(LogDAO.QUERY_ALL_URI, null, null, null, null);
		Log.v("_createErrorEntity",LogDAO.QUERY_ALL_URI.toString());
		if (cur!=null && cur.getCount()>0){
			List< pe.beyond.gls.model.Log> lstLogs = LogDAO.createObjects(cur);
			cur.close();
			if (lstLogs!=null && lstLogs.size()>0){
				objTransferErrorEntity = new TransferErrorEntity();
				objTransferErrorEntity.setLstLogs(lstLogs);
			}
		}		
		return objTransferErrorEntity;
	}
	
	public TransferEntity _createInsertEntity(){
		TransferEntity objTransferEntity = null;
		String [] selectionArgsRegister = new String[]{String.valueOf(Constants.FLG_DISABLED)};
		Cursor curRegisters = context.getContentResolver().query(RegisterDAO.QUERY_UPLOAD_SET_URI, null, null, selectionArgsRegister, null);
		Log.v("_createInsertEntity",RegisterDAO.QUERY_UPLOAD_SET_URI.toString());
		if (curRegisters!=null && curRegisters.getCount()>0){
			
			List<Register> lstRegisters = RegisterDAO.createObjects(curRegisters);
			curRegisters.close();
			if (lstRegisters!=null && lstRegisters.size()>0){
				for (Register objRegister:lstRegisters){		
					String strListId = String.valueOf(objRegister.getIntListId());
					String strDownloadCounter = String.valueOf(objRegister.getIntDownloadCounter());
					String[] selectionArgsRegisterAnswer = new String[]{strListId,strDownloadCounter};
					Cursor curRegisterAnswers = context.getContentResolver().query(RegisterAnswerDAO.QUERY_UPLOAD_BY_REGISTER_ID_URI, null, null, selectionArgsRegisterAnswer, null);
					List<RegisterAnswer> lstRegisterAnswers = RegisterAnswerDAO.createObjects(curRegisterAnswers);
					curRegisterAnswers.close();
					objRegister.setLstRegisterAnswers(lstRegisterAnswers);
				}
				objTransferEntity = new TransferEntity();
				objTransferEntity.setLstRegisters(lstRegisters);
			}
		}
		return objTransferEntity;
	}


	public TransferEntity _createUpdateEntity() {
		TransferEntity objTransferEntity = null;
		String[] selectionArgs = new String[]{String.valueOf(Constants.FLG_ENABLED)};
		Cursor curRegisterAnswers = context.getContentResolver().query(RegisterAnswerDAO.QUERY_UPLOAD_BY_REGISTER_UPDATED_URI, null, null, selectionArgs, null);
		
		if (curRegisterAnswers!= null && curRegisterAnswers.getCount()>0){
			List<RegisterAnswer> lstRegisterAnswers = RegisterAnswerDAO.createObjects(curRegisterAnswers);
			curRegisterAnswers.close();
			if (lstRegisterAnswers!=null && lstRegisterAnswers.size()>0 ){
				objTransferEntity = new TransferEntity();
				objTransferEntity.setLstRegisters(new ArrayList<Register>());
				Register objRegister = new Register();
				objRegister.setLstRegisterAnswers(lstRegisterAnswers);
				objTransferEntity.getLstRegisters().add(objRegister);
			}
			
			for (int i=0;i<objTransferEntity.getLstRegisters().get(0).getLstRegisterAnswers().size();i++){
				Log.d("PHOTOSENT","VALUE: " + objTransferEntity.getLstRegisters().get(0).getLstRegisterAnswers().get(i).getStrValue());
				Log.d("PHOTOSENT","VALUE REAL: " + objTransferEntity.getLstRegisters().get(0).getLstRegisterAnswers().get(i).getStrValueReal());
			}
		}
		
		
		return objTransferEntity;
	}
	
	public List<String> _obtainIdsFromResponse(String strResponse) {
		List<String> lstResult = null;
		
		if (strResponse!=null && !strResponse.equals(Constants.LBL_EMPTY)){
			SavedIdEntity objSavedIdEntity = new SavedIdEntity();
			Serializer serializer = new Persister();
		
			try {
				objSavedIdEntity = serializer.read(SavedIdEntity.class, strResponse);
			} catch (Exception e) {
				//TODO MANEJAR EXCEPCION
				e.printStackTrace();
			}
			if (objSavedIdEntity.getLstIds()!=null && objSavedIdEntity.getLstIds().size()>0){
				lstResult =  objSavedIdEntity.getLstIds();
			} 
		}
		return lstResult;
	}
	
	public void _deleteLogs(List<String> lstIdSaved) {
		if (lstIdSaved!= null){
			String[] selectionArgs = new String[1];
			for (String strId : lstIdSaved){
				selectionArgs[0] = strId;
				context.getContentResolver().delete(LogDAO.DELETE_BY_ID_URI, null, selectionArgs);
			}
		} else{
			Log.i("GMD", "Respuesta sin Ids de Logs para borrar de la BD");
		}
	}
	
	
	public void _updateTracking(List<String> lstIdSaved) {
		
		ContentValues valuesRegisterAnswer = new ContentValues();
		valuesRegisterAnswer.put(TrackDAO.COL_FLG_ENVIADO, Constants.FLG_ENABLED);
		
		String strWhere = null;
		if (lstIdSaved!= null){
			String[] selectionArgs = new String[1];
			for (String strId : lstIdSaved){
				selectionArgs[0] = strId;
				strWhere = TrackDAO.COL_ID+"=?"; 
//				context.getContentResolver().delete(LogDAO.DELETE_BY_ID_URI, null, selectionArgs);
				
				context.getContentResolver().update(TrackDAO.UPDATE_TRACK_URI, valuesRegisterAnswer, strWhere, selectionArgs);
			}
		} else{
			Log.i("GMD", "Respuesta sin Ids de Tracking para actualizar de la BD");
			
		}
	}
	
	public void _updateRegisterAnswerUploadState(List<String> lstIdSaved) {
		ContentValues valuesRegisterAnswer = new ContentValues();
		valuesRegisterAnswer.put(RegisterAnswerDAO.COL_FLG_ACTUALIZADO, Constants.FLG_DISABLED);
		
		String[] selectionArgs = null;
		for (String strIds:lstIdSaved){
			String[] arrIds = strIds.split(Constants.LBL_COMMA_SEPARATOR);
			// 0 = ListId , 1 = QuestionId
			selectionArgs = new String[]{arrIds[0],arrIds[1]};
			
			context.getContentResolver().update(
					RegisterAnswerDAO.UPDATE_BY_LIST_QUESTION_ID_URI, valuesRegisterAnswer,
					null, selectionArgs);	
		}
	}	
	

	public void registerPhotos(List<String> lstIdSaved,TransferEntity objTransferEntity) {
		
		ArrayList<Photo> lstPhoto = _obtainPhotosToSave(lstIdSaved,objTransferEntity);
		for (Photo objPhoto : lstPhoto){
			ContentValues objContentValues = PhotoDAO.createContentValues(objPhoto);
			context.getContentResolver().insert(PhotoDAO.INSERT_URI,objContentValues);
		}
	}
	
	public void _updateRegisterUploadState(List<String> lstIdSaved) {
		for(String strListIdSaved:lstIdSaved){
			String[] selectionArgs = new String[] { strListIdSaved};
			ContentValues valuesRegister = new ContentValues();
			valuesRegister.put(RegisterDAO.COL_FLG_ENVIO_COMPLETO, Constants.FLG_ENABLED);
			context.getContentResolver().update(
					RegisterDAO.UPDATE_BY_LIST_ID_URI, valuesRegister,
					null, selectionArgs);
			
			ContentValues valuesRegisterAnswer = new ContentValues();
			valuesRegisterAnswer.put(RegisterAnswerDAO.COL_FLG_ACTUALIZADO, Constants.FLG_DISABLED);
			context.getContentResolver().update(
					RegisterAnswerDAO.UPDATE_BY_LIST_ID_URI, valuesRegisterAnswer,
					null, selectionArgs);		
		}
	}
	


	public ArrayList<Photo> _obtainPhotosToSave(List<String> lstIdSaved,
			TransferEntity objTransferEntity) {
		ArrayList<Photo> lstPhoto = new ArrayList<Photo>();
		
		for(String strIds : lstIdSaved){
			String[] arrIds = strIds.split(Constants.LBL_COMMA_SEPARATOR);		
			if (arrIds.length==1){
				for (Register objRegister :objTransferEntity.getLstRegisters()){
					if (Integer.valueOf(arrIds[0])==objRegister.getIntListId()){
						for (RegisterAnswer objRegisterAnswer : objRegister.getLstRegisterAnswers()){
							_obtainPhotosFromRegisterAnswer(lstPhoto,objRegisterAnswer);
						}
						break;
					}
				}
				
			} else if (arrIds.length==2){
				for (RegisterAnswer objRegisterAnswer : objTransferEntity.getLstRegisters().get(0).getLstRegisterAnswers()){
					int intSavedListId = Integer.valueOf(arrIds[0]);
					int intSavesQuestionId = Integer.valueOf(arrIds[1]);
					if (intSavedListId == objRegisterAnswer.getIntListId() && intSavesQuestionId == objRegisterAnswer.getIntQuestionId()){
						_obtainPhotosFromRegisterAnswer(lstPhoto,objRegisterAnswer);
						break;
					}
				}
			}
		}
		return lstPhoto;
	}
	
	public void _obtainPhotosFromRegisterAnswer(ArrayList<Photo> lstPhoto,
			final RegisterAnswer objRegisterAnswer) {
			AppPreferences appPreferences = AppPreferences.getInstance(context);
			if (objRegisterAnswer.getIntTemplateId()==Constants.RESP_ACCION_TOMA_FOTO){
				String[] arrPhotoAbrevNames = objRegisterAnswer.getStrValue().split(Constants.LBL_COMMA_SEPARATOR);
				String[] arrPhotoRealNames = objRegisterAnswer.getStrValueReal().split(Constants.LBL_COMMA_SEPARATOR);
				if (arrPhotoAbrevNames.length>0){
					for (int itePhotoNames = 0;itePhotoNames<arrPhotoAbrevNames.length;itePhotoNames++){
						boolean booPhotoFound = _searchPhotoAbrevName(arrPhotoAbrevNames[itePhotoNames]);
						if (!booPhotoFound){
							Photo objPhoto = new Photo(-1, arrPhotoAbrevNames[itePhotoNames], arrPhotoRealNames[itePhotoNames], false,appPreferences._loadCurrentModuleId());
							lstPhoto.add(objPhoto);
						}	
					}
				}
			}
	}
	
	private boolean _searchPhotoAbrevName(String strPhotoAbrevName) {
		String[] selectionArgs = new String[] {strPhotoAbrevName};
		Cursor cursor = context.getContentResolver().query(PhotoDAO.QUERY_ONE_BY_ABREV_NAME_URI, null, null, selectionArgs, null);
		int intCount = cursor.getCount();
		cursor.close();
		return intCount>0;
	}
	

	/**
	 * Crear notificatión de envóo de información al servidor, 
	 * para informar sobre el envío de contenido y de fotos.
	 * 
	 * @param intOption - ocultar(0) o mostrar(1)
	 * @param intType - contenido(0) o fotos (1)
	 * @param strMessage - mensaje a mostrar en la barra de notificaciones
	 */
	public void _manageNotification(final int intOption,final int intType, final String strMessage){
		NotificationManager objNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (intOption == Constants.NOTIFICATION_DISPLAY){
			String strTitle = Constants.LBL_EMPTY;
			if (intType == Constants.NOTIFICATION_CONTENT){
				strTitle = context.getString(R.string.notification_title_content);
			} else if (intType == Constants.NOTIFICATION_PHOTOS){
				strTitle = context.getString(R.string.notification_title_photos);
			}
			
			long lngCurrentTime = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.ic_launcher, strTitle, lngCurrentTime);
			
			Intent objIntent = new Intent(context, context.getClass());
			PendingIntent objPendingIntent = PendingIntent.getActivity(context, 0, objIntent, 0);

			notification.setLatestEventInfo(context, strTitle, strMessage, objPendingIntent);
			objNotificationManager.notify(intType, notification);
		} else if (intOption == Constants.NOTIFICATION_HIDE){
			objNotificationManager.cancel(intType);
		}
	}
}

