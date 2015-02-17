package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class OptionResult extends Generic {
	@Element(name = "rid", required = false)
	private int intOptionResultId;
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "oid", required = false)
	private int intOptionId;
	@Element(name = "edi", required = false)
	private boolean booFlgEdition;
	@Element(name = "val", required = false)
	private String strValue;
	@Element(name = "vre", required = false)
	private String strRealValue;
	@Element(name = "sta", required = false)
	private int intState;

	

	public int getIntOptionResultId() {
		return intOptionResultId;
	}

	public void setIntOptionResultId(int intOptionResultId) {
		this.intOptionResultId = intOptionResultId;
	}

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public int getIntOptionId() {
		return intOptionId;
	}

	public void setIntOptionId(int intOptionId) {
		this.intOptionId = intOptionId;
	}

	public boolean isBooFlgEdition() {
		return booFlgEdition;
	}

	public void setBooFlgEdition(boolean booFlgEdition) {
		this.booFlgEdition = booFlgEdition;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
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

	public OptionResult() {
	}

	public OptionResult(int intOptionResultId, int intQuestionId, int intOptionId,
			boolean booFlgEdition, String strValue, String strRealValue,
			int intState) {
		super();
		this.intOptionResultId = intOptionResultId;
		this.intQuestionId = intQuestionId;
		this.intOptionId = intOptionId;
		this.booFlgEdition = booFlgEdition;
		this.strValue = strValue;
		this.strRealValue = strRealValue;
		this.intState = intState;
	}	
}
