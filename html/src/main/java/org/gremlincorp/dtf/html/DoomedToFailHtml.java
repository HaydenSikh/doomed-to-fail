package org.gremlincorp.dtf.html;

import org.gremlincorp.dtf.core.DoomedToFail;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class DoomedToFailHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new DoomedToFail();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
