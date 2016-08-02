package test.DataCreation

import org.scalatest.FlatSpec
import main.dl4j.DrawingGraphs
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.nd4j.linalg.api.ndarray.INDArray
import main.dl4j.LoadingDataFromCSVToIND
import main.dl4j.DrawingGraphs
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import scala.collection.mutable.ListBuffer
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.nd4j.linalg.dataset.DataSet
import org.deeplearning4j.nn.conf.Updater
import main.dl4j.DrawingGraphs
import main.dl4j.DrawingGraphs
import main.dl4j.NamedSequence

class TestDrawingGraphs extends FlatSpec{
  
  val dg = new DrawingGraphs
  
  val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
    
    val currenciesTestFileName = "currenciesEarliestToLatest.csv"
    val currenciesTrainingFileName = "currenciesLargeEarliestToLatest.csv"
    val smallCurrencies = "currenciesSmallEarliestToLatest.csv"
    
    val stocksTestFileName = "veryLargeTestFileEarliestToLatest.csv"
    val stocksTrainingFileName = "mediumTestFileEarliestToLatest.csv"
    val smallStocks = "smallTestFileEarliestToLatest.csv"
    
    val dataLoader = new LoadingDataFromCSVToIND
    val trainingData = dataLoader.getDataSetFromCSV(folderPath + stocksTrainingFileName)
    
    val testLoader = new LoadingDataFromCSVToIND
    val testData = testLoader.getDataSetFromCSV(folderPath + stocksTestFileName)
    var testIter = testData.iterator()
    
    val optimal: Seq[Double] = testLoader.optimalAtTimeStep.toSeq
    val indexOfOptimal: Seq[Int] = testLoader.indexOfOptimalAtTimeStep.toSeq
    val differences = testLoader.arraysOfDifferences
    
    val size = dataLoader.SIZE_OF_DATA
    val numOutputs = dataLoader.WIDTH
    
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
    (0 until 100).foreach(_ => net.fit(trainingData))
    //net.fit(trainingData)
    
    val eval = new Evaluation(numOutputs)
    var outcomeList: ListBuffer[Int] = new ListBuffer
    
    while (testIter.hasNext()) {
      val t: DataSet = testIter.next()
      val features: INDArray = t.getFeatureMatrix()
      val labels: INDArray = t.getLabels()
      val predicted: INDArray = net.rnnTimeStep(features)
      val outputProbDistribution = Array.range(0, numOutputs).map(predicted.getDouble)
      val outputIndex: Int = dg.findIndexOfHighestValue(outputProbDistribution)
      outcomeList += outputIndex
    }
    
    var predictionValues: ListBuffer[Double] = new ListBuffer
    var averageValues: ListBuffer[Double] = new ListBuffer
    var counter = 0
    for (outcome <- outcomeList) {
      val diff = differences(counter)(outcome)
      val average = differences(counter).sum / differences(counter).length
      predictionValues += diff
      averageValues += average
      counter += 1
    }
    
    counter = 0
    var correct = 0
    for (x <- outcomeList) {
      if (outcomeList(counter) == indexOfOptimal(counter)) {
        correct += 1
      }
      counter += 1
    }
    
    println("Total correct Predictions : " + correct + " out of : " + (outcomeList.size-1))
    println("Sum of Values for Optimal : " + optimal.sum)
    println("Sum of Values for Prediction : " + predictionValues.sum)
    println("Sum of Values for Average : " + averageValues.sum)
    println("Prediction as a percentage of Optimal : " + predictionValues.sum * 100 / optimal.sum)
    println("Average as a percentage of Optimal : " + averageValues.sum * 100 / optimal.sum)
    
    val pre: Seq[Double] = predictionValues.toSeq
    val avg: Seq[Double] = averageValues.toSeq
    
    val predictedAsPercentageOfOptimal = dg.sequenceAsPercentageOfAnother(pre, optimal)
    val averageAsPercentageOfOptimal = dg.sequenceAsPercentageOfAnother(avg, optimal)
    
    
    val y = (1 until optimal.length+1).map { _.toDouble }
    
//    val a = new NamedSequence("Predicted", pre)
//    val b = new NamedSequence("Average", avg)
//    val c = new NamedSequence("Optimal", optimal)
//    dg.drawGraphFromSequences(List(a, b, c))
  
    "A graph Drawer" should " draw graphs with two sequence" in {
      val a = new NamedSequence("Predicted", predictedAsPercentageOfOptimal)
      val b = new NamedSequence("Average", averageAsPercentageOfOptimal)
      dg.drawGraphFromSequences("Two sequence graph",List(a, b))
    }
    it should " draw graphs with three sequences" in {
      val a = new NamedSequence("Predicted", pre)
      val b = new NamedSequence("Average", avg)
      val c = new NamedSequence("Optimal", optimal)
      dg.drawGraphFromSequences("Three sequence graph", List(a, b, c))
    }
}