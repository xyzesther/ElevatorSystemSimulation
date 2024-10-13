package views;

import building.BuildingReport;
import controller.BuildingController;
import elevator.ElevatorReport;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * This class is responsible for displaying the building and elevator system in a Swing GUI.
 */
public class BuildingView extends JFrame implements BuildingViewInterface {
  private BuildingController buildingController;
  private JPanel mainPanel;
  private JPanel reportPanel;
  private JPanel elevatorStatusPanel;
  private JPanel controlPanel;
  private JButton startButton;
  private JButton stepButton;
  private JButton stopButton;
  private JButton requestButton;
  private JTextField startFloorField;
  private JTextField endFloorField;
  private JLabel errorLabel;

  /**
   * This constructor is used to create a new BuildingView object.
   */
  public BuildingView() {
    super("Building Elevator System");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 750);
    setLayout(new BorderLayout());
    initializeUi();
  }

  private void initializeUi() {
    mainPanel = new JPanel();
    reportPanel = new JPanel();
    elevatorStatusPanel = new JPanel();
    controlPanel = new JPanel();

    // stacking components vertically.
    mainPanel.setLayout(new GridLayout(0, 1));
    // stacking components horizontally.
    elevatorStatusPanel.setLayout(new GridLayout(1, 0));
    // arrange a left-to-right flow, wrapping to the next line when space runs out
    controlPanel.setLayout(new FlowLayout());
    reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
    reportPanel.setPreferredSize(new Dimension(350, getHeight()));

    JPanel elevatorsContainer = new JPanel(new BorderLayout());
    elevatorsContainer.add(elevatorStatusPanel, BorderLayout.NORTH);
    elevatorsContainer.add(mainPanel, BorderLayout.CENTER);

    add(elevatorsContainer, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.SOUTH);
    add(reportPanel, BorderLayout.EAST);

    initializeControlPanel();
    initializeReportPanel();
  }

  private void initializeControlPanel() {
    startButton = new JButton("Start");
    stepButton = new JButton("Step");
    stopButton = new JButton("Stop");
    requestButton = new JButton("Request");

    startFloorField = new JTextField(5);
    endFloorField = new JTextField(5);

    controlPanel.add(startButton);
    controlPanel.add(stepButton);
    controlPanel.add(stopButton);
    controlPanel.add(new JLabel("From:"));
    controlPanel.add(startFloorField);
    controlPanel.add(new JLabel("To:"));
    controlPanel.add(endFloorField);
    controlPanel.add(requestButton);

    startButton.addActionListener(e -> buildingController.startElevatorSystem());
    stepButton.addActionListener(e -> buildingController.stepElevatorSystem());
    stopButton.addActionListener(e -> buildingController.stopElevatorSystem());
    requestButton.addActionListener(e -> {
      try {
        int startFloor = Integer.parseInt(startFloorField.getText());
        int endFloor = Integer.parseInt(endFloorField.getText());
        buildingController.processRequest(startFloor, endFloor);
      } catch (NumberFormatException ex) {
        displayError("Invalid input: Please enter valid numbers for floors.");
      }
    });
  }

  private void initializeReportPanel() {
    JLabel reportTitle = new JLabel("Building Report", SwingConstants.CENTER);
    reportPanel.add(reportTitle);

    errorLabel = new JLabel("");
    errorLabel.setForeground(Color.RED);
    errorLabel.setVisible(false);
    reportPanel.add(errorLabel);
  }

  @Override
  public void setController(BuildingController controller) {
    this.buildingController = controller;
  }

  @Override
  public void initializeFloors(int numFloors, int numElevators) {
    mainPanel.removeAll();
    elevatorStatusPanel.removeAll();

    mainPanel.setLayout(new GridLayout(numFloors, numElevators));
    elevatorStatusPanel.setLayout(new GridLayout(1, numElevators));

    for (int i = 0; i < numElevators; i++) {
      elevatorStatusPanel.add(new JLabel("Elevator " + (i + 1)));
      for (int j = 0; j < numFloors; j++) {
        JPanel floorPanel = new JPanel();
        floorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        floorPanel.setBackground(Color.WHITE);
        mainPanel.add(floorPanel);
      }
    }
    revalidate();
    repaint();
  }

  @Override
  public void updateView(BuildingReport report) {
    clearError();
    updateElevatorDisplays(report);
    updateReportPanel(report);
  }

  private void updateElevatorDisplays(BuildingReport report) {
    ElevatorReport[] elevatorReports = report.getElevatorReports();
    int numFloors = report.getNumFloors();
    int numElevators = report.getNumElevators();

    Component[] panels = mainPanel.getComponents();
    for (Component panel : panels) {
      panel.setBackground(Color.WHITE);
    }

    for (int i = 0; i < elevatorReports.length; i++) {
      int floor = elevatorReports[i].getCurrentFloor();
      int index = (numFloors - floor - 1) * numElevators + i;
      panels[index].setBackground(Color.BLACK);
    }
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void updateReportPanel(BuildingReport report) {
    reportPanel.removeAll();

    JTextArea reportArea = new JTextArea(10, 30);
    reportArea.setText(report.toString());
    reportArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(reportArea);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    reportPanel.add(errorLabel);

    reportPanel.add(scrollPane);
    reportPanel.revalidate();
    reportPanel.repaint();
  }

  private void clearError() {
    if (errorLabel != null) {
      errorLabel.setText("");
      errorLabel.setVisible(false);
      reportPanel.revalidate();
      reportPanel.repaint();
    }
  }

  @Override
  public void displayError(String errorMessage) {
    errorLabel.setText(errorMessage);
    errorLabel.setVisible(true);
    reportPanel.revalidate();
    reportPanel.repaint();
  }

}
