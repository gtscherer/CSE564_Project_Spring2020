package CSE564_Project_Spring2020.sim;

public class DelaySettings {
	public int gyroDelay;
	public int rollActuatorDelay, pitchActuatorDelay, yawActuatorDelay;
	
	public DelaySettings(
		int _gyroDelay,
		int _rollActuatorDelay,
		int _pitchActuatorDelay,
		int _yawActuatorDelay
	) {
		gyroDelay = _gyroDelay;
		rollActuatorDelay = _rollActuatorDelay;
		pitchActuatorDelay = _pitchActuatorDelay;
		yawActuatorDelay = _yawActuatorDelay;
	}
}
