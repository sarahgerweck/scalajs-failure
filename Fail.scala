import language.reflectiveCalls

object LexerUtil {
  def reflectiveLongest[A <: { def chars: String }](data: A): Unit = {
    val fn = { a: A => a.chars }
    ()
  }
}

