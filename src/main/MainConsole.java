package main;

import building.Building;
import building.BuildingInterface;
import controller.BuildingController;
import javax.swing.JFrame;
import views.BuildingView;
import views.BuildingViewInterface;

/**
 * This class is the entry point of the program.
 * It creates the building, view, and controller objects.
 */
public class MainConsole {

  /**
   * This method is the entry point of the program.
   * It creates the building, view, and controller objects.
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    int numFloors = 10;
    int numElevators = 8;
    int numCapacity = 5;
    BuildingInterface building = new Building(numFloors, numElevators, numCapacity);
    BuildingViewInterface view = new BuildingView();
    BuildingController controller = new BuildingController(building, view);
    view.setController(controller);

    ((JFrame) view).setVisible(true);
  }
}
