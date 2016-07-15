package main.Players

import java.util.UUID

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.concurrent.Await

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.pattern._
import akka.util.Timeout

import main.Actors.SimpleRandomActor
import main.traits.Message
import main.traits.Player
import main.traits.ServerMessage
import main.traits.ActorMessage._
import main.traits.SimpleBuyAction

class ActorPlayerImpl(val playerId: UUID) extends Player {
  
  private val receivedData: ListBuffer[Message]= ListBuffer()
  private var widthOfServerMessageData = -1
  private var counter = 0
  val mySystem = ActorSystem("mySystem")
  val actor = mySystem.actorOf(SimpleRandomActor.props)
  implicit val timeout = Timeout(5 seconds)
  
  def getReceivedData: List[Message] = ???
  def receiveMsgFromPlayer(playerId: Int, msg: Message) = ???
  def sendMsgToPlayer(playerId: Int, msg: Message) = ???
  
  override def receiveMsgFromServer(msg: Message) = {
    receivedData += (msg)
    widthOfServerMessageData = msg.asInstanceOf[ServerMessage].data.length-1
    println("Player: " + playerId + " received data: " + msg)
  }
  
  override def action = {
    val msg = receivedData(counter)
    counter += 1
    val name = msg.asInstanceOf[ServerMessage].data.head
    val data = msg.asInstanceOf[ServerMessage].data.tail
    val future = actor ? DataSet(data)
    val result = Await.result(future, timeout.duration).asInstanceOf[SingleAction]
    SimpleBuyAction(result.di)
  }
}