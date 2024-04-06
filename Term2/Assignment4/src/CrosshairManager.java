package com.duckhunt;

import java.util.HashMap;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

public class CrosshairManager {
    public enum CrosshairType {
        Red(1),
        Green(2),
        Blue(3),
        Yellow(4),
        Purple(5),
        Aqua(6),
        Black(7);

        public int id;

        private CrosshairType(int id) {
            this.id = id;
        }
    }

    private static Cursor defaultCursor;
    private static HashMap<CrosshairType, Image> crosshairImages;

    public static void Initialize() {
        defaultCursor = Cursor.DEFAULT;
        initCrosshairImages();
        setCrosshair(CrosshairType.Red);
    }

    private static void initCrosshairImages() {
        crosshairImages = new HashMap<CrosshairType, Image>();

        for (CrosshairType type : CrosshairType.values()) {
            String path = String.format("assets/crosshair/%d.png", type.id);
            Image image = DuckHunt.getImage(path);
            crosshairImages.put(type, image);
        }
    }

    public static void setCrosshair(CrosshairType crosshairType) {
        Cursor crosshairCursor = new ImageCursor(crosshairImages.get(crosshairType),
                crosshairImages.get(crosshairType).getWidth() / 2,
                crosshairImages.get(crosshairType).getHeight() / 2);
        SceneManager.getCurrentScene().setCursor(crosshairCursor);
    }

    public static void setDefaultCursor() {
        SceneManager.getCurrentScene().setCursor(defaultCursor);
    }
}
