package main.Entities

import main.traits.Entity
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import main.DataCreation.Creator
import org.nd4j.linalg.dataset.DataSet
import scala.collection.mutable.ListBuffer
import main.dl4j.DataLoader
import org.nd4j.linalg.api.ndarray.INDArray

class NNCreatorEntity(val name: String, 
                      val net: MultiLayerNetwork, 
                      val iterations: Int, 
                      val historyToKeep: Int,
                      val creator: Creator) extends Entity {
  
  private var trainingData: ListBuffer[Array[Double]] = new ListBuffer
  private var outcomeList: ListBuffer[Int] = new ListBuffer
  private var dataLoader: DataLoader = new DataLoader
  
  private var first = true
  
  override def receive(t: DataSet, arr: Array[Double]) {
    val numOutputs = arr.length
    
    dataLoader.WIDTH = arr.length
    
    if (first) {
      creator.addDataToKnownData(arr)
      first = false
    }
    else {
      creator.addDataToKnownData(arr)
      val trainWith = dataLoader.getDataSetFromListBuffer(
                                 creator.createDataFromAverageOfEachItem)
      (0 until iterations).foreach { x => net.fit(trainWith) }
      //net.clear()
      if (creator.getSizeOfKnownData > historyToKeep) {
        creator.removeDataFromKnownData
      }
      val features: INDArray = t.getFeatureMatrix()
      val labels: INDArray = t.getLabels()
      
      val predicted: INDArray = net.rnnTimeStep(features)
      
      val outputProbDistribution = Array.range(0, numOutputs).map(predicted.getDouble)
      val outputIndex: Int = Entity.findIndexOfHighestValue(outputProbDistribution)
      outcomeList += outputIndex
    }
    
  }
  
  override def getListOfIndiciesOfActions: List[Int] = {
    outcomeList.toList
  }
  
  override def getName: String = {
    name
  }
}