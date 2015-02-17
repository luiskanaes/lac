package pe.beyond.gls.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo extends Generic implements Parcelable {
	private int intId;
	private String strAbrevName;
	private String strFullName;
	private boolean booIsSent;
	private int intSendedPhotos;
	private int intNotSendedPhotos;
	private int intModule;

	// Photos

	public int getIntSendedPhotos() {
		return intSendedPhotos;
	}

	public void setIntSendedPhotos(int intSendedPhotos) {
		this.intSendedPhotos = intSendedPhotos;
	}

	public int getIntNotSendedPhotos() {
		return intNotSendedPhotos;
	}

	public void setIntNotSendedPhotos(int intNotSendedPhotos) {
		this.intNotSendedPhotos = intNotSendedPhotos;
	}
			
	public int getintModule() {
		return intModule;
	}

	public void setintModule(int intModule) {
		this.intModule = intModule;
	}

	public int getIntId() {
		return intId;
	}

	public void setIntId(int intId) {
		this.intId = intId;
	}

	public String getStrAbrevName() {
		return strAbrevName;
	}

	public void setStrAbrevName(String strAbrevName) {
		this.strAbrevName = strAbrevName;
	}

	public String getStrFullName() {
		return strFullName;
	}

	public void setStrFullName(String strFullName) {
		this.strFullName = strFullName;
	}

	public boolean isBooIsSent() {
		return booIsSent;
	}

	public void setBooIsSent(boolean booIsSent) {
		this.booIsSent = booIsSent;
	}

	public Photo() {
	}

 	public Photo(int intId, String strAbrevName, String strFullName, boolean booIsSent, int intModule) {
		super();
		this.intId = intId;
		this.strAbrevName = strAbrevName;
		this.strFullName = strFullName;
		this.booIsSent = booIsSent;
		this.intModule = intModule;
	} 
	
	  public Photo(int intId, String strAbrevName, String strFullName, boolean booIsSent) {
		super();
		this.intId = intId;
		this.strAbrevName = strAbrevName;
		this.strFullName = strFullName;
		this.booIsSent = booIsSent;
	}  

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(strAbrevName);
		dest.writeString(strFullName);
		dest.writeByte((byte) (booIsSent ? 1 : 0));
	}

	public Photo(Parcel source) {
		this.strAbrevName = source.readString();
		this.strFullName = source.readString();
		this.booIsSent = source.readByte() == 1;
	  // this.intModule = source.readInt();
	}

	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};
	
}