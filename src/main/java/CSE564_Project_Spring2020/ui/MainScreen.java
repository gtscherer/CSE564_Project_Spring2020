package CSE564_Project_Spring2020.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CSE564_Project_Spring2020.sim.Simulator;


public class MainScreen {
	private MainScreenModel model;
	
    public static void main(String[] args) throws InterruptedException {
        MainScreen mainScreen = new MainScreen();
        JFrame mainScreenFrame = mainScreen.buildFrame();
        mainScreen.openSubscreens(mainScreenFrame);
        mainScreenFrame.setVisible(true);
        
        Simulator s = new Simulator();

        s.setWorldDataListener(DataScreenController.worldDataListener);
        s.setGyroscopeDataListener(DataScreenController.gyroDataListener);
        MainScreenController.mainScreenStateListener.registerSimulator(s);
        MainScreenController.startStopButtonListener.registerSimulator(s);

        mainScreen.getModel().accelerationEventData.registerWorldEventManager(s.getWorldEventManager());

        s.start();
        s.pause();
    }
    
    public MainScreen() {
    	model = new MainScreenModel();
    }
    
    public MainScreenModel getModel() {
    	return model;
    }

    public JFrame buildFrame() {
        JFrame newFrame = new JFrame("Environment Control");
        
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.addWindowStateListener(MainScreenController.mainScreenStateListener);
        
        JPanel framePanel = new JPanel(new GridBagLayout());
        framePanel.add(buildSimControlPanel(), gridLocation(0, 0));
        framePanel.add(buildControlPanel(), gridLocation(0, 1));
        framePanel.add(buildReopenDataScreenPanel(), gridLocation(0, 2));
        newFrame.add(framePanel);
        
        newFrame.setSize(400, 400);
        
        MainScreenController.addEventButtonListener.registerMainScreen(newFrame);
        MainScreenController.addEventButtonListener.registerMainScreenModel(model);

        return newFrame;
    }
    
    public void openSubscreens(JFrame mainScreen) {
    	DataScreen worldView = new DataScreen(mainScreen, "World View");
    	JDialog worldDataDialog = worldView.buildDialog();
    	MainScreenController.openWorldDataScreenListener.registerWorldDataScreen(worldDataDialog);
    	
    	worldDataDialog.setVisible(true);
    }
    
    public JPanel buildReopenDataScreenPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(makeReopenWorldDataScreenButton(), gridLocation(0, 0));
    	
    	return newPanel;
    }
    
    public JButton makeReopenWorldDataScreenButton() {
    	JButton reopenWorldDataScreenButton = new JButton("Reopen World View");
    	
    	reopenWorldDataScreenButton.addActionListener(MainScreenController.openWorldDataScreenListener);
    	
    	return reopenWorldDataScreenButton;
    }
    
    public JPanel buildSimControlPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(makeStartStopButton(), gridLocation(0, 0));
    	
    	return newPanel;
    }
    
    public JButton makeStartStopButton() {
    	JButton startStopButton = new JButton("Start");
    	
    	startStopButton.addActionListener(MainScreenController.startStopButtonListener);
    	MainScreenController.startStopButtonListener.registerStartStopButton(startStopButton);
    	
    	return startStopButton;
    }
    
    public JPanel buildControlPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(new JLabel("Define new event:"), gridLocation(0, 0));
    	newPanel.add(makeTimeField(), gridLocation(0, 1));
    	newPanel.add(makeDurationField(), gridLocation(0, 2));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.ROLL), gridLocation(0, 3));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.PITCH), gridLocation(0, 4));
    	newPanel.add(makeAccelerationField(MainScreenController.AccelerationType.YAW), gridLocation(0, 5));
    	newPanel.add(makeAddEventButton(), gridLocation(0, 6));

    	newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	return newPanel;
    }
    
    public GridBagConstraints gridLocation(int x, int y) {
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
    	JLabel unitsLabel = new JLabel("deg/ms");
    	
    	MainScreenController.addEventButtonListener.registerAccelerationField(accelerationField, type);
    	
    	accelerationPanel.add(accelerationLabel);
    	accelerationPanel.add(accelerationField);
    	accelerationPanel.add(unitsLabel);
    	
    	return accelerationPanel;
    }
    
    public JPanel makeDurationField() {
    	JPanel durationPanel = new JPanel();
    	
    	JLabel durationLabel = new JLabel("Duration: ");
    	JTextField durationField = new JTextField(10);
    	JLabel msLabel =  new JLabel("ms");
    	
    	MainScreenController.addEventButtonListener.registerDurationField(durationField);
    	
    	durationPanel.add(durationLabel);
    	durationPanel.add(durationField);
    	durationPanel.add(msLabel);
    	
    	return durationPanel;
    }
    
    public JPanel makeTimeField() {
    	JPanel timePanel = new JPanel();
    	
    	JLabel timeLabel = new JLabel("Start Time: ");
    	JTextField timeField = new JTextField(10);
    	JLabel msLabel = new JLabel("ms");
    	
    	MainScreenController.addEventButtonListener.registerTimeField(timeField);
    	
    	timePanel.add(timeLabel);
    	timePanel.add(timeField);
    	timePanel.add(msLabel);
    	
    	return timePanel;
    }
}
