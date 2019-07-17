package com.mazhangjing.recorder

import java.io.{FileInputStream, FileReader, InputStream}
import java.nio.file.Paths

import com.mazhangjing.recorder.bean.Person
import org.slf4j.{Logger, LoggerFactory}
import org.yaml.snakeyaml.Yaml

import scala.collection.mutable.ArrayBuffer

class Model {

  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  private[this] val data: ArrayBuffer[Person] = new ArrayBuffer[Person]()

  private[this] def loadConfig(inputStream: InputStream): Person = {
    val res = new Yaml().loadAs(inputStream, classOf[Person])
    try {
      inputStream.close()
    } catch {
      case _ : Throwable => logger.warn("Config Closed Error.")
    }
    res
  }

  def getDbSize: Int = data.size

  def getAllData: Array[Person] = data.toArray

  def saveData(person: Person): Int = {
    logger.info(s"Saving Data-[${data.size}] for $person now..., Before Saving")
    if (person != null && !data.contains(person)) {
      data.append(person)
    }
    data indexOf person
  }

  def emptyData(): Unit = {
    data.clear()
  }

  def getNewPerson(config: String): Person = {
    //val stream = getClass.getClassLoader.getResourceAsStream(config)
    val stream = new FileInputStream(Paths.get(config).toFile)
    loadConfig(stream)
  }

  def getListPerson(lastPersonIndex: Int, moveStep: Int):Option[(Person,Int)] = {
    if (lastPersonIndex == -1) {
      if (data.isEmpty) None //清空后没有数据
      else Option(data.last, data.size - 1)
    } else {
      logger.debug(s"LastIndex is $lastPersonIndex, Move by $moveStep")
      try {
        val p = data(lastPersonIndex - moveStep)
        Option((p, data indexOf p))
      } catch {
        case _: Throwable => None
      }
    }
  }

}
