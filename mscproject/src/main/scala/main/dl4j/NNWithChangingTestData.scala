package main.dl4j

import scala.collection.mutable.ListBuffer
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.DataSet
import main.DataCreation.Creator

object NNWithChangingTestData {
  
  def main(args: Array[String]) {
    
    val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
    
    val smallStocks = "smallTestFileEarliestToLatest.csv"
    val stocksTestFileName = "veryLargeTestFileEarliestToLatest.csv"
    val stocksTrainingFileName = "mediumTestFileEarliestToLatest.csv"
    
    val smallCurrencies = "currenciesSmallEarliestToLatest.csv"
    val currenciesTestFileName = "currenciesEarliestToLatest.csv"
    
    val stocksThenCurrencies = "stocksThenCurrencies.csv"
    val currenciesThenStocks = "currenciesThenStocks.csv"
    
    val dataLoader = new LoadingDataFromCSVToIND
    val dg = new DrawingGraphs
    
    val test = dataLoader.getDataSetFromCSV(folderPath + stocksThenCurrencies)
    val testData = dataLoader.dataFromFile
    var testIter = test.iterator()
    
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
    val creatorNet: MultiLayerNetwork = new MultiLayerNetwork(conf)
    
    val creator: Creator = new Creator
    creator.setNumberOfDataItemsToCreate(500)
    creator.setRangeToCreateDataFromWithin(1.5)
    
    var outcomeList: ListBuffer[Int] = new ListBuffer
    var creatorOutcomeList: ListBuffer[Int] = new ListBuffer
    
    var trainingData = new ListBuffer[Array[Double]]
    //var creatorTrainingData = new ListBuffer[Array[Double]]
    
    var first = true
    var counter = 0
    
    while (testIter.hasNext()) {
      val t: DataSet = testIter.next()
      val row = testData(counter)
      println("Test iteration : " + counter)
      counter += 1
      
      println("Training Data size : " + trainingData.size)
      if (first) {
        trainingData += row
        creator.addDataToKnownData(row)
        first = false
      }
      else {
        trainingData += row
        creator.addDataToKnownData(row)
        val trainWith = dataLoader.getDataSetFromListBuffer(trainingData)
        val creatorTrainWith = dataLoader.getDataSetFromListBuffer(
                               creator.createDataFromAverageOfEachItem)
        //(0 until 20).foreach {_ => net.fit(trainWith)
        //                          creatorNet.fit(creatorTrainWith)}
        net.fit(trainWith)
        creatorNet.fit(creatorTrainWith)
        if (trainingData.size > 30) {
          trainingData.remove(0)
          creator.removeDataFromKnownData
        }
        val features: INDArray = t.getFeatureMatrix()
        val labels: INDArray = t.getLabels()
        
        val predicted: INDArray = net.rnnTimeStep(features)
        val creatorPredicted: INDArray = creatorNet.rnnTimeStep(features)
        
        val outputProbDistribution = Array.range(0, numOutputs).map(predicted.getDouble)
        val creatorOutputProbDistribution = Array.range(0, numOutputs).map(creatorPredicted.getDouble)
        
        val outputIndex: Int = dg.findIndexOfHighestValue(outputProbDistribution)
        val creatorOutputIndex: Int = dg.findIndexOfHighestValue(creatorOutputProbDistribution)
        
        outcomeList += outputIndex
        creatorOutcomeList += creatorOutputIndex
      }
    }
    
    val optimal = dataLoader.optimalAtTimeStep
    val indexOfOptimal: Seq[Int] = dataLoader.indexOfOptimalAtTimeStep.toSeq
    val differences = dataLoader.arraysOfDifferences
    
    
    var predictionValues: ListBuffer[Double] = new ListBuffer
    var creatorPredictionValues:ListBuffer[Double] = new ListBuffer
    var averageValues: ListBuffer[Double] = new ListBuffer
    counter = 0
    for (outcome <- outcomeList) {
      val diff = differences(counter)(outcome)
      val creatorDiff = differences(counter)(creatorOutcomeList(counter))
      val average = differences(counter).sum / differences(counter).length
      predictionValues += diff
      creatorPredictionValues += creatorDiff
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
    
    counter = 0
    var creatorCorrect = 0
    for (x <- creatorOutcomeList) {
      if (creatorOutcomeList(counter) == indexOfOptimal(counter)) {
        creatorCorrect += 1
      }
      counter += 1
    }
    
    println("Total correct Predictions : " + correct + " out of : " + (outcomeList.size-1))
    println("Creator correct Predictions : " + creatorCorrect + " out of : " + (creatorOutcomeList.size-1))
    println("Sum of Values for Optimal : " + optimal.sum)
    println("Sum of Values for Prediction : " + predictionValues.sum)
    println("Sum of Values for Creator Prediction : " + creatorPredictionValues.sum)
    println("Sum of Values for Average : " + averageValues.sum)
    println("Prediction as a percentage of Optimal : " + predictionValues.sum * 100 / optimal.sum)
    println("Creator Prediction as a precentage of Optimal : " + creatorPredictionValues.sum * 100 / optimal.sum)
    println("Creator Prediction as a percentage of Prediction : " + creatorPredictionValues.sum * 100 / predictionValues.sum)
    println("Average as a percentage of Optimal : " + averageValues.sum * 100 / optimal.sum)
    
    val pre: Seq[Double] = predictionValues.toSeq
    val predictedNS = new NamedSequence("Predicted",pre)
    
    val creatorPre: Seq[Double] = creatorPredictionValues.toSeq
    val creatorPreNS = new NamedSequence("Creator Predicted", creatorPre)
    
    val avg: Seq[Double] = averageValues.toSeq
    val avgNS = new NamedSequence("average", avg)
    
    val optimalNS = new NamedSequence("Optimal", optimal.take(optimal.length-1))
    
    val predictedAsPercentageOfOptimal = dg.sequenceAsPercentageOfAnother(pre, optimal)
    val predictedAsPercentageNS = new NamedSequence("Predicted", predictedAsPercentageOfOptimal)
    
    val creatorPreAsPercentageOfOptimal = dg.sequenceAsPercentageOfAnother(creatorPre, optimal)
    val creatorPreAsPercentageNS = new NamedSequence("Creator Predicted", creatorPreAsPercentageOfOptimal)
    
    val averageAsPercentageOfOptimal = dg.sequenceAsPercentageOfAnother(avg, optimal)
    val averageAsPercentageNS = new NamedSequence("Average", averageAsPercentageOfOptimal)
    
    dg.drawGraphFromSequences("Predicted", List(predictedNS, 
                                                avgNS, 
                                                creatorPreNS,
                                                optimalNS))
                                                
    dg.drawGraphFromSequences("As a Percentage Of optimal", List(
                                                 predictedAsPercentageNS,
                                                 averageAsPercentageNS,
                                                 creatorPreAsPercentageNS))
  }
}