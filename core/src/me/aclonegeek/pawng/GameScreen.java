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

    private float ballCenterY;
    private float paddleCenterY;
    private float difference;
    private float position;
    private float angle;

    private boolean paddle1MoveDown = false;
    private boolean paddle1MoveUp = false;
    private boolean paddle2MoveDown = false;
    private boolean paddle2MoveUp = false;

    private int paddle1Score;
    private int paddle2Score;

    private Texture paddleImage;
    private Texture ballImage;

    private Sound hitSound;
    private float hitSoundVolume = 0.1f;

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

        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void reset() {
        // Reset score
        paddle1Score = 0;
        paddle2Score = 0;

        // Reset object locations
        ball.move((Gdx.graphics.getWidth() / 2) - ball.getWidth(), (Gdx.graphics.getHeight() / 2) - ball.getHeight()); // Center ball\
        velocity = ball.getVelocity();
        velocity.set(ball.getSpeed(), 0f);
        velocity.setAngle(45f);
        ball.setVelocity(velocity.x, velocity.y);

        paddle1.move(field.x + (field.width * 0.1f), field.y + (field.height - paddle1.getHeight()) / 2);
        paddle2.move(field.x + field.width - (field.width * 0.1f), field.y + (field.height - paddle2.getHeight()) / 2);
    }

    private void score(boolean player1Score) {
        if(player1Score) {
            paddle1Score++;
        } else {
            paddle2Score++;
        }

        // Reset object locations
        ball.move((Gdx.graphics.getWidth() / 2) - ball.getWidth(), (Gdx.graphics.getHeight() / 2) - ball.getHeight()); // Center ball
        velocity = ball.getVelocity();
        velocity.set(ball.getSpeed(), 0f);
        velocity.setAngle(45f);
        ball.setVelocity(velocity.x, velocity.y);

        paddle1.move(field.x + (field.width * 0.1f), field.y + (field.height - paddle1.getHeight()) / 2);
        paddle2.move(field.x + field.width - (field.width * 0.1f), field.y + (field.height - paddle2.getHeight()) / 2);
    }

    private void processEvents(float dt) {
        handleInput();

        ballLogic(dt);
        paddle1Logic(dt);
        paddle2Logic(dt);
    }

    private void handleInput() {
        // Menu Related
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Reset game screen
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            reset();
        }

        // Paddle1 input
        // Move paddle up
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            paddle1MoveUp = true;
            paddle1MoveDown = false;
        }

        // Move paddle down
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            paddle1MoveUp = false;
            paddle1MoveDown = true;
        }

        // Paddle 2 input
        // Move paddle up
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            paddle2MoveUp = true;
            paddle2MoveDown = false;
        }

        // Move paddle down
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            paddle2MoveUp = false;
            paddle2MoveDown = true;
        }
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
            score(false); // Player 2 scores
        }

        if(ball.right() > fieldRight) {
            score(true); // Player 1 scores
        }

        // Ball-paddle collision logic
        if(ball.getBounds().overlaps(paddle1.getBounds())) {
            if(ball.left() < paddle1.right() && ball.right() > paddle1.right()) {
                hitSound.setVolume(hitSound.play(), hitSoundVolume);

                ball.move(paddle1.right(), ball.getY());
                ball.reflect(true, false);

                ballCenterY = ball.getY() + (ball.getHeight() / 2);
                paddleCenterY = paddle1.getY() + (paddle1.getHeight() / 2);
                difference = ballCenterY - paddleCenterY;
                position = difference / paddle1.getHeight();
                angle = ball.getReflectAngle() * position;

                velocity.setAngle(angle);
                velocity.scl(ball.getSpeedModifier());
                ball.setVelocity(velocity.x, velocity.y);
            }
        } else if(ball.getBounds().overlaps(paddle2.getBounds())) {
            if(ball.right() > paddle2.left() && ball.left() < paddle2.left()) {
                hitSound.setVolume(hitSound.play(), hitSoundVolume);

                ball.move(paddle2.left() - ball.getWidth(), ball.getY());
                ball.reflect(true, false);

                ballCenterY = ball.getY() + (ball.getHeight() / 2);
                paddleCenterY = paddle2.getY() + (paddle2.getHeight() / 2);
                difference = ballCenterY - paddleCenterY;
                position = difference / paddle2.getHeight();
                angle = ball.getReflectAngle() * position;

                velocity.setAngle(180f - angle);
                velocity.scl(ball.getSpeedModifier());
                ball.setVelocity(velocity.x, velocity.y);
            }
        }
    }

    public void paddle1Logic(float dt) {
        if(paddle1MoveUp) {
            paddle1.setVelocity(0f, paddle1.getSpeed());
            paddle1MoveUp = false;
        } else if(paddle1MoveDown) {
            paddle1.setVelocity(0f, -paddle1.getSpeed());
            paddle1MoveDown = false;
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

    private void paddle2Logic(float dt) {
        if(paddle2MoveUp) {
            paddle2.setVelocity(0f, paddle2.getSpeed());
            paddle2MoveUp = false;
        } else if(paddle2MoveDown) {
            paddle2.setVelocity(0f, -paddle2.getSpeed());
            paddle2MoveDown = false;
        } else {
            paddle2.setVelocity(0f, 0f);
        }

        paddle2.integrate(dt);
        paddle2.updateBounds();

        // Collision logic
        if(paddle2.top() > fieldTop) {
            paddle2.move(paddle2.getX(), fieldTop - paddle2.getHeight());
            paddle2.setVelocity(0f, 0f);
        }

        if(paddle2.bottom() < fieldBottom) {
            paddle2.move(paddle2.getX(), fieldBottom);
            paddle2.setVelocity(0f, 0f);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        dt = Gdx.graphics.getDeltaTime();

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
            game.font.draw(game.batch, Integer.toString(paddle1Score), 320, 700);
            game.font.draw(game.batch, Integer.toString(paddle2Score), 960, 700);
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
        hitSound.dispose();
    }
}
