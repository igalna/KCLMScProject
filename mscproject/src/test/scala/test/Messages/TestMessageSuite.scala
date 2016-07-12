package test.Messages

import org.scalatest.FunSuite
import main.traits.DataItem
import main.traits.ServerMessage
import main.traits.Message
import play.api.libs.json.Writes
import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._

class MessageSuite extends FunSuite{
  
  val serverName = "server"
  val serverLocation = "localhost"
  
  val di1: DataItem = new DataItem("MMM", "432")
  val di2: DataItem = new DataItem("AAPL", "521")
  val di3: DataItem = new DataItem("USD/GBP", "1.3323")
  val di4: DataItem = new DataItem("GBP/CHF", "1.0223")
  
  val stockList = List(di1, di2)
  val currencyList = List(di3, di4)
  
  val stockServerMessage = new ServerMessage(
                                serverName, 
                                serverLocation,
                                stockList)
  
  val currencyServerMessage = new ServerMessage(
                                serverName,
                                serverLocation,
                                currencyList)
  val jsonSSM = Json.toJson(stockServerMessage)
  val jsonCSM = Json.toJson(currencyServerMessage)
  
  
  test("ServerMessage serverName should convert to JSON format") {
    assert((jsonSSM \ "name").get.equals(JsString(serverName)))
  }
  test("ServerMessage serverLocation should convert to JSON format") {
    assert((jsonCSM \ "location").get.equals(JsString(serverLocation)))
  }
  
  test("ServerMessage data should convert DataItem name to JSON format") {
    assert(((jsonCSM \ "data")(0).get \ "name").get.equals(JsString("USD/GBP")))
  }
  test("ServerMessage data should convert DataItem value to JSON") {
    assert(((jsonSSM \ "data")(1).get \ "value").get.equals(JsString("521")))
  }
  
  test("JSON format ServerMessage name should convert back to ServerMessage") {
    val ssm: JsResult[ServerMessage] = jsonSSM.validate[ServerMessage]
    assert(ssm.get.name.equals(serverName))
  }
  test("JSON format ServerMessage location should converty back to ServerMessage") {
    val csm: JsResult[ServerMessage] = jsonCSM.validate[ServerMessage]
    assert(csm.get.location.equals(serverLocation))
  }
  
  test("JSON format DataItem name should convert back to DataItem") {
    val csm: JsResult[ServerMessage] = jsonCSM.validate[ServerMessage]
    assert(csm.get.data(1).name.equals("GBP/CHF"))
  }
  test("JSON format DataItem value should convert back to DataItem") {
    val ssm: JsResult[ServerMessage] = jsonSSM.validate[ServerMessage]
    assert(ssm.get.data(0).value.equals("432"))
  }
}