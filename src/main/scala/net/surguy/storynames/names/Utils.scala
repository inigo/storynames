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
    implicit def wrapList(traversable: LongTraversable[String]) = new {
      def groupStartingWith(groupingFn: (String) => Boolean) = Utils.groupStartingWith(traversable, groupingFn)
    }
  }

  def groupStartingWith(traversable: LongTraversable[String], groupingFn: (String) => Boolean):Seq[List[String]] = {
    val lists = new ListBuffer[List[String]]()

    var currentBuffer = new ListBuffer[String]()
    for (s <- traversable) {
      if (groupingFn(s)) {
        if (currentBuffer.size > 0) { lists.append(currentBuffer.toList) }
        currentBuffer = new ListBuffer[String]()
      }
      currentBuffer.append(s)
    }
    lists.append(currentBuffer.toList)

    lists
  }


}
