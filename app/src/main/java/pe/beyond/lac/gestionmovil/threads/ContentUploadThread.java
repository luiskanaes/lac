package pe.beyond.lac.gestionmovil.threads;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.beyond.gls.model.Register;
import pe.beyond.gls.model.TransferEntity;
import pe.beyond.gls.model.TransferErrorEntity;
import pe.beyond.gls.model.TransferTrackEntity;
import pe.beyond.lac.gestionmovil.controllers.ContentController;
import pe.beyond.lac.gestionmovil.utils.Connector;
import pe.beyond.lac.gestionmovil.utils.Constants;
import android.content.Context;
import android.util.Log;

public class ContentUploadThread extends Thread{
	private Context context;
	private ContentController controller;
	private PhotoUploadThread thrPhotoUpload;	
	public ContentUploadThread(Context context) {
		super();
		this.context = context;
		this.controller = new ContentController(context);
	}

	
	@Override
	public boolean isInterrupted() {
		Date date = new Date();
		Log.d("GMD", "HILO INTERRUMPIDO A LAS " + date.toLocaleString());
		return super.isInterrupted();
	}


	@Override
	public void destroy() {
		Date date = new Date();
		Log.d("GMD", "HILO DESTRUIDO A LAS " + date.toLocaleString());
		super.destroy();
	}


	public void run() {
		while(true){
			try{
				Thread.sleep(Constants.TIME_EXECUTE_UPLOAD);
			} catch(Exception e){
				e.printStackTrace();
			}
			Date date = new Date(System.currentTimeMillis());
			Log.d("GMD",  "HILO EJECUTANDOSE A LAS : " + date.toLocaleString());
			_sendInserts();
			_sendUpdates();
			_sendErrors();
			_executePhotoThread();
			_sendTrack();
		}
	}
	
	private void _sendInserts() {
		boolean booKeepInserts = true;
		while (booKeepInserts){
			TransferEntity objTransferEntity = controller._createInsertEntity();
			if (objTransferEntity!=null && objTransferEntity.getLstRegisters()!=null && objTransferEntity.getLstRegisters().size()>0){
				StringBuilder sb = new StringBuilder("Enviando registros : ");
				for (Register objRegister: objTransferEntity.getLstRegisters()){
					sb.append(objRegister.getIntListId() + " - ");
				}
				Log.i("GMD", sb.toString());
				if (objTransferEntity.getLstRegisters().size()==Constants.NUM_UPLOAD_SET_LIMIT){
					booKeepInserts = true;
				} else {
					booKeepInserts = false;
				}
				Serializer objSerializer = new Persister();		
				ByteArrayOutputStream outstream = new ByteArrayOutputStream();
				
				String strResponse = null;
				
				try {
					objSerializer.write(objTransferEntity, outstream);
					String strTransferStream = outstream.toString();			
					
					Connector objConnector = Connector.getInstance(context);			
					strResponse = objConnector._sendConsumedInfo(strTransferStream);
				} catch (Exception e) {
					Log.i("GMD", "CATCH EN _sendConsumedInfo");
					e.printStackTrace();
				}
				List<String> lstIdSaved = controller._obtainIdsFromResponse(strResponse);
				if (lstIdSaved!= null && lstIdSaved.size()>0){
					//TODO MOSTRAR IDS RECUPERADOS					
//					Handler h = new Handler(context.getMainLooper());
//
//				    h.post(new Runnable() {
//				        @Override
//				        public void run() {
//				             Toast.makeText(context,"ENVIADOS: " + _createIdsInserted(lstIdSaved);,Toast.LENGTH_SHORT).show();
//				        }
//				    });
					controller._updateRegisterUploadState(lstIdSaved);
					controller.registerPhotos(lstIdSaved,objTransferEntity);
				} else {
					Log.i("GMD", "El servicio no retornó IDs de los registros/preguntas correctamente guardados");
				}
				
			} else{
				Log.i("GMD", "No se encontraron nuevos registros trabajados para enviar al servidor");
				booKeepInserts = false;
			}
		}
	}

	private void _sendUpdates() {
		TransferEntity objAnswersUpdated = controller._createUpdateEntity();
		if (objAnswersUpdated!=null && objAnswersUpdated.getLstRegisters()!=null && objAnswersUpdated.getLstRegisters().size()>0){
			Serializer objSerializer = new Persister();		
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			
			String strResponse = null;
			
			try {
				objSerializer.write(objAnswersUpdated, outstream);
				String strTransferStream = outstream.toString();			
				
				Connector objConnector = Connector.getInstance(context);			
				strResponse = objConnector._sendUpdatedInfo(strTransferStream);
			} catch (Exception e) {
				//TODO MANEJAR EXCEPCION
				e.printStackTrace();
			}
			List<String> lstIdSaved = controller._obtainIdsFromResponse(strResponse);
			
			if (lstIdSaved!= null && lstIdSaved.size()>0){
				controller._updateRegisterAnswerUploadState(lstIdSaved);
				controller.registerPhotos(lstIdSaved,objAnswersUpdated);
			} else {
				Log.i("GMD", "El servicio no retornó IDs de los registros/preguntas correctamente guardados");
			}
			
		} else{
			Log.i("GMD", "No se encontraron respuestas actualizadas para enviar al servidor");
		}
	}
	
	private void _sendErrors() {
		TransferErrorEntity objTransferEntity = controller._createErrorEntity();
		if (objTransferEntity!=null && objTransferEntity.getLstLogs()!=null && objTransferEntity.getLstLogs().size()>0){
			Serializer objSerializer = new Persister();		
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			
			String strResponse = null;
			try {
				objSerializer.write(objTransferEntity, outstream);
				String strTransferStream = outstream.toString();			
				
				Connector objConnector = Connector.getInstance(context);			
				strResponse = objConnector._sendErrorInfo(strTransferStream);
			} catch (Exception e) {
				//TODO MANEJAR EXCEPCION
				e.printStackTrace();
			}
			
			List<String> lstIdSaved = controller._obtainIdsFromResponse(strResponse);
			controller._deleteLogs(lstIdSaved);
			
		} else{
			Log.i("GMD", "No se encontraron nuevos Logs para enviar al servidor");
		}
	}
	
	// TR - Jav
	
	private void _sendTrack() {
		TransferTrackEntity objTransferEntity = controller._createTrackEntity();
		if (objTransferEntity!=null && objTransferEntity.getLstTrack()!=null && objTransferEntity.getLstTrack().size()>0){
			Serializer objSerializer = new Persister();		
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			
			String strResponse = null;
			try {
				objSerializer.write(objTransferEntity, outstream);
				String strTransferStream = outstream.toString();			
				Connector objConnector = Connector.getInstance(context);			
				strResponse = objConnector._sendTrackingInfo(strTransferStream);
				//Log.d("response =>",strResponse);
			} catch (Exception e) {
				Log.v("GMD EX","_sendTrack()");
			}
			Log.i("GMD", "ACTUALIZANDO IDs RETORNADOS");
			
			List<String> lstIdSaved = controller._obtainIdsFromResponse(strResponse);
			controller._updateTracking(lstIdSaved);

		} else{
			Log.i("GMD", "Tracking vacio ó NULL");
		}
	}
	
	
	
	
	private void _executePhotoThread() {
		if (thrPhotoUpload == null){
			thrPhotoUpload = new PhotoUploadThread(context);
			thrPhotoUpload.start();
		} else{
			if (!thrPhotoUpload.isAlive()){
				thrPhotoUpload = new PhotoUploadThread(context);
				thrPhotoUpload.start();
			} else{
				thrPhotoUpload.booRequiresRetry = true;
			}
		}
	}
	
}