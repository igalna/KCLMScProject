package main.traits

import org.nd4j.linalg.dataset.DataSet

trait Entity {
  
  def receive(t: DataSet, arr: Array[Double])
  def getListOfIndiciesOfActions: List[Int]
  def getName: String
}

object Entity {
	def findIndexOfHighestValue(distribution: Array[Double]): Int = {
		var maxValueIndex: Int = 0
		var maxValue: Double = 0
		Seq.range(0, distribution.length).foreach { i =>
			if(distribution(i) > maxValue) {
				maxValue = distribution(i)
				maxValueIndex = i
			}
		}
		maxValueIndex
  }
}