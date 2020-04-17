package CSE564_Project_Spring2020.sim;

import java.util.Optional;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataListener;
import CSE564_Project_Spring2020.ui.DataType;

public class World {
	private WorldState rollState, pitchState, yawState;
	private Optional<DataListener> worldStateListener;
	
	public World() {
		rollState = new WorldState();
		pitchState = new WorldState();
		yawState = new WorldState();
		
		worldStateListener = Optional.empty();
	}
	
	public void rollChanged(double d_deg) {
		rollState.degreeChanged(d_deg);

		worldStateListener.ifPresent(
			(DataListener l) -> {
				final String currentRoll = rollState.getCurrentDegrees().toString();
				final DataChangeEvent e = new DataChangeEvent(DataType.WorldRoll, currentRoll);
				l.dataChanged(e);
			}
		);
	}
	
	public void pitchChanged(double d_deg) {
		pitchState.degreeChanged(d_deg);

		worldStateListener.ifPresent(
			(DataListener l) -> {
				final String currentPitch = pitchState.getCurrentDegrees().toString();
				final DataChangeEvent e = new DataChangeEvent(DataType.WorldPitch, currentPitch);
				l.dataChanged(e);
			}
		);
	}
	
	public void yawChanged(double d_deg) {
		yawState.degreeChanged(d_deg);
		
		worldStateListener.ifPresent(
			(DataListener l) -> {
				final String currentYaw = yawState.getCurrentDegrees().toString();
				final DataChangeEvent e = new DataChangeEvent(DataType.WorldYaw, currentYaw);
				l.dataChanged(e);
			}
		);
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
	
	public void setStateListener(DataListener l) {
		assert(l != null);
		worldStateListener = Optional.of(l);
	}
}
