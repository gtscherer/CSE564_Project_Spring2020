package CSE564_Project_Spring2020.sim;

import java.time.Clock;
import java.util.Optional;

/** 
 * Main simulation loop for synchronized components.
 * Ordering of "ticks" denotes precedence relationships
 * between components.
 */
public class Simulator extends Thread implements ClockedComponent {
	private World world;
	private WorldEventManager worldEventManager;
	private Gyroscope gyroscope;
	private Actuator rollActuator;
	private Actuator pitchActuator;
	private Actuator yawActuator;
	
	private TimingAdjuster gyroAdjuster;
	private TimingAdjuster rollActuatorAdjuster;
	private TimingAdjuster pitchActuatorAdjuster;
	private TimingAdjuster yawActuatorAdjuster;

	private boolean isWaiting;
	private long maxTicks;
	private long numTicks;

	private Optional<Controller> rollController;
	private Optional<Controller> pitchController;
	private Optional<Controller> yawController;

	private Optional<DataListener> worldStateListener;
	
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
		
		gyroAdjuster = new TimingAdjuster();
		rollActuatorAdjuster = new TimingAdjuster();
		pitchActuatorAdjuster = new TimingAdjuster();
		yawActuatorAdjuster = new TimingAdjuster();
		
		isWaiting = false;
		maxTicks = _maxTicks;
		numTicks = 0;
		rollController = Optional.empty();
		pitchController = Optional.empty();
		yawController = Optional.empty();
		
		worldStateListener = Optional.empty();
		
		worldEventManager.setWorld(world);
		gyroscope.setWorld(world);
		rollActuator.setWorld(world);
		pitchActuator.setWorld(world);
		yawActuator.setWorld(world);
		
		gyroAdjuster.setAdjustedComponent(gyroscope);
		rollActuatorAdjuster.setAdjustedComponent(rollActuator);
		pitchActuatorAdjuster.setAdjustedComponent(pitchActuator);
		yawActuatorAdjuster.setAdjustedComponent(yawActuator);
		
		gyroAdjuster.setRate(1);
		rollActuatorAdjuster.setRate(1);
		pitchActuatorAdjuster.setRate(1);
		yawActuatorAdjuster.setRate(1);
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
		gyroscope.setGyroscopeStateListener(l);
	}
	
	public void setActuatorDataListener(DataListener l) {
		assert(l != null);
		rollActuator.setActuatorListener(l);
		pitchActuator.setActuatorListener(l);
		yawActuator.setActuatorListener(l);
	}
	
	public void setGyroDelay(int ms) {
		gyroAdjuster.setRate(ms);
	}
	
	public void setActuatorDelay(RotationAxis axis, int ms) {
		if (axis == RotationAxis.ROLL) {
			rollActuatorAdjuster.setRate(ms);
		}
		else if (axis == RotationAxis.PITCH) {
			pitchActuatorAdjuster.setRate(ms);
		}
		else if (axis == RotationAxis.YAW) {
			yawActuatorAdjuster.setRate(ms);
		}
	}
	
	@Override
	public void run() {
		Clock clock = Clock.systemDefaultZone();
		final long startMillis = clock.millis();
		long prevMillis = startMillis;
		while (!isInterrupted() && numTicks < Math.min(maxTicks, Integer.MAX_VALUE)) {
			try {
				pauseIfNeeded();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}

			final long currentTime = clock.millis();

			if (currentTime - prevMillis < 1) {
				try {
					Thread.sleep(0, 100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			
			tick();

			prevMillis = currentTime;
		}
	}
	
	@Override
	public void tick() {
		++numTicks;
		final long relativeTime = numTicks;

		worldStateListener.ifPresent((DataListener l) -> {
			l.dataChanged(new DataChangeEvent(DataType.WorldTime, Long.toString(relativeTime)));
		});

		worldEventManager.tick();
		gyroAdjuster.tick();

		rollController.ifPresent(Controller::tick);
		pitchController.ifPresent(Controller::tick);
		yawController.ifPresent(Controller::tick);

		rollActuatorAdjuster.tick();
		pitchActuatorAdjuster.tick();
		yawActuatorAdjuster.tick();
	}
	
	public synchronized void pause() {
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
