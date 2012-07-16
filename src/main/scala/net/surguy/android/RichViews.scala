package net.surguy.android

import android.app.Activity
import android.widget.{CompoundButton, TextView, Spinner, Button}
import android.view.View
import android.view.View.{OnLongClickListener, OnClickListener}
import android.widget.CompoundButton.OnCheckedChangeListener

/**
 * Add Scala wrappers to improve syntax of Android listeners.
 *
 * Usage:
 *  Extend RichViews from your Activity
 *  Add code like:
 *     findButton(R.id.myId).onClick{ /* do something */ }.onLongClick( /* do something else */)
 *
 * @author Inigo Surguy
 */
trait RichViews extends Activity {

  def findButton(id: Int) = findViewById(id).asInstanceOf[Button]
  def findSpinner(id: Int) = findViewById(id).asInstanceOf[Spinner]
  def findTextView(id: Int) = findViewById(id).asInstanceOf[TextView]

  def findView[WidgetType](id : Int): WidgetType = findViewById(id).asInstanceOf[WidgetType]

  implicit def richView(v: View): RichView = new RichView(v)
  implicit def richCompoundButton(v: CompoundButton): RichCompoundButton = new RichCompoundButton(v)

  class RichCompoundButton(v: CompoundButton) {
    def onChecked(fn: (Boolean) => Unit):CompoundButton = {
      v.setOnCheckedChangeListener(new OnCheckedChangeListener {
        def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
          fn(isChecked)
        }
      })
      v
    }
  }

  class RichView(v: View) {
    def onClick(fn: => Unit):View = {
      v.setOnClickListener(new OnClickListener {
        override def onClick(v: View) { fn }
      })
      v
    }
    def onLongClick(fn: => Boolean):View = {
      v.setOnLongClickListener(new OnLongClickListener {
        override def onLongClick(v: View): Boolean = fn
      })
      v
    }
  }

}