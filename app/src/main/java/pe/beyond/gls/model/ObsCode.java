package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class ObsCode extends Generic {
	@Element(name = "oid", required = false)
	private int intObsId;
	@Element(name = "fid", required = false)
	private int intFluxId;
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "sta", required = false)
	private int intState;
	
	public int getIntObsId() {
		return intObsId;
	}

	public void setIntObsId(int intObsId) {
		this.intObsId = intObsId;
	}

	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public ObsCode() {
	}

	public ObsCode(int intObsId, int intFluxId, int intQuestionId, int intState) {
		super();
		this.intObsId = intObsId;
		this.intFluxId = intFluxId;
		this.intQuestionId = intQuestionId;
		this.intState = intState;
	}
}
