package CSE564_Project_Spring2020.sim;

public class Degree {
	private double value;

	static final double epsilon = 0.0001d;
	
	public Degree() {
		value = 0.0d;
	}
	
	public Degree(double _value) {
		assert(!Double.isInfinite(_value));
		assert(!Double.isNaN(_value));
		value = _value;
		normalize();
	}
	
	public Degree plus(Degree _value) {
		return plus(_value.getValue());
	}
	
	public Degree plus(double _value) {
		return new Degree(value + _value);
	}

	public void add(Degree _value) {
		add(_value.getValue());
	}
	
	public void add(double _value) {
		value += _value;
		normalize();
	}
	
	public double getValue() {
		return value;
	}
	
	private void normalize() {
		if (value >= 360) {
			value %= 360;
		}
		else if (value < 0.0d) {
			value %= 360;
			
			if (value < 0.0d) {
				value += 360;
			}
			else if (value == -0.0d) {
				value = 0.0d;
			}
		}
	}
	
	public boolean isZero() {
		return Math.abs(value - 0.0d) < epsilon;
	}
	
	public Degree copy() {
		return new Degree(value);
	}
	
	@Override
	public boolean equals(Object rhs) {
		if (rhs.getClass() == Degree.class) {
			return Math.abs(((Degree)rhs).value - value) < epsilon;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("%.4f", value);
	}
}
