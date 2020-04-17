package CSE564_Project_Spring2020.sim;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class AxisEventManager implements ClockedComponent {
	private Queue<AxisEvent> inactive;
	private List<AxisEvent> active;
	private int currentTime;
	
	public AxisEventManager() {
		inactive = new PriorityQueue<AxisEvent>((AxisEvent lhs, AxisEvent rhs) -> {
			return Integer.compare(lhs.startTime,  rhs.startTime);
		});
		active = new LinkedList<AxisEvent>();
		currentTime = 0;
	}
	
	public List<AxisEvent> getActiveEvents() {
		return active;
	}
	
	public void addEvent(int time, int duration, Degree d_deg) {
		inactive.add(new AxisEvent(time, duration, d_deg));
	}
	
	@Override
	public void tick() {
		++currentTime;
		active = active.stream()
					.filter((AxisEvent e) -> currentTime - (e.startTime + e.duration) < 0)
					.collect(Collectors.toCollection(() -> new LinkedList<AxisEvent>()));
		
		while (!inactive.isEmpty() && inactive.peek().startTime == currentTime) {
			active.add(inactive.poll());
		}
	}
}
