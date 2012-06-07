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
//
//  "Celtic culture" should {
//    "have male names" in {Celtic.maleName(rnd) must beEqualTo("Aiden")}
//    "have female names" in {Celtic.femaleName(rnd) must beEqualTo("Brigit")}
//  }
//
//  "Elizabethan culture" should {
//    "have male names" in { Elizabethan.maleName(rnd) must beEqualTo("Peter Wotton")}
//    "have female names" in { Elizabethan.femaleName(rnd) must beEqualTo("Joan Wotton") }
//  }

  "Roman culture" should {
    "have male names" in { Roman.maleName(rnd) must beEqualTo("Gaius Pontius Praeconinus (herald)")}
    "have female names ending in 'ia'" in { Roman.femaleName(rnd) must beEqualTo("Antonia")}
    "have female names with an optional cognomen" in { Roman.femaleName(new Random(101)) must beEqualTo("Ambrosia Piso (mortar)")}
  }
//
//  "Egyptian culture" should {
//    "have male names" in { Egyptian.maleName(rnd) must beEqualTo("Menetnasht�")}
//    "have female names" in { Egyptian.femaleName(rnd) must beEqualTo("DjaDja") }
//    "have god names" in { Egyptian.createName("god", rnd) must beEqualTo("Amun") }
//    "have goddess names" in { Egyptian.createName("goddess", rnd) must beEqualTo("Neith") }
//  }
//
//  "Angels" should {
//    "have male names" in { Angels.maleName(rnd) must beEqualTo("Yurkemo, Revealer of the Rock")}
//    "have female names" in { Angels.femaleName(rnd) must beEqualTo("Ra'uel, Revealer of the Rock") }
//  }
//
//  "Bible people" should {
//    "have male names" in { Biblical.maleName(rnd) must beEqualTo("Michael")}
//    "have female names" in { Biblical.femaleName(rnd) must beEqualTo("Rebecca") }
//  }
//
//  "Russian culture" should {
//    "have male names" in { Russian.maleName(rnd) must beEqualTo("Boris Piotrovich Vorapaev")}
//    "have female names" in { Russian.femaleName(rnd) must beEqualTo("Evgeniya Piotrovna Vorapaev") }
//  }

}
