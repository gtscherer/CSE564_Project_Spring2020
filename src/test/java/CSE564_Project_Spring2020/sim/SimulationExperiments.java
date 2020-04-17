package CSE564_Project_Spring2020.sim;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import CSE564_Project_Spring2020.ui.DataChangeEvent;
import CSE564_Project_Spring2020.ui.DataType;

class SimulationExperiments {
	
	private List<ExperimentWorldData> worldTable;
	private List<ExperimentGyroData> gyroTable;

	/**
	 * Convenience method for running experiments with predefined events.
	 * 
	 * @param experimentDuration Maximum runtime of experiment. Directly correlates
	 *                           to the number of rows output into the tables.
	 * @param eventTable (Optional) Event tuples that describe when, for how long, and 
	 * 	                 to what effect the world state should change.
	 * @throws InterruptedException propagated from Thread.join
	 */
	private void runExperiment(final long experimentDuration, Optional<List<ExperimentWorldEvent>> eventTable) throws InterruptedException {
		Simulator s = new Simulator(experimentDuration);
		
		ExperimentWorldDataListener testWorldListener = new ExperimentWorldDataListener();
		ExperimentGyroDataListener testGyroListener = new ExperimentGyroDataListener();
		
		testWorldListener.setGyroDataListener(testGyroListener);
		
		s.setWorldDataListener(testWorldListener);
		s.setGyroscopeDataListener(testGyroListener);
		
		eventTable.ifPresent((List<ExperimentWorldEvent> eventTab) -> {
			eventTab.forEach((ExperimentWorldEvent event) -> {
				s.getWorldEventManager().addEvent(
					event.startTime,
					event.duration,
					event.d_roll,
					event.d_pitch,
					event.d_yaw
				);
			});
		});
		
		s.start();
		s.join();
		
		testWorldListener.dataChanged(new DataChangeEvent(DataType.WorldTime, Long.toString(experimentDuration)));
		
		worldTable = testWorldListener.getTable();
		worldTable.remove(worldTable.size() - 1);
		
		gyroTable = testGyroListener.getTable();
		gyroTable.remove(gyroTable.size() - 1);
	}
	
	
	/**
	 * Basic integration test. More involved tests should create
	 * CSV files from output and be analyzed manually.
	 * 
	 * @throws InterruptedException propagated from Thread.join
	 */
	@Test
	void testBasicWorldEvents() throws InterruptedException {
		List<ExperimentWorldEvent> events = Arrays.asList(
			new ExperimentWorldEvent(10, 10, 0.1, 0.2, 0.3),
			new ExperimentWorldEvent(30, 10, 0.3, 0.1, 0.2)
		);
		
		runExperiment(40l, Optional.of(events));
		
		List<ExperimentWorldData> expectedWorldData = Arrays.asList(
			new ExperimentWorldData(1, 0.0, 0.0, 0.0),
			new ExperimentWorldData(2, 0.0, 0.0, 0.0),
			new ExperimentWorldData(3, 0.0, 0.0, 0.0),
			new ExperimentWorldData(4, 0.0, 0.0, 0.0),
			new ExperimentWorldData(5, 0.0, 0.0, 0.0),
			new ExperimentWorldData(6, 0.0, 0.0, 0.0),
			new ExperimentWorldData(7, 0.0, 0.0, 0.0),
			new ExperimentWorldData(8, 0.0, 0.0, 0.0),
			new ExperimentWorldData(9, 0.0, 0.0, 0.0),

			new ExperimentWorldData(10, 0.1, 0.2, 0.3),
			new ExperimentWorldData(11, 0.2, 0.4, 0.6),
			new ExperimentWorldData(12, 0.3, 0.6, 0.9),
			new ExperimentWorldData(13, 0.4, 0.8, 1.2),
			new ExperimentWorldData(14, 0.5, 1.0, 1.5),
			new ExperimentWorldData(15, 0.6, 1.2, 1.8),
			new ExperimentWorldData(16, 0.7, 1.4, 2.1),
			new ExperimentWorldData(17, 0.8, 1.6, 2.4),
			new ExperimentWorldData(18, 0.9, 1.8, 2.7),
			new ExperimentWorldData(19, 1.0, 2.0, 3.0),
			
			new ExperimentWorldData(20, 1.0, 2.0, 3.0),
			new ExperimentWorldData(21, 1.0, 2.0, 3.0),
			new ExperimentWorldData(22, 1.0, 2.0, 3.0),
			new ExperimentWorldData(23, 1.0, 2.0, 3.0),
			new ExperimentWorldData(24, 1.0, 2.0, 3.0),
			new ExperimentWorldData(25, 1.0, 2.0, 3.0),
			new ExperimentWorldData(26, 1.0, 2.0, 3.0),
			new ExperimentWorldData(27, 1.0, 2.0, 3.0),
			new ExperimentWorldData(28, 1.0, 2.0, 3.0),
			new ExperimentWorldData(29, 1.0, 2.0, 3.0),
			
			new ExperimentWorldData(30, 1.3, 2.1, 3.2),
			new ExperimentWorldData(31, 1.6, 2.2, 3.4),
			new ExperimentWorldData(32, 1.9, 2.3, 3.6),
			new ExperimentWorldData(33, 2.2, 2.4, 3.8),
			new ExperimentWorldData(34, 2.5, 2.5, 4.0),
			new ExperimentWorldData(35, 2.8, 2.6, 4.2),
			new ExperimentWorldData(36, 3.1, 2.7, 4.4),
			new ExperimentWorldData(37, 3.4, 2.8, 4.6),
			new ExperimentWorldData(38, 3.7, 2.9, 4.8),
			new ExperimentWorldData(39, 4.0, 3.0, 5.0),
			
			new ExperimentWorldData(40, 4.0, 3.0, 5.0)		
		);
		
		List<ExperimentGyroData> expectedGyroData = new ArrayList<ExperimentGyroData>();
		
		for (int i = 1; i < 41; ++i) {
			expectedGyroData.add(new ExperimentGyroData(i, 0.0, 0.0, 0.0)); // Updates every 200ms; no data
		}
		
		assertIterableEquals(expectedWorldData, worldTable);
		assertIterableEquals(expectedGyroData, gyroTable);
	}

}
