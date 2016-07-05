package main.traits

sealed trait Action
case class SimpleBuyAction(dataItem: DataItem) extends Action
case class SimpleSellAction(dataItem: DataItem) extends Action