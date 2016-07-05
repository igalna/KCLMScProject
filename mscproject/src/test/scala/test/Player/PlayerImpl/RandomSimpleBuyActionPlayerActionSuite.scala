package test.Player.PlayerImpl

import org.scalatest.FlatSpec

import com.datastax.driver.core.utils.UUIDs._

import main.DataConverter.CSVToServerMessage
import main.Players.RandomSimpleBuyActionPlayer
import main.traits.DataItem
import main.traits.ServerMessage
import main.traits.SimpleBuyAction

class RandomSimpleBuyActionPlayerActionSuite extends FlatSpec {
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\testfile.csv"
  val bufferedSource = scala.io.Source.fromFile(fileName)
  val serverName:String = "myServer"
  val serverLocation:String = "localhost"
  
  val csvReader: CSVToServerMessage = new CSVToServerMessage(
                                          bufferedSource, 
                                          serverName, 
                                          serverLocation)
  
  val serverMessages:List[ServerMessage] = csvReader.buildList
  val player = new RandomSimpleBuyActionPlayer(random)
  serverMessages.foreach { x => player.receiveMsgFromServer(x) }
  
  "A Player's Action" should "give the First ServerMessage received in the first call to action" in {
    val selectedData: DataItem = player.action.asInstanceOf[SimpleBuyAction].dataItem
    assert(serverMessages(0).data.contains(selectedData))
  }
  it should "give the second ServerMessage received in the second call to Action" in {
    val selectedData: DataItem = player.action.asInstanceOf[SimpleBuyAction].dataItem
    assert(serverMessages(1).data.contains(selectedData))
  }
  it should "give the third ServerMessage received in the third call to Action" in {
    val selectedData: DataItem = player.action.asInstanceOf[SimpleBuyAction].dataItem
    assert(serverMessages(2).data.contains(selectedData))
  }
  it should "give the fourth ServerMessage received in the third call to Action" in {
    val selectedData: DataItem = player.action.asInstanceOf[SimpleBuyAction].dataItem
    assert(serverMessages(3).data.contains(selectedData))
  }
  it should "A Player should not select the DataItem from the Data/Time column" in {
    val di = new DataItem("Date", "06/06/2016")
    assert(!player.action.equals(di))
  }
  it should "produce a IndexOutOfBoundsException when invoking after all the ServerMessages have been Actioned" in {
    player.action
    intercept [IndexOutOfBoundsException] {
      player.action
    }
  }
}