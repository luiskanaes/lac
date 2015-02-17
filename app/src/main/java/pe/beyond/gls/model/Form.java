package pe.beyond.gls.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "register")
public class Form extends Generic {
	@Element(name = "fid", required = false)
	private int intModuleId;
	@Element(name = "ord", required = false)
	private int intOrder;
	@Element(name = "vis", required = false)
	private boolean booFlgVisible;
	@Element(name = "nam", required = false)
	private String strName;
	@Element(name = "sta", required = false)
	private int intStartLength;
	@Element(name = "end", required = false)
	private int intEndLength;
	@Element(name = "sea", required = false)
	private int intFlgSearch;
	@Element(name = "ori", required = false)
	private boolean booFlgOrientation;
	@Element(name = "for", required = false)
	private boolean booFlgOrdering;
	@Element(name = "oen", required = false)
	private int intOrderEntry;
	@Element(name = "sel", required = false)
	private int intSearchLocation;
	@Element(name = "tip", required = false)
	private String strDataType;

	public int getIntModuleId() {
		return intModuleId;
	}

	public void setIntModuleId(int intModuleId) {
		this.intModuleId = intModuleId;
	}

	public int getIntOrder() {
		return intOrder;
	}

	public void setIntOrder(int intOrder) {
		this.intOrder = intOrder;
	}

	public boolean isBooFlgVisible() {
		return booFlgVisible;
	}

	public void setBooFlgVisible(boolean booFlgVisible) {
		this.booFlgVisible = booFlgVisible;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public int getIntStartLength() {
		return intStartLength;
	}

	public void setIntStartLength(int intStartLength) {
		this.intStartLength = intStartLength;
	}

	public int getIntEndLength() {
		return intEndLength;
	}

	public void setIntEndLength(int intEndLength) {
		this.intEndLength = intEndLength;
	}

	public int getIntFlgSearch() {
		return intFlgSearch;
	}

	public void setIntFlgSearch(int intFlgSearch) {
		this.intFlgSearch = intFlgSearch;
	}

	public boolean isBooFlgOrientation() {
		return booFlgOrientation;
	}

	public void setBooFlgOrientation(boolean booFlgOrientation) {
		this.booFlgOrientation = booFlgOrientation;
	}

	public boolean isBooFlgOrdering() {
		return booFlgOrdering;
	}

	public void setBooFlgOrdering(boolean booFlgOrdering) {
		this.booFlgOrdering = booFlgOrdering;
	}

	public int getIntOrderEntry() {
		return intOrderEntry;
	}

	public void setIntOrderEntry(int intOrderEntry) {
		this.intOrderEntry = intOrderEntry;
	}

	public int getIntSearchLocation() {
		return intSearchLocation;
	}

	public void setIntSearchLocation(int intSearchLocation) {
		this.intSearchLocation = intSearchLocation;
	}

	public String getStrDataType() {
		return strDataType;
	}

	public void setStrDataType(String strDataType) {
		this.strDataType = strDataType;
	}

	public Form() {
	}

	public Form(int intModuleId, int intOrder, boolean booFlgVisible,
			String strName, int intStartLength, int intEndLength,
			int intFlgSearch, boolean booFlgOrientation,
			boolean booFlgOrdering, int intOrderEntry, int intSearchLocation,
			String strDataType) {
		super();
		this.intModuleId = intModuleId;
		this.intOrder = intOrder;
		this.booFlgVisible = booFlgVisible;
		this.strName = strName;
		this.intStartLength = intStartLength;
		this.intEndLength = intEndLength;
		this.intFlgSearch = intFlgSearch;
		this.booFlgOrientation = booFlgOrientation;
		this.booFlgOrdering = booFlgOrdering;
		this.intOrderEntry = intOrderEntry;
		this.intSearchLocation = intSearchLocation;
		this.strDataType = strDataType;
	}
}
