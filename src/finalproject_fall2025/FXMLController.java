package finalproject_fall2025;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * FXML Controller class
 *
 * @author massi
 */
public class FXMLController implements Initializable {

    @FXML
    private Label angleValueLabel;

    private AnimationTimer currentTimer;
    @FXML
    private Rectangle heightRectangle;
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

    @FXML
    private Button simulationResetButton;

    @FXML
    private Rectangle canonHead;

    @FXML
    private Circle canonBase;

    private static final double MAXHEIGHTCANVAS = 100;   //sets top of canvas to max 100m 
    private double launchX;
    private long animationStartTime;
    private double previousX;
    private double previousY;

    /**
     * Initializes the controller class. Sets up event handlers for buttons and
     * updates the angle label when the slider value changes.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if unknown.
     * @param rb The resources used to localize the root object, or null if not
     * provided.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image backgroundImage = new Image(getClass().getResource("images\\SimulationBackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // no horizontal repeat
                BackgroundRepeat.NO_REPEAT, // no vertical repeat
                BackgroundPosition.CENTER, // position at center
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true)
        );
        simulationPane.setBackground(new Background(background));
        simulationStartButton.setOnAction(e -> handleSimulationStart());
        simulationResetButton.setOnAction(e -> handleSimulationReset());
        simulationResetButton.setDisable(true);

        angleValueLabel.setText("Angle: " + (int) angleSlider.getValue() + "°");

        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            angleValueLabel.setText("Angle: " + newVal.intValue() + "°");
        });

        double initialHeight = 0; // default starting height
        double yScale = simulationCanvas.getHeight() / MAXHEIGHTCANVAS;
        updatePlatformHeight(initialHeight, yScale);
    }

    /**
     * Handles the action of starting the projectile simulation.
     * <p>
     * Reads user input for initial velocity, launch angle, and initial height,
     * validates inputs, creates a {@link Projectile} instance, updates result
     * labels, and starts the animation timer to draw the projectile's
     * trajectory.
     * </p>
     */
    @FXML
    private void handleSimulationStart() {
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

            if (inputVelocity < 0 || inputHeight < 0) {
                showInvalidInputAlert();
            }

            // Draw trajectory arc
            drawTrajectoryArc(projectile);

            simulationResetButton.setDisable(false);
            simulationStartButton.setDisable(true);

        } catch (NumberFormatException ex) {
            showInvalidInputAlert();
        }

    }

    /**
     * Handles resetting the simulation to its initial state.
     * <p>
     * Stops any ongoing animation, clears input fields, resets labels, clears
     * the canvas, and restores the launch platform rectangle.
     * </p>
     */
    @FXML
    private void handleSimulationReset() {
        if (currentTimer != null) {
            currentTimer.stop();
            currentTimer = null;
        }
        editInitialSpeed.clear();
        editHeightTextArea.clear();

        flightTimeLabel.setText("Flight Time:");
        maxHeightLabel.setText("Max Height:");
        rangeLabel.setText("Range:");
        finalVelocityLabel.setText("Final Velocity:");

        var gc = simulationCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, simulationCanvas.getWidth(), simulationCanvas.getHeight());

        simulationStartButton.setDisable(false);
        simulationResetButton.setDisable(true);
        heightRectangle.setHeight(20);
        heightRectangle.setLayoutY(simulationCanvas.getHeight() - 20);
        updateCannonPosition();
    }

    /**
     * Displays an alert to the user indicating that the input values are
     * invalid.
     * <p>
     * Updates the result labels to indicate invalid input and shows a red alert
     * message.
     * </p>
     */
    private void showInvalidInputAlert() {
        flightTimeLabel.setText("Flight Time: invalid input");
        maxHeightLabel.setText("Max Height: invalid input");
        rangeLabel.setText("Range: invalid input");
        finalVelocityLabel.setText("Final Velocity: invalid input");

        Alert invalidInputAlert = new Alert(Alert.AlertType.INFORMATION);
        invalidInputAlert.setHeaderText("ERROR");

        //Alerting the user that there are invalid inputs
        Label alertLabel = new Label("One or more INVALID inputs");
        alertLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: red; -fx-font-weight: bold;");
        invalidInputAlert.setGraphic(alertLabel);
        invalidInputAlert.setTitle("Invalid input(s)");
        invalidInputAlert.show();
    }

    /**
     * Draws the trajectory arc of the given projectile on the simulation
     * canvas.
     * <p>
     * Uses an {@link AnimationTimer} to animate the projectile's motion over
     * time, drawing line segments between successive positions. Adjusts the
     * launch platform height according to the projectile's initial height.
     * </p>
     *
     * @param projectile The {@link Projectile} object representing the
     * simulated motion.
     */
    private void drawTrajectoryArc(Projectile projectile) {
        var gc = simulationCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, simulationCanvas.getWidth(), simulationCanvas.getHeight());

        double canvasWidth = simulationCanvas.getWidth();
        double canvasHeight = simulationCanvas.getHeight();

        double range = projectile.getRange();
        double initialHeight = projectile.getInitialHeight();
        double v0x = projectile.getV0x();
        boolean isAlmostVertical = Math.abs(v0x) < 1e-3;

        if (!isAlmostVertical && range <= 0) {
            return;
        }

        double minDisplayedRange = 50;
        double effectiveRange = Math.max(range, minDisplayedRange);
        double xScale = canvasWidth / effectiveRange;
        double yScale = canvasHeight / MAXHEIGHTCANVAS;

        // Update platform and cannon
        updatePlatformHeight(initialHeight, yScale);
        rotateCannon(angleSlider.getValue());

        double flightTime = projectile.getFlightTime();

        // Get the transformed tip of the cannon head
        Point2D tipLocal = new Point2D(canonHead.getWidth() / 2, 0); // top-center
        Point2D tipScene = canonHead.localToScene(tipLocal);

        // Convert to canvas coordinates
        Point2D canvasOrigin = simulationCanvas.localToScene(0, 0);
        double cannonTipX = tipScene.getX() - canvasOrigin.getX();
        double cannonTipY = tipScene.getY() - canvasOrigin.getY();

        previousX = cannonTipX;
        previousY = cannonTipY;
        animationStartTime = System.nanoTime();

        currentTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedSeconds = (now - animationStartTime) / 1_000_000_000.0;

                if (elapsedSeconds > flightTime) {
                    stop();
                    return;
                }

                // Physics world coordinates (relative to ground)
                double x = projectile.getX(elapsedSeconds);         // horizontal distance from launch
                double y = projectile.getY(elapsedSeconds);         // height above ground

                // Clamp y to canvas bounds
                y = Math.max(0, Math.min(y, MAXHEIGHTCANVAS));

                // Map to canvas:
                // X: draw to the left from the tip (if your cannon faces left)
                double canvasX = cannonTipX - x * xScale;

                // Y: offset by initial height so t=0 starts exactly at the tip
                double canvasY = cannonTipY - (y - initialHeight) * yScale;

                gc.strokeLine(previousX, previousY, canvasX, canvasY);

                previousX = canvasX;
                previousY = canvasY;
            }
        };
        gc.setFill(Color.RED);
        gc.fillOval(cannonTipX - 3, cannonTipY - 3, 6, 6);

        currentTimer.start();
    }

    /**
     * Updates the height of the launch platform rectangle according to the
     * projectile's initial height.
     * <p>
     * Converts height in meters to pixels using the canvas scale and anchors
     * the rectangle to the bottom of the canvas.
     * </p>
     *
     * @param heightMeters Height of the platform in meters.
     * @param scale Conversion factor from meters to pixels.
     */
    private void updatePlatformHeight(double heightMeters, double scale) {
        double canvasHeight = simulationCanvas.getHeight();

        // Convert meters -> pixels using the SAME uniform scale as trajectory
        double pixelHeight = heightMeters * scale;

        // Ensure minimum visible size
        if (pixelHeight < 5) {
            pixelHeight = 5;
        }

        // Apply size
        heightRectangle.setHeight(pixelHeight);

        // Anchor to ground (bottom of canvas)
        double platformY = canvasHeight - pixelHeight;
        heightRectangle.setLayoutY(platformY);

        updateCannonPosition();
    }

    /**
     * Repositions the cannon head and base on top of the platform.
     */
    private void updateCannonPosition() {
        if (canonBase != null) {
            // Center the cannon base on the platform
            double baseX = heightRectangle.getLayoutX() + (heightRectangle.getWidth() / 2);
            double baseY = heightRectangle.getLayoutY(); // base sits on top of platform
            canonBase.setLayoutX(baseX);
            canonBase.setLayoutY(baseY);
        }

        if (canonHead != null && canonBase != null) {
            // Center the cannon head horizontally on the base
            double headX = canonBase.getLayoutX() - canonHead.getWidth() / 2;
            double headY = canonBase.getLayoutY() - canonHead.getHeight(); // snap on top of base
            canonHead.setLayoutX(headX);
            canonHead.setLayoutY(headY);

            // Reset any previous rotation
            canonHead.setRotate(0);
        }
    }

    /**
     * Rotates the cannon to match the launch angle.
     *
     * @param angleDegrees Angle in degrees from horizontal
     */
    private void rotateCannon(double angleDegrees) {
        if (canonHead != null && canonBase != null) {
            // Clear previous transforms
            canonHead.getTransforms().clear();

            // Pivot at bottom center of the cannon head
            double pivotX = canonHead.getWidth() / 2.0;
            double pivotY = canonHead.getHeight(); // bottom edge

            // Apply rotation
            Rotate rotate = new Rotate(-(90 - angleDegrees), pivotX, pivotY); // negative to rotate clockwise
            canonHead.getTransforms().add(rotate);
        }
    }
}
