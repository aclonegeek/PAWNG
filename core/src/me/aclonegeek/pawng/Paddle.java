package me.aclonegeek.pawng;

/**
 * Created by RandyT on 7/17/2015.
 */
public class Paddle extends GameObject {
    private static final float SPEED = 400f;

    public Paddle() {
        super(20, 100);
    }

// Getters
    public static float getSpeed() {
        return SPEED;
    }
}
