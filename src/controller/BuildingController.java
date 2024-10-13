package controller;

import building.BuildingInterface;
import building.BuildingReport;
import javax.swing.SwingUtilities;
import scanerzus.Request;
import views.BuildingViewInterface;


/**
 * This class is responsible for controlling the elevator system and managing interactions
 * between the model (building) and the view (Swing GUI).
 */
public class BuildingController implements BuildingControllerInterface {
  private BuildingInterface building;
  private BuildingViewInterface view;

  /**
   * The constructor is used to create a new BuildingController object.
   * @param building the building object.
   * @param view the view object.
   */
  public BuildingController(BuildingInterface building, BuildingViewInterface view) {
    this.building = building;
    this.view = view;
    this.view.setController(this);
    initializeView();
    updateView();
  }

  private void initializeView() {
    int numFloors = building.getNumOfFloors();
    int numElevators = building.getNumOfElevators();
    SwingUtilities.invokeLater(() -> {
      view.initializeFloors(numFloors, numElevators);
    });
  }

  @Override
  public void startElevatorSystem() {
    try {
      building.startElevatorSystem();
      updateView();
    } catch (IllegalStateException e) {
      view.displayError(e.getMessage());
    }
  }

  @Override
  public void stopElevatorSystem() {
    try {
      building.stopElevatorSystem();
      updateView();
    } catch (IllegalStateException e) {
      view.displayError(e.getMessage());
    }
  }

  @Override
  public void stepElevatorSystem() {
    try {
      building.stepElevatorSystem();
      updateView();
    } catch (IllegalStateException e) {
      view.displayError(e.getMessage());
    }
  }

  private void updateView() {
    BuildingReport report = building.getBuildingReport();
    SwingUtilities.invokeLater(() -> {
      view.updateView(report);
    });
  }

  @Override
  public void processRequest(int startFloor, int endFloor) {
    try {
      Request request = new Request(startFloor, endFloor);
      building.addRequest(request);
      view.updateView(building.getBuildingReport());
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.displayError(e.getMessage());
    }
  }
}
