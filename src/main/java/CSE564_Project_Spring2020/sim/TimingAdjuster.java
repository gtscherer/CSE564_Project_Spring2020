package CSE564_Project_Spring2020.sim;

import java.util.Optional;

public class TimingAdjuster {
	private int refreshRate, ticks;
	private Optional<TimingAdjusted> adjustedComponent;
	
	public TimingAdjuster() {
		adjustedComponent = Optional.empty();
		refreshRate = 0;
		ticks = 0;
	}
	
	public void setRate(int rate) {
		assert(rate > 0);
		refreshRate = rate;
	}
	
	public void setAdjustedComponent(TimingAdjusted component) {
		assert(component != null);
		adjustedComponent = Optional.of(component);
	}
	
	public void tick() {
		assert(refreshRate > 0);
		assert(adjustedComponent.isPresent());
		++ticks;
		
		if (ticks == refreshRate) {
			adjustedComponent.get().simTick();
			ticks = 0;
		}
	}
}
