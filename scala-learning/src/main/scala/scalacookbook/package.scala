/**
  * Created by christine on 30/01/2017.
  */
//NOTE: that the name of this object is the same as the package.
package object scalacookbook {

  //note, implicit classes cannot live standalone, but must go inside an object, this package object does the job
  implicit class StringImprovements( val s : String ){

    def dongify = s + " dong!"

  }

  def packageObjectMethod = {
    println("Package Object Method!!!")
  }

}
