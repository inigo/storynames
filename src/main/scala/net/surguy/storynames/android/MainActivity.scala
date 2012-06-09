package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import android.view.View
import android.widget.{Spinner, Button, TextView}
import net.surguy.android.shake.{ShakeEventListener, ShakeActivity}
import android.view.View.OnClickListener
import net.surguy.android.ViewHelpers

/**
 * Main activity for the app - displays names and allows culture selection.
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity with ShakeActivity with ViewHelpers {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val text = findTextView(R.id.text)
    text.setText(Cultures.Roman.maleName())

    val spinner = findSpinner(R.id.spinner)
    def currentCulture = Cultures.getCulture(spinner.getSelectedItem.toString)

    findButton(R.id.male).onClick{ text.setText(currentCulture.maleName()) }
    findButton(R.id.female).onClick{ text.setText(currentCulture.femaleName()) }

    shakeListener = createShakeListener( text.setText(currentCulture.maleName())  )
  }

}
