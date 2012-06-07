package net.surguy.storynames.android

import android.app.Activity
import android.os.Bundle
import net.surguy.storynames.names.Cultures
import android.widget.{Button, TextView}
import android.view.View

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

    val maleButton = findViewById(R.id.male).asInstanceOf[Button]
    val femaleButton = findViewById(R.id.female).asInstanceOf[Button]

    maleButton.setOnClickListener( new ClickListener( () => text.setText( Cultures.Roman.maleName() ) ) )
    femaleButton.setOnClickListener( new ClickListener( () => text.setText( Cultures.Roman.femaleName() ) ) )

  }

  class ClickListener(val fn: () => Unit ) extends View.OnClickListener {
    def onClick(v: View) { fn() }
  }

}