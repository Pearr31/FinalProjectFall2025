package finalproject_fall2025;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author massi
 */
public class FXMLController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label finalVelocityLabel;
    @FXML
    private Label flightTimeLabel;
    @FXML
    private Label rangeLabel;
    @FXML
    private Label maxHeightLabel;
    @FXML
    private Pane bottomPane;
    @FXML
    private Button simulationStartButton;
    @FXML
    private Pane simulationPane;
    @FXML
    private Canvas simulationCanvas;
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
        var gc = simulationCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, simulationCanvas.getWidth(), simulationCanvas.getHeight());

        double canvasWidth = simulationCanvas.getWidth();
        double canvasHeight = simulationCanvas.getHeight();

        double range = projectile.getRange();
        double maxHeight = projectile.getMaxHeight();
        double initialHeight = projectile.getInitialHeight();

        if (range <= 0) {
            return;
        }

        double xScale = canvasWidth / range;
        double yScale = canvasHeight / (maxHeight + initialHeight + 1);
        double flightTime = projectile.getFlightTime();

        // Animation state
        final long[] startTime = {0};
        final double[] prevX = {canvasWidth}; // ✔️ Start at right edge
        final double[] prevY = {canvasHeight - initialHeight * yScale};

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void start() {
                startTime[0] = System.nanoTime();
                super.start();
            }

            @Override
            public void handle(long now) {

                double elapsedSeconds = (now - startTime[0]) / 1_000_000_000.0;

                if (elapsedSeconds > flightTime) {
                    this.stop();
                    return;
                }

                double x = projectile.getX(elapsedSeconds);
                double y = projectile.getY(elapsedSeconds);

                // ✔️ MIRROR X so it draws from right to left
                double canvasX = canvasWidth - (x * xScale);
                double canvasY = canvasHeight - (y * yScale);

                gc.strokeLine(prevX[0], prevY[0], canvasX, canvasY);

                prevX[0] = canvasX;
                prevY[0] = canvasY;

                if (y < 0) {
                    this.stop();
                }
            }
        };

        timer.start();
    }
}
