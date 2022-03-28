package com.iapp.chess;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.iapp.chess.controller.Account;
import com.iapp.chess.controller.Controller;
import com.iapp.chess.util.Assets;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;
import com.iapp.chess.view.GameView;
import com.iapp.chess.view.MainView;
import com.iapp.chess.view.SplashScreen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GdxGame extends Game {

	private final Launcher launcher;
	private TextureAtlas chessAtlas;
	private TextureAtlas standardFigures;
	private TextureAtlas modeFigures;

	private TextureAtlas uiKitAtlas;
	private ExecutorService threadPool;

	private Skin buttonSkin;
	private Skin labelSkin;
	private Skin dialogSkin;
	private Skin uiKit;

	private OrthographicCamera gdxCamera;
	private SpriteBatch gdxBatch;
	private BitmapFont gdxFont;

	public GdxGame(Launcher launcher) {
		this.launcher = launcher;
	}

	public TextureAtlas.AtlasRegion findRegion(String name) {
		TextureAtlas.AtlasRegion region = chessAtlas.findRegion(name);
		if (region == null) throw new RuntimeException("Iapp: region can not be null!" + " Title of region = " + name);
		return region;
	}

	public TextureAtlas.AtlasRegion findUIKitRegion(String name) {
		TextureAtlas.AtlasRegion region = uiKitAtlas.findRegion(name);
		if (region == null) throw new RuntimeException("Iapp: region can not be null!" + " Title of region = " + name);
		return region;
	}

	public TextureAtlas getChessAtlas() {
		return chessAtlas;
	}

	public TextureAtlas getStandardFigures() {
		return standardFigures;
	}

	public TextureAtlas getModeFigures() {
		return modeFigures;
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

	public OrthographicCamera getGdxCamera() {
		return gdxCamera;
	}

	public void execute(Runnable task) {
		threadPool.execute(task);
	}

	@Override
	public void create () {
		try {
			initSettings();
			initGdxGUI();
			threadPool = Executors.newFixedThreadPool(3);

			setScreen(new SplashScreen());
		} catch (Throwable t) {
			t.printStackTrace();
			Settings.LOGGER.log(t);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor( 0, 0, 0, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		try {
			if (!Settings.LOGGER.isEmpty()) {
				gdxCamera.update();
				gdxBatch.setProjectionMatrix(gdxCamera.combined);

				gdxBatch.begin();
				gdxFont.draw(gdxBatch, Settings.LOGGER.getText(), 20.0f, Orientation.cameraHeight - 20.0f, Orientation.cameraWidth - 30.0f, Align.left, true);
				gdxBatch.end();
			} else {
				super.render();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			Settings.LOGGER.log(t);
		}
	}

	@Override
	public void dispose () {
		Settings.controller.saveGame();
		Settings.account.saveWindowSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Settings.DATA.saveAccount(Settings.account);

		threadPool.shutdownNow();
		if (chessAtlas != null) chessAtlas.dispose();
		Settings.SOUNDS.dispose();
		Settings.FONT.dispose();
		Settings.ASSETS.dispose();

		super.dispose();
		System.exit(0);
	}

	public void goToMenu(GameView gameView, boolean saveGame) {
		Settings.controller.unblockGame();
		Settings.account.setDrawableHintMoves(true);

		if (saveGame) Settings.controller.saveGame();
		else Settings.controller.removeSavedGame();

		MainView mainView = new MainView();
		setScreen(mainView);
		gameView.dispose();
	}

	public void goToGame(Screen lastScreen) {
		Settings.controller.unblockGame();
		Settings.account.setDrawableHintMoves(true);

		GameView gameView = new GameView();
		Settings.controller.setGameView(gameView);
		Settings.controller.resetGame(Settings.account.getChoiceLevel());

		gameView.initGUI(Settings.controller);
		Settings.gdxGame.setScreen(gameView);

		lastScreen.dispose();
	}

	public void goToGame(Screen lastScreen, Stage lastStage) {
		Settings.controller.unblockGame();

		GameView gameView = new GameView();
		Settings.controller.setGameView(gameView);
		Settings.controller.resetGame(Settings.account.getChoiceLevel());

		gameView.initGUI(Settings.controller);

		goToScreen(lastStage, Actions.run(() -> {
				Settings.gdxGame.setScreen(gameView);
				lastScreen.dispose();
		}), 0.20f);
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
		standardFigures = Settings.ASSETS.get(Assets.STANDARD_FIGURES);
		modeFigures = Settings.ASSETS.get(Assets.MODE_FIGURES);

		BitmapFont arial = Settings.FONT.createArial();
		BitmapFont algerian = Settings.FONT.createAlgerian(50);

		buttonSkin = new Skin();
		buttonSkin.addRegions(chessAtlas);
		buttonSkin.add("arial", arial);
		buttonSkin.load(Gdx.files.internal("jsons/button.json"));

		labelSkin = new Skin();
		labelSkin.addRegions(chessAtlas);
		labelSkin.add("arial", arial);
		labelSkin.add("algerian", algerian);
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

		Settings.controller = new Controller();
		Settings.account = Settings.DATA.readAccount();
		if (Settings.account == null) Settings.account = new Account();
	}

	private void initGdxGUI() {
		gdxCamera = new OrthographicCamera();
		gdxCamera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);

		gdxBatch = new SpriteBatch();
		gdxFont = Settings.FONT.createArial(18);

		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			Settings.orientation.init(Settings.account.getOrientationType(), true);
		} else {
			Settings.orientation.init(Settings.account.getOrientationType());
		}
	}
}
