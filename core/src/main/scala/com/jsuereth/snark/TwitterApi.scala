package com.jsuereth.snark

import java.io.File
import java.util.Properties

import twitter4j.{Twitter, TwitterFactory}
import twitter4j.conf.ConfigurationBuilder


object TwitterApi {
  private val credsFile = new File(new File(sys.props("user.home")), ".snark-twitter-creds")

  private def loadCredentialsFile(): TwitterOauthCredentials = {
    val props = new Properties()
    sbt.IO.load(props, credsFile)
    val result = TwitterOauthCredentials(
       props.getProperty("oauth.consumerKey", null),
       props.getProperty("oauth.consumerSecret", null),
       props.getProperty("oauth.accessToken", null),
       props.getProperty("oauth.accessSecret", null)
    )
    if(result.accessSecret == null || result.accessToken == null || result.consumerKey == null || result.consumerSecret == null) {
      throw new RuntimeException("stored twitter credentials are invalid.")
    }
    result
  }

  private def writeCredentialsFile(credentials: TwitterOauthCredentials) = {
    import credentials._
    sbt.IO.write(credsFile, s"""|oauth.consumerKey=$consumerKey
        |oauth.consumerSecret=$consumerSecret
        |oauth.accessToken=$accessToken
        |oauth.accessSecret=$accessSecret""".stripMargin)
  }

  def loadCredentials(): TwitterOauthCredentials = {
    try loadCredentialsFile()
    catch {
      case e: Exception =>
        val newCreds = TwitterOauth.getCredentialsViaConsole()
        writeCredentialsFile(newCreds)
        newCreds
    }
  }


  def apply(): Twitter = {
    val creds = loadCredentials()
    val cb = new ConfigurationBuilder
    cb.
      setDebugEnabled(true).
      setOAuthConsumerKey(creds.consumerKey).
      setOAuthConsumerSecret(creds.consumerSecret).
      setOAuthAccessToken(creds.accessToken).
      setOAuthAccessTokenSecret(creds.accessSecret)
    val factory = new TwitterFactory(cb.build)
    factory.getInstance()
  }

}
