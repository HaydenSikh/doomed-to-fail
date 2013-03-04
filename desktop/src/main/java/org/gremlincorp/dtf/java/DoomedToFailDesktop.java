package org.gremlincorp.dtf.java;

import org.gremlincorp.dtf.core.DoomedToFail;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DoomedToFailDesktop {
	public static void main(final String[] args) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = (int) DoomedToFail.viewWidth();
		config.height = (int) DoomedToFail.viewHeight();
		new LwjglApplication(new DoomedToFail(), config);
	}
}
