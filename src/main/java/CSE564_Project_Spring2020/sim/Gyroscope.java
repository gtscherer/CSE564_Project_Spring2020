package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class Gyroscope implements TimingAdjusted {
	private Optional<Degree> prevRoll, prevPitch, prevYaw;
	private Optional<World> world;
	
	public Gyroscope() {
		prevRoll = Optional.empty();
		prevPitch = Optional.empty();
		prevYaw = Optional.empty();
		world = Optional.empty();
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
	
	private void refresh() {
		prevRoll = Optional.of(world.get().getCurrentRoll());
		prevPitch = Optional.of(world.get().getCurrentPitch());
		prevYaw = Optional.of(world.get().getCurrentYaw());
	}
}
