package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import building.enums.ElevatorSystemStatus;
import elevator.ElevatorInterface;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * This is a test class using JUnit 4 for the Building class.
 */
public class BuildingTest {
  private Building building;

  @Before
  public void setUp() {
    building = new Building(10, 3, 8);
  }

  /**
   * Test the constructor.
   */
  @Test
  public void testValidConstructorParameters() {
    assertEquals(10, building.getNumOfFloors());
    assertEquals(3, building.getNumOfElevators());
    assertEquals(8, building.getElevatorCapacity());
  }

  /**
   * Test the constructor with invalid number of floors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeFloors() {
    new Building(-10, 3, 8);
  }

  /**
   * Test the constructor with invalid number of floors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorOneFloor() {
    new Building(1, 3, 8);
  }

  /**
   * Test the constructor with invalid number of elevators.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeElevators() {
    new Building(10, -3, 8);
  }

  /**
   * Test the constructor with invalid number of elevators.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorZeroElevators() {
    new Building(10, 0, 8);
  }

  /**
   * Test the constructor with invalid elevator capacity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeElevatorCapacity() {
    new Building(10, 3, -8);
  }

  /**
   * Test the constructor with invalid elevator capacity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorTwoElevatorCapacity() {
    new Building(10, 3, 2);
  }


  /**
   * Test the getNumOfFloors method.
   */
  @Test
  public void testGetNumOfFloors() {
    assertEquals(10, building.getNumOfFloors());
  }

  /**
   * Test the getNumOfElevators method.
   */
  @Test
  public void testGetNumOfElevators() {
    assertEquals(3, building.getNumOfElevators());
  }

  /**
   * Test the getElevatorCapacity method.
   */
  @Test
  public void testGetElevatorCapacity() {
    assertEquals(8, building.getElevatorCapacity());
  }

  /**
   * Test the startElevatorSystem method.
   */
  @Test
  public void testStartElevatorSystemValid() {
    building.startElevatorSystem();
    assertEquals(ElevatorSystemStatus.running, building.elevatorsStatus);
  }

  /**
   * Test the startElevatorSystem method when the system is already running.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartElevatorSystemAlreadyRunning() {
    building.startElevatorSystem();
    building.startElevatorSystem();
  }

  /**
   * Test the startElevatorSystem method when the system is stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartElevatorSystemStopping() {
    building.startElevatorSystem();
    building.stopElevatorSystem();
    building.startElevatorSystem();
  }

  /**
   * Test the stopElevatorSystem method.
   */
  @Test
  public void testStopElevatorSystemValid() {
    building.startElevatorSystem();
    building.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping, building.elevatorsStatus);
  }

  /**
   * Test the stopElevatorSystem method when the system is out of service.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopElevatorSystemNotRunning() {
    building.stopElevatorSystem();
  }

  /**
   * Test the stopElevatorSystem method when the system is already stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopElevatorSystemStopping() {
    building.startElevatorSystem();
    building.stopElevatorSystem();
    building.stopElevatorSystem();
  }

  /**
   * Test the addRequest method with a single request.
   */
  @Test
  public void testAddSingleRequest() {
    building.startElevatorSystem();
    Request request = new Request(1, 5);
    assertTrue(building.addRequest(request));
    assertEquals(1, building.upRequests.size());
    assertEquals(request, building.upRequests.get(0));
  }

  /**
   * Test the addRequest method with multiple requests.
   */
  @Test
  public void testAddMultipleRequests() {
    building.startElevatorSystem();
    Request request1 = new Request(1, 5);
    Request request2 = new Request(2, 6);
    Request request3 = new Request(7, 3);
    assertTrue(building.addRequest(request1));
    assertTrue(building.addRequest(request2));
    assertTrue(building.addRequest(request3));
    assertEquals(2, building.upRequests.size());
    assertEquals(1, building.downRequests.size());
    assertEquals(request2, building.upRequests.get(1));
  }

  /**
   * Test the addRequest method with request allocation extensively.
   */
  @Test
  public void testAddRequestExtensively() {
    Building smallBuilding = new Building(5, 1, 3);
    smallBuilding.startElevatorSystem();

    Request request1 = new Request(0, 4);
    assertTrue(smallBuilding.addRequest(request1));
    Request request2 = new Request(1, 3);
    assertTrue(smallBuilding.addRequest(request2));
    Request request3 = new Request(2, 1);
    assertTrue(smallBuilding.addRequest(request3));
    Request request4 = new Request(3, 0);
    assertTrue(smallBuilding.addRequest(request4));
    assertEquals(2, smallBuilding.upRequests.size());
    assertEquals(2, smallBuilding.downRequests.size());
    assertEquals(request2, smallBuilding.upRequests.get(1));
    assertEquals(request4, smallBuilding.downRequests.get(1));
  }

  /**
   * Test add more requests than the max capacity of the elevator.
   */
  @Test
  public void testRequestsOverElevatorMaxCapacity() {
    // Create a building with one elevator and max capacity of 5
    Building building = new Building(10, 1, 5);

    building.startElevatorSystem();

    // Add more than max capacity requests
    building.addRequest(new Request(0, 1));
    building.addRequest(new Request(1, 2));
    building.addRequest(new Request(2, 3));
    building.addRequest(new Request(3, 4));
    building.addRequest(new Request(4, 5));
    building.addRequest(new Request(5, 6));

    building.stepElevatorSystem();

    BuildingReport report = building.getBuildingReport();
    int totalPendingRequests = report.getUpRequests().size() + report.getDownRequests().size();

    assertEquals(1, totalPendingRequests);

  }

  /**
   * Test the addRequest method when the request is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullRequest() {
    building.startElevatorSystem();
    building.addRequest(null);
  }

  /**
   * Test the addRequest method when the system is not running.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestSystemOutOfService() {
    Request request = new Request(1, 5);
    building.addRequest(request);
  }

  /**
   * Test the addRequest method when the system is not running.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestSystemStopping() {
    building.startElevatorSystem();
    building.stopElevatorSystem();
    Request request = new Request(1, 5);
    building.addRequest(request);
  }

  /**
   * Test the addRequest method with the same floor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestSameFloor() {
    building.startElevatorSystem();
    Request request = new Request(1, 1);
    building.addRequest(request);
  }

  /**
   * Test the addRequest method with an invalid start floor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestInvalidStartFloor() {
    building.startElevatorSystem();
    Request request = new Request(-1, 5);
    building.addRequest(request);
  }

  /**
   * Test the addRequest method with an invalid end floor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestInvalidEndFloor() {
    building.startElevatorSystem();
    Request request = new Request(1, 10);
    building.addRequest(request);
  }

  /**
   * Test the stepElevatorSystem method with requests.
   */
  @Test
  public void testStepElevatorSystemWithRequests() {
    building.startElevatorSystem();
    // Add requests to simulate the situation where there are pending requests
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(3, 1));

    // Step the elevator system
    building.stepElevatorSystem();

    // Assert that elevators have processed the requests
    for (ElevatorInterface elevator : building.elevators) {
      assertTrue(elevator.getCurrentFloor() == 0 || elevator.getCurrentFloor() == 3);
    }
  }

  /**
   * Test the stepElevatorSystem method with no requests.
   */
  @Test
  public void testStepElevatorSystemNoRequests() {
    building.startElevatorSystem();

    building.stepElevatorSystem();

    for (ElevatorInterface elevator : building.elevators) {
      assertEquals(0, elevator.getCurrentFloor());
    }
  }

  /**
   * Test the stepElevatorSystem method with system out of service.
   */
  @Test
  public void testStepElevatorSystemOutOfService() {

    building.stepElevatorSystem();

    for (ElevatorInterface elevator : building.elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertFalse(elevator.isTakingRequests());
    }
  }

  /**
   * Test the stepElevatorSystem method with system stopping.
   */
  @Test
  public void testStepElevatorSystemStopping() {
    building.startElevatorSystem();
    building.stopElevatorSystem();

    building.stepElevatorSystem();
    for (ElevatorInterface elevator : building.elevators) {
      assertEquals(0, elevator.getCurrentFloor());
      assertFalse(elevator.isTakingRequests());
    }
  }

  /**
   * Test the getBuildingReport method.
   */
  @Test
  public void testGetBuildingReportWithRequestsPending() {
    building.startElevatorSystem();
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(3, 1));

    BuildingReport report = building.getBuildingReport();
    assertEquals(10, report.getNumFloors());
    assertEquals(3, report.getNumElevators());
    assertEquals(8, report.getElevatorCapacity());
    assertEquals(ElevatorSystemStatus.running, report.getSystemStatus());
    assertEquals(1, report.getUpRequests().size());
    assertEquals(1, report.getDownRequests().size());
    assertEquals(3, report.getElevatorReports().length);
  }

  /**
   * Test the getBuildingReport method with some requests allocated.
   */
  @Test
  public void testGetBuildingReportWithPartRequestsAllocated() {
    building.startElevatorSystem();
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(3, 1));

    // Deal with the up request
    building.stepElevatorSystem();

    BuildingReport report1 = building.getBuildingReport();
    assertEquals(10, report1.getNumFloors());
    assertEquals(3, report1.getNumElevators());
    assertEquals(8, report1.getElevatorCapacity());
    assertEquals(ElevatorSystemStatus.running, report1.getSystemStatus());
    assertEquals(0, report1.getUpRequests().size());
    assertEquals(1, report1.getDownRequests().size());
    assertEquals(3, report1.getElevatorReports().length);

    //Elevators get to the top floor and start to process the down request
    for (int i = 0; i < (building.getNumOfFloors() + 5); i++) {
      building.stepElevatorSystem();
    }

    BuildingReport report2 = building.getBuildingReport();
    assertEquals(10, report2.getNumFloors());
    assertEquals(3, report2.getNumElevators());
    assertEquals(8, report2.getElevatorCapacity());
    assertEquals(ElevatorSystemStatus.running, report2.getSystemStatus());
    assertEquals(0, report2.getUpRequests().size());
    assertEquals(0, report2.getDownRequests().size());
    assertEquals(3, report2.getElevatorReports().length);
  }


  /**
   * Test the building will correctly be out of service when all elevators are out of service.
   */
  @Test
  public void testBuildingOutOfService() {
    building.startElevatorSystem();

    for (ElevatorInterface elevator : building.elevators) {
      elevator.takeOutOfService();
    }
    building.stopElevatorSystem();
    building.stepElevatorSystem();

    BuildingReport report = building.getBuildingReport();
    assertEquals(ElevatorSystemStatus.outOfService, report.getSystemStatus());
  }

  /**
   * Test the toString method in the BuildingReport class.
   */
  @Test
  public void testBuildingReportToString() {
    building.startElevatorSystem();
    building.addRequest(new Request(0, 5));
    building.addRequest(new Request(3, 1));

    BuildingReport report = building.getBuildingReport();
    String reportString = report.toString();

    String expectedReportString = """
        BuildingReport {
        \tNumber of Floors: 10
        \tNumber of Elevators: 3
        \tElevator Capacity: 8
        \tElevator Reports:\s
        \t\tWaiting[Floor 0, Time 5]
        \t\tWaiting[Floor 0, Time 5]
        \t\tWaiting[Floor 0, Time 5]
        \tUp Requests: [0->5]
        \tDown Requests: [3->1]
        \tSystem Status: Running
        }""";

    assertEquals(reportString, expectedReportString);
  }
}
