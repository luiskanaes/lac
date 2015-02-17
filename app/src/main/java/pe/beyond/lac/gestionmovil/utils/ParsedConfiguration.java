package pe.beyond.lac.gestionmovil.utils;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import pe.beyond.gls.model.ConfigurationEntity;


@Root(name="configurationXML")
public class ParsedConfiguration {
	@ElementList(inline=true)
	public List<ConfigurationEntity> tables;

	public List<ConfigurationEntity> getTables() {
		return tables;
	}

	public void setTables(List<ConfigurationEntity> tables) {
		this.tables = tables;
	}
	
}
