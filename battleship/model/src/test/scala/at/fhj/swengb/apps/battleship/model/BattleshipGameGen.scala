package at.fhj.swengb.apps.battleship.model

import org.scalacheck.Gen

/**
  * Implement in the same manner like MazeGen from the lab, adapt it to requirements of BattleShip
  */

object BattleshipGameGen {

  val battleShipGameGen: Gen[BattleShipGame] = for {
    battlefield <- field
  }
    yield BattleShipGame(battlefield, x => x.toDouble, x => x.toDouble, x => (),x => ())

  val field : Gen[BattleField] = for {
    width <- Gen.chooseNum[Int](1, 10)
    height <- Gen.chooseNum[Int](1, 10)
  }
    yield BattleField(width, height, Fleet(FleetConfig.Standard))

}
