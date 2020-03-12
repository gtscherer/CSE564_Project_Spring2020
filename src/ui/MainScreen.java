package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainScreen {
	private MainScreenModel model;
	
    public static void main(String[] args) {
        new MainScreen().buildFrame().setVisible(true);
    }
    
    public MainScreen()
    {
    	model = new MainScreenModel();
    }

    public JFrame buildFrame() {
        JFrame newFrame = new JFrame("Environment Control");
        
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        newFrame.add(buildControlPanel());
        
        newFrame.setSize(500, 500);
        
        MainScreenController.addEventButtonListener.registerMainScreen(newFrame);
        MainScreenController.addEventButtonListener.registerMainScreenModel(model);

        return newFrame;
    }
    
    public JPanel buildControlPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(makeAddEventButton(), gridLocation(0, 0));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.ROLL), gridLocation(0, 1));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.PITCH), gridLocation(0, 2));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.YAW), gridLocation(0, 3));
    	
    	return newPanel;
    }
    
    public GridBagConstraints gridLocation(int x, int y)
    {
    	GridBagConstraints newConstraint = new GridBagConstraints();
    	newConstraint.anchor = GridBagConstraints.PAGE_START;
    	newConstraint.gridx = x;
    	newConstraint.gridy = y;
    	
    	return newConstraint;
    }
    
    public JButton makeAddEventButton() {
    	JButton btn = new JButton("Add Event");
    	
    	btn.addActionListener(MainScreenController.addEventButtonListener);
    	
    	return btn;
    }
    
    public JPanel makeAccelerationField(MainScreenController.AccelerationType type) {
    	JPanel accelerationPanel = new JPanel();
    	
    	JLabel accelerationLabel = new JLabel(String.format("%s Acceleration: ", type.getText()));
    	JTextField accelerationField = new JTextField(10);
    	JLabel unitsLabel = new JLabel("deg/s");
    	
    	MainScreenController.addEventButtonListener.registerAccelerationField(accelerationField, type);
    	
    	accelerationPanel.add(accelerationLabel);
    	accelerationPanel.add(accelerationField);
    	accelerationPanel.add(unitsLabel);
    	
    	return accelerationPanel;
    }
}
