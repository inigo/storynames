package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import net.surguy.android.shake.ShakeActivity
import net.surguy.android.RichViews
import android.widget.RadioGroup

/**
 * Main activity for the app - displays names and allows culture selection.
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity with ShakeActivity with RichViews {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val text = findTextView(R.id.text)
    text.setText("Press button or shake")

    val spinner = findSpinner(R.id.spinner)
    def currentCulture = Cultures.getCulture(spinner.getSelectedItem.toString)
    def currentGender = findView[RadioGroup](R.id.gender).getCheckedRadioButtonId
    def createName(): String = {
      if (currentGender==R.id.male) currentCulture.maleName() else currentCulture.femaleName()
    }

    findButton(R.id.create).onClick{ text.setText(createName()) }
    shakeListener = createShakeListener( text.setText(createName())  )
  }

}
