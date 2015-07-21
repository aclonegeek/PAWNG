package me.aclonegeek.pawng;

/**
 * Created by RandyT on 7/17/2015.
 */
public class Paddle extends GameObject {
    private static final float SPEED = 400f;

    public float paddleCenterY = super.getY() + (super.getHeight() / 2);

    public Paddle() {
        super(20, 100);
    }

// Getters
    public static float getSpeed() {
        return SPEED;
    }
}
