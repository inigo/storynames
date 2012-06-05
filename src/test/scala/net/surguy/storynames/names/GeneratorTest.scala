package net.surguy.storynames.names

import org.specs2.mutable.{SpecificationWithJUnit, Specification}

/**
 * @author Inigo Surguy
 */
class GeneratorTest extends SpecificationWithJUnit {

  "Baker street" should {
    "have male names" in {
      println(Cultures.bakerStreet.maleName())
      Cultures.bakerStreet.maleName() must not beNull
    }
    "have female names" in {
      println(Cultures.bakerStreet.femaleName())
      Cultures.bakerStreet.femaleName() must not beNull
    }
  }

 "Celtic culture" should {
    "have male names" in {
      println(Cultures.celtic.maleName())
      Cultures.celtic.maleName() must not beNull
    }
    "have female names" in {
      println(Cultures.celtic.femaleName())
      Cultures.celtic.femaleName() must not beNull
    }
  }

 "Elizabethan culture" should {
    "have male names" in {
      println(Cultures.elizabethan.maleName())
      Cultures.elizabethan.maleName() must not beNull
    }
    "have female names" in {
      println(Cultures.elizabethan.femaleName())
      Cultures.elizabethan.femaleName() must not beNull
    }
  }

}
