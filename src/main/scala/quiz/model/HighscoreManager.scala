package quiz.model

import java.io._
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import scala.collection.mutable

class HighscoreManager() {

  var scoreList: mutable.MutableList[Score] = new mutable.MutableList[Score]
  val highScoreFile = "scores.dat"
  val highscorefilePath = s"data\\$highScoreFile"  //TODO adjust "/" to linux

  private def loadScoreFile() = {
    try{
      val inputStream = new ObjectInputStream(new FileInputStream(highscorefilePath))
      scoreList = inputStream.readObject().asInstanceOf[mutable.MutableList[Score]]
      inputStream.close()

    } catch {
        case ex1: FileNotFoundException => {
          val outputStream = new ObjectOutputStream(new FileOutputStream(highscorefilePath))
          outputStream.writeObject(scoreList)
          outputStream.close()    // TODO try catch exception
        }
        case ex: Throwable => {
          println(ex.toString)
        }
      }
  }

  private def updateScoreFile() = {
    try{
      val outputStream = new ObjectOutputStream(new FileOutputStream(highscorefilePath))
      outputStream.writeObject(scoreList)
      outputStream.close()
    } catch {
      case ex: Throwable => {
        println(ex.toString)
      }
    }
  }

  def addScore(score: Score) = {
    loadScoreFile()
    scoreList += score
    scoreList = scoreList.sortWith(_.score > _.score)
    updateScoreFile()
  }

  def getHighscoreString() = {
    var result = StringBuilder.newBuilder
    loadScoreFile()
    for(score <- scoreList){
      result.append(score.playername)
      result.append("\t")
      result.append(score.score)
      result.append("\t\t")
      val sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val strDate = sdfDate.format(score.date)
      result.append(strDate.toString)
      result.append("\n")
    }

    result.toString()
  }

}
