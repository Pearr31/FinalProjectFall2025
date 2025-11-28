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
import javafx.scene.shape.Rectangle;

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

    @FXML
    private Rectangle ProjectileStartPlatform;

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
        // Remove old curves
        simulationPane.getChildren().removeIf(node -> node instanceof javafx.scene.shape.Polyline);

        double paneWidth = simulationPane.getWidth();
        double paneHeight = simulationPane.getHeight();
        double margin = 40;

        double range = projectile.getRange();
        double maxHeight = projectile.getMaxHeight();
        double initialHeight = projectile.getInitialHeight();
        double flightTime = projectile.getFlightTime();

        // Compute scale so the entire trajectory fits in pane
        double scaleX = (paneWidth - 2 * margin) / range;
        double scaleY = (paneHeight - 2 * margin) / maxHeight;
        double scale = Math.min(scaleX, scaleY);

        // Ground Y-level
        double groundY = paneHeight - margin;

        double platformWidth = ProjectileStartPlatform.getWidth(); // preserve width
        double platformHeight = initialHeight * scale;             // scaled height

        double platformX = paneWidth - margin - platformWidth;     // snap to bottom-right
        double platformY = groundY - platformHeight;               // top rises, bottom fixed

        ProjectileStartPlatform.setWidth(platformWidth);
        ProjectileStartPlatform.setHeight(platformHeight);
        ProjectileStartPlatform.setLayoutX(platformX);
        ProjectileStartPlatform.setLayoutY(platformY);

        // Projectile should fire from the TOP of the platform
        double startX = platformX + platformWidth / 2;  // center of platform
        double startY = platformY;                      // top of platform

        javafx.scene.shape.Polyline curve = new javafx.scene.shape.Polyline();
        curve.setStroke(Color.RED);
        curve.setStrokeWidth(2);

        int steps = 200;
        for (int i = 0; i <= steps; i++) {
            double t = flightTime * i / steps;

            // Physics model
            double x = projectile.getInitialVelocity() * Math.cos(projectile.getLaunchAngle()) * t;
            double y = initialHeight
                    + projectile.getInitialVelocity() * Math.sin(projectile.getLaunchAngle()) * t
                    - 0.5 * 9.81 * t * t;

            // Convert physics position -> pane coordinates
            double sx = startX - x * scale;       // moves LEFT as x increases
            double sy = groundY - y * scale;      // moves UP as y increases

            curve.getPoints().addAll(sx, sy);
        }

        simulationPane.getChildren().add(curve);
    }
}
