package main.dl4j

import scala.io.Source
import scala.collection.mutable.ListBuffer
import java.io.PrintWriter

object CSVLabelFromInputFile {
  
  def main(args: Array[String]) {
    val srcPath = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\dl4j\\"
    val src = scala.io.Source.fromFile(srcPath + "veryLargeTestFile.csv")
    
    val destinationPath = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\dl4j\\"
    val destination = new PrintWriter(destinationPath + "veryLargeTestFileLabels.csv")
    
    var lastLine: ListBuffer[Double] = null
    var currentLine: ListBuffer[Double] = null
    var nextLine: ListBuffer[Double] = null
    
    val labelsList: ListBuffer[Int] = new ListBuffer
    
    val iter = src.getLines().drop(1).toList.reverse
    
    var first = true
    var second = true
    
    for (line <- iter) {

      val cols = line.split(",").map { _.trim }
      val doubleList: ListBuffer[Double] = new ListBuffer
      
      var index = 0
      
      var currentHighest = 0

      var counter = 0
      for (item <- cols) {
        if (!(item == "null")) {
          doubleList += item.toDouble
        } else if (lastLine != null) {
          doubleList += lastLine(index).toDouble
        } else {
          doubleList += 0.0
        }
        index += 1
      }
       if (first) {
        lastLine = doubleList
        first = false
      } else {
        val diff = (lastLine zip doubleList).map { x => Math.abs(x._1 - x._2) }
        val greatest = diff.max
        val index = diff.indexOf(greatest)
        labelsList += index
        lastLine = doubleList
        if(true) {
          println("Greatest: " + greatest)
          println("Index of Greatest : " + index)
          second = false
        }
      }
      index = 0
      
    }
    labelsList.reverse.foreach { x => destination.append(x.toString() + "\n")}
    
    destination.close()
    src.close()
  }
}