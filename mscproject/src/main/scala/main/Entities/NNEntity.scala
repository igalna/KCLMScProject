package main.Entities

import main.traits.Entity
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import scala.collection.mutable.ListBuffer
import main.dl4j.LoadingDataFromCSVToIND
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.DataSet
import com.twelvemonkeys.io.ole2.SIdChain

class NNEntity(val name: String, 
               val net: MultiLayerNetwork, 
               val iterations: Int, 
               val historyToKeep: Int) extends Entity{
  
  private var trainingData: ListBuffer[Array[Double]] = new ListBuffer
  private var outcomeList: ListBuffer[Int] = new ListBuffer
  private var dataLoader: LoadingDataFromCSVToIND = new LoadingDataFromCSVToIND
  
  private var first = true
  
  override def receive(t: DataSet, arr: Array[Double]) = {
    val numOutputs = arr.length
    
    dataLoader.WIDTH = arr.length
    
    if (first) {
      trainingData += arr
      first = false
    }
    else {
      trainingData += arr
      val trainWith = dataLoader.getDataSetFromListBuffer(trainingData)
      (0 until iterations).foreach { x => net.fit(trainWith) }
      if (trainingData.size > historyToKeep) {
        trainingData.remove(0)
      }
      val features: INDArray = t.getFeatureMatrix()
      val labels: INDArray = t.getLabels()
      
      val predicted: INDArray = net.rnnTimeStep(features)
      
      val outputProbDistribution = Array.range(0, numOutputs).map(predicted.getDouble)
      val outputIndex: Int = findIndexOfHighestValue(outputProbDistribution)
      outcomeList += outputIndex
    }
  }
  
  override def getListOfIndiciesOfActions: List[Int] = {
    outcomeList.toList
  }
  
  override def getName: String = {
    name
  }
  
	private def findIndexOfHighestValue(distribution: Array[Double]): Int = {
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