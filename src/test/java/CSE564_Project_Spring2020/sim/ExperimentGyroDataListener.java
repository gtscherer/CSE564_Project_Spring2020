package CSE564_Project_Spring2020.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataListener;
import CSE564_Project_Spring2020.ui.DataType;

public class ExperimentGyroDataListener implements DataListener {

	private List<ExperimentGyroData> worldTable;
	private long time;
	private Boolean[] valuesSet;
	
	public ExperimentGyroDataListener() {
		worldTable = new ArrayList<ExperimentGyroData>();
		time = 0l;
		valuesSet = new Boolean[]{false, false, false};
	}

	@Override
	public void dataChanged(DataChangeEvent e) {
		final DataType type = e.getType();

		final ExperimentGyroData previousRow = worldTable.isEmpty() ? new ExperimentGyroData() : worldTable.get(worldTable.size() - 1);

		if (type == DataType.WorldTime) {
			
			if (!allSet() && worldTable.size() > 1) {
				final ExperimentGyroData rowBeforePrevious = worldTable.get(worldTable.size() - 2);
				if (!valuesSet[0]) previousRow.roll = rowBeforePrevious.roll;
				if (!valuesSet[1]) previousRow.pitch = rowBeforePrevious.pitch;
				if (!valuesSet[2]) previousRow.yaw = rowBeforePrevious.yaw;
			}

			ExperimentGyroData newRow = new ExperimentGyroData();
			newRow.time = ++time;
			worldTable.add(newRow);
			Arrays.fill(valuesSet, Boolean.FALSE);
		}
		else if (type == DataType.GyroRoll) {
			previousRow.roll = Double.parseDouble(e.getValue());
			valuesSet[0] = Boolean.TRUE;
		}
		else if (type == DataType.GyroPitch) {
			previousRow.pitch = Double.parseDouble(e.getValue());
			valuesSet[1] = Boolean.TRUE;
		}
		else if (type == DataType.GyroYaw) {
			previousRow.yaw = Double.parseDouble(e.getValue());
			valuesSet[2] = Boolean.TRUE;
		}
		
	}

	public List<ExperimentGyroData> getTable() {
		return worldTable;
	}

	private Boolean allSet() {
		return Arrays.stream(valuesSet).reduce((Boolean lhs, Boolean rhs) -> Boolean.logicalAnd(lhs, rhs)).get();
	}
}
