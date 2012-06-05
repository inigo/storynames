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
    "have female names" in { Roman.femaleName(rnd) must beEqualTo("Antonius Varro (blockhead)")}
  }

}
