package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "module")
public class Module extends Generic {
	@Element(name = "mid", required = false)
	private int intModuleId;
	@Element(name = "des", required = false)
	private String strDescription;
	@Element(name = "sta", required = false)
	private int intState;
	@Element(name = "acr", required = false)
	private String strAcronym;
	@Element(name = "squ", required = false)
	private int intSearchQuantity;
	@Element(name = "sor", required = false)
	private boolean booSearch;
	@Element(name = "qrc", required = false)
	private int intQR;

	public int getIntModuleId() {
		return intModuleId;
	}

	public void setIntModuleId(int intModuleId) {
		this.intModuleId = intModuleId;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public String getStrAcronym() {
		return strAcronym;
	}

	public void setStrAcronym(String strAcronym) {
		this.strAcronym = strAcronym;
	}

	public int getIntSearchQuantity() {
		return intSearchQuantity;
	}

	public void setIntSearchQuantity(int intSearchQuantity) {
		this.intSearchQuantity = intSearchQuantity;
	}
	
	public boolean isBooSearch() {
		return booSearch;
	}

	public void setBooSearch(boolean booSearch) {
		this.booSearch = booSearch;
	}

	public int getIntQR() {
		return intQR;
	}

	public void setIntQR(int intQR) {
		this.intQR = intQR;
	}

	public Module() {
	}

	public Module(int intModuleId, String strDescription, int intState,
			String strAcronym, int intSearchQuantity,
			boolean booSearch, int intQR) {
		super();
		this.intModuleId = intModuleId;
		this.strDescription = strDescription;
		this.intState = intState;
		this.strAcronym = strAcronym;
		this.intSearchQuantity = intSearchQuantity;
		this.booSearch = booSearch;
		this.intQR = intQR;
	}
}
