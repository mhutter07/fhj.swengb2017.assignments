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
  @FXML private var log: TextArea = _

  @FXML private var currentGame : BattleShipGame = _

  @FXML def newGame(): Unit = initGame()

  @FXML def savingGame() : Unit = {
    val saveFrame = new JFrame()
    val saveJFileChooser = new JFileChooser
    saveJFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

    if (saveJFileChooser.showSaveDialog(saveFrame) == JFileChooser.APPROVE_OPTION) {
      val saveGame = BattleShipProtocol.convert(currentGame)
      val saveFile = saveJFileChooser.getSelectedFile
      saveGame.writeTo(Files.newOutputStream(Paths.get(saveFile.getAbsolutePath + "\\savegame.bin")))
    }
  }

  @FXML def loadingGame() : Unit = {
    val loadFrame = new JFrame()
    val loadJFileChooser = new JFileChooser
    loadJFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)

    if (loadJFileChooser.showOpenDialog(loadFrame) == JFileChooser.APPROVE_OPTION) {
      val loadFile = loadJFileChooser.getSelectedFile
      val loadPath = loadFile.getAbsolutePath
      val inputStream = Files.newInputStream(Paths.get(loadPath))
      val loadGame : BattleShipProtobuf.BattleShipGame = BattleShipProtobuf.BattleShipGame.parseFrom(inputStream)
      val resumeGame = BattleShipGame(convert(loadGame).battleField, getCellWidth, getCellHeight, appendLog)
      resumeGame.clicks = convert(loadGame).clicks
      init(resumeGame)
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