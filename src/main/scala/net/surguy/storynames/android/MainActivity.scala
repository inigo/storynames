package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import net.surguy.android.shake.ShakeActivity
import net.surguy.android.RichViews
import android.widget.AdapterView.OnItemSelectedListener
import android.view.View
import android.graphics.Color
import android.widget.{RadioButton, LinearLayout, AdapterView, RadioGroup}

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

    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      override def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        val backgroundImage = spinner.getSelectedItem.toString
        // @todo There must be a way of looking up these IDs based on name, without usng reflection
        val imageId = backgroundImage match {
          case "Roman" => R.drawable.roman
          case "Celtic" => R.drawable.celtic
          case "Egyptian" => R.drawable.egyptian
          case _ => R.drawable.paper
        }
        backgroundImage match {
          case "Roman" => setTextColor(Color.BLACK, Color.WHITE)
          case "Egyptian" => setTextColor(Color.BLACK, Color.BLACK)
          case "Celtic" => setTextColor(Color.WHITE, Color.WHITE)
          case _ => setTextColor(Color.BLACK, Color.BLACK)
        }
        findView[LinearLayout](R.id.background).setBackgroundResource(imageId)
        text.setText(createName())
      }
      override def onNothingSelected(parent: AdapterView[_]) {}
    })
    findButton(R.id.create).onClick{ text.setText(createName()) }
    shakeListener = createShakeListener( text.setText(createName())  )
  }

  def setTextColor(top: Int, bottom: Int) {
    findTextView(R.id.text).setTextColor(top)
    findView[RadioButton](R.id.male).setTextColor(bottom)
    findView[RadioButton](R.id.female).setTextColor(bottom)
  }

}
