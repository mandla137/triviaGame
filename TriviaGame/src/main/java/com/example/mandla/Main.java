package com.example.mandla;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Set up the root layout
        StackPane root = new StackPane();

        // Load Lesotho image
        Image lesothoImage = new Image(getClass().getResourceAsStream("/images/waterfall.jpg"));

        BackgroundImage backgroundImage = new BackgroundImage(lesothoImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        root.setBackground(new Background(backgroundImage));

        // Create a VBox for the banner
        VBox banner = new VBox();
        banner.getStyleClass().add("banner");
        banner.setStyle("-fx-background-color: linear-gradient(to bottom right, #00ff7f, #00bfff, white); " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");
        banner.setPadding(new Insets(20));
        banner.setSpacing(20);
        banner.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/lsl.jpg")));
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        //  circular clipping mask
        Circle clip = new Circle(150, 150, 150);
        imageView.setClip(clip);

        //  text description
        Text descriptionText = new Text("Welcome to Lesotho Trivia Game!\nTest your knowledge about Lesotho.");
        descriptionText.getStyleClass().add("description");
        descriptionText.setFill(Color.WHITE);

        //  start button
        Button startButton = new Button("PLAY GAME");
        startButton.getStyleClass().add("button");
        startButton.setOnAction(e -> {
            TriviaGame gameScene = new TriviaGame();
            gameScene.start(primaryStage);
        });
        //  TranslateTransition to animate text
        TranslateTransition textAnimation = new TranslateTransition(Duration.seconds(3), startButton);
        textAnimation.setFromX(startButton.getWidth());
        textAnimation.setToX(-startButton.getWidth());
        textAnimation.setCycleCount(Timeline.INDEFINITE);
        textAnimation.setAutoReverse(true);
        textAnimation.play();

        //  TranslateTransition to animate button
        TranslateTransition buttonAnimation = new TranslateTransition(Duration.seconds(6), startButton);
        buttonAnimation.setFromX(0);
        buttonAnimation.setToX(100);
        buttonAnimation.setCycleCount(Timeline.INDEFINITE);
        buttonAnimation.setAutoReverse(true); 
        buttonAnimation.play();
        // Add elements to the banner
        banner.getChildren().addAll(imageView,descriptionText, startButton);

        // Add the banner to the root layout
        root.getChildren().add(banner);

        // Set up the scene
        Scene scene = new Scene(root, 600, 480);

        // CSS styling
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}