package pe.beyond.lac.gestionmovil.utils;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import pe.beyond.gls.model.Listing;

@Root(name="list")
public class ParsedInformation {
	@ElementList(inline=true)
	public List<Listing> listElement;

	public List<Listing> getListElement() {
		return listElement;
	}

	public void setListElement(List<Listing> listElement) {
		this.listElement = listElement;
	}

	
}
