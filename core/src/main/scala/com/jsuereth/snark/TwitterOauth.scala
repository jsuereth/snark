package com.jsuereth.snark

import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import org.scribe.model.Verifier
import sbt.SimpleReader


case class TwitterOauthCredentials(
  consumerKey: String,
  consumerSecret: String,
  accessToken: String,
  accessSecret: String
) {
  override def toString =
    s"""|oauth.consumerKey=$consumerKey
        |oauth.consumerSecret=$consumerSecret
        |oauth.accessToken=$accessToken
        |oauth.accessSecret=$accessSecret""".stripMargin
}
/**
 * Helper to grab OAuth token
 */
object TwitterOauth {
  // Note - EvilSecrets contains our API key and is not committed to github.
  import EvilSecrets._
  val service = {
    (new ServiceBuilder()).provider(classOf[TwitterApi])
      .apiKey(apiKey)
      .apiSecret(apiSecret).build()
  }

  /** uses the console to grab a valid access token for a twitter account. */
  def getCredentialsViaConsole(): TwitterOauthCredentials = {
    val requestToken = service.getRequestToken
    val url = service.getAuthorizationUrl(requestToken)
    System.out.println(s"Opening $url, please authorize and enter obtain twitter PIN.")
    Browser.openUrl(url)
    val pin = SimpleReader.readLine("Enter your twitter PIN (or credit card number): ", Some('*'))
    // TODO - ask for pin until we get a legit one.

    val accessToken = service.getAccessToken(requestToken, new Verifier(pin.getOrElse {
      throw new Exception("no pin entered, killing application.")
    }))
    TwitterOauthCredentials(apiKey, apiSecret, accessToken.getToken, accessToken.getSecret)
  }

}
