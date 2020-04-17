package CSE564_Project_Spring2020.sim;

import java.time.Clock;
import java.util.Optional;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataListener;
import CSE564_Project_Spring2020.ui.DataType;

/** 
 * 
 * Work in progress! Do not use yet until missing pieces are added.
 *
 */
public class Simulator extends Thread {
	private World world;
	private WorldEventManager worldEventManager;
	private boolean isWaiting;
	private long maxTicks;
	private Optional<ClockedComponent> controller;

	private Optional<DataListener> worldStateListener;
	
	private static final ClockedComponent DEFAULT_CONTROLLER = new ClockedComponent() {

		@Override
		public void tick() {
			//Default implementation
			//Do nothing
		}

	};
	
	public Simulator() {
		this(Long.MAX_VALUE);
	}
	
	public Simulator(long _maxTicks) {
		world = new World();
		worldEventManager = new WorldEventManager();
		worldStateListener = Optional.empty();
		isWaiting = false;
		maxTicks = _maxTicks;
		controller = Optional.empty();
	}
	
	public void setController(ClockedComponent _controller) {
		assert(_controller != null);
		controller = Optional.of(_controller);
	}
	
	public WorldEventManager getWorldEventManager() {
		return worldEventManager;
	}
	
	public void setWorldDataListener(DataListener l) {
		assert(l != null);
		worldStateListener = Optional.of(l);
		world.setStateListener(l);
	}
	
	@Override
	public void run() {
		Gyroscope gyroscope = new Gyroscope();
		Actuator rollActuator = new Actuator(RotationAxis.ROLL);
		Actuator pitchActuator = new Actuator(RotationAxis.PITCH);
		Actuator yawActuator = new Actuator(RotationAxis.YAW);
		
		TimingAdjuster gyroAdjuster = new TimingAdjuster();
		TimingAdjuster rollActuatorAdjuster = new TimingAdjuster();
		TimingAdjuster pitchActuatorAdjuster = new TimingAdjuster();
		TimingAdjuster yawActuatorAdjuster = new TimingAdjuster();
		
		gyroAdjuster.setAdjustedComponent(gyroscope);
		rollActuatorAdjuster.setAdjustedComponent(rollActuator);
		pitchActuatorAdjuster.setAdjustedComponent(pitchActuator);
		yawActuatorAdjuster.setAdjustedComponent(yawActuator);
		
		gyroAdjuster.setRate(200); //Tick every 200 ms
		rollActuatorAdjuster.setRate(300); //Tick every 300 ms
		pitchActuatorAdjuster.setRate(400); //Tick every 400 ms
		yawActuatorAdjuster.setRate(300); //Tick every 300 ms
		
		worldEventManager.setWorld(world);
		gyroscope.setWorld(world);
		rollActuator.setWorld(world);
		pitchActuator.setWorld(world);
		yawActuator.setWorld(world);
		
		Clock clock = Clock.systemDefaultZone();
		final long startMillis = clock.millis();
		long prevMillis = startMillis;
		long numTicks = 0;
		for (int i = 0; i < Integer.MAX_VALUE && !isInterrupted() && numTicks < maxTicks; ++i) {
			try {
				pauseIfNeeded();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}

			final long currentTime = clock.millis();

			if (currentTime - prevMillis < 1) {
				continue;
			}

			++numTicks;
			final long relativeTime = numTicks;

			worldStateListener.ifPresent((DataListener l) -> {
				l.dataChanged(new DataChangeEvent(DataType.WorldTime, Long.toString(relativeTime)));
			});

			worldEventManager.tick();
			gyroAdjuster.tick();

			controller.orElse(DEFAULT_CONTROLLER).tick();

			rollActuatorAdjuster.tick();
			pitchActuatorAdjuster.tick();
			yawActuatorAdjuster.tick();
			
			prevMillis = currentTime;
		}
	}
	
	public synchronized void pause() throws InterruptedException {
		isWaiting = true;
	}
	
	public synchronized void unpause() {
		notify();
	}
	
	private void pauseIfNeeded() throws InterruptedException {
		if (isWaiting) {
			synchronized (this) {
				wait();
			}
			isWaiting = false;
		}
	}
}
