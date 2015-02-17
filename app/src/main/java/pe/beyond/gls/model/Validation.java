package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class Validation extends Generic {
	@Element(name = "vid", required = false)
	private int intValidationId;
	@Element(name = "max", required = false)
	private String strMaxValue;
	@Element(name = "min", required = false)
	private String strMinValue;
	@Element(name = "des", required = false)
	private String strValue;
	@Element(name = "err", required = false)
	private String strErrorMessage;
	@Element(name = "res", required = false)
	private boolean booRestrictive;
	@Element(name = "typ", required = false)
	private int intValidationType;

	public int getIntValidationId() {
		return intValidationId;
	}

	public void setIntValidationId(int intValidationId) {
		this.intValidationId = intValidationId;
	}

	public String getStrMaxValue() {
		return strMaxValue;
	}

	public void setStrMaxValue(String strMaxValue) {
		this.strMaxValue = strMaxValue;
	}

	public String getStrMinValue() {
		return strMinValue;
	}

	public void setStrMinValue(String strMinValue) {
		this.strMinValue = strMinValue;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public String getStrErrorMessage() {
		return strErrorMessage;
	}

	public void setStrErrorMessage(String strErrorMessage) {
		this.strErrorMessage = strErrorMessage;
	}

	public boolean isBooRestrictive() {
		return booRestrictive;
	}

	public void setBooRestrictive(boolean booRestrictive) {
		this.booRestrictive = booRestrictive;
	}

	public int getIntValidationType() {
		return intValidationType;
	}

	public void setIntValidationType(int intValidationType) {
		this.intValidationType = intValidationType;
	}

	public Validation() {
	}

	public Validation(int intValidationId, String strMaxValue, String strMinValue,
			String strValue, String strErrorMessage,
			boolean booRestrictive, int intValidationType) {
		super();
		this.intValidationId = intValidationId;
		this.strMaxValue = strMaxValue;
		this.strMinValue = strMinValue;
		this.strValue = strValue;
		this.strErrorMessage = strErrorMessage;
		this.booRestrictive = booRestrictive;
		this.intValidationType = intValidationType;
	}
}
