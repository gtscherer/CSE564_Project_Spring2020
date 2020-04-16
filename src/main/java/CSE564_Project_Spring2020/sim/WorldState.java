package CSE564_Project_Spring2020.sim;

public class WorldState {
	private Degree currentDegrees;
	
	public WorldState() {
		currentDegrees = new Degree();
	}
	
	public void degreeChanged(Degree d_deg) {
		currentDegrees.add(d_deg);
	}
	
	public Degree getCurrentDegrees() {
		return currentDegrees;
	}
}
