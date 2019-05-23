import akka.actor._
import akka.pattern.gracefulStop
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import Manager._

class CrashDummy extends Actor {
     def receive = {
         case s:String => println("Message received: " + s)
         case _ => println("Unknown message received")
     }
     override def postStop { println("CrashDummy's Will")
}
}
 
object Manager {
  case object Shutdown
}
 
class Manager extends Actor {
 
  val worker = context.watch(context.actorOf(Props[Manager], name = "worker"))
 
  def receive = {
    case "job" => worker ! "crunch"
    case Shutdown =>
      worker ! PoisonPill
      context become shuttingDown
  }
 
  def shuttingDown: Receive = {
    case "job" => sender() ! "service unavailable, shutting down"
    case Terminated(`worker`) =>
      context stop self
  }
}
 
object CrashDummyExample extends App {
     val system = ActorSystem("CrashDummyExample")
     val dummy = system.actorOf(Props[CrashDummy], name = "dummy")
     // try to stop the dummy gracefully
     try {
         val stopped:Future[Boolean] = gracefulStop(dummy,5 seconds,Manager.Shutdown)
         Await.result(stopped, 3 seconds)
         println("dummy was stopped")
     } 
     catch {
        case e:Exception => e.printStackTrace
     } 
     finally {
     system.shutdown
     }
}