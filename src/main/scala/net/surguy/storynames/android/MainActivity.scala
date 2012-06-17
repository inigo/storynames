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

  var lastGenderWasMale = true

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    lastGenderWasMale = math.random >= 0.5

    val text = findTextView(R.id.text)

    val spinner = findSpinner(R.id.spinner)
    def cultureName = spinner.getSelectedItem.toString
    def currentCulture = Cultures.getCulture(cultureName)
    def createName(): String = {
      if (lastGenderWasMale) currentCulture.maleName() else currentCulture.femaleName()
    }

    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      override def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        val textColor = cultureName match {
          case "Celtic" => Color.WHITE
          case "Elizabethan" => Color.WHITE
          case "Roman" => Color.WHITE
          case "Russian" => Color.WHITE
          case _ => Color.BLACK
        }
        text.setTextColor(textColor)

        val display = getWindowManager.getDefaultDisplay
        val newWidth = display.getWidth
        val newHeight = display.getHeight

        var bg = if (newWidth > newHeight) this.getClass.getResourceAsStream("/res/drawable/" + cultureName.toLowerCase + "_landscape.jpg") else null
        if (bg==null) bg = this.getClass.getResourceAsStream("/res/drawable/" + cultureName.toLowerCase + ".jpg")
        if (bg==null) bg = this.getClass.getResourceAsStream("/res/drawable/paper.jpg")

        val bitmap = BitmapFactory.decodeStream(bg)
        val resizedBitmap = new ImageResizer().resizeImage(bitmap, newWidth, newHeight)
        val bitmapDrawable = new BitmapDrawable(getResources, resizedBitmap)
        findView[LinearLayout](R.id.background).setBackgroundDrawable(bitmapDrawable)

        text.setText(createName())
      }
      override def onNothingSelected(parent: AdapterView[_]) {}
    })
    findButton(R.id.male).onClick{ text.setText(currentCulture.maleName()); lastGenderWasMale = true }
    findButton(R.id.female).onClick{ text.setText(currentCulture.femaleName()); lastGenderWasMale = false }
    shakeListener = createShakeListener( text.setText(createName()) )
  }

}
