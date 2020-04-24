package CSE564_Project_Spring2020.sim;

public class IncrementalController implements Controller {
	private Gyroscope gyro;
	private Actuator actuator;
	private RotationAxis axis;
	
	private double p_deg;
	private double multi;
	
	public IncrementalController() {
		gyro = null;
		actuator = null;
		axis = null;
		
		p_deg = 0d;
		multi = 0d;
	}
	
	@Override
	public void tick() {
		assert(gyro != null);
		assert(axis != null);
		assert(actuator != null);
		
		Degree deg = new Degree();
		
		if (axis == RotationAxis.ROLL) {
			deg = gyro.getRoll();
		}
		else if (axis == RotationAxis.PITCH) {
			deg = gyro.getPitch();
		}
		else if (axis == RotationAxis.YAW) {
			deg = gyro.getYaw();
		}
		
		double amount = deg.getValue();
		
		if (deg.isZero()) {
			actuator.rotate(0.0d);
		} else {
			if (amount < 180) {
				if (p_deg <= amount) {
					multi -= 1;
				} else {
					multi += 0.2;
				}
			} else {
				if (p_deg >= amount) {
					multi += 1;
				} else {
					multi -= 0.2;
				}
			}
			actuator.rotate((Math.abs(amount)/amount) * multi);
		}

		p_deg = amount;
	}

	@Override
	public void setGyroscope(Gyroscope _gyro) {
		assert(_gyro != null);
		gyro = _gyro;
	}

	@Override
	public void setActuator(RotationAxis _axis, Actuator _actuator) {
		assert(_actuator != null);
		assert(_axis != null);
		
		actuator = _actuator;
		axis = _axis;
	}
}