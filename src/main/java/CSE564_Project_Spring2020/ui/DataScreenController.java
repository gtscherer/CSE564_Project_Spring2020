package CSE564_Project_Spring2020.ui;

import javax.swing.JLabel;

public class DataScreenController {
	
	static class WorldDataListener implements DataListener {
		private JLabel timeLabel, rollLabel, pitchLabel, yawLabel;
		
		@Override
		public void dataChanged(DataChangeEvent e) {
			final DataType type = e.getType();

			if (type == DataType.WorldTime) {
				timeLabel.setText(e.getValue());
			}
			else if (type == DataType.WorldRoll) {
				rollLabel.setText(e.getValue());
			}
			else if (type == DataType.WorldPitch) {
				pitchLabel.setText(e.getValue());
			}
			else if (type == DataType.WorldYaw) {
				yawLabel.setText(e.getValue());
			}
		}
		
		public void registerTimeLabel(JLabel _timeLabel) {
			timeLabel = _timeLabel;
		}
		
		public void registerRollLabel(JLabel _rollLabel) {
			rollLabel = _rollLabel;
		}
		
		public void registerPitchLabel(JLabel _pitchLabel) {
			pitchLabel = _pitchLabel;
		}
		
		public void registerYawLabel(JLabel _yawLabel) {
			yawLabel = _yawLabel;
		}
	}

	public static final WorldDataListener worldDataListener = new WorldDataListener();

	static class GyroDataListener implements DataListener {
		private JLabel rollLabel, pitchLabel, yawLabel;

		@Override
		public void dataChanged(DataChangeEvent e) {
			final DataType type = e.getType();

			if (type == DataType.GyroRoll) {
				rollLabel.setText(e.getValue());
			}
			else if (type == DataType.GyroPitch) {
				pitchLabel.setText(e.getValue());
			}
			else if (type == DataType.GyroYaw) {
				yawLabel.setText(e.getValue());
			}
		}
		
		public void registerRollLabel(JLabel _rollLabel) {
			rollLabel = _rollLabel;
		}
		
		public void registerPitchLabel(JLabel _pitchLabel) {
			pitchLabel = _pitchLabel;
		}
		
		public void registerYawLabel(JLabel _yawLabel) {
			yawLabel = _yawLabel;
		}
	}
	
	public static final GyroDataListener gyroDataListener = new GyroDataListener();
}
