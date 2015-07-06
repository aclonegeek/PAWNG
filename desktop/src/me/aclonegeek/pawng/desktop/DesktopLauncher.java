package me.aclonegeek.pawng.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.aclonegeek.pawng.PAWNG;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "PAWNG";
		cfg.width = 1280;
		cfg.height = 720;

		new LwjglApplication(new PAWNG(), cfg);
	}
}
