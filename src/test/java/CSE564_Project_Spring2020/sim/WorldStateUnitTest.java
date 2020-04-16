package CSE564_Project_Spring2020.sim;

import static CSE564_Project_Spring2020.sim.DegreeAssertions.assertEquals;

import org.junit.jupiter.api.Test;

class WorldStateUnitTest {

	@Test
	void smokeTest() {
		WorldState cut = new WorldState();
		
		assertEquals(0.0d, cut.getCurrentDegrees());
		
		cut.degreeChanged(new Degree(4.1d));
		
		assertEquals(4.1d, cut.getCurrentDegrees());
		
		cut.degreeChanged(new Degree(-2.0d));
		
		assertEquals(2.1d, cut.getCurrentDegrees());
		
		cut.degreeChanged(new Degree(361d));
		
		assertEquals(3.1d, cut.getCurrentDegrees());
		
		cut.degreeChanged(new Degree(-4.0d));
		
		assertEquals(359.1d, cut.getCurrentDegrees());
	}

}
