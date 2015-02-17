package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "user", strict = false)
public class User {
	@Element(name = "uid", required = false)
	private int intUserId;
	@Element(name = "nam", required = false)
	private String strName;
	@Element(name = "fla", required = false)
	private String strFLastName;
	@Element(name = "mla", required = false)
	private String strMLastName;
	@Element(name = "pho", required = false)
	private String strPhoneNumber;
	@Element(name = "sta", required = false)
	private int intState;
	@Element(name = "log", required = false)
	private String strLogin;
	@Element(name = "pas", required = false)
	private String strPassword;
	@Element(name = "sex", required = false)
	private boolean booFlgSex;
	@Element(name = "pid", required = false)
	private int intProfileId;
	
	public int getIntUserId() {
		return intUserId;
	}

	public void setIntUserId(int intUserId) {
		this.intUserId = intUserId;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrFLastName() {
		return strFLastName;
	}

	public void setStrFLastName(String strFLastName) {
		this.strFLastName = strFLastName;
	}

	public String getStrMLastName() {
		return strMLastName;
	}

	public void setStrMLastName(String strMLastName) {
		this.strMLastName = strMLastName;
	}

	public String getStrPhoneNumber() {
		return strPhoneNumber;
	}

	public void setStrPhoneNumber(String strPhoneNumber) {
		this.strPhoneNumber = strPhoneNumber;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public String getStrLogin() {
		return strLogin;
	}

	public void setStrLogin(String strLogin) {
		this.strLogin = strLogin;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public boolean isBooFlgSex() {
		return booFlgSex;
	}

	public void setBooFlgSex(boolean booFlgSex) {
		this.booFlgSex = booFlgSex;
	}

	public int getIntProfileId() {
		return intProfileId;
	}

	public void setIntProfileId(int intProfileId) {
		this.intProfileId = intProfileId;
	}

	public User() {
	}

	public User(int intUserId, String strName, String strFLastName,
			String strMLastName, String strPhoneNumber, int intState,
			String strLogin, String strPassword, boolean booFlgSex,
			int intProfileId) {
		super();
		this.intUserId = intUserId;
		this.strName = strName;
		this.strFLastName = strFLastName;
		this.strMLastName = strMLastName;
		this.strPhoneNumber = strPhoneNumber;
		this.intState = intState;
		this.strLogin = strLogin;
		this.strPassword = strPassword;
		this.booFlgSex = booFlgSex;
		this.intProfileId = intProfileId;
	}

	@Element(name = "tkn", required = false)
	private String strToken;

	public String getStrToken() {
		return strToken;
	}

	public void setStrToken(String strToken) {
		this.strToken = strToken;
	}
}
