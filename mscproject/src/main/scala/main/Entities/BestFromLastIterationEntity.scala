package main.Entities

import scala.collection.mutable.ListBuffer

import org.nd4j.linalg.dataset.DataSet

import main.traits.Entity

class BestFromLastIterationEntity(val name: String) extends Entity {
  
  private var outcomeList: ListBuffer[Int] = new ListBuffer
  private var trainingData: Array[Double] = _
  
  private var first = true
  
  override def receive(t: DataSet, arr: Array[Double]) = {
    if (first) {
      trainingData = new Array[Double](arr.length)
      trainingData = arr
      first = false
    }
    else {
      outcomeList += Entity.findIndexOfHighestValue(trainingData)
      trainingData = arr
    }
  }
  
  override def getListOfIndiciesOfActions: List[Int] = {
    outcomeList.toList
  }
  
  override def getName: String = {
    name
  }
  
}