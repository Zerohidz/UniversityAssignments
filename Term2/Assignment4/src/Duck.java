package com.duckhunt;

import java.util.Arrays;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Duck {
    private static final String[] DuckTypeNames = new String[] { "duck_black", "duck_red", "duck_blue" };

    private DuckMoveType moveType;
    private ImageView imageView;
    private Image[] images;
    private SpriteAnimation flyAnimation;
    private SpriteAnimation deathAnimation;
    private DuckMoveAnimation moveAnimation;
    private Animator animator;

    public Duck(Pane root, DuckMoveType moveType) {
        images = getRandomDuckSprites();
        this.moveType = moveType;
        initImageView(root);
        initAnimations();
    }

    public void die() {
        animator.removeAnimation(flyAnimation);
        animator.AddAnimation(deathAnimation);
        animator.Start();
    }

    private void initAnimations() {
        animator = new Animator(Duration.millis(250));
        if (moveType == DuckMoveType.DIAGONAL)
            flyAnimation = new SpriteAnimation(imageView, Arrays.copyOfRange(images, 0, 3), true);
        else
            flyAnimation = new SpriteAnimation(imageView, Arrays.copyOfRange(images, 3, 6), true);
        moveAnimation = new DuckMoveAnimation(imageView, moveType, new BoundingBox(0, 0, DuckHunt.WIDTH * DuckHunt.SCALE, DuckHunt.HEIGHT * DuckHunt.SCALE));
        deathAnimation = new SpriteAnimation(imageView, Arrays.copyOfRange(images, 6, 8), false);
        animator.AddAnimation(flyAnimation);
        animator.AddAnimation(moveAnimation);
        animator.Start();
    }

    private void initImageView(Pane root) {
        imageView = new ImageView();
        imageView.setScaleX(DuckHunt.SCALE / 2);
        imageView.setScaleY(DuckHunt.SCALE / 2);
        root.getChildren().add(imageView);
    }

    private Image[] getRandomDuckSprites() {
        String randomDuckTypeName = DuckTypeNames[DuckHunt.Random.nextInt(DuckTypeNames.length)];
        return new Image[] {
                DuckHunt.getImage(String.format("assets/%s/1.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/2.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/3.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/4.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/5.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/6.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/7.png", randomDuckTypeName)),
                DuckHunt.getImage(String.format("assets/%s/8.png", randomDuckTypeName)),
        };
    }
}
