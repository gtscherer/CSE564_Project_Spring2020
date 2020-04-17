package CSE564_Project_Spring2020.sim;

import java.util.List;
import java.util.Optional;

public class WorldEventManager implements ClockedComponent {
	private Optional<World> world;
	private AxisEventManager rollEventManager, pitchEventManager, yawEventManager;
	
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
		
		Degree roll = aggregateEventData(rollEventManager.getActiveEvents());
		Degree pitch = aggregateEventData(pitchEventManager.getActiveEvents());
		Degree yaw = aggregateEventData(yawEventManager.getActiveEvents());
		
		if (!roll.isZero()) {
			world.get().rollChanged(roll);
		}
		
		if (!pitch.isZero()) {
			world.get().pitchChanged(pitch);
		}
		
		if (!yaw.isZero()) {
			world.get().yawChanged(yaw);
		}
	}
	
	public void addEvent(int startTime, int duration, Degree d_roll, Degree d_pitch, Degree d_yaw) {
		rollEventManager.addEvent(startTime, duration, d_roll);
		pitchEventManager.addEvent(startTime, duration, d_pitch);
		yawEventManager.addEvent(startTime, duration, d_yaw);
	}
	
	private Degree aggregateEventData(List<AxisEvent> events) {
		Optional<Degree> aggregate = events.stream()
				.map((AxisEvent e) -> e.d_deg)
				.reduce((Degree lhs, Degree rhs) -> lhs.plus(rhs));
		return aggregate.orElseGet(() -> new Degree());
	}
}
