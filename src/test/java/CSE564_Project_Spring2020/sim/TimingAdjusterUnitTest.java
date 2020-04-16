package CSE564_Project_Spring2020.sim;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
		TimingAdjuster cut = new TimingAdjuster();
		
		assertThrows(AssertionError.class, () -> { cut.setAdjustedComponent(null); });
		
		TimingAdjusted mockAdjustedComponent = mock(TimingAdjusted.class);
		
		assertThrows(AssertionError.class, () -> { cut.tick(); });
		cut.setRate(2);
		assertThrows(AssertionError.class, () -> { cut.tick(); });
		cut.setAdjustedComponent(mockAdjustedComponent);
		
		cut.tick();
		verify(mockAdjustedComponent, times(0)).simTick();
		cut.tick();
		verify(mockAdjustedComponent, times(1)).simTick();
		
		cut.tick();
		verify(mockAdjustedComponent, times(1)).simTick();
		cut.tick();
		verify(mockAdjustedComponent, times(2)).simTick();
		
		cut.setRate(3);
		
		cut.tick();
		verify(mockAdjustedComponent, times(2)).simTick();
		cut.tick();
		verify(mockAdjustedComponent, times(2)).simTick();
		cut.tick();
		verify(mockAdjustedComponent, times(3)).simTick();
	}

}
