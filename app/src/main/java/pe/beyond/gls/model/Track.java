package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register")
public class Track extends Generic {
	@Element(name = "pro")
	private int intProviderId;
	@Element(name = "lat")
	private float fltLatitude;
	@Element(name = "lon")
	private float fltLongitude;
	@Element(name = "acc")
	private float fltAccuracy;
	@Element(name = "tim")
	private long lngTime;

	//TRACK IDENTIFIER
	@Element(name = "env")
	private int intEnviado;
	@Element(name = "id")
	private int intId;
	@Element(name = "usr")
	private int intIdUser;
	
	//IMEI + VERSION
	@Element(name = "ime")
	private String strIMEI;
	@Element(name = "ver")
	private String strVersion;
	
	
	public String getStrIMEI() {
		return strIMEI;
	}

	public void setStrIMEI(String strIMEI) {
		this.strIMEI = strIMEI;
	}
	

	public String getStrVersion() {
		return strVersion;
	}

	public void setStrVersion(String strVersion) {
		this.strVersion = strVersion;
	}

	public int getIntIdUser() {
		return intIdUser;
	}

	public void setIntIdUser(int intIdUser) {
		this.intIdUser = intIdUser;
	}

	public int getIntId() {
		return intId;
	}

	public void setIntId(int intId) {
		this.intId = intId;
	}

	public int getIntEnviado() {
		return intEnviado;
	}

	public void setIntEnviado(int intEnviado) {
		this.intEnviado = intEnviado;
	}
	

	public int getIntProviderId() {
		return intProviderId;
	}

	public void setIntProviderId(int intProviderId) {
		this.intProviderId = intProviderId;
	}

	public float getFltLatitude() {
		return fltLatitude;
	}

	public void setFltLatitude(float fltLatitude) {
		this.fltLatitude = fltLatitude;
	}

	public float getFltLongitude() {
		return fltLongitude;
	}

	public void setFltLongitude(float fltLongitude) {
		this.fltLongitude = fltLongitude;
	}

	public float getFltAccuracy() {
		return fltAccuracy;
	}

	public void setFltAccuracy(float fltAccuracy) {
		this.fltAccuracy = fltAccuracy;
	}

	public long getLngTime() {
		return lngTime;
	}

	public void setLngTime(long lngTime) {
		this.lngTime = lngTime;
	}

	public Track() {
	}

	public Track(int intProviderId, float fltLatitude, float fltLongitude,
			float fltAccuracy, long lngTime, int intEnviado, int intId ) {
		super();
		this.intProviderId = intProviderId;
		this.fltLatitude = fltLatitude;
		this.fltLongitude = fltLongitude;
		this.fltAccuracy = fltAccuracy;
		this.lngTime = lngTime;
		this.intEnviado = intEnviado;
		this.intId = intId;
		
		
		
	}
	
	
	
//	public Track(int intProviderId, float fltLatitude, float fltLongitude,
//			float fltAccuracy, long lngTime, int intEnviado, int intId, int intIdUser ) {
//		super();
//		this.intProviderId = intProviderId;
//		this.fltLatitude = fltLatitude;
//		this.fltLongitude = fltLongitude;
//		this.fltAccuracy = fltAccuracy;
//		this.lngTime = lngTime;
//		this.intEnviado = intEnviado;
//		this.intIdUser = intIdUser;
//	}
}
