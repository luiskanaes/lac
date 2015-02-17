package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class FluxValidation extends Generic {
	@Element(name = "fvi", required = false)
	private int intFluxValidationId;
	@Element(name = "fid", required = false)
	private int intFluxId;
	@Element(name = "vid", required = false)
	private int intValidationId;
	@Element(name = "val", required = false)
	private String strValue;

	public int getIntFluxValidationId() {
		return intFluxValidationId;
	}

	public void setIntFluxValidationId(int intFluxValidationId) {
		this.intFluxValidationId = intFluxValidationId;
	}

	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}

	public int getIntValidationId() {
		return intValidationId;
	}

	public void setIntValidationId(int intValidationId) {
		this.intValidationId = intValidationId;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public FluxValidation() {
	}

	public FluxValidation(int intFluxValidationId, int intFluxId,
			int intValidationId, String strValue) {
		super();
		this.intFluxValidationId = intFluxValidationId;
		this.intFluxId = intFluxId;
		this.intValidationId = intValidationId;
		this.strValue = strValue;
	}
}
