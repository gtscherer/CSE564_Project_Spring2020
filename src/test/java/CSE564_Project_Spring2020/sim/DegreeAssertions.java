package CSE564_Project_Spring2020.sim;

import org.junit.jupiter.api.Assertions;

public class DegreeAssertions {
	public static void assertEquals(double expectedValue, Degree actualValue) {
		Assertions.assertEquals(expectedValue, actualValue.getValue(), 0.001d);
	}
	
	public static void assertEquals(Degree expectedValue, Degree actualValue) {
		Assertions.assertEquals(expectedValue.getValue(), actualValue.getValue(), 0.001d);
	}
}
