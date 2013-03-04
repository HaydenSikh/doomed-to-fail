package org.gremlincorp.dtf.android

import org.gremlincorp.dtf.core.DoomedToFail
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.ApplicationListener

class DoomedToFailActivity extends AndroidApplication {
    lazy val applicationListener = new DoomedToFail

    override def onCreate(savedInstanceState : Bundle) : Unit = {
        super.onCreate(savedInstanceState)
        val config = new AndroidApplicationConfiguration()
        config.useGL20 = true
        this.initialize(applicationListener, config)
    }

    override def getApplicationListener () : ApplicationListener = {
        return this.applicationListener
    }
}