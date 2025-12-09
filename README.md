# 2D Projectile Motion Simulator

##Overview
This project is a **2D Projectile Motion Simulator** developed using **JavaFX**.  
The application visually demonstrates the physics of projectile motion using real-world equations, allowing users to adjust **initial velocity**, **launch angle**, and **initial height**.

The simulator converts real physics units (meters, seconds) into on-screen pixel coordinates and animates the projectile motion in real time.

--------

# #How to use our Project

### Requirements to run the project
- Java JDK 17+ (tested with JDK 23)
- JavaFX SDK and FXML(added to project libraries)
- IDE such as **IntelliJ** or **NetBeans**

###How to use the project
1. Clone or download the repository.
2. Open the project in your IDE.
3. Make sure JavaFX is properly linked to the project.
4. Run the `FinalProject_Fall2025.java` file.
5. Click **Start** on the welcome screen to access the simulator.

--------

## How to Use the Simulator
-Enter three values on the right side of the screen: the initial launch speed, the launch angle, and the initial height. Adjust the angle using the slider, and type the other two values into their text fields.
Once your inputs are ready, click Start Simulation. The cannon will rotate to match the inputed angle, the projectile will launch, and the trajectory will be drawn in real time on the canvas. The program will also display the calculated flight time, maximum height, range, and final velocity.
-To run another simulation, click Reset Simulation. This clears the canvas, resets the controls, and allows you to enter new values.
-Make sure all inputs are valid numbers—if something is incorrect, the program will display an error message to help you correct it (negative values and extreme heights are invalid).

--------

## Features
- Real-time projectile animation using physics equations
- Accurate scaling from meters to pixels
- Cannon rotation based on launch angle
- Platform height adjusts dynamically with initial height
- Error handling for invalid input
- Clean and intuitive JavaFX user interface
- Animates the projectile motion in real time using JavaFx's Animation Timer

--------

## Teamwork Summary
| Team Member | Contribution |

| Matthew Pereira | Core JavaFX animation logic, styling, scaling and physics integration |
| Massimo Deluca | Physics logic, projectile equations, calculating and physics integration |
| Amine Achik | UI layout, FXML design, physics logic, debugging and documentation |


  
