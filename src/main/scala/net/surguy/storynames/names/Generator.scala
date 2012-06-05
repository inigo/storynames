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
  def readCulture(filename: String, rules: Map[String, Ruleset]): Culture = {
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
    Map("male" -> Ruleset("MALE" then "SURNAME"),
      "female" -> Ruleset("FEMALE" then "SURNAME")  ))

  val Celtic = generator.readCulture("/celtic.txt",
    Map("male" -> Ruleset(List("MALE")), "female" -> Ruleset(List("FEMALE"))  ))

  val Elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> Ruleset("MALE" then "SURNAME"), "female" -> Ruleset("FEMALE" then "SURNAME") ))

  val Roman = generator.readCulture("/roman.txt",
    Map("male" -> Ruleset("PRAENOMEN LIST" then "NOMEN LIST" then "COGNOMEN LIST"),
      "female" -> Ruleset("NOMEN LIST" then "COGNOMEN LIST") ))

}

case class Culture(name: String, private val lists: Iterable[Names], private val rules: Map[String, Ruleset]) {
  private val rnd = new Random()

  def maleName(randomizer:Random = rnd) = createName("male", randomizer)
  def femaleName(randomizer:Random = rnd) = createName("female", randomizer)

  def createName(nameType: String, randomizer:Random = rnd) =
    rules(nameType).actions.map( listName => lists.find( _.name==listName ).get.randomName(randomizer) ).mkString(" ")
}

case class Names(name: String, items: Seq[String]) {
  def randomName(random: Random) = items( random.nextInt(items.length) )
}

case class Ruleset(actions: List[String])
