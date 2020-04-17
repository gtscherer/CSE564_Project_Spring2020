package CSE564_Project_Spring2020.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataListener;
import CSE564_Project_Spring2020.ui.DataType;

public class ExperimentWorldDataListener implements DataListener {

	private List<ExperimentWorldData> worldTable;
	private long time;
	private Boolean[] valuesSet;
	
	private Optional<DataListener> gyroListener;
	
	public ExperimentWorldDataListener() {
		worldTable = new ArrayList<ExperimentWorldData>();
		time = 0l;
		valuesSet = new Boolean[]{false, false, false};
		
		gyroListener = Optional.empty();
	}
	
	public void setGyroDataListener(DataListener l) {
		assert(l != null);
		gyroListener = Optional.of(l);
	}

	@Override
	public void dataChanged(DataChangeEvent e) {
		final DataType type = e.getType();

		final ExperimentWorldData previousRow = worldTable.isEmpty() ? new ExperimentWorldData() : worldTable.get(worldTable.size() - 1);

		if (type == DataType.WorldTime) {
			
			if (!allSet() && worldTable.size() > 1) {
				final ExperimentWorldData rowBeforePrevious = worldTable.get(worldTable.size() - 2);
				if (!valuesSet[0]) previousRow.roll = rowBeforePrevious.roll;
				if (!valuesSet[1]) previousRow.pitch = rowBeforePrevious.pitch;
				if (!valuesSet[2]) previousRow.yaw = rowBeforePrevious.yaw;
			}

			ExperimentWorldData newRow = new ExperimentWorldData();
			newRow.time = ++time;
			worldTable.add(newRow);
			Arrays.fill(valuesSet, Boolean.FALSE);
			
			gyroListener.ifPresent((DataListener l) -> l.dataChanged(e));
		}
		else if (type == DataType.WorldRoll) {
			previousRow.roll = Double.parseDouble(e.getValue());
			valuesSet[0] = Boolean.TRUE;
		}
		else if (type == DataType.WorldPitch) {
			previousRow.pitch = Double.parseDouble(e.getValue());
			valuesSet[1] = Boolean.TRUE;
		}
		else if (type == DataType.WorldYaw) {
			previousRow.yaw = Double.parseDouble(e.getValue());
			valuesSet[2] = Boolean.TRUE;
		}
		
	}

	public List<ExperimentWorldData> getTable() {
		return worldTable;
	}

	private Boolean allSet() {
		return Arrays.stream(valuesSet).reduce((Boolean lhs, Boolean rhs) -> Boolean.logicalAnd(lhs, rhs)).get();
	}
}
