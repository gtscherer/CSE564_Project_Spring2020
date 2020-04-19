package CSE564_Project_Spring2020.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import CSE564_Project_Spring2020.sim.Controller;
import CSE564_Project_Spring2020.sim.ControllerFactory;
import CSE564_Project_Spring2020.sim.ControllerType;
import CSE564_Project_Spring2020.sim.RotationAxis;
import CSE564_Project_Spring2020.sim.Simulator;

public class MainScreenController {
	
	static class MainScreenStateListener implements WindowStateListener {
		private Optional<Simulator> sim;
		private List<Thread> threads;
		
		public MainScreenStateListener() {
			sim = Optional.empty();
			threads = new LinkedList<Thread>();
		}

		@Override
		public void windowStateChanged(WindowEvent e) {
			if (e.getNewState() == WindowEvent.WINDOW_CLOSED) {
				sim.ifPresent((Simulator s) -> {
					s.interrupt();
				});
				threads.stream().filter((Thread t) -> t.isAlive()).forEach((Thread t) -> t.interrupt());
			}
		}
		
		public void registerSimulator(Simulator _sim) {
			assert(_sim != null);
			sim = Optional.of(_sim);
		}
		
		public void addThread(Thread thread) {
			threads.add(thread);
			
			if (threads.size() > 1000) {
				threads = threads.stream()
							.filter((Thread t) -> t.isAlive())
							.collect(Collectors.toCollection(() -> new LinkedList<Thread>()));
			}
		}
	}
	
	public static final MainScreenStateListener mainScreenStateListener = new MainScreenStateListener();

	static abstract class SimulationStarter implements ActionListener {
		protected Optional<Simulator> sim;
		protected Optional<JComboBox<ControllerType>> controllerPicker;
		private Optional<JTextField> gyroDelayField, rollActuatorDelayField, pitchActuatorDelayField, yawActuatorDelayField;
		private Optional<JFrame> mainScreen;
		private boolean controllerIsSet, delaysAreSet;
		
		public SimulationStarter() {
			sim = Optional.empty();
			controllerPicker = Optional.empty();
			gyroDelayField = Optional.empty();
			rollActuatorDelayField = Optional.empty();
			pitchActuatorDelayField = Optional.empty();
			yawActuatorDelayField = Optional.empty();

			controllerIsSet = false;
			delaysAreSet = false;
		}
		
		protected void setController() {
			if (controllerIsSet) {
				return;
			}
			final ControllerType controllerType = getControllerType();
	        if (controllerType != ControllerType.None) {
	        	sim.ifPresent(
        			(Simulator s) -> ControllerFactory.CreateController(controllerType)
    									.ifPresent((Controller c) -> s.setRollController(c))
				);
	        	sim.ifPresent(
        			(Simulator s) -> ControllerFactory.CreateController(controllerType)
        								.ifPresent((Controller c) -> s.setPitchController(c))
				);
	        	sim.ifPresent(
        			(Simulator s) -> ControllerFactory.CreateController(controllerType)
        								.ifPresent((Controller c) -> s.setYawController(c))
				);
	        }
	        controllerIsSet = true;
		}
		
		protected boolean setSimulationDelays() {
			if (delaysAreSet) {
				return true;
			}
			
			boolean[] success = new boolean[] { true };
			
			sim.ifPresent((Simulator s) -> {
				gyroDelayField.ifPresent((JTextField f) -> {
					Integer value = parseDelayField(f.getText());
					if (value != null) {
						s.setGyroDelay(value);
					}
					else {
						success[0] = false;
					}
				});
				
				rollActuatorDelayField.ifPresent((JTextField f) -> {
					Integer value = parseDelayField(f.getText());
					if (value != null) {
						s.setActuatorDelay(RotationAxis.ROLL, value);
					}
					else {
						success[0] = false;
					}
				});
				
				pitchActuatorDelayField.ifPresent((JTextField f) -> {
					Integer value = parseDelayField(f.getText());
					if (value != null) {
						s.setActuatorDelay(RotationAxis.PITCH, value);
					}
					else {
						success[0] = false;
					}
				});
				
				yawActuatorDelayField.ifPresent((JTextField f) -> {
					Integer value = parseDelayField(f.getText());
					if (value != null) {
						s.setActuatorDelay(RotationAxis.YAW, value);
					}
					else {
						success[0] = false;
					}
				});
			});
			
			if (success[0]) {
				gyroDelayField.ifPresent((JTextField f) -> f.setEnabled(false));
				rollActuatorDelayField.ifPresent((JTextField f) -> f.setEnabled(false));
				pitchActuatorDelayField.ifPresent((JTextField f) -> f.setEnabled(false));
				yawActuatorDelayField.ifPresent((JTextField f) -> f.setEnabled(false));
			}
			
			return success[0];
		}
		
		private Integer parseDelayField(String fieldText) {
			try {
				Integer value = Integer.parseUnsignedInt(fieldText);

				if (value.compareTo(1) >= 0) {
					return value;
				}
			}
			catch (NumberFormatException e) {
				e.printStackTrace(System.err);
			}
			
			JOptionPane.showMessageDialog(
				mainScreen.get(),
				String.format("Invalid positive integer: %s.\nPlease enter a valid value greater than 1", fieldText),
				"Number Error!",
				JOptionPane.ERROR_MESSAGE
			);
			
			return null;
		}
		
		public ControllerType getControllerType() {
			if (controllerPicker.isPresent()) {
				int index = controllerPicker.get().getSelectedIndex();
				
				if (index > -1) {
					return (ControllerType) controllerPicker.get().getSelectedItem();
				}
			}
			return ControllerType.None;
		}
		
		public void registerControllerPicker(JComboBox<ControllerType> _controllerPicker) {
			assert(_controllerPicker != null);
			
			controllerPicker = Optional.of(_controllerPicker);
		}
		
		public void registerSimulator(Simulator _sim) {
			assert(_sim != null);

			sim = Optional.of(_sim);
		}
		
		public void registerGyroDelayField(JTextField _gyroDelayField) {
			assert(_gyroDelayField != null);
			
			gyroDelayField = Optional.of(_gyroDelayField);
		}
		
		public void registerActuatorDelayField(RotationAxis axis, JTextField actuatorDelayField) {
			assert(actuatorDelayField != null);
			
			if (axis == RotationAxis.ROLL) {
				rollActuatorDelayField = Optional.of(actuatorDelayField);
			}
			else if (axis == RotationAxis.PITCH) {
				pitchActuatorDelayField = Optional.of(actuatorDelayField);
			}
			else if (axis == RotationAxis.YAW) {
				yawActuatorDelayField = Optional.of(actuatorDelayField);
			}
		}
		
		public void registerMainScreen(JFrame _mainScreen) {
			assert(_mainScreen != null);
			
			mainScreen = Optional.of(_mainScreen);
		}
	}
	
	static class StartStopButtonListener extends SimulationStarter {
		private Optional<JButton> startStopButton;
		private Optional<JButton> singleStepButton;
		private Optional<JButton> multiStepButton;
		private Optional<JSpinner> stepPicker;
		
		public StartStopButtonListener() {
			super();
			startStopButton = Optional.empty();
			singleStepButton = Optional.empty();
			multiStepButton = Optional.empty();
			stepPicker = Optional.empty();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getID() == ActionEvent.ACTION_PERFORMED) {
				startStopButton.ifPresent((JButton ssb) -> {
					if(ssb.getText().contentEquals("Start")) {
				        if (!setSimulationDelays()) {
				        	return;
				        }
				        setController();
						controllerPicker.ifPresent((JComboBox<ControllerType> b) -> b.setEnabled(false));
					}
					
					if (ssb.getText().contentEquals("Start") || ssb.getText().contentEquals("Resume")) {
						sim.ifPresent((Simulator s) -> s.unpause());
						ssb.setText("Pause");
						
						singleStepButton.ifPresent((JButton stb) -> stb.setEnabled(false));
						multiStepButton.ifPresent((JButton msb) -> msb.setEnabled(false));
						stepPicker.ifPresent((JSpinner picker) -> picker.setEnabled(false));
					}
					else if (ssb.getText().contentEquals("Pause")) {
						sim.ifPresent((Simulator s) -> s.pause());
						ssb.setText("Resume");
						
						singleStepButton.ifPresent((JButton stb) -> stb.setEnabled(true));
						multiStepButton.ifPresent((JButton msb) -> msb.setEnabled(true));
						stepPicker.ifPresent((JSpinner picker) -> picker.setEnabled(true));
					}
				});
			}
		}
		
		public void registerStartStopButton(JButton _startStopButton) {
			assert(_startStopButton != null);

			startStopButton = Optional.of(_startStopButton);
		}
		
		public void registerSingleStepButton(JButton _singleStepButton) {
			assert(_singleStepButton != null);

			singleStepButton = Optional.of(_singleStepButton);
		}
		
		public void registerMultiStepButton(JButton _multiStepButton) {
			assert(_multiStepButton != null);
			
			multiStepButton = Optional.of(_multiStepButton);
		}
		
		public void registerStepPicker(JSpinner _stepPicker) {
			assert(_stepPicker != null);
			
			stepPicker = Optional.of(_stepPicker);
		}
	}
	
	public static final StartStopButtonListener startStopButtonListener = new StartStopButtonListener();
	
	static class SingleStepButtonListener extends SimulationStarter {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getID() == ActionEvent.ACTION_PERFORMED) {
				if (!setSimulationDelays()) {
					return;
				}
				setController();
				sim.ifPresent((Simulator s) -> {
					Thread t = new Thread(() -> s.tick());
					t.start();
					mainScreenStateListener.addThread(t);
				});
				controllerPicker.ifPresent((JComboBox<ControllerType> box) -> box.setEnabled(false));
			}
		}
	}
	
	public static final SingleStepButtonListener singleStepButtonListener = new SingleStepButtonListener();
	
	static class MultiStepButtonListener extends SimulationStarter {
		private Optional<JSpinner> stepPicker;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getID() == ActionEvent.ACTION_PERFORMED) {
				if (!setSimulationDelays()) {
					return;
				}
				setController();
				sim.ifPresent((Simulator simulator) -> {
					stepPicker.ifPresent((JSpinner stepSpinner) -> {
						final int numSteps = (Integer) stepSpinner.getValue();
						Thread t = new Thread(() -> {
							for (int i = 0; i < numSteps; ++i) {
								simulator.tick();
							}
						});
						t.start();
						mainScreenStateListener.addThread(t);
					});
				});
				controllerPicker.ifPresent((JComboBox<ControllerType> box) -> box.setEnabled(false));
			}
		}
		
		public void registerStepPicker(JSpinner _stepPicker) {
			assert(_stepPicker != null);
			
			stepPicker = Optional.of(_stepPicker);
		}
	}
	
	public static final MultiStepButtonListener multiStepButtonListener = new MultiStepButtonListener();
	
	static class AddEventButtonListener implements ActionListener {
		private JFrame mainScreen;
		private JTextField[] accelerationFields;
		private JTextField durationField;
		private JTextField timeField;
		
		private MainScreenModel model;

		AddEventButtonListener() {
			mainScreen = null;
			accelerationFields = new JTextField[3];
			durationField = null;
			timeField = null;
			
			model = null;
		}
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			if (ev.getID() == ActionEvent.ACTION_PERFORMED) {
				ArrayList<Optional<Double>> accelerationValues = processAccelerationFields();
				Optional<Integer> durationValue = processUnsignedIntegerField(durationField);
				Optional<Integer> timeValue = processUnsignedIntegerField(timeField);
				
				if (accelerationValues != null
						&& durationValue.isPresent()
						&& timeValue.isPresent()) {
					setAccelerationValues(accelerationValues);
					setStartTime(timeValue.get());
					setDuration(durationValue.get());
					model.accelerationEventData.sendToEventManager();
				}
			}
		}
		
		private ArrayList<Optional<Double>> processAccelerationFields() {
			ArrayList<Optional<Double>> fieldValues = new ArrayList<Optional<Double>>();
			for (int i = 0; i < accelerationFields.length; ++i) {
				JTextField accelerationField = accelerationFields[i];
				if (accelerationField != null) {
					String fieldText = accelerationField.getText();
					
					if (!fieldText.isEmpty()) {
						Double value = parseDoubleField(fieldText);
						
						if (value == null) {
							return null;
						}
						
						fieldValues.add(Optional.of(value));
					}
					else {
						fieldValues.add(Optional.empty());
					}
				}
			}
			return fieldValues;
		}
		
		private void setAccelerationValues(ArrayList<Optional<Double>> accelerationValues) {
			for (int i = 0; i < 3; ++i) {
				final int index = i;
				Double value = accelerationValues.get(index).orElse(Double.valueOf(0.0d));

				RotationAxis type = RotationAxis.values()[i];

				if (type == RotationAxis.ROLL) {
					model.accelerationEventData.rollAcceleration = value;
				}
				else if (type == RotationAxis.PITCH) {
					model.accelerationEventData.pitchAcceleration = value;
				}
				else if (type == RotationAxis.YAW) {
					model.accelerationEventData.yawAcceleration = value;
				}
				System.out.println(String.format("%s Acceleration entered: %f", type.toString(), value));
			}
		}
		
		private void setStartTime(int startTime) {
			model.accelerationEventData.startTime = startTime;
			System.out.println(String.format("Start time entered: %d", startTime));
		}
		
		private void setDuration(int duration) {
			model.accelerationEventData.duration = duration;
			System.out.println(String.format("Duration entered: %d", duration));
		}
		
		private Double parseDoubleField(String fieldText) {
			try {
				Double value = Double.parseDouble(fieldText);

				return value;
			}
			catch (NumberFormatException e) {
				e.printStackTrace(System.err);

				JOptionPane.showMessageDialog(
					mainScreen,
					String.format("Invalid number: %s", fieldText),
					"Number Error!",
					JOptionPane.ERROR_MESSAGE
				);
				
				return null;
			}
		}
		
		private Optional<Integer> processUnsignedIntegerField(JTextField integerField) {
			Integer i = parseUnsignedIntegerField(integerField.getText());
			
			return Optional.ofNullable(i);
		}
		
		private Integer parseUnsignedIntegerField(String fieldText) {
			try {
				Integer value = Integer.parseUnsignedInt(fieldText);

				return value;
			}
			catch (NumberFormatException e) {
				e.printStackTrace(System.err);

				JOptionPane.showMessageDialog(
					mainScreen,
					String.format("Invalid positive integer: %s", fieldText),
					"Number Error!",
					JOptionPane.ERROR_MESSAGE
				);
				
				return null;
			}
		}

		public AddEventButtonListener registerAccelerationField(JTextField accelerationField, RotationAxis axis) {
			this.accelerationFields[axis.ordinal()] = accelerationField;
			
			return this;
		}
		
		public AddEventButtonListener registerMainScreen(JFrame mainScreen) {
			this.mainScreen = mainScreen;
			
			return this;
		}
		
		public AddEventButtonListener registerMainScreenModel(MainScreenModel model) {
			this.model = model;
			
			return this;
		}
		
		public AddEventButtonListener registerDurationField(JTextField durationField) {
			this.durationField = durationField;
			
			return this;
		}
		
		public AddEventButtonListener registerTimeField(JTextField timeField) {
			this.timeField = timeField;
			
			return this;
		}
	}
	
	public static final AddEventButtonListener addEventButtonListener = new AddEventButtonListener();
	
	static class OpenWorldDataScreenListener implements ActionListener {
		private Optional<JDialog> worldDataScreen;
		
		public OpenWorldDataScreenListener() {
			worldDataScreen = Optional.empty();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getID() == ActionEvent.ACTION_PERFORMED) {
				worldDataScreen.ifPresent((JDialog wds) -> wds.setVisible(true));
			}
		}
		
		public void registerWorldDataScreen(JDialog _worldDataScreen) {
			assert(_worldDataScreen != null);
			worldDataScreen = Optional.of(_worldDataScreen);
		}
		
	}
	
	public static final OpenWorldDataScreenListener openWorldDataScreenListener = new OpenWorldDataScreenListener();
	
}
