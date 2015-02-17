package pe.beyond.gls.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root(name = "register")
public class Listing extends Generic {
	@Element(name = "lid")
	private int intListId;
	@Element(name = "nde", required = false)
	private int intDownloadCounter;
	@Element(name = "fid")
	private int intFluxId;
	@Element(name = "com")
	private String strCommercialManagmentCode;
	@Element(name = "sta")
	private long lngStartDate;
	@Element(name = "end")
	private long lngEndDate;
	@Element(name = "uid")
	private int intUserId;
	@Element(name = "lat")
	private float fltLatitude;
	@Element(name = "lon")
	private float fltLongitude;
	@Element(name = "ord")
	private int intOrderNumber;
	@Element(name = "vis")
	private int intVisitNumber;
	@Element(name = "ste")
	private int intState;
	@Element(required = false)
	private String strField1;
	@Element(required = false)
	private String strField2;
	@Element(required = false)
	private String strField3;
	@Element(required = false)
	private String strField4;
	@Element(required = false)
	private String strField5;
	@Element(required = false)
	private String strField6;
	@Element(required = false)
	private String strField7;
	@Element(required = false)
	private String strField8;
	@Element(required = false)
	private String strField9;
	@Element(required = false)
	private String strField10;
	@Element(required = false)
	private String strValidation1;
	@Element(required = false)
	private String strValidation2;
	@Element(required = false)
	private String strValidation3;
	@Element(required = false)
	private String strValidation4;
	@Element(required = false)
	private String strValidation5;
	@Element(required = false)
	private String strValidation6;
	@Element(required = false)
	private String strValidation7;
	@Element(required = false)
	private String strValidation8;
	@Element(required = false)
	private String strValidation9;
	@Element(required = false)
	private String strValidation10;
	@Element(required = false)
	private String strValidation11;
	@Element(required = false)
	private String strValidation12;
	@Element(required = false)
	private String strValidation13;
	@Element(required = false)
	private String strValidation14;
	@Element(required = false)
	private String strValidation15;
	@Element(required = false)
	private String strValidation16;
	@Element(required = false)
	private String strValidation17;
	@Element(required = false)
	private String strValidation18;
	@Element(required = false)
	private String strValidation19;
	@Element(required = false)
	private String strValidation20;

	public int getIntListId() {
		return intListId;
	}

	public void setIntListId(int intListId) {
		this.intListId = intListId;
	}

	public int getIntDownloadCounter() {
		return intDownloadCounter;
	}

	public void setIntDownloadCounter(int intDownloadCounter) {
		this.intDownloadCounter = intDownloadCounter;
	}

	public int getIntFluxId() {
		return intFluxId;
	}

	public void setIntFluxId(int intFluxId) {
		this.intFluxId = intFluxId;
	}

	public String getStrCommercialManagmentCode() {
		return strCommercialManagmentCode;
	}

	public void setStrCommercialManagmentCode(String strCommercialManagmentCode) {
		this.strCommercialManagmentCode = strCommercialManagmentCode;
	}

	public long getLngStartDate() {
		return lngStartDate;
	}

	public void setLngStartDate(long lngStartDate) {
		this.lngStartDate = lngStartDate;
	}

	public long getLngEndDate() {
		return lngEndDate;
	}

	public void setLngEndDate(long lngEndDate) {
		this.lngEndDate = lngEndDate;
	}

	public int getIntUserId() {
		return intUserId;
	}

	public void setIntUserId(int intUserId) {
		this.intUserId = intUserId;
	}

	public float getFltLatitude() {
		return fltLatitude;
	}

	public void setFltLatitude(float fltLatitude) {
		this.fltLatitude = fltLatitude;
	}

	public float getFltLongitude() {
		return fltLongitude;
	}

	public void setFltLongitude(float fltLongitude) {
		this.fltLongitude = fltLongitude;
	}

	public int getIntOrderNumber() {
		return intOrderNumber;
	}

	public void setIntOrderNumber(int intOrderNumber) {
		this.intOrderNumber = intOrderNumber;
	}

	public int getIntVisitNumber() {
		return intVisitNumber;
	}

	public void setIntVisitNumber(int intVisitNumber) {
		this.intVisitNumber = intVisitNumber;
	}

	public int getIntState() {
		return intState;
	}

	public void setIntState(int intState) {
		this.intState = intState;
	}
	
	public String getStrField1() {
		return strField1;
	}

	public void setStrField1(String strField1) {
		this.strField1 = strField1;
	}

	public String getStrField2() {
		return strField2;
	}

	public void setStrField2(String strField2) {
		this.strField2 = strField2;
	}

	public String getStrField3() {
		return strField3;
	}

	public void setStrField3(String strField3) {
		this.strField3 = strField3;
	}

	public String getStrField4() {
		return strField4;
	}

	public void setStrField4(String strField4) {
		this.strField4 = strField4;
	}

	public String getStrField5() {
		return strField5;
	}

	public void setStrField5(String strField5) {
		this.strField5 = strField5;
	}

	public String getStrField6() {
		return strField6;
	}

	public void setStrField6(String strField6) {
		this.strField6 = strField6;
	}

	public String getStrField7() {
		return strField7;
	}

	public void setStrField7(String strField7) {
		this.strField7 = strField7;
	}

	public String getStrField8() {
		return strField8;
	}

	public void setStrField8(String strField8) {
		this.strField8 = strField8;
	}

	public String getStrField9() {
		return strField9;
	}

	public void setStrField9(String strField9) {
		this.strField9 = strField9;
	}

	public String getStrField10() {
		return strField10;
	}

	public void setStrField10(String strField10) {
		this.strField10 = strField10;
	}

	public String getStrValidation1() {
		return strValidation1;
	}

	public void setStrValidation1(String strValidation1) {
		this.strValidation1 = strValidation1;
	}

	public String getStrValidation2() {
		return strValidation2;
	}

	public void setStrValidation2(String strValidation2) {
		this.strValidation2 = strValidation2;
	}

	public String getStrValidation3() {
		return strValidation3;
	}

	public void setStrValidation3(String strValidation3) {
		this.strValidation3 = strValidation3;
	}

	public String getStrValidation4() {
		return strValidation4;
	}

	public void setStrValidation4(String strValidation4) {
		this.strValidation4 = strValidation4;
	}

	public String getStrValidation5() {
		return strValidation5;
	}

	public void setStrValidation5(String strValidation5) {
		this.strValidation5 = strValidation5;
	}

	public String getStrValidation6() {
		return strValidation6;
	}

	public void setStrValidation6(String strValidation6) {
		this.strValidation6 = strValidation6;
	}

	public String getStrValidation7() {
		return strValidation7;
	}

	public void setStrValidation7(String strValidation7) {
		this.strValidation7 = strValidation7;
	}

	public String getStrValidation8() {
		return strValidation8;
	}

	public void setStrValidation8(String strValidation8) {
		this.strValidation8 = strValidation8;
	}

	public String getStrValidation9() {
		return strValidation9;
	}

	public void setStrValidation9(String strValidation9) {
		this.strValidation9 = strValidation9;
	}

	public String getStrValidation10() {
		return strValidation10;
	}

	public void setStrValidation10(String strValidation10) {
		this.strValidation10 = strValidation10;
	}

	public String getStrValidation11() {
		return strValidation11;
	}

	public void setStrValidation11(String strValidation11) {
		this.strValidation11 = strValidation11;
	}

	public String getStrValidation12() {
		return strValidation12;
	}

	public void setStrValidation12(String strValidation12) {
		this.strValidation12 = strValidation12;
	}

	public String getStrValidation13() {
		return strValidation13;
	}

	public void setStrValidation13(String strValidation13) {
		this.strValidation13 = strValidation13;
	}

	public String getStrValidation14() {
		return strValidation14;
	}

	public void setStrValidation14(String strValidation14) {
		this.strValidation14 = strValidation14;
	}

	public String getStrValidation15() {
		return strValidation15;
	}

	public void setStrValidation15(String strValidation15) {
		this.strValidation15 = strValidation15;
	}

	public String getStrValidation16() {
		return strValidation16;
	}

	public void setStrValidation16(String strValidation16) {
		this.strValidation16 = strValidation16;
	}

	public String getStrValidation17() {
		return strValidation17;
	}

	public void setStrValidation17(String strValidation17) {
		this.strValidation17 = strValidation17;
	}

	public String getStrValidation18() {
		return strValidation18;
	}

	public void setStrValidation18(String strValidation18) {
		this.strValidation18 = strValidation18;
	}

	public String getStrValidation19() {
		return strValidation19;
	}

	public void setStrValidation19(String strValidation19) {
		this.strValidation19 = strValidation19;
	}

	public String getStrValidation20() {
		return strValidation20;
	}

	public void setStrValidation20(String strValidation20) {
		this.strValidation20 = strValidation20;
	}

	public Listing() {
	}

	public Listing(int intListId, int intDownloadCounter, String strCommercialManagmentCode,
			int intFluxId, long lngStartDate, long lngEndDate, int intUserId,
			float fltLatitude, float fltLongitude, int intOrderNumber,
			int intVisitNumber, int intState, String strField1,
			String strField2, String strField3, String strField4,
			String strField5, String strField6, String strField7,
			String strField8, String strField9, String strField10,
			String strValidation1, String strValidation2,
			String strValidation3, String strValidation4,
			String strValidation5, String strValidation6,
			String strValidation7, String strValidation8,
			String strValidation9, String strValidation10,
			String strValidation11, String strValidation12,
			String strValidation13, String strValidation14,
			String strValidation15, String strValidation16,
			String strValidation17, String strValidation18,
			String strValidation19, String strValidation20) {
		super();
		this.intListId = intListId;
		this.intDownloadCounter = intDownloadCounter;
		this.strCommercialManagmentCode = strCommercialManagmentCode;
		this.intFluxId = intFluxId;
		this.lngStartDate = lngStartDate;
		this.lngEndDate = lngEndDate;
		this.intUserId = intUserId;
		this.fltLatitude = fltLatitude;
		this.fltLongitude = fltLongitude;
		this.intOrderNumber = intOrderNumber;
		this.intVisitNumber = intVisitNumber;
		this.intState = intState;
		lstField = new ArrayList<String>();
		lstField.add(strField1);
		lstField.add(strField2);
		lstField.add(strField3);
		lstField.add(strField4);
		lstField.add(strField5);
		lstField.add(strField6);
		lstField.add(strField7);
		lstField.add(strField8);
		lstField.add(strField9);
		lstField.add(strField10);
		lstValidations = new ArrayList<String>();
		lstValidations.add(strValidation1);
		lstValidations.add(strValidation2);
		lstValidations.add(strValidation3);
		lstValidations.add(strValidation4);
		lstValidations.add(strValidation5);
		lstValidations.add(strValidation6);
		lstValidations.add(strValidation7);
		lstValidations.add(strValidation8);
		lstValidations.add(strValidation9);
		lstValidations.add(strValidation10);
		lstValidations.add(strValidation11);
		lstValidations.add(strValidation12);
		lstValidations.add(strValidation13);
		lstValidations.add(strValidation14);
		lstValidations.add(strValidation15);
		lstValidations.add(strValidation16);
		lstValidations.add(strValidation17);
		lstValidations.add(strValidation18);
		lstValidations.add(strValidation19);
		lstValidations.add(strValidation20);

		this.strField1 = strField1;
		this.strField2 = strField2;
		this.strField3 = strField3;
		this.strField4 = strField4;
		this.strField5 = strField5;
		this.strField6 = strField6;
		this.strField7 = strField7;
		this.strField8 = strField8;
		this.strField9 = strField9;
		this.strField10 = strField10;
		this.strValidation1 = strValidation1;
		this.strValidation2 = strValidation2;
		this.strValidation3 = strValidation3;
		this.strValidation4 = strValidation4;
		this.strValidation5 = strValidation5;
		this.strValidation6 = strValidation6;
		this.strValidation7 = strValidation7;
		this.strValidation8 = strValidation8;
		this.strValidation9 = strValidation9;
		this.strValidation10 = strValidation10;
		this.strValidation11 = strValidation11;
		this.strValidation12 = strValidation12;
		this.strValidation13 = strValidation13;
		this.strValidation14 = strValidation14;
		this.strValidation15 = strValidation15;
		this.strValidation16 = strValidation16;
		this.strValidation17 = strValidation17;
		this.strValidation18 = strValidation18;
		this.strValidation19 = strValidation19;
		this.strValidation20 = strValidation20;
	}

	@Transient
	private List<String> lstField;

	@Transient
	private List<String> lstValidations;

	public List<String> getLstField() {
		return lstField;
	}

	public void setLstField(List<String> lstField) {
		this.lstField = lstField;
	}

	public List<String> getLstValidations() {
		return lstValidations;
	}

	public void setLstValidations(List<String> lstValidations) {
		this.lstValidations = lstValidations;
	}
}
