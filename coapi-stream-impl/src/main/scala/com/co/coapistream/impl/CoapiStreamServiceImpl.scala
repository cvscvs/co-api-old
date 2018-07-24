package com.co.coapistream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.co.coapistream.api.CoapiStreamService
import com.co.coapi.api.CoapiService

import scala.concurrent.Future

/**
  * Implementation of the CoapiStreamService.
  */
class CoapiStreamServiceImpl(coapiService: CoapiService) extends CoapiStreamService {
  def stream = ServiceCall { hellos =>

    Future.successful(hellos.mapAsync(8)(coapiService.hello(_).invoke()))
  }
}
