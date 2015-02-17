package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class State extends Generic {
	@Element(name = "sid", required = false)
	private int intStateId;
	@Element(name = "tid", required = false)
	private String strTableId;
	@Element(name = "sta", required = false)
	private String strState;

	public int getIntStateId() {
		return intStateId;
	}

	public void setIntStateId(int intStateId) {
		this.intStateId = intStateId;
	}

	public String getStrTableId() {
		return strTableId;
	}

	public void setStrTableId(String strTableId) {
		this.strTableId = strTableId;
	}

	public String getStrState() {
		return strState;
	}

	public void setStrState(String strState) {
		this.strState = strState;
	}

	public State() {
	}

	public State(int intStateId, String strTableId, String strState) {
		super();
		this.intStateId = intStateId;
		this.strTableId = strTableId;
		this.strState = strState;
	}
}
