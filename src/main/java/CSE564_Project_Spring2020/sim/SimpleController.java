package CSE564_Project_Spring2020.sim;

public class SimpleController implements Controller {
	private Gyroscope gyro;
	private Actuator rollActuator, pitchActuator, yawActuator;
	
	public SimpleController() {
		gyro = null;
		rollActuator = null;
		pitchActuator = null;
		yawActuator = null;
	}
	
	@Override
	public void tick() {
		assert(gyro != null);
		assert(rollActuator != null);
		assert(pitchActuator != null);
		assert(yawActuator != null);
		
		Degree roll = gyro.getRoll();
		if (!roll.isZero()) {
			rollActuator.rotate(-1 * roll.getValue());
		}
		
		Degree pitch = gyro.getPitch();
		if (!pitch.isZero()) {
			pitchActuator.rotate(-1 * pitch.getValue());
		}
		
		Degree yaw = gyro.getYaw();
		if (!yaw.isZero()) {
			yawActuator.rotate(-1 * yaw.getValue());
		}
	}

	@Override
	public void setGyroscope(Gyroscope _gyro) {
		assert(_gyro != null);
		gyro = _gyro;
	}

	@Override
	public void setRollActuator(Actuator _rollActuator) {
		assert(_rollActuator != null);
		rollActuator = _rollActuator;
	}

	@Override
	public void setPitchActuator(Actuator _pitchActuator) {
		assert(_pitchActuator != null);
		pitchActuator = _pitchActuator;
	}

	@Override
	public void setYawActuator(Actuator _yawActuator) {
		assert(_yawActuator != null);
		yawActuator = _yawActuator;
	}

}
