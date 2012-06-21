package net.surguy.storynames.names

import util.Random
import scalax.io.Resource

/**
 * Generate plausible names based on the lists from the Story Games Names Project.
 *
 * @author Inigo Surguy
 */
class Generator {
  def readCulture(filename: String, rules: Map[String, Ruleset]): Culture = {
    val lines = Resource.fromInputStream(this.getClass.getResourceAsStream(filename)).lines()
    val names: Seq[Names] = for (group <- Utils.groupStartingWith(lines, allUppercase )) yield {
      val name = group(0)
      val items = group.tail.filter( _.trim.size > 0 ).map( _.replaceFirst("^\\d+(\\.)?","").replaceAll("\\s"," ").trim  )
      Names(name, items)
    }

    val cultureName = names(0).name
    val mergedNames = names.groupBy( _.name.replaceAll("\\s+\\d+\\s*$","") )
                                           .map{ case (k, v: Seq[Names]) => Names(k, v.map( _.items ).flatten )  }
    Culture(cultureName, mergedNames, rules)
  }

  private def allUppercase(s:String) = ! s.exists( _.isLower )
}

case class Culture(name: String, private val lists: Iterable[Names], private val rules: Map[String, Ruleset]) {
  private val rnd = new Random()

  def maleName(randomizer:Random = rnd) = createName("male", randomizer)
  def femaleName(randomizer:Random = rnd) = createName("female", randomizer)
  def createName(nameType: String, randomizer:Random = rnd) =
    rules(nameType).actions.map( fn => fn.apply(this, randomizer) ).mkString(" ").trim

  def nameComponent(listName: String, randomizer: Random) = lists.find( _.name.trim == listName ).
    getOrElse( sys.error("No such list name "+listName+" in "+lists.map( _.name.trim )) ).randomName(randomizer)
}

case class Names(name: String, items: Seq[String]) {
  def randomName(random: Random) = items( random.nextInt(items.length) ).trim
}

case class Ruleset(actions: List[Ruleset.Rule])

object Ruleset {
  type Rule = (Culture, Random) => String

  def take(listName: String) = (c: Culture, random: Random) => c.nameComponent(listName, random)
  def feminize(rule: Ruleset.Rule) = (c: Culture, random: Random) => rule(c, random).replaceAll("ius$","ia")
  def optional(rule: Ruleset.Rule, percent: Float = 0.5F) =
    (c: Culture, random: Random) => if (random.nextFloat() < percent) rule(c, random) else ""
  def suffix(rule: Ruleset.Rule, suffix: String) =
    (c: Culture, random: Random) => rule(c, random)+suffix
  def removeBrackets(rule: Ruleset.Rule) = regex(rule, "\\s*\\(.*\\)")
  def regex(rule: Ruleset.Rule, regex: String, replacement: String = "") =
    (c: Culture, random: Random) => rule(c, random).replaceAll(regex, replacement)
  def patronymic(rule: Ruleset.Rule, text: String) = (c: Culture, random: Random) => rule(c, random).trim+text
  def oneOf(rules: Ruleset.Rule*) = (c: Culture, random: Random) => rules(random.nextInt(rules.length)).apply(c, random)
}

object Cultures {
  import Ruleset._

  private val generator = new Generator()

  val Victorian = generator.readCulture("/baker_street.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAME"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAME")))  ))

  val Celtic = generator.readCulture("/celtic.txt",
    Map("male" -> Ruleset(List(take("MALE"))),
      "female" -> Ruleset(List(take("FEMALE"))) ) )

  val Elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> Ruleset(List(take("MALE"),take("SURNAME"))),
      "female" -> Ruleset(List(take("FEMALE"),take("SURNAME") )) ))

  val Roman = generator.readCulture("/roman.txt",
    Map("male" -> Ruleset(List(take("PRAENOMEN LIST"),take("NOMEN LIST"),take("COGNOMEN LIST"))),
      "female" -> Ruleset(List(feminize(take("NOMEN LIST")),optional(take("COGNOMEN LIST")) )) ) )

  val Egyptian = generator.readCulture("/fantasy_egyptian.txt",
    Map("male" -> Ruleset(List(take("ANCIENT MALE"))),
      "female" -> Ruleset(List(take("ANCIENT FEMALE"))),
      "god" -> Ruleset(List(take("GODS"))),
      "goddess" -> Ruleset(List(take("GODDESSES")))
    ))

  val Angels = generator.readCulture("/angels_and_demons.txt",
    Map("male" -> Ruleset(List(suffix(take("MASCULINE NAMES"),","), take("TITLES OF ANGELS"))),
      "female" -> Ruleset(List(suffix(take("FEMININE NAMES"),","), take("TITLES OF ANGELS")))
    ))

  val Russian = generator.readCulture("/russian.txt",
    Map("male" -> Ruleset(List(removeBrackets(take("MALE")),patronymic(removeBrackets(take("MALE")),"ovich"), take("SURNAMES"))),
      "female" -> Ruleset(List(removeBrackets(take("FEMALE")),patronymic(removeBrackets(take("MALE")),"ovna"), regex(take("SURNAMES"),"sky$", "skaya")))
    ))

  val Biblical = generator.readCulture("/biblical.txt",
    Map("male" -> Ruleset(List(oneOf(take("COMMON MALE"), take("LESS COMMON MALE"), take("UNUSUAL MALE"))) ),
      "female" -> Ruleset(List(oneOf(take("COMMON FEMALE"), take("LESS COMMON FEMALE"), take("UNUSUAL FEMALE"))) )
    ))

  val Chinese = generator.readCulture("/chinese.txt",
    Map("male" -> Ruleset(List(oneOf(take("COMMON SURNAMES"), take("COMPOUND SURNAMES")), take("MALE MANDARIN GIVEN NAMES"))),
      "female" -> Ruleset(List(oneOf(take("COMMON SURNAMES"), take("COMPOUND SURNAMES")), take("FEMALE MANDARIN GIVEN NAMES")))
    ))

  val Finnish = generator.readCulture("/finnish.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"), take("LAST NAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES"), take("LAST NAMES")))
    ))

  val Quechua = generator.readCulture("/quechua.txt",
    Map("male" -> Ruleset(List(take("MALE"))),
      "female" -> Ruleset(List(take("FEMALE")))
    ))

  def getCulture(s: String): Culture = {
    s match {
      case "Egyptian" => Egyptian
      case "Victorian" => Victorian
      case "Roman" => Roman
      case "Angels" => Angels
      case "Russian" => Russian
      case "Biblical" => Biblical
      case "Elizabethan" => Elizabethan
      case "Celtic" => Celtic
      case "Chinese" => Chinese
      case "Finnish" => Finnish
      case "Quechua" => Quechua
      case _ => throw new IllegalArgumentException("No such culture")
    }
  }


}
