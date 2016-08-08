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
    this.entities = newEntities
  }
  
  def run = {
    var first = true
    var counter = 0
    
    while (testIter.hasNext()) {
      val t: DataSet = testIter.next()
      val row = comparisonData(counter)
      counter +=1
      if (first) {
        entities.foreach { x => x.receive(t ,row) }
        first = false
      }
      else {
        entities.foreach { x => x.receive(t, row) }
      }
    }
    val optimal = dataLoader.optimalAtTimeStep
    val indexOfOptimal: Seq[Int] = dataLoader.indexOfOptimalAtTimeStep.toSeq
    val differences = dataLoader.arraysOfDifferences
    
    counter = 0
    var meanValues: ListBuffer[Double] = new ListBuffer
    var medianValues: ListBuffer[Double] = new ListBuffer
    var arrayOfNamedSequences: Array[NamedSequence] = new Array(entities.size +3)
    var arrayOfNSAsAPercentageOfOptimal: Array[NamedSequence] = new Array(entities.size +2)
    for (x <- entities) {
      var predictionValues: ListBuffer[Double] = new ListBuffer
      var internalCounter = 0
      val outcomes = x.getListOfIndiciesOfActions
      for (out <- outcomes) {
        val diff = differences(internalCounter)(out)
        predictionValues += diff
        if (counter == 0) {
          val mean = differences(internalCounter).sum / differences(internalCounter).length
          val m = differences(internalCounter).sortWith(_ < _)
          val median = m(m.size/2)
          meanValues += mean
          medianValues += median
        }
        internalCounter += 1
      }
      val namedSequence = new NamedSequence(x.getName, predictionValues)
      arrayOfNamedSequences(counter) = namedSequence
      val asAPercentageOfOptimal = new NamedSequence(x.getName, graphDrawer.sequenceAsPercentageOfAnother(predictionValues, optimal))
      arrayOfNSAsAPercentageOfOptimal(counter) = asAPercentageOfOptimal
      counter += 1
    }
    val meanNamedSequence = new NamedSequence("Mean", meanValues)
    val medianNamedSequence = new NamedSequence("Median", medianValues)
    val meanAsPercentageOfOptimal = new NamedSequence("Mean", graphDrawer.sequenceAsPercentageOfAnother(meanValues, optimal))
    val medianAsPercentageOfOptimal = new NamedSequence("Median", graphDrawer.sequenceAsPercentageOfAnother(medianValues, optimal))
    arrayOfNamedSequences(counter) = meanNamedSequence
    arrayOfNamedSequences(counter + 1) = medianNamedSequence
    arrayOfNSAsAPercentageOfOptimal(counter) = meanAsPercentageOfOptimal
    arrayOfNSAsAPercentageOfOptimal(counter + 1) = medianAsPercentageOfOptimal
    
    val optimalNamedSequence = new NamedSequence("Optimal", optimal.take(optimal.length-1))
    arrayOfNamedSequences(counter + 2) = optimalNamedSequence
    
    printStats(arrayOfNamedSequences, optimalNamedSequence, indexOfOptimal)
    val list = arrayOfNamedSequences.toList
    println("predicted list length : " + list.length)
    graphDrawer.drawGraphFromSequences("Predicted", list)
    println("Here")
    graphDrawer.drawGraphFromSequences("As a Percentage Of Optimal", arrayOfNSAsAPercentageOfOptimal.toList)
  }
  
  private def printStats(arr: Array[NamedSequence], opt: NamedSequence, inx: Seq[Int]) = {
    val sumOfOptimal = opt.seq.sum
    val sumOfMean = arr(arr.length-3).seq.sum
    val sumOfMedian = arr(arr.length-2).seq.sum
    
    println("Array Length : " + arr.length)
    val length = opt.seq.length -1
    
    println("Sum of Values for Optimal : " + sumOfOptimal)
    println("Sum of Values for Mean : " + sumOfMean)
    println("Sum of Values for Median : " + sumOfMedian)
    println("Mean as a percentage of Optimal : " + sumOfMean * 100 / sumOfOptimal)
    println("Median as a percentage of Optimal : " + sumOfMedian * 100 / sumOfOptimal)
    println()
    val newArr = arr.dropRight(3)
    for (ns <- newArr) {
      val name = ns.name
      val sum = ns.seq.sum
      println("Sum of Values for " + name + " : " + sum)
      println(name + " as a percentage of Optimal : " + sum * 100 / sumOfOptimal)
    }
    
  }
}