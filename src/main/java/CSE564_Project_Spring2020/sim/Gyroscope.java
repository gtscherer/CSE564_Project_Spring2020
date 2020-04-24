package CSE564_Project_Spring2020.sim;

import java.util.Optional;

/**
 * The type Gyroscope.
 */
public class Gyroscope implements ClockedComponent {
	private Degree prevRoll, prevPitch, prevYaw;
	private World world;
	
	private Optional<DataListener> gyroscopeStateListener;

	/**
	 * Instantiates a new Gyroscope.
	 */
	public Gyroscope() {
		prevRoll = new Degree();
		prevPitch = new Degree();
		prevYaw = new Degree();
		world = null;
		
		gyroscopeStateListener = Optional.empty();
	}

	/**
	 * Sets world.
	 *
	 * @param _world the world
	 */
	public void setWorld(World _world) {
		assert(_world != null);
		world = _world;
	}

	@Override
	public void tick() {
		assert(world != null);
		refresh();
	}

	/**
	 * Gets roll.
	 *
	 * @return the roll
	 */
	public Degree getRoll() {
		return prevRoll;
	}

	/**
	 * Gets pitch.
	 *
	 * @return the pitch
	 */
	public Degree getPitch() {
		return prevPitch;
	}

	/**
	 * Gets yaw.
	 *
	 * @return the yaw
	 */
	public Degree getYaw() {
		return prevYaw;
	}

	/**
	 * Sets gyroscope state listener.
	 *
	 * @param l the l
	 */
	public void setGyroscopeStateListener(DataListener l) {
		assert(l != null);
		gyroscopeStateListener = Optional.of(l);
	}
	
	private void refresh() {
		Degree newRoll = world.getCurrentRoll();
		
		if (!prevRoll.equals(newRoll)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroRoll, newRoll.toString()))
			);
		}
		prevRoll = newRoll.copy();

		Degree newPitch = world.getCurrentPitch();
		
		if (!prevPitch.equals(newPitch)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroPitch, newPitch.toString()))
			);
		}
		prevPitch = newPitch.copy();
		
		Degree newYaw = world.getCurrentYaw();
		
		if (!prevYaw.equals(newYaw)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroYaw, newYaw.toString()))
			);
		}
		prevYaw = newYaw.copy();
	}
}
