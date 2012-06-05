package net.surguy.storynames.names

import org.specs2.mutable.{SpecificationWithJUnit, Specification}
import Cultures._

/**
 * @author Inigo Surguy
 */
class GeneratorTest extends SpecificationWithJUnit {

  "Baker street" should {
    "have male names" in {
      println(Victorian.maleName())
      Victorian.maleName() must not beNull
    }
    "have female names" in {
      println(Victorian.femaleName())
      Victorian.femaleName() must not beNull
    }
  }

 "Celtic culture" should {
    "have male names" in {
      println(Celtic.maleName())
      Celtic.maleName() must not beNull
    }
    "have female names" in {
      println(Celtic.femaleName())
      Celtic.femaleName() must not beNull
    }
  }

 "Elizabethan culture" should {
    "have male names" in {
      println(Elizabethan.maleName())
      Elizabethan.maleName() must not beNull
    }
    "have female names" in {
      println(Elizabethan.femaleName())
      Elizabethan.femaleName() must not beNull
    }
  }

  "Roman culture" should {
    "have male names" in {
      println(Roman.maleName())
      Roman.maleName() must not beNull
    }
    "have female names" in {
      println(Roman.femaleName())
      Roman.femaleName() must not beNull
    }
  }

}
