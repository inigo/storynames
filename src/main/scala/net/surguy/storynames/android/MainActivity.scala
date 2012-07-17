package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import net.surguy.android.shake.ShakeActivity
import android.widget.AdapterView.OnItemSelectedListener
import android.view.{MenuItem, Menu, View}
import android.widget._
import android.graphics.{BitmapFactory, Color}
import android.graphics.drawable.BitmapDrawable
import net.surguy.android.{Logging, RichViews}
import android.content.{SharedPreferences, Context, Intent}
import collection.mutable.ListBuffer

/**
 * Main activity for the app - displays names and allows culture selection.
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity with ShakeActivity with RichViews with Logging {

  // The Unicode symbols for male and female
  val maleSymbol = "\u2642"
  val femaleSymbol = "\u2640"

  var lastGenderWasMale = true
  var previousCultureName = ""

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    lastGenderWasMale = math.random >= 0.5

    val settings = getSharedPreferences("StoryNamesPreferences", Context.MODE_PRIVATE)

    val text = findTextView(R.id.text)

    val spinner: Spinner = findSpinner(R.id.spinner)
    val cultureList = ArrayAdapter.createFromResource(this, R.array.culture_array, android.R.layout.simple_spinner_item)
    val fullCultureList = ArrayAdapter.createFromResource(this, R.array.full_culture_array, android.R.layout.simple_spinner_item)
    def setSpinnerAdapter() {
      debug("Changing spinner adapter - new value is "+(if (settings.getBoolean("showMore", false)) "full" else "short"))
      val listToUse = if (settings.getBoolean("showMore", false)) fullCultureList else cultureList
      spinner.setAdapter(listToUse)
    }
    setSpinnerAdapter()
    spinner.setSelection(8) // Default to "Roman"

    settings.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
      def onSharedPreferenceChanged(p1: SharedPreferences, key: String) {
        if (key=="showMore") setSpinnerAdapter()
      }
    })

    def cultureName = spinner.getSelectedItem.toString
    def currentCulture = Cultures.getCulture(cultureName)
    def genderSymbolVisible = settings.getBoolean("showGender", false)
    def multipleNames = settings.getBoolean("showMultiple", false)

    def getTextToDisplay(isMale: Boolean) = {
      val genderSymbol = if (genderSymbolVisible) (if (isMale) maleSymbol+" " else femaleSymbol+" " ) else ""
      val numberOfNames = if (multipleNames) 3 else 1
      def createNewName = if (isMale) currentCulture.maleName() else currentCulture.femaleName()
      val nameItems: ListBuffer[String] = new ListBuffer[String]
      while (nameItems.length < numberOfNames) {
        val newName = genderSymbol + createNewName
        if (! nameItems.contains(newName)) nameItems += newName
      }
      nameItems.mkString("\n\n")
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

        spinner.getChildAt(0).asInstanceOf[TextView].setTextColor(Color.BLACK)

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
          text.setText(getTextToDisplay(lastGenderWasMale))
        }

        previousCultureName = cultureName
      }
      override def onNothingSelected(parent: AdapterView[_]) {}
    })

    findButton(R.id.male).onClick{ text.setText(getTextToDisplay(isMale = true)); lastGenderWasMale = true }
    findButton(R.id.female).onClick{ text.setText(getTextToDisplay(isMale = false)); lastGenderWasMale = false }
    shakeListener = createShakeListener( text.setText(getTextToDisplay(lastGenderWasMale)) )
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
