import akka.actor._

class Bobomb extends Actor {
    println("in the Bob-omb constructor")
    override def preStart {
        println("in the Bob-omb preStart")
 }

  override def postStop {
     println("in the Bob-omb postStop")
 }
 override def preRestart(reason: Throwable, message: Option[Any]) {
     println("in the Bob-omb preRestart")
     println(s" preRestart message: ${message.getOrElse("")}")
     println(s" preRestart reason: ${reason.getMessage}")
     super.preRestart(reason, message)
 }
 
 override def postRestart(reason: Throwable) {
     println("in the Bob-omb postRestart")
     println(s" postRestart reason: ${reason.getMessage}")
     super.postRestart(reason)
 }
 
 def receive = {
     case ForceExploit => throw new Exception("Boom!")
     case _ => println("Bob-omb received a message")
 }
}

case object ForceExploit

object LifecycleDemo extends App {
     val system = ActorSystem("BombSystem")
     val bobomb = system.actorOf(Props[Bobomb], name = "Bobomb")
     
     println("sending Bob-omb a message")
     bobomb ! "activate"
     Thread.sleep(4000)
     println("making Bob-omb exploit")
     bobomb ! ForceExploit
     Thread.sleep(4000)
     println("stopping Bob-omb")
     system.stop(bobomb)
     println("shutting down bomb system")
     system.shutdown
}