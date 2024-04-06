package com.duckhunt;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteAnimation implements IAnimation {
    public static int frameIndex = 0;
    private ImageView imageView;
    private Image[] animationSprites;
    private boolean loopInfinitely;

    public SpriteAnimation(ImageView target, Image[] sprites, boolean loopInfinitely) {
        animationSprites = sprites;
        imageView = target;
        imageView.setImage(sprites[0]);
        this.loopInfinitely = loopInfinitely;
    }

    public void Tick() {
        if (loopInfinitely)
            frameIndex = (frameIndex + 1) % animationSprites.length;
        else
            frameIndex = Math.min(frameIndex + 1, animationSprites.length - 1);
            
        imageView.setImage(animationSprites[frameIndex]);
    }
}
