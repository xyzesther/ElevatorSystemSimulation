package building;

import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {

  /**
   * This method is used to get the number of floors in the building.
   *
   * @return the number of floors in the building
   */
  int getNumOfFloors();

  /**
   * This method is used to get the number of elevators in the building.
   *
   * @return the number of elevators in the building
   */
  int getNumOfElevators();

  /**
   * This method is used to get the max occupancy of the elevator.
   *
   * @return the max occupancy of the elevator.
   */
  int getElevatorCapacity();

  /**
   * This method is used to start the elevator system.
   */
  void startElevatorSystem();

  /**
   * This method is used to stop the elevator system.
   */
  void stopElevatorSystem();

  /**
   * This method is used to step the elevator system.
   */
  void stepElevatorSystem();

  /**
   * This method is used to get the report of the elevator system.
   *
   * @return the building report of the elevator system.
   */
  BuildingReport getBuildingReport();

  /**
   * This method is used to add a request to the elevator system.
   *
   * @param request the request to add to the elevator system.
   * @return true if the request was added, false otherwise.
   */
  boolean addRequest(Request request);
}
