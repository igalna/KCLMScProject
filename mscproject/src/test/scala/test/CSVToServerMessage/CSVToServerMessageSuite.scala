package test.CSVToServerMessage

import org.scalatest.FunSuite
import main.DataConverter.CSVToServerMessage
import main.DataConverter.CSVToServerMessage
import com.softwaremill.macwire._
import main.DataConverter.CSVToServerMessage
import main.traits.DataItem
import main.traits.ServerMessage

class CSVToServerMessageSuite extends FunSuite {
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\testfile.csv"
  val bufferedSource = io.Source.fromFile(fileName)
  val serverName:String = "myServer"
  val serverLocation:String = "localhost"
  
  val csvReader: CSVToServerMessage = new CSVToServerMessage(
                                          bufferedSource, 
                                          serverName, 
                                          serverLocation)
  
  val serverMessages:List[ServerMessage] = csvReader.buildList
  
  val firstServerMessage:ServerMessage = serverMessages(0)
  val middleServerMessage = serverMessages(2)
  val lastServerMessage = serverMessages(serverMessages.length -1)
  
  val smMidIndex = middleServerMessage.data.length/2 -1
  val smFinalIndex = middleServerMessage.data.length -1
  
  test("The column names in the CSV reader should contain the first CSV column") {
    val firstCSVColumn: String = "Date"
    assert(csvReader.columnNames.contains(firstCSVColumn))
  }
  
  test("The column names in the CSV reader should contain the last column") {
    val indexOfLastElementInColumnNames: Int = csvReader.columnNames.length-1
    val lastColumnName: String = csvReader.columnNames(indexOfLastElementInColumnNames)
    assert(csvReader.columnNames(indexOfLastElementInColumnNames).equals(lastColumnName))
  }
  test("ServerName should be the same as supplied") {
    val server: String = "myServer"
    assert(csvReader.serverName.equals(server))
  }
  test("ServerLocation in the CSVReader should be as supplied") {
    val location = "localhost"
    assert(csvReader.serverLocation.equals(location))
  }
  
  /*
   * testing the first row of the CSV file
   */
  test("First DataItem in the First ServerMessage built by the CSVReader should equal") {
    val di = new DataItem("Date", "06/06/2016")
    assert(firstServerMessage.data.contains(di))
  }
  test("Middle DataItem in the First ServerMessage built by the CSVReader should equal") {
    val di = new DataItem("IBM", "152.73")
    assert(firstServerMessage.data.contains(di))
  }
  test("Last DataItem in the First ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("DIS", "98.78")
    assert(firstServerMessage.data.contains(di))
  }
  
  /*
   * testing the middle row of the CSV file
   */
  test("First DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("Date", "02/06/2016")
    assert(middleServerMessage.data.contains(di))
  }
  test("First DataItem in the Middle ServerMessage built by the CSVReader should be in the first position") {
    val di = new DataItem("Date", "02/06/2016")
    assert(middleServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("IBM", "153.5")
    assert(middleServerMessage.data.contains(di))
  }
  test("Middle DataItem in the Middle ServerMessage built by the CSVReader should be in the middle position") {
    val di = new DataItem("IBM", "153.5")
    assert(middleServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("DIS", "98.72")
    assert(middleServerMessage.data.contains(di))
  }
  test("Last DataItem in the Middle ServerMessage built by the CSVReader should be in the final position") {
    val di = new DataItem("DIS", "98.72")
    assert(middleServerMessage.data(smFinalIndex).equals(di))
  }
  
  /*
   * testing the final entry from the CSV file
   */
  test("First DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("Date", "27/05/2016")
    assert(lastServerMessage.data.contains(di))
  }
  test("First DataItem in the Last ServerMessage built by the CSVReader should be in the first position") {
    val di = new DataItem("Date", "27/05/2016")
    assert(lastServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("IBM", "152.84")
    assert(lastServerMessage.data.contains(di))
  }
  test("Middle DataItem in the Last ServerMessage built by the CSVReader should be in the middle position") {
    val di = new DataItem("IBM", "152.84")
    assert(lastServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the Last ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("DIS", "100.29")
    assert(lastServerMessage.data.contains(di))
  }
  test("Last DataItem in the Last ServerMessage built by the CSVReader should be in the final position") {
    val di = new DataItem("DIS", "100.29")
    assert(lastServerMessage.data(smFinalIndex).equals(di))
  }
  
}