package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import pe.beyond.lac.gestionmovil.utils.Constants;

@Root(name = "registeranswer", strict = false)
public class RegisterAnswer extends Generic {
	@Element(name = "lid", required = false)
	private int intListId;
	@Element(name = "nde", required = false)
	private int intDownloadCounter;
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "val", required = false)
	private String strValue;
	@Element(name = "vre", required = false)
	private String strValueReal;

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

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public String getStrValueReal() {
		return strValueReal;
	}

	public void setStrValueReal(String strValueReal) {
		this.strValueReal = strValueReal;
	}

	public RegisterAnswer() {
	}
	
	public RegisterAnswer(int intListId,int intDownloadCounter, int intQuestionId,
			String strValue, String strValueReal, boolean booIsUpdated) {
		super();
		this.intListId = intListId;
		this.intDownloadCounter = intDownloadCounter;
		this.intQuestionId = intQuestionId;
		this.strValue = strValue;
		this.strValueReal = strValueReal;
		this.booIsUpdated = booIsUpdated;
	}

	public RegisterAnswer(final Question objQuestion,final int intListId, final int intDownloadCounter) {
		this.intListId = intListId;
		this.intDownloadCounter = intDownloadCounter;
		this.intQuestionId = objQuestion.getIntQuestionId();
		
		if (objQuestion.getIntTemplateId()==Constants.RESP_ACCION_TOMA_FOTO){
			this.strValueReal = objQuestion.getStrTemporalRealAnswer();
			this.strValue = objQuestion.getStrTemporalAbrevAnswer();
		} else{
			this.strValue = objQuestion.getStrTemporalAbrevAnswer();
			this.strValueReal = objQuestion.getStrTemporalRealAnswer();
		}
		this.booIsUpdated = true;
	}
	
	@Transient
	private int intTemplateId;

	@Element(name = "biu", required = false)
	private boolean booIsUpdated;

	public int getIntTemplateId() {
		return intTemplateId;
	}

	public void setIntTemplateId(int intTemplateId) {
		this.intTemplateId = intTemplateId;
	}

	public boolean isBooIsUpdated() {
		return booIsUpdated;
	}

	public void setBooIsUpdated(boolean booIsUpdated) {
		this.booIsUpdated = booIsUpdated;
	}

}
