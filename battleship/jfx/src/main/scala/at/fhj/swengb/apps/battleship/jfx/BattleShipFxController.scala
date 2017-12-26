package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea}
import javafx.scene.layout.GridPane
import java.nio.file.{Files, Paths}
import javax.swing.{JFileChooser, JFrame}

import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.{BattleShipProtobuf, BattleShipProtocol}
import at.fhj.swengb.apps.battleship.BattleShipProtocol._

class BattleShipFxController extends Initializable {



  @FXML private var battleGroundGridPane: GridPane = _

  /**
    * A text area box to place the history of the game
    */

  @FXML private var textLog: TextArea = _

  @FXML private var currentGame : BattleShipGame = _

  @FXML private var slider : Slider = _

  @FXML def newGame(): Unit = initGame()

  @FXML def savingGame() : Unit = {
    val saveJFileChooser = new JFileChooser
    saveJFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

    if (saveJFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      val saveGame = BattleShipProtocol.convert(currentGame)
      val saveFile = saveJFileChooser.getSelectedFile
      saveGame.writeTo(Files.newOutputStream(Paths.get(saveFile.getAbsolutePath + "\\savegame.bin")))
    }
  }

  @FXML def loadingGame() : Unit = {
    val loadJFileChooser = new JFileChooser
    loadJFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)

    if (loadJFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      val loadFile = loadJFileChooser.getSelectedFile
      val loadPath = loadFile.getAbsolutePath
      val getSavedGame : BattleShipProtobuf.BattleShipGame = BattleShipProtobuf.BattleShipGame
        .parseFrom(Files.newInputStream(Paths.get(loadPath)))
      val loadGame : BattleShipGame = BattleShipProtocol.convert(getSavedGame)
      val buildGame = BattleShipGame(loadGame.battleField, getCellWidth, getCellHeight, appendLog, updateSlider)
      init(buildGame, loadGame.clicks)
    }
  }

  @FXML def updateSlider(slideClicks : Int) : Unit = {
    slider.setMax(slideClicks)
    slider.setValue(slideClicks)
  }

  @FXML def slideAlong() : Unit = {
    val sliderPos : Int = slider.getValue.toInt
    val sliderPosList = currentGame.clicks.reverse.take(sliderPos).reverse
    var bool : Boolean = false

    if (sliderPos == slider.getMax.toInt) {
      bool = false
      currentGame.clicks = List()
    }
    else {
      bool = true
    }
    battleGroundGridPane.getChildren.clear()
   for (cell <- currentGame.getCells()) {
     battleGroundGridPane.add(cell, cell.pos.x, cell.pos.y)
     cell.init()
     cell.setDisable(bool)
   }
    currentGame.loadingClicks(sliderPosList)
  }

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = textLog.appendText(message + "\n")

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(game : BattleShipGame, getClicks : List[BattlePos]) : Unit = {
    currentGame = game
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }

    textLog.clear()

    updateSlider(getClicks.size)
    game.getCells().foreach(c => c.init)
    game.clicks = List()
    game.loadingClicks(getClicks)
  }

  private def initGame(): Unit = {
    val game: BattleShipGame = createGame()
    init(game, List())
    appendLog("New game started.")
  }

  private def createGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battleField: BattleField = BattleField.placeRandomly(field)

    BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog, updateSlider)
  }


}