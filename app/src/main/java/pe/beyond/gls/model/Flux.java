package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class Flux extends Generic {
	@Element(name = "fid", required = false)
	private int intFluxId;
	@Element(name = "des", required = false)
	private String strDescription;
	@Element(name = "mid", required = false)
	private int intModuleId;
	@Element(name = "cid", required = false)
	private int intConfigurationId;
	@Element(name = "stt", required = false)
	private int intState;
	@Element(name = "sta", required = false)
	private long lngStartHour;
	@Element(name = "end", required = false)
	private long lngEndHour;
	@Element(name = "pro", required = false)
	private long intProcess;
	
	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public int getIntModuleId() {
		return intModuleId;
	}

	public void setIntModuleId(int intModuleId) {
		this.intModuleId = intModuleId;
	}

	public int getIntConfigurationId() {
		return intConfigurationId;
	}

	public void setIntConfigurationId(int intConfigurationId) {
		this.intConfigurationId = intConfigurationId;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public long getLngStartHour() {
		return lngStartHour;
	}

	public void setLngStartHour(long lngStartHour) {
		this.lngStartHour = lngStartHour;
	}

	public long getLngEndHour() {
		return lngEndHour;
	}

	public void setLngEndHour(long lngEndHour) {
		this.lngEndHour = lngEndHour;
	}

	public long getIntProcess() {
		return intProcess;
	}

	public void setIntProcess(long intProcess) {
		this.intProcess = intProcess;
	}

	public Flux() {
	}

	public Flux(int intFluxId, String strDescription, int intModuleId,
			int intConfigurationId, int intState, long lngStartHour,
			long lngEndHour, long intProcess) {
		super();
		this.intFluxId = intFluxId;
		this.strDescription = strDescription;
		this.intModuleId = intModuleId;
		this.intConfigurationId = intConfigurationId;
		this.intState = intState;
		this.lngStartHour = lngStartHour;
		this.lngEndHour = lngEndHour;
		this.intProcess = intProcess;
	}
}
