package CSE564_Project_Spring2020.sim.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import CSE564_Project_Spring2020.sim.ClockedComponent;
import CSE564_Project_Spring2020.sim.TimingAdjuster;
import org.junit.jupiter.api.Test;

class TimingAdjusterUnitTest {

	@Test
	void testSetRate() {
		TimingAdjuster cut = new TimingAdjuster();
		
		cut.setRate(1);
		cut.setRate(Integer.MAX_VALUE);

		assertThrows(AssertionError.class, () -> { cut.setRate(0); });
		assertThrows(AssertionError.class, () -> { cut.setRate(-1); });
		assertThrows(AssertionError.class, () -> { cut.setRate(-2); });
	}
	
	@Test
	void testTick() {
		final TimingAdjuster oldCut = new TimingAdjuster();
		
		assertThrows(AssertionError.class, () -> { oldCut.setAdjustedComponent(null); });
		
		ClockedComponent mockAdjustedComponent = mock(ClockedComponent.class);
		
		assertThrows(AssertionError.class, () -> { oldCut.tick(); });
		oldCut.setRate(1);
		assertThrows(AssertionError.class, () -> { oldCut.tick(); });
		oldCut.setAdjustedComponent(mockAdjustedComponent);
		
		final TimingAdjuster newCut = new TimingAdjuster();
		newCut.setAdjustedComponent(mockAdjustedComponent);
		
		assertThrows(AssertionError.class, () -> { newCut.tick(); });
		
		newCut.setRate(2);

		newCut.tick();
		verify(mockAdjustedComponent, times(0)).tick();
		newCut.tick();
		verify(mockAdjustedComponent, times(1)).tick();
		
		newCut.tick();
		verify(mockAdjustedComponent, times(1)).tick();
		newCut.tick();
		verify(mockAdjustedComponent, times(2)).tick();
		
		newCut.setRate(3);
		
		newCut.tick();
		verify(mockAdjustedComponent, times(2)).tick();
		newCut.tick();
		verify(mockAdjustedComponent, times(2)).tick();
		newCut.tick();
		verify(mockAdjustedComponent, times(3)).tick();
	}

}
