package test.CSVToServerMessage

import org.scalatest.FunSuite

import main.DataConverter.CSVToServerMessage
import main.traits.DataItem
import main.traits.ServerMessage

class CSVToServerMessageCurrencySuite extends FunSuite{
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\currenciesTestFile.csv"
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
   * test column names
   */
  test("first column name should be \"Date/Time\"") {
    val dateColumnName = "Date/Time"
    assert(csvReader.columnNames(0).equals(dateColumnName))
  }
  test("Second column name should be \"AUDCHF=X\"") {
    val secondColumnName = "AUDCHF=X"
    assert(csvReader.columnNames(1).equals(secondColumnName))
  }
  test("Final column name should be \"USDNZD=X\"") {
    val finalColumnName = "USDNZD=X"
    assert(csvReader.columnNames(csvReader.columnNames.length -1).equals(finalColumnName))
  }
  
  /*
   * test values for middle row of table
   */
  test("First DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("Date/Time", "2016-06-27T13:29:01.518")
    assert(middleServerMessage.data.contains(di))
  }
  test("First DataItem in the Middle ServerMessage built by the CSVReader should be in the first position") {
    val di = new DataItem("Date/Time", "2016-06-27T13:29:01.518")
    assert(middleServerMessage.data(0).equals(di))
  }
  test("Middle DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("GBPCHF=X", "1.2867")
    assert(middleServerMessage.data.contains(di))
  }
  test("Middle DataItem in the Middle ServerMessage built by the CSVReader should be in the middle position") {
    val di = new DataItem("GBPCHF=X", "1.2867")
    assert(middleServerMessage.data(smMidIndex).equals(di))
  }
  test("Last DataItem in the Middle ServerMessage built by the CSVReader should equal di") {
    val di = new DataItem("USDNZD=X", "1.4182")
    assert(middleServerMessage.data.contains(di))
  }
  test("Last DataItem in the Middle ServerMessage built by the CSVReader should be in the final position") {
    val di = new DataItem("USDNZD=X", "1.4182")
    assert(middleServerMessage.data(smFinalIndex).equals(di))
  }
  
}