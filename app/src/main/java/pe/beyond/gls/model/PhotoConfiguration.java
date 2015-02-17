package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register")
public class PhotoConfiguration extends Generic {
	@Element(name = "pid", required = false)
	private int intPhotoConfigurationId;
	@Element(name = "hei", required = false)
	private int intHeight;
	@Element(name = "wid", required = false)
	private int intWidth;
	@Element(name = "des", required = false)
	private String strDescription;

	public int getIntPhotoConfigurationId() {
		return intPhotoConfigurationId;
	}

	public void setIntPhotoConfigurationId(int intPhotoConfigurationId) {
		this.intPhotoConfigurationId = intPhotoConfigurationId;
	}

	public int getIntWidth() {
		return intWidth;
	}

	public void setIntWidth(int intWidth) {
		this.intWidth = intWidth;
	}

	public int getIntHeight() {
		return intHeight;
	}

	public void setIntHeight(int intHeight) {
		this.intHeight = intHeight;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public PhotoConfiguration() {
	}

	public PhotoConfiguration(int intPhotoConfigurationId, int intHeight,
			int intWidth, String strDescription) {
		super();
		this.intPhotoConfigurationId = intPhotoConfigurationId;
		this.intHeight = intHeight;
		this.intWidth = intWidth;
		this.strDescription = strDescription;
	}

}
