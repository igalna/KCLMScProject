package test.TestLoadingDataFromCSVToIND

import org.scalatest.FlatSpec
import main.dl4j.LoadingDataFromCSVToIND

class TestLoadingDataFromCSVToIND extends FlatSpec{
  
  val ld = new LoadingDataFromCSVToIND
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
  val smallStocks = "stocks1001.csv"
  val smallCurrencies = "currencies1001.csv"
  
  val listOfEqualLengthCSV = List((folderPath + smallCurrencies), (folderPath + smallCurrencies))
  val listOfUnequalLengthCSV = List((folderPath + smallStocks), (folderPath + smallCurrencies))
  val blockSize = 50
  val destination = folderPath + "test.csv"
  val unequalDestination = folderPath + "unequalLengthTest.csv"
  
  "A LoadingDataFromCSVToIND" should "break up multiple CSVs into blocks and inserted into another CSV " in {
    ld.getDataFromMultipleCSV(listOfEqualLengthCSV, blockSize, destination)
  }
  it should " break up CSV of unequal length, so if one CSV is 30 rows and another is 25 " in {
    ld.getDataFromMultipleCSV(listOfUnequalLengthCSV, blockSize, unequalDestination)
  }
}