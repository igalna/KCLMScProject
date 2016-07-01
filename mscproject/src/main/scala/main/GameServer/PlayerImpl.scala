package main.GameServer

import main.traits.Player
import main.traits.Message
import scala.collection.mutable.ListBuffer
import java.util.UUID
import main.traits.ServerMessage

class PlayerImpl(val playerId: UUID) extends Player {
  
  private val receivedData: ListBuffer[Message]= ListBuffer()
  
  @Override
  def sendMsgToPlayer(playerID: Int, msg: Message) = ???
  @Override
  def receiveMsgFromPlayer(playerId: Int, msg: Message) = ???
  @Override
  def receiveMsgFromServer(msg: Message) = {
    receivedData += (msg)
    println("Player: " + playerId + " received data: " + msg)
  }
  @Override
  def getReceivedData = receivedData.toList
  @Override
  def action = ???
}