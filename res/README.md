# Building Elevator System

## Overview

This project simulates an elevator system within a building, providing an efficient way to manage multiple elevator operations simultaneously. The software is designed to handle multiple elevator requests, optimize elevator movements, and reduce wait times by smartly allocating elevator tasks based on current demand and status.

## List of Features

- **Multi-Elevator Support:** Manages multiple elevators simultaneously.
- **Request Handling:** Processes both up and down requests efficiently.
- **Real-time Simulation:** Displays real-time status of each elevator including current floor, direction, and queue of requests.
- **Interactive Controls:** Start, stop, and step through the elevator operations interactively.
- **Dynamic Request Addition:** Allows adding elevator requests on the fly during simulation.

## How To Run

To run this program, you will need Java installed on your machine. Follow these steps:

### Running the Jar File

1. Open a command line interface (CLI).
2. Navigate to the directory containing the `ElevatorSystem.jar` file.
3. Run the command: `java -jar ElevatorSystem.jar`

### Arguments

No arguments are needed to run the jar file. The system is self-contained and interactive, allowing you to control the simulation through the GUI.

## How to Use the Program

After starting the application:

1. **Start Simulation:** Click the 'Start' button to initiate the elevator system.
2. **Add Requests:** Enter the start and destination floors in the provided fields and press 'Request' to add a new elevator request.
3. **Step Through Simulation:** Use the 'Step' button to advance the elevator simulation one step at a time.
4. **Stop Simulation:** Press the 'Stop' button to pause the elevator movements. You can restart by pressing 'Start'.

The program provides a visual representation of elevator statuses and allows real-time interaction.

## Design/Model Changes

- **Part 1:** Initial design with the given elevator model.
- **Part 2:** Modified the BuildingReport class to display the report in a more user-friendly style.

These changes were implemented to enhance user interaction and improve the simulation's efficiency and usability.

## Assumptions

- Each elevator has an identical maximum capacity and speed, the capacity is constant.
- Requests are immediately processed by the system in the order they are received without delay.
- The elevators only deal with up requests when they are on the ground floor and down requests when they are on the top floor.
- The building layout does not change dynamically; the number of floors and elevators are constants.
- The system does not account for maintenance or repair scenarios.

## Limitations

- **Fault Tolerance:** The system does not handle scenarios where an elevator becomes inoperative.
- **Scalability:** Performance may degrade with a significantly large number of elevators or floors.

## Citations

- Bro Code. (2020, September 14). Java GUI: Full Course ☕ (FREE) [Video]. YouTube. https://www.youtube.com/watch?v=Kmgo00avvEw (retrieved on 2024-04-14)
- Oracle Java Documentation: https://docs.oracle.com/javase/8/docs/api/ (retrieved on 2024-03-30)
