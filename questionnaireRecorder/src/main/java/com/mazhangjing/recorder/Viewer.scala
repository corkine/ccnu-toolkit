package com.mazhangjing.recorder

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.text.Font

class Viewer {

  var baseInfo: SimpleStringProperty = _

  var controller: Controller = _

  val header: Label = {
    val title = new Label("问卷录入")
    title.setFont(Font.font(40))
    title
  }

  val saveAndGoNext = new Button("保存并继续")
  val before = new Button("上一个")
  val save = new Button("保存")
  val next = new Button("下一个")
  val new_ = new Button("新建")
  val clearAll = new Button("清空")
  val output = new Button("导出")

  val footer: HBox = {
    val box = new HBox()
    box.setAlignment(Pos.BASELINE_LEFT)
    box.setSpacing(10)
    box.getChildren.addAll(saveAndGoNext, before, save, next, new_, clearAll, output)
    box
  }

  var content: Node = _

  private[this] def system(): HBox = {
    val box = new VBox(); box.setAlignment(Pos.CENTER_LEFT)
    box.setSpacing(18)
    box.getChildren.add(header)
    box.getChildren.add(content)
    box.getChildren.add(footer)
    val hbox = new HBox()
    hbox.setAlignment(Pos.CENTER)
    hbox.getChildren.add(box)
    hbox
  }

  lazy val scene: Scene = new Scene(getRoot, 800, 930)

  def getRoot: Parent = {
    //draw content
    controller.bindContentWithPerson(controller.current_person)
    //build root
    system()
  }
}
