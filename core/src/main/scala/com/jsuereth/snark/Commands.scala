package com.jsuereth.snark

import twitter4j.{Query, Twitter}
import collection.JavaConverters._

sealed trait TwitterCommand {
  def run(client: Twitter): Unit
}

object TwitterCommand {
  import sbt.complete._
  import DefaultParsers._

  private val restOfLineParser = any.* map (_.mkString(""))

  val parser: Parser[TwitterCommand] = {
    val restOfLineParser = any.* map (_.mkString(""))
    val exit = token("exit" ^^^ Exit)
    val listMentions = token("mentions" ^^^ ListMentions)
    val tweet = {
      ((token("tweet") ~ Space) ~> restOfLineParser) map {
        text => Tweet(text)
      }
    }
    val search = {
      ((token("search") ~ Space) ~> restOfLineParser) map {
        text => Search(text)
      }
    }
    exit | listMentions| tweet | search
  }


}

object Exit extends TwitterCommand {
  override def run(client: Twitter): Unit = ()
}

object ListMentions extends TwitterCommand {
  override def run(client: Twitter): Unit = {
    val mentions = client.timelines().getMentionsTimeline()
    System.out.println(s"-- MENTIONS --")
    for(mention <- mentions.asScala) {
      System.out.println(s"${Ansi.author(mention.getUser.getScreenName)}: ${Ansi.highlightTweet(mention.getText)}")
    }
  }
}

case class Tweet(content: String) extends TwitterCommand {
  override def run(client: Twitter): Unit = {
    client.updateStatus(content)
    System.out.println(s"Tweeted: ${Ansi.highlightTweet(content)}")
  }
}

case class Search(content: String) extends TwitterCommand {
  override def run(client: Twitter): Unit = {
    System.out.println(s" -- SEARCH $content -- ")
    for(tweet <- client.search(new Query(content)).getTweets.asScala) {
      System.out.println(s"${Ansi.author(tweet.getUser.getScreenName)}: ${Ansi.highlightTweet(tweet.getText)}")
    }

  }
}