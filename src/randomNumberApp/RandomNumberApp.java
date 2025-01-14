package randomNumberApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import opencvutils.WebcamCapture;

import java.util.Timer;
import java.util.TimerTask;

public class RandomNumberApp extends Application {
    private WebcamCapture webcamCapture;
    private ImageView imageView;
    private Label hashLabel;
    private Label statusBar;
    private Timer timer;
    private boolean isStreaming = true;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Random Number Generator");

        // Main Layout
        BorderPane root = new BorderPane();
        VBox topLayout = new VBox(15);
        topLayout.setPadding(new Insets(20));
        topLayout.setAlignment(Pos.CENTER);

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        hashLabel = new Label("Hash: Not Captured");
        hashLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        topLayout.getChildren().addAll(imageView, hashLabel);

        root.setTop(topLayout);

        // Input and Buttons Layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(15);
        inputGrid.setPadding(new Insets(20));
        inputGrid.setAlignment(Pos.CENTER);

        TextField minField = new TextField("1");
        TextField maxField = new TextField("1000");

        Label minLabel = new Label("Min:");
        Label maxLabel = new Label("Max:");
        Button captureButton = new Button("Capture Picture");
        Button generateButton = new Button("Generate Number");

        // Styling Buttons
        captureButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        generateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Tooltips
        captureButton.setTooltip(new Tooltip("Click to capture an image."));
        generateButton.setTooltip(new Tooltip("Click to generate a random number using the image hash."));
        minField.setTooltip(new Tooltip("Enter the minimum value for the random number range."));
        maxField.setTooltip(new Tooltip("Enter the maximum value for the random number range."));

        inputGrid.addRow(0, minLabel, minField);
        inputGrid.addRow(1, maxLabel, maxField);
        inputGrid.addRow(2, captureButton, generateButton);

        Label numberLabel = new Label("Generated Number: -");
        numberLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox centerLayout = new VBox(20, inputGrid, numberLabel);
        centerLayout.setAlignment(Pos.CENTER);
        root.setCenter(centerLayout);

        // Status Bar
        statusBar = new Label("Ready");
        statusBar.setStyle("-fx-background-color: #ddd; -fx-padding: 10px; -fx-font-size: 12px;");
        root.setBottom(statusBar);

        // Initialize Webcam
        webcamCapture = new WebcamCapture();
        if (!webcamCapture.isOpened()) {
            showError("Error: Could not access webcam.");
            return;
        }

        // Start Webcam Feed
        startWebcamFeed();

        // Button Actions
        captureButton.setOnAction(e -> captureImage());
        generateButton.setOnAction(e -> generateRandomNumber(minField, maxField, numberLabel));

        // Show Scene
        Scene scene = new Scene(root, 500, 700);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> stopWebcamFeed());
        primaryStage.show();
    }

    private void startWebcamFeed() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isStreaming) {
                    Image frame = webcamCapture.getCurrentFrame();
                    if (frame != null) {
                        Platform.runLater(() -> imageView.setImage(frame));
                    }
                }
            }
        }, 0, 33); // Update every ~33ms (30 FPS)
    }

    private void stopWebcamFeed() {
        if (timer != null) {
            timer.cancel();
        }
        if (webcamCapture != null) {
            webcamCapture.release();
        }
    }

    private void captureImage() {
        isStreaming = false; // Stop live streaming
        Image capturedFrame = webcamCapture.getCurrentFrame();
        if (capturedFrame != null) {
            imageView.setImage(capturedFrame);
            String hash = webcamCapture.calculateImageHash();
            hashLabel.setText("Hash: " + hash);
            updateStatus("Image captured successfully!");
        } else {
            showError("Error: Unable to capture image.");
        }
    }

    private void generateRandomNumber(TextField minField, TextField maxField, Label numberLabel) {
        try {
            captureImage(); // Capture a new frame before generating a number

            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());
            if (min > max) {
                showError("Error: Minimum value cannot be greater than maximum value.");
                return;
            }
            String hashText = hashLabel.getText().replace("Hash: ", "").trim();
            if (hashText.isEmpty() || hashText.equals("Not Captured")) {
                showError("Error: No hash available.");
                return;
            }

            // Generate random number using hash
            int hashValue = Math.abs(hashText.hashCode());
            int randomNum = min + (hashValue % (max - min + 1));
            numberLabel.setText("Generated Number: " + randomNum);
            updateStatus("Random number generated!");
        } catch (NumberFormatException e) {
            showError("Error: Invalid range input.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        updateStatus(message);
    }

    private void updateStatus(String message) {
        statusBar.setText(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
