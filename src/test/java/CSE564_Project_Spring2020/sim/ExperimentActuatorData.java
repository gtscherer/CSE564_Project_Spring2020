package CSE564_Project_Spring2020.sim;

public class ExperimentActuatorData {
	public long time;
	public double roll;
	public double pitch;
	public double yaw;
	
	public ExperimentActuatorData() {
		this(0l, 0.0d, 0.0d, 0.0d);
	}
	
	public ExperimentActuatorData(long _time, double _roll, double _pitch, double _yaw) {
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
		if (rhs.getClass().equals(ExperimentActuatorData.class)) {
			ExperimentActuatorData rhsData = ((ExperimentActuatorData) rhs);
			final double epsilon = 0.0001d;
			return time == rhsData.time
					&& Math.abs(roll - rhsData.roll) < epsilon
					&& Math.abs(pitch - rhsData.pitch) < epsilon
					&& Math.abs(yaw - rhsData.yaw) < epsilon;
		}
		return false;
	}
}
