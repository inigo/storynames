package net.surguy.android.shake

import android.hardware.{SensorEvent, SensorEventListener, Sensor, SensorManager}

/**
 * Listener that detects shake gesture.
 *
 * @author Based on StackOverflow answer by peceps - http://stackoverflow.com/a/5117254/544689
 * @author Inigo Surguy
 */
class ShakeEventListener(val shakeAction: () => Unit) extends SensorEventListener {

  /**Minimum movement force to consider. */
  private val MIN_FORCE = 10

  /**Minimum times in a shake gesture that the direction of movement needs to change. */
  private val MIN_DIRECTION_CHANGE = 3

  /**Maximum pause between movements. */
  private val MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 200

  /**Maximum allowed time for shake gesture. */
  private val MAX_TOTAL_DURATION_OF_SHAKE = 400

  /**Time when the gesture started. */
  private var mFirstDirectionChangeTime = 0L

  /**Time when the last movement started. */
  private var mLastDirectionChangeTime = 0L

  /**How many movements are considered so far. */
  private var mDirectionChangeCount = 0L

  /**The last x position. */
  private var lastX = 0F

  /**The last y position. */
  private var lastY = 0F

  /**The last z position. */
  private var lastZ = 0F

  override def onSensorChanged(se: SensorEvent) {
    // get sensor data
    val x = se.values(SensorManager.DATA_X)
    val y = se.values(SensorManager.DATA_Y)
    val z = se.values(SensorManager.DATA_Z)

    // calculate movement
    val totalMovement = math.abs(x + y + z - lastX - lastY - lastZ)

    if (totalMovement > MIN_FORCE) {
      val now = System.currentTimeMillis()

      // store first movement time
      if (mFirstDirectionChangeTime == 0) {
        mFirstDirectionChangeTime = now
        mLastDirectionChangeTime = now
      }

      // check if the last movement was not long ago
      val lastChangeWasAgo = now - mLastDirectionChangeTime
      if (lastChangeWasAgo < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE) {

        // store movement data
        mLastDirectionChangeTime = now
        mDirectionChangeCount = mDirectionChangeCount + 1

        // store last sensor data
        lastX = x
        lastY = y
        lastZ = z

        // check how many movements are so far
        if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

          // check total duration
          val totalDuration = now - mFirstDirectionChangeTime
          if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
            shakeAction()
            resetShakeParameters()
          }
        }

      } else {
        resetShakeParameters()
      }
    }
  }

  /**
   * Resets the shake parameters to their default values.
   */
  private def resetShakeParameters() {
    mFirstDirectionChangeTime = 0
    mDirectionChangeCount = 0
    mLastDirectionChangeTime = 0
    lastX = 0
    lastY = 0
    lastZ = 0
  }

  override def onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}
