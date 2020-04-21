package CSE564_Project_Spring2020.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import CSE564_Project_Spring2020.sim.ControllerType;
import CSE564_Project_Spring2020.sim.RotationAxis;
import CSE564_Project_Spring2020.sim.Simulator;


public class MainScreen {
	private final MainScreenModel model;
	
    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();
        JFrame mainScreenFrame = mainScreen.buildFrame();
        mainScreen.openSubscreens(mainScreenFrame);
        
        MainScreenController.startStopButtonListener.registerMainScreen(mainScreenFrame);
        MainScreenController.singleStepButtonListener.registerMainScreen(mainScreenFrame);
        MainScreenController.multiStepButtonListener.registerMainScreen(mainScreenFrame);
        
        mainScreenFrame.setVisible(true);
        
        Simulator s = new Simulator();
        
        s.setWorldDataListener(DataScreenController.worldDataListener);
        s.setGyroscopeDataListener(DataScreenController.gyroDataListener);
        s.setActuatorDataListener(DataScreenController.actuatorDataListener);
        
        MainScreenController.mainScreenStateListener.registerSimulator(s);
        MainScreenController.startStopButtonListener.registerSimulator(s);
        MainScreenController.singleStepButtonListener.registerSimulator(s);
        MainScreenController.multiStepButtonListener.registerSimulator(s);

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
        framePanel.add(buildEventControlPanel(), gridLocation(0, 1));
        framePanel.add(buildReopenDataScreenPanel(), gridLocation(0, 2));
        newFrame.add(framePanel);
        
        newFrame.setSize(400, 600);
        
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
    
    public JPanel buildSimControlPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(makeControllerPicker(), gridLocation(0, 0));
    	newPanel.add(makeDelayForm(), gridLocation(0, 1));
    	newPanel.add(makeSimButtonPanel(), gridLocation(0, 2));
    	newPanel.add(makeMultiStepButton(), gridLocation(0, 3));
    	
    	newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	return newPanel;
    }
    
    public JPanel buildEventControlPanel() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(new JLabel("Define new event"), gridLocation(0, 0));
    	newPanel.add(makeTimeField(), gridLocation(0, 1));
    	newPanel.add(makeDurationField(), gridLocation(0, 2));
    	newPanel.add(makeAccelerationField(RotationAxis.ROLL), gridLocation(0, 3));
    	newPanel.add(makeAccelerationField(RotationAxis.PITCH), gridLocation(0, 4));
    	newPanel.add(makeAccelerationField(RotationAxis.YAW), gridLocation(0, 5));
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
    
    public JPanel makeControllerPicker() {
    	JPanel newPanel = new JPanel();
    	
    	JLabel pickerLabel = new JLabel("Choose controller:");
    	JComboBox<ControllerType> controllerPicker = new JComboBox<>(ControllerType.values());
    	
    	newPanel.add(pickerLabel);
    	newPanel.add(controllerPicker);
    	
    	MainScreenController.startStopButtonListener.registerControllerPicker(controllerPicker);
    	MainScreenController.singleStepButtonListener.registerControllerPicker(controllerPicker);
    	MainScreenController.multiStepButtonListener.registerControllerPicker(controllerPicker);
    	
    	return newPanel;
    }
    
    public JPanel makeDelayForm() {
    	JPanel newPanel = new JPanel(new GridBagLayout());
    	
    	newPanel.add(makeGyroDelayField(), gridLocation(0, 0));
    	newPanel.add(makeActuatorDelayField(RotationAxis.ROLL), gridLocation(0, 1));
    	newPanel.add(makeActuatorDelayField(RotationAxis.PITCH), gridLocation(0, 2));
    	newPanel.add(makeActuatorDelayField(RotationAxis.YAW), gridLocation(0, 3));
    	
    	return newPanel;
    }
    
    public JPanel makeGyroDelayField() {
    	JPanel newPanel = new JPanel();
    	
    	newPanel.add(new JLabel("Gyro delay:"));
    	
    	JTextField gyroDelayField = new JTextField(10);
    	gyroDelayField.setText("1");
    	
    	MainScreenController.startStopButtonListener.registerGyroDelayField(gyroDelayField);
    	MainScreenController.singleStepButtonListener.registerGyroDelayField(gyroDelayField);
    	MainScreenController.multiStepButtonListener.registerGyroDelayField(gyroDelayField);
    	
    	newPanel.add(gyroDelayField);
    	
    	newPanel.add(new JLabel("ms"));
    	
    	return newPanel;
    }
    
    public JPanel makeActuatorDelayField(RotationAxis axis) {
    	JPanel newPanel = new JPanel();
    	
    	String name = String.format("%c%s", axis.toString().charAt(0), axis.toString().substring(1).toLowerCase());
    	newPanel.add(new JLabel(String.format("%s Actuator delay:", name)));
    	
    	JTextField actuatorDelayField = new JTextField(10);
    	actuatorDelayField.setText("1");
    	
    	MainScreenController.startStopButtonListener.registerActuatorDelayField(axis, actuatorDelayField);
    	MainScreenController.singleStepButtonListener.registerActuatorDelayField(axis, actuatorDelayField);
    	MainScreenController.multiStepButtonListener.registerActuatorDelayField(axis, actuatorDelayField);
    	
    	newPanel.add(actuatorDelayField);
    	
    	newPanel.add(new JLabel("ms"));
    	
    	return newPanel;
    }
    
    public JPanel makeSimButtonPanel() {
    	JPanel newPanel = new JPanel();
    	
    	newPanel.add(makeStartStopButton());
    	newPanel.add(makeStepButton());
    	
    	return newPanel;
    }
    
    public JButton makeStartStopButton() {
    	JButton startStopButton = new JButton("Start");
    	
    	startStopButton.addActionListener(MainScreenController.startStopButtonListener);
    	MainScreenController.startStopButtonListener.registerStartStopButton(startStopButton);
    	
    	return startStopButton;
    }
    
    public JButton makeStepButton() {
    	JButton stepButton = new JButton("Single Step");
    	
    	stepButton.addActionListener(MainScreenController.singleStepButtonListener);
    	MainScreenController.startStopButtonListener.registerSingleStepButton(stepButton);
    	
    	return stepButton;
    }
    
    public JPanel makeMultiStepButton() {
    	JPanel newPanel = new JPanel();
    	
    	JButton stepButton = new JButton("Execute Steps");
    	SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
    	JSpinner numberPicker = new JSpinner(numberModel);
    	
    	stepButton.addActionListener(MainScreenController.multiStepButtonListener);
    	MainScreenController.startStopButtonListener.registerMultiStepButton(stepButton);
    	MainScreenController.startStopButtonListener.registerStepPicker(numberPicker);
    	MainScreenController.multiStepButtonListener.registerStepPicker(numberPicker);
    	
    	newPanel.add(stepButton);
    	newPanel.add(numberPicker);
    	
    	return newPanel;
    }
    
    public JButton makeAddEventButton() {
    	JButton btn = new JButton("Add Event");
    	
    	btn.addActionListener(MainScreenController.addEventButtonListener);
    	
    	return btn;
    }
    
    public JPanel makeAccelerationField(RotationAxis axis) {
    	JPanel accelerationPanel = new JPanel();
    	
    	String name = String.format("%c%s", axis.toString().charAt(0), axis.toString().substring(1).toLowerCase());
    	JLabel accelerationLabel = new JLabel(String.format("%s Acceleration: ", name));
    	JTextField accelerationField = new JTextField(10);
    	JLabel unitsLabel = new JLabel("deg/ms");
    	
    	MainScreenController.addEventButtonListener.registerAccelerationField(accelerationField, axis);
    	
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
}
