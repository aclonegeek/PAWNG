package me.aclonegeek.pawng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by RandyT on 7/25/2015.
 */
public class OptionsScreen implements Screen {
    private final PAWNG game;

    public OptionsScreen(final PAWNG game) {
        this.game = game;

        Gdx.app.log("Screen", "Options");
    }

    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
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

    }
}
