package CSE564_Project_Spring2020.sim;

public class AxisEvent {
	public int startTime, duration;
	private double d_deg;
	
	public AxisEvent() {
		this(0, 0, 0.0d);
	}
	
	public AxisEvent(int _startTime, int _duration, double _d_deg) {
		startTime = _startTime;
		duration = _duration;
		setDeltaDegrees(_d_deg);
	}
	
	public void setDeltaDegrees(double _d_deg) {
		assert(!Double.isInfinite(_d_deg));
		assert(!Double.isNaN(_d_deg));
		d_deg = _d_deg;
	}
	
	public double getDeltaDegrees() {
		return d_deg;
	}
}
