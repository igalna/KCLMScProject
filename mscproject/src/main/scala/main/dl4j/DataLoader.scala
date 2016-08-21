package main.dl4j

import scala.io.Source.fromFile
import scala.collection.mutable.ListBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.dataset.DataSet
import org.deeplearning4j.datasets.iterator.DataSetIterator
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator
import scala.io.BufferedSource
import java.io.PrintWriter

class DataLoader {
  
  var dataFromFile: ListBuffer[Array[Double]] = new ListBuffer
  var WIDTH = 0
  lazy val SIZE_OF_DATA = dataFromFile.size
  
  lazy val optimalAtTimeStep: ListBuffer[Double] = new ListBuffer
  lazy val indexOfOptimalAtTimeStep: ListBuffer[Int] = new ListBuffer
  lazy val arraysOfDifferences: ListBuffer[Array[Double]] = new ListBuffer
  
  /**
   * Returns a DataSet from a CSV file name passed to it
   * 
   * @Params str String name of the CSV file to parse into a DataSet
   * @Return the DataSet resulting from the CSV file
   */
  def getDataSetFromCSV(str: String): DataSet = {
    
    val fileName = str
    val src = scala.io.Source.fromFile(fileName)
    val iter = src.getLines().drop(1)
    
    for (line <- iter) {
      
      val item = line.split(",").map { _.trim }
      WIDTH = item.length
      val lineData: Array[Double] = new Array(WIDTH)
      
      var counter = 0
      for (data <- item) {
        lineData(counter) = data.toDouble
        counter += 1
      }
      dataFromFile += lineData
    }
    
    getDataSetFromListBuffer
  }
  
  def getDataFromMultipleCSV(listOfCSV: List[String], blockSize: Int, destination: String) = {
    val listOfSources = listOfCSV.map { x => 
                                scala.io.Source.fromFile(x) }
    
    val listOfBuffers = listOfSources.mapConserve { x => x.getLines().drop(1) }.asInstanceOf[List[Iterator[String]]]
    
    var lb: Array[ListBuffer[Array[Double]]] = new Array(listOfBuffers.size)
    var counter = 0
    for (iter <- listOfBuffers) {
      var data: ListBuffer[Array[Double]] = new ListBuffer
      while (iter.hasNext) {
        val next = iter.next().split(",").map(_.trim)
        val arr: Array[Double] = new Array(next.length)
        var internalCounter = 0
        for (str <- next) {
          arr(internalCounter) = str.toDouble
          internalCounter += 1
        }
        data += arr
      }
      lb(counter) = data
      counter +=1
    }
    
    val dest = destination
    val writer: PrintWriter = new PrintWriter(dest)
    
    val biggest = lb.map { x => x.size }.max
    val smallest = lb.map { x => x.size }.min
    
    var choice = 0
    counter = 0
    var increment = counter + blockSize
    var currentList: ListBuffer[Array[Double]] = lb(choice)
    while (counter < biggest) {
      for (list <- lb) {
        println("Choice :" + choice)
        println("Counter : " + counter)
        println("Increment : " + increment)
        println("lb size : " + lb.size)
        println("Biggest : " + biggest)
        println("Smallest : " + smallest)
        (counter until increment).foreach { x => 
                                  val line = if(list(x) == null) null else list(x)
                                  if (line != null) {
                                    val l = line.map { y =>
                                         y.toString() }
                                    l.foreach { y =>
                                         writer.append(y)
                                         writer.append(",")}
                                  }
                                  writer.append("\n")
                                }
      }
      choice += 1
      
      if (choice == lb.size-1) {
        choice = 0
        counter = increment
        increment += blockSize
        if (increment >= smallest) {
          for (list <- lb) {
            val listSize = list.size
            (counter until listSize).foreach { x =>
                                   val line = if(list(x) == null) null else list(x)
                                   if (line != null) {
                                    val l = line.map { y =>
                                         y.toString() }
                                    l.foreach { y =>
                                         writer.append(y)
                                         writer.append(",")}
                                  }
                                  writer.append("\n") 
                                }
          }
          increment = smallest
          counter = biggest
        }
      }
    }
    writer.close()
    listOfSources.foreach { x => x.close() }
  }
  
  private def getDataSetFromListBuffer: DataSet = {
    val input: INDArray = Nd4j.zeros(dataFromFile.size-1, WIDTH)
    val label: INDArray = Nd4j.zeros(dataFromFile.size-1, WIDTH)

    var counter = 0
    for (row <- dataFromFile) {
      
      if (counter < dataFromFile.size -1) {
        val firstRow = dataFromFile(counter)
        val secondRow = dataFromFile(counter+1)
        val diff = (firstRow zip secondRow).map { x => Math.abs(x._1 - x._2) }
        arraysOfDifferences += diff
        val greatest = diff.max
        optimalAtTimeStep += greatest
        val index = diff.indexOf(greatest)
        indexOfOptimalAtTimeStep += index
        
        var internalCounter = 0
        for (number <- firstRow) {
          input.putScalar(counter, internalCounter, number)
          label.putScalar(counter, index, 1)
          internalCounter += 1
        }
      }
      counter += 1
    }
    val dataSet: DataSet = new DataSet(input, label)
    return dataSet
  }
  
  def getDataSetFromListBuffer(buff: ListBuffer[Array[Double]]): DataSet = {
    val input: INDArray = Nd4j.zeros(buff.size-1, WIDTH)
    val label: INDArray = Nd4j.zeros(buff.size-1, WIDTH)

    var counter = 0
    for (row <- buff) {
      
      if (counter < buff.size -1) {
        val firstRow = buff(counter)
        val secondRow = buff(counter+1)
        val diff = (firstRow zip secondRow).map { x => Math.abs(x._1 - x._2) }
        val greatest = diff.max
        val index = diff.indexOf(greatest)
        
        var internalCounter = 0
        for (number <- firstRow) {
          input.putScalar(counter, internalCounter, number)
          label.putScalar(counter, index, 1)
          internalCounter += 1
        }
      }
      counter += 1
    }
    val dataSet: DataSet = new DataSet(input, label)
    return dataSet
  }
}