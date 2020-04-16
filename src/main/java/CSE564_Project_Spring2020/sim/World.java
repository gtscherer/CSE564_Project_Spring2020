package CSE564_Project_Spring2020.sim;

public class World {
	private WorldState rollState, pitchState, yawState;
	
	public World() {
		rollState = new WorldState();
		pitchState = new WorldState();
		yawState = new WorldState();
	}
	
	public void rollChanged(Degree d_deg) {
		rollState.degreeChanged(d_deg);
	}
	
	public void pitchChanged(Degree d_deg) {
		pitchState.degreeChanged(d_deg);
	}
	
	public void yawChanged(Degree d_deg) {
		yawState.degreeChanged(d_deg);
	}
	
	public Degree getCurrentRoll() {
		return rollState.getCurrentDegrees();
	}
	
	public Degree getCurrentPitch() {
		return pitchState.getCurrentDegrees();
	}
	
	public Degree getCurrentYaw() {
		return yawState.getCurrentDegrees();
	}
}
