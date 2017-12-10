package at.fhj.swengb.apps.calculator

import scala.util.Try

/**
  * Companion object for our reverse polish notation calculator.
  */
object RpnCalculator {

  /**
    * Returns empty RpnCalculator if string is empty, otherwise pushes all operations
    * on the stack of the empty RpnCalculator.
    *
    * @param s a string representing a calculation, for example '1 2 +'
    * @return
    */
  def apply(s : String): Try[RpnCalculator] = {
    if (s.isEmpty) {Try(RpnCalculator())}
    else try {
      s.split("\\s").map(x => Op(x)).toList.foldLeft(Try(RpnCalculator()))((acc, op) => acc.get.push(op))
    } catch {
      case e : Exception => Try[RpnCalculator] (throw e)
    }
  }
}
/**
  * Reverse Polish Notation Calculator.
  *
  * @param stack a data structure holding all operations
  */
case class RpnCalculator(stack: List[Op] = Nil) {

  /**
    * By pushing Op on the stack, the Op is potentially executed. If it is a Val, it the op instance is just put on the
    * stack, if not then the stack is examined and the correct operation is performed.
    *
    * @param op
    * @return
    */
  def push(op : Op): Try[RpnCalculator] = op match {
    case value : Val => Try(RpnCalculator(stack :+ value))
    case binop : BinOp => try {
      def examineStack(x : RpnCalculator) : Val = x.peek() match {
          case value: Val => value
          case _ => throw new NoSuchElementException
        }
      val nextStep = pop()._2
      val putOnStack : Val = binop.eval(examineStack(this), examineStack(nextStep))
      nextStep.pop()._2.push(putOnStack)
    }
      catch {
        case e : Exception => Try[RpnCalculator](throw e)
      }
    }

  /**
    * Pushes val's on the stack.
    *
    * If op is not a val, pop two numbers from the stack and apply the operation.
    *
    * @param op
    * @return
    */
  def push(op: Seq[Op]): Try[RpnCalculator] = {
    op.foldLeft(Try(RpnCalculator()))((acc, op) => acc.get.push(op))
  }

  /**
    * Returns an tuple of Op and a RevPolCal instance with the remainder of the stack.
    *
    * @return
    */
  def pop(): (Op, RpnCalculator) = {
    (stack.head, RpnCalculator(stack.tail))
  }


  /**
    * If stack is nonempty, returns the top of the stack. If it is empty, this function throws a NoSuchElementException.
    *
    * @return
    */
  def peek(): Op = {
    if (stack.nonEmpty) {stack.head}
    else {throw new NoSuchElementException}
  }

  /**
    * returns the size of the stack.
    *
    * @return
    */
  def size: Int = {
    stack.size
  }
}