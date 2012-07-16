package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import net.surguy.android.shake.ShakeActivity
import android.widget.AdapterView.OnItemSelectedListener
import android.view.{MenuItem, Menu, View}
import android.widget.{Toast, LinearLayout, AdapterView}
import android.graphics.{BitmapFactory, Color}
import android.graphics.drawable.BitmapDrawable
import net.surguy.android.{Logging, ImageResizer, RichViews}
import android.content.Intent

/**
 * Main activity for the app - displays names and allows culture selection.
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity with ShakeActivity with RichViews with Logging {

  var lastGenderWasMale = true
  var previousCultureName = ""

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    lastGenderWasMale = math.random >= 0.5

    val text = findTextView(R.id.text)

    val spinner = findSpinner(R.id.spinner)
    spinner.setSelection(8) // Default to "Roman"
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
          case "Chinese" => Color.WHITE
          case "Quechua" => Color.WHITE
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
        // val resizedBitmap = new ImageResizer().resizeImage(bitmap, newWidth, newHeight)
//        val bitmapDrawable = new BitmapDrawable(getResources, resizedBitmap)
        val bitmapDrawable = new BitmapDrawable(getResources, bitmap)
        findView[LinearLayout](R.id.background).setBackgroundDrawable(bitmapDrawable)

        if (previousCultureName!=cultureName) {
          text.setText(createName())
        }

        previousCultureName = cultureName
      }
      override def onNothingSelected(parent: AdapterView[_]) {}
    })
    findButton(R.id.male).onClick{ text.setText(currentCulture.maleName()); lastGenderWasMale = true }
    findButton(R.id.female).onClick{ text.setText(currentCulture.femaleName()); lastGenderWasMale = false }
    shakeListener = createShakeListener( text.setText(createName()) )
  }

  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.menu.menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = {
    // Should be possible to replace this with an onClick reference in the menu.xml to showAbout... but this doesn't seem to work in Scala?
    item.getItemId match {
      case R.id.about => showAbout(item)
      case R.id.options => showOptions(item)
    }
    true
  }

  def showAbout(item: MenuItem) {
    startActivity(new Intent(this, classOf[AboutActivity]))
  }

  def showOptions(item: MenuItem) {
    startActivity(new Intent(this, classOf[OptionsActivity]))
  }

  override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("currentName", findTextView(R.id.text).getText.toString)
    outState.putString("previousCultureName", previousCultureName)
    outState.putBoolean("lastGenderWasMale", lastGenderWasMale)
    debug("Saving name as "+findTextView(R.id.text).getText.toString)
  }

  override def onRestoreInstanceState(savedState: Bundle) {
    super.onRestoreInstanceState(savedState)
    debug("Restoring name "+savedState.getString("currentName"))
    findTextView(R.id.text).setText(savedState.getString("currentName"))
    lastGenderWasMale = savedState.getBoolean("lastGenderWasMale")
    previousCultureName = savedState.getString("previousCultureName")
  }

}
