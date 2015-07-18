package me.aclonegeek.pawng;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PAWNG extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	private GameState currentState = GameState.MAINMENU;

	private enum GameState {
		MAINMENU,
		PLAY,
		RESET,
		EXIT,
	}

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();

		switch(currentState) {
			case MAINMENU:
				this.setScreen(new MainMenuScreen(this));
				break;
			case PLAY:
				this.setScreen(new GameScreen(this));
				break;
			case RESET:
				break;
			case EXIT:
				Gdx.app.exit();
				break;
		}
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
