package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Slider, TextArea}
import javafx.scene.layout.GridPane
import java.nio.file.{Files, Paths}
import javax.swing.JFileChooser

import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.{BattleShipProtobuf, BattleShipProtocol}
import at.fhj.swengb.apps.battleship.BattleShipProtocol._

class BattleShipFxController extends Initializable {



  @FXML private var battleGroundGridPane: GridPane = _


  /**
    * A text area box to place the history of the game
    */
  @FXML private var log: TextArea = _

  var currentGame : BattleShipGame = _

  @FXML def newGame(): Unit = initGame()

  @FXML def saving() : Unit = {
    val saveJFileChooser = new JFileChooser
    saveJFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    saveJFileChooser.setAcceptAllFileFilterUsed(false)

    if (saveJFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      val saveGame = BattleShipProtocol.convert(currentGame)
      val savePath = saveJFileChooser.getSelectedFile.getAbsolutePath + "\\savegame.bin"
      saveGame.writeTo(Files.newOutputStream(Paths.get(savePath)))
    }
  }

  @FXML def loading() : Unit = {
    val loadJFileChooser = new JFileChooser
    loadJFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
    loadJFileChooser.setAcceptAllFileFilterUsed(false)

    if (loadJFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      val loadPath = Paths.get(loadJFileChooser.getSelectedFile.getAbsolutePath)
      val loadedGame : BattleShipProtobuf.BattleShipGame = BattleShipProtobuf.BattleShipGame.parseFrom(Files.newInputStream(loadPath))
      init(BattleShipGame(convert(loadedGame).battleField, getCellWidth, getCellHeight, appendLog))
    }
  }


  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(game : BattleShipGame) : Unit = {
    currentGame = game
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    game.getCells().foreach(c => c.init)
  }


  private def initGame(): Unit = {
    val game: BattleShipGame = createGame()
    init(game)
    appendLog("New game started.")
  }

  private def createGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battleField: BattleField = BattleField.placeRandomly(field)

    BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog)
  }


}