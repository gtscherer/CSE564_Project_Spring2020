package CSE564_Project_Spring2020.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExperimentGyroDataListener implements DataListener {

	private List<ExperimentGyroData> gyroTable;
	private long time;
	private Boolean[] valuesSet;
	
	public ExperimentGyroDataListener() {
		gyroTable = new ArrayList<ExperimentGyroData>();
		time = 0l;
		valuesSet = new Boolean[]{false, false, false};
	}

	@Override
	public void dataChanged(DataChangeEvent e) {
		final DataType type = e.getType();

		final ExperimentGyroData previousRow = gyroTable.isEmpty() ? new ExperimentGyroData() : gyroTable.get(gyroTable.size() - 1);

		if (type == DataType.WorldTime) {
			
			if (!allSet() && gyroTable.size() > 1) {
				final ExperimentGyroData rowBeforePrevious = gyroTable.get(gyroTable.size() - 2);
				if (!valuesSet[0]) previousRow.roll = rowBeforePrevious.roll;
				if (!valuesSet[1]) previousRow.pitch = rowBeforePrevious.pitch;
				if (!valuesSet[2]) previousRow.yaw = rowBeforePrevious.yaw;
			}

			ExperimentGyroData newRow = new ExperimentGyroData();
			newRow.time = ++time;
			gyroTable.add(newRow);
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
		return gyroTable;
	}

	private Boolean allSet() {
		return Arrays.stream(valuesSet).reduce((Boolean lhs, Boolean rhs) -> Boolean.logicalAnd(lhs, rhs)).get();
	}
}
