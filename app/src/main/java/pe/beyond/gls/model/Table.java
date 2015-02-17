package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class Table extends Generic {
	@Element(name = "tid", required = false)
	private String strTableId;
	@Element(name = "tab", required = false)
	private String strAbrev;

	public String getStrTableId() {
		return strTableId;
	}

	public void setStrTableId(String strTableId) {
		this.strTableId = strTableId;
	}

	public String getStrAbrev() {
		return strAbrev;
	}

	public void setStrAbrev(String strAbrev) {
		this.strAbrev = strAbrev;
	}

	public Table() {
	}

	public Table(String strTableId, String strAbrev) {
		super();
		this.strTableId = strTableId;
		this.strAbrev = strAbrev;
	}
}
