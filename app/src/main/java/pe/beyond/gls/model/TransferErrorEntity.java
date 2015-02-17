package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="TransferErrorEntity")
public class TransferErrorEntity {
	@ElementList(inline=true)
	private List<Log> lstLogs;

	public List<Log> getLstLogs() {
		return lstLogs;
	}

	public void setLstLogs(List<Log> lstLogs) {
		this.lstLogs = lstLogs;
	}
} 
