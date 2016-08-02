package main.NNBuilder

import org.deeplearning4j.nn._
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.conf.layers.ActivationLayer.Builder
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit

class NNBuilder {
  def buildFromConf(conf: MultiLayerConfiguration): MultiLayerNetwork = {
    return new MultiLayerNetwork(conf)
  }
  
  def buildFromMap(map: Map[String, Any]): MultiLayerNetwork = {
    val hiddenLayerWidth = map.get("hiddenLayerWidth").get.toString().toInt
    val numHiddenLayers = map.get("numHiddenLayers").get.toString().toInt
    
    val numInputs = map.get("numInputs").get.toString().toInt
    val numOutputs = map.get("numOutputs").get.toString().toInt
    
    val iterations = map.get("iterations").get.toString().toInt
    val learningRate = map.get("learningRate").get.toString().toInt
    val optimizationAlgo = OptimizationAlgorithm.valueOf(map.get("optimizationAlgo").get.toString())
    val seed = map.get("seed").get.toString().toInt
    val biasInit = map.get("biasInit").get.toString().toInt
    val miniBatch = map.get("miniBatch").get.toString().toBoolean
    val updater = Updater.valueOf(map.get("updater").get.toString)
    val weightInit = WeightInit.valueOf(map.get("weightInit").get.toString())
    
    val builder = new NeuralNetConfiguration.Builder()
      .iterations(iterations)
      .learningRate(learningRate)
      .optimizationAlgo(optimizationAlgo)
      .seed(seed)
      .biasInit(biasInit)
      .miniBatch(miniBatch)
      .updater(updater)
      .weightInit(weightInit)
    
    val numLayers = map.get("numLayers").get.toString.toInt
    val layerType = map.get("layerType").get.toString()
    
    val mlconf: MultiLayerConfiguration = listBuilder.build()
    return new MultiLayerNetwork(mlconf)
  }
  
  
  def getLayersFromList(builder: NeuralNetConfiguration.Builder, numHiddenLayers: Int, layers: List[Map[String, String]]): ListBuilder = {
    val listBuilder: ListBuilder = builder.list(numHiddenLayers + 1)
    for (layer <- layers) {
      val layerType = layerFactory(layer.get("layerType").get)
      val activation = layer.get("activation").get
      val nIn = layer.get("nIn").get.toInt
      val nOut = layer.get("nOut").get.toInt
      
     //layerType.Builder()
    }
    return listBuilder
  }
  private def layerFactory(string: String): Any = {
    string match {
      case "graveslstm"     => return new GravesLSTM.Builder()
      case "rnnoutputlayer" => return new RnnOutputLayer.Builder()
    }
  }
}