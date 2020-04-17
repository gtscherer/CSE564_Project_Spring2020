package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class Actuator implements TimingAdjusted {
	private Optional<Double> lastDeltaDeg;
	private Optional<World> world;
	private final RotationAxis direction;
	
	public Actuator(RotationAxis _direction) {
		assert(_direction != null);

		lastDeltaDeg = Optional.empty();
		world = Optional.empty();
		direction = _direction;
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = Optional.of(_world);
	}
	
	public void rotate(Double amount) {
		assert(amount != null);
		assert(!amount.isInfinite());
		assert(!amount.isNaN());
		lastDeltaDeg = Optional.of(amount);
	}

	@Override
	public void adjustedTick() {
		assert(world.isPresent());

		lastDeltaDeg.ifPresent((Double amount) -> {
			final World w = world.get();

			if (direction == RotationAxis.ROLL) {
				w.rollChanged(amount);
			}
			else if (direction == RotationAxis.PITCH) {
				w.pitchChanged(amount);
			}
			else if (direction == RotationAxis.YAW) {
				w.yawChanged(amount);
			}
		});
	}
}
