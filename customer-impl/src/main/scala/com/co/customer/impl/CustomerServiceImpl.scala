package com.co.customer.impl

import akka.NotUsed
import com.co.customer.api.CustomerService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class CustomerServiceImpl extends CustomerService{
  override def getCustomer(custotmerId: Long): ServiceCall[NotUsed, String] = ServiceCall {_ =>
    Future("testing")
  }
}
