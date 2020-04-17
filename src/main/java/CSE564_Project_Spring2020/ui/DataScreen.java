package CSE564_Project_Spring2020.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DataScreen {
	private JFrame mainScreen;
	private String title;
	
	public DataScreen(JFrame _mainScreen, String _title) {
		mainScreen = _mainScreen;
		title = _title;
	}
	
	public JDialog buildDialog() {
		JDialog screen = new JDialog(mainScreen, title);
		screen.setSize(300, 200);
		
		screen.add(buildDataPanel());
		
		return screen;
	}
	
	private JPanel buildDataPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(makeDataDisplayField("Current Time", "ms", DataType.WorldTime), gridLocation(0, 0));
		panel.add(makeDataDisplayField("Roll", "deg", DataType.WorldRoll), gridLocation(0, 1));
		panel.add(makeDataDisplayField("Pitch", "deg", DataType.WorldPitch), gridLocation(0, 2));
		panel.add(makeDataDisplayField("Yaw", "deg", DataType.WorldYaw), gridLocation(0, 3));
		
		return panel;
	}
	
    public JPanel makeDataDisplayField(final String name, final String units, final DataType type) {
    	JPanel displayField = new JPanel();
    	
    	JLabel fieldName = new JLabel(String.format("%s: ", name));
    	JLabel data = new JLabel();
    	JLabel unitsLabel = new JLabel(units);
    	
    	if (type == DataType.WorldTime) {
        	DataScreenController.worldDataListener.registerTimeLabel(data);
    	}
    	else if (type == DataType.WorldRoll) {
    		DataScreenController.worldDataListener.registerRollLabel(data);
    	}
    	else if (type == DataType.WorldPitch) {
    		DataScreenController.worldDataListener.registerPitchLabel(data);
    	}
    	else if (type == DataType.WorldYaw) {
    		DataScreenController.worldDataListener.registerYawLabel(data);
    	}
    	
    	displayField.add(fieldName);
    	displayField.add(data);
    	displayField.add(unitsLabel);
    	
    	return displayField;
    }
	
    private GridBagConstraints gridLocation(int x, int y)
    {
    	GridBagConstraints newConstraint = new GridBagConstraints();
    	newConstraint.anchor = GridBagConstraints.PAGE_START;
    	newConstraint.gridx = x;
    	newConstraint.gridy = y;
    	
    	return newConstraint;
    }
}
