package test.TestNNBuilder

import org.scalatest.FlatSpec
import main.NNBuilder.NNMutator

class TestNNMutator extends FlatSpec {
  
  val NNMutator = new NNMutator(5)
  val map = getMap
  
  val mutatedMap = NNMutator.mutateFromMap(map)
  
  "A mutated map" should " be different from it's source" in {
    println(map)
    println(mutatedMap)
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
    
//    val numLayers = "3"
//    map += ("numLayers" -> numLayers)
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
}