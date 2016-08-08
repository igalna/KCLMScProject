package main.Entities

import main.traits.Entity
import org.nd4j.linalg.dataset.DataSet
import scala.collection.mutable.ListBuffer
import java.util.Random

class RandomEntity(val name: String) extends Entity {
  
  private var outcomeList: ListBuffer[Int] = new ListBuffer
  private var first = true
  
  override def receive(t: DataSet, arr: Array[Double]) {
    if (first) {
      first = false
    }
    else {
      val rand = new Random()
      val range = 0 until arr.length-1
      val idx = range(rand.nextInt(range.length))
      outcomeList += idx
    }
  }
  
  override def getListOfIndiciesOfActions: List[Int] = {
    outcomeList.toList
  }
  
  override def getName: String = {
    name
  }
}