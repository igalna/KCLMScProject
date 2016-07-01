package main.traits

import play.api.libs.json.Writes
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.json.Json
import play.api.libs.functional.syntax._

case class DataItem(name: String, value: String)


object DataItem {
  implicit val dataItemWrites = new Writes[DataItem] {
    def writes(item: DataItem) = Json.obj(
          "name" -> item.name,
          "value" -> item.value
        )
  }
  implicit val dataItemReads: Reads[DataItem] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "value").read[String]
  )(DataItem.apply _)
}