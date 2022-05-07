package com.iapp.chess;

import com.iapp.chess.util.CallListener;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate implements Launcher {

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new GdxGame(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void setRequestedVertically() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRequestedHorizontally() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addOnFinish(CallListener callListener) {
        throw new UnsupportedOperationException();
    }
}