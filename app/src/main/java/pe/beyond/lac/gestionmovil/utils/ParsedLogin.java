package pe.beyond.lac.gestionmovil.utils;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import pe.beyond.gls.model.User;
import pe.beyond.gls.model.UserAssigned;

@Root(name="loginXML")
public class ParsedLogin {
	@Element(name="user")
	public User objUser;
	
	@ElementList(name = "userassigned", inline = true)
	private List<UserAssigned> lstAssignedUser;
	
	public User getObjUser() {
		return objUser;
	}
	public void setObjUser(User objUser) {
		this.objUser = objUser;
	}
	public List<UserAssigned> getLstAssignedUser() {
		return lstAssignedUser;
	}

	public void setLstAssignedUser(List<UserAssigned> lstAssignedUser) {
		this.lstAssignedUser = lstAssignedUser;
	}

	public ParsedLogin(){}
	public ParsedLogin(User objUser) {
		super();
		this.objUser = objUser;
	}
	
}