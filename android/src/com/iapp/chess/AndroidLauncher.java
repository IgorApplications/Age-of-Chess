package com.iapp.chess;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.iapp.chess.util.CallListener;

public class AndroidLauncher extends AndroidApplication implements Launcher {

	private CallListener onFinish;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new GdxGame(this), config);
	}

	@Override
	public void setRequestedVertically() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void setRequestedHorizontally() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	public void addOnFinish(CallListener callListener) {
		onFinish = callListener;
	}

	@Override
	public int[] getDeviceScreen() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void finish() {
		if (onFinish != null) onFinish.call();
		else super.finish();
	}
}
