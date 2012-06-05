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
  private implicit val codec = scalax.io.Codec.UTF8

  def readCulture(filename: String, rules: Map[String, List[String]]): Culture = {
    val allUppercase = (s:String) => ! s.exists( _.isLower )

    val lines = Resource.fromInputStream(this.getClass.getResourceAsStream(filename)).lines()
    val names = for (group <- lines.groupStartingWith( allUppercase )) yield {
      val name = group(0)
      val items = group.tail.filter( _.trim.size > 0 ).map( _.replaceFirst("^\\d+(\\.)?","").trim  )
      Names(name, items)
    }

    val tidiedFilename = filename.substring(0, filename.lastIndexOf(".")).replaceAll("_"," ")
    Culture(tidiedFilename, names.toList, rules)
  }

}

object Cultures {
  private val generator = new Generator()

  val bakerStreet = generator.readCulture("/baker_street.txt",
    Map("male" -> List("MALE","SURNAME"), "female" -> List("FEMALE", "SURNAME")  ))

  val celtic = generator.readCulture("/celtic.txt",
    Map("male" -> List("MALE"), "female" -> List("FEMALE")  ))

  val elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> List("MALE", "SURNAME"), "female" -> List("FEMALE", "SURNAME")  ))

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
