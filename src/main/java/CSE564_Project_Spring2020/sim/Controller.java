package CSE564_Project_Spring2020.sim;

public interface Controller extends ClockedComponent {
	public void setGyroscope(Gyroscope gyro);
	public void setRollActuator(Actuator rollActuator);
	public void setPitchActuator(Actuator pitchActuator);
	public void setYawActuator(Actuator yawActuator);
}
