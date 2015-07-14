package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by RandyT on 7/13/2015.
 */
public class GameScreen implements Screen {
    final PAWNG game;

    Texture paddleImage;
    Texture ballImage;

    Rectangle paddle1;
    Rectangle paddle2;
    Rectangle ball;

    Sound hit;

    OrthographicCamera camera;

    public GameScreen(final PAWNG game) {
        this.game = game;

        loadAssets();
    }

    private void loadAssets() {
        paddleImage = new Texture(Gdx.files.internal("img/paddle.png"));
        ballImage = new Texture(Gdx.files.internal("img/ball.png"));

        hit = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        paddle1 = new Rectangle(50, 360, 20, 100); // Left paddle (player paddle)
        paddle2 = new Rectangle(1230, 360, 20, 100); // Right paddle

        ball = new Rectangle(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, 32, 32);
    }

    private void processEvents() {
        // Move paddle up
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            paddle1.y += Constants.PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }

        // Move paddle down
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            paddle1.y -= Constants.PADDLE_SPEED * Gdx.graphics.getDeltaTime();
        }

        // Return to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Keep the player paddle in bounds
        if (paddle1.y < 0) paddle1.y = 0;
        if (paddle1.y > Constants.SCREEN_HEIGHT - 100) paddle1.y = Constants.SCREEN_HEIGHT - 100;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
            game.batch.draw(ballImage, ball.x, ball.y);
            game.batch.draw(paddleImage, paddle1.x, paddle1.y);
            game.batch.draw(paddleImage, paddle2.x, paddle2.y);
        game.batch.end();

        processEvents();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        paddleImage.dispose();
        ballImage.dispose();
        hit.dispose();
    }
}
