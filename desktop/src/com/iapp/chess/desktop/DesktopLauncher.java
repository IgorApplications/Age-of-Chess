package com.iapp.chess.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.iapp.chess.GdxGame;
import com.iapp.chess.Launcher;
import com.iapp.chess.util.CallListener;
public class DesktopLauncher implements Launcher {

	public static void main (String[] arg) {
		System.setProperty("user.name", "Пользователь");

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(900, 500);
		config.setTitle("Chess");
		config.setWindowIcon(Files.FileType.Internal,"icons/chess_icon128.png", "icons/chess_icon32.png", "icons/chess_icon16.png");
		new Lwjgl3Application(new GdxGame(new DesktopLauncher()), config);
	}

	@Override
	public void setRequestedVertically() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (height < width) {
			flip();
		}
	}

	@Override
	public void setRequestedHorizontally() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (height > width) {
			flip();
		}
	}

	@Override
	public void addOnFinish(CallListener callListener) {}

	private void flip() {
		Gdx.graphics.setWindowedMode(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
	}
}
