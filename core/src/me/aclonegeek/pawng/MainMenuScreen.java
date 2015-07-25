package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by RandyT on 7/13/2015.
 */
public class MainMenuScreen implements Screen {
    private final PAWNG game;

    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 50f;

    private Stage stage = new Stage();

    private Skin buttonSkin = new Skin();
/*    private Skin labelSkin = new Skin();*/

    private Pixmap pixmap = new Pixmap((int)BUTTON_WIDTH, (int)BUTTON_HEIGHT, Pixmap.Format.RGB888);

    OrthographicCamera camera;

    public MainMenuScreen(final PAWNG game) {
        this.game = game;

        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        createButtonSkin();
/*        createLabelSkin();*/
        create();
    }

    public void createButtonSkin() {
        // Create font
        buttonSkin.add("default", game.font);

        // Create background
        pixmap.setColor(Color.rgb565(52f, 152f, 219f)); // Blue button background colour
        pixmap.fill();

        buttonSkin.add("background", new Texture(pixmap));

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonSkin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = buttonSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = buttonSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = buttonSkin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = buttonSkin.getFont("default");
        buttonSkin.add("default", textButtonStyle);
    }

/*    public void createLabelSkin() {
        // Create font
        labelSkin.add("default", game.font);

        //Create style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;
        labelStyle.fontColor = Color.WHITE;
    }*/

    private void create() {
/*        Label title = new Label("PAWNG", labelSkin);
        title.setPosition((Gdx.graphics.getWidth() / 2) - (title.getWidth() / 2), (Gdx.graphics.getHeight()) - (title.getHeight()));
        stage.addActor(title);*/

        TextButton playButton = new TextButton("PLAY", buttonSkin);
        playButton.setPosition((Gdx.graphics.getWidth() / 2) - (playButton.getWidth() / 2), 550f);
        stage.addActor(playButton);

        TextButton optionsButtons = new TextButton("OPTIONS", buttonSkin);
        optionsButtons.setPosition((Gdx.graphics.getWidth() / 2) - (optionsButtons.getWidth() / 2), 450f);
        stage.addActor(optionsButtons);

        TextButton exitButton = new TextButton("EXIT", buttonSkin);
        exitButton.setPosition((Gdx.graphics.getWidth() / 2) - (exitButton.getWidth() / 2), 350f);
        stage.addActor(exitButton);

        playButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
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

        stage.act();
        stage.draw();
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
        stage.dispose();
        buttonSkin.dispose();
        pixmap.dispose();
    }
}
