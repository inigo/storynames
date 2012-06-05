package net.surguy.storynames.names

import scalax.io.LongTraversable
import collection.mutable.ListBuffer

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 */
object Utils {

  object Implicits {
    implicit def wrapList[T](traversable: LongTraversable[T]) = new {
      def groupStartingWith(groupingFn: (T) => Boolean) = Utils.groupStartingWith(traversable, groupingFn)
    }
  }

  def groupStartingWith[T](traversable: LongTraversable[T], groupingFn: (T) => Boolean):Seq[List[T]] = {
    val lists = new ListBuffer[List[T]]()

    var currentBuffer = new ListBuffer[T]()
    for (item <- traversable) {
      if (groupingFn(item)) {
        if (currentBuffer.size > 0) { lists.append(currentBuffer.toList) }
        currentBuffer = new ListBuffer[T]()
      }
      currentBuffer.append(item)
    }
    lists.append(currentBuffer.toList)

    lists.toList
  }


}
