package com.co.coapi.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.co.coapi.api.CoapiService
import com.co.customer.api.CustomerService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class CoapiLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CoapiApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CoapiApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CoapiService])
}

abstract class CoapiApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[CoapiService](wire[CoapiServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = CoapiSerializerRegistry

  //Bind the customer service client
  lazy val customerService = serviceClient.implement[CustomerService]

  // Register the CoAPI persistent entity
  persistentEntityRegistry.register(wire[CoapiEntity])
}
