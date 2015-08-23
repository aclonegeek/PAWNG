package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by RandyT on 7/13/2015.
 */
public class GameScreen implements Screen {
    private final PAWNG game;

    private Ball ball;
    private Paddle paddle1;
    private Paddle paddle2;

    private float difference;
    private float position;
    private float angle;

    private int paddle1Score;
    private int paddle2Score;

    private Sound hitSound;
    private float hitSoundVolume = 0.1f;

    private OrthographicCamera camera;

    private Vector2 velocity;

    public static int scored = 0;

    public GameScreen(final PAWNG game) {
        this.game = game;

        initialize();
        reset(true);
    }

    private void initialize() {
        ball = new Ball(32, 32);
        paddle1 = new Paddle(20, 100, true);
        paddle2 = new Paddle(20, 100, false);

        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        velocity = ball.getVelocity();
    }

    private void reset(boolean resetScore) {
        // Reset score
        if (resetScore) {
            scored = 0;
            paddle1Score = 0;
            paddle2Score = 0;
        }

        // Reset object locations
        ball.reset();
        paddle1.reset();
        paddle2.reset();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        dt = Gdx.graphics.getDeltaTime();

        draw();
        update(dt);
    }

    private void checkIfScored() {
        if (scored == 1) {
            paddle1Score++;
            scored = 0;
        } else if (scored == 2){
            paddle2Score++;
            scored = 0;
        } else {
            return;
        }

        reset(false);
    }

    private void update(float dt) {
        ball.update(dt);
        paddle1.update(dt);
        paddle1.handleInput(true);
        paddle2.update(dt);
        paddle2.handleInput(false);

        handleInput();
        handleCollisions();
        checkIfScored();
    }

    private void handleInput() {
        // Return to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Reset game screen
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            reset(true);
        }
    }

    private void handleCollisions() {
        // Ball-paddle collision logic
        if (ball.getBounds().overlaps(paddle1.getBounds())) {
            if (ball.left() < paddle1.right() && ball.right() > paddle1.right()) {
                hitSound.setVolume(hitSound.play(), hitSoundVolume);

                ball.move(paddle1.right(), ball.getY());
                ball.reflect(true, false);

                difference = ball.centerY() - paddle1.centerY();
                position = difference / paddle1.getHeight();
                angle = ball.getReflectAngle() * position;

                velocity.setAngle(angle);
                velocity.scl(ball.getSpeedModifier());
                ball.setVelocity(velocity.x, velocity.y);
            }
        } else if (ball.getBounds().overlaps(paddle2.getBounds())) {
            if (ball.right() > paddle2.left() && ball.left() < paddle2.left()) {
                hitSound.setVolume(hitSound.play(), hitSoundVolume);

                ball.move(paddle2.left() - ball.getWidth(), ball.getY());
                ball.reflect(true, false);

                difference = ball.centerY() - paddle2.centerY();
                position = difference / paddle2.getHeight();
                angle = ball.getReflectAngle() * position;

                velocity.setAngle(180f - angle);
                velocity.scl(ball.getSpeedModifier());
                ball.setVelocity(velocity.x, velocity.y);
            }
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
            game.font.draw(game.batch, Integer.toString(paddle1Score), 320, 700);
            game.font.draw(game.batch, Integer.toString(paddle2Score), 960, 700);
            game.batch.draw(Ball.ballTexture, ball.getX(), ball.getY());
            game.batch.draw(Paddle.paddleTexture, paddle1.getX(), paddle1.getY());
            game.batch.draw(Paddle.paddleTexture, paddle2.getX(), paddle2.getY());
        game.batch.end();
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
        Ball.ballTexture.dispose();
        Paddle.paddleTexture.dispose();
        hitSound.dispose();
    }
}
