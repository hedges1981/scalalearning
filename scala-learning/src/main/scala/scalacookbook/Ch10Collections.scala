package scalacookbook

import scala.collection.immutable.ListMap
import scala.collection.{LinearSeq, SortedMap, mutable}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by christine on 12/02/2017.
  */
object Ch10Collections extends App {

  //filter a list using a predicate:
  val _1to100 : List[Int] = 1 to 100 toList
  //filter out only even numbers:
  val evenNos = _1to100.filter( _ % 2 == 0 )
  println(evenNos)

  //Difference between IndexedSeq and LinearSeq:
  //default IndexedSeq is a immutable vector:
  //****USE indexed seq when you want quick look up of elements by index:
  val indexedSeq : IndexedSeq[Int] = IndexedSeq( 1,2,3,4,5,6,7,8)
  println( indexedSeq.getClass() )  //prints showing it is of class vector

  //USE linerSeq when you want efficient chopping into head and tail elements:
  //default linearSeq impl is immutable list:
  val linearSeq = LinearSeq( 1,2,3,4)
  println( linearSeq.getClass() )
  val first2 = linearSeq.slice(0,2) //e.g. can slice up the list

  //defaults are always the immutable ones, to get a mutable, need to specify it:
  val mutableSet = collection.mutable.Set(1,2,3)

  //NOTE: no such thing as a mutable list, use ListBuffer:
  val listBuffer = ListBuffer(1,2,3)
  listBuffer(0)=999  //couldn't do this if it was immutable
  println( listBuffer)

  //AND for the vector equivalent, use ArrayBuffer:
  val arrayBuffer = ArrayBuffer( 1,2,3)
  arrayBuffer(2) = 4 //couldn't do this if it was immutable

  //MAPs, different implementations get iterated over in different orders:
  //LinkedHashMap .... order in which they were inserted:
  val linkedHashMap = mutable.LinkedHashMap( 1->"1", 2 ->"2")
  linkedHashMap.foreach( println )  // 1,2
  //ListMap .... also does it in inserton order:
  val listMap = ListMap(1->"1",2->"2")
  listMap.foreach( println )  //prints a before z

  //Sorted Maps, do the thing in key order, like a tree map:
  val sortedMap = SortedMap( "z"->"Z", "a"->"A")
  sortedMap.foreach( println )
  println( sortedMap.getClass() ) //default SortedMap impl is a tree set:

  //PERFORMANCE OF COLLECTIONS:
  //see book page 262 for nice table regarding the various operations you do on collections, very useful for deciding
  //which one you want for a specific usage case.

  //Looping over a collection with a counter... use zipWithIndex...
  for( (elt, count) <- evenNos.zipWithIndex ){
    //note how we get the counter as the count variable:
    println(s"element number is: $count and element is $elt" )
  }

  //For / yield construct:
  var evenOddList = for( elt <- _1to100 ) yield{
    if(elt % 2 == 0 ) "evenNumber" else "oddNumber"
  }
  println( evenOddList )

  //Map can be used to do same thing:
  evenOddList = _1to100.map{ elt => if ( elt % 2 == 0 ) "evenNumber" else "oddNumber" }
  println( evenOddList )

  //Iterators, not recommended in scala, work like java, only go one way, once exhausted, cannot be re-used:
  val iterator : Iterator[Int] = Iterator(1,2,3)
  println( iterator.hasNext ) //true
  iterator.foreach( println ) //exhausts the iterator
  println( iterator.hasNext ) //false

  //Flatten a list of lists:
  val list1 = List(1,2)
  val list2 = List(3,4)
  val list3 = List(5,6)
  val listOfLists = List( list1, list2, list3 )

  val bigList = listOfLists.flatten
  println( bigList )  // 1,2,3,4,5,6

  //NOTE: flatten can also be used to process a load of Some / None values:
  val dodgyList = List( None, Some(1), None, Some(2), Some(3))
  println( dodgyList.flatten ) //1,2,3


  //Flat map..... think of it as being like map, then flatten, e.g. this example:
  val bag = List("aaa","1","2","3","ffffff","4","5","6","7")
  //we want to convert bag into a list of actual integers, as best we can:
  def toInt( s : String ) : Option[Int] = {
    try {
      Some( Integer.parseInt(s))
    }
    catch {
      case e : Exception => None
    }
  }

  println( bag.map( toInt ).flatten ) //  1,2,3,4,5,6,7
  println( bag.flatMap(toInt) )  //same as above, as flatMap means map, then flatten:

  //Filtering uses a predicate, but can also be done using case statement , e.g to get just the strings out of this list:
  val mixedList = List( 1,"asasas",2 , "wwwww")
  val listOfStrings = mixedList.filter {
    case s : String => true
    case _  => false
  }
  println( listOfStrings )

  //you can filter as many times as you want:
  println(  _1to100.filter( _ % 2 == 0 ).filter( _ < 50 ) ) //even numbers under 50

  //Use of drop, to drop first x elements of a thing:
  val _6to100 = _1to100.drop(5)
  println( _6to100 )

  //drop right drops from the other end:
  val _1to95 = _1to100.dropRight(5 )
  println( _1to95 )

  //drop while, keeps dropping unto a condition returns false:
  println( _1to100.dropWhile( Math.pow( _, 2) < 200 ) )  //drops all elements whose square < 200:

  //take method, opposite of drop:
  val _1to10 = _1to100.take( 10 )
  println( _1to10 )
  val _96to100 = _1to100.takeRight( 5 )
  println( _96to100 )

  //Use slice, to take a bit of a list:
  val _11to20 = _1to100.slice( 10,20 )
  println( _11to20 )


  // Splitting up sequences:

  //split at the nth element:
  val splitAt20 : (List[Int], List[Int]) = _1to100.splitAt(20)
  println( splitAt20 )

  //partition lets you split them by a function, e.g. split into odd and even numbers:
  val oddsAndEvens : (List[Int], List[Int]) = _1to100.partition( _ % 2 ==0 )
  //in the above case, the list that matches the partition function is the first elt in the tuple:
  println(oddsAndEvens)

  //Slide.... allows you to take a window along the list, can define size and step:
  val slidedWindows : List[List[Int]] = _1to10.sliding( 3,1 ).toList
  //here 3 is the size, step is 1, so is like (1,2,3), (2,3,4)
  println( slidedWindows )

  //use unzip to create a two 'split' lists from a list of tuples:
  val listOfTuples : List[(Int,String)] = List( (1,"one"), (2,"two"), (3,"three"))
  val intsAndStrings : (List[Int],List[String]) = listOfTuples.unzip
  println( intsAndStrings )

  //Walking along a sequence and doing stuff:
  //use reduce left to go from the start of a list and do some stuff:
  //e.g. sum up a list:
  val sum1To10 : Int = _1to10.reduceLeft( _ + _ )
  println( sum1To10 )

  //it basically takes the result of each move and uses it in the next one, e.g. to find the max of a list:
  val maxOfList = _1to10.reduceLeft(
    (retValOfLastCall:Int, nextValueAlongList : Int ) => {
        println( s"x is $retValOfLastCall and y is $nextValueAlongList")
        retValOfLastCall max nextValueAlongList
    }
  )

  //see above for how it works, basically the result of the previous execution and the next thing in the list
  //are passed into the 'reduction function' each time.

  //reduce right just goes the other way:
  println("doing reduce right")
  _1to10.reduceRight(
    (retValOfLastCall:Int, nextValueAlongList : Int ) => {
      println(s" nextValueAlongList is $nextValueAlongList")
      nextValueAlongList
    }
  )


  //fold left and fold right are the same as reduce, but they let you specify an initial value,
  //e.g. this gives the sum of 1 to 10, with 20 added on at the start:
  val sumPlus20 : Int = _1to10.foldLeft(20)( _ + _ )
  println( sumPlus20 )

  //*** DONT understand scanLeft ****

//  //scan left / right allow you to record the cumulative results of the operation,
//  //e.g. this keeps all the interim values of the summing, that last one is the final sum:
//  val cumulativeSum : List[Int] = _1to10.scanLeft( (x:Int, y:  Int ) => x + y )
//  println( cumulativeSum )

  //use of distinct to get list into unique elements
  val _1122 = Vector( 1,1,2,2 )
  println( _1122.distinct )  //only contains 1 and 2
  //can also be done same like java by converting to a set:
  println( _1122.toSet )

  //need to imlement equals and hash code for distinct to work, same as in java :

  class EqualsHashCodeDemo( val x : Int ) {

    override def equals( that : Any): Boolean = {
      that.isInstanceOf[EqualsHashCodeDemo] && this.x == that.asInstanceOf[EqualsHashCodeDemo].x
    }

    override def hashCode : Int = {
      x
    }
  }

  //Merging sequential collections:
  // use ++ to merge collections:
  val _1to20 = _1to10 ++ _11to20
  //duplicate elements are kept with ++
  println( Vector(1) ++ Vector(1))   //contains 2 ones

  //Intersect to get elements in both collection:
  println( Vector(1,2) intersect Vector(2,3)) //Vector(2)

  //dif method used to find the elements not in one of the collections:
  println( Vector(1,2,3) diff Vector(1,2,4)) //gives 3, elements in A that are not in b

  // can also use -- instead of diff:
  println( Set(1,2,3) -- Set(1,2,4))   //again gives 3.

  //Unzip method reverse of ZIP:
  val aMap = Map ( 1->"one", 2->"two ")
  val unZipped = aMap.unzip
  println( unZipped )  //is a tuple of two lists.


  //Use of views...
  //allow for transform methods to be carried out lazily:
  println("doing map on not view")
  val notView = _1to10.map {
    i =>
      println("doing map on notVIew ") //this will get printed out
      i * 2
  }


  val withView = _1to10.view.map {
    i =>
      println("doing map on viewVIew ") //this not get printed out here
      i * 2
  }

  println("now executing map on view")
  withView.foreach( i => {} )//this will finally cause the doing map to get printed...
  //basically you can think of a view as a proxy which lazily implements all transformers.

   //Use of ranges:
  val intRange : Range = 1 to 10
  println( intRange )

  //can also be letters and also converted into a different collection type:
  val letterRange  = ('a' to 'z').toList
  println( letterRange )

  //ranges can also miss out elements, e.g.
  val everyOtherLetter = ('a' to 'z').by(2)
  println( everyOtherLetter )

  //Enums
  object AnEnum extends Enumeration {
    type AnEnum = Value
    val Enum1, Enum2 , Enum3 = Value
  }

  //Tuples, use for collections of diferent things:
  val intStringTuple = (1, "one")
  //get to the elements like:
  println( intStringTuple._1 )
  println( intStringTuple._1 )

  //use wild cards to just pull out things from a tuple that you want into individual variables, like this:
  val _3Strings = ( "one", "two", "three ")
  val ( _1, _2, _ ) = _3Strings
  println( _1 ) //prints "one"
  println( _2 ) //prints "two"

  //tuples can go up to 22 elements, e.g.:
  val _bigTuple  = (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 )
  println( _bigTuple._22 )

  //Sorting collections:
  //Numeric and String types have default sorting built in:
  val intList = List(1,3,7,6,4,2,7,1 )
  println( intList.sorted )

  val stringList = List("z","a","m")
  println( stringList.sorted )

  //use sort with to sort with a custom predicate:
  val differentLengthStrings = List("a","aaa","aaaaaa","a")
  //e.g. to sort them by string length:
  val sortedByStringLength = differentLengthStrings.sortWith{
    (a,b) => a.length < b.length
  }
  println( sortedByStringLength )

  //Use of ordered trait:  For classes that don't have implicit ordering can do this:

  class SortedClass( val x : Int ) extends Ordered[SortedClass ]{

    //set it so it sorts in reverse order of x:
    override def compare(that: SortedClass): Int = {
      //logic like l
      var retVal : Int = 0
      if( that.x == this.x ) retVal = 0
      else if( that.x > this.x ) retVal = 1
      else if( that.x < this.x ) retVal = -1

      retVal
    }


    override def toString = s"SortedClass $x"
  }

  val toBeSorted = List( new SortedClass(1), new SortedClass(2), new SortedClass(3))
  println( toBeSorted.sorted )

  //use of makeString...
  println( _1to10.mkString )  // prints: 12345678910
  //NOTE: how you can do it with a separator:
  println( _1to10.mkString(":")) //prints : 1:2:3:4:5:6:7:8:9:10






















}
