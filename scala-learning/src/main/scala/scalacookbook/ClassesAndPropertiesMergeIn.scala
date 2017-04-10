package scalacookbook

/**
  * Created by christine on 07/02/2017.
  */
object ClassesAndPropertiesMergeIn extends App{


  //Lazy class fields only get evaluated when accessed:
  class LazyClass {

    lazy val lazyField = {
      println("initialising lazy field")
      "hello"
    }

    //pattern for fields that start off un-initialised is to use an option, start with none
    var uninitialisedField = None : Option[String]
  }

  val lazyClass = new LazyClass()//prints nothing
  lazyClass.lazyField //prints 'initialising lazy field'

  //to later set and get the un-initialised field:
  lazyClass.uninitialisedField = Some("hello")
  println( lazyClass.uninitialisedField.get ) // prints hello

  //Some inheritance patterns:
  //Handling constructor parameters

  class Person( val name : String, val age : Int ) {
    println("Person being built via primary constructor")

    def this( age : Int ) = {

      this( "Person built by auxiliary constructor" , age )
    }


    def canEqual(other: Any): Boolean = other.isInstanceOf[Person]


    //NOTE: equals method same as java, note how like groovy, == means .equals(...)
    override def equals(other: Any): Boolean = other match {
      case that: Person =>
        (that canEqual this) &&
          name == that.name &&
          age == that.age
      case _ => false
    }

    override def hashCode(): Int = {
      val state = Seq(name, age)
      state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
    }
  }


  //NOTE: by saying extends Person( age ), you are telling it which of the superclass constructors
  //to call
  class Employee( name : String, age : Int, val employeeNumber : Int ) extends Person( age ){
    println(" Employee being built")
  }

  println( new Employee( "name", 21, 1 ).name )



  //******************************************NEW*****************************//


  //abstract classes:
  abstract class AbstractClass {

    val abstractField :String
    def abstractMethod( s : String ) : Int
    def abstractSomething :Unit

    val overriddenActualVal = {
      println("setting overriddenActualVal to Abstract super class value xxx ")
      "xxx"
    }
  }

  class AbstractClassImpl extends AbstractClass{
    override val abstractField: String = "hello"
    override def abstractMethod(s: String): Int = 999
    override def abstractSomething: Unit = {
      //nothing
    }

    override val overriddenActualVal = {
      println("setting overriddenActualVal to AbstractClassImpl class value xxx ")
      "xxx"
    }
  }

  //NOTE how this calls stuff in the super class when setting the variable which is then overridden.
  new AbstractClassImpl

  //CASE CLASSES:::
  case class MyCaseClass( s : Int, t : Int )

  //no need to use new keyword to make one:
  val caseClass1 = MyCaseClass(1,2)
  val caseClass2 = MyCaseClass(1,2)

  //automatically generates equals and hash codes for the classes
  assert( caseClass1 == caseClass2 )
  assert( caseClass1.hashCode() == caseClass2.hashCode() )

  //copy good for immutable objects and merging in data:
  val caseClass3= caseClass1.copy(t=3)

  //NOTE also the nice toString that you get:
  println( caseClass3)












}
