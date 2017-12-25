package at.fhj.swengb.apps.battleship


// import at.fhj.swengb.apps.battleship.BattleShipProtobuf.{BattleField, Fleet}
import at.fhj.swengb.apps.battleship.model._
import org.scalacheck.{Gen, Prop}
import org.scalatest.WordSpecLike
import org.scalatest.prop.Checkers


class BattleShipProtocolSpec extends WordSpecLike {

  import at.fhj.swengb.apps.battleship.model.BattleshipGameGen._

  "BattleShipProtocol" should {
    "be deserializable" in {
      val field = BattleField(2, 2, Fleet(FleetConfig.Standard))
      val expected = BattleShipGame(field, n => n.toDouble, n => n.toDouble, n => ())
      val actual = BattleShipProtocol.convert(BattleShipProtocol.convert(expected))
      assert(actual.battleField == expected.battleField)
      assert(actual.clicks == expected.clicks)
        }
      }
}
