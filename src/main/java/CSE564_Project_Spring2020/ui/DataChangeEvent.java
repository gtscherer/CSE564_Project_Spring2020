package CSE564_Project_Spring2020.ui;

public class DataChangeEvent {
	private DataType type;
	private String value;
	
	public DataChangeEvent(DataType _type, String _value) {
		type = _type;
		value = _value;
	}
	
	public DataType getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
}
