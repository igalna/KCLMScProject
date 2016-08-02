package main.dl4j

import org.sameersingh.scalaplot.Implicits._
import org.sameersingh.scalaplot.XYPlotStyle.Type
import org.sameersingh.scalaplot.XYPlotStyle.Value
import org.sameersingh.scalaplot.XYPlotStyle
import org.sameersingh.scalaplot.Style.Color._
import org.sameersingh.scalaplot.Style.Color
import org.sameersingh.scalaplot.Style.Color.Type
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import scala.collection.mutable.ListBuffer
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

  
class DrawingGraphs {
  
  def drawGraphFromSequences(title: String, sequences: List[NamedSequence]) {
    val size = sequences.size
    size match {
      case 2 => drawGraphFromSequences(title, sequences(0), sequences(1))
      case 3 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2))
      case 4 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2), sequences(3))
    }
  }
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, seq2: NamedSequence) = {
    val y = (1 until seq1.seq.size+1).map { _.toDouble }
    output(GUI,
    xyChart(y -> Seq(
                     Y(seq1.seq, seq1.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Red),
                     Y(seq2.seq, seq2.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Mustard))
        ,title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, seq2: NamedSequence, seq3: NamedSequence) = {
    val y = (1 until seq1.seq.size+1).map { _.toDouble }
    output(GUI,
    xyChart(y -> Seq(
                     Y(seq1.seq, seq1.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Red),
                     Y(seq2.seq, seq2.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Mustard),
                     Y(seq3.seq, seq3.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Blue))
        , title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, seq2: NamedSequence, seq3: NamedSequence, seq4: NamedSequence) = {
    val y = (1 until seq1.seq.size+1).map { _.toDouble }
    output(GUI,
    xyChart(y -> Seq(
                     Y(seq1.seq, seq1.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Red),
                     Y(seq2.seq, seq2.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Mustard),
                     Y(seq3.seq, seq3.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Blue),
                     Y(seq4.seq, seq4.name, 
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Purple))
        , title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  
  def sequenceAsPercentageOfAnother(seq: Seq[Double], optimal: Seq[Double] ): Seq[Double] = {
    val zipped = seq.zip(optimal)
    val asPercentageOf = zipped.map( x => x._1 * 100 / x._2)
    asPercentageOf
  }
  
	def findIndexOfHighestValue(distribution: Array[Double]): Int = {
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

//object DrawingGraphs {
//  def main(args: Array[String]) {
//    
//    val folderPath = "C:/Users/igaln/Documents/King's stuff/King's MSc project/Data/Trading/test data/dl4j/"
//    
//    val currenciesTestFileName = "currenciesEarliestToLatest.csv"
//    val currenciesTrainingFileName = "currenciesLargeEarliestToLatest.csv"
//    val smallCurrencies = "currenciesSmallEarliestToLatest.csv"
//    
//    val stocksTestFileName = "veryLargeTestFileEarliestToLatest.csv"
//    val stocksTrainingFileName = "mediumTestFileEarliestToLatest.csv"
//    val smallStocks = "smallTestFileEarliestToLatest.csv"
//    
//    val dataLoader = new LoadingDataFromCSVToIND
//    val trainingData = dataLoader.getDataSetFromCSV(folderPath + stocksTrainingFileName)
//    
//    val testLoader = new LoadingDataFromCSVToIND
//    val testData = testLoader.getDataSetFromCSV(folderPath + stocksTestFileName)
//    var testIter = testData.iterator()
//    
//    val optimal: Seq[Double] = testLoader.optimalAtTimeStep.toSeq
//    val indexOfOptimal: Seq[Int] = testLoader.indexOfOptimalAtTimeStep.toSeq
//    val differences = testLoader.arraysOfDifferences
//    
//    val size = dataLoader.SIZE_OF_DATA
//    val numOutputs = dataLoader.WIDTH
//    
//    val conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
//      .iterations(1)
//      .learningRate(0.001)
//      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//      .seed(123)
//      .biasInit(0)
//      .miniBatch(false)
//      .updater(Updater.RMSPROP)
//      .weightInit(WeightInit.XAVIER)
//      .regularization(true)
//      .list()
//      .layer(0, new GravesLSTM.Builder()
//        .nIn(numOutputs)
//        .nOut(30)
//        .activation("tanh")
//        .build())
//      .layer(1, new GravesLSTM.Builder()
//        .nIn(30)
//        .nOut(30)
//        .activation("tanh")
//        .build())
//      .layer(2, new GravesLSTM.Builder()
//        .nIn(30)
//        .nOut(30)
//        .activation("tanh")
//        .build())
//      .layer(3, new RnnOutputLayer.Builder(LossFunction.MCXENT)
//        .nIn(30)
//        .nOut(numOutputs)
//        .activation("softmax")
//        .build())
//      .pretrain(false)
//      .backprop(true)
//      .build()
//    
//    val net: MultiLayerNetwork = new MultiLayerNetwork(conf)
//    (0 until 100).foreach(_ => net.fit(trainingData))
//    //net.fit(trainingData)
//    
//    val eval = new Evaluation(numOutputs)
//    var outcomeList: ListBuffer[Int] = new ListBuffer
//    
//    while (testIter.hasNext()) {
//      val t: DataSet = testIter.next()
//      val features: INDArray = t.getFeatureMatrix()
//      val labels: INDArray = t.getLabels()
//      val predicted: INDArray = net.rnnTimeStep(features)
//      val outputProbDistribution = Array.range(0, numOutputs).map(predicted.getDouble)
//      val outputIndex: Int = findIndexOfHighestValue(outputProbDistribution)
//      outcomeList += outputIndex
//    }
//    
//    var predictionValues: ListBuffer[Double] = new ListBuffer
//    var averageValues: ListBuffer[Double] = new ListBuffer
//    var counter = 0
//    for (outcome <- outcomeList) {
//      val diff = differences(counter)(outcome)
//      val average = differences(counter).sum / differences(counter).length
//      predictionValues += diff
//      averageValues += average
//      counter += 1
//    }
//    
//    counter = 0
//    var correct = 0
//    for (x <- outcomeList) {
//      if (outcomeList(counter) == indexOfOptimal(counter)) {
//        correct += 1
//      }
//      counter += 1
//    }
//    
//    println("Total correct Predictions : " + correct + " out of : " + (outcomeList.size-1))
//    println("Sum of Values for Optimal : " + optimal.sum)
//    println("Sum of Values for Prediction : " + predictionValues.sum)
//    println("Sum of Values for Average : " + averageValues.sum)
//    println("Prediction as a percentage of Optimal : " + predictionValues.sum * 100 / optimal.sum)
//    println("Average as a percentage of Optimal : " + averageValues.sum * 100 / optimal.sum)
//    
//    val pre: Seq[Double] = predictionValues.toSeq
//    val avg: Seq[Double] = averageValues.toSeq
//    
//    val predictedAsPercentageOfOptimal = sequenceAsPercentageOfAnother(pre, optimal)
//    val averageAsPercentageOfOptimal = sequenceAsPercentageOfAnother(avg, optimal)
//    
//    
//    val y = (1 until optimal.length+1).map { _.toDouble }
//    
//    val gd = new DrawingGraphs
//    val a = new NamedSequence("Predicted", pre)
//    val b = new NamedSequence("Average", avg)
//    val c = new NamedSequence("Optimal", optimal)
//    gd.drawGraphFromSequences(List(a, b, c))
////    output(GUI,
////        xyChart(y -> Seq(
////                         Y(predictedAsPercentageOfOptimal, "PREDICTION", 
////                             style = XYPlotStyle.LinesPoints,
////                             color = Color.Red),
////                         Y(averageAsPercentageOfOptimal, "AVERAGE", 
////                             style = XYPlotStyle.LinesPoints,
////                             color = Color.Mustard))
////            ,showLegend = true
////            ,x = Axis(label = "Time Step")
////            ,y = Axis(label = "Fluctuation")))
////    output(PNG(folderPath, "test"), 
////        xyChart(y -> Seq(Y(y1, "ONE"), Y(y2, "TWO"), Y(y3, "THREE"))))
//    
//  }


//  def sequenceAsPercentageOfAnother(seq: Seq[Double], optimal: Seq[Double] ): Seq[Double] = {
//    val zipped = seq.zip(optimal)
//    val asPercentageOf = zipped.map( x => x._1 * 100 / x._2)
//    asPercentageOf
//  }
//  
//	private def findIndexOfHighestValue(distribution: Array[Double]): Int = {
//		var maxValueIndex: Int = 0
//		var maxValue: Double = 0
//		Seq.range(0, distribution.length).foreach { i =>
//			if(distribution(i) > maxValue) {
//				maxValue = distribution(i)
//				maxValueIndex = i
//			}
//		}
//		maxValueIndex
//  }
//}