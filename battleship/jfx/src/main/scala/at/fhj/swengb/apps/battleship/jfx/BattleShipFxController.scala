package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea}
import javafx.scene.layout.GridPane
import java.nio.file.{Files, Paths}
import javafx.stage.FileChooser
import java.io.File

import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.{BattleShipProtobuf, BattleShipProtocol}



class BattleShipFxController extends Initializable {

  @FXML private var battleGroundGridPane: GridPane = _

  /**
    * A text area box to place the history of the game
    */

  @FXML private var textLog: TextArea = _

  @FXML private var currentGame : BattleShipGame = _

  @FXML private var slider : Slider = _

  @FXML def newGame(): Unit = initGame()

  @FXML def savingGame() : Unit ={
    val saveFileChooser : FileChooser = new FileChooser
    val saveFileFilter : FileChooser.ExtensionFilter = new FileChooser.ExtensionFilter("BattleShip Savegame", "*.bin")
    saveFileChooser.getExtensionFilters.add(saveFileFilter)
    val gameToSave : File = saveFileChooser.showSaveDialog(BattleShipFxApp.game)

    if (gameToSave != null) {
      BattleShipProtocol.convert(currentGame).writeTo(Files.newOutputStream(Paths.get(gameToSave.getAbsolutePath)))
    }
    appendLog("Game successfully saved!")
  }

  @FXML def loadingGame() : Unit = {
    val loadFileChooser  : FileChooser  = new FileChooser
    val loadFileFilter : FileChooser.ExtensionFilter = new FileChooser.ExtensionFilter("BattleShip Savegame", "*.bin")
    loadFileChooser.getExtensionFilters.add(loadFileFilter)
    val gameToLoad : File = loadFileChooser.showOpenDialog(BattleShipFxApp.game)

    if (gameToLoad != null) {
      val getSavedGame : BattleShipProtobuf.BattleShipGame = BattleShipProtobuf.BattleShipGame.parseFrom(Files.newInputStream(Paths.get(gameToLoad.getAbsolutePath)))
      val loadGame : BattleShipGame = BattleShipProtocol.convert(getSavedGame)
      init(BattleShipGame(loadGame.battleField, getCellWidth, getCellHeight, appendLog, updateSlider), loadGame.clicks)
    }
    appendLog("Game successfully loaded!")
  }

  @FXML def updateSlider(slideClicks : Int) : Unit = {
    slider.setMax(slideClicks)
    slider.setMin(0)
    slider.setValue(slideClicks)
    slider.setMinorTickCount(0)
    slider.setMajorTickUnit(1)
    slider.setSnapToTicks(true)
    slider.setShowTickMarks(true)
    slider.setShowTickLabels(true)
  }

  @FXML def slideAlong() : Unit = {
    val sliderPos : Int = slider.getValue.toInt
    val sliderPosList : List[BattlePos] = currentGame.clicks.takeRight(sliderPos)
    var bool : Boolean = false

    if (sliderPos == slider.getMax.toInt) {
      bool = false
      currentGame.clicks = List()
    }
    else {
      bool = true
    }
    battleGroundGridPane.getChildren.clear()
    currentGame.getCells().foreach{cell =>
      battleGroundGridPane.add(cell, cell.pos.x, cell.pos.y)
      cell.init()
      cell.setDisable(bool)
   }
    currentGame.loadingClicks(sliderPosList)
  }

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = textLog.appendText("\n" + message + "\n")

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
    for (c <- game.getCells()) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }

    textLog.clear()

    updateSlider(getClicks.size)
    game.getCells().foreach(c => c.init())
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