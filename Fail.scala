import language.reflectiveCalls

import scala.util.parsing._
import combinator._
import lexical._

trait LexerUtil extends Scanners {
  def reflectiveLongest[A <: { def chars: String }](constants: Iterable[A]): Parser[A] = {
    longest(constants, { a: A => a.chars })
  }

  def longest[A](constants: Iterable[A], chars: A => String): Parser[A]
}

