package CSE564_Project_Spring2020.sim;

public class AxisEvent {
	public int startTime, duration;
	public Degree d_deg;
	
	public AxisEvent() {
		this(0, 0, new Degree());
	}
	
	public AxisEvent(int _startTime, int _duration, Degree _d_deg) {
		startTime = _startTime;
		duration = _duration;
		d_deg = _d_deg;
	}
}
