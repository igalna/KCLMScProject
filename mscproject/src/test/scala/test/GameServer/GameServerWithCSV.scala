package test.GameServer

import java.util.UUID
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import com.softwaremill.macwire._
import main.DataConverter.CSVToServerMessage
import main.GameServer.PlayerImpl
import main.traits.Player
import main.traits.ServerMessage
import main.GameServer.GameServer
import main.traits.Message
import scala.annotation.tailrec

class GameServerWithCSV extends FunSuite {
  
  val fileName = "C:\\Users\\igaln\\Documents\\King's stuff\\King's MSc project\\Data\\Trading\\testfile.csv"
  val bufferedSource = io.Source.fromFile(fileName)
  val serverName:String = "myServer"
  val serverLocation:String = "localhost"
  
  val csvReader: CSVToServerMessage = new CSVToServerMessage(
                                          bufferedSource, 
                                          serverName, 
                                          serverLocation)
  
  val serverMessages:List[ServerMessage] = csvReader.buildList
  

  val p1:Player = new PlayerImpl(java.util.UUID.randomUUID())
  val p2:Player = new PlayerImpl(java.util.UUID.randomUUID())
  val p3:Player = new PlayerImpl(java.util.UUID.randomUUID())
  val p4:Player = new PlayerImpl(java.util.UUID.randomUUID())
  val playerList: List[Player] = List(p1, p2, p3, p4)
  
  val gs = wire[GameServer]
  gs.start
  
  test("A Player should receive the First data item sent from the server") {
    //assert(p1.getReceivedData().contains(serverMessages(1)))
  }
  test("A Player should receive the Final data item from the server") {
    //assert(p1.getReceivedData().contains(serverMessages(serverMessages.length-1)))
  }
  
  test("Each player should receive the same data items") {
    //assert(p1.getReceivedData().equals(p2.getReceivedData()))
  }
  
  /**
   * testing GameServer with greater than 2 (>2) Players
   */
  test("Player 1 and Player 3 get the same data") {
    //assert(p1.getReceivedData().equals(p3.getReceivedData()))
  }
  test("Player 2 and Player 4 get the same data") {
    //assert(p2.getReceivedData().equals(p4.getReceivedData()))
  }
  test("Messages can be 'asInstanceOf[ServerMessage]") {
    val sm = p1.getReceivedData()
    @tailrec
    def messageMatcher(sm: List[Message]): Unit = {
      sm match {
        case x :: tail => 
          x match {
            case ServerMessage(_,_,_) => assert(x.asInstanceOf[ServerMessage].isInstanceOf[Message])
            case _ => Nil
          }
          messageMatcher(tail)
        case nil => Nil
      }
    }
    messageMatcher(sm)
  }
}