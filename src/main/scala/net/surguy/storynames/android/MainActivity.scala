package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import net.surguy.android.shake.ShakeActivity
import android.widget.AdapterView.OnItemSelectedListener
import android.view.View
import android.widget.{RadioButton, LinearLayout, AdapterView, RadioGroup}
import android.graphics.{BitmapFactory, Color}
import android.graphics.drawable.BitmapDrawable
import net.surguy.android.{ImageResizer, RichViews}

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
        backgroundImage match {
          case "Angels" => setTextColor(Color.BLACK, Color.WHITE)
          case "Biblical" => setTextColor(Color.BLACK, Color.WHITE)
          case "Celtic" => setTextColor(Color.WHITE, Color.WHITE)
          case "Egyptian" => setTextColor(Color.BLACK, Color.BLACK)
          case "Elizabethan" => setTextColor(Color.WHITE, Color.WHITE)
          case "Finnish" => setTextColor(Color.BLACK, Color.BLACK)
          case "Roman" => setTextColor(Color.WHITE, Color.WHITE)
          case "Russian" => setTextColor(Color.WHITE, Color.WHITE)
          case "Victorian" => setTextColor(Color.BLACK, Color.WHITE)
          case _ => setTextColor(Color.BLACK, Color.BLACK)
        }

        val display = getWindowManager.getDefaultDisplay
        val newWidth = display.getWidth
        val newHeight = display.getHeight

        var bg = if (newWidth > newHeight) this.getClass.getResourceAsStream("/res/drawable/" + backgroundImage.toLowerCase + "_landscape.jpg") else null
        if (bg==null) bg = this.getClass.getResourceAsStream("/res/drawable/" + backgroundImage.toLowerCase + ".jpg")
        if (bg==null) bg = this.getClass.getResourceAsStream("/res/drawable/paper.jpg")

        val bitmap = BitmapFactory.decodeStream(bg)
        val resizedBitmap = new ImageResizer().resizeImage(bitmap, newWidth, newHeight)
        val bitmapDrawable = new BitmapDrawable(resizedBitmap)
        findView[LinearLayout](R.id.background).setBackgroundDrawable(bitmapDrawable)

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
