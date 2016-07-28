package main.dl4j

import java.io.File
import org.canova.api.records.reader.SequenceRecordReader
import org.canova.api.records.reader.impl.CSVSequenceRecordReader
import org.canova.api.split.FileSplit
import org.deeplearning4j.datasets.canova.SequenceRecordReaderDataSetIterator
import org.deeplearning4j.datasets.iterator.DataSetIterator
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.GravesLSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.Updater
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.BackpropType
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.deeplearning4j.eval.Evaluation
import org.nd4j.linalg.dataset.DataSet
import org.canova.api.records.reader.RecordReader
import org.canova.api.records.reader.impl.CSVRecordReader
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator
import java.io.PrintWriter
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.datavec.api.records.reader.BaseRecordReader
import org.datavec.api.conf.Configuration
import org.datavec.api.records.reader.SequenceRecordReader
import org.datavec.api.split.InputSplit
import org.datavec.api.writable.Writable
import org.datavec.api.records.reader.impl.collection.CollectionSequenceRecordReader
import scala.collection.mutable.ListBuffer
import scala.util.Random


object TimeSeries {
  
  def main(args: Array[String]) {
  
    val folderPath = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\dl4j\\"
    
    val currenciesTestFileName = "currenciesEarliestToLatest.csv"
    val currenciesTrainingFileName = "currenciesLargeEarliestToLatest.csv"
    val smallCurrencies = "currenciesSmallEarliestToLatest.csv"
    
    val stocksTestFileName = "veryLargeTestFileEarliestToLatest.csv"
    val stocksTrainingFileName = "mediumTestFileEarliestToLatest.csv"
    
    val dataLoader = new LoadingDataFromCSVToIND
    
    val trainingData = dataLoader.getDataSetFromCSV(folderPath + smallCurrencies)
    
    val testData = dataLoader.getDataSetFromCSV(folderPath + smallCurrencies)
    var testIter = testData.iterator()

//    while(testIter.hasNext()) {
//      val next = testIter.next()
//      println(next.outcome())
//    }
    
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
//      .backpropType(BackpropType.TruncatedBPTT)
//      .tBPTTForwardLength(10)
//      .tBPTTBackwardLength(10)
      .backprop(true)
      .build()
      
    val net: MultiLayerNetwork = new MultiLayerNetwork(conf)
    net.fit(trainingData)
    
//    var list: ListBuffer[Array[Double]] = new ListBuffer
//    var anotherList: ListBuffer[Array[Double]] = new ListBuffer
//    
//    val array = Array(1.2, 3.2, 5.4)
//    val array1 = Array(10.4, 30.1, 2.9)
//    anotherList += array
//    anotherList += array1
//        
//    val array2 = Array(332.0, 232.0)
//    list += array2
//    println(list.foreach { x => println(x.foreach { y => print(y + " , ") } )})
//    
//    list.prependAll(anotherList)
//    println(list.foreach { x => println(x.foreach { y => print(y + " , ") } )})

    val rnd = new Random
    val random = rnd.nextDouble()
    println(random)
    
    val array = Array(1.2, 3.2, 5.4, 100)
    val range = 2.0
    
    array.foreach { x => print(x + " , ") }
    println()
    for (doub <- array) {
      val minRange = doub - range
      val maxRange = doub + range
      val randomInRange = (minRange + (maxRange - minRange) * random)
      print(randomInRange + " , ")
    }
    println()
    val num = 20
    val randomValue = (num - range) + ((num + range) - (num - range)) * random
    
    //println(randomValue)
    
//    (0 until 5).foreach { x => 
//        net.fit(trainingData) 
//        println("Epoch " + x)
//        net.rnnClearPreviousState()
//        
//        val t: INDArray = testIter.next().getFeatureMatrix
//        var output: INDArray = net.rnnTimeStep(t)
//        
//        (0 until 15).foreach { j =>
//            val outputProbDistribution = Array.range(0, numOutputs).map(output.getDouble)
//            val selectedIndex: Int = findIndexOfHighestValue(outputProbDistribution)
//            println("Index Chosen " + selectedIndex + " : " + output.getDouble(selectedIndex))
//            
//            val nextInput = Nd4j.zeros(numOutputs)
//            nextInput.putScalar(selectedIndex, 1)
//            output = net.rnnTimeStep(nextInput)
//          }
//        }
    
    
//    println("Evaluate model...")
//    val eval = new Evaluation(numOutputs)
//    
//    var counter = 0
//    while (testIter.hasNext()) {
//      val t: DataSet = testIter.next()
//      val features: INDArray = t.getFeatureMatrix()
//      val labels: INDArray = t.getLabels()
//      val predicted: INDArray = net.rnnTimeStep(features)
//      println("Time step : " + counter)
//      println(" Features : " + features.getRow(0))
//      println("Time step : " + counter)
//      println(" Predicted : " + predicted.getRow(0))
//      counter += 1
//      eval.eval(labels, predicted)
//    }
//    println(eval.stats())
    
    //---------- Classifier ------------//
    
//    val anotherConf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
//            .seed(123)
//            .iterations(1)
//            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//            .learningRate(0.001)
//            .updater(Updater.NESTEROVS)
//            .momentum(0.9)
//            .list(2)
//            .layer(0, new DenseLayer.Builder()
//                     .nIn(numOutputs)
//                     .nOut(50)
//                     .weightInit(WeightInit.XAVIER)
//                     .activation("relu")
//                     .build())
//            .layer(1, new DenseLayer.Builder()
//                     .nIn(numOutputs)
//                     .nOut(50)
//                     .weightInit(WeightInit.XAVIER)
//                     .activation("relu")
//                     .build())
//            .layer(2, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
//                     .weightInit(WeightInit.XAVIER)
//                     .activation("softmax")
//                     .weightInit(WeightInit.XAVIER)
//                     .nIn(50)
//                     .nOut(numOutputs)
//                     .build())
//            .pretrain(false)
//            .backprop(true)
//            .build()
//              
//  val model = new MultiLayerNetwork(conf)
//  model.init()
//  model.fit(trainingData)
//  
//  println("Evaluating Classifier...")
//  testIter = testData.iterator()
//  val classifier = new Evaluation(numOutputs)
//  while (testIter.hasNext()) {
//      val t: DataSet = testIter.next()
//      val features: INDArray = t.getFeatureMatrix()
//      val labels: INDArray = t.getLabels()
//      val predicted: INDArray = model.output(features)
//      classifier.eval(labels, predicted)
//    }
//    println(classifier.stats())
  }
  
  
	private def findIndexOfHighestValue(distribution: Array[Double]): Int = {
		var maxValueIndex: Int = 0
		var maxValue: Double = 0
		Seq.range(0, distribution.length).foreach { i =>
			if(distribution(i) > maxValue) {
				maxValue = distribution(i)
				maxValueIndex = i
			}
		}
		maxValueIndex
  }
}

