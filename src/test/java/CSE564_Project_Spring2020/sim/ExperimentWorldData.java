package CSE564_Project_Spring2020.sim;

public class ExperimentWorldData {
	public long time;
	public double roll;
	public double pitch;
	public double yaw;
	
	public ExperimentWorldData() {
		this(0, 0.0d, 0.0d, 0.0d);
	}
	
	public ExperimentWorldData(int _time, double _roll, double _pitch, double _yaw) {
		time = _time;
		roll = _roll;
		pitch = _pitch;
		yaw = _yaw;
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %f, %f, %f)", time, roll, pitch, yaw);
	}
	
	@Override
	public boolean equals(Object rhs) {
		if (rhs.getClass().equals(this.getClass())) {
			ExperimentWorldData rhsData = ((ExperimentWorldData) rhs);
			final double sigma = 0.0001d;
			return time == rhsData.time
					&& Math.abs(roll - rhsData.roll) < sigma
					&& Math.abs(pitch - rhsData.pitch) < sigma
					&& Math.abs(yaw - rhsData.yaw) < sigma;
		}
		return false;
	}
}
