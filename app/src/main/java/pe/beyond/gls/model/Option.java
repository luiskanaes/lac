package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "register", strict = false)
public class Option extends Generic implements Parcelable  {
	@Element(name = "oid", required = false)
	private int intOptionId;
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "abr", required = false)
	private String strOptionAbreviation;
	@Element(name = "rea", required = false)
	private String strOptionReal;
	@Element(name = "des", required = false)
	private String strDescription;
	@Element(name = "key", required = false)
	private int intKey;
	@Element(name = "sta", required = false)
	private int intState;
	@Element(name = "imp", required = false)
	private boolean booFlgImpossibility;

	public int getIntOptionId() {
		return intOptionId;
	}

	public void setIntOptionId(int intOptionId) {
		this.intOptionId = intOptionId;
	}

	public String getStrOptionAbreviation() {
		return strOptionAbreviation;
	}

	public void setStrOptionAbreviation(String strOptionAbreviation) {
		this.strOptionAbreviation = strOptionAbreviation;
	}

	public String getStrOptionReal() {
		return strOptionReal;
	}

	public void setStrOptionReal(String strOptionReal) {
		this.strOptionReal = strOptionReal;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public int getIntKey() {
		return intKey;
	}

	public void setIntKey(int intKey) {
		this.intKey = intKey;
	}

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public boolean isBooFlgImpossibility() {
		return booFlgImpossibility;
	}

	public void setBooFlgImpossibility(boolean booFlgImpossibility) {
		this.booFlgImpossibility = booFlgImpossibility;
	}

	public Option() {
	}

	public Option(int intOptionId, int intQuestionId,
			String strOptionAbreviation, String strOptionReal,
			String strDescription, int intKey, int intState,
			boolean booFlgImpossibility) {
		super();
		this.intOptionId = intOptionId;
		this.intQuestionId = intQuestionId;
		this.strOptionAbreviation = strOptionAbreviation;
		this.strOptionReal = strOptionReal;
		this.strDescription = strDescription;
		this.intKey = intKey;
		this.intState = intState;
		this.booFlgImpossibility = booFlgImpossibility;
	}
	
	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(intOptionId);
		dest.writeInt(intQuestionId);
		dest.writeString(strOptionAbreviation);
		dest.writeString(strOptionReal);
		dest.writeString(strDescription);
		dest.writeInt(intKey);
		dest.writeInt(intState);
		dest.writeByte((byte) (booFlgImpossibility ? 1 : 0));
	}
	
	public Option(Parcel source) {
		this.intOptionId = source.readInt();		
		this.intQuestionId = source.readInt();		
		this.strOptionAbreviation = source.readString();		
		this.strOptionReal = source.readString();		
		this.strDescription = source.readString();	
		this.intKey = source.readInt();
		this.intState = source.readInt();
		this.booFlgImpossibility = source.readByte() == 1;
	}

	public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
		public Option createFromParcel(Parcel in) {
			return new Option(in);
		}

		public Option[] newArray(int size) {
			return new Option[size];
		}
	};
}
