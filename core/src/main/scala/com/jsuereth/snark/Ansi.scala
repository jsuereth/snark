package com.jsuereth.snark

import sbt.complete.Parser


// TODO - a nice ansii api.
object Ansi {

  def green(msg: String) = withAnsiCode(Console.GREEN, msg)
  def red(msg: String) = withAnsiCode(Console.RED, msg)
  def blue(msg: String) = withAnsiCode(Console.BLUE, msg)


  def author(msg: String) = withAnsiCode(s"${Console.BLUE}${Console.BOLD}", msg)


  def highlightTweet(msg: String): String = {
    msg.
      replaceAll("(@[^\\s]+)", s"${Console.GREEN}$$1${Console.RESET}").
      replaceAll("(#[^\\s]+)", s"${Console.CYAN}$$1${Console.RESET}")
  }


  def withAnsiCode(in: String, msg: String) =
    // TODO - disable ANSI if not i nappropriate terminal.
   s"$in$msg${Console.RESET}"




}
