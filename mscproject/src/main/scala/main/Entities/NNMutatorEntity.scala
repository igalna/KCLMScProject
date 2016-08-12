package main.Entities

import main.traits.Entity
import main.NNBuilder.NNMutator
import org.nd4j.linalg.dataset.DataSet
import scala.collection.mutable.ListBuffer
import main.dl4j.LoadingDataFromCSVToIND
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import main.NNBuilder.NNBuilder
import main.NNBuilder.NNBuilder
import org.nd4j.linalg.api.ndarray.INDArray

class NNMutatorEntity(val name: String, 
                      private var map: Map[String, String],
                      val iterations: Int,
                      val historyToKeep: Int,
                      var nnm: NNMutator,
                      val percentageOfOptimal: Int,
                      val mutateEveryXIteration: Int
                      ) extends Entity {
  
  private var trainingData: ListBuffer[Array[Double]] = new ListBuffer
  private var outcomeList: ListBuffer[Int] = new ListBuffer
  private var dataLoader: LoadingDataFromCSVToIND = new LoadingDataFromCSVToIND
  
  private var first = true
  
  private var optimalSum = 0.0
  private var choiceSum = 0.0
  private var iterNum = 0
  
  val nnb = new NNBuilder
  private var net: MultiLayerNetwork = _
  
  override def receive(t: DataSet, arr: Array[Double]) = {
    val numOutputs = arr.length
    dataLoader.WIDTH = arr.length
    
    optimalSum += arr.toList.max
    
    if (first) {
      net = nnb.buildFromMap(map)
      println
      println("NN at iteration : " + iterNum + " : " + map)
      println
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
      val outputIndex: Int = Entity.findIndexOfHighestValue(outputProbDistribution)
      choiceSum += arr(outputIndex)
      outcomeList += outputIndex
    }
    
    var counter = 0
    if ((iterNum % mutateEveryXIteration) == 0) {
      println("Inside counter == history")
      val opt = optimalSum / iterNum
      val cho = choiceSum / iterNum
      println("Optimal : " + opt)
      println("Choice : " + cho)
      
      val pco = cho * 100 / opt
      //val pco = (cho / opt) * 100
      println("Choice as a percent of Oprimal : " + pco)
      if (pco < percentageOfOptimal) {
        println("Inside choice less than percentOfOptimal")
        map = nnm.mutateFromMap(map)
        println
        println("NN at iteration : " + iterNum + " : " + map)
        println
        net = nnb.buildFromMap(map)
      }
      counter = 0
    }
    counter += 1
    iterNum += 1
  }
  
  override def getListOfIndiciesOfActions: List[Int] = {
    outcomeList.toList
  }
  
  override def getName: String = {
    name
  }
}