package main.NNBuilder

import java.util.Random
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.weights.WeightInit

class NNMutator(private var numericalMutatePercentageRange: Double) {
  
  def mutateFromMap(attrs: Map[String, String]): Map[String,String] = {
    var replaceWithMap: Map[String, String] = Map()
    
    attrs.foreach{ case(k,v) => replaceWithMap += (matchKVToNewValue(k, v)) }
    
    replaceWithMap
  }
  
  def setNumericalMutatePercentageRange(range: Double) = {
    numericalMutatePercentageRange = range
  }
  
  private def matchKVToNewValue(key: String, value: String): (String,String) = {
    key match {
      case "hiddenLayerWidth" => (key, intMutator(value.toInt))
      case "numHiddenLayers"  => (key, intMutator(value.toInt))
      case "numInputs"        => (key,value)
      case "numOutputs"       => (key,value)
      case "iterations"       => (key, intMutator(value.toInt))
      case "learningRate"     => (key, doubleMutator(value.toDouble))
      case "optimizationAlgo" => (key, stringMutator(key))
      case "seed"             => (key, intMutator(value.toInt))
      case "biasInit"         => (key, value)
      case "miniBatch"        => (key, value)
      case "updater"          => (key, stringMutator(key))
      case "weightInit"       => (key, stringMutator(key))
      case "momentum"         => (key, doubleMutator(value.toDouble))
      case "layerType"        => (key, stringMutator(key))
      case "activation"       => (key, stringMutator(key))
      case "lossFunction"     => (key, stringMutator(key))
      case "outputActivation" => (key, stringMutator(key))
      case "preTrain"         => (key, value)
      case "backProp"         => (key, value)
    }
  }
  
  private def intMutator(previous: Int): String = {
    val whatIsXPercentOfY = (numericalMutatePercentageRange * previous) / 100.0
    
    val upperBound: Int = previous + whatIsXPercentOfY.toInt
    val lowerBound: Int = (if (previous - whatIsXPercentOfY.toInt > 0) {
                            previous - whatIsXPercentOfY.toInt
                          } else 1)
    val range = lowerBound to upperBound
   
    val rnd = new Random
    (range(rnd.nextInt(range length))).toString()
  }
  
  private def doubleMutator(previous: Double): String = {
    val whatIsXPercentOfY = (numericalMutatePercentageRange * previous) / 100.0
    
    val upperBound = previous + whatIsXPercentOfY
    val lowerBound = (if (previous - whatIsXPercentOfY > 0) {
                            previous - whatIsXPercentOfY
                          } else 0.1)
    val range = lowerBound.toInt to upperBound.toInt
   
    val rnd = new Random
    (range(rnd.nextInt(range length))).toString()
  }
  
  private def stringMutator(key: String): String = {
    key match {
      case "weightInit"       => NNMutator.getRandomFromList(NNMutator.listOfWeightInitAlgo)
      case "updater"          => NNMutator.getRandomFromList(NNMutator.listOfUpdaterAlgo)
      case "optimizationAlgo" => NNMutator.getRandomFromList(NNMutator.listOfOptoAlgo)
      case "layerType"        => NNMutator.getRandomFromList(NNMutator.listOfLayerTypes)
      case "activation"       => NNMutator.getRandomFromList(NNMutator.listOfActivationFunctions)
      case "lossFunction"     => NNMutator.getRandomFromList(NNMutator.listOfLossFunctions)
      case "outputActivation" => NNMutator.getRandomFromList(NNMutator.listOfActivationFunctions)
    }
  }
}

object NNMutator {
  def getRandomFromList(list: List[String]): String = {
    val rand = new Random
    list(rand.nextInt(list.size-1))
  }
  val listOfWeightInitAlgo = List("DISTRIBUTION","NORMALIZED",
                                    "RELU","SIZE",
                                    "UNIFORM","VI",
                                    "XAVIER","ZERO")
  val listOfUpdaterAlgo = List("ADADELTA","ADAGRAD",
                                 "ADAM","CUSTOM",
                                 "NESTEROVS","NONE",
                                 "RMSPROP","SGD")
  val listOfOptoAlgo = List("CONJUGATE_GRADIENT", "HESSIAN_FREE",
                              "LBFGS", "LINE_GRADIENT_DESCENT",
                              "STOCHASTIC_GRADIENT_DESCENT")
  val listOfLayerTypes = List("denselayer", "graveslstm")
  
  val listOfActivationFunctions = List("tanh","relu",
                                       "sigmoid","ELU",
                                       "acos","asin",
                                       "atan","ceil",
                                       "cos","exp",
                                       "floor","hardtanh",
                                       "identity","maxout",
                                       "negative","pow",
                                       "round","sign",
                                       "sin","softmax",
                                       "sqrt","stabilize")
  val listOfLossFunctions = List("MSE","EXPLL",
                                 "XENT","MCXENT",
                                 "RMSE_XENT","SQUARED_LOSS",
                                 "RECONSTRUCTION_CROSSENTROPY",
                                 "NEGATIVELOGLIKELIHOOD")
}