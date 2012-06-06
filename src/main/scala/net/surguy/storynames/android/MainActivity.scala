package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 06/06/2012 08:48
 */

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}