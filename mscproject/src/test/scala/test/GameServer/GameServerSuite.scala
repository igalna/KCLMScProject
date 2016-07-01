package test.GameServer

import org.scalatest.FunSuite
import main.GameServer.PlayerImpl
import main.traits.Player
import main.GameServer.GameServer
import com.softwaremill.macwire._
import java.util.UUID
import main.traits.Message
import main.traits.ServerMessage
import play.api.libs.json.{JsNull,Json,JsString,JsValue}
import main.traits.DataItem

class GameServerSuite extends FunSuite{
  
  val serverName = "server"
  val serverLocation = "localhost"
 
  val dummyUuid: UUID = java.util.UUID.randomUUID()
  val p1:Player = wire[PlayerImpl]
  val p2:Player = wire[PlayerImpl]
  val p3:Player = wire[PlayerImpl]
  val p4:Player = wire[PlayerImpl]
  val playerList: List[Player] = List(p1, p2, p3, p4)
  
  val di1: DataItem = new DataItem("MMM", "432")
  val di2: DataItem = new DataItem("AAPL", "521")
  val di3: DataItem = new DataItem("USD/GBP", "1.3323")
  val di4: DataItem = new DataItem("GBP/CHF", "1.0223")
  
  val stockList = List(di1, di2)
  val currencyList = List(di3, di4)
  
  val data1:Message = new ServerMessage(serverName,
                                        serverLocation,
                                        stockList)
  val data2:Message = new ServerMessage(serverName,
                                        serverLocation,
                                        stockList)
  val data3:Message = new ServerMessage(serverName,
                                        serverLocation,
                                        currencyList)
  val data4:Message = new ServerMessage(serverName,
                                        serverLocation,
                                        currencyList)
  val dataList: List[Message] = List(data1, data2, data3, data4)
  
  val gs = wire[GameServer]
  gs.start
  
  test("A Player should receive the First data item sent from the server") {
    assert(p1.getReceivedData().contains(data1) == true)
  }
  test("A Player should receive the Final data item from the server") {
    assert(p1.getReceivedData().contains(data4))
  }
  
  test("Each player should receive the same data items") {
    assert(p1.getReceivedData().equals(p2.getReceivedData()))
  }
  
  test("GameServer should work with greater than Two players") {
    assert(p3.getReceivedData().equals(p1.getReceivedData()))
  }
}