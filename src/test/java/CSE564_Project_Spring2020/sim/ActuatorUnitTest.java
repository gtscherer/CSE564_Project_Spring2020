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
		Actuator cut = new Actuator();
		
		assertThrows(AssertionError.class, () -> { cut.simTick(); });
		assertThrows(AssertionError.class, () -> { cut.setWorld(null); });

		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);

		assertThrows(AssertionError.class, () -> { cut.simTick(); });
	}
	
	@Test
	void testSetDirection() {
		Actuator cut = new Actuator();
		
		assertThrows(AssertionError.class, () -> { cut.simTick(); });
		assertThrows(AssertionError.class, () -> { cut.setDirection(null); });
		
		cut.setDirection(RotationDirection.ROLL);
		cut.setDirection(RotationDirection.PITCH);
		cut.setDirection(RotationDirection.YAW);
		
		assertThrows(AssertionError.class, () -> { cut.simTick(); });
	}
	
	@Test
	void testSimTickNoRotate() {
		Actuator cut = new Actuator();
		
		assertThrows(AssertionError.class, () -> { cut.simTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		cut.setDirection(RotationDirection.ROLL);
		
		cut.simTick();
		
		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
		
		cut.simTick();

		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
	}

	@Test
	void testSimTickWithRotate() {
		Actuator cut = new Actuator();
		
		assertThrows(AssertionError.class, () -> { cut.simTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		cut.setDirection(RotationDirection.ROLL);
		
		cut.simTick();
		
		verify(mockWorld, never()).rollChanged(any(Degree.class));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
		
		cut.rotate(new Degree(5.3d));
		
		cut.simTick();

		verify(mockWorld, times(1)).rollChanged(new Degree(5.3d));
		verify(mockWorld, never()).pitchChanged(any(Degree.class));
		verify(mockWorld, never()).yawChanged(any(Degree.class));
	}
}
