/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproject_fall2025;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label flightTimeLabel;
    @FXML
    private Label finalVelocityLabel;
    @FXML
    private Label maxHeightLabel;
    @FXML
    private Pane bottomPane;
    @FXML
    private Button simulationStartButton;
    @FXML
    private Label rangeLabel;
    @FXML
    private Pane simulationPane;
    @FXML
    private TextArea editHeightTextArea;
    @FXML
    private TextArea editInitialSpeed;
    @FXML
    private Slider angleSlider;
    @FXML
    private Label titleLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        simulationStartButton.setOnAction(e -> handleSimulationStartRestart());
    }

    @FXML
    private void handleSimulationStartRestart() {
        try {
            double inputVelocity = Double.parseDouble(editInitialSpeed.getText());
            double inputLaunchAngle = angleSlider.getValue();
            double inputHeight = Double.parseDouble(editHeightTextArea.getText());

            Projectile projectile = new Projectile(inputVelocity, inputLaunchAngle, inputHeight);

            // Update result labels
            flightTimeLabel.setText(String.format("Flight Time: %.2f s", projectile.getFlightTime()));
            maxHeightLabel.setText(String.format("Max Height: %.2f m", projectile.getMaxHeight()));
            rangeLabel.setText(String.format("Range: %.2f m", projectile.getRange()));
            finalVelocityLabel.setText(String.format("Final Velocity: %.2f m/s", projectile.getFinalVelocity()));

            // Draw trajectory arc
            drawTrajectoryArc(projectile);

        } catch (NumberFormatException ex) {
            // super simple error handling for now
            flightTimeLabel.setText("Flight Time: invalid input");
            maxHeightLabel.setText("Max Height: invalid input");
            rangeLabel.setText("Range: invalid input");
            finalVelocityLabel.setText("Final Velocity: invalid inpu");

        }
    }
    

    

    private void drawTrajectoryArc(Projectile projectile) {
        simulationPane.getChildren().removeIf(node -> node instanceof Arc);

        double paneWidth = simulationPane.getPrefWidth();
        double paneHeight = simulationPane.getPrefHeight();

        // Margin so arc isn't glued to the border
        double margin = 40.0;
        double usableWidth = paneWidth - 2 * margin;
        double usableHeight = paneHeight - 2 * margin;

        // Scale physics values to fit the pane
        double range = projectile.getRange();
        double heightDiff = projectile.getMaxHeight() - projectile.getInitialHeight();
        if (range <= 0 || heightDiff <= 0) {
            return; // nothing reasonable to draw
        }

        double scaleX = usableWidth / range;
        double scaleY = usableHeight / heightDiff;
        double scale = Math.min(scaleX, scaleY);

        // Arc radii based on range & height
        double radiusX = (range * scale) / 2.0;
        double radiusY = heightDiff * scale;

        // Position the ground a bit above the bottom of the pane
        double groundY = paneHeight - margin - projectile.getInitialHeight() * scale;

        // Center of the arc horizontally is in the middle of the range
        double centerX = margin + radiusX;
        double centerY = groundY;

        Arc trajectoryArc = new Arc();
        trajectoryArc.setCenterX(centerX);
        trajectoryArc.setCenterY(centerY);
        trajectoryArc.setRadiusX(radiusX);
        trajectoryArc.setRadiusY(radiusY);

        // Make a "∩" shape from left ground to right ground
        trajectoryArc.setStartAngle(180);   // left side
        trajectoryArc.setLength(-180);      // sweep to right side
        trajectoryArc.setType(ArcType.OPEN); // *** this is the arc type you want ***

        trajectoryArc.setStroke(Color.BLUE);
        trajectoryArc.setStrokeWidth(2);
        trajectoryArc.setFill(Color.TRANSPARENT);

        simulationPane.getChildren().add(trajectoryArc);
    }

}
