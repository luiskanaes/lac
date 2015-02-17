package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class UserValidation extends Generic {
	@Element(name = "uvi", required = false)
	private int intUserValidationId;

	@Element(name = "uid", required = false)
	private int intUserId;

	@Element(name = "vid", required = false)
	private int intValidationId;

	@Element(name = "ada", required = false)
	private long lngAssignationDate;

	@Element(name = "oab", required = false)
	private String strOptionAbreviation;

	@Element(name = "ore", required = false)
	private String strOptionReal;
	
	@Element(name = "est", required = false)
	private int intState;

	public int getIntUserValidationId() {
		return intUserValidationId;
	}

	public void setIntUserValidationId(int intUserValidationId) {
		this.intUserValidationId = intUserValidationId;
	}

	public int getIntUserId() {
		return intUserId;
	}

	public void setIntUserId(int intUserId) {
		this.intUserId = intUserId;
	}

	public int getIntValidationId() {
		return intValidationId;
	}

	public void setIntValidationId(int intValidationId) {
		this.intValidationId = intValidationId;
	}

	public long getLngAssignationDate() {
		return lngAssignationDate;
	}

	public void setLngAssignationDate(long lngAssignationDate) {
		this.lngAssignationDate = lngAssignationDate;
	}

	public String getStrOptionAbreviation() {
		return strOptionAbreviation;
	}

	public void setStrOptionAbreviation(String strOptionAbreviation) {
		this.strOptionAbreviation = strOptionAbreviation;
	}

	public String getStrOptionReal() {
		return strOptionReal;
	}

	public void setStrOptionReal(String strOptionReal) {
		this.strOptionReal = strOptionReal;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public UserValidation() {
	}

	public UserValidation(int intUserValidationId, int intUserId,
			int intValidationId, long lngAssignationDate,
			String strOptionAbreviation, String strOptionReal, int intState) {
		super();
		this.intUserValidationId = intUserValidationId;
		this.intUserId = intUserId;
		this.intValidationId = intValidationId;
		this.lngAssignationDate = lngAssignationDate;
		this.strOptionAbreviation = strOptionAbreviation;
		this.strOptionReal = strOptionReal;
		this.intState = intState;
	}
	
}
