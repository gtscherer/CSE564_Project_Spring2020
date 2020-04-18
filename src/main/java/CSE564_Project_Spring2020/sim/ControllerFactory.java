package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class ControllerFactory {
	public static Optional<Controller> CreateController(ControllerType type) {
		if (type == ControllerType.Simple ) {
			return Optional.of(new SimpleController());
		}
		else if (type == ControllerType.Incremental) {
			return Optional.of(new IncrementalController());
		}
		
		return Optional.empty();
	}
}
