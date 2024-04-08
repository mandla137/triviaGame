package com.example.mandla;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class TriviaGame extends Application {
    public String[] mediaFiles = {
            getClass().getResource("lesotho_flag.jpg").toExternalForm(),
            getClass().getResource("peak.jpg").toExternalForm(),
            getClass().getResource("lang.jpg").toExternalForm(),
            getClass().getResource("waterfall.jpg").toExternalForm(),
            getClass().getResource("hat.jpg").toExternalForm()

    };
    private int currentQuestionIndex = 0;
    private int points = 0;
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #00008B, #00008B);" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 10, 0, 0, 0);");


        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setSpacing(140);

        Button exitButton = new Button("Quit Game");
        exitButton.setOnAction(e -> primaryStage.close());
        exitButton.getStyleClass().add("exit-button");

        Label stopwatchLabel = new Label("00:06");
        stopwatchLabel.getStyleClass().add("stopwatch-label");
        startTimer(stopwatchLabel, primaryStage);

        Label pointsLabel = new Label("Points: " + points);
        pointsLabel.getStyleClass().add("points-label");

        topBar.getChildren().addAll(exitButton, stopwatchLabel, pointsLabel);
        root.setTop(topBar);

        VBox mediaBox = new VBox();
        mediaBox.getStyleClass().add("media-box");
        mediaBox.setSpacing(20);

        Label questionLabel = new Label(questions[currentQuestionIndex]);
        questionLabel.getStyleClass().add("question-label");
        questionLabel.setWrapText(true);

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(mediaBox, questionLabel);

        root.setCenter(centerBox);


        VBox optionsBox = new VBox();
        optionsBox.getStyleClass().add("options-box");
        optionsBox.setSpacing(10);
        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            Button optionButton = new Button(options[currentQuestionIndex][i]);
            int finalI = i;
            optionButton.setOnAction(e -> handleAnswer(primaryStage, finalI));
            optionButton.getStyleClass().add("option-button");
            optionsBox.getChildren().add(optionButton);
        }
        root.setBottom(optionsBox);

        Scene scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();
    }
    private String[] questions = {
            "Question 1: What is the capital city of Lesotho?",
            "Question 2: What is the highest peak in Lesotho?",
            "Question 3: What is the official language of Lesotho?",
            "Question 4: Which river forms part of the border between Lesotho and South Africa?",
            "Question 5: What is the traditional Basotho hat called?"
    };

    private String[][] options = {
            {"Maseru", "Leribe", "Mafeteng", "Quthing"},
            {"Thabana Ntlenyana", "Thaba Putsoa", "Sani Pass", "Cathedral Peak"},
            {"English", "Sesotho", "French", "Zulu"},
            {"Caledon River", "Limpopo River", "Orange River", "Tugela River"},
            {"Mokorotlo", "Tophat", "Fez", "Sombrero"}
    };

    private int[] correctAnswers = {0, 0, 1, 0, 0};

    private void startTimer(Label stopwatchLabel, Stage stage) {
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> stopwatchLabel.setText("00:06")),
                new KeyFrame(Duration.seconds(1), e -> {
                    int secondsLeft = Integer.parseInt(stopwatchLabel.getText().substring(3)) - 1;
                    if (secondsLeft >= 0) {
                        stopwatchLabel.setText(String.format("00:0%d", secondsLeft));
                    } else {
                        timeline.stop();
                        handleAnswer(stage, -1); // Automatically select correct answer when time runs out
                    }
                })
        );
        timeline.setCycleCount(6);
        timeline.playFromStart();
    }


    private void handleAnswer(Stage stage, int selectedAnswerIndex) {
        timeline.stop();
        VBox optionsBox = (VBox) ((BorderPane) stage.getScene().getRoot()).getBottom();

        // Adjusted the access to option buttons
        Button[] optionButtons = optionsBox.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .toArray(Button[]::new);

        for (int i = 0; i < optionButtons.length; i++) {
            if (i == correctAnswers[currentQuestionIndex]) {
                optionButtons[i].getStyleClass().add("correct-answer");
            } else if (i == selectedAnswerIndex) {
                optionButtons[i].getStyleClass().add("wrong-answer");
            }
            optionButtons[i].setDisable(true);
        }

        if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
            points += 10;
        }

        Label pointsLabel = (Label) ((BorderPane) stage.getScene().getRoot()).getTop().lookup(".points-label");
        pointsLabel.setText("Points: " + points);

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            updateQuestion(stage);
        } else {
            showScore(stage);
        }
    }
    private void updateQuestion(Stage stage) {
        Label questionLabel = (Label) ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().get(1);
        questionLabel.setText(questions[currentQuestionIndex]);


        VBox optionsBox = (VBox) ((BorderPane) stage.getScene().getRoot()).getBottom();
        optionsBox.getChildren().clear();

        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            Button optionButton = new Button(options[currentQuestionIndex][i]);
            int finalI = i;
            optionButton.setOnAction(e -> handleAnswer(stage, finalI));
            optionButton.getStyleClass().add("option-button");
            optionsBox.getChildren().add(optionButton);
        }
        // Display media for the current question
        displayMedia((VBox) ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().get(0), mediaFiles[currentQuestionIndex]);
    }
    private void displayMedia( VBox mediaBox,String mediaFiles) {
        mediaBox.getChildren().clear(); // Clear existing media views

        // Create and style media view
        if (mediaFiles.endsWith(".jpg") || mediaFiles.endsWith(".png")) {
            ImageView imageView = new ImageView(new Image(mediaFiles));
            imageView.setPreserveRatio(true);
            double mediaBoxWidth = 500;
            double mediaBoxHeight = 300;
            imageView.setFitWidth(mediaBoxWidth);
            imageView.setFitHeight(mediaBoxHeight);
            imageView.getStyleClass().add("media-image");
            mediaBox.getChildren().add(imageView);
        } else if (mediaFiles.endsWith(".mp4")) {
            Media media = new Media(mediaFiles);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.getStyleClass().add("media-video");
            mediaBox.getChildren().add(mediaView);
            mediaPlayer.play(); // Automatically play video
        }
    }
    private void showScore(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Your score: " + points);

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e -> {
            alert.close(); // Close the alert
            restartGame(stage); // Restart the game
        });

        alert.getDialogPane().getButtonTypes().clear();
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        alert.getDialogPane().setContent(new VBox(new Label(alert.getContentText()), playAgainButton));

        alert.showAndWait(); // Show the alert
    }
    private void restartGame(Stage stage) {
        currentQuestionIndex = 0;
        points = 0;
        timeline.playFromStart();
        start(stage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}