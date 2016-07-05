package main.GameServer

import main.traits.Player
import scala.concurrent.duration._
import main.traits.Message
import main.traits.ServerMessage

class GameServer(val players: List[Player],val data: List[Message]) {
  
  def start = {
    println("GameServer Running")
    var counter = 0
    
    def iterate(data: List[Message]) {
      println("GameServer iterating")
      data match {
        case x :: tail => 
          players.par.foreach 
            { x => x.receiveMsgFromServer(data.head) 
                   x.action}
            println("GameServer Waiting")
            Thread.sleep(5000)
            iterate(tail)
        case _ =>  
          println("Data elements ended")
      }
    }
    iterate(data)
  }
}