package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import pe.beyond.lac.gestionmovil.utils.Constants;

@Root(name = "register", strict = false)
public class Register extends Generic {
	@Element(name = "lid", required = false)
	private int intListId;
	@Element(name = "nde", required = false)
	private int intDownloadCounter;
	
	@Element(name = "sdl", required = false)
	private long lngStartDateReal;
	@Element(name = "edl", required = false)
	private long lngEndDateReal;
	@Element(name = "sdr", required = false)
	private long lngStartDateRegister;
	@Element(name = "edr", required = false)
	private long lngEndDateRegister;
	
	@Element(name = "lal", required = false)
	private float fltLatitudeReal;
	@Element(name = "lol", required = false)
	private float fltLongitudeReal;
	@Element(name = "lar", required = false)
	private float fltLatitudeRegister;
	@Element(name = "lor", required = false)
	private float fltLongitudeRegister;
	
	@Element(name = "acc", required = false)
	private float fltGPSAccuracy;
	@Element(name = "fqr", required = false)
	private boolean booFlgQR;
	@Element(name = "fdz", required = false)
	private boolean booFlgDangerousZone;
	@Element(name = "use", required = false)
	private int intUserId;
	@Element(name = "uai", required = false)
	private String strUserCode;
	
	@Element(name = "mov", required = false)
	private String strMovilNumber;
	@Element(name = "com", required = false)
	private String strCommercialManagmentCode;
	@Element(name = "fid", required = false)
	private int intFluxId;
	@Element(name = "dst", required = false)
	private int intDeliveryState;
	@Element(name = "sta", required = false)
	private int intState;

	public int getIntListId() {
		return intListId;
	}

	public void setIntListId(int intListId) {
		this.intListId = intListId;
	}

	

	public int getIntDownloadCounter() {
		return intDownloadCounter;
	}

	public void setIntDownloadCounter(int intDownloadCounter) {
		this.intDownloadCounter = intDownloadCounter;
	}

	public long getLngStartDateReal() {
		return lngStartDateReal;
	}

	public void setLngStartDateReal(long lngStartDateReal) {
		this.lngStartDateReal = lngStartDateReal;
	}

	public long getLngEndDateReal() {
		return lngEndDateReal;
	}

	public void setLngEndDateReal(long lngEndDateReal) {
		this.lngEndDateReal = lngEndDateReal;
	}

	public long getLngStartDateRegister() {
		return lngStartDateRegister;
	}

	public void setLngStartDateRegister(long lngStartDateRegister) {
		this.lngStartDateRegister = lngStartDateRegister;
	}

	public long getLngEndDateRegister() {
		return lngEndDateRegister;
	}

	public void setLngEndDateRegister(long lngEndDateRegister) {
		this.lngEndDateRegister = lngEndDateRegister;
	}

	public float getFltLatitudeReal() {
		return fltLatitudeReal;
	}

	public void setFltLatitudeReal(float fltLatitudeReal) {
		this.fltLatitudeReal = fltLatitudeReal;
	}

	public float getFltLongitudeReal() {
		return fltLongitudeReal;
	}

	public void setFltLongitudeReal(float fltLongitudeReal) {
		this.fltLongitudeReal = fltLongitudeReal;
	}

	public float getFltLatitudeRegister() {
		return fltLatitudeRegister;
	}

	public void setFltLatitudeRegister(float fltLatitudeRegister) {
		this.fltLatitudeRegister = fltLatitudeRegister;
	}

	public float getFltLongitudeRegister() {
		return fltLongitudeRegister;
	}

	public void setFltLongitudeRegister(float fltLongitudeRegister) {
		this.fltLongitudeRegister = fltLongitudeRegister;
	}

	public float getFltGPSAccuracy() {
		return fltGPSAccuracy;
	}

	public void setFltGPSAccuracy(float fltGPSAccuracy) {
		this.fltGPSAccuracy = fltGPSAccuracy;
	}

	public boolean isBooFlgQR() {
		return booFlgQR;
	}

	public void setBooFlgQR(boolean booFlgQR) {
		this.booFlgQR = booFlgQR;
	}

	public boolean isBooFlgDangerousZone() {
		return booFlgDangerousZone;
	}

	public void setBooFlgDangerousZone(boolean booFlgDangerousZone) {
		this.booFlgDangerousZone = booFlgDangerousZone;
	}

	public int getIntUserId() {
		return intUserId;
	}

	public void setIntUserId(int intUserId) {
		this.intUserId = intUserId;
	}

	public String getStrUserCode() {
		return strUserCode;
	}

	public void setStrUserCode(String strUserCode) {
		this.strUserCode = strUserCode;
	}

	public String getStrMovilNumber() {
		return strMovilNumber;
	}

	public void setStrMovilNumber(String strMovilNumber) {
		this.strMovilNumber = strMovilNumber;
	}

	public String getStrCommercialManagmentCode() {
		return strCommercialManagmentCode;
	}

	public void setStrCommercialManagmentCode(String strCommercialManagmentCode) {
		this.strCommercialManagmentCode = strCommercialManagmentCode;
	}

	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}
	
	public int getIntDeliveryState() {
		return intDeliveryState;
	}

	public void setIntDeliveryState(int intDeliveryState) {
		this.intDeliveryState = intDeliveryState;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public Register() {
	}

	public Register(final Listing objListing, final boolean booFlgQR,final String strUserCode) {

		this.intListId = objListing.getIntListId();
		this.intDownloadCounter = objListing.getIntDownloadCounter();
		this.lngStartDateReal = -1;
		this.lngEndDateReal = -1;
		this.lngStartDateRegister = -1;
		this.lngEndDateRegister = -1;
		this.fltLatitudeReal = -1;
		this.fltLongitudeReal = -1;
		this.fltLatitudeRegister = -1;
		this.fltLongitudeRegister = -1;
		this.booFlgQR = booFlgQR;
		this.booFlgDangerousZone = false;
		this.intUserId = objListing.getIntUserId();
		this.strUserCode = strUserCode;
		this.strCommercialManagmentCode = objListing.getStrCommercialManagmentCode();
		this.intFluxId = objListing.getIntFluxId();
		this.intState = Constants.REGISTER_STATE_FINALIZED;
		this.intDeliveryState = Constants.FLG_DISABLED;
		
	}

	public Register(int intListId, int intDownloadCounter, long lngStartDateReal,
			long lngEndDateReal, long lngStartDateRegister,
			long lngEndDateRegister, float fltLatitudeReal,
			float fltLongitudeReal, float fltLatitudeRegister,
			float fltLongitudeRegister, float fltGPSAccuracy, boolean booFlgQR,
			boolean booFlgDangerousZone, int intUserId, String strUserCode, String strMovilNumber,
			String strCommercialManagmentCode, int intFluxId,
			int intDeliveryState, int intState) {
		super();
		this.intListId = intListId;
		this.intDownloadCounter = intDownloadCounter;
		this.lngStartDateReal = lngStartDateReal;
		this.lngEndDateReal = lngEndDateReal;
		this.lngStartDateRegister = lngStartDateRegister;
		this.lngEndDateRegister = lngEndDateRegister;
		this.fltLatitudeReal = fltLatitudeReal;
		this.fltLongitudeReal = fltLongitudeReal;
		this.fltLatitudeRegister = fltLatitudeRegister;
		this.fltLongitudeRegister = fltLongitudeRegister;
		this.fltGPSAccuracy = fltGPSAccuracy;
		this.booFlgQR = booFlgQR;
		this.booFlgDangerousZone = booFlgDangerousZone;
		this.intUserId = intUserId;
		this.strUserCode = strUserCode;
		this.strMovilNumber = strMovilNumber;
		this.strCommercialManagmentCode = strCommercialManagmentCode;
		this.intFluxId = intFluxId;
		this.intDeliveryState = intDeliveryState;
		this.intState = intState;
	}

	@ElementList(inline = true)
	private List<RegisterAnswer> lstRegisterAnswers;

	public List<RegisterAnswer> getLstRegisterAnswers() {
		return lstRegisterAnswers;
	}

	public void setLstRegisterAnswers(List<RegisterAnswer> lstRegisterAnswers) {
		this.lstRegisterAnswers = lstRegisterAnswers;
	}

}
