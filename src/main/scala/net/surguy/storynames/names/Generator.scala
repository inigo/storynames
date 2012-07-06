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
  def feminize(rule: Ruleset.Rule) = (c: Culture, random: Random) => rule(c, random).replaceAll("ius\\b","ia").replaceAll("(\\w{2,})us\\b", "$1a")
  def optional(rule: Ruleset.Rule, percent: Float = 0.5F) =
    (c: Culture, random: Random) => if (random.nextFloat() < percent) rule(c, random) else ""
  def suffix(rule: Ruleset.Rule, suffix: String) =
    (c: Culture, random: Random) => rule(c, random)+suffix
  def prefix(prefix: String, rule: Ruleset.Rule) =
    (c: Culture, random: Random) => prefix + rule(c, random)
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
      "female" -> Ruleset(List(feminize(take("NOMEN LIST")),optional(feminize(take("COGNOMEN LIST"))) )) ) )

  val Egyptian = generator.readCulture("/fantasy_egyptian.txt",
    Map("male" -> Ruleset(List(take("ANCIENT MALE"))),
      "female" -> Ruleset(List(take("ANCIENT FEMALE"))),
      "god" -> Ruleset(List(take("GODS"))),
      "goddess" -> Ruleset(List(take("GODDESSES")))
    ))

  val Angels = generator.readCulture("/angels_and_demons.txt",
    Map("male" -> Ruleset(List(suffix(take("MASCULINE NAMES"),","), take("TITLES OF ANGELS"))),
      "female" -> Ruleset(List(suffix(take("FEMININE NAMES"),","), take("TITLES OF FEMININE ANGELS")))
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

  val Scottish = generator.readCulture("/united_kingdom.txt",
    Map("male" -> Ruleset(List(take("SCOTTISH MALE"), take("SCOTTISH SURNAMES"))),
      "female" -> Ruleset(List(take("SCOTTISH FEMALE"), take("SCOTTISH SURNAMES")))
    ))

  val Welsh = generator.readCulture("/united_kingdom.txt",
    Map("male" -> Ruleset(List(take("WELSH MALE"), take("WELSH SURNAME"))),
      "female" -> Ruleset(List(take("WELSH FEMALE"), take("WELSH SURNAME")))
    ))

  val English = generator.readCulture("/united_kingdom.txt",
    Map("male" -> Ruleset(List(take("BRITISH MALE"), take("BRITISH SURNAMES"))),
      "female" -> Ruleset(List(take("BRITISH FEMALE"), take("BRITISH SURNAMES")))
    ))

  val Thai = generator.readCulture("/thai.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Sicilian = generator.readCulture("/sicilian.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Senegalese = generator.readCulture("/senegalese.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Portuguese = generator.readCulture("/portuguese.txt",
    Map("male" -> Ruleset(List(oneOf(take("MALE"), take("OLD MALE")), take("SURNAMES"))),
      "female" -> Ruleset(List(oneOf(take("FEMALE"), take("OLD FEMALE")), take("SURNAMES")))
    ))

  val Danish = generator.readCulture("/danish.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES"), take("SURNAMES")))
    ))

  val Mongolian = generator.readCulture("/mongolian.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES"), take("SURNAMES")))
    ))

  val Italian = generator.readCulture("/italian.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Florentine = generator.readCulture("/italian.txt",
    Map("male" -> Ruleset(List(take("FLORENCE, 1427 MALE"), take("FLORENCE, 1427 SURNAMES"))),
      "female" -> Ruleset(List(take("FLORENCE, 1427 FEMALE"), take("FLORENCE, 1427 SURNAMES")))
    ))

  val French = generator.readCulture("/french.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Modern_Greek = generator.readCulture("/greek.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val German = generator.readCulture("/german.txt",
    Map("male" -> Ruleset(List(oneOf(take("REALISTIC MALE"), take("COLORFUL MALE")), oneOf(take("REALISTIC SURNAMES"), take("COLORFUL SURNAMES")))),
      "female" -> Ruleset(List(oneOf(take("REALISTIC FEMALE"), take("COLORFUL FEMALE")), oneOf(take("REALISTIC SURNAMES"), take("COLORFUL SURNAMES"))))
    ))

  val Brazilian = generator.readCulture("/brazilian.txt",
    Map("male" -> Ruleset(List(take("MALE"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE"), take("SURNAMES")))
    ))

  val Modern_Irish = generator.readCulture("/irish.txt",
    Map("male" -> Ruleset(List(take("IRISH MALE"), take("IRISH SURNAMES"))),
      "female" -> Ruleset(List(take("IRISH FEMALE"), take("IRISH SURNAMES")))
    ))

  val Irish_Folk = generator.readCulture("/irish.txt",
    Map("male" -> Ruleset(List(take("MALE FOLK"))),
      "female" -> Ruleset(List(take("FEMALE FOLK")))
    ))

  val Czech = generator.readCulture("/czech.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"), take("SURNAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES"), take("SURNAMES")))
    ))

  val Spanish = generator.readCulture("/spanish.txt",
    Map("male" -> Ruleset(List(regex(take("MALE"),"\\s*\\*",""), take("SURNAMES"), take("SURNAMES"))),
      "female" -> Ruleset(List(regex(take("FEMALE"),"\\s*\\*",""), take("SURNAMES"), take("SURNAMES")))
    ))

  val Two_Fisted_Space_Opera = generator.readCulture("/space_opera.txt",
    Map("male" -> Ruleset(List(regex(take("MALE"),"\\s*\\*",""), take("SURNAMES"))),
      "female" -> Ruleset(List(regex(take("FEMALE"),"\\s*\\*",""), take("SURNAMES")))
    ))

  val Arthurian = generator.readCulture("/arthurian.txt",
    Map("male" -> Ruleset(List(take("MALE"))),
      "female" -> Ruleset(List(take("FEMALE")))
    ))

  val Medieval_French = generator.readCulture("/medieval_french.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES")))
    ))

  val Assyrian = generator.readCulture("/assyrian.txt",
    Map("male" -> Ruleset(List(take("MALE NAMES"))),
      "female" -> Ruleset(List(take("FEMALE NAMES")))
    ))

  val Persian = generator.readCulture("/persian.txt",
    Map("male" -> Ruleset(List(take("MALE"))),
      "female" -> Ruleset(List(take("FEMALE")))
    ))

  val Thousand_and_one_nights = generator.readCulture("/1001_nights.txt",
    Map("male" -> Ruleset(List(take("MALE"), oneOf(prefix("ibn ", take("MALE")), prefix("al-", take("ADJECTIVES")))  )),
      "female" -> Ruleset(List(take("FEMALE"), optional(prefix("bint ", take("MALE"))) ))
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
      case "Scottish" => Scottish
      case "Welsh" => Welsh
      case "English" => English
      case "Thai" => Thai
      case "Sicilian" => Sicilian
      case "Senegalese" => Senegalese
      case "Portuguese" => Portuguese
      case "Danish" => Danish
      case "Mongolian" => Mongolian
      case "Italian" => Italian
      case "Florentine" => Florentine
      case "French" => French
      case "Modern Greek" => Modern_Greek
      case "German" => German
      case "Brazilian" => Brazilian
      case "Modern Irish" => Modern_Irish
      case "Irish Folk" => Irish_Folk
      case "Czech" => Czech
      case "Spanish" => Spanish
      case "Two fisted Space Opera" => Two_Fisted_Space_Opera
      case "Arthurian" => Arthurian
      case "Medieval_French" => Medieval_French
      case "Assyrian" => Assyrian
      case "Persian" => Persian
      case "1001 Nights" => Thousand_and_one_nights
      case _ => throw new IllegalArgumentException("No such culture")
    }
  }


}
