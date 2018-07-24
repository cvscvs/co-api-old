package com.co.customer.impl

import com.co.customer.api.CustomerService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

class CustomerLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication = new CustomerApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
  }
}

abstract class CustomerApplication(context: LagomApplicationContext) extends LagomApplication(context)
with AhcWSComponents {
  override lazy val lagomServer = serverFor[CustomerService](wire[CustomerServiceImpl])

  // Bind the CustomerService client
  lazy val customerService = serviceClient.implement[CustomerService]

}
