package views;

import building.BuildingReport;
import controller.BuildingController;

/**
 * This interface is implemented by BuildingView class.
 */
public interface BuildingViewInterface {

  /**
   * This method is called by the controller to set itself in the view.
   * @param controller the controller object.
   */
  void setController(BuildingController controller);

  /**
   * This method is called by the controller to update the view.
   * @param report the building report.
   */
  void updateView(BuildingReport report);

  /**
   * This method is called by the controller to display an error message.
   * @param errorMessage the error message.
   */
  void displayError(String errorMessage);

  /**
   * This method is called by the controller to initialize the elevators in the view.
   * @param numFloors the number of floors.
   * @param numElevators the number of elevators.
   */
  void initializeFloors(int numFloors, int numElevators);
}
