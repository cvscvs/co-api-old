package com.co.customer.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

object CustomerService

/**
  * Customer API Interface
  */
trait CustomerService extends Service {

  def getCustomer(custotmerId: Long):ServiceCall[NotUsed, String]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("customer")
      .withCalls(
        pathCall("/api/v1/customers/:id", getCustomer _)

      ).withAutoAcl(true)

  }


}
