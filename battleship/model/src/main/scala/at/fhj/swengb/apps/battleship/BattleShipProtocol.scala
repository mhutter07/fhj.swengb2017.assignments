package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.model._
import scala.collection.JavaConverters._


object BattleShipProtocol {

  def convert(g : BattleShipGame) : BattleShipProtobuf.BattleShipGame = {
    BattleShipProtobuf.BattleShipGame.newBuilder().setBattlefield(BattleShipProtobuf.BattleField.newBuilder()
        .setWidth(g.battleField.width).setHeight(g.battleField.height).setFleet(BattleShipProtobuf.Fleet
        .newBuilder().addAllVessel(g.battleField.fleet.vessels.map(convert).asJava).build()).build())
        .addAllClick(g.clicks.map(convert).asJava).build()
  }

  def convert(g : BattleShipProtobuf.BattleShipGame) : BattleShipGame = {
    val battlefield : BattleField = BattleField(g.getBattlefield.getWidth, g.getBattlefield.getHeight, Fleet(g.getBattlefield.getFleet.getVesselList.asScala.map(convert).toSet))
    val battleshipGame = BattleShipGame(battlefield, ((i : Int) => i.toDouble), ((i : Int) => i.toDouble), i => ())
    g.getClickList.asScala.map(convert).foreach(battleshipGame.updateClicks)
    battleshipGame
  }


  def convert(g: BattleShipProtobuf.Vessels) : Vessel = {
    val dir = g.getDirection match {
        case "horizontal" => Horizontal
        case "vertical" => Vertical
      }
    Vessel(NonEmptyString(g.getName), convert(g.getStartPos), dir, g.getSize)
  }

  def convert(g: Vessel) : BattleShipProtobuf.Vessels = {
    BattleShipProtobuf.Vessels.newBuilder().setName(g.name.value).setStartPos(convert(g.startPos))
      .setSize(g.size).setDirection(g.direction match {
        case Vertical => "vertical"
        case Horizontal => "horizontal"
      }).build()
  }

  def convert(pos: BattlePos): BattleShipProtobuf.Position = {
    BattleShipProtobuf.Position.newBuilder().setCoordX(pos.x).setCoordY(pos.y).build()
  }

  def convert(pos: BattleShipProtobuf.Position) : BattlePos = {
    BattlePos(pos.getCoordX,pos.getCoordY)
  }
}

