package com.duckhunt;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class SceneManager {
    private static Scene currentScene;
    private static Scene[] scenes;
    private static int currentSceneIndex = -1;

    private static int currentBackgroundIndex = 0;
    private static int currentCrosshairIndex = 0;
    private static CrosshairManager.CrosshairType[] crosshairTypes = CrosshairManager.CrosshairType.values();

    public static void initialize() {
        initScenes();
        nextScene();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void nextScene() {
        currentSceneIndex++;
        if (currentSceneIndex >= scenes.length)
            currentSceneIndex = 0;
        currentScene = scenes[currentSceneIndex];
        DuckHunt.Stage.setScene(currentScene);
    }

    private static void initScenes() {
        scenes = new Scene[6];

        scenes[0] = getMainMenuScene();
        scenes[1] = getSelectionScene();
        scenes[2] = getScene2();
    }

    private static Scene getMainMenuScene() {
        Pane root = new Pane();
        ImageView welcomeBackground = new ImageView(DuckHunt.getImage("assets/welcome/1.png"));
        welcomeBackground.setFitWidth(DuckHunt.WIDTH * DuckHunt.SCALE);
        welcomeBackground.setFitHeight(DuckHunt.HEIGHT * DuckHunt.SCALE);
        root.getChildren().add(welcomeBackground);

        putWelcomeTexts(root);

        Scene scene = new Scene(root, DuckHunt.WIDTH * DuckHunt.SCALE, DuckHunt.HEIGHT * DuckHunt.SCALE);
        scene.setOnKeyPressed(e -> {
            if (e.getCode().toString() == "ENTER")
                nextScene();
            else if (e.getCode().toString() == "ESCAPE")
                DuckHunt.Stage.close();
        });
        return scene;
    }

    private static void putWelcomeTexts(Pane root) {
        double SCALE = DuckHunt.SCALE;

        VBox vBox = new VBox();
        vBox.setTranslateX(35 * SCALE);
        vBox.setTranslateY(SCALE * 100);
        Text instructionsText = new Text("PRESS ENTER TO PLAY");
        instructionsText.setFont(Font.font(14 * SCALE));
        instructionsText.setFill(Color.ORANGE);
        vBox.getChildren().add(instructionsText);

        Text instructionsText2 = new Text("PRESS ESC TO EXIT");
        instructionsText2.setFont(Font.font(14 * SCALE));
        instructionsText2.setFill(Color.ORANGE);
        instructionsText2.setTranslateX(20 * SCALE);
        vBox.getChildren().add(instructionsText2);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            vBox.setVisible(!vBox.isVisible());
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely
        timeline.play();

        root.getChildren().add(vBox);
    }

    private static Scene getSelectionScene() {
        Pane root = new StackPane();
        Image[] backgroundImages = new Image[] {
                DuckHunt.getImage("assets/background/1.png"),
                DuckHunt.getImage("assets/background/2.png"),
                DuckHunt.getImage("assets/background/3.png"),
                DuckHunt.getImage("assets/background/4.png"),
                DuckHunt.getImage("assets/background/5.png"),
                DuckHunt.getImage("assets/background/6.png"),
        };

        ImageView backgroundImageView = new ImageView(backgroundImages[currentBackgroundIndex]);
        backgroundImageView.setFitWidth(DuckHunt.WIDTH * DuckHunt.SCALE);
        backgroundImageView.setFitHeight(DuckHunt.HEIGHT * DuckHunt.SCALE);
        root.getChildren().add(backgroundImageView);

        Scene scene = new Scene(root, DuckHunt.WIDTH * DuckHunt.SCALE, DuckHunt.HEIGHT * DuckHunt.SCALE);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    currentBackgroundIndex = event.getCode() == KeyCode.RIGHT
                            ? (currentBackgroundIndex + 1) % backgroundImages.length
                            : (backgroundImages.length + currentBackgroundIndex - 1) % backgroundImages.length;
                    backgroundImageView.setImage(backgroundImages[currentBackgroundIndex]);
                    break;
                case UP:
                case DOWN:
                    currentCrosshairIndex = event.getCode() == KeyCode.DOWN
                            ? (currentCrosshairIndex + 1) % crosshairTypes.length
                            : (crosshairTypes.length + currentCrosshairIndex - 1) % crosshairTypes.length;
                    CrosshairManager.setCrosshair(crosshairTypes[currentCrosshairIndex]);
                    break;
                case ENTER:
                    nextScene();
                default:
                    break;
            }
        });

        return scene;
    }

    private static Scene getScene2() {
        Pane root = new StackPane();
        new Duck(root, DuckMoveType.HORIZONTAL);

        Scene scene = new Scene(root, DuckHunt.WIDTH * DuckHunt.SCALE, DuckHunt.HEIGHT * DuckHunt.SCALE);
        return scene;
    }
}
