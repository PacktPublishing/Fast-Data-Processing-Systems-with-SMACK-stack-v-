import akka.actor._
class Subordinate extends Actor {
	def receive = {
		case _ => println("Subordinate received a message")
	}
}

class Boss extends Actor {
	// start subordinate as a child
	val sub = context.actorOf(Props[Subordinate], name = "sub")
	context.watch(sub)
	def receive = {
		case Terminated(`sub`) => println("They've killed my sub")	
		case _ => println("Boss received a message")
	}
}

object MonitoringTest extends App {
	// create the ActorSystem instance
	val system = ActorSystem("MonitoringTest")

	// create the Boss (and it will create a Sub)
	val boss = system.actorOf(Props[Boss], name = "Boss")
	
	// look for sub, then we kill it
	val sub = system.actorSelection("/user/Boss/Subordinate")

    //from a sibling actor
    //val sub = context.actorSelection("../Subordinate")
    //val sub = system.actorFor("akka://MonitoringTest/user/Boss/Subordinate ")
    //val sub = system.actorFor(Seq("user", "Boss", "Subordinate"))
    
    //val sub = system.actorFor(Seq("...", "Subordinate"))

    
	sub ! PoisonPill
	Thread.sleep(3000)
	println("Game over")
	system.shutdown
}
