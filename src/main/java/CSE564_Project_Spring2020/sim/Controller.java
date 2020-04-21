package CSE564_Project_Spring2020.sim;

public interface Controller extends ClockedComponent {
	void setGyroscope(Gyroscope gyro);
	void setActuator(RotationAxis axis, Actuator actuator);
}
