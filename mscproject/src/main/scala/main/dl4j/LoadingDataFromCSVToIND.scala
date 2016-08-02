package main.dl4j

import scala.io.Source.fromFile
import scala.collection.mutable.ListBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.dataset.DataSet
import org.deeplearning4j.datasets.iterator.DataSetIterator
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator

class LoadingDataFromCSVToIND {
  
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