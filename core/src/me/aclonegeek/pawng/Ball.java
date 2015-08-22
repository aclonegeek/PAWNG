package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by RandyT on 7/13/2015.
 */
public class Ball extends GameObject {
    private Vector2 velocity = getVelocity();

    private static final float REFLECT_ANGLE = 65f;
    private static final float SPEED = 350f;
    private static final float MAX_SPEED = 650f;
    private static final float SPEED_MODIFIER = 1.05f;

    public static final Texture ballTexture = new Texture(Gdx.files.internal("img/ball.png"));

    public Ball(int width, int height) {
        super(width, height);
    }

    public void reflect(boolean x, boolean y) {
        if (x) velocity.x *= -1;
        if (y) velocity.y *= -1;

        setVelocity(velocity.x, velocity.y);
    }

    @Override
    public void update(float dt) {
        integrate(dt);
        updateBounds();
        contain();
    }

    private void contain() {
        // Collision logic
        if (top() > Gdx.graphics.getHeight()) {
            move(getX(), Gdx.graphics.getHeight() - getHeight());
            reflect(false, true);
        }

        if (bottom() < 0) {
            move(getX(), 0);
            reflect(false, true);
        }

        if (left() < 0) {
            GameScreen.scored = 2; // Player 2 scores
        }

        if (right() > Gdx.graphics.getWidth()) {
            GameScreen.scored = 1; // Player 1 scores
        }
    }

    @Override
    public void reset() {
        move((Gdx.graphics.getWidth() / 2) - getWidth(), (Gdx.graphics.getHeight() / 2) - getHeight());
        velocity.set(SPEED, 0f);
        velocity.setAngle(randomAngle());
        velocity.set(velocity.x, velocity.y);
    }

    private float randomAngle() {
        Random rand = new Random();
        int x = rand.nextInt(4) + 1;

        float angle = 45f;

        if (x == 1) angle = 45f;
        if (x == 2) angle = 135f;
        if (x == 3) angle = 225f;
        if (x == 4) angle = 315f;

        return angle;
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
