package com.jsuereth.snark

import java.util.Scanner
import sbt.complete.Parser
import twitter4j.Twitter

import collection.JavaConverters._


object Main {
  def main(args: Array[String]): Unit = {
    // Grabs the API, this will prompt user for credentials if needed.
    val twitter = TwitterApi()
    parseLine(args.mkString(" "), TwitterCommand.parser) match {
      case Some(cmd) => cmd.run(twitter)
      case None => runCli(twitter)
    }
  }

  def runCli(twitter: Twitter): Unit = {
    def readCommand(): Option[TwitterCommand] = readLine(TwitterCommand.parser)
    def loop(): Unit = readCommand() match {
      case None => () // Exit
      case Some(Exit) => () // Exit
      case Some(cmd) =>
        cmd.run(twitter)
        loop()
    }
    loop()
  }


  def readCommand(): Option[TwitterCommand] = {
    readLine(TwitterCommand.parser)
  }

  /** Uses SBT complete library to read user input with a given auto-completing parser. */
  private def readLine[U](parser: Parser[U], prompt: String = "> ", mask: Option[Char] = None): Option[U] = {
    val reader = new sbt.FullReader(None, parser)
    reader.readLine(prompt, mask) flatMap { line =>
      parseLine(line, parser)
    }
  }
  // Parses a line of input
  private def parseLine[U](line: String, parser: Parser[U]): Option[U] = {
    val parsed = Parser.parse(line, parser)
    parsed match {
      case Right(value) => Some(value)
      case Left(e) => None
    }
  }
}
