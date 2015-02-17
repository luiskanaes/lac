package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class Log extends Generic  {	
	@Element(name = "lid", required = false)
	private int intLogId;
	@Element(name = "uid", required = false)
	private String strUserId;
	@Element(name = "cid", required = false)
	private String strCellId;
	@Element(name = "eme", required = false)
	private String strErrorMessage;
	@Element(name = "ese", required = false)
	private int intErrorLine;
	@Element(name = "est", required = false)
	private String strErrorProcedure;
	@Element(name = "eli", required = false)
	private long lngDate;
	
	public int getIntLogId() {
		return intLogId;
	}
	public void setIntLogId(int intLogId) {
		this.intLogId = intLogId;
	}
	public String getStrUserId() {
		return strUserId;
	}
	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}
	
	public String getStrCellId() {
		return strCellId;
	}
	public void setStrCellId(String strCellId) {
		this.strCellId = strCellId;
	}
	public String getStrErrorMessage() {
		return strErrorMessage;
	}
	public void setStrErrorMessage(String strErrorMessage) {
		this.strErrorMessage = strErrorMessage;
	}
	public int getIntErrorLine() {
		return intErrorLine;
	}
	public void setIntErrorLine(int intErrorLine) {
		this.intErrorLine = intErrorLine;
	}
	public String getStrErrorProcedure() {
		return strErrorProcedure;
	}
	public void setStrErrorProcedure(String strErrorProcedure) {
		this.strErrorProcedure = strErrorProcedure;
	}
	public long getLngDate() {
		return lngDate;
	}
	public void setLngDate(long lngDate) {
		this.lngDate = lngDate;
	}
	public Log(int intLogId, String strUserId, String strCellId,
			String strErrorMessage, int intErrorLine, String strErrorProcedure,
			long lngDate) {
		super();
		this.intLogId = intLogId;
		this.strUserId = strUserId;
		this.strCellId = strCellId;
		this.strErrorMessage = strErrorMessage;
		this.intErrorLine = intErrorLine;
		this.strErrorProcedure = strErrorProcedure;
		this.lngDate = lngDate;
	}
}
