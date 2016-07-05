package main.Players

import main.traits.Player
import com.datastax.driver.core.utils.UUIDs._
import main.traits.Message
import scala.collection.mutable.ListBuffer
import java.util.UUID
import scala.annotation.tailrec
import main.traits.ServerMessage
import main.traits.PlayerMessage
import main.traits.DataItem
import main.traits.Action
import main.traits.SimpleBuyAction
import akka.event.Logging

class RandomSimpleBuyActionPlayer(val playerId: UUID) extends Player {
  
  private val receivedData: ListBuffer[Message]= ListBuffer()
  private var counter = 0
  private var widthOfServerMessageData = -1
  
  @Override
  def sendMsgToPlayer(playerID: Int, msg: Message) = ???
  @Override
  def receiveMsgFromPlayer(playerId: Int, msg: Message) = ???
  @Override
  def receiveMsgFromServer(msg: Message) = {
    receivedData += (msg)
    widthOfServerMessageData = msg.asInstanceOf[ServerMessage].data.length-1
    println("Player: " + playerId + " received data: " + msg)
  }
  @Override
  def getReceivedData = receivedData.toList
  @Override
  def action: Action = {
    val currentMsg = receivedData(counter)
    currentMsg match {
      case ServerMessage(_,_,_) => counter +=1
                                   new SimpleBuyAction(currentMsg.asInstanceOf[ServerMessage].data(randomIntegerInRange)) 
                                   
      case PlayerMessage(_,_)   => ???
    }
  }
  private def randomIntegerInRange: Int = {
    val rnd = new scala.util.Random
    val range = 1 to widthOfServerMessageData
    return range(rnd.nextInt(range.length))
  }
  
  def messageMatcher(msg: Message): Message = {
    msg match {
      case ServerMessage(_,_,_) => msg.asInstanceOf[ServerMessage]
      case PlayerMessage(_,_)   => msg.asInstanceOf[PlayerMessage]
    }
  }
}