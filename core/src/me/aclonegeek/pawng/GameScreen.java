package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by RandyT on 7/13/2015.
 */
public class GameScreen implements Screen {
    private final PAWNG game;

    private Ball ball = new Ball();
    private Paddle paddle1 = new Paddle();
    private Paddle paddle2 = new Paddle();

    private Rectangle field = new Rectangle();

    private float fieldTop;
    private float fieldBottom;
    private float fieldLeft;
    private float fieldRight;

    private boolean moveDown = false;
    private boolean moveUp = false;

    private Texture paddleImage;
    private Texture ballImage;

    private Sound hit;

    private OrthographicCamera camera;

    private Vector2 velocity;

    public GameScreen(final PAWNG game) {
        this.game = game;

        field.set(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fieldTop = field.y + field.height;
        fieldBottom = field.y;
        fieldLeft = field.x;
        fieldRight = field.x + field.width;

        loadAssets();
        reset();
    }

    private void loadAssets() {
        paddleImage = new Texture(Gdx.files.internal("img/paddle.png"));
        ballImage = new Texture(Gdx.files.internal("img/ball.png"));

        hit = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void reset() {
        // Reset object locations
        ball.move((Gdx.graphics.getWidth() / 2) - ball.getWidth(), (Gdx.graphics.getHeight() / 2) - ball.getHeight()); // Center ball\
        velocity = ball.getVelocity();
        velocity.set(ball.getSpeed(), 0f);
        velocity.setAngle(45f);
        ball.setVelocity(velocity.x, velocity.y);

        paddle1.move(field.x + (field.width * 0.1f), field.y + (field.height - paddle1.getHeight()) / 2);
        paddle2.move(field.x + field.width - (field.width * 0.1f), field.y + (field.height - paddle2.getHeight()) / 2);
    }

    private void processEvents(float dt) {
        // Menu Related
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Reset game screen
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            reset();
        }

        ballLogic(dt);
        paddle1Logic(dt);
        //paddle2Logic(dt);
    }

    public void ballLogic(float dt) {
        ball.integrate(dt);
        ball.updateBounds();

        // Collision logic
        if(ball.top() > fieldTop) {
            ball.move(ball.getX(), fieldTop - ball.getHeight());
            ball.reflect(false, true);
        }

        if(ball.bottom() < fieldBottom) {
            ball.move(ball.getX(), fieldBottom);
            ball.reflect(false, true);
        }

        if(ball.left() < fieldLeft) {
            ball.move(fieldLeft, ball.getY());
            ball.reflect(true, false);
        }

        if(ball.right() > fieldRight) {
            ball.move(fieldRight - ball.getHeight(), ball.getY());
            ball.reflect(true, false);
        }
    }

    public void paddle1Logic(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveUp = true;
            moveDown = false;
        }

        // Move paddle down
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveUp = false;
            moveDown = true;
        }

        if(moveUp) {
            paddle1.setVelocity(0f, paddle1.getSpeed());
            moveUp = false;
        } else if(moveDown) {
            paddle1.setVelocity(0f, -paddle1.getSpeed());
            moveDown = false;
        } else {
            paddle1.setVelocity(0f, 0f);
        }

        paddle1.integrate(dt);
        paddle1.updateBounds();

        // Collision logic
        if(paddle1.top() > fieldTop) {
            paddle1.move(paddle1.getX(), fieldTop - paddle1.getHeight());
            paddle1.setVelocity(0f, 0f);
        }

        if(paddle1.bottom() < fieldBottom) {
            paddle1.move(paddle1.getX(), fieldBottom);
            paddle1.setVelocity(0f, 0f);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        dt = Gdx.graphics.getRawDeltaTime();

        //update(dt); - use this for states down the road (if i can get states working :()
        draw(dt);
        processEvents(dt);
    }

    private void draw(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
            game.batch.draw(ballImage, ball.getX(), ball.getY());
            game.batch.draw(paddleImage, paddle1.getX(), paddle1.getY());
            game.batch.draw(paddleImage, paddle2.getX(), paddle2.getY());
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
        paddleImage.dispose();
        ballImage.dispose();
        hit.dispose();
    }
}
