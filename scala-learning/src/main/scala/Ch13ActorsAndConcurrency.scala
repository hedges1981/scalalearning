import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Terminated}

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


  //so its : send message-> constructor called -> pre start called -> message received -> [exception thrown] -> pre restart
  // -> post stop -> constructor called -> post restart -> pre-start -> post stop.

  //Nested actors and children, including looking them up

  class Child extends Actor {

    println("Child made")

    override def preStart(): Unit = {
      println( "Path of child is"+ context.self.path)
    }

    override def receive: Receive = {
      case _ => println( "child received")
    }
  }

  class Parent extends Actor {

    //create a child on start up, and watch it:
    val childActorInConstructor = context.actorOf(Props[Child], name = "childActorInConstructor")
    context.watch( childActorInConstructor )

    override def preStart(): Unit = {
      println( "Path of parent is"+ context.self.path)
    }

    override def receive: Receive = {
      case Terminated(childActorInConstructor ) => println("They killed my child")
      case "createChild" =>
        println("Parent creating child")
        //when an actor creates a child actor, can use the context inside here:
        val child = context.actorOf(Props[Child], name = "childActor")

        val x =0
    }

    override def postStop(): Unit = {
      println("Parent actor stopping")
    }
  }

  val parent = actorSystem.actorOf(Props[Parent], name = "ParentActor")

  parent ! "createChild"
  Thread.sleep(1000)

  //can use string paths to look up child actors:Actor[akka://HelloActorSystem/user/ParentActor/childActor]
  //the path stuff looks shit, took ages to get to work.
  val child = actorSystem.actorFor("/user/ParentActor/childActor")
  child ! "someMessage"

  //TO stop an actor:
  actorSystem.stop( parent ) //note how the postStop is called
  //NOW this will not do anything, i.e. the message goes nowhere
  //NOTE, no "actor stopped" exception is thrown, is just silent.
  parent ! "createChild"

  //start a new one:
  val parent2 = actorSystem.actorOf(Props[Parent], name = "ParentActor2")
  Thread.sleep(1000)
  parent2 ! "createChild"
  //can also send a PoisonPill
  parent2 ! PoisonPill  //note that this also calls the postStop method


  //DEMO of watching, note how the parent does something when its child is killed:
  val parent3 = actorSystem.actorOf(Props[Parent], name = "ParentActor3")
  Thread.sleep(1000)
  //get the inner child:
  val innerChild = actorSystem.actorFor("/user/ParentActor3/childActorInConstructor")
  Thread.sleep(1000)
  innerChild ! PoisonPill   //NOTice how this causes the parent to print "they killed my child"

  //this shuts down the system gracefully.... without this will run for ever
  actorSystem.shutdown
}
