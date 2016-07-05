package test.CSVToServerMessage

import org.scalatest.FunSuite
import main.DataConverter.CSVToServerMessage
import main.traits.ServerMessage
import main.traits.DataItem

class CSVToServerMessageLargeFileSuite extends FunSuite {
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\largeTestFile.csv"
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
   * testing the first row of the CSV file
	 */
  test("First DataItem in the First ServerMessage built by the CSVReader should equal") {
    val di = new DataItem("Date", "06/06/2016")
    assert(firstServerMessage.data.contains(di))
  }
  test("First DataItem in the First ServerMessage should be in the first index position") {
    val di = new DataItem("Date", "06/06/2016")
    assert(firstServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the First ServerMessage built by the CSVReader should equal") {
    val di = new DataItem("IBM", "152.73")
    assert(firstServerMessage.data.contains(di))
  }
  test("Middle DataItem in the First ServerMessage should be in the middle index position") {
    val di = new DataItem("IBM", "152.73")
    assert(firstServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the First ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("DIS", "98.78")
    assert(firstServerMessage.data.contains(di))
  }
  test("Last DataItem in the First ServerMessage should be in the last index position") {
    val di = new DataItem("DIS", "98.78")
    assert(firstServerMessage.data(smFinalIndex).equals(di))
  }
  
  /*
   * testing the final row of the CSV file
   */
  test("First DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("Date", "27/06/2008")
    assert(lastServerMessage.data.contains(di))
  }
  test("First DataItem in the Last ServerMessage should be in the first index position") {
    val di = new DataItem("Date", "27/06/2008")
    assert(lastServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("IBM", "120.05")
    assert(lastServerMessage.data.contains(di))
  }
  test("Middle DataItem in the Last ServerMessage should be in the middle index position") {
    val di = new DataItem("IBM", "120.05")
    assert(lastServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the Last ServerMessage buid by the CSVReader should equal di") {
    val di = new DataItem("DIS", "31.57")
    assert(lastServerMessage.data.contains(di))
  }
  test("Last DataItem in the Last ServerMessage should be in the last index position") {
    val di = new DataItem("DIS", "31.57")
    assert(lastServerMessage.data(smFinalIndex).equals(di))
  }
}