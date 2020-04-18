package CSE564_Project_Spring2020.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExperimentActuatorDataListener implements DataListener {

	private List<ExperimentActuatorData> actuatorTable;
	private long time;
	private Boolean[] valuesSet;
	
	public ExperimentActuatorDataListener() {
		actuatorTable = new ArrayList<ExperimentActuatorData>();
		time = 0l;
		valuesSet = new Boolean[]{false, false, false};
	}

	@Override
	public void dataChanged(DataChangeEvent e) {
		final DataType type = e.getType();

		final ExperimentActuatorData previousRow = actuatorTable.isEmpty() ? new ExperimentActuatorData() : actuatorTable.get(actuatorTable.size() - 1);

		if (type == DataType.WorldTime) {
			
			if (!allSet() && actuatorTable.size() > 1) {
				final ExperimentActuatorData rowBeforePrevious = actuatorTable.get(actuatorTable.size() - 2);
				if (!valuesSet[0]) previousRow.roll = rowBeforePrevious.roll;
				if (!valuesSet[1]) previousRow.pitch = rowBeforePrevious.pitch;
				if (!valuesSet[2]) previousRow.yaw = rowBeforePrevious.yaw;
			}

			ExperimentActuatorData newRow = new ExperimentActuatorData();
			newRow.time = ++time;
			actuatorTable.add(newRow);
			Arrays.fill(valuesSet, Boolean.FALSE);
		}
		else if (type == DataType.ActuatorRoll) {
			previousRow.roll = Double.parseDouble(e.getValue());
			valuesSet[0] = Boolean.TRUE;
		}
		else if (type == DataType.ActuatorPitch) {
			previousRow.pitch = Double.parseDouble(e.getValue());
			valuesSet[1] = Boolean.TRUE;
		}
		else if (type == DataType.ActuatorYaw) {
			previousRow.yaw = Double.parseDouble(e.getValue());
			valuesSet[2] = Boolean.TRUE;
		}
		
	}

	public List<ExperimentActuatorData> getTable() {
		return actuatorTable;
	}

	private Boolean allSet() {
		return Arrays.stream(valuesSet).reduce((Boolean lhs, Boolean rhs) -> Boolean.logicalAnd(lhs, rhs)).get();
	}
}
