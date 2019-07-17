package com.mazhangjing.recorder

import java.nio.file.{Path, Paths}
import java.util.concurrent.{ExecutorService, Executors}

import com.mazhangjing.recorder.bean.{Item, Person}
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, Label, TextField}
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import org.slf4j.{Logger, LoggerFactory}
import org.testfx.api.FxRobot
import scala.collection.JavaConverters._

import scala.collection.mutable.ArrayBuffer

class Controller(val app: Recorder, val viewer: Viewer, val model: Model) {

  var ds: DataShift = new DataShift

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val config = "config.yaml"

  val outputPath: Path = Paths.get("result.csv")

  var current_person: Person = model.getNewPerson(config)

  val textFields = new ArrayBuffer[(TextField, Item)]()

  var currentIndex: Int = -1

  var ready_exit = true

  val robot = new FxRobot()

  val ec: ExecutorService = Executors.newFixedThreadPool(3)

  /**
    * 根据传入 demoPerson 信息构建 viewer.content 内容
    * @param person DemoPerson 模板
    */
  def bindContentWithPerson(person: Person): Unit = {
    logger.info(s"Loading Data from $config")
    logger.info(s"Handling ${person.projectName} now...")
    //所有的组
    val groups = person.groupList
    val grid = new GridPane()
    //对于每一组而言
    var currentRow = -1
    groups.forEach(group => {

      //先设置组名组件
      val group_label = new Label(group.groupName)
      group_label.setFont(Font.font(18))

      currentRow += 1
      grid.add(group_label, 0, currentRow)

      //对于每一项而言
      val items = group.itemList
      items.forEach(item => {

        //先设置项目名称
        val item_label = new Label(item.name)

        //再设置项目输入框
        val item_text_field = new TextField()

        /*if (item.value != null && !item.value.isEmpty) {
          item_text_field.setText(item.value)
        }*/

        /*item_text_field.textProperty().addListener((_,_,n) => {
          item.value = n
        })*/

        handleFieldFor(item, item_text_field)

        currentRow += 1
        grid.add(item_label, 0, currentRow)
        grid.add(item_text_field, 1, currentRow)
      })

    })
    grid.setVgap(6)
    grid.setHgap(10)
    viewer.content = grid
  }

  /**
    * 为各 TextFields 添加监听器、添加到集合 TextFields 中，设置提示文本
    */
  def handleFieldFor(item: Item, textField: TextField): Unit = {
    textFields.append((textField, item))
    textField.setPromptText(handlePromptText(item))
    textField.textProperty().addListener((_, _, n) => {
      handleTextPropertyEvent(item, n)
    })
  }

  def handlePromptText(item: Item): String = {
    var text = ""
    if (item.castType.toUpperCase() != "STR") text += s"${item.castType} "
    if (item.equalSize != null) text += s"Fix ${item.equalSize} "
    if (item.matchIn != null && item.matchIn.size() > 0) text += s"In ${item.matchIn.asScala.mkString(", ")} "
    if (item.sizeSmallerThan != null) text += s"< ${item.sizeSmallerThan} "
    text
  }

  def handleTextPropertyEvent(item: Item, newText: String): Unit = {
    if (item.equalSize != null && newText != null && newText.length == item.equalSize)
      ec.execute(() => {
        var ok = true
        //当输入内容完全时
        if (item.matchIn != null && item.matchIn.size() > 0) {
          ok = newText != null && item.matchIn.contains(newText)
        }
        if (item.sizeSmallerThan != null) {
          ok = newText != null && newText.length <= item.sizeSmallerThan
        }
        if (item.castType.toUpperCase() != "STR") {
          ok = Recorder.canGetLongFrom(newText)
        }
        //如果均 OK，则跳转到下一个，反之则全选
        if (ok) robot.push(KeyCode.TAB)
        else {
          robot.push(KeyCode.COMMAND, KeyCode.A)
        }
      })
  }

  def doLoadPersonDataToTextFields(person: Person, textFields: ArrayBuffer[(TextField,Item)]): Unit = {
    logger.info(s"Binding CurrentPerson to UI Elements...")
    var current_Pointer = 0
    person.groupList.forEach(group => {
      group.itemList.forEach(item => {
        val currentTF = textFields.unzip._1(current_Pointer)
        current_Pointer += 1
        if (item.value != null && !item.value.isEmpty) {
          currentTF.setText(item.value)
        } else {
          currentTF.clear()
        }
      })
    })
  }

  def doSavingTextFieldsDataToPerson(currentPerson: Person, textFields: ArrayBuffer[(TextField,Item)]): Unit = {
    logger.info(s"Setting UI Elements data to CurrentPerson...")
    var pointer = 0
    currentPerson.groupList.forEach(group => {
      group.itemList.forEach(item => {
        val ui = textFields.unzip._1(pointer)
        pointer += 1
        if (!ui.getText().isEmpty) {
          item.value = ui.getText()
        }
      })
    })
  }

  def bindSceneEvent(scene: Scene): Unit = {

    scene.setOnKeyPressed(e => {
      Platform.runLater(() => {
        doWhenKeyPressed(e.getCode)
      })
    })

    viewer.saveAndGoNext.setOnAction(_ => {
      doWhenSaveAndGoBtnFired()
    })
    viewer.save.setOnAction(_ => {
      val res = ds.savingData()
      currentIndex = res
      ds.updateDatabaseInfo()
    })

    viewer.before.setOnAction(_ => {
      ds.setDataFromModel(1)
      ds.updateDatabaseInfo()
    })
    viewer.next.setOnAction(_ => {
      ds.setDataFromModel(-1)
      ds.updateDatabaseInfo()
    })
    viewer.new_.setOnAction(_ => {
      ds.settingForNewPerson()
      ds.updateDatabaseInfo()
      textFields.head._1.requestFocus()
    })

    viewer.clearAll.setOnAction(_ => {
      ds.clearAllData()
      ds.settingForNewPerson()
      ds.updateDatabaseInfo()
      textFields.head._1.requestFocus()
    })
    viewer.output.setOnAction(_ => {
      try {
        Recorder.convertPersonsToCsv(model.getAllData, outputPath)
        val alert = new Alert(AlertType.INFORMATION)
        alert.setHeaderText(s"数据导出为 $outputPath")
        alert.show()
      } catch {
        case e: Throwable =>
          val alert = new Alert(AlertType.INFORMATION)
          alert.setHeaderText(s"数据导出出错")
          alert.setContentText(Recorder.getError(e))
          alert.show()
      }
    })
  }

  def doWhenKeyPressed(e:KeyCode): Unit = {
    if (e == KeyCode.ENTER) {
      if (viewer.saveAndGoNext.isFocused) {
        doWhenSaveAndGoBtnFired()
      } else {
        //对于没有固定长度的项目，使用 Tab 或者 Enter 均可跳转到下一项
        //如果检查不通过，则全选组件
        textFields.find(p => p._1.isFocused) match {
          case Some(tf) =>
            var ok = true
            val needCompare = tf._1.getText()
            if (tf._2.castType.toUpperCase.contains("INT")) ok = Recorder.canGetLongFrom(needCompare)
            if (tf._2.equalSize != null && tf._2.equalSize != needCompare.length) ok = false
            if (tf._2.matchIn != null && tf._2.matchIn.size() != 0 && !tf._2.matchIn.contains(needCompare)) ok = false
            if (tf._2.sizeSmallerThan != 0 && needCompare.length > tf._2.sizeSmallerThan) ok = false
            if (!ok) {
              tf._1.selectAll()
              tf._1.requestFocus()
            } else {
              robot.push(KeyCode.TAB)
            }
          case None =>
        }
      }
    }
  }

  def doWhenSaveAndGoBtnFired(): Unit = {
    ds.savingData()
    ds.settingForNewPerson()
    ds.updateDatabaseInfo()
    textFields.head._1.requestFocus()
  }

  class DataShift {

    val logger: Logger = LoggerFactory.getLogger(getClass)

    /**
      * 保存数据：从 UI 到 Bean，从 Bean 到 Model
      */
    def savingData(): Int = {
      logger.info(s"Saving Action Array Start...")
      doSavingTextFieldsDataToPerson(current_person, textFields)
      model.saveData(current_person)
    }

    /**
      * 准备输入新的数据：获取新的 Bean，更新本地引用 current_person，currentIndex, 绑定到 UI
      */
    def settingForNewPerson(): Unit = {
      logger.info("Clearing Status and Ready for Next Person now...")
      val newPerson = model.getNewPerson(config)
      current_person = newPerson
      doLoadPersonDataToTextFields(current_person, textFields)
      currentIndex = -1
    }

    /**
      * 从模型中获取指定位置的数据并更新到 UI：如果有，则更新 current_person, currentIndex 否则不行动
      * @param moveForwardStep 向前移动的步数
      */
    def setDataFromModel(moveForwardStep: Int): Unit = {
      model.getListPerson(currentIndex, moveForwardStep) match {
        case Some((p, ind)) =>
          current_person = p
          doLoadPersonDataToTextFields(current_person, textFields)
          currentIndex = ind
        case None =>
          logger.warn("数据库不存在这样的数据!!!")
      }
    }

    /**
      * 清空所有数据，更新 currentIndex
      */
    def clearAllData(): Unit = {
      model.emptyData()
      currentIndex = -1
    }

    /**
      * 更新数据库信息到 UI 界面
      */
    def updateDatabaseInfo(): Unit = {
      val dbSize = model.getDbSize
      if (currentIndex == -1) {
        viewer.saveAndGoNext.setDisable(false)
        viewer.baseInfo.set(s"[Add] Total Saved: $dbSize - Powered by Scala and JavaFx")
      } else {
        viewer.saveAndGoNext.setDisable(true)
        viewer.baseInfo.set(s"[Edit] ${currentIndex + 1} of $dbSize - Powered by Scala and JavaFx")
      }
    }
  }
}
