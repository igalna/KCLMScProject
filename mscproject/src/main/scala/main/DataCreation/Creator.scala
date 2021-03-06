package main.DataCreation

import scala.collection.mutable.ListBuffer
import org.nd4j.linalg.dataset.DataSet
import scala.util.Random

class Creator {
  
  private var currentlyKnownData: ListBuffer[Array[Double]] = new ListBuffer
  private var rangeToCreateDataFromWithin: Double = 0
  private var numberOfDataItemsToCreate: Int = 0
  
  val rnd: Random = new Random
  
  def addDataToKnownData(arr: Array[Double]) = {
    currentlyKnownData += arr
  }
  def removeDataFromKnownData = {
    currentlyKnownData.remove(0)
  }
  def getSizeOfKnownData: Int = {
    currentlyKnownData.size
  }
  def setRangeToCreateDataFromWithin(range: Double) = {
    rangeToCreateDataFromWithin = range
  }
  def setNumberOfDataItemsToCreate(num: Int) = {
    numberOfDataItemsToCreate = num
  }
  
  def createDataFromAverageOfEachItem: ListBuffer[Array[Double]] = {
    var avgList: Array[Double] = new Array(currentlyKnownData(0).length)
    
    var counter = 0
    for (data <- currentlyKnownData) {
      for (doub <- data) {
        avgList(counter) += doub
        counter += 1
      }
      counter = 0
    }
    avgList.map { x => x / avgList.length }
    
    var createdData: ListBuffer[Array[Double]] = new ListBuffer
    counter = 0
    while (counter <= numberOfDataItemsToCreate) {
      var addList: ListBuffer[Double] = new ListBuffer
      for (double <- avgList) {
        //val random = rnd.nextDouble()
        val whatIsXPercentOfY = (rangeToCreateDataFromWithin * double) / 100.0
        //val whatIsXPercentOfY = rangeToCreateDataFromWithin * 100 / double
        val upperBound = double + whatIsXPercentOfY
        val lowerBound = (if (double - whatIsXPercentOfY > 0) {
                            double - whatIsXPercentOfY
                          } else 0.1)
        val range = lowerBound.toInt to upperBound.toInt
   
        val rnd = new Random
        val randomInRange = (range(rnd.nextInt(range length)))
//        val rangeMin = double - rangeToCreateDataFromWithin
//        val rangeMax = double + rangeToCreateDataFromWithin
//        val randomInRange = rangeMin - (rangeMin + rangeMax) * random
        addList += randomInRange
      }
      createdData += addList.toArray
      counter += 1
    }
    createdData.prependAll(currentlyKnownData)
    return createdData
  }
}