package com.jsuereth.snark

import java.awt.Desktop

/**
 * Helper to open your browser.
 */
object Browser {

  private val desktop: Option[Desktop] =
    if (Desktop.isDesktopSupported)
      Some(Desktop.getDesktop) filter (_ isSupported Desktop.Action.BROWSE)
    else None


  def openUrl(url: String): Unit =
    desktop match {
      case Some(desktop) =>
        System.out.println(s"We are opening your system web browser to: $url")
        desktop.browse(new java.net.URI(url))
      case None =>
        System.out.println(s"We are unable to open your browser automatically.  Please navigate to: $url")
    }

}
