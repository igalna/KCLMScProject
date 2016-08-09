package test.TestNNBuilder

import org.scalatest.FlatSpec
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.conf.Updater
import main.NNBuilder.NNBuilder
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

class TestNNBuilder extends FlatSpec{
  
  val nnb = new NNBuilder
  
  val conf = getConf
  val numOutputs = 5
  val map = getMap
  
  
  "An NNBuilder " should " return a Neural Network from a conf " in {
    nnb.buildFromConf(conf).isInstanceOf[MultiLayerNetwork]
  }
  it should " return a Neural Network from a map[Stirng, String]" in {
    val nn = nnb.buildFromMap(map)
    nn.isInstanceOf[MultiLayerNetwork]
  }
  it should "return a NN with 2 layers if it has only an input and output layer" in {
    val nn = nnb.buildFromConf(conf)
    assert(nn.getnLayers == 2)
  }
  
  "Because neural network configuration is 0-n inclusive, an NNBuilder" should
    ", when the number of layers = n, the total layers should be n+1 (+1 for the output layer)" in {
    val nn = nnb.buildFromMap(map)
    val totalLayers = 4
    assert(nn.getnLayers == totalLayers)
  }
  
  def getMap: Map[String, String] = {
    var map: Map[String, String] = Map()
    
    val hiddenLayerWidth = "50"
    map += ("hiddenLayerWidth" -> "50")
    val numHiddenLayers = "3"
    map += ("numHiddenLayers" -> numHiddenLayers)
    
    val numInputs = "30"
    map += ("numInputs" -> numInputs)
    val numOutputs = "30"
    map += ("numOutputs" -> numOutputs)
    
    val iterations = "1"
    map += ("iterations" -> iterations)
    val learningRate = "0.01"
    map += ("learningRate" -> learningRate)
    val optimizationAlgo = "STOCHASTIC_GRADIENT_DESCENT"
    map += ("optimizationAlgo" -> optimizationAlgo)
    val seed = "123"
    map += ("seed" -> seed)
    val biasInit = "0"
    map += ("biasInit" -> biasInit)
    val miniBatch = "false"
    map += ("miniBatch" -> miniBatch)
    val updater = "rmsprop"
    map += ("updater" -> updater)
    val weightInit = "xavier"
    map += ("weightInit" -> weightInit)
    val momentum = "0.1"
    map += ("momentum" -> momentum)
    
    
    val numLayers = "3"
    map += ("numLayers" -> numLayers)
    val layerType = "graveslstm"
    map += ("layerType" -> layerType)
    val activation = "tanh"
    map += ("activation" -> activation)
    
    val lossFunction = "mcxent"
    map += ("lossFunction" -> lossFunction)
    val outputActivation = "softmax"
    map += ("outputActivation" -> outputActivation)
    
    val preTrain = "false"
    map += ("preTrain" -> preTrain)
    val backProp = "true"
    map += ("backProp" -> backProp)
    
    map
  }
  
  def getConf: MultiLayerConfiguration = {
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
      .layer(1, new RnnOutputLayer.Builder(LossFunction.MCXENT)
        .nIn(30)
        .nOut(numOutputs)
        .activation("softmax")
        .build())
      .pretrain(false)
      .backprop(true)
      .build()
    return conf
  }
}