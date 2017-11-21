package at.fhj.swengb.assignments.functional

/**
  * In this assignment you have the chance to demonstrate basic understanding of
  * functions like map/filter/foldleft a.s.o.
  **/
object FunctionalAssignment {

  /**
    * A function which returns its parameters in a changed order. Look at the type signature.
    */
  def flip[A, B](t: (A, B)): (B, A) = {
    (t._2,t._1)
  }

  /**
    * given a Seq[A] and a function f : A => B, return a Seq[B]
    */
  def unknown[A, B](as: Seq[A], fn: A => B): Seq[B] = {
    for (a <- as) yield fn(a)
  }

  /**
    * Returns the absolute value of the parameter i.
    *
    * @param i a value, either with a positive or a negative sign.
    * @return
    */
  def abs(i: Int): Int = {
    if (i > 0)
      i
    else {i * (-1)}
  }


  // Describe with your own words what this function does.
  // in the comment below, add a description what this function does - in your own words - and give
  // the parameters more descriptive names.
  //
  // Afterwards, compare your new naming scheme with the original one.
  // What did you gain with your new names? What did you loose?
  //
  /**
    *
  //  * @param as -> the parameter as is a Sequence that contains certain values, in this case integers
  // * @param b -> the parameter b is the so-called accumulator, which accumulates the values from as and applies the function to them
  //  * @param fn -> fn is the function that is applied to as, in this case foldLeft
  //  * @tparam A -> the first parameter given to op
  //  * @tparam B -> the second parameter given to op
  //  * @return -> returns the result
    *
    * Renaming:
    * as -> list
    * b -> acc
    * fn -> func
    * A -> param1
    * B -> param2
    *
    * By renaming the parameters it becomes more obvious what each and every parameter is actually doing.
    * However, a disadvantage is that the function op is now about twice as long as it was before, making it more clumsy to write down
    */
  def op[param1, param2](list: Seq[param1], acc: param2)(func: (param2, param1) => param2): param2 = list.foldLeft(acc)(func)

  /**
    * implement the summation of the given numbers parameter.
    * Use the function 'op' defined above.
    *
    * @param numbers
    * @return
    */
  def sum(numbers: Seq[Int]): Int = {
    op(numbers,0)(_+_)
  }


  /**
    * calculate the factorial number of the given parameter.
    *
    * for example, if we pass '5' as parameter, the function should do the following:
    *
    * 5! = 5 * 4 * 3 * 2 * 1
    *
    * @param i parameter for which the factorial must be calculated
    * @return i!
    */
  def fact(i: Int): Int = {
    if (i > 0) {i * fact(i-1)}
    else 1
  }

  /**
    * compute the n'th fibonacci number
    *
    * hint: use a internal loop function which should be written in a way that it is tail
    * recursive.
    *
    * https://en.wikipedia.org/wiki/Fibonacci_number
    */
  def fib(n: Int): Int = {
    def fibRec(n: Int, a: Int, b: Int) : Int = {n match {
      case 0 =>  b
      case _ => fibRec(n-1, a+b, a)
    }}
    fibRec(n, 1, 0)
  }

  /**
    * Implement a isSorted which checks whether an Array[A] is sorted according to a
    * given comparison function.
    *
    * Implementation hint: you always have to compare two consecutive elements of the array.
    * Elements which are equal are considered to be ordered.
    */
  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {
    def sortRec(n: Int): Boolean =
      if ((n <= as.length - 1)) true
      else if (gt(as(n), as(n + 1))) false
      else sortRec(n + 1)
    sortRec(0)
  }

  /**
    * Takes both lists and combines them, element per element.
    *
    * If one sequence is shorter than the other one, the function stops at the last element
    * of the shorter sequence.
    */
  def genPairs[A, B](as: Seq[A], bs: Seq[B]): Seq[(A, B)] = {
    for (a <- as; b <- bs if as.indexOf(a) == bs.indexOf(b)) yield (a,b)
  }

  // a simple definition of a linked list, we define our own list data structure
  sealed trait MyList[+A]

  case object MyNil extends MyList[Nothing]

  case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

  // the companion object contains handy methods for our data structure.
  // it also provides a convenience constructor in order to instantiate a MyList without hassle
  object MyList {

    def sum(list: MyList[Int]): Int = list match {
      case MyNil => 0
      case Cons(h,t) => h + sum(t)
    }

    def product(list: MyList[Int]): Int = list match {
      case MyNil => 1
      case Cons(0,_) => 0
      case Cons(h,t) => h * product(t)
    }

    def apply[A](as: A*): MyList[A] = {
      if (as.isEmpty) MyNil
      else Cons(as.head, apply(as.tail: _*))
    }

  }

}

