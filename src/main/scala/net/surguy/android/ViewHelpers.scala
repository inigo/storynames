package net.surguy.android

import android.app.Activity
import android.widget.{TextView, Spinner, Button}
import android.view.View
import android.view.View.{OnLongClickListener, OnClickListener}

/**
 * Add Scala wrappers to improve syntax of Android listeners.
 *
 * Usage:
 *  Extend ViewHelpers from your Activity
 *  Add code like:
 *     findButton(R.id.myId).onClick{ /* do something */ }.onLongClick( /* do something else */)
 *
 * @author Inigo Surguy
 */
trait ViewHelpers extends Activity {

  def findButton(id: Int) = findViewById(id).asInstanceOf[Button]
  def findSpinner(id: Int) = findViewById(id).asInstanceOf[Spinner]
  def findTextView(id: Int) = findViewById(id).asInstanceOf[TextView]

  implicit def richView(v: View): RichView = new RichView(v)

  class RichView(v: View) {
    def onClick(fn: => Unit):View = {
      v.setOnClickListener(new OnClickListener {
        def onClick(v: View) { fn }
      })
      v
    }
    def onLongClick(fn: => Boolean):View = {
      v.setOnLongClickListener(new OnLongClickListener {
        def onLongClick(v: View) { fn }
      })
      v
    }
  }

}