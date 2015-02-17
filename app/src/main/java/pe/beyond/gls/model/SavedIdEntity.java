package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="SavedIdEntity")
public class SavedIdEntity {
	@ElementList(inline=true)
	private List<String> lstIds;

	public List<String> getLstIds() {
		return lstIds;
	}

	public void setLstIds(List<String> lstIds) {
		this.lstIds = lstIds;
	}
} 
