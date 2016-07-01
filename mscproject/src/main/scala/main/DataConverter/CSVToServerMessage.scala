package main.DataConverter

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import main.traits.ServerMessage
import scala.collection.mutable.ListBuffer
import main.traits.DataItem

class CSVToServerMessage(val source: Source,
                         val serverName: String,
                         val serverLocation: String) {
  
  val columnNames:ListBuffer[String] = new ListBuffer[String]
  val rows = ArrayBuffer[Array[String]]()
  
  private def readFile = {
    for (line <- source.getLines) {
       rows += line.split(",").map {_.trim}
     }
  }
  
  private def getColumnNames = {
    for (line <- rows.take(1)) {
       for (x <- line) {
         columnNames += x
       }
     }
  }
  private def readLineIntoListOfDataItems: ListBuffer[ListBuffer[DataItem]] = {
    val listOfListsOfDataItems: ListBuffer[ListBuffer[DataItem]] = new ListBuffer[ListBuffer[DataItem]]
    var listOfDataItems: ListBuffer[DataItem] = new ListBuffer[DataItem]
    var counter: Int = 0
    var counter2: Int = 1
    for (line <- rows.drop(counter2)) {
      for (x <- line) {
        listOfDataItems += new DataItem(columnNames(counter), x)
        counter += 1
      }
      listOfListsOfDataItems += listOfDataItems
      counter = 0
      counter2 += 1
      listOfDataItems = new ListBuffer[DataItem]
    }
    return listOfListsOfDataItems
  }
  def buildList:List[ServerMessage] = {
    readFile
    getColumnNames
    val serverMessages: ListBuffer[ServerMessage] = new ListBuffer[ServerMessage]
    val list = readLineIntoListOfDataItems
    for (l <- list) {
      serverMessages += new ServerMessage(serverName, serverLocation, l.toList)
    }
    return serverMessages.toList
    
  }
}