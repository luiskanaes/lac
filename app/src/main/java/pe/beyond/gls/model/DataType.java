package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register", strict = false)
public class DataType extends Generic {
	@Element(name = "did", required = false)
	private int intDataTypeId;
	@Element(name = "des", required = false)
	private String strDescription;
	@Element(name = "min", required = false)
	private int intLongitudeMin;
	@Element(name = "max", required = false)
	private int intLongitudeMax;
	@Element(name = "dat", required = false)
	private String strData;
	@Element(name = "inv", required = false)
	private String strInvalidCharacters;
	@Element(name = "sta", required = false)
	private int intState;
	@Element(name = "for", required = false)
	private String strFormatSentence;
	@Element(name = "deq", required = false)
	private int intDecimalsQuantity;

	public int getIntDataTypeId() {
		return intDataTypeId;
	}

	public void setIntDataTypeId(int intDataTypeId) {
		this.intDataTypeId = intDataTypeId;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public int getIntLongitudeMin() {
		return intLongitudeMin;
	}

	public void setIntLongitudeMin(int intLongitudeMin) {
		this.intLongitudeMin = intLongitudeMin;
	}

	public int getIntLongitudeMax() {
		return intLongitudeMax;
	}

	public void setIntLongitudeMax(int intLongitudeMax) {
		this.intLongitudeMax = intLongitudeMax;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

	public String getStrInvalidCharacters() {
		return strInvalidCharacters;
	}

	public void setStrInvalidCharacters(String strInvalidCharacters) {
		this.strInvalidCharacters = strInvalidCharacters;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}

	public String getStrFormatSentence() {
		return strFormatSentence;
	}

	public void setStrFormatSentence(String strFormatSentence) {
		this.strFormatSentence = strFormatSentence;
	}

	public int getIntDecimalsQuantity() {
		return intDecimalsQuantity;
	}

	public void setIntDecimalsQuantity(int intDecimalsQuantity) {
		this.intDecimalsQuantity = intDecimalsQuantity;
	}

	public DataType() {
	}

	public DataType(int intDataTypeId, String strDescription,
			int intLongitudeMin, int intLongitudeMax, String strData,
			String strInvalidCharacters, int intState,
			String strFormatSentence, int intDecimalsQuantity) {
		super();
		this.intDataTypeId = intDataTypeId;
		this.strDescription = strDescription;
		this.intLongitudeMin = intLongitudeMin;
		this.intLongitudeMax = intLongitudeMax;
		this.strData = strData;
		this.strInvalidCharacters = strInvalidCharacters;
		this.intState = intState;
		this.strFormatSentence = strFormatSentence;
		this.intDecimalsQuantity = intDecimalsQuantity;
	}
}
