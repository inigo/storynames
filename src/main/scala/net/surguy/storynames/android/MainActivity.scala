package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import android.view.View
import android.widget.{Spinner, Button, TextView}

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 */
class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val text = findViewById(R.id.text).asInstanceOf[TextView]
    text.setText( Cultures.Roman.maleName() )

    val spinner = findViewById(R.id.spinner).asInstanceOf[Spinner]

    val maleButton = findViewById(R.id.male).asInstanceOf[Button]
    val femaleButton = findViewById(R.id.female).asInstanceOf[Button]
    maleButton.setOnClickListener( new ClickListener( () => text.setText( Cultures.getCulture( spinner.getSelectedItem.toString ).maleName() ) ) )
    femaleButton.setOnClickListener( new ClickListener( () => text.setText( Cultures.getCulture( spinner.getSelectedItem.toString ).femaleName() ) ) )


  }

  class ClickListener(val fn: () => Unit ) extends View.OnClickListener {
    def onClick(v: View) { fn() }
  }

}