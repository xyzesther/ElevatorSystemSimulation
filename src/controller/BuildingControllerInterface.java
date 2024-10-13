package controller;

/**
 * This interface is implemented by BuildingController class.
 */
public interface BuildingControllerInterface {

  /**
   * This method is called by the view to start the elevator system.
   */
  void startElevatorSystem();

  /**
   * This method is called by the view to stop the elevator system.
   */
  void stopElevatorSystem();

  /**
   * This method is called by the view to step the elevator system.
   */
  void stepElevatorSystem();

  /**
   * This method is called by the view to process a request.
   *
   * @param startFloor the start floor of the request
   * @param endFloor the end floor of the request
   */
  void processRequest(int startFloor, int endFloor);
}
