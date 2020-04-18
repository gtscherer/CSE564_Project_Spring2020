package CSE564_Project_Spring2020.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import CSE564_Project_Spring2020.sim.Controller;
import CSE564_Project_Spring2020.sim.ControllerFactory;
import CSE564_Project_Spring2020.sim.ControllerType;
import CSE564_Project_Spring2020.sim.Simulator;

public class MainScreenController {
	public enum AccelerationType {
		ROLL(0),
		PITCH(1),
		YAW(2);
		
		private int i;
		AccelerationType(int i) {
			this.i = i;
		}
		
		public int getIndex() {
			return i;
		}
		
		public String getText() {
			if (i == 0) {
				return "Roll";
			}
			else if (i == 1) {
				return "Pitch";
			}
			else if (i == 2) {
				return "Yaw";
			}
			return "";
		}
		
		public static AccelerationType getType(int index) {
			if (index == 0) {
				return ROLL;
			}
			else if (index == 1) {
				return PITCH;
			}
			else if (index == 2) {
				return YAW;
			}
			return null;
		}
	}
	
	static class MainScreenStateListener implements WindowStateListener {
		private Optional<Simulator> sim;
		
		public MainScreenStateListener() {
			sim = Optional.empty();
		}

		@Override
		public void windowStateChanged(WindowEvent e) {
			if (e.getNewState() == WindowEvent.WINDOW_CLOSED) {
				sim.ifPresent((Simulator s) -> {
					s.interrupt();
				});
			}
		}
		
		public void registerSimulator(Simulator _sim) {
			assert(_sim != null);
			sim = Optional.of(_sim);
		}
		
	}
	
	public static final MainScreenStateListener mainScreenStateListener = new MainScreenStateListener();

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
	
	static class StartStopButtonListener implements ActionListener {
		private Optional<JButton> startStopButton;
		private Optional<Simulator> sim;
		private Optional<JComboBox<ControllerType>> controllerPicker;
		
		public StartStopButtonListener() {
			startStopButton = Optional.empty();
			sim = Optional.empty();
			controllerPicker = Optional.empty();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getID() == ActionEvent.ACTION_PERFORMED) {
				startStopButton.ifPresent((JButton ssb) -> {
					if(ssb.getText().contentEquals("Start")) {
						controllerPicker.ifPresent((JComboBox<ControllerType> b) -> b.setEnabled(false));
				        ControllerType controllerType = getControllerType();
				        
				        if (controllerType != ControllerType.NoController) {
				        	sim.ifPresent((Simulator s) -> ControllerFactory.CreateController(controllerType).ifPresent((Controller c) -> s.setRollController(c)));
				        	sim.ifPresent((Simulator s) -> ControllerFactory.CreateController(controllerType).ifPresent((Controller c) -> s.setPitchController(c)));
				        	sim.ifPresent((Simulator s) -> ControllerFactory.CreateController(controllerType).ifPresent((Controller c) -> s.setYawController(c)));
				        }
					}
					
					if (ssb.getText().contentEquals("Start") || ssb.getText().contentEquals("Resume")) {
						sim.ifPresent((Simulator s) -> s.unpause());
						ssb.setText("Pause");
					}
					else if (ssb.getText().contentEquals("Pause")) {
						sim.ifPresent((Simulator s) -> {
							try {
								s.pause();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						});
						ssb.setText("Resume");
					}
				});
			}
		}
		
		public void registerStartStopButton(JButton _startStopButton) {
			assert(_startStopButton != null);
			startStopButton = Optional.of(_startStopButton);
		}
		
		public void registerSimulator(Simulator _sim) {
			assert(_sim != null);
			sim = Optional.of(_sim);
		}
		
		public void registerControllerPicker(JComboBox<ControllerType> _controllerPicker) {
			assert(_controllerPicker != null);
			
			controllerPicker = Optional.of(_controllerPicker);
		}
		
		public ControllerType getControllerType() {
			if (controllerPicker.isPresent()) {
				int index = controllerPicker.get().getSelectedIndex();
				
				if (index > -1) {
					return (ControllerType) controllerPicker.get().getSelectedItem();
				}
			}
			return ControllerType.NoController;
		}
	}
	
	public static final StartStopButtonListener startStopButtonListener = new StartStopButtonListener();
	
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

				AccelerationType type = AccelerationType.getType(index);

				if (type == AccelerationType.ROLL) {
					model.accelerationEventData.rollAcceleration = value;
				}
				else if (type == AccelerationType.PITCH) {
					model.accelerationEventData.pitchAcceleration = value;
				}
				else if (type == AccelerationType.YAW) {
					model.accelerationEventData.yawAcceleration = value;
				}
				System.out.println(String.format("%s Acceleration entered: %f", type.getText(), value));
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

		public AddEventButtonListener registerAccelerationField(JTextField accelerationField, AccelerationType type) {
			this.accelerationFields[type.getIndex()] = accelerationField;
			
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
	
}
