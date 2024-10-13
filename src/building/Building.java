package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;


/**
 * This class represents a building.
 */
public class Building implements BuildingInterface {
  protected final List<Request> downRequests = new ArrayList<>();
  protected final List<Request> upRequests = new ArrayList<>();
  protected final ElevatorInterface[] elevators;
  protected ElevatorSystemStatus elevatorsStatus;
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;


  /**
   * The constructor for the building.
   *
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity the capacity of the elevators in the building.
   * @throws IllegalArgumentException if the number of floors is less than 2.
   * @throws IllegalArgumentException if the number of elevators is less than 1.
   * @throws IllegalArgumentException if the elevator capacity is less than 3.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity) {
    if (numberOfFloors < 2) {
      throw new IllegalArgumentException("numberOfFloors must be no less than 2");
    } else if (numberOfElevators < 1) {
      throw new IllegalArgumentException("numberOfElevators must be a positive integer");
    } else if (elevatorCapacity < 3) {
      throw new IllegalArgumentException("elevatorCapacity must be no less than 3");
    }

    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new Elevator[numberOfElevators];

    for (int i = 0; i < numberOfElevators; i++) {
      this.elevators[i] = new Elevator(this.numberOfFloors, this.elevatorCapacity);
    }

    this.elevatorsStatus = ElevatorSystemStatus.outOfService;
  }

  @Override
  public int getNumOfFloors() {
    return this.numberOfFloors;
  }

  @Override
  public int getNumOfElevators() {
    return this.numberOfElevators;
  }

  @Override
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  @Override
  public void startElevatorSystem() {
    if (this.elevatorsStatus != ElevatorSystemStatus.running) {
      if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
        throw new IllegalStateException("Elevator cannot be started until it is stopped");
      } else {
        for (ElevatorInterface elevator : this.elevators) {
          elevator.start();
        }
        this.elevatorsStatus = ElevatorSystemStatus.running;
      }
    } else {
      throw new IllegalStateException("Elevator system is already running");
    }
  }

  @Override
  public void stopElevatorSystem() {
    if (this.elevatorsStatus != ElevatorSystemStatus.outOfService
        && this.elevatorsStatus != ElevatorSystemStatus.stopping) {

      for (ElevatorInterface elevator : this.elevators) {
        elevator.takeOutOfService();
        this.elevatorsStatus = ElevatorSystemStatus.stopping;
        this.upRequests.clear();
        this.downRequests.clear();
      }
    } else {
      throw new IllegalStateException("Elevator system is not running now.");
    }
  }

  @Override
  public void stepElevatorSystem() {
    if (this.elevatorsStatus != ElevatorSystemStatus.outOfService) {
      for (ElevatorInterface elevator : this.elevators) {
        elevator.step();
      }

      if (this.elevatorsStatus != ElevatorSystemStatus.stopping) {
        this.distributeRequests();
      } else {
        boolean allElevatorsOnGroundFloor = true;
        for (ElevatorInterface elevator : this.elevators) {
          if (elevator.getCurrentFloor() != 0) {
            allElevatorsOnGroundFloor = false;
            break;
          }
        }

        if (allElevatorsOnGroundFloor) {
          this.elevatorsStatus = ElevatorSystemStatus.outOfService;
        }
      }
    }
  }

  private void distributeRequests() {
    if (!this.upRequests.isEmpty() || !this.downRequests.isEmpty()) {

      for (ElevatorInterface elevator : this.elevators) {
        if (elevator.isTakingRequests()) {
          List<Request> requestsForElevator = new ArrayList<>();
          List<Request> sourceRequests;

          if (elevator.getCurrentFloor() == 0) {
            sourceRequests = this.upRequests;
          } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
            sourceRequests = this.downRequests;
          } else {
            continue;
          }

          while (!sourceRequests.isEmpty() && requestsForElevator.size() < this.elevatorCapacity) {
            requestsForElevator.add(sourceRequests.remove(0));
          }

          elevator.processRequests(requestsForElevator);
        }
      }
    }
  }

  @Override
  public BuildingReport getBuildingReport() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.numberOfElevators];

    for (int i = 0; i < this.numberOfElevators; i++) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }

    return new BuildingReport(this.numberOfFloors,
        this.numberOfElevators, this.elevatorCapacity, elevatorReports,
        this.upRequests, this.downRequests, this.elevatorsStatus);
  }

  @Override
  public boolean addRequest(Request request) {
    if (this.elevatorsStatus == ElevatorSystemStatus.outOfService
        || this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system is not running.");
    } else if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    } else if (request.getStartFloor() >= 0 && request.getStartFloor() < this.numberOfFloors) {
      if (request.getEndFloor() >= 0 && request.getEndFloor() < this.numberOfFloors) {
        if (request.getStartFloor() == request.getEndFloor()) {
          throw new IllegalArgumentException("Start floor and end floor cannot be the same.");
        } else {
          if (request.getStartFloor() < request.getEndFloor()) {
            this.upRequests.add(request);
          } else {
            this.downRequests.add(request);
          }
          return true;
        }
      } else {
        throw new IllegalArgumentException("The end floor must be between 0 and "
            + (this.numberOfFloors - 1));
      }
    } else {
      throw new IllegalArgumentException("The start floor must be between 0 and "
          + (this.numberOfFloors - 1));
    }
  }
}



