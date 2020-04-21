package CSE564_Project_Spring2020.sim;

public class DataChangeEvent {
	private final DataType type;
	private final String value;
	
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
