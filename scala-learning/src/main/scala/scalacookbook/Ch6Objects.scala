package scalacookbook

/**
  * Created by christine on 09/02/2017.
  */
object Ch6Objects extends App{

  //how to do casting:
  class Animal{}
  class Dog extends Animal {}

  val aDog : Dog = new Dog
  val aAnimal = aDog.asInstanceOf[Animal]

  //casting of lists and arrays to just be of type Object
  val stringList : List[String] = List("1","2","3")
  val objectList = stringList.asInstanceOf[List[Object]]

  // .class in scala.... use classOf[TYPE]
  println( classOf[String] ) //same as String.class in Java

  //and to get the class of an instance:
  val aString = "333"
  println( aString.getClass )


  //**************************NEW*****************************************************************

  //having a runnable class, as well a extending App like this one, you can also use a main method:
  object MainObject{

    def main( args : Array[String]): Unit ={
      println("Hello!")
    }
  }

  //Class and companion object can access each other's private methods:
  class AnClass( val x : Int ){
    private val privVal = 3
    AnClass.privateMethod
  }

  object AnClass {

    def privTest ={
      println( new AnClass(1).privVal )
    }

    private def privateMethod = {
      println("in An class private method")
    }

    //use of apply method to create objects default behaviour, here can make an AnClass via AnClass(x)
    def apply( x : Int ) : AnClass = {
      new AnClass(x)
    }

  }

  //note the private access that gets called:
  AnClass.privTest
  new AnClass(1)

  //Use of companion object's apply method , common pattern to instantiate stuff:
  val madeUsingApply = AnClass(2)  // scala routes this call to the AnClass.apply(x) 'static factory' method.

  //can make case classes a similar way:
  case class MyCaseClass( x : Int )  //remember that with a case class, members are all vals by default
  val myCaseClass : MyCaseClass = MyCaseClass(3)

  //USE Of stuff in package object:
  //see file package.scala, package object is for code that you want available to all classes in the package, e.g. we an do this:
  //note where this method resides: must be in file called package.scala in object called same as the package name.
  packageObjectMethod

  //TYPE aliasing, e.g.
  type Age = Int
  val myAge = 21
  println(myAge.getClass )  // prints int

  //can use it to make code clearer, and also interchange the types with the backing one:
  def printAge( age : Age ) = {
    println(" your age is: "+ age )
  }

  printAge( myAge ) //defined as type Age
  val intAge : Int = 22
  printAge( intAge ) // also works







}
