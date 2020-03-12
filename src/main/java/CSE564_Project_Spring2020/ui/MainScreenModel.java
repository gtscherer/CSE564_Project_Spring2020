package CSE564_Project_Spring2020.ui;

public class MainScreenModel {
	static class AccelerationEventData {
		public double rollAcceleration;
		public double yawAcceleration;
		public double pitchAcceleration;
	}
	
	public final AccelerationEventData accelerationEventData = new AccelerationEventData();
}
