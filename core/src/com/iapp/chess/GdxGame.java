package com.iapp.chess;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.iapp.chess.controller.Account;
import com.iapp.chess.util.Assets;
import com.iapp.chess.util.FigureSet;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;
import com.iapp.chess.view.SplashScreen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GdxGame extends Game {

	private final Launcher launcher;

	private TextureAtlas chessAtlas;
	private TextureAtlas uiKitAtlas;
	private FigureSet figureSet;
	private ExecutorService threadPool;

	private Skin buttonSkin;
	private Skin labelSkin;
	private Skin dialogSkin;
	private Skin uiKit;

	public GdxGame(Launcher launcher) {
		this.launcher = launcher;
	}

	public TextureAtlas.AtlasRegion findRegion(String name) {
		TextureAtlas.AtlasRegion region = chessAtlas.findRegion(name);
		if (region == null) throw new IllegalArgumentException("Region can't' be null! Title of region = " + name);
		return region;
	}

	public TextureAtlas.AtlasRegion findUIKitRegion(String name) {
		TextureAtlas.AtlasRegion region = uiKitAtlas.findRegion(name);
		if (region == null) throw new IllegalArgumentException("Region can't be null! Title of region = " + name);
		return region;
	}

	public FigureSet getFigureSet() {
		return figureSet;
	}

	public Skin getButtonSkin() {
		return buttonSkin;
	}

	public Skin getLabelSkin() {
		return labelSkin;
	}

	public Skin getDialogSkin() {
		return dialogSkin;
	}

	public Skin getUIKit() {
		return uiKit;
	}

	public void execute(Runnable task) {
		threadPool.execute(task);
	}

	@Override
	public void create() {
		try {
			initSettings();
			initGdxGUI();
			threadPool = Executors.newFixedThreadPool(2);

			setScreen(new SplashScreen());
		} catch (Throwable t) {
			Settings.LOGGER.log(t);
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		try {
			if (Settings.LOGGER.isEmpty()) super.render();
			Settings.LOGGER.render();
		} catch  (Throwable t) {
			Settings.LOGGER.log(t);
		}
 	}

	@Override
	public void dispose() {
		Settings.account.saveWindowSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Settings.DATA.saveAccount(Settings.account);
		Settings.DATA.appendLogs();

		threadPool.shutdownNow();
		if (chessAtlas != null) chessAtlas.dispose();

		Settings.SOUNDS.dispose();
		Settings.FONT.dispose();
		Settings.ASSETS.dispose();
		Settings.LOGGER.dispose();

		super.dispose();
		System.exit(0);
	}

	public void goToScreen(Stage currentStage, RunnableAction intent, float duration) {
		SequenceAction sequenceAction = new SequenceAction();

		sequenceAction.addAction(Actions.run(Settings.SOUNDS::playClick));
		sequenceAction.addAction(Actions.moveBy(0, -currentStage.getHeight(), duration));
		sequenceAction.addAction(intent);

		currentStage.getRoot().addAction(sequenceAction);
	}

	public void initGameRes() {
		Settings.SOUNDS.initSounds();
		chessAtlas = Settings.ASSETS.get(Assets.CHESS_ATLAS);
		uiKitAtlas = Settings.ASSETS.get(Assets.UI_KIT);
		figureSet = new FigureSet(
				Settings.ASSETS.get(Assets.ISOMETRIC_FIGURES),
				Settings.ASSETS.get(Assets.ROYALS_FIGURES),
				Settings.ASSETS.get(Assets.STANDARD_FIGURES)
		);
		BitmapFont arial = Settings.FONT.createArial();

		buttonSkin = new Skin();
		buttonSkin.addRegions(chessAtlas);
		buttonSkin.add("arial", arial);
		buttonSkin.load(Gdx.files.internal("jsons/button.json"));

		labelSkin = new Skin();
		labelSkin.addRegions(chessAtlas);
		labelSkin.add("arial", arial);
		labelSkin.load(Gdx.files.internal("jsons/label.json"));

		dialogSkin = new Skin();
		dialogSkin.addRegions(chessAtlas);
		dialogSkin.add("arial", arial);
		dialogSkin.load(Gdx.files.internal("jsons/dialog.json"));

		uiKit = new Skin();
		uiKit.addRegions(uiKitAtlas);
		uiKit.add("arial", arial);
		uiKit.load(Gdx.files.internal("jsons/ui_kit.json"));
	}

	private void initSettings() {
		Settings.gdxGame = this;
		Settings.launcher = launcher;
		Settings.orientation = new Orientation();

		Settings.account = Settings.DATA.readAccount();
		if (Settings.account == null) Settings.account = new Account();
	}

	private void initGdxGUI() {
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			Settings.account.updateOrientationType(Orientation.Type.HORIZONTAL);
			Settings.orientation.init(Orientation.Type.HORIZONTAL);
		} else {
			Settings.orientation.init(Settings.account.getOrientation());
		}
	}
}
