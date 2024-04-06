package com.duckhunt;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public Vector2(Vector2 other) {
        this(other.x, other.y);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 sub(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 mul(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public Vector2 div(double scalar) {
        return new Vector2(this.x / scalar, this.y / scalar);
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2 normalize() {
        return this.div(this.magnitude());
    }
}
