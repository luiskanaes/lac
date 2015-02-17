package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class ValidationType extends Generic {
	@Element(name = "vti", required = false)
	private int intValidationTypeId;
	@Element(name = "des", required = false)
	private String strDescription;

	public int getIntValidationTypeId() {
		return intValidationTypeId;
	}

	public void setIntValidationTypeId(int intValidationTypeId) {
		this.intValidationTypeId = intValidationTypeId;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public ValidationType() {
	}

	public ValidationType(int intValidationTypeId, String strDescription) {
		super();
		this.intValidationTypeId = intValidationTypeId;
		this.strDescription = strDescription;
	}

}
