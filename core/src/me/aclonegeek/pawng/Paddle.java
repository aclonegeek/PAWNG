package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by RandyT on 7/17/2015.
 */
public class Paddle extends GameObject {
    private static final float SPEED = 400f;
    public static final Texture paddleTexture = new Texture(Gdx.files.internal("img/paddle.png"));

    private boolean paddle1;

    public Paddle(int width, int height, boolean paddle1) {
        super(width, height); // 20, 100
        this.paddle1 = paddle1;
    }

    @Override
    public void update(float dt) {
        integrate(dt);
        updateBounds();
        contain();
    }

    private void contain() {
        if (top() > Gdx.graphics.getHeight()) {
            move(getX(), Gdx.graphics.getHeight() - getHeight());
            setVelocity(0f, 0f);
        }

        if (bottom() < 0) {
            move(getX(), 0);
            setVelocity(0f, 0f);
        }
    }

    public void handleInput(boolean paddle1) {
        if (paddle1) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                setVelocity(0f, getSpeed());
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                setVelocity(0f, -getSpeed());
            } else {
                setVelocity(0f, 0f);
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setVelocity(0f, getSpeed());
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setVelocity(0f, -getSpeed());
            } else {
                setVelocity(0f, 0f);
            }
        }
    }

    @Override
    public void reset() {
        if (paddle1) {
            move((Gdx.graphics.getWidth() * 0.1f), (Gdx.graphics.getHeight() - getHeight()) / 2);
        } else {
            move(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.1f), (Gdx.graphics.getHeight() - getHeight()) / 2);
        }
    }

    // Getters
    public static float getSpeed() {
        return SPEED;
    }
}
