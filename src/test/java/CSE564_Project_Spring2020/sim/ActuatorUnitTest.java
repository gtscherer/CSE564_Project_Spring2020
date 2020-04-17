package CSE564_Project_Spring2020.sim;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;

class ActuatorUnitTest {

	@Test
	void testSetWorld() {
		Actuator cut = new Actuator(RotationDirection.PITCH);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		assertThrows(AssertionError.class, () -> { cut.setWorld(null); });

		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
	}
	
	@Test
	void testConstructor() {
		Actuator cut = new Actuator(RotationDirection.ROLL);
		
		assertThrows(AssertionError.class, () -> { new Actuator(null); });
	}
	
	@Test
	void testSimTickNoRotate() {
		Actuator cut = new Actuator(RotationDirection.YAW);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		
		cut.adjustedTick();
		
		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
		
		cut.adjustedTick();

		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
	}

	@Test
	void testSimTickWithRotate() {
		Actuator cut = new Actuator(RotationDirection.ROLL);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		cut.adjustedTick();
		
		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
		
		cut.rotate(new Degree(5.3d));
		
		cut.adjustedTick();

		verify(mockWorld, times(1)).rollChanged(new Degree(5.3d));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
	}
}
