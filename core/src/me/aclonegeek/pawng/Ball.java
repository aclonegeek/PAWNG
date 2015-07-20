package me.aclonegeek.pawng;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by RandyT on 7/13/2015.
 */
public class Ball extends GameObject {
    private Vector2 velocity = getVelocity();

    private static final float REFLECT_ANGLE = 65f;
    private static final float SPEED = 350f;
    private static final float MAX_SPEED = 650f;
    private static final float SPEED_MODIFIER = 1.05f;

    private float ballCenterY = super.getY() + (super.getHeight() / 2);

    public Ball() {
        super(32, 32);
    }

    public void reflect(boolean x, boolean y) {
        if(x) velocity.x *= -1;
        if(y) velocity.y *= -1;

        setVelocity(velocity.x, velocity.y);
    }

// Getters
    public static float getSpeed() {
        return SPEED;
    }

    public static float getMaxSpeed() {
        return MAX_SPEED;
    }

    public static float getReflectAngle() {
        return REFLECT_ANGLE;
    }

    public static float getSpeedModifier() {
        return SPEED_MODIFIER;
    }
}
