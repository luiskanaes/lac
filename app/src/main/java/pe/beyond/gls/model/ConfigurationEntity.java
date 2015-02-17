package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="table")
public class ConfigurationEntity {	
	@Attribute(name="name")
	public String name;
	
	@ElementList(inline=true)
	public List<Generic> entities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Generic> getEntities() {
		return entities;
	}

	public void setEntities(List<Generic> entities) {
		this.entities = entities;
	}
	
}
