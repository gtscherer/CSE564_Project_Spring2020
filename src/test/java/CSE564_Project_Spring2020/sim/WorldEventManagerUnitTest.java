package CSE564_Project_Spring2020.sim;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WorldEventManagerUnitTest {

	@Test
	void smokeTest() {
		WorldEventManager cut = new WorldEventManager();
		
		assertThrows(AssertionError.class, () -> { cut.tick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		
		cut.tick();
		
		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
		
		cut.addEvent(2, 2, new Degree(1.0), new Degree(2.0), new Degree(3.0));
		
		cut.tick();
		
		verify(mockWorld, times(1)).rollChanged(new Degree(1.0));
		verify(mockWorld, times(1)).pitchChanged(new Degree(2.0));
		verify(mockWorld, times(1)).yawChanged(new Degree(3.0));
		
		cut.tick();
		
		verify(mockWorld, times(2)).rollChanged(new Degree(1.0));
		verify(mockWorld, times(2)).pitchChanged(new Degree(2.0));
		verify(mockWorld, times(2)).yawChanged(new Degree(3.0));
		
		cut.tick();
		
		verify(mockWorld, times(2)).rollChanged(any(Degree.class));
		verify(mockWorld, times(2)).pitchChanged(any(Degree.class));
		verify(mockWorld, times(2)).yawChanged(any(Degree.class));
	}

}
