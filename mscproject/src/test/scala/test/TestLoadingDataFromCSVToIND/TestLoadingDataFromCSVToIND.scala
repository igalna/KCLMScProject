package test.TestLoadingDataFromCSVToIND

import org.scalatest.FlatSpec
import main.dl4j.LoadingDataFromCSVToIND

class TestLoadingDataFromCSVToIND extends FlatSpec{
  
  val ld = new LoadingDataFromCSVToIND
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
  val smallStocks = "stocks501Recent.csv"
  val smallCurrencies = "currencies501Recent.csv"
  
  val listOfEqualLengthCSV = List((folderPath + smallCurrencies), (folderPath + smallStocks))
  val listOfUnequalLengthCSV = List((folderPath + smallStocks), (folderPath + smallCurrencies))
  val blockSize = 25
  val destination = folderPath + "test.csv"
  val unequalDestination = folderPath + "destination.csv"
  
//  "A LoadingDataFromCSVToIND" should "break up multiple CSVs into blocks and inserted into another CSV " in {
//    ld.getDataFromMultipleCSV(listOfEqualLengthCSV, blockSize, destination)
//  }
//  it should " break up CSV of unequal length, so if one CSV is 30 rows and another is 25 " in {
//    ld.getDataFromMultipleCSV(listOfUnequalLengthCSV, blockSize, unequalDestination)
//  }
  
  "A LoadingData " should " load this data " in {
    ld.getDataFromMultipleCSV(listOfUnequalLengthCSV, blockSize, unequalDestination)
    ld.getDataFromMultipleCSV(listOfEqualLengthCSV, blockSize, destination)
  }
  
//  "this " should " work" in {
//    val per = 5
//    val value = 50
//    val percentOf = per * 100.0 / value
//    val whatIsOf = (per * value) / 100.0
//    val percentIncDec = (value - per) / per * 100.0
//    
//    val lower = value - whatIsOf
//    val upper = value + whatIsOf
//    println(per + " percent of " + value + " should be : " + percentOf)
//    println("what is " + per + " percent of " + value + " : " + whatIsOf)
//    println("percentage increas/decrease : " + percentIncDec)
//    println(lower)
//    println(upper)
//  }
}