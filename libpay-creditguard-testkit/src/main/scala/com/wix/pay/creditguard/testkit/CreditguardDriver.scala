package com.wix.pay.creditguard.testkit

import java.util.{List => JList}

import com.google.api.client.http.UrlEncodedParser
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.pay.creditguard.model._
import com.wix.pay.creditguard.{RequestParser, ResponseParser}
import spray.http._

import scala.collection.JavaConversions._
import scala.collection.mutable


class CreditguardDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)

  def startProbe() {
    probe.doStart()
  }

  def stopProbe() {
    probe.doStop()
  }

  def resetProbe() {
    probe.handlers.clear()
  }

  def aRequestFor(user: String, password: String, request: AshraitRequest): RequestCtx = {
    new RequestCtx(
      user = user,
      password = password,
      request = request
    )
  }

  class RequestCtx(user: String, password: String, request: AshraitRequest) {
    def returns(response: AshraitResponse): Unit = {
      val responseXml = ResponseParser.stringify(response)

      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/"),
        _,
        entity,
        _) if isStubbedRequestEntity(entity) =>
          HttpResponse(
            status = StatusCodes.OK,
            entity = HttpEntity(ContentType(MediaTypes.`application/xml`, HttpCharsets.`UTF-8`), responseXml))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestParams = urlDecode(entity.asString)

      requestParams.get(Fields.user).contains(user) &&
        requestParams.get(Fields.password).contains(password) &&
        requestParams.get(Fields.int_in).map { RequestParser.parse }.contains(request)
    }

    private def urlDecode(str: String): Map[String, String] = {
      val params = mutable.LinkedHashMap[String, JList[String]]()
      UrlEncodedParser.parse(str, mutableMapAsJavaMap(params))
      params.mapValues( _.head ).toMap
    }
  }
}
