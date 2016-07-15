package main.traits

sealed trait ActorMessage

object ActorMessage {
  final case class DataSet(data: List[DataItem]) extends ActorMessage
  final case class SingleAction(di: DataItem) extends ActorMessage
  final case class SuccessfulAction() extends ActorMessage
  final case class UnsuccessfulAction() extends ActorMessage
  final case class AddChild(name: String) extends ActorMessage
  final case class RemoveChild(name: String) extends ActorMessage
  final case object GetRoutees extends ActorMessage
  final case object Begin extends ActorMessage
}
