package org.gremlincorp.dtf.java;

import org.gremlincorp.dtf.core.DoomedToFail;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DoomedToFailDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		new LwjglApplication(new DoomedToFail(), config);
	}
}
