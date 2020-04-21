package CSE564_Project_Spring2020.sim;

import java.util.List;
import java.util.Optional;

public class WorldEventManager implements ClockedComponent {
	private Optional<World> world;
	private final AxisEventManager rollEventManager;
	private final AxisEventManager pitchEventManager;
	private final AxisEventManager yawEventManager;
	
	public WorldEventManager() {
		world = Optional.empty();
		rollEventManager = new AxisEventManager();
		pitchEventManager = new AxisEventManager();
		yawEventManager = new AxisEventManager();
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = Optional.of(_world);
	}
	
	@Override
	public void tick() {
		assert(world.isPresent());
		
		rollEventManager.tick();
		pitchEventManager.tick();
		yawEventManager.tick();
		
		Double roll = aggregateEventData(rollEventManager.getActiveEvents());
		Double pitch = aggregateEventData(pitchEventManager.getActiveEvents());
		Double yaw = aggregateEventData(yawEventManager.getActiveEvents());
		
		if (roll != 0.0d) {
			world.get().rollChanged(roll);
		}
		
		if (pitch != 0.0d) {
			world.get().pitchChanged(pitch);
		}
		
		if (yaw != 0.0d) {
			world.get().yawChanged(yaw);
		}
	}
	
	public void addEvent(
		int startTime,
		int duration,
		double d_roll,
		double d_pitch,
		double d_yaw
	) throws AssertionError {
		rollEventManager.addEvent(startTime, duration, d_roll);
		pitchEventManager.addEvent(startTime, duration, d_pitch);
		yawEventManager.addEvent(startTime, duration, d_yaw);
	}
	
	private Double aggregateEventData(List<AxisEvent> events) {
		Optional<Double> aggregate = events.stream()
				.map(AxisEvent::getDeltaDegrees)
				.reduce(Double::sum);
		return aggregate.orElseGet(() -> 0.0d);
	}
}
