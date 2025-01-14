
package javafxtest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class JavaFXTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JavaFX is working!");
        Scene scene = new Scene(label, 300, 200);

        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
