package CSE564_Project_Spring2020.sim;

import java.util.Optional;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataListener;
import CSE564_Project_Spring2020.ui.DataType;

public class Gyroscope implements TimingAdjusted {
	private Optional<Degree> prevRoll, prevPitch, prevYaw;
	private Optional<World> world;
	
	private Optional<DataListener> gyroscopeStateListener;
	
	public Gyroscope() {
		prevRoll = Optional.empty();
		prevPitch = Optional.empty();
		prevYaw = Optional.empty();
		world = Optional.empty();
		
		gyroscopeStateListener = Optional.empty();
	}
	
	public void setWorld(World _world) {
		assert(_world != null);
		world = Optional.of(_world);
	}

	@Override
	public void adjustedTick() {
		assert(world.isPresent());
		refresh();
	}
	
	public Degree getRoll() {
		assert(prevRoll.isPresent());
		return prevRoll.get();
	}
	
	public Degree getPitch() {
		assert(prevPitch.isPresent());
		return prevPitch.get();
	}
	
	public Degree getYaw() {
		assert(prevYaw.isPresent());
		return prevYaw.get();
	}
	
	public void setGyroscopeStateListener(DataListener l) {
		assert(l != null);
		gyroscopeStateListener = Optional.of(l);
	}
	
	private void refresh() {
		Degree newRoll = world.get().getCurrentRoll();
		
		if (!prevRoll.isPresent() || !prevRoll.get().equals(newRoll)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroRoll, newRoll.toString()))
			);
		}
		prevRoll = Optional.of(newRoll.copy());

		Degree newPitch = world.get().getCurrentPitch();
		
		if (!prevPitch.isPresent() || !prevPitch.get().equals(newPitch)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroPitch, newPitch.toString()))
			);
		}
		prevPitch = Optional.of(newPitch.copy());
		
		Degree newYaw = world.get().getCurrentYaw();
		
		if (!prevYaw.isPresent() || !prevYaw.get().equals(newYaw)) {
			gyroscopeStateListener.ifPresent(
				(DataListener l) -> l.dataChanged(new DataChangeEvent(DataType.GyroYaw, newYaw.toString()))
			);
		}
		prevYaw = Optional.of(newYaw.copy());
	}
}
