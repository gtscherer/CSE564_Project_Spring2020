package CSE564_Project_Spring2020.sim;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class ActuatorUnitTest {

	@Test
	void testSetWorld() {
		Actuator cut = new Actuator(RotationAxis.PITCH);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		assertThrows(AssertionError.class, () -> { cut.setWorld(null); });

		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
	}
	
	@Test
	void testConstructor() {
		assertThrows(AssertionError.class, () -> { new Actuator(null); });
	}
	
	@Test
	void testSimTickNoRotate() {
		Actuator cut = new Actuator(RotationAxis.YAW);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		
		cut.adjustedTick();
		
		verify(mockWorld, never()).rollChanged(anyDouble());
		verify(mockWorld, never()).pitchChanged(anyDouble());
		verify(mockWorld, never()).yawChanged(anyDouble());
		
		cut.adjustedTick();

		verify(mockWorld, never()).rollChanged(anyDouble());
		verify(mockWorld, never()).pitchChanged(anyDouble());
		verify(mockWorld, never()).yawChanged(anyDouble());
	}

	@Test
	void testSimTickWithRotate() {
		Actuator cut = new Actuator(RotationAxis.ROLL);
		
		assertThrows(AssertionError.class, () -> { cut.adjustedTick(); });
		
		World mockWorld = mock(World.class);
		
		cut.setWorld(mockWorld);
		cut.adjustedTick();
		
		verify(mockWorld, never()).rollChanged(anyDouble());
		verify(mockWorld, never()).pitchChanged(anyDouble());
		verify(mockWorld, never()).yawChanged(anyDouble());
		
		cut.rotate(5.3d);
		
		cut.adjustedTick();

		verify(mockWorld, times(1)).rollChanged(5.3d);
		verify(mockWorld, never()).pitchChanged(anyDouble());
		verify(mockWorld, never()).yawChanged(anyDouble());
	}
	
	@Test
	void testRotatePreconditions() {
		Actuator cut = new Actuator(RotationAxis.YAW);

		cut.rotate(5.3d);
		
		assertThrows(AssertionError.class, () -> { cut.rotate(Double.POSITIVE_INFINITY); });
		assertThrows(AssertionError.class, () -> { cut.rotate(Double.NEGATIVE_INFINITY); });
		assertThrows(AssertionError.class, () -> { cut.rotate(Double.NaN); });
	}
}
