package main.Actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import main.traits.DataItem
import akka.routing.RoundRobinGroup
import akka.routing.Router
import scala.collection.mutable.ArrayBuffer
import akka.routing.BroadcastGroup
import akka.routing.RoundRobinGroup
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.actor.PoisonPill
import akka.actor.Kill


class SimpleRandomActor extends Actor with ActorLogging {
  import SimpleRandomActor._
  
  private var numberOfDataItems: Int = _
  private var routeePaths: ArrayBuffer[String] = ArrayBuffer.empty
  
  private var router = 
    context.actorOf(BroadcastGroup(routeePaths.toList).props())
  
  def receive = {
    case AddChild(name)    => val actor = context.actorOf(SimpleRandomActor.props, name)
                              sender ! actor.path.name
    case RemoveChild(name) => val ref = context.children.find { x => x.path.name == name }.get
                              context.stop(ref)
                              sender ! context.children.toList
    case GetRoutees        => sender ! context.children.toList
    case DataSet(data)     => context.children.foreach { child => child.forward(DataSet(data)) }
                              numberOfDataItems = data.length-1
                              sender ! SingleAction(data(randomIntegerInRange))
  }
  
  private def randomIntegerInRange: Int = {
    val rnd = new scala.util.Random
    val range = 1 to numberOfDataItems
    return range(rnd.nextInt(range.length))
  }
}

object SimpleRandomActor {
  
  def props: Props = Props(classOf[SimpleRandomActor])
  
  final case class DataSet(data: List[DataItem])
  final case class SingleAction(di: DataItem)
  final case class SuccessfulAction()
  final case class UnsuccessfulAction()
  final case class AddChild(name: String)
  final case class RemoveChild(name: String)
  final case object GetRoutees
}