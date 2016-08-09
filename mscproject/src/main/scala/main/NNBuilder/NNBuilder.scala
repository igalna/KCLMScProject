package main.NNBuilder

import org.deeplearning4j.nn._
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.nn.conf.layers.OutputLayer

class NNBuilder {
  
  def buildFromConf(conf: MultiLayerConfiguration): MultiLayerNetwork = {
    return new MultiLayerNetwork(conf)
  }
  
  def buildFromMap(map: Map[String, String]): MultiLayerNetwork = {
    val hiddenLayerWidth = map.get("hiddenLayerWidth").get.toString().toInt
    val numHiddenLayers = map.get("numHiddenLayers").get.toString().toInt -1
    
    val numInputs = map.get("numInputs").get.toString().toInt
    val numOutputs = map.get("numOutputs").get.toString().toInt
    
    val iterations = map.get("iterations").get.toString().toInt
    val learningRate = map.get("learningRate").get.toString().toDouble
    val optimizationAlgo = OptimizationAlgorithm.valueOf(map.get("optimizationAlgo").get.toUpperCase())
    val seed = map.get("seed").get.toString().toInt
    val biasInit = map.get("biasInit").get.toString().toInt
    val miniBatch = map.get("miniBatch").get.toString().toBoolean
    val updater = Updater.valueOf(map.get("updater").get.toUpperCase())
    val weightInit = WeightInit.valueOf(map.get("weightInit").get.toUpperCase())
    val momentum = map.get("momentum").get.toString().toDouble
    
    val builder = new NeuralNetConfiguration.Builder()
      .iterations(iterations)
      .learningRate(learningRate)
      .optimizationAlgo(optimizationAlgo)
      .seed(seed)
      .biasInit(biasInit)
      .miniBatch(miniBatch)
      .updater(updater)
      .weightInit(weightInit)
      .momentum(momentum)
    
    val layerType = map.get("layerType").get
    val activation = map.get("activation").get
    
    val listBuilder: ListBuilder = builder.list(numHiddenLayers + 1)
    
    (0 until numHiddenLayers +1).foreach { x => 
        layerType match {
          case "graveslstm" => listBuilder.layer(x, new GravesLSTM.Builder()
              .nIn(if (x == 0) numInputs else hiddenLayerWidth)
              .nOut(hiddenLayerWidth)
              .activation(activation)
              .build())
          case "denselayer" => listBuilder.layer(x, new DenseLayer.Builder()
              .nIn(if (x == 0) numInputs else hiddenLayerWidth)
              .nOut(hiddenLayerWidth)
              .activation(activation)
              .build())
          }
        }
    
    val lossFunction = LossFunction.valueOf(map.get("lossFunction").get.toUpperCase())
    val outputActivation = map.get("outputActivation").get
    
    layerType match {
      case "graveslstm" => listBuilder.layer(numHiddenLayers +1, new RnnOutputLayer.Builder()
        .activation(outputActivation)
        .nIn(hiddenLayerWidth)
        .nOut(numOutputs)
        .build())
      case "denselayer" => listBuilder.layer(numHiddenLayers +1, new OutputLayer.Builder()
        .nIn(hiddenLayerWidth)
        .nOut(numOutputs)
        .activation(outputActivation)
        .build())
    }
    
    val preTrain = map.get("preTrain").get.toBoolean
    val backProp = map.get("backProp").get.toBoolean
    
    listBuilder.pretrain(preTrain)
               .backprop(backProp)
               .build()
    
    val mlconf: MultiLayerConfiguration = listBuilder.build()
    return new MultiLayerNetwork(mlconf)
  }
}