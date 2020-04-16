package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class Actuator implements TimingAdjusted {
	private Optional<Degree> lastDeltaDeg;
	private Optional<World> world;
	private Optional<RotationDirection> direction;
	
	public Actuator() {
		lastDeltaDeg = Optional.empty();
		world = Optional.empty();
		direction = Optional.empty();
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = Optional.of(_world);
	}
	
	public void setDirection(RotationDirection _direction) {
		assert(_direction != null);
		direction = Optional.of(_direction);
	}
	
	public void rotate(Degree amount) {
		assert(amount != null);
		lastDeltaDeg = Optional.of(amount);
	}

	@Override
	public void simTick() {
		assert(world.isPresent());
		assert(direction.isPresent());

		lastDeltaDeg.ifPresent((Degree amount) -> {
			final RotationDirection axis = direction.get();
			final World w = world.get();

			if (axis == RotationDirection.ROLL) {
				w.rollChanged(amount);
			}
			else if (axis == RotationDirection.PITCH) {
				w.pitchChanged(amount);
			}
			else if (axis == RotationDirection.YAW) {
				w.yawChanged(amount);
			}
		});
	}
}
