package pe.beyond.gls.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/*
 * ENTIDAD DE ENVIO DE TRACKING
 */
@Root(name = "TransferTrackEntity")
public class TransferTrackEntity {

	@ElementList(inline = true)
	private List<Track> _lstTrack;

	// @Element(name = "usr")
	// private String strUser;

	// public String getStrUser() {
	// return strUser;
	// }
	//
	// public void setStrUser(String strUser) {
	// this.strUser = strUser;
	// }

	public List<Track> getLstTrack() {
		return _lstTrack;
	}

	public void setLstTrack(List<Track> lstTrack) {
		this._lstTrack = lstTrack;
	}

	/**
	 * setTrackEntityUser => This method should be used when we already set a
	 * list on this object.
	 * 
	 * @param intIdUser
	 */
	public void setTrackEntityUser(int intIdUser) {
		for (Track objTrack : this._lstTrack) {
			objTrack.setIntIdUser(intIdUser);
		}
	}

	/**
	 * setTrackEntityIMEI => This method should be used when we already set a
	 * Track list on this class and when we're about send this trace to the
	 * server.
	 * 
	 * @param strIMEI
	 */
	public void setTrackEntityIMEI(String strIMEI) {
		for (Track objTrack : this._lstTrack) {
			objTrack.setStrIMEI(strIMEI);
		}
	}
	
	
	/**
	 * setTrackEntityVersion => This method should be used when we already set a Track list on this class
	 * and when we're about send this trace to the server.
	 * @param strVersion
	 */
	public void setTrackEntityVersion(String strVersion) {
		for (Track objTrack : this._lstTrack) {
			objTrack.setStrVersion(strVersion);
		}
	}

}
