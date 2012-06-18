package net.surguy.storynames.android

import net.surguy.android.{Logging, RichViews}
import android.app.Activity
import android.os.Bundle

/**
 * @todo Write some documentation!
 *
 * @author Inigo Surguy
 */

class AboutActivity extends Activity with RichViews with Logging {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.about)
  }
}