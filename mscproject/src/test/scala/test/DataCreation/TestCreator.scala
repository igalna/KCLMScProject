package test.DataCreation

import org.scalatest.FlatSpec
import main.DataCreation.Creator
import main.dl4j.LoadingDataFromCSVToIND
import scala.collection.mutable.ListBuffer

class TestCreator extends FlatSpec{
  
  val creator: Creator = new Creator
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
  val smallStocks = "smallTestFileEarliestToLatest.csv"
  
  var dataFromFile: ListBuffer[Array[Double]] = new ListBuffer
  var WIDTH = 0
  lazy val SIZE_OF_DATA = dataFromFile.size
  
  createFileData
  
  "A Creator object " should "be able to add data to it's currently known data " in {
    addDataToCreator
  }
  it should " be able to set the number of imaginary data to be created " in {
    creator.setNumberOfDataItemsToCreate(5)
  }
  it should " be able to set the range for data to be created from within" in {
    creator.setRangeToCreateDataFromWithin(2.0)
  }
  it should " create data items based on the average of it's known data items" in {
    val extraData = creator.createDataFromAverageOfEachItem
    assert(extraData.size == 35)
  }
  
  private def createFileData = {
    val src = scala.io.Source.fromFile(folderPath + smallStocks)
    val iter = src.getLines().drop(1)
    
    for (line <- iter) {
      
      val item = line.split(",").map { _.trim }
      WIDTH = item.length
      val lineData: Array[Double] = new Array(WIDTH)
      
      var counter = 0
      for (data <- item) {
        lineData(counter) = data.toDouble
        counter += 1
      }
      dataFromFile += lineData
    }
  }
  private def addDataToCreator = {
    for (data <- dataFromFile) {
      creator.addDataToKnownData(data)
    }
  }
}