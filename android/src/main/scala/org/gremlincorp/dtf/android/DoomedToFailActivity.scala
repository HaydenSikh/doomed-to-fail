package org.gremlincorp.dtf.android

import org.gremlincorp.dtf.core.DoomedToFail

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class DoomedToFailActivity extends AndroidApplication {

   override def onCreate(savedInstanceState : Bundle) : Unit = {
       super.onCreate(savedInstanceState)
       val config = new AndroidApplicationConfiguration()
       config.useGL20 = true
       this.initialize(new DoomedToFail(), config)
   }
}