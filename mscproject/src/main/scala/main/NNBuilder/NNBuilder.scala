package main.NNBuilder

import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.conf.MultiLayerConfiguration

class NNBuilder {
  def buildFromConf(conf: NeuralNetConfiguration): MultiLayerNetwork = {
    return new MultiLayerNetwork(conf.asInstanceOf[MultiLayerConfiguration])
  }
}