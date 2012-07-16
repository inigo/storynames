package net.surguy.storynames.android

import net.surguy.android.{Logging, RichViews}
import android.app.Activity
import android.os.Bundle
import android.content.{Context, SharedPreferences}
import android.widget.CheckBox

/**
 * Display the options page
 *
 * @author Inigo Surguy
 */
class OptionsActivity extends Activity with RichViews with Logging {

  val PREFERENCES = "StoryNamesPreferences"

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.options)

    val settings = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    val showMoreBox = findView[CheckBox](R.id.showMore)
    showMoreBox.setChecked(settings.getBoolean("showMore", false))
    showMoreBox.onChecked( (b: Boolean) => settings.edit.putBoolean("showMore", b).commit() )

    val showGenderBox = findView[CheckBox](R.id.showGender)
    showGenderBox.setChecked(settings.getBoolean("showGender", false))
    showGenderBox.onChecked( (b: Boolean) => settings.edit.putBoolean("showGender", b).commit() )

    val showMultipleBox = findView[CheckBox](R.id.showMultipleNames)
    showMultipleBox.setChecked(settings.getBoolean("showMultiple", false))
    showMultipleBox.onChecked( (b: Boolean) => settings.edit.putBoolean("showMultiple", b).commit() )
  }
}