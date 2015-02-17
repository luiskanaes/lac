package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class Template extends Generic {
	@Element(name = "tid", required = false)
	private int intTemplateId;
	@Element(name = "des", required = false)
	private String strDescription;
	@Element(name = "sta", required = false)
	private int intState;

	public int getIntTemplateId() {
		return intTemplateId;
	}

	public void setIntTemplateId(int intTemplateId) {
		this.intTemplateId = intTemplateId;
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

	public Template() {
	}

	public Template(int intTemplateId, String strDescription, int intState) {
		super();
		this.intTemplateId = intTemplateId;
		this.strDescription = strDescription;
		this.intState = intState;
	}
}
