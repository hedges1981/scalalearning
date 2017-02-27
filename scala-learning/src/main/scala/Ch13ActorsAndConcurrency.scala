import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by rhall on 20/02/2017.
  */
object Ch13ActorsAndConcurrency extends App{

  //very simple actor:
  class HelloActor( val  x : Int ) extends Actor {

    //no args constructor:
    def this(){
      this(2)
    }

    override def receive: Receive = {
      case "hello" => {
        println("Actor Current thread Id is : "+Thread.currentThread().getId )
        println("hello to you to!")
      }
      case _ => {
        println("Actor Current thread Id is : "+Thread.currentThread().getId )
        println("eh?")
      }
    }
  }
  //need an actor system:
  val actorSystem = ActorSystem("HelloActorSystem")
  //create and start the actor:

  /* NOTE on actor system, “An actor system is a hierarchical group of actors which share common configuration,
    e.g. dispatchers, deployments, remote capabilities and addresses. It is also the entry point
    for creating or looking up actors.”
    basically its the thing that allocates the threads, similar to the Spring container....
    generally only want one actor per application.
*/

  //NOTE: how we don't get a reference to the actual actor object, but an ActorRef... to proxy it and handle requests.
  val helloActor : ActorRef  = actorSystem.actorOf( Props[HelloActor], name = "helloActor")


  println("Calling thread : Current thread Id is : "+Thread.currentThread().getId )
  //NOTE: how the actor logic gets executed in a different thread... see the thread IDs in the outputs.
  //send the actor some messages:
  helloActor ! "hello"
  helloActor ! "anotherMessage"

  //shutdown the system... important to do this else keeps running for ever.
  //actorSystem.shutdown

  //creating actor with constructor arguments:
  //use this syntax, see how it is slightly different to the above way:
  val _helloActor : ActorRef  = actorSystem.actorOf( Props( new HelloActor(5)), name = "helloActor2")

  //ACTOR LIFECYCLE STUFF:
  class ActorLifecycleDemo extends Actor {

    println("constructor called ")

    override def preStart(): Unit = println( "in PreStart")

    override def postStop(): Unit = println( " in PostStop")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit ={
      println(" in pre-restart ")
      super.preRestart( reason,message )
    }

    override def postRestart(reason: Throwable): Unit ={
      println( "in post restart")
      super.postRestart(reason)
    }

    override def receive: Receive = {
      case "restart" => throw new Exception("restart")
      case _ => println("received message")
    }
  }

  val lifecycleDemoActor = actorSystem.actorOf( Props[ActorLifecycleDemo], name = "LSD" )

  println("::send simple message")
  lifecycleDemoActor ! "message"
  Thread.sleep(1000)

  println("::make it restart")
  lifecycleDemoActor ! "restart"
  Thread.sleep(1000)
  actorSystem.shutdown

  //so its : send message-> constructor called -> pre start called -> message received -> [exception thrown] -> pre restart
  // -> post stop -> constructor called -> post restart -> pre-start -> post stop.

}
