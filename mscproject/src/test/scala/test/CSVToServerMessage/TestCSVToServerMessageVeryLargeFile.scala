package test.CSVToServerMessage

import org.scalatest.FunSuite
import main.traits.ServerMessage
import main.traits.Player
import main.GameServer.GameServer
import main.DataConverter.CSVToServerMessage
import com.softwaremill.macwire._
import main.traits.DataItem

class CSVToServerMessageVeryLargeFile extends FunSuite {
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\veryLargeTestFile.csv"
  val bufferedSource = scala.io.Source.fromFile(fileName)
  val serverName:String = "myServer"
  val serverLocation:String = "localhost"
  
  val csvReader: CSVToServerMessage = new CSVToServerMessage(
                                          bufferedSource, 
                                          serverName, 
                                          serverLocation)
  
  val serverMessages:List[ServerMessage] = csvReader.buildList
  
  val firstServerMessage:ServerMessage = serverMessages(0)
  val middleServerMessage = serverMessages(serverMessages.length /2)
  val lastServerMessage = serverMessages(serverMessages.length -1)
  
  val smMidIndex = middleServerMessage.data.length/2 -1
  val smFinalIndex = middleServerMessage.data.length -1
  
  
  /*
   * testing the final row of the CSV file
   */
  test("First DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("Date", "1999-01-04")
    assert(lastServerMessage.data.contains(di))
  }
  test("First DataItem in the Last ServerMessage should be in the first index position") {
    val di = new DataItem("Date", "1999-01-04")
    assert(lastServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("IBM", "183.0")
    assert(lastServerMessage.data.contains(di))
  }
  test("Middle DataItem in the Last ServerMessage should be in the middle index position") {
    val di = new DataItem("IBM", "183.0")
    assert(lastServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the Last ServerMessage buid by the CSVReader should equal di") {
    val di = new DataItem("DIS", "29.56")
    assert(lastServerMessage.data.contains(di))
  }
  test("Last DataItem in the Last ServerMessage should be in the last index position") {
    val di = new DataItem("DIS", "29.56")
    assert(lastServerMessage.data(smFinalIndex).equals(di))
  }
  
  
  /*
   * testing null values as a "null" string
   */
  test("Null values in data should equal \"null\"") {
    val di = new DataItem("GS", "null")
    assert(lastServerMessage.data(12).equals(di))
  }
}