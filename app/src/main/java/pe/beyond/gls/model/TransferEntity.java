package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="TransferEntity")
public class TransferEntity {
	@ElementList(inline=true)
	private List<Register> lstRegisters;

	public List<Register> getLstRegisters() {
		return lstRegisters;
	}
	public void setLstRegisters(List<Register> lstRegisters) {
		this.lstRegisters = lstRegisters;
	}	
} 
