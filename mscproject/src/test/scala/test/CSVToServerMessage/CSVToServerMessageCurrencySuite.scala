package test.CSVToServerMessage

import org.scalatest.FunSuite
import main.traits.ServerMessage
import main.DataConverter.CSVToServerMessage

class CSVToServerMessageCurrencySuite extends FunSuite{
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\currenciesTestFile.csv"
  val bufferedSource = io.Source.fromFile(fileName)
  
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
}