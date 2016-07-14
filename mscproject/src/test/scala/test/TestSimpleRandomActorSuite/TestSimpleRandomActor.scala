package test.TestSimpleRandomActorSuite

import scala.concurrent.duration._
import scala.concurrent.Await

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern._
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import akka.testkit.TestProbe
import akka.util.Timeout
import main.Actors.SimpleRandomActor
import main.Actors.SimpleRandomActor._
import main.DataConverter.CSVToServerMessage
import main.traits.ServerMessage

class TestSimpleRandomActor extends TestKit(ActorSystem("MySpec")) 
  with ImplicitSender with WordSpecLike with Matchers
  with BeforeAndAfterAll {
  
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
  
  val firstServerMessage:ServerMessage = serverMessages(0)
  val middleServerMessage = serverMessages(2)
  val lastServerMessage = serverMessages(serverMessages.length -1)
  
  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }
  
  "A SimpleRandomActor Actor" must {
    val actor = system.actorOf(SimpleRandomActor.props)
    
    "be able to add children to it's routes" in {
      actor ! AddChild("FirstChild")
      expectMsg("FirstChild")
    }
    "be able to add more than one child from it's routes" in {
      actor ! AddChild("SecondChild")
      expectMsg("SecondChild")
      val future = actor ? GetRoutees
      val result = Await.result(future, timeout.duration).asInstanceOf[List[ActorRef]]
      assert(result.map { x => x.path.name }.contains("SecondChild"))
    }
      "be able to remove children from it's routes" in {
      actor ! AddChild("ThirdChild")
      expectMsg("ThirdChild")
      actor ! RemoveChild("SecondChild")
      Thread.sleep(7)
      val future = actor ? GetRoutees
      val result = Await.result(future, timeout.duration).asInstanceOf[List[ActorRef]]
      assert(!result.map { x => x.path.name }.contains("SecondChild"))
    }
    "be able to randomly return one of the DataItems when you send it a dataset" in {
      val future = actor ? DataSet(firstServerMessage.data)
      val result = Await.result(future, timeout.duration).asInstanceOf[SingleAction]
      assert(firstServerMessage.data.contains(result.di))
    }
  }
}