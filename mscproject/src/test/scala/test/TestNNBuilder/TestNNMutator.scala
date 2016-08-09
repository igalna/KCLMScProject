package test.TestNNBuilder

import org.scalatest.FlatSpec
import main.NNBuilder.NNMutator
import main.NNBuilder.NNBuilder
import main.NNBuilder.NNBuilder
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

class TestNNMutator extends FlatSpec {
  
  val NNBuilder = new NNBuilder
  val NNMutator = new NNMutator(100)
  val map = getMap
  
  val mutatedMap = NNMutator.mutateFromMap(map)
  val mutant = NNMutator.mutateFromMap(mutatedMap)
  val secondGenerationMutant = NNMutator.mutateFromMap(mutant)

  
  "A mutated map" should " be different from it's source" in {
    println(map)
    println(mutatedMap)
    
  }
  it should "be able to create an NN from a mutated map" in {
    val x = NNBuilder.buildFromMap(mutant)
    assert(x.isInstanceOf[MultiLayerNetwork])
  }
  it should "be able to create NN from second generation mutated maps" in {
    val secondGenerationNet = NNBuilder.buildFromMap(secondGenerationMutant)
    assert(secondGenerationNet.isInstanceOf[MultiLayerNetwork])
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
    val momentum = "0.01"
    map += ("momentum" -> momentum)
    

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