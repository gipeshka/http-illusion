package com.gipeshka.model.condition

import com.gipeshka.model.condition.ConditionHelper._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}

class GenericConditionSpec
  extends FlatSpec
    with Matchers
{
  "GenericConditionSpec" should "combine conditions" in {
    val conditionTable = Table[Boolean, Boolean, (GenericCondition[Any], GenericCondition[Any]) => GenericCondition[Any], Boolean](
      ("firstPredicate", "secondPredicate", "combinator", "expectedResult"),
      (            true,              true,      _ and _,             true),
      (            true,             false,      _ and _,            false),
      (           false,              true,      _ and _,            false),
      (           false,             false,      _ and _,            false),
      (            true,              true,       _ or _,             true),
      (            true,             false,       _ or _,             true),
      (           false,              true,       _ or _,             true),
      (           false,             false,       _ or _,            false)
    )

    forAll(conditionTable) { (firstPredicate, secondPredicate, combinator, expected) =>
      combinator(
        GenericCondition(_ => firstPredicate),
        GenericCondition(_ => secondPredicate)
      )(()) shouldBe expected
    }
  }

  it should "combine optional conditions" in {
    val conditionTable = Table[Option[Boolean], Option[Boolean], (Option[GenericCondition[Any]], Option[GenericCondition[Any]]) => GenericCondition[Any], Boolean](
      ("firstPredicate", "secondPredicate", "combinator", "expectedResult"),
      (            None,              None,      _ and _,             true),
      (            None,        Some(true),      _ and _,             true),
      (            None,       Some(false),      _ and _,            false),
      (      Some(true),              None,      _ and _,             true),
      (     Some(false),              None,      _ and _,            false),
      (      Some(true),        Some(true),      _ and _,             true),
      (      Some(true),       Some(false),      _ and _,            false),

      (            None,              None,       _ or _,            false),
      (            None,        Some(true),       _ or _,             true),
      (     Some(false),              None,       _ or _,            false),
      (     Some(false),        Some(true),       _ or _,             true),
      (     Some(true),         Some(true),       _ or _,             true)
    )

    forAll(conditionTable) { (firstPredicateOption, secondPredicateOption, combinator, expected) => {}
      combinator(
        firstPredicateOption.map { firstPredicate => GenericCondition(_ => firstPredicate) },
        secondPredicateOption.map { secondPredicate => GenericCondition(_ => secondPredicate) }
      )(()) shouldBe expected
    }
  }
}
