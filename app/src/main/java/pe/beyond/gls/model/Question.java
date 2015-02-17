package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "register", strict = false)
public class Question extends Generic implements Parcelable {
	@Element(name = "qid", required = false)
	private int intQuestionId;
	@Element(name = "tid", required = false)
	private int intTemplateId;
	@Element(name = "fid", required = false)
	private int intFluxId;
	@Element(name = "did", required = false)
	private int intDataTypeId;
	@Element(name = "vid", required = false)
	private int intValidationId;
	@Element(name = "des", required = false)
	private String strQuestionDescription;
	@Element(name = "opt", required = false)
	private boolean booFlgOption;
	@Element(name = "edi", required = false)
	private boolean booFlgEdition;
	@Element(name = "key", required = false)
	private int intKey;
	@Element(name = "ord", required = false)
	private int intOrderNum;
	@Element(name = "sta", required = false)
	private int intState;
	@Element(name = "ent", required = false)
	private int intEntryType;
	@Element(name = "gro", required = false)
	private int intGroup;
	@Element(name = "pos", required = false)
	private int intPosition;

	public int getIntQuestionId() {
		return intQuestionId;
	}

	public void setIntQuestionId(int intQuestionId) {
		this.intQuestionId = intQuestionId;
	}

	public int getIntTemplateId() {
		return intTemplateId;
	}

	public void setIntTemplateId(int intTemplateId) {
		this.intTemplateId = intTemplateId;
	}

	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}

	public int getIntDataTypeId() {
		return intDataTypeId;
	}

	public void setIntDataTypeId(int intDataTypeId) {
		this.intDataTypeId = intDataTypeId;
	}

	public int getIntValidationId() {
		return intValidationId;
	}

	public void setIntValidationId(int intValidationId) {
		this.intValidationId = intValidationId;
	}

	public String getStrQuestionDescription() {
		return strQuestionDescription;
	}

	public void setStrQuestionDescription(String strQuestionDescription) {
		this.strQuestionDescription = strQuestionDescription;
	}

	public boolean isBooFlgOption() {
		return booFlgOption;
	}

	public void setBooFlgOption(boolean booFlgOption) {
		this.booFlgOption = booFlgOption;
	}

	public boolean isBooFlgEdition() {
		return booFlgEdition;
	}

	public void setBooFlgEdition(boolean booFlgEdition) {
		this.booFlgEdition = booFlgEdition;
	}

	public int getIntKey() {
		return intKey;
	}

	public void setIntKey(int intKey) {
		this.intKey = intKey;
	}

	public int getIntOrderNum() {
		return intOrderNum;
	}

	public void setIntOrderNum(int intOrderNum) {
		this.intOrderNum = intOrderNum;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public int getIntEntryType() {
		return intEntryType;
	}

	public void setIntEntryType(int intEntryType) {
		this.intEntryType = intEntryType;
	}

	public int getIntGroup() {
		return intGroup;
	}

	public void setIntGroup(int intGroup) {
		this.intGroup = intGroup;
	}

	public int getIntPosition() {
		return intPosition;
	}

	public void setIntPosition(int intPosition) {
		this.intPosition = intPosition;
	}

	public Question() {
	}

	public Question(int intQuestionId, int intTemplateId, int intFluxId,
			int intDataTypeId, int intValidationId,
			String strQuestionDescription, boolean booFlgOption,
			boolean booFlgEdition, int intKey, int intOrderNum, int intState,
			int intEntryType, int intGroup, int intPosition) {
		super();
		this.intQuestionId = intQuestionId;
		this.intTemplateId = intTemplateId;
		this.intFluxId = intFluxId;
		this.intDataTypeId = intDataTypeId;
		this.intValidationId = intValidationId;
		this.strQuestionDescription = strQuestionDescription;
		this.booFlgOption = booFlgOption;
		this.booFlgEdition = booFlgEdition;
		this.intKey = intKey;
		this.intOrderNum = intOrderNum;
		this.intState = intState;
		this.intEntryType = intEntryType;
		this.intGroup = intGroup;
		this.intPosition = intPosition;
	}

	private String strTemporalAbrevAnswer;
	
	private String strTemporalRealAnswer;

	private Object objTemporalExtraInfo;
	
	private boolean booIsUpdated;
	
	
	public String getStrTemporalAbrevAnswer() {
		return strTemporalAbrevAnswer;
	}

	public void setStrTemporalAbrevAnswer(String strTemporalAbrevAnswer) {
		this.strTemporalAbrevAnswer = strTemporalAbrevAnswer;
	}

	public String getStrTemporalRealAnswer() {
		return strTemporalRealAnswer;
	}

	public void setStrTemporalRealAnswer(String strTemporalRealAnswer) {
		this.strTemporalRealAnswer = strTemporalRealAnswer;
	}

	public Object getObjTemporalExtraInfo() {
		return objTemporalExtraInfo;
	}

	public void setObjTemporalExtraInfo(Object objTemporalExtraInfo) {
		this.objTemporalExtraInfo = objTemporalExtraInfo;
	}
	
	public boolean isBooIsUpdated() {
		return booIsUpdated;
	}

	public void setBooIsUpdated(boolean booIsUpdated) {
		this.booIsUpdated = booIsUpdated;
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(intQuestionId);
		dest.writeString(strQuestionDescription);
		dest.writeByte((byte) (booFlgOption ? 1 : 0));
		dest.writeByte((byte) (booFlgEdition ? 1 : 0));
		dest.writeInt(intKey);
		dest.writeInt(intOrderNum);
		dest.writeInt(intFluxId);
		dest.writeInt(intDataTypeId);
		dest.writeInt(intValidationId);
		dest.writeInt(intState);
		dest.writeInt(intEntryType);
		dest.writeInt(intGroup);
		dest.writeInt(intPosition);
		dest.writeInt(intTemplateId);
	}

	public Question(Parcel source) {
		this.intQuestionId = source.readInt();		
		this.strQuestionDescription = source.readString();		
		this.booFlgOption = source.readByte() == 1;
		this.booFlgEdition = source.readByte() == 1;
		this.intKey = source.readInt();		
		this.intOrderNum = source.readInt();		
		this.intFluxId = source.readInt();	
		this.intDataTypeId = source.readInt();
		this.intValidationId = source.readInt();
		this.intState = source.readInt();
		this.intEntryType = source.readInt();
		this.intGroup = source.readInt();
		this.intPosition = source.readInt();
		this.intTemplateId = source.readInt();
		
	}

	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
		public Question createFromParcel(Parcel in) {
			return new Question(in);
		}

		public Question[] newArray(int size) {
			return new Question[size];
		}
	};
	
	
}
