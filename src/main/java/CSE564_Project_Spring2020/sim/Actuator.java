package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class Actuator implements ClockedComponent {
	private Optional<Double> lastDeltaDeg;
	private World world;
	private final RotationAxis direction;
	
	private Optional<DataListener> actuatorListener;
	
	public Actuator(RotationAxis _direction) {
		assert(_direction != null);

		lastDeltaDeg = Optional.empty();
		world = null;
		direction = _direction;
		
		actuatorListener = Optional.empty();
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = _world;
	}
	
	public void rotate(Double amount) {
		assert(amount != null);
		assert(!amount.isInfinite());
		assert(!amount.isNaN());
		lastDeltaDeg = Optional.of(amount);
	}

	public void setActuatorListener(DataListener l) {
		assert(l != null);
		
		actuatorListener = Optional.of(l);
	}

	@Override
	public void tick() {
		assert(world != null);

		lastDeltaDeg.ifPresent((Double amount) -> {
			final String amountString = String.format("%.6f", amount);
			
			if (direction == RotationAxis.ROLL) {
				world.rollChanged(amount);
				
				actuatorListener.ifPresent(
					(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.ActuatorRoll, amountString))
				);
			}
			else if (direction == RotationAxis.PITCH) {
				world.pitchChanged(amount);
				
				actuatorListener.ifPresent(
					(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.ActuatorPitch, amountString))
				);
			}
			else if (direction == RotationAxis.YAW) {
				world.yawChanged(amount);
				
				actuatorListener.ifPresent(
					(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.ActuatorYaw, amountString))
				);
			}
		});
	}
}
