package main.traits

import org.nd4j.linalg.dataset.DataSet

trait Entity {
  
  def receive(t: DataSet, arr: Array[Double])
  def getListOfIndiciesOfActions: List[Int]
  def getName: String
}