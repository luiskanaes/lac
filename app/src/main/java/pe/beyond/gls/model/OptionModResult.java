package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class OptionModResult extends Generic {
	@Element(name = "rid", required = false)
	private int intOptionModResultId;
	@Element(name = "oid", required = false)
	private int intOptionId;
	@Element(name = "mid", required = false)
	private int intOptionModId;
	@Element(name = "sta", required = false)
	private int intState ;
	
	public int getIntOptionModResultId() {
		return intOptionModResultId;
	}

	public void setIntOptionModResultId(int intOptionModResultId) {
		this.intOptionModResultId = intOptionModResultId;
	}

	public int getIntOptionId() {
		return intOptionId;
	}

	public void setIntOptionId(int intOptionId) {
		this.intOptionId = intOptionId;
	}

	public int getIntOptionModId() {
		return intOptionModId;
	}

	public void setIntOptionModId(int intOptionModId) {
		this.intOptionModId = intOptionModId;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public OptionModResult() {
	}

	public OptionModResult(int intOptionModResultId, int intOptionId,
			int intOptionModId, int intState) {
		super();
		this.intOptionModResultId = intOptionModResultId;
		this.intOptionId = intOptionId;
		this.intOptionModId = intOptionModId;
		this.intState = intState;
	}
}
