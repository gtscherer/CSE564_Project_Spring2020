package CSE564_Project_Spring2020.sim;

/** 
 * 
 * Work in progress! Do not use yet until missing pieces are added.
 *
 */
public class Simulator {
	public void run() {
		World world = new World();
		WorldEventManager worldEventManager = new WorldEventManager();
		Gyroscope gyroscope = new Gyroscope();
		Actuator rollActuator = new Actuator(RotationDirection.ROLL);
		Actuator pitchActuator = new Actuator(RotationDirection.PITCH);
		Actuator yawActuator = new Actuator(RotationDirection.YAW);
		
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
		
		for (int i = 0; i < Integer.MAX_VALUE; ++i) {
			worldEventManager.tick();
			gyroAdjuster.tick();
			// Controller updates here
			rollActuatorAdjuster.tick();
			pitchActuatorAdjuster.tick();
			yawActuatorAdjuster.tick();
		}
	}
}
