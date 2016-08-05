package main.Simulation

import main.dl4j.DrawingGraphs
import main.dl4j.LoadingDataFromCSVToIND
import main.traits.Entity
import org.nd4j.linalg.dataset.DataSet
import scala.collection.mutable.ListBuffer
import main.dl4j.NamedSequence

class Runner(val dataLoader: LoadingDataFromCSVToIND, val graphDrawer: DrawingGraphs, val fileName: String) {
  
  private var entities: List[Entity] = _
  
  val test = dataLoader.getDataSetFromCSV(fileName)
  val testIter = test.iterator()
  val comparisonData = dataLoader.dataFromFile
  
  val numOutputs = dataLoader.WIDTH
  
  def setEntities(newEntities: List[Entity]) = {
    println("Inside setEntities")
    this.entities = newEntities
  }
  
  def run = {
    println("Inside Run")
    var first = true
    var counter = 0
    
    while (testIter.hasNext()) {
      println("Here")
      val t: DataSet = testIter.next()
      val row = comparisonData(counter)
      counter +=1
      println("Training Data size : " + comparisonData.size)
      if (first) {
        entities.par.foreach { x => x.receive(t ,row) }
        first = false
      }
      else {
        println("now HEre")
        entities.par.foreach { x => x.receive(t, row) }
      }
    }
    val optimal = dataLoader.optimalAtTimeStep
    val indexOfOptimal: Seq[Int] = dataLoader.indexOfOptimalAtTimeStep.toSeq
    val differences = dataLoader.arraysOfDifferences
    
    counter = 0
    var meanValues: ListBuffer[Double] = new ListBuffer
    var medianValues: ListBuffer[Double] = new ListBuffer
    var arrayOfNamedSequences: Array[NamedSequence] = new Array(entities.size +2)
    for (x <- entities) {
      var predictionValues: ListBuffer[Double] = new ListBuffer
      var internalCounter = 0
      val outcomes = x.getListOfIndiciesOfActions
      for (out <- outcomes) {
        val diff = differences(internalCounter)(out)
        predictionValues += diff
        if (internalCounter == 0) {
          val mean = differences(internalCounter).sum / differences(internalCounter).length
          val median = differences(internalCounter).sortWith(_ < _)(differences.size/2)
          meanValues += mean
          medianValues += median
        }
        internalCounter += 1
      }
      val namedSequence = new NamedSequence(x.getName, predictionValues)
      arrayOfNamedSequences(counter) = namedSequence
      counter += 1
    }
    val meanNamedSequence = new NamedSequence("Mean", meanValues)
    val medianNamedSequence = new NamedSequence("Median", medianValues)
    arrayOfNamedSequences(counter) = meanNamedSequence
    arrayOfNamedSequences(counter+1) = medianNamedSequence
    
    val optimalNamedSequence = new NamedSequence("Optimal", optimal.take(optimal.length-1))
    
    printStats(arrayOfNamedSequences, optimalNamedSequence, indexOfOptimal)
    println("just before drawing Graphs")
    graphDrawer.drawGraphFromSequences("Predicted", arrayOfNamedSequences.toList)
    println("just after drawing graphs")
  }
  
  private def printStats(arr: Array[NamedSequence], opt: NamedSequence, inx: Seq[Int]) = {
    val sumOfOptimal = opt.seq.sum
    val sumOfMean = arr(arr.length-2).seq.sum
    val sumOfMedian = arr(arr.length-1).seq.sum
    
    val length = opt.seq.length -1
    
    println("Sum of Values for Optimal : " + sumOfOptimal)
    println("Sum of Values for Mean : " + sumOfMean)
    println("Sum of Values for Median : " + sumOfMedian)
    println("Mean as a percentage of Optimal : " + sumOfMean * 100 / sumOfOptimal)
    println("Median as a percentage of Optimal : " + sumOfMedian * 100 / sumOfOptimal)
    println()
    val newArr = arr.dropRight(2)
    for (ns <- newArr) {
      val name = ns.name
      val sum = ns.seq.sum
      println("Sum of Values for " + name + " : " + sum)
      println(name + " as a percentage of Optimal : " + sum * 100 / sumOfOptimal)
    }
    
  }
}