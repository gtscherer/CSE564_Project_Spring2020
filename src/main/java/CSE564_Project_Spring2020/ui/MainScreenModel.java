package CSE564_Project_Spring2020.ui;

import java.util.Optional;

import CSE564_Project_Spring2020.sim.WorldEventManager;

public class MainScreenModel {
	static class AccelerationEventData {
		public int startTime;
		public int duration;
		public double rollAcceleration;
		public double pitchAcceleration;
		public double yawAcceleration;
		
		private Optional<WorldEventManager> worldEventManager;
		
		public AccelerationEventData() {
			startTime = 0;
			duration = 0;
			rollAcceleration = 0;
			yawAcceleration = 0;
			pitchAcceleration = 0;
			worldEventManager = Optional.empty();
		}
		
		public void registerWorldEventManager(WorldEventManager _worldEventManager) {
			assert(_worldEventManager != null);
			worldEventManager = Optional.of(_worldEventManager);
		}
		
		public void sendToEventManager() {
			worldEventManager.ifPresent(
				(WorldEventManager m) -> m.addEvent(
					startTime,
					duration,
					rollAcceleration,
					pitchAcceleration,
					yawAcceleration
				)
			);
		}
	}
	
	public final AccelerationEventData accelerationEventData = new AccelerationEventData();
}
