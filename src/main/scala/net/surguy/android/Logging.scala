package net.surguy.android

import android.util.Log

/**
 * Adds log messages using the Android logging system.
 *
 * @author Inigo Surguy
 */
trait Logging {

  def debug(msg: String) = Log.d(this.getClass.getSimpleName, msg)

}