package com.co.coapistream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.co.coapistream.api.CoapiStreamService
import com.co.coapi.api.CoapiService
import com.softwaremill.macwire._

class CoapiStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CoapiStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CoapiStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CoapiStreamService])
}

abstract class CoapiStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[CoapiStreamService](wire[CoapiStreamServiceImpl])

  // Bind the CoapiService client
  lazy val coapiService = serviceClient.implement[CoapiService]
}
