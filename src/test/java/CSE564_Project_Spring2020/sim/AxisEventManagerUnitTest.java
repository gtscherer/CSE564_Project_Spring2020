package CSE564_Project_Spring2020.sim;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static CSE564_Project_Spring2020.sim.DegreeAssertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class AxisEventManagerUnitTest {

	@Test
	void smokeTest() {
		AxisEventManager cut = new AxisEventManager();
		
		assertTrue(cut.getActiveEvents().isEmpty());
		
		cut.addEvent(1, 2, new Degree(2.0d));
		
		assertTrue(cut.getActiveEvents().isEmpty());

		cut.tick();
		
		List<AxisEvent> activeEvents = cut.getActiveEvents();
		
		assertEquals(1, activeEvents.size());
		
		AxisEvent firstEvent = activeEvents.get(0);
		
		assertEquals(1, firstEvent.startTime);
		assertEquals(2, firstEvent.duration);
		assertEquals(new Degree(2.0d), firstEvent.d_deg);
		
		cut.tick();

		assertEquals(1, activeEvents.size());

		AxisEvent secondEvent = activeEvents.get(0);
		
		assertEquals(1, secondEvent.startTime);
		assertEquals(2, secondEvent.duration);
		assertEquals(new Degree(2.0d), secondEvent.d_deg);
		
		cut.tick();
		
		assertTrue(cut.getActiveEvents().isEmpty());
	}
	
	@Test
	public void multipleEventsTest() {
		AxisEventManager cut = new AxisEventManager();

		assertTrue(cut.getActiveEvents().isEmpty());
		
		cut.addEvent(1, 1, new Degree(11.0d));
		cut.addEvent(2, 1, new Degree(21.0d));
		cut.addEvent(2, 2, new Degree(22.0d));
		cut.addEvent(3, 1, new Degree(31.0d));
		
		cut.tick();
		
		List<AxisEvent> activeEvents = cut.getActiveEvents();
		
		assertEquals(1, activeEvents.size());

		assertEquals(1, activeEvents.get(0).startTime);
		assertEquals(1, activeEvents.get(0).duration);
		assertEquals(new Degree(11.0d), activeEvents.get(0).d_deg);
		
		cut.tick();
		
		activeEvents = cut.getActiveEvents();
		activeEvents.sort((AxisEvent lhs, AxisEvent rhs) -> {
			return Double.compare(lhs.d_deg.getValue(), rhs.d_deg.getValue());
		});
		
		assertEquals(2, activeEvents.size());
		
		
		assertEquals(2, activeEvents.get(0).startTime);
		assertEquals(1, activeEvents.get(0).duration);
		assertEquals(new Degree(21.0d), activeEvents.get(0).d_deg);
		
		assertEquals(2, activeEvents.get(1).startTime);
		assertEquals(2, activeEvents.get(1).duration);
		assertEquals(new Degree(22.0d), activeEvents.get(1).d_deg);
		
		cut.tick();
		
		activeEvents = cut.getActiveEvents();
		activeEvents.sort((AxisEvent lhs, AxisEvent rhs) -> {
			return Double.compare(lhs.d_deg.getValue(), rhs.d_deg.getValue());
		});
		
		assertEquals(2, activeEvents.size());
		
		assertEquals(2, activeEvents.get(0).startTime);
		assertEquals(2, activeEvents.get(0).duration);
		assertEquals(new Degree(22.0d), activeEvents.get(0).d_deg);
		
		assertEquals(3, activeEvents.get(1).startTime);
		assertEquals(1, activeEvents.get(1).duration);
		assertEquals(new Degree(31.0d), activeEvents.get(1).d_deg);
		
		cut.tick();

		assertTrue(cut.getActiveEvents().isEmpty());
	}

}
