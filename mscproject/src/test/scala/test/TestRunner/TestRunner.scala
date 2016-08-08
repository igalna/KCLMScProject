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
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.conf.layers.DenseLayer
import main.Entities.NNEntity
import main.Entities.RandomEntity
import main.Entities.NNCreatorEntity
import main.traits.Entity
import main.DataCreation.Creator
import main.Entities.BestFromLastIterationEntity

class TestRunner extends FlatSpec {
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
  val smallStocks = "smallTestFileEarliestToLatest.csv"
  val smallCurrencies = "currenciesSmallEarliestToLatest.csv"
  val stocksThenCurrencies = "stocksThenCurrencies.csv"
  val currenciesThenStocks = "currenciesThenStocks.csv"
  val blockSize50Intermixed = "unequalLengthTest.csv"
  val blockSize10Length500StocksCurrencies = "blockSize10Length500StocksCurrencies.csv"
  val blockSize10Length500CurrenciesStocks = "blockSize10Length500StocksCurrencies.csv"
  
  val dataLoader = new LoadingDataFromCSVToIND
  val graphDrawer = new DrawingGraphs
  
  val runner = new Runner(dataLoader, graphDrawer, folderPath + blockSize10Length500StocksCurrencies)
  
  var numOutputs = runner.numOutputs
  
//  "A Runner " should " be able to have it's name set " in {
//    //runner.setFileName(folderPath + smallStocks)
//  }
//  it should " be able to have its entity list set" in {
//    val net = getNN
//    val entity = new NNEntity("Predictor", net, 20, 30)
//    val entityList = List(entity)
//    runner.setEntities(entityList)
//  }
//  it should " be able to run a simulation with one entity" in {
//    val net = getNN
//    val entity = new NNEntity("Predictor", net, 20, 30)
//    val entityList = List(entity)
//    runner.setEntities(entityList)
//    runner.run
//  }
  
//  "A Runner " should " be able to have two entities" in {
//    val entity1 = new NNEntity("Entity1", getNN, 20, 30)
//    
//    val creator = new Creator
//    creator.setNumberOfDataItemsToCreate(500)
//    creator.setRangeToCreateDataFromWithin(1.0)
//    
//    val creatorEntity = new NNCreatorEntity("Creator Entity", getNN,
//                                            20,
//                                            5,
//                                            creator)
//    val entityList = List(entity1, creatorEntity)
//    runner.setEntities(entityList)
//    runner.run
//  }
//  
//  "A Runner" should " be able to use Random Entities" in {
//    val randomEntity = new RandomEntity("Mr. Random")
//    val entityList = List(randomEntity)
//    
//    runner.setEntities(entityList)
//    runner.run
//  }
  
//  "A Runner" should " work with tit for tat entities" in {
//    val tft = new BestFromLastIterationEntity("tit for tat")
//    val entityList = List(tft)
//    
//    runner.setEntities(entityList)
//    runner.run
//  }
  
  "A Runner" should " have multiple entities" in {
    val tft = new BestFromLastIterationEntity("tit for tat")
    val randomEntity = new RandomEntity("Mr. Random")
    val entity = new NNEntity("Predictor", getNN, 20, 30)
    val creator = new Creator
    creator.setNumberOfDataItemsToCreate(500)
    creator.setRangeToCreateDataFromWithin(1.5)
    
    val creatorEntity = new NNCreatorEntity("Creator Entity", getNN,
                                            20,
                                            7,
                                            creator)
    
    val classifierEntity = new NNEntity("Classifier", getClassifier, 20, 30)
    
    val entityList:List[Entity] = List(tft, randomEntity, entity, creatorEntity)
    
    runner.setEntities(entityList)
    runner.run
  }
  
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
  
  private def getClassifier: MultiLayerNetwork = {
    val conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
            .seed(123)
            .iterations(1)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .learningRate(0.001)
            .updater(Updater.NESTEROVS)
            .momentum(0.9)
            .list(2)
            .layer(0, new DenseLayer.Builder()
                     .nIn(numOutputs)
                     .nOut(30)
                     .weightInit(WeightInit.XAVIER)
                     .activation("relu")
                     .build())
            .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                     .weightInit(WeightInit.XAVIER)
                     .activation("softmax")
                     .weightInit(WeightInit.XAVIER)
                     .nIn(30)
                     .nOut(numOutputs)
                     .build())
            .pretrain(false)
            .backprop(true)
            .build()
              
    val model = new MultiLayerNetwork(conf)
    model
  }
}