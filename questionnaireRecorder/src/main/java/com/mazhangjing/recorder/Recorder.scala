package com.mazhangjing.recorder

import java.io.{PrintWriter, StringWriter}
import java.nio.file.Path

import com.mazhangjing.recorder.bean.Person
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.stage.Stage

class Recorder extends Application {

  val baseInfo = new SimpleStringProperty("questionnaireRecorder")

  val viewer = new Viewer
  val model = new Model
  val controller = new Controller(this, viewer, model)
  viewer.controller = controller
  viewer.baseInfo = baseInfo

  override def start(stage: Stage): Unit = {
    stage.titleProperty().bind(baseInfo)
    stage.setScene(viewer.scene)
    stage.show()
    stage.setOnCloseRequest(_ => {
      if (controller.ready_exit) System.exit(0)
    })
    controller.bindSceneEvent(viewer.scene)
  }

}

object Recorder {
  def canGetLongFrom(in:String): Boolean = try {
    in.toLong; true
  } catch {
    case _: Throwable => false
  }

  def getError(e:Throwable):String = {
    val sb = new StringWriter()
    val pw = new PrintWriter(sb)
    e.printStackTrace(pw)
    val res = sb.toString
    pw.close()
    sb.close()
    res
  }

  def convertPersonToString(person:Person): String = {
    def textFieldSplit(str: String):String = str match {
      case i if canGetLongFrom(i) =>
        val strings = str.toCharArray
        strings.mkString(", ")
      case o => o
    }

    val sb = new StringBuilder()
    person.groupList.forEach(group => {
      group.itemList.forEach(item => {
        val v = {
          val t = item.value match {
            case err if err == null || err.isEmpty => ""
            case nor => nor
          }
          item.castType.toUpperCase match {
            case i if i == "INT" =>
              textFieldSplit(t)
            case _ => t
          }
        }
        sb.append(v).append(", ")
      })
    })
    sb.append("\n")
    sb.toString()
  }

  def convertPersonsToCsv(persons: Array[Person], output:Path): Unit = {
    val sb = new StringBuilder()
    persons.foreach(p => {
      val row = convertPersonToString(p)
      sb.append(row)
    })
    val file = new PrintWriter(output.toFile)
    file.print(sb.toString())
    file.flush()
    file.close()
  }
}