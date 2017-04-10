package scalacookbook

/**
  * Created by christine on 11/02/2017.
  */
object Ch9FunctionalProgramming extends App{

  //anonymous functions, (also known as function literals )...
  //note the varying degrees of simplicity of these examples:
  val list : List[Int]= List(1,2,3)
  //full explicit definition
  list.foreach( (i :Int) => println(i))
  list.foreach( i => println(i)) //only one argument and type inferred from list type so can be dropped
  list.foreach( println(_ )) //even simpler , reference each list element with _
  list.foreach( println ) //finally, because there is only 1 argument, and println takes just 1 argument, can write this also

  // using  functions as variables:
   val doublesInt = ( i : Int )  => { i * 2 }
  println( doublesInt ) //prints <function1>, i.e. the type of the function variable

  //can declare function with return type if you want, to make code easier to follow:
  val tripplesInt : (Int) => Int = i => { i * 3  } //says: is a function of an int that returns an int and = the literal

  //full definition example using two parameters:
  val addsIntAndDoubleReturnsDouble : (Int, Double) => Double = (anInt, aDouble ) =>{ anInt + aDouble }

  println( addsIntAndDoubleReturnsDouble(1, 2.333 ))

  //using an existing function as a variable:
  def addsDoubles( d1: Double, d2 : Double): Double = {
    d1 + d2
  }

  val addsDoublesVal = addsDoubles( _, _ ) // need the _,_ else it wont compile
  println( addsDoublesVal(1d,3d))

  //method that takes a function as a parameter:
  //this will execute a siple void:
  def doCallback( callback :()=>Unit ): Unit ={  //callback is of type: function that takes no parameters,
    //that is of Unit (i.e. void) return type
    callback()  //need the () on it to get it to actually make the function call
  }

  def simpleVoid() : Unit = {
    println("simpleVoid")
  }

  doCallback( simpleVoid ) //prints "simpleVoid"

  //More complex example, using a two arg function that returns something:
  def callsTwoIntFuctionWith5And6( twoIntFunction : (Int,Int)=> Int ) :Int = {
    twoIntFunction(5,6)
  }

  def addsTwoInts( i : Int, j : Int ) : Int = {
    i + j
  }

  val someInt : Int = callsTwoIntFuctionWith5And6( addsTwoInts )
  println( someInt ) //prints 11

  //convoluted example where we pass in some variables as well as the two int function:
  def callsTwoIntFunctionWithVars( twoIntFunction : (Int,Int)=> Int, x : Int, y : Int ) = {
    twoIntFunction(x,y)
  }

  println( callsTwoIntFunctionWithVars( addsTwoInts, 1,2 ) ) //prints 3

  //Using a closure:
  var hello = "hello"
  //sayHello is a closure because i) it references a variable declared outside ( here: hello ) and
  //ii) it can be passed around as a variable, see here how we pass it to the do callback method.
  def sayHello(): Unit = {
    println( hello )
  }

  doCallback( sayHello ) //prints 'hello'

  hello = "hola"
  doCallback( sayHello ) //prints 'hola',
  // as sayHello references the hello variable declared outside of its immediate scope.

  //Partially applied functions:
  def addTwoInts( i : Int, j : Int) : Int = {
    i + j
  }

  val addsTo3 : (Int) => Int = addTwoInts( 3, _ : Int )

  println( addsTo3(3)) //prints 6

  //function that returns a function:
  def getAnAddsToFunction( base : Int ) : Int => Int = {
    val retVal : Int => Int = ( i : Int ) =>{
      base + i
    }
    retVal
  }

  //NOTE: how we take the return value of the function, then use it as a function:
  val addsTo4 : Int => Int = getAnAddsToFunction(4)

  println( addsTo4(4)) // prints 8

  //more concise way of writing the above:
  def _getAnAddsToFunction( base : Int ) = (i : Int) => { i + base }

  val addsTo5 = _getAnAddsToFunction(5)
  println( addsTo5(5))  //prints 10

  //Partial functions, a function that is only defined for a subset of input values, e.g. limit to all non zero integers
  val divideInto42 = new PartialFunction[Int ,Int] {

    override def isDefinedAt(x: Int): Boolean = x != 0
    override def apply(v1: Int): Int = 42/v1
  }

  // divideInto42(0) throws exception, so you can use this to see if it is defined as a pre-check:
  println(divideInto42.isDefinedAt(0)) //gives false
  println( divideInto42.isDefinedAt(-22)) //gives true

  //other way of implementing a partial function, using case statements:

  def printsIfHelloOrGoodbye: PartialFunction[String, Unit] = {

    //NOTE: here how we use the cases to define the various ranges of values for which the function is defined
    case s : String if "hello".equals(s) => println(s)
    case s : String if "goodbye".equals(s) => println(s)
  }

  printsIfHelloOrGoodbye("hello") //prints hello
  // printsIfHelloOrGoodbye("not in value range")  //throws a match error as input string is not in the range of the function

  println( printsIfHelloOrGoodbye.isDefinedAt("hello")) //prints true
  println( printsIfHelloOrGoodbye.isDefinedAt("some other string")) //prints false

  //combining partial functions with orElse:
  def handlesEvenNumbers : PartialFunction[Int,Unit] = {
    case i : Int if i %2 ==0  => println(s"$i is an EVEN number")
  }

  def handlesOddNumbers : PartialFunction[Int,Unit] = {
    case i : Int if i %2 != 0  => println(s"$i is an ODD number")
  }

  //so if the input value isn't even, it is checked and then passed on to the handlesOddNumbers method...
  def handlesAllNumbers = handlesEvenNumbers orElse handlesOddNumbers

  handlesAllNumbers(3)

  //use of partial functions is illustrated when iterating over collections:

  def oddNumberString : PartialFunction[Int, String ] = {
    case i : Int  if i % 2 !=0 => s"$i is an odd number"
  }

  // val strList : List[String] = (1 to 20 ).map( oddNumberString ).toList .... this will give a match error,
  // as the map does not check the range of the partial function

  //this works ok, as collect DOES check for the partial function range:
  val strList : List[String] = (1 to 20 ).collect( oddNumberString ).toList
  println( strList )  //works ok, and prints the odd number strings

  //Compose example:
  def doubleInt( i : Int ) : Int = {
    i * 2
  }

  def add1ToInt( i : Int ) : Int = {
    i + 1
  }

  //this is like doublesInt(adds1ToInt(i)
  //NOTE that the in-params and return types of the chain must all add up for it to compile
  def add1ThenDouble = doubleInt _ compose add1ToInt _

  println( add1ThenDouble( 4 ))  // (4+1) * 2 ... prints 10

  def doubleThenAdd1 = add1ToInt _ compose doubleInt _

  println( doubleThenAdd1(3 )) // (3*2) + 1 prints 7

  //andThen example, and then is like compose, but in reverse order:
  def _add1ThenDouble = add1ToInt _ andThen doubleInt _
  println( _add1ThenDouble( 4 ))  // (4+1) * 2 ... prints 10

}
