package CSE564_Project_Spring2020.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		private MainScreenModel model;

		AddEventButtonListener()
		{
			accelerationFields = new JTextField[3];
		}
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			if (ev.getID() == ActionEvent.ACTION_PERFORMED)
			{
				for (int i = 0; i < accelerationFields.length; ++i) {
					JTextField accelerationField = accelerationFields[i];
					if (accelerationField != null)
					{
						String fieldText = accelerationField.getText();
						
						if (!fieldText.isEmpty())
						{
							try {
								double value = Double.parseDouble(fieldText);
								
								AccelerationType type = AccelerationType.getType(i);
								
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
							catch (NumberFormatException e) {
								e.printStackTrace(System.err);

								JOptionPane.showMessageDialog(
									mainScreen,
									String.format("Invalid number: %s", fieldText),
									"Number Error!",
									JOptionPane.ERROR_MESSAGE
								);	
							}
						}
					}
				}

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
	}
	
	public static final AddEventButtonListener addEventButtonListener = new AddEventButtonListener();
	
}
