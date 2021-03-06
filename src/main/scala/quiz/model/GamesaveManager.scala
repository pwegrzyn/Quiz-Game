package quiz.model

import java.io._
import java.text.SimpleDateFormat

import scala.collection.mutable

class GamesaveManager {

  var gamesaveList: mutable.MutableList[Gamesave] = new mutable.MutableList[Gamesave]
  val gamesaveFile = "gamesaves.sav"
  val gamesaveFilePath = s"$gamesaveFile"

  def loadGamesaveFile(): Unit = {
    try{
      val inputStream = new ObjectInputStream(new FileInputStream(gamesaveFilePath))
      gamesaveList = inputStream.readObject().asInstanceOf[mutable.MutableList[Gamesave]]
      inputStream.close()

    } catch {
      case ex1: FileNotFoundException =>
        gamesaveList = new mutable.MutableList[Gamesave]()
      case ex: Throwable =>
        println(ex.toString)
    }
  }

  def updateGamesaveFile(): Unit = {
    try{
      val outputStream = new ObjectOutputStream(new FileOutputStream(gamesaveFilePath))
      outputStream.writeObject(gamesaveList)
      outputStream.close()
    } catch {
      case ex: Throwable =>
        println(ex.toString)
    }
  }

  def addGamesave(newGameSave: Gamesave): Unit = {
    loadGamesaveFile()
    gamesaveList += newGameSave
    updateGamesaveFile()
  }

  def deleteGamesave(gamesave : Gamesave): Unit = {
    gamesaveList = gamesaveList diff Seq(gamesave)
    updateGamesaveFile()
  }

  def reset(): Boolean = {
    new File(gamesaveFilePath).delete()
  }

  def getGamesaveString: String = {
    loadGamesaveFile()
    var result = StringBuilder.newBuilder
    for(gamesave <- gamesaveList){
      result.append(gamesave.player)
      result.append("\t")
      result.append(gamesave.score)
      result.append("\t")
      result.append(gamesave.currentQuestion + "/" + gamesave.numberOfQuestions)
      result.append("\t")
      val sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val strDate = sdfDate.format(gamesave.date)
      result.append(strDate.toString)
      result.append("\n")
    }

    result.toString()
  }
}