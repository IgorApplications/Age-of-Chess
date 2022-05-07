package com.iapp.chess;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.iapp.chess.util.CallListener;

import java.util.Arrays;

public class AndroidLauncher extends AndroidApplication implements Launcher {

	private View gameView;
	private CallListener onFinish;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
	public void finish() {
		if (onFinish != null) onFinish.call();
		else super.finish();
	}
}
