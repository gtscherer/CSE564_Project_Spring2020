package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class Actuator implements TimingAdjusted {
	private Optional<Degree> lastDeltaDeg;
	private Optional<World> world;
	private final RotationDirection direction;
	
	public Actuator(RotationDirection _direction) {
		assert(_direction != null);

		lastDeltaDeg = Optional.empty();
		world = Optional.empty();
		direction = _direction;
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = Optional.of(_world);
	}
	
	public void rotate(Degree amount) {
		assert(amount != null);
		lastDeltaDeg = Optional.of(amount);
	}

	@Override
	public void adjustedTick() {
		assert(world.isPresent());

		lastDeltaDeg.ifPresent((Degree amount) -> {
			final World w = world.get();

			if (direction == RotationDirection.ROLL) {
				w.rollChanged(amount);
			}
			else if (direction == RotationDirection.PITCH) {
				w.pitchChanged(amount);
			}
			else if (direction == RotationDirection.YAW) {
				w.yawChanged(amount);
			}
		});
	}
}
