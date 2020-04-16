package CSE564_Project_Spring2020.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
				
				if (accelerationValues != null) {
					setAccelerationValues(accelerationValues);
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
						Double value = parseField(fieldText);
						
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
				accelerationValues.get(index).ifPresent((Double value) -> {
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
				});
			}
		}
		
		private Double parseField(String fieldText) {
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
