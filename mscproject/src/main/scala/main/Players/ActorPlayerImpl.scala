package main.Players

import main.traits.Player
import akka.actor.Actor
import main.traits.Message
import scala.collection.mutable.ListBuffer
import main.traits.ServerMessage
import java.util.UUID
import akka.actor.ActorSystem

class ActorPlayerImpl(val playerId: UUID) extends Player {
  
  private val receivedData: ListBuffer[Message]= ListBuffer()
  private var widthOfServerMessageData = -1
  val mySystem = ActorSystem("mySystem")
  
  def getReceivedData: List[Message] = ???
  def receiveMsgFromPlayer(playerId: Int, msg: Message) = ???
  def sendMsgToPlayer(playerId: Int, msg: Message) = ???
  
  override def receiveMsgFromServer(msg: Message) = {
    receivedData += (msg)
    widthOfServerMessageData = msg.asInstanceOf[ServerMessage].data.length-1
    println("Player: " + playerId + " received data: " + msg)
  }
  
  def action = {
    ???
  }
}