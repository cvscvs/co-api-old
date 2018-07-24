package com.co.coapi.impl

import akka.NotUsed
import com.co.coapi.api
import com.co.coapi.api.CoapiService
import com.co.customer.api.CustomerService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

import scala.concurrent.Future

/**
  * Implementation of the CoapiService.
  */
class CoapiServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, customerService: CustomerService) extends CoapiService {

  override def hello(message: String): ServiceCall[NotUsed, String] = {request =>
    val ref = persistentEntityRegistry.refFor(message)
    ref.ask("hello")
  }

  override def getCustomer(id: String) = ServiceCall { customers =>
    //validate user authentication: valid user/password?
    //get that customer, read-side Jdbc
    val test = customerService.getCustomer(1)
    Future.successful(test.invoke())

  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the CoAPI entity for the given ID.
    val ref = persistentEntityRegistry.refFor[CoapiEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(CoapiEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[CoapiEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
