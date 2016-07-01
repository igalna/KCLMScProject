package main.traits

import play.api.libs.json.Writes
import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.UUID


sealed trait Message
case class ServerMessage(name: String, location: String, data: List[DataItem]) extends Message
case class PlayerMessage(playerId: UUID, location: String) extends Message

object ServerMessage {
  implicit val serverMessageWrites = new Writes[ServerMessage] {
    def writes(msg: ServerMessage) = Json.obj(
        "name" -> msg.name,
        "location" -> msg.location,
        "data" -> msg.data)
  }
  implicit val serverMessage: Reads[ServerMessage] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "location").read[String] and
    (JsPath \ "data").read[List[DataItem]]
  )(ServerMessage.apply _)
}