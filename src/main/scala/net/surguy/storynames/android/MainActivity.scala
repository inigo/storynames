package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import android.view.View
import android.widget.{Spinner, Button, TextView}
import net.surguy.android.shake.{ShakeEventListener, ShakeActivity}

/**
 * Main activity for the app - displays names and allows culture selection.
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity with ShakeActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val text = findViewById(R.id.text).asInstanceOf[TextView]
    text.setText(Cultures.Roman.maleName())

    val spinner = findViewById(R.id.spinner).asInstanceOf[Spinner]
    def currentCulture = Cultures.getCulture(spinner.getSelectedItem.toString)

    val maleButton = findViewById(R.id.male).asInstanceOf[Button]
    val femaleButton = findViewById(R.id.female).asInstanceOf[Button]
    maleButton.setOnClickListener(new ClickListener(() => text.setText(currentCulture.maleName())))
    femaleButton.setOnClickListener(new ClickListener(() => text.setText(currentCulture.femaleName())))

    shakeListener = createShakeListener( text.setText(currentCulture.maleName())  )
  }

  class ClickListener(val fn: () => Unit) extends View.OnClickListener {
    def onClick(v: View) { fn() }
  }

}
