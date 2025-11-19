package assignment2_massimo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author massi
 */
public class FXMLController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox Hbox;
    @FXML
    private VBox buttonVbox;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label marathonStatusLabel;
    @FXML
    private Pane racePane;

    private boolean isPaused = false;

    private boolean raceFinished = false;

    private double finishLine = 750;

    private List<Runner> runners = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting the baclground for the race area
        Image background = new Image(getClass().getResource("/assignment2_massimo/images/Marathon.png").toExternalForm());

        BackgroundImage backgroundImage = new BackgroundImage(
                background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false, false, true, true
                )
        );

        racePane.setBackground(new Background(backgroundImage));
    }

    @FXML
    private void startButtonPressed(ActionEvent event) {
        racePane.getChildren().clear();
        runners.clear();
        // Reset for new race when start is pressed
        raceFinished = false;
        marathonStatusLabel.setText("The Race Has Begun");
        String[] names = {
            "Bolt #1",
            "Kipchoge #2",
            "Matadi #3",
            "Lyles #4",
            "Asemoto #5"
        };

        for (int i = 0; i < 5; i++) {
            final int runnerNumber = i + 1;
            Runner runner = new Runner();
            runner.setNumber(runnerNumber);
            double laneY = 5 + i * 85;
            runner.setY(laneY);

            // Add a callback to announce winner
            runner.setOnFinish(r -> {
                if (!raceFinished) {
                    raceFinished = true;
                    // Creating a pop out screne to display the winner
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText("Race Winner");
                    javafx.scene.control.Label winnerLabel = new javafx.scene.control.Label( names[runner.getNumber()-1] +
                    " is the winner!");
                    winnerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: green; -fx-font-weight: bold;");
                    alert.setGraphic(winnerLabel);
                    alert.setTitle("! Marathon Winner !");
                    marathonStatusLabel.setText("Runner " + runner.getNumber() + " is the winner! And the race is over!");
                    alert.show();
                    raceEnd();
                }
            });

            runners.add(runner);
            racePane.getChildren().add(runner.getImageView());
            runner.startMoving(racePane, finishLine);
        }
    }

    @FXML
    private void pauseButtonPressed(ActionEvent event) {
        if (!isPaused) {
            // Pause all runners
            for (Runner runner : runners) {
                runner.stop();
            }

            pauseButton.setText("Resume");
            isPaused = true;
        } else {
            // Resume all runners
            for (Runner runner : runners) {
                runner.startMoving(racePane, finishLine);
            }

            pauseButton.setText("Pause");
            isPaused = false;
        }
    }

    @FXML
    private void exitButtonPressed(ActionEvent event) {
        // Stop all runners
        for (Runner runner : runners) {
            runner.stop();
        }

        // Close the application
        Platform.exit();
    }
    private void raceEnd() {
    for (Runner runner : runners) {
                      runner.stop();
                  }
    }
}
