

  package package1 {
    class PackageClass { override def toString = "package1 class"}
  }


  package package2 {
    class PackageClass { override def toString = "package2 class"}
  }

  object Ch7PackagingAndImports extends App {

    //NOTE see above, having multiple packages inside the same file:
    println( new package1.PackageClass )
    println( new package2.PackageClass )

    //Flexibility of import statements - can go anywhere, check out the notation for multiple imports:
    import java.util.{List, Map} //saves having a separate line for each import..
    import java.io._  //use _ to import a whole packages worth of stuff instead of * in java

    //To cause or solve(?) confusion, things can be renamed on import:
    import java.util.{ArrayList => JavaList }
    val javaList = new JavaList()
    println( javaList.getClass )  //prints ArrayList

    //val arrayList = new ArrayList() wont compile as 'ArrayList' has not strictly speaking been imported.

    //blocking a thing from being imported ( pretty pointless but you can do it)

    import java.lang.{Integer =>_, Float =>_ , _} //imports all apart from Integer and Float, as they are
    // mapped to _,so can't  be referenced

    //import statement scope, follows the curly braces rule:
    {
      import java.awt.AWTKeyStroke
      //val anAwtKeyStroke = new AWTKeyStroke()
    }

    //val anAwtKeyStroke = new AWTKeyStroke() not compile, as the import for it is in a different scope:

}
