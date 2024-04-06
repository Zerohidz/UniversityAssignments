package com.duckhunt;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

public class DuckMoveAnimation implements IAnimation {
    private DuckMoveType moveType;
    private boolean isDead;
    private Vector2 position;
    private Vector2 velocity;
    private ImageView imageView;
    private Bounds gameBounds;
    private double boundsOffsetX;
    private double boundsOffsetY;

    public DuckMoveAnimation(ImageView imageView, DuckMoveType moveType, Bounds gameBounds) {
        this.imageView = imageView;
        this.moveType = moveType;
        this.gameBounds = gameBounds;
        boundsOffsetX = imageView.getFitWidth();
        boundsOffsetY = imageView.getFitHeight();
        velocity = new Vector2(10, moveType == DuckMoveType.DIAGONAL ? 10 : 0);
    }

    public void Tick() {
        handleMovement();

        if (isDead)
            moveDead();
        else if (moveType == DuckMoveType.HORIZONTAL)
            moveHorizontal();
        else if (moveType == DuckMoveType.DIAGONAL)
            moveDiagonal();
    }

    public void die() {
        isDead = true;
    }

    public void moveHorizontal() {
        velocity.y = 0; // No vertical movement in horizontal mode
    }

    public void moveDiagonal() {
        velocity.y = 10; // Set to a value for diagonal movement
    }

    public void moveDead() {
        velocity.y = 0; // No movement when dead
        velocity.x = 0;
    }

    public void handleMovement() {
        position = new Vector2(imageView.getTranslateX(), imageView.getTranslateY());
        position = position.add(velocity);
        
        // Check bounds and reverse direction if necessary
        if (position.x <= gameBounds.getMinX() || position.x >= gameBounds.getMaxX() - boundsOffsetX) {
            velocity.x *= -1;
        }

        if (moveType == DuckMoveType.DIAGONAL && (position.y <= gameBounds.getMinY() || position.y >= gameBounds.getMaxY() - boundsOffsetY)) {
            velocity.y *= -1;
        }

        imageView.setTranslateX(position.x);
        imageView.setTranslateY(position.y);
    }
}
