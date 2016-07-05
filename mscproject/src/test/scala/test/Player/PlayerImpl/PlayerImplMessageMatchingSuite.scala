package test.Player.PlayerImpl

import org.scalatest.FunSuite
import com.datastax.driver.core.utils.UUIDs._
import com.datastax.driver.core.utils.UUIDs._
import main.traits.DataItem
import main.traits.PlayerMessage
import main.traits.ServerMessage
import main.GameServer.GameServer
import main.Players.RandomSimpleBuyActionPlayer

class PlayerImplMessageMatchingSuite extends FunSuite{
  
  val player = new RandomSimpleBuyActionPlayer(random())
  
  val serverName = "server"
  val serverLocation = "localhost"
  
  
  val stockServerMessage = new ServerMessage(
                                serverName, 
                                serverLocation,
                                List())
  val playerMessage = new PlayerMessage(random, "HelloMessage")

  
  test("the messages a player receives should be matched to their Message implementations") {
    assert(player.messageMatcher(stockServerMessage).isInstanceOf[ServerMessage])
  }
  test("messageMatcher should match the PlayerMessage type of Message") {
    assert(player.messageMatcher(playerMessage).isInstanceOf[PlayerMessage])
  }
}