package com.jsuereth.snark

import java.util.Scanner
import collection.JavaConverters._


object Main {
  def main(args: Array[String]): Unit = {
    val twitter = TwitterApi()
    //ListMentions().run(twitter)
    //Tweet("Hello, boston #nescala2015").run(twitter)
    Search("#nescala2015").run(twitter)



  }
}
