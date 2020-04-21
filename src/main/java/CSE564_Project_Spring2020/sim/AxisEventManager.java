package CSE564_Project_Spring2020.sim;

import java.util.*;
import java.util.stream.Collectors;

public class AxisEventManager implements ClockedComponent {
	private final Queue<AxisEvent> inactive;
	private List<AxisEvent> active;
	private int currentTime;
	
	public AxisEventManager() {
		inactive = new PriorityQueue<>(Comparator.comparingInt((AxisEvent lhs) -> lhs.startTime));
		active = new LinkedList<>();
		currentTime = 0;
	}
	
	public List<AxisEvent> getActiveEvents() {
		return active;
	}
	
	public void addEvent(int time, int duration, double d_deg) {
		inactive.add(new AxisEvent(time, duration, d_deg));
	}
	
	@Override
	public void tick() {
		++currentTime;
		active = active.stream()
					.filter((AxisEvent e) -> currentTime - (e.startTime + e.duration) < 0)
					.collect(Collectors.toCollection(LinkedList::new));
		
		while (!inactive.isEmpty() && inactive.peek().startTime <= currentTime) {
			active.add(inactive.poll());
		}
	}
}
