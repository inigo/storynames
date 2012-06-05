package net.surguy.storynames.names

import util.Random
import scalax.io.Resource
import Utils.Implicits._

/**
 * Generate plausible names based on the lists from the Story Games Names Project.
 *
 * @author Inigo Surguy
 */
class Generator {
  def readCulture(filename: String, rules: Map[String, List[String]]): Culture = {
    val lines = Resource.fromInputStream(this.getClass.getResourceAsStream(filename)).lines()
    val names = for (group <- lines.groupStartingWith( allUppercase )) yield {
      val name = group(0)
      val items = group.tail.filter( _.trim.size > 0 ).map( _.replaceFirst("^\\d+(\\.)?","").trim  )
      Names(name, items)
    }

    val cultureName = names(0).name
    Culture(cultureName, names.toList, rules)
  }

  private def allUppercase(s:String) = ! s.exists( _.isLower )
}

object Cultures {
  private val generator = new Generator()

  val bakerStreet = generator.readCulture("/baker_street.txt",
    Map("male" -> ("MALE" then "SURNAME"), "female" -> ("FEMALE" then "SURNAME")  ))

  val celtic = generator.readCulture("/celtic.txt",
    Map("male" -> List("MALE"), "female" -> List("FEMALE")  ))

  val elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> ("MALE" then "SURNAME"), "female" -> ("FEMALE" then "SURNAME") ))


  implicit def wrapString(first: String) = new {
    def then(suffix: String):List[String] = List(first, suffix)
  }
  implicit def wrapStringList(first: List[String]) = new {
    def then(suffix: String):List[String] = first ++ List(suffix)
  }

}

case class Culture(name: String, private val lists: List[Names], private val rules: Map[String, List[String]]) {
  def maleName() = rules("male").map( s => lists.find( _.name==s).get.randomName ).mkString(" ")
  def femaleName() = rules("female").map( s => lists.find( _.name==s).get.randomName ).mkString(" ")

  def createName(nameType: String) = rules(nameType).map( listName => lists.find( _.name==listName ).get.randomName ).mkString(" ")
}

case class Names(name: String, items: List[String]) {
  private val random: Random = new Random()

  def randomName = items( random.nextInt(items.length) )
}

case class Gender(name: String)
