package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class ValidationResult extends Generic {
	@Element(name = "rid", required = false)
	private int intValidationResultId;
	@Element(name = "vid", required = false)
	private int intValidationId;
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "edi", required = false)
	private boolean booFlgEdition;
	@Element(name = "val", required = false)
	private String strValue;
	@Element(name = "rva", required = false)
	private String strRealValue;
	@Element(name = "sta", required = false)
	private int intState;
	
	public int getIntValidationResultId() {
		return intValidationResultId;
	}

	public void setIntValidationResultId(int intValidationResultId) {
		this.intValidationResultId = intValidationResultId;
	}

	public int getIntValidationId() {
		return intValidationId;
	}

	public void setIntValidationId(int intValidationId) {
		this.intValidationId = intValidationId;
	}

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public boolean isBooFlgEdition() {
		return booFlgEdition;
	}

	public void setBooFlgEdition(boolean booFlgEdition) {
		this.booFlgEdition = booFlgEdition;
	}

	public String getStrRealValue() {
		return strRealValue;
	}

	public void setStrRealValue(String strRealValue) {
		this.strRealValue = strRealValue;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public ValidationResult() {
	}

	public ValidationResult(int intValidationResultId, int intValidationId,
			int intQuestionId, boolean booFlgEdition, String strValue,
			String strRealValue, int intState) {
		super();
		this.intValidationResultId = intValidationResultId;
		this.intValidationId = intValidationId;
		this.intQuestionId = intQuestionId;
		this.booFlgEdition = booFlgEdition;
		this.strValue = strValue;
		this.strRealValue = strRealValue;
		this.intState = intState;
	}

}
