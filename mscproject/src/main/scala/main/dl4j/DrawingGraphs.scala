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
      case 5 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2), sequences(3), sequences(4))
      case 6 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2), sequences(3), sequences(4), sequences(5))
      case 7 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2), sequences(3), sequences(4), sequences(5), sequences(6))
      case 8 => drawGraphFromSequences(title, sequences(0), sequences(1), sequences(2), sequences(3), sequences(4), sequences(5), sequences(6), sequences(7))
    }
  }
  private def drawGraphFromSequences(title: String, seq1: NamedSequence,
                                                    seq2: NamedSequence) = {
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
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, 
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence) = {
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
  private def drawGraphFromSequences(title: String, seq1: NamedSequence,
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence, 
                                                    seq4: NamedSequence) = {
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
  
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, 
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence, 
                                                    seq4: NamedSequence, 
                                                    seq5: NamedSequence) = {
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
                         color = Color.Purple),
                     Y(seq5.seq, seq5.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.DarkGreen))
        , title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, 
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence, 
                                                    seq4: NamedSequence, 
                                                    seq5: NamedSequence,
                                                    seq6: NamedSequence) = {
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
                         color = Color.Purple),
                     Y(seq5.seq, seq5.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.DarkGreen),
                     Y(seq6.seq, seq6.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Magenta))
        , title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, 
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence, 
                                                    seq4: NamedSequence, 
                                                    seq5: NamedSequence,
                                                    seq6: NamedSequence,
                                                    seq7: NamedSequence) = {
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
                         color = Color.Purple),
                     Y(seq5.seq, seq5.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.DarkGreen),
                     Y(seq6.seq, seq6.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Magenta),
                     Y(seq7.seq, seq7.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Black))
        , title = title
        ,showLegend = true
        ,x = Axis(label = "Time Step")
        ,y = Axis(label = "Fluctuation")))
  }
  
  private def drawGraphFromSequences(title: String, seq1: NamedSequence, 
                                                    seq2: NamedSequence, 
                                                    seq3: NamedSequence, 
                                                    seq4: NamedSequence, 
                                                    seq5: NamedSequence,
                                                    seq6: NamedSequence,
                                                    seq7: NamedSequence,
                                                    seq8: NamedSequence) = {
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
                         color = Color.Purple),
                     Y(seq5.seq, seq5.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.DarkGreen),
                     Y(seq6.seq, seq6.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Magenta),
                     Y(seq7.seq, seq7.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Black),
                     Y(seq8.seq, seq8.name,
                         style = XYPlotStyle.LinesPoints,
                         color = Color.Grey))
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