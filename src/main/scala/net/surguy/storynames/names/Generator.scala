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
      val items = group.tail.filter( _.trim.size > 0 ).map( _.replaceFirst("^\\d+(\\.)?","").replaceAll("\\s"," ").trim  )
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
    def then(suffix: String):List[Ruleset.Rule] = List(take(first), take(suffix))
  }
  implicit def wrapString(first: Ruleset.Rule) = new {
    def then(suffix: String):List[Ruleset.Rule] = List(first, take(suffix))
    def then(suffix: Ruleset.Rule):List[Ruleset.Rule] = List(first, suffix)
  }
  implicit def wrapStringList(first: List[Ruleset.Rule]) = new {
    def then(suffix: String):List[Ruleset.Rule] = first ++ List(take(suffix))
    def then(suffix: Ruleset.Rule):List[Ruleset.Rule] = first ++ List(suffix)
  }

  val Victorian = generator.readCulture("/baker_street.txt",
    Map("male" -> Ruleset("MALE" then "SURNAME"),
      "female" -> Ruleset("FEMALE" then "SURNAME")  ))

  val Celtic = generator.readCulture("/celtic.txt",
    Map("male" -> Ruleset(List(take("MALE"))), "female" -> Ruleset(List(take("FEMALE")))  ))

  val Elizabethan = generator.readCulture("/elizabethan.txt",
    Map("male" -> Ruleset("MALE" then "SURNAME"), "female" -> Ruleset("FEMALE" then "SURNAME") ))

  val Roman = generator.readCulture("/roman.txt",
    Map("male" -> Ruleset("PRAENOMEN LIST" then "NOMEN LIST" then "COGNOMEN LIST"),
      "female" -> Ruleset( feminize(take("NOMEN LIST")) then optional(take("COGNOMEN LIST"))) ))

  val Egyptian = generator.readCulture("/fantasy_egyptian.txt",
    Map("male" -> Ruleset(List(take("ANCIENT MALE"))),
      "female" -> Ruleset(List(take("ANCIENT FEMALE"))),
      "god" -> Ruleset(List(take("GODS"))),
      "goddess" -> Ruleset(List(take("GODDESSES")))
    ))

  val Angels = generator.readCulture("/angels_and_demons.txt",
    Map("male" -> Ruleset(suffix(take("MASCULINE NAMES"),",") then "TITLES OF ANGELS"),
      "female" -> Ruleset(suffix(take("FEMININE NAMES"),",") then "TITLES OF ANGELS")
    ))

  val Russian = generator.readCulture("/russian.txt",
    Map("male" -> Ruleset(removeBrackets(take("MALE")) then patronymic(removeBrackets(take("MALE")),"ovich") then "SURNAMES"),
      "female" -> Ruleset(removeBrackets(take("FEMALE")) then patronymic(removeBrackets(take("MALE")),"ovna")
                    then regex(take("SURNAMES"),"sky$", "skaya"))
    ))

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
}

case class Culture(name: String, private val lists: Iterable[Names], private val rules: Map[String, Ruleset]) {
  private val rnd = new Random()

  def maleName(randomizer:Random = rnd) = createName("male", randomizer)
  def femaleName(randomizer:Random = rnd) = createName("female", randomizer)
  def createName(nameType: String, randomizer:Random = rnd) =
    rules(nameType).actions.map( fn => fn.apply(this, randomizer) ).mkString(" ").trim

  def nameComponent(listName: String, randomizer: Random) = lists.find( _.name.trim == listName ).get.randomName(randomizer)
}

case class Names(name: String, items: Seq[String]) {
  def randomName(random: Random) = items( random.nextInt(items.length) ).trim
}

case class Ruleset(actions: List[Ruleset.Rule])

object Ruleset {
  type Rule = (Culture, Random) => String
}
