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
  
  
  "An NNBuilder " should " return a Neural Network from a conf " in {
    nnb.buildFromConf(conf).isInstanceOf[MultiLayerNetwork]
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