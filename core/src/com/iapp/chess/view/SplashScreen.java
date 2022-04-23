package com.iapp.chess.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.iapp.chess.controller.MenuController;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;

public class SplashScreen implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;

    private Music splashMusic;
    private Texture logo;
    private Texture logo1;
    private Texture logo2;

    private volatile float progress, lastProgress;

    public SplashScreen() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Settings.orientation.initSplashScreen(Orientation.Type.HORIZONTAL);
            Settings.orientation.initCamera(Orientation.Type.HORIZONTAL);
        }
        Gdx.graphics.setResizable(false);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        font = Settings.FONT.createArial(40);

        renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);

        splashMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/splash.mp3"));
        logo1 = new Texture(Gdx.files.internal("logo/iapp_logo.png"));
        logo2 = new Texture(Gdx.files.internal("logo/iapp_logo2.png"));


        Settings.ASSETS.loadRes();
        splashMusic.play();
        startAnimationLogo();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        synchronized (Settings.MUTEX) {
            update();
            camera.update();
            Gdx.graphics.setWindowedMode(900, 500);

            renderer.setColor(0.41f, 0.41f, 0.41f, 0);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.rect(0, 0, 900, Orientation.grayLineHeight);
            renderer.setColor(0.75f, 0.75f, 0.75f, 0);
            renderer.rect(0, 0, Orientation.cameraWidth * progress, Orientation.grayWhiteLineHeight);
            renderer.end();

            batch.begin();
            if (logo != null) {
                batch.draw(logo, Orientation.logoX, Orientation.logoY, Orientation.logoWidth, Orientation.logoHeight);
            }
            font.draw(batch, String.format("Loading %.2f%%", progress * 100), Orientation.bootTextX, Orientation.bootTextY);
            batch.end();

            if (isFinished()) {
                Settings.gdxGame.initGameRes();
                Gdx.graphics.setResizable(true);

                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    Settings.orientation.initSplashScreen(Settings.account.getOrientationType());
                    Settings.orientation.initCamera(Settings.account.getOrientationType());
                }

                MenuView mainView = new MenuView(new MenuController());
                Settings.gdxGame.setScreen(mainView);

                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            int windowWidth = Settings.account.getWindowSize().getKey();
            int windowHeight = Settings.account.getWindowSize().getValue();

            Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        }

        splashMusic.dispose();
        batch.dispose();
        renderer.dispose();
        font.dispose();
        logo1.dispose();
        logo2.dispose();
        splashMusic.dispose();
    }

    private void update() {
        if (!Settings.ASSETS.isFinished()) Settings.ASSETS.update();

        lastProgress = progress;
        progress = Settings.ASSETS.getProgress();

        if (lastProgress + 0.2f < progress || lastProgress + 0.05f > progress) {
            progress = lastProgress + 0.015f;
        } else if (progress > 0.79f) {
            progress = lastProgress + 0.015f;
        }

        if (progress > 1.0f) progress = 1.0f;
    }

    private boolean isFinished() {
        return Settings.ASSETS.isFinished() && progress == 1.0f;
    }

    private void startAnimationLogo() {
        Runnable task = () -> {
            while (true) {
                if (progress <= 0.4f || progress > 0.7f) {
                    setLogo(logo1);
                } else if (progress > 0.4f) {
                    setLogo(logo2);
                }
            }
        };
        Settings.gdxGame.execute(task);
    }

    private void setLogo(Texture logo) {
        this.logo = logo;
    }
}
