package finalproject_fall2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author massi
 */
public class FinalProject_Fall2025 extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent fxmlRoot = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        Scene fxmlScene = new Scene(fxmlRoot);
        
        Pane startPane = new Pane();
        startPane.setPrefSize(900, 700);
        Label startLabel = new Label("WELCOME To Our Projectile Motion Simulator");
        startLabel.setFont(new Font(32));
        startLabel.setTextFill(Color.WHITE);

        startLabel.layoutXProperty().bind(startPane.widthProperty().subtract(startLabel.widthProperty()).divide(2));
        startLabel.setLayoutY(40);

        Button startButton1 = new Button("Start");
        startButton1.setFont(new Font(22));
        //centering start button
        startButton1.layoutXProperty().bind(startPane.widthProperty().subtract(startButton1.widthProperty()).divide(2));
        startButton1.layoutYProperty().bind(startPane.heightProperty().subtract(startButton1.heightProperty()).divide(2));

        // Background image
        Image bgImage = new Image(getClass().getResource("images\\StartBackground.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
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
        startPane.setBackground(new Background(backgroundImage));
        
        // START button action
        startButton1.setOnAction(e -> {
            stage.setScene(fxmlScene);// go to Porjectile Simulator Scene
        });
        startPane.getChildren().addAll(startLabel, startButton1);
        Scene startScene = new Scene(startPane);
        stage.setResizable(false);
        stage.setScene(startScene);
        stage.show();
    }
}
