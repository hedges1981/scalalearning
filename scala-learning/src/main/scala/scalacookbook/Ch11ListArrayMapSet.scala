package scalacookbook

import java.util

import scala.collection.immutable.ListMap
import scala.collection.{SortedMap, SortedSet, mutable}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Sorting

/**
  * Created by christine on 27/02/2017.
  */
object Ch11ListArrayMapSet extends App{

  //Lists:
  // how to make a list of all the same thing:
  val allSameList = List.fill(4)("thing")
  println( allSameList ) //4 of the same thing

  //handy :: operator to add stuff to head / tail of existing list:
  //Note: when appending at the end , last thing must be Nil element
  val nowDifferent = "differentThing" :: allSameList :: "moreDifferent" :: Nil
  println( nowDifferent )

  //reached page 323

  //Mutable list, use a list buffer:
  val mutableList : ListBuffer[Int] = ListBuffer(1,2,3,4)
  mutableList += 5
  mutableList.update(0,100) //set first elt to 100
  println(mutableList)

  //to add stuff to immmutable list end up with new list:
  val immutableList = List(1,2,3)
  val newImmutableList = immutableList.updated(1,100)
  println( newImmutableList )

  //deleting elements from a immutable list very painful as no remove
  val _1to5 = 1 to 5 toList
  //remove a given element:
  val _1to4 = _1to5.filter( _ != 5 )
  println(_1to4)

  //from mutable list is easier:
  val sss = ListBuffer(1,2,3)
  sss.remove(0)
  println(sss) //removes the first element
  sss -= 3 //removes the 3
  println(sss)

  //use of --= to delete mutiple elements:
  val mutable2 = ListBuffer(1,2,3,4,5,6)
  mutable2 --= List(1,2,3)
  println( mutable2 )

  //STREAMS

  //build a stream using the #:: operator:
  val aStream = 1 #:: 2 #:: 3 #:: Stream.empty
  println( aStream )   // note how this prints 1,? as only the first element of the stream is considered to have been
  //evaluated.

  //Arrays.... are immutable in terms of size, but are mutable in that the elements themselves can change:
  val anArray : Array[Int] = new Array(3)
  anArray(1)= 1
  anArray(1)= 2 //ok to do
  println( anArray(1) )

  //Array buffer... is the mutable version of a Vector:
  val arrayBuffer : ArrayBuffer[Int] = ArrayBuffer(1,2,3,4,5,6,7,8,9,10)
  println( arrayBuffer )
  //remove an element by position:
  arrayBuffer.remove(8)
  println( arrayBuffer )
  //remove a range of positions:
  arrayBuffer.remove(0,2) //remove 2 elements, starting at 0
  println( arrayBuffer )
  //remove explicit elements:
  arrayBuffer -= 4
  println(arrayBuffer)
  //remove more than one explicit elements:
  arrayBuffer -= (5,6)
  println( arrayBuffer )
  //use clear to remove all elements:
  arrayBuffer.clear
  println(arrayBuffer)
  //sorting an array:
  //can use quickSort if the elements ( such as String ) have implicit ordering:
  val strArray = Array("Z","X","Y","B","A")
  Sorting.quickSort( strArray )
  println( strArray(0) )

  //Creating multi-dimensional arrays:
  //Use array ofDim
  val _2dArray = Array.ofDim[Int](2,2)
  _2dArray(0)(1)= 2  //etc etc
  println(_2dArray(0)(1))

  //USE of maps, Deciding what Map implementation to use:
  //USE sorted map for automatic ordering on the keys:
  val sortedMap = SortedMap("Z"->"z","Y"->"y","A"->"A")
  println( sortedMap )  //note how it comes out in sorted order:

  //USE linked hash map to maintain the original insertion order:
  val linkedHashMap = mutable.LinkedHashMap("Z"->"z","Y"->"y","A"->"A")
  println( linkedHashMap )  //insertion order maintained

  //USE List map, for it to reverse the insertion order, bit like a stack:
  val listMap = new mutable.ListMap[String,String]
  listMap.put("Z","z")
  listMap.put("A","a")
  listMap.keySet.foreach(println)  //prints A,Z

  //Can also use the default Map:
  val defaultMap = Map(1->1,2->2)  //is a immutable.Map

  //Ways to add / remove from a map:
  var mutableIntMap = mutable.LinkedHashMap(1->1)
  //add with put:
  mutableIntMap.put(2,2)
  //add with +=, add multiple things if you want:
  mutableIntMap += (3->3,4->4,5->5,6->6,7->7,8->8)
  //remove a single element by key:
  mutableIntMap -= 6
  //remove >1 elements by a list:
  mutableIntMap --= List(4,5)
  println( mutableIntMap )

  //use retain with a predicate to keep e.g. elements with a key less than 3:
  mutableIntMap.retain{ (k,v) => k <= 3}
  println( mutableIntMap )

  //ACCESSING map elements:  easy way:
  println( mutableIntMap(1)) //BUT be careful as this throws a noSuchElement exception:

  try{
    mutableIntMap(123)
  } catch {
    case e => println(e)
  }

  //stop by e.g.:
  val defaultValueMap = Map(1->2).withDefaultValue(0)
  println(defaultValueMap(123))

  //or use get which returns an option:
  //where it is there:
  println(mutableIntMap.get(1).get) //use get on the Some option to actually get at it:
  //gives an option:None
  println(mutableIntMap.get(123))

  //transforming the values of a map, using mapValues:
  val doubledMap = mutableIntMap.mapValues( _*2 )
  println( doubledMap )

  //or use transform when you need to use the key in the thing, e.g. to multiply keys by the values:
  var keysTimesValuesMap = mutableIntMap.transform{ (k,v)=> k*v}
  println( keysTimesValuesMap )

  //to find the maximum key and value of the map:
  println( keysTimesValuesMap.keysIterator.max )
  println( keysTimesValuesMap.valuesIterator.max )

  //Sets: can basically operate on them like you can with the other collections
  //use to set to get unique elements:
  val aSet = List(1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,3).toSet
  println( aSet )
  //use sorted set to get one sorted naturally:
  //NOTE; unfortunately it is only available in immutable form, apparently u need to use java.util.TreeSet for a
  //mutable sorted set:
  val sortedSet = SortedSet(3,2,1)
  println( sortedSet )

  //QUEUES
  //set one up:
  var queue : mutable.Queue[Int] = new mutable.Queue[Int]
  //put stuff on the queue:
  queue += 1
  //for adding multiples, use ++=
  queue ++= List(2,2,3,4,5,6,7,8,9)

  //queue is o course FIFO, so this prints 1:
  println( queue.dequeue )

  //can use a predicate to get the first one that matches a criteria, e.g. first one > 4
  //note that it returns an option, so use get to actually get it:
  println( queue.dequeueFirst(_ > 4 ).get)

  //Using a stack:
  var stack : mutable.Stack[Int] = new mutable.Stack[Int]
  //push stuff onto it:
  stack.push(1,2,3,4,5)
  //top is the equivalent of peek, gets you the top thing without actually removing it from the stack:
  println( stack.top ) // prints 5
  //pop actually removes, it:
  stack.pop
  println( stack.top ) // now prints 4

  //Ranges are straight forward:
  val aRange = 1 to 10 // is 1 to 10 inclusive
  val anotherRange = 1 until 10 // just 1 to 9, note the 'until'
  println(anotherRange)








}




