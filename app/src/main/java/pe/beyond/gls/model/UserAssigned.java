package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "userassigned", strict = false)
public class UserAssigned extends Generic {
	@Element(name = "uid", required = false)
	private int intUserId;

	@Element(name = "bid", required = false)
	private int intBaseId;

	@Element(name = "mif", required = false)
	private int intModuleId;

	@Element(name = "cod", required = false)
	private String strUserCode;

	@Element(name = "sta", required = false)
	private int intState;

	@Element(name = "fdm", required = false)
	private boolean booFlgDefaultModule;

	/**
	 * Nombre de la base identificada por el intBaseId, para ser mostrado en la
	 * lista de perfiles.
	 */
	@Element(name = "bas", required = false)
	private String strBase;

	/**
	 * Nombre de la empresa identificado por el intBaseId.
	 */
	@Element(name = "com", required = false)
	private String strCompany;

	/**
	 * Última fecha en la que se sincronizó la informción del usuario/módulo.
	 */
	private long lngLastTimeSynced;

	/**
	 * Se contiene la información del módulo asociado al momento de descargar la
	 * información del operador.
	 */
	@Element(name = "mod")
	private Module module;

	
	public int getIntUserId() {
		return intUserId;
	}

	public void setIntUserId(int intUserId) {
		this.intUserId = intUserId;
	}

	public int getIntBaseId() {
		return intBaseId;
	}

	public void setIntBaseId(int intBaseId) {
		this.intBaseId = intBaseId;
	}

	public int getIntModuleId() {
		return intModuleId;
	}

	public void setIntModuleId(int intModuleId) {
		this.intModuleId = intModuleId;
	}

	public String getStrUserCode() {
		return strUserCode;
	}

	public void setStrUserCode(String strUserCode) {
		this.strUserCode = strUserCode;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public boolean isBooFlgDefaultModule() {
		return booFlgDefaultModule;
	}

	public void setBooFlgDefaultModule(boolean booFlgDefaultModule) {
		this.booFlgDefaultModule = booFlgDefaultModule;
	}

	public String getStrBase() {
		return strBase;
	}

	public void setStrBase(String strBase) {
		this.strBase = strBase;
	}

	public String getStrCompany() {
		return strCompany;
	}

	public void setStrCompany(String strCompany) {
		this.strCompany = strCompany;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public long getLngLastTimeSynced() {
		return lngLastTimeSynced;
	}

	public void setLngLastTimeSynced(long lngLastTimeSynced) {
		this.lngLastTimeSynced = lngLastTimeSynced;
	}

	public UserAssigned() {
	}

	public UserAssigned(int intUserId, int intBaseId, int intModuleId,
			String strUserCode, int intState, boolean booFlgDefaultModule,
			String strBase, String strCompany, long lngLastTimeSynced) {
		super();
		this.intUserId = intUserId;
		this.intBaseId = intBaseId;
		this.intModuleId = intModuleId;
		this.strUserCode = strUserCode;
		this.intState = intState;
		this.booFlgDefaultModule = booFlgDefaultModule;
		this.strBase = strBase;
		this.strCompany = strCompany;
		this.lngLastTimeSynced = lngLastTimeSynced;
	}
}
