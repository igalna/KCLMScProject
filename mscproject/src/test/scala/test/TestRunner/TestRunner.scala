package test.TestRunner

import org.scalatest.FlatSpec
import main.dl4j.LoadingDataFromCSVToIND
import main.dl4j.DrawingGraphs
import main.Simulation.Runner
import main.NNBuilder.NNBuilder
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.conf.Updater
import main.Entities.NNEntity

class TestRunner extends FlatSpec {
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
  val smallStocks = "smallTestFileEarliestToLatest.csv"
  
  
  val dataLoader = new LoadingDataFromCSVToIND
  val graphDrawer = new DrawingGraphs
  
  val runner = new Runner(dataLoader, graphDrawer, folderPath + smallStocks)
  
  val numOutputs = runner.numOutputs
  
  "A Runner " should " be able to have it's name set " in {
    //runner.setFileName(folderPath + smallStocks)
  }
  it should " be able to have its entity list set" in {
    val net = getNN
    val entity = new NNEntity("Predictor", net, 20, 30)
    val entityList = List(entity)
    runner.setEntities(entityList)
  }
  it should " be able to run a simulation with one entity" in {
    val net = getNN
    val entity = new NNEntity("Predictor", net, 20, 30)
    val entityList = List(entity)
    runner.setEntities(entityList)
    runner.run
  }
//  it should "be able to run a simulation with two entities" in {
//    val net = getNN
//    val entity = new NNEntity("another", net, 20, 30)
//    val entity1 = new NNEntity("another", net, 1, 5)
//    val list = List(entity, entity1)
//    runner.setEntities(list)
//    runner.run
//  }
  
  private def getNN: MultiLayerNetwork = {
    val conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
      .iterations(1)
      .learningRate(0.001)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .seed(123)
      .biasInit(0)
      .miniBatch(false)
      .updater(Updater.RMSPROP)
      .weightInit(WeightInit.XAVIER)
      .regularization(true)
      .list()
      .layer(0, new GravesLSTM.Builder()
        .nIn(numOutputs)
        .nOut(30)
        .activation("tanh")
        .build())
      .layer(1, new GravesLSTM.Builder()
        .nIn(30)
        .nOut(30)
        .activation("tanh")
        .build())
      .layer(2, new GravesLSTM.Builder()
        .nIn(30)
        .nOut(30)
        .activation("tanh")
        .build())
      .layer(3, new RnnOutputLayer.Builder(LossFunction.MCXENT)
        .nIn(30)
        .nOut(numOutputs)
        .activation("softmax")
        .build())
      .pretrain(false)
      .backprop(true)
      .build()
    
    val net: MultiLayerNetwork = new MultiLayerNetwork(conf)
    net
  }
}