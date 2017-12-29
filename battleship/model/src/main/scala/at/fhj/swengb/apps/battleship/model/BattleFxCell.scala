package at.fhj.swengb.apps.battleship.model

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
;

/**
  * Represents one part of a vessel or one part of the ocean.
  */

case class BattleFxCell(pos: BattlePos
                        , width: Double
                        , height: Double
                        , log: String => Unit
                        , someVessel: Option[Vessel] = None
                        , fn: (Vessel, BattlePos) => Unit
                        , updateClicks : BattlePos => Unit
                        ) extends Rectangle(width, height) {

  def init(): Unit = {
    if (someVessel.isDefined) {
      setFill(Color.rgb(154,205,50,0.7))
    } else {
      setFill(Color.rgb(200,200,200,0.1))
    }
  }

  setOnMouseClicked(e => {helperMouseClicks()})

  def helperMouseClicks() = {
    if (!isDisable)
      updateClicks(pos)
    someVessel match {
      case None =>
        log(s"Missed. Just hit water.")
        setFill(Color.rgb(127,255,212,0.5))
      case Some(v) =>
        // log(s"Hit an enemy vessel!")
        fn(v, pos)
        setFill(Color.rgb(255,0,0,0.5))
    }
  }
}
