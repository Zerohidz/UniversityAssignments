package com.duckhunt;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;

public class DuckHunt extends Application {
    public static final double SCALE = 3;
    public static final int WIDTH = 640 / 3;
    public static final int HEIGHT = 480 / 3;
    public static final Random Random = new Random();
    public static Stage Stage;

    @Override
    public void start(Stage primaryStage) {
        Stage = primaryStage;
        primaryStage.setTitle("Duck Hunt");
        primaryStage.setResizable(false);

        SceneManager.initialize();
        primaryStage.show();

        CrosshairManager.Initialize();
    }

    public static Image getImage(String path) {
        try {
            File imageFile = new File(path);
            String imagePath = imageFile.toURI().toURL().toString();
            Image image = new Image(imagePath);
            return image;
        } catch (MalformedURLException e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
