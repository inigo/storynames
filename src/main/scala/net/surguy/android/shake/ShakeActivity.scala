package net.surguy.android.shake

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.hardware.{Sensor, SensorManager}

/**
 * An activity that should be notified when the Android device is shaken.
 *
 * Usage:
 *  Extend ShakeActivity in your activity.
 *  To the onCreate method, add:
 *    shakeListener = createShakeListener( /* Do something */ )
 *
 * @author Based on StackOverflow answer by peceps - http://stackoverflow.com/a/5117254/544689
 * @author Inigo Surguy
 */
trait ShakeActivity extends Activity {
  private var sensorManager: SensorManager = null
  var shakeListener: ShakeEventListener = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    sensorManager = getSystemService(Context.SENSOR_SERVICE).asInstanceOf[SensorManager]
  }

  def createShakeListener(fn: => Unit) = new ShakeEventListener(() => fn )

  override def onResume() {
    super.onResume()
    if (shakeListener != null) {
      sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
    }
  }

  override def onPause() {
    if (shakeListener != null) {
      sensorManager.unregisterListener(shakeListener)
    }
    super.onStop()
  }
}
