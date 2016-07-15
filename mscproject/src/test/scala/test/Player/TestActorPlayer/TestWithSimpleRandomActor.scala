package test.Player.TestActorPlayer

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import main.traits.ServerMessage
import main.DataConverter.CSVToServerMessage
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import org.scalatest.FlatSpec
import main.Players.ActorPlayerImpl
import com.datastax.driver.core.utils.UUIDs._
import main.traits.SimpleBuyAction
import main.traits.DataItem

class TestWithSimpleRandomActor extends FlatSpec {
  
  implicit val timeout = Timeout(5 seconds)
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\test data\\testfile.csv"
  val bufferedSource = scala.io.Source.fromFile(fileName)
  val serverName:String = "myServer"
  val serverLocation:String = "localhost"
  
  val csvReader: CSVToServerMessage = new CSVToServerMessage(
                                          bufferedSource, 
                                          serverName, 
                                          serverLocation)
  
  val serverMessages:List[ServerMessage] = csvReader.buildList

  val player = new ActorPlayerImpl(random)
  serverMessages.foreach { x => player.receiveMsgFromServer(x) }
  
  "A player with a Random selecting ActorSystem	" should "select a random item to buy" in {
    val selectedData: DataItem = player.action.dataItem
    assert(serverMessages(0).data.contains(selectedData))
  }
  it should "select another random item to buy from the second message received" in {
    val selectedData: DataItem = player.action.dataItem
    assert(serverMessages(1).data.contains(selectedData))
  }
  it should "select another random item to buy from the final message received" in {
    player.action
    player.action
    player.action
    val selectedData: DataItem = player.action.dataItem
    assert(serverMessages(serverMessages.length -1).data.contains(selectedData))
  }
  
  
  
}