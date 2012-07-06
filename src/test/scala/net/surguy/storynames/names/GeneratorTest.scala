package net.surguy.storynames.names

import org.specs2.mutable.SpecificationWithJUnit
import Cultures._
import util.Random

/**
 * @author Inigo Surguy
 */
class GeneratorTest extends SpecificationWithJUnit {
  def rnd = new Random(0)

  "Using a randomizer" should {
    "return the same answer for the same randomizer" in {Victorian.maleName(rnd) must beEqualTo(Victorian.maleName(rnd))}
    "return different answers using the default" in {Roman.maleName() must not(beEqualTo(Roman.maleName()))}
  }

  "Baker street" should {
    "have male names" in {Victorian.maleName(rnd) must beEqualTo("George Roylott")}
    "have female names" in {Victorian.femaleName(rnd) must beEqualTo("Patience Roylott")}
  }

  "Celtic culture" should {
    "have male names" in {Celtic.maleName(rnd) must beEqualTo("Aiden")}
    "have female names" in {Celtic.femaleName(rnd) must beEqualTo("Brigit")}
  }

  "Elizabethan culture" should {
    "have male names" in { Elizabethan.maleName(rnd) must beEqualTo("Peter Wotton")}
    "have female names" in { Elizabethan.femaleName(rnd) must beEqualTo("Joan Wotton") }
  }

  "Roman culture" should {
    "have male names" in { Roman.maleName(rnd) must beEqualTo("Gaius Pontius Praeconinus (herald)")}
    "have female names ending in 'ia'" in { Roman.femaleName(rnd) must beEqualTo("Antonia")}
    "have female names with an optional cognomen" in { Roman.femaleName(new Random(101)) must beEqualTo("Ambrosia Piso (mortar)")}
    "have female names with a feminine cognomen" in { Roman.femaleName(new Random(103)) must beEqualTo("Spuria Bibula (drunkard)")}
  }

  "Egyptian culture" should {
    "have male names" in { Egyptian.maleName(rnd) must beEqualTo("Menetnashté")}
    "have female names" in { Egyptian.femaleName(rnd) must beEqualTo("DjaDja") }
    "have god names" in { Egyptian.createName("god", rnd) must beEqualTo("Amun") }
    "have goddess names" in { Egyptian.createName("goddess", rnd) must beEqualTo("Neith") }
  }

  "Angels" should {
    "have male names" in { Angels.maleName(rnd) must beEqualTo("Yurkemo, Revealer of the Rock")}
    "have female names" in { Angels.femaleName(rnd) must beEqualTo("Ra'uel, Revealer of the Rock") }
  }

  "Bible people" should {
    "have male names" in { Biblical.maleName(rnd) must beEqualTo("Michael")}
    "have female names" in { Biblical.femaleName(rnd) must beEqualTo("Rebecca") }
  }

  "Russian culture" should {
    "have male names" in { Russian.maleName(rnd) must beEqualTo("Boris Piotrovich Vorapaev")}
    "have female names" in { Russian.femaleName(rnd) must beEqualTo("Evgeniya Piotrovna Vorapaev") }
  }

  "Chinese culture" should {
    "have male names" in { Chinese.maleName(rnd) must beEqualTo("Murong / Mo Yung Zhili")}
    "have female names" in { Chinese.femaleName(rnd) must beEqualTo("Murong / Mo Yung Jiaohua") }
    "have more male names" in { (for (i <- 1 to 100) yield Chinese.maleName()) must not(beNull)}
  }

  "Finnish culture" should {
    "have male names" in { Finnish.maleName(rnd) must beEqualTo("Aarre Kangas")}
    "have female names" in { Finnish.femaleName(rnd) must beEqualTo("Aili Kangas") }
  }

  "Quna culture" should {
    "have male names" in { Quechua.maleName(rnd) must beEqualTo("Ñuqa")}
    "have female names" in { Quechua.femaleName(rnd) must beEqualTo("Aysay") }
  }

  "English culture" should {
    "have male names" in { English.maleName(rnd) must beEqualTo("Alexander Thomas")}
    "have female names" in { English.femaleName(rnd) must beEqualTo("Alice Thomas") }
  }

  "Scottish culture" should {
    "have male names" in { Scottish.maleName(rnd) must beEqualTo("John Robertson")}
    "have female names" in { Scottish.femaleName(rnd) must beEqualTo("Mary Robertson") }
  }

  "Welsh culture" should {
    "have male names" in { Welsh.maleName(rnd) must beEqualTo("Andrew Harris")}
    "have female names" in { Welsh.femaleName(rnd) must beEqualTo("Charlotte Harris") }
  }

  "Thai culture" should {
    "have male names" in { Thai.maleName(rnd) must beEqualTo("Anand Wongsa")}
    "have female names" in { Thai.femaleName(rnd) must beEqualTo("Rasamee Wongsa") }
  }

  "Sicilian culture" should {
    "have male names" in { Sicilian.maleName(rnd) must beEqualTo("Salvatore (Totò) Lucido")}
    "have female names" in { Sicilian.femaleName(rnd) must beEqualTo("Liliana Lucido") }
  }

  "Senegalese culture" should {
    "have male names" in { Senegalese.maleName(rnd) must beEqualTo("Thiemo Ndar")}
    "have female names" in { Senegalese.femaleName(rnd) must beEqualTo("Aissatou Ndar") }
  }

  "Portuguese culture" should {
    "have male names" in { Portuguese.maleName(rnd) must beEqualTo("Gil Cruz")}
    "have female names" in { Portuguese.femaleName(rnd) must beEqualTo("Apolónia Cruz") }
  }

  "Danish culture" should {
    "have male names" in { Danish.maleName(rnd) must beEqualTo("Magnus Rasmussen")}
    "have female names" in { Danish.femaleName(rnd) must beEqualTo("Camilla Rasmussen") }
  }

  "Mongolian culture" should {
    "have male names" in { Mongolian.maleName(rnd) must beEqualTo("Arban Dashyondon")}
    "have female names" in { Mongolian.femaleName(rnd) must beEqualTo("Arikaboke Dashyondon") }
  }

  "Florentine culture" should {
    "have male names" in { Florentine.maleName(rnd) must beEqualTo("Ugo Ruccelai")}
    "have female names" in { Florentine.femaleName(rnd) must beEqualTo("Savia Ruccelai") }
  }

  "Italian culture" should {
    "have male names" in { Italian.maleName(rnd) must beEqualTo("Vito Vasile")}
    "have female names" in { Italian.femaleName(rnd) must beEqualTo("Caterina Vasile") }
  }

  "French culture" should {
    "have male names" in { French.maleName(rnd) must beEqualTo("Eteinne Menard")}
    "have female names" in { French.femaleName(rnd) must beEqualTo("Manon Menard") }
  }

  "Modern Greek culture" should {
    "have male names" in { Modern_Greek.maleName(rnd) must beEqualTo("Iosif Ioannidis")}
    "have female names" in { Modern_Greek.femaleName(rnd) must beEqualTo("Eleni Ioannidis") }
  }

  "German culture" should {
    "have male names" in { German.maleName(rnd) must beEqualTo("Freidrich Wagner")}
    "have female names" in { German.femaleName(rnd) must beEqualTo("Gunn Wagner") }
  }

  "Brazilian culture" should {
    "have male names" in { Brazilian.maleName(rnd) must beEqualTo("Rui do Nascimento")}
    "have female names" in { Brazilian.femaleName(rnd) must beEqualTo("Andreia do Nascimento") }
  }

  "Irish culture" should {
    "have male names" in { Modern_Irish.maleName(rnd) must beEqualTo("Brendan O'Connor")}
    "have female names" in { Modern_Irish.femaleName(rnd) must beEqualTo("Fiona O'Connor") }
  }

  "Irish folk culture" should {
    "have male names" in { Irish_Folk.maleName(rnd) must beEqualTo("Bóinn (boe-in)")}
    "have female names" in { Irish_Folk.femaleName(rnd) must beEqualTo("Aoibheall (ee-vul)") }
  }

  "Czech culture" should {
    "have male names" in { Czech.maleName(rnd) must beEqualTo("Jan Nemec")}
    "have female names" in { Czech.femaleName(rnd) must beEqualTo("Tereza Nemec") }
  }

  "Spanish culture" should {
    "have male names" in { Spanish.maleName(rnd) must beEqualTo("Abelino del Campo Robledo")}
    "have female names" in { Spanish.femaleName(rnd) must beEqualTo("Lourdes del Campo Robledo") }
    "have various more names" in {
      for (i <- (1 to 100)) {
        println(Spanish.maleName())
        println(Spanish.femaleName())
      }
      "fish" must beEqualTo("fish")
    }
  }




}
