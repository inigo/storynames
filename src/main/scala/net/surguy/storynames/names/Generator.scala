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
    val names: Seq[Names] = for (group <- lines.groupStartingWith( allUppercase )) yield {
      val name = group(0)
      val items = group.tail.filter( _.trim.size > 0 ).map( _.replaceFirst("^\\d+(\\.)?","").trim  )
      Names(name, items)
    }

    val cultureName = names(0).name
    val mergedNames = names.groupBy( _.name.replaceAll("\\s+\\d+$","") )
                                           .map{ case (k, v: Seq[Names]) => Names(k, v.map( _.items ).flatten )  }
    Culture(cultureName, mergedNames, rules)
  }

  private def allUppercase(s:String) = ! s.exists( _.isLower )
}

object Cultures {
  private val generator = new Generator()

  implicit def wrapString(first: String) = new {
    def then(suffix: String):List[String] = List(first, suffix)
  }
  implicit def wrapStringList(first: List[String]) = new {
    def then(suffix: String):List[String] = first ++ List(suffix)
  }

  val Victorian = generator.readCulture("/baker_street.txt",
    Map("male" -> ("MALE" then "SURNAME"), "female" -> ("FEMALE" then "SURNAME")  ))

  val Celtic = generator.readCulture("/celtic.txt",
    Map("male" -> List("MALE"), "female" -> List("FEMALE")  ))

  val Elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> ("MALE" then "SURNAME"), "female" -> ("FEMALE" then "SURNAME") ))

  val Roman = generator.readCulture("/roman.txt",
    Map("male" -> ("PRAENOMEN LIST" then "NOMEN LIST" then "COGNOMEN LIST"),
      "female" -> ("NOMEN LIST" then "COGNOMEN LIST") ))

}

case class Culture(name: String, private val lists: Iterable[Names], private val rules: Map[String, Iterable[String]]) {
  def maleName() = rules("male").map( s => lists.find( _.name==s).get.randomName ).mkString(" ")
  def femaleName() = rules("female").map( s => lists.find( _.name==s).get.randomName ).mkString(" ")

  def createName(nameType: String) = rules(nameType).map( listName => lists.find( _.name==listName ).get.randomName ).mkString(" ")
}

case class Names(name: String, items: Seq[String]) {
  private val random: Random = new Random()

  def randomName = items( random.nextInt(items.length) )
}

case class Gender(name: String)
