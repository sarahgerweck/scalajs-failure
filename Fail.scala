import language.implicitConversions
import language.reflectiveCalls

import scala.annotation.tailrec
import scala.collection.JavaConverters._

import java.util.concurrent.ConcurrentHashMap

// Parsing imports
import scala.util.parsing._
import combinator._
import lexical._

trait LexerUtil extends Scanners {
  // TBD: This might perform better as a macro
  final def chars(s: String, sensitive: Boolean = false): Parser[Vector[Char]] = {
    (s :\ success(Vector.empty[Char])) { (head, tailParser) =>
      @inline def headParser =
        if (sensitive) elem(head)
        else           insensitive(head)

      headParser ~ tailParser ^^ { case h ~ t => h +: t }
    }
  }

  def insensitive(c: Char) = elem(c.toString, { String.valueOf(_) equalsIgnoreCase String.valueOf(c) })

  final def str(s: String, sensitive: Boolean = false): Parser[String] = {
    chars(s, sensitive) ^^ { _.mkString }
  }

  final def longest[A <: { def chars: String }](constants: Iterable[A]): Parser[A] = {
    longest(constants, { a: A => a.chars })
  }

  final def longest[A <: { def chars: String }](constants: Iterable[A], sensitive: Boolean): Parser[A] = {
    longest(constants, { a: A => a.chars }, sensitive)
  }

  final def longest[A](constants: Iterable[A], chars: A => String, sensitive: Boolean = false): Parser[A] = {
    val sorted = constants.toIndexedSeq sortWith { chars(_).length > chars(_).length }
    implicit def toParser(a: A) = str(chars(a), sensitive) ^^^ a
    (toParser(sorted.head) /: sorted.tail) { _ | _ }
  }

  final def longestString[A](constants: Iterable[String], sensitive: Boolean = false): Parser[String] = {
    longest(constants, identity[String], sensitive)
  }
}

