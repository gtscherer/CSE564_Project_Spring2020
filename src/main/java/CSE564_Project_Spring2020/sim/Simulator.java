package CSE564_Project_Spring2020.sim;

import java.time.Clock;
import java.util.Optional;

/** 
 * Main simulation loop for synchronized components.
 * Ordering of "ticks" denotes precedence relationships
 * between components.
 */
public class Simulator extends Thread {
	private World world;
	private WorldEventManager worldEventManager;
	private Gyroscope gyroscope;
	private Actuator rollActuator;
	private Actuator pitchActuator;
	private Actuator yawActuator;

	private boolean isWaiting;
	private long maxTicks;
	private Optional<Controller> rollController;
	private Optional<Controller> pitchController;
	private Optional<Controller> yawController;

	private Optional<DataListener> worldStateListener;
	private Optional<DataListener> gyroscopeDataListener;
	private Optional<DataListener> actuatorDataListener;
	
	public Simulator() {
		this(Long.MAX_VALUE);
	}
	
	public Simulator(long _maxTicks) {
		world = new World();
		worldEventManager = new WorldEventManager();
		gyroscope = new Gyroscope();
		rollActuator = new Actuator(RotationAxis.ROLL);
		pitchActuator = new Actuator(RotationAxis.PITCH);
		yawActuator = new Actuator(RotationAxis.YAW);
		
		isWaiting = false;
		maxTicks = _maxTicks;
		rollController = Optional.empty();
		pitchController = Optional.empty();
		yawController = Optional.empty();
		
		worldStateListener = Optional.empty();
		gyroscopeDataListener = Optional.empty();
		actuatorDataListener = Optional.empty();
	}
	
	public void setRollController(Controller _controller) {
		assert(_controller != null);
		_controller.setGyroscope(gyroscope);
		_controller.setActuator(RotationAxis.ROLL, rollActuator);
		rollController = Optional.of(_controller);
	}
	
	public void setPitchController(Controller _controller) {
		assert(_controller != null);
		_controller.setGyroscope(gyroscope);
		_controller.setActuator(RotationAxis.PITCH, pitchActuator);
		pitchController = Optional.of(_controller);
	}
	
	public void setYawController(Controller _controller) {
		assert(_controller != null);
		_controller.setGyroscope(gyroscope);
		_controller.setActuator(RotationAxis.YAW, yawActuator);
		yawController = Optional.of(_controller);
	}
	
	public WorldEventManager getWorldEventManager() {
		return worldEventManager;
	}
	
	public void setWorldDataListener(DataListener l) {
		assert(l != null);
		worldStateListener = Optional.of(l);
		world.setStateListener(l);
	}
	
	public void setGyroscopeDataListener(DataListener l) {
		assert(l != null);
		gyroscopeDataListener = Optional.of(l);
	}
	
	public void setActuatorDataListener(DataListener l) {
		assert(l != null);
		actuatorDataListener = Optional.of(l);
	}
	
	@Override
	public void run() {
		gyroscopeDataListener.ifPresent((DataListener l) -> gyroscope.setGyroscopeStateListener(l));

		actuatorDataListener.ifPresent((DataListener l) -> {
			rollActuator.setActuatorListener(l);
			pitchActuator.setActuatorListener(l);
			yawActuator.setActuatorListener(l);
		});

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

			rollController.ifPresent((Controller c) -> c.tick());
			pitchController.ifPresent((Controller c) -> c.tick());
			yawController.ifPresent((Controller c) -> c.tick());

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
